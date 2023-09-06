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
				.post(idamURL + "/o/token") //change this to idamapiurl if this not works
				.formParam("grant_type", "password")
				.formParam("username", "perftestuser.new@mailinator.com")
				.formParam("password", "Password12!")
				.formParam("client_id", "civil_citizen_ui")
				// .formParam("client_secret", clientSecret)
				.formParam("client_secret", "47js6e86Wv5718D2O77OL466020731ii")
				.formParam("scope", "profile roles openid")
				.header("Content-Type", "application/x-www-form-urlencoded")
				.check(jsonPath("$.access_token").saveAs("bearerToken")))
			
			.pause(minThinkTime,maxThinkTime)
	
	//def run(implicit postHeaders: Map[String, String]): ChainBuilder = {
		val run =
	group("CIVIL_AssignCase_000_AssignCase") {
		feed(caseFeeder)
		.exec(http("CIVIL_AssignCase_000_AssignCase")
			.post("http://civil-service-perftest.service.core-compute-perftest.internal/testing-support/assign-case/#{caseId}/RESPONDENTSOLICITORONE")
			//   .get( "/cases/searchCases?start_date=#{randomStartDate}&end_date=#{randomEndDate}")
			// .get( "/cases/searchCases?start_date=2022-01-13T00:00:00&end_date=2023-04-16T15:38:00")
			.header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJraWQiOiI4cDJpajg2S0pTeENKeGcveUovV2w3TjcxMXM9IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJobWN0cy5jaXZpbCtvcmdhbmlzYXRpb24uMi5zb2xpY2l0b3IuMTNAZ21haWwuY29tIiwiY3RzIjoiT0FVVEgyX1NUQVRFTEVTU19HUkFOVCIsImF1dGhfbGV2ZWwiOjAsImF1ZGl0VHJhY2tpbmdJZCI6IjRiYzlhMzkwLWU2Y2YtNGE0MS1iNTRmLWQwNjZmYzQ3NDg2Ny03OTI5NzgwOCIsInN1Ym5hbWUiOiJobWN0cy5jaXZpbCtvcmdhbmlzYXRpb24uMi5zb2xpY2l0b3IuMTNAZ21haWwuY29tIiwiaXNzIjoiaHR0cHM6Ly9mb3JnZXJvY2stYW0uc2VydmljZS5jb3JlLWNvbXB1dGUtaWRhbS1wZXJmdGVzdC5pbnRlcm5hbDo4NDQzL29wZW5hbS9vYXV0aDIvcmVhbG1zL3Jvb3QvcmVhbG1zL2htY3RzIiwidG9rZW5OYW1lIjoiYWNjZXNzX3Rva2VuIiwidG9rZW5fdHlwZSI6IkJlYXJlciIsImF1dGhHcmFudElkIjoiS280LUZxemxIb2xFOUxEb0ZkQjIzbHdURkw0Iiwibm9uY2UiOiJXSUxzMW1EbHpDbkxubmNyengxV3lWQ3BPeGdLZV81U2NKQ2tzNzJ5MWswIiwiYXVkIjoieHVpd2ViYXBwIiwibmJmIjoxNjg5MTAyMTczLCJncmFudF90eXBlIjoiYXV0aG9yaXphdGlvbl9jb2RlIiwic2NvcGUiOlsib3BlbmlkIiwicHJvZmlsZSIsInJvbGVzIiwiY3JlYXRlLXVzZXIiLCJtYW5hZ2UtdXNlciIsInNlYXJjaC11c2VyIl0sImF1dGhfdGltZSI6MTY4OTEwMjE3MiwicmVhbG0iOiIvaG1jdHMiLCJleHAiOjE2ODkxMzA5NzMsImlhdCI6MTY4OTEwMjE3MywiZXhwaXJlc19pbiI6Mjg4MDAsImp0aSI6ImtvX2FTY3FPMFlZcHV6b0QwSjhuSWZlX0FpbyJ9.K73IA1Jlo0UwsZWzFInrhnZ-b3496FLjcaxADjQaigMLyK9QQJyaunvAFoQT_OSDQTZRZyJsjFHhqzTW4IsfI98p6GymFlc1i1spewAWNBomnc8jLTp9-32Zeek8I_eAaZM03i6oaI_d9P2nhqzoSjM1LmAp10d2T4TQUVZjz66PMibKx6b1eCjOR1XDBdYP2oxcVnYnMzvK0dMZ0Gy6Ksvl93fKWbbZMK9s4keQ_PViDq-mEqq3OXUMfAfLy7hX47wzGmYRfj3HVz9c2wGfGm3sgAeUXLH7_tDaDNev54KcB3aL9M22MwifiFTYxvwOwisx5mcZ6lG2-eoHGonH5w")
			.header("Content-Type", "application/json")
			.header("Accept", "*/*")
			.check(status.in(200, 201))
		)
	}
		.pause(minThinkTime, maxThinkTime)
		
	
	
	
	val cuiassign =
		group("CIVIL_AssignCase_000_AssignCase") {
		//	feed(caseFeeder)
			exec(Auth)
				.exec(http("CIVIL_AssignCase_000_AssignCase")
					.post("http://civil-service-perftest.service.core-compute-perftest.internal/testing-support/assign-case/${caseId}/DEFENDANT")
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
