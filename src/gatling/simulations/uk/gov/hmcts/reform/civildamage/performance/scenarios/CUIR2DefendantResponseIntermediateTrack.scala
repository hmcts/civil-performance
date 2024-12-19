
package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef.{exec, _}
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, CsrfCheck, Environment}

object CUIR2DefendantResponseIntermediateTrack {
  
  val BaseURL = Environment.baseURL
  val IdAMURL = Environment.idamURL
  val CivilUiURL = "https://civil-citizen-ui.perftest.platform.hmcts.net"
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime
  
  val run=
    
    /*======================================================================================
                   * Civil UI Claim - Respond to Claim- Click On Claim
        ==========================================================================================*/
    group("CUIR2_DefResponse_030_ClickOnClaimToRespond") {
      exec(http("CUIR2_DefResponse_030_005_ClickOnClaimToRespond")
        .get(CivilUiURL + "/dashboard/#{caseId}/defendant")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(status.in(200, 304))
        .check(substring("You need to respond before"))
        
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
    
    /*======================================================================================
               * Civil UI Claim - click on Respond to Claim
    ==========================================================================================*/
    .group("CUIR2_DefResponse_040_LanguagePreference") {
      exec(http("CUIR2_DefResponse_040_005_LanguagePreference")
        .get(CivilUiURL + "/case/#{caseId}/response/bilingual-language-preference")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("do you want to respond to this claim?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
             * Civil UI Claim - Do you want to respond to this claim in Welsh?
  ==========================================================================================*/

    .group("CUIR2_DefResponse_050_InWelshYesNo") {
      exec(http("CUIR2_DefResponse_050_005_InWelshYesNo")
        .post(CivilUiURL + "/case/#{caseId}/response/bilingual-language-preference")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "ENGLISH")
        .check(status.in(200, 304))
        .check(substring("Respond to a money claim"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
           * Civil UI Claim - Confirm Your Details Redirect
==========================================================================================*/

    .group("CUIR2_DefResponse_060_ConfirmDefDetails") {
      exec(http("CUIR2_DefResponse_060_005_ConfirmDefDetails")
        .get(CivilUiURL + "/case/#{caseId}/response/your-details")
        .headers(CivilDamagesHeader.CUIR2Get)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Confirm your details"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
         * Civil UI Claim - Confirm Your Details Confirm
==========================================================================================*/

    .group("CUIR2_DefResponse_070_ConfirmDetailsConfirm") {
      exec(http("CUIR2_DefResponse_070_005_ConfirmDetailsConfirm")
        .post(CivilUiURL + "/case/#{caseId}/response/your-details")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("title", "Mr")
        .formParam("firstName", "Def First")
        .formParam("lastName", "Def Last")
        .formParam("addressLine1", "10 Hibernia Gardens")
        .formParam("addressLine2", "")
        .formParam("addressLine3", "")
        .formParam("city", "Hounslow")
        .formParam("postCode", "TW3 3SD")
        .formParam("postToThisAddress", "no")
        .formParam("correspondenceAddressPostcode", "")
        .formParam("addressList", "")
        .formParam("addressLine1", "")
        .formParam("addressLine2", "")
        .formParam("addressLine3", "")
        .formParam("city", "")
        .formParam("postCode", "")
        .check(status.in(200, 304))
        .check(CsrfCheck.save)
        .check(substring("Enter your date of birth"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
         * Civil UI Claim - Enter your date of birth
==========================================================================================*/

    .group("CUIR2_DefResponse_080_DateOfBirth") {
      exec(http("CUIR2_DefResponse_080_005_DateOfBirth")
        .post(CivilUiURL + "/case/#{caseId}/response/your-dob")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("day", "01")
        .formParam("month", "08")
        .formParam("year", "1983")
        .check(status.in(200, 304))
        .check(substring("Enter a phone number"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
       * Civil UI Claim - Enter a phone number
==========================================================================================*/

    .group("CUIR2_DefResponse_090_PhoneNumber") {
      exec(http("CUIR2_DefResponse_090_005_PhoneNumber")
        .post(CivilUiURL + "/case/#{caseId}/response/your-phone")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("telephoneNumber", "01234567890")
        .check(status.in(200, 304))
        .check(substring("Respond to a money claim"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
         * Civil UI Claim - View your options before response deadline -task list
==========================================================================================*/

    .group("CUIR2_DefResponse_100_ViewOptions") {
      exec(http("CUIR2_DefResponse_100_005_ViewOptions")
        .get(CivilUiURL + "/case/#{caseId}/response/understanding-your-options")
        .headers(CivilDamagesHeader.CUIR2Get)
       // .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Requesting extra time"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
       * Civil UI Claim - Requesting extra time
==========================================================================================*/

    .group("CUIR2_DefResponse_110_ExtraTime") {
      exec(http("CUIR2_DefResponse_110_005_ExtraTime")
        .get(CivilUiURL + "/case/#{caseId}/response/response-deadline-options")
        .headers(CivilDamagesHeader.CUIR2Get)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Response deadline"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
     * Civil UI Claim - Response deadline - no
==========================================================================================*/

    .group("CUIR2_DefResponse_120_ResponseDeadline") {
      exec(http("CUIR2_DefResponse_120_005_ResponseDeadline")
        .post(CivilUiURL + "/case/#{caseId}/response/response-deadline-options")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "no")
        .check(status.in(200, 304))
        .check(substring("View your options before response deadline"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
         * Civil UI Claim - Choose A Response Redirect - task list
==========================================================================================*/

    .group("CUIR2_DefResponse_130_ChooseAResponse") {
      exec(http("CUIR2_DefResponse_130_005_ChooseAResponse")
        .get(CivilUiURL + "/case/#{caseId}/response/response-type")
        .headers(CivilDamagesHeader.CUIR2Get)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("How do you respond to the claim?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
     * Civil UI Claim - How do you respond to the claim? - I admit part of the claim
==========================================================================================*/

    .group("CUIR2_DefResponse_140_ResponseType") {
      exec(http("CUIR2_DefResponse_140_005_ResponseType")
        .post(CivilUiURL + "/case/#{caseId}/response/response-type")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("responseType", "FULL_DEFENCE")
        .check(status.in(200, 304))
        .check(CsrfCheck.save)
        .check(substring("Why do you believe you don’t owe Mr Claimant First Claimant Last any money?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
     * Civil UI Claim - Have you paid the claimant the amount you admit you owe? - no
==========================================================================================*/

    .group("CUIR2_DefResponse_150_RejectAll") {
      exec(http("CUIR2_DefResponse_150_005_RejectAll")
        .post(CivilUiURL + "/case/#{caseId}/response/reject-all-of-claim")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "dispute")
        .check(status.in(200, 304))
        .check(substring("Choose a response"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
         * Civil UI Claim - Your Defence
==========================================================================================*/

    .group("CUIR2_DefResponse_160_YourDefence") {
      exec(http("CUIR2_DefResponse_160_005_YourDefence")
        .get(CivilUiURL + "/case/#{caseId}/response/your-defence")
        .headers(CivilDamagesHeader.CUIR2Get)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Why do you disagree with the claim?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)

    
    /*======================================================================================
       * Civil UI Claim - How much money do you admit you owe?
==========================================================================================*/

    .group("CUIR2_DefResponse_170_YourDefencePost") {
      exec(http("CUIR2_DefResponse_170_005_YourDefencePost")
        .post(CivilUiURL + "/case/#{caseId}/response/your-defence")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("text", "asasasasas")
        .check(status.in(200, 304))
      )
        
    }
    .pause(MinThinkTime, MaxThinkTime)
      

      /*======================================================================================
   * Civil UI Claim - Add your timeline of events
==========================================================================================*/

    .group("CUIR2_DefResponse_200_TimelineOfEvents") {
      exec(http("CUIR2_DefResponse_200_005_TimelineOfEvents")
        .post(CivilUiURL + "/case/#{caseId}/response/timeline")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("rows[0][day]", "01")
        .formParam("rows[0][month]", "01")
        .formParam("rows[0][year]", "2024")
        .formParam("rows[0][description]", "first description0")
        .formParam("rows[1][day]", "01")
        .formParam("rows[1][month]", "02")
        .formParam("rows[1][year]", "2024")
        .formParam("rows[1][description]", "description1")
        .formParam("rows[2][day]", "")
        .formParam("rows[2][month]", "")
        .formParam("rows[2][year]", "")
        .formParam("rows[2][description]", "")
        .formParam("rows[3][day]", "")
        .formParam("rows[3][month]", "")
        .formParam("rows[3][year]", "")
        .formParam("rows[3][description]", "")
        .formParam("comment]", "perftest comment")
        .check(substring("List your evidence"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
* Civil UI Claim - List your evidence
==========================================================================================*/


.group("CUIR2_DefResponse_210_ListEvidence") {
  exec(http("CUIR2_DefResponse_210_005_ListEvidence")
  .post(CivilUiURL + "/case/#{caseId}/response/evidence")
  .headers(CivilDamagesHeader.CUIR2Post)
  .formParam("_csrf", "#{csrf}")
  .formParam("evidenceItem[0][type]", "Expert witness")
  .formParam("evidenceItem[0][description]]", "timelineDescription Expert Witness")
    .formParam("evidenceItem[1][type]", "")
    .formParam("evidenceItem[1][description]]", "")
    .formParam("evidenceItem[2][type]", "")
    .formParam("evidenceItem[2][description]]", "")
    .formParam("evidenceItem[3][type]", "")
    .formParam("evidenceItem[3][description]]", "")
  .formParam("comment", "comment")
    .check(substring("Choose a response"))
  )
}
  .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
       * Civil UI Claim - Give us details incase there is a hearing -directions questionaire
==========================================================================================*/

    .group("CUIR2_DefResponse_240_TriedToSettle") {
      exec(http("CUIR2_DefResponse_240_005_TriedToSettle")
        .get(CivilUiURL + "/case/#{caseId}/directions-questionnaire/tried-to-settle")
        .headers(CivilDamagesHeader.CUIR2Get)
        .check(status.in(200, 304))
        .check(substring("Have you tried to settle this claim before going to court?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



   
    /*======================================================================================
 * Civil UI Claim - Tried to settle
==========================================================================================*/

    .group("CUIR2_DefResponse_260_TriedSettle") {
      exec(http("CUIR2_DefResponse_260_005_ConfirmNumberTriedSettle")
        .post(CivilUiURL + "/case/#{caseId}/directions-questionnaire/tried-to-settle")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "no")
        .check(substring("Do you want an extra 4 weeks to try to settle the claim?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
  
  
      /*======================================================================================
      * Civil UI Claim - Request extra Time
      ==========================================================================================*/
  
      .group("CUIR2_DefResponse_260_RequestExtraTime") {
        exec(http("CUIR2_DefResponse_260_005_RequestExtraTime")
          .post(CivilUiURL + "/case/#{caseId}/directions-questionnaire/request-extra-4-weeks")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "no")
          .check(substring("Are there any documents the other party has that you want the court to consider?"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
      * Civil UI Claim - Claim Documents
      ==========================================================================================*/
  
      .group("CUIR2_DefResponse_260_ClaimantDocuments") {
        exec(http("CUIR2_DefResponse_260_005_ClaimantDocuments")
          .post(CivilUiURL + "/case/#{caseId}/directions-questionnaire/consider-claimant-documents")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("detail","")
          .formParam("option", "no")
          .check(substring("Do you want to use expert evidence?"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
    /*======================================================================================
     * Civil UI Claim - Give us details in case expert evidence
==========================================================================================*/

    .group("CUIR2_DefResponse_270_GiveDetails") {
      exec(http("CUIR2_DefResponse_270_005_GiveDetails")
        .post(CivilUiURL + "/case/#{caseId}/directions-questionnaire/expert-evidence")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "no")
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Do you want to give evidence yourself?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)

    


    /*======================================================================================
 * Civil UI Claim - Do you want to give evidence yourself? - yes
==========================================================================================*/

    .group("CUIR2_DefResponse_300_GiveEvidence") {
      exec(http("CUIR2_DefResponse_300_005_GiveEvidence")
        .post(CivilUiURL + "/case/#{caseId}/directions-questionnaire/give-evidence-yourself")
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
        .post(CivilUiURL + "/case/#{caseId}/directions-questionnaire/other-witnesses")
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
        .post(CivilUiURL + "/case/#{caseId}/directions-questionnaire/cant-attend-hearing-in-next-12-months")
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
        .post(CivilUiURL + "/case/#{caseId}/directions-questionnaire/phone-or-video-hearing")
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
        .post(CivilUiURL + "/case/#{caseId}/directions-questionnaire/vulnerability")
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
        .post(CivilUiURL + "/case/#{caseId}/directions-questionnaire/support-required")
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
        .post(CivilUiURL + "/case/#{caseId}/directions-questionnaire/court-location")
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
        .post(CivilUiURL + "/case/#{caseId}/directions-questionnaire/welsh-language")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("speakLanguage", "en")
        .formParam("documentsLanguage", "en")
        .check(substring("Give us details in case there"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
   * Civil UI Claim - Check and submit your response Redirect
==========================================================================================*/

   .group("CUIR2_DefResponse_380_CheckYourAnswers") {
      exec(http("CUIR2_DefResponse_380_005_CheckYourAnswers")
        .get(CivilUiURL + "/case/#{caseId}/response/check-and-send")
        .headers(CivilDamagesHeader.CUIR2Get)
       // .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Check your answers"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
* Civil UI Claim - Check your answers
==========================================================================================*/

    .group("CUIR2_DefResponse_390_CheckAndSubmit") {
      exec(http("CUIR2_DefResponse_390_005_CheckYourAnswers")
        .post(CivilUiURL + "/case/#{caseId}/response/check-and-send")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("type", "basic")
        .formParam("isFullAmountRejected", "true")
        .formParam("signed", "true")
        .formParam("directionsQuestionnaireSigned", "true")
        .check(substring("You&#39;ve submitted your response"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
}

