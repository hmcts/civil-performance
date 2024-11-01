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
//  			group("-2-") {
	val selectRespon2Claim =

// =================================select respond to claim================================================
			exec(http("Civil_Claim_420_005_RespondToClaim")
						.get("/workallocation/case/tasks/#{caseId}/event/DEFENDANT_RESPONSE_SPEC/caseType/CIVIL/jurisdiction/CIVIL")
						.headers(headers_30)
						.check(substring("task_required_for_event"))
					.check(status.is(200)))

			.exec(http("Civil_Claim_420_010_RespondToClaim")
						.get("/data/internal/cases/#{caseId}/event-triggers/DEFENDANT_RESPONSE_SPEC?ignore-warning=false")
						.headers(headers_31)
						.check(substring("DEFENDANT_RESPONSE"))
						.check(jsonPath("$.event_token").optional.saveAs("event_token")))

//			.exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("xsrf_token")))

  			// ================================CHECK TIMELINE=========================,
  		.exec(http("b_request_34")
					.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECRespondentCheckList")
  				.headers(headers_34)
					//.header("X-Xsrf-Token", "#{xsrf_token}")
  				.body(ElFileBody("b_DefResp_bodies/0034_request.dat")))
  				.pause(10)

  			// ===========================IS DEFENDANT  ADDRESS CORRECT====================,
  		.exec(http("b_request_35")
					.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECResponseConfirmNameAddress")
					.headers(headers_34)
  				//.header("X-Xsrf-Token", "#{xsrf_token}")
  				.body(ElFileBody("b_DefResp_bodies/0035_request.dat")))
  				.pause(385.milliseconds)

			.exec(http("b_request_36")
				.get("/api/caseshare/orgs").headers(headers_34))
  				.pause(10)

  			// =============================================LEGAL REPO ADDRESS CORECT==========================,
  		.exec(http("b_request_37")
					.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECResponseConfirmDetails")
					.headers(Headers.validateHeader)
					.body(ElFileBody("b_DefResp_bodies/0037_request.dat")))
  				.pause(10)

  			// ========================RESPOND TO CLAIM==================================,
  		.exec(http("b_request_38")
					.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECRespondentResponseTypeSpec")
  				.headers(Headers.validateHeader)
					//.header("X-Xsrf-Token", "#{xsrf_token}")
  				.body(ElFileBody("b_DefResp_bodies/0038_request.dat")))
  				.pause(10)

  			// ========================DISPUTE MONEY==================,
  		.exec(http("b_request_39")
					.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECdefenceRoute")
  				.headers(Headers.validateHeader)
					//.header("X-Xsrf-Token", "#{xsrf_token}")
  				.body(ElFileBody("b_DefResp_bodies/0039_request.dat")))
  				.pause(10)

  			// ===============================DESC DISPUTING THE CLAIM===============,
  		.exec(http("b_request_40")
					.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECUpload")
  				.headers(Headers.validateHeader)
					//.header("X-Xsrf-Token", "#{xsrf_token}")
  				.body(ElFileBody("b_DefResp_bodies/0040_request.dat")))
  				.pause(10)

  			// ===========================CLAIM TIMELINE===========================,
  		.exec(http("b_request_41")
					.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECHowToAddTimeline")
						.headers(Headers.validateHeader)
						//.header("X-Xsrf-Token", "#{xsrf_token}")
						 .body(ElFileBody("b_DefResp_bodies/0041_request.dat")))
						.pause(10)

  		.exec(http("b_request_42")
				.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECHowToAddTimelineManual")
					.headers(Headers.validateHeader)
					//.header("X-Xsrf-Token", "#{xsrf_token}")
					.body(ElFileBody("b_DefResp_bodies/0042_request.dat")))
					.pause(10)

  			// ========================MEDINIATION NO===============,
  		.exec(http("b_request_43")
					.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECMediation")
  				.headers(Headers.validateHeader)
					//.header("X-Xsrf-Token", "#{xsrf_token}")
  				.body(ElFileBody("b_DefResp_bodies/0043_request.dat")))
  				.pause(10)

  			// ==============================NO EXPERTS======================,
  		.exec(http("b_request_44")
					.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECSmallClaimExperts")
  				.headers(headers_34)
					//.header("X-Xsrf-Token", "#{xsrf_token}")
  				.body(ElFileBody("b_DefResp_bodies/0044_request.dat")))
  				.pause(10)

  		.exec(http("b_request_45")
				.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECSmallClaimWitnesses")
					.headers(headers_34)
					//.header("X-Xsrf-Token", "#{xsrf_token}")
					.body(ElFileBody("b_DefResp_bodies/0045_request.dat")))
					.pause(10)

  			// ========================================ANY WITNESS NO================,
  			// ========================LANGUAGE=================,
  		.exec(http("b_request_46")
					.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECLanguage")
  				.headers(headers_34)
					//.header("X-Xsrf-Token", "#{xsrf_token}")
  				.body(ElFileBody("b_DefResp_bodies/0046_request.dat")))
  				.pause(10)

  			// ==================HEARIN=============,
  		.exec(http("b_request_47")
					.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECSmaillClaimHearing")
  				.headers(headers_34)
					.body(ElFileBody("b_DefResp_bodies/0047_request.dat")))
  				.pause(10)

  			// ==================================COURT LOCATION====================,
  		.exec(http("b_request_48")
					.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECRequestedCourtLocationLRspec")
  				.headers(headers_34)
					.body(ElFileBody("b_DefResp_bodies/0048_request.dat")))
  				.pause(10)

  			// ====================================ACCESS NEEDS===========
  		.exec(http("b_request_49")
					.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECHearingSupport")
  				.headers(headers_34)
					//.header("X-Xsrf-Token", "#{xsrf_token}")
  				.body(ElFileBody("b_DefResp_bodies/0049_request.dat")))
  				.pause(3)

  		.exec(http("b_request_50")
				.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECVulnerabilityQuestions")
					.headers(headers_34)
					//.header("X-Xsrf-Token", "#{xsrf_token}")
					.body(ElFileBody("b_DefResp_bodies/0050_request.dat")))
					.pause(10)

  			// ================================================SOT============================
  		.exec(http("b_request_51")
					.post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECStatementOfTruth")
  				.headers(headers_34)
					//.header("X-Xsrf-Token", "#{xsrf_token}")
  				.body(ElFileBody("b_DefResp_bodies/0051_request.dat")))
  				.pause(404.milliseconds)

  			// ==================================SOT SUBMIT==================,
  		.exec(http("b_request_53")
					.post("/data/cases/#{caseId}/events")
  				.headers(headers_53)
					.body(ElFileBody("b_DefResp_bodies/0053_request.dat")))

			.pause(5)


}