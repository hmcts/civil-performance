package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{Environment, Headers}


object CUIDraftSearch {
  
  val minThinkTime = Environment.minThinkTime
  val maxThinkTime = Environment.maxThinkTime
  
  val cuiURL = Environment.citizenURL
  
  def draftSearch (prefix: String, taskListText: String): ChainBuilder =
  
  // clear Gatling's HTTP cache so pages already seen during login return 200 (not 304)
    exec(flushHttpCache)
      
      // Dashboard - CUI looks up the user's draft
      .group(s"${prefix}_010_Dashboard") {
        exec(http(s"${prefix}_010_005_Dashboard")
          .get(cuiURL + "/dashboard")
          .headers(Headers.navigationHeader)
          .check(status.is(200))
          .check(substring("To view or progress your claim click on your claim number")))
      }
      .pause(minThinkTime, maxThinkTime)
      
      // Draft claim dashboard - "Your claim is saved as a draft"
      .group(s"${prefix}_020_ContinueClaim_ClaimantNewDesign") {
        exec(http(s"${prefix}_020_005_ClaimantNewDesign")
          .get(cuiURL + "/dashboard/draft/claimantNewDesign")
          .headers(Headers.navigationHeader)
          .check(status.is(200))
          .check(substring("Your claim is saved as a draft")))
      }
      .pause(minThinkTime, maxThinkTime)
      
      // Task list - loads the full draft from the draft store
      .group(s"${prefix}_030_ContinueClaim_TaskList") {
        exec(http(s"${prefix}_030_005_TaskList")
          .get(cuiURL + "/claim/task-list")
          .headers(Headers.navigationHeader)
          .check(status.is(200))
          .check(substring(taskListText)))
      }
      .pause(minThinkTime, maxThinkTime)
      
      // Check your answers - reads the whole draft
      .group(s"${prefix}_040_CheckYourAnswers") {
        exec(http(s"${prefix}_040_005_CheckYourAnswers")
          .get(cuiURL + "/claim/check-and-send")
          .headers(Headers.navigationHeader)
          .check(status.is(200))
          .check(substring("Check your answers")))
      }
      .pause(minThinkTime, maxThinkTime)
  
  // Redis-origin drafts (testing-support)
  val DraftSearch = draftSearch("CUIDraftSearch", "Application incomplete")
  
  // CMC DB-origin drafts (created through the CUI claim journey) - reported separately
  val DraftSearchDB = draftSearch("CUIDraftSearchDB", "Application incomplete")
  
}