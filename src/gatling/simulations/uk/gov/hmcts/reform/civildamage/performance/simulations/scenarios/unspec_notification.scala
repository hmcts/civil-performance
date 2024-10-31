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
        .headers(headers_110))
        .pause(5)
        .exec(http("010_warning")
          .get("/data/internal/cases/#{caseId}/event-triggers/NOTIFY_DEFENDANT_OF_CLAIM?ignore-warning=false")
          .headers(headers_111)
          .check(status.in(200, 304))
          .check(jsonPath("$.event_token").optional.saveAs("event_token")))

        .exec(http("015_Profile")
          .get("/data/internal/profile")
          .headers(headers_112))

        .exec(http("020_jurisdiction")
          .get("/workallocation/case/tasks/#{caseId}/event/NOTIFY_DEFENDANT_OF_CLAIM/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(headers_113))
    }
        .pause(10)

      // =======================DF REP NOTIFY=======================,
  .group("Civil_UnSpecClaim_10_NotifyDEF") {
        exec(http("005_CLAIMAccessGrantedWarning")
          .post("/data/case-types/CIVIL/validate?pageId=NOTIFY_DEFENDANT_OF_CLAIMAccessGrantedWarning")
          .headers(headers_114)
          .body(ElFileBody("ua_unspec_CreateClaim_Bodies/0114_request.dat")))
      }
          .pause(10)

      // =======================SUBMIT NOTIFY=======================,
      .group("Civil_UnSpecClaim_10_NotifyDEF") {
        exec(http("330_SubmitNotification")
          .post("/data/cases/#{caseId}/events")
          .headers(headers_115)
          .body(ElFileBody("ua_unspec_CreateClaim_Bodies/0115_request.dat")))
      }
          .pause(10)

      // =======================CLOSE & RETURN TO CASE DETAILS=======================,
      .group("Civil_UnSpecClaim_10_NotifyDEF") {
        exec(http("005_jurisdiction")
          .get("/api/wa-supported-jurisdiction/get")
          .headers(headers_117))

          .exec(http("010_LabellingRoleAssignment")
            .post("/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
            .headers(headers_118)
            .body(ElFileBody("ua_unspec_CreateClaim_Bodies/0118_request.bin")))
      }
      .pause(10)


  val NotifyClaimDetails =
group("Civil_UnSpecClaim_10_NotifyDetails") {
  // =======================NOTIFY CLAIM DETAILS=======================,
  exec(http("005_jurisdiction")
    .get("/workallocation/case/tasks/#{caseId}/event/NOTIFY_DEFENDANT_OF_CLAIM_DETAILS/caseType/CIVIL/jurisdiction/CIVIL")
    .headers(headers_131))

    .exec(http("010_IgnoreWarning")
      .get("/data/internal/cases/#{caseId}/event-triggers/NOTIFY_DEFENDANT_OF_CLAIM_DETAILS?ignore-warning=false")
      .headers(headers_132)
      .check(status.in(200, 304))
      .check(jsonPath("$.event_token").optional.saveAs("event_token")))

    .exec(http("015_profile")
      .get("/data/internal/profile")
      .headers(headers_133))

    .exec(http("020_jurisdiction")
      .get("/workallocation/case/tasks/#{caseId}/event/NOTIFY_DEFENDANT_OF_CLAIM_DETAILS/caseType/CIVIL/jurisdiction/CIVIL")
      .headers(headers_134))
}
      .pause(19)

      // =======================UPLOAD DOC PARTICULARS=======================,
      .group("Civil_UnSpecClaim_10_NotifyDetails") {
        exec(http("005_DetailsUpload")
          .post("/data/case-types/CIVIL/validate?pageId=NOTIFY_DEFENDANT_OF_CLAIM_DETAILSUpload")
          .headers(headers_135)
          .body(ElFileBody("ua_unspec_CreateClaim_Bodies/0135_request.dat")))
      }
      .pause(10)

      // =======================SUBMIT CLAIM DETAILS=======================,
      .group("Civil_UnSpecClaim_10_NotifyDetails") {
        exec(http("005_SubmitNotifyDetails")
          .post("/data/cases/#{caseId}/events")
          .headers(headers_136)
          .body(ElFileBody("ua_unspec_CreateClaim_Bodies/0136_request.dat")))
      }
}
