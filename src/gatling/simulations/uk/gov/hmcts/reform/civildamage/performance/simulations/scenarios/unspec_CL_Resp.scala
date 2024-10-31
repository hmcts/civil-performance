package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.uc_unspec_CL_Resp_Headers._
import utils._

import scala.concurrent.duration.DurationInt

object unspec_CL_Resp {
	val BaseURL = Environment.baseURL
	val IdamURL = Environment.idamURL
	val MinThinkTime = Environment.minThinkTime
	val MaxThinkTime = Environment.maxThinkTime



	val RespToDF=
		// =======================OPEN CASE=======================,
		group("Civil_30_UnSpecClaim_ClaimantResp") {
			exec(http("OpenCase_005_InternalCases")
				.get("/data/internal/cases/#{caseId}")
				.headers(headers_0))
				.pause(162.milliseconds)

				.exec(http("OpenCase_010_Jurisdiction")
					.get("/api/wa-supported-jurisdiction/get")
					.headers(headers_1))

				.exec(http("OpenCase_015_RoleAssignment")
					.post("/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
					.headers(headers_2)
					.body(ElFileBody("uc_unspec_CL_resp_bodies/0002_request.bin")))
		}
			.pause(10)
			// =======================RESPOND TO DEF=======================,
			.group("Civil_30_UnSpecClaim_ClaimantResp") {
				exec(http("RespToDef_005_Jurisdiction")
					.get("/workallocation/case/tasks/#{caseId}/event/CLAIMANT_RESPONSE/caseType/CIVIL/jurisdiction/CIVIL")
					.headers(headers_3))

					.exec(http("RespToDef_010_IgnoreWarning")
						.get("/data/internal/cases/#{caseId}/event-triggers/CLAIMANT_RESPONSE?ignore-warning=false")
						.headers(headers_4)
						.check(jsonPath("$.event_token").optional.saveAs("event_token"))
						.check(regex("document_url\":\"(.*?)\"").saveAs("DF_Document_url"))
						.check(regex("upload_timestamp\":\"(.*?)\"").saveAs("upload_timestamp"))
						.check(regex("partyID\":\"(.*?)\"").saveAs("partyID"))
					)
					.exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("xsrf_token")))

					.exec(http("RespToDef_015_profile")
						.get("/data/internal/profile")
						.headers(headers_5))

					.exec(http("RespToDef_020_jurisdiction")
						.get("/workallocation/case/tasks/#{caseId}/event/CLAIMANT_RESPONSE/caseType/CIVIL/jurisdiction/CIVIL")
						.headers(headers_6))
			}
					.pause(10)

					// =======================DOC URL=DO YOU WANT TO PROCEED WITH CLAIM=======================,
					.group("Civil_30_UnSpecClaim_ClaimantResp") {
						exec(http("005_RespondentResponse")
							.post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSERespondentResponse")
							.headers(Headers.validateHeader)
							.body(ElFileBody("uc_unspec_CL_resp_bodies/0007_request.dat")))
					}
					.pause(5)
					//			// =======================CONTINUE========================,
					.group("Civil_30_UnSpecClaim_ClaimantResp") {
						exec(http("005_DefenceResponseDocument")
							.post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEApplicantDefenceResponseDocument")
							.headers(Headers.validateHeader)
							.body(ElFileBody("uc_unspec_CL_resp_bodies/0008_request.dat")))
					}
					.pause(5)
					//			// =======================FILE DIRECTION QUESTIONAIE=======================,
					.group("Civil_30_UnSpecClaim_ClaimantResp") {
						exec(http("005_FileDirectionsQuestionnaire")
							.post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEFileDirectionsQuestionnaire")
							.headers(Headers.validateHeader)
							.body(ElFileBody("uc_unspec_CL_resp_bodies/0009_request.dat")))
					}
					.pause(5)
					// =======================FIXED RECOVERABLE COST=======================,
					.group("Civil_30_UnSpecClaim_ClaimantResp") {
						exec(http("005_FixedRecoverableCosts")
							.post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEFixedRecoverableCosts")
							.headers(Headers.validateHeader)
							.body(ElFileBody("uc_unspec_CL_resp_bodies/0010_request.dat")))
					}
					.pause(5)
					// =======================ELECTRONIC DISCLOSURE=======================,
					.group("Civil_30_UnSpecClaim_ClaimantResp") {
						exec(http("005_DisclosureOfNonElectronicDocuments")
							.post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEDisclosureOfNonElectronicDocuments")
							.headers(Headers.validateHeader)
							.body(ElFileBody("uc_unspec_CL_resp_bodies/0011_request.dat")))
					}
					.pause(5)
					// =======================EPERT=======================,
					.group("Civil_30_UnSpecClaim_ClaimantResp") {
						exec(http("005_Experts")
							.post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEExperts")
							.headers(Headers.validateHeader)
							.body(ElFileBody("uc_unspec_CL_resp_bodies/0012_request.dat")))
					}
					.pause(5)
					// =======================WITNESS=======================,
					.group("Civil_30_UnSpecClaim_ClaimantResp") {
						exec(http("005_Witnesses")
							.post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEWitnesses")
							.headers(Headers.validateHeader)
							.body(ElFileBody("uc_unspec_CL_resp_bodies/0013_request.dat")))
					}
					.pause(5)
					// =======================LANGUAGE=======================,
					.group("Civil_30_UnSpecClaim_ClaimantResp") {
						exec(http("005_Language")
							.post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSELanguage")
							.headers(Headers.validateHeader)
							.body(ElFileBody("uc_unspec_CL_resp_bodies/0014_request.dat")))
					}
					.pause(5)
					// =======================HEARING=======================,
					.group("Civil_30_UnSpecClaim_ClaimantResp") {
						exec(http("005_Hearing")
							.post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEHearing")
							.headers(Headers.validateHeader)
							.body(ElFileBody("uc_unspec_CL_resp_bodies/0015_request.dat")))
					}
					.pause(5)
					// =======================UPLOAD DRAFT DIRECTION=======================,
					.group("Civil_30_UnSpecClaim_ClaimantResp") {
						exec(http("005_UploadDraftDirection")
							.post("/documentsv2")
							.headers(headers_16)
							.header("content-type", "multipart/form-data; boundary=----263708296816916542312907778465")
							.bodyPart(RawFileBodyPart("files", "CL_upload.docx")
								.fileName("CL_upload.docx")
								.transferEncoding("binary"))
							.asMultipartForm
							.formParam("classification", "PUBLIC")
							.formParam("caseTypeId", "CIVIL")
							.formParam("jurisdictionId", "CIVIL")
							.check(jsonPath("$.documents[0].hashToken").saveAs("CL_HashToken"))
							.check(jsonPath("$.documents[0]._links.self.href").saveAs("CL_Document_url"))
							.check(substring("CL_upload.docx")))
					}
					.pause(5)
					.group("Civil_30_UnSpecClaim_ClaimantResp") {
						exec(http("010_DraftDirections")
							.post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEDraftDirections")
							.headers(Headers.validateHeader)
							.body(ElFileBody("uc_unspec_CL_resp_bodies/0017_request.dat")))
					}
					.pause(5)
					// =======================SUPPORT WITH ACCESS NEEDS=======================,
					.group("Civil_30_UnSpecClaim_ClaimantResp") {
						exec(http("005_HearingSupport")
							.post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEHearingSupport")
							.headers(Headers.validateHeader)
							.body(ElFileBody("uc_unspec_CL_resp_bodies/0018_request.dat")))
					}
					.pause(5)
					// =======================VULNERABILITY=======================,
					.group("Civil_30_UnSpecClaim_ClaimantResp") {
						exec(http("005_VulnerabilityQuestions")
							.post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEVulnerabilityQuestions")
							.headers(Headers.validateHeader)
							.body(ElFileBody("uc_unspec_CL_resp_bodies/0019_request.dat")))
					}
					.pause(5)
					// =======================FUTU APPLICATION=======================,
					.group("Civil_30_UnSpecClaim_ClaimantResp") {
						exec(http("005_FurtherInformation")
							.post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEFurtherInformation")
							.headers(Headers.validateHeader)
							.body(ElFileBody("uc_unspec_CL_resp_bodies/0020_request.dat")))
					}
					.pause(5)
					// =======================NAME=======================,
					.group("Civil_30_UnSpecClaim_ClaimantResp") {
						exec(http("005_StatementOfTruth")
						.post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEStatementOfTruth")
						.headers(Headers.validateHeader)
						.body(ElFileBody("uc_unspec_CL_resp_bodies/0021_request.dat")))
							}
					.pause(5)
					// =======================SUBMIT=======================,
					.group("Civil_30_UnSpecClaim_ClaimantResp") {
						exec(http("005_Submit")
							.post("/data/cases/#{caseId}/events")
							.headers(headers_22)
							.body(ElFileBody("uc_unspec_CL_resp_bodies/0022_request.dat")))
					}
//
//					.exec(http("request_23")
//						.get("/data/internal/cases/#{caseId}")
//						.headers(headers_23))
//					.pause(9)
//			}

}
