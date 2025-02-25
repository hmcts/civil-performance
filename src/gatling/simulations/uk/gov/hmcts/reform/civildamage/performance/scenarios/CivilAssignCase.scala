package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{Environment, S2S}

import java.io.{BufferedWriter, FileWriter}

object CivilAssignCase {
  
  val minThinkTime = Environment.minThinkTime
  val maxThinkTime = Environment.maxThinkTime
  
  val manageOrgURL = Environment.manageOrgURL
  val idamURL = Environment.idamURL
  val caseFeeder = csv("caseIdsForAssign.csv").circular
  
  /*======================================================================================
*Business process : As part of the create FR respondent application share a case
* Below group contains all the share the unassigned case
======================================================================================*/
  //userType must be "Caseworker", "Legal" or "Citizen"
  val Auth =
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
  
  val cuiassign =
    group("CIVIL_AssignCase_000_AssignCase") {
      exec(Auth)
      .exec(http("CIVIL_AssignCase_000_AssignCase")
        .post("http://civil-service-perftest.service.core-compute-perftest.internal/testing-support/assign-case/#{caseId}/DEFENDANT")
        .header("Authorization", "Bearer ${bearerToken}")
        .header("Content-Type", "application/json")
        .header("Accept", "*/*")
        .check(status.in(200, 201)))
    }

  //Updating the respondent deadline so that the default judgment journey becomes available
  val UpdateDeadlineDefaultJudgment =
    exec(S2S.s2s("civil_service"))
    .exec(http("Civil_000_GetBearerToken")
      .post(idamURL + "/o/token")
      .formParam("grant_type", "password")
      .formParam("username", "#{claimantuser}")
      .formParam("password", "Password12!")
      .formParam("client_id", "civil_citizen_ui")
      .formParam("client_secret", "47js6e86Wv5718D2O77OL466020731ii")
      .formParam("scope", "profile roles openid")
      .header("Content-Type", "application/x-www-form-urlencoded")
      .check(jsonPath("$.access_token").saveAs("claimToken")))

    .exec(http("API_Civil_UpdateDeadline")
      .put("http://civil-service-perftest.service.core-compute-perftest.internal/testing-support/case/#{caseId}")
      .header("Authorization", "Bearer #{claimToken}")
      .header("ServiceAuthorization", "#{civil_serviceBearerToken}")
      .header("Content-type", "application/json")
      .body(StringBody("""{"respondent1ResponseDeadline":"2024-02-13T15:59:50"}""")))
    .pause(15)

  //Updating the respondent payment date so that the judgment by admission journey becomes available
  val UpdatePaymentDateForRespondentJO =
    exec(S2S.s2s("civil_service"))
    .exec(http("Civil_000_GetBearerToken")
      .post(idamURL + "/o/token")
      .formParam("grant_type", "password")
      .formParam("username", "#{claimantuser}")
      .formParam("password", "Password12!")
      .formParam("client_id", "civil_citizen_ui")
      .formParam("client_secret", "47js6e86Wv5718D2O77OL466020731ii")
      .formParam("scope", "profile roles openid")
      .header("Content-Type", "application/x-www-form-urlencoded")
      .check(jsonPath("$.access_token").saveAs("claimToken")))

//    .exec(http("API_Civil_UpdateSubmittedDate")
//      .put("http://civil-service-perftest.service.core-compute-perftest.internal/testing-support/case/#{caseId}")
//      .header("Authorization", "Bearer #{claimToken}")
//      .header("ServiceAuthorization", "#{civil_serviceBearerToken}")
//      .header("Content-type", "application/json")
//      .body(StringBody("""{"whenWillThisAmountBePaid":"2025-02-13}"}""")))
//      .body(StringBody("""{"respondToClaimAdmitPartLRspec":"{whenWillThisAmountBePaid:2025-02-13}"}""")))
//      .body(StringBody("""{"whenToBePaidText":"13 February 2025"}""")))
//      .body(StringBody("""{"respondent1ResponseDate":"2025-02-13T15:10:30"}""")))

    .exec(http("API_Civil_UpdateSubmittedDate")
      .get("http://civil-service-perftest.service.core-compute-perftest.internal/testing-support/case/#{caseId}")
      .header("Authorization", "Bearer #{claimToken}")
      .header("ServiceAuthorization", "#{civil_serviceBearerToken}")
      .header("Content-type", "application/json")
      .check(bodyString.saveAs("caseDetails")))
    .exec { session =>
      println(session("caseDetails").as[String])
      session
    }
//    .pause(15)


  //Deepak - Cases that make the final step
     /* .exec { session =>
        val fw = new BufferedWriter(new FileWriter("CUIDefUserDetails.csv", true))
        try {
          fw.write(session("claimantEmailAddress").as[String] + "," + session("defEmailAddress").as[String] + "," + session("password").as[String] + "," + session("claimNumber").as[String] + "\r\n")
        } finally fw.close()
        session
      }*/
  
  
}