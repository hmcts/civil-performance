
package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, Common, CsrfCheck, Environment}

object CUIR2ClaimCreation {

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
                 * Civil Citizen - Eligibility Questionaire
      ==========================================================================================*/
      .group("Civil_Citizen_040_Eligibility") {
        exec(http("Civil_Citizen_040_005_Eligibility")
          .get ("/eligibility")
          .headers(CivilDamagesHeader.CivilCitizenPost)
         // .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
          .check(CsrfCheck.save)
          .check(substring("Try the new online service")))
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
               * Civil Citizen - Eligibility Questionaire - Know claim amount claiming
    ==========================================================================================*/
      .group("Civil_Citizen_050_EligibilityClaimAmount") {
        exec(http("Civil_Citizen_050_005_EligibilityClaimAmount")
          .get(CitizenURL + "/eligibility/known-claim-amount")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .check(CsrfCheck.save)
          .check(substring("Do you know the amount you are claiming?")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen - Eligibility Questionaire - Know claim amount claiming post
    ==========================================================================================*/
      .group("Civil_Citizen_050_EligibilityClaimAmount") {
        exec(http("Civil_Citizen_050_005_EligibilityClaimAmount")
          .post(CitizenURL + "/eligibility/known-claim-amount")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("option","yes")
          .check(CsrfCheck.save)
          .check(substring("Is this claim against more than one person or organisation?")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen - Eligibility Questionaire - Single defendant
    ==========================================================================================*/
      .group("Civil_Citizen_050_IsSingleDefendant") {
        exec(http("Civil_Citizen_050_005_IsSingleDefendant")
          .post(CitizenURL + "/eligibility/single-defendant")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "no")
          .check(CsrfCheck.save)
          .check(substring("Does the person or organisation you’re claiming against have a postal address in England or Wales?")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen - Eligibility Questionaire - Person belongs to England and Wales
    ==========================================================================================*/
      .group("Civil_Citizen_050_DoesDefBelongsEng") {
        exec(http("Civil_Citizen_050_005_DoesDefBelongsEng")
          .post(CitizenURL + "/eligibility/defendant-address")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "no")
          .check(CsrfCheck.save)
          .check(substring("Does the person or organisation you’re claiming against have a postal address in England or Wales?")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      /*======================================================================================
               * Civil Citizen - Eligibility Questionaire - who are you makig the claim
    ==========================================================================================*/
      .group("Civil_Citizen_050_WhoYouMakingClaimFor") {
        exec(http("Civil_Citizen_050_005_WhoYouMakingClaimFor")
          .post(CitizenURL + "/eligibility/defendant-address")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "no")
          .check(CsrfCheck.save)
          .check(substring("Who are you making the claim for?")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen - Eligibility Questionaire - Claim type
    ==========================================================================================*/
      .group("Civil_Citizen_050_ClaimType") {
        exec(http("Civil_Citizen_050_005_ClaimType")
          .post(CitizenURL + "/eligibility/claim-type")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("claimType", "just-myself")
          .check(CsrfCheck.save)
          .check(substring("Do you have a postal address in the UK?")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen - Eligibility Questionaire - do you have a postal address
    ==========================================================================================*/
      .group("Civil_Citizen_050_DoYouHavePostalSddress") {
        exec(http("Civil_Citizen_050_005_DoYouHavePostalAddress")
          .post(CitizenURL + "/eligibility/claimant-address")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "yes")
          .check(CsrfCheck.save)
          .check(substring("Is your claim for a tenancy deposit?")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen - Eligibility Questionaire - Is this for tenancy deposite
    ==========================================================================================*/
      .group("Civil_Citizen_050_TenancyDeposit") {
        exec(http("Civil_Citizen_050_005_TenancyDeposit")
          .post(CitizenURL + "/eligibility/claim-is-for-tenancy-deposit")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "no")
          .check(CsrfCheck.save)
          .check(substring("Are you claiming against a government department?")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen - Eligibility Questionaire - against govt department
    ==========================================================================================*/
      .group("Civil_Citizen_050_GovtDept") {
        exec(http("Civil_Citizen_050_005_GovtDept")
          .post(CitizenURL + "/eligibility/government-department")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "no")
          .check(CsrfCheck.save)
          .check(substring("Do you believe the person you’re claiming against is 18 or over?")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen - Eligibility Questionaire - Def age above 18
    ==========================================================================================*/
      .group("Civil_Citizen_050_DefAge") {
        exec(http("Civil_Citizen_050_005_DefAge")
          .post(CitizenURL + "/eligibility/defendant-age")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "yes")
          .check(CsrfCheck.save)
          .check(substring("Are you 18 or over?")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
        /*======================================================================================
                 * Civil Citizen - Eligibility Questionaire - Def age above 18
      ==========================================================================================*/
        .group("Civil_Citizen_050_Over18") {
          exec(http("Civil_Citizen_050_005_Over18")
            .post(CitizenURL + "/eligibility/over-18")
            .headers(CivilDamagesHeader.CivilCitizenPost)
            .formParam("_csrf", "#{csrf}")
            .formParam("option", "yes")
            .check(CsrfCheck.save)
            .check(substring("Do you need help paying your court fee?")))
        }
        .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen - Eligibility Questionaire - Need help paying fee
    ==========================================================================================*/
      .group("Civil_Citizen_050_HelpPayingFee") {
        exec(http("Civil_Citizen_050_005_HelpPayingFee")
          .post(CitizenURL + "/eligibility/help-with-fees")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "no")
          .check(CsrfCheck.save)
          .check(substring("You can use this service")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen - Eligibility Questionaire - You can use your service
    ==========================================================================================*/
      .group("Civil_Citizen_050_HelpPayingFee") {
        exec(http("Civil_Citizen_050_005_HelpPayingFee")
          .post(CitizenURL + "/eligibility/help-with-fees")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "no")
          .check(CsrfCheck.save)
          .check(substring("You can use this service")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  // below some language steps are there need to complete them later
      /*======================================================================================
               * Civil Citizen - Eligibility Questionaire - You can use your service
    ==========================================================================================*/
      .group("Civil_Citizen_050_HelpPayingFee") {
        exec(http("Civil_Citizen_050_005_HelpPayingFee")
          .post(CitizenURL + "/eligibility/help-with-fees")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("option", "no")
          .check(CsrfCheck.save)
          .check(substring("You can use this service")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      
  // after english below steps are to be continued
  
      /*======================================================================================
               * Civil Citizen - 1 .Consider other options - Resolving the dispute
    ==========================================================================================*/
      .group("Civil_Citizen_050_RelvingDisputeGet") {
        exec(http("Civil_Citizen_050_005_ResolvingDisputeGet")
          .get(CitizenURL + "/claim/resolving-this-dispute")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .check(CsrfCheck.save)
          .check(substring("Try to resolve the dispute")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      /*======================================================================================
               * Civil Citizen -  1 .Consider other options - Resolving the dispute
    ==========================================================================================*/
      .group("Civil_Citizen_050_RelvingDisputePost") {
        exec(http("Civil_Citizen_050_005_RelvingDisputePost")
          .post(CitizenURL + "/claim/resolving-this-dispute")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .check(substring("You have completed 1 of 7 sections")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen - 2.Prepare your claim - Completing your claim
    ==========================================================================================*/
      .group("Civil_Citizen_050_CompletingYourClaimGet") {
        exec(http("Civil_Citizen_050_005_CompletingYourClaimGet")
          .get(CitizenURL + "/claim/completing-claim")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .check(CsrfCheck.save)
          .check(substring("Get the details right")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen -   2.Prepare your claim - Completing your claim
    ==========================================================================================*/
      .group("Civil_Citizen_050_CompletingYourClaimPost") {
        exec(http("Civil_Citizen_050_005_CompletingYourClaimPost")
          .post(CitizenURL + "/claim/completing-claim")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .check(substring("You have completed 2 of 7 sections")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen - 2.Prepare your claim - Your details
    ==========================================================================================*/
      .group("Civil_Citizen_050_YourDetails") {
        exec(http("Civil_Citizen_050_005_YourDetailsGet")
          .get(CitizenURL + "/claim/claimant-party-type-selection")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .check(CsrfCheck.save)
          .check(substring("About you and this claim")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - Your details
    ==========================================================================================*/
      .group("Civil_Citizen_050_YourDetailsPost") {
        exec(http("Civil_Citizen_050_005_YourDetailsPost")
          .post(CitizenURL + "/claim/claimant-party-type-selection")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .check(substring("Enter your details")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
     
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - Claimant Individual Details
    ==========================================================================================*/
      .group("Civil_Citizen_050_ClaimantIndividualDetails") {
        exec(http("Civil_Citizen_050_005_ClaimantIndividualDetails")
          .post(CitizenURL + "/claim/claimant-individual-details")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("title", "Mr")
          .formParam("firstName", "Claimant First")
          .formParam("lastName", "Claimant Last")
          .formParam("primaryAddressPostcode", "TW3 3SD")
          .formParam("addressList", "25434301")
          .formParam("addressLine1", "36, HIBERNIA GARDENS")
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
      .group("Civil_Citizen_050_ClaimantDOB") {
        exec(http("Civil_Citizen_050_005_ClaimantDOB")
          .post(CitizenURL + "/claim/claimant-dob")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("day", "01")
          .formParam("month", "08")
          .formParam("year", "1983")
          .formParam("saveAndContinue", "true")
          .check(substring("Enter a phone number")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - Your details - dob
    ==========================================================================================*/
      .group("Civil_Citizen_050_ClaimantPhone") {
        exec(http("Civil_Citizen_050_005_ClaimantPhone")
          .post(CitizenURL + "/claim/claimant-phone")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("day", "01")
          .formParam("month", "08")
          .formParam("year", "1983")
          .formParam("saveAndContinue", "true")
          .check(CsrfCheck.save)
          .check(substring("You have completed 3 of 7 sections")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      
      
  
  
      /*======================================================================================
               * Civil Citizen - 2.Prepare your claim - Their details
    ==========================================================================================*/
      .group("Civil_Citizen_050_TheirDetailsGet") {
        exec(http("Civil_Citizen_050_005_TheirDetailsGet")
          .get(CitizenURL + "/claim/defendant-party-type-selection")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .check(CsrfCheck.save)
          .check(substring("Who are you making the claim against?")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - Your details
    ==========================================================================================*/
      .group("Civil_Citizen_050_YourDetailsPost") {
        exec(http("Civil_Citizen_050_005_YourDetailsPost")
          .post(CitizenURL + "/claim/defendant-party-type-selection")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .check(CsrfCheck.save)
          .check(substring("Enter the defendant’s details")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - Claimant Individual Details
    ==========================================================================================*/
      .group("Civil_Citizen_050_ClaimantIndividualDetails") {
        exec(http("Civil_Citizen_050_005_ClaimantIndividualDetails")
          .post(CitizenURL + "/claim/claimant-individual-details")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("title", "Mr")
          .formParam("firstName", "Def First")
          .formParam("lastName", "Def Last")
          .formParam("primaryAddressPostcode", "TW3 3SD")
          .formParam("addressList", "25434287")
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
      .group("Civil_Citizen_050_DefEmail") {
        exec(http("Civil_Citizen_050_005_DefEmail")
          .post(CitizenURL + "/claim/defendant-email")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("emailAddress", "civilmoneyclaimsdemo@gmail.com")
          .formParam("saveAndContinue", "true")
          .check(substring("Enter a phone number")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - Your details - phone number
    ==========================================================================================*/
      .group("Civil_Citizen_050_ClaimantPhone") {
        exec(http("Civil_Citizen_050_005_ClaimantPhone")
          .post(CitizenURL + "/claim/defendant-mobile")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("telephoneNumber", "07234567890")
          .formParam("saveAndContinue", "true")
          .check(CsrfCheck.save)
          .check(substring("Make a money claim")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  
      /*======================================================================================
               * Civil Citizen - 2.Prepare your claim - claim amount
    ==========================================================================================*/
      .group("Civil_Citizen_050_ClaimamountGet") {
        exec(http("Civil_Citizen_050_005_ClaimAmountGet")
          .get(CitizenURL + "/claim/amount")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .check(CsrfCheck.save)
          .check(substring("Claim amount")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - claim amount desc
    ==========================================================================================*/
      .group("Civil_Citizen_050_ClaimamountPost") {
        exec(http("Civil_Citizen_050_005_ClaimAmountPost")
          .post(CitizenURL + "/claim/amount")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("claimAmountRows[0][reason]", "Claim Amount Perftest Desc")
          .formParam("claimAmountRows[0][amount]", "9000")
          .formParam("claimAmountRows[1][reason]", "")
          .formParam("claimAmountRows[1][amount]", "")
          .formParam("claimAmountRows[2][reason]", "")
          .formParam("claimAmountRows[2][amount]", "")
          .formParam("claimAmountRows[2][reason]", "")
          .formParam("claimAmountRows[2][amount]", "")
          .formParam("totalAmount", "9000")
          .formParam("saveAndContinue", "true")
          .check(CsrfCheck.save)
          .check(substring("Do you want to claim interest?")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - do you want to claim interest
    ==========================================================================================*/
      .group("Civil_Citizen_050_ClaimInterest") {
        exec(http("Civil_Citizen_050_00_ClaimInterest")
          .post(CitizenURL + "/claim/interest")
          .headers(CivilDamagesHeader.CivilCitizenPost)
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
      .group("Civil_Citizen_050_HelpWithFee") {
        exec(http("Civil_Citizen_050_00_HelpWithFee")
          .post(CitizenURL + "/claim/help-with-fees")
          .headers(CivilDamagesHeader.CivilCitizenPost)
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
      .group("Civil_Citizen_050_ClaimTotal") {
        exec(http("Civil_Citizen_050_00_ClaimTotal")
          .post(CitizenURL + "/claim/total")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("saveAndContinue", "true")
          .check(CsrfCheck.save)
          .check(substring("You have completed 5 of 7 sections")))
       /* .exec(http("Civil_Citizen_050_00_TaskList")
          .get(CitizenURL + "//claim/task-list")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("saveAndContinue", "true")
          .check(CsrfCheck.save)
          .check(substring("You have completed 5 of 7 sections")))*/
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - Claim details
    ==========================================================================================*/
      .group("Civil_Citizen_050_ClaimReasonGet") {
        exec(http("Civil_Citizen_050_00_ClaimReason")
          .get(CitizenURL + "/claim/reason")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .check(CsrfCheck.save)
          .check(substring("Briefly explain your claim")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - Claim reason post
    ==========================================================================================*/
      .group("Civil_Citizen_050_ClaimReason") {
        exec(http("Civil_Citizen_050_00_ClaimReason")
          .post(CitizenURL + "/claim/reason")
          .headers(CivilDamagesHeader.CivilCitizenPost)
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
      .group("Civil_Citizen_050_ClaimEvents") {
        exec(http("Civil_Citizen_050_00_ClaimEvents")
          .post(CitizenURL + "/claim/timeline")
          .headers(CivilDamagesHeader.CivilCitizenPost)
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
      .group("Civil_Citizen_050_ClaimEvidence") {
        exec(http("Civil_Citizen_050_00_ClaimEvidence")
          .post(CitizenURL + "/claim/evidence")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("evidenceItem[0][type]: ", "Contracts and agreements")
          .formParam("evidenceItem[0][description]", "Perftest Evidences")
          .formParam("evidenceItem[1][type]: ", "")
          .formParam("evidenceItem[1][description]", "")
          .formParam("evidenceItem[2][type]: ", "")
          .formParam("evidenceItem[2][description]", "")
          .formParam("evidenceItem[3][type]: ", "")
          .formParam("evidenceItem[3][description]", "")
          .formParam("saveAndContinue", "true")
          .check(CsrfCheck.save)
          .check(substring("You have completed 6 of 7 sections")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - CheckAndSendGet
    ==========================================================================================*/
      .group("Civil_Citizen_050_CheckAndSendGet") {
        exec(http("Civil_Citizen_050_00_CheckAndSendGet")
          .get(CitizenURL + "/claim/check-and-send")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .check(CsrfCheck.save)
          .check(substring("Check your answers")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - Check And Send Post
    ==========================================================================================*/
      .group("Civil_Citizen_050_CheckAndSendPost") {
        exec(http("Civil_Citizen_050_00_CheckAndSendPost")
          .post(CitizenURL + "/claim/check-and-send")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("type", "basic")
          .formParam("acceptNoChangesAllowed", "true")
          .formParam("signed:", "true")
          .formParam("saveAndContinue", "true")
          .check(CsrfCheck.save)
          .check(substring("Claim submitted"))
          .check(regex("""href="https://www\.smartsurvey\.co\.uk/s/CMC_ExitSurvey_Claimant/\?pageUrl=/claim/(\d+)/confirmation"""")
              .saveAs("extractedValue")
          )
          .check (css (".reference-number>h1.bold-large").saveAs ("claimNumber"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - pay claim fee
    ==========================================================================================*/
      .group("Civil_Citizen_050_PayClaimFeeGet") {
        exec(http("Civil_Citizen_050_00_PayClaimFee")
          .get(CitizenURL + "/claim/1719854669290777/fee")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .check(CsrfCheck.save)
          .check(substring("Pay your claim fee")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      /*======================================================================================
               * Civil Citizen -  2.Prepare your claim - Continue to pay
    ==========================================================================================*/
      .group("Civil_Citizen_050_ContinueToPayPost") {
        exec(http("Civil_Citizen_050_00_ContinueToPay")
          .post(CitizenURL + "/claim/1719854669290777/fee")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("saveAndContinue", "true")
          .check(CsrfCheck.save)
          .check(substring("Enter card details"))
          .check(css("input[name='csrfToken']", "value").saveAs("_csrfTokenCardDetailPage"))
          .check(css("input[name='chargeId']", "value").saveAs("CardDetailPageChargeId"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
                    * Civil Citizen -  2.Prepare your claim - Enter Card Details
         ==========================================================================================*/
      .group("TX038_CMC_CardDetail_SubmitCardDetail") {
        exec(http("TX038_CMC_CardDetail_SubmitCardDetail")
          .post(paymentURL + "/card_details/#{CardDetailPageChargeId}")
          .formParam("chargeId", "#{CardDetailPageChargeId}")
          .formParam("csrfToken", "#{_csrfTokenCardDetailPage}")
          .formParam("cardNo", "4444333322221111")
          .formParam("expiryMonth", "07")
          .formParam("expiryYear", "26")
          .formParam("cardholderName", "Perf Tester")
          .formParam("cvc", "123")
          .formParam("addressCountry", "GB")
          .formParam("addressLine1", "4")
          .formParam("addressLine2", "Hibernia Gardens")
          .formParam("addressCity", "Hounslow")
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
      .group("TX039_CMC_CardDetail_ConfirmCardDetail") {
        exec(http("TX039_CMC_CardDetail_ConfirmCardDetail")
          .post(paymentURL + "/card_details/${CardDetailPageChargeId}/confirm")
          .formParam("csrfToken", "#{_csrfTokenCardDetailConfirm}")
          .formParam("chargeId", "#{CardDetailPageChargeId}")
          .check(regex("Claim submitted"))
          .check(css(".receipt-download-container>a", "href").saveAs("pdfDownload"))
          .check(css(".reference-number>h1.bold-large").saveAs("claimNumber")))
      }
  
      .pause(MinThinkTime, MaxThinkTime)
  
}
