
package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, Common, CsrfCheck, Environment}

object CUIR2ClaimantIntentionCaseProgFastTrack {

  val BaseURL = Environment.baseURL
  val CitizenURL = Environment.citizenURL
  val IdAMURL = Environment.idamURL
  val IdamUrl = Environment.idamURL
  val paymentURL = Environment.PaymentURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val caseFeeder=csv("claimNumbers.csv").circular

  /*======================================================================================
             * Civil Citizen R2 Claim Intention
  ==========================================================================================*/
  /*======================================================================================
             * Civil Citizen R2 Claim creation
  ==========================================================================================*/
  val run =



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
    .group("CUICPFT_ClaimantIntention_030_ClickOnClaimToRespond") {
      exec(http("CUICPFT_ClaimantIntention_030_005_ClickOnClaimToRespond")
        .get(CitizenURL + "/dashboard/#{claimNumber}/claimant")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(status.in(200, 304))
        .check(substring("Response to the claim"))
    
      )
    }
  
    /* /*======================================================================================
                    * Civil UI Claim - Click On View And Respond from Claimant
         ==========================================================================================*/
     .group("CUICPFT_ClaimantIntention_040_ClickOnViewAndRespond") {
       exec(http("CUICPFT_ClaimantIntention_040_005_ClickOnViewAndRespond")
         .get(CitizenURL + "/case/#{claimNumber}/claimant-response/task-list")
         .headers(CivilDamagesHeader.MoneyClaimNavHeader)
         .check(status.in(200, 304))
         .check(substring("Your response"))
   
       )
     }
     .pause(MinThinkTime, MaxThinkTime)*/
  
    /*======================================================================================
               * Civil Citizen - View Def Response
    ==========================================================================================*/
    .group("CUICPFT_ClaimantIntention_050_ViewDefResponse") {
      exec(http("CUICPFT_ClaimantIntention_050_005_ViewDefResponse")
        .get(CitizenURL + "/case/#{claimNumber}/claimant-response/defendants-response")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        // .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
        .check(CsrfCheck.save)
        .check(substring("The defendant’s response")))
    }
    .pause(MinThinkTime, MaxThinkTime)
  
    /*======================================================================================
             * Civil Citizen - View Def response Continue post
  ==========================================================================================*/
    .group("CUICPFT_ClaimantIntention_060_HowtheywanttoPay") {
      exec(http("CUICPFT_ClaimantIntention_060_005_HowtheywantToPay")
        .post(CitizenURL + "/case/#{claimNumber}/claimant-response/defendants-response?page=how-they-want-to-pay-response")
        .headers(CivilDamagesHeader.CivilCitizenPost)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "yes")
        // .check(CsrfCheck.save)
        .check(substring("You have completed 1 of 3 sections")))
    }
    .pause(MinThinkTime, MaxThinkTime)
  
    /*======================================================================================
               * Civil Citizen - Decide whether to proceed
    ==========================================================================================*/
    .group("CUICPFT_ClaimantIntention_070_IntentionToProceedGet") {
      exec(http("CUICPFT_ClaimantIntention_070_005_IntentionToProceed")
        .get(CitizenURL + "/case/#{claimNumber}/claimant-response/intention-to-proceed")
        .headers(CivilDamagesHeader.CivilCitizenPost)
        // .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
        .check(CsrfCheck.save)
        .check(substring("Do you want to proceed with claim?")))
    }
    .pause(MinThinkTime, MaxThinkTime)
  
  
  
    /*======================================================================================
               * Civil Citizen - do you want to proceed with the claim post
    ==========================================================================================*/
    .group("CUICPFT_ClaimantIntention_080_DoYouProceedWithClaim") {
      exec(http("CUICPFT_ClaimantIntention_080_005_DoYouProceedWithClaim")
        .post(CitizenURL + "/case/#{claimNumber}/claimant-response/intention-to-proceed")
        .headers(CivilDamagesHeader.CivilCitizenPost)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "yes")
        // .check(CsrfCheck.save)
        .check(substring("Your response")))
    }
    .pause(MinThinkTime, MaxThinkTime)
  
    /*======================================================================================
       * Civil UI Claim - intention to proceed
  ==========================================================================================*/
  
    .group("CUICPFT_ClaimantIntention_090_TriedToSettleGet") {
      exec(http("CUICPFT_ClaimantIntention_090_005_TriedToSettleGet")
        .get(CitizenURL + "/case/#{claimNumber}/directions-questionnaire/tried-to-settle")
        .headers(CivilDamagesHeader.CUIR2Get)
        .check(status.in(200, 304))
        .check(substring("Have you tried to settle this claim before going to court?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
  
  
    /*======================================================================================
               * Civil Citizen - do you want to proceed with the claim post
    ==========================================================================================*/
    .group("CUICPFT_ClaimantIntention_100_TriedToSettle") {
      exec(http("CUICPFT_ClaimantIntention_100_005_TriedToSettle")
        .post(CitizenURL + "/case/#{claimNumber}/directions-questionnaire/tried-to-settle")
        .headers(CivilDamagesHeader.CivilCitizenPost)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "no")
         .check(CsrfCheck.save)
        .check(substring("Do you want an extra 4 weeks to try to settle the claim?")))
    }
    .pause(MinThinkTime, MaxThinkTime)
  
  
    /*======================================================================================
               * Civil Citizen - do you want to proceed with the claim post
    ==========================================================================================*/
    .group("CUICPFT_ClaimantIntention_110_DoYouProceedWithClaim") {
      exec(http("CUICPFT_ClaimantIntention_110_005_DoYouProceedWithClaim")
        .post(CitizenURL + "/case/#{claimNumber}/directions-questionnaire/request-extra-4-weeks")
        .headers(CivilDamagesHeader.CivilCitizenPost)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "no")
         .check(CsrfCheck.save)
        .check(substring("Are there any documents the other party has that you want the court to consider?")))
    }
    .pause(MinThinkTime, MaxThinkTime)
  
  
    /*======================================================================================
               * Civil Citizen - do you want to proceed with the claim post
    ==========================================================================================*/
    .group("CUICPFT_ClaimantIntention_120_ClaimantDocs") {
      exec(http("CUICPFT_ClaimantIntention_120_005_ClaimantDocs")
        .post(CitizenURL + "/case/#{claimNumber}/directions-questionnaire/consider-claimant-documents")
        .headers(CivilDamagesHeader.CivilCitizenPost)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "no")
         .check(CsrfCheck.save)
        .check(substring("Do you want to use expert evidence?")))
    }
    .pause(MinThinkTime, MaxThinkTime)
  
  
    /*======================================================================================
               * Civil Citizen - do you want to proceed with the claim post
    ==========================================================================================*/
    .group("CUICPFT_ClaimantIntention_130_ExpertEvidence") {
      exec(http("CUICPFT_ClaimantIntention_130_005_DoYouProceedWithClaim")
        .post(CitizenURL + "/case/#{claimNumber}/directions-questionnaire/ExpertEvidence")
        .headers(CivilDamagesHeader.CivilCitizenPost)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "no")
        .check(CsrfCheck.save)
        .check(substring("Do you want to give evidence yourself?")))
    }
    .pause(MinThinkTime, MaxThinkTime)
  
  
    /*======================================================================================
               * Civil Citizen - do you want to proceed with the claim post
    ==========================================================================================*/
    .group("CUICPFT_ClaimantIntention_140_Evidence") {
      exec(http("CUICPFT_ClaimantIntention_140_005_DoYouProceedWithClaim")
        .post(CitizenURL + "/case/#{claimNumber}/directions-questionnaire/Evidence")
        .headers(CivilDamagesHeader.CivilCitizenPost)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "yes")
        .check(CsrfCheck.save)
        .check(substring("Do you have other witnesses?")))
    }
    .pause(MinThinkTime, MaxThinkTime)
  
  
    /*======================================================================================
               * Civil Citizen - do you want to proceed with the claim post
    ==========================================================================================*/
    .group("CUICPFT_ClaimantIntention_150_Witness") {
      exec(http("CUICPFT_ClaimantIntention_150_005_Witness")
        .post(CitizenURL + "/case/#{claimNumber}/directions-questionnaire/other-witnesses")
        .headers(CivilDamagesHeader.CivilCitizenPost)
        .formParam("_csrf", "#{csrf}")
        .formParam("witnessItems[0][firstName]", "")
        .formParam("witnessItems[0][lastName]", "")
        .formParam("witnessItems[0][email]", "")
        .formParam("witnessItems[0][telephone]", "")
        .formParam("witnessItems[0][details]", "")
        .formParam("option", "no")
        .check(CsrfCheck.save)
        .check(substring("Are there any dates in the next 12 months when you, your experts or witnesses cannot go to a hearing?")))
    }
    .pause(MinThinkTime, MaxThinkTime)
  
    /*======================================================================================
               * Civil Citizen - do you want to proceed with the claim post
    ==========================================================================================*/
    .group("CUICPFT_ClaimantIntention_160_SettleHearing") {
      exec(http("CUICPFT_ClaimantIntention_160_005_SettleHearing")
        .post(CitizenURL + "/case/#{claimNumber}/directions-questionnaire/cant-attend-hearing-in-next-12-months")
        .headers(CivilDamagesHeader.CivilCitizenPost)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "no")
        .check(CsrfCheck.save)
        .check(substring("Do you want to ask for a telephone or video hearing?")))
    }
    .pause(MinThinkTime, MaxThinkTime)
  
    /*======================================================================================
               * Civil Citizen - do you want to proceed with the claim post
    ==========================================================================================*/
    .group("CUICPFT_ClaimantIntention_170_PhoneOrVideo") {
      exec(http("CUICPFT_ClaimantIntention_010_005_PhoneOrVideo")
        .post(CitizenURL + "/case/#{claimNumber}/directions-questionnaire/phone-or-video-hearing")
        .headers(CivilDamagesHeader.CivilCitizenPost)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "no")
        .check(CsrfCheck.save)
        .check(substring("Are you, your experts or witnesses vulnerable in a way that the court needs to consider?")))
    }
    .pause(MinThinkTime, MaxThinkTime)
    
  
    /*======================================================================================
* Civil UI Claim - Are you, your experts or witnesses vulnerable in a way that the court needs to consider? - no
==========================================================================================*/
  
    .group("CUICPFT_ClaimantIntention_180_Vulnerable") {
      exec(http("CUICPFT_ClaimantIntention_180_005_Vulnerable")
        .post(CitizenURL + "/case/#{claimNumber}/directions-questionnaire/vulnerability")
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
  
    .group("CUICPFT_ClaimantIntention_190_NeedSupport") {
      exec(http("CUICPFT_ClaimantIntention_190_005_NeedSupport")
        .post(CitizenURL + "/case/#{claimNumber}/directions-questionnaire/support-required")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("model[items][0][fullName]", "")
        .formParam("model[items][0][signLanguageInterpreter][content]", "")
        .formParam("model[items][0][languageInterpreter][content]", "")
        .formParam("model[items][0][otherSupport][content]", "")
        .formParam("option", "no")
        .check(substring("Please select your preferred court hearing location"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
  
  
    /*======================================================================================
* Civil UI Claim - Do you want to ask for the hearing to be held at a specific court? - no
==========================================================================================*/
  
    .group("CUICPFT_ClaimantIntention_200_SpecifcCourt") {
      exec(http("CUICPFT_ClaimantIntention_200_005_SpecifcCourt")
        .post(CitizenURL + "/case/#{claimNumber}/directions-questionnaire/court-location")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("courtLocation", "Birmingham Civil and Family Justice Centre - Priory Courts, 33 Bull Street - B4 6DS")
        .formParam("reason", "asasasasas")
        // .formParam("option", "no")
        .check(substring("Welsh language"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
  
  
    /*======================================================================================
* Civil UI Claim - Welsh language
==========================================================================================*/
  
    .group("CUICPFT_ClaimantIntention_210_WelshLanguage") {
      exec(http("CUICPFT_ClaimantIntention_210_005_WelshLanguage")
        .post(CitizenURL + "/case/#{claimNumber}/directions-questionnaire/welsh-language")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("speakLanguage", "en")
        .formParam("documentsLanguage", "en")
        .check(substring("Your response"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
  
    /*======================================================================================
   * Civil UI Claim - Check and submit your response to Defendant
  ==========================================================================================*/
  
    .group("CUICPFT_ClaimantIntention_220_CheckYourAnswers") {
      exec(http("CUICPFT_ClaimantIntention_220_005_CheckYourAnswers")
        .get(CitizenURL + "/case/#{claimNumber}/claimant-response/check-and-send")
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
  
    .group("CUICPFT_ClaimantIntention_230_CheckAndSubmit") {
      exec(http("CUICPFT_ClaimantIntention_230_005_CheckYourAnswers")
        .post(CitizenURL + "/case/#{claimNumber}/claimant-response/check-and-send")
        .headers(CivilDamagesHeader.DefCheckAndSendPost)
        .formParam("_csrf", "#{csrf}")
        .formParam("type", "basic")
        .formParam("isClaimantRejectedDefendantOffer", "false")
        .check(substring("You've rejected their response"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
}
