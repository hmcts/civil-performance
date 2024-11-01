package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.Environment._

import scala.concurrent.duration.DurationInt
import io.gatling.core.Predef.ElFileBody
import utils._
import utils.spec_SDO_Judge_Headers._

object spec_SDO_Judge {

  val BaseURL = Environment.baseURL
  val IdamURL = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  // ====================================LOGIN======================,
  val sdoJudge =
    exec(_.setAll(
      "HearingYear" -> Common.getYearFuture(),
      "HearingDay" -> Common.getDay(),
      "HearingMonth" -> Common.getMonth(),
      "Plus4WeeksDay" -> Common.getPlus4WeeksDay(),
      "Plus4WeeksMonth" -> Common.getPlus4WeeksMonth(),
      "Plus4WeeksYear" -> Common.getPlus4WeeksYear(),
      "Plus6WeeksDay" -> Common.getPlus6WeeksDay(),
      "Plus6WeeksMonth" -> Common.getPlus6WeeksMonth(),
      "Plus6WeeksYear" -> Common.getPlus6WeeksYear(),
      "Plus8WeeksDay" -> Common.getPlus8WeeksDay(),
      "Plus8WeeksMonth" -> Common.getPlus8WeeksMonth(),
      "Plus8WeeksYear" -> Common.getPlus8WeeksYear(),
      "Plus10WeeksDay" -> Common.getPlus10WeeksDay(),
      "Plus10WeeksMonth" -> Common.getPlus10WeeksMonth(),
      "Plus10WeeksYear" -> Common.getPlus10WeeksYear(),
      "Plus12WeeksDay" -> Common.getPlus12WeeksDay(),
      "Plus12WeeksMonth" -> Common.getPlus12WeeksMonth(),
      "Plus12WeeksYear" -> Common.getPlus12WeeksYear(),
      "LRrandomString" -> Common.randomString(5)
    ))

    // ===================================OPEN CASE=================,
    .exec(http("D_request_46")
      .get(BaseURL + "/data/internal/cases/#{caseId}")
      .headers(headers_46))
    .pause(157.milliseconds)


    // =========================================GO TO TASK TAB======================,
    .exec(http("Go_to_Task")
      .post(BaseURL + "/workallocation/case/task/#{caseId}")
      .headers(headers_58)
      //.header("X-Xsrf-Token", "#{xsrf_token}")
      .body(ElFileBody("d_SDO_Judge_bodies/0058_request.json"))
      .check(jsonPath("$[0].id").saveAs("JudgeId")))

//      .exec(getCookieValue(CookieKey("__userid__").withDomain(BaseURL.replace("https://", "")).saveAs("UserIdx")))

//    // =====================ASIGN TO ME=================,
    .exec(http("D_request_65")
      .post(BaseURL + "/workallocation/task/#{JudgeId}/claim")
      .headers(headers_58)
      ////.header("X-Xsrf-Token", "#{xsrf_token}")
      .body(ElFileBody("d_SDO_Judge_bodies/0065_request.bin")))
    .pause(5)


    // =======================SELECT DIRECTION SMALL CLAIM COURT========================,
    .exec(http("D_request_76")
      .get(BaseURL + "/cases/case-details/#{caseId}/trigger/CREATE_SDO/CREATE_SDOSmallClaims?tid=#{JudgeId}")
      .headers(headers_76))
    .pause(863.milliseconds)

      .exec(Common.configurationui)
      .exec(Common.configUI)
      .exec(Common.TsAndCs)
      .exec(Common.isAuthenticated)
      .exec(Common.userDetails)
      .exec(Common.monitoringTools)
      .exec(Common.isAuthenticated)


      //===========================================
    .exec(http("D_request_86")
      .get(BaseURL + "/data/internal/cases/#{caseId}")
      .headers(headers_86))
    .pause(105.milliseconds)


    .exec(http("D_request_88")
      .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/CREATE_SDO?ignore-warning=false")
      .headers(headers_88)
      .check(jsonPath("$.event_token").saveAs("event_token"))
    )
    .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("xsrf_token")))
    .pause(182.milliseconds)


    // ===========================SDO AMOUNT ===========================,

    .exec(http("D_request_104")
      .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_SDOSDO")
      .headers(Headers.validateHeader)
      //.header("X-Xsrf-Token", "#{xsrf_token}")
      .body(ElFileBody("d_SDO_Judge_bodies/0104_request.dat")))


    // ========================FAST TRACK==================,

    .exec(http("D_request_117")
      .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_SDOClaimsTrack")
      .headers(Headers.validateHeader)
      //.header("X-Xsrf-Token", "#{xsrf_token}")
      .body(ElFileBody("d_SDO_Judge_bodies/0117_request.dat")))
    .pause(2)


    // ========================JUDGE ORDER====================,
    .exec(http("D_request_135")
      .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_SDOFastTrack")
      .headers(Headers.validateHeader)
      //.header("X-Xsrf-Token", "#{xsrf_token}")
      .body(ElFileBody("d_SDO_Judge_bodies/0135_request.dat"))
      .check(jsonPath("$.data.sdoOrderDocument.createdDatetime").saveAs("createdDatetime"))
      .check(jsonPath("$.data.sdoOrderDocument.documentLink.document_url").saveAs("sdoDocument_url"))
      .check(jsonPath("$.data.sdoOrderDocument.documentLink.document_hash").saveAs("sdoDocument_hash"))
      .check(jsonPath("$.data.sdoOrderDocument.documentName").saveAs("sdoDocumentName"))
      .check(jsonPath("$.data.sdoOrderDocument.documentSize").saveAs("documentSize"))
)
    .pause(1)


    // ========================VIEW SDO==============,

    .exec(http("CREATE_SDOOrderPreview")
      .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_SDOOrderPreview")
      .headers(Headers.validateHeader)
      //.header("X-Xsrf-Token", "#{xsrf_token}")
      .body(ElFileBody("d_SDO_Judge_bodies/0143_request.dat")))
      .pause(2)


    // ======================SUBMIT===================,

    .exec(http("SUBMIT")
      .post(BaseURL + "/data/cases/#{caseId}/events")
      .headers(headers_150)
      //.header("X-Xsrf-Token", "#{xsrf_token}")
      .body(ElFileBody("d_SDO_Judge_bodies/0150_request.dat")))
}