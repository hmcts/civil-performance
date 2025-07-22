package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.spec_CreateClaim_Headers._
import utils._
object spec_CreateClaim {

  val BaseURL = Environment.baseURL
  val IdamURL = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val CreateSpecClaim =

    // ============================LANDING PAGE========================,
    group("Civil_SpecClaim_10_010_CreateCase_Land") {
      exec(http("Jurisdiction")
        .get("/aggregated/caseworkers/:uid/jurisdictions?access=read")
        .headers(Headers.commonHeader)
        .check(substring("callback_get_case_url")))

      .exec(http("Organisation")
        .get("/api/organisation")
        .headers(Headers.commonHeader)
        .check(substring("organisationProfileIds")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ============================START========================,
    .group("Civil_SpecClaim_10_020_CreateCase_Start") {
      exec(http("Create")
        .get("/aggregated/caseworkers/:uid/jurisdictions?access=create")
        .headers(Headers.commonHeader)
        .check(substring("Create claim - Specified")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    .group("Civil_SpecClaim_10_030_CreateCase_CreateCase") {
      exec(http("IgnoreWarning")
        .get("/data/internal/case-types/CIVIL/event-triggers/CREATE_CLAIM_SPEC?ignore-warning=false")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-case-trigger.v2+json;charset=UTF-8")
        .check(substring("Issue civil court proceedings"))
        .check(jsonPath("$.event_token").saveAs("event_token")))
      .exitHereIf(session => !session.contains("event_token"))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==================CONTINUE1=================,
    .group("Civil_SpecClaim_10_040_CreateCase_CaseChecklist") {
      exec(http("CaseChecklist")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECCheckList")
        .headers(Headers.validateHeader)
        .body(StringBody("""{"data": {}, "event": {"id": "CREATE_CLAIM_SPEC", "summary": "", "description": ""},
            |"event_data": {}, "event_token": "#{event_token}", "ignore_warning": false}""".stripMargin))
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/case-types/CIVIL/validate?" +
          "pageId=CREATE_CLAIM_SPECCheckList")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    .group("Civil_SpecClaim_10_050_CreateCase_Eligibility") {
      exec(http("Eligibility")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECEligibility")
        .headers(Headers.validateHeader)
        .body(StringBody("""{"data": {}, "event": {"id": "CREATE_CLAIM_SPEC", "summary": "", "description": ""},
            |"event_data": {}, "event_token": "#{event_token}", "ignore_warning": false}""".stripMargin))
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/case-types/CIVIL/validate?" +
          "pageId=CREATE_CLAIM_SPECEligibility")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    .group("Civil_SpecClaim_10_060_CreateCase_References") {
      exec(http("References")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECReferences")
        .headers(Headers.validateHeader)
        .body(StringBody("""{"data": {}, "event": {"id": "CREATE_CLAIM_SPEC", "summary": "", "description": ""},
            |"event_data": {}, "event_token": "#{event_token}", "ignore_warning": false}""".stripMargin))
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/case-types/CIVIL/validate?" +
          "pageId=CREATE_CLAIM_SPECReferences")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ======================CLAIM ADDRESS===============,
    .group("Civil_SpecClaim_10_070_CreateCase_PostcodeSearch") {
      exec(http("PostcodeSearch")
        .get("/api/addresses?postcode=HA11GP")
        .headers(Headers.commonHeader)
        .check(substring("This record is within England")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ========================CLAIM DETAILS===============,
    .group("Civil_SpecClaim_10_080_CreateCase_Claimant") {
      exec(http("Claimant")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimant")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/claimantDetails.dat"))
        .check(substring("primaryAddress")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==================ANOTHER CLAIMANT===============,
    .group("Civil_SpecClaim_10_090_CreateCase_AddAnotherClaimant") {
      exec(http("AddAnotherClaimant")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECAddAnotherClaimant")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/addAnotherClaimant.dat"))
        .check(substring("addApplicant2")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==================CLAIMANT EMAIL============,
    .group("Civil_SpecClaim_10_100_CreateCase_Notifications") {
      exec(http("Notifications")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECNotifications")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/claimantNotificationDetails.dat"))
        .check(substring("applicantSolicitor1CheckEmail")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==========================LEGAL REP===============,
    .group("Civil_SpecClaim_10_110_CreateCase_SolicitorOrganisation") {
      exec(http("ClaimantSolOrg")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimantSolicitorOrganisation")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/claimantSolicitorOrganisation.dat"))
        .check(substring("PrepopulateToUsersOrganisation")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ================LEGAL REP POSTAL==============,
    .group("Civil_SpecClaim_10_120_CreateCase_CorrespondenceAddress") {
      exec(http("CorrespondenceAddress")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECspecCorrespondenceAddress")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/claimantServiceAddress.dat"))
        .check(substring("specApplicantCorrespondenceAddressRequired")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================DEF ADDRESS===========,
    .group("Civil_SpecClaim_10_130_CreateCase_PostcodeSearch") {
      exec(http("DefPostCode")
        .get("/api/addresses?postcode=WD171BN")
        .headers(Headers.commonHeader)
        .check(substring("https://api.os.uk/search/places/v1/postcode?postcode=WD171BN")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ========================DEF DETAILS=============,
    .group("Civil_SpecClaim_10_140_CreateCase_Defendant") {
      exec(http("DefendantDetails")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECDefendant")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/defendantDetails.dat"))
        .check(substring("DEFENDANT")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==================DEF LEGAL REP=============,
    .group("Civil_SpecClaim_10_150_CreateCase_LegalRepresentation") {
      exec(http("DefLegalRep")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECLegalRepresentation")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/defendantRepresented.dat"))
        .check(substring("specRespondent1Represented")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    .group("Civil_SpecClaim_10_160_CreateCase_SolicitorOrganisation") {
      exec(http("DefSolOrg")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECDefendantSolicitorOrganisation")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/defendantSolicitorOrganisation.dat"))
        .check(substring("respondent1OrganisationPolicy")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==================DF REP EMAIL==================,
    .group("Civil_SpecClaim_10_170_CreateCase_DefendantEmail") {
      exec(http("CDefSolEmail")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECDefendantSolicitorEmail")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/defendantNotificationDetails.dat"))
        .check(substring("respondentSolicitor1EmailAddress")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==================DF REP ADDRESS==================,
    .group("Civil_SpecClaim_10_180_CreateCase_DefendantAddress") {
      exec(http("DefAddr")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECspecRespondentCorrespondenceAddress")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/defendantServiceAddress.dat"))
        .check(substring("respondentSolicitor1EmailAddress")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ============ANOTHER DF===============,
    .group("Civil_SpecClaim_10_190_CreateCase_AddAnotherDefendant") {
      exec(http("AddAnotherDefendant")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECAddAnotherDefendant")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/addAnotherDefendant.dat"))
        .check(substring("addRespondent2")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =====================AIRLINE CLAIM===========,
    .group("Civil_SpecClaim_10_200_CreateCase_FlightDelayClaim") {
      exec(http("FlightDelayClaim")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECFlightDelayClaim")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/claimType.dat"))
        .check(substring("isFlightDelayClaim")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==================DESC CLAIM===============,
    .group("Civil_SpecClaim_10_210_CreateCase_CaseDetails") {
      exec(http("CaseDetails")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECDetails")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/claimDescription.dat"))
        .check(substring("detailsOfClaim")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ===============CLAIM TIMELINE=================,
    .group("Civil_SpecClaim_10_220_CreateCase_UploadClaimDocument") {
      exec(http("UploadClaimDoc")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECUploadClaimDocument")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/claimParticulars.dat"))
        .check(substring("MANUAL")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    .group("Civil_SpecClaim_10_230_CreateCase_ClaimTimeline") {
      exec(http("ClaimTimeline")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimTimeline")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/claimTimeline.dat"))
        .check(substring("timelineOfEvents")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =============LIST EVIDENCE=================================,
    .group("Civil_SpecClaim_10_240_CreateCase_EvidenceList") {
      exec(http("EvidenceList")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECEvidenceList")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/claimEvidence.dat"))
        .check(substring("expertWitnessEvidence")))
    }
    .pause(MinThinkTime, MaxThinkTime)

   // =========================CLAIM AMOUNT===================,
    .group("Civil_SpecClaim_10_250_CreateCase_ClaimAmount") {
      exec(http("ClaimAmount")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimAmount")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/claimAmount.dat"))
        .check(substring("claimAmountBreakup")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    .group("Civil_SpecClaim_10_260_CreateCase_ClaimAmountDetails") {
      exec(http("ClaimAmountDetails")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimAmountDetails")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/claimAmountDetails.dat"))
        .check(substring("claimAmountBreakupSummaryObject")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    .group("Civil_SpecClaim_10_270_CreateCase_ClaimInterest") {
      exec(http("ClaimInterest")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimInterest")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/claimAmountInterest.json"))
        .check(substring("calculatedInterest")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    .group("Civil_SpecClaim_10_280_CreateCase_InterestSummary") {
      exec(http("InterestSummary")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECInterestSummary")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/claimAmountSummary.json"))
        .check(substring("applicantSolicitor1PbaAccountsIsEmpty")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    .group("Civil_SpecClaim_10_290_CreateCase_PBANumber") {
      exec(http("PBANumber")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECPbaNumber")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/claimFee.json"))
        .check(substring("calculatedAmountInPence")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================FIXED COSTS================,

    .group("Civil_SpecClaim_10_300_CreateCase_FCC") {
      exec(http("FixedCommencementCosts")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECFixedCommencementCosts")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/claimFixedCosts.json"))
        .check(substring("claimFixedCosts")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================SOT===============,
    .group("Civil_SpecClaim_10_310_CreateCase_StatementOfTruth") {
      exec(http("StatementOfTruth")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECStatementOfTruth")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/statementOfTruth.json"))
        .check(substring("StatementOfTruth")))
    }

    .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("xsrf_token")))

    .pause(MinThinkTime, MaxThinkTime)

    // =============================SUBMIT================,
    .group("Civil_SpecClaim_10_320_CreateCase_Submit") {
      exec(http("SOT_Submit")
        .post("/data/case-types/CIVIL/cases?ignore-warning=false")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-case.v2+json;charset=UTF-8")
        .body(ElFileBody("a_CreateClaim_bodies/submitClaimFinal.dat"))
        .check(substring("CALLBACK_COMPLETED"))
        .check(jsonPath("$.id").saveAs("caseId")))
      .exitHereIf(session => !session.contains("caseId"))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==================API ROLE ASSIGNMENT==================,
    .group("Civil_SpecClaim_10_330_CreateCase_GetCase") {
      exec(http("GetCase")
        .get("/data/internal/cases/#{caseId}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{caseId}")))
    }

    .group("Civil_SpecClaim_10_330_CreateCase_API_RoleAssignment") {
      exec(http("RoleAssignment")
        .post("/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
        .headers(Headers.commonHeader)
        .check(status.is(204)))
    }

    .group("Civil_SpecClaim_10_330_CreateCase_API_SupportedJurisdiction") {
      exec(http("SupportedJurisdiction")
        .get("/api/wa-supported-jurisdiction/get")
        .headers(Headers.commonHeader)
        .check(substring("CIVIL")))
    }
    .pause(10)

  val ClaimFeePayment =

    // ================================SERVICE REQUEST TAB==================================,
    group("Civil_SpecClaim_20_010_CreateCase_PaymentGroups") {
      exec(http("Paymentgroups")
        .get("/payments/cases/#{caseId}/paymentgroups")
        .headers(Headers.commonHeader)
        .check(regex("calculated_amount\":(.*?).00,").saveAs("calculated_amount"))
        .check(substring("payment_group_reference")))

      .exec(http("CaseNo")
        .get("/pay-bulkscan/cases/#{caseId}")
        .headers(Headers.commonHeader)
        .check(substring("HMCTS Manage cases")))

      .exec(http("PayOrder")
        .get("/payments/case-payment-orders?case_ids=#{caseId}")
        .headers(Headers.commonHeader)
        .header("csrf-token", "#{csrf}")
        .header("x-requested-with", "xmlhttprequest")
        .check(jsonPath("$.content[0].orderReference").optional.saveAs("OrdRefNo")))
    }
    .pause(2)

    // ================================Click Pay Now=======================,
    .group("Civil_SpecClaim_20_020_CreateCase_PayNow") {
      exec(http("PayNow")
        .get("/payments/pba-accounts")
        .headers(Headers.commonHeader)
        .check(substring("paymentAccount")))
    }
    .pause(2)

    // ================================CONFIRM PAY========================,
    .group("Civil_SpecClaim_20_030_CreateCase_ConfirmPayment") {
      exec(http("ConfirmPayment")
        .post("/payments/service-request/#{OrdRefNo}/pba-payments")
        .headers(Headers.commonHeader)
        .header("x-requested-with", "xmlhttprequest")
        .body(StringBody("""{"account_number": "PBA0077597", "amount": #{calculated_amount}, "currency": "GBP",
          |"customer_reference": "NFT", "idempotency_key": "idam-key-#{Idempotencynumber}",
          |"organisation_name": "Civil Damages Claims - Organisation 1"}""".stripMargin))
        .check(substring("success")))
    }
    .pause(60)

}