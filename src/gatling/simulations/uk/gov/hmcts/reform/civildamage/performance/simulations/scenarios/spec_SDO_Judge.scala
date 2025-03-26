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
      "LRrandomString" -> Common.randomString(5),
      "uId" -> "66bec075-0ae0-4743-a20d-05febfb2081a"
    ))

    // =======================LANDING PAGE==================,
    .group("Civil_SpecClaim_40_SDO") {
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
        .body(StringBody("""{"searchRequest": {"search_parameters": [ {"key": "user", "operator": "IN",
            |"values": ["#{uId}"]}, {"key": "state", "operator": "IN", "values": ["assigned"]} ],
            |"sorting_parameters": [], "search_by": "judge"}, "view": "MyTasks"}""".stripMargin))
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
    .group("Civil_SpecClaim_40_SDO") {
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
    .group("Civil_SpecClaim_40_SDO") {
      exec(http("005_OpenCase")
        .get("/data/internal/cases/#{caseId}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{caseId}")))
    }

    .group("Civil_SpecClaim_40_SDO") {
      exec(http("010_OpenCase")
        .post("/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
        .headers(Headers.commonHeader)
        .check(status.is(204)))
    }

    .group("Civil_SpecClaim_40_SDO") {
      exec(http("015_OpenCase")
        .get("/api/wa-supported-jurisdiction/get")
        .headers(Headers.commonHeader)
        .check(substring("CIVIL")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================MEDIATION UNSUCCESSFUL DROPDOWN================,
    .group("Civil_SpecClaim_40_SDO") {
      exec(http("005_Jurisdiction")
        .get("/workallocation/case/tasks/#{caseId}/event/MEDIATION_UNSUCCESSFUL/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(Headers.commonHeader)
        .check(substring("task_required_for_event")))

      .exec(http("010_Profile")
        .get("/data/internal/profile")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-user-profile.v2+json;charset=UTF-8")
        .check(substring("#{judgeuser}")))

      exec(http("015_IgnoreWarning")
        .get("/data/internal/cases/#{caseId}/event-triggers/MEDIATION_UNSUCCESSFUL?ignore-warning=false")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
        .check(substring("MEDIATION_UNSUCCESSFUL"))
        .check(jsonPath("$.event_token").saveAs("event_token_mediation")))
      .exitHereIf(session => !session.contains("event_token_mediation"))

      .exec(http("020_HearingScheduled")
        .get("/workallocation/case/tasks/#{caseId}/event/MEDIATION_UNSUCCESSFUL/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(Headers.commonHeader)
        .check(substring("task_required_for_event")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================APPOINTMENT NO AGREEMENT================,
    .group("Civil_SpecClaim_40_SDO") {
      exec(http("005_PartyWithdraws")
        .post("/data/case-types/CIVIL/validate?pageId=MEDIATION_UNSUCCESSFULmediationUnsuccessful")
        .headers(Headers.validateHeader)
        .body(ElFileBody("d_SDO_Judge_bodies/mediationUnsuccessfulReason.dat"))
        .check(substring("mediationUnsuccessfulReasonsMultiSelect")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================CONTINUE=======================,
    .group("Civil_SpecClaim_40_SDO") {
      exec(http("005_Continue")
        .post("/data/case-types/CIVIL/validate?pageId=MEDIATION_UNSUCCESSFULWorkAllocationIntegrationFields")
        .headers(Headers.validateHeader)
        .body(ElFileBody("d_SDO_Judge_bodies/mediationUnsuccessfulInformation.dat"))
        .check(substring("responseClaimTrack")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================CHECK YOUR ANSWERS================,
    .group("Civil_SpecClaim_40_SDO") {
      exec(http("005_CheckYourAnswers")
        .post("/data/cases/#{caseId}/events")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
        .body(ElFileBody("d_SDO_Judge_bodies/mediationUnsuccessfulSubmit.dat"))
        .check(substring("CALLBACK_COMPLETED")))

      .exec(http("010_CheckYourAnswers")
        .get("/data/internal/cases/#{caseId}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{caseId}")))
    }

    .group("Civil_SpecClaim_40_SDO") {
      exec(http("015_CheckYourAnswers")
        .post("/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
        .headers(Headers.commonHeader)
        .check(status.is(204)))
    }

    .group("Civil_SpecClaim_40_SDO") {
      exec(http("020_CheckYourAnswers")
        .get("/api/wa-supported-jurisdiction/get")
        .headers(Headers.commonHeader)
        .check(substring("CIVIL")))
    }
    .pause(150)

    // ===============================TASK TAB======================,
    .group("Civil_SpecClaim_40_SDO") {
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
    .group("Civil_SpecClaim_40_SDO") {
      exec(http("005_AssignToMe")
        .post("/workallocation/task/#{judgeId}/claim")
        .headers(Headers.commonHeader)
        .check(status.is(204)))

      exec(http("010_AssignToMe")
        .post("/workallocation/case/task/#{caseId}")
        .headers(Headers.commonHeader)
        .body(StringBody("""{"refined": true}""".stripMargin))
//        .check(regex("id\":\"(.*?)\"").optional.saveAs("judgeId"))
        .check(substring("task_system")))

      .exec(http("015_AssignToMe")
        .post("/api/role-access/roles/getJudicialUsers")
        .body(StringBody("""{"userIds": ["#{uId}"], "services": ["CIVIL"]}""".stripMargin))
        .headers(Headers.commonHeader)
        .check(status.in(200, 406)))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================SELECT DIRECTION SMALL CLAIM COURT========================,
    .group("Civil_SpecClaim_40_SDO") {
      exec(http("005_SmallClaims")
        .get("/cases/case-details/#{caseId}/trigger/CREATE_SDO/CREATE_SDOSmallClaims?tid=#{judgeId}")
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

      .exec(http("010_SmallClaims")
        .get("/workallocation/case/tasks/#{caseId}/event/CREATE_SDO/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(Headers.commonHeader)
        .check(substring("case_management_category")))

      .exec(http("015_SmallClaims")
        .get("/data/internal/cases/#{caseId}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{caseId}")))

      .exec(http("020_SmallClaims")
        .get("/data/internal/profile")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-user-profile.v2+json;charset=UTF-8")
        .check(substring("#{judgeuser}")))

      .exec(http("025_SmallClaims")
        .get("/data/internal/cases/#{caseId}/event-triggers/CREATE_SDO?ignore-warning=false")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
        .check(substring("Standard Direction Order"))
        .check(jsonPath("$.event_token").saveAs("event_token_judge")))
      .exitHereIf(session => !session.contains("event_token_judge"))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ===========================SDO AMOUNT===========================,
    .group("Civil_SpecClaim_40_SDO") {
      exec(http("005_SDOAmount")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_SDOSDO")
        .headers(Headers.validateHeader)
        .body(ElFileBody("d_SDO_Judge_bodies/judgeJudgementSum.dat"))
        .check(substring("drawDirectionsOrderRequired")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ========================FAST TRACK==================,
    .group("Civil_SpecClaim_40_SDO") {
      exec(http("005_ClaimsTrack")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_SDOClaimsTrack")
        .headers(Headers.validateHeader)
        .body(ElFileBody("d_SDO_Judge_bodies/judgeFastTrack.dat"))
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/case-types/CIVIL/validate?" +
          "pageId=CREATE_SDOClaimsTrack")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ========================ORDER DETAILS====================,
    .group("Civil_SpecClaim_40_SDO") {
      exec(http("005_OrderDetails")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_SDOFastTrack")
        .headers(Headers.validateHeader)
        .body(ElFileBody("d_SDO_Judge_bodies/judgeSDODetails.dat"))
        .check(jsonPath("$.data.sdoOrderDocument.createdDatetime").saveAs("createdDatetime"))
        .check(jsonPath("$.data.sdoOrderDocument.documentLink.document_url").saveAs("sdoDocument_url"))
        .check(jsonPath("$.data.sdoOrderDocument.documentLink.document_hash").saveAs("sdoDocument_hash"))
        .check(jsonPath("$.data.sdoOrderDocument.documentName").saveAs("sdoDocumentName"))
        .check(jsonPath("$.data.sdoOrderDocument.documentSize").saveAs("documentSize")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ========================VIEW SDO==============,
    .group("Civil_SpecClaim_40_SDO") {
      exec(http("005_ViewSDO")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_SDOOrderPreview")
        .headers(Headers.validateHeader)
        .body(ElFileBody("d_SDO_Judge_bodies/judgeViewOrder.dat"))
        .check(substring("sdoOrderDocument")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ======================SUBMIT SDO===================,
    .group("Civil_SpecClaim_40_SDO") {
      exec(http("005_SubmitSDO")
        .get("/workallocation/task/#{judgeId}")
        .headers(Headers.commonHeader)
        .check(substring("role_category")))

      .exec(http("010_SubmitSDO")
        .post("/data/cases/#{caseId}/events")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
        .body(ElFileBody("d_SDO_Judge_bodies/judgeSubmitSDO.dat"))
        .check(substring("Your order has been issued")))

      .exec(http("015_SubmitSDO")
        .post("/workallocation/task/#{judgeId}/complete")
        .headers(Headers.commonHeader)
        .body(StringBody("""{"actionByEvent": true, "eventName": "Standard Direction Order"}""".stripMargin))
        .check(status.in(204, 403)))

      .exec(http("020_SubmitSDO")
        .get("/data/internal/cases/#{caseId}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{caseId}")))
    }
    .pause(MinThinkTime, MaxThinkTime)
}