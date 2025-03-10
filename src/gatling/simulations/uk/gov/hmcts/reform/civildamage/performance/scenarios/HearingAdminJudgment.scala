package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef.{ElFileBody, _}
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils._


object HearingAdminJudgment{

  val BaseURL = Environment.baseURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  //BASE URL REMINDER!!

  val SetAsideJudgment =
    exec(_.set("uId_hearing", "4581b838-873e-4d28-8134-50eb1edb3d43"))

    // =======================LANDING PAGE==================,
    .group("HearingAdmin_SetAside_600_LandingPage") {
      exec(http("005_HealthCheck")
        .get(BaseURL + "/api/healthCheck?path=%2Fwork%2Fmy-work%2Flist")
        .headers(Headers.commonHeader)
        .check(substring("healthState")))

      .exec(http("005_RegionLocation")
        .post(BaseURL + "/workallocation/region-location")
        .headers(Headers.commonHeader)
        .body(StringBody("""{"serviceIds": ["CIVIL"]}""".stripMargin))
        .check(substring("regionId")))

      .exec(http("005_GetMyAccess")
        .get(BaseURL + "/api/role-access/roles/get-my-access-new-count")
        .headers(Headers.commonHeader)
        .check(substring("count")))

      .exec(http("010_GetMyAccess")
        .get(BaseURL + "/api/role-access/roles/get-my-access-new-count")
        .headers(Headers.commonHeader)
        .check(substring("count")))

      .exec(http("005_TypesOfWork")
        .get(BaseURL + "/workallocation/task/types-of-work")
        .headers(Headers.commonHeader)
        .check(substring("Hearing work")))

      .exec(http("005_Jurisdiction")
        .get(BaseURL + "/api/wa-supported-jurisdiction/get")
        .headers(Headers.commonHeader)
        .check(substring("CIVIL")))

      .exec(http("005_WorkAllocationTask")
        .post(BaseURL + "/workallocation/task")
        .body(StringBody(
          """{"searchRequest": {"search_parameters": [ {"key": "user", "operator":"IN", "values":
            |["#{uId_hearing}"]}, {"key": "state", "operator":"IN", "values":["assigned"]} ], "sorting_parameters": [],
            |"search_by": "caseworker"}, "view": "MyTasks"}""".stripMargin))
        .headers(Headers.commonHeader)
        .check(substring("total_records")))

      .exec(http("010_Jurisdiction")
        .get(BaseURL + "/api/wa-supported-jurisdiction/get")
        .headers(Headers.commonHeader)
        .check(substring("CIVIL")))

      .exec(http("005_GetUsersByServiceName")
        .post(BaseURL + "/workallocation/caseworker/getUsersByServiceName")
        .body(StringBody("""{"services": ["IA", "CIVIL", "PRIVATELAW", "PUBLICLAW", "SSCS", "ST_CIC",
                           |"EMPLOYMENT"]}""".stripMargin))
        .headers(Headers.commonHeader)
        .check(substring("BIRMINGHAM CIVIL AND FAMILY JUSTICE CENTRE")))

      .exec(http("010_WorkAllocationTask")
        .post(BaseURL + "/workallocation/task")
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
    .group("HearingAdmin_SetAside_610_OpenCase") {
      exec(http("005_OpenCase")
        .get(BaseURL + "/data/internal/cases/#{caseId}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{caseId}")))

      .exec(http("010_OpenCase")
        .post(BaseURL + "/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
        .headers(Headers.commonHeader)
        .check(status.is(204)))

      .exec(http("015_OpenCase")
        .get(BaseURL + "/api/wa-supported-jurisdiction/get")
        .headers(Headers.commonHeader)
        .check(substring("CIVIL")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================SET ASIDE (DROPDOWN)=======================,
    .group("HearingAdmin_SetAside_620_EventTrigger") {
      exec(http("005_Jurisdiction")
        .get(BaseURL + "/workallocation/case/tasks/#{caseId}/event/SET_ASIDE_JUDGMENT/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(Headers.commonHeader)
        .check(substring("task_required_for_event")))

      .exec(http("010_Profile")
        .get(BaseURL + "/data/internal/profile")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-user-profile.v2+json;charset=UTF-8")
        .check(substring("#{centreadminuser}")))

      .exec(http("015_IgnoreWarning")
        .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/SET_ASIDE_JUDGMENT?ignore-warning=false")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
        .check(substring("Why is this judgment being set aside?"))
        .check(jsonPath("$.event_token").saveAs("event_token_admin")))
      .exitHereIf(session => !session.contains("event_token_admin"))

      .exec(http("020_Jurisdiction")
        .get(BaseURL + "/workallocation/case/tasks/#{caseId}/event/SET_ASIDE_JUDGMENT/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(Headers.commonHeader)
        .check(substring("task_required_for_event")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================WHY SET ASIDE=======================,
    .group("HearingAdmin_SetAside_630_WhySetAside") {
      exec(http("HearingAdmin_SetAside_630_WhySetAside")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=SET_ASIDE_JUDGMENTSetAsideJudgment")
        .headers(Headers.validateHeader)
        .body(ElFileBody("bodies/HearingAdminJudgment/setAsideJudgeMadeOrder.json"))
        .check(substring("joSetAsideReason")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================DATE OF ORDER=======================,
    .group("HearingAdmin_SetAside_640_DateOfOrder") {
      exec(http("HearingAdmin_SetAside_640_DateOfOrder")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=SET_ASIDE_JUDGMENTSetAsideOrderType")
        .headers(Headers.validateHeader)
        .body(ElFileBody("bodies/HearingAdminJudgment/setAsideOrderDate.json"))
        .check(substring("joSetAsideApplicationDate")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================SUBMIT=======================,
    .group("HearingAdmin_SetAside_650_Submit") {
      exec(http("HearingAdmin_SetAside_650_Submit")
        .post(BaseURL + "/data/cases/#{caseId}/events")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
        .body(ElFileBody("bodies/HearingAdminJudgment/setAsideSubmit.json"))
        .check(substring("The judgment has been set aside")))

      .exec(http("HearingAdmin_SetAside_655_Submit")
        .get(BaseURL + "/data/internal/cases/#{caseId}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{caseId}")))
    }
    .pause(MinThinkTime, MaxThinkTime)


  val MarkJudgmentAsPaid =
    exec(_.set("uId_hearing", "4581b838-873e-4d28-8134-50eb1edb3d43"))

    // =======================LANDING PAGE==================,
    .group("HearingAdmin_MarkAsPaid_600_LandingPage") {
      exec(http("005_HealthCheck")
        .get(BaseURL + "/api/healthCheck?path=%2Fwork%2Fmy-work%2Flist")
        .headers(Headers.commonHeader)
        .check(substring("healthState")))

      .exec(http("005_RegionLocation")
        .post(BaseURL + "/workallocation/region-location")
        .headers(Headers.commonHeader)
        .body(StringBody("""{"serviceIds": ["CIVIL"]}""".stripMargin))
        .check(substring("regionId")))

      .exec(http("005_GetMyAccess")
        .get(BaseURL + "/api/role-access/roles/get-my-access-new-count")
        .headers(Headers.commonHeader)
        .check(substring("count")))

      .exec(http("010_GetMyAccess")
        .get(BaseURL + "/api/role-access/roles/get-my-access-new-count")
        .headers(Headers.commonHeader)
        .check(substring("count")))

      .exec(http("005_TypesOfWork")
        .get(BaseURL + "/workallocation/task/types-of-work")
        .headers(Headers.commonHeader)
        .check(substring("Hearing work")))

      .exec(http("005_Jurisdiction")
        .get(BaseURL + "/api/wa-supported-jurisdiction/get")
        .headers(Headers.commonHeader)
        .check(substring("CIVIL")))

      .exec(http("005_WorkAllocationTask")
        .post(BaseURL + "/workallocation/task")
        .body(StringBody(
          """{"searchRequest": {"search_parameters": [ {"key": "user", "operator":"IN", "values":
            |["#{uId_hearing}"]}, {"key": "state", "operator":"IN", "values":["assigned"]} ], "sorting_parameters": [],
            |"search_by": "caseworker"}, "view": "MyTasks"}""".stripMargin))
        .headers(Headers.commonHeader)
        .check(substring("total_records")))

      .exec(http("010_Jurisdiction")
        .get(BaseURL + "/api/wa-supported-jurisdiction/get")
        .headers(Headers.commonHeader)
        .check(substring("CIVIL")))

      .exec(http("005_GetUsersByServiceName")
        .post(BaseURL + "/workallocation/caseworker/getUsersByServiceName")
        .body(StringBody("""{"services": ["IA", "CIVIL", "PRIVATELAW", "PUBLICLAW", "SSCS", "ST_CIC",
                           |"EMPLOYMENT"]}""".stripMargin))
        .headers(Headers.commonHeader)
        .check(substring("BIRMINGHAM CIVIL AND FAMILY JUSTICE CENTRE")))

      .exec(http("010_WorkAllocationTask")
        .post(BaseURL + "/workallocation/task")
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
    .group("HearingAdmin_MarkAsPaid_610_OpenCase") {
      exec(http("005_OpenCase")
        .get(BaseURL + "/data/internal/cases/#{caseId}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{caseId}")))

      .exec(http("010_OpenCase")
        .post(BaseURL + "/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
        .headers(Headers.commonHeader)
        .check(status.is(204)))

      .exec(http("015_OpenCase")
        .get(BaseURL + "/api/wa-supported-jurisdiction/get")
        .headers(Headers.commonHeader)
        .check(substring("CIVIL")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================MARK AS PAID (DROPDOWN)=======================,




    // =======================TASK TAB=======================,
    .group("Civil_SpecClaim_50_HearingAdmin") {
      exec(http("005_TaskTab")
        .post(BaseURL + "/workallocation/case/task/#{caseId}")
        .headers(Headers.commonHeader)
        .body(StringBody("""{"refined": true}""".stripMargin))
        .check(status.is(200)))

        .exec(http("010_TaskTab")
          .post(BaseURL + "/workallocation/caseworker/getUsersByServiceName")
          .body(StringBody("""{"services": ["CIVIL"]}""".stripMargin))
          .headers(Headers.commonHeader)
          .check(substring("BIRMINGHAM CIVIL AND FAMILY JUSTICE CENTRE")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================ASSIGN TO ME=======================,
    //    .group("Civil_SpecClaim_50_HearingAdmin") {
    //      exec(http("005_AssignToMe")
    //        .post(BaseURL + "/workallocation/task/#{HearingCaseId}/claim")
    //        .headers(Headers.commonHeader)
    //        .check(status.is(204)))
    //    }

    .group("Civil_SpecClaim_50_HearingAdmin") {
      exec(http("010_AssignToMe")
        .post(BaseURL + "/workallocation/case/task/#{caseId}")
        .headers(Headers.commonHeader)
        .body(StringBody("""{"refined": true}""".stripMargin))
        //.check(substring("task_system"))
        .check(jsonPath("$[0].id").optional.saveAs("HearingCaseId")))
    }
    .pause(MinThinkTime, MaxThinkTime)
}