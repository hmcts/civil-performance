package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.Environment._

import scala.concurrent.duration.DurationInt
import io.gatling.core.Predef.ElFileBody
import utils._
import utils.spec_HearingAdmin_Headers._


object spec_HearingAdmin{

  val BaseURL = Environment.baseURL
  val IdamURL = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val ClickTaskTab =
    // ================================GO TO CASE LIST=============,
  exec(_.setAll(
        "CaseProgRandomString" -> Common.randomString(5),
        "Plus2WeeksDay" -> Common.getPlus2WeeksDay(),
        "Plus2WeeksMonth" -> Common.getPlus2WeeksMonth(),
        "Plus2WeeksYear" -> Common.getPlus2WeeksYear(),
        "EvidenceYear" -> Common.getYear(),
        "EvidenceDay" -> Common.getDay(),
        "EvidenceMonth" -> Common.getMonth())
  )
    .exec(http("E_request_33")
      .get(BaseURL + "/api/organisation")
      .headers(headers_33)
      .check(status.is(403)))

    .exec(http("E_request_34")
      .get(BaseURL + "/aggregated/caseworkers/:uid/jurisdictions?access=read")
      .headers(headers_34))
//
//    .doIf("#{caseId.isUndefined()}") {
//      exec(_.set("caseId", "0"))
//    }
    .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("xsrf_token")))


    // ======================SEARC CASE========================,

  .pause(977.milliseconds)
    .exec(http("E_request_44")
      .get(BaseURL + "/data/internal/cases/#{caseId}")
      .headers(headers_44))
  .pause(137.milliseconds)


  val ScheduleHearing =
    // ======================================SCHEDULE A HEARING================,
    exec(http("E_request_76")
      .get(BaseURL + "/cases/case-details/#{caseId}/trigger/HEARING_SCHEDULED/HEARING_SCHEDULEDHearingNoticeSelect?tid=#{AdminId}")
      .headers(headers_76))
  .pause(819.milliseconds)

      .exec(Common.configurationui)
      .exec(Common.configUI)
      .exec(Common.TsAndCs)
      .exec(Common.isAuthenticated)
      .exec(Common.userDetails)
      .exec(Common.monitoringTools)
      .exec(Common.isAuthenticated)

    .exec(http("E_request_85")
      .get(BaseURL + "/workallocation/case/tasks/#{caseId}/event/HEARING_SCHEDULED/caseType/CIVIL/jurisdiction/CIVIL")
      .headers(headers_85))

//    .exec(http("E_request_88")
//      .get(BaseURL + "/data/internal/cases/#{caseId}")
//      .headers(headers_88))
//  .pause(114.milliseconds)

    .exec(http("E_request_90")
      .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/HEARING_SCHEDULED?ignore-warning=false")
      .headers(headers_90)
      .check(substring("HEARING_SCHEDULED"))
      .check(jsonPath("$.event_token").saveAs("event_token"))
)
      .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("xsrf_token")))


    // ============================SMALL CLAIM TRIAL=====================,

    .exec(http("E_request_100")
      .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=HEARING_SCHEDULEDHearingNoticeSelect")
      .headers(Headers.validateHeader).header("X-Xsrf-Token", "#{xsrf_token}")
      .body(ElFileBody("e_HearingAdmin_bodies/0100_request.dat")))
  .pause(2)


    // ========================LISTING===================,

    .exec(http("E_request_106")
      .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=HEARING_SCHEDULEDListingOrRelisting")
      .headers(Headers.validateHeader).header("X-Xsrf-Token", "#{xsrf_token}")
      .body(ElFileBody("e_HearingAdmin_bodies/0106_request.dat")))
  .pause(1)

    // ============================Create a hearing notice======================,

    .exec(http("HEARING_SCHEDULEDHearingDetails")
      .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=HEARING_SCHEDULEDHearingDetails")
      .headers(Headers.validateHeader).header("X-Xsrf-Token", "#{xsrf_token}")
      .body(ElFileBody("e_HearingAdmin_bodies/0123_request.dat")))

    .exec(http("HEARING_SCHEDULEDHearingInformation")
      .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=HEARING_SCHEDULEDHearingInformation")
      .headers(Headers.validateHeader).header("X-Xsrf-Token", "#{xsrf_token}")
      .body(ElFileBody("e_HearingAdmin_bodies/0127_request.dat")))
  .pause(2)

    .exec(http("SUBMIT")
      .post(BaseURL + "/data/cases/#{caseId}/events")
      .headers(headers_131).header("X-Xsrf-Token", "#{xsrf_token}")
      .body(ElFileBody("e_HearingAdmin_bodies/0131_request.dat")))
}