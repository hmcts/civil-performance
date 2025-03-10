package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils._

object XUIRespondToDefence {

	val BaseURL = Environment.baseURL
	val IdamURL = Environment.idamURL
	val MinThinkTime = Environment.minThinkTime
	val MaxThinkTime = Environment.maxThinkTime

	// ==============================SELECT RESPOND TO DEFENDANT====================
	val viewAndRespond =

		// ========================LANDING PAGE=====================,
		group("XUI_ClaimantResponse_400_LandingPage") {
			exec(http("Jurisdictions")
				.get(BaseURL + "/aggregated/caseworkers/:uid/jurisdictions?access=read")
				.headers(Headers.commonHeader)
				.check(substring("callback_get_case_url")))

				.exec(http("Organisation")
					.get(BaseURL + "/api/organisation")
					.headers(Headers.commonHeader)
					.check(substring("organisationProfileIds")))
		}
			.pause(MinThinkTime, MaxThinkTime)

			// ========================SEARCH=====================,
			.group("XUI_ClaimantResponse_410_Search") {
				exec(http("WorkBasket")
					.get(BaseURL + "/data/internal/case-types/CIVIL/work-basket-inputs")
					.headers(Headers.validateHeader)
					.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-workbasket-input-details.v2+json;charset=UTF-8")
					.check(substring("workbasketInputs")))

					.exec(http("CaseReference")
						.post(BaseURL + "/data/internal/searchCases?ctid=CIVIL&use_case=WORKBASKET&view=WORKBASKET&page=1&case_reference=#{caseId}")
						.headers(Headers.commonHeader)
						.body(StringBody("""{"size": 25}""".stripMargin))
						.check(substring("AWAITING_APPLICANT_INTENTION")))
			}
			.pause(MinThinkTime, MaxThinkTime)

			// ================================OPEN CASE===============================,
			.group("XUI_ClaimantResponse_420_OpenCase") {
				exec(http("InternalCases")
					.get(BaseURL + "/data/internal/cases/#{caseId}")
					.headers(Headers.validateHeader)
					.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
					.check(substring("Awaiting Defendant Response")))
			}

			.group("XUI_ClaimantResponse_430_RoleAssignment") {
				exec(http("RoleAssignment")
					.post(BaseURL + "/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
					.headers(Headers.commonHeader)
					.check(status.is(204)))
			}

			.group("XUI_ClaimantResponse_440_Jurisdiction") {
				exec(http("Jurisdiction")
					.get(BaseURL + "/api/wa-supported-jurisdiction/get")
					.headers(Headers.commonHeader)
					.check(substring("CIVIL")))
			}
			.pause(MinThinkTime, MaxThinkTime)

			// =============================VIEW & RESPOND=============================,
			.group("XUI_ClaimantResponse_450_ViewAndRespond") {
				exec(http("RespToDef_005_Jurisdiction")
					.get(BaseURL + "/workallocation/case/tasks/#{caseId}/event/CLAIMANT_RESPONSE_SPEC/caseType/CIVIL/jurisdiction/CIVIL")
					.headers(Headers.commonHeader)
					.check(substring("task_required_for_event")))

					.exec(http("RespToDef_010_Profile")
						.get(BaseURL + "/data/internal/profile")
						.headers(Headers.validateHeader)
						.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-user-profile.v2+json;charset=UTF-8")
						.check(substring("solicitor")))

					.exec(http("RespToDef_015_IgnoreWarning")
						.get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/CLAIMANT_RESPONSE_SPEC?ignore-warning=false")
						.headers(Headers.validateHeader)
						.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
						.check(substring("CLAIMANT_RESPONSE"))
						.check(regex("partyID\":\"(.*?)\"").saveAs("repPartyID"))
						.check(regex("partyName\":\"(.*?)\"").saveAs("partyName"))
						.check(jsonPath("$..formatted_value.documentLink.document_url").saveAs("document_url"))
						.check(jsonPath("$..formatted_value.documentLink.document_filename").saveAs("document_filename"))
						.check(jsonPath("$..formatted_value.documentSize").saveAs("document_size"))
						.check(jsonPath("$..formatted_value.createdDatetime").saveAs("createdDateTime"))
						.check(jsonPath("$.event_token").saveAs("event_token")))
					.exitHereIf(session => !session.contains("repPartyID"))

					.exec(http("RespToDef_020_Jurisdiction")
						.get(BaseURL + "/workallocation/case/tasks/#{caseId}/event/CLAIMANT_RESPONSE_SPEC/caseType/CIVIL/jurisdiction/CIVIL")
						.headers(Headers.commonHeader)
						.check(substring("task_required_for_event")))
			}
			.pause(MinThinkTime, MaxThinkTime)

			// =============================AGREE TO SETTLE=============================,
			.group("XUI_ClaimantResponse_460_AgreeToSettle") {
				exec(http("AgreeToSettle")
					.post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECRespondentResponse")
					.headers(Headers.validateHeader)
					.body(ElFileBody("bodies/XUIRespondToDefence/agreeToSettle.json"))
					.check(substring("applicant1AcceptAdmitAmountPaidSpec")))
			}
			.pause(MinThinkTime, MaxThinkTime)

			// =============================SUBMIT=============================,
			.group("XUI_ClaimantResponse_470_Submit") {
				exec(http("Submit")
					.post(BaseURL + "/data/cases/#{caseId}/events")
					.headers(Headers.validateHeader)
					.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
					.body(ElFileBody("bodies/XUIRespondToDefence/submit.json"))
					.check(substring("The defendant said")))

				.exec(http("ViewCase")
					.get(BaseURL + "/data/internal/cases/#{caseId}")
					.headers(Headers.validateHeader)
					.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
					.check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{caseId}")))
			}
			.pause(MinThinkTime, MaxThinkTime)

			// =============================RETURN TO CASE=============================,
			.group("XUI_ClaimantResponse_480_ReturnToCase") {
				exec(http("RoleAssignment")
					.post("/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
					.headers(Headers.commonHeader)
					.check(status.in(200, 204)))

				.exec(http("Jurisdiction")
					.get(BaseURL + "/api/wa-supported-jurisdiction/get")
					.headers(Headers.commonHeader)
					.check(substring("CIVIL")))
			}
			.pause(15)
}