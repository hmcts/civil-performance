package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{AssignCase_Header, Environment}

import java.io.{BufferedWriter, FileWriter}

object  CivilAssignCase {
	
	val minThinkTime = Environment.minThinkTime
	val maxThinkTime = Environment.maxThinkTime
	
	val manageOrgURL = Environment.manageOrgURL
	val idamURL=Environment.idamURL
	val caseFeeder = csv("caseIdsForAssign.csv").circular
	
	/*======================================================================================
*Business process : As part of the create FR respondent application share a case
* Below group contains all the share the unassigned case
======================================================================================*/
	//userType must be "Caseworker", "Legal" or "Citizen"
	val Auth  =
		
			exec(http("Civil_000_GetBearerToken")
				.post("https://idam-api.perftest.platform.hmcts.net/loginUser") //change this to idamapiurl if this not works
		//		.formParam("grant_type", "password")
				.formParam("username", "hmcts.civil+organisation.2.solicitor.1@gmail.com")
				.formParam("password", "Password12!")
		//		.formParam("client_id", "civil_citizen_ui")
				// .formParam("client_secret", clientSecret)
			//	.formParam("client_secret", "47js6e86Wv5718D2O77OL466020731ii")
				//.formParam("scope", "profile roles openid")
				.header("Content-Type", "application/x-www-form-urlencoded")
				.check(jsonPath("$.access_token").saveAs("bearerToken")))
			
			.pause(minThinkTime,maxThinkTime)
	
	//def run(implicit postHeaders: Map[String, String]): ChainBuilder = {
		val run =
	group("CIVIL_AssignCase_000_AssignCase") {
	//	feed(caseFeeder)
		exec(http("CIVIL_AssignCase_000_AssignCase")
			.post("http://civil-service-perftest.service.core-compute-perftest.internal/testing-support/assign-case/#{caseId}/RESPONDENTSOLICITORONE")
			.header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJraWQiOiI4cDJpajg2S0pTeENKeGcveUovV2w3TjcxMXM9IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJjaXZpbC5kYW1hZ2VzLmNsYWltcytvcmdhbmlzYXRpb24uMi5zb2xpY2l0b3IuMUBnbWFpbC5jb20iLCJjdHMiOiJPQVVUSDJfU1RBVEVMRVNTX0dSQU5UIiwiYXV0aF9sZXZlbCI6MCwiYXVkaXRUcmFja2luZ0lkIjoiYjBiODY4MzItOThhMi00OWUwLWJhNmQtZmZiNTFjNjhmNGY5LTEzNDMzNTM2MSIsInN1Ym5hbWUiOiJjaXZpbC5kYW1hZ2VzLmNsYWltcytvcmdhbmlzYXRpb24uMi5zb2xpY2l0b3IuMUBnbWFpbC5jb20iLCJpc3MiOiJodHRwczovL2Zvcmdlcm9jay1hbS5zZXJ2aWNlLmNvcmUtY29tcHV0ZS1pZGFtLXBlcmZ0ZXN0LmludGVybmFsOjg0NDMvb3BlbmFtL29hdXRoMi9yZWFsbXMvcm9vdC9yZWFsbXMvaG1jdHMiLCJ0b2tlbk5hbWUiOiJhY2Nlc3NfdG9rZW4iLCJ0b2tlbl90eXBlIjoiQmVhcmVyIiwiYXV0aEdyYW50SWQiOiJfeF9ZWTlrY015Vi1wai1GY215WmlBZU40Y2siLCJhdWQiOiJobWN0cyIsIm5iZiI6MTY5NzQ1MDM0NywiZ3JhbnRfdHlwZSI6InBhc3N3b3JkIiwic2NvcGUiOlsib3BlbmlkIiwicHJvZmlsZSIsInJvbGVzIl0sImF1dGhfdGltZSI6MTY5NzQ1MDM0NywicmVhbG0iOiIvaG1jdHMiLCJleHAiOjE2OTc0NzkxNDcsImlhdCI6MTY5NzQ1MDM0NywiZXhwaXJlc19pbiI6Mjg4MDAsImp0aSI6ImxOUHVyNVFZZG53RG1tV21fR0dickF2MmJnOCJ9.b_4-TkSJMSrE6i4O6XOk2V25KFyBJ7fd_TZf75n89FCN_FQvxkzJGzZiYeyzWSvbsoWzUXYHzacVB6SS_IGdbhyzIow4VgZRgyLCGMDgNFHRhGh96lsyKr1TxA-mDm8ebapC3MmuBLg4xNiv63MqYM_fx0QnKyb_zYcSC2K0ZGW-fg0vEkIm_slBL8ebYXvUfWMYctr69xklknzNDzAMvOhX4KKhoRERi1fLjblofXXp821CUuawXwZaBaN3P22-cJQ11bAFkQ4qCcYuwDPCvN09RhkHoUEKwppVuYkcoGk-AMEkWGk_uGXndnwbh1v-K8DDOtSnVkuHwKgcuoppBg")
			.header("Content-Type", "application/json")
			.header("Accept", "*/*")
			.check(status.in(200, 201))
		)
	}
		.pause(minThinkTime, maxThinkTime)
		
		.exec { session =>
			val fw = new BufferedWriter(new FileWriter("AssignedCases.csv", true))
			try {
				fw.write(session("caseId").as[String] + "\r\n")
			} finally fw.close()
			session
		}
		
	
	
	
	val cuiassign =
		group("CIVIL_AssignCase_000_AssignCase") {
		//	feed(caseFeeder)
			exec(Auth)
				.exec(http("CIVIL_AssignCase_000_AssignCase")
					.post("http://civil-service-perftest.service.core-compute-perftest.internal/testing-support/assign-case/#{caseId}/RESPONDENTSOLICITORONE")
					//   .get( "/cases/searchCases?start_date=#{randomStartDate}&end_date=#{randomEndDate}")
					// .get( "/cases/searchCases?start_date=2022-01-13T00:00:00&end_date=2023-04-16T15:38:00")
					.header("Authorization", "Bearer ${bearerToken}")
					.header("Content-Type", "application/json")
					.header("Accept", "*/*")
					.check(status.in(200, 201))
				)
		}
			.pause(minThinkTime, maxThinkTime)
			
			//Deepak - Cases that make the final step
			.exec { session =>
				val fw = new BufferedWriter(new FileWriter("FinalcaseIds.csv", true))
				try {
					fw.write(session("caseId").as[String] + "\r\n")
				} finally fw.close()
				session
			}
		
	
	
}
