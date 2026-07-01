package uk.gov.hmcts.reform.civildamage.performance.scenarios

import java.io.{BufferedWriter, FileWriter}

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.{Common, Environment}

object CreateUser {

  val IdamAPIURL = Environment.idamAPIURL

  // Guaranteed-unique per virtual user. A 5-char random suffix (52^5) collides as accounts
  // accumulate across nightly runs, so two VUs can be minted the same email: the second gets a
  // 409 from IDAM and both journeys end up on the SAME claimant account, whose single draft
  // (keyed by userId in civil-citizen-ui) they then submit concurrently -> the shared-draft race
  // (DTSCCI-5716). A UUID removes any collision, so every VU has its own account and draft.
  private def uniqueSuffix() = java.util.UUID.randomUUID().toString.replace("-", "")

  val newUserFeeder = Iterator.continually(Map(
    "claimantEmailAddress" -> ("cuiimtclaimantuser" + uniqueSuffix() + "@gmail.com"),
    "password" -> "Password12!",
    "role" -> "citizen"
  ))

  val newUserFeederDef = Iterator.continually(Map(
    "defEmailAddress" -> ("cuiimtdefuser" + uniqueSuffix() + "@gmail.com"),
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
