
package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef.{http, _}
import utils.{CivilDamagesHeader, Common, Environment, Headers}

import java.io.{BufferedWriter, FileWriter}

object CUIClaimCreation {
  
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
          .body(ElFileBody("bodies/cuiclaim/CUICreateClaim-BeforeStart.json"))
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
          .body(ElFileBody("bodies/cuiclaim/CivilCreateClaim-Eligibility.json"))
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
          .body(ElFileBody("bodies/cuiclaim/CivilCreateClaim-References.json"))
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
          .body(ElFileBody("bodies/cuiclaim/CivilCreateClaim-Claimant.json"))
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
          .body(ElFileBody("bodies/cuiclaim/CivilCreateClaim-AddAnotherClaim.json"))
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
          .body(ElFileBody("bodies/cuiclaim/CivilCreateClaim-Notifications.json"))
          .check(substring("CREATE_CLAIM_SPECNotifications"))
          .check(status.in(200, 304))
        )
          
          //val caseshareorgs =
          /*======================================================================================
                       * Civil UI Claim - Claim case Share
            ==========================================================================================*/
          
          .exec(http("CUI_CreateClaim_100_010_CaseShare")
            .get("/api/caseshare/orgs")
            .headers(CivilDamagesHeader.headers_418)
            .check(status.in(200, 304))
          )
      }
      .pause(MinThinkTime, MaxThinkTime)
    
      /*======================================================================================
                   * Civil UI Claim - Claim Solicitor Organization
        ==========================================================================================*/
      .group("CUI_CreateClaim_110_SolicitorOrganisation") {
        exec(http("CUI_CreateClaim_110_005_SolicitorOrganisation")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimantSolicitorOrganisation")
          .headers(CivilDamagesHeader.headers_347)
          .body(ElFileBody("bodies/cuiclaim/CivilCreateClaim-Organisation.json"))
          .check(substring("CREATE_CLAIM_SPECClaimantSolicitorOrganisation"))
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
    
      //create solicitor service address
      /*======================================================================================
                   * Civil UI Claim - Claim- Solicitor Correspondance Address
        ==========================================================================================*/
      .group("CUI_CreateClaim_120_SolicitorCorrespondanceAddress") {
        exec(http("CUI_CreateClaim_120_005_SolicitorCorrespondanceAddress")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECspecCorrespondenceAddress")
          .headers(CivilDamagesHeader.headers_347)
          .body(ElFileBody("bodies/cuiclaim/CivilCreateClaim-Correspondance.json"))
          .check(substring("CREATE_CLAIM_SPECspecCorrespondenceAddress"))
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
 
      //val claimdefendant =
      /*======================================================================================
                   * Civil UI Claim - Claim -Defendant Details
        ==========================================================================================*/
      
      .group("CUI_CreateClaim_130_CLAIMDefendant") {
        exec(http("CUI_CreateClaim_130_005_Def")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECDefendant")
          .headers(CivilDamagesHeader.headers_394)
          .body(ElFileBody("bodies/cuiclaim/CivilCreateClaim-DefDetails.json"))
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
          .body(ElFileBody("bodies//cuiclaim/CivilCreateClaim-DefRep.json"))
          .check(substring("CREATE_CLAIM_SPECLegalRepresentation"))
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
          .body(ElFileBody("bodies/cuiclaim/CivilCreateClaim-AddAnotherDef.json"))
          .check(substring("CREATE_CLAIM_SPECAddAnotherDefendant"))
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
    
      // val createclaimdetail =
      /*======================================================================================
                   * Civil UI Claim - Claim Details
        ==========================================================================================*/
      .group("CUI_CreateClaim_160_CLAIMDetails") {
        exec(http("CUI_CreateClaim_160_005_Details")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECDetails")
          .headers(CivilDamagesHeader.headers_534)
          .body(ElFileBody("bodies/cuiclaim/CivilCreateClaim-claimdetails.json"))//skipped upload
          .check(substring("CREATE_CLAIM_SPECDetails"))
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      // val postclaimdocs =
      /*======================================================================================
                   * Civil UI Claim - Upload Claim Docs
        ==========================================================================================*/
      
      .group("CUI_CreateClaim_170_Documents") {
        exec(http("CUI_CreateClaim_170_005_Docs")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECUploadClaimDocument")
          .headers(CivilDamagesHeader.headers_534)
          .body(ElFileBody("bodies/cuiclaim/CivilCreateClaim-ClaimDocs.json"))
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
          .body(ElFileBody("bodies/cuiclaim/CivilCreateClaim-ClaimTimeline.json"))
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
          .body(ElFileBody("bodies/cuiclaim/CivilCreateClaim-EvidenceList.json"))
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
          .body(ElFileBody("bodies/cuiclaim/CivilCreateClaim-ClaimAmount.json"))
          .check(substring("CREATE_CLAIM_SPECClaimAmount"))
          .check(jsonPath("$.data.claimAmountBreakup[0].id").saveAs("claimAmountBreakupId"))
         // .check(jsonPath("$.data.speclistYourEvidenceList[0].id").saveAs("speclistYourEvidenceListId"))
          .check(jsonPath("$.data.timelineOfEvents[0].id").saveAs("timelineOfEventsId"))
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      // val createclaimamountdetails =
      /*======================================================================================
                        * Civil UI Claim - Claim Amount Details
             ==========================================================================================*/
      .group("CUI_CreateClaim_210_ClaimAmountDetails") {
        exec(http("CUI_CreateClaim_210_005_ClaimAmountDetails")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimAmountDetails")
          .headers(CivilDamagesHeader.headers_595)
          .body(ElFileBody("bodies/cuiclaim/CivilCreateClaim-ClaimAmountDetails.json"))
          .check(substring("CREATE_CLAIM_SPECClaimAmountDetails"))
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
    
      /*======================================================================================
                   * Civil UI Claim - Claim Amount Interest
        ==========================================================================================*/
      .group("CUI_CreateClaim_220_ClaimInterest") {
        exec(http("CUI_CreateClaim_220_005_ClaimInterest")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimInterest")
          .headers(CivilDamagesHeader.headers_595)
          .body(ElFileBody("bodies/cuiclaim/CivilCreateClaim-ClaimInterest.json"))
          .check(substring("CREATE_CLAIM_SPECClaimInterest"))
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
    
      /*======================================================================================
                   * Civil UI Claim - Claim Amount Interest Summary
        ==========================================================================================*/
      .group("CUI_CreateClaim_230_CLAIMClaimInterestSummery") {
        exec(http("CUI_CreateClaim_230_005_CLAIMClaimInterestSummary")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECInterestSummary")
          .headers(CivilDamagesHeader.headers_595)
          .body(ElFileBody("bodies/cuiclaim/CivilCreateClaim-ClaimInterestSummary.json"))
          .check(jsonPath("$.data.claimFee.calculatedAmountInPence").saveAs("calculatedAmountInPence"))
          .check(jsonPath("$.data.claimFee.code").saveAs("claimFeeCode"))
          .check(jsonPath("$.data.claimFee.version").saveAs("version"))
          .check(substring("CREATE_CLAIM_SPECInterestSummary"))
          .check(status.in(200, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      /*======================================================================================
                   * Civil UI Claim - Claim PBANumber
        ==========================================================================================*/
      .group("CUI_CreateClaim_240_CLAIMPbaNumber") {
        exec(http("CUI_CreateClaim_240_005_PBANumber")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECPbaNumber")
          .headers(CivilDamagesHeader.headers_610)
          .body(ElFileBody("bodies/cuiclaim/CivilCreateClaim-PbaNumber.json"))
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
          .body(ElFileBody("bodies/cuiclaim/CivilCreateClaim-SOT.json"))
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
      /*======================================================================================
                   * Civil UI Claim - Claim Submit
        ==========================================================================================*/
      .group("CUI_CreateClaim_260_SubmitClaim") {
        exec(http("CUI_CreateClaim_260_005_Submit")
          .post(BaseURL + "/data/case-types/CIVIL/cases?ignore-warning=false")
        //  .headers(CivilDamagesHeader.MoneyClaimSubmitHeader)
          .headers(CivilDamagesHeader.CUI_Submit)
          .body(ElFileBody("bodies/cuiclaim/CivilCreateClaim-Submit.json"))
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
          .get("/data/internal/cases/#{caseId}")
          .headers(CivilDamagesHeader.headers_717)
          .check(status.in(200, 201,204,304))
        )
          .exec(http("CUI_CreateClaim_270_010_CaseDetails2")
            .post("/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
            .headers(CivilDamagesHeader.headers_717)
            .body(ElFileBody("bodies/cuiclaim/CivilAccessClaim-RollAccess.json"))
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
    exec(_.setAll(
      "Idempotencynumber" -> (Common.getIdempotency())
     )
    )
  // payment pba fee payment
    .group("CUI_CreateClaim_280_ServiceRequestForPay") {
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
            .get("/payments/case-payment-orders?case_ids=#{caseId}")
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
          .body(ElFileBody("bodies/cuiclaim/CivilCreateClaim-PBAPayment.json"))
          .check(substring("success"))
          .check(status.in(200, 201, 304))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      .pause(50)
      
      
        //Deepak - Cases that make the final step
        .exec { session =>
          val fw = new BufferedWriter(new FileWriter("XUISpecClaims.csv", true))
          try {
            fw.write(session("caseId").as[String] + "\r\n")
          } finally fw.close()
          session
        }
  
  
  
}
