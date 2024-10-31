package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils._
import utils.ub_unspec_DF_Resp_Headers._
import scala.concurrent.duration.DurationInt

object unspec_DF_resp{

  val BaseURL = Environment.baseURL
  val IdamURL = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime


  val DF_Resp =
    // ========================SEARCH=====================,
  group("Civil_20_UnSpecClaim_DefResp") {
    exec(http("Search_005_CaseReference")
      .post("/data/internal/searchCases?ctid=CIVIL&use_case=WORKBASKET&view=WORKBASKET&page=1&case_reference=#{caseId}")
      .headers(headers_28).header("X-Xsrf-Token", "#{xsrf_token}")
      .body(ElFileBody("ub_unspec_DF_resp_bodies/0028_request.json")))
  }
      .pause(7)

      // ========================OPEN CASE========================,
      .group("Civil_20_UnSpecClaim_DefResp") {
        exec(http("OpenCase_005_InternalCases")
          .get("/data/internal/cases/#{caseId}")
          .headers(headers_29))
      }
      .pause(167.milliseconds)

    .group("Civil_20_UnSpecClaim_DefResp") {
      exec( http("OpenCase_010_RoleAssignment")
        .post("/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
        .headers(headers_30).header("X-Xsrf-Token", "#{xsrf_token}")
        .body(ElFileBody("ub_unspec_DF_resp_bodies/0030_request.bin")))

      .exec( http("OpenCase_015_Jurisdiction")
        .get("/api/wa-supported-jurisdiction/get")
        .headers(headers_31))
        }
      .pause(13)

      // ========================RESPONSD TO CLAIM========================,
      .group("Civil_20_UnSpecClaim_DefResp") {
        exec(http("RespToClaim_005_jurisdiction")
          .get("/workallocation/case/tasks/#{caseId}/event/DEFENDANT_RESPONSE/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(headers_32))

          .exec(http("RespToClaim_010_IgnoreWarning")
            .get("/data/internal/cases/#{caseId}/event-triggers/DEFENDANT_RESPONSE?ignore-warning=false")
            .headers(headers_33)
            .check(status.in(200, 304))
            .check(jsonPath("$.event_token").optional.saveAs("event_token"))
            .check(jsonPath("$.case_fields[75].value.partyID").saveAs("PartyID"))
          )

          .exec(http("RespToClaim_015_profile")
            .get("/data/internal/profile")
            .headers(headers_34))

          .exec(http("RespToClaim_020_jurisdiction")
            .get("/workallocation/case/tasks/#{caseId}/event/DEFENDANT_RESPONSE/caseType/CIVIL/jurisdiction/CIVIL")
            .headers(headers_35))
      }

      .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("xsrf_token")))

      .pause(29)
      // ========================CLAIM INFO========================,
      .group("Civil_20_UnSpecClaim_DefResp") {
        exec(http("ClaimInfo_005_ConfirmDetails")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEConfirmDetails")
          .headers(Headers.validateHeader).header("X-Xsrf-Token", "#{xsrf_token}")
          .body(ElFileBody("ub_unspec_DF_resp_bodies/0036_request.dat")))
      }
      .pause(14)
      // ========================REJECT CLAIM========================,
      .group("Civil_20_UnSpecClaim_DefResp") {
        exec(http("RejectClaim_005_RespondentResponseType")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSERespondentResponseType")
          .headers(Headers.validateHeader).header("X-Xsrf-Token", "#{xsrf_token}")
          .body(ElFileBody("ub_unspec_DF_resp_bodies/0037_request.dat")))
      }
      .pause(10)
      // ========================FILE REF========================,
      .group("Civil_20_UnSpecClaim_DefResp") {
        exec(http("FileRef_005_SolicitorReferences")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSESolicitorReferences")
          .headers(Headers.validateHeader).header("X-Xsrf-Token", "#{xsrf_token}")
          .body(ElFileBody("ub_unspec_DF_resp_bodies/0038_request.dat")))
      }
      .pause(20)

      // ========================UPLOAD DEFENCE========================,
      .group("Civil_20_UnSpecClaim_DefResp") {
        exec(http("005_UploadDefence")
          .post("/documentsv2")
          .headers(headers_39).header("X-Xsrf-Token", "#{xsrf_token}")
          //        .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundary1cdrHU4TGOqco6W7")
          .header("content-type", "multipart/form-data; boundary=----263708296816916542312907778465")
          .bodyPart(RawFileBodyPart("files", "DF_upload.docx").fileName("DF_upload.docx")
            .transferEncoding("binary"))
          .asMultipartForm
          .formParam("classification", "PUBLIC")
          .formParam("caseTypeId", "CIVIL")
          .formParam("jurisdictionId", "CIVIL")
          .check(jsonPath("$.documents[0].hashToken").saveAs("DF_HashToken"))
          .check(jsonPath("$.documents[0]._links.self.href").saveAs("DF_Document_url"))
          .check(substring("DF_upload.docx"))
        )
      }
      .pause(10)

      // ========================DIRECTION QUESTIONAIRE========================,
      .group("Civil_20_UnSpecClaim_DefResp") {
        exec(http("005_FileDirectionsQuestionnaire")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEFileDirectionsQuestionnaire")
          .headers(Headers.validateHeader).header("X-Xsrf-Token", "#{xsrf_token}")
          .body(ElFileBody("ub_unspec_DF_resp_bodies/0041_request.dat")))
      }
      .pause(15)
      // ========================FIXED RECOVER COST========================,
      .group("Civil_20_UnSpecClaim_DefResp") {
        exec(http("005_FixedRecoverableCosts")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEFixedRecoverableCosts")
          .headers(Headers.validateHeader).header("X-Xsrf-Token", "#{xsrf_token}")
          .body(ElFileBody("ub_unspec_DF_resp_bodies/0042_request.dat")))
      }
      .pause(11)
      // ========================DISCLOSE NON ELECTRONIC DOC========================,
      .group("Civil_20_UnSpecClaim_DefResp") {
        exec(http("005_DisclosureOfNonElectronicDocuments")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEDisclosureOfNonElectronicDocuments")
          .headers(Headers.validateHeader).header("X-Xsrf-Token", "#{xsrf_token}")
          .body(ElFileBody("ub_unspec_DF_resp_bodies/0043_request.dat")))
      }
      .pause(8)
      // ========================EXPERT========================,
      .group("Civil_20_UnSpecClaim_DefResp") {
        exec(http("005_RESPONSEExperts")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEExperts")
          .headers(Headers.validateHeader).header("X-Xsrf-Token", "#{xsrf_token}")
          .body(ElFileBody("ub_unspec_DF_resp_bodies/0044_request.dat")))
      }
      .pause(6)
      // ========================WITNESS========================,
      .group("Civil_20_UnSpecClaim_DefResp") {
        exec(http("005_RESPONSEWitnesses")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEWitnesses")
          .headers(Headers.validateHeader).header("X-Xsrf-Token", "#{xsrf_token}")
          .body(ElFileBody("ub_unspec_DF_resp_bodies/0045_request.dat")))
      }
      .pause(10)
      // ========================LANGUAGE========================,
      .group("Civil_20_UnSpecClaim_DefResp") {
        exec(http("005_RESPONSELanguage")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSELanguage")
          .headers(Headers.validateHeader).header("X-Xsrf-Token", "#{xsrf_token}")
          .body(ElFileBody("ub_unspec_DF_resp_bodies/0046_request.dat")))
      }
      .pause(20)
      // ========================HEARING========================,
      .group("Civil_20_UnSpecClaim_DefResp") {
        exec(http("005_RESPONSEHearing")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEHearing")
          .headers(Headers.validateHeader).header("X-Xsrf-Token", "#{xsrf_token}")
          .body(ElFileBody("ub_unspec_DF_resp_bodies/0047_request.dat")))
      }
      .pause(8)


      // ========================DRAFT DIRECTION========================,
      .group("Civil_20_UnSpecClaim_DefResp") {
        exec(http("005_UploadDraftDirection")
          .post("/documentsv2")
          .headers(headers_48).header("X-Xsrf-Token", "#{xsrf_token}")
          .header("content-type", "multipart/form-data; boundary=----263708296816916542312907778465")
          .bodyPart(RawFileBodyPart("files", "DF_upload.docx")
            .fileName("DF_upload.docx")
            .transferEncoding("binary"))
          .asMultipartForm
          .formParam("classification", "PUBLIC")
          .formParam("caseTypeId", "CIVIL")
          .formParam("jurisdictionId", "CIVIL")
          .check(jsonPath("$.documents[0].hashToken").saveAs("DF_DraftHashToken"))
          .check(jsonPath("$.documents[0]._links.self.href").saveAs("DF_DraftDocument_url"))
          .check(substring("DF_upload.docx")))
      }
      .pause(4)


    .group("Civil_20_UnSpecClaim_DefResp") {
      exec(http("005_RESPONSEDraftDirections")
        .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEDraftDirections")
        .headers(Headers.validateHeader).header("X-Xsrf-Token", "#{xsrf_token}")
        .body(ElFileBody("ub_unspec_DF_resp_bodies/0049_request.dat")))
    }
      .pause(27)


      // ========================COURT LOCATION========================,
      .group("Civil_20_UnSpecClaim_DefResp") {
        exec(http("005_RESPONSERequestedCourt")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSERequestedCourt")
          .headers(Headers.validateHeader).header("X-Xsrf-Token", "#{xsrf_token}")
          .body(ElFileBody("ub_unspec_DF_resp_bodies/0050_request.dat")))
      }
      .pause(10)
      // ========================ACCESS NEEDS========================,
      .group("Civil_20_UnSpecClaim_DefResp") {
        exec(http("005_RESPONSEHearingSupport")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEHearingSupport")
          .headers(Headers.validateHeader).header("X-Xsrf-Token", "#{xsrf_token}")
          .body(ElFileBody("ub_unspec_DF_resp_bodies/0051_request.dat")))
      }
      .pause(10)
      // ========================VULNERABILITY========================,
      .group("Civil_20_UnSpecClaim_DefResp") {
        exec(http("005_RESPONSEVulnerabilityQuestions")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEVulnerabilityQuestions")
          .headers(Headers.validateHeader).header("X-Xsrf-Token", "#{xsrf_token}")
          .body(ElFileBody("ub_unspec_DF_resp_bodies/0052_request.dat")))
      }
      .pause(14)
      // ========================FUTURE APPLICTION========================,
      .group("Civil_20_UnSpecClaim_DefResp") {
      exec(http("005_RESPONSEFurtherInformation")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEFurtherInformation")
          .headers(Headers.validateHeader)
          .body(ElFileBody("ub_unspec_DF_resp_bodies/0053_request.dat")))
      }
      .pause(20)
      // ========================DEF DETAILS========================,
      .group("Civil_20_UnSpecClaim_DefResp") {
      exec(http("005_RESPONSEStatementOfTruth")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEStatementOfTruth")
          .headers(Headers.validateHeader).header("X-Xsrf-Token", "#{xsrf_token}")
          .body(ElFileBody("ub_unspec_DF_resp_bodies/0054_request.dat")))
      }
      .pause(10)
      // ========================SOT SUBMIT===================,
      .group("Civil_20_UnSpecClaim_DefResp") {
        exec(http("005_Submit")
          .post("/data/cases/#{caseId}/events")
          .headers(headers_55).header("X-Xsrf-Token", "#{xsrf_token}")
          .body(ElFileBody("ub_unspec_DF_resp_bodies/0055_request.dat")))
      }
      .pause(23)

}