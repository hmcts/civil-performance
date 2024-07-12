package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{AssignCase_Header, Environment}

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
    .formParam("username", "#{defEmail}")
    .formParam("password", "Password12!")
    .formParam("client_id", "civil_citizen_ui")
    // .formParam("client_secret", clientSecret)
    .formParam("client_secret", "47js6e86Wv5718D2O77OL466020731ii")
    .formParam("scope", "profile roles openid")
    .header("Content-Type", "application/x-www-form-urlencoded")
    .check(jsonPath("$.access_token").saveAs("bearerToken")))
    
    .pause(minThinkTime, maxThinkTime)
  
  //def run(implicit postHeaders: Map[String, String]): ChainBuilder = {
  val run =
    group("CIVIL_AssignCase_000_AssignCase") {
      //feed(caseFeeder)
      exec(http("CIVIL_AssignCase_000_AssignCase")
        .post("http://civil-service-perftest.service.core-compute-perftest.internal/testing-support/assign-case/#{caseId}/RESPONDENTSOLICITORONE")
        .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJraWQiOiI4cDJpajg2S0pTeENKeGcveUovV2w3TjcxMXM9IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJobWN0cy5jaXZpbCtvcmdhbmlzYXRpb24uMy5zb2xpY2l0b3IuMUBnbWFpbC5jb20iLCJjdHMiOiJPQVVUSDJfU1RBVEVMRVNTX0dSQU5UIiwiYXV0aF9sZXZlbCI6MCwiYXVkaXRUcmFja2luZ0lkIjoiNGJjOWEzOTAtZTZjZi00YTQxLWI1NGYtZDA2NmZjNDc0ODY3LTEyNDcwMjk4NiIsInN1Ym5hbWUiOiJobWN0cy5jaXZpbCtvcmdhbmlzYXRpb24uMy5zb2xpY2l0b3IuMUBnbWFpbC5jb20iLCJpc3MiOiJodHRwczovL2Zvcmdlcm9jay1hbS5zZXJ2aWNlLmNvcmUtY29tcHV0ZS1pZGFtLXBlcmZ0ZXN0LmludGVybmFsOjg0NDMvb3BlbmFtL29hdXRoMi9yZWFsbXMvcm9vdC9yZWFsbXMvaG1jdHMiLCJ0b2tlbk5hbWUiOiJhY2Nlc3NfdG9rZW4iLCJ0b2tlbl90eXBlIjoiQmVhcmVyIiwiYXV0aEdyYW50SWQiOiJwOUlvNUMzWEp4Vk82NWdQcGFzSUtRQ0JNNkEiLCJub25jZSI6Ims4US1kNldFYWtMc0swR0t6RkViWGE0Z1YzWWFUSEtuaUQtV2VrZENjNjgiLCJhdWQiOiJ4dWl3ZWJhcHAiLCJuYmYiOjE2OTQ1MzQ2NTEsImdyYW50X3R5cGUiOiJhdXRob3JpemF0aW9uX2NvZGUiLCJzY29wZSI6WyJvcGVuaWQiLCJwcm9maWxlIiwicm9sZXMiLCJjcmVhdGUtdXNlciIsIm1hbmFnZS11c2VyIiwic2VhcmNoLXVzZXIiXSwiYXV0aF90aW1lIjoxNjk0NTM0NjQ2LCJyZWFsbSI6Ii9obWN0cyIsImV4cCI6MTY5NDU2MzQ1MSwiaWF0IjoxNjk0NTM0NjUxLCJleHBpcmVzX2luIjoyODgwMCwianRpIjoiS2M3OWZISlZwbnMwVjAwUktGT004NGVSZFZBIn0.KMXwOtzsfbTOfnMSWCFXo7VgL5Dw6Bp-tHOxVidQ3Ivzqx_EiwnIRg6DOn78zfA3yMZXULPK26x5kRDDpWOBwrfvO0MXc6O5tpdw26i_aGDq13kOWbs3At8RHllNK-GmuXhcOD961Cj-qRpAxmmLegJxQHYheH9QAymJc8QL96Xil8vR6MCZNCsn9LqBnytBGP3ADX0HQe5_lmbkiM6MkxOIVB19WrXwikJqSV7ZmkPp6HZg0uVf_DYxsHHfg1dmZj16azXKinPil709VAQuETmClmh9xcnr9ckDezBhhiFlqaRtcWe6a__3VE9hrumDCsi0tVtBR-StRANEzoWTEA")
        .header("Content-Type", "application/json")
        .header("Accept", "*/*")
        .check(status.in(200, 201))
      )
    }
      .pause(minThinkTime, maxThinkTime)
      
      .exec { session =>
        val fw = new BufferedWriter(new FileWriter("FinalcaseIds.csv", true))
        try {
          fw.write(session("caseId").as[String] + "\r\n")
        } finally fw.close()
        session
      }
  
  
  val cuiassign =
    group("CIVIL_AssignCase_000_AssignCase") {
      //	feed(caseFeeder)
      exec(Auth)
        .exec(http("CIVIL_AssignCase_000_AssignCase")
          .post("http://civil-service-perftest.service.core-compute-perftest.internal/testing-support/assign-case/#{caseId}/DEFENDANT")
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
     /* .exec { session =>
        val fw = new BufferedWriter(new FileWriter("CUIDefUserDetails.csv", true))
        try {
          fw.write(session("claimantEmailAddress").as[String] + "," + session("defEmailAddress").as[String] + "," + session("password").as[String] + "," + session("claimNumber").as[String] + "\r\n")
        } finally fw.close()
        session
      }*/
  
  
}