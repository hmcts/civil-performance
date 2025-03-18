package uk.gov.hmcts.reform.civildamage.performance.scenarios

import java.io.{BufferedWriter, FileWriter}

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.{Common, Environment}

object CreateUser {

  val IdamAPIURL = Environment.idamAPIURL

  val newUserFeeder = Iterator.continually(Map(
    "claimantEmailAddress" -> ("cuiimtclaimantuser" + Common.randomString(5) + "@gmail.com"),
    "password" -> "Password12!",
    "role" -> "citizen"
  ))
  
  val newUserFeederDef = Iterator.continually(Map(
    "defEmailAddress" -> ("cuiimtdefuser" + Common.randomString(5) + "@gmail.com"),
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
      .exec { session =>
        val fw = new BufferedWriter(new FileWriter("CUIClaimUsers.csv", true))
        try {
          fw.write(session("claimantEmailAddress").as[String] + "\r\n")
        } finally fw.close()
        session
      }
  
  val CreateDefCitizen =
    feed(newUserFeederDef)
      .group("CUIR2_Defendant_CreateUser") {
        exec(http("CUIR2_Defendant_CreateUser")
          .post(IdamAPIURL + "/testing-support/accounts")
          .body(ElFileBody("CreateUserTemplateDef.json")).asJson
          .check(status.is(201)))
      }
      .exec { session =>
        val fw = new BufferedWriter(new FileWriter("CUIDefUsers.csv", true))
        try {
          fw.write(session("defEmailAddress").as[String]  + "\r\n")
        } finally fw.close()
        session
      }
  
  
  
    
    
    

}
