package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.spec_DF1_Headers._
import utils._
import scala.concurrent.duration.DurationInt

object spec_DF1_Resp {

	val BaseURL = Environment.baseURL
	val IdamURL = Environment.idamURL
	val MinThinkTime = Environment.minThinkTime
	val MaxThinkTime = Environment.maxThinkTime
  /*======================================================================================
 * Create Civil Claim - Start Event 'Respond to Claim'
  ==========================================================================================*/

	val selectRespondToClaim =

		// ========================LANDING PAGE=====================,
		group("Civil_SpecClaim_30_01_RespondToClaim") {
			exec(http("Land_005_Jurisdictions")
				.get("/aggregated/caseworkers/:uid/jurisdictions?access=read")
				.headers(Headers.commonHeader)
				.check(substring("callback_get_case_url")))

			.exec(http("Land_010_Organisation")
				.get("/api/organisation")
				.headers(Headers.commonHeader)
				.check(substring("organisationProfileIds")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ========================ASSIGN CASE TO RESPONDENT=====================
		.group("Civil_SpecClaim_30_02_RespondToClaim") {
			exec(http("CIVIL_AssignCase_000_AssignCase")
				.post("http://civil-service-perftest.service.core-compute-perftest.internal/" +
					"testing-support/assign-case/#{caseId}/RESPONDENTSOLICITORONE")
				.header("Authorization", "Bearer #{auth_token}")
				.header("Content-Type", "application/json")
				.header("Accept", "*/*")
				.check(status.in(200, 201)))
		}

		// ========================SEARCH=====================,
		.group("Civil_SpecClaim_30_03_RespondToClaim") {
			exec(http("Search_005_WorkBasket")
				.get("/data/internal/case-types/CIVIL/work-basket-inputs")
				.headers(Headers.validateHeader)
				.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-workbasket-input-details.v2+json;charset=UTF-8")
				.check(substring("workbasketInputs")))

			.exec(http("Search_010_CaseReference")
				.post("/data/internal/searchCases?ctid=CIVIL&use_case=WORKBASKET&view=WORKBASKET&page=1&case_reference=#{caseId}")
				.headers(Headers.commonHeader)
				.body(StringBody("""{"size": 25}""".stripMargin))
				.check(substring("AWAITING_RESPONDENT_ACKNOWLEDGEMENT")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ========================OPEN CASE========================,
		.group("Civil_SpecClaim_30_04_RespondToClaim") {
			exec(http("OpenCase_005_InternalCases")
				.get("/data/internal/cases/#{caseId}")
				.headers(Headers.validateHeader)
				.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
				.check(substring("Awaiting Defendant Response")))
		}

		.group("Civil_SpecClaim_30_04_RespondToClaim") {
			exec(http("OpenCase_010_RoleAssignment")
				.post("/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
				.headers(Headers.commonHeader)
				.check(status.is(204)))
		}

		.group("Civil_SpecClaim_30_04_RespondToClaim") {
			exec(http("OpenCase_015_Jurisdiction")
				.get("/api/wa-supported-jurisdiction/get")
				.headers(Headers.commonHeader)
				.check(substring("CIVIL")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// =========================Select respond to claim====================,
		.group("Civil_SpecClaim_30_05_RespondToClaim") {
			exec(http("RespondToClaim_005_WA")
				.get("/workallocation/case/tasks/#{caseId}/event/DEFENDANT_RESPONSE_SPEC/caseType/CIVIL/jurisdiction/CIVIL")
				.headers(Headers.commonHeader)
				.check(substring("task_required_for_event")))

			.exec(http("RespondToClaim_010_Profile")
				.get("/data/internal/profile")
				.headers(Headers.validateHeader)
				.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-user-profile.v2+json;charset=UTF-8")
				.check(substring("solicitor")))

			.exec(http("RespondToClaim_015_IgnoreWarning")
				.get("/data/internal/cases/#{caseId}/event-triggers/DEFENDANT_RESPONSE_SPEC?ignore-warning=false")
				.headers(Headers.validateHeader)
				.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
				.header("content-type", "application/json")
				.check(substring("DEFENDANT_RESPONSE"))
				.check(jsonPath("$.event_token").saveAs("event_token"))
				.check(regex("partyID\":\"(.*?)\",").saveAs("PartyID")))
			.exitHereIf(session => !session.contains("PartyID"))

			.exec(http("RespondToClaim_020_WA")
				.get("/workallocation/case/tasks/#{caseId}/event/DEFENDANT_RESPONSE/caseType/CIVIL/jurisdiction/CIVIL")
				.headers(Headers.commonHeader)
				.check(substring("task_required_for_event")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ================================CHECK TIMELINE=========================,
		.group("Civil_SpecClaim_30_06_RespondToClaim") {
			exec(http("RespondToClaim_005_ViewTimeline")
				.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECRespondentCheckList")
				.headers(Headers.validateHeader)
				.body(ElFileBody("b_DefResp_bodies/respondentTimelineView.dat"))
				.check(substring("respondent1ResponseDeadline")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ===========================IS DEFENDANT ADDRESS CORRECT====================,
		.group("Civil_SpecClaim_30_07_RespondToClaim") {
			exec(http("RespondToClaim_005_DefAddress")
				.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECResponseConfirmNameAddress")
				.headers(Headers.validateHeader)
				.body(ElFileBody("b_DefResp_bodies/respondentAddressConfirmation.dat"))
				.check(substring("http://gateway-ccd.perftest.platform.hmcts.net/case-types/CIVIL/validate?" +
					"pageId=DEFENDANT_RESPONSE_SPECResponseConfirmNameAddress")))

			.exec(http("RespondToClaim_010_DefAddress")
				.get("/api/caseshare/orgs")
				.headers(Headers.commonHeader)
				.check(substring("organisationIdentifier")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ==========================LEGAL REP ADDRESS CORRECT==========================,
		.group("Civil_SpecClaim_30_08_RespondToClaim") {
			exec(http("RespondToClaim_005_LegalRepAddress")
				.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECResponseConfirmDetails")
				.headers(Headers.validateHeader)
				.body(ElFileBody("b_DefResp_bodies/respondentAddressConfirmation_LegalRep.dat"))
				.check(substring("specAoSRespondentCorrespondenceAddressRequired")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ========================RESPOND TO CLAIM==================================,
		.group("Civil_SpecClaim_30_09_RespondToClaim") {
			exec(http("RespondToClaim_005_Response")
				.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECRespondentResponseTypeSpec")
				.headers(Headers.validateHeader)
				.body(ElFileBody("b_DefResp_bodies/respondentReject.dat"))
				.check(substring("FULL_DEFENCE")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ========================DISPUTE MONEY==================,
		.group("Civil_SpecClaim_30_10_RespondToClaim") {
			exec(http("RespondToClaim_005_Dispute")
				.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECdefenceRoute")
				.headers(Headers.validateHeader)
				.body(ElFileBody("b_DefResp_bodies/respondentDispute.dat"))
				.check(substring("DISPUTES_THE_CLAIM")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ========================DESC DISPUTING THE CLAIM===============,
		.group("Civil_SpecClaim_30_11_RespondToClaim") {
			exec(http("RespondToClaim_005_DisputeDescription")
				.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECUpload")
				.headers(Headers.validateHeader)
				.body(ElFileBody("b_DefResp_bodies/respondentDisputeDescription.dat"))
				.check(substring("detailsOfWhyDoesYouDisputeTheClaim")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ===========================CLAIM TIMELINE===========================,
		.group("Civil_SpecClaim_30_12_RespondToClaim") {
			exec(http("RespondToClaim_005_ManualTimeline")
				.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECHowToAddTimeline")
				.headers(Headers.validateHeader)
				.body(ElFileBody("b_DefResp_bodies/respondentTimelineManual.dat"))
				.check(substring("TIMELINE_MANUALLY")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ========================ADD TO TIMELINE==================,
		.group("Civil_SpecClaim_30_13_RespondToClaim") {
			exec(http("RespondToClaim_005_AddTimeline")
				.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECHowToAddTimelineManual")
				.headers(Headers.validateHeader)
				.body(ElFileBody("b_DefResp_bodies/respondentTimelineAddition.dat"))
				.check(substring("specResponseTimelineOfEvents")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ========================MEDIATION===================,
		.group("Civil_SpecClaim_30_14_RespondToClaim") {
			exec(http("RespondToClaim_005_Mediation")
				.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECMediationContactInformation")
				.headers(Headers.validateHeader)
				//.body(ElFileBody("b_DefResp_bodies/respondentMediationOverride.dat"))
				.body(ElFileBody("b_DefResp_bodies/respondentMediationInfo.dat"))
				.check(substring("Mediation")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		//The commented POST bodies below were an attempt to circumvent the mediation step which didn't work at the time
		//this comment was written. They were kept (along with the corresponding files) in case they work in the future.

		.group("Civil_SpecClaim_30_15_RespondToClaim") {
			exec(http("RespondToClaim_010_Mediation")
				.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECMediationAvailability")
				.headers(Headers.validateHeader)
				.body(ElFileBody("b_DefResp_bodies/respondentMediationInfo.dat"))
				.check(substring("unavailableDates")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ==============================NO EXPERTS======================,
		.group("Civil_SpecClaim_30_16_RespondToClaim") {
			exec(http("RespondToClaim_005_Experts")
				.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECSmallClaimExperts")
				.headers(Headers.validateHeader)
				//.body(ElFileBody("b_DefResp_bodies/respondentExperts.dat"))
				.body(ElFileBody("b_DefResp_bodies/respondentExperts_Mediation.dat"))
				.check(substring("responseClaimExpertSpecRequired")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ==============================ANY WITNESS NO================,
		.group("Civil_SpecClaim_30_17_RespondToClaim") {
			exec(http("RespondToClaim_005_Witnesses")
				.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECSmallClaimWitnesses")
				.headers(Headers.validateHeader)
				//.body(ElFileBody("b_DefResp_bodies/respondentWitnesses.dat"))
				.body(ElFileBody("b_DefResp_bodies/respondentWitnesses_Mediation.dat"))
				.check(substring("respondent1DQWitnessesSmallClaim")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ========================LANGUAGE=================,
		.group("Civil_SpecClaim_30_18_RespondToClaim") {
			exec(http("RespondToClaim_005_Language")
				.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECLanguage")
				.headers(Headers.validateHeader)
				//.body(ElFileBody("b_DefResp_bodies/respondentLanguage.dat"))
				.body(ElFileBody("b_DefResp_bodies/respondentLanguage_Mediation.dat"))
				.check(substring("respondent1DQLanguage")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ==================HEARING AVAILABILITY=============,
		.group("Civil_SpecClaim_30_19_RespondToClaim") {
			exec(http("RespondToClaim_005_Hearing")
				.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECSmaillClaimHearing")
				.headers(Headers.validateHeader)
				//.body(ElFileBody("b_DefResp_bodies/respondentHearingAvailability.dat"))
				.body(ElFileBody("b_DefResp_bodies/respondentHearingAvailability_Mediation.dat"))
				.check(substring("unavailableDates")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ====================COURT LOCATION====================,
		.group("Civil_SpecClaim_30_20_RespondToClaim") {
			exec(http("RespondToClaim_005_CourtLocation")
				.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECRequestedCourtLocationLRspec")
				.headers(Headers.validateHeader)
				//.body(ElFileBody("b_DefResp_bodies/respondentCourtLocation.dat"))
				.body(ElFileBody("b_DefResp_bodies/respondentCourtLocation_Mediation.dat"))
				.check(substring("respondToCourtLocation")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// =====================ACCESS NEEDS================
		.group("Civil_SpecClaim_30_21_RespondToClaim") {
			exec(http("RespondToClaim_005_AccessNeeds")
				.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECHearingSupport")
				.headers(Headers.validateHeader)
				.body(ElFileBody("b_DefResp_bodies/respondentHearingSupport.dat"))
				//.body(ElFileBody("b_DefResp_bodies/respondentHearingSupport_Mediation.dat"))
				.check(substring("respondent1DQHearingSupport")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ===============================VULNERABILITY QUESTIONS=================
		.group("Civil_SpecClaim_30_22_RespondToClaim") {
			exec(http("RespondToClaim_005_Vulnerability")
				.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECVulnerabilityQuestions")
				.headers(Headers.validateHeader)
				//.body(ElFileBody("b_DefResp_bodies/respondentVulnerability.dat"))
				.body(ElFileBody("b_DefResp_bodies/respondentVulnerability_Mediation.dat"))
				.check(substring("respondent1DQVulnerabilityQuestions")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ===============================SOT============================
		.group("Civil_SpecClaim_30_23_RespondToClaim") {
			exec(http("RespondToClaim_005_StatementOfTruth")
				.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECStatementOfTruth")
				.headers(Headers.validateHeader)
				//.body(ElFileBody("b_DefResp_bodies/respondentStatementOfTruth.dat"))
				.body(ElFileBody("b_DefResp_bodies/respondentStatementOfTruth_Mediation.dat"))
				.check(substring("StatementOfTruth")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		.group("Civil_SpecClaim_30_24_RespondToClaim") {
			exec(http("RespondToClaim_010_StatementOfTruth")
				.get("/api/caseshare/orgs")
				.headers(Headers.commonHeader)
				.check(substring("organisationIdentifier")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// ==================================SOT SUBMIT==================,
		.group("Civil_SpecClaim_30_25_RespondToClaim") {
			exec(http("RespondToClaim_005_SubmitSOT")
				.post("/data/cases/#{caseId}/events")
				.headers(Headers.validateHeader)
				.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
				//.body(ElFileBody("b_DefResp_bodies/respondentSubmitDefence.dat"))
				.body(ElFileBody("b_DefResp_bodies/respondentSubmitDefence_Mediation.dat"))
				.check(substring("You have submitted your response")))

			.exec(http("RespondToClaim_010_SubmitSOT")
				.get("/data/internal/cases/#{caseId}")
				.headers(Headers.validateHeader)
				.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
				.check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{caseId}")))
		}
		.pause(MinThinkTime, MaxThinkTime)

}


//Part of another alternative route, this is right after the ADD TO TIMELINE section and it is only partial.
//		// ========================DIRECTIONS QUESTIONNAIRE==================,
//		.group("Civil_Claim_20_RespondToClaim") {
//			exec(http("RespondToClaim_005_Directions")
//				.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECFileDirectionsQuestionnaire")
//				.headers(Headers.validateHeader)
//				.body(ElFileBody("b_DefResp_bodies/respondentDirectionsQuestionnaire.dat"))
//				.check(substring("respondent1DQFileDirectionsQuestionnaire")))
//		}
//		.pause(MinThinkTime, MaxThinkTime)
//
//		// ========================FIXED RECOVER COST========================,
//		.group("Civil_Claim_20_RespondToClaim") {
//			exec(http("RespondToClaim_005_FixedRecoverableCosts")
//				.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECFixedRecoverableCosts")
//				.headers(Headers.validateHeader)
//				.body(ElFileBody("ub_unspec_DF_resp_bodies/respondentFixedRecoverable.dat"))
//				.check(substring("respondent1DQFixedRecoverableCosts")))
//		}
//		.pause(MinThinkTime, MaxThinkTime)
//
//		// ========================AGREEMENT ELECTRONIC DOCS========================,
//		.group("Civil_Claim_20_RespondToClaim") {
//			exec(http("RespondToClaim_005_AgreementElectronicDocs")
//				.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECDisclosureOfElectronicDocumentsLRspec")
//				.headers(Headers.validateHeader)
//				.body(ElFileBody("b_DefResp_bodies/respondentElectronicDocs.dat"))
//				.check(substring("specRespondent1DQDisclosureOfElectronicDocuments")))
//		}
//		.pause(MinThinkTime, MaxThinkTime)