package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef.{ElFileBody, _}
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils._

object XUIJudgeSDO {

  val BaseURL = Environment.baseURL
  val IdamURL = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val standardDirectionOrder =

    exec(_.set("uId", "66bec075-0ae0-4743-a20d-05febfb2081a"))

    // =======================LANDING PAGE==================,
    .group("Civil_SpecClaim_50_010_SDO_Land") {
      exec(http("005_HealthCheck")
        .get(BaseURL + "/api/healthCheck?path=%2Fwork%2Fmy-work%2Flist")
        .headers(Headers.commonHeader)
        .check(substring("healthState")))

      .exec(http("005_RegionLocation")
        .post(BaseURL + "/workallocation/region-location")
        .headers(Headers.commonHeader)
        .body(StringBody("""{"serviceIds": ["PRIVATELAW", "CIVIL"]}""".stripMargin))
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
        .body(StringBody("""{"searchRequest": {"search_parameters": [ {"key": "user", "operator": "IN",
            |"values": ["#{uId}"]}, {"key": "state", "operator": "IN", "values": ["assigned"]} ],
            |"sorting_parameters": [], "search_by": "judge"}, "view": "MyTasks"}""".stripMargin))
        .headers(Headers.commonHeader)
        .check(substring("Small Claims Track Directions")))

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
        .body(StringBody("""{"searchRequest": {"search_parameters": [{"key": "user", "operator": "IN", "values":
                           |["#{uId}"]}, {"key": "state", "operator": "IN", "values": ["assigned"]}, {"key":
                           |"jurisdiction", "operator": "IN", "values": ["CIVIL", "PRIVATELAW"]}], "sorting_parameters":
                           |[], "search_by": "judge", "pagination_parameters": {"page_number": 1, "page_size": 25}},
                           |"view": "MyTasks", "refined": false, "currentUser": "#{uId}"}""".stripMargin))
        .headers(Headers.commonHeader)
        .check(substring("Small Claims Track Directions")))

      .exec(http("005_GetJudicialUsers")
        .post(BaseURL + "/api/role-access/roles/getJudicialUsers")
        .body(StringBody("""{"userIds": ["#{uId}", "#{uId}", "#{uId}", "#{uId}", "#{uId}", "#{uId}", "#{uId}", "#{uId}",
                           |"#{uId}", "#{uId}", "#{uId}", "#{uId}", "#{uId}", "#{uId}", "#{uId}", "#{uId}", "#{uId}",
                           |"#{uId}", "#{uId}", "#{uId}", "#{uId}", "#{uId}", "#{uId}", "#{uId}", "#{uId}"],
                           |"services": ["CIVIL", "PRIVATELAW"]}""".stripMargin))
        .headers(Headers.commonHeader)
        .check(status.in(200, 406)))

      .exec(http("015_Jurisdiction")
        .get(BaseURL + "/aggregated/caseworkers/:uid/jurisdictions?access=read")
        .headers(Headers.commonHeader)
        .check(substring("callback_get_case_url")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================SEARCH CASE=======================,
    .group("Civil_SpecClaim_50_020_SDO_Search") {
      exec(http("005_SearchCase")
        .get(BaseURL + "/data/internal/case-types/CIVIL/work-basket-inputs")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-workbasket-input-details.v2+json;charset=UTF-8")
        .check(substring("workbasketInputs")))

      .exec(http("010_SearchCase")
        .post(BaseURL + "/data/internal/searchCases?ctid=CIVIL&use_case=WORKBASKET&view=WORKBASKET&page=1&case_reference=#{claimNumber}")
        .headers(Headers.commonHeader)
        .body(StringBody("""{"size": 25}""".stripMargin))
        .check(substring("JUDICIAL_REFERRAL")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================OPEN CASE=======================,
    .group("Civil_SpecClaim_50_030_SDO_GetCase") {
      exec(http("005_OpenCase")
        .get(BaseURL + "/data/internal/cases/#{claimNumber}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{claimNumber}")))
    }

    .group("Civil_SpecClaim_50_030_SDO_API_RoleAssignment") {
      exec(http("010_OpenCase")
        .post(BaseURL + "/api/role-access/roles/manageLabellingRoleAssignment/#{claimNumber}")
        .headers(Headers.commonHeader)
        .check(status.is(204)))
    }

    .group("Civil_SpecClaim_50_030_SDO_API_SupportedJurisdiction") {
      exec(http("015_OpenCase")
        .get(BaseURL + "/api/wa-supported-jurisdiction/get")
        .headers(Headers.commonHeader)
        .check(substring("CIVIL")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================MEDIATION UNSUCCESSFUL DROPDOWN================,
    .group("Civil_SpecClaim_50_040_SDO_StartEvent_MediationUnsuccessful") {
      exec(http("005_Jurisdiction")
        .get(BaseURL + "/workallocation/case/tasks/#{claimNumber}/event/MEDIATION_UNSUCCESSFUL/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(Headers.commonHeader)
        .check(substring("task_required_for_event")))

      .exec(http("010_Profile")
        .get(BaseURL + "/data/internal/profile")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-user-profile.v2+json;charset=UTF-8")
        .check(substring("#{judgeuser}")))

      .exec(http("015_IgnoreWarning")
        .get(BaseURL + "/data/internal/cases/#{claimNumber}/event-triggers/MEDIATION_UNSUCCESSFUL?ignore-warning=false")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
        .check(substring("MEDIATION_UNSUCCESSFUL"))
        .check(jsonPath("$.event_token").saveAs("event_token_mediation")))
      .exitHereIf(session => !session.contains("event_token_mediation"))

      .exec(http("020_HearingScheduled")
        .get(BaseURL + "/workallocation/case/tasks/#{claimNumber}/event/MEDIATION_UNSUCCESSFUL/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(Headers.commonHeader)
        .check(substring("task_required_for_event")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================APPOINTMENT NO AGREEMENT================,
    .group("Civil_SpecClaim_50_050_SDO_PartyWithdraws") {
      exec(http("005_PartyWithdraws")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=MEDIATION_UNSUCCESSFULmediationUnsuccessful")
        .headers(Headers.validateHeader)
        .body(ElFileBody("bodies/XUIJudgeSDO/mediationUnsuccessfulReason.dat"))
        .check(substring("mediationUnsuccessfulReasonsMultiSelect")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================CONTINUE=======================,
    .group("Civil_SpecClaim_50_060_SDO_Information") {
      exec(http("005_Continue")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=MEDIATION_UNSUCCESSFULWorkAllocationIntegrationFields")
        .headers(Headers.validateHeader)
        .body(ElFileBody("bodies/XUIJudgeSDO/mediationUnsuccessfulInformation.dat"))
        .check(substring("responseClaimTrack")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================CHECK YOUR ANSWERS================,
    .group("Civil_SpecClaim_50_070_SDO_Submit") {
      exec(http("005_CheckYourAnswers")
        .post(BaseURL + "/data/cases/#{claimNumber}/events")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
        .body(ElFileBody("bodies/XUIJudgeSDO/mediationUnsuccessfulSubmit.dat"))
        .check(substring("CALLBACK_COMPLETED")))
    }

    .group("Civil_SpecClaim_50_070_SDO_GetCase") {
      exec(http("010_CheckYourAnswers")
        .get(BaseURL + "/data/internal/cases/#{claimNumber}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{claimNumber}")))
    }

    .group("Civil_SpecClaim_50_070_SDO_API_RoleAssignment") {
      exec(http("015_CheckYourAnswers")
        .post(BaseURL + "/api/role-access/roles/manageLabellingRoleAssignment/#{claimNumber}")
        .headers(Headers.commonHeader)
        .check(status.is(204)))
    }

    .group("Civil_SpecClaim_50_070_SDO_API_SupportedJurisdiction") {
      exec(http("020_CheckYourAnswers")
        .get(BaseURL + "/api/wa-supported-jurisdiction/get")
        .headers(Headers.commonHeader)
        .check(substring("CIVIL")))
    }
    .exitHere
    .pause(150)

    // ===============================TASK TAB======================,
    .group("Civil_SpecClaim_60_010_SDO_TaskTab") {
      exec(http("005_TaskTab")
        .post(BaseURL + "/workallocation/case/task/#{claimNumber}")
        .headers(Headers.commonHeader)
        .body(StringBody("""{"refined": true}""".stripMargin))
        .check(regex("id\":\"(.*?)\"").optional.saveAs("judgeId")))
//        .check(substring("task_state")))

      .exec(http("010_TaskTab")
        .post(BaseURL + "/workallocation/caseworker/getUsersByServiceName")
        .headers(Headers.commonHeader)
        .body(StringBody("""{"services": ["CIVIL"]}""".stripMargin))
        .check(substring("Birmingham")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================ASSIGN TO ME=======================,
    .doIf(session => session.contains("judgeId")) (
      group("Civil_SpecClaim_60_020_SDO_AssignToMe") {
        exec(http("005_AssignToMe")
          .post(BaseURL + "/workallocation/task/#{judgeId}/claim")
          .headers(Headers.commonHeader)
          .check(status.is(204)))

        .exec(http("010_AssignToMe")
          .post(BaseURL + "/workallocation/case/task/#{claimNumber}")
          .headers(Headers.commonHeader)
          .body(StringBody("""{"refined": true}""".stripMargin))
          .check(substring("task_system")))

      .exec(http("015_AssignToMe")
        .post(BaseURL + "/api/role-access/roles/getJudicialUsers")
        .body(StringBody("""{"userIds": ["#{uId}"], "services": ["CIVIL"]}""".stripMargin))
        .headers(Headers.commonHeader)
        .check(status.in(200, 406)))
      }
    )
    .pause(MinThinkTime, MaxThinkTime)

    // =======================SELECT DIRECTION SMALL CLAIM COURT========================,
    .group("Civil_SpecClaim_60_030_SDO_OrderStart") {
      doIf(session => session.contains("judgeId")) (
        exec(http("005_SmallClaims")
          .get(BaseURL + "/cases/case-details/#{claimNumber}/trigger/CREATE_SDO/CREATE_SDOSmallClaims?tid=#{judgeId}")
          .headers(Headers.navigationHeader)
          .check(substring("flexbox no-flexboxtwenner")))
      )

      .exec(Common.configurationui)
      .exec(Common.configUI)
      .exec(Common.TsAndCs)
      .exec(Common.userDetails)
      .exec(Common.isAuthenticated)
      .exec(Common.monitoringTools)
      .exec(Common.monitoringTools)
      .exec(Common.monitoringTools)

      .exec(http("010_SmallClaims")
        .get(BaseURL + "/workallocation/case/tasks/#{claimNumber}/event/CREATE_SDO/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(Headers.commonHeader)
        .check(substring("case_management_category")))

      .exec(http("015_SmallClaims")
        .get(BaseURL + "/data/internal/cases/#{claimNumber}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{claimNumber}")))

      .exec(http("020_SmallClaims")
        .get(BaseURL + "/data/internal/profile")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-user-profile.v2+json;charset=UTF-8")
        .check(substring("#{judgeuser}")))

      .exec(http("025_SmallClaims")
        .get(BaseURL + "/data/internal/cases/#{claimNumber}/event-triggers/CREATE_SDO?ignore-warning=false")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
        .check(substring("Standard Direction Order"))
        .check(jsonPath("$.event_token").saveAs("event_token_judge")))
      .exitHereIf(session => !session.contains("event_token_judge"))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ===========================SDO AMOUNT===========================,
    .group("Civil_SpecClaim_60_040_SDO_Amount") {
      exec(http("005_SDOAmount")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_SDOSDO")
        .headers(Headers.validateHeader)
        .body(ElFileBody("bodies/XUIJudgeSDO/judgeJudgementSum.dat"))
        .check(substring("drawDirectionsOrderRequired")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ========================FAST TRACK==================,
    .group("Civil_SpecClaim_60_050_SDO_ClaimsTrack") {
      exec(http("005_ClaimsTrack")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_SDOClaimsTrack")
        .headers(Headers.validateHeader)
        .body(ElFileBody("bodies/XUIJudgeSDO/judgeFastTrack.dat"))
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/case-types/CIVIL/validate?" +
          "pageId=CREATE_SDOClaimsTrack")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ========================ORDER DETAILS====================,
    .group("Civil_SpecClaim_60_060_SDO_OrderDetails") {
      exec(http("005_OrderDetails")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_SDOFastTrack")
        .headers(Headers.validateHeader)
        .body(ElFileBody("bodies/XUIJudgeSDO/judgeSDODetails.dat"))
        .check(jsonPath("$.data.sdoOrderDocument.createdDatetime").saveAs("createdDatetime"))
        .check(jsonPath("$.data.sdoOrderDocument.documentLink.document_url").saveAs("sdoDocument_url"))
        .check(jsonPath("$.data.sdoOrderDocument.documentLink.document_hash").saveAs("sdoDocument_hash"))
        .check(jsonPath("$.data.sdoOrderDocument.documentName").saveAs("sdoDocumentName"))
        .check(jsonPath("$.data.sdoOrderDocument.documentSize").saveAs("documentSize")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ========================VIEW SDO==============,
    .group("Civil_SpecClaim_60_070_SDO_View") {
      exec(http("005_ViewSDO")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_SDOOrderPreview")
        .headers(Headers.validateHeader)
        .body(ElFileBody("bodies/XUIJudgeSDO/judgeViewOrder.dat"))
        .check(substring("sdoOrderDocument")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ======================SUBMIT SDO===================,
    .group("Civil_SpecClaim_60_08_SDO_Submit") {
      doIf(session => session.contains("judgeId")) (
        exec(http("005_SubmitSDO")
          .get(BaseURL + "/workallocation/task/#{judgeId}")
          .headers(Headers.commonHeader)
          .check(substring("role_category")))
      )

      .exec(http("010_SubmitSDO")
        .post(BaseURL + "/data/cases/#{claimNumber}/events")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
        .body(ElFileBody("bodies/XUIJudgeSDO/judgeSubmitSDO.dat"))
        .check(substring("Your order has been issued")))

//      .exec(http("015_SubmitSDO")
//        .post(BaseURL + "/workallocation/task/#{judgeId}/complete")
//        .headers(Headers.commonHeader)
//        .body(StringBody("""{"actionByEvent": true, "eventName": "Standard Direction Order"}""".stripMargin))
//        .check(status.in(204, 403)))

      .exec(http("015_SubmitSDO")
        .get(BaseURL + "/data/internal/cases/#{claimNumber}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{claimNumber}")))
    }
    .pause(MinThinkTime, MaxThinkTime)
}