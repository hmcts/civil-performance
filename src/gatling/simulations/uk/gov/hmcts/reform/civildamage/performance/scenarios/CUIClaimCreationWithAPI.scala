package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.Environment
import uk.gov.hmcts.reform.civildamage.performance.scenarios.CreateUser.IdamAPIURL
import java.io.{BufferedWriter, FileWriter}

object CUIClaimCreationWithAPI {

	val minThinkTime = Environment.minThinkTime
	val maxThinkTime = Environment.maxThinkTime

	val manageOrgURL = Environment.manageOrgURL
	val idamURL = Environment.idamURL
	val caseFeeder = csv("caseIdsForAssign.csv").circular

	// ---- Draft store settings ----
	val cuiURL = Environment.citizenURL
	val civilServiceURL = "http://civil-service-perftest.service.core-compute-perftest.internal"


	/*======================================================================================
	* Business process : As part of the creating bundle we need to create bundle on the fly which is a workaround
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
			.check(jsonPath("$.access_token").saveAs("bearerToken"))
			// id_token is returned because scope includes "openid" - needed by the Redis create-draft endpoint
			.check(jsonPath("$.id_token").optional.saveAs("idToken")))


	val CreateClaimCUIR2WithAPI =
		group("CUIR2_CreateCase_Case_000_CreateCase") {
			exec(http("CIVIL_AssignCase_000_AssignCase")
				.post(civilServiceURL + "/cases/draft/citizen/#{userId}/event")
				.header("Authorization", "Bearer #{bearerToken}")
				.header("ServiceAuthorization", "#{ServiceToken}")
				.header("Content-Type", "application/json")
				.body(ElFileBody("bodies/cuiclaim/CUI_Create_Claim.json"))
				.check(jsonPath("$.id").saveAs("claimNumber"))
				.check(status.in(200, 201)))
		}
			.pause(minThinkTime, maxThinkTime)
			.exec { session =>
				val fw = new BufferedWriter(new FileWriter("CUIR2ClaimsWithAPI60k2.csv", true))
				try {
					fw.write(session("claimantEmailAddress").as[String] + "," + session("claimNumber").as[String] + "," + session("password").as[String] + "\r\n")
				} finally fw.close()
				session
			}


	/*======================================================================================
	* Draft store - create draft claims
	* One IDAM citizen user per draft (one active draft per user in both implementations).
	 ======================================================================================*/

	// Appends the given session values as one CSV line (blank if a value is missing)
	def recordDraft(fileName: String, keys: String*) =
		exec { session =>
			val fw = new BufferedWriter(new FileWriter(fileName, true))
			try {
				fw.write(keys.map(k => session(k).asOption[Any].map(_.toString).getOrElse("")).mkString(",") + "\r\n")
			} finally fw.close()
			session
		}


	// Redis draft store (current master): log in through CUI first, then call testing-support
	// from the same session - mirrors the dev team's functional tests
	val CreateDraftClaimRedisLoggedIn =
		exec(CUIR2HomePage.CUIR2HomePage)
			.exec(CUIR2Login.CUIR2Login)

			// pick up the CSRF token from a logged-in page (optional - not all pages have one)
			.exec(http("CUI_DraftStore_005_GetCsrf")
				.get(cuiURL + "/dashboard")
				.check(regex("""name="_csrf" value="([^"]+)"""").optional.saveAs("csrf")))

			.doIfOrElse(session => session.contains("csrf")) {
				exec(http("CUI_DraftStore_000_CreateDraftRedis")
					.post(cuiURL + "/testing-support/create-draft-claim")
					.formParam("_csrf", "#{csrf}")
					.formParam("idToken", "#{idToken}")
					.check(status.is(200)))
			} {
				exec(http("CUI_DraftStore_000_CreateDraftRedis")
					.post(cuiURL + "/testing-support/create-draft-claim")
					.formParam("idToken", "#{idToken}")
					.check(status.is(200)))
			}

			// only reached if the create succeeded (exitBlockOnFail in the scenario)
			.exec(recordDraft("CUIDraftClaimsRedis.csv", "claimantEmailAddress", "userId"))
			.exec(CUIR2Logout.CUILogout)


	// CMC DB draft store - use once civil-service #8135 / CUI #8136 are merged and deployed
	// Body file must be: {"payload": { ...draft claim... }}
	val CreateDraftClaimDB =
		group("CUI_DraftStore_010_CreateDraftDB") {
			exec(http("CUI_DraftStore_010_CreateDraftDB")
				.post(civilServiceURL + "/dashboard/draft-claims")
				.header("Authorization", "Bearer #{bearerToken}")
				.header("ServiceAuthorization", "#{ServiceToken}") // remove if the endpoint doesn't require S2S
				.header("Content-Type", "application/json")
				.body(ElFileBody("bodies/cuiclaim/CUI_Draft_Claim_DB.json"))
				.check(status.is(201))
				.check(jsonPath("$.draftId").saveAs("draftId"))) // confirm response field name with dev team
		}
			.pause(minThinkTime, maxThinkTime)
			.exec(recordDraft("CUIDraftClaimsDB.csv", "claimantEmailAddress", "userId", "draftId"))


	val getUserId =
		group("CUIR2_Claimant_GetUser") {
			exec(http("CUIR2_Claimant_GetUser")
				.get(IdamAPIURL + "/o/userinfo")
				.header("Authorization", "Bearer #{bearerToken}")
				.header("Content-Type", "application/json")
				.check(jsonPath("$.uid").saveAs("userId"))
				.check(status.is(200)))
		}


	val deleteClaimantUser =
		group("CUIR2_Delete_User") {
			exec(http("CUIR2_Delete_User")
				.delete(IdamAPIURL + "/testing-support/accounts/#{claimantEmailAddress}")
				.header("Content-Type", "application/json")
				.check(status.is(204)))
		}

	val deleteDefendantUser =
		group("CUIR2_Delete_User") {
			exec(http("CUIR2_Delete_User")
				.delete(IdamAPIURL + "/testing-support/accounts/#{defEmailAddress}")
				.header("Content-Type", "application/json")
				.check(status.is(204)))
		}

}
