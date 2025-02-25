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
*Business process : As part of the creating bundle we need to create bundle on the fly which is a workaround
* http://civil-service-perftest.service.core-compute-perftest.internal/testing-support/1725556015019824/trigger-trial-bundle

* we should use hearing admin and password as username and password
 ======================================================================================*/
	
	

	//userType must be "Caseworker", "Legal" or "Citizen"
	val BearerTokenForBundle =
	
	exec(http("Civil_000_GetBearerToken")
		.post(idamURL + "/o/token") //change this to idamapiurl if this not works
		.formParam("grant_type", "password")
		.formParam("username", "#{centreadminuser}")
		.formParam("password", "Password12!")
		.formParam("client_id", "civil_citizen_ui")
		// .formParam("client_secret", clientSecret)
		.formParam("client_secret", "47js6e86Wv5718D2O77OL466020731ii")
		.formParam("scope", "profile roles openid")
		.header("Content-Type", "application/x-www-form-urlencoded")
		.check(jsonPath("$.access_token").saveAs("bearerToken")))
		
		.pause(minThinkTime, maxThinkTime)
	
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
	
	//Below is the code for assign the case to defendant,
		val run =
	group("CIVIL_AssignCase_000_AssignCase") {
	//	feed(caseFeeder)
		exec(http("CIVIL_AssignCase_000_AssignCase")
			.post("http://civil-service-perftest.service.core-compute-perftest.internal/testing-support/assign-case/#{caseId}/RESPONDENTSOLICITORONE")
			.header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJraWQiOiI4cDJpajg2S0pTeENKeGcveUovV2w3TjcxMXM9IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJjaXZpbC5kYW1hZ2VzLmNsYWltcytvcmdhbmlzYXRpb24uMi5zb2xpY2l0b3IuMUBnbWFpbC5jb20iLCJjdHMiOiJPQVVUSDJfU1RBVEVMRVNTX0dSQU5UIiwiYXV0aF9sZXZlbCI6MCwiYXVkaXRUcmFja2luZ0lkIjoiZTA1NmNmYjYtNjJlOC00NTlkLTk1ZDQtODIyZmM5MmI2ZTVjLTk3ODM0NDYiLCJzdWJuYW1lIjoiY2l2aWwuZGFtYWdlcy5jbGFpbXMrb3JnYW5pc2F0aW9uLjIuc29saWNpdG9yLjFAZ21haWwuY29tIiwiaXNzIjoiaHR0cHM6Ly9mb3JnZXJvY2stYW0uc2VydmljZS5jb3JlLWNvbXB1dGUtaWRhbS1wZXJmdGVzdC5pbnRlcm5hbDo4NDQzL29wZW5hbS9vYXV0aDIvcmVhbG1zL3Jvb3QvcmVhbG1zL2htY3RzIiwidG9rZW5OYW1lIjoiYWNjZXNzX3Rva2VuIiwidG9rZW5fdHlwZSI6IkJlYXJlciIsImF1dGhHcmFudElkIjoiY2ZtbENEVFhEOUtsTnlLM05lYS1SRVlUVWlvIiwibm9uY2UiOiJVWG40YTN6YnR5cU9WVVUxZ19xY3JiaEttc3Rhc05BWVFiZGxwUmY4XzRzIiwiYXVkIjoieHVpd2ViYXBwIiwibmJmIjoxNzQwNDE0MDE5LCJncmFudF90eXBlIjoiYXV0aG9yaXphdGlvbl9jb2RlIiwic2NvcGUiOlsib3BlbmlkIiwicHJvZmlsZSIsInJvbGVzIiwiY3JlYXRlLXVzZXIiLCJtYW5hZ2UtdXNlciIsInNlYXJjaC11c2VyIl0sImF1dGhfdGltZSI6MTc0MDQxNDAxOSwicmVhbG0iOiIvaG1jdHMiLCJleHAiOjE3NDA0NDI4MTksImlhdCI6MTc0MDQxNDAxOSwiZXhwaXJlc19pbiI6Mjg4MDAsImp0aSI6Im9IQ0htLVU1V0lsQTlWSEJHcmR3ZUpXMC1YQSJ9.eyv9OOkRt9vJyhIZ-umEvfP6pm93D6slS23qzH10aWxWfSt1gdWL-HDIifD9W2H1kR4zVmUNJJVJnq0trGE7iXELT4JRPBQuVK305zLoQp219t0ZQYT9Gv9Nu5S49WQ24MqWV_GkTNf1xtS9hevgdwaP6ZNSLmyWRI3WtSdLn0050lLMUh49dScrecujKCvCKeg_u-V3betGNWthRO3ADU7MlVgzdQe4vmK2B4K3lo8r78nO-gjmFVJJIHDaV7Hxdgyrxd106YYXHSF25dwtIbW2TRzt1UNPRNMQOQpXDEcLNCqiBx9qRDpeE6c3eCypQ9qwQblzL0BY0ZPtMtzudA")
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
		}*/
	
	
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
			/*.exec { session =>
				val fw = new BufferedWriter(new FileWriter("FinalcaseIds.csv", true))
				try {
					fw.write(session("caseId").as[String] + "\r\n")
				} finally fw.close()
				session
			}*/
	
	
	val cuibundle =
		group("CIVIL_AssignCase_000_CreateBundle") {
			//	feed(caseFeeder)
			exec(BearerTokenForBundle)
				.exec(http("CIVIL_AssignCase_000_CreateBundle")
					.get("http://civil-service-perftest.service.core-compute-perftest.internal/testing-support/#{caseId}/trigger-trial-bundle")
					.header("Authorization", "Bearer ${bearerToken}")
					.header("Content-Type", "application/json")
					.header("Accept", "*/*")
					.check(status.in(200, 201))
				)
		}
			.pause(minThinkTime, maxThinkTime)
	
	
}
