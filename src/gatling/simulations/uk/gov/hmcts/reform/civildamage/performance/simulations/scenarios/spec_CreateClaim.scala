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
    group("Civil_CreateSpecClaim_10_00_LandingPage") {
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
    .group("Civil_CreateSpecClaim_10_01_CreateCase") {
      exec(http("Create")
        .get("/aggregated/caseworkers/:uid/jurisdictions?access=create")
        .headers(Headers.commonHeader)
        .check(substring("Create claim - Specified")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    .group("Civil_CreateSpecClaim_10_02_CreateCase") {
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
    .group("Civil_CreateSpecClaim_10_03_CreateCase") {
      exec(http("CaseChecklist")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECCheckList")
        .headers(Headers.validateHeader)
        .body(StringBody("""{"data": {}, "event": {"id": "CREATE_CLAIM_SPEC", "summary": "", "description": ""},
            |"event_data": {}, "event_token": "#{event_token}", "ignore_warning": false}""".stripMargin))
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/case-types/CIVIL/validate?" +
          "pageId=CREATE_CLAIM_SPECCheckList")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    .group("Civil_CreateSpecClaim_10_04_CreateCase") {
      exec(http("Eligibility")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECEligibility")
        .headers(Headers.validateHeader)
        .body(StringBody("""{"data": {}, "event": {"id": "CREATE_CLAIM_SPEC", "summary": "", "description": ""},
            |"event_data": {}, "event_token": "#{event_token}", "ignore_warning": false}""".stripMargin))
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/case-types/CIVIL/validate?" +
          "pageId=CREATE_CLAIM_SPECEligibility")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    .group("Civil_CreateSpecClaim_10_05_CreateCase") {
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
    .group("Civil_CreateSpecClaim_10_06_CreateCase") {
      exec(http("PostCodeSearch")
        .get("/api/addresses?postcode=HA11GP")
        .headers(Headers.commonHeader)
        .check(substring("This record is within England")))
    }
    .pause(MinThinkTime, MaxThinkTime)
    // ========================CLAIM DETAILS===============,
    .group("Civil_CreateSpecClaim_10_07_CreateCase") {
      exec(http("Claimant")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimant")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/claimantDetails.dat"))
        .check(substring("primaryAddress")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==================ANOTHER CLAIMANT===============,
    .group("Civil_CreateSpecClaim_10_08_CreateCase") {
      exec(http("AddAnotherClaimant")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECAddAnotherClaimant")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/addAnotherClaimant.dat"))
        .check(substring("addApplicant2")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==================CLAIMANT EMAIL============,
    .group("Civil_CreateSpecClaim_10_09_CreateCase") {
      exec(http("Notifications")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECNotifications")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/claimantNotificationDetails.dat"))
        .check(substring("applicantSolicitor1CheckEmail")))
    }

    // ==========================LEGAL REP===============,
    .group("Civil_CreateSpecClaim_10_10_CreateCase") {
      exec(http("ClaimantSolOrg")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimantSolicitorOrganisation")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/claimantSolicitorOrganisation.dat"))
        .check(substring("PrepopulateToUsersOrganisation")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ================LEGAL REP POSTAL==============,
    .group("Civil_CreateSpecClaim_10_11_CreateCase") {
      exec(http("CorrespondenceAddress")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECspecCorrespondenceAddress")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/claimantServiceAddress.dat"))
        .check(substring("specApplicantCorrespondenceAddressRequired")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================DEF ADDR===========,
    .group("Civil_CreateSpecClaim_10_12_CreateCase") {
      exec(http("DefPostCode")
        .get("/api/addresses?postcode=WD171BN")
        .headers(Headers.commonHeader)
        .check(substring("https://api.os.uk/search/places/v1/postcode?postcode=WD171BN")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ========================DEF DETAILS=============,
    .group("Civil_CreateSpecClaim_10_13_CreateCase") {
      exec(http("DefendantDetails")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECDefendant")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/defendantDetails.dat"))
        .check(substring("DEFENDANT")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==================DEF LEGAL REP=============,
    .group("Civil_CreateSpecClaim_10_14_CreateCase") {
      exec(http("DefLegalRep")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECLegalRepresentation")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/defendantRepresented.dat"))
        .check(substring("specRespondent1Represented")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    .group("Civil_CreateSpecClaim_10_15_CreateCase") {
      exec(http("DefSolOrg")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECDefendantSolicitorOrganisation")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/defendantSolicitorOrganisation.dat"))
        .check(substring("respondent1OrganisationPolicy")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==================DF REP EMAIL==================,
    .group("Civil_CreateSpecClaim_10_16_CreateCase") {
      exec(http("CDefSolEmail")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECDefendantSolicitorEmail")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/defendantNotificationDetails.dat"))
        .check(substring("respondentSolicitor1EmailAddress")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==================DF REP ADDRESS==================,
    .group("Civil_CreateSpecClaim_10_17_CreateCase") {
      exec(http("DefAddr")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECspecRespondentCorrespondenceAddress")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/defendantServiceAddress.dat"))
        .check(substring("respondentSolicitor1EmailAddress")))
    }
    .pause(MinThinkTime, MaxThinkTime)


    // ============ANOTHER DF===============,
    .group("Civil_CreateSpecClaim_10_18_CreateCase") {
      exec(http("AddAnotherDefendant")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECAddAnotherDefendant")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/addAnotherDefendant.dat"))
        .check(substring("addRespondent2")))
    }
    .pause(MinThinkTime, MaxThinkTime)


    // =====================AIRLINE CLAIM===========,
    .group("Civil_CreateSpecClaim_10_19_CreateCase") {
      exec(http("FlightDelayClaim")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECFlightDelayClaim")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/claimType.dat"))
        .check(substring("isFlightDelayClaim")))
    }
    .pause(MinThinkTime, MaxThinkTime)


    // ==================DESC CLAIM===============,
    .group("Civil_CreateSpecClaim_10_20_CreateCase") {
      exec(http("CaseDetails")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECDetails")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/claimDescription.dat"))
        .check(substring("detailsOfClaim")))
    }
    .pause(MinThinkTime, MaxThinkTime)


    // ===============CLAIM TIMELINE=================,
    .group("Civil_CreateSpecClaim_10_21_CreateCase") {
      exec(http("UploadClaimDoc")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECUploadClaimDocument")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/claimParticulars.dat"))
        .check(substring("MANUAL")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    .group("Civil_CreateSpecClaim_10_22_CreateCase") {
      exec(http("ClaimTimeline")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimTimeline")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/claimTimeline.dat"))
        .check(substring("timelineOfEvents")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =============LIST EVIDENCE=================================,
    .group("Civil_CreateSpecClaim_10_23_CreateCase") {
      exec(http("SPECEvidenceList")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECEvidenceList")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/claimEvidence.dat"))
        .check(substring("expertWitnessEvidence")))
    }
    .pause(MinThinkTime, MaxThinkTime)


   // =========================CLAIM AMOUNT===================,
    .group("Civil_CreateSpecClaim_10_24_CreateCase") {
      exec(http("CREATE_CLAIM_SPECClaimAmount")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimAmount")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/claimAmount.dat"))
        .check(substring("claimAmountBreakup")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    .group("Civil_CreateSpecClaim_10_25_CreateCase") {
      exec(http("CREATE_CLAIM_SPECClaimAmountDetails")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimAmountDetails")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/claimAmountDetails.dat"))
        .check(substring("claimAmountBreakupSummaryObject")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    .group("Civil_CreateSpecClaim_10_26_CreateCase") {
      exec(http("CREATE_CLAIM_SPECClaimInterest")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimInterest")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/claimAmountInterest.json"))
        .check(substring("calculatedInterest")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    .group("Civil_CreateSpecClaim_10_27_CreateCase") {
      exec(http("CREATE_CLAIM_SPECInterestSummary")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECInterestSummary")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/claimAmountSummary.json"))
        .check(substring("applicantSolicitor1PbaAccountsIsEmpty")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    .group("Civil_CreateSpecClaim_10_28_CreateCase") {
      exec(http("CREATE_CLAIM_SPECPbaNumber")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECPbaNumber")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/claimFee.json"))
        .check(substring("calculatedAmountInPence")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================FIXED COSTS================,

    .group("Civil_CreateSpecClaim_10_29_CreateCase") {
      exec(http("CREATE_CLAIM_SPECFixedCommencementCosts")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECFixedCommencementCosts")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/claimFixedCosts.json"))
        .check(substring("claimFixedCosts")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================SOT===============,
    .group("Civil_CreateSpecClaim_10_30_CreateCase") {
      exec(http("CREATE_CLAIM_SPECStatementOfTruth")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECStatementOfTruth")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/statementOfTruth.json"))
        .check(substring("StatementOfTruth")))
    }

    .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("xsrf_token")))

    .pause(MinThinkTime, MaxThinkTime)

    // =============================SUBMIT================,
    .group("Civil_CreateSpecClaim_10_32_CreateCase") {
      exec(http("CREATE_CLAIM_SPECSOT_Submit")
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
    .group("Civil_CreateSpecClaim_10_33_APIRoleAssignment") {
      exec(http("CREATE_CLAIM_SPECGetCase")
        .get("/data/internal/cases/#{caseId}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{caseId}")))
    }

    .group("Civil_CreateSpecClaim_10_34_APIRoleAssignment") {
      exec(http("CREATE_CLAIM_SPECAPIRoleAssignment")
        .post("/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
        .headers(Headers.commonHeader)
        .check(status.is(204)))
    }

    .group("Civil_CreateSpecClaim_10_35_SupportedJurisdiction") {
      exec(http("CREATE_CLAIM_SPECSupportedJurisdiction")
        .get("/api/wa-supported-jurisdiction/get")
        .headers(Headers.commonHeader)
        .check(substring("CIVIL")))
    }
    .pause(10)

  val ClaimFeePayment =
    // ================================SERVICE REQUEST TAB==================================,
    group("Civil_CreateSpecClaim_10_36_CreateCase") {
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
    .group("Civil_CreateSpecClaim_10_37_CreateCase") {
      exec(http("PayNow")
        .get("/payments/pba-accounts")
        .headers(Headers.commonHeader)
        .check(substring("paymentAccount")))
    }
    .pause(2)

    // ====================CONFIRM PAY========================,
    .group("Civil_CreateSpecClaim_10_38_CreateCase") {
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