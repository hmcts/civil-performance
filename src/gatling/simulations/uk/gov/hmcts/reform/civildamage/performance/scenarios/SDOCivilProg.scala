
package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, Common, Environment, Headers}

object SDOCivilProg {

  val BaseURL = Environment.baseURL
  val IdAMURL = Environment.idamURL
  val IdamUrl = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  //val sdoenhancementsfasttrackfeeder=csv("sdocpftclaimNumbers.csv").circular
 // val sdocpfasttrackcuir2=csv("sdocpfasttrackcuir2Ids.csv").circular
//  val sdoflightdelayfeeder=csv("sdoflightdelayclaimNumbers.csv").circular
  //val sdodrhfeeder=csv("sdodrhclaimNumbers.csv").circular
  

      //Deepak - Cases that make the final step
  
  
  /*======================================================================================
             * SDO Fast Track
  ==========================================================================================*/
  val SDOFastTrackForCUIR2 =
    //feed(sdoenhancementsfasttrackfeeder)
      group("CUICPSC_CreateClaim_330_BackToCaseDetailsPage") {
        exec(flushHttpCache)
        .exec(_.setAll(
          "Idempotencynumber" -> Common.getIdempotency()
        
        ))
          .exec(http("CUICPSC_CreateClaim_330_005_CaseDetails")
            .get(BaseURL + "/data/internal/cases/#{claimNumber}")
            .headers(CivilDamagesHeader.MoneyClaimNav)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
            .check(substring("Civil"))
            .check(status.in(200, 201, 304)))
      }
  
      .pause(MinThinkTime, MaxThinkTime)
        .pause(30)
      
      /*======================================================================================
           * Create Civil Claim - Click on Tasks Tab
    ==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("XUI_CreateClaim_790_TaskTabs") {
        exec(http("XUI_CreateClaim_790_005_AssignToMe")
          .post(BaseURL + "/workallocation/case/task/#{claimNumber}")
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
          .body(ElFileBody("bodies/sdocpfasttrack/AssignToMe.json"))
         // .check(substring("assignee"))
        )
        
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      /*======================================================================================
     * Create Civil Claim - Start Event 'Directions - Fast Track'
  ==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("XUI_CreateClaim_810_DirectionsFastTrack") {
        exec(http("XUI_CreateClaim_810_005_DirectionsFastTrack")
          .get("/cases/case-details/#{claimNumber}/trigger/CREATE_SDO/CREATE_SDOFastTrack?tid=#{JudgeId}")
          .headers(CivilDamagesHeader.headers_notify)
          .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
          //  .check(substring("assignee"))
        )
          
          .exec(http("XUI_CreateClaim_810_010_DirectionsFastTrack")
            .get(BaseURL + "/data/internal/cases/#{claimNumber}/event-triggers/CREATE_SDO?ignore-warning=false")
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
         // .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))
        
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
          .body(ElFileBody("bodies/sdocpfasttrack/EnterJudgmentDamages.json"))
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
          .body(ElFileBody("bodies/sdocpfasttrack/WhatTrackAllocating.json"))
          .check(substring("CREATE_SDOClaimsTrack"))
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
          .body(ElFileBody("bodies/sdocpfasttrack/SDOdetails.json"))
          .check(jsonPath("$.data.sdoOrderDocument.createdDatetime").saveAs("createdDatetime"))
          .check(jsonPath("$.data.sdoOrderDocument.documentLink.document_url").saveAs("sdoDocument_url"))
          .check(jsonPath("$.data.sdoOrderDocument.documentLink.document_hash").saveAs("sdoDocument_hash"))
          .check(jsonPath("$.data.sdoOrderDocument.documentName").saveAs("sdoDocumentName"))
          .check(jsonPath("$.data.sdoOrderDocument.documentSize").saveAs("documentSize"))
          .check(substring("FULL_DEFENCE"))
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
          .body(ElFileBody("bodies/sdocpfasttrack/SDOContinue.json"))
          .check(substring("CREATE_SDOOrderPreview"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
  * Create Civil Claim - Standard Direction Order Submit
  ==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("XUI_CreateClaim_860_SDOSubmit") {
        exec(http("XUI_CreateClaim_860_005_SDOSubmit")
          .post("/data/cases/#{claimNumber}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .body(ElFileBody("bodies/sdocpfasttrack/SDOSubmit.json"))
          .check(substring("Civil"))
        )
          
          .exec(http("CUICPSC_SDOE_RRByC_060_010_SDOSubmit")
            .get("/data/internal/cases/#{claimNumber}")
            .headers(CivilDamagesHeader.MoneyClaimPostHeader)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
            .check(substring("Civil"))
          )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  val MediaionUnsuccessfulBeforeSDO =
      
      // following code is for the mediation unsuccessful events
      group("CUICPSC_SDOE_050_MediationUnsuccessful") {
        exec(http("CUICPSC_SDOE_050_005MediationUnsuccessful")
          .get("/data/internal/cases/#{claimNumber}/event-triggers/MEDIATION_UNSUCCESSFUL?ignore-warning=false")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
          .check(substring("Mediation Unsuccessful"))
          .check(jsonPath("$.event_token").saveAs("event_token"))
        )
        //  .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      .group("CUICPSC_SDOE_050_ValidateReason") {
        exec(http("CivilMT_HearingNotice_040_010_ValidateReason")
          .post("/data/case-types/CIVIL/validate?pageId=MEDIATION_UNSUCCESSFULmediationUnsuccessful")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/CaseProg/smallclaims/validateMediationReason.json"))
          .check(substring("MEDIATION_UNSUCCESSFUL"))
        )
        //  .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      .group("CUICPSC_SDOE_050_ValidateIntegration") {
        exec(http("CivilMT_HearingNotice_040_010_ValidateIntegration")
          .post("/data/case-types/CIVIL/validate?pageId=MEDIATION_UNSUCCESSFULWorkAllocationIntegrationFields")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/CaseProg/smallclaims/validateIntegration.json"))
          .check(substring("MEDIATION_UNSUCCESSFUL"))
        )
        //  .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      .group("CUICPSC_SDOE_050_SubmitMediationUnsuccessful") {
        exec(http("CivilMT_HearingNotice_040_010_SubmitMediationUnsuccessful")
          .post("/data/cases/#{claimNumber}/events")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/CaseProg/smallclaims/MediationUnsuccessfulSubmit.json"))
          .check(substring("mediationUnsuccessful"))

        )
        //  .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      .pause(120) //120
  
  val SDOSmallClaimsForCUIR2 =
    //feed(sdodrhfeeder)
      group("CUICPSC_SDOE_DRH_30_CaseDetails") {
        exec(http("CUICPSC_SDOE_DRH_30_005_CaseDetails")
            .get(BaseURL + "/data/internal/cases/#{claimNumber}")
            .headers(Headers.commonHeader)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
            .check(substring("Civil"))
            .check(status.in(200, 201, 304)))
      }
  
      .pause(MinThinkTime, MaxThinkTime)
      .pause(120)
      
      // following code is for the mediation unsuccessful events
      
      /*======================================================================================
           * Create Civil Claim - Click on Tasks Tab
    ==========================================================================================*/
      .group("CUICPSC_SDOE_DRH_40_TaskTab") {
        exec(http("CUICPSC_SDOE_DRH_40_005_TaskTab")
          .post(BaseURL + "/workallocation/case/task/#{claimNumber}")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .body(ElFileBody("bodies/sdodrh/TaskTab.json"))
          .check(jsonPath("$[0].id").saveAs("JudgeId"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)


        /*======================================================================================
           * Create Civil Claim - Start Event 'Assign To Me'
    ==========================================================================================*/
      //  val returntocasedetailsafternotifydetails =
      .group("CUICPSC_SDOE_DRH_50_AssignToMe") {
        exec(http("CUICPSC_SDOE_DRH_50_005_AssignToMe")
          .post("/workallocation/task/#{JudgeId}/claim")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          .body(ElFileBody("bodies/sdodrh/AssignToMe.json"))
          .check(status.in(200, 201, 204, 304))
          // .check(substring("assignee"))
        )
        
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      /*======================================================================================
     * Create Civil Claim - Start Event 'Directions - Small claims - DRH'
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("CUICPSC_SDOE_DRH_60_DirectionsDRH") {
        exec(http("CUICPSC_SDOE_DRH_60_005_DirectionsDRH")
          .get("/cases/case-details/#{claimNumber}/trigger/CREATE_SDO/CREATE_SDOSmallClaims?tid=#{JudgeId}")
          .headers(CivilDamagesHeader.headers_notify)
          .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
          //  .check(substring("assignee"))
        )
          
          .exec(http("CUICPSC_SDOE_DRH_60_010_DirectionsDRH")
            .get(BaseURL + "/data/internal/cases/#{claimNumber}/event-triggers/CREATE_SDO?ignore-warning=false")
            .headers(CivilDamagesHeader.headers_notify)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
            .check(substring("CREATE_SDO"))
           
            .check(jsonPath("$.event_token").saveAs("event_token"))
          )
         // .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
     * Create Civil Claim - Do you wish to enter judgment for a sum of damages to be decided ?No
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("CUICPSC_SDOE_DRH_70_EnterJudgmentDamages") {
        exec(http("CUICPSC_SDOE_DRH_70_005_EnterJudgmentDamages")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_SDOSDO")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sdodrh/EnterJudgmentDamages.json"))
          .check(substring("drawDirectionsOrderRequired"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
* Create Civil Claim - What track are you allocating the claim to?
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("CUICPSC_SDOE_DRH_80_WhatTrackAllocating") {
        exec(http("CUICPSC_SDOE_DRH_80_005_WhatTrackAllocating")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_SDOClaimsTrack")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sdodrh/WhatTrackAllocating.json"))
          .check(substring("documentLink"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      /*======================================================================================
* Create Civil Claim - Standard Direction Order Details
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("CUICPSC_SDOE_DRH_90_SDOdetails") {
        exec(http("CUICPSC_SDOE_DRH_90_005_SDOdetails")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_SDOSdoR2SmallClaims")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sdodrh/SDODRHsmallclaimsdetails.json"))
          .check(jsonPath("$.data.sdoOrderDocument.createdDatetime").saveAs("createdDatetime"))
          .check(jsonPath("$.data.sdoOrderDocument.documentLink.document_url").saveAs("sdoDocument_url"))
          .check(jsonPath("$.data.sdoOrderDocument.documentLink.document_hash").saveAs("sdoDocument_hash"))
          .check(jsonPath("$.data.sdoOrderDocument.documentName").saveAs("sdoDocumentName"))
          .check(jsonPath("$.data.sdoOrderDocument.documentSize").saveAs("documentSize"))
          .check(substring("claimNotification"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
* Create Civil Claim - Standard Direction Order Continue
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("CUICPSC_SDOE_DRH_100_SDOContinue") {
        exec(http("CUICPSC_SDOE_DRH_100_005_SDOContinue")
          .post("/data/case-types/CIVIL/validate?pageId=pageId=CREATE_SDOOrderPreview")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sdodrh/SDODRHContinueSmallClaims.json"))
          .check(substring("sdoOrderDocument"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
* Create Civil Claim - Standard Direction Order Submit
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("CUICPSC_SDOE_DRH_110_SDOSubmit") {
        exec(http("CUICPSC_SDOE_DRH_110_005_SDOSubmit")
          .post("/data/cases/#{claimNumber}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .body(ElFileBody("bodies/sdodrh/SDODRHSmallClaimsSubmit.json"))
          .check(substring("CASE_PROGRESSION"))
        )
          .exec(http("CUICPSC_SDOE_DRH_110_010_SDOSubmit")
            .get("/ data/internal/cases/#{claimNumber}")
            .headers(CivilDamagesHeader.MoneyClaimPostHeader)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
            .check(status.in(200, 201, 204, 304))
          )
          
         
        
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  /*======================================================================================
 * Standard Direction Order for Request For ReConsider- small claim less than 1000
 ==========================================================================================*/
  
  
  val SDORequestForReConsiderByTribunal =
      group("CUICPSC_SDOE_RFRByTri_30_CaseDetails") {
       
          exec(http("CUICPSC_SDOE_RFRByTri_30_005_CaseDetails")
            .get(BaseURL + "/data/internal/cases/#{claimNumber}")
            .headers(Headers.commonHeader)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
            .check(substring("Civil"))
            .check(status.in(200, 201, 204,304)))
      }
        .pause(MinThinkTime, MaxThinkTime)
      
      /*======================================================================================
           * Create Civil Claim - Click on Tasks Tab
    ==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("CUICPSC_SDOE_RFRByTri_40_TaskTab") {
        exec(http("CUICPSC_SDOE_RFRByTri_40_005_TaskTab")
          .post(BaseURL + "/workallocation/case/task/#{claimNumber}")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .body(ElFileBody("bodies/sdorequestforreconsider/TaskTab.json"))
          .check(jsonPath("$[0].id").saveAs("TribunalJudgeId"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      /*======================================================================================
           * Create Civil Claim - Start Event 'Assign To Me'
    ==========================================================================================*/
      //  val returntocasedetailsafternotifydetails =
      .group("CUICPSC_SDOE_RFRByTri_50_AssignToMe") {
        exec(http("CUICPSC_SDOE_RFRByTri_50_005_AssignToMe")
          .post("/workallocation/task/#{TribunalJudgeId}/claim")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          .body(ElFileBody("bodies/sdorequestforreconsider/AssignToMe.json"))
          .check(status.in(200, 201, 204, 304))
          // .check(substring("assignee"))
        )
        .exec(http("CUICPSC_SDOE_RFRByTri_50_010_AssignToMe")
          .post("/workallocation/case/task/#{claimNumber}")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          .body(ElFileBody("bodies/sdorequestforreconsider/CaseTask.json"))
          .check(status.in(200, 201, 204, 304))
        )
        
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      
      
      /*======================================================================================
     * Create Civil Claim - Start Event SDO 'Directions - Request For ReConsideration'
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("CUICPSC_SDOE_RFRByTri_60_DirectionsRFR") {
        exec(http("CUICPSC_SDOE_RFRByTri_60_005_DirectionsRFR")
          .get("/cases/case-details/#{claimNumber}/trigger/CREATE_SDO/CREATE_SDOSmallClaims?tid=#{TribunalJudgeId}")
          .headers(CivilDamagesHeader.headers_notify)
          .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
          .check(status.in(200, 201, 204, 304))
        )
          .exec(http("CUICPSC_SDOE_RFRByTri_60_010_DirectionRFRCase")
            .get("/data/internal/cases/#{claimNumber}/event-triggers/CREATE_SDO?ignore-warning=false")
            .headers(CivilDamagesHeader.headers_notify)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
            .check(substring("CREATE_SDO"))
            .check(status.in(200, 201, 204, 304))
            .check(jsonPath("$.event_token").saveAs("event_token"))
          )
          //.exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))
  
          .exec(http("CUICPSC_SDOE_RFRByTri_60_015_DirectionRFRCase")
            .get("/data/internal/cases/#{claimNumber}")
            .headers(CivilDamagesHeader.headers_notify)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
            .check(substring("#{claimNumber}"))
            .check(status.in(200, 201, 204, 304))
          )
        
        
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
     * Create Civil Claim - Do you wish to enter judgment for a sum of damages to be decided ?No
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("CUICPSC_SDOE_RFRByTri_70_EnterJudgment") {
        exec(http("CUICPSC_SDOE_RFRByTri_70_005_EnterJudgment")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_SDOSDO")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sdorequestforreconsider/EnterJudgmentDamages.json"))
          .check(substring("drawDirectionsOrderRequired"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      /*======================================================================================
* Create Civil Claim - What track are you allocating the claim to?
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("CUICPSC_SDOE_RFRByTri_80_WhatTrackAllocating") {
        exec(http("CUICPSC_SDOE_RFRByTri_80_005_WhatTrackAllocating")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_SDOClaimsTrack")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sdorequestforreconsider/WhatTrackAllocating.json"))
         // .check(substring("documentLink"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      /*======================================================================================
* Create Civil Claim - Standard Direction Order Details
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("CUICPSC_SDOE_RFRByTri_90_SDOdetails") {
        exec(http("CUICPSC_SDOE_RFRByTri_90_005_SDOdetails")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_SDOSmallClaims")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sdorequestforreconsider/SDOenhancementssmallclaimsdetails.json"))
          .check(jsonPath("$.data.sdoOrderDocument.createdDatetime").saveAs("createdDatetime"))
          .check(jsonPath("$.data.sdoOrderDocument.documentLink.document_url").saveAs("sdoDocument_url"))
          .check(jsonPath("$.data.sdoOrderDocument.documentLink.document_hash").saveAs("sdoDocument_hash"))
          .check(jsonPath("$.data.sdoOrderDocument.documentName").saveAs("sdoDocumentName"))
          .check(jsonPath("$.data.sdoOrderDocument.documentSize").saveAs("documentSize"))
          .check(substring("claimNotification"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
* Create Civil Claim - Standard Direction Order Continue
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("CUICPSC_SDOE_RFRByTri_100_SDOContinue") {
        exec(http("CUICPSC_SDOE_RFRByTri_100_005_SDOContinue")
          .post("/data/case-types/CIVIL/validate?pageId=pageId=CREATE_SDOOrderPreview")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sdorequestforreconsider/SDOEnhancementsContinueSmallClaims.json"))
          .check(substring("sdoOrderDocument"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
* Create Civil Claim - Standard Direction Order Submit
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("CUICPSC_SDOE_RFRByTri_110_SDOSubmit") {
        exec(http("CUICPSC_SDOE_RFRTri_110_005_SDOSubmit")
          .post("/data/cases/#{claimNumber}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .body(ElFileBody("bodies/sdorequestforreconsider/SDOEnhancementsSmallClaimsSubmit.json"))
          .check(substring("CASE_PROGRESSION"))
        )
          .exec(http("CUICPSC_SDOE_RFRByTri_110_010_SDOSubmit")
            .get("/data/internal/cases/#{claimNumber}")
            .headers(CivilDamagesHeader.MoneyClaimPostHeader)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
            .check(substring("case_id"))
          )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
  
        .group("CUICPSC_SDOE_RFRByTri_120_ViewCaseAfterSDOByTri") {
            exec(http("CUICPSC_SDOE_RFRByTri_120_005_ViewCaseByJudge")
              .post(BaseURL + "/api/role-access/roles/manageLabellingRoleAssignment/#{claimNumber}")
              .headers(CivilDamagesHeader.MoneyClaimNav)
              .body(ElFileBody("bodies/sdorequestforreconsider/viewcasebyjudge.json"))
              .header("accept", "application/json, text/plain, */*")
              .check(status.in(200, 201, 204, 304)))
      
            .exec(http("CUICPSC_SDOE_RFRByTri_120_010_CaseDetails")
              .get(BaseURL + "/api/wa-supported-jurisdiction/get")
              .headers(Headers.commonHeader)
              .header("accept", "application/json, text/plain, */*")
              .check(status.in(200, 201, 204, 304)))
    
        }
        .pause(MinThinkTime, MaxThinkTime)
  
  
 
  
    
  
 
  
  
 
  
  
}
