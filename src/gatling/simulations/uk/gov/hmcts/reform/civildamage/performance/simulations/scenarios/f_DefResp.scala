package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios.utils.Common
import utils._

import scala.concurrent.duration.DurationInt
import utils.spec_DF2_Headers._

object f_DefResp{


  val BaseURL = Environment.baseURL
  val IdamURL = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

val GoToCase =
    // =============================GO TO CASE LIST===================,

  exec(http("F_request_30")
      .get("/aggregated/caseworkers/:uid/jurisdictions?access=read")
      .headers(headers_30))
    .pause(117.milliseconds)

      .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("xsrf_token")))

      .pause(20)

      .exec(_.setAll(
          "CaseProgRandomString" -> Common.randomString(5),
          "Plus2WeeksDay" -> Common.getPlus2WeeksDay(),
          "Plus2WeeksMonth" -> Common.getPlus2WeeksMonth(),
          "Plus2WeeksYear" -> Common.getPlus2WeeksYear(),
          "EvidenceYear" -> Common.getYear(),
          "EvidenceDay" -> Common.getDay(),
          "EvidenceMonth" -> Common.getMonth(),
          "CaseProgRandomString" -> Common.randomString(5))
      )


    // ==================================OPEN CASE====================,

    .exec(http("F_request_79")
      .get("/data/internal/cases/#{caseId}")
      .headers(headers_79))
    .pause(204.milliseconds)
    .pause(12)


    // =====================SELECT UPLOAD ====================,

val SelectUpload =
    exec(http("F_request_100")
      .get("/workallocation/case/tasks/#{caseId}/event/EVIDENCE_UPLOAD_RESPONDENT/caseType/CIVIL/jurisdiction/CIVIL")
      .headers(headers_100))

    .exec(http("F_request_101")
      .get("/data/internal/cases/#{caseId}/event-triggers/EVIDENCE_UPLOAD_RESPONDENT?ignore-warning=false")
      .headers(headers_101).check(substring("EVIDENCE_UPLOAD_RESPONDENT"))
      .check(jsonPath("$.event_token").saveAs("event_token")))

       .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("xsrf_token")))

    // ================================================CONTINUE=================,

    .exec(http("F_request_118")
      .post("/data/case-types/CIVIL/validate?pageId=EVIDENCE_UPLOAD_RESPONDENTEvidenceUpload")
      .headers(headers_118)
      .header("X-Xsrf-Token", "#{xsrf_token}")
      .body(ElFileBody("f_DefResp_bodies/0118_request.dat")).check(substring("caseProgAllocatedTrack")))
    .pause(241.milliseconds)

      //==============Select the type of document==================
    .exec(http("F_request_138")
        .post("/data/case-types/CIVIL/validate?pageId=EVIDENCE_UPLOAD_RESPONDENTDocumentSelectionFastTrack")
        .headers(headers_118)
        .header("X-Xsrf-Token", "#{xsrf_token}")
      . body(ElFileBody("f_DefResp_bodies/0138_request.dat")))
        .pause(491.milliseconds)


      //===========- Witness Document  Upload Def
      .exec(http("CivilCaseProg_UploadEvidence_243_005_WitnessStatement")
        .post(BaseURL + "/documentsv2")
        .headers(headers_186).header("X-Xsrf-Token", "#{xsrf_token}")
//        .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundary1cdrHU4TGOqco6W7")
        .header("content-type", "multipart/form-data; boundary=----263708296816916542312907778465")
        .bodyPart(RawFileBodyPart("files", "WitnessStatement.docx")
        .fileName("WitnessStatement.docx")
        .transferEncoding("binary"))
        .asMultipartForm
        .formParam("classification", "PUBLIC")
        .formParam("caseTypeId", "CIVIL")
        .formParam("jurisdictionId", "CIVIL")
        .check(jsonPath("$.documents[0].hashToken").saveAs("DF_WitnessStatementHashToken"))
        .check(jsonPath("$.documents[0]._links.self.href").saveAs("DF_WitnessStatementDocument_url"))
        .check(substring("WitnessStatement.docx"))
      )
        .pause(218.milliseconds)


//=========================- Witness Statement Upload DOC================
    .exec(http("EVIDENCE_UPLOAD_RESPONDENTDocumentUpload")
      .post("/data/case-types/CIVIL/validate?pageId=EVIDENCE_UPLOAD_RESPONDENTDocumentUpload")
      .headers(headers_118).header("X-Xsrf-Token", "#{xsrf_token}")
      .body(ElFileBody("f_DefResp_bodies/0220_request.dat"))
    .check(substring("EVIDENCE_UPLOAD_RESPONDENTDocumentUpload")))
    .pause(563.milliseconds)


      //================================ - Upload Documents Submit Def
    .exec(http("Submit")
      .post("/data/cases/#{caseId}/events")
      .headers(headers_231).header("X-Xsrf-Token", "#{xsrf_token}")
      .body(ElFileBody("f_DefResp_bodies/0231_request.dat"))
      .check(substring("confirmation_body")))
    .pause(338.milliseconds)
}