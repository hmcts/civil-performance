
package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, Common, Environment,Headers,CsrfCheck}

import java.io.{BufferedWriter, FileWriter}

  object CaseProg {

  val BaseURL = Environment.baseURL
  val IdAMURL = Environment.idamURL
  val IdamUrl = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val caseFeeder=csv("caseIds.csv").circular

  /*======================================================================================
             Case Notes - Judge User
  ==========================================================================================*/
  val JudgeCaseNotes =


    exec(_.setAll(
      "LRrandomString" -> Common.randomString(5))
    )
      //val createclaim =
      /*======================================================================================
                        * Civil Progression - Search Case
      ==========================================================================================*/
      .group("CivilCaseProg_CaseNotes_030_SearchCase") {
        exec(http("CivilCaseProg_CaseNotes_030_SearchCase")
          .get(BaseURL + "/data/internal/cases/#{caseId}")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .check(substring("caseFile")))
      }
      .pause(MinThinkTime, MaxThinkTime)
      //val startCreateClaim =
      /*======================================================================================
                   *  Civil Progression - 'Add a Case Note' event
        ==========================================================================================*/
      .group("CivilCaseProg_CaseNotes_040_AddACaseNote") {
        exec(http("CivilCaseProg_CaseNotes_040_005_AddACaseNote")
          .get("/workallocation/case/tasks/#{caseId}/event/EVIDENCE_UPLOAD_JUDGE/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .check(substring("task_required_for_event"))
        )

          .exec(http("CivilCaseProg_CaseNotes_040_010_AddACaseNote")
            .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/EVIDENCE_UPLOAD_JUDGE?ignore-warning=false")
            .headers(CivilDamagesHeader.headers_notify)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
            .check(substring("EVIDENCE_UPLOAD_JUDGE"))
            //  .check(jsonPath("$.case_fields[4].formatted_value.partyID").saveAs("repPartyID"))
            //  .check(jsonPath("$.case_fields[4].formatted_value.partyName").saveAs("partyName"))
            .check(jsonPath("$.event_token").saveAs("event_token"))
          )
          .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))

      }
      .pause(MinThinkTime, MaxThinkTime)




      /*======================================================================================
                   *  Civil Progression - Document with a Note
        ==========================================================================================*/
      .group("CivilCaseProg_CaseNotes_050_DocWithANote") {
        exec(http("CivilCaseProg_CaseNotes_050_005_DocWithANote")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=EVIDENCE_UPLOAD_JUDGECaseNoteSelection")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/CaseProg/DocWithANote.json"))
          .check(substring("caseNoteType"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
                 *  Civil Progression - Upload Document
      ==========================================================================================*/
      .group("CivilCaseProg_CaseNotes_060_UploadCaseNoteDoc") {
        exec(http("CivilCaseProg_CaseNotes_060_005_UploadCaseNoteDoc")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=EVIDENCE_UPLOAD_JUDGECaseNoteSelection")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/CaseProg/DocWithANote.json"))
          .check(substring("caseNoteType"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)

}
