package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.Environment

/*======================================================================================
* Business process : CUI Draft Search - citizen resumes an existing draft claim
* Read-only (no saves/submit), so the same users and drafts can be reused every run.
*
* Store-agnostic: CUI decides where the draft lives, so this same script works for
*   - Baseline (flag OFF -> Redis) and
*   - CMC DB runs (flag ON -> civil-service /dashboard/draft-claims -> Postgres)
* giving a like-for-like comparison.
*
* Pre-requisites (from your existing scripts):
*   1. feed a citizen user that already has a draft (CUIDraftClaimsRedis.csv / CUIDraftClaimsDB.csv)
*   2. run your existing CUI login chain (CUIR2 homepage + login steps) so the session cookie is set
*   3. then exec(CUIDraftSearch.DraftSearch), then your existing logout
*
* TODO: confirm URLs and text checks from a browser recording of the resume journey in perftest.
 ======================================================================================*/

object CUIDraftSearch {

	val minThinkTime = Environment.minThinkTime
	val maxThinkTime = Environment.maxThinkTime

	// TODO: replace with the CUI base URL value your existing claim scripts use
	val cuiURL = "https://civil-citizen-ui.perftest.platform.hmcts.net"

	val DraftSearch =

		// Dashboard - CUI looks up the user's draft to show "continue your claim"
		group("CUIDraftSearch_010_Dashboard") {
			exec(http("CUIDraftSearch_010_005_Dashboard")
				.get(cuiURL + "/dashboard")                         // TODO confirm
				.headers(Map("accept" -> "text/html,application/xhtml+xml"))
				.check(status.is(200))
				.check(substring("TODO_draft_claim_link_text")))     // TODO: text shown only when a draft exists
		}
		.pause(minThinkTime, maxThinkTime)

		// Resume draft - task list loads the full draft from the draft store
		.group("CUIDraftSearch_020_ContinueClaim_TaskList") {
			exec(http("CUIDraftSearch_020_005_TaskList")
				.get(cuiURL + "/claim/task-list")                   // TODO confirm
				.headers(Map("accept" -> "text/html,application/xhtml+xml"))
				.check(status.is(200))
				.check(substring("TODO_task_list_heading")))
		}
		.pause(minThinkTime, maxThinkTime)

		// A page that displays saved draft answers (draft read)
		.group("CUIDraftSearch_030_ViewSavedAnswers") {
			exec(http("CUIDraftSearch_030_005_ViewSavedAnswers")
				.get(cuiURL + "/TODO_claimant_details_page")        // TODO: e.g. your details page from the claim journey
				.headers(Map("accept" -> "text/html,application/xhtml+xml"))
				.check(status.is(200)))
		}
		.pause(minThinkTime, maxThinkTime)

		// Check your answers - reads the whole draft
		.group("CUIDraftSearch_040_CheckYourAnswers") {
			exec(http("CUIDraftSearch_040_005_CheckYourAnswers")
				.get(cuiURL + "/claim/check-and-send")              // TODO confirm (CheckAndSendGet in your claim script)
				.headers(Map("accept" -> "text/html,application/xhtml+xml"))
				.check(status.in(200, 302)))
		}
		.pause(minThinkTime, maxThinkTime)
	
}
