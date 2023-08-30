
package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef.{exec, _}
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, CsrfCheck, Environment}

object DefendantResponse {
  
  val BaseURL = Environment.baseURL
  val IdAMURL = Environment.idamURL
  val CivilUiURL = "https://civil-citizen-ui.perftest.platform.hmcts.net"
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime
  
  
  val run=
  /*======================================================================================
       * Civil UI Claim - View your options before response deadline
==========================================================================================*/
  
  group("CUI_DefResponse_010_CivilUIHomepage") {
    exec(http("CUI_DefResponse_010_005_CivilUIHomepage")
      .get(CivilUiURL + "/dashboard")
      .headers(CivilDamagesHeader.MoneyClaimNavHeader)
      .check(CsrfCheck.save)
      .check(status.in(200, 304))
      .check(substring("Sign in or create an account"))
    )
  }
    .pause(MinThinkTime, MaxThinkTime)
  
  
//sign in
    .group("CUI_DefResponse_020_SignIn ") {
      exec(http("CUI_DefResponse_020_005_SignIn")
        .post(IdAMURL + "/login?client_id=civil_citizen_ui&response_type=code&redirect_uri=https://civil-citizen-ui.perftest.platform.hmcts.net/oauth2/callback&scope=profile%20openid%20roles%20manage-user%20create-user%20search-user")
        .headers(CivilDamagesHeader.MoneyClaimSignInHeader)
        .formParam("username", "perftestuser@mailinator.com")
        .formParam("password", "Password12!")
        .formParam("selfRegistrationEnabled", "true")
        .formParam("_csrf", "${csrf}")
        .check(status.in(200, 304))
        .check(substring("Your money claims account"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)

    
    /*======================================================================================
                   * Civil UI Claim - Respond to Claim- Click On Claim
        ==========================================================================================*/
    .group("CUI_DefResponse_030_ClickOnClaimToRespond") {
      exec(http("CUI_DefResponse_030_005_ClickOnClaimToRespond")
        .get(CivilUiURL + "/dashboard/#{caseId}/defendant")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(status.in(200, 304))
        .check(substring("You need to respond before"))
        
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
    /*======================================================================================
               * Civil UI Claim - Respond to Claim
    ==========================================================================================*/

    .group("CUI_DefResponse_040_LanguagePreference") {
      exec(http("CUI_DefResponse_040_005_LanguagePreference")
        .get(CivilUiURL + "/case/#{caseId}/response/bilingual-language-preference")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Do you want to respond to this claim in Welsh?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
             * Civil UI Claim - Do you want to respond to this claim in Welsh?
  ==========================================================================================*/

    .group("CUI_DefResponse_050_InWelshYesNo") {
      exec(http("CUI_DefResponse_050_005_InWelshYesNo")
        .post(CivilUiURL + "/case/#{caseId}/response/bilingual-language-preference")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
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

    .group("CUI_DefResponse_060_ConfirmDefDetails") {
      exec(http("CUI_DefResponse_060_005_ConfirmDefDetails")
        .get(CivilUiURL + "/case/#{caseId}/response/your-details")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Confirm your details"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
         * Civil UI Claim - Confirm Your Details Confirm
==========================================================================================*/

    .group("CUI_DefResponse_070_ConfirmDetailsConfirm") {
      exec(http("CUI_DefResponse_070_005_ConfirmDetailsConfirm")
        .post(CivilUiURL + "/case/#{caseId}/response/your-details")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("individualTitle", "mr")
        .formParam("individualFirstName", "vijaydef")
        .formParam("individualLastName", "vykuntamdef")
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

    .group("CUI_DefResponse_080_DateOfBirth") {
      exec(http("CUI_DefResponse_080_005_DateOfBirth")
        .post(CivilUiURL + "/case/#{caseId}/response/your-dob")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("day", "01")
        .formParam("month", "08")
        .formParam("year", "1983")
        .check(status.in(200, 304))
        .check(substring("Enter a phone number (optional)"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
       * Civil UI Claim - Enter a phone number
==========================================================================================*/

    .group("CUI_DefResponse_090_PhoneNumber") {
      exec(http("CUI_DefResponse_090_005_PhoneNumber")
        .post(CivilUiURL + "/case/#{caseId}/response/your-phone")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("telephoneNumber", "01234567890")
        .check(status.in(200, 304))
        .check(substring("Confirm your details"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
         * Civil UI Claim - View your options before response deadline -task list
==========================================================================================*/

    .group("CUI_DefResponse_100_ViewOptions") {
      exec(http("CUI_DefResponse_100_005_ViewOptions")
        .get(CivilUiURL + "/case/#{caseId}/response/understanding-your-options")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
       // .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Requesting extra time"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
       * Civil UI Claim - Requesting extra time
==========================================================================================*/

    .group("CUI_DefResponse_110_ExtraTime") {
      exec(http("CUI_DefResponse_110_005_ExtraTime")
        .get(CivilUiURL + "/case/#{caseId}/response/response-deadline-options")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Response deadline"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
     * Civil UI Claim - Response deadline - no
==========================================================================================*/

    .group("CUI_DefResponse_120_ResponseDeadline") {
      exec(http("CUI_DefResponse_120_005_ResponseDeadline")
        .post(CivilUiURL + "/case/#{caseId}/response/response-deadline-options")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
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

    .group("CUI_DefResponse_130_ChooseAResponse") {
      exec(http("CUI_DefResponse_130_005_ChooseAResponse")
        .get(CivilUiURL + "/case/#{caseId}/response/response-type")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("How do you respond to the claim?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
     * Civil UI Claim - How do you respond to the claim? - I admit part of the claim
==========================================================================================*/

    .group("CUI_DefResponse_140_ExtraTime") {
      exec(http("CUI_DefResponse_140_005_ExtraTime")
        .post(CivilUiURL + "/case/#{caseId}/response/response-type")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("responseType", "PART_ADMISSION")
        .check(status.in(200, 304))
        .check(CsrfCheck.save)
        .check(substring("Have you paid the claimant the amount you admit you owe?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
     * Civil UI Claim - Have you paid the claimant the amount you admit you owe? - no
==========================================================================================*/

    .group("CUI_DefResponse_150_HaveYouPaid") {
      exec(http("CUI_DefResponse_150_005_HaveYouPaid")
        .post(CivilUiURL + "/case/#{caseId}/response/partial-admission/already-paid")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "no")
        .check(status.in(200, 304))
        .check(substring("Choose a response"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
         * Civil UI Claim - How much money do you admit you owe? - redirect
==========================================================================================*/

    .group("CUI_DefResponse_160_ConfirmDetails") {
      exec(http("CUI_DefResponse_160_005_ConfirmDetails")
        .get(CivilUiURL + "/case/#{caseId}/response/partial-admission/how-much-do-you-owe")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("How much money do you admit you owe?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)

    
    /*======================================================================================
       * Civil UI Claim - How much money do you admit you owe?
==========================================================================================*/

    .group("CUI_DefResponse_170_MoneyOwed") {
      exec(http("CUI_DefResponse_170_005_MoneyOwed")
        .post(CivilUiURL + "/case/#{caseId}/response/partial-admission/how-much-do-you-owe")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("amount", "500")
        .check(status.in(200, 304))
      )
        
    }
    .pause(MinThinkTime, MaxThinkTime)
    


    /*======================================================================================
       * Civil UI Claim - Why do you disagree with the amount claimed? - redirect
==========================================================================================*/

    .group("CUI_DefResponse_180_WhyDisagree") {
      exec(http("CUI_DefResponse_180_005_WhyDisagree")
        .get(CivilUiURL + "/case/#{caseId}/response/partial-admission/why-do-you-disagree")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("This includes the claim fee and any interest."))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
     * Civil UI Claim - Why do you disagree with the claim amount?
==========================================================================================*/

    .group("CUI_DefResponse_190_WhyDisagreeExplain") {
      exec(http("CUI_DefResponse_190_005_WhyDisagreeExplain")
        .post(CivilUiURL + "/case/#{caseId}/response/partial-admission/why-do-you-disagree")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("text", "yes we disagree for perftest purposes")
        .check(substring("Add your timeline of events"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)




    /*======================================================================================
   * Civil UI Claim - Add your timeline of events
==========================================================================================*/

    .group("CUI_DefResponse_200_TimelineOfEvents") {
      exec(http("CUI_DefResponse_200_005_TimelineOfEvents")
        .post(CivilUiURL + "/case/#{caseId}/response/timeline")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("rows[0][day]", "01")
        .formParam("rows[0][month]", "08")
        .formParam("rows[0][year]", "2023")
        .formParam("rows[0][description]", "first description0")
        .formParam("rows[1][day]", "02")
        .formParam("rows[1][month]", "08")
        .formParam("rows[1][year]", "2023")
        .formParam("rows[1][description]", "description1")
        .formParam("comment]", "perftest comment")
        .check(substring("List your evidence"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
* Civil UI Claim - List your evidence
==========================================================================================*/


.group("CUI_DefResponse_210_ListEvidence") {
  exec(http("CUI_DefResponse_210_005_ListEvidence")
  .post(CivilUiURL + "/case/#{caseId}/response/evidence")
  .headers(CivilDamagesHeader.MoneyClaimPostHeader)
  .formParam("_csrf", "#{csrf}")
  .formParam("evidenceItem[0][type]", "Expert witness")
  .formParam("evidenceItem[0][description]]", "timelineDescription Expert Witness")
  .formParam("comment", "comment")
    .check(substring("Why do you disagree with the amount claimed?"))
  )
}
  .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
       * Civil UI Claim - When will you pay? Redirect
==========================================================================================*/

    .group("CUI_DefResponse_220_WhenPay") {
      exec(http("CUI_DefResponse_220_005_WhenPay")
        .get(CivilUiURL + "/case/#{caseId}/response/partial-admission/payment-option")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("When do you want to pay the"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
   * Civil UI Claim - When do you want to pay the?
==========================================================================================*/

    .group("CUI_DefResponse_230_PayDate") {
      exec(http("CUI_DefResponse_230_005_PayDate")
        .post(CivilUiURL + "/case/#{caseId}/response/partial-admission/payment-option")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("paymentType", "IMMEDIATELY")
        .check(substring("When will you pay the £500.00?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
       * Civil UI Claim - Free telephone mediation Redirect
==========================================================================================*/

    .group("CUI_DefResponse_240_FreeTelephone") {
      exec(http("CUI_DefResponse_240_005_FreeTelephone")
        .get(CivilUiURL + "/case/#{caseId}/mediation/free-telephone-mediation")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(status.in(200, 304))
        .check(substring("Free telephone mediation"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
     * Civil UI Claim - Free telephone mediation
==========================================================================================*/

    .group("CUI_DefResponse_250_FreeTelephoneMed") {
      exec(http("CUI_DefResponse_250_005_FreeTelephoneMed")
        .get(CivilUiURL + "/case/#{caseId}/mediation/can-we-use")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Confirm your telephone number"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
 * Civil UI Claim - Confirm your telephone number - yes
==========================================================================================*/

    .group("CUI_DefResponse_260_ConfirmNumber") {
      exec(http("CUI_DefResponse_260_005_ConfirmNumber")
        .post(CivilUiURL + "/case/#{caseId}/mediation/can-we-use")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "yes")
        .formParam("mediationPhoneNumber", "")
        .check(substring("Free telephone mediation"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
     * Civil UI Claim - Give us details in case there's a hearing Redirect
==========================================================================================*/

    .group("CUI_DefResponse_270_GiveDetails") {
      exec(http("CUI_DefResponse_270_005_GiveDetails")
        .get(CivilUiURL + "/case/#{caseId}/directions-questionnaire/determination-without-hearing")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Determination without Hearing Questions"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
     * Civil UI Claim - Determination without Hearing Questions
==========================================================================================*/

    .group("CUI_DefResponse_280_Determination") {
      exec(http("CUI_DefResponse_280_005_Determination")
        .post(CivilUiURL + "/case/#{caseId}/directions-questionnaire/determination-without-hearing")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "yes")
        .formParam("reasonForHearing", "")
        .check(substring("Using an expert"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
   * Civil UI Claim - Using an expert
==========================================================================================*/

    .group("CUI_DefResponse_290_UsingExpert") {
      exec(http("CUI_DefResponse_290_005_UsingExpert")
        .post(CivilUiURL + "/case/#{caseId}/directions-questionnaire/expert")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .check(substring("Do you want to give evidence yourself?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
 * Civil UI Claim - Do you want to give evidence yourself? - yes
==========================================================================================*/

    .group("CUI_DefResponse_300_GiveEvidence") {
      exec(http("CUI_DefResponse_300_005_GiveEvidence")
        .post(CivilUiURL + "/case/#{caseId}/directions-questionnaire/give-evidence-yourself")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "yes")
        .check(substring("Do you have other witnesses?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
* Civil UI Claim - Do you have other witnesses? - no
==========================================================================================*/

    .group("CUI_DefResponse_310_OtherWitnesses") {
      exec(http("CUI_DefResponse_310_005_OtherWitnesses")
        .post(CivilUiURL + "/case/#{caseId}/directions-questionnaire/other-witnesses")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
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

    .group("CUI_DefResponse_320_AnyDates") {
      exec(http("CUI_DefResponse_320_005_AnyDates")
        .post(CivilUiURL + "/case/#{caseId}/directions-questionnaire/cant-attend-hearing-in-next-12-months")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "no")
        .check(substring("Do you want to ask for a telephone or video hearing?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
* Civil UI Claim - Do you want to ask for a telephone or video hearing? - yes
==========================================================================================*/

    .group("CUI_DefResponse_330_AskForTelephone") {
      exec(http("CUI_DefResponse_330_005_AskForTelephone")
        .post(CivilUiURL + "/case/#{caseId}/directions-questionnaire/phone-or-video-hearing")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
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

    .group("CUI_DefResponse_340_Vulnerable") {
      exec(http("CUI_DefResponse_340_005_Vulnerable")
        .post(CivilUiURL + "/case/#{caseId}/directions-questionnaire/vulnerability")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
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

    .group("CUI_DefResponse_350_NeedSupport") {
      exec(http("CUI_DefResponse_350_005_NeedSupport")
        .post(CivilUiURL + "/case/#{caseId}/directions-questionnaire/support-required")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
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

    .group("CUI_DefResponse_360_SpecifcCourt") {
      exec(http("CUI_DefResponse_360_005_SpecifcCourt")
        .post(CivilUiURL + "/case/#{caseId}/directions-questionnaire/court-location")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
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

    .group("CUI_DefResponse_370_WelshLanguage") {
      exec(http("CUI_DefResponse_370_005_WelshLanguage")
        .post(CivilUiURL + "/case/#{caseId}/directions-questionnaire/welsh-language")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
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

    .group("CUI_DefResponse_380_CheckYourAnswers") {
      exec(http("CUI_DefResponse_380_005_CheckYourAnswers")
        .get(CivilUiURL + "/case/#{caseId}/response/check-and-send")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
       // .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Your answers will help us check we are treating people fairly and equally"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
* Civil UI Claim - Check your answers
==========================================================================================*/

    .group("CUI_DefResponse_390_CheckAndSubmit") {
      exec(http("CUI_DefResponse_390_005_CheckYourAnswers")
        .post(CivilUiURL + "/case/#{caseId}/response/check-and-send")
        .headers(CivilDamagesHeader.DefCheckAndSendPost)
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

