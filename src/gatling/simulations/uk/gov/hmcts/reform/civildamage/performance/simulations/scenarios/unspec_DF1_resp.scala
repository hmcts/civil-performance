package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils._
import utils.unspec_DF1_Resp_Headers._
import scala.concurrent.duration.DurationInt

object unspec_DF1_resp{

  val BaseURL = Environment.baseURL
  val IdamURL = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime


  val DF_Resp =

    // ========================LANDING PAGE=====================,
    group("Civil_UnSpecClaim_20_DefResp") {
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

    // ========================ASSIGN CASE TO RESPONDENT=====================
    .group("Civil_UnSpecClaim_20_DefResp") {
      exec(http("CIVIL_AssignCase_000_AssignCase")
        .post("http://civil-service-perftest.service.core-compute-perftest.internal/" +
          "testing-support/assign-case/#{caseId}/RESPONDENTSOLICITORONE")
        .header("Authorization", "Bearer #{auth_token}")
        .header("Content-Type", "application/json")
        .header("Accept", "*/*")
        .check(status.in(200, 201)))
    }

    // ========================SEARCH=====================,
    .group("Civil_UnSpecClaim_20_DefResp") {
      exec(http("Search_005_WorkBasket")
        .get("/data/internal/case-types/CIVIL/work-basket-inputs")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-workbasket-input-details.v2+json;charset=UTF-8")
        .check(substring("workbasketInputs")))

      .exec(http("Search_010_CaseReference")
        .post("/data/internal/searchCases?ctid=CIVIL&use_case=WORKBASKET&view=WORKBASKET&page=1&case_reference=#{caseId}")
        .headers(Headers.commonHeader)
        .body(StringBody("""{"size": 25}""".stripMargin))
        .check(substring("AWAITING_RESPONDENT_ACKNOWLEDGEMENT")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ========================OPEN CASE========================,
    .group("Civil_UnSpecClaim_20_DefResp") {
      exec(http("OpenCase_005_InternalCases")
        .get("/data/internal/cases/#{caseId}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("Awaiting Defendant Response")))
    }

    .group("Civil_UnSpecClaim_20_DefResp") {
      exec(http("OpenCase_010_RoleAssignment")
        .post("/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
        .headers(Headers.commonHeader)
        .check(status.is(204)))
    }

    .group("Civil_UnSpecClaim_20_DefResp") {
      exec(http("OpenCase_015_Jurisdiction")
        .get("/api/wa-supported-jurisdiction/get")
        .headers(Headers.commonHeader)
        .check(substring("CIVIL")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ========================RESPONSD TO CLAIM========================,
    .group("Civil_UnSpecClaim_20_DefResp") {
      exec(http("RespToClaim_005_jurisdiction")
        .get("/workallocation/case/tasks/#{caseId}/event/DEFENDANT_RESPONSE/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(Headers.commonHeader)
        .check(substring("task_required_for_event")))

      .exec(http("RespToClaim_010_Profile")
        .get("/data/internal/profile")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-user-profile.v2+json;charset=UTF-8")
        .check(substring("#{LoginId}")))
      .pause(45)

      .exec(http("RespToClaim_015_IgnoreWarning")
        .get("/data/internal/cases/#{caseId}/event-triggers/DEFENDANT_RESPONSE?ignore-warning=false")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
        //.check(status.in(200, 304))
        .check(substring("DEFENDANT_RESPONSE"))
        .check(jsonPath("$.event_token").saveAs("event_token"))
        .check(jsonPath("$.case_fields[75].value.partyID").saveAs("PartyID")))

      .exec(http("RespToClaim_020_jurisdiction")
        .get("/workallocation/case/tasks/#{caseId}/event/DEFENDANT_RESPONSE/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(Headers.commonHeader)
        .check(substring("task_required_for_event")))
    }

    //.exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("xsrf_token")))

    .pause(MinThinkTime, MaxThinkTime)

    // ========================CLAIM INFO========================,
    .group("Civil_UnSpecClaim_20_DefResp") {
      exec(http("ClaimInfo_005_ConfirmDetails")
        .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEConfirmDetails")
        .headers(Headers.validateHeader)
        //.header("X-Xsrf-Token", "#{xsrf_token}")
        .body(ElFileBody("ub_unspec_DF_resp_bodies/respondentDetails.dat"))
        .check(substring("respondent1ResponseDeadline")))
    }
    .pause(MinThinkTime, MaxThinkTime)
    // ========================REJECT CLAIM========================,
    .group("Civil_UnSpecClaim_20_DefResp") {
      exec(http("RejectClaim_005_RespondentResponseType")
        .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSERespondentResponseType")
        .headers(Headers.validateHeader)
        .body(ElFileBody("ub_unspec_DF_resp_bodies/respondentReject.dat"))
        .check(substring("FULL_DEFENCE")))
    }
    .pause(MinThinkTime, MaxThinkTime)
    // ========================FILE REF========================,
    .group("Civil_UnSpecClaim_20_DefResp") {
      exec(http("FileRef_005_SolicitorReferences")
        .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSESolicitorReferences")
        .headers(Headers.validateHeader)
        .body(ElFileBody("ub_unspec_DF_resp_bodies/respondentReferences.dat"))
        .check(substring("solicitorReferences")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ========================UPLOAD DEFENCE========================,
    .group("Civil_UnSpecClaim_20_DefResp") {
      exec(http("005_UploadDefence")
        .post("/documentsv2")
        .headers(Headers.commonHeader)
        .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundaryIY0EwqjYau28ZwLd")
        .bodyPart(RawFileBodyPart("files", "DF_upload.docx").fileName("DF_upload.docx")
          .transferEncoding("binary")).asMultipartForm
        .formParam("classification", "PUBLIC")
        .formParam("caseTypeId", "CIVIL")
        .formParam("jurisdictionId", "CIVIL")
        .check(jsonPath("$.documents[0].hashToken").saveAs("DF_HashToken"))
        .check(jsonPath("$.documents[0]._links.self.href").saveAs("DF_Document_url"))
        .check(substring("DF_upload.docx")))
    }
    .pause(10)

    .group("Civil_UnSpecClaim_20_DefResp") {
      exec(http("010_UploadDefence")
        .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEUpload")
        .headers(Headers.validateHeader)
        .body(ElFileBody("ub_unspec_DF_resp_bodies/respondentUpload.dat"))
        .check(substring("respondent1ClaimResponseDocument")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ========================DIRECTIONS QUESTIONNAIRE========================,
    .group("Civil_UnSpecClaim_20_DefResp") {
      exec(http("005_FileDirectionsQuestionnaire")
        .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEFileDirectionsQuestionnaire")
        .headers(Headers.validateHeader)
        .body(ElFileBody("ub_unspec_DF_resp_bodies/respondentQuestionnaire.dat"))
        .check(substring("respondent1DQFileDirectionsQuestionnaire")))
    }
    .pause(MinThinkTime, MaxThinkTime)
    // ========================FIXED RECOVER COST========================,
    .group("Civil_UnSpecClaim_20_DefResp") {
      exec(http("005_FixedRecoverableCosts")
        .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEFixedRecoverableCosts")
        .headers(Headers.validateHeader)
        .body(ElFileBody("ub_unspec_DF_resp_bodies/respondentFixedRecoverable.dat"))
        .check(substring("respondent1DQFixedRecoverableCosts")))
    }
    .pause(MinThinkTime, MaxThinkTime)
    // ========================DISCLOSE NON ELECTRONIC DOC========================,
    .group("Civil_UnSpecClaim_20_DefResp") {
      exec(http("005_DisclosureOfNonElectronicDocuments")
        .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEDisclosureOfNonElectronicDocuments")
        .headers(Headers.validateHeader)
        .body(ElFileBody("ub_unspec_DF_resp_bodies/respondentNonElectronic.dat"))
        .check(substring("respondent1DQDisclosureOfNonElectronicDocuments")))
    }
    .pause(MinThinkTime, MaxThinkTime)
    // ========================EXPERT========================,
    .group("Civil_UnSpecClaim_20_DefResp") {
      exec(http("005_RESPONSEExperts")
        .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEExperts")
        .headers(Headers.validateHeader)
        .body(ElFileBody("ub_unspec_DF_resp_bodies/respondentExperts.dat"))
        .check(substring("respondent1DQExperts")))
    }
    .pause(MinThinkTime, MaxThinkTime)
    // ========================WITNESS========================,
    .group("Civil_UnSpecClaim_20_DefResp") {
      exec(http("005_RESPONSEWitnesses")
        .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEWitnesses")
        .headers(Headers.validateHeader)
        .body(ElFileBody("ub_unspec_DF_resp_bodies/respondentWitnesses.dat"))
        .check(substring("respondent1DQWitnesses")))
    }
    .pause(MinThinkTime, MaxThinkTime)
    // ========================LANGUAGE========================,
    .group("Civil_UnSpecClaim_20_DefResp") {
      exec(http("005_RESPONSELanguage")
        .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSELanguage")
        .headers(Headers.validateHeader)
        .body(ElFileBody("ub_unspec_DF_resp_bodies/respondentLanguage.dat"))
        .check(substring("respondent1DQLanguage")))
    }
    .pause(MinThinkTime, MaxThinkTime)
    // ========================HEARING========================,
    .group("Civil_UnSpecClaim_20_DefResp") {
      exec(http("005_RESPONSEHearing")
        .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEHearing")
        .headers(Headers.validateHeader)
        .body(ElFileBody("ub_unspec_DF_resp_bodies/respondentHearingAvailability.dat"))
        .check(substring("respondent1DQHearing")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ========================DRAFT DIRECTION========================,
    .group("Civil_UnSpecClaim_20_DefResp") {
      exec(http("005_UploadDraftDirection")
        .post("/documentsv2")
        .headers(Headers.commonHeader)
        .header("content-type", "multipart/form-data; boundary=----263708296816916542312907778465")
        .bodyPart(RawFileBodyPart("files", "DF_upload.docx").fileName("DF_upload.docx")
          .transferEncoding("binary")).asMultipartForm
        .formParam("classification", "PUBLIC")
        .formParam("caseTypeId", "CIVIL")
        .formParam("jurisdictionId", "CIVIL")
        .check(jsonPath("$.documents[0].hashToken").saveAs("DF_DraftHashToken"))
        .check(jsonPath("$.documents[0]._links.self.href").saveAs("DF_DraftDocument_url"))
        .check(substring("DF_upload.docx")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    .group("Civil_UnSpecClaim_20_DefResp") {
      exec(http("005_RESPONSEDraftDirections")
        .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEDraftDirections")
        .headers(Headers.validateHeader)
        .body(ElFileBody("ub_unspec_DF_resp_bodies/respondentDraftDirections.dat"))
        .check(substring("respondent1DQDraftDirections")))
    }
    .pause(MinThinkTime, MaxThinkTime)


    // ========================COURT LOCATION========================,
    .group("Civil_UnSpecClaim_20_DefResp") {
      exec(http("005_RESPONSERequestedCourt")
        .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSERequestedCourt")
        .headers(Headers.validateHeader)
        .body(ElFileBody("ub_unspec_DF_resp_bodies/respondentCourtLocation.dat"))
        .check(substring("requestHearingAtSpecificCourt")))
    }
    .pause(MinThinkTime, MaxThinkTime)
    // ========================ACCESS NEEDS========================,
    .group("Civil_UnSpecClaim_20_DefResp") {
      exec(http("005_RESPONSEHearingSupport")
        .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEHearingSupport")
        .headers(Headers.validateHeader)
        .body(ElFileBody("ub_unspec_DF_resp_bodies/respondentHearingSupport.dat"))
        .check(substring("respondent1DQHearingSupport")))
    }
    .pause(MinThinkTime, MaxThinkTime)
    // ========================VULNERABILITY========================,
    .group("Civil_UnSpecClaim_20_DefResp") {
      exec(http("005_RESPONSEVulnerabilityQuestions")
        .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEVulnerabilityQuestions")
        .headers(Headers.validateHeader)
        .body(ElFileBody("ub_unspec_DF_resp_bodies/respondentVulnerability.dat"))
        .check(substring("respondent1DQVulnerabilityQuestions")))
    }
    .pause(MinThinkTime, MaxThinkTime)
    // ========================FUTURE APPLICATION========================,
    .group("Civil_UnSpecClaim_20_DefResp") {
      exec(http("005_RESPONSEFurtherInformation")
        .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEFurtherInformation")
        .headers(Headers.validateHeader)
        .body(ElFileBody("ub_unspec_DF_resp_bodies/respondentFurtherInformation.dat"))
        .check(substring("respondent1DQFurtherInformation\"")))
    }
    .pause(MinThinkTime, MaxThinkTime)
    // ========================DEF DETAILS========================,
    .group("Civil_UnSpecClaim_20_DefResp") {
      exec(http("005_RESPONSEStatementOfTruth")
        .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEStatementOfTruth")
        .headers(Headers.validateHeader)
        .body(ElFileBody("ub_unspec_DF_resp_bodies/respondentStatementOfTruth.dat"))
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/case-types/CIVIL/validate" +
          "?pageId=DEFENDANT_RESPONSEStatementOfTruth")))
    }
    .pause(MinThinkTime, MaxThinkTime)
    // ========================SOT SUBMIT===================,
    .group("Civil_UnSpecClaim_20_DefResp") {
      exec(http("005_Submit")
        .post("/data/cases/#{caseId}/events")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
        .body(ElFileBody("ub_unspec_DF_resp_bodies/respondentSubmitDefence.dat"))
        .check(substring("You have submitted the Defendant's defence")))

      .exec(http("010_DefenceSubmitted")
        .get("/data/internal/cases/#{caseId}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{caseId}")))
    }
    .pause(MinThinkTime, MaxThinkTime)

}