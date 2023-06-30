
package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, Common, Environment}

import java.io.{BufferedWriter, FileWriter}

object ClaimCreation {
  
  val BaseURL = Environment.baseURL
  val IdAMURL = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime
  
  //def run(implicit postHeaders: Map[String, String]): ChainBuilder = {
    val run=
      exec(_.setAll("Idempotencynumber" -> (Common.getIdempotency())
       ))
    //val createclaim =
    .group("Civil_CreateClaim_030_CreateCase") {
      exec(http("Civil_CreateClaim_030_CreateCase")
        .get("/aggregated/caseworkers/:uid/jurisdictions?access=create")
        .headers(CivilDamagesHeader.headers_104)
        .check(status.in(200, 304))
      ).exitHereIfFailed
    }
      .pause(MinThinkTime, MaxThinkTime)
      //val startCreateClaim =
      .group("Civil_CreateClaim_040_StartCreateCase1") {
      exec(http("Civil_CreateClaim_040_StartCreateCase1")
        .get("/data/internal/case-types/CIVIL/event-triggers/CREATE_CLAIM?ignore-warning=false")
        .headers(CivilDamagesHeader.headers_140)
        .check(status.is(200))
        .check(jsonPath("$.event_token").optional.saveAs("event_token"))
      )
        .pause(MinThinkTime, MaxThinkTime)
    }
  
      //val claimliability =
      .group("Civil_CreateClaim_050_CLAIMEligibility") {
      exec(http("Civil_CreateClaim_050_Eligibility")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMEligibility")
        .headers(CivilDamagesHeader.headers_163)
        .body(ElFileBody("bodies/0013_request.json"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  
      //val claimreferences =
      .group("Civil_CreateClaim_060_CLAIMReferences") {
      exec(http("Civil_CreateClaim_060_Reference")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMReferences")
        .headers(CivilDamagesHeader.headers_192)
        .body(ElFileBody("bodies/0017_request.json"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  
      //val claimcourt =
      .group("Civil_CreateClaim_070_CLAIMCourt") {
      exec(http("Civil_CreateClaim_070_Court")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMCourt")
        .headers(CivilDamagesHeader.headers_213)
        .body(ElFileBody("bodies/0021_request.json"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      //val postcode =
      .group("Civil_CreateClaim_080_ClaimantPostcode") {
      exec(http("Civil_CreateClaim_080_Postcode")
        .get("/api/addresses?postcode=TW33SD")
        .headers(CivilDamagesHeader.headers_104)
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
      //val createclaimclaimant =
      .group("Civil_CreateClaim_090_CLAIMClaimant") {
      exec(http("Civil_CreateClaim_090_Claimant")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMClaimant")
        .headers(CivilDamagesHeader.headers_258)
        .body(ElFileBody("bodies/0027_request.json"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  
      // val createclaimlitigantfriend =
      .group("Civil_CreateClaim_100_CLAIMClaimantLitigationFriend") {
      exec(http("Civil_CreateClaim_100_Litigation")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMClaimantLitigationFriend")
        .headers(CivilDamagesHeader.headers_273)
        .body(ElFileBody("bodies/0031_request.json"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  
      //val createclaimantnotifications =
      .group("Civil_CreateClaim_110_CLAIMNotifications") {
        exec(http("Civil_CreateClaim_110_Notifications")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMNotifications")
          .headers(CivilDamagesHeader.headers_295)
          .body(ElFileBody("bodies/0035_request.json"))
          .check(status.in(200, 304))
        )
  
        //val caseshareorgs =
  
        .exec(http("Civil_CreateClaim_120_CaseShare")
          .get("/api/caseshare/orgs")
          .headers(CivilDamagesHeader.headers_418)
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
    
  
  
      //val claimantsolorganisation =
      .group("Civil_CreateClaim_130_CLAIMClaimantSolicitorOrganisation") {
      exec(http("Civil_CreateClaim_130_Org")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMClaimantSolicitorOrganisation")
        .headers(CivilDamagesHeader.headers_347)
        .body(ElFileBody("bodies/0040_request.json"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      //val postcode1 =
      .group("Civil_CreateClaim_140_SolPostcode") {
      exec(http("Civil_CreateClaim_140_Postcode")
        .get("/api/addresses?postcode=TW33SD")
        .headers(CivilDamagesHeader.headers_104)
        .check(status.in(200, 304))
      )
    }
      //create solicitor service address
      .group("Civil_CreateClaim_150_CLAIMClaimantSolicitorServiceAddress") {
        exec(http("Civil_CreateClaim_150_Serviceaddress")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMClaimantSolicitorServiceAddress")
          .headers(CivilDamagesHeader.headers_347)
          .body(ElFileBody("bodies/0046_request.json"))
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      //add another claim
      .group("Civil_CreateClaim_160_CLAIMAddAnotherClaimantn") {
        exec(http("Civil_CreateClaim_160_CLAIMAddAnotherClaimant")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMAddAnotherClaimant")
          .headers(CivilDamagesHeader.headers_347)
          .body(ElFileBody("bodies//0050_request.json"))
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      //post code
      
      .group("Civil_CreateClaim_170_SolPostcode") {
        exec(http("Civil_CreateClaim_170_Postcode")
          .get("/api/addresses?postcode=TW33SD")
          .headers(CivilDamagesHeader.headers_104)
          .check(status.in(200, 304))
        )
      }
      
      //val claimdefendant =
      
      .group("Civil_CreateClaim_150_CLAIMDefendant") {
      exec(http("Civil_CreateClaim_150_Def")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMDefendant")
        .headers(CivilDamagesHeader.headers_394)
        .body(ElFileBody("bodies/0056_request.json"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  
      //val claimLegalRep =
      .group("Civil_CreateClaim_160_CLAIMLegalRepresentation") {
      exec(http("Civil_CreateClaim_160_Rep")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMLegalRepresentation")
        .headers(CivilDamagesHeader.headers_413)
        .body(ElFileBody("bodies/0060_request.json"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      //val caseshareorgs1 =
      .group("Civil_CreateClaim_170_DefCaseShare") {
      exec(http("Civil_CreateClaim_170_CaseShare")
        .get("/api/caseshare/orgs")
        .headers(CivilDamagesHeader.headers_418)
        .check(status.in(200, 304))
      )
    }
  
      //val claimdefsolicitororg =
      
      .group("Civil_CreateClaim_180_CLAIMDefendantSolicitorOrganisation") {
      exec(http("Civil_CreateClaim_180_Org")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMDefendantSolicitorOrganisation")
        .headers(CivilDamagesHeader.headers_469)
        .body(ElFileBody("bodies/0066_request.json"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  
      //post code
  
      .group("Civil_CreateClaim_170_SolPostcode") {
        exec(http("Civil_CreateClaim_170_Postcode")
          .get("/api/addresses?postcode=TW33SD")
          .headers(CivilDamagesHeader.headers_104)
          .check(status.in(200, 304))
        )
      }
  
      //create defendant solicitor service address
      .group("Civil_CreateClaim_150_CLAIMClaimantSolicitorServiceAddress") {
        exec(http("Civil_CreateClaim_150_Serviceaddress")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMDefendantSolicitorServiceAddress")
          .headers(CivilDamagesHeader.headers_347)
          .body(ElFileBody("bodies/0072_request.json"))
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      // val claimdefsolicitororgemail =
      .group("Civil_CreateClaim_190_CLAIMDefendantSolicitorEmail") {
      exec(http("Civil_CreateClaim_190_SolEmail")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMDefendantSolicitorEmail")
        .headers(CivilDamagesHeader.headers_491)
        .body(ElFileBody("bodies/0076_request.json"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
      
      //add another defendant
      // val claimdefsolicitororgemail =
      .group("Civil_CreateClaim_190_CLAIMDefendantSolicitorEmail") {
        exec(http("Civil_CreateClaim_190_SolEmail")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMAddAnotherDefendant")
          .headers(CivilDamagesHeader.headers_491)
          .body(ElFileBody("bodies/0080_request.json"))
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      // val claimtype =
      .group("Civil_CreateClaim_200_CLAIMClaimType") {
      exec(http("Civil_CreateClaim_200_ClaimType")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMClaimType")
        .headers(CivilDamagesHeader.headers_514)
        .body(ElFileBody("bodies/0084_request.json"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
      
      //create claim injury type
      
      .group("Civil_CreateClaim_200_CLAIMClaimType") {
        exec(http("Civil_CreateClaim_200_ClaimType")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMPersonalInjuryType")
          .headers(CivilDamagesHeader.headers_514)
          .body(ElFileBody("bodies/0088_request.json"))
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      // val createclaimdetail =
      .group("Civil_CreateClaim_210_CLAIMDetails") {
      exec(http("Civil_CreateClaim_210_Details")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMDetails")
        .headers(CivilDamagesHeader.headers_534)
        .body(ElFileBody("bodies/0092_request.json"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  
      // val postclaimdocs =
      
      .group("Civil_CreateClaim_220_Documents") {
      exec(http("Civil_CreateClaim_220_Docs")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMuploadParticularsOfClaim")
        .headers(CivilDamagesHeader.headers_534)
        .body(ElFileBody("bodies/0096_request.json"))
        .check(status.in(200, 304)))
    }
      .pause(MinThinkTime, MaxThinkTime)
      // val createclaimupload =
    
      // val createclaimvalue =
      .group("Civil_CreateClaim_240_CLAIMClaimValue") {
      exec(http("Civil_CreateClaim_240_ClaimValue")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMClaimValue")
        .headers(CivilDamagesHeader.headers_595)
        .body(ElFileBody("bodies/0100_request.json"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
      // val createclaimpbanumber =
      .group("Civil_CreateClaim_250_CLAIMPbaNumber") {
      exec(http("Civil_CreateClaim_250_PBANumber")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMPbaNumber")
        .headers(CivilDamagesHeader.headers_610)
        .body(ElFileBody("bodies/0104_request.json"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
     
      //val createclaimstatementoftruth =
      .group("Civil_CreateClaim_270_CLAIMStatementOfTruth") {
      exec(http("Civil_CreateClaim_270_SOT")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMStatementOfTruth")
        .headers(CivilDamagesHeader.headers_650)
        .body(ElFileBody("bodies/0108_request.json"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
      // val submitclaimevent =
      .group("Civil_CreateClaim_280_SubmitClaim") {
      
        exec(http("Civil_CreateClaim_280_015_Submit")
          .post("/data/case-types/CIVIL/cases?ignore-warning=false")
          .headers(CivilDamagesHeader.headers_672)
          .body(ElFileBody("bodies/0112_request.json"))
          .check(jsonPath("$.event_token").optional.saveAs("event_token_claimcreate"))
          .check(jsonPath("$.id").optional.saveAs("caseId"))
          .check(jsonPath("$.legacyCaseReference").optional.saveAs("claimNumber"))
          .check(status.in(200, 201))
        )
        .exec(http("Civil_CreateClaim_280_010_Orgs")
          .get("/api/caseshare/orgs")
          .headers(CivilDamagesHeader.headers_658)
          .check(status.in(200, 304))
        )
    }
      .pause(MinThinkTime, MaxThinkTime)
        .pause(50)
  
      .exec { session =>
        val fw = new BufferedWriter(new FileWriter("caseIds.csv", true))
        try {
          fw.write(session("caseId").as[String] + "\r\n")
        } finally fw.close()
        session
      }
      
  
      //  val getcasedetailspage=
      .group("Civil_CreateClaim_290_BackToCaseDetailsPage") {
      exec(http("Civil_CreateClaim_290_005_CaseDetails")
        .get("/data/internal/cases/${caseId}")
        .headers(CivilDamagesHeader.headers_717)
        .check(status.in(200,201,304))
      )
        .exec(http("Civil_CreateClaim_290_010_CaseDetails2")
          .get("/api/role-access/roles/manageLabellingRoleAssignment/${caseId}")
          .headers(CivilDamagesHeader.headers_717)
          .check(status.in(200, 201, 304))
        )
        .exec(http("Civil_CreateClaim_290_015_CaseDetails3")
          .get("/api/wa-supported-jurisdiction/get")
          .headers(CivilDamagesHeader.headers_717)
          .check(status.in(200, 201, 204,304))
        )
        
    }
      .pause(MinThinkTime, MaxThinkTime)
      
      // payment fee
      .group("Civil_CreateClaim_300_ClickPay") {
        exec(http("Civil_CreateClaim_300_005_ClickPay")
          .get( "/pay-bulkscan/cases/${caseId}")
          .headers(CivilDamagesHeader.headers_717)
          .check(status.in(200, 304,201))
        )
    
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      // payment pba fee payment
      .group("Civil_CreateClaim_310_PaymentGroups") {
        exec(http("Civil_CreateClaim_310_005_SelectPayGroups")
          .get("/payments/cases/${caseId}/paymentgroups")
          .headers(CivilDamagesHeader.headers_717)
          .check(status.in(200, 304))
        )
        .exec(http("Civil_CreateClaim_310_010_PaymentOrder")
          .get("/payments/case-payment-orders?case_ids=${caseId}")
          .headers(CivilDamagesHeader.headers_717)
          .check(jsonPath("$.content[0].orderReference").optional.saveAs("serviceRef"))
          .check(status.in(200, 304))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      //pba account selection and pay
  
      .group("Civil_CreateClaim_320_PBAAccounts") {
        exec(http("Civil_CreateClaim_320_005_SelectPBA")
          .get("/payments/pba-accounts")
          .headers(CivilDamagesHeader.headers_717)
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      //pba payment final
  
      .group("Civil_CreateClaim_330_SubmitPayment") {
        exec(http("Civil_CreateClaim_330_005_SubmitPayment")
          .post("/payments/service-request/${serviceRef}/pba-payments")
          .headers(CivilDamagesHeader.headers_650)
          .body(ElFileBody("bodies/0137_request.json"))
          .check(status.in(200,201,304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      .pause(50)
      
      
      //notify claim step
      
      .group("Civil_CreateClaim_310_NotifyClaimEvent") {
        /*exec(http("Civil_CreateClaim_310_005_Notify")
          .get("/workallocation/case/tasks/${caseId}/event/NOTIFY_DEFENDANT_OF_CLAIM/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(CivilDamagesHeader.headers_769)
          .check(status.in(200, 304))
          
        )*/
        exec(http("Civil_CreateClaim_310_010_Notify")
          .get("/data/internal/cases/${caseId}/event-triggers/NOTIFY_DEFENDANT_OF_CLAIM?ignore-warning=false")
          .headers(CivilDamagesHeader.headers_769)
          //.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
          .check(status.in(200,201, 304))
          .check(jsonPath("$.event_token").optional.saveAs("event_token_notifyclaimtodef"))
        )
          
          .exec(http("Civil_CreateClaim_310_015_profile")
            .get("/data/internal/profile")
            .headers(CivilDamagesHeader.headers_149)
            .check(status.in(200, 304))
          )
      }
      .pause(MinThinkTime, MaxThinkTime)
      // val claimnotifyeventcontinue =
      .group("Civil_CreateClaim_320_CLAIMAccessGrantedWarning") {
      exec(http("Civil_CreateClaim_320_005_grant")
        .post("/data/case-types/CIVIL/validate?pageId=NOTIFY_DEFENDANT_OF_CLAIMAccessGrantedWarning")
        .headers(CivilDamagesHeader.headers_783)
        .body(ElFileBody("bodies/0187_request.json"))
        .check(status.in(200, 304))
      )
        .exec(http("Civil_CreateClaim_320_010_profile")
          .get("/data/internal/profile")
          .headers(CivilDamagesHeader.headers_149)
          .check(status.in(200, 304))
        )
    }
      .pause(MinThinkTime, MaxThinkTime)
        .pause(50)
    
      // val claimnotifyeventsubmit =
      .group("Civil_CreateClaim_330_ClaimNotifyEventSubmit") {
      exec(http("Civil_CreateClaim_330_ClaimNotifyEventSubmit")
        .post("/data/cases/${caseId}/events")
        .headers(CivilDamagesHeader.headers_803)
        .body(ElFileBody("bodies/0191_request.json"))
        .check(status.in(200, 201))
      ).exitHereIfFailed
    }
      //.exec(session => session.set("caseId", "${caseId}"))
      .pause(MinThinkTime, MaxThinkTime)
  
      //val backtocasedetailsafterclaimnotify =
      
      .group("Civil_CreateClaim_340_CasedetailsAfterClaimNotify") {
       
        exec(http("Civil_AfterClaimNotify_340_010")
          .get("/data/internal/cases/${caseId}")
          .headers(CivilDamagesHeader.headers_717)
          .check(status.in(200, 201))
        )
    }
      .pause(MinThinkTime, MaxThinkTime)
        .pause(20)
  
    //end of  claim notify
  
  //beginning of notify details
  
  .group("CD_CreateClaim_350_NotifyDetailsEvent") {
    exec(http("Civil_CreateClaim_350_005_NotifyDetails")
      .get("/workallocation/case/tasks/${caseId}/event/NOTIFY_DEFENDANT_OF_CLAIM/caseType/CIVIL/jurisdiction/CIVIL")
      .headers(CivilDamagesHeader.headers_769)
      .check(status.in(200, 201, 304))
    
    )
      .exec(http("Civil_CreateClaim_350_010_DetailCreate")
        .get("/data/internal/cases/${caseId}/event-triggers/NOTIFY_DEFENDANT_OF_CLAIM_DETAILS?ignore-warning=false")
        .headers(CivilDamagesHeader.headers_769)
        //.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
        .check(status.in(200, 304))
        .check(jsonPath("$.event_token").optional.saveAs("event_token_notifyclaimdetail"))
      )
      
      .exec(http("Civil_CreateClaim_350_010_profile")
        .get("/data/internal/profile")
        .headers(CivilDamagesHeader.headers_149)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-user-profile.v2+json;charset=UTF-8")
        
        .check(status.in(200, 304))
      )
  }
    .pause(MinThinkTime, MaxThinkTime)
    // val notifyclaimdetailsupload =
    .group("Civil_CreateClaim_360_CLAIM_DETAILSUpload") {
      exec(http("CD_CreateClaim_360_005_upload")
        .post("/data/case-types/CIVIL/validate?pageId=NOTIFY_DEFENDANT_OF_CLAIM_DETAILSUpload")
        .headers(CivilDamagesHeader.headers_868)
        // .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
        .body(ElFileBody("bodies/0210_request.json"))
        .check(status.in(200, 304))
      
      )
        .exec(http("CD_CreateClaim_360_010_profile")
          .get("/data/internal/profile")
          .headers(CivilDamagesHeader.headers_149)
          .check(status.in(200, 304))
        )
    }
    .pause(MinThinkTime, MaxThinkTime)
    // val notifyclaimdetailseventsubmit=
    .group("CD_CreateClaim_370_NotifyDetailsEventSubmit") {
      exec(http("CD_CreateClaim_370_EventSubmit")
        .post("/data/cases/${caseId}/events")
        .headers(CivilDamagesHeader.headers_886)
        // .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
        .body(ElFileBody("bodies/0214_request.json"))
        .check(status.in(200, 201))
      )
        .exec(http("CD_CreateClaim_380_010_case")
          .get("/data/internal/cases/${caseId}")
          .headers(CivilDamagesHeader.headers_717)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
          
          .check(status.in(200,201,204,304))
        )
    }
    .pause(MinThinkTime, MaxThinkTime)
    
    
    // val returntocasedetailsafternotifydetails =
    .group("CD_CreateClaim_380_ReturnToCaseDetailsAfterNotifyDetails") {
      exec(http("CD_CreateClaim_380_005_NotifyDetails")
        .post("/api/role-access/roles/manageLabellingRoleAssignment/${caseId}")
        .headers(CivilDamagesHeader.headers_894)
        .body(StringBody("{\"searchRequest\":{\"ccdId\":\"${caseId}\",\"eventId\":\"NOTIFY_DEFENDANT_OF_CLAIM_DETAILS\",\"jurisdiction\":\"CIVIL\",\"caseTypeId\":\"UNSPECIFIED_CLAIMS\"}}"))
        .check(status.in(200,204,201,304))
      )
        
        .exec(http("CD_CreateClaim_380_010_case")
          .get("/data/internal/cases/${caseId}")
          .headers(CivilDamagesHeader.headers_717)
          .check(status.in(200, 204,304))
        )
    }
    .pause(MinThinkTime, MaxThinkTime)
        .pause(20)
        
  
  // following is for assign the case to defendent
        .group("CIVIL_AssignCase_000_AssignCase") {
          exec(http("CIVIL_AssignCase_000_AssignCase")
            .post("http://civil-service-perftest.service.core-compute-perftest.internal/testing-support/assign-case/${caseId}/RESPONDENTSOLICITORONE")
            //   .get( "/cases/searchCases?start_date=${randomStartDate}&end_date=${randomEndDate}")
            // .get( "/cases/searchCases?start_date=2022-01-13T00:00:00&end_date=2023-04-16T15:38:00")
            .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJraWQiOiI4cDJpajg2S0pTeENKeGcveUovV2w3TjcxMXM9IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJobWN0cy5jaXZpbCtvcmdhbmlzYXRpb24uMi5zb2xpY2l0b3IuMTNAZ21haWwuY29tIiwiY3RzIjoiT0FVVEgyX1NUQVRFTEVTU19HUkFOVCIsImF1dGhfbGV2ZWwiOjAsImF1ZGl0VHJhY2tpbmdJZCI6IjcxOGU5MjlhLTJiNTctNGYxOC1hYWFhLWU0YmU0N2ZiMDg4Yy02NzA4ODgzOSIsInN1Ym5hbWUiOiJobWN0cy5jaXZpbCtvcmdhbmlzYXRpb24uMi5zb2xpY2l0b3IuMTNAZ21haWwuY29tIiwiaXNzIjoiaHR0cHM6Ly9mb3JnZXJvY2stYW0uc2VydmljZS5jb3JlLWNvbXB1dGUtaWRhbS1wZXJmdGVzdC5pbnRlcm5hbDo4NDQzL29wZW5hbS9vYXV0aDIvcmVhbG1zL3Jvb3QvcmVhbG1zL2htY3RzIiwidG9rZW5OYW1lIjoiYWNjZXNzX3Rva2VuIiwidG9rZW5fdHlwZSI6IkJlYXJlciIsImF1dGhHcmFudElkIjoiX2xubW10V2JLYkotblhPcWtpS3hDX2FrNzNJIiwibm9uY2UiOiItZFlRVDZ1V2plMk5ycy1BcDM3U21xWWlpaE5KaUJ5Q3FON1R6aUZnXzJnIiwiYXVkIjoieHVpd2ViYXBwIiwibmJmIjoxNjg4MTI3NzcwLCJncmFudF90eXBlIjoiYXV0aG9yaXphdGlvbl9jb2RlIiwic2NvcGUiOlsib3BlbmlkIiwicHJvZmlsZSIsInJvbGVzIiwiY3JlYXRlLXVzZXIiLCJtYW5hZ2UtdXNlciIsInNlYXJjaC11c2VyIl0sImF1dGhfdGltZSI6MTY4ODEyNzc3MCwicmVhbG0iOiIvaG1jdHMiLCJleHAiOjE2ODgxNTY1NzAsImlhdCI6MTY4ODEyNzc3MCwiZXhwaXJlc19pbiI6Mjg4MDAsImp0aSI6IjJrR3c3MFdpNk1INkZDOEtxbTdsVkFvZkJKUSJ9.hN0LDcj7gdY3R9IbafHJNbyEojytU6PtzfAXuiHbIPT-EuaK0F4N-uNSjAP6yT7Mn4yopgBzwFrD4-NSaV8qLQPvmhKA9vL6EBkyP4uOsrswDU_KehLhFCFEio9W9BJpIYbpYUmzC5ccRBgoCLnbZwE4w2rj-HwOYSOx8sc-VODyKnN2xptqAhED8cSGFGdKsPFTPEfyXN7QDie4Q-JZ-STUUhkLUvIRh4XttnRmCKBaZ_1JQBEUL05p4XXqmA__GftWRMH_CMQU6WccFNVwdr53IDMW6rMj-gGKnWM5nIJRiJEuQTGtYShpOAvottWFaUi8PFLCLNqaEZsnZo2f1Q")
            .header("Content-Type", "application/json")
            .header("Accept", "*/*")
            .check(status.in(200, 201))
          )
        }
        .pause(MinThinkTime, MaxThinkTime)
  
  
  
}
