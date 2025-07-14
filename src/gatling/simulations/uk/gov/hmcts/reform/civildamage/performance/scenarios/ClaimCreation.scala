
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
  
  /*======================================================================================
             * Click On pay from Service Page
  ==========================================================================================*/
    val run=
      exec(_.setAll("Idempotencynumber" -> (Common.getIdempotency())
       ))
    //val createclaim =
   /*======================================================================================
                     * Create Civil Claim - UnSpecified
   ==========================================================================================*/
    .group("Civil_CreateClaim_030_CreateCase") {
      exec(http("Civil_CreateClaim_030_CreateCase")
        .get("/aggregated/caseworkers/:uid/jurisdictions?access=create")
        .headers(CivilDamagesHeader.headers_104)
        .header("x-dynatrace-test", "FW4;TSN=CivilOnDynatrace;PSL=CitizenCreateClaimOnDynatrace")
        .check(status.in(200, 304))
      ).exitHereIfFailed
    }
      .pause(MinThinkTime, MaxThinkTime)
      //val startCreateClaim =
        /*======================================================================================
                     * Create Civil Claim - Start the case
          ==========================================================================================*/
      .group("Civil_CreateClaim_040_StartCreateCase1") {
      exec(http("Civil_CreateClaim_040_StartCreateCase1")
        .get("/data/internal/case-types/CIVIL/event-triggers/CREATE_CLAIM?ignore-warning=false")
        .headers(CivilDamagesHeader.headers_140)
        .header("x-dynatrace", "FW4;TSN=CivilOnDynatrace;PSL=CitizenCreateClaimOnDynatrace")
        .check(status.is(200))
        .check(jsonPath("#.event_token").optional.saveAs("event_token"))
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
        .header("x-dynatrace", "FW4;TSN=CivilOnDynatrace;PSL=CitizenCreateClaimOnDynatrace")
        .body(ElFileBody("bodies/0013_request.json"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  
      //val claimreferences =
        /*======================================================================================
                     * Create Civil Claim - Claim References
          ==========================================================================================*/
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
        /*======================================================================================
                     *  Create Civil Claim - Claim Court
          ==========================================================================================*/
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
        /*======================================================================================
                     *  Create Civil Claim - Postcode
          ==========================================================================================*/
      .group("Civil_CreateClaim_080_ClaimantPostcode") {
      exec(http("Civil_CreateClaim_080_Postcode")
        .get("/api/addresses?postcode=TW33SD")
        .headers(CivilDamagesHeader.headers_104)
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
      //val createclaimclaimant =
        /*======================================================================================
                     *  Create Civil Claim - Claimant Claim
          ==========================================================================================*/
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
        /*======================================================================================
                     *  Create Civil Claim - Mitigant Friend
          ==========================================================================================*/
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
        /*======================================================================================
                     *  Create Civil Claim - Claim Notification
          ==========================================================================================*/
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
  
  
        /*======================================================================================
                     *  Create Civil Claim - Solicitor Organization
          ==========================================================================================*/
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
        /*======================================================================================
                     * Solicitor Postcode
          ==========================================================================================*/
  
      //val postcode1 =
      .group("Civil_CreateClaim_140_SolPostcode") {
      exec(http("Civil_CreateClaim_140_Postcode")
        .get("/api/addresses?postcode=TW33SD")
        .headers(CivilDamagesHeader.headers_104)
        .check(status.in(200, 304))
      )
    }
  
        /*======================================================================================
                     * Sol Service address
          ==========================================================================================*/
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
        /*======================================================================================
                     *  Create Civil Claim - Add another claim
          ==========================================================================================*/
      //add another claim
      .group("Civil_CreateClaim_160_CLAIMAddAnotherClaimantn") {
        exec(http("Civil_CreateClaim_160_CLAIMAddAnotherClaimant")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMAddAnotherClaimant")
          .headers(CivilDamagesHeader.headers_347)
          .body(ElFileBody("bodies//CivilCreateClaim-AddAnotherClaim.json"))
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      //post code
        /*======================================================================================
                     *  Create Civil Claim - Solicitor Postcode
          ==========================================================================================*/
      .group("Civil_CreateClaim_170_SolPostcode") {
        exec(http("Civil_CreateClaim_170_Postcode")
          .get("/api/addresses?postcode=TW33SD")
          .headers(CivilDamagesHeader.headers_104)
          .check(status.in(200, 304))
        )
      }
      
      //val claimdefendant =
        /*======================================================================================
                     *  Create Civil Claim - Claim Defendant
          ==========================================================================================*/
      .group("Civil_CreateClaim_150_CLAIMDefendant") {
      exec(http("Civil_CreateClaim_150_Def")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMDefendant")
        .headers(CivilDamagesHeader.headers_394)
        .body(ElFileBody("bodies/0056_request.json"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
        /*======================================================================================
                     *  Create Civil Claim - Claim Legal Representation
          ==========================================================================================*/
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
  
        /*======================================================================================
                     *  Create Civil Claim - Defendanet Case share
          ==========================================================================================*/
      //val caseshareorgs1 =
      .group("Civil_CreateClaim_170_DefCaseShare") {
      exec(http("Civil_CreateClaim_170_CaseShare")
        .get("/api/caseshare/orgs")
        .headers(CivilDamagesHeader.headers_418)
        .check(status.in(200, 304))
      )
    }
  
      //val claimdefsolicitororg =
        /*======================================================================================
                     *  Create Civil Claim - Def Solicitor Org
          ==========================================================================================*/
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
        /*======================================================================================
                     *  Create Civil Claim - Postcode
          ==========================================================================================*/
      .group("Civil_CreateClaim_170_SolPostcode") {
        exec(http("Civil_CreateClaim_170_Postcode")
          .get("/api/addresses?postcode=TW33SD")
          .headers(CivilDamagesHeader.headers_104)
          .check(status.in(200, 304))
        )
      }
        /*======================================================================================
                     * Create Civil Claim - Defendent Solicitor Service address
          ==========================================================================================*/
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
  
        /*======================================================================================
                     * Create Civil Claim - Claim Of Solicitor Org
          ==========================================================================================*/
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
        /*======================================================================================
                     * Create Civil Claim - Add another defendent
          ==========================================================================================*/
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
        /*======================================================================================
                     * Create Civil Claim - claim type
          ==========================================================================================*/
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
        /*======================================================================================
                     * Create Civil Claim - Injury Type
          ==========================================================================================*/
      .group("Civil_CreateClaim_200_InjuryType") {
        exec(http("Civil_CreateClaim_200_InjuryType")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMPersonalInjuryType")
          .headers(CivilDamagesHeader.headers_514)
          .body(ElFileBody("bodies/0088_request.json"))
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
        /*======================================================================================
                     * Create Civil Claim - ClaimDetails
          ==========================================================================================*/
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
        /*======================================================================================
                     * Create Civil Claim - Documents
          ==========================================================================================*/
      .group("Civil_CreateClaim_220_Documents") {
      exec(http("Civil_CreateClaim_220_Docs")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMuploadParticularsOfClaim")
        .headers(CivilDamagesHeader.headers_534)
        .body(ElFileBody("bodies/0096_request.json"))
        .check(status.in(200, 304)))
    }
      .pause(MinThinkTime, MaxThinkTime)
      // val createclaimupload =
        /*======================================================================================
                     * Create Civil Claim - Claim value
          ==========================================================================================*/
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
        /*======================================================================================
                     * Create Civil Claim - PBA Number
          ==========================================================================================*/
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
        /*======================================================================================
                     * Create Civil Claim - SOT
          ==========================================================================================*/
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
        /*======================================================================================
                     * Create Civil Claim - Submit Claim
          ==========================================================================================*/
      // val submitclaimevent =
      .group("Civil_CreateClaim_280_SubmitClaim") {
      
        exec(http("Civil_CreateClaim_280_015_Submit")
          .post("/data/case-types/CIVIL/cases?ignore-warning=false")
          .headers(CivilDamagesHeader.headers_672)
          .body(ElFileBody("bodies/0112_request.json"))
          .check(jsonPath("#.event_token").optional.saveAs("event_token_claimcreate"))
          .check(jsonPath("#.id").optional.saveAs("caseId"))
          .check(jsonPath("#.legacyCaseReference").optional.saveAs("claimNumber"))
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
        /*======================================================================================
                     * Create Civil Claim - Back To Search pages
          ==========================================================================================*/
      //  val getcasedetailspage=
      .group("Civil_CreateClaim_290_BackToCaseDetailsPage") {
      exec(http("Civil_CreateClaim_290_005_CaseDetails")
        .get("/data/internal/cases/#{caseId}")
        .headers(CivilDamagesHeader.headers_717)
        .check(status.in(200,201,304))
      )
        .exec(http("Civil_CreateClaim_290_010_CaseDetails2")
          .get("/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
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
        /*======================================================================================
                     * Create Civil Claim - Click pay
          ==========================================================================================*/
      // payment fee
      .group("Civil_CreateClaim_300_ClickPay") {
        exec(http("Civil_CreateClaim_300_005_ClickPay")
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
      .group("Civil_CreateClaim_310_PaymentGroups") {
        exec(http("Civil_CreateClaim_310_005_SelectPayGroups")
          .get("/payments/cases/#{caseId}/paymentgroups")
          .headers(CivilDamagesHeader.headers_717)
          .check(status.in(200, 304))
        )
        .exec(http("Civil_CreateClaim_310_010_PaymentOrder")
          .get("/payments/case-payment-orders?case_ids=#{caseId}")
          .headers(CivilDamagesHeader.headers_717)
          .check(jsonPath("#.content[0].orderReference").optional.saveAs("serviceRef"))
          .check(status.in(200, 304))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
        /*======================================================================================
                     * Select PBA
          ==========================================================================================*/
      //pba account selection and pay
  
      .group("Civil_CreateClaim_320_PBAAccounts") {
        exec(http("Civil_CreateClaim_320_005_SelectPBA")
          .get("/payments/pba-accounts")
          .headers(CivilDamagesHeader.headers_717)
          .check(status.in(200, 304))
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
          .check(status.in(200,201,304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      .pause(50)
  
        /*======================================================================================
                     * Create Civil Claim - Notify
          ==========================================================================================*/
      //notify claim step
      
      .group("Civil_CreateClaim_310_NotifyClaimEvent") {
        exec(http("Civil_CreateClaim_310_005_Notify")
          .get("/workallocation/case/tasks/#{caseId}/event/NOTIFY_DEFENDANT_OF_CLAIM/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(CivilDamagesHeader.headers_769)
          .check(status.in(200, 304))
          
        )
       
        exec(http("Civil_CreateClaim_310_010_Notify")
          .get("/data/internal/cases/#{caseId}/event-triggers/NOTIFY_DEFENDANT_OF_CLAIM?ignore-warning=false")
          .headers(CivilDamagesHeader.headers_notify)
          //.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
          .check(status.in(200,201, 304))
          .check(jsonPath("#.event_token").optional.saveAs("event_token_notifyclaimtodef"))
        )
          
          .exec(http("Civil_CreateClaim_310_015_profile")
            .get("/data/internal/profile")
            .headers(CivilDamagesHeader.headers_149)
            .check(status.in(200, 304))
          )
      }
      .pause(MinThinkTime, MaxThinkTime)
        /*======================================================================================
                     * Create Civil Claim - Back To Case after Notify
          ==========================================================================================*/
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
        /*======================================================================================
                     * Submit Notify
          ==========================================================================================*/
      // val claimnotifyeventsubmit =
      .group("Civil_CreateClaim_330_ClaimNotifyEventSubmit") {
      exec(http("Civil_CreateClaim_330_ClaimNotifyEventSubmit")
        .post("/data/cases/#{caseId}/events")
        .headers(CivilDamagesHeader.headers_803)
        .body(ElFileBody("bodies/0191_request.json"))
        .check(status.in(200, 201))
      ).exitHereIfFailed
    }
      //.exec(session => session.set("caseId", "#{caseId}"))
      .pause(MinThinkTime, MaxThinkTime)
        /*======================================================================================
                     * Create Civil Claim - Case Details
          ==========================================================================================*/
      //val backtocasedetailsafterclaimnotify =
      
      .group("Civil_CreateClaim_340_CasedetailsAfterClaimNotify") {
       
        exec(http("Civil_AfterClaimNotify_340_010")
          .get("/data/internal/cases/#{caseId}")
          .headers(CivilDamagesHeader.headers_717)
          .check(status.in(200, 201))
        )
    }
      .pause(MinThinkTime, MaxThinkTime)
        .pause(20)
  
    //end of  claim notify
        /*======================================================================================
                     * Create Civil Claim - Notify Details
          ==========================================================================================*/
  //beginning of notify details
  
  .group("CD_CreateClaim_350_NotifyDetailsEvent") {
    exec(http("Civil_CreateClaim_350_005_NotifyDetails")
      .get("/workallocation/case/tasks/#{caseId}/event/NOTIFY_DEFENDANT_OF_CLAIM/caseType/CIVIL/jurisdiction/CIVIL")
      .headers(CivilDamagesHeader.headers_769)
      .check(status.in(200, 201, 304))
    
    )
      .exec(http("Civil_CreateClaim_350_010_NotifyDetailCreate")
        .get("/data/internal/cases/#{caseId}/event-triggers/NOTIFY_DEFENDANT_OF_CLAIM_DETAILS?ignore-warning=false")
        .headers(CivilDamagesHeader.headers_notify)
        //.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
        .check(status.in(200, 304))
        .check(jsonPath("#.event_token").optional.saveAs("event_token_notifyclaimdetail"))
      )
      
      .exec(http("Civil_CreateClaim_350_010_profile")
        .get("/data/internal/profile")
        .headers(CivilDamagesHeader.headers_149)
        //.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-user-profile.v2+json;charset=UTF-8")
        
        .check(status.in(200, 304))
      )
  }
    .pause(MinThinkTime, MaxThinkTime)
        /*======================================================================================
                     * Create Civil Claim - Detals Upload
          ==========================================================================================*/
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
        /*======================================================================================
                     * Create Civil Claim - event submit
          ==========================================================================================*/
    // val notifyclaimdetailseventsubmit=
    .group("CD_CreateClaim_370_NotifyDetailsEventSubmit") {
      exec(http("CD_CreateClaim_370_EventSubmit")
        .post("/data/cases/#{caseId}/events")
        .headers(CivilDamagesHeader.headers_886)
        // .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
        .body(ElFileBody("bodies/0214_request.json"))
        .check(status.in(200, 201))
      )
        .exec(http("CD_CreateClaim_380_010_case")
          .get("/data/internal/cases/#{caseId}")
          .headers(CivilDamagesHeader.headers_717)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
          
          .check(status.in(200,201,204,304))
        )
    }
    .pause(MinThinkTime, MaxThinkTime)
  
        /*======================================================================================
                     * Create Civil Claim - Notify Details
          ==========================================================================================*/
    // val returntocasedetailsafternotifydetails =
    .group("CD_CreateClaim_380_ReturnToCaseDetailsAfterNotifyDetails") {
      exec(http("CD_CreateClaim_380_005_NotifyDetails")
        .post("/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
        .headers(CivilDamagesHeader.headers_894)
        .body(StringBody("{\"searchRequest\":{\"ccdId\":\"#{caseId}\",\"eventId\":\"NOTIFY_DEFENDANT_OF_CLAIM_DETAILS\",\"jurisdiction\":\"CIVIL\",\"caseTypeId\":\"UNSPECIFIED_CLAIMS\"}}"))
        .check(status.in(200,204,201,304))
      )

        

        .exec(http("CD_CreateClaim_380_010_case")
          .get("/data/internal/cases/#{caseId}")
          .headers(CivilDamagesHeader.headers_717)
          .check(status.in(200, 204,304))
        )
    }
    .pause(MinThinkTime, MaxThinkTime)
        .pause(20)
  
        /*======================================================================================
                     * Create Civil Claim - Assign case - need to change the token every time we create a case
          ==========================================================================================*/
  // following is for assign the case to defendent
        .group("CIVIL_AssignCase_000_AssignCase") {
          exec(http("CIVIL_AssignCase_000_AssignCase")
            .post("http://civil-service-perftest.service.core-compute-perftest.internal/testing-support/assign-case/#{caseId}/RESPONDENTSOLICITORONE")
            //   .get( "/cases/searchCases?start_date=#{randomStartDate}&end_date=#{randomEndDate}")
            // .get( "/cases/searchCases?start_date=2022-01-13T00:00:00&end_date=2023-04-16T15:38:00")
            .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJraWQiOiI4cDJpajg2S0pTeENKeGcveUovV2w3TjcxMXM9IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJobWN0cy5jaXZpbCtvcmdhbmlzYXRpb24uMS5zb2xpY2l0b3IuMUBnbWFpbC5jb20iLCJjdHMiOiJPQVVUSDJfU1RBVEVMRVNTX0dSQU5UIiwiYXV0aF9sZXZlbCI6MCwiYXVkaXRUcmFja2luZ0lkIjoiNGJjOWEzOTAtZTZjZi00YTQxLWI1NGYtZDA2NmZjNDc0ODY3LTgwNTYxOTk2Iiwic3VibmFtZSI6ImhtY3RzLmNpdmlsK29yZ2FuaXNhdGlvbi4xLnNvbGljaXRvci4xQGdtYWlsLmNvbSIsImlzcyI6Imh0dHBzOi8vZm9yZ2Vyb2NrLWFtLnNlcnZpY2UuY29yZS1jb21wdXRlLWlkYW0tcGVyZnRlc3QuaW50ZXJuYWw6ODQ0My9vcGVuYW0vb2F1dGgyL3JlYWxtcy9yb290L3JlYWxtcy9obWN0cyIsInRva2VuTmFtZSI6ImFjY2Vzc190b2tlbiIsInRva2VuX3R5cGUiOiJCZWFyZXIiLCJhdXRoR3JhbnRJZCI6IjVkYURnVVd6MDg1b1l2Zm5fTk96MnNkUkVOayIsIm5vbmNlIjoiSWZyYWdhaEJtZEQ1WGhxX2dwNTRaeVVmRGF2dkliT0ZhM1hZTUsxYjhHQSIsImF1ZCI6Inh1aXdlYmFwcCIsIm5iZiI6MTY4OTY5MjEyOCwiZ3JhbnRfdHlwZSI6ImF1dGhvcml6YXRpb25fY29kZSIsInNjb3BlIjpbIm9wZW5pZCIsInByb2ZpbGUiLCJyb2xlcyIsImNyZWF0ZS11c2VyIiwibWFuYWdlLXVzZXIiLCJzZWFyY2gtdXNlciJdLCJhdXRoX3RpbWUiOjE2ODk2OTIxMjYsInJlYWxtIjoiL2htY3RzIiwiZXhwIjoxNjg5NzIwOTI4LCJpYXQiOjE2ODk2OTIxMjgsImV4cGlyZXNfaW4iOjI4ODAwLCJqdGkiOiJkMTJaN0pGOW05dzBPQlB5VHZKRXFaaEFqRWcifQ.cyRqEj1A7bhor-k39UADN-fOjbOEKFhhHFcfhDafjf3wRxEH8Dt0un9JhjCNuOxzLCP1VO3aKeXlpWq1jrD8LyYWzszbxu67bfgK7uPSDIJlN1RIYNATRibWGUpMVzUmVIcvVlPnULcQgzLhebltJGmcHXUuoTS1egR5gNTwdxvLIgAlX5Q8vThQMgRZrfzJy_XJ-ajUGBU7ec1XTWdmrImbhYx99ME260ewBkUL7cd9vcuA7rPpVjxLujNbrxpftQkAS_h0ur04_aPgkVhDQ4cQPIJoJuEscgECFS_cmWmUQMpA2x7ox-5KXHRrwNjLt61jMSVKLm42xe4FcQ92Gw")
            .header("Content-Type", "application/json")
            .header("Accept", "*/*")
            .check(status.in(200, 201))
          )
        }
        .pause(MinThinkTime, MaxThinkTime)
  
        //Deepak - Cases that make the final step
        .exec { session =>
          val fw = new BufferedWriter(new FileWriter("FinalcaseIds.csv", true))
          try {
            fw.write(session("caseId").as[String] + "\r\n")
          } finally fw.close()
          session
        }
  
  
  
}
