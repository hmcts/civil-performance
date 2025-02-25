package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils._

object XUIClaimCreation {

  val BaseURL = Environment.baseURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val CreateSpecClaim =

    // ============================LANDING PAGE========================,
    group("XUI_CreateSpecClaim_030_LandingPage") {
      exec(http("Jurisdiction")
        .get(BaseURL + "/aggregated/caseworkers/:uid/jurisdictions?access=read")
        .headers(Headers.commonHeader)
        .check(substring("callback_get_case_url")))

      .exec(http("Organisation")
        .get(BaseURL + "/api/organisation")
        .headers(Headers.commonHeader)
        .check(substring("organisationProfileIds")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ============================START========================,
    .group("XUI_CreateSpecClaim_040_CreateCase") {
      exec(http("Create")
        .get(BaseURL + "/aggregated/caseworkers/:uid/jurisdictions?access=create")
        .headers(Headers.commonHeader)
        .check(substring("Create claim - Specified")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    .group("XUI_CreateSpecClaim_050_CreateCase") {
      exec(http("IgnoreWarning")
        .get(BaseURL + "/data/internal/case-types/CIVIL/event-triggers/CREATE_CLAIM_SPEC?ignore-warning=false")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-case-trigger.v2+json;charset=UTF-8")
        .check(substring("Issue civil court proceedings"))
        .check(jsonPath("$.event_token").saveAs("event_token")))
      .exitHereIf(session => !session.contains("event_token"))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==================CONTINUE1=================,
    .group("XUI_CreateSpecClaim_060_CreateCase") {
      exec(http("CaseChecklist")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECCheckList")
        .headers(Headers.validateHeader)
        .body(StringBody("""{"data": {}, "event": {"id": "CREATE_CLAIM_SPEC", "summary": "", "description": ""},
            |"event_data": {}, "event_token": "#{event_token}", "ignore_warning": false}""".stripMargin))
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/case-types/CIVIL/validate?" +
          "pageId=CREATE_CLAIM_SPECCheckList")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    .group("XUI_CreateSpecClaim_070_CreateCase") {
      exec(http("Eligibility")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECEligibility")
        .headers(Headers.validateHeader)
        .body(StringBody("""{"data": {}, "event": {"id": "CREATE_CLAIM_SPEC", "summary": "", "description": ""},
            |"event_data": {}, "event_token": "#{event_token}", "ignore_warning": false}""".stripMargin))
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/case-types/CIVIL/validate?" +
          "pageId=CREATE_CLAIM_SPECEligibility")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    .group("XUI_CreateSpecClaim_080_CreateCase") {
      exec(http("References")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECReferences")
        .headers(Headers.validateHeader)
        .body(StringBody("""{"data": {}, "event": {"id": "CREATE_CLAIM_SPEC", "summary": "", "description": ""},
            |"event_data": {}, "event_token": "#{event_token}", "ignore_warning": false}""".stripMargin))
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/case-types/CIVIL/validate?" +
          "pageId=CREATE_CLAIM_SPECReferences")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ======================CLAIM ADDRESS===============,
    .group("XUI_CreateSpecClaim_090_CreateCase") {
      exec(http("PostCodeSearch")
        .get(BaseURL + "/api/addresses?postcode=HA11GP")
        .headers(Headers.commonHeader)
        .check(substring("This record is within England")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ========================CLAIM DETAILS===============,
    .group("XUI_CreateSpecClaim_100_CreateCase") {
      exec(http("Claimant")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimant")
        .headers(Headers.validateHeader)
        .body(ElFileBody("bodies/XUIClaimCreation/claimantDetails.dat"))
        .check(substring("primaryAddress")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==================ANOTHER CLAIMANT===============,
    .group("XUI_CreateSpecClaim_110_CreateCase") {
      exec(http("AddAnotherClaimant")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECAddAnotherClaimant")
        .headers(Headers.validateHeader)
        .body(ElFileBody("bodies/XUIClaimCreation/addAnotherClaimant.dat"))
        .check(substring("addApplicant2")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==================CLAIMANT EMAIL============,
    .group("XUI_CreateSpecClaim_120_CreateCase") {
      exec(http("Notifications")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECNotifications")
        .headers(Headers.validateHeader)
        .body(ElFileBody("bodies/XUIClaimCreation/claimantNotificationDetails.dat"))
        .check(substring("applicantSolicitor1CheckEmail")))
    }

    // ==========================LEGAL REP===============,
    .group("XUI_CreateSpecClaim_130_CreateCase") {
      exec(http("ClaimantSolOrg")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimantSolicitorOrganisation")
        .headers(Headers.validateHeader)
        .body(ElFileBody("bodies/XUIClaimCreation/claimantSolicitorOrganisation.dat"))
        .check(substring("PrepopulateToUsersOrganisation")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ================LEGAL REP POSTAL==============,
    .group("XUI_CreateSpecClaim_140_CreateCase") {
      exec(http("CorrespondenceAddress")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECspecCorrespondenceAddress")
        .headers(Headers.validateHeader)
        .body(ElFileBody("bodies/XUIClaimCreation/claimantServiceAddress.dat"))
        .check(substring("specApplicantCorrespondenceAddressRequired")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================DEF ADDR===========,
    .group("XUI_CreateSpecClaim_150_CreateCase") {
      exec(http("DefPostCode")
        .get(BaseURL + "/api/addresses?postcode=WD171BN")
        .headers(Headers.commonHeader)
        .check(substring("https://api.os.uk/search/places/v1/postcode?postcode=WD171BN")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ========================DEF DETAILS=============,
    .group("XUI_CreateSpecClaim_160_CreateCase") {
      exec(http("DefendantDetails")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECDefendant")
        .headers(Headers.validateHeader)
        .body(ElFileBody("bodies/XUIClaimCreation/defendantDetails.dat"))
        .check(substring("DEFENDANT")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==================DEF NO LEGAL REP=============,
    .group("XUI_CreateSpecClaim_170_CreateCase") {
      exec(http("DefLegalRep")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECLegalRepresentation")
        .headers(Headers.validateHeader)
        .body(ElFileBody("bodies/XUIClaimCreation/defendantRepresented.dat"))
        .check(substring("specRespondent1Represented")))
    }
    .pause(MinThinkTime, MaxThinkTime)

//    .group("XUI_CreateSpecClaim_180_CreateCase") {
//      exec(http("DefSolOrg")
//        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECDefendantSolicitorOrganisation")
//        .headers(Headers.validateHeader)
//        .body(ElFileBody("bodies/XUIClaimCreation/defendantSolicitorOrganisation.dat"))
//        .check(substring("respondent1OrganisationPolicy")))
//    }
//    .pause(MinThinkTime, MaxThinkTime)
//
//    // ==================DF REP EMAIL==================,
//    .group("XUI_CreateSpecClaim_190_CreateCase") {
//      exec(http("CDefSolEmail")
//        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECDefendantSolicitorEmail")
//        .headers(Headers.validateHeader)
//        .body(ElFileBody("bodies/XUIClaimCreation/defendantNotificationDetails.dat"))
//        .check(substring("respondentSolicitor1EmailAddress")))
//    }
//    .pause(MinThinkTime, MaxThinkTime)
//
//    // ==================DF REP ADDRESS==================,
//    .group("XUI_CreateSpecClaim_200_CreateCase") {
//      exec(http("DefAddr")
//        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECspecRespondentCorrespondenceAddress")
//        .headers(Headers.validateHeader)
//        .body(ElFileBody("bodies/XUIClaimCreation/defendantServiceAddress.dat"))
//        .check(substring("respondentSolicitor1EmailAddress")))
//    }
//    .pause(MinThinkTime, MaxThinkTime)

    // ============ANOTHER DF===============,
    .group("XUI_CreateSpecClaim_180_CreateCase") {
      exec(http("AddAnotherDefendant")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECAddAnotherDefendant")
        .headers(Headers.validateHeader)
        .body(ElFileBody("bodies/XUIClaimCreation/addAnotherDefendant.dat"))
        .check(substring("addRespondent2")))
    }
    .pause(MinThinkTime, MaxThinkTime)


    // =====================AIRLINE CLAIM===========,
    .group("XUI_CreateSpecClaim_190_CreateCase") {
      exec(http("FlightDelayClaim")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECFlightDelayClaim")
        .headers(Headers.validateHeader)
        .body(ElFileBody("bodies/XUIClaimCreation/claimType.dat"))
        .check(substring("isFlightDelayClaim")))
    }
    .pause(MinThinkTime, MaxThinkTime)


    // ==================DESC CLAIM===============,
    .group("XUI_CreateSpecClaim_200_CreateCase") {
      exec(http("CaseDetails")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECDetails")
        .headers(Headers.validateHeader)
        .body(ElFileBody("bodies/XUIClaimCreation/claimDescription.dat"))
        .check(substring("detailsOfClaim")))
    }
    .pause(MinThinkTime, MaxThinkTime)


    // ===============CLAIM TIMELINE=================,
    .group("XUI_CreateSpecClaim_210_CreateCase") {
      exec(http("UploadClaimDoc")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECUploadClaimDocument")
        .headers(Headers.validateHeader)
        .body(ElFileBody("bodies/XUIClaimCreation/claimParticulars.dat"))
        .check(substring("MANUAL")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    .group("XUI_CreateSpecClaim_220_CreateCase") {
      exec(http("ClaimTimeline")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimTimeline")
        .headers(Headers.validateHeader)
        .body(ElFileBody("bodies/XUIClaimCreation/claimTimeline.dat"))
        .check(substring("timelineOfEvents")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =============LIST EVIDENCE=================================,
    .group("XUI_CreateSpecClaim_230_CreateCase") {
      exec(http("SPECEvidenceList")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECEvidenceList")
        .headers(Headers.validateHeader)
        .body(ElFileBody("bodies/XUIClaimCreation/claimEvidence.dat"))
        .check(substring("expertWitnessEvidence")))
    }
    .pause(MinThinkTime, MaxThinkTime)


   // =========================CLAIM AMOUNT===================,
    .group("XUI_CreateSpecClaim_240_CreateCase") {
      exec(http("ClaimAmount")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimAmount")
        .headers(Headers.validateHeader)
        .body(ElFileBody("bodies/XUIClaimCreation/claimAmount.dat"))
        .check(substring("claimAmountBreakup")))
    }
    .pause(MinThinkTime, MaxThinkTime)

//    .group("XUI_CreateSpecClaim_250_CreateCase") {
//      exec(http("CREATE_CLAIM_SPECClaimAmountDetails")
//        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimAmountDetails")
//        .headers(Headers.validateHeader)
//        .body(ElFileBody("bodies/XUIClaimCreation/claimAmountDetails.dat"))
//        .check(substring("claimAmountBreakupSummaryObject")))
//    }
//    .pause(MinThinkTime, MaxThinkTime)

    .group("XUI_CreateSpecClaim_260_CreateCase") {
      exec(http("ClaimInterest")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimInterest")
        .headers(Headers.validateHeader)
        .body(ElFileBody("bodies/XUIClaimCreation/claimAmountInterest.json"))
        .check(substring("calculatedInterest")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    .group("XUI_CreateSpecClaim_270_CreateCase") {
      exec(http("InterestSummary")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECInterestSummary")
        .headers(Headers.validateHeader)
        .body(ElFileBody("bodies/XUIClaimCreation/claimAmountSummary.json"))
        .check(substring("applicantSolicitor1PbaAccountsIsEmpty")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    .group("XUI_CreateSpecClaim_280_CreateCase") {
      exec(http("PbaNumber")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECPbaNumber")
        .headers(Headers.validateHeader)
        .body(ElFileBody("bodies/XUIClaimCreation/claimFee.json"))
        .check(substring("calculatedAmountInPence")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================FIXED COSTS================,

    .group("XUI_CreateSpecClaim_290_CreateCase") {
      exec(http("FixedCommencementCosts")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECFixedCommencementCosts")
        .headers(Headers.validateHeader)
        .body(ElFileBody("bodies/XUIClaimCreation/claimFixedCosts.json"))
        .check(substring("claimFixedCosts")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================SOT===============,
    .group("XUI_CreateSpecClaim_300_CreateCase") {
      exec(http("StatementOfTruth")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECStatementOfTruth")
        .headers(Headers.validateHeader)
        .body(ElFileBody("bodies/XUIClaimCreation/statementOfTruth.json"))
        .check(substring("StatementOfTruth")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    // =============================SUBMIT================,
    .group("XUI_CreateSpecClaim_310_CreateCase") {
      exec(http("SOT_Submit")
        .post(BaseURL + "/data/case-types/CIVIL/cases?ignore-warning=false")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-case.v2+json;charset=UTF-8")
        .body(ElFileBody("bodies/XUIClaimCreation/submitClaimFinal.dat"))
        .check(substring("CALLBACK_COMPLETED"))
        .check(jsonPath("$.id").saveAs("caseId")))
      .exitHereIf(session => !session.contains("caseId"))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==================API ROLE ASSIGNMENT==================,
    .group("XUI_CreateSpecClaim_320_APIRoleAssignment") {
      exec(http("GetCase")
        .get(BaseURL + "/data/internal/cases/#{caseId}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{caseId}")))
    }

    .group("XUI_CreateSpecClaim_330_APIRoleAssignment") {
      exec(http("APIRoleAssignment")
        .post(BaseURL + "/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
        .headers(Headers.commonHeader)
        .check(status.is(204)))
    }

    .group("XUI_CreateSpecClaim_340_SupportedJurisdiction") {
      exec(http("SupportedJurisdiction")
        .get(BaseURL + "/api/wa-supported-jurisdiction/get")
        .headers(Headers.commonHeader)
        .check(substring("CIVIL")))
    }
    .pause(10)

  val ClaimFeePayment =
    // ================================SERVICE REQUEST TAB==================================,
    group("XUI_CreateSpecClaim_350_CreateCase") {
      exec(http("Paymentgroups")
        .get(BaseURL + "/payments/cases/#{caseId}/paymentgroups")
        .headers(Headers.commonHeader)
        .check(regex("calculated_amount\":(.*?).00,").optional.saveAs("calculated_amount"))
        .check(substring("payment_group_reference")))

      .exec(http("CaseNo")
        .get(BaseURL + "/pay-bulkscan/cases/#{caseId}")
        .headers(Headers.commonHeader)
        .check(substring("HMCTS Manage cases")))

      .exec(http("PayOrder")
        .get(BaseURL + "/payments/case-payment-orders?case_ids=#{caseId}")
        .headers(Headers.commonHeader)
        .header("csrf-token", "#{csrf}")
        .header("x-requested-with", "xmlhttprequest")
        .check(jsonPath("$.content[0].orderReference").saveAs("OrdRefNo")))
    }
    .pause(2)

    // ================================Click Pay Now=======================,
    .group("XUI_CreateSpecClaim_360_CreateCase") {
      exec(http("PayNow")
        .get(BaseURL + "/payments/pba-accounts")
        .headers(Headers.commonHeader)
        .check(substring("paymentAccount")))
    }
    .pause(2)

    // ====================CONFIRM PAY========================,
    .group("XUI_CreateSpecClaim_370_CreateCase") {
      exec(http("ConfirmPayment")
        .post(BaseURL + "/payments/service-request/#{OrdRefNo}/pba-payments")
        .headers(Headers.commonHeader)
        .header("x-requested-with", "xmlhttprequest")
        .body(StringBody("""{"account_number": "PBA0077597", "amount": 455, "currency": "GBP",
          |"customer_reference": "NFT", "idempotency_key": "idam-key-#{Idempotencynumber}",
          |"organisation_name": "Civil Damages Claims - Organisation 1"}""".stripMargin))
        .check(substring("success")))
    }
    .pause(60)

}