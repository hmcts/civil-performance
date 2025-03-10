package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils._

object XUIJudgmentByAdmission {

	val BaseURL = Environment.baseURL
	val IdamURL = Environment.idamURL
	val MinThinkTime = Environment.minThinkTime
	val MaxThinkTime = Environment.maxThinkTime

	val requestJudgmentByAdmission =

		// =============================REQUEST JUDGMENT BY ADMISSION=============================,
		group("XUI_JudgmentByAdmission_500_RequestJudgmentByAdmission") {
			exec(http("ReqJudgmentByAdmission_005_Jurisdiction")
				.get(BaseURL + "/workallocation/case/tasks/#{caseId}/event/REQUEST_JUDGEMENT_ADMISSION_SPEC/caseType/CIVIL/jurisdiction/CIVIL")
				.headers(Headers.commonHeader)
				.check(substring("task_required_for_event")))

			.exec(http("ReqJudgmentByAdmission_010_Profile")
				.get(BaseURL + "/data/internal/profile")
				.headers(Headers.validateHeader)
				.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-user-profile.v2+json;charset=UTF-8")
				.check(substring("solicitor")))

			.exec(http("ReqJudgmentByAdmission_015_IgnoreWarning")
				.get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/REQUEST_JUDGEMENT_ADMISSION_SPEC?ignore-warning=false")
				.headers(Headers.validateHeader)
				.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
				.check(substring("REQUEST_JUDGEMENT_ADMISSION_SPEC"))
				.check(jsonPath("$.event_token").saveAs("event_token_admission")))
			.exitHereIf(session => !session.contains("event_token_admission"))

			.exec(http("ReqJudgmentByAdmission_020_Jurisdiction")
				.get(BaseURL + "/workallocation/case/tasks/#{caseId}/event/REQUEST_JUDGEMENT_ADMISSION_SPEC/caseType/CIVIL/jurisdiction/CIVIL")
				.headers(Headers.commonHeader)
				.check(substring("task_required_for_event")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// =============================HAS DEFENDANT PAID=============================,
		.group("XUI_JudgmentByAdmission_510_HasDefendantPaid") {
			exec(http("HasDefendantPaid")
				.post(BaseURL + "/data/case-types/CIVIL/validate?pageId=REQUEST_JUDGEMENT_ADMISSION_SPECCcjPaymentPaidSome")
				.headers(Headers.validateHeader)
				.body(ElFileBody("bodies/XUIJudgmentByAdmission/hasDefendantPaid.json"))
				.check(substring("ccjPaymentPaidSomeAmount")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// =============================FIXED COSTS=============================,
		.group("XUI_JudgmentByAdmission_520_FixedCosts") {
			exec(http("FixedCosts")
				.post(BaseURL + "/data/case-types/CIVIL/validate?pageId=REQUEST_JUDGEMENT_ADMISSION_SPECFixedCost")
				.headers(Headers.validateHeader)
				.body(ElFileBody("bodies/XUIJudgmentByAdmission/fixedCosts.json"))
				.check(substring("ccjJudgmentFixedCostOption")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// =============================CONTINUE=============================,
		.group("XUI_JudgmentByAdmission_530_Continue") {
			exec(http("Continue")
				.post(BaseURL + "/data/case-types/CIVIL/validate?pageId=REQUEST_JUDGEMENT_ADMISSION_SPECWorkAllocationIntegrationFields")
				.headers(Headers.validateHeader)
				.body(ElFileBody("bodies/XUIJudgmentByAdmission/continue.json"))
				.check(substring("applicant1LiPResponse")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// =============================AMOUNT SUMMARY=============================,
		.group("XUI_JudgmentByAdmission_540_AmountSummary") {
			exec(http("AmountSummary")
				.post(BaseURL + "/data/case-types/CIVIL/validate?pageId=REQUEST_JUDGEMENT_ADMISSION_SPECCcjJudgmentSummary")
				.headers(Headers.validateHeader)
				.body(ElFileBody("bodies/XUIJudgmentByAdmission/amountSummary.json"))
				.check(status.is(200)))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// =============================SUBMIT=============================,
		.group("XUI_JudgmentByAdmission_550_Submit") {
			exec(http("Submit")
				.post(BaseURL + "/data/cases/#{caseId}/events")
				.headers(Headers.validateHeader)
				.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
				.body(ElFileBody("bodies/XUIJudgmentByAdmission/submit.json"))
				.check(substring("The judgment request will be processed and a County Court Judgment (CCJ) will be issued")))

			.exec(http("ViewCase")
				.get(BaseURL + "/data/internal/cases/#{caseId}")
				.headers(Headers.validateHeader)
				.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
				.check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{caseId}")))
		}
		.pause(MinThinkTime, MaxThinkTime)
}