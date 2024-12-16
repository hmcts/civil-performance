package scenarios

import com.typesafe.config.{Config, ConfigFactory}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.Environment

object CivilAssignCase {

  val config: Config = ConfigFactory.load()
  val minThinkTime = Environment.minThinkTime
  val maxThinkTime = Environment.maxThinkTime
  val idamURL = Environment.idamURL
  val civilSecret = config.getString("auth.civilUiSecret")

  //userType must be "Caseworker", "Legal" or "Citizen"
  val Auth =
  
    exec(http("Civil_000_GetBearerToken")
      .post(idamURL + "/o/token") //change this to idamapiurl if this not works
      .formParam("grant_type", "password")
      .formParam("username", "#{defEmailAddress}")
      .formParam("password", "Password12!")
      .formParam("client_id", "civil_citizen_ui")
      .formParam("client_secret", civilSecret)
      .formParam("scope", "profile roles openid")
      .header("Content-Type", "application/x-www-form-urlencoded")
      .check(jsonPath("$.access_token").saveAs("bearerToken")))

    .pause(minThinkTime, maxThinkTime)

  val cuiassign =

    group("CIVIL_AssignCase_000_AssignCase") {
      exec(Auth)
        .exec(http("CIVIL_AssignCase_000_AssignCase")
          .post("http://civil-service-perftest.service.core-compute-perftest.internal/testing-support/assign-case/#{claimNumber}/DEFENDANT")
          .header("Authorization", "Bearer ${bearerToken}")
          .header("Content-Type", "application/json")
          .header("Accept", "*/*")
          .check(status.in(200, 201)))
    }

    .pause(minThinkTime, maxThinkTime)
}