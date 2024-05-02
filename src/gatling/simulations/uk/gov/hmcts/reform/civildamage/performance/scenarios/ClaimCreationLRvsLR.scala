
package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.{CivilDamagesHeader, Common, Environment}

import java.io.{BufferedWriter, FileWriter}

object ClaimCreationLRvsLR {

  val BaseURL = Environment.baseURL
  val IdAMURL = Environment.idamURL
  val IdamUrl = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val caseFeeder=csv("caseIds.csv").circular

  /*======================================================================================
             * Click On pay from Service Page
  ==========================================================================================*/
  val run=


    exec(_.setAll(
      "Idempotencynumber" -> (Common.getIdempotency()),
      "LRrandomString" -> Common.randomString(5))
    )
  //val createclaim =

    //val startCreateClaim =
    /*======================================================================================
                 * Create Civil Claim - Start the case
      ==========================================================================================*/
    .group("Civil_CreateClaim_040_StartCreateCase1") {
      exec(http("Civil_CreateClaim_040_StartCreateCase1")
        .get("/data/internal/case-types/CIVIL/event-triggers/CREATE_CLAIM?ignore-warning=false")
        .headers(CivilDamagesHeader.headers_140)
        .check(substring("Create claim - Unspecified"))
        .check(status.is(200))
        .check(jsonPath("$.event_token").optional.saveAs("event_token"))
      )
        .pause(MinThinkTime, MaxThinkTime)
    }

    //val claimliability =
    /*======================================================================================
                 * Create Civil Claim - Claim Eligibility
      ==========================================================================================*/
    .group("Civil_CreateClaim_050_CLAIMEligibility") {
      exec(http("Civil_CreateClaim_050_Eligibility")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMEligibility")
        .headers(CivilDamagesHeader.headers_163)
        .body(ElFileBody("bodies/0013_request.json"))
        .check(substring("courtLocation"))
        .check(status.in(200, 304))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)

    //val claimreferences =



    /*======================================================================================
                 * Create Civil Claim - Claim Of Solicitor Org
      ==========================================================================================*/
    // val claimdefsolicitororgemail =
    .group("Civil_CreateClaim_230_CLAIMDefendantSolicitorEmail") {
      exec(http("Civil_CreateClaim_230_SolEmail")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMDefendantSolicitorEmail")
        .headers(CivilDamagesHeader.headers_491)
        .body(ElFileBody("bodies/0076_request.json"))
        .check(substring("CREATE_CLAIMDefendantSolicitorEmail"))
        .check(status.in(200, 304))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
    /*======================================================================================
                 * Create Civil Claim - Add another defendent
      ==========================================================================================*/


    /*======================================================================================
                 * Create Civil Claim - PBA Number
      ==========================================================================================*/
    // val createclaimpbanumber =
    .group("Civil_CreateClaim_300_CLAIMPbaNumber") {
      exec(http("Civil_CreateClaim_300_PBANumber")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMPbaNumber")
        .headers(CivilDamagesHeader.headers_610)
        .body(ElFileBody("bodies/0104_request.json"))
        .check(substring("CREATE_CLAIMPbaNumber"))
        .check(status.in(200, 304))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
    /*======================================================================================
                 * Create Civil Claim - SOT
      ==========================================================================================*/
    //val createclaimstatementoftruth =
    .group("Civil_CreateClaim_310_CLAIMStatementOfTruth") {
      exec(http("Civil_CreateClaim_310_SOT")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMStatementOfTruth")
        .headers(CivilDamagesHeader.headers_650)
        .body(ElFileBody("bodies/0108_request.json"))
        .check(substring("CREATE_CLAIMStatementOfTruth"))
        .check(status.in(200, 304))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
    /*======================================================================================
                 * Create Civil Claim - Submit Claim
      ==========================================================================================*/
    // val submitclaimevent =
    .group("Civil_CreateClaim_320_SubmitClaim") {

      exec(http("Civil_CreateClaim_320_005_Submit")
        .post("/data/case-types/CIVIL/cases?ignore-warning=false")
        .headers(CivilDamagesHeader.headers_672)
        .body(ElFileBody("bodies/0112_request.json"))
        .check(jsonPath("$.event_token").optional.saveAs("event_token_claimcreate"))
        .check(jsonPath("$.id").optional.saveAs("caseId"))
        .check(jsonPath("$.legacyCaseReference").optional.saveAs("claimNumber"))
        .check(substring("created_on")))

    }
    .pause(MinThinkTime, MaxThinkTime)
    .pause(50)
    /*======================================================================================
                 * Create Civil Claim - Back To Search pages
      ==========================================================================================*/
    //  val getcasedetailspage=
    .group("Civil_CreateClaim_330_BackToCaseDetailsPage") {
      exec(http("Civil_CreateClaim_330_005_CaseDetails")
        .get("/data/internal/cases/#{caseId}")
        .headers(CivilDamagesHeader.headers_717)
        .check(substring("Civil"))
        .check(status.in(200,201,304))
      )


    }
    .pause(MinThinkTime, MaxThinkTime)
  
  val civilAddPayment =
    
    exec(http("PaymentAPI_GetCasePaymentOrders")
      .get("http://payment-api-perftest.service.core-compute-perftest.internal/case-payment-orders?case_ids=#{caseId}")
      //.header("Authorization", "Bearer #{access_tokenPayments}")
      .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJraWQiOiI4cDJpajg2S0pTeENKeGcveUovV2w3TjcxMXM9IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJjaXZpbC5kYW1hZ2VzLmNsYWltcytvcmdhbmlzYXRpb24uMS5zb2xpY2l0b3IuMUBnbWFpbC5jb20iLCJjdHMiOiJPQVVUSDJfU1RBVEVMRVNTX0dSQU5UIiwiYXV0aF9sZXZlbCI6MCwiYXVkaXRUcmFja2luZ0lkIjoiNGJjOWEzOTAtZTZjZi00YTQxLWI1NGYtZDA2NmZjNDc0ODY3LTM2NTAzNzQ3NiIsInN1Ym5hbWUiOiJjaXZpbC5kYW1hZ2VzLmNsYWltcytvcmdhbmlzYXRpb24uMS5zb2xpY2l0b3IuMUBnbWFpbC5jb20iLCJpc3MiOiJodHRwczovL2Zvcmdlcm9jay1hbS5zZXJ2aWNlLmNvcmUtY29tcHV0ZS1pZGFtLXBlcmZ0ZXN0LmludGVybmFsOjg0NDMvb3BlbmFtL29hdXRoMi9yZWFsbXMvcm9vdC9yZWFsbXMvaG1jdHMiLCJ0b2tlbk5hbWUiOiJhY2Nlc3NfdG9rZW4iLCJ0b2tlbl90eXBlIjoiQmVhcmVyIiwiYXV0aEdyYW50SWQiOiJtRmZzMVduOFFGS1hsd0h1elBTdktsWDlSczAiLCJub25jZSI6InVSNXpXX0RtbVdFMU1Vc1YzWERCbEZwTUZELU1STEM5cFR3ZmdwS1QyR28iLCJhdWQiOiJ4dWl3ZWJhcHAiLCJuYmYiOjE3MTI3OTg5MzYsImdyYW50X3R5cGUiOiJhdXRob3JpemF0aW9uX2NvZGUiLCJzY29wZSI6WyJvcGVuaWQiLCJwcm9maWxlIiwicm9sZXMiLCJjcmVhdGUtdXNlciIsIm1hbmFnZS11c2VyIiwic2VhcmNoLXVzZXIiXSwiYXV0aF90aW1lIjoxNzEyNzk4OTM2LCJyZWFsbSI6Ii9obWN0cyIsImV4cCI6MTcxMjgyNzczNiwiaWF0IjoxNzEyNzk4OTM2LCJleHBpcmVzX2luIjoyODgwMCwianRpIjoicW4xZzNyZkI1MTRoXzN6eUxUQ0tIQ3V1VG5JIn0.iA6bVpIf0SGKocdMIb43k8cYJfYrsjy_qBSO4uTfFRBXX8Hy-9wyKuu3t5fOjWmZecTFhGZBu0feAgwrPYGwPp1n7Z0uXcKGi1tiV0c22jlM2RhGva0xP4C85d7M-IM1sGKW0skd5Kwrme3fP8To_CDrjT3dfRNlTmIdhyov4EolyAWLxZHhQ3Bms8Z6Emd3RLJhrIz_5k2HdJl4Kt1XPNQh3vnLSGnnpp3DBp3-I2lC30C3wQJswa42boGcyc8OLC1NSX6qKUtAtINHoBCDEJGg0pXvCkHbAg2YqY9cbzekTiTtZzeIcdwvVdeKi0GLo6zUhZ45uj1HVwZ5vMHLYA")
      .header("ServiceAuthorization", "#{xui_webappBearerToken}")
      .header("Content-Type", "application/json")
      .header("accept", "*/*")
      .check(jsonPath("$.content[0].orderReference").saveAs("caseIdPaymentRef")))
  
      .pause(MinThinkTime, MaxThinkTime)
      
      .tryMax(2) {
        exec(http("API_Civil_AddPayment")
          .put("http://civil-service-#{env}.service.core-compute-#{env}.internal/service-request-update-claim-issued")
          .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJraWQiOiI4cDJpajg2S0pTeENKeGcveUovV2w3TjcxMXM9IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJjaXZpbC5kYW1hZ2VzLmNsYWltcytvcmdhbmlzYXRpb24uMS5zb2xpY2l0b3IuMUBnbWFpbC5jb20iLCJjdHMiOiJPQVVUSDJfU1RBVEVMRVNTX0dSQU5UIiwiYXV0aF9sZXZlbCI6MCwiYXVkaXRUcmFja2luZ0lkIjoiNGJjOWEzOTAtZTZjZi00YTQxLWI1NGYtZDA2NmZjNDc0ODY3LTM2NTAzNzQ3NiIsInN1Ym5hbWUiOiJjaXZpbC5kYW1hZ2VzLmNsYWltcytvcmdhbmlzYXRpb24uMS5zb2xpY2l0b3IuMUBnbWFpbC5jb20iLCJpc3MiOiJodHRwczovL2Zvcmdlcm9jay1hbS5zZXJ2aWNlLmNvcmUtY29tcHV0ZS1pZGFtLXBlcmZ0ZXN0LmludGVybmFsOjg0NDMvb3BlbmFtL29hdXRoMi9yZWFsbXMvcm9vdC9yZWFsbXMvaG1jdHMiLCJ0b2tlbk5hbWUiOiJhY2Nlc3NfdG9rZW4iLCJ0b2tlbl90eXBlIjoiQmVhcmVyIiwiYXV0aEdyYW50SWQiOiJtRmZzMVduOFFGS1hsd0h1elBTdktsWDlSczAiLCJub25jZSI6InVSNXpXX0RtbVdFMU1Vc1YzWERCbEZwTUZELU1STEM5cFR3ZmdwS1QyR28iLCJhdWQiOiJ4dWl3ZWJhcHAiLCJuYmYiOjE3MTI3OTg5MzYsImdyYW50X3R5cGUiOiJhdXRob3JpemF0aW9uX2NvZGUiLCJzY29wZSI6WyJvcGVuaWQiLCJwcm9maWxlIiwicm9sZXMiLCJjcmVhdGUtdXNlciIsIm1hbmFnZS11c2VyIiwic2VhcmNoLXVzZXIiXSwiYXV0aF90aW1lIjoxNzEyNzk4OTM2LCJyZWFsbSI6Ii9obWN0cyIsImV4cCI6MTcxMjgyNzczNiwiaWF0IjoxNzEyNzk4OTM2LCJleHBpcmVzX2luIjoyODgwMCwianRpIjoicW4xZzNyZkI1MTRoXzN6eUxUQ0tIQ3V1VG5JIn0.iA6bVpIf0SGKocdMIb43k8cYJfYrsjy_qBSO4uTfFRBXX8Hy-9wyKuu3t5fOjWmZecTFhGZBu0feAgwrPYGwPp1n7Z0uXcKGi1tiV0c22jlM2RhGva0xP4C85d7M-IM1sGKW0skd5Kwrme3fP8To_CDrjT3dfRNlTmIdhyov4EolyAWLxZHhQ3Bms8Z6Emd3RLJhrIz_5k2HdJl4Kt1XPNQh3vnLSGnnpp3DBp3-I2lC30C3wQJswa42boGcyc8OLC1NSX6qKUtAtINHoBCDEJGg0pXvCkHbAg2YqY9cbzekTiTtZzeIcdwvVdeKi0GLo6zUhZ45uj1HVwZ5vMHLYA")
          .header("ServiceAuthorization", "#{civil_serviceBearerToken}")
          .header("Content-type", "application/json")
          .body(ElFileBody("bodies/AddPayment.json")))
      }
  
      .pause(MinThinkTime, MaxThinkTime)
      
      val addPBAPayment=
    /*======================================================================================
                 * Create Civil Claim - Click pay
      ==========================================================================================*/
    // payment fee
    group("Civil_CreateClaim_340_ClickPay") {
      exec(http("Civil_CreateClaim_340_005_ClickPay")
        .get( "/pay-bulkscan/cases/#{caseId}")
        .headers(CivilDamagesHeader.headers_717)
        .check(status.in(200, 304,201))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
    /*======================================================================================
                 * Create Civil Claim - PBA Payment groups
      ==========================================================================================*/
    // payment pba fee payment
    .group("Civil_CreateClaim_350_PaymentGroups") {
      exec(http("Civil_CreateClaim_350_005_SelectPayGroups")
        .get("/payments/cases/#{caseId}/paymentgroups")
        .headers(CivilDamagesHeader.headers_717)
        .check(status.in(200, 304))
      )
        .exec(http("Civil_CreateClaim_350_010_PaymentOrder")
          .get("/payments/case-payment-orders?case_ids=#{caseId}")
          .headers(CivilDamagesHeader.headers_717)
          .check(jsonPath("$.content[0].orderReference").optional.saveAs("serviceRef"))
          .check(status.in(200, 304))
        )

    }
    .pause(MinThinkTime, MaxThinkTime)
    /*======================================================================================
                 * Select PBA
      ==========================================================================================*/
    //pba account selection and pay

    .group("Civil_CreateClaim_360_PBAAccounts") {
      exec(http("Civil_CreateClaim_360_005_SelectPBA")
        .get("/payments/pba-accounts")
        .headers(CivilDamagesHeader.headers_717)
        .check(substring("organisationEntityResponse"))
        .check(status.in(200,204,304))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
    /*======================================================================================
                 * Create Civil Claim - Submit Payment
      ==========================================================================================*/
    //pba payment final

    .group("Civil_CreateClaim_330_SubmitPayment") {
      exec(http("Civil_CreateClaim_330_005_SubmitPayment")
        .post("/payments/service-request/#{serviceRef}/pba-payments")
        .headers(CivilDamagesHeader.headers_650)
        .body(ElFileBody("bodies/0137_request.json"))
        .check(substring("success"))
        .check(status.in(200,201,304))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
    .pause(80)
      
      val notifyClaim=
    /*======================================================================================
                 * Create Civil Claim - Notify
      ==========================================================================================*/
    //notify claim step

    group("Civil_CreateClaim_340_NotifyClaimEvent") {
      exec(http("Civil_CreateClaim_340_005_Notify")
        .get("/workallocation/case/tasks/#{caseId}/event/NOTIFY_DEFENDANT_OF_CLAIM/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(CivilDamagesHeader.headers_769)
        .check(status.in(200, 304))

      )

      exec(http("Civil_CreateClaim_340_010_Notify")
        .get("/data/internal/cases/#{caseId}/event-triggers/NOTIFY_DEFENDANT_OF_CLAIM?ignore-warning=false")
        .headers(CivilDamagesHeader.headers_notify)
        //.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
        .check(substring("NOTIFY_DEFENDANT_OF_CLAIM"))
        .check(status.in(200,201, 304))
        .check(jsonPath("$.event_token").optional.saveAs("event_token_notifyclaimtodef"))
      )


    }
    .pause(MinThinkTime, MaxThinkTime)
    /*======================================================================================
                 * Create Civil Claim - Notify Continue
      ==========================================================================================*/
    // val claimnotifyeventcontinue =
    .group("Civil_CreateClaim_350_CLAIMAccessGrantedWarning") {
      exec(http("Civil_CreateClaim_350_005_grant")
        .post("/data/case-types/CIVIL/validate?pageId=NOTIFY_DEFENDANT_OF_CLAIMAccessGrantedWarning")
        .headers(CivilDamagesHeader.headers_783)
        .body(ElFileBody("bodies/0187_request.json"))
        .check(substring("NOTIFY_DEFENDANT_OF_CLAIMAccessGrantedWarning"))
        .check(status.in(200, 304))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
    .pause(50)
    /*======================================================================================
                 * Submit Notify
      ==========================================================================================*/
    // val claimnotifyeventsubmit =
    .group("Civil_CreateClaim_360_ClaimNotifyEventSubmit") {
      exec(http("Civil_CreateClaim_360_ClaimNotifyEventSubmit")
        .post("/data/cases/#{caseId}/events")
        .headers(CivilDamagesHeader.headers_803)
        .body(ElFileBody("bodies/0191_request.json"))
        .check(substring("Notification of claim sent"))
        .check(status.in(200, 201))
      ).exitHereIfFailed
    }
    //.exec(session => session.set("caseId", "#{caseId}"))
    .pause(MinThinkTime, MaxThinkTime)
    /*======================================================================================
                 * Create Civil Claim - Back To case Details  after Notify
      ==========================================================================================*/
    //val backtocasedetailsafterclaimnotify =

    .group("Civil_CreateClaim_370_CasedetailsAfterClaimNotify") {

      exec(http("Civil_AfterClaimNotify_370_010")
        .get("/data/internal/cases/#{caseId}")
        .headers(CivilDamagesHeader.headers_717)
        .check(substring("Notify claim"))
        .check(status.in(200, 201))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
    .pause(50)

    //end of  claim notify
    /*======================================================================================
                 * Create Civil Claim - Notify Details
      ==========================================================================================*/
    //beginning of notify details

    .group("CD_CreateClaim_380_NotifyDetailsEvent") {

      exec(http("Civil_CreateClaim_380_010_NotifyDetailCreate")
        .get("/data/internal/cases/#{caseId}/event-triggers/NOTIFY_DEFENDANT_OF_CLAIM_DETAILS?ignore-warning=false")
        .headers(CivilDamagesHeader.headers_notify)
        .check(substring("NOTIFY_DEFENDANT_OF_CLAIM_DETAILS"))
        //.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
        .check(status.in(200, 304))
        .check(jsonPath("$.event_token").optional.saveAs("event_token_notifyclaimdetail"))
      )



    }
    .pause(MinThinkTime, MaxThinkTime)
    /*======================================================================================
                 * Create Civil Claim - Detals Upload
      ==========================================================================================*/


    /*======================================================================================
                 * Create Civil Claim - event submit for notify detail
      ==========================================================================================*/
    // val notifyclaimdetailseventsubmit=
    .group("CD_CreateClaim_400_NotifyDetailsEventSubmit") {
      exec(http("CD_CreateClaim_370_EventSubmit")
        .post("/data/cases/#{caseId}/events")
        .headers(CivilDamagesHeader.headers_886)
        // .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
        .body(ElFileBody("bodies/0214_request.json"))
        .check(substring("organisation has been notified of the claim details"))
        .check(status.in(200, 201))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
                 * Create Civil Claim - Notify Details
      ==========================================================================================*/
    // val returntocasedetailsafternotifydetails =
    .group("CD_CreateClaim_410_ReturnToCaseDetailsAfterNotifyDetails") {
      exec(http("CD_CreateClaim_410_005_NotifyDetails")
        .post("/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
        .headers(CivilDamagesHeader.headers_894)
        .body(StringBody("{\"searchRequest\":{\"ccdId\":\"#{caseId}\",\"eventId\":\"NOTIFY_DEFENDANT_OF_CLAIM_DETAILS\",\"jurisdiction\":\"CIVIL\",\"caseTypeId\":\"UNSPECIFIED_CLAIMS\"}}"))
        .check(status.in(200,204,201,304))
      )

        /*.exec { session =>
          val fw = new BufferedWriter(new FileWriter("solicitor9.csv", true))
          try {
            fw.write(session("caseId").as[String] + "\r\n")
          } finally fw.close()
          session
        }*/

    }
    .pause(MinThinkTime, MaxThinkTime)


  val RespondToClaim =
    
    group("Civil_CreateClaim_330_BackToCaseDetailsPage") {
      exec(http("Civil_CreateClaim_330_005_CaseDetails")
        .get("https://manage-case.perftest.platform.hmcts.net/data/internal/cases/#{caseId}")
        .headers(CivilDamagesHeader.headers_717)
        .check(substring("Civil"))
        .check(status.in(200,201,304)))


        .exec(_.setAll(
          "Idempotencynumber" -> (Common.getIdempotency()),
          "LRrandomString" -> Common.randomString(5)
        ))


    }
      /*======================================================================================
           * Create Civil Claim - Start Event 'Respond to Claim'
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("XUI_CreateClaim_420_RespondToClaim") {
        exec(http("XUI_CreateClaim_420_005_RespondToClaim")
          .get("/workallocation/case/tasks/#{caseId}/event/DEFENDANT_RESPONSE/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .check(substring("task_required_for_event"))
        )

          .exec(http("XUI_CreateClaim_420_010_RespondToClaim")
            .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/DEFENDANT_RESPONSE?ignore-warning=false")
            .headers(CivilDamagesHeader.headers_notify)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
            .check(substring("DEFENDANT_RESPONSE"))
            .check(jsonPath("$.case_fields[75].value.partyID").saveAs("repPartyID"))
            .check(jsonPath("$.case_fields[75].value.partyName").saveAs("partyName"))
            .check(jsonPath("$.event_token").saveAs("event_token"))
          )
          .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))

      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
   * Create Civil Claim - Respond to Claim Defendant Details
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("XUI_CreateClaim_430_RespondDefDetails") {
        exec(http("XUI_CreateClaim_430_005_RespondDefDetails")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEConfirmDetails")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("x-xsrf-token", "#{XSRFToken}")
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/LRvsLR/RespondDefDetails.json"))
          .check(substring("RESPONDENTSOLICITORONE"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
* Create Civil Claim - Respond to Claim Defendant Choice - Reject
==========================================================================================*/

      .group("XUI_CreateClaim_440_RespondDefChoice") {
        exec(http("XUI_CreateClaim_440_005_RespondDefChoice")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSERespondentResponseType")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("x-xsrf-token", "#{XSRFToken}")
          .body(ElFileBody("bodies/LRvsLR/RespondDefChoice.json"))
          .check(substring("RESPONDENTSOLICITORONE"))
          //change when you get around to it
        )
      }
      .pause(MinThinkTime, MaxThinkTime)

      /*======================================================================================
* Create Civil Claim - Your File Reference
==========================================================================================*/

      .group("XUI_CreateClaim_450_YourFileReference") {
        exec(http("XUI_CreateClaim_450_005_YourFileReference")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSESolicitorReferences")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("x-xsrf-token", "#{XSRFToken}")
          .body(ElFileBody("bodies/LRvsLR/YourFileReference.json"))
          .check(substring("respondentSolicitor1Reference"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* Create Civil Claim - Upload Defence
==========================================================================================*/

      .group("XUI_CreateClaim_460_UploadDefence") {
        exec(http("XUI_CreateClaim_460_005_UploadDefence")
          .post(BaseURL + "/documentsv2")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundary3qhEjdcm5ZAK4s7o")
          .header("sec-fetch-dest", "empty")
          .header("sec-fetch-mode", "cors")
          .bodyPart(RawFileBodyPart("files", "casesummary.docx")
            .fileName("casesummary.docx")
            .transferEncoding("binary"))
          .asMultipartForm
          .formParam("classification", "PUBLIC")
          .formParam("caseTypeId", "CIVIL")
          .formParam("jurisdictionId", "CIVIL")
          .check(jsonPath("$.documents[0].hashToken").saveAs("hashToken"))
          .check(jsonPath("$.documents[0]._links.self.href").saveAs("document_url"))
          .check(substring("casesummary.docx")))

      }
      .pause(MinThinkTime, MaxThinkTime)

      /*======================================================================================
* Create Civil Claim - Upload Defence Submit
==========================================================================================*/

      .group("XUI_CreateClaim_465_UploadDefenceSubmit") {
        exec(http("XUI_CreateClaim_465_005_UploadDefenceSubmit")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEUpload")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .header("x-xsrf-token", "#{XSRFToken}")
          .body(ElFileBody("bodies/LRvsLR/UploadDefence.json"))
          .check(substring("respondent1ClaimResponseDocument"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* Create Civil Claim -File directions questionnaire
==========================================================================================*/

      .group("XUI_CreateClaim_470_FileDirectionsQuestionnaire") {
        exec(http("XUI_CreateClaim_470_005_FileDirectionsQuestionnaire")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEFileDirectionsQuestionnaire")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("x-xsrf-token", "#{XSRFToken}")
          .body(ElFileBody("bodies/LRvsLR/FileDirectionsQuestionnaire.json"))
          .check(substring("respondent1DQFileDirectionsQuestionnaire"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* Create Civil Claim - Fixed Recoverable Costs
==========================================================================================*/

      .group("XUI_CreateClaim_480_FixedRecoverableCosts") {
        exec(http("XUI_CreateClaim_480_005_FixedRecoverableCosts")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEFixedRecoverableCosts")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("x-xsrf-token", "#{XSRFToken}")
          .body(ElFileBody("bodies/LRvsLR/FixedRecoverableCosts.json"))
          .check(substring("respondent1DQFixedRecoverableCosts"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* Create Civil Claim - Disclosure of non-electronic documents
==========================================================================================*/

      .group("XUI_CreateClaim_490_DisclosureNonElectDocuments") {
        exec(http("XUI_CreateClaim_490_005_DisclosureNonElectDocuments")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEDisclosureOfNonElectronicDocuments")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("x-xsrf-token", "#{XSRFToken}")
          .body(ElFileBody("bodies/LRvsLR/DisclosureNonElectDocuments.json"))
          .check(substring("respondent1DQDisclosureOfNonElectronicDocuments"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
* Create Civil Claim - Do you want to use an expert?
==========================================================================================*/

      .group("XUI_CreateClaim_500_UseAnExpert") {
        exec(http("XUI_CreateClaim_500_005_UseAnExpert")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEExperts")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("x-xsrf-token", "#{XSRFToken}")
          .body(ElFileBody("bodies/LRvsLR/UseAnExpert.json"))
          .check(substring("expertRequired"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
* Create Civil Claim - Are there any witnesses who should attend the hearing?
==========================================================================================*/

      .group("XUI_CreateClaim_510_AnyWitnesses") {
        exec(http("XUI_CreateClaim_510_005_AnyWitnesses")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEWitnesses")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("x-xsrf-token", "#{XSRFToken}")
          .body(ElFileBody("bodies/LRvsLR/WitnessesToAppear.json"))
          .check(substring("witnessesToAppear"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
* Create Civil Claim - Welsh language
==========================================================================================*/

      .group("XUI_CreateClaim_520_WelshLanguage") {
        exec(http("XUI_CreateClaim_520_005_WelshLanguage")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSELanguage")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("x-xsrf-token", "#{XSRFToken}")
          .body(ElFileBody("bodies/LRvsLR/WelshLanguage.json"))
          .check(substring("respondent1DQLanguage"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* Create Civil Claim - Hearing Availability
==========================================================================================*/

      .group("XUI_CreateClaim_530_HearingAvailability") {
        exec(http("XUI_CreateClaim_530_005_HearingAvailability")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEHearing")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("x-xsrf-token", "#{XSRFToken}")
          .body(ElFileBody("bodies/LRvsLR/HearingAvailability.json"))
          .check(substring("DEFENDANT_RESPONSEHearing"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
* Create Civil Claim - Upload draft directions
==========================================================================================*/

      .group("XUI_CreateClaim_540_UploadDraftDirections") {
        exec(http("XUI_CreateClaim_540_005_UploadDraftDirections")
          .post(BaseURL + "/documentsv2")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundary3qhEjdcm5ZAK4s7o")
          .header("sec-fetch-dest", "empty")
          .header("sec-fetch-mode", "cors")
          .bodyPart(RawFileBodyPart("files", "casesummary.docx")
            .fileName("casesummary.docx")
            .transferEncoding("binary"))
          .asMultipartForm
          .formParam("classification", "PUBLIC")
          .formParam("caseTypeId", "CIVIL")
          .formParam("jurisdictionId", "CIVIL")
          .check(jsonPath("$.documents[0].hashToken").saveAs("hashTokenDraft"))
          .check(jsonPath("$.documents[0]._links.self.href").saveAs("draftDocument_url"))
          .check(substring("casesummary.docx")))
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
* Create Civil Claim - Upload draft directions Submit
==========================================================================================*/

      .group("XUI_CreateClaim_550_UploadDraftDirectionsSubmit") {
        exec(http("XUI_CreateClaim_550_005_UploadDraftDirectionsSubmit")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEDraftDirections")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("x-xsrf-token", "#{XSRFToken}")
          .body(ElFileBody("bodies/LRvsLR/UploadDraftDirections.json"))
          .check(substring("respondent1DQDraftDirections"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* Create Civil Claim - Court location code
==========================================================================================*/

      .group("XUI_CreateClaim_560_CourtLocationCode") {
        exec(http("XUI_CreateClaim_560_005_CourtLocationCode")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSERequestedCourt")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("x-xsrf-token", "#{XSRFToken}")
          .body(ElFileBody("bodies/LRvsLR/ResCourtLocationCode.json"))
          .check(substring("respondent1DQRequestedCourt"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      /*======================================================================================
* Create Civil Claim - Support with access needs
==========================================================================================*/

      .group("XUI_CreateClaim_570_SupportAccessNeeds") {
        exec(http("XUI_CreateClaim_570_005_SupportAccessNeeds")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEHearingSupport")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("x-xsrf-token", "#{XSRFToken}")
          .body(ElFileBody("bodies/LRvsLR/SupportAccessNeeds.json"))
          .check(substring("respondent1DQHearingSupport"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
* Create Civil Claim - Vulnerability Questions
==========================================================================================*/

      .group("XUI_CreateClaim_580_VulnerabilityQuestions") {
        exec(http("XUI_CreateClaim_580_005_VulnerabilityQuestions")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEVulnerabilityQuestions")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("x-xsrf-token", "#{XSRFToken}")
          .body(ElFileBody("bodies/LRvsLR/VulnerabilityQuestions.json"))
          .check(substring("respondent1DQVulnerabilityQuestions"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
* Create Civil Claim - Do you intend to make any applications in the future?
==========================================================================================*/

      .group("XUI_CreateClaim_590_AnyFutureApplications") {
        exec(http("XUI_CreateClaim_590_005_AnyFutureApplications")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEFurtherInformation")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("x-xsrf-token", "#{XSRFToken}")
          .body(ElFileBody("bodies/LRvsLR/AnyFutureApplications.json"))
          .check(substring("respondent1DQFurtherInformation"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
* Create Civil Claim - Respond to claim SoT
==========================================================================================*/

      .group("XUI_CreateClaim_600_RespondToClaimSoT") {
        exec(http("XUI_CreateClaim_600_005_RespondToClaimSoT")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEStatementOfTruth")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("x-xsrf-token", "#{XSRFToken}")
          .body(ElFileBody("bodies/LRvsLR/RespondToClaimSoT.json"))
          .check(substring("DEFENDANT_RESPONSEStatementOfTruth"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
* Create Civil Claim - Respond to claim Submit
==========================================================================================*/

      .group("XUI_CreateClaim_610_RespondToClaimSubmit") {
        exec(http("XUI_CreateClaim_610_005_RespondToClaimSubmit")
          .post("/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .body(ElFileBody("bodies/LRvsLR/RespondToClaimSubmit.json"))
          .check(substring("AWAITING_APPLICANT_INTENTION"))
        )

          .exec { session =>
            val fw = new BufferedWriter(new FileWriter("ResponseToClaimCompleted.csv", true))
            try {
              fw.write(session("caseId").as[String] + "\r\n")
            } finally fw.close()
            session
          }
      }
      .pause(MinThinkTime, MaxThinkTime)
  

  val RespondToDefence =


    group("Civil_CreateClaim_330_BackToCaseDetailsPage") {

      exec(_.setAll(
        "Idempotencynumber" -> (Common.getIdempotency()),
        "LRrandomString" -> Common.randomString(5)
      ))

      .exec(http("Civil_CreateClaim_330_005_CaseDetails")
        .get("/data/internal/cases/#{caseId}")
        .headers(CivilDamagesHeader.headers_717)
        .check(substring("Civil"))
        .check(status.in(200, 201, 304)))


    }
      /*======================================================================================
           * Create Civil Claim - Start Event 'View and Respond to Defence'
    ==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("XUI_CreateClaim_620_RespondToDefence") {
        exec(http("XUI_CreateClaim_620_005_RespondToDefence")
          .get("/workallocation/case/tasks/#{caseId}/event/CLAIMANT_RESPONSE/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .check(substring("task_required_for_event"))
        )
          .exec(http("XUI_CreateClaim_620_010_RespondToDefence")
            .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/CLAIMANT_RESPONSE?ignore-warning=false")
            .headers(CivilDamagesHeader.headers_notify)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
            .check(substring("CLAIMANT_RESPONSE"))
            .check(jsonPath("$.case_fields[0].value.partyID").saveAs("repPartyID"))
            .check(jsonPath("$.case_fields[0].value.partyName").saveAs("partyName"))
            .check(jsonPath("$.case_fields[0].value.partyName").saveAs("defPartyName"))
            .check(jsonPath("$..formatted_value.file.document_url").saveAs("document_url"))
            .check(jsonPath("$.event_token").saveAs("event_token"))
          )
          .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))


      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
  * Create Civil Claim - View and respond to defence
  ==========================================================================================*/

      .group("XUI_CreateClaim_630_ViewAndRespond") {
        exec(http("XUI_CreateClaim_630_005_ViewAndRespond")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSERespondentResponse")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("x-xsrf-token", "#{XSRFToken}")
          .body(ElFileBody("bodies/LRvsLR/ViewAndRespond.json"))
          .check(substring("CLAIMANT_RESPONSERespondentResponse"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)




      /*======================================================================================
  * Create Civil Claim - Upload Response To Defence
  ==========================================================================================*/

      .group("XUI_CreateClaim_640_UploadResponseToDefence") {
        exec(http("XUI_CreateClaim_640_005_UploadResponseToDefence")
          .post(BaseURL + "/documentsv2")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundary3qhEjdcm5ZAK4s7o")
          .header("sec-fetch-dest", "empty")
          .header("sec-fetch-mode", "cors")
          .bodyPart(RawFileBodyPart("files", "casesummary.docx")
            .fileName("casesummary.docx")
            .transferEncoding("binary"))
          .asMultipartForm
          .formParam("classification", "PUBLIC")
          .formParam("caseTypeId", "CIVIL")
          .formParam("jurisdictionId", "CIVIL")
          .check(jsonPath("$.documents[0].hashToken").saveAs("defenceHashToken"))
          .check(jsonPath("$.documents[0]._links.self.href").saveAs("defenceDocument_url"))
          .check(substring("casesummary.docx")))
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
  * Create Civil Claim - View and respond to defence Submit
  ==========================================================================================*/

      .group("XUI_CreateClaim_640_UploadResponseToDefSubmit") {
        exec(http("XUI_CreateClaim_640_005_UploadResponseToDefSubmit")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEApplicantDefenceResponseDocument")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .body(ElFileBody("bodies/LRvsLR/UploadResponseToDef.json"))
          .check(substring("applicant1DefenceResponseDocument"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)

      /*======================================================================================
  * Create Civil Claim - File directions questionnaire
  ==========================================================================================*/

      .group("XUI_CreateClaim_650_FileDirectionsQuestionnaireDef") {
        exec(http("XUI_CreateClaim_650_005_FileDirectionsQuestionnaireDef")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEFileDirectionsQuestionnaire")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .body(ElFileBody("bodies/LRvsLR/FileDirectionsQuestionnaireDef.json"))
          .check(substring("applicant1DQFileDirectionsQuestionnaire"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
  * Create Civil Claim -Fixed Recoverable Costs
  ==========================================================================================*/

      .group("XUI_CreateClaim_660_FixedRecoverableCosts") {
        exec(http("XUI_CreateClaim_660_005_FixedRecoverableCosts")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEFixedRecoverableCosts")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .body(ElFileBody("bodies/LRvsLR/FixedRecoverableCostsDef.json"))
          .check(substring("applicant1DQFixedRecoverableCosts"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
  * Create Civil Claim -Do you want to propose directions for disclosure?
  ==========================================================================================*/

      .group("XUI_CreateClaim_670_ProposeDirectionsForDisclosure") {
        exec(http("XUI_CreateClaim_670_005_ProposeDirectionsForDisclosure")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEDisclosureOfNonElectronicDocuments")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .body(ElFileBody("bodies/LRvsLR/ProposeDirectionsForDisclosure.json"))
          .check(substring("applicant1DQDisclosureOfNonElectronicDocuments"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
  * Create Civil Claim -Do you want to propose directions for disclosure?
  ==========================================================================================*/

      .group("XUI_CreateClaim_680_UseExpertClaim") {
        exec(http("XUI_CreateClaim_680_005_UseExpertClaim")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEExperts")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .body(ElFileBody("bodies/LRvsLR/UseExpertClaim.json"))
          .check(substring("CLAIMANT_RESPONSEExperts"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
  * Create Civil Claim -Are there any witnesses who should attend the hearing?
  ==========================================================================================*/

      .group("XUI_CreateClaim_690_AnyWitnessesClaim") {
        exec(http("XUI_CreateClaim_690_005_AnyWitnessesClaim")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEWitnesses")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .body(ElFileBody("bodies/LRvsLR/AnyWitnessesClaim.json"))
          .check(substring("CLAIMANT_RESPONSEWitnesses"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
  * Create Civil Claim -Welsh language
  ==========================================================================================*/

      .group("XUI_CreateClaim_700_WelshLanguageClaim") {
        exec(http("XUI_CreateClaim_700_005_WelshLanguageClaim")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSELanguage")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .body(ElFileBody("bodies/LRvsLR/WelshLanguageClaim.json"))
          .check(substring("applicant1DQLanguage"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
  * Create Civil Claim -Hearing availability
  ==========================================================================================*/

      .group("XUI_CreateClaim_710_HearingAvailabilityClaim") {
        exec(http("XUI_CreateClaim_710_HearingAvailabilityClaim")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEHearing")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .body(ElFileBody("bodies/LRvsLR/HearingAvailabilityClaim.json"))
          .check(substring("CLAIMANT_RESPONSEHearing"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
  * Create Civil Claim -Upload draft directions
  ==========================================================================================*/

      .group("XUI_CreateClaim_720_UploadDraftDirectionsClaim") {
        exec(http("XUI_CreateClaim_720_005_UploadDraftDirectionsClaim")
          .post(BaseURL + "/documentsv2")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundary3qhEjdcm5ZAK4s7o")
          .header("sec-fetch-dest", "empty")
          .header("sec-fetch-mode", "cors")
          .bodyPart(RawFileBodyPart("files", "casesummary.docx")
            .fileName("casesummary.docx")
            .transferEncoding("binary"))
          .asMultipartForm
          .formParam("classification", "PUBLIC")
          .formParam("caseTypeId", "CIVIL")
          .formParam("jurisdictionId", "CIVIL")
          .check(jsonPath("$.documents[0].hashToken").saveAs("draftClaimHashToken"))
          .check(jsonPath("$.documents[0]._links.self.href").saveAs("draftClaimDocument_url"))
          .check(substring("casesummary.docx")))
      }

      .pause(MinThinkTime, MaxThinkTime)




      /*======================================================================================
  * Create Civil Claim -Hearing availability
  ==========================================================================================*/

      .group("XUI_CreateClaim_730_UploadDirectionsClaimSubmit") {
        exec(http("XUI_CreateClaim_730_005_UploadDirectionsClaimSubmit")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEDraftDirections")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .body(ElFileBody("bodies/LRvsLR/UploadDirectionsClaimSubmit.json"))
          .check(substring("applicant1DQDraftDirections"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)




      /*======================================================================================
  * Create Civil Claim -Does anyone require support for a court hearing?
  ==========================================================================================*/

      .group("XUI_CreateClaim_740_RequireSupportClaim") {
        exec(http("XUI_CreateClaim_740_005_RequireSupportClaim")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEHearingSupport")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .body(ElFileBody("bodies/LRvsLR/RequireSupportClaim.json"))
          .check(substring("applicant1DQHearingSupport"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
  * Create Civil Claim -Vulnerability Questions
  ==========================================================================================*/

      .group("XUI_CreateClaim_750_VulnerabilityQuestionsClaim") {
        exec(http("XUI_CreateClaim_750_005_VulnerabilityQuestionsClaim")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEVulnerabilityQuestions")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .body(ElFileBody("bodies/LRvsLR/VulnerabilityQuestionsClaim.json"))
          .check(substring("applicant1DQVulnerabilityQuestions"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
  * Create Civil Claim -Further information
  ==========================================================================================*/

      .group("XUI_CreateClaim_760_FurtherInformationClaim") {
        exec(http("XUI_CreateClaim_760_005_FurtherInformationClaim")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEFurtherInformation")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .body(ElFileBody("bodies/LRvsLR/FurtherInformationClaim.json"))
          .check(substring("applicant1DQFurtherInformation"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
  * Create Civil Claim -Statement of Truth
  ==========================================================================================*/

      .group("XUI_CreateClaim_770_SoTClaim") {
        exec(http("XUI_CreateClaim_770_005_SoTClaim")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEStatementOfTruth")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .body(ElFileBody("bodies/LRvsLR/SoTClaim.json"))
          .check(substring("CLAIMANT_RESPONSEStatementOfTruth"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
  * Create Civil Claim -View and respond to defence submit
  ==========================================================================================*/

      .group("XUI_CreateClaim_780_ViewRespondToDefenceSubmit") {
        exec(http("XUI_CreateClaim_780_005_ViewRespondToDefenceSubmit")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/LRvsLR/ViewRespondToDefenceSubmit.json"))
          .check(substring("JUDICIAL_REFERRAL"))
        )

          .exec { session =>
            val fw = new BufferedWriter(new FileWriter("JudicialRefVMLatest.csv", true))
            try {
              fw.write(session("caseId").as[String] + "\r\n")
            } finally fw.close()
            session
          }
      }
      .pause(MinThinkTime, MaxThinkTime)



  val SDO =

    group("Civil_CreateClaim_330_BackToCaseDetailsPage") {


      exec(_.setAll(
        "HearingYear" -> Common.getYearFuture(),
        "HearingDay" -> Common.getDay(),
        "HearingMonth" -> Common.getMonth(),
        "Plus4WeeksDay" -> Common.getPlus4WeeksDay(),
        "Plus4WeeksMonth" -> Common.getPlus4WeeksMonth(),
        "Plus4WeeksYear" -> Common.getPlus4WeeksYear(),
        "Plus6WeeksDay" -> Common.getPlus6WeeksDay(),
        "Plus6WeeksMonth" -> Common.getPlus6WeeksMonth(),
        "Plus6WeeksYear" -> Common.getPlus6WeeksYear(),
        "Plus8WeeksDay" -> Common.getPlus8WeeksDay(),
        "Plus8WeeksMonth" -> Common.getPlus8WeeksMonth(),
        "Plus8WeeksYear" -> Common.getPlus8WeeksYear(),
        "Plus10WeeksDay" -> Common.getPlus10WeeksDay(),
        "Plus10WeeksMonth" -> Common.getPlus10WeeksMonth(),
        "Plus10WeeksYear" -> Common.getPlus10WeeksYear(),
        "Plus12WeeksDay" -> Common.getPlus12WeeksDay(),
        "Plus12WeeksMonth" -> Common.getPlus12WeeksMonth(),
        "Plus12WeeksYear" -> Common.getPlus12WeeksYear(),
        "LRrandomString" -> Common.randomString(5)
      ))
        .exec(http("Civil_CreateClaim_330_005_CaseDetails")
          .get(BaseURL + "/data/internal/cases/#{caseId}")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
          .check(substring("Civil"))
          .check(status.in(200, 201, 304)))


    }
      

      /*======================================================================================
           * Create Civil Claim - Click on Tasks Tab
    ==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("XUI_CreateClaim_790_TaskTabs") {
        exec(http("XUI_CreateClaim_790_005_AssignToMe")
          .post(BaseURL + "/workallocation/case/task/#{caseId}")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .body(ElFileBody("bodies/sdofasttrack/TaskTab.json"))
          .check(jsonPath("$[0].id").saveAs("JudgeId"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
           * Create Civil Claim - Start Event 'Assign To Me'
    ==========================================================================================*/
    //  val returntocasedetailsafternotifydetails =
         .group("XUI_CreateClaim_800_AssignToMe") {
         exec(http("XUI_CreateClaim_800_005_AssignToMe")
         .post("/workallocation/task/#{JudgeId}/claim")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
         .body(ElFileBody("bodies/sdofasttrack/AssignToMe.json"))
         .check(substring("assignee"))
      )


        }
      .pause(MinThinkTime, MaxThinkTime)





      /*======================================================================================
     * Create Civil Claim - Start Event 'Directions - Fast Track'
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("XUI_CreateClaim_810_DirectionsFastTrack") {
        exec(http("XUI_CreateClaim_810_005_DirectionsFastTrack")
          .get("/cases/case-details/#{caseId}/trigger/CREATE_SDO/CREATE_SDOFastTrack?tid=#{JudgeId}")
          .headers(CivilDamagesHeader.headers_notify)
          .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
          //  .check(substring("assignee"))
        )
          
          .exec(http("XUI_CreateClaim_810_010_DirectionsFastTrack")
            .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/CREATE_SDO?ignore-warning=false")
            .headers(CivilDamagesHeader.headers_notify)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
            .check(substring("CREATE_SDO"))
            /*   .check(jsonPath("$.case_fields[62].formatted_value.partyID").saveAs("repPartyID"))
               .check(jsonPath("$.case_fields[62].formatted_value.partyName").saveAs("partyName"))
               .check(jsonPath("$.case_fields[62].value.flags.partyName").saveAs("defPartyName"))
               .check(jsonPath("$.case_fields[58].formatted_value.file.document_url").saveAs("document_url"))

             */
            .check(jsonPath("$.event_token").saveAs("event_token"))
          )
          .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))
        
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
     * Create Civil Claim - Do you wish to enter judgment for a sum of damages to be decided ?
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("XUI_CreateClaim_820_EnterJudgmentDamages") {
        exec(http("XUI_CreateClaim_820_005_EnterJudgmentDamages")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_SDOSDO")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sdofasttrack/EnterJudgmentDamages.json"))
          .check(substring("drawDirectionsOrderRequired"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
* Create Civil Claim - What track are you allocating the claim to?
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("XUI_CreateClaim_830_WhatTrackAllocating") {
        exec(http("XUI_CreateClaim_830_005_WhatTrackAllocating")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_SDOClaimsTrack")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sdofasttrack/WhatTrackAllocating.json"))
          .check(substring("allocatedTrack"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* Create Civil Claim - Standard Direction Order Details
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("XUI_CreateClaim_840_SDOdetails") {
        exec(http("XUI_CreateClaim_840_005_SDOdetails")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_SDOFastTrack")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/LRvsLR/SDOdetails.json"))
          .check(jsonPath("$.data.sdoOrderDocument.createdDatetime").saveAs("createdDatetime"))
          .check(jsonPath("$.data.sdoOrderDocument.documentLink.document_url").saveAs("sdoDocument_url"))
          .check(jsonPath("$.data.sdoOrderDocument.documentLink.document_hash").saveAs("sdoDocument_hash"))
          .check(jsonPath("$.data.sdoOrderDocument.documentName").saveAs("sdoDocumentName"))
          .check(jsonPath("$.data.sdoOrderDocument.documentSize").saveAs("documentSize"))
          .check(substring("claimNotificationDeadline"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
* Create Civil Claim - Standard Direction Order Continue
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("XUI_CreateClaim_850_SDOContinue") {
        exec(http("XUI_CreateClaim_850_005_SDOContinue")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_SDOOrderPreview")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/LRvsLR/SDOContinue.json"))
          .check(substring("sdoOrderDocument"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
* Create Civil Claim - Standard Direction Order Submit
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("XUI_CreateClaim_860_SDOSubmit") {
        exec(http("XUI_CreateClaim_860_005_SDOSubmit")
          .post("/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .body(ElFileBody("bodies/LRvsLR/SDOSubmit.json"))
          .check(substring("CASE_PROGRESSION"))
        )

          .exec { session =>
            val fw = new BufferedWriter(new FileWriter("CaseProg.csv", true))
            try {
              fw.write(session("caseId").as[String] + "\r\n")
            } finally fw.close()
            session
          }

      }
      .pause(MinThinkTime, MaxThinkTime)


      //Deepak - Cases that make the final step
  
  
  val SDOEnhancementFastTrack =
    
    group("Civil_CreateClaim_330_BackToCaseDetailsPage") {
      
      
      exec(_.setAll(
        "HearingYear" -> Common.getYearFuture(),
        "HearingDay" -> Common.getDay(),
        "HearingMonth" -> Common.getMonth(),
        "Plus4WeeksDay" -> Common.getPlus4WeeksDay(),
        "Plus4WeeksMonth" -> Common.getPlus4WeeksMonth(),
        "Plus4WeeksYear" -> Common.getPlus4WeeksYear(),
        "Plus6WeeksDay" -> Common.getPlus6WeeksDay(),
        "Plus6WeeksMonth" -> Common.getPlus6WeeksMonth(),
        "Plus6WeeksYear" -> Common.getPlus6WeeksYear(),
        "Plus8WeeksDay" -> Common.getPlus8WeeksDay(),
        "Plus8WeeksMonth" -> Common.getPlus8WeeksMonth(),
        "Plus8WeeksYear" -> Common.getPlus8WeeksYear(),
        "Plus10WeeksDay" -> Common.getPlus10WeeksDay(),
        "Plus10WeeksMonth" -> Common.getPlus10WeeksMonth(),
        "Plus10WeeksYear" -> Common.getPlus10WeeksYear(),
        "Plus12WeeksDay" -> Common.getPlus12WeeksDay(),
        "Plus12WeeksMonth" -> Common.getPlus12WeeksMonth(),
        "Plus12WeeksYear" -> Common.getPlus12WeeksYear(),
        "LRrandomString" -> Common.randomString(5)
      ))
        .exec(http("Civil_CreateClaim_330_005_CaseDetails")
          .get(BaseURL + "/data/internal/cases/#{caseId}")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
          .check(substring("Civil"))
          .check(status.in(200, 201, 304)))
      
      
    }
      
      
      /*======================================================================================
           * Create Civil Claim - Click on Tasks Tab
    ==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("XUI_CreateClaim_790_TaskTabs") {
        exec(http("XUI_CreateClaim_790_005_AssignToMe")
          .post(BaseURL + "/workallocation/case/task/#{caseId}")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .body(ElFileBody("bodies/sdofasttrack/TaskTab.json"))
          .check(jsonPath("$[0].id").saveAs("JudgeId"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      /*======================================================================================
           * Create Civil Claim - Start Event 'Assign To Me'
    ==========================================================================================*/
      //  val returntocasedetailsafternotifydetails =
      .group("XUI_CreateClaim_800_AssignToMe") {
        exec(http("XUI_CreateClaim_800_005_AssignToMe")
          .post("/workallocation/task/#{JudgeId}/claim")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          .body(ElFileBody("bodies/sdofasttrack/AssignToMe.json"))
          .check(substring("assignee"))
        )
        
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      
      
      /*======================================================================================
     * Create Civil Claim - Start Event 'Directions - Fast Track'
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("XUI_CreateClaim_810_DirectionsFastTrack") {
        exec(http("XUI_CreateClaim_810_005_DirectionsFastTrack")
          .get("/cases/case-details/#{caseId}/trigger/CREATE_SDO/CREATE_SDOFastTrack?tid=#{JudgeId}")
          .headers(CivilDamagesHeader.headers_notify)
          .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
          //  .check(substring("assignee"))
        )
          
          .exec(http("XUI_CreateClaim_810_010_DirectionsFastTrack")
            .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/CREATE_SDO?ignore-warning=false")
            .headers(CivilDamagesHeader.headers_notify)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
            .check(substring("CREATE_SDO"))
            /*   .check(jsonPath("$.case_fields[62].formatted_value.partyID").saveAs("repPartyID"))
               .check(jsonPath("$.case_fields[62].formatted_value.partyName").saveAs("partyName"))
               .check(jsonPath("$.case_fields[62].value.flags.partyName").saveAs("defPartyName"))
               .check(jsonPath("$.case_fields[58].formatted_value.file.document_url").saveAs("document_url"))

             */
            .check(jsonPath("$.event_token").saveAs("event_token"))
          )
          .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
     * Create Civil Claim - Do you wish to enter judgment for a sum of damages to be decided ?
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("XUI_CreateClaim_820_EnterJudgmentDamages") {
        exec(http("XUI_CreateClaim_820_005_EnterJudgmentDamages")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_SDOSDO")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sdofasttrack/EnterJudgmentDamages.json"))
          .check(substring("drawDirectionsOrderRequired"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
* Create Civil Claim - What track are you allocating the claim to?
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("XUI_CreateClaim_830_WhatTrackAllocating") {
        exec(http("XUI_CreateClaim_830_005_WhatTrackAllocating")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_SDOClaimsTrack")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sdofasttrack/WhatTrackAllocating.json"))
          .check(substring("allocatedTrack"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      /*======================================================================================
* Create Civil Claim - Standard Direction Order Details
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("XUI_CreateClaim_840_SDOdetails") {
        exec(http("XUI_CreateClaim_840_005_SDOdetails")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_SDOSdoR2FastTrack")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sdofasttrack/SDOenhancementsfasttrackdetails.json"))
          .check(jsonPath("$.data.sdoOrderDocument.createdDatetime").saveAs("createdDatetime"))
          .check(jsonPath("$.data.sdoOrderDocument.documentLink.document_url").saveAs("sdoDocument_url"))
          .check(jsonPath("$.data.sdoOrderDocument.documentLink.document_hash").saveAs("sdoDocument_hash"))
          .check(jsonPath("$.data.sdoOrderDocument.documentName").saveAs("sdoDocumentName"))
          .check(jsonPath("$.data.sdoOrderDocument.documentSize").saveAs("documentSize"))
          .check(substring("claimNotificationDeadline"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
* Create Civil Claim - Standard Direction Order Continue
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("XUI_CreateClaim_850_SDOContinue") {
        exec(http("XUI_CreateClaim_850_005_SDOContinue")
          .post("/data/case-types/CIVIL/validate?pageId=pageId=CREATE_SDOOrderPreview")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sdofasttrack/SDOEnhancementsContinueFastTrack.json"))
          .check(substring("sdoOrderDocument"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
* Create Civil Claim - Standard Direction Order Submit
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("XUI_CreateClaim_860_SDOSubmit") {
        exec(http("XUI_CreateClaim_860_005_SDOSubmit")
          .post("/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .body(ElFileBody("bodies/sdofasttrack/SDOEnhancementsFastTrackSubmit.json"))
          .check(substring("CASE_PROGRESSION"))
        )
          
          .exec { session =>
            val fw = new BufferedWriter(new FileWriter("CaseProg.csv", true))
            try {
              fw.write(session("caseId").as[String] + "\r\n")
            } finally fw.close()
            session
          }
        
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
}
