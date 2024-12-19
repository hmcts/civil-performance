package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios.utils.Headers.commonHeader
import utils._
import utils.unspec_notification_Headers._

import scala.concurrent.duration._

object unspec_notification {

    val BaseURL = Environment.baseURL
    val IdamURL = Environment.idamURL
    val MinThinkTime = Environment.minThinkTime
    val MaxThinkTime = Environment.maxThinkTime


  val NotifyClaim =
      // =======================NOTIFY CLAIM=======================,
    group("Civil_UnSpecClaim_10_NotifyDEF") {
      exec(http("005_jurisdiction")
        .get("/workallocation/case/tasks/#{caseId}/event/NOTIFY_DEFENDANT_OF_CLAIM/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(Headers.commonHeader)
        .check(substring("task_required_for_event")))

      .exec(http("010_Profile")
        .get("/data/internal/profile")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-user-profile.v2+json;charset=UTF-8")
        .check(substring("#{LoginId}")))
      .pause(45)

      .exec(http("015_warning")
        .get("/data/internal/cases/#{caseId}/event-triggers/NOTIFY_DEFENDANT_OF_CLAIM?ignore-warning=false")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
        .check(substring("NOTIFY_DEFENDANT_OF_CLAIM"))
        .check(jsonPath("$.event_token").saveAs("event_token")))

      .exec(http("020_jurisdiction")
        .get("/workallocation/case/tasks/#{caseId}/event/NOTIFY_DEFENDANT_OF_CLAIM/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(Headers.commonHeader)
        .check(substring("task_required_for_event")))
    }
    .pause(MinThinkTime, MaxThinkTime)

      // =======================DF REP NOTIFY=======================,
    .group("Civil_UnSpecClaim_10_NotifyDEF") {
      exec(http("005_CLAIMAccessGrantedWarning")
        .post("/data/case-types/CIVIL/validate?pageId=NOTIFY_DEFENDANT_OF_CLAIMAccessGrantedWarning")
        .headers(Headers.validateHeader)
        .body(StringBody(
          """{"data":{},"event":{"id":"NOTIFY_DEFENDANT_OF_CLAIM","summary":"","description":""},"event_data":{},
            |"event_token":"#{event_token}","ignore_warning":false,"case_reference":"#{caseId}"}""".stripMargin))
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/case-types/CIVIL/validate?pageId=NOTIFY_DEFENDANT_OF_CLAIMAccessGrantedWarning")))
    }
    .pause(MinThinkTime, MaxThinkTime)

      // =======================SUBMIT NOTIFY=======================,
    .group("Civil_UnSpecClaim_10_NotifyDEF") {
      exec(http("330_SubmitNotification")
        .post("/data/cases/#{caseId}/events")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
        .body(StringBody(
          """{"data":{},"event":{"id":"NOTIFY_DEFENDANT_OF_CLAIM","summary":"","description":""},
            |"event_token":"#{event_token}","ignore_warning":false}""".stripMargin))
        .check(substring("after_submit_callback_response")))

      .exec(http("335_getCases")
        .get("/data/internal/cases/#{caseId}")
        .headers(Headers.validateHeader)
        .header("accept","application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("NOTIFY_DEFENDANT_OF_CLAIM")))
    }
    .pause(MinThinkTime, MaxThinkTime)

      // =======================CLOSE & RETURN TO CASE DETAILS=======================,
    .group("Civil_UnSpecClaim_10_NotifyDEF") {
      exec(http("005_APIRoleAssignment")
        .post("/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
        .headers(Headers.commonHeader)
        .check(status.is(204)))
    }

    .group("Civil_UnSpecClaim_10_NotifyDEF") {
      exec(http("010_SupportedJurisdiction")
        .get("/api/wa-supported-jurisdiction/get")
        .headers(Headers.commonHeader)
        .check(substring("CIVIL")))
    }
    .pause(MinThinkTime, MaxThinkTime)


  //val NotifyClaimDetails =
    .group("Civil_UnSpecClaim_10_NotifyDetails") {
      // =======================NOTIFY CLAIM DETAILS=======================,
      exec(http("005_jurisdiction")
        .get("/workallocation/case/tasks/#{caseId}/event/NOTIFY_DEFENDANT_OF_CLAIM_DETAILS/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(Headers.commonHeader)
        .check(substring("task_required_for_event")))
        //.pause(45)

      .exec(http("010_IgnoreWarning")
        .get("/data/internal/cases/#{caseId}/event-triggers/NOTIFY_DEFENDANT_OF_CLAIM_DETAILS?ignore-warning=false")
        .headers(Headers.validateHeader)
        .header("accept","application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
        .check(substring("NOTIFY_DEFENDANT_OF_CLAIM_DETAILS"))
        .check(jsonPath("$.event_token").saveAs("event_token")))

//      .exec(http("015_profile")
//        .get("/data/internal/profile")
//        .headers(Headers.UIUserProfileHeader))

      .exec(http("020_jurisdiction")
        .get("/workallocation/case/tasks/#{caseId}/event/NOTIFY_DEFENDANT_OF_CLAIM_DETAILS/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(Headers.commonHeader)
        .check(substring("task_required_for_event")))
    }
    .pause(MinThinkTime, MaxThinkTime)

      // =======================UPLOAD DOC PARTICULARS=======================,
    .group("Civil_UnSpecClaim_10_NotifyDetails") {
      exec(http("005_DetailsUpload")
        .post("/data/case-types/CIVIL/validate?pageId=NOTIFY_DEFENDANT_OF_CLAIM_DETAILSUpload")
        .headers(Headers.validateHeader)
        .body(StringBody(
          """{"data":{"servedDocumentFiles":{"particularsOfClaimText":"NFT","particularsOfClaimDocument":[],"medicalReport":[],
            |"scheduleOfLoss":[],"certificateOfSuitability":[],"other":[],"timelineEventUpload":[]}},
            |"event":{"id":"NOTIFY_DEFENDANT_OF_CLAIM_DETAILS","summary":"","description":""},
            |"event_data":{"servedDocumentFiles":{"particularsOfClaimText":"NFT","particularsOfClaimDocument":[],"medicalReport":[],
            |"scheduleOfLoss":[],"certificateOfSuitability":[],"other":[],"timelineEventUpload":[]}},"event_token":"#{event_token}",
            |"ignore_warning":false,"case_reference":"#{caseId}"}""".stripMargin))
        .check(substring("caseNamePublic")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================SUBMIT CLAIM DETAILS=======================,
    .group("Civil_UnSpecClaim_10_NotifyDetails") {
      exec(http("005_SubmitNotifyDetails")
        .post("/data/cases/#{caseId}/events")
        .headers(Headers.validateHeader)
        .header("accept","application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
        .body(StringBody(
          """{"data":{"servedDocumentFiles":{"particularsOfClaimText":"NFT","particularsOfClaimDocument":[],
            |"medicalReport":[],"scheduleOfLoss":[],"certificateOfSuitability":[],"other":[],"timelineEventUpload":[]}},
            |"event":{"id":"NOTIFY_DEFENDANT_OF_CLAIM_DETAILS","summary":"","description":""},
            |"event_token":"#{event_token}","ignore_warning":false}""".stripMargin))
//        .body(ElFileBody("ua_unspec_CreateClaim_Bodies/judgeViewOrder.dat"))
        .check(substring("Defendant notified")))

      .exec(http("010_getCases")
        .get("/data/internal/cases/#{caseId}")
        .headers(Headers.validateHeader)
        .header("accept","application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("NOTIFY_DEFENDANT_OF_CLAIM_DETAILS")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =======================CLOSE & RETURN TO CASE DETAILS=======================,
    .group("Civil_UnSpecClaim_10_NotifyDetails") {
      exec(http("005_APIRoleAssignment")
        .post("/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
        .headers(Headers.commonHeader)
        //.body(StringBody("""{}"""))
        .check(status.is(204)))
    }

    .group("Civil_UnSpecClaim_10_NotifyDetails") {
      exec(http("010_SupportedJurisdiction")
        .get("/api/wa-supported-jurisdiction/get")
        .headers(Headers.commonHeader)
        .check(substring("CIVIL")))
    }
    .pause(MinThinkTime, MaxThinkTime)
}
