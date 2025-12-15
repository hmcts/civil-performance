
package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.{CivilDamagesHeader, CsrfCheck, Environment}

object CUIR2DefendantResponse {
  
  val BaseURL = Environment.baseURL
  val IdAMURL = Environment.idamURL
  val CivilUiURL = "https://civil-citizen-ui.perftest.platform.hmcts.net"
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val run=
    
    /*======================================================================================
                   * Civil UI Claim - Respond to Claim- Click On Claim
    ======================================================================================*/

    group("CUIR2_DefResponse_030_ClickOnClaimToRespond") {
      exec(http("CUIR2_DefResponse_030_005_ClickOnClaimToRespond")
        .get(CivilUiURL + "/dashboard/#{claimNumber}/defendant")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(status.in(200, 304))
        .check(substring("You need to respond before")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
               * Civil UI Claim - click on Respond to Claim
    ======================================================================================*/

    .group("CUIR2_DefResponse_040_LanguagePreference") {
      exec(http("CUIR2_DefResponse_040_005_LanguagePreference")
        .get(CivilUiURL + "/case/#{claimNumber}/response/bilingual-language-preference")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("do you want to respond to this claim?")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
             * Civil UI Claim - Do you want to respond to this claim in Welsh?
    ======================================================================================*/

    .group("CUIR2_DefResponse_050_InWelshYesNo") {
      exec(http("CUIR2_DefResponse_050_005_InWelshYesNo")
        .post(CivilUiURL + "/case/#{claimNumber}/response/bilingual-language-preference")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "ENGLISH")
        .check(status.in(200, 304))
        .check(substring("Respond to a money claim")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
           * Civil UI Claim - Confirm Your Details Redirect
    ======================================================================================*/

    .group("CUIR2_DefResponse_060_ConfirmDefDetails") {
      exec(http("CUIR2_DefResponse_060_005_ConfirmDefDetails")
        .get(CivilUiURL + "/case/#{claimNumber}/response/your-details")
        .headers(CivilDamagesHeader.CUIR2Get)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Confirm your details")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
         * Civil UI Claim - Confirm Your Details Confirm
    ======================================================================================*/

    .group("CUIR2_DefResponse_070_ConfirmDetailsConfirm") {
      exec(http("CUIR2_DefResponse_070_005_ConfirmDetailsConfirm")
        .post(CivilUiURL + "/case/#{claimNumber}/response/your-details")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("title", "Mr")
        .formParam("firstName", "DFirst")
        .formParam("lastName", "DLast")
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
        .check(substring("Enter your date of birth")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
         * Civil UI Claim - Enter your date of birth
    ======================================================================================*/

    .group("CUIR2_DefResponse_080_DateOfBirth") {
      exec(http("CUIR2_DefResponse_080_005_DateOfBirth")
        .post(CivilUiURL + "/case/#{claimNumber}/response/your-dob")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("day", "01")
        .formParam("month", "08")
        .formParam("year", "1983")
        .check(status.in(200, 304))
        .check(substring("Enter a phone number")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
     * Civil UI Claim - Enter a phone number
    ======================================================================================*/

//    .group("CUIR2_DefResponse_090_PhoneNumber") {
//      exec(http("CUIR2_DefResponse_090_005_PhoneNumber")
//        .post(CivilUiURL + "/case/#{claimNumber}/response/your-phone")
//        .headers(CivilDamagesHeader.CUIR2Post)
//        .formParam("_csrf", "#{csrf}")
//        .formParam("telephoneNumber", "01234567890")
//        .check(status.in(200, 304))
//        .check(substring("Respond to a money claim"))
//      )
//    }
//
//    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
     * Civil UI Claim - View your options before response deadline -task list
    ==========================================================================================*/

    .group("CUIR2_DefResponse_100_ViewOptions") {
      exec(http("CUIR2_DefResponse_100_005_ViewOptions")
        .get(CivilUiURL + "/case/#{claimNumber}/response/understanding-your-options")
        .headers(CivilDamagesHeader.CUIR2Get)
       // .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Requesting extra time")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
       * Civil UI Claim - Requesting extra time
    ==========================================================================================*/

    .group("CUIR2_DefResponse_110_ExtraTime") {
      exec(http("CUIR2_DefResponse_110_005_ExtraTime")
        .get(CivilUiURL + "/case/#{claimNumber}/response/response-deadline-options")
        .headers(CivilDamagesHeader.CUIR2Get)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Response deadline")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
     * Civil UI Claim - Response deadline - no
    ======================================================================================*/

    .group("CUIR2_DefResponse_120_ResponseDeadline") {
      exec(http("CUIR2_DefResponse_120_005_ResponseDeadline")
        .post(CivilUiURL + "/case/#{claimNumber}/response/response-deadline-options")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "no")
        .check(status.in(200, 304))
        .check(substring("View your options before response deadline")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
         * Civil UI Claim - Choose A Response Redirect - task list
    ======================================================================================*/

    .group("CUIR2_DefResponse_130_ChooseAResponse") {
      exec(http("CUIR2_DefResponse_130_005_ChooseAResponse")
        .get(CivilUiURL + "/case/#{claimNumber}/response/response-type")
        .headers(CivilDamagesHeader.CUIR2Get)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("How do you respond to the claim?")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
     * Civil UI Claim - How do you respond to the claim? - I admit part of the claim
    ======================================================================================*/

    .group("CUIR2_DefResponse_140_ResponseType") {
      exec(http("CUIR2_DefResponse_140_005_ResponseType")
        .post(CivilUiURL + "/case/#{claimNumber}/response/response-type")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("responseType", "PART_ADMISSION")
        .check(status.in(200, 304))
        .check(CsrfCheck.save)
        .check(substring("Have you paid the claimant the amount you admit you owe?")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
     * Civil UI Claim - Have you paid the claimant the amount you admit you owe? - no
    ======================================================================================*/

    .group("CUIR2_DefResponse_150_HaveYouPaid") {
      exec(http("CUIR2_DefResponse_150_005_HaveYouPaid")
        .post(CivilUiURL + "/case/#{claimNumber}/response/partial-admission/already-paid")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "no")
        .check(status.in(200, 304))
        .check(substring("Choose a response")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
         * Civil UI Claim - How much money do you admit you owe? - redirect
    ======================================================================================*/

    .group("CUIR2_DefResponse_160_ConfirmDetails") {
      exec(http("CUIR2_DefResponse_160_005_ConfirmDetails")
        .get(CivilUiURL + "/case/#{claimNumber}/response/partial-admission/how-much-do-you-owe")
        .headers(CivilDamagesHeader.CUIR2Get)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("How much money do you admit you owe?")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
       * Civil UI Claim - How much money do you admit you owe?
    ======================================================================================*/

    .group("CUIR2_DefResponse_170_MoneyOwed") {
      exec(http("CUIR2_DefResponse_170_005_MoneyOwed")
        .post(CivilUiURL + "/case/#{claimNumber}/response/partial-admission/how-much-do-you-owe")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("amount", "500")
        .check(status.in(200, 304)))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
       * Civil UI Claim - Why do you disagree with the amount claimed? - redirect
    ======================================================================================*/

    .group("CUIR2_DefResponse_180_WhyDisagree") {
      exec(http("CUIR2_DefResponse_180_005_WhyDisagree")
        .get(CivilUiURL + "/case/#{claimNumber}/response/partial-admission/why-do-you-disagree")
        .headers(CivilDamagesHeader.CUIR2Get)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("The total amount, including any interest claimed to date")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
     * Civil UI Claim - Why do you disagree with the claim amount?
    ======================================================================================*/

    .group("CUIR2_DefResponse_190_WhyDisagreeExplain") {
      exec(http("CUIR2_DefResponse_190_005_WhyDisagreeExplain")
        .post(CivilUiURL + "/case/#{claimNumber}/response/partial-admission/why-do-you-disagree")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("text", "yes we disagree for perftest purposes")
        .check(substring("Add your timeline of events")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
     * Civil UI Claim - Add your timeline of events
    ======================================================================================*/

    .group("CUIR2_DefResponse_200_TimelineOfEvents") {
      exec(http("CUIR2_DefResponse_200_005_TimelineOfEvents")
        .post(CivilUiURL + "/case/#{claimNumber}/response/timeline")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("rows[0][day]", "01")
        .formParam("rows[0][month]", "01")
        .formParam("rows[0][year]", "2024")
        .formParam("rows[0][description]", "first description")
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
        .check(substring("List your evidence")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
    * Civil UI Claim - List your evidence
    ======================================================================================*/


    .group("CUIR2_DefResponse_210_ListEvidence") {
      exec(http("CUIR2_DefResponse_210_005_ListEvidence")
        .post(CivilUiURL + "/case/#{claimNumber}/response/evidence")
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
        .check(substring("Why do you disagree with the amount claimed?")))
    }

  .pause(MinThinkTime, MaxThinkTime)

  /*======================================================================================
    * Civil UI Claim - When will you pay? Redirect
  ======================================================================================*/

    .group("CUIR2_DefResponse_220_WhenPay") {
      exec(http("CUIR2_DefResponse_220_005_WhenPay")
        .get(CivilUiURL + "/case/#{claimNumber}/response/partial-admission/payment-option")
        .headers(CivilDamagesHeader.CUIR2Get)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("When do you want to pay the")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
    * Civil UI Claim - When do you want to pay the?
    ======================================================================================*/

    .group("CUIR2_DefResponse_230_PayDate") {
      exec(http("CUIR2_DefResponse_230_005_PayDate")
        .post(CivilUiURL + "/case/#{claimNumber}/response/partial-admission/payment-option")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("paymentType", "IMMEDIATELY")
        .check(substring("When will you pay the £500.00?")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
     * Civil UI Claim - Free telephone mediation Redirect
    ======================================================================================*/

    .group("CUIR2_DefResponse_240_FreeTelephone") {
      exec(http("CUIR2_DefResponse_240_005_FreeTelephone")
        .get(CivilUiURL + "/case/#{claimNumber}/mediation/telephone-mediation")
        .headers(CivilDamagesHeader.CUIR2Get)
        .check(status.in(200, 304))
        .check(substring("telephone mediation")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
     * Civil UI Claim - Free telephone mediation Redirect
    ======================================================================================*/

    .group("CUIR2_DefResponse_245_FreeTelephonePost") {
      exec(http("CUIR2_DefResponse_245_005_FreeTelephone")
        .post(CivilUiURL + "/case/#{claimNumber}/mediation/telephone-mediation")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .check(status.in(200, 304))
//        .check(substring("telephone mediation"))
      )
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
     * Civil UI Claim - Can the mediator use your phone - yes
    ======================================================================================*/

    .group("CUIR2_DefResponse_250_FreeTelephoneMed") {
      exec(http("CUIR2_DefResponse_250_005_FreeTelephoneMed")
        .post(CivilUiURL + "/case/#{claimNumber}/mediation/phone-confirmation")
        .headers(CivilDamagesHeader.CUIR2Get)
        .check(CsrfCheck.save)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "yes")
        .check(status.in(200, 304))
        .check(substring("Can the mediation team use")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
     * Civil UI Claim - Can the mediator use your email - yes
    ======================================================================================*/

    .group("CUIR2_DefResponse_260_ConfirmNumber") {
      exec(http("CUIR2_DefResponse_260_005_ConfirmNumber")
        .post(CivilUiURL + "/case/#{claimNumber}/mediation/email-confirmation")
        .headers(CivilDamagesHeader.CUIR2Post)
        .check(CsrfCheck.save)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "yes")
        .check(substring("Are there any dates")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
     * Civil UI Claim - Are there any dates in the next 3 months - no
    ======================================================================================*/

    .group("CUIR2_DefResponse_270_GiveDetails") {
      exec(http("CUIR2_DefResponse_270_005_GiveDetails")
        .post(CivilUiURL + "/case/#{claimNumber}/mediation/next-three-months")
        .headers(CivilDamagesHeader.CUIR2Post)
//        .check(CsrfCheck.save)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "no")
        .check(status.in(200, 304))
        .check(substring("Give us details in case")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
     * Civil UI Claim - Determination without Hearing Questions - Redirect
    ======================================================================================*/

    .group("CUIR2_DefResponse_280_Determination") {
      exec(http("CUIR2_DefResponse_280_005_Determination")
        .get(CivilUiURL + "/case/#{claimNumber}/directions-questionnaire/determination-without-hearing")
        .headers(CivilDamagesHeader.CUIR2Get)
        .check(CsrfCheck.save)
        .check(substring("Do you consider that this claim is suitable for determination without a hearing")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
     * Civil UI Claim - Determination without Hearing Questions - yes
    ======================================================================================*/

    .group("CUIR2_DefResponse_285_DeterminationAnswers") {
      exec(http("CUIR2_DefResponse_280_005_Determination")
        .post(CivilUiURL + "/case/#{claimNumber}/directions-questionnaire/determination-without-hearing")
        .headers(CivilDamagesHeader.CUIR2Post)
        .check(CsrfCheck.save)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "yes")
        .formParam("reasonForHearing", "")
        .check(substring("Using an expert")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
    * Civil UI Claim - Using an expert
    ======================================================================================*/

    .group("CUIR2_DefResponse_290_UsingExpert") {
      exec(http("CUIR2_DefResponse_290_005_UsingExpert")
        .post(CivilUiURL + "/case/#{claimNumber}/directions-questionnaire/expert")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .check(substring("Do you want to give evidence yourself?")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
     * Civil UI Claim - Do you want to give evidence yourself? - yes
    ======================================================================================*/

    .group("CUIR2_DefResponse_300_GiveEvidence") {
      exec(http("CUIR2_DefResponse_300_005_GiveEvidence")
        .post(CivilUiURL + "/case/#{claimNumber}/directions-questionnaire/give-evidence-yourself")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "yes")
        .check(substring("Do you have other witnesses?")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
    * Civil UI Claim - Do you have other witnesses? - no
    ======================================================================================*/

    .group("CUIR2_DefResponse_310_OtherWitnesses") {
      exec(http("CUIR2_DefResponse_310_005_OtherWitnesses")
        .post(CivilUiURL + "/case/#{claimNumber}/directions-questionnaire/other-witnesses")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("witnessItems[0][firstName]", "")
        .formParam("witnessItems[0][lastName]", "")
        .formParam("witnessItems[0][email]", "")
        .formParam("witnessItems[0][telephone]", "")
        .formParam("witnessItems[0][details]", "")
        .formParam("option", "no")
        .check(substring("Are there any dates in the next 12 months when you, your experts or witnesses cannot go to a hearing?")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
    * Civil UI Claim - Are there any dates in the next 12 months when you, your experts or witnesses cannot go to a hearing? - no
    ======================================================================================*/

    .group("CUIR2_DefResponse_320_AnyDates") {
      exec(http("CUIR2_DefResponse_320_005_AnyDates")
        .post(CivilUiURL + "/case/#{claimNumber}/directions-questionnaire/cant-attend-hearing-in-next-12-months")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "no")
        .check(substring("Do you want to ask for a telephone or video hearing?")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
    * Civil UI Claim - Do you want to ask for a telephone or video hearing? - yes
    ======================================================================================*/

    .group("CUIR2_DefResponse_330_AskForTelephone") {
      exec(http("CUIR2_DefResponse_330_005_AskForTelephone")
        .post(CivilUiURL + "/case/#{claimNumber}/directions-questionnaire/phone-or-video-hearing")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "yes")
        .formParam("details", "perf details")
        .check(substring("Are you, your experts or witnesses vulnerable in a way that the court needs to consider?")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
    * Civil UI Claim - Are you, your experts or witnesses vulnerable in a way that the court needs to consider? - no
    ======================================================================================*/

    .group("CUIR2_DefResponse_340_Vulnerable") {
      exec(http("CUIR2_DefResponse_340_005_Vulnerable")
        .post(CivilUiURL + "/case/#{claimNumber}/directions-questionnaire/vulnerability")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("vulnerabilityDetails", "")
        .formParam("option", "no")
        .check(substring("Do you, your experts or witnesses need support to attend a hearing?")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
    * Civil UI Claim - Do you, your experts or witnesses need support to attend a hearing? - no
    ==========================================================================================*/

    .group("CUIR2_DefResponse_350_NeedSupport") {
      exec(http("CUIR2_DefResponse_350_005_NeedSupport")
        .post(CivilUiURL + "/case/#{claimNumber}/directions-questionnaire/support-required")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("model[items][0][fullName]", "")
        .formParam("model[items][0][signLanguageInterpreter][content]", "")
        .formParam("model[items][0][languageInterpreter][content]", "")
        .formParam("model[items][0][otherSupport][content]", "")
        .formParam("option", "no")
        .check(substring("You can ask for the hearing to be held at a specific court")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
    * Civil UI Claim - Do you want to ask for the hearing to be held at a specific court?
    ======================================================================================*/

    .group("CUIR2_DefResponse_360_SpecificCourt") {
      exec(http("CUIR2_DefResponse_360_005_SpecificCourt")
        .post(CivilUiURL + "/case/#{claimNumber}/directions-questionnaire/court-location")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("courtLocation", "Horsham County Court And Family Court - The Law Courts, Hurst Road - RH12 2ET")
        .formParam("reason", "perf testing")
        .check(substring("What languages will you")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
    * Civil UI Claim - Welsh language
    ======================================================================================*/

    .group("CUIR2_DefResponse_370_WelshLanguage") {
      exec(http("CUIR2_DefResponse_370_005_WelshLanguage")
        .post(CivilUiURL + "/case/#{claimNumber}/directions-questionnaire/welsh-language")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("speakLanguage", "en")
        .formParam("documentsLanguage", "en")
        .check(substring("Give us details in case there")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
    * Civil UI Claim - Check and submit your response Redirect
    ======================================================================================*/

    .group("CUIR2_DefResponse_380_CheckYourAnswers") {
      exec(http("CUIR2_DefResponse_380_005_CheckYourAnswers")
        .get(CivilUiURL + "/case/#{claimNumber}/response/check-and-send")
        .headers(CivilDamagesHeader.CUIR2Get)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Equality and diversity questions")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    .group("CUIR2_DefResponse_385_PCQQuestionnaire") {
      exec(http("CUIR2_DefResponse_385_005PCQQuestionnaire")
        .post("https://pcq.#{env}.platform.hmcts.net/opt-out")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("opt-out-button", "")
        .check(CsrfCheck.save)
        .check(substring("Check your answers")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
    * Civil UI Claim - Check your answers
    ======================================================================================*/

    .group("CUIR2_DefResponse_390_CheckAndSubmit") {
      exec(http("CUIR2_DefResponse_390_005_CheckYourAnswers")
        .post(CivilUiURL + "/case/#{claimNumber}/response/check-and-send")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("type", "basic")
        .formParam("isFullAmountRejected", "true")
        .formParam("signed", "true")
        .formParam("directionsQuestionnaireSigned", "true")
        .check(substring("submitted your response")))
    }

    .pause(MinThinkTime, MaxThinkTime)
}

