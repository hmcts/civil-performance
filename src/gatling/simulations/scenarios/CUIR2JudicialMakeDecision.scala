package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.{Environment, Common, Headers}
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/*======================================================================================
               * Civil UI Claim - Respond to the Defendant's Request for a change
======================================================================================*/

case object CUIR2JudicialMakeDecision {

  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime
  val manageCaseURL = Environment.manageCaseURL
  val patternDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
  val now = LocalDate.now()

  val run = {

    group("XUI_JudicialOrder_010_ViewCase") {
      exec(http("XUI_JudicialOrder_010_005_ViewCase")
        .get(manageCaseURL + "/data/internal/cases/#{newClaimNumber}")
        .headers(Headers.commonHeader)
        .header("Content-Type", "application/json")
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .header("x-xsrf-token", "#{XSRFToken}")
        .header("experimental", "true"))

        .exec(Common.manageLabellingRoleAssignment)
    }

    .pause(MinThinkTime , MaxThinkTime)

    .group("XUI_JudicialOrder_020_SelectMakeDecision") {
      exec(http("XUI_JudicialOrder_020_005_SelectMakeDecision")
        .get(manageCaseURL + "/data/internal/cases/#{newClaimNumber}/event-triggers/MAKE_DECISION?ignore-warning=false")
        .headers(Headers.commonHeader)
        .header("Content-Type", "application/json")
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
        .check(jsonPath("$.event_token").saveAs("event_token"))
        .check(jsonPath("$.case_fields[7].value.judgeRecitalText").saveAs("judgeRecitalText")))

        .exec(http("XUI_JudicialOrder_020_010_GetTask")
          .get(manageCaseURL + "/workallocation/case/tasks/#{newClaimNumber}/event/MAKE_DECISION/caseType/GENERALAPPLICATION/jurisdiction/CIVIL")
          .headers(Headers.commonHeader)
          .header("accept", "application/json")
          .check(substring("task_required_for_event")))
    }

    .pause(MinThinkTime , MaxThinkTime)

    .exec(http("XUI_JudicialOrder_030_MakeDecisionPage1")
      .post(manageCaseURL + "/data/case-types/GENERALAPPLICATION/validate?pageId=MAKE_DECISIONGAJudicialDecision")
      .headers(Headers.commonHeader)
      .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
      .header("x-xsrf-token", "#{XSRFToken}")
      .body(ElFileBody("bodies/lips/judicial/Judicial_MakeDecision_Page1.json")))

    .pause(MinThinkTime , MaxThinkTime)

    .exec(_.set("currentDate", now.format(patternDate)))

    .exec(http("XUI_JudicialOrder_040_MakeDecisionPage2")
      .post(manageCaseURL + "/data/case-types/GENERALAPPLICATION/validate?pageId=MAKE_DECISIONGAJudicialMakeADecisionScreen")
      .headers(Headers.commonHeader)
      .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
      .header("x-xsrf-token", "#{XSRFToken}")
      .body(ElFileBody("bodies/lips/judicial/Judicial_MakeDecision_Page2.json"))
      .check(jsonPath("$.data.judicialMakeOrderDocPreview.document_url").saveAs("documentUrl"))
      .check(jsonPath("$.data.judicialMakeOrderDocPreview.document_filename").saveAs("documentFilename"))
      .check(jsonPath("$.data.judicialMakeOrderDocPreview.document_hash").saveAs("documentHash")))

    .pause(MinThinkTime , MaxThinkTime)

    .exec(http("XUI_JudicialOrder_050_MakeDecisionPage3")
      .post(manageCaseURL + "/data/case-types/GENERALAPPLICATION/validate?pageId=MAKE_DECISIONGAJudicialMakeADecisionDocPreview")
      .headers(Headers.commonHeader)
      .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
      .header("x-xsrf-token", "#{XSRFToken}")
      .body(ElFileBody("bodies/lips/judicial/Judicial_MakeDecision_Page3.json")))

    .pause(MinThinkTime , MaxThinkTime)

    .exec(http("XUI_JudicialOrder_060_MakeDecisionSubmit")
      .post(manageCaseURL + "/data/cases/#{newClaimNumber}/events")
      .headers(Headers.commonHeader)
      .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
      .header("x-xsrf-token", "#{XSRFToken}")
      .body(ElFileBody("bodies/lips/judicial/Judicial_MakeDecision_Submit.json")))

    .pause(MinThinkTime , MaxThinkTime)

    .exec(http("XUI_JudicialOrder_070_ViewCase")
      .get(manageCaseURL + "/data/internal/cases/#{newClaimNumber}")
      .headers(Headers.commonHeader)
      .header("Content-Type", "application/json")
      .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
      .header("x-xsrf-token", "#{XSRFToken}")
      .header("experimental", "true"))

    .exec(Common.manageLabellingRoleAssignment)
  }
}