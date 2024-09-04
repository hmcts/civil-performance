package uk.gov.hmcts.reform.civildamage.performance.scenarios

import java.io.{BufferedWriter, FileWriter}

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.{Common, Environment}

object CreateUser {

  val IdamAPIURL = Environment.idamAPIURL

  val newUserFeeder = Iterator.continually(Map(
    "claimantEmailAddress" -> ("cuicpclaimantuser" + Common.randomString(5) + "@gmail.com"),
    "password" -> "Password12!",
    "role" -> "citizen"
  ))
  
  val newUserFeederDef = Iterator.continually(Map(
    "defEmailAddress" -> ("cuicpdefuser" + Common.randomString(5) + "@gmail.com"),
    "password" -> "Password12!",
    "role" -> "citizen"
  ))

  //takes an userType e.g. petitioner/respondent, to create unique users for each user
  val CreateClaimantCitizen =
    feed(newUserFeeder)
      .group("CUIR2_Claimant_CreateCitizen") {
        exec(http("CUIR2_Claimant_CreateCitizen")
          .post(IdamAPIURL + "/testing-support/accounts")
          .body(ElFileBody("CreateUserTemplate.json")).asJson
          .check(status.is(201)))
      }
  
  
  val CreateDefCitizen =
    feed(newUserFeederDef)
      .group("CUIR2_Defendant_CreateUser") {
        exec(http("CUIR2_Defendant_CreateUser")
          .post(IdamAPIURL + "/testing-support/accounts")
          .body(ElFileBody("CreateUserTemplateDef.json")).asJson
          .check(status.is(201)))
      }
      
    
    
    

}
