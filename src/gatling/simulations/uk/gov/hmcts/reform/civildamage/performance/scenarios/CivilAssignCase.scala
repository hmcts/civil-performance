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
	val Auth = {
		
		exec(http("Civil_000_GetBearerToken")
			.post(idamURL + "/o/token") //change this to idamapiurl if this not works
			.formParam("grant_type", "password")
			.formParam("username", "#{defEmailAddress}")
			.formParam("password", "Password12!")
			.formParam("client_id", "civil_citizen_ui")
			// .formParam("client_secret", clientSecret)
			.formParam("client_secret", "47js6e86Wv5718D2O77OL466020731ii")
			.formParam("scope", "profile roles openid")
			.header("Content-Type", "application/x-www-form-urlencoded")
			.check(jsonPath("$.access_token").saveAs("bearerToken")))
	}
		.pause(minThinkTime, maxThinkTime)
	
	
	val AuthXUI =
		
		exec(http("Civil_000_GetBearerToken")
			.post(idamURL + "/o/token") //change this to idamapiurl if this not works
			.formParam("grant_type", "password")
			.formParam("username", "#{defendantuser}")
			.formParam("password", "Password12!")
			.formParam("client_id", "civil_citizen_ui")
			// .formParam("client_secret", clientSecret)
			.formParam("client_secret", "47js6e86Wv5718D2O77OL466020731ii")
			.formParam("scope", "profile roles openid")
			.header("Content-Type", "application/x-www-form-urlencoded")
			.check(jsonPath("$.access_token").saveAs("bearerToken")))
			
			.pause(minThinkTime, maxThinkTime)
	
	//Below is the code for assign the case to defendant,
		val run =
	group("CIVIL_AssignCase_000_AssignCase") {
	//	feed(caseFeeder)
		exec(http("CIVIL_AssignCase_000_AssignCase")
			.post("http://civil-service-perftest.service.core-compute-perftest.internal/testing-support/assign-case/#{caseId}/RESPONDENTSOLICITORONE")
			.header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJraWQiOiI4cDJpajg2S0pTeENKeGcveUovV2w3TjcxMXM9IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJjaXZpbC5kYW1hZ2VzLmNsYWltcytvcmdhbmlzYXRpb24uMi5zb2xpY2l0b3IuMUBnbWFpbC5jb20iLCJjdHMiOiJPQVVUSDJfU1RBVEVMRVNTX0dSQU5UIiwiYXV0aF9sZXZlbCI6MCwiYXVkaXRUcmFja2luZ0lkIjoiMjM1NWUwZmQtNzFjMi00NTdmLTg2ZGUtOWMwMGQ0MzBlNjExLTM4MDIxOTcyIiwic3VibmFtZSI6ImNpdmlsLmRhbWFnZXMuY2xhaW1zK29yZ2FuaXNhdGlvbi4yLnNvbGljaXRvci4xQGdtYWlsLmNvbSIsImlzcyI6Imh0dHBzOi8vZm9yZ2Vyb2NrLWFtLnNlcnZpY2UuY29yZS1jb21wdXRlLWlkYW0tcGVyZnRlc3QuaW50ZXJuYWw6ODQ0My9vcGVuYW0vb2F1dGgyL3JlYWxtcy9yb290L3JlYWxtcy9obWN0cyIsInRva2VuTmFtZSI6ImFjY2Vzc190b2tlbiIsInRva2VuX3R5cGUiOiJCZWFyZXIiLCJhdXRoR3JhbnRJZCI6IkZJR01BSmtBNU5naHZoLTZHalhGa09VcTRBbyIsIm5vbmNlIjoieVQ2aENhWDJOeEZ5Znkxc2Y4YV9FTkVLY2ZJS3JiM210TFVVS1hzNVdqTSIsImF1ZCI6Inh1aXdlYmFwcCIsIm5iZiI6MTczMTU3OTkxMiwiZ3JhbnRfdHlwZSI6ImF1dGhvcml6YXRpb25fY29kZSIsInNjb3BlIjpbIm9wZW5pZCIsInByb2ZpbGUiLCJyb2xlcyIsImNyZWF0ZS11c2VyIiwibWFuYWdlLXVzZXIiLCJzZWFyY2gtdXNlciJdLCJhdXRoX3RpbWUiOjE3MzE1Nzk5MTIsInJlYWxtIjoiL2htY3RzIiwiZXhwIjoxNzMxNjA4NzEyLCJpYXQiOjE3MzE1Nzk5MTIsImV4cGlyZXNfaW4iOjI4ODAwLCJqdGkiOiJ5cHRvUS1oYkJZUGpLRmJWZmR6LVc5MmZEcTgifQ.K9GtjEISBiuQZlbSSJn69Bm9168CQHBFXA6mLDxJy3yb726q_GFog3MaqsjJSb50GPXNGf7wGGDAmkScml_HO6PIv4NKhkIhNVcid4Ts9TE0_D4Mg0jjOvaKdE0H5bm8ZHjNCr6J4HqVFkI4aJouvEPSipaOJEoJARkgH-DEsqOGKJ0fz-_lS7UwLNYi6K-JYc0zD0mVTQ2UF8xSISdUvMsEBgxfgMgU3MdisnwiH6V36-iTkAxJRnhCz88Wc5vwTrhSI3lEE2bNPDWzRpdfkTTJNWyEKQ_XA2gL5yBi19eH3mUkBrLfxWinF1rLvTKbHkXkaMh5gTlXdGqQ6UIqcg")
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
					.post("http://civil-service-perftest.service.core-compute-perftest.internal/testing-support/assign-case/#{claimNumber}/DEFENDANT")
					.header("Authorization", "Bearer #{bearerToken}")
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
	
	
	//userType must be "Caseworker", "Legal" or "Citizen"
	val AuthForClaimCreationAPI =
		
		exec(http("Civil_000_GetBearerToken")
			.post(idamURL + "/o/token") //change this to idamapiurl if this not works
			.formParam("grant_type", "password")
			.formParam("username", "#{claimantEmailAddress}")
			.formParam("password", "Password12!")
			.formParam("client_id", "civil_citizen_ui")
			// .formParam("client_secret", clientSecret)
			.formParam("client_secret", "47js6e86Wv5718D2O77OL466020731ii")
			.formParam("scope", "profile roles openid")
			.header("Content-Type", "application/x-www-form-urlencoded")
			.check(jsonPath("$.access_token").saveAs("bearerToken")))
	
	
	val cuibundle =
		group("CIVIL_AssignCase_000_CreateBundle") {
			//	feed(caseFeeder)
			exec(BearerTokenForBundle)
				.exec(http("CIVIL_AssignCase_000_CreateBundle")
					.get("http://civil-service-perftest.service.core-compute-perftest.internal/testing-support/#{caseId}/trigger-trial-bundle")
					.header("Authorization", "Bearer #{bearerToken}")
					.header("Content-Type", "application/json")
					.header("Accept", "*/*")
					.check(status.in(200, 201))
				)
		}
			.pause(minThinkTime, maxThinkTime)
	
	
}
