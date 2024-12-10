
package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, Common, CsrfCheck, Environment}

import java.io.{BufferedWriter, FileWriter}

object CUIR2ClaimCreationMultiTrack {

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
                 * Civil Citizen - Click on make a new claim
      ==========================================================================================*/
      .group("CUIR2_Claimant_030_ClickOnMakeNewClaim") {
        exec(http("CUIR2_Claimant_030_005_ClickOnMakeNewClaim")
          .get("/eligibility")
          .headers(CivilDamagesHeader.CUIR2Get)
          .check(substring("Try the new online service"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
               * Civil Citizen - Eligibility Questionaire - Know claim amount claiming
    ==========================================================================================*/
      .group("CUIR2_Claimant_040_EligibilityClaimAmount") {
        exec(http("CUIR2_Claimant_040_005_EligibilityClaimAmount")
          .get("/eligibility/known-claim-amount")
          .headers(CivilDamagesHeader.CUIR2Get)
          .check(CsrfCheck.save)
          .check(substring("Do you know the amount you are claiming?")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen - Eligibility Questionaire - Know claim amount claiming post
    ==========================================================================================*/
      .group("CUIR2_Claimant_050_EligibilityClaimAmount") {
        exec(http("CUIR2_Claimant_050_005_EligibilityClaimAmount")
          .post("/eligibility/known-claim-amount")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("option","yes")
          .check(CsrfCheck.save)
          .check(substring("Is this claim against more than one person or organisation?")))
         
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen - Eligibility Questionaire - Single defendant
    ==========================================================================================*/
      .group("CUIR2_Claimant_060_IsSingleDefendant") {
        exec(http("CUIR2_Claimant_060_005_IsSingleDefendant")
          .post("/eligibility/single-defendant")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "no")
          .check(CsrfCheck.save)
          .check(substring("Does the person or organisation you’re claiming against have a postal address in England or Wales?")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen - Eligibility Questionaire - Person belongs to England and Wales
    ==========================================================================================*/
      .group("CUIR2_Claimant_070_DoesDefBelongsEng") {
        exec(http("CUIR2_Claimant_070_005_DoesDefBelongsEng")
          .post("/eligibility/defendant-address")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "yes")
          .check(CsrfCheck.save)
          .check(substring("Who are you making the claim for?")))
      }
      .pause(MinThinkTime, MaxThinkTime)
    
      /*======================================================================================
               * Civil Citizen - Eligibility Questionaire - who are you making the claim
    ==========================================================================================*/
      .group("CUIR2_Claimant_080_WhoYouMakingClaimFor") {
        exec(http("CUIR2_Claimant_080_005_WhoYouMakingClaimFor")
          .post("/eligibility/claim-type")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("claimType", "just-myself")
          .check(CsrfCheck.save)
          .check(substring("Do you have a postal address in England or Wales?")))
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      /*======================================================================================
               * Civil Citizen - Eligibility Questionaire - do you have a postal address
    ==========================================================================================*/
      .group("CUIR2_Claimant_100_DoYouHavePostalAddress") {
        exec(http("CUIR2_Claimant_100_005_DoYouHavePostalAddress")
          .post("/eligibility/claimant-address")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "yes")
          .check(CsrfCheck.save)
          .check(substring("Is your claim for a tenancy deposit?")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen - Eligibility Questionaire - Is this for tenancy deposite
    ==========================================================================================*/
      .group("CUIR2_Claimant_110_TenancyDeposit") {
        exec(http("CUIR2_Claimant_110_005_TenancyDeposit")
          .post("/eligibility/claim-is-for-tenancy-deposit")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "no")
          .check(CsrfCheck.save)
          .check(substring("Are you claiming against a government department?")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen - Eligibility Questionaire - against govt department
    ==========================================================================================*/
      .group("CUIR2_Claimant_120_GovtDept") {
        exec(http("CUIR2_Claimant_120_005_GovtDept")
          .post("/eligibility/government-department")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "no")
          .check(CsrfCheck.save)
          .check(substring("Do you believe the person you’re claiming against is 18 or over?")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen - Eligibility Questionaire - Def age above 18
    ==========================================================================================*/
      .group("CUIR2_Claimant_130_DefAge") {
        exec(http("CUIR2_Claimant_130_005_DefAge")
          .post("/eligibility/defendant-age")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "yes")
          .check(CsrfCheck.save)
          .check(substring("Are you 18 or over?")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
        /*======================================================================================
                 * Civil Citizen - Eligibility Questionaire - Def age above 18
      ==========================================================================================*/
        .group("CUIR2_Claimant_140_Over18") {
          exec(http("CUIR2_Claimant_140_005_Over18")
            .post("/eligibility/over-18")
            .headers(CivilDamagesHeader.CUIR2Post)
            .formParam("_csrf", "#{csrf}")
            .formParam("option", "yes")
            .check(CsrfCheck.save)
            .check(substring("Do you need help paying your court fee?")))
        }
        .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen - Eligibility Questionaire - Need help paying fee
    ==========================================================================================*/
      .group("CUIR2_Claimant_150_HelpPayingFee") {
        exec(http("CUIR2_Claimant_150_005_HelpPayingFee")
          .post("/eligibility/help-with-fees")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "no")
         // .check(CsrfCheck.save)
          .check(substring("You can use this service")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen - Eligibility Questionaire - You can use your service
    ==========================================================================================*/
      .group("CUIR2_Claimant_160_LanguagePreference") {
        exec(http("CUIR2_Claimant_160_005_LanguagePreference")
          .get("/claim/bilingual-language-preference")
          .headers(CivilDamagesHeader.CUIR2Get)
          .check(CsrfCheck.save)
          .check(substring("Language")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  // below some language steps are there need to complete them later
      /*======================================================================================
               * Civil Citizen - Eligibility Questionaire - You can use your service
    ==========================================================================================*/
      .group("CUIR2_Claimant_170_LanguagePreferencePost") {
        exec(http("CUIR2_Claimant_170_005_LanguagePreferencePost")
          .post("/claim/bilingual-language-preference")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "ENGLISH")
          .check(substring("Make a money claim")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      
  // after english below steps are to be continued
  
      /*======================================================================================
               * Civil Citizen - 1 .Consider other options - Resolving the dispute
    ==========================================================================================*/
      .group("CUIR2_Claimant_180_ResolvingDisputeGet") {
        exec(http("CUIR2_Claimant_180_005_ResolvingDisputeGet")
          .get("/claim/resolving-this-dispute")
          .headers(CivilDamagesHeader.CUIR2Get)
          .check(CsrfCheck.save)
          .check(substring("Try to resolve the dispute")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      /*======================================================================================
               * Civil Citizen -  1 .Consider other options - Resolving the dispute
    ==========================================================================================*/
      .group("CUIR2_Claimant_190_ResolvingDisputePost") {
        exec(http("CUIR2_Claimant_190_005_ResolvingDisputePost")
          .post("/claim/resolving-this-dispute")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .check(substring("You have completed 1 of 7 sections")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen - 2.Prepare your claim - Completing your claim
    ==========================================================================================*/
      .group("CUIR2_Claimant_200_CompletingYourClaimGet") {
        exec(http("CUIR2_Claimant_200_005_CompletingYourClaimGet")
          .get("/claim/completing-claim")
          .headers(CivilDamagesHeader.CUIR2Get)
          .check(CsrfCheck.save)
          .check(substring("Get the details right")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen -   2.Prepare your claim - Completing your claim
    ==========================================================================================*/
      .group("CUIR2_Claimant_210_CompletingYourClaimPost") {
        exec(http("CUIR2_Claimant_210_005_CompletingYourClaimPost")
          .post("/claim/completing-claim")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .check(substring("You have completed 2 of 7 sections")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen - 2.Prepare your claim - Your details
    ==========================================================================================*/
      .group("CUIR2_Claimant_220_YourDetails") {
        exec(http("CUIR2_Claimant_220_005_YourDetailsGet")
          .get("/claim/claimant-party-type-selection")
          .headers(CivilDamagesHeader.CUIR2Get)
          .check(CsrfCheck.save)
          .check(substring("About you and this claim")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - Your details
    ==========================================================================================*/
      .group("CUIR2_Claimant_230_YourDetailsPost") {
        exec(http("CUIR2_Claimant_230_005_YourDetailsPost")
          .post("/claim/claimant-party-type-selection")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "INDIVIDUAL")
          .check(CsrfCheck.save)
          .check(substring("Enter your details")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
     
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - Claimant Individual Details
    ==========================================================================================*/
      .group("CUIR2_Claimant_240_ClaimantIndividualDetails") {
        exec(http("CUIR2_Claimant_240_005_ClaimantDetails")
          .post("/claim/claimant-individual-details")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("title", "Mr")
          .formParam("firstName", "Claimant First")
          .formParam("lastName", "Claimant Last")
          .formParam("primaryAddressPostcode","")
          .formParam("addressList", "")
          .formParam("addressLine1", "28 HIBERNIA GARDENS")
          .formParam("addressLine2", "")
          .formParam("addressLine3", "")
          .formParam("city", "HOUNSLOW")
          .formParam("postCode", "TW3 3SD")
          .formParam("provideCorrespondenceAddress", "no")
          .formParam("correspondenceAddressPostcode", "")
          .formParam("addressList", "")
          .formParam("addressLine1", "")
          .formParam("addressLine2", "")
          .formParam("addressLine3", "")
          .formParam("city", "")
          .formParam("postCode", "")
          .check(CsrfCheck.save)
          .check(substring("What is your date of birth?")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - Your details - dob
    ==========================================================================================*/
      .group("CUIR2_Claimant_250_ClaimantDOB") {
        exec(http("CUIR2_Claimant_250_005_ClaimantDOB")
          .post("/claim/claimant-dob")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("day", "01")
          .formParam("month", "08")
          .formParam("year", "1983")
          .formParam("saveAndContinue", "true")
          .check(CsrfCheck.save)
          .check(substring("Enter a phone number")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - Your details - dob
    ==========================================================================================*/
      .group("CUIR2_Claimant_260_ClaimantPhone") {
        exec(http("CUIR2_Claimant_260_005_ClaimantPhone")
          .post("/claim/claimant-phone")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("telephoneNumber", "07234567890")
          .formParam("saveAndContinue", "true")
          .check(substring("You have completed 3 of 7 sections")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen - 2.Prepare your claim - Their details
    ==========================================================================================*/
      .group("CUIR2_Claimant_270_TheirDetailsGet") {
        exec(http("CUIR2_Claimant_270_005_TheirDetailsGet")
          .get("/claim/defendant-party-type-selection")
          .headers(CivilDamagesHeader.CUIR2Get)
          .check(CsrfCheck.save)
          .check(substring("Who are you making the claim against?")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - Your details
    ==========================================================================================*/
      .group("CUIR2_Claimant_280_YourDetailsPost") {
        exec(http("CUIR2_Claimant_280_005_YourDetailsPost")
          .post("/claim/defendant-party-type-selection")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "INDIVIDUAL")
          .check(CsrfCheck.save)
          .check(substring("Enter the defendant’s details")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - Claimant Individual Details
    ==========================================================================================*/
      .group("CUIR2_Claimant_290_DefIndividualDetails") {
        exec(http("CUIR2_Claimant_290_005_DefIndividualDetails")
          .post(CitizenURL + "/claim/defendant-individual-details")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("title", "Mr")
          .formParam("firstName", "Def First")
          .formParam("lastName", "Def Last")
          .formParam("primaryAddressPostcode", "")
          .formParam("addressList", "")
          .formParam("addressLine1", "10, HIBERNIA GARDENS")
          .formParam("addressLine2", "")
          .formParam("addressLine3", "")
          .formParam("city", "HOUNSLOW")
          .formParam("postCode", "TW3 3SD")
          .check(CsrfCheck.save)
          .check(substring("Their email address")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim -their details email address
    ==========================================================================================*/
      .group("CUIR2_Claimant_300_DefEmail") {
        exec(http("CUIR2_Claimant_300_005_DefEmail")
          .post("/claim/defendant-email")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("emailAddress", "civilmoneyclaimsdemo@gmail.com")
          .formParam("saveAndContinue", "true")
          .check(CsrfCheck.save)
          .check(substring("Their phone number")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - Your details - phone number
    ==========================================================================================*/
      .group("CUIR2_Claimant_310_ClaimantPhone") {
        exec(http("CUIR2_Claimant_310_005_ClaimantPhone")
          .post("/claim/defendant-mobile")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("telephoneNumber", "07234567890")
          .formParam("saveAndContinue", "true")
        //  .check(CsrfCheck.save)
          .check(substring("Make a money claim")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  
      /*======================================================================================
               * Civil Citizen - 2.Prepare your claim - claim amount
    ==========================================================================================*/
      .group("CUIR2_Claimant_320_ClaimamountGet") {
        exec(http("CUIR2_Claimant_320_005_ClaimAmountGet")
          .get("/claim/amount")
          .headers(CivilDamagesHeader.CUIR2Get)
          .check(CsrfCheck.save)
          .check(substring("Claim amount")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - claim amount desc
    ==========================================================================================*/
      .group("CUIR2_Claimant_330_ClaimamountPost") {
        exec(http("CUIR2_Claimant_330_005_ClaimAmountPost")
          .post(CitizenURL + "/claim/amount")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("claimAmountRows[0][reason]", "Claim Amount Perftest Desc")
          .formParam("claimAmountRows[0][amount]", "110000")
          .formParam("claimAmountRows[1][reason]", "")
          .formParam("claimAmountRows[1][amount]", "")
          .formParam("claimAmountRows[2][reason]", "")
          .formParam("claimAmountRows[2][amount]", "")
          .formParam("claimAmountRows[3][reason]", "")
          .formParam("claimAmountRows[3][amount]", "")
          .formParam("totalAmount", "110000")
          .formParam("saveAndContinue", "true")
          .check(CsrfCheck.save)
          .check(substring("Do you want to claim interest?")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - do you want to claim interest
    ==========================================================================================*/
      .group("CUIR2_Claimant_340_ClaimInterest") {
        exec(http("CUIR2_Claimant_340_005_ClaimInterest")
          .post("/claim/interest")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "no")
          .formParam("saveAndContinue", "true")
          .check(CsrfCheck.save)
          .check(substring("Do you have a Help With Fees reference number?")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - help with fee
    ==========================================================================================*/
      .group("CUIR2_Claimant_350_HelpWithFee") {
        exec(http("CUIR2_Claimant_350_005_HelpWithFee")
          .post("/claim/help-with-fees")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("referenceNumber", "")
          .formParam("option", "no")
          .formParam("saveAndContinue", "true")
          .check(CsrfCheck.save)
          .check(substring("Total amount you’re claiming")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - Claim Total
    ==========================================================================================*/
      .group("CUIR2_Claimant_360_ClaimTotal") {
        exec(http("CUIR2_Claimant_360_005_ClaimTotal")
          .post("/claim/total")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("saveAndContinue", "true")
         // .check(CsrfCheck.save)
          .check(substring("You have completed 5 of 7 sections")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - Claim details
    ==========================================================================================*/
      .group("CUIR2_Claimant_370_ClaimReasonGet") {
        exec(http("CUIR2_Claimant_370_005_ClaimReason")
          .get("/claim/reason")
          .headers(CivilDamagesHeader.CUIR2Get)
          .check(CsrfCheck.save)
          .check(substring("Briefly explain your claim")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - Claim reason post
    ==========================================================================================*/
      .group("CUIR2_Claimant_380_ClaimReason") {
        exec(http("CUIR2_Claimant_380_005_ClaimReason")
          .post("/claim/reason")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("text", "Perftest Reason")
          .formParam("saveAndContinue", "true")
          .check(CsrfCheck.save)
          .check(substring("Timeline of events")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - Claim events
    ==========================================================================================*/
      .group("CUIR2_Claimant_390_ClaimEvents") {
        exec(http("CUIR2_Claimant_390_005_ClaimEvents")
          .post("/claim/timeline")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("rows[0][day]", "01")
          .formParam("rows[0][month]", "01")
          .formParam("rows[0][year]", "2024")
          .formParam("rows[0][description]", "Perftest Event Description")
          .formParam("rows[1][day]", "")
          .formParam("rows[1][month]","" )
          .formParam("rows[1][year]", "")
          .formParam("rows[1][description]", "")
          .formParam("rows[2][day]", "")
          .formParam("rows[2][month]", "")
          .formParam("rows[2][year]", "")
          .formParam("rows[2][description]", "")
          .formParam("rows[3][day]", "")
          .formParam("rows[3][month]", "")
          .formParam("rows[3][year]", "")
          .formParam("rows[3][description]", "")
          .formParam("saveAndContinue", "true")
          .check(CsrfCheck.save)
          .check(substring("List your evidence")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - Claim evidences
    ==========================================================================================*/
      .group("CUIR2_Claimant_400_ClaimEvidence") {
        exec(http("CUIR2_Claimant_400_005_ClaimEvidence")
          .post("/claim/evidence")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("evidenceItem[0][type]", "Contracts and agreements")
          .formParam("evidenceItem[0][description]", "Perftest Evidences")
          .formParam("evidenceItem[1][type]","")
          .formParam("evidenceItem[1][description]","")
          .formParam("evidenceItem[2][type]","")
          .formParam("evidenceItem[2][description]","")
          .formParam("evidenceItem[3][type]","")
          .formParam("evidenceItem[3][description]","")
          .formParam("saveAndContinue", "true")
          .check(substring("You have completed 6 of 7 sections")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
     
     
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - CheckAndSendGet
    ==========================================================================================*/
     .group("CUIR2_Claimant_410_CheckAndSendGet") {
        exec(http("CUIR2_Claimant_410_005_CheckAndSendGet")
          .get("/claim/check-and-send")
          .headers(CivilDamagesHeader.CUIR2Get)
          .check(CsrfCheck.save)
          .check(substring("Equality and diversity question"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen -  PCQ Questionaire Opt out
    ==========================================================================================*/
      .group("CUIR2_Claimant_420_PCQQuestionaire") {
        exec(http("CUIR2_Claimant_420_005_PCQQuestionaire")
          .post("https://pcq.perftest.platform.hmcts.net/opt-out")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("opt-out-button", "")
          .check(CsrfCheck.save)
          .check(substring("Check your answers")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - Check And Send Post
    ==========================================================================================*/
      .group("CUIR2_Claimant_430_CheckAndSendPost") {
        exec(http("CUIR2_Claimant_430_005_CheckAndSendPost")
          .post("/claim/check-and-send")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("type", "basic")
          .formParam("acceptNoChangesAllowed", "true")
          .formParam("signed", "true")
          .formParam("saveAndContinue", "true")
          .check(substring("Claim submitted"))
          .check(regex("""/claim/(\d+)/fee""").find.saveAs("claimNumber"))
          
         // .check (css (".reference-number>h1.bold-large").saveAs ("claimNumber"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      .pause(30)
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - pay claim fee
    ==========================================================================================*/
      .group("CUIR2_Claimant_440_PayClaimFeeGet") {
        exec(http("CUIR2_Claimant_440_005_PayClaimFee")
          .get(CitizenURL + "/claim/#{claimNumber}/fee")
          .headers(CivilDamagesHeader.CUIR2Get)
          .check(CsrfCheck.save)
          .check(substring("Pay your claim fee")))
      }
      .pause(MinThinkTime, MaxThinkTime)
      .pause(10)
  
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - Continue to pay
    ==========================================================================================*/
      .group("CUIR2_Claimant_450_ContinueToPayPost") {
        exec(http("CUIR2_Claimant_450_005_ContinueToPay")
          .post(CitizenURL + "/claim/#{claimNumber}/fee")
          .headers(CivilDamagesHeader.CUIR2Post)
          .formParam("_csrf", "#{csrf}")
          .formParam("saveAndContinue", "true")
          //.check(CsrfCheck.save)
          .check(substring("Enter card details"))
          .check(css("input[name='csrfToken']", "value").saveAs("_csrfTokenCardDetailPage"))
          .check(css("input[name='chargeId']", "value").saveAs("CardDetailPageChargeId"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
                    * Civil Citizen -  2.Prepare your claim - Enter Card Details
         ==========================================================================================*/
      .group("CUIR2_Claimant_460_CardDetail_SubmitCardDetail") {
        exec(http("CUIR2_Claimant_460_005_CardDetail_SubmitCardDetail")
          .post(paymentURL + "/card_details/#{CardDetailPageChargeId}")
          .formParam("chargeId", "#{CardDetailPageChargeId}")
          .formParam("csrfToken", "#{_csrfTokenCardDetailPage}")
          .formParam("cardNo", "4444333322221111")
          .formParam("expiryMonth", "07")
          .formParam("expiryYear", "26")
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
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
                         * Civil Citizen -  2.Prepare your claim - Enter Card Details
       ==========================================================================================*/
  
      // confirm the card details and submit
      .group("CUIR2_Claimant_470_CardDetail_ConfirmCardDetail") {
        exec(http("CUIR2_Claimant_470_005_CardDetail_ConfirmCardDetail")
          .post(paymentURL + "/card_details/${CardDetailPageChargeId}/confirm")
          .formParam("csrfToken", "#{_csrfTokenCardDetailConfirm}")
          .formParam("chargeId", "#{CardDetailPageChargeId}")
          .check(regex("Your payment was"))
         )
      }
      .pause(MinThinkTime, MaxThinkTime)

     // .exec(_.set("caseId", "#{claimNumber}"))
  
      .exec { session =>
        val fw = new BufferedWriter(new FileWriter("CUIMultiTrackClaimDetails.csv", true))
        try {
          fw.write(session("claimantEmailAddress").as[String] + "," + session("defEmailAddress").as[String] + "," + session("password").as[String] + "," + session("claimNumber").as[String] + "\r\n")
        } finally fw.close()
        session
      }
  
     /* .exec { session =>
        val fw = new BufferedWriter(new FileWriter("CUIDefClaimDetails.csv", true))
        try {
          fw.write(session("claimantEmailAddress").as[String] +  "," + session("defEmailAddress").as[String] +  ","+ session("password").as[String] + "," + session("claimNumber").as[String] + "\r\n")
        } finally fw.close()
        session
      }*/
  
}
