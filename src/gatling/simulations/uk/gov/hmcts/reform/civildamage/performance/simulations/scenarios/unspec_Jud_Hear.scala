package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.unspec_Jud_Hear_Headers._
import utils._

import scala.concurrent.duration.DurationInt

object unspec_Jud_Hear {
	val BaseURL = Environment.baseURL
	val IdamURL = Environment.idamURL
	val MinThinkTime = Environment.minThinkTime
	val MaxThinkTime = Environment.maxThinkTime

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
      "LRrandomString" -> Common.randomString(5),
      "uId" -> "66bec075-0ae0-4743-a20d-05febfb2081a"
    ))

    // =======================LANDING PAGE==================,
    .group("Civil_UnSpecClaim_60_01_SDO") {
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
    .group("Civil_UnSpecClaim_60_02_SDO") {
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
        .check(substring("JUDICIAL_REFERRAL")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================OPEN CASE=======================,
    .group("Civil_UnSpecClaim_60_03_SDO") {
      exec(http("005_OpenCase")
        .get("/data/internal/cases/#{caseId}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{caseId}")))
    }

    .group("Civil_UnSpecClaim_60_03_SDO") {
      exec(http("010_OpenCase")
        .post("/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
        .headers(Headers.commonHeader)
        .check(status.is(204)))
    }

    .group("Civil_UnSpecClaim_60_03_SDO") {
      exec(http("015_OpenCase")
        .get("/api/wa-supported-jurisdiction/get")
        .headers(Headers.commonHeader)
        .check(substring("CIVIL")))
    }
    .pause(150)

    // =======================TASK TAB=======================,
    .group("Civil_UnSpecClaim_60_04_SDO") {
      exec(http("005_TaskTab")
        .post("/workallocation/case/task/#{caseId}")
        .headers(Headers.commonHeader)
        .body(StringBody("""{"refined": true}""".stripMargin))
        .check(regex("id\":\"(.*?)\"").optional.saveAs("judgeId"))
        .check(substring("task_state")))

      .exec(http("010_TaskTab")
        .post("/workallocation/caseworker/getUsersByServiceName")
        .headers(Headers.commonHeader)
        .body(StringBody("""{"services": ["CIVIL"]}""".stripMargin))
        .check(substring("Birmingham")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================ASSIGN TO ME=======================,
    .group("Civil_UnSpecClaim_60_05_SDO") {
      exec(http("005_AssignToMe")
        .post("/workallocation/task/#{judgeId}/claim")
        .headers(Headers.commonHeader)
        .check(status.is(204)))

      exec(http("010_AssignToMe")
        .post("/workallocation/case/task/#{caseId}")
        .headers(Headers.commonHeader)
        .body(StringBody("""{"refined": true}""".stripMargin))
        .check(substring("task_system")))

      .exec(http("015_AssignToMe")
        .post("/api/role-access/roles/getJudicialUsers")
        .body(StringBody("""{"userIds": ["#{uId}"], "services": ["CIVIL"]}""".stripMargin))
        .headers(Headers.commonHeader)
        .check(status.in(200, 406)))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================FAST TRACK=======================,
    .group("Civil_UnSpecClaim_60_06_SDO") {
      exec(http("005_FastTrack")
        .get("/cases/case-details/#{caseId}/trigger/CREATE_SDO/CREATE_SDOFastTrack?tid=#{judgeId}")
        .headers(Headers.navigationHeader)
        .check(substring("flexbox no-flexboxtwenner")))

      .exec(Common.configurationui)
      .exec(Common.configUI)
      .exec(Common.TsAndCs)
      .exec(Common.userDetails)
      .exec(Common.isAuthenticated)
      .exec(Common.monitoringTools)
      .exec(Common.monitoringTools)
      .exec(Common.monitoringTools)

      .exec(http("010_FastTrack")
        .get("/workallocation/case/tasks/#{caseId}/event/CREATE_SDO/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(Headers.commonHeader)
        .check(substring("case_management_category")))

      .exec(http("015_FastTrack")
        .get("/data/internal/cases/#{caseId}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{caseId}")))

      .exec(http("020_FastTrack")
        .get("/data/internal/profile")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-user-profile.v2+json;charset=UTF-8")
        .check(substring("#{judgeuser}")))

      .exec(http("025_FastTrack")
        .get("/data/internal/cases/#{caseId}/event-triggers/CREATE_SDO?ignore-warning=false")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
        .check(substring("Standard Direction Order"))
        .check(jsonPath("$.event_token").saveAs("event_token_judge")))
      .exitHereIf(session => !session.contains("event_token_judge"))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ===========================SDO AMOUNT===========================,
    .group("Civil_UnSpecClaim_60_07_SDO") {
      exec(http("005_SDO_Amount")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_SDOSDO")
        .headers(Headers.validateHeader)
        .body(ElFileBody("ud_ue_jud_hear_bodies/judgeJudgementSum.dat"))
        .check(substring("drawDirectionsOrderRequired")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================ALLOCATE FAST TRACK=======================,
    .group("Civil_UnSpecClaim_60_08_SDO") {
      exec(http("005_ClaimsTrack")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_SDOClaimsTrack")
        .headers(Headers.validateHeader)
        .body(ElFileBody("ud_ue_jud_hear_bodies/judgeFastTrack.dat"))
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/case-types/CIVIL/validate?" +
          "pageId=CREATE_SDOClaimsTrack")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================ORDER DETAILS=======================,
    .group("Civil_UnSpecClaim_60_09_SDO") {
      exec(http("005_OrderDetails")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_SDOFastTrack")
        .headers(Headers.validateHeader)
        .body(ElFileBody("ud_ue_jud_hear_bodies/judgeSDODetails.dat"))
        .check(jsonPath("$.data.sdoOrderDocument.createdDatetime").saveAs("createdDatetime"))
        .check(jsonPath("$.data.sdoOrderDocument.documentLink.document_url").saveAs("sdoDocument_url"))
        .check(jsonPath("$.data.sdoOrderDocument.documentLink.document_hash").saveAs("sdoDocument_hash"))
        .check(jsonPath("$.data.sdoOrderDocument.documentName").saveAs("sdoDocumentName"))
        .check(jsonPath("$.data.sdoOrderDocument.documentSize").saveAs("documentSize")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================VIEW DIRECTIONAL ORDER=======================,
    .group("Civil_UnSpecClaim_60_10_SDO") {
      exec(http("005_OrderPreview")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_SDOOrderPreview")
        .headers(Headers.validateHeader)
        .body(ElFileBody("ud_ue_jud_hear_bodies/judgeViewOrder.dat"))
        .check(substring("sdoOrderDocument")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================SUBMIT SDO=======================,
    .group("Civil_UnSpecClaim_60_11_SDO") {
      exec(http("005_SubmitSDO")
        .get("/workallocation/task/#{judgeId}")
        .headers(Headers.commonHeader)
        .check(substring("role_category")))

      .exec(http("010_SubmitSDO")
        .post("/data/cases/#{caseId}/events")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
        .body(ElFileBody("ud_ue_jud_hear_bodies/judgeSubmitSDO.dat"))
        .check(substring("Your order has been issued")))

//      .exec(http("015_SubmitSDO")
//        .post("/workallocation/task/#{judgeId}/complete")
//        .headers(Headers.commonHeader)
//        .body(StringBody("""{"actionByEvent": true, "eventName": "Standard Direction Order"}""".stripMargin))
//        .check(status.in(204, 403)))

      .exec(http("015_SubmitSDO")
        .get("/data/internal/cases/#{caseId}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{caseId}")))
    }
    .pause(MinThinkTime, MaxThinkTime)





  val HearingAdmin =
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
    .group("Civil_UnSpecClaim_70_01_HearingAdmin") {
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
    .group("Civil_UnSpecClaim_70_02_HearingAdmin") {
      exec(http("005_OpenCase")
        .get("/data/internal/cases/#{caseId}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{caseId}")))
    }

    .group("Civil_UnSpecClaim_70_02_HearingAdmin") {
      exec(http("010_OpenCase")
        .post("/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
        .headers(Headers.commonHeader)
        .check(status.is(204)))
    }

    .group("Civil_UnSpecClaim_70_02_HearingAdmin") {
      exec(http("015_OpenCase")
        .get("/api/wa-supported-jurisdiction/get")
        .headers(Headers.commonHeader)
        .check(substring("CIVIL")))
    }
    .pause(90)

    // =======================TASK TAB=======================,
    .group("Civil_UnSpecClaim_70_03_HearingAdmin") {
      exec(http("005_TaskTab")
        .post("/workallocation/case/task/#{caseId}")
        .headers(Headers.commonHeader)
        .body(StringBody("""{"refined": true}""".stripMargin))
        .check(substring("Assign to me"))
        .check(jsonPath("$[0].id").optional.saveAs("HearingCaseId")))

      .exec(http("010_TaskTab")
        .post("/workallocation/caseworker/getUsersByServiceName")
        .body(StringBody("""{"services": ["CIVIL"]}""".stripMargin))
        .headers(Headers.commonHeader)
        .check(substring("Birmingham")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================ASSIGN TO ME=======================,
    .group("Civil_UnSpecClaim_70_04_HearingAdmin") {
      exec(http("005_AssignToMe")
        .post("/workallocation/task/#{HearingCaseId}/claim")
        .headers(Headers.commonHeader)
        .check(status.is(204)))

      .exec(http("010_AssignToMe")
        .post("/workallocation/case/task/#{caseId}")
        .headers(Headers.commonHeader)
        .body(StringBody("""{"refined": true}""".stripMargin))
        .check(substring("Unassign task")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================HEARING NOTICE DROPDOWN=======================,
    .group("Civil_UnSpecClaim_70_05_HearingAdmin") {
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

    // =======================SMALL CLAIM=======================,
    .group("Civil_UnSpecClaim_70_06_HearingAdmin") {
      exec(http("005_HearingNoticeSelect")
        .post("/data/case-types/CIVIL/validate?pageId=HEARING_SCHEDULEDHearingNoticeSelect")
        .headers(Headers.validateHeader)
        .body(ElFileBody("ud_ue_jud_hear_bodies/adminHearingType.dat"))
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/case-types/CIVIL/validate?" +
          "pageId=HEARING_SCHEDULEDHearingNoticeSelect")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================LISTING=======================,
    .group("Civil_UnSpecClaim_70_07_HearingAdmin") {
      exec(http("005_ListingOrRelisting")
        .post("/data/case-types/CIVIL/validate?pageId=HEARING_SCHEDULEDListingOrRelisting")
        .headers(Headers.validateHeader)
        .body(ElFileBody("ud_ue_jud_hear_bodies/adminListing.dat"))
        .check(substring("listingOrRelisting")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================HEARING DETAILS=======================,
    .group("Civil_UnSpecClaim_70_08_HearingAdmin") {
      exec(http("005_HearingDetails")
        .post("/data/case-types/CIVIL/validate?pageId=HEARING_SCHEDULEDHearingDetails")
        .headers(Headers.validateHeader)
        .body(ElFileBody("ud_ue_jud_hear_bodies/adminHearingDetails.dat"))
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/case-types/CIVIL/validate?" +
          "pageId=HEARING_SCHEDULEDHearingDetails")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================NOTICE LETTER INFO=======================,
    .group("Civil_UnSpecClaim_70_09_HearingAdmin") {
      exec(http("005_HearingInformation")
        .post("/data/case-types/CIVIL/validate?pageId=HEARING_SCHEDULEDHearingInformation")
        .headers(Headers.validateHeader)
        .body(ElFileBody("ud_ue_jud_hear_bodies/adminHearingInformation.dat"))
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/case-types/CIVIL/validate?" +
          "pageId=HEARING_SCHEDULEDHearingInformation")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================SUBMIT=======================,
    .group("Civil_UnSpecClaim_70_10_HearingAdmin") {
      exec(http("005_Submit")
        .get("/workallocation/task/#{HearingCaseId}")
        .headers(Headers.commonHeader)
        .check(substring("role_category")))

      .exec(http("010_Submit")
        .post("/data/cases/#{caseId}/events")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
        .body(ElFileBody("ud_ue_jud_hear_bodies/adminSubmitHearing.dat"))
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




  //  Wrong path through the Task Tab (after Assign to Me), not part of the journey
  //    // =======================HEARINGS TAB=======================,
  //    .group("Civil_UnSpecClaim_50_HearingAdmin") {
  //      exec(http("005_HEARING REQUEST")
  //        .get("/cases/case-details/#{caseId}/hearings?tid=#{HearingCaseId}")
  //        .headers(Headers.navigationHeader)
  //        .check(substring("flexbox no-flexboxtwenner")))
  //
  //      .exec(Common.configurationui)
  //      .exec(Common.configUI)
  //      .exec(Common.TsAndCs)
  //      .exec(Common.monitoringTools)
  //      .exec(Common.isAuthenticated)
  //
  //      .exec(http("010_HEARING REQUEST")
  //        .get("/data/internal/cases/#{caseId}")
  //        .headers(Headers.validateHeader)
  //        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
  //        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{caseId}")))
  //
  //      .exec(http("015_HEARING REQUEST")
  //        .post("/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
  //        .headers(Headers.commonHeader)
  //        .check(status.is(204)))
  //
  //      .exec(http("020_HEARING REQUEST")
  //        .get("/api/wa-supported-jurisdiction/get")
  //        .headers(Headers.commonHeader)
  //        .check(substring("CIVIL")))
  //
  //      .exec(http("025_HEARING REQUEST")
  //        .get("/api/hearings/getHearings?caseId=#{caseId}")
  //        .headers(Headers.commonHeader)
  //        .check(substring("caseHearings")))
  //
  //      .exec(http("030_HEARING REQUEST")
  //        .post("/api/hearings/loadServiceHearingValues?jurisdictionId=CIVIL")
  //        .body(StringBody("""{"caseReference": "#{caseId}"}""".stripMargin))
  //        .headers(Headers.commonHeader)
  //        .check(substring("caserestrictedFlag")))
  //    }
  //    .pause(MinThinkTime, MaxThinkTime)
  //    //.exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("xsrf_token")))
  //
  //    // =======================REQUEST A HEARING=======================,
  //    .group("Civil_UnSpecClaim_50_HearingAdmin") {
  //      exec(http("005_RequestHearing")
  //        .get("/api/prd/lov/getLovRefData?categoryId=caseType&serviceId=AAA7&isChildRequired=Y")
  //        .headers(Headers.commonHeader)
  //        .check(substring("category_key")))
  //
  //      .exec(http("010_RequestHearing")
  //        .get("/api/prd/caseFlag/getCaseFlagRefData?serviceId=AAA7")
  //        .headers(Headers.commonHeader)
  //        .check(substring("FlagDetails")))
  //
  //      .exec(http("015_RequestHearing")
  //        .get("/api/prd/location/getLocationById?epimms_id=231596")
  //        .headers(Headers.commonHeader)
  //        .check(substring("Birmingham Civil and Family Justice Centre")))
  //    }
  //    .pause(MinThinkTime, MaxThinkTime)
  //
  //    // =======================HEARING REQUIREMENTS=======================,
  //    .group("Civil_UnSpecClaim_50_HearingAdmin") {
  //      exec(http("005_HearingRequirements")
  //        .post("/api/hearings/loadServiceHearingValues?jurisdictionId=CIVIL")
  //        .body(StringBody("""{"caseReference": "#{caseId}"}""".stripMargin))
  //        .headers(Headers.commonHeader)
  //        .check(substring("caserestrictedFlag")))
  //
  //      .exec(http("010_HearingRequirements")
  //        .get("/api/prd/caseFlag/getCaseFlagRefData?serviceId=AAA7")
  //        .headers(Headers.commonHeader)
  //        .check(substring("FlagDetails")))
  //
  //      .exec(http("015_HearingRequirements")
  //        .get("/api/prd/lov/getLovRefData?categoryId=Facilities&serviceId=AAA7&isChildRequired=N")
  //        .headers(Headers.commonHeader)
  //        .check(substring("category_key")))
  //    }
  //    .pause(MinThinkTime, MaxThinkTime)
  //
  //    // =======================HEARING SECURITY=======================,
  //    .group("Civil_UnSpecClaim_50_HearingAdmin") {
  //      exec(http("005_HearingSecurity")
  //        .get("/api/prd/lov/getLovRefData?categoryId=HearingType&serviceId=AAA7&isChildRequired=N")
  //        .headers(Headers.commonHeader)
  //        .check(substring("category_key")))
  //    }
  //    .pause(MinThinkTime, MaxThinkTime)
  //
  //    // =======================HEARING TYPE=======================,
  //    .group("Civil_UnSpecClaim_50_HearingAdmin") {
  //      exec(http("005_HearingType")
  //        .get("/api/prd/lov/getLovRefData?categoryId=HearingChannel&serviceId=AAA7&isChildRequired=N")
  //        .headers(Headers.commonHeader)
  //        .check(substring("category_key")))
  //    }
  //    .pause(MinThinkTime, MaxThinkTime)
  //
  //    // =======================HEARING CHANNEL=======================,
  //    .group("Civil_UnSpecClaim_50_HearingAdmin") {
  //      exec(http("005_HearingChannel")
  //        .get("/api/prd/location/getLocationById?epimms_id=231596")
  //        .headers(Headers.commonHeader)
  //        .check(substring("Birmingham Civil and Family Justice Centre")))
  //    }
  //    .pause(MinThinkTime, MaxThinkTime)
  //
  //    // =======================HEARING LOCATION=======================,
  //    .group("Civil_UnSpecClaim_50_HearingAdmin") {
  //      exec(http("005_HearingLocation")
  //        .get("/api/prd/lov/getLovRefData?categoryId=JudgeType&serviceId=AAA7&isChildRequired=N")
  //        .headers(Headers.commonHeader)
  //        .check(substring("category_key")))
  //    }
  //    .pause(MinThinkTime, MaxThinkTime)
  //
  //    // =======================HEARING JUDGE=======================,
  //    .group("Civil_UnSpecClaim_50_HearingAdmin") {
  //      exec(http("005_HearingJudge")
  //        .get("/api/prd/lov/getLovRefData?categoryId=HearingPriority&serviceId=AAA7&isChildRequired=N")
  //        .headers(Headers.commonHeader)
  //        .check(substring("category_key")))
  //    }
  //    .pause(MinThinkTime, MaxThinkTime)
  //
  //    // =======================HEARING PRIORITY=======================,
  //    .group("Civil_UnSpecClaim_50_HearingAdmin") {
  //      exec(http("005_HearingPriority")
  //        .get("/api/prd/lov/getLovRefData?categoryId=HearingDuration&serviceId=AAA7&isChildRequired=N")
  //        .headers(Headers.commonHeader)
  //        .check(substring("category_key")))
  //    }
  //    .pause(MinThinkTime, MaxThinkTime)
  //
  //    // =======================SUBMIT HEARING=======================,
  //    .group("Civil_UnSpecClaim_50_HearingAdmin") {
  //      exec(http("005_SubmitHearing")
  //        .post("/api/hearings/submitHearingRequest")
  //        .headers(Headers.commonHeader)
  //        //.body(ElFileBody("ud_ue_jud_hear_bodies/judgeSubmitHearing.dat"))
  //        .check(substring("HEARING_REQUESTED")))
  //    }


}