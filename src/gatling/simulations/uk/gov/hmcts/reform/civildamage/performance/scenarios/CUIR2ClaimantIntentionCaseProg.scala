
package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, Common, CsrfCheck, Environment}

object CUIR2ClaimantIntentionCaseProg {

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
                 * Civil Citizen - Decide whether to proceed
      ==========================================================================================*/
      .group("CUIR2_ClaimantIntention_070_AcceptOrRejectMoney") {
        exec(http("CUIR2_ClaimantIntention_070_005_AcceptOrRejectMoney")
          .get("/case/#{caseId}/claimant-response/intention-to-proceed")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          // .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
          .check(CsrfCheck.save)
          .check(substring("Do you want to proceed with claim?")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  
      /*======================================================================================
                 * Civil Citizen - do you want to proceed with the claim post
      ==========================================================================================*/
      .group("CUIR2_ClaimantIntention_070_DoYouProceedWithClaim") {
        exec(http("CUIR2_ClaimantIntention_070_005_DoYouProceedWithClaim")
          .post("/case/#{caseId}/claimant-response/intention-to-proceed")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "yes")
         // .check(CsrfCheck.save)
          .check(substring("You have completed 2 of 4 sections")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      /*======================================================================================
       * Civil UI Claim - Give us details in case there's a hearing Redirect
  ==========================================================================================*/
  
      .group("CUIR2_DefResponse_270_GiveDetails") {
        exec(http("CUIR2_DefResponse_270_005_GiveDetails")
          .get(CitizenURL + "/case/#{caseId}/directions-questionnaire/determination-without-hearing")
          .headers(CivilDamagesHeader.CUIR2Get)
          .check(CsrfCheck.save)
          .check(status.in(200, 304))
          .check(substring("Determination without Hearing Questions"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
       * Civil UI Claim - Determination without Hearing Questions
  ==========================================================================================*/
  
      .group("CUIR2_DefResponse_280_Determination") {
        exec(http("CUIR2_DefResponse_280_005_Determination")
          .post(CitizenURL + "/case/#{caseId}/directions-questionnaire/determination-without-hearing")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "no")
          .formParam("reasonForHearing", "asasasasas")
          .check(substring("Using an expert"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      /*======================================================================================
     * Civil UI Claim - Using an expert
  ==========================================================================================*/
  
      .group("CUIR2_DefResponse_290_UsingExpert") {
        exec(http("CUIR2_DefResponse_290_005_UsingExpert")
          .post(CitizenURL + "/case/#{caseId}/directions-questionnaire/expert")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .check(substring("Do you want to give evidence yourself?"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      /*======================================================================================
   * Civil UI Claim - Do you want to give evidence yourself? - yes
  ==========================================================================================*/
  
      .group("CUIR2_DefResponse_300_GiveEvidence") {
        exec(http("CUIR2_DefResponse_300_005_GiveEvidence")
          .post(CitizenURL + "/case/#{caseId}/directions-questionnaire/give-evidence-yourself")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "yes")
          .check(substring("Do you have other witnesses?"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      /*======================================================================================
  * Civil UI Claim - Do you have other witnesses? - no
  ==========================================================================================*/
  
      .group("CUIR2_DefResponse_310_OtherWitnesses") {
        exec(http("CUIR2_DefResponse_310_005_OtherWitnesses")
          .post(CitizenURL + "/case/#{caseId}/directions-questionnaire/other-witnesses")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("witnessItems[0][firstName]", "")
          .formParam("witnessItems[0][lastName]", "")
          .formParam("witnessItems[0][email]", "")
          .formParam("witnessItems[0][telephone]", "")
          .formParam("witnessItems[0][details]", "")
          .formParam("option", "no")
          .check(substring("Are there any dates in the next 12 months when you, your experts or witnesses cannot go to a hearing?"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      /*======================================================================================
  * Civil UI Claim - Are there any dates in the next 12 months when you, your experts or witnesses cannot go to a hearing? - no
  ==========================================================================================*/
  
      .group("CUIR2_DefResponse_320_AnyDates") {
        exec(http("CUIR2_DefResponse_320_005_AnyDates")
          .post(CitizenURL + "/case/#{caseId}/directions-questionnaire/cant-attend-hearing-in-next-12-months")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "no")
          .check(substring("Do you want to ask for a telephone or video hearing?"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  
      /*======================================================================================
  * Civil UI Claim - Do you want to ask for a telephone or video hearing? - yes
  ==========================================================================================*/
  
      .group("CUIR2_DefResponse_330_AskForTelephone") {
        exec(http("CUIR2_DefResponse_330_005_AskForTelephone")
          .post(CitizenURL + "/case/#{caseId}/directions-questionnaire/phone-or-video-hearing")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "yes")
          .formParam("details", "perf details")
          .check(substring("Are you, your experts or witnesses vulnerable in a way that the court needs to consider?"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      /*======================================================================================
  * Civil UI Claim - Are you, your experts or witnesses vulnerable in a way that the court needs to consider? - no
  ==========================================================================================*/
  
      .group("CUIR2_DefResponse_340_Vulnerable") {
        exec(http("CUIR2_DefResponse_340_005_Vulnerable")
          .post(CitizenURL + "/case/#{caseId}/directions-questionnaire/vulnerability")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("vulnerabilityDetails", "")
          .formParam("option", "no")
          .check(substring("Do you, your experts or witnesses need support to attend a hearing?"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  
      /*======================================================================================
  * Civil UI Claim - Do you, your experts or witnesses need support to attend a hearing? - no
  ==========================================================================================*/
  
      .group("CUIR2_DefResponse_350_NeedSupport") {
        exec(http("CUIR2_DefResponse_350_005_NeedSupport")
          .post(CitizenURL + "/case/#{caseId}/directions-questionnaire/support-required")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("model[items][0][fullName]", "")
          .formParam("model[items][0][signLanguageInterpreter][content]", "")
          .formParam("model[items][0][languageInterpreter][content]", "")
          .formParam("model[items][0][otherSupport][content]", "")
          .formParam("option", "no")
          .check(substring("Do you want to ask for the hearing to be held at a specific court?"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      /*======================================================================================
  * Civil UI Claim - Do you want to ask for the hearing to be held at a specific court? - no
  ==========================================================================================*/
  
      .group("CUIR2_DefResponse_360_SpecifcCourt") {
        exec(http("CUIR2_DefResponse_360_005_SpecifcCourt")
          .post(CitizenURL + "/case/#{caseId}/directions-questionnaire/court-location")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("courtLocation", "")
          .formParam("reason", "")
          .formParam("option", "no")
          .check(substring("Welsh language"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      /*======================================================================================
  * Civil UI Claim - Welsh language
  ==========================================================================================*/
  
      .group("CUIR2_DefResponse_370_WelshLanguage") {
        exec(http("CUIR2_DefResponse_370_005_WelshLanguage")
          .post(CitizenURL + "/case/#{caseId}/directions-questionnaire/welsh-language")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("speakLanguage", "en")
          .formParam("documentsLanguage", "en")
          .check(substring("You have completed 3 of 4 sections"))
        )
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
          .check(substring("Do you want to proceed with the claim?"))
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
          .check(substring("You've rejected their response"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
}