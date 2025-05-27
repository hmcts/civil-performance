package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils._

object XUINoticeOfChange {

	val BaseURL = Environment.baseURL
	val IdamURL = Environment.idamURL
	val MinThinkTime = Environment.minThinkTime
	val MaxThinkTime = Environment.maxThinkTime

	// ==============================NOTICE OF CHANGE====================
	val noticeOfChange =

		group("XUI_NoticeOfChange_010_Start") {
			exec(http("Start")
				.get(BaseURL + "/206.7229c22bd59dc9a1.js")
				.headers(Headers.commonHeader)
				.header("sec-fetch-dest", "script")
				.check(substring("You can use this notice of change (sometimes called a 'notice of acting')")))

			.exec(Common.isAuthenticatedXUI)
		}
		.pause(MinThinkTime, MaxThinkTime)

		.group("XUI_NoticeOfChange_020_Case") {
			exec(http("Case")
				.get(BaseURL + "/api/noc/nocQuestions?caseId=#{claimNumber}")
				.headers(Headers.commonHeader)
				.check(substring("Your Client's name as it appears as written on the case.")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		.group("XUI_NoticeOfChange_030_DefendantName") {
			exec(http("DefendantName")
				.post(BaseURL + "/api/noc/validateNoCQuestions")
				.headers(Headers.commonHeader)
				.body(StringBody("""{"case_id": "#{claimNumber}", "answers": [{"question_id":
						|"clientName", "value": "Mr Def First Def Last"}]}""".stripMargin))
				.check(substring("Notice of Change answers verified successfully")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		.group("XUI_NoticeOfChange_040_Submit") {
			exec(http("Submit")
				.post(BaseURL + "/api/noc/submitNoCEvents")
				.headers(Headers.commonHeader)
				.body(StringBody("""{"case_id": "#{claimNumber}", "answers": [{"question_id":
					|"clientName", "value": "Mr Def First Def Last"}]}""".stripMargin))
				.check(bodyString.saveAs("nocResponse"))
				.check(substring("The Notice of Change request has been successfully submitted.")))

			.exitHereIf(session =>
				!session("nocResponse").as[String].contains("The Notice of Change request has been successfully submitted."))

			.exec(Common.isAuthenticatedXUI)
		}
		.pause(MinThinkTime, MaxThinkTime)
}