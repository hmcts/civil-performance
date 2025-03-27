package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils._

object spec_DF2_CL2_FinalOrder_Not_In_Scope {

 val BaseURL = Environment.baseURL
 val IdamURL = Environment.idamURL
 val MinThinkTime = Environment.minThinkTime
 val MaxThinkTime = Environment.maxThinkTime


  val DF_upload =

    // ========================LANDING PAGE=====================,
    group("Civil_SpecClaim_80_01_DefendantDocUpload") {
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
    .group("Civil_SpecClaim_80_02_DefendantDocUpload") {
      exec(http("Search_005_WorkBasket")
        .get("/data/internal/case-types/CIVIL/work-basket-inputs")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-workbasket-input-details.v2+json;charset=UTF-8")
        .check(substring("workbasketInputs")))

      .exec(http("Search_010_CaseReference")
        .post("/data/internal/searchCases?ctid=CIVIL&use_case=WORKBASKET&view=WORKBASKET&page=1&case_reference=#{caseId}")
        .headers(Headers.commonHeader)
        .body(StringBody("""{"size": 25}""".stripMargin))
        .check(substring("HEARING_READINESS")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================OPEN CASE=======================,
    .group("Civil_SpecClaim_80_03_DefendantDocUpload") {
      exec(http("OpenCase_005_InternalCases")
        .get("/data/internal/cases/#{caseId}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("Hearing Readiness")))

      .exec(http("OpenCase_010_RoleAssignment")
        .post("/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
        .headers(Headers.commonHeader)
        .check(status.is(204)))

      .exec(http("OpenCase_015_Jurisdiction")
        .get("/api/wa-supported-jurisdiction/get")
        .headers(Headers.commonHeader)
        .check(substring("CIVIL")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================DF UPLOAD DOC=======================,
    .group("Civil_SpecClaim_80_04_DefendantDocUpload") {
      exec(http("DF_005_UploadDoc")
        .get("/workallocation/case/tasks/#{caseId}/event/EVIDENCE_UPLOAD_RESPONDENT/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(Headers.commonHeader)
        .check(substring("task_required_for_event")))

      .exec(http("DF_010_Profile")
        .get("/data/internal/profile")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-user-profile.v2+json;charset=UTF-8")
        .check(substring("solicitor")))

      .exec(http("DF_015_IgnoreWarning")
        .get("/data/internal/cases/#{caseId}/event-triggers/EVIDENCE_UPLOAD_RESPONDENT?ignore-warning=false")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
        .check(substring("EVIDENCE_UPLOAD_RESPONDENT"))
        .check(jsonPath("$.event_token").saveAs("event_token_docUpload")))
      .exitHereIf(session => !session.contains("event_token_docUpload"))

      .exec(http("DF_020_UploadDoc")
        .get("/workallocation/case/tasks/#{caseId}/event/EVIDENCE_UPLOAD_RESPONDENT/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(Headers.commonHeader)
        .check(substring("task_required_for_event")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("xsrf_token")))

    // =======================HOW TO UPLOAD=======================,
    .group("Civil_SpecClaim_80_05_DefendantDocUpload") {
      exec(http("DF_005_UploadChoice")
        .post("/data/case-types/CIVIL/validate?pageId=EVIDENCE_UPLOAD_RESPONDENTEvidenceUpload")
        .headers(Headers.validateHeader)
        .body(ElFileBody("f_DefResp_bodies/respondentHowToUpload.dat"))
        .check(substring("caseTypeFlag")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================DF WITNESS STATEMENT=======================,
    .group("Civil_SpecClaim_80_06_DefendantDocUpload") {
      exec(http("DF_005_WitnessStatement")
        .post("/data/case-types/CIVIL/validate?pageId=EVIDENCE_UPLOAD_RESPONDENTDocumentSelectionFastTrack")
        .headers(Headers.validateHeader)
        .body(ElFileBody("f_DefResp_bodies/respondentWitnessStatement.dat"))
        .check(substring("respondent1PaymentDateToStringSpec")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================DF UPLOAD WITNESS STATEMENT=======================,
    .group("Civil_SpecClaim_80_07_DefendantDocUpload") {
      exec(http("DF_005_UploadWitnessStatement")
        .post("/documentsv2")
        .headers(Headers.commonHeader)
        .header("content-type", "multipart/form-data; boundary=----263708296816916542312907778465")
        .bodyPart(RawFileBodyPart("files", "WitnessStatement.docx").fileName("WitnessStatement.docx")
          .transferEncoding("binary")).asMultipartForm
        .formParam("classification", "PUBLIC")
        .formParam("caseTypeId", "CIVIL")
        .formParam("jurisdictionId", "CIVIL")
        .check(jsonPath("$.documents[0].hashToken").saveAs("DF_HashToken"))
        .check(jsonPath("$.documents[0]._links.self.href").saveAs("DF_StatementDocument_url"))
        .check(substring("WitnessStatement.docx")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    .group("Civil_SpecClaim_80_08_DefendantDocUpload") {
      exec(http("DF_010_UploadWitnessStatement")
        .post("/data/case-types/CIVIL/validate?pageId=EVIDENCE_UPLOAD_RESPONDENTDocumentUpload")
        .headers(Headers.validateHeader)
        .body(ElFileBody("f_DefResp_bodies/respondentFileUpload.dat"))
        .check(substring("documentWitnessStatement")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================DF SUBMIT=======================,
    .group("Civil_SpecClaim_80_09_DefendantDocUpload") {
      exec(http("DF_005_Submit")
        .post("/data/cases/#{caseId}/events")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
        .body(ElFileBody("f_DefResp_bodies/respondentSubmit.dat"))
        .check(substring("You can continue uploading documents or return later.")))

      .exec(http("DF_010_InternalCases")
        .get("/data/internal/cases/#{caseId}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{caseId}")))
    }
    .pause(MinThinkTime, MaxThinkTime)



  val CL_upload =

    // ========================LANDING PAGE=====================,
    group("Civil_SpecClaim_85_01_ClaimantDocUpload") {
      exec(http("005_SearchCase")
        .get("/aggregated/caseworkers/:uid/jurisdictions?access=read")
        .headers(Headers.commonHeader)
        .check(substring("callback_get_case_url")))

      .exec(http("010_SearchCase")
        .get("/api/organisation")
        .headers(Headers.commonHeader)
        .check(substring("organisationProfileIds")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ========================SEARCH=====================,
    .group("Civil_SpecClaim_85_02_ClaimantDocUpload") {
      exec(http("Search_005_WorkBasket")
        .get("/data/internal/case-types/CIVIL/work-basket-inputs")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-workbasket-input-details.v2+json;charset=UTF-8")
        .check(substring("workbasketInputs")))

      .exec(http("Search_010_CaseReference")
        .post("/data/internal/searchCases?ctid=CIVIL&use_case=WORKBASKET&view=WORKBASKET&page=1&case_reference=#{caseId}")
        .headers(Headers.commonHeader)
        .body(StringBody("""{"size": 25}""".stripMargin))
        .check(substring("HEARING_READINESS")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================OPEN CASE=======================,
    .group("Civil_SpecClaim_85_03_ClaimantDocUpload") {
      exec(http("OpenCase_005_InternalCases")
        .get("/data/internal/cases/#{caseId}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("Hearing Readiness")))

      .exec(http("OpenCase_010_RoleAssignment")
        .post("/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
        .headers(Headers.commonHeader)
        .check(status.is(204)))

      .exec(http("OpenCase_015_Jurisdiction")
        .get("/api/wa-supported-jurisdiction/get")
        .headers(Headers.commonHeader)
        .check(substring("CIVIL")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================CLAIMANT UPLOAD DOC=======================,
    .group("Civil_SpecClaim_85_04_ClaimantDocUpload") {
      exec(http("CL_005_UploadDoc")
        .get("/workallocation/case/tasks/#{caseId}/event/EVIDENCE_UPLOAD_APPLICANT/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(Headers.commonHeader)
        .check(substring("task_required_for_event")))

      .exec(http("CL_010_Profile")
        .get("/data/internal/profile")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-user-profile.v2+json;charset=UTF-8")
        .check(substring("solicitor")))

      .exec(http("CL_015_IgnoreWarning")
        .get("/data/internal/cases/#{caseId}/event-triggers/EVIDENCE_UPLOAD_APPLICANT?ignore-warning=false")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
        .check(substring("EVIDENCE_UPLOAD_APPLICANT"))
        .check(jsonPath("$.event_token").saveAs("event_token")))

      .exec(http("CL_020_UploadDoc")
        .get("/workallocation/case/tasks/#{caseId}/event/EVIDENCE_UPLOAD_APPLICANT/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(Headers.commonHeader)
        .check(substring("task_required_for_event")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("xsrf_token")))

    // =======================HOW TO UPLOAD=======================,
    .group("Civil_SpecClaim_85_05_ClaimantDocUpload") {
      exec(http("CL_005_UploadChoice")
        .post("/data/case-types/CIVIL/validate?pageId=EVIDENCE_UPLOAD_APPLICANTEvidenceUpload")
        .headers(Headers.validateHeader)
        .body(ElFileBody("g_ClaimResp_bodies/claimantHowToUpload.dat"))
        .check(substring("caseTypeFlag")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================CL WITNESS STATEMENT=======================,
    .group("Civil_SpecClaim_85_06_ClaimantDocUpload") {
      exec(http("CL_005_WitnessStatement")
        .post("/data/case-types/CIVIL/validate?pageId=EVIDENCE_UPLOAD_APPLICANTDocumentSelectionFastTrack")
        .headers(Headers.validateHeader)
        .body(ElFileBody("g_ClaimResp_bodies/claimantWitnessStatement.dat"))
        .check(substring("respondent1PaymentDateToStringSpec")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================CL UPLOAD WITNESS STATEMENT=======================,
    .group("Civil_SpecClaim_85_07_ClaimantDocUpload") {
      exec(http("CL_005_UploadWitnessStatement")
        .post("/documentsv2")
        .headers(Headers.commonHeader)
        .header("content-type", "multipart/form-data; boundary=----263708296816916542312907778465")
        .bodyPart(RawFileBodyPart("files", "WitnessStatement.docx").fileName("WitnessStatement.docx")
          .transferEncoding("binary")).asMultipartForm
        .formParam("classification", "PUBLIC")
        .formParam("caseTypeId", "CIVIL")
        .formParam("jurisdictionId", "CIVIL")
        .check(jsonPath("$.documents[0].hashToken").saveAs("CL_HashToken"))
        .check(jsonPath("$.documents[0]._links.self.href").saveAs("CL_StatementDocument_url"))
        .check(substring("WitnessStatement.docx")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    .group("Civil_SpecClaim_85_08_ClaimantDocUpload") {
      exec(http("CL_010_UploadWitnessStatement")
        .post("/data/case-types/CIVIL/validate?pageId=EVIDENCE_UPLOAD_APPLICANTDocumentUpload")
        .headers(Headers.validateHeader)
        .body(ElFileBody("g_ClaimResp_bodies/claimantFileUpload.json"))
        .check(substring("documentWitnessStatement")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================CL SUBMIT=======================,
    .group("Civil_SpecClaim_85_09_ClaimantDocUpload") {
      exec(http("CL_005_Submit")
        .post("/data/cases/#{caseId}/events")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
        .body(ElFileBody("g_ClaimResp_bodies/claimantSubmit.dat"))
        .check(substring("You can continue uploading documents or return later.")))

      .exec(http("CL_010_InternalCases")
        .get("/data/internal/cases/#{caseId}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{caseId}")))
    }
    .pause(MinThinkTime, MaxThinkTime)



  val FinalOrder =

    // ========================LANDING PAGE=====================,
    group("Civil_SpecClaim_89_01_FinalOrder") {
      exec(http("005_HealthCheck")
        .get("/api/healthCheck?path=%2Fwork%2Fmy-work%2Flist")
        .headers(Headers.commonHeader)
        .check(substring("healthState")))

      .exec(http("005_RegionLocation")
        .post("/workallocation/region-location")
        .headers(Headers.commonHeader)
        .body(StringBody("""{"serviceIds": ["PRIVATELAW", "CIVIL"]}""".stripMargin))
        .check(substring("regionId")))

      .exec(http("005_GetMyAccess")
        .get("/api/role-access/roles/get-my-access-new-count")
        .headers(Headers.commonHeader)
        .check(substring("count")))

      .exec(http("010_GetMyAccess")
        .get("/api/role-access/roles/get-my-access-new-count")
        .headers(Headers.commonHeader)
        .check(substring("count")))

      .exec(http("005_TypesOfWork")
        .get("/workallocation/task/types-of-work")
        .headers(Headers.commonHeader)
        .check(substring("Hearing work")))

      .exec(http("005_Jurisdiction")
        .get("/api/wa-supported-jurisdiction/get")
        .headers(Headers.commonHeader)
        .check(substring("CIVIL")))

      .exec(http("005_WorkAllocationTask")
        .post("/workallocation/task")
        .body(StringBody(
          """{"searchRequest": {"search_parameters": [ {"key": "user", "operator": "IN", "values": ["#{uId}"]},
            |{"key": "state", "operator": "IN", "values": ["assigned"]} ], "sorting_parameters": [],
            |"search_by": "judge"}, "view": "MyTasks"}""".stripMargin))
        .headers(Headers.commonHeader)
        .check(substring("Small Claims Track Directions")))

      .exec(http("010_Jurisdiction")
        .get("/api/wa-supported-jurisdiction/get")
        .headers(Headers.commonHeader)
        .check(substring("CIVIL")))

      .exec(http("005_GetUsersByServiceName")
        .post("/workallocation/caseworker/getUsersByServiceName")
        .body(StringBody("""{"services": ["IA", "CIVIL", "PRIVATELAW", "PUBLICLAW", "SSCS", "ST_CIC",
                           |"EMPLOYMENT"]}""".stripMargin))
        .headers(Headers.commonHeader)
        .check(substring("BIRMINGHAM CIVIL AND FAMILY JUSTICE CENTRE")))

      .exec(http("010_WorkAllocationTask")
        .post("/workallocation/task")
        .body(StringBody("""{"searchRequest": {"search_parameters": [{"key": "user", "operator": "IN", "values":
                           |["#{uId}"]}, {"key": "state", "operator": "IN", "values": ["assigned"]}, {"key":
                           |"jurisdiction", "operator": "IN", "values": ["CIVIL", "PRIVATELAW"]}], "sorting_parameters":
                           |[], "search_by": "judge", "pagination_parameters": {"page_number": 1, "page_size": 25}},
                           |"view": "MyTasks", "refined": false, "currentUser": "#{uId}"}""".stripMargin))
        .headers(Headers.commonHeader)
        .check(substring("Small Claims Track Directions")))

      .exec(http("005_GetJudicialUsers")
        .post("/api/role-access/roles/getJudicialUsers")
        .body(StringBody("""{"userIds": ["#{uId}", "#{uId}", "#{uId}", "#{uId}", "#{uId}", "#{uId}", "#{uId}", "#{uId}",
                           |"#{uId}", "#{uId}", "#{uId}", "#{uId}", "#{uId}", "#{uId}", "#{uId}", "#{uId}", "#{uId}",
                           |"#{uId}", "#{uId}", "#{uId}", "#{uId}", "#{uId}", "#{uId}", "#{uId}", "#{uId}"],
                           |"services": ["CIVIL", "PRIVATELAW"]}""".stripMargin))
        .headers(Headers.commonHeader)
        .check(status.in(200, 406)))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================SEARCH CASE=======================,
    .group("Civil_SpecClaim_89_02_FinalOrder") {
      exec(http("005_SearchCase")
        .get("/aggregated/caseworkers/:uid/jurisdictions?access=read")
        .headers(Headers.commonHeader)
        .check(substring("callback_get_case_url")))

      .exec(http("010_SearchCase")
        .get("/data/internal/case-types/CIVIL/work-basket-inputs")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-workbasket-input-details.v2+json;charset=UTF-8")
        .check(substring("workbasketInputs")))

      .exec(http("015_SearchCase")
        .post("/data/internal/searchCases?ctid=CIVIL&use_case=WORKBASKET&view=WORKBASKET&page=1&case_reference=#{caseId}")
        .headers(Headers.commonHeader)
        .body(StringBody("""{"size": 25}""".stripMargin))
        .check(substring("HEARING_READINESS")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================OPEN CASE=======================,
    .group("Civil_SpecClaim_89_03_FinalOrder") {
      exec(http("005_OpenCase")
        .get("/data/internal/cases/#{caseId}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{caseId}")))
    }

    .group("Civil_SpecClaim_89_03_FinalOrder") {
      exec(http("010_OpenCase")
        .post("/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
        .headers(Headers.commonHeader)
        .check(status.is(204)))
    }

    .group("Civil_SpecClaim_89_03_FinalOrder") {
      exec(http("015_OpenCase")
        .get("/api/wa-supported-jurisdiction/get")
        .headers(Headers.commonHeader)
        .check(substring("CIVIL")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================SELECT MAKE AN ORDER=======================,
    .group("Civil_SpecClaim_89_04_FinalOrder") {
      exec(http("005_MakeAnOrder")
        .get("/workallocation/case/tasks/#{caseId}/event/GENERATE_DIRECTIONS_ORDER/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(Headers.commonHeader)
        .check(substring("task_required_for_event")))

      .exec(http("010_MakeAnOrder")
        .get("/data/internal/profile")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-user-profile.v2+json;charset=UTF-8")
        .check(substring("#{judgeuser}")))

      .exec(http("015_MakeAnOrder")
        .get("/data/internal/cases/#{caseId}/event-triggers/GENERATE_DIRECTIONS_ORDER?ignore-warning=false")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
        .check(substring("GENERATE_DIRECTIONS_ORDER"))
        .check(jsonPath("$.event_token").saveAs("event_token")))

      .exec(http("020_MakeAnOrder")
        .get("/workallocation/case/tasks/#{caseId}/event/GENERATE_DIRECTIONS_ORDER/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(Headers.commonHeader)
        .check(substring("task_required_for_event")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================ORDER SELECT=======================,
    .group("Civil_SpecClaim_89_05_FinalOrder") {
      exec(http("005_OrderSelect")
        .post("/data/case-types/CIVIL/validate?pageId=GENERATE_DIRECTIONS_ORDERFinalOrderSelect")
        .headers(Headers.validateHeader)
        .body(ElFileBody("h_CourtOrder_bodies/judgeOrderSelect.dat"))
        .check(substring("FREE_FORM_ORDER")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================FREE FORM ORDER=======================,
    .group("Civil_SpecClaim_89_06_FinalOrder") {
      exec(http("005_FreeFormOrder")
        .post("/data/case-types/CIVIL/validate?pageId=GENERATE_DIRECTIONS_ORDERFreeFormOrder")
        .headers(Headers.validateHeader)
        .body(ElFileBody("h_CourtOrder_bodies/judgeFreeFormOrder.json"))
        .check(substring("freeFormOrderedTextArea"))
        .check(jsonPath("$.data.finalOrderDocument.documentLink.document_url").saveAs("finalOrderDocument_url"))
        .check(jsonPath("$.data.finalOrderDocument.documentLink.document_hash").saveAs("finalOrderDocument_hash"))
        .check(jsonPath("$.data.finalOrderDocument.documentLink.document_filename").saveAs("finalOrderDocument_filename"))
        .check(jsonPath("$.data.finalOrderDocument.createdDatetime").saveAs("createdDatetime"))
        .check(jsonPath("$.data.finalOrderDocument.documentSize").saveAs("documentSize")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================FINAL ORDER PREVIEW=======================,
    .group("Civil_SpecClaim_89_07_FinalOrder") {
      exec(http("005_FinalOrderPreview")
        .post("/data/case-types/CIVIL/validate?pageId=GENERATE_DIRECTIONS_ORDERFinalOrderPreview")
        .headers(Headers.validateHeader)
        .body(ElFileBody("h_CourtOrder_bodies/judgeFinalOrderPreview.json"))
        .check(substring("finalOrderDocument")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================ORDER SUBMIT=======================,
    .group("Civil_SpecClaim_89_08_FinalOrder") {
      exec(http("005_SubmitOrder")
        .post("/data/cases/#{caseId}/events")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
        .body(ElFileBody("h_CourtOrder_bodies/judgeSubmit.json"))
        .check(substring("The order has been sent to")))

      .exec(http("005_InternalCases")
        .get("/data/internal/cases/#{caseId}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{caseId}")))
    }
    .pause(MinThinkTime, MaxThinkTime)
}
