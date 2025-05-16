package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils._

object XUIDefendantResponse {

	val BaseURL = Environment.baseURL
	val IdamURL = Environment.idamURL
	val MinThinkTime = Environment.minThinkTime
	val MaxThinkTime = Environment.maxThinkTime
  /*======================================================================================
 * Create Civil Claim - Start Event 'Respond to Claim'
  ==========================================================================================*/

	val selectRespondToClaim =

		// ========================LANDING PAGE=====================,
		group("Civil_SpecClaim_30_010_RespondToClaim_Land") {
			exec(http("Land_005_Jurisdictions")
				.get(BaseURL + "/aggregated/caseworkers/:uid/jurisdictions?access=read")
				.headers(Headers.commonHeader)
				.check(substring("callback_get_case_url")))

			.exec(http("Land_010_Organisation")
				.get(BaseURL + "/api/organisation")
				.headers(Headers.commonHeader)
				.check(substring("organisationProfileIds")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ========================ASSIGN CASE TO RESPONDENT=====================
//		.group("Civil_SpecClaim_30_020_RespondToClaim_API_AssignCase") {
//			exec(http("CIVIL_AssignCase_000_AssignCase")
//				.post("http://civil-service-perftest.service.core-compute-perftest.internal/" +
//					"testing-support/assign-case/#{claimNumber}/RESPONDENTSOLICITORONE")
//				.header("Authorization", "Bearer #{auth_token}")
//				.header("Content-Type", "application/json")
//				.header("Accept", "*/*")
//				.check(status.in(200, 201)))
//		}

		// ========================SEARCH=====================,
		.group("Civil_SpecClaim_30_030_RespondToClaim_Search") {
			exec(http("Search_005_WorkBasket")
				.get(BaseURL + "/data/internal/case-types/CIVIL/work-basket-inputs")
				.headers(Headers.validateHeader)
				.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-workbasket-input-details.v2+json;charset=UTF-8")
				.check(substring("workbasketInputs")))

			.exec(http("Search_010_CaseReference")
				.post(BaseURL + "/data/internal/searchCases?ctid=CIVIL&use_case=WORKBASKET&view=WORKBASKET&page=1&case_reference=#{claimNumber}")
				.headers(Headers.commonHeader)
				.body(StringBody("""{"size": 25}""".stripMargin))
				.check(substring("AWAITING_RESPONDENT_ACKNOWLEDGEMENT")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ========================OPEN CASE========================,
		.group("Civil_SpecClaim_30_040_RespondToClaim_GetCase") {
			exec(http("OpenCase_005_InternalCases")
				.get(BaseURL + "/data/internal/cases/#{claimNumber}")
				.headers(Headers.validateHeader)
				.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
				.check(substring("Awaiting Defendant Response")))
		}

		.group("Civil_SpecClaim_30_040_RespondToClaim_API_RoleAssignment") {
			exec(http("OpenCase_010_RoleAssignment")
				.post(BaseURL + "/api/role-access/roles/manageLabellingRoleAssignment/#{claimNumber}")
				.headers(Headers.commonHeader)
				.check(status.is(204)))
		}

		.group("Civil_SpecClaim_30_040_RespondToClaim_API_SupportedJurisdiction") {
			exec(http("OpenCase_015_Jurisdiction")
				.get(BaseURL + "/api/wa-supported-jurisdiction/get")
				.headers(Headers.commonHeader)
				.check(substring("CIVIL")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// =========================Select respond to claim====================,
		.group("Civil_SpecClaim_30_050_RespondToClaim_StartEvent") {
			exec(http("RespondToClaim_005_WA")
				.get(BaseURL + "/workallocation/case/tasks/#{claimNumber}/event/DEFENDANT_RESPONSE_SPEC/caseType/CIVIL/jurisdiction/CIVIL")
				.headers(Headers.commonHeader)
				.check(substring("task_required_for_event")))

			.exec(http("RespondToClaim_010_Profile")
				.get(BaseURL + "/data/internal/profile")
				.headers(Headers.validateHeader)
				.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-user-profile.v2+json;charset=UTF-8")
				.check(substring("solicitor")))

			.exec(http("RespondToClaim_015_IgnoreWarning")
				.get(BaseURL + "/data/internal/cases/#{claimNumber}/event-triggers/DEFENDANT_RESPONSE_SPEC?ignore-warning=false")
				.headers(Headers.validateHeader)
				.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
				.header("content-type", "application/json")
				.check(substring("DEFENDANT_RESPONSE"))
				.check(jsonPath("$.event_token").saveAs("event_token"))
				.check(regex("partyID\":\"(.*?)\",").saveAs("PartyID")))
			.exitHereIf(session => !session.contains("PartyID"))

			.exec(http("RespondToClaim_020_WA")
				.get(BaseURL + "/workallocation/case/tasks/#{claimNumber}/event/DEFENDANT_RESPONSE/caseType/CIVIL/jurisdiction/CIVIL")
				.headers(Headers.commonHeader)
				.check(substring("task_required_for_event")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ================================CHECK TIMELINE=========================,
		.group("Civil_SpecClaim_30_060_RespondToClaim_RespondentChecklist") {
			exec(http("RespondToClaim_005_ViewTimeline")
				.post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECRespondentCheckList")
				.headers(Headers.validateHeader)
				.body(ElFileBody("bodies/XUIDefendantResponse/respondentTimelineView.dat"))
				.check(substring("respondent1ResponseDeadline")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ===========================IS DEFENDANT ADDRESS CORRECT====================,
		.group("Civil_SpecClaim_30_070_RespondToClaim_ConfirmNameAddress") {
			exec(http("RespondToClaim_005_DefAddress")
				.post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECResponseConfirmNameAddress")
				.headers(Headers.validateHeader)
				.body(ElFileBody("bodies/XUIDefendantResponse/respondentAddressConfirmation.dat"))
				.check(substring("http://gateway-ccd.perftest.platform.hmcts.net/case-types/CIVIL/validate?" +
					"pageId=DEFENDANT_RESPONSE_SPECResponseConfirmNameAddress")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ==========================LEGAL REP ADDRESS CORRECT==========================,
		.group("Civil_SpecClaim_30_080_RespondToClaim_ConfirmDetails") {
			exec(http("RespondToClaim_005_LegalRepAddress")
				.post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECResponseConfirmDetails")
				.headers(Headers.validateHeader)
				.body(ElFileBody("bodies/XUIDefendantResponse/respondentAddressConfirmation_LegalRep.dat"))
				.check(substring("specAoSRespondentCorrespondenceAddressRequired")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ========================RESPOND TO CLAIM==================================,
		.group("Civil_SpecClaim_30_090_RespondToClaim_ResponseType") {
			exec(http("RespondToClaim_005_Response")
				.post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECRespondentResponseTypeSpec")
				.headers(Headers.validateHeader)
				.body(ElFileBody("bodies/XUIDefendantResponse/respondentReject.dat"))
				.check(substring("FULL_DEFENCE")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ========================DISPUTE MONEY==================,
		.group("Civil_SpecClaim_30_100_RespondToClaim_Dispute") {
			exec(http("RespondToClaim_005_Dispute")
				.post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECdefenceRoute")
				.headers(Headers.validateHeader)
				.body(ElFileBody("bodies/XUIDefendantResponse/respondentDispute.dat"))
				.check(substring("DISPUTES_THE_CLAIM")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ========================DESC DISPUTING THE CLAIM===============,
		.group("Civil_SpecClaim_30_110_RespondToClaim_UploadDescription") {
			exec(http("RespondToClaim_005_DisputeDescription")
				.post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECUpload")
				.headers(Headers.validateHeader)
				.body(ElFileBody("bodies/XUIDefendantResponse/respondentDisputeDescription.dat"))
				.check(substring("detailsOfWhyDoesYouDisputeTheClaim")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ===========================CLAIM TIMELINE===========================,
		.group("Civil_SpecClaim_30_120_RespondToClaim_AddTimelineManual") {
			exec(http("RespondToClaim_005_ManualTimeline")
				.post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECHowToAddTimeline")
				.headers(Headers.validateHeader)
				.body(ElFileBody("bodies/XUIDefendantResponse/respondentTimelineManual.dat"))
				.check(substring("TIMELINE_MANUALLY")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ========================ADD TO TIMELINE==================,
		.group("Civil_SpecClaim_30_130_RespondToClaim_AddTimelineManual") {
			exec(http("RespondToClaim_005_AddTimeline")
				.post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECHowToAddTimelineManual")
				.headers(Headers.validateHeader)
				.body(ElFileBody("bodies/XUIDefendantResponse/respondentTimelineAddition.dat"))
				.check(substring("specResponseTimelineOfEvents")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ========================MEDIATION===================,
		.group("Civil_SpecClaim_30_140_RespondToClaim_MediationContact") {
			exec(http("RespondToClaim_005_Mediation")
				.post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECMediationContactInformation")
				.headers(Headers.validateHeader)
				.body(ElFileBody("bodies/XUIDefendantResponse/respondentMediationInfo.dat"))
				.check(substring("Mediation")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		.group("Civil_SpecClaim_30_150_RespondToClaim_MediationAvailability") {
			exec(http("RespondToClaim_010_Mediation")
				.post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECMediationAvailability")
				.headers(Headers.validateHeader)
				.body(ElFileBody("bodies/XUIDefendantResponse/respondentMediationInfo.dat"))
				.check(substring("unavailableDates")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ==============================NO EXPERTS======================,
		.group("Civil_SpecClaim_30_160_RespondToClaim_Experts") {
			exec(http("RespondToClaim_005_Experts")
				.post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECSmallClaimExperts")
				.headers(Headers.validateHeader)
				.body(ElFileBody("bodies/XUIDefendantResponse/respondentExperts_Mediation.dat"))
				.check(substring("responseClaimExpertSpecRequired")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ==============================ANY WITNESS NO================,
		.group("Civil_SpecClaim_30_170_RespondToClaim_Witnesses") {
			exec(http("RespondToClaim_005_Witnesses")
				.post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECSmallClaimWitnesses")
				.headers(Headers.validateHeader)
				.body(ElFileBody("bodies/XUIDefendantResponse/respondentWitnesses_Mediation.dat"))
				.check(substring("respondent1DQWitnessesSmallClaim")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ========================LANGUAGE=================,
		.group("Civil_SpecClaim_30_180_RespondToClaim_Language") {
			exec(http("RespondToClaim_005_Language")
				.post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECLanguage")
				.headers(Headers.validateHeader)
				.body(ElFileBody("bodies/XUIDefendantResponse/respondentLanguage_Mediation.dat"))
				.check(substring("respondent1DQLanguage")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ==================HEARING AVAILABILITY=============,
		.group("Civil_SpecClaim_30_190_RespondToClaim_Hearing") {
			exec(http("RespondToClaim_005_Hearing")
				.post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECSmaillClaimHearing")
				.headers(Headers.validateHeader)
				.body(ElFileBody("bodies/XUIDefendantResponse/respondentHearingAvailability_Mediation.dat"))
				.check(substring("unavailableDates")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ====================COURT LOCATION====================,
		.group("Civil_SpecClaim_30_200_RespondToClaim_CourtLocation") {
			exec(http("RespondToClaim_005_CourtLocation")
				.post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECRequestedCourtLocationLRspec")
				.headers(Headers.validateHeader)
				.body(ElFileBody("bodies/XUIDefendantResponse/respondentCourtLocation_Mediation.dat"))
				.check(substring("respondToCourtLocation")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// =====================ACCESS NEEDS================
		.group("Civil_SpecClaim_30_210_RespondToClaim_AccessNeeds") {
			exec(http("RespondToClaim_005_AccessNeeds")
				.post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECHearingSupport")
				.headers(Headers.validateHeader)
				.body(ElFileBody("bodies/XUIDefendantResponse/respondentHearingSupport_Mediation.dat"))
				.check(substring("respondent1DQHearingSupport")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ===============================VULNERABILITY QUESTIONS=================
		.group("Civil_SpecClaim_30_220_RespondToClaim_Vulnerability") {
			exec(http("RespondToClaim_005_Vulnerability")
				.post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECVulnerabilityQuestions")
				.headers(Headers.validateHeader)
				.body(ElFileBody("bodies/XUIDefendantResponse/respondentVulnerability_Mediation.dat"))
				.check(substring("respondent1DQVulnerabilityQuestions")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ===============================SOT============================
		.group("Civil_SpecClaim_30_230_RespondToClaim_StatementOfTruth") {
			exec(http("RespondToClaim_005_StatementOfTruth")
				.post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECStatementOfTruth")
				.headers(Headers.validateHeader)
				.body(ElFileBody("bodies/XUIDefendantResponse/respondentStatementOfTruth_Mediation.dat"))
				.check(substring("StatementOfTruth")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ==================================SOT SUBMIT==================,
		.group("Civil_SpecClaim_30_240_RespondToClaim_Submit") {
			exec(http("RespondToClaim_005_SubmitSOT")
				.post(BaseURL + "/data/cases/#{claimNumber}/events")
				.headers(Headers.validateHeader)
				.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
				.body(ElFileBody("bodies/XUIDefendantResponse/respondentSubmitDefence_Mediation.dat"))
				.check(substring("You have submitted your response")))

			.exec(http("RespondToClaim_010_SubmitSOT")
				.get(BaseURL + "/data/internal/cases/#{claimNumber}")
				.headers(Headers.validateHeader)
				.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
				.check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{claimNumber}")))
		}
		.pause(MinThinkTime, MaxThinkTime)

}