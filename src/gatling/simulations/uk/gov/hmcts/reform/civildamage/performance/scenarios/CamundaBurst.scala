package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, Environment, S2S}

import java.io.{BufferedWriter, FileWriter}
import scala.concurrent.duration._
import scala.util.Random

/*======================================================================================
* DTSCCI-6615 - Camunda burst test
*
* PREP  (TEST_TYPE=burstprep): create a LiP claim through the API (CREATE_LIP_CLAIM),
*       create the claim fee payment, pay by card on gov.uk Pay, but STOP before the
*       redirect back to CUI. Writes caseRef,paymentReference,email to burst_cases.csv.
*
* BURST (TEST_TYPE=burst): get a token per claimant, wait for a random moment inside
*       the 2-minute window, then call the payment status endpoint. A "Success"
*       makes civil-service submit CITIZEN_CLAIM_ISSUE_PAYMENT, which starts
*       CREATE_CLAIM_SPEC_AFTER_PAYMENT (~16 case events per claim).
*
* Lines marked CHECK are the only assumptions left. Verify them in the 5-user smoke run.
======================================================================================*/

object BurstPrep {
  
  val civilServiceURL = CUIClaimCreationWithAPI.civilServiceURL
  val paymentURL = Environment.PaymentURL
  
  private def writeRow(line: String): Unit = synchronized {
    val fw = new BufferedWriter(new FileWriter("burst_cases.csv", true))
    try fw.write(line + "\r\n") finally fw.close()
  }
  
  /*------------------------------------------------------------------------------------
  * 1. Create the claim (existing steps). CreateClaimCUIR2WithAPI saves the case id as
  *    "claimNumber". CHECK: bodies/cuiclaim/CUI_Create_Claim.json uses event CREATE_LIP_CLAIM.
  ------------------------------------------------------------------------------------*/
  val createClaim =
    exec(CreateUser.CreateClaimantCitizen)
      .exec(CUIClaimCreationWithAPI.AuthForClaimCreationAPI)   // saves bearerToken
      .exec(S2S.s2s())                                          // saves ServiceToken (CHECK: microservice used by S2S.s2s())
      .exec(CUIClaimCreationWithAPI.getUserId)                  // saves userId
      .exec(CUIClaimCreationWithAPI.CreateClaimCUIR2WithAPI)    // saves claimNumber
      .pause(20)                                                // let the small create-claim process run first
  
  /*------------------------------------------------------------------------------------
  * 2. Create the claim fee payment in civil-service. This is what CUI does on
  *    "Continue to pay" (request 450). Returns the gov.uk Pay page and the payment ref.
  *    CHECK: response field names (paymentReference, nextUrl). If the call needs a JSON
  *    body or a return URL, copy it from the CUI request in the browser network tab.
  ------------------------------------------------------------------------------------*/
  val createFeePayment =
    group("BurstPrep_010_CreateClaimFeePayment") {
      exec(http("BurstPrep_010_005_CreateClaimFeePayment")
        .post(civilServiceURL + "/fees/CLAIMISSUED/case/#{claimNumber}/payment")
        .header("Authorization", "Bearer #{bearerToken}")
        .header("ServiceAuthorization", "#{ServiceToken}")
        .header("Content-Type", "application/json")
        .check(status.in(200, 201))
        .check(jsonPath("$.paymentReference").saveAs("paymentReference"))
        .check(jsonPath("$.nextUrl").saveAs("payNextUrl")))
    }
      .pause(2)
  
  /*------------------------------------------------------------------------------------
  * 3. gov.uk Pay: open the card page (same checks as request 450), enter the card
  *    (same as 460), then confirm (same as 470) WITHOUT following the redirect.
  ------------------------------------------------------------------------------------*/
  val payByCard =
    group("BurstPrep_020_GovPayCardPage") {
      exec(http("BurstPrep_020_005_GovPayCardPage")
        .get("#{payNextUrl}")
        .headers(CivilDamagesHeader.CUIR2Get)
        .check(substring("Enter card details"))
        .check(css("input[name='csrfToken']", "value").saveAs("_csrfTokenCardDetailPage"))
        .check(css("input[name='chargeId']", "value").saveAs("CardDetailPageChargeId")))
    }
      .pause(2)
      
      .group("BurstPrep_030_SubmitCardDetail") {
        exec(http("BurstPrep_030_005_SubmitCardDetail")
          .post(paymentURL + "/card_details/#{CardDetailPageChargeId}")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("chargeId", "#{CardDetailPageChargeId}")
          .formParam("csrfToken", "#{_csrfTokenCardDetailPage}")
          .formParam("cardNo", "4444333322221111")
          .formParam("expiryMonth", "07")
          .formParam("expiryYear", "31")
          .formParam("cardholderName", "Perf Tester")
          .formParam("cvc", "123")
          .formParam("addressLine1", "4")
          .formParam("addressLine2", "Hibernia Gardens")
          .formParam("addressCity", "Hounslow")
          .formParam("addressCountry", "GB")
          .formParam("addressPostcode", "TW3 3SD")
          .formParam("email", "vijaykanth6@gmail.com")
          .check(css("input[name='csrfToken']", "value").saveAs("_csrfTokenCardDetailConfirm"))
          .check(regex("Confirm your payment")))
      }
      .pause(2)
      
      // The ONLY difference from request 470: do not follow the redirect back to CUI.
      // Following it makes CUI call the status endpoint and issues the claim during prep.
      .group("BurstPrep_040_ConfirmCardDetail_NoRedirect") {
        exec(http("BurstPrep_040_005_ConfirmCardDetail_NoRedirect")
          .post(paymentURL + "/card_details/#{CardDetailPageChargeId}/confirm")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("csrfToken", "#{_csrfTokenCardDetailConfirm}")
          .formParam("chargeId", "#{CardDetailPageChargeId}")
          .disableFollowRedirect
          .check(status.in(302, 303))                                  // CHECK in smoke run
          .check(header("Location").optional.saveAs("payReturnUrl")))  // should point at CUI, not followed
      }
  
  // 4. Record the claim. Only reached if every step above passed (exitBlockOnFail).
  val recordClaim =
    exec { session =>
      writeRow(Seq("claimNumber", "paymentReference", "claimantEmailAddress")
        .map(k => session(k).as[String]).mkString(","))
      session
    }
  
  val prepScenario =
    scenario("Camunda Burst - Data Prep")
      .exitBlockOnFail {
        exec(_.set("env", "perftest"))
          .exec(createClaim)
          .exec(createFeePayment)
          .exec(payByCard)
          .exec(recordClaim)
        // Do NOT delete the claimant user: the burst logs in as them.
      }
}

object Burst {
  
  // Burst start, epoch milliseconds. Set as an environment variable, like TEST_TYPE:
  //   BURST_START=$(date -d "10:30" +%s%3N)
  val burstStart: Long = sys.env.getOrElse("BURST_START", System.getProperty("burstStart", "0")).toLong
  val burstWindowMs: Int = 120000 // 2 minutes
  
  val civilServiceURL = CUIClaimCreationWithAPI.civilServiceURL
  
  // Fire at a random moment inside the window. Users who are ready early just wait.
  val waitForWindow =
    exec { session =>
      val fireAt = burstStart + Random.nextInt(burstWindowMs)
      session.set("waitMs", math.max(0L, fireAt - System.currentTimeMillis))
    }
      .pause(session => session("waitMs").as[Long].milliseconds)
  
  // What CUI calls after gov.uk Pay. "Success" issues the claim and starts the
  // after-payment process. CHECK: response field name for the status.
  val paymentStatus =
  group("Burst_010_ClaimIssuePaymentStatus") {
    exec(http("Burst_010_005_ClaimIssuePaymentStatus")
      .get(civilServiceURL + "/fees/CLAIMISSUED/case/#{claimNumber}/payment/#{paymentReference}/status")
      .header("Authorization", "Bearer #{bearerToken}")
      .header("ServiceAuthorization", "#{ServiceToken}")
      .check(status.is(200))
      .check(jsonPath("$.status").is("Success")))
  }
  
  // Feeder file needs the header line: claimNumber,paymentReference,claimantEmailAddress
  def burstScenario(feederFile: String) =
    scenario("Camunda Burst - Payment Status")
      .feed(csv(feederFile).queue) // each claim used once only
      .exitBlockOnFail {
        exec(CUIClaimCreationWithAPI.AuthForClaimCreationAPI) // direct IDAM token, no UI login
          .exec(S2S.s2s())
          .exec(waitForWindow)
          .exec(paymentStatus)
      }
}

/*======================================================================================
* Changes in CivilDamagesSimulation.scala - see the step-by-step instructions.
* Never run the burst with -Ddebug=on: debug disables pauses, so every call fires at once.
======================================================================================*/