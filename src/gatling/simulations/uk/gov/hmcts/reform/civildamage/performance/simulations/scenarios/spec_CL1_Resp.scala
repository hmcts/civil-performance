package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils._
import utils.spec_CL1_Headers._

import scala.concurrent.duration.DurationInt

object spec_CL1_Resp{

  val BaseURL = Environment.baseURL
  val IdamURL = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime


  val RespToDef =

    // ========================LANDING PAGE=====================,
    group("Civil_SpecClaim_40_010_ClaimantResponse_Land") {
      exec(http("Land_005_Jurisdictions")
        .get("/aggregated/caseworkers/:uid/jurisdictions?access=read")
        .headers(Headers.commonHeader)
        .check(substring("callback_get_case_url")))

      .exec(http("Land_010_Organisation")
        .get("/api/organisation")
        .headers(Headers.commonHeader)
        .check(substring("organisationProfileIds")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ========================SEARCH=====================,
    .group("Civil_SpecClaim_40_020_ClaimantResponse_Search") {
      exec(http("Search_005_WorkBasket")
        .get("/data/internal/case-types/CIVIL/work-basket-inputs")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-workbasket-input-details.v2+json;charset=UTF-8")
        .check(substring("workbasketInputs")))

      .exec(http("Search_010_CaseReference")
        .post("/data/internal/searchCases?ctid=CIVIL&use_case=WORKBASKET&view=WORKBASKET&page=1&case_reference=#{caseId}")
        .headers(Headers.commonHeader)
        .body(StringBody("""{"size": 25}""".stripMargin))
        .check(substring("AWAITING_APPLICANT_INTENTION")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ================================OPEN CASE===============================,
    .group("Civil_SpecClaim_40_030_ClaimantResponse_GetCase") {
      exec(http("OpenCase_005_InternalCases")
        .get("/data/internal/cases/#{caseId}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("Awaiting Defendant Response")))
      }

    .group("Civil_SpecClaim_40_030_ClaimantResponse_API_RoleAssignment") {
      exec(http("OpenCase_010_RoleAssignment")
        .post("/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
        .headers(Headers.commonHeader)
        .check(status.is(204)))
    }

    .group("Civil_SpecClaim_40_030_ClaimantResponse_API_SupportedJurisdiction") {
      exec(http("OpenCase_015_Jurisdiction")
        .get("/api/wa-supported-jurisdiction/get")
        .headers(Headers.commonHeader)
        .check(substring("CIVIL")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =============================VIEW & RESPOND=============================,
    .group("Civil_SpecClaim_40_040_ClaimantResponse_StartEvent") {
      exec(http("RespToDef_005_Jurisdiction")
        .get("/workallocation/case/tasks/#{caseId}/event/CLAIMANT_RESPONSE_SPEC/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(Headers.commonHeader)
        .check(substring("task_required_for_event")))

      .exec(http("RespToDef_010_Profile")
        .get("/data/internal/profile")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-user-profile.v2+json;charset=UTF-8")
        .check(substring("solicitor")))

      .exec(http("RespToDef_015_IgnoreWarning")
        .get("/data/internal/cases/#{caseId}/event-triggers/CLAIMANT_RESPONSE_SPEC?ignore-warning=false")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
        .check(substring("CLAIMANT_RESPONSE"))
        .check(regex("partyID\":\"(.*?)\"").saveAs("repPartyID"))
        .check(regex("partyName\":\"(.*?)\"").saveAs("partyName"))
        .check(jsonPath("$..formatted_value.documentLink.document_url").saveAs("document_url"))
        .check(jsonPath("$..formatted_value.documentLink.document_filename").saveAs("document_filename"))
        .check(jsonPath("$..formatted_value.documentSize").saveAs("document_size"))
        .check(jsonPath("$..formatted_value.createdDatetime").saveAs("createDateTime"))
        .check(jsonPath("$.event_token").saveAs("event_token")))
      .exitHereIf(session => !session.contains("repPartyID"))

      .exec(http("RespToDef_020_Jurisdiction")
        .get("/workallocation/case/tasks/#{caseId}/event/CLAIMANT_RESPONSE_SPEC/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(Headers.commonHeader)
        .check(substring("task_required_for_event")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ========================DEF RESP FORM======================,
    .group("Civil_SpecClaim_40_050_ClaimantResponse_RespondentResponse") {
      exec(http("RespToDef_005_ProceedWithClaim")
        .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECRespondentResponse")
        .headers(Headers.validateHeader)
        .body(ElFileBody("c_RespToDef/claimantResponse.dat"))
        .check(substring("applicant1ProceedWithClaim")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==============================CONTINUE==========================,
    .group("Civil_SpecClaim_40_060_ClaimantResponse_ReponseDocument") {
      exec(http("RespToDef_005_Continue")
        .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECApplicantDefenceResponseDocument")
        .headers(Headers.validateHeader)
        .body(ElFileBody("c_RespToDef/claimantDocument.dat"))
        .check(substring("applicant1DefenceResponseDocumentSpec")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==============================MEDIATION==========================,
    //Please see spec_DF1_Resp.scala for more details in regards with the commented POST bodies.
    .group("Civil_SpecClaim_40_070_ClaimantResponse_MediationContact") {
      exec(http("RespToDef_005_Mediation")
        .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECMediationContactInformation")
        .headers(Headers.validateHeader)
        .body(ElFileBody("c_RespToDef/claimantMediationInfo.dat"))
        .check(substring("app1MediationContactInfo")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    .group("Civil_SpecClaim_40_080_ClaimantResponse_MediationAvailability") {
      exec(http("RespToDef_010_Mediation")
        .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECMediationAvailability")
        .headers(Headers.validateHeader)
        .body(ElFileBody("c_RespToDef/claimantMediationAvailability.dat"))
        .check(substring("isMediationUnavailablityExists")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =============================USE OF EXPERTS=============================,
    .group("Civil_SpecClaim_40_090_ClaimantResponse_Experts") {
      exec(http("RespToDef_005_Experts")
        .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECSmallClaimExperts")
        .headers(Headers.validateHeader)
        //.body(ElFileBody("c_RespToDef/claimantExperts.dat"))
        .body(ElFileBody("c_RespToDef/claimantExperts_Mediation.dat"))
        .check(substring("applicant1ClaimExpertSpecRequired")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ===================================WITNESSES===================,
    .group("Civil_SpecClaim_40_100_ClaimantResponse_Witnesses") {
      exec(http("RespToDef_005_Witnesses")
        .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECSmallClaimWitnesses")
        .headers(Headers.validateHeader)
        //.body(ElFileBody("c_RespToDef/claimantWitnesses.dat"))
        .body(ElFileBody("c_RespToDef/claimantWitnesses_Mediation.dat"))
        .check(substring("applicant1DQWitnessesSmallClaim")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ===================================LANGUAGE===========================,
    .group("Civil_SpecClaim_40_110_ClaimantResponse_Language") {
      exec(http("RespToDef_005_Language")
        .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECLanguage")
        .headers(Headers.validateHeader)
        //.body(ElFileBody("c_RespToDef/claimantLanguage.dat"))
        .body(ElFileBody("c_RespToDef/claimantLanguage_Mediation.dat"))
        .check(substring("applicant1DQLanguage")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ===========================HEARING==================,
    .group("Civil_SpecClaim_40_120_ClaimantResponse_Hearing") {
      exec(http("RespToDef_005_Hearing")
        .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECHearing")
        .headers(Headers.validateHeader)
        //.body(ElFileBody("c_RespToDef/claimantHearingAvailability.dat"))
        .body(ElFileBody("c_RespToDef/claimantHearingAvailability_Mediation.dat"))
        .check(substring("unavailableDatesRequired")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==================================REQUESTED COURT======================,
    .group("Civil_SpecClaim_40_130_ClaimantResponse_CourtLocation") {
      exec(http("RespToDef_005_Court")
        .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECApplicantCourtLocationLRspec")
        .headers(Headers.validateHeader)
        //.body(ElFileBody("c_RespToDef/claimantCourtLocation.dat"))
        .body(ElFileBody("c_RespToDef/claimantCourtLocation_Mediation.dat"))
        .check(substring("applicant1DQRemoteHearingLRspec")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==============================SUPPORT WITH ACCESS NEEDS=======================,
//    .group("Civil_SpecClaim_40_140_ClaimantResponse_AccessNeeds") {
//      exec(http("RespToDef_005_AccessNeeds")
//        .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECHearingSupport")
//        .headers(Headers.validateHeader)
//        //.body(ElFileBody("c_RespToDef/claimantHearingSupport.dat"))
//        .body(ElFileBody("c_RespToDef/claimantHearingSupport_Mediation.dat"))
//        .check(substring("applicant1DQHearingSupport")))
//    }
//    .pause(MinThinkTime, MaxThinkTime)
//
//    // =====================================VULNERABILITY=======================,
//    .group("Civil_SpecClaim_40_150_ClaimantResponse_Vulnerability") {
//      exec(http("RespToDef_005_Vulnerability")
//        .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECVulnerabilityQuestions")
//        .headers(Headers.validateHeader)
//        //.body(ElFileBody("c_RespToDef/claimantVulnerability.dat"))
//        .body(ElFileBody("c_RespToDef/claimantVulnerability_Mediation.dat"))
//        .check(substring("applicant1DQVulnerabilityQuestions")))
//    }
//    .pause(MinThinkTime, MaxThinkTime)

    // ========================================SOT===========================,
    .group("Civil_SpecClaim_40_160_ClaimantResponse_StatementOfTruth") {
      exec(http("RespToDef_005_SOT")
        .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECStatementOfTruth")
        .headers(Headers.validateHeader)
        //.body(ElFileBody("c_RespToDef/claimantStatementOfTruth.dat"))
        .body(ElFileBody("c_RespToDef/claimantStatementOfTruth_Mediation.dat"))
        .check(substring("StatementOfTruth")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==========================================SOT SUBMIT======================,
    .group("Civil_SpecClaim_40_170_ClaimantResponse_Submit") {
      exec(http("RespToDef_005_SOTSubmit")
        .post("/data/cases/#{caseId}/events")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
        //.body(ElFileBody("c_RespToDef/claimantProceedWithClaim.dat"))
        .body(ElFileBody("c_RespToDef/claimantProceedWithClaim_Mediation.dat"))
        .check(substring("You have decided to proceed with the claim")))

      .exec(http("RespToDef_010_Submit")
        .get("/data/internal/cases/#{caseId}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{caseId}")))
    }
    .pause(MinThinkTime, MaxThinkTime)

}