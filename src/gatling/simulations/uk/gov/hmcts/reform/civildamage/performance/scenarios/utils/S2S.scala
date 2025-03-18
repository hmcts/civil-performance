package uk.gov.hmcts.reform.civildamage.performance.scenarios.utils

import com.typesafe.config.{Config, ConfigFactory}
import io.gatling.core.Predef._
import io.gatling.http.Predef._

object S2S {

  val config: Config = ConfigFactory.load()

  //microservice is a string defined in the Simulation and passed into the body below
  def s2s() = {

    exec(http("GetS2SToken")
      .post(Environment.s2sUrl + "/testing-support/lease")
      .header("Content-Type", "application/json")
      .body(StringBody("""{"microservice":"civil_service"}""")).asJson
      .check(regex("(.+)").saveAs("ServiceToken")))
      .exitHereIfFailed
    
  
  
  }

  //microservice is a string defined in the Simulation and passed into the body below
  def s2sForCUIAPI() = {

    exec(http("GetS2STokenFor CUI API")
      .post(Environment.s2sUrl + "/testing-support/lease")
      .header("Content-Type", "application/json")
      .body(StringBody(s"""{"microservice":"civil_service"}"""))
      .check(bodyString.saveAs(s"civil_service_ServiceToken")))
      .exitHereIfFailed

  }
}