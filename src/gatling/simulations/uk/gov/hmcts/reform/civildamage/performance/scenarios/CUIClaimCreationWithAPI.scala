package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.Environment
import uk.gov.hmcts.reform.civildamage.performance.scenarios.CreateUser.IdamAPIURL
import java.io.{BufferedWriter, FileWriter}

object  CUIClaimCreationWithAPI {
	
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
	
	
	val CreateClaimCUIR2WithAPI =
		group("CUIR2_CreateCase_Case_000_CreateCase") {
			//	feed(caseFeeder)
			exec(http("CIVIL_AssignCase_000_AssignCase")
				.post("http://civil-service-perftest.service.core-compute-perftest.internal/cases/draft/citizen/#{userId}/event")
				.header("Authorization", "Bearer ${bearerToken}")
				.header("ServiceAuthorization", "#{ServiceToken}")
				.header("Content-Type", "application/json")
				.body(ElFileBody("bodies/cuiclaim/CUI_Create_Claim.json"))
				.check(jsonPath("$.id").saveAs("claimNumber"))
				.check(status.in(200, 201))
			)
		}
			.pause(minThinkTime, maxThinkTime)
			
			.exec { session =>
				val fw = new BufferedWriter(new FileWriter("CUIR2ClaimsWithAPI60k2.csv", true))
				try {
					fw.write(session("claimantEmailAddress").as[String] + "," + session("claimNumber").as[String] + "," + session("password").as[String] + "\r\n")
				} finally fw.close()
				
				session
			}
	
	
	val getUserId =
		group("CUIR2_Claimant_GetUser") {
			exec(http("CUIR2_Claimant_GetUser")
				.get(IdamAPIURL + "/o/userinfo")
				.header("Authorization", "Bearer ${bearerToken}")
				.header("Content-Type", "application/json")
				.check(jsonPath("$.uid").saveAs("userId"))
				.check(status.is(200)))
		}

	
	
	
	
	
}
