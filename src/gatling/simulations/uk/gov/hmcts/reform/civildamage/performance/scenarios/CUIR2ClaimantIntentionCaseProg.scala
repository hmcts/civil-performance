
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
          .get(CitizenURL + "/dashboard/#{claimNumber}/claimant")
          .headers(CivilDamagesHeader.CUIR2Get)
          .check(substring("Response to the claim"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)

      /*======================================================================================
                 * Civil Citizen - View Def Response
      ==========================================================================================*/
      .group("CUIR2_ClaimantIntention_040_ViewDefResponse") {
        exec(http("CUIR2_ClaimantIntention_040_005_ViewDefResponse")
          .get("/case/#{claimNumber}/claimant-response/defendants-response")
          .headers(CivilDamagesHeader.CUIR2Get)
          .check(CsrfCheck.save)
          .check(substring("The defendant’s response")))
      }
      .pause(MinThinkTime, MaxThinkTime)

      /*======================================================================================
                     * Civil Citizen - View Def response Continue post
          ==========================================================================================*/
      .group("CUIR2_ClaimantIntention_050_HowtheywanttoPay") {
        exec(http("CUIR2_ClaimantIntention_050_005_HowtheywantToPay")
          .post(CitizenURL + "/case/#{claimNumber}/claimant-response/defendants-response?page=how-they-want-to-pay-response")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
         // .check(CsrfCheck.save)
          .check(substring("You have completed 1")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
                     * Civil UI Claim - Click On View And Respond from Claimant
          ==========================================================================================*/
//      .group("CUIR2_ClaimantIntention_060_ClickOnViewAndRespond") {
//        exec(http("CUIR2_ClaimantIntention_060_005_ClickOnViewAndRespond")
//          .get(CitizenURL + "/case/#{claimNumber}/claimant-response/task-list")
//          .headers(CivilDamagesHeader.MoneyClaimNavHeader)
//          .check(status.in(200, 304))
//          .check(substring("Your response"))
//        )
//      }
//      .pause(MinThinkTime, MaxThinkTime)

      /*======================================================================================
                 * Civil Citizen - Decide whether to proceed
      ==========================================================================================*/
      .group("CUIR2_ClaimantIntention_070_AcceptOrRejectMoney") {
        exec(http("CUIR2_ClaimantIntention_070_005_AcceptOrRejectMoney")
          .get("/case/#{claimNumber}/claimant-response/intention-to-proceed")
          .headers(CivilDamagesHeader.CUIR2Get)
          .check(CsrfCheck.save)
          .check(substring("Do you want to proceed with claim?")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  
      /*======================================================================================
                 * Civil Citizen - Do you want to proceed with the claim - POST
      ==========================================================================================*/
      .group("CUIR2_ClaimantIntention_075_DoYouProceedWithClaim") {
        exec(http("CUIR2_ClaimantIntention_075_005_DoYouProceedWithClaim")
          .post("/case/#{claimNumber}/claimant-response/intention-to-proceed")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "yes")
          .check(substring("You have completed 2")))
      }
      .pause(MinThinkTime, MaxThinkTime)

      /*======================================================================================
                       * Civil Citizen - Telephone mediation
      ==========================================================================================*/
      .group("CUIR2_ClaimantIntention_080_TelephoneMediation") {
        exec(http("CUIR2_ClaimantIntention_080_005_TelephoneMediation")
          .get("/case/#{claimNumber}/mediation/telephone-mediation")
          .headers(CivilDamagesHeader.CUIR2Get)
          .check(CsrfCheck.save)
          .check(substring("Telephone mediation")))
      }
      .pause(MinThinkTime, MaxThinkTime)

      .group("CUIR2_ClaimantIntention_090_MediationContinue") {
        exec(http("CUIR2_ClaimantIntention_090_005_MediationContinue")
          .post("/case/#{claimNumber}/mediation/telephone-mediation")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .check(substring("You have completed 3")))
      }
      .pause(MinThinkTime, MaxThinkTime)

      /*======================================================================================
                       * Civil Citizen - Availability for mediation
      ==========================================================================================*/
      .group("CUIR2_ClaimantIntention_100_MediationAvailability") {
        exec(http("CUIR2_ClaimantIntention_100_005_MediationAvailability")
          .get("/case/#{claimNumber}/mediation/phone-confirmation")
          .headers(CivilDamagesHeader.CUIR2Get)
          .check(CsrfCheck.save)
          .check(substring("Can the mediator use")))
      }
      .pause(MinThinkTime, MaxThinkTime)

      .group("CUIR2_ClaimantIntention_110_PhoneConfirmation") {
        exec(http("CUIR2_ClaimantIntention_110_005_PhoneConfirmation")
          .post("/case/#{claimNumber}/mediation/phone-confirmation")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "yes")
          .check(CsrfCheck.save)
          .check(substring("Can the mediation team use")))
      }
      .pause(MinThinkTime, MaxThinkTime)

      .group("CUIR2_ClaimantIntention_120_EmailConfirmation") {
        exec(http("CUIR2_ClaimantIntention_120_005_EmailConfirmation")
          .post("/case/#{claimNumber}/mediation/email-confirmation")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "yes")
          .check(CsrfCheck.save)
          .check(substring("Are there any dates")))
      }
      .pause(MinThinkTime, MaxThinkTime)

      .group("CUIR2_ClaimantIntention_130_UnavailableDates") {
        exec(http("CUIR2_ClaimantIntention_130_005_UnavailableDates")
          .post("/case/#{claimNumber}/mediation/next-three-months")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "no")
          .check(substring("You have completed 4")))
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
       * Civil UI Claim - Give us details in case there's a hearing Redirect
  ==========================================================================================*/
  
      .group("CUIR2_ClaimantIntention_140_GiveDetails") {
        exec(http("CUIR2_ClaimantIntention_140_005_GiveDetails")
          .get(CitizenURL + "/case/#{claimNumber}/directions-questionnaire/determination-without-hearing")
          .headers(CivilDamagesHeader.CUIR2Get)
          .check(CsrfCheck.save)
          .check(substring("Determination without Hearing Questions"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)

      /*======================================================================================
       * Civil UI Claim - Determination without Hearing Questions
  ==========================================================================================*/

      .group("CUIR2_ClaimantIntention_150_Determination") {
        exec(http("CUIR2_ClaimantIntention_150_005_Determination")
          .post(CitizenURL + "/case/#{claimNumber}/directions-questionnaire/determination-without-hearing")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "no")
          .formParam("reasonForHearing", "asasasasas")
          .check(CsrfCheck.save)
          .check(substring("Using an expert"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
     * Civil UI Claim - Using an expert
  ==========================================================================================*/

      .group("CUIR2_ClaimantIntention_160_UsingExpert") {
        exec(http("CUIR2_ClaimantIntention_160_005_UsingExpert")
          .post(CitizenURL + "/case/#{claimNumber}/directions-questionnaire/expert")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .check(CsrfCheck.save)
          .check(substring("Do you want to give evidence yourself?"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
   * Civil UI Claim - Do you want to give evidence yourself? - yes
  ==========================================================================================*/

      .group("CUIR2_ClaimantIntention_170_GiveEvidence") {
        exec(http("CUIR2_ClaimantIntention_170_005_GiveEvidence")
          .post(CitizenURL + "/case/#{claimNumber}/directions-questionnaire/give-evidence-yourself")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "yes")
          .check(CsrfCheck.save)
          .check(substring("Confirm your details"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
  * Civil UI Claim - Confirm your details
  ==========================================================================================*/

      .group("CUIR2_ClaimantIntention_180_ConfirmDetails") {
        exec(http("CUIR2_ClaimantIntention_180_005_ConfirmDetails")
          .post(CitizenURL + "/case/#{claimNumber}/directions-questionnaire/confirm-your-details")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("firstName", "Second")
          .formParam("lastName", "Third")
          .formParam("emailAddress", "")
          .formParam("phoneNumber", "")
          .formParam("jobTitle", "Title")
          .check(CsrfCheck.save)
          .check(substring("Do you have other witnesses?"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
  * Civil UI Claim - Do you have other witnesses? - no
  ==========================================================================================*/

      .group("CUIR2_ClaimantIntention_190_OtherWitnesses") {
        exec(http("CUIR2_ClaimantIntention_190_005_OtherWitnesses")
          .post(CitizenURL + "/case/#{claimNumber}/directions-questionnaire/other-witnesses")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("witnessItems%5B0%5D%5BfirstName%5D", "")
          .formParam("witnessItems%5B0%5D%5BlastName%5D", "")
          .formParam("witnessItems%5B0%5D%5Bemail%5D", "")
          .formParam("witnessItems%5B0%5D%5Btelephone%5D", "")
          .formParam("witnessItems%5B0%5D%5Bdetails%5D", "")
          .formParam("option", "no")
          .check(CsrfCheck.save)
          .check(substring("Are there any dates in the next"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
  * Civil UI Claim - Are there any dates in the next 12 months when you, your experts or witnesses cannot go to a hearing? - no
  ==========================================================================================*/

      .group("CUIR2_ClaimantIntention_200_AnyDates") {
        exec(http("CUIR2_ClaimantIntention_200_005_AnyDates")
          .post(CitizenURL + "/case/#{claimNumber}/directions-questionnaire/cant-attend-hearing-in-next-12-months")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "no")
          .check(CsrfCheck.save)
          .check(substring("Do you want to ask for a telephone or video hearing?"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
  * Civil UI Claim - Do you want to ask for a telephone or video hearing? - yes
  ==========================================================================================*/

      .group("CUIR2_ClaimantIntention_210_AskForTelephone") {
        exec(http("CUIR2_ClaimantIntention_210_005_AskForTelephone")
          .post(CitizenURL + "/case/#{claimNumber}/directions-questionnaire/phone-or-video-hearing")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "yes")
          .formParam("details", "perf details")
          .check(CsrfCheck.save)
          .check(substring("Are you, your experts or witnesses vulnerable in a way that the court needs to consider?"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
  * Civil UI Claim - Are you, your experts or witnesses vulnerable in a way that the court needs to consider? - no
  ==========================================================================================*/

      .group("CUIR2_ClaimantIntention_220_Vulnerable") {
        exec(http("CUIR2_ClaimantIntention_220_005_Vulnerable")
          .post(CitizenURL + "/case/#{claimNumber}/directions-questionnaire/vulnerability")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("vulnerabilityDetails", "")
          .formParam("option", "no")
          .check(CsrfCheck.save)
          .check(substring("Do you, your experts or witnesses need support to attend a hearing?"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
  * Civil UI Claim - Do you, your experts or witnesses need support to attend a hearing? - no
  ==========================================================================================*/

      .group("CUIR2_ClaimantIntention_230_NeedSupport") {
        exec(http("CUIR2_ClaimantIntention_230_005_NeedSupport")
          .post(CitizenURL + "/case/#{claimNumber}/directions-questionnaire/support-required")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("model%5Bitems%5D%5B0%5D%5BfullName%5D", "")
          .formParam("model%5Bitems%5D%5B0%5D%5BsignLanguageInterpreter%5D%5Bcontent%5D", "")
          .formParam("model%5Bitems%5D%5B0%5D%5BlanguageInterpreter%5D%5Bcontent%5D", "")
          .formParam("model%5Bitems%5D%5B0%5D%5BotherSupport%5D%5Bcontent%5D", "")
          .formParam("option", "no")
          .check(CsrfCheck.save)
          .check(substring("Please select your preferred court"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
  * Civil UI Claim - Do you want to ask for the hearing to be held at a specific court? - no
  ==========================================================================================*/

      .group("CUIR2_ClaimantIntention_240_SpecifcCourt") {
        exec(http("CUIR2_ClaimantIntention_240_005_SpecifcCourt")
          .post(CitizenURL + "/case/#{claimNumber}/directions-questionnaire/court-location")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("courtLocation", "Birmingham Civil and Family Justice Centre - Priory Courts, 33 Bull Street - B4 6DS")
          .formParam("reason", "no")
          .check(CsrfCheck.save)
          .check(substring("Welsh language"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
  * Civil UI Claim - Welsh language
  ==========================================================================================*/

      .group("CUIR2_ClaimantIntention_250_WelshLanguage") {
        exec(http("CUIR2_ClaimantIntention_250_005_WelshLanguage")
          .post(CitizenURL + "/case/#{claimNumber}/directions-questionnaire/welsh-language")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("speakLanguage", "en")
          .formParam("documentsLanguage", "en")
          .check(substring("You have completed 5"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
     * Civil UI Claim - Check and submit your response to Defendant
    ==========================================================================================*/
  
      .group("CUIR2_ClaimantIntention_260_CheckYourAnswers") {
        exec(http("CUIR2_ClaimantIntention_260_005_CheckYourAnswers")
          .get(CitizenURL + "/case/#{claimNumber}/claimant-response/check-and-send")
          .headers(CivilDamagesHeader.CUIR2Get)
          .check(CsrfCheck.save)
          .check(substring("Do you want to proceed with the claim?"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      /*======================================================================================
    * Civil UI Claim - Check your answers
    ==========================================================================================*/
  
      .group("CUIR2_ClaimantIntention_270_CheckAndSubmit") {
        exec(http("CUIR2_ClaimantIntention_270_005_CheckYourAnswers")
          .post(CitizenURL + "/case/#{claimNumber}/claimant-response/check-and-send")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("type", "basic")
          .formParam("isClaimantRejectedDefendantOffer", "false")
          .check(substring("You've rejected their response"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
}
