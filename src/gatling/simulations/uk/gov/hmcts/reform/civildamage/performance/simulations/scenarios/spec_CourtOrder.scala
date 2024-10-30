package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.Environment._

import scala.concurrent.duration.DurationInt
import io.gatling.core.Predef.ElFileBody
import utils._
import utils.h_CourtOrder_Headers._

object spec_CourtOrder{

  val BaseURL = Environment.baseURL
  val IdamURL = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime


    // =================================================SEARCH CASE====================,
//    .exec(http("H_request_41")
//      .get("/data/internal/case-types/GENERALAPPLICATION/work-basket-inputs")
//      .headers(headers_41))
//      .pause(118.milliseconds)
//
//    .exec(http("H_request_42")
//      .get("/data/internal/case-types/CIVIL/work-basket-inputs")
//      .headers(headers_42))
//      .pause(11)

    val GoToCase =

  exec(_.setAll(
    "CaseProgRandomString" -> Common.randomString(5),
    "EvidenceYear" -> Common.getCurrentYear(),
    "EvidenceMonth" -> Common.getCurrentMonth(),
    "EvidenceDay" -> Common.getCurrentDay())
  )



    .exec(http("H_request_43")
      .post("/data/internal/searchCases?ctid=CIVIL&use_case=WORKBASKET&view=WORKBASKET&page=1&case_reference=#{caseId}&case.CaseAccessCategory=SPEC_CLAIM")
      .headers(headers_43)
      .body(ElFileBody("h_CourtOrder_bodies/0043_request.json")))



    // ============================OPEN CASE===================,
    .exec(http("H_request_46")
      .get("/data/internal/cases/#{caseId}")
      .headers(headers_46))
      .pause(156.milliseconds)

    .exec(http("H_request_47")
      .get("/api/wa-supported-jurisdiction/get")
      .headers(headers_47))

    .exec(http("H_request_49")
      .post("/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
      .headers(headers_49)
      .body(ElFileBody("h_CourtOrder_bodies/0049_request.bin")))


val MakeAnOrder =
    // ===============================SELECT MAKE AN ORDER======================,
    exec(http("H_request_58")
      .get("/workallocation/case/tasks/#{caseId}/event/GENERATE_DIRECTIONS_ORDER/caseType/CIVIL/jurisdiction/CIVIL")
      .headers(headers_58)
      .check(substring("task_required_for_event")))

    .exec(http("H_request_61")
      .get("/data/internal/cases/#{caseId}/event-triggers/GENERATE_DIRECTIONS_ORDER?ignore-warning=false")
      .headers(headers_61)
      .check(substring("GENERATE_DIRECTIONS_ORDER"))
      .check(jsonPath("$.event_token").saveAs("event_token")))

.exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("xsrf_token")))


    // ====================SELECT TRACK--=================,
//
    .exec(http("H_request_72")
      .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=GENERATE_DIRECTIONS_ORDERTrackAllocation")
      .headers(headers_72).header("X-Xsrf-Token", "#{xsrf_token}")
      .body(ElFileBody("h_CourtOrder_bodies/0072_request.dat")))
//  .pause(4)

  .pause(2)



    // ============================MAKE AN ORDER=================,

    .exec(http("GENERATE_DIRECTIONS_ORDERFinalOrderSelect")
      .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=GENERATE_DIRECTIONS_ORDERFinalOrderSelect")
      .headers(headers_80).header("X-Xsrf-Token", "#{xsrf_token}")
      .body(ElFileBody("h_CourtOrder_bodies/OrderSelection.dat")))
  .pause(4)

    .exec(http("GENERATE_DIRECTIONS_ORDERFreeFormOrder")
      .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=GENERATE_DIRECTIONS_ORDERFreeFormOrder")
      .headers(headers_89).header("X-Xsrf-Token", "#{xsrf_token}")
      .body(ElFileBody("h_CourtOrder_bodies/RecitalsAndOrder.json"))
//      .check(substring("GENERATE_DIRECTIONS_ORDERFreeFormOrder"))
      .check(jsonPath("$.data.finalOrderDocument.documentLink.document_url").saveAs("finalOrderDocument_url"))
      .check(jsonPath("$.data.finalOrderDocument.documentLink.document_hash").saveAs("finalOrderDocument_hash"))
      .check(jsonPath("$.data.finalOrderDocument.documentLink.document_filename").saveAs("finalOrderDocument_filename"))
      .check(jsonPath("$.data.finalOrderDocument.createdDatetime").saveAs("createdDatetime"))
      .check(jsonPath("$.data.finalOrderDocument.documentSize").saveAs("documentSize")))
      .pause(2)

    .exec(http("GENERATE_DIRECTIONS_ORDERFinalOrderPreview")
      .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=GENERATE_DIRECTIONS_ORDERFinalOrderPreview")
      .headers(headers_93).header("X-Xsrf-Token", "#{xsrf_token}")
      .body(ElFileBody("h_CourtOrder_bodies/OrderPDF.json")))
      .pause(1)

    .exec(http("FinalOrdersSubmit")
      .post("/data/cases/#{caseId}/events")
      .headers(headers_94).header("X-Xsrf-Token", "#{xsrf_token}")
      .body(ElFileBody("h_CourtOrder_bodies/FinalOrdersSubmit.json")))
      .pause(185.milliseconds)

  .pause(3)



}