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
		feed(caseFeeder)
		.exec(http("CIVIL_AssignCase_000_AssignCase")
			.post("http://civil-service-perftest.service.core-compute-perftest.internal/testing-support/assign-case/#{caseId}/RESPONDENTSOLICITORONE")
			.header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJraWQiOiI4cDJpajg2S0pTeENKeGcveUovV2w3TjcxMXM9IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJjaXZpbC5kYW1hZ2VzLmNsYWltcytvcmdhbmlzYXRpb24uMi5zb2xpY2l0b3IuMUBnbWFpbC5jb20iLCJjdHMiOiJPQVVUSDJfU1RBVEVMRVNTX0dSQU5UIiwiYXV0aF9sZXZlbCI6MCwiYXVkaXRUcmFja2luZ0lkIjoiODRkODg2YTktYTA4Ni00ZDgwLWI1YmYtNDIxMGYzZmZmZDkyLTE2MDE4MDAzIiwic3VibmFtZSI6ImNpdmlsLmRhbWFnZXMuY2xhaW1zK29yZ2FuaXNhdGlvbi4yLnNvbGljaXRvci4xQGdtYWlsLmNvbSIsImlzcyI6Imh0dHBzOi8vZm9yZ2Vyb2NrLWFtLnNlcnZpY2UuY29yZS1jb21wdXRlLWlkYW0tcGVyZnRlc3QuaW50ZXJuYWw6ODQ0My9vcGVuYW0vb2F1dGgyL3JlYWxtcy9yb290L3JlYWxtcy9obWN0cyIsInRva2VuTmFtZSI6ImFjY2Vzc190b2tlbiIsInRva2VuX3R5cGUiOiJCZWFyZXIiLCJhdXRoR3JhbnRJZCI6Ik04ZmEtZW01ODZ6Wlg0WTIxRndFd1Vub0xrbyIsImF1ZCI6ImhtY3RzIiwibmJmIjoxNjk1OTg2NzA0LCJncmFudF90eXBlIjoicGFzc3dvcmQiLCJzY29wZSI6WyJvcGVuaWQiLCJwcm9maWxlIiwicm9sZXMiXSwiYXV0aF90aW1lIjoxNjk1OTg2NzA0LCJyZWFsbSI6Ii9obWN0cyIsImV4cCI6MTY5NjAxNTUwNCwiaWF0IjoxNjk1OTg2NzA0LCJleHBpcmVzX2luIjoyODgwMCwianRpIjoibURBWm5SVXo5UWlUWFVNdUZCMkdLaVBmUllrIn0.LSiphUMxM8RvMJIsrBl5F5NZgNlNDM8ZXICsLt4L8TYB-z9vrxCnDYSeOsxabaBiYo6uvUNHIQBqNJ20MeLaD6QU6mYBnmpIe2ZskmVF0ibicssnmb72bNrpZ_Eup4i5wONlCjCN8uXJbNshna05nEvDWLrS309M6JZyVkxU3UjCT7ZOYHgou466UQ3HaBb9xM_GjzzQw7UrN_m2_ZtxePROXitCS_VR6CwFbn5YHo3Uk5p59qr9TWhx-jIKKjM1jUqiHbKxgQ8P3oaHcmvtim_EcmTuV2LzcAa28EVXb9It7xhEVTkJC0ofnl2zqCJEzfYAeMfKf4HtAlpdNRPSHw")
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
