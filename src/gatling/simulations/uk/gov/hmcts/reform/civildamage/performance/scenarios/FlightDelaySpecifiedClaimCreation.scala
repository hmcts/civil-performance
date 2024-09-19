
package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef.{http, _}
import uk.gov.hmcts.reform.civildamage.performance.scenarios.ClaimCreationLRvsLR.{BaseURL, MaxThinkTime, MinThinkTime}
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, Common, Environment, Headers}

import java.io.{BufferedWriter, FileWriter}

object FlightDelaySpecifiedClaimCreation {
  
  val BaseURL = Environment.baseURL
  val IdAMURL = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime
  val postcodeFeeder = csv("postcodes.csv").random
  
  //def run(implicit postHeaders: Map[String, String]): ChainBuilder = {
  /*======================================================================================
               * Below run is for create Civil claim for specified category
    ==========================================================================================*/
  val run =
  exec(_.setAll(
    "Idempotencynumber" -> (Common.getIdempotency()),
    "randomString" -> (Common.randomString(7)),
    "applicantFirstName" -> ("First" + Common.randomString(5)),
    "applicantLastName" -> ("Last" + Common.randomString(5)),
    "applicantFirstName" -> ("App2" + Common.randomString(5)),
    "applicantLastName" -> ("Test" + Common.randomString(5)),
    "birthDay" -> Common.getDay(),
    "birthMonth" -> Common.getMonth(),
    "birthYear" -> Common.getYear(),
    "requestId" -> Common.getRequestId(),
    "Idempotencynumber" -> Common.getIdempotency())
  )

    
      /*======================================================================================
                   *   Civil UI Claim - Initiate Claim
        ==========================================================================================*/
      .group("CUI_CreateClaim_030_CreateCase") {
        exec(http("CUI_CreateClaim_030_005_CreateCase")
          .get("/aggregated/caseworkers/:uid/jurisdictions?access=create")
          .headers(CivilDamagesHeader.headers_104)
          .check(status.in(200, 304))
          .check(substring("CREATE_CLAIM_SPEC"))
        ).exitHereIfFailed
      }
      .pause(MinThinkTime, MaxThinkTime)
      /*======================================================================================
                   * Civil UI Claim - Create Claim
        ==========================================================================================*/
      //val startCreateClaim =
      .group("CUI_CreateClaim_040_StartCreateCase1") {
        exec(http("CUI_CreateClaim_040_005_StartCreateCase1")
          .get("/data/internal/case-types/CIVIL/event-triggers/CREATE_CLAIM_SPEC?ignore-warning=false")
          .headers(CivilDamagesHeader.headers_140)
          .check(status.is(200))
          .check(substring("Legal representatives: specified civil money claims service"))
          .check(jsonPath("$.event_token").optional.saveAs("event_token"))
        )
          .pause(MinThinkTime, MaxThinkTime)
      }
      
      //val before you start =
      /*======================================================================================
                   * Civil UI Claim -Before You Start
        ==========================================================================================*/
      .group("CUI_CreateClaim_050_BeforeYouStart") {
        exec(http("CUI_CreateClaim_005_BeforeYoustart")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECCheckList")
          .headers(CivilDamagesHeader.headers_163)
          .body(ElFileBody("bodies/flightdelayclaim/CUICreateClaim-BeforeStart.json"))
          .check(substring("SPECCheckList"))
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      /*======================================================================================
                   Civil UI Claim -  Claim Eligibility
        ==========================================================================================*/
      //val claimliability =
      .group("CUI_CreateClaim_060_CLAIMEligibility") {
        exec(http("CUI_CreateClaim_060_005_Eligibility")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECEligibility")
          .headers(CivilDamagesHeader.headers_163)
          .body(ElFileBody("bodies/flightdelayclaim/CivilCreateClaim-Eligibility.json"))
          .check(substring("CREATE_CLAIM_SPECEligibility"))
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      /*======================================================================================
                   * Civil UI Claim - Claim Reference
        ==========================================================================================*/
      //val claimreferences =
      .group("CUI_CreateClaim_070_CLAIMReferences") {
        exec(http("CUI_CreateClaim_070_005_Reference")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECReferences")
          .headers(CivilDamagesHeader.headers_192)
          .body(ElFileBody("bodies/flightdelayclaim/CivilCreateClaim-References.json"))
          .check(substring("CREATE_CLAIM_SPECReferences"))
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
    
      /*======================================================================================
                   * Civil UI Claim - Claim Claimant
        ==========================================================================================*/
      .group("CUI_CreateClaim_080_CLAIMClaimant") {
        feed(postcodeFeeder)
        .exec(http("CUI_CreateClaim_080_005_Claimant")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimant")
          .headers(CivilDamagesHeader.headers_258)
          .body(ElFileBody("bodies/flightdelayclaim/CivilCreateClaim-Claimant.json"))
          .check(substring("CREATE_CLAIM_SPECClaimant"))
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
                   *Civil UI Claim - Claim Claimant Add Another Claimant
        ==========================================================================================*/
      
      //add another claim
      .group("CUI_CreateClaim_090_CLAIMAddAnotherClaimant") {
        exec(http("CUI_CreateClaim_090_005_CLAIMAddAnotherClaimant")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECAddAnotherClaimant")
          .headers(CivilDamagesHeader.headers_347)
          .body(ElFileBody("bodies/flightdelayclaim/CivilCreateClaim-AddAnotherClaim.json"))
          .check(substring("CREATE_CLAIM_SPECAddAnotherClaimant"))
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      //val createclaimantnotifications =
      /*======================================================================================
                   * Civil UI Claim - Claim Notification
        ==========================================================================================*/
      .group("CUI_CreateClaim_100_CLAIMNotifications") {
        exec(http("CUI_CreateClaim_100_005_Notifications")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECNotifications")
          .headers(CivilDamagesHeader.headers_295)
          .body(ElFileBody("bodies/flightdelayclaim/CivilCreateClaim-Notifications.json"))
          .check(substring("CREATE_CLAIM_SPECNotifications"))
          .check(status.in(200, 304))
        )
          
         /* //val caseshareorgs =
          /*======================================================================================
                       * Civil UI Claim - Claim case Share
            ==========================================================================================*/
          
          .exec(http("CUI_CreateClaim_100_010_CaseShare")
            .get("/api/caseshare/orgs")
            .headers(CivilDamagesHeader.headers_418)
            .check(status.in(200, 304))
          )*/
      }
      .pause(MinThinkTime, MaxThinkTime)
    
      /*======================================================================================
                   * Civil UI Claim - Claim Solicitor Organization
        ==========================================================================================*/
      .group("CUI_CreateClaim_110_SolicitorOrganisation") {
        exec(http("CUI_CreateClaim_110_005_SolicitorOrganisation")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimantSolicitorOrganisation")
          .headers(CivilDamagesHeader.headers_347)
          .body(ElFileBody("bodies/flightdelayclaim/CivilCreateClaim-Organisation.json"))
          .check(substring("CREATE_CLAIM_SPECClaimantSolicitorOrganisation"))
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
          /*======================================================================================
                   * Civil UI Claim - Claim- Solicitor Correspondance Address
        ==========================================================================================*/
      .group("CUI_CreateClaim_120_SolicitorCorrespondanceAddress") {
        exec(http("CUI_CreateClaim_120_005_SolicitorCorrespondanceAddress")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECspecCorrespondenceAddress")
          .headers(CivilDamagesHeader.headers_347)
          .body(ElFileBody("bodies/flightdelayclaim/CivilCreateClaim-Correspondance.json"))
          .check(substring("CREATE_CLAIM_SPECspecCorrespondenceAddress"))
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
                   * Civil UI Claim - Claim -Defendant Details
        ==========================================================================================*/
      
      .group("CUI_CreateClaim_130_CLAIMDefendant") {
        exec(http("CUI_CreateClaim_130_005_Def")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECDefendant")
          .headers(CivilDamagesHeader.headers_394)
          .body(ElFileBody("bodies/flightdelayclaim/CivilCreateClaim-DefDetails.json"))
          .check(substring("CREATE_CLAIM_SPECDefendant"))
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      //val claimLegalRep =
      /*======================================================================================
                   * Civil UI Claim - Claim Def Legal Representation
        ==========================================================================================*/
      .group("CUI_CreateClaim_140_LegalRepresentation") {
        exec(http("CUI_CreateClaim_140_005_Rep")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECLegalRepresentation")
          .headers(CivilDamagesHeader.headers_413)
          .body(ElFileBody("bodies/flightdelayclaim/CivilCreateClaim-DefRep.json"))
          .check(substring("CREATE_CLAIM_SPECLegalRepresentation"))
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
    /*======================================================================================
                      * Civil UI Claim - Claim Def Solicitor Organisation
           ==========================================================================================*/
      
      .group("CUI_CreateClaim_180_CLAIMDefendantSolicitorOrganisation") {
      exec(http("CUI_CreateClaim_180_Org")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECDefendantSolicitorOrganisation")
        .headers(CivilDamagesHeader.headers_469)
        .body(ElFileBody("bodies/flightdelayclaim/CivilCreateClaim-DefSolOrg.json"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  
  
    /*======================================================================================
                      * Civil UI Claim - Claim Solicitor Organisation email
           ==========================================================================================*/
     
      .group("CUI_CreateClaim_190_CLAIMDefendantSolicitorEmail") {
      exec(http("CUI_CreateClaim_190_SolEmail")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECDefendantSolicitorEmail")
        .headers(CivilDamagesHeader.headers_491)
        .body(ElFileBody("bodies/flightdelayclaim/CivilCreateClaim-SolEmail.json"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
    
    
      
      //add another defendant
      // val claimdefsolicitororgemail =
      .group("CUI_CreateClaim_190_CLAIMDefendantSolicitorEmail") {
        exec(http("CUI_CreateClaim_190_SolEmail")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECspecRespondentCorrespondenceAddress")
          .headers(CivilDamagesHeader.headers_491)
          .body(ElFileBody("bodies/flightdelayclaim/CivilCreateClaim-DefCorrAddress.json"))
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      /*======================================================================================
                   * Civil UI Claim - Claim - Add another def - No
        ==========================================================================================*/
      .group("CUI_CreateClaim_150_DefendantSolicitorEmail") {
        exec(http("CUI_CreateClaim_150_005_SolEmail")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECAddAnotherDefendant")
          .headers(CivilDamagesHeader.headers_491)
          .body(ElFileBody("bodies/flightdelayclaim/CivilCreateClaim-AddAnotherDef.json"))
          .check(substring("CREATE_CLAIM_SPECAddAnotherDefendant"))
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
    /*======================================================================================
                      * Civil UI Claim - Flight Delay Claim
           ==========================================================================================*/
      
        // val claimtype =
      .group("CUI_CreateClaim_200_FlightDelayClaimType") {
      exec(http("CUI_CreateClaim_200_FlightDelayClaimType")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECFlightDelayClaim")
        .headers(CivilDamagesHeader.headers_514)
        .body(ElFileBody("bodies/flightdelayclaim/CivilCreateClaim-FlightdelayClaimType.json"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
      
     


    


      // val createclaimdetail =
      /*======================================================================================
                   * Civil UI Claim - Claim Details - from here flight details added into the payload
        ==========================================================================================*/
      .group("CUI_CreateClaim_160_CLAIMDetails") {
        exec(http("CUI_CreateClaim_160_005_Details")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECDetails")
          .headers(CivilDamagesHeader.headers_534)
          .body(ElFileBody("bodies/flightdelayclaim/CivilCreateClaim-claimdetails.json"))//skipped upload
          .check(substring("CREATE_CLAIM_SPECDetails"))
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      // val postclaimdocs =
      /*======================================================================================
                   * Civil UI Claim - Manual Claim Docs
        ==========================================================================================*/
      
      .group("CUI_CreateClaim_170_Documents") {
        exec(http("CUI_CreateClaim_170_005_Docs")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECUploadClaimDocument")
          .headers(CivilDamagesHeader.headers_534)
          .body(ElFileBody("bodies/flightdelayclaim/CivilCreateClaim-ClaimDocs.json"))
          .check(substring("CREATE_CLAIM_SPECUploadClaimDocument"))
          .check(status.in(200, 304)))
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      // val claim timeline =
      /*======================================================================================
                   * Civil UI Claim - Claim Timeline
        ==========================================================================================*/
      
      .group("CUI_CreateClaim_180_ClaimTimeline") {
        exec(http("CUI_CreateClaim_180_005_ClaimTimeline")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimTimeline")
          .headers(CivilDamagesHeader.headers_534)
          .body(ElFileBody("bodies/flightdelayclaim/CivilCreateClaim-ClaimTimeline.json"))
          .check(substring("CREATE_CLAIM_SPECClaimTimeline"))
          .check(status.in(200, 304)))
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      // val claim evidencelist =
      /*======================================================================================
                   * Civil UI Claim - Claim Evidence List
        ==========================================================================================*/
      
      .group("CUI_CreateClaim_190_EvidenceList") {
        exec(http("CUI_CreateClaim_190_005_EvidenceList")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECEvidenceList")
          .headers(CivilDamagesHeader.headers_534)
          .body(ElFileBody("bodies/flightdelayclaim/CivilCreateClaim-EvidenceList.json"))
          .check(substring("CREATE_CLAIM_SPECEvidenceList"))
          .check(status.in(200, 304)))
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      
      
      // val createclaimvalue =
      /*======================================================================================
                   * Civil UI Claim - Claim Value
        ==========================================================================================*/
      .group("CUI_CreateClaim_200_CLAIMClaimValue") {
        exec(http("CUI_CreateClaim_200_005_ClaimValue")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimAmount")
          .headers(CivilDamagesHeader.headers_595)
          .body(ElFileBody("bodies/flightdelayclaim/CivilCreateClaim-ClaimAmount.json"))
          .check(substring("CREATE_CLAIM_SPECClaimAmount"))
          .check(jsonPath("$.data.claimAmountBreakup[0].id").saveAs("claimAmountBreakupId"))
         // .check(jsonPath("$.data.speclistYourEvidenceList[0].id").saveAs("speclistYourEvidenceListId"))
          .check(jsonPath("$.data.timelineOfEvents[0].id").saveAs("timelineOfEventsId"))
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
            /*======================================================================================
                        * Civil UI Claim - Claim Amount Details
             ==========================================================================================*/
      .group("CUI_CreateClaim_210_ClaimAmountDetails") {
        exec(http("CUI_CreateClaim_210_005_ClaimAmountDetails")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimAmountDetails")
          .headers(CivilDamagesHeader.headers_595)
          .body(ElFileBody("bodies/flightdelayclaim/CivilCreateClaim-ClaimAmountDetails.json"))
          .check(substring("CREATE_CLAIM_SPECClaimAmountDetails"))
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      // val claiminterest=
      /*======================================================================================
                   * Civil UI Claim - Claim Amount Interest
        ==========================================================================================*/
      .group("CUI_CreateClaim_220_ClaimInterest") {
        exec(http("CUI_CreateClaim_220_005_ClaimInterest")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimInterest")
          .headers(CivilDamagesHeader.headers_595)
          .body(ElFileBody("bodies/flightdelayclaim/CivilCreateClaim-ClaimInterest.json"))
          .check(substring("CREATE_CLAIM_SPECClaimInterest"))
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      // val claiminterestsummary=
      /*======================================================================================
                   * Civil UI Claim - Claim Amount Interest Summary
        ==========================================================================================*/
      .group("CUI_CreateClaim_230_CLAIMClaimInterestSummery") {
        exec(http("CUI_CreateClaim_230_005_CLAIMClaimInterestSummary")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECInterestSummary")
          .headers(CivilDamagesHeader.headers_595)
          .body(ElFileBody("bodies/flightdelayclaim/CivilCreateClaim-ClaimInterestSummary.json"))
          .check(jsonPath("$.data.claimFee.calculatedAmountInPence").saveAs("calculatedAmountInPence"))
          .check(jsonPath("$.data.claimFee.code").saveAs("claimFeeCode"))
          .check(jsonPath("$.data.claimFee.version").saveAs("version"))
          .check(substring("CREATE_CLAIM_SPECInterestSummary"))
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      // val createclaimpbanumber =
      /*======================================================================================
                   * Civil UI Claim - Claim PBANumber
        ==========================================================================================*/
      .group("CUI_CreateClaim_240_CLAIMPbaNumber") {
        exec(http("CUI_CreateClaim_240_005_PBANumber")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECPbaNumber")
          .headers(CivilDamagesHeader.headers_610)
          .body(ElFileBody("bodies/flightdelayclaim/CivilCreateClaim-PbaNumber.json"))
          .check(substring("CREATE_CLAIM_SPECPbaNumber"))
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      //val createclaimstatementoftruth =
      /*======================================================================================
                   * Civil UI Claim - Statement Of Truth
        ==========================================================================================*/
      .group("CUI_CreateClaim_250_StatementOfTruth") {
        exec(http("CUI_CreateClaim_250_005_SOT")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECStatementOfTruth")
          .headers(CivilDamagesHeader.headers_650)
          .body(ElFileBody("bodies/flightdelayclaim/CivilCreateClaim-SOT.json"))
          .check(substring("CREATE_CLAIM_SPECStatementOfTruth"))
          .check(status.in(200, 304))
        )
          .exec(http("CUI_CreateClaim_280_010_CaseShareOrgs")
            .get("/api/caseshare/orgs")
            .headers(CivilDamagesHeader.headers_658)
            .check(status.in(200, 304))
          )
      }
      .pause(MinThinkTime, MaxThinkTime)
      // val submitclaimevent =
      /*======================================================================================
                   * Civil UI Claim - Claim Submit
        ==========================================================================================*/
      .group("CUI_CreateClaim_260_SubmitClaim") {
        
        exec(http("CUI_CreateClaim_260_005_Submit")
          .post(BaseURL + "/data/case-types/CIVIL/cases?ignore-warning=false")
        //  .headers(CivilDamagesHeader.MoneyClaimSubmitHeader)
          .headers(CivilDamagesHeader.CUI_Submit)
          .body(ElFileBody("bodies/flightdelayclaim/CivilCreateClaim-Submit.json"))
          .check(jsonPath("$.event_token").optional.saveAs("event_token_claimcreate"))
          .check(jsonPath("$.id").optional.saveAs("caseId"))
          .check(jsonPath("$.legacyCaseReference").optional.saveAs("claimNumber"))
          .check(status.in(200, 201))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      .pause(50)
      
      //  val getcasedetailspage=
      /*======================================================================================
                   * Civil UI Claim - Refresh Claim Details
        ==========================================================================================*/
      .group("CUI_CreateClaim_270_BackToCaseDetailsPage") {
        exec(http("CUI_CreateClaim_270_005_CaseDetails")
          .get("/data/internal/cases/${caseId}")
          .headers(CivilDamagesHeader.headers_717)
          .check(status.in(200, 201,204,304))
        )
          .exec(http("CUI_CreateClaim_270_010_CaseDetails2")
            .post("/api/role-access/roles/manageLabellingRoleAssignment/${caseId}")
            .headers(CivilDamagesHeader.headers_717)
            .body(ElFileBody("bodies/flightdelayclaim/CivilAccessClaim-RollAccess.json"))
            .check(status.in(200, 201,204, 304))
          )
          .exec(http("CUI_CreateClaim_270_015_CaseDetails3")
            .get("/api/wa-supported-jurisdiction/get")
            .headers(CivilDamagesHeader.headers_717)
            .check(status.in(200, 201, 204, 304))
          )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  /*======================================================================================
    * Making a Payment using Payment Service Request - click on Service Request
    ======================================================================================*/
  val PBSPayment =
  // payment pba fee payment
    group("CUI_CreateClaim_280_ServiceRequestForPay") {
      exec(http("CUI_CreateClaim_280_005_ServiceRequestToPay")
        .get("/pay-bulkscan/cases/#{caseId}")
        .headers(Headers.commonHeader)
        .check(status.in(200, 304))
      )
    }
      
      // https://manage-case.perftest.platform.hmcts.net/payments/pba-accounts?????
      /*======================================================================================
            * Click On pay from Service Page
       ==========================================================================================*/
      
      .group("CUI_CreateClaim_290_ClickOnPay") {
        exec(http("CUI_CreateClaim_290_005_ClickOnPay")
          .get("/payments/cases/#{caseId}/paymentgroups")
          .headers(Headers.commonHeader)
          .check(jsonPath("$..payment_group_reference").optional.saveAs("serviceRef"))
          .check(status.in(200, 201, 304))
        )
          
        /*  .exec(http("CUI_CreateClaim_290_010_Paycases")
            .get("/payments/case-payment-orders?case_ids=${caseId}")
            .headers(Headers.commonHeader)
            .check(jsonPath("$.content[0].orderReference").optional.saveAs("serviceRef"))
            .check(status.in(200, 201, 304))
          )*/
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      //choose PBA Payments
      .group("CUI_CreateClaim_300_ChoosePBAPayments") {
        exec(http("CUI_CreateGA_300_005_ChoosePBAPayments")
          .get("/payments/pba-accounts")
          .headers(Headers.commonHeader)
          .check(status.in(200, 201, 304))
          
        )
      }
      
      //Final Payment
      
      .group("CUI_CreateClaim_310_FinalPayment") {
        exec(http("CUI_CreateGA_310_005_FinalPay")
          .post("/payments/service-request/#{serviceRef}/pba-payments")
          .headers(Headers.commonHeader)
          .body(ElFileBody("bodies/flightdelayclaim/CivilCreateClaim-PBAPayment.json"))
          .check(substring("success"))
          .check(status.in(200, 201, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      .pause(50)
      
      
       /* //Deepak - Cases that make the final step
        .exec { session =>
          val fw = new BufferedWriter(new FileWriter("CUICases.csv", true))
          try {
            fw.write(session("caseId").as[String] + "\r\n")
          } finally fw.close()
          session
        }*/
  
  
  /*======================================================================================
                 * Create Specified Claim - Start Event 'Respond to Claim'
      ==========================================================================================*/
  
       val RespondToClaim =
  
         group("Civil_CreateClaim_330_BackToCaseDetailsPage") {
           exec(http("Civil_CreateClaim_330_005_CaseDetails")
             .get("https://manage-case.perftest.platform.hmcts.net/data/internal/cases/#{caseId}")
             .headers(CivilDamagesHeader.headers_717)
             .check(substring("Civil"))
             .check(status.in(200, 201, 304)))
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
               .get("/workallocation/case/tasks/1712847795410423/event/DEFENDANT_RESPONSE_SPEC/caseType/CIVIL/jurisdiction/CIVIL")
               .headers(CivilDamagesHeader.MoneyClaimNav)
               .check(substring("task_required_for_event"))
             )
        
               .exec(http("XUI_CreateClaim_420_010_RespondToClaim")
                 .get(BaseURL + "/data/internal/cases/1712847795410423/event-triggers/DEFENDANT_RESPONSE_SPEC?ignore-warning=false")
                 .headers(CivilDamagesHeader.headers_notify)
                 .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
                 .check(substring("DEFENDANT_RESPONSE"))
                 /*.check(jsonPath("$.case_fields[75].value.partyID").saveAs("repPartyID"))
                 .check(jsonPath("$.case_fields[75].value.partyName").saveAs("partyName"))*/
                 .check(jsonPath("$.event_token").saveAs("event_token"))
               )
               .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))
      
           }
           .pause(MinThinkTime, MaxThinkTime)
  
  
           /*======================================================================================
        * Create Civil Claim - Respond to Claim Defendant Details - Continue
     ==========================================================================================*/
           // val returntocasedetailsafternotifydetails =
           .group("XUI_CreateClaim_430_RespondDefDetails") {
             exec(http("XUI_CreateClaim_430_005_RespondDefDetails")
               .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECRespondentCheckList")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
               .body(ElFileBody("bodies/specifiedclaimresponses/RespondDefCheckList.json"))
               .check(substring("RESPONDENTSOLICITORONE"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
    
    
    
           /*======================================================================================
        * Create Civil Claim - Respond to Claim Defendant Details
     ==========================================================================================*/
           // val returntocasedetailsafternotifydetails =
           .group("XUI_CreateClaim_430_RespondDefDetails") {
             exec(http("XUI_CreateClaim_430_005_RespondDefDetails")
               .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECResponseConfirmNameAddress")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
               .body(ElFileBody("bodies/specifiedclaimresponses/RespondDefDetails.json"))
               .check(substring("RESPONDENTSOLICITORONE"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
  
  
           /*======================================================================================
        * Create Civil Claim - Respond to Claim Defendant Details
     ==========================================================================================*/
           // val returntocasedetailsafternotifydetails =
           .group("XUI_CreateClaim_430_RespondDefDetails") {
             exec(http("XUI_CreateClaim_430_005_RespondDefDetails")
               .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSERespondentResponseType")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
               .body(ElFileBody("bodies/specifiedclaimresponses/RespondDefDetails.json"))
               .check(substring("RESPONDENTSOLICITORONE"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
    
    
    
           /*======================================================================================
     * Create Civil Claim - Respond to Claim Defendant Choice - Reject
     ==========================================================================================*/
    
           .group("XUI_CreateClaim_440_RespondDefChoice") {
             exec(http("XUI_CreateClaim_440_005_RespondDefChoice")
               .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECRespondentResponseTypeSpec")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .body(ElFileBody("bodies/specifiedclaimresponses/RespondDefChoice.json"))
               .check(substring("RESPONDENTSOLICITORONE"))
               //change when you get around to it
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
  
  
           /*======================================================================================
     * Create Civil Claim - Respond to Claim Defendant Choice - Defence route
     ==========================================================================================*/
  
           .group("XUI_CreateClaim_440_RespondDefChoice") {
             exec(http("XUI_CreateClaim_440_005_RespondDefChoice")
               .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECdefenceRoute")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .body(ElFileBody("bodies/specifiedclaimresponses/RespondDefRoute.json"))
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
               .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECUpload")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .body(ElFileBody("bodies/specifiedclaimresponses/RespSpecUpload.json"))
               .check(substring("respondentSolicitor1Reference"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
  
  
  
           /*======================================================================================
     * Create Civil Claim - Adding Timeline
     ==========================================================================================*/
  
           .group("XUI_CreateClaim_450_YourFileReference") {
             exec(http("XUI_CreateClaim_450_005_YourFileReference")
               .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECHowToAddTimeline")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .body(ElFileBody("bodies/specifiedclaimresponses/RespSpecTimeline.json"))
               .check(substring("respondent1ClaimResponseTypeForSpec"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
  
  
           /*======================================================================================
     * Create Civil Claim - Adding manual Timeline
     ==========================================================================================*/
  
           .group("XUI_CreateClaim_450_YourFileReference") {
             exec(http("XUI_CreateClaim_450_005_YourFileReference")
               .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECHowToAddTimelineManual")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .body(ElFileBody("bodies/specifiedclaimresponses/RespSpecTimelineManual.json"))
               .check(substring("specResponseTimelineOfEvents"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
  
           /*======================================================================================
       * Create Civil Claim - SPec mediation
       ==========================================================================================*/
  
           .group("XUI_CreateClaim_450_YourFileReference") {
             exec(http("XUI_CreateClaim_450_005_YourFileReference")
               .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECMediation")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .body(ElFileBody("bodies/specifiedclaimresponses/RespSpecMediation.json"))
               .check(substring("specResponseTimelineOfEvents"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
           
           
         
           /*======================================================================================
     * Create Civil Claim - Do you want to use an expert?
     ==========================================================================================*/
    
           .group("XUI_CreateClaim_500_UseAnExpert") {
             exec(http("XUI_CreateClaim_500_005_UseAnExpert")
               .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECSmallClaimExperts")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .body(ElFileBody("bodies/specifiedclaimresponses/UseAnExpert.json"))
               .check(substring("responseClaimExpertSpecRequired"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
    
    
    
           /*======================================================================================
     * Create Civil Claim - Are there any witnesses who should attend the hearing?
     ==========================================================================================*/
    
           .group("XUI_CreateClaim_510_AnyWitnesses") {
             exec(http("XUI_CreateClaim_510_005_AnyWitnesses")
               .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECSmallClaimWitnesses")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .body(ElFileBody("bodies/specifiedclaimresponses/WitnessesToAppear.json"))
               .check(substring("respondent1ClaimResponseTypeForSpec"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
    
    
    
           /*======================================================================================
     * Create Civil Claim - Welsh language
     ==========================================================================================*/
    
           .group("XUI_CreateClaim_520_WelshLanguage") {
             exec(http("XUI_CreateClaim_520_005_WelshLanguage")
               .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECLanguage")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .body(ElFileBody("bodies/specifiedclaimresponses/WelshLanguage.json"))
               .check(substring("DEFENDANT_RESPONSE_SPECLanguage"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
    
    
           /*======================================================================================
     * Create Civil Claim - Hearing Availability
     ==========================================================================================*/
    
           .group("XUI_CreateClaim_530_HearingAvailability") {
             exec(http("XUI_CreateClaim_530_005_HearingAvailability")
               .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECSmaillClaimHearing")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .body(ElFileBody("bodies/specifiedclaimresponses/HearingAvailability.json"))
               .check(substring("DEFENDANT_RESPONSEHearing"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
    
    
    
         
           /*======================================================================================
     * Create Civil Claim - Court location code
     ==========================================================================================*/
    
           .group("XUI_CreateClaim_560_CourtLocationCode") {
             exec(http("XUI_CreateClaim_560_005_CourtLocationCode")
               .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECRequestedCourtLocationLRspec")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .body(ElFileBody("bodies/specifiedclaimresponses/ResCourtLocationCode.json"))
               .check(substring("respondent2DQRemoteHearingLRspec"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
    
           /*======================================================================================
     * Create Civil Claim - Support with access needs
     ==========================================================================================*/
    
           .group("XUI_CreateClaim_570_SupportAccessNeeds") {
             exec(http("XUI_CreateClaim_570_005_SupportAccessNeeds")
               .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECHearingSupport")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .body(ElFileBody("bodies/specifiedclaimresponses/SupportAccessNeeds.json"))
               .check(substring("respondent2DQHearingSupport"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
    
    
    
           /*======================================================================================
     * Create Civil Claim - Vulnerability Questions
     ==========================================================================================*/
    
           .group("XUI_CreateClaim_580_VulnerabilityQuestions") {
             exec(http("XUI_CreateClaim_580_005_VulnerabilityQuestions")
               .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECVulnerabilityQuestions")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .body(ElFileBody("bodies/specifiedclaimresponses/VulnerabilityQuestions.json"))
               .check(substring("respondent1DQVulnerabilityQuestions"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
    
    
           /*======================================================================================
     * Create Civil Claim - Respond to claim SoT
     ==========================================================================================*/
    
           .group("XUI_CreateClaim_600_RespondToClaimSoT") {
             exec(http("XUI_CreateClaim_600_005_RespondToClaimSoT")
               .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECStatementOfTruth")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .body(ElFileBody("bodies/specifiedclaimresponses/RespondToClaimSoT.json"))
               .check(substring("respondent1ClaimResponseTypeForSpec"))
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
               .body(ElFileBody("bodies/specifiedclaimresponses/RespondToClaimSubmit.json"))
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
  
  
  /*======================================================================================
      * Create Specified Civil Claim - Respond to Defence
      ==========================================================================================*/
  
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
          .get("/workallocation/case/tasks/#{caseId}/event/CLAIMANT_RESPONSE_SPEC/caseType/CIVIL/jurisdiction/CIVIL")
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
          .body(ElFileBody("bodies/specifiedclaimresponses/ViewAndRespond.json"))
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
          .body(ElFileBody("bodies/specifiedclaimresponses/UploadResponseToDef.json"))
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
          .body(ElFileBody("bodies/specifiedclaimresponses/FileDirectionsQuestionnaireDef.json"))
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
          .body(ElFileBody("bodies/specifiedclaimresponses/FixedRecoverableCostsDef.json"))
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
          .body(ElFileBody("bodies/specifiedclaimresponses/ProposeDirectionsForDisclosure.json"))
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
          .body(ElFileBody("bodies/specifiedclaimresponses/UseExpertClaim.json"))
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
          .body(ElFileBody("bodies/specifiedclaimresponses/AnyWitnessesClaim.json"))
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
          .body(ElFileBody("bodies/specifiedclaimresponses/WelshLanguageClaim.json"))
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
          .body(ElFileBody("bodies/specifiedclaimresponses/HearingAvailabilityClaim.json"))
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
          .body(ElFileBody("bodies/specifiedclaimresponses/UploadDirectionsClaimSubmit.json"))
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
          .body(ElFileBody("bodies/specifiedclaimresponses/RequireSupportClaim.json"))
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
          .body(ElFileBody("bodies/specifiedclaimresponses/VulnerabilityQuestionsClaim.json"))
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
          .body(ElFileBody("bodies/specifiedclaimresponses/FurtherInformationClaim.json"))
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
          .body(ElFileBody("bodies/specifiedclaimresponses/SoTClaim.json"))
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
          .body(ElFileBody("bodies/specifiedclaimresponses/ViewRespondToDefenceSubmit.json"))
          .check(substring("JUDICIAL_REFERRAL"))
        )
          
          .exec { session =>
            val fw = new BufferedWriter(new FileWriter("Flightdelaycases.csv", true))
            try {
              fw.write(session("caseId").as[String] + "\r\n")
            } finally fw.close()
            session
          }
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
}
