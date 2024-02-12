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
			.header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJraWQiOiI4cDJpajg2S0pTeENKeGcveUovV2w3TjcxMXM9IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJjaXZpbC5kYW1hZ2VzLmNsYWltcytvcmdhbmlzYXRpb24uMi5zb2xpY2l0b3IuMUBnbWFpbC5jb20iLCJjdHMiOiJPQVVUSDJfU1RBVEVMRVNTX0dSQU5UIiwiYXV0aF9sZXZlbCI6MCwiYXVkaXRUcmFja2luZ0lkIjoiNGJjOWEzOTAtZTZjZi00YTQxLWI1NGYtZDA2NmZjNDc0ODY3LTIzMjA4MDc2MiIsInN1Ym5hbWUiOiJjaXZpbC5kYW1hZ2VzLmNsYWltcytvcmdhbmlzYXRpb24uMi5zb2xpY2l0b3IuMUBnbWFpbC5jb20iLCJpc3MiOiJodHRwczovL2Zvcmdlcm9jay1hbS5zZXJ2aWNlLmNvcmUtY29tcHV0ZS1pZGFtLXBlcmZ0ZXN0LmludGVybmFsOjg0NDMvb3BlbmFtL29hdXRoMi9yZWFsbXMvcm9vdC9yZWFsbXMvaG1jdHMiLCJ0b2tlbk5hbWUiOiJhY2Nlc3NfdG9rZW4iLCJ0b2tlbl90eXBlIjoiQmVhcmVyIiwiYXV0aEdyYW50SWQiOiJQQ0RPMFFRUFVrS2JvZHhXUkxSTm5oeEVXdHMiLCJub25jZSI6ImJJTXlnTk03LWxLeWpfeU5wTXh4ajF4dEE1T2xPT3hKN1JLeGFDY1pDRDgiLCJhdWQiOiJ4dWl3ZWJhcHAiLCJuYmYiOjE3MDc3NjI4NTcsImdyYW50X3R5cGUiOiJhdXRob3JpemF0aW9uX2NvZGUiLCJzY29wZSI6WyJvcGVuaWQiLCJwcm9maWxlIiwicm9sZXMiLCJjcmVhdGUtdXNlciIsIm1hbmFnZS11c2VyIiwic2VhcmNoLXVzZXIiXSwiYXV0aF90aW1lIjoxNzA3NzYyODU3LCJyZWFsbSI6Ii9obWN0cyIsImV4cCI6MTcwNzc5MTY1NywiaWF0IjoxNzA3NzYyODU3LCJleHBpcmVzX2luIjoyODgwMCwianRpIjoia1JoSHUycURGM0lzNlM5TVdXOGRMSlJoUXFvIn0.LpfF8_Kr12cQ7_bEjlWFTn4gAnKBknIHgUicpGuOv7YRUx7ilAFFJh0jnn0Onh29TSCPu_APk469yySFLISYdKUhAgwJdUBxPLlxd4rgvd_Yq1RAN4VjwvZmyI0J317GPP9JnwOjU0Hu1-qtgT9w5FGMSmKxMKhn_wHSk0maUdZ529-k4rsTIciXRfHYYpnM0We6CN8ASODaHZJi0knSO2Ua06SGXh67JlTIx8DWNuuoY3DQxyOkvp3TUS1D-7ZAW2tvHNM1guUqWW2Xq3weesGng28S_8tHQq47L22EmoD73Xu2C3J_QG531Mh8nffmDuRvBy9KH8T7MmHKlm_NIg")
			.header("Content-Type", "application/json")
			.header("Accept", "*/*")
			.check(status.in(200, 201))
		)
	}
		.pause(minThinkTime, maxThinkTime)
		
	/*	.exec { session =>
			val fw = new BufferedWriter(new FileWriter("AssignedCases.csv", true))
			try {
				fw.write(session("caseId").as[String] + "\r\n")
			} finally fw.close()
			session
		}
		*/
	
	
	
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
