package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.uf_ug_uh_unspec_Headeers._
import uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios.utils._

import scala.concurrent.duration.DurationInt

object unspec_DF_CL_FinalOrder {

   val BaseURL = Environment.baseURL
   val IdamURL = Environment.idamURL
   val MinThinkTime = Environment.minThinkTime
   val MaxThinkTime = Environment.maxThinkTime


val DF_upload =

      // =======================OPEN CASE=======================,
      group("Civil_UnSpecClaim_60_DocUpload") {
        exec(http("request_28")
          .get("/data/internal/cases/#{caseId}")
          .headers(headers_28))
      }
      .pause(5)
      // =======================DF UPLOAD DOC=======================,
      .exec(http("request_31")
        .get("/workallocation/case/tasks/#{caseId}/event/EVIDENCE_UPLOAD_RESPONDENT/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(headers_31))
      .exec(http("request_32")
        .get("/data/internal/profile")
        .headers(headers_32))
      .exec(http("request_33")

        .get("/data/internal/cases/#{caseId}/event-triggers/EVIDENCE_UPLOAD_RESPONDENT?ignore-warning=false")
        .headers(headers_33)
        .check(jsonPath("$.event_token").saveAs("event_token")))

        .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("xsrf_token")))

      .exec(http("request_34")
        .get("/workallocation/case/tasks/#{caseId}/event/EVIDENCE_UPLOAD_RESPONDENT/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(headers_34))
      .pause(5)


      // =======================HOW TO UPLOAD=======================,
      .exec(http("request_35")
        .post("/data/case-types/CIVIL/validate?pageId=EVIDENCE_UPLOAD_RESPONDENTEvidenceUpload")
        .headers(headers_35)
        .body(ElFileBody("uf_ug_uh_orders_bodes/0035_request.dat")))
      .pause(5)


      // =======================DF WITNESS STATEMENT=======================,
      .exec(http("request_36")
        .post("/data/case-types/CIVIL/validate?pageId=EVIDENCE_UPLOAD_RESPONDENTDocumentSelectionFastTrack")
        .headers(headers_36)
        .body(ElFileBody("uf_ug_uh_orders_bodes/0036_request.dat")))

      .pause(5)


      // =======================DF UPLOAD WITNESS STATEMENT=======================,
      .exec(http("request_37")
        .post("/documentsv2")
        .headers(headers_37)
          .header("content-type", "multipart/form-data; boundary=----263708296816916542312907778465")
          .bodyPart(RawFileBodyPart("files", "WitnessStatement.docx")
          .fileName("WitnessStatement.docx")
          .transferEncoding("binary"))
          .asMultipartForm
          .formParam("classification", "PUBLIC")
          .formParam("caseTypeId", "CIVIL")
          .formParam("jurisdictionId", "CIVIL")
          .check(jsonPath("$.documents[0].hashToken").saveAs("DF_HashToken"))
          .check(jsonPath("$.documents[0]._links.self.href").saveAs("DF_StatementDocument_url"))
          .check(substring("WitnessStatement.docx")))
      .pause(5)


      .exec(http("request_38")
        .post("/data/case-types/CIVIL/validate?pageId=EVIDENCE_UPLOAD_RESPONDENTDocumentUpload")
        .headers(headers_38)
        .body(ElFileBody("uf_ug_uh_orders_bodes/0038_request.dat")))
      .pause(5)

      // =======================DF SUBMIT=======================,
      .exec(http("request_39")
        .post("/data/cases/#{caseId}/events")
        .headers(headers_39)
        .body(ElFileBody("uf_ug_uh_orders_bodes/0039_request.dat")))
      .exec(http("request_40")
        .get("/data/internal/cases/#{caseId}")
        .headers(headers_40))
      .pause(5)



   val CL_upload =
      // =======================SEARCH CASE=======================,
//      exec(http("request_61")
//        .post("/data/internal/searchCases?ctid=CIVIL&use_case=WORKBASKET&view=WORKBASKET&page=1&case_reference=#{caseId}&case.CaseAccessCategory=UNSPEC_CLAIM")
//        .headers(headers_61)
//        .body(ElFileBody("uf_ug_uh_orders_bodes/0061_request.json")))
//      .pause(5)

      // =======================OPEN CASE=======================,
      exec(http("request_62")
        .get("/data/internal/cases/#{caseId}")
        .headers(headers_62))
      .pause(169.milliseconds)
      .exec(http("request_63")
        .get("/api/wa-supported-jurisdiction/get")
        .headers(headers_63))
      .exec(http("request_64")
        .post("/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
        .headers(headers_64)
        .body(ElFileBody("uf_ug_uh_orders_bodes/0064_request.bin")))
      .pause(5)

      // =======================SELECT UPLOAD DOC=======================,
      .exec(http("request_65")
        .get("/workallocation/case/tasks/#{caseId}/event/EVIDENCE_UPLOAD_APPLICANT/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(headers_65))
      .exec(http("request_66")
        .get("/data/internal/profile")
        .headers(headers_66))
      .exec(http("request_67")
        .get("/data/internal/cases/#{caseId}/event-triggers/EVIDENCE_UPLOAD_APPLICANT?ignore-warning=false")
        .headers(headers_67)
        .check(jsonPath("$.event_token").saveAs("event_token")))

        .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("xsrf_token")))

      .exec(http("request_68")
        .get("/workallocation/case/tasks/#{caseId}/event/EVIDENCE_UPLOAD_APPLICANT/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(headers_68))
      .pause(5)


      // =======================HOW TO UPLOAD=======================,
      .exec(http("request_69")
        .post("/data/case-types/CIVIL/validate?pageId=EVIDENCE_UPLOAD_APPLICANTEvidenceUpload")
        .headers(headers_69)
        .body(ElFileBody("uf_ug_uh_orders_bodes/0069_request.dat")))
      .pause(5)

      // =======================CL WITNESS STATE=======================,
      .exec(http("request_70")
        .post("/data/case-types/CIVIL/validate?pageId=EVIDENCE_UPLOAD_APPLICANTDocumentSelectionFastTrack")
        .headers(headers_70)
        .body(ElFileBody("uf_ug_uh_orders_bodes/0070_request.dat")))

      .pause(5)

      // =======================CL UPLOAD DOC=======================,
      .exec(http("request_71")
        .post("/documentsv2")
        .headers(headers_71)
//        .body(ElFileBody("uf_ug_uh_orders_bodes/0071_request.json")))
        .header("content-type", "multipart/form-data; boundary=----263708296816916542312907778465")
        .bodyPart(RawFileBodyPart("files", "WitnessStatement.docx")
          .fileName("WitnessStatement.docx")
          .transferEncoding("binary"))
        .asMultipartForm
        .formParam("classification", "PUBLIC")
        .formParam("caseTypeId", "CIVIL")
        .formParam("jurisdictionId", "CIVIL")
        .check(jsonPath("$.documents[0].hashToken").saveAs("CL_HashToken"))
        .check(jsonPath("$.documents[0]._links.self.href").saveAs("CL_StatementDocument_url"))
        .check(substring("WitnessStatement.docx")))
        .pause(5)
      .pause(5)


      .exec(http("request_72")
        .post("/data/case-types/CIVIL/validate?pageId=EVIDENCE_UPLOAD_APPLICANTDocumentUpload")
        .headers(headers_72)
        .body(ElFileBody("uf_ug_uh_orders_bodes/0072_request.dat")))
      .pause(5)

      // =======================SUBMIT DOC=======================,
      .exec(http("request_73")
        .post("/data/cases/#{caseId}/events")
        .headers(headers_73)
        .body(ElFileBody("uf_ug_uh_orders_bodes/0073_request.dat")))
      .exec(http("request_74")
        .get("/data/internal/cases/#{caseId}")
        .headers(headers_74))
      .pause(5)


val FinalOrder =
      // =======================GO TO CASE LIST=======================,
      exec(http("request_100")
        .get("/auth/isAuthenticated")
        .headers(headers_100))

      .exec(http("request_103")
        .get("/api/organisation")
        .headers(headers_103)
        .check(status.is(403)))
      .exec(http("request_104")
        .get("/aggregated/caseworkers/:uid/jurisdictions?access=read")
        .headers(headers_104))
      .exec(http("request_105")
        .post("/data/internal/searchCases?ctid=CIVIL&use_case=WORKBASKET&view=WORKBASKET&page=1&case_reference=#{caseId}&case.CaseAccessCategory=UNSPEC_CLAIM")
        .headers(headers_105)
        .body(ElFileBody("uf_ug_uh_orders_bodes/0105_request.json")))

      .pause(5)
      // =======================SEARCH CASE=======================,
      .exec(http("request_109")
        .post("/data/internal/searchCases?ctid=CIVIL&use_case=WORKBASKET&view=WORKBASKET&page=1&case_reference=#{caseId}&case.CaseAccessCategory=UNSPEC_CLAIM")
        .headers(headers_109)
        .body(ElFileBody("uf_ug_uh_orders_bodes/0109_request.json")))

      .pause(5)
      // =======================OPEN CASE=======================,
      .exec(http("request_112")
        .get("/data/internal/cases/#{caseId}")
        .headers(headers_112))
      .pause(178.milliseconds)
      .exec(http("request_113")
        .get("/api/wa-supported-jurisdiction/get")
        .headers(headers_113))

      .exec(http("request_115")
        .post("/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
        .headers(headers_115)
        .body(ElFileBody("uf_ug_uh_orders_bodes/0115_request.bin")))

      // =======================TASK TAB=======================,
      .exec(http("request_120")
        .post("/workallocation/case/task/#{caseId}")
        .headers(headers_120)
        .body(ElFileBody("uf_ug_uh_orders_bodes/0120_request.json")))
      .pause(5)


      // =======================SELECT MAKE AN ORDER=======================,

      .exec(http("request_135")
        .get("/workallocation/case/tasks/#{caseId}/event/GENERATE_DIRECTIONS_ORDER/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(headers_135))
      .exec(http("request_136")
        .get("/data/internal/profile")
        .headers(headers_136))
      .pause(472.milliseconds)

      .exec(http("request_139")
        .get("/data/internal/cases/#{caseId}/event-triggers/GENERATE_DIRECTIONS_ORDER?ignore-warning=false")
        .headers(headers_139)
        .check(substring("GENERATE_DIRECTIONS_ORDER"))
        .check(jsonPath("$.event_token").saveAs("event_token")))

      .pause(141.milliseconds)
      .exec(http("request_140")
        .get("/workallocation/case/tasks/#{caseId}/event/GENERATE_DIRECTIONS_ORDER/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(headers_140))

      // =======================SELECT TRACK=======================,

      .exec(http("request_147")
        .post("/data/case-types/CIVIL/validate?pageId=GENERATE_DIRECTIONS_ORDERTrackAllocation")
        .headers(headers_147)
        .body(ElFileBody("uf_ug_uh_orders_bodes/0147_request.dat")))
      .pause(5)

      // =======================FREE FORM ORDER=======================,

      .exec(http("request_152")
        .post("/data/case-types/CIVIL/validate?pageId=GENERATE_DIRECTIONS_ORDERFinalOrderSelect")
        .headers(headers_152)
        .body(ElFileBody("uf_ug_uh_orders_bodes/0152_request.dat")))
      .pause(394.milliseconds)

      // =======================MAKE AN ORDER=======================,

      .exec(http("request_166")
        .post("/data/case-types/CIVIL/validate?pageId=GENERATE_DIRECTIONS_ORDERFreeFormOrder")
        .headers(headers_166)
        .body(ElFileBody("uf_ug_uh_orders_bodes/0166_request.dat"))
        .check(jsonPath("$.data.finalOrderDocument.documentLink.document_url").saveAs("finalOrderDocument_url"))
        .check(jsonPath("$.data.finalOrderDocument.documentLink.document_hash").saveAs("finalOrderDocument_hash"))
        .check(jsonPath("$.data.finalOrderDocument.documentLink.document_filename").saveAs("finalOrderDocument_filename"))
        .check(jsonPath("$.data.finalOrderDocument.createdDatetime").saveAs("createdDatetime"))
        .check(jsonPath("$.data.finalOrderDocument.documentSize").saveAs("documentSize")))
        .pause(2)

      .pause(5)

      // =======================ORDR DOC=======================,
      .exec(http("request_172")
        .post("/data/case-types/CIVIL/validate?pageId=GENERATE_DIRECTIONS_ORDERFinalOrderPreview")
        .headers(headers_172)
        .body(ElFileBody("uf_ug_uh_orders_bodes/0172_request.dat")))
      .pause(5)

      // =======================ORDER SUBMIT=======================,

      .exec(http("request_177")
        .post("/data/cases/#{caseId}/events")
        .headers(headers_177)
        .body(ElFileBody("uf_ug_uh_orders_bodes/0177_request.dat")))
      .pause(833.milliseconds)

      .pause(5)
       
}
