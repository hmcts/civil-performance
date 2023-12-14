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
			.header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJraWQiOiI4cDJpajg2S0pTeENKeGcveUovV2w3TjcxMXM9IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJoZWFyaW5nc19hZG1pbl9yZWdpb24xX3VzZXJAanVzdGljZS5nb3YudWsiLCJjdHMiOiJPQVVUSDJfU1RBVEVMRVNTX0dSQU5UIiwiYXV0aF9sZXZlbCI6MCwiYXVkaXRUcmFja2luZ0lkIjoiNGJjOWEzOTAtZTZjZi00YTQxLWI1NGYtZDA2NmZjNDc0ODY3LTE2NDQ1OTc3MyIsInN1Ym5hbWUiOiJoZWFyaW5nc19hZG1pbl9yZWdpb24xX3VzZXJAanVzdGljZS5nb3YudWsiLCJpc3MiOiJodHRwczovL2Zvcmdlcm9jay1hbS5zZXJ2aWNlLmNvcmUtY29tcHV0ZS1pZGFtLXBlcmZ0ZXN0LmludGVybmFsOjg0NDMvb3BlbmFtL29hdXRoMi9yZWFsbXMvcm9vdC9yZWFsbXMvaG1jdHMiLCJ0b2tlbk5hbWUiOiJhY2Nlc3NfdG9rZW4iLCJ0b2tlbl90eXBlIjoiQmVhcmVyIiwiYXV0aEdyYW50SWQiOiJYcGhvR19FZ3BxLTdfc1Bxd2Fqb3BzYnFMc28iLCJub25jZSI6Ik9HVVVIOElzUWdSQ3BwejBBMnNtSXh0M0NNU3ROdVVtazRydTBVOXFPamciLCJhdWQiOiJ4dWl3ZWJhcHAiLCJuYmYiOjE3MDI1NzgxNjYsImdyYW50X3R5cGUiOiJhdXRob3JpemF0aW9uX2NvZGUiLCJzY29wZSI6WyJvcGVuaWQiLCJwcm9maWxlIiwicm9sZXMiLCJjcmVhdGUtdXNlciIsIm1hbmFnZS11c2VyIiwic2VhcmNoLXVzZXIiXSwiYXV0aF90aW1lIjoxNzAyNTc4MTY1LCJyZWFsbSI6Ii9obWN0cyIsImV4cCI6MTcwMjYwNjk2NiwiaWF0IjoxNzAyNTc4MTY2LCJleHBpcmVzX2luIjoyODgwMCwianRpIjoiZk43TmhxV3AycHJNdWNNOGRibVJnMDNRRE1rIn0.LwocBtaUreqZBcjZvsxgthrbmMNEIMYvkh-1Oq0kAT--SYsR-3L_NvVEFVHx01mlExTqiYDoh4Njq_780LOR2PayxgNaBj0ltn6Ji-thBjeqSYrGPzAPp3O6kgBLqJyFaBkEKcLmZwWrYT8xbwQKhAidHCqBsMOUdGrxL9MEQQKN_s4DzBkpPV2YRSZHx2uCyLHhU59NNe0lvLBkWAJGytWIJ1SOrXzzafbNTqrRoyr5W6gd7gisW1KL-Md9s1ZsvqUKAh8WRn1argvlu8M-3ukB4uJLLpOlbQlicT2Y0mIZqtqGIz7GWMIRrl47eafDn6ONEXynmbBdQr736L0uPw; dtSa=-; dtPC=4$378709109_710h-vHHLITMLRHBKLVIVTQNRAHADTVNKIBPHH-0e0; _gat_UA-124734893-1=1; _ga_0M3ZQMR272=GS1.1.1702578059.9.1.1702578712.0.0.0; rxvt=1702580547902|1702577913258; XSRF-TOKEN=qwkMREqP-Vp7-hT1lRu-qwscdl25S1dkI7Lg")
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
