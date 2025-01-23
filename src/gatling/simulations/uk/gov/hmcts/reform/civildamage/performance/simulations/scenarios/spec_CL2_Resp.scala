package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
//import uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios.utils.Common
import utils._
import scala.concurrent.duration.DurationInt
import utils.spec_CL2_Headers._

object spec_CL2_Resp {

  val BaseURL = Environment.baseURL
  val IdamURL = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val GoToCase =

    exec(http("G_request_30")
      .get("/aggregated/caseworkers/:uid/jurisdictions?access=read")
      .headers(headers_30))
      .pause(117.milliseconds)

  .doIf("#{caseId.isUndefined()}") {
    exec(_.set("caseId", "0"))
  }
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
      // ======================OPEN CASE=============================,
      .exec(http("G_OPEN CASE")
        .get("/data/internal/cases/#{caseId}")
        .headers(headers_28))
      .pause(196.milliseconds)

//      .exec(http("G_request_29")
//        .get("/api/wa-supported-jurisdiction/get")
//        .headers(headers_29))
//
//      .exec(http("G_request_30")
//        .post("/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
//        .headers(headers_30)
//        .body(ElFileBody("g_ClaimResp_bodies/0030_request.bin")))
//      .pause(18)


    val SelectUpload =
        // =================================SELECT UPLOAD DOCUMENT================,
        exec(http("G_SELECT UPLOAD DOCUMENT")
          .get("/workallocation/case/tasks/#{caseId}/event/EVIDENCE_UPLOAD_APPLICANT/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(headers_31))

          .exec(http("UPLOAD DOCUMENT-Warning")
            .get("/data/internal/cases/#{caseId}/event-triggers/EVIDENCE_UPLOAD_APPLICANT?ignore-warning=false")
            .headers(headers_32).check(jsonPath("$.event_token").saveAs("event_token")))

          .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("xsrf_token")))

          // ============HOW TO UPLOAD============,

          .exec(http("HOW TO UPLOAD")
            .post("/data/case-types/CIVIL/validate?pageId=EVIDENCE_UPLOAD_APPLICANTEvidenceUpload")
            .headers(headers_35).header("X-Xsrf-Token", "#{xsrf_token}")
            .body(ElFileBody("g_ClaimResp_bodies/claimantHowToUpload.dat")))
          .pause(12)

          // ==========WITNESS STATEMENT========,
          .exec(http("EVIDENCE_UPLOAD_APPLICANTDocumentSelectionFastTrack")
            .post("/data/case-types/CIVIL/validate?pageId=EVIDENCE_UPLOAD_APPLICANTDocumentSelectionFastTrack")
            .headers(headers_35).header("X-Xsrf-Token", "#{xsrf_token}")
            .body(ElFileBody("g_ClaimResp_bodies/claimantWitnessStatement.dat"))
          )
          .pause(19)

          //===========- Disclosure List Upload CL
          .exec(http("1documentsv2")
            .post("/documentsv2")
            .headers(headers_37).header("X-Xsrf-Token", "#{xsrf_token}")
//            .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundaryzGvBAZBXDDUaRq3D")
            .header("content-type", "multipart/form-data; boundary=----263708296816916542312907778465")
            .bodyPart(RawFileBodyPart("files", "WitnessStatement.docx")
            .fileName("WitnessStatement.docx")
            .transferEncoding("binary"))
            .asMultipartForm
            .formParam("classification", "PUBLIC")
            .formParam("caseTypeId", "CIVIL")
            .formParam("jurisdictionId", "CIVIL")
            .check(jsonPath("$.documents[0].hashToken").saveAs("CL_WitnessStatementHashToken"))
            .check(jsonPath("$.documents[0]._links.self.href").saveAs("CL_WitnessStatementDocument_url"))
            .check(substring("WitnessStatement.docx"))
  )


          // =================SUBMIT================,
          .exec(http("2SUBMIT")
            .post("/data/case-types/CIVIL/validate?pageId=EVIDENCE_UPLOAD_APPLICANTDocumentUpload")
            .headers(headers_38)
//            .headers(headers_37)
            .header("X-Xsrf-Token", "#{xsrf_token}")
            .body(ElFileBody("g_ClaimResp_bodies/claimantSubmit.dat")))
          .pause(11)

}