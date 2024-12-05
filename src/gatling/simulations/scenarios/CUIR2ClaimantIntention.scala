
package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.{CivilDamagesHeader, Common, CsrfCheck, Environment}

object CUIR2ClaimantIntention {

  val BaseURL = Environment.baseURL
  val CitizenURL = Environment.citizenURL
  val IdAMURL = Environment.idamURL
  val IdamUrl = Environment.idamURL
  val paymentURL = Environment.PaymentURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val caseFeeder=csv("caseIds.csv").circular

  /*======================================================================================
             * Civil Citizen R2 Claim creation
  ==========================================================================================*/
  val run=


    exec(_.setAll(
      "Idempotencynumber" -> (Common.getIdempotency()),
      "EvidenceYear" -> Common.getYear(),
      "EvidenceDay" -> Common.getDay(),
      "EvidenceMonth" -> Common.getMonth(),
      "CitizenRandomString" -> Common.randomString(5),
      "representativeFullName" -> (Common.randomString(5) + "representativeFullName"))
    )
  
  
      /*======================================================================================
                     * Civil UI Claim - View And Respond to Claim- Click On Claim
          ==========================================================================================*/
      .group("CUIR2_ClaimantIntention_030_ClickOnClaimToRespond") {
        exec(http("CUIR2_ClaimantIntention_030_005_ClickOnClaimToRespond")
          .get(CitizenURL + "/dashboard/#{caseId}/claimant")
          .headers(CivilDamagesHeader.MoneyClaimNavHeader)
          .check(status.in(200, 304))
          .check(substring("Response to the claim"))
    
        )
      }
  
      /*======================================================================================
                     * Civil UI Claim - Click On View And Respond from Claimant
          ==========================================================================================*/
      .group("CUIR2_ClaimantIntention_040_ClickOnViewAndRespond") {
        exec(http("CUIR2_ClaimantIntention_040_005_ClickOnViewAndRespond")
          .get(CitizenURL + "/case/#{caseId}/claimant-response/task-list")
          .headers(CivilDamagesHeader.MoneyClaimNavHeader)
          .check(status.in(200, 304))
          .check(substring("Your response"))
    
        )
      }
      .pause(MinThinkTime, MaxThinkTime)

      /*======================================================================================
                 * Civil Citizen - View Def Response
      ==========================================================================================*/
      .group("CUIR2_ClaimantIntention_050_ViewDefResponse") {
        exec(http("CUIR2_ClaimantIntention_050_005_ViewDefResponse")
          .get ("/case/#{caseId}/claimant-response/defendants-response")
          .headers(CivilDamagesHeader.CivilCitizenPost)
         // .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
          .check(CsrfCheck.save)
          .check(substring("The defendant’s response")))
      }
      .pause(MinThinkTime, MaxThinkTime)


     
  
      /*======================================================================================
               * Civil Citizen - View Def response Continue post
    ==========================================================================================*/
      .group("CUIR2_ClaimantIntention_060_HowtheywanttoPay") {
        exec(http("CUIR2_ClaimantIntention_060_005_HowtheywantToPay")
          .post(CitizenURL + "/case/#{caseId}/claimant-response/defendants-response?page=how-they-want-to-pay-response")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("option","yes")
         // .check(CsrfCheck.save)
          .check(substring("You have completed 1 of 3 sections")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
                 * Civil Citizen - Accept or Reject 500
      ==========================================================================================*/
      .group("CUIR2_ClaimantIntention_070_AcceptOrRejectMoney") {
        exec(http("CUIR2_ClaimantIntention_070_005_AcceptOrRejectMoney")
          .get("/case/#{caseId}/claimant-response/settle-admitted")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          // .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
          .check(CsrfCheck.save)
          .check(substring("Do you want to settle the claim for the")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  
      /*======================================================================================
               * Civil Citizen - Do you want to settle the amount  post
    ==========================================================================================*/
      .group("CUIR2_ClaimantIntention_080_DoYouSettleClaim") {
        exec(http("CUIR2_ClaimantIntention_080_005_DoYouSettleClaim")
          .post(CitizenURL + "/case/#{caseId}/claimant-response/settle-admitted")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "yes")
         // .check(CsrfCheck.save)
          .check(substring("You have completed 2 of 3 sections")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      /*======================================================================================
     * Civil UI Claim - Check and submit your response to Defendant
    ==========================================================================================*/
  
      .group("CUIR2_ClaimantIntention_090_CheckYourAnswers") {
        exec(http("CUIR2_ClaimantIntention_090_005_CheckYourAnswers")
          .get(CitizenURL + "/case/#{caseId}/claimant-response/check-and-send")
          .headers(CivilDamagesHeader.MoneyClaimNavHeader)
           .check(CsrfCheck.save)
          .check(status.in(200, 304))
          .check(substring("Do you accept or reject the defendant&#39;s admission?"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      /*======================================================================================
    * Civil UI Claim - Check your answers
    ==========================================================================================*/
  
      .group("CUIR2_ClaimantIntention_100_CheckAndSubmit") {
        exec(http("CUIR2_ClaimantIntention_100_005_CheckYourAnswers")
          .post(CitizenURL + "/case/#{caseId}/claimant-response/check-and-send")
          .headers(CivilDamagesHeader.DefCheckAndSendPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("type", "basic")
          .formParam("isClaimantRejectedDefendantOffer", "false")
          .check(substring("You've accepted their response"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
}
