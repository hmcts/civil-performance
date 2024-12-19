package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.unspec_CL1_Resp_Headers._
import utils._

import scala.concurrent.duration.DurationInt

object unspec_CL1_Resp {
	val BaseURL = Environment.baseURL
	val IdamURL = Environment.idamURL
	val MinThinkTime = Environment.minThinkTime
	val MaxThinkTime = Environment.maxThinkTime



	val RespToDF =
		// ========================LANDING PAGE=====================,
		group("Civil_UnSpecClaim_30_ClaimantResp") {
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

		// ========================SEARCH=====================,
		.group("Civil_UnSpecClaim_30_ClaimantResp") {
			exec(http("Search_005_WorkBasket")
				.get("/data/internal/case-types/CIVIL/work-basket-inputs")
				.headers(Headers.validateHeader)
				.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-workbasket-input-details.v2+json;charset=UTF-8")
				.check(substring("workbasketInputs")))

			.exec(http("Search_010_CaseReference")
				.post("/data/internal/searchCases?ctid=CIVIL&use_case=WORKBASKET&view=WORKBASKET&page=1&case_reference=#{caseId}")
				.headers(Headers.commonHeader)
				.body(StringBody("""{"size": 25}""".stripMargin))
				.check(substring("AWAITING_APPLICANT_INTENTION")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// =======================OPEN CASE=======================,
		.group("Civil_UnSpecClaim_30_ClaimantResp") {
//			exec(http("SearchCase_005_CaseReference")
//				.post("/data/internal/searchCases?ctid=CIVIL&use_case=WORKBASKET&view=WORKBASKET&page=1&case_reference=#{caseId}")
//				.headers(Headers.commonHeader)
//				.header("accept", "application/json")
//				.body(StringBody("""{"size": 25}""".stripMargin))
//				.check(substring("AWAITING_RESPONDENT_ACKNOWLEDGEMENT")))

			exec(http("OpenCase_005_InternalCases")
				.get("/data/internal/cases/#{caseId}")
				.headers(Headers.validateHeader)
				.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
				.check(substring("Awaiting Defendant Response")))
		}

		.group("Civil_UnSpecClaim_30_ClaimantResp") {
			exec(http("OpenCase_010_RoleAssignment")
				.post("/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
				.headers(Headers.commonHeader)
				.check(status.is(204)))
		}

		.group("Civil_UnSpecClaim_30_ClaimantResp") {
			exec(http("OpenCase_015_Jurisdiction")
				.get("/api/wa-supported-jurisdiction/get")
				.headers(Headers.commonHeader)
				.check(substring("CIVIL")))
		}

		.pause(MinThinkTime, MaxThinkTime)
		// =======================RESPOND TO DEF=======================,
		.group("Civil_UnSpecClaim_30_ClaimantResp") {
			exec(http("RespToDef_005_Jurisdiction")
				.get("/workallocation/case/tasks/#{caseId}/event/CLAIMANT_RESPONSE/caseType/CIVIL/jurisdiction/CIVIL")
				.headers(Headers.commonHeader)
				.check(substring("task_required_for_event")))

			.exec(http("RespToDef_010_profile")
				.get("/data/internal/profile")
				.headers(Headers.validateHeader)
				.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-user-profile.v2+json;charset=UTF-8")
				.check(substring("#{LoginId}")))

			.exec(http("RespToDef_015_IgnoreWarning")
				.get("/data/internal/cases/#{caseId}/event-triggers/CLAIMANT_RESPONSE?ignore-warning=false")
				.headers(Headers.validateHeader)
				.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
				.check(substring("CLAIMANT_RESPONSE"))
				.check(jsonPath("$.event_token").saveAs("event_token"))
				.check(regex("document_url\":\"(.*?)\"").saveAs("DF_Document_url"))
				.check(regex("upload_timestamp\":\"(.*?)\"").saveAs("upload_timestamp"))
				.check(regex("partyID\":\"(.*?)\"").saveAs("partyID")))
			//.exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("xsrf_token")))

			.exec(http("RespToDef_020_jurisdiction")
				.get("/workallocation/case/tasks/#{caseId}/event/CLAIMANT_RESPONSE/caseType/CIVIL/jurisdiction/CIVIL")
				.headers(Headers.commonHeader)
				.check(substring("task_required_for_event")))
		}
		.pause(MinThinkTime, MaxThinkTime)

		// =======================DOC URL=DO YOU WANT TO PROCEED WITH CLAIM=======================,
		.group("Civil_UnSpecClaim_30_ClaimantResp") {
			exec(http("005_RespondentResponse")
				.post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSERespondentResponse")
				.headers(Headers.validateHeader)
				.body(ElFileBody("uc_unspec_CL_resp_bodies/claimantResponse.dat"))
				.check(substring("applicantsProceedIntention")))
		}
		.pause(MinThinkTime, MaxThinkTime)
		// =======================CONTINUE========================,
		.group("Civil_UnSpecClaim_30_ClaimantResp") {
			exec(http("005_DefenceResponseDocument")
				.post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEApplicantDefenceResponseDocument")
				.headers(Headers.validateHeader)
				.body(ElFileBody("uc_unspec_CL_resp_bodies/claimantDocument.dat"))
				.check(substring("applicant1DefenceResponseDocument")))
		}
		.pause(MinThinkTime, MaxThinkTime)
		// =======================FILE DIRECTION QUESTIONAIE=======================,
		.group("Civil_UnSpecClaim_30_ClaimantResp") {
			exec(http("005_FileDirectionsQuestionnaire")
				.post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEFileDirectionsQuestionnaire")
				.headers(Headers.validateHeader)
				.body(ElFileBody("uc_unspec_CL_resp_bodies/claimantQuestionnaire.dat"))
				.check(substring("applicant1DQFileDirectionsQuestionnaire")))
		}
		.pause(MinThinkTime, MaxThinkTime)
		// =======================FIXED RECOVERABLE COST=======================,
		.group("Civil_UnSpecClaim_30_ClaimantResp") {
			exec(http("005_FixedRecoverableCosts")
				.post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEFixedRecoverableCosts")
				.headers(Headers.validateHeader)
				.body(ElFileBody("uc_unspec_CL_resp_bodies/claimantFixedRecoverable.dat"))
				.check(substring("applicant1DQFixedRecoverableCosts")))
		}
		.pause(MinThinkTime, MaxThinkTime)
		// =======================ELECTRONIC DISCLOSURE=======================,
		.group("Civil_UnSpecClaim_30_ClaimantResp") {
			exec(http("005_DisclosureOfNonElectronicDocuments")
				.post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEDisclosureOfNonElectronicDocuments")
				.headers(Headers.validateHeader)
				.body(ElFileBody("uc_unspec_CL_resp_bodies/claimantNonElectronic.dat"))
				.check(substring("applicant1DQDisclosureOfNonElectronicDocuments")))
		}
		.pause(MinThinkTime, MaxThinkTime)
		// =======================EXPERT=======================,
		.group("Civil_UnSpecClaim_30_ClaimantResp") {
			exec(http("005_Experts")
				.post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEExperts")
				.headers(Headers.validateHeader)
				.body(ElFileBody("uc_unspec_CL_resp_bodies/claimantExperts.dat"))
				.check(substring("applicant1DQExperts")))
		}
		.pause(MinThinkTime, MaxThinkTime)
		// =======================WITNESS=======================,
		.group("Civil_UnSpecClaim_30_ClaimantResp") {
			exec(http("005_Witnesses")
				.post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEWitnesses")
				.headers(Headers.validateHeader)
				.body(ElFileBody("uc_unspec_CL_resp_bodies/claimantWitnesses.dat"))
				.check(substring("applicant1DQWitnesses")))
		}
		.pause(MinThinkTime, MaxThinkTime)
		// =======================LANGUAGE=======================,
		.group("Civil_UnSpecClaim_30_ClaimantResp") {
			exec(http("005_Language")
				.post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSELanguage")
				.headers(Headers.validateHeader)
				.body(ElFileBody("uc_unspec_CL_resp_bodies/claimantLanguage.dat"))
				.check(substring("applicant1DQLanguage")))
		}
		.pause(MinThinkTime, MaxThinkTime)
		// =======================HEARING=======================,
		.group("Civil_UnSpecClaim_30_ClaimantResp") {
			exec(http("005_Hearing")
				.post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEHearing")
				.headers(Headers.validateHeader)
				.body(ElFileBody("uc_unspec_CL_resp_bodies/claimantHearingAvailability.dat"))
				.check(substring("applicant1DQHearing")))
		}
		.pause(MinThinkTime, MaxThinkTime)
		// =======================UPLOAD DRAFT DIRECTION=======================,
		.group("Civil_UnSpecClaim_30_ClaimantResp") {
			exec(http("005_UploadDraftDirection")
				.post("/documentsv2")
				.headers(Headers.commonHeader)
				.header("content-type", "multipart/form-data; boundary=----263708296816916542312907778465")
				.bodyPart(RawFileBodyPart("files", "CL_upload.docx").fileName("CL_upload.docx")
					.transferEncoding("binary")).asMultipartForm
				.formParam("classification", "PUBLIC")
				.formParam("caseTypeId", "CIVIL")
				.formParam("jurisdictionId", "CIVIL")
				.check(jsonPath("$.documents[0].hashToken").saveAs("CL_HashToken"))
				.check(jsonPath("$.documents[0]._links.self.href").saveAs("CL_Document_url"))
				.check(substring("CL_upload.docx")))
		}
		.pause(MinThinkTime, MaxThinkTime)
		.group("Civil_UnSpecClaim_30_ClaimantResp") {
			exec(http("010_DraftDirections")
				.post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEDraftDirections")
				.headers(Headers.validateHeader)
				.body(ElFileBody("uc_unspec_CL_resp_bodies/claimantDraftDirections.dat"))
				.check(substring("applicant1DQDraftDirections")))
		}
		.pause(MinThinkTime, MaxThinkTime)
		// =======================SUPPORT WITH ACCESS NEEDS=======================,
		.group("Civil_UnSpecClaim_30_ClaimantResp") {
			exec(http("005_HearingSupport")
				.post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEHearingSupport")
				.headers(Headers.validateHeader)
				.body(ElFileBody("uc_unspec_CL_resp_bodies/claimantHearingSupport.dat"))
				.check(substring("applicant1DQHearingSupport")))
		}
		.pause(MinThinkTime, MaxThinkTime)
		// =======================VULNERABILITY=======================,
		.group("Civil_UnSpecClaim_30_ClaimantResp") {
			exec(http("005_VulnerabilityQuestions")
				.post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEVulnerabilityQuestions")
				.headers(Headers.validateHeader)
				.body(ElFileBody("uc_unspec_CL_resp_bodies/claimantVulnerability.dat"))
				.check(substring("applicant1DQVulnerabilityQuestions")))
		}
		.pause(MinThinkTime, MaxThinkTime)
		// =======================FUTURE APPLICATION=======================,
		.group("Civil_UnSpecClaim_30_ClaimantResp") {
			exec(http("005_FurtherInformation")
				.post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEFurtherInformation")
				.headers(Headers.validateHeader)
				.body(ElFileBody("uc_unspec_CL_resp_bodies/claimantFurtherInformation.dat"))
				.check(substring("applicant1DQFurtherInformation")))
		}
		.pause(MinThinkTime, MaxThinkTime)
		// =======================NAME=======================,
		.group("Civil_UnSpecClaim_30_ClaimantResp") {
			exec(http("005_StatementOfTruth")
			.post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEStatementOfTruth")
			.headers(Headers.validateHeader)
			.body(ElFileBody("uc_unspec_CL_resp_bodies/claimantStatementOfTruth.dat"))
			.check(substring("http://gateway-ccd.perftest.platform.hmcts.net/case-types/CIVIL/validate" +
				"?pageId=CLAIMANT_RESPONSEStatementOfTruth")))
		}
		.pause(MinThinkTime, MaxThinkTime)
		// =======================SUBMIT=======================,
		.group("Civil_UnSpecClaim_30_ClaimantResp") {
			exec(http("005_Submit")
				.post("/data/cases/#{caseId}/events")
				.headers(Headers.validateHeader)
				.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
				.body(ElFileBody("uc_unspec_CL_resp_bodies/claimantProceedWithClaim.dat"))
				.check(substring("You have chosen to proceed with the claim")))

			.exec(http("010_Submit")
				.get("/data/internal/cases/#{caseId}")
				.headers(Headers.validateHeader)
				.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
				.check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{caseId}")))
		}
		.pause(MinThinkTime, MaxThinkTime)
}
