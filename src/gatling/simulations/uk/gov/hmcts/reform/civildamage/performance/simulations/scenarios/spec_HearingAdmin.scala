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

  val HearingAdmin =
    // ================================GO TO CASE LIST=============,
    exec(_.setAll(
      "CaseProgRandomString" -> Common.randomString(5),
      "Plus2WeeksDay" -> Common.getPlus2WeeksDay(),
      "Plus2WeeksMonth" -> Common.getPlus2WeeksMonth(),
      "Plus2WeeksYear" -> Common.getPlus2WeeksYear(),
      "EvidenceYear" -> Common.getYear(),
      "EvidenceDay" -> Common.getDay(),
      "EvidenceMonth" -> Common.getMonth(),
      "uId_hearing" -> "4581b838-873e-4d28-8134-50eb1edb3d43"
    ))

    // =======================LANDING PAGE==================,
    .group("Civil_SpecClaim_50_HearingAdmin") {
      exec(http("005_HealthCheck")
        .get("/api/healthCheck?path=%2Fwork%2Fmy-work%2Flist")
        .headers(Headers.commonHeader)
        .check(substring("healthState")))

      .exec(http("005_RegionLocation")
        .post("/workallocation/region-location")
        .headers(Headers.commonHeader)
        .body(StringBody("""{"serviceIds": ["CIVIL"]}""".stripMargin))
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
          """{"searchRequest": {"search_parameters": [ {"key": "user", "operator":"IN", "values":
            |["#{uId_hearing}"]}, {"key": "state", "operator":"IN", "values":["assigned"]} ], "sorting_parameters": [],
            |"search_by": "caseworker"}, "view": "MyTasks"}""".stripMargin))
        .headers(Headers.commonHeader)
        .check(substring("total_records")))

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
        .body(StringBody(
          """{"searchRequest": {"search_parameters": [ {"key": "user", "operator": "IN", "values":
            |["#{uId_hearing}"]}, {"key": "state", "operator": "IN", "values": ["assigned"]}, {"key": "jurisdiction",
            |"operator": "IN", "values": ["CIVIL"]} ], "sorting_parameters": [], "search_by": "caseworker",
            |"pagination_parameters": {"page_number": 1, "page_size": 25}}, "view": "MyTasks", "refined": false,
            |"currentUser": "#{uId_hearing}"}""".stripMargin))
        .headers(Headers.commonHeader)
        .check(substring("total_records")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================OPEN CASE=======================,
    .group("Civil_SpecClaim_50_HearingAdmin") {
      exec(http("005_OpenCase")
        .get("/data/internal/cases/#{caseId}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{caseId}")))
    }

    .group("Civil_SpecClaim_50_HearingAdmin") {
      exec(http("010_OpenCase")
        .post("/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
        .headers(Headers.commonHeader)
        .check(status.is(204)))
    }

    .group("Civil_SpecClaim_50_HearingAdmin") {
      exec(http("015_OpenCase")
        .get("/api/wa-supported-jurisdiction/get")
        .headers(Headers.commonHeader)
        .check(substring("CIVIL")))
    }
    .pause(60)

    // =======================TASK TAB=======================,
    .group("Civil_SpecClaim_50_HearingAdmin") {
      exec(http("005_TaskTab")
        .post("/workallocation/case/task/#{caseId}")
        .headers(Headers.commonHeader)
        .body(StringBody("""{"refined": true}""".stripMargin))
        .check(regex("id\":\"(.*?)\"").optional.saveAs("HearingCaseId"))
        .check(substring("task_state")))

      .exec(http("010_TaskTab")
        .post("/workallocation/caseworker/getUsersByServiceName")
        .body(StringBody("""{"services": ["CIVIL"]}""".stripMargin))
        .headers(Headers.commonHeader)
        .check(substring("Birmingham")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================ASSIGN TO ME=======================,
    .group("Civil_SpecClaim_50_HearingAdmin") {
      exec(http("005_AssignToMe")
        .post("/workallocation/task/#{HearingCaseId}/claim")
        .headers(Headers.commonHeader)
        .check(status.is(204)))

      .exec(http("010_AssignToMe")
        .post("/workallocation/case/task/#{caseId}")
        .headers(Headers.commonHeader)
        .body(StringBody("""{"refined": true}""".stripMargin))
//        .check(jsonPath("$[0].id").optional.saveAs("HearingCaseId")))
        .check(substring("task_system")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================HEARING NOTICE DROPDOWN=======================,
    .group("Civil_SpecClaim_50_HearingAdmin") {
      exec(http("005_Jurisdiction")
        .get("/workallocation/case/tasks/#{caseId}/event/HEARING_SCHEDULED/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(Headers.commonHeader)
        .check(substring("task_required_for_event")))

      .exec(http("010_Profile")
        .get("/data/internal/profile")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-user-profile.v2+json;charset=UTF-8")
        .check(substring("#{centreadminuser}")))

      .exec(http("015_IgnoreWarning")
        .get("/data/internal/cases/#{caseId}/event-triggers/HEARING_SCHEDULED?ignore-warning=false")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
        .check(substring("HEARING_SCHEDULED"))
        .check(jsonPath("$.event_token").saveAs("event_token_admin")))
      .exitHereIf(session => !session.contains("event_token_admin"))

      .exec(http("020_HearingScheduled")
        .get("/workallocation/case/tasks/#{caseId}/event/HEARING_SCHEDULED/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(Headers.commonHeader)
        .check(substring("Schedule a hearing using the Hearings tab")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================SMALL CLAIM==============================,
    .group("Civil_SpecClaim_50_HearingAdmin") {
      exec(http("005_HearingNoticeSelect")
        .post("/data/case-types/CIVIL/validate?pageId=HEARING_SCHEDULEDHearingNoticeSelect")
        .headers(Headers.validateHeader)
        .body(ElFileBody("e_HearingAdmin_bodies/adminHearingType.dat"))
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/case-types/CIVIL/validate?" +
          "pageId=HEARING_SCHEDULEDHearingNoticeSelect")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================LISTING==============================,
    .group("Civil_SpecClaim_50_HearingAdmin") {
      exec(http("005_ListingOrRelisting")
        .post("/data/case-types/CIVIL/validate?pageId=HEARING_SCHEDULEDListingOrRelisting")
        .headers(Headers.validateHeader)
        .body(ElFileBody("e_HearingAdmin_bodies/adminListing.dat"))
        .check(substring("listingOrRelisting")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================HEARING DETAILS==============================,
    .group("Civil_SpecClaim_50_HearingAdmin") {
      exec(http("005_HearingDetails")
        .post("/data/case-types/CIVIL/validate?pageId=HEARING_SCHEDULEDHearingDetails")
        .headers(Headers.validateHeader)
        .body(ElFileBody("e_HearingAdmin_bodies/adminHearingDetails.dat"))
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/case-types/CIVIL/validate?" +
          "pageId=HEARING_SCHEDULEDHearingDetails")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================NOTICE LETTER INFO=======================,
    .group("Civil_SpecClaim_50_HearingAdmin") {
      exec(http("005_HearingInformation")
        .post("/data/case-types/CIVIL/validate?pageId=HEARING_SCHEDULEDHearingInformation")
        .headers(Headers.validateHeader)
        .body(ElFileBody("e_HearingAdmin_bodies/adminHearingInformation.dat"))
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/case-types/CIVIL/validate?" +
          "pageId=HEARING_SCHEDULEDHearingInformation")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================SUBMIT=======================,
    .group("Civil_SpecClaim_50_HearingAdmin") {
      exec(http("005_Submit")
        .get("/workallocation/task/#{HearingCaseId}")
        .headers(Headers.commonHeader)
        .check(substring("role_category")))

      .exec(http("010_Submit")
        .post("/data/cases/#{caseId}/events")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
        .body(ElFileBody("e_HearingAdmin_bodies/adminSubmitHearing.dat"))
        .check(status.is(201)))

      .exec(http("015_Submit")
        .post("/workallocation/task/#{HearingCaseId}/complete")
        .headers(Headers.commonHeader)
        .body(StringBody("""{"actionByEvent": true, "eventName": "Create a hearing notice"}""".stripMargin))
        .check(status.is(204)))

      .exec(http("020_Submit")
        .get("/data/internal/cases/#{caseId}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{caseId}")))
    }
    .pause(MinThinkTime, MaxThinkTime)


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
      .body(ElFileBody("e_HearingAdmin_bodies/adminHearingType.dat")))
  .pause(2)


    // ========================LISTING===================,

    .exec(http("E_request_106")
      .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=HEARING_SCHEDULEDListingOrRelisting")
      .headers(Headers.validateHeader).header("X-Xsrf-Token", "#{xsrf_token}")
      .body(ElFileBody("e_HearingAdmin_bodies/adminListing.dat")))
  .pause(1)

    // ============================Create a hearing notice======================,

    .exec(http("HEARING_SCHEDULEDHearingDetails")
      .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=HEARING_SCHEDULEDHearingDetails")
      .headers(Headers.validateHeader).header("X-Xsrf-Token", "#{xsrf_token}")
      .body(ElFileBody("e_HearingAdmin_bodies/adminHearingDetails.dat")))

    .exec(http("HEARING_SCHEDULEDHearingInformation")
      .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=HEARING_SCHEDULEDHearingInformation")
      .headers(Headers.validateHeader).header("X-Xsrf-Token", "#{xsrf_token}")
      .body(ElFileBody("e_HearingAdmin_bodies/adminHearingInformation.dat")))
  .pause(2)

    .exec(http("SUBMIT")
      .post(BaseURL + "/data/cases/#{caseId}/events")
      .headers(headers_131).header("X-Xsrf-Token", "#{xsrf_token}")
      .body(ElFileBody("e_HearingAdmin_bodies/adminSubmitHearing.dat")))
}