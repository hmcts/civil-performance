package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{Environment, Headers}

/*======================================================================================
* Business process : CUI Draft Search - citizen resumes an existing draft claim
* Read-only (no saves / submit), so the same users and drafts can be reused every run.
*
* Store-agnostic: CUI decides where the draft lives, so the same script is used for
*   - Baseline (flag OFF -> Redis)
*   - CMC DB runs (flag ON -> civil-service -> Postgres)
*
* Pre-requisites (see CUIDraftSearchScenario in CivilDamagesSimulation):
*   1. feed a citizen user that already has a draft (CUIDraftClaimsRedis.csv / CUIDraftClaimsDB.csv)
*   2. CUIR2HomePage + CUIR2Login so the session cookie is set
*   3. CUIDraftSearch.DraftSearch, then CUIR2Logout
 ======================================================================================*/

object CUIDraftSearch {

	val minThinkTime = Environment.minThinkTime
	val maxThinkTime = Environment.maxThinkTime

	val cuiURL = Environment.citizenURL

	val DraftSearch =

		// clear Gatling's HTTP cache so pages already seen during login return 200 (not 304)
		exec(flushHttpCache)

		// Dashboard - CUI looks up the user's draft
		.group("CUIDraftSearch_010_Dashboard") {
			exec(http("CUIDraftSearch_010_005_Dashboard")
				.get(cuiURL + "/dashboard")
				.headers(Headers.navigationHeader)
				.check(status.is(200))
				.check(substring("To view or progress your claim click on your claim number")))
		}
		.pause(minThinkTime, maxThinkTime)

		// Draft claim dashboard - "Your claim is saved as a draft"
		.group("CUIDraftSearch_020_ContinueClaim_ClaimantNewDesign") {
			exec(http("CUIDraftSearch_020_005_ClaimantNewDesign")
				.get(cuiURL + "/dashboard/draft/claimantNewDesign")
				.headers(Headers.navigationHeader)
				.check(status.is(200))
				.check(substring("Your claim is saved as a draft")))
		}
		.pause(minThinkTime, maxThinkTime)

		// Task list - loads the full draft from the draft store
		.group("CUIDraftSearch_030_ContinueClaim_TaskList") {
			exec(http("CUIDraftSearch_030_005_TaskList")
				.get(cuiURL + "/claim/task-list")
				.headers(Headers.navigationHeader)
				.check(status.is(200))
				.check(substring("Application complete")))
		}
		.pause(minThinkTime, maxThinkTime)

		// Check your answers - reads the whole draft
		.group("CUIDraftSearch_040_CheckYourAnswers") {
			exec(http("CUIDraftSearch_040_005_CheckYourAnswers")
				.get(cuiURL + "/claim/check-and-send")
				.headers(Headers.navigationHeader)
				.check(status.is(200))
				.check(substring("Check your answers")))
		}
		.pause(minThinkTime, maxThinkTime)

}
