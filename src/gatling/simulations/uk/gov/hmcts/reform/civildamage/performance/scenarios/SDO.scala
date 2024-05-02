
package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, Common, Environment, Headers}

import java.io.{BufferedWriter, FileWriter}

object SDO {

  val BaseURL = Environment.baseURL
  val IdAMURL = Environment.idamURL
  val IdamUrl = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val sdoenhancementsfasttrackfeeder=csv("sdoftcaseIds.csv").circular
  val sdoflightdelayfeeder=csv("sdoflightdelaycaseIds.csv").circular
  val sdodrhfeeder=csv("sdodrhcaseIds.csv").circular
  

  /*======================================================================================
             * SDO Fast Track
  ==========================================================================================*/
  val run=
    feed(sdoenhancementsfasttrackfeeder)
    .group("Civil_CreateClaim_330_BackToCaseDetailsPage") {
      exec(_.setAll(
        "Idempotencynumber" -> Common.getIdempotency()
      
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
          .check(substring("Civil"))
        )
  
          .exec(http("Civil_SDOE_RRByC_060_010_SDOSubmit")
            .get("/ data/internal/cases/#{caseId}")
            .headers(CivilDamagesHeader.MoneyClaimPostHeader)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
            .check(substring("Civil"))
          )
        

      }
      .pause(MinThinkTime, MaxThinkTime)


      //Deepak - Cases that make the final step
  
  
  val SDOEnhancementFastTrack =
    feed(sdoenhancementsfasttrackfeeder)
    .group("Civil_CreateClaim_30_BackToCaseDetailsPage") {
      
      
      exec(_.setAll(
        "Idempotencynumber" -> (Common.getIdempotency()),
        "LRrandomString" -> Common.randomString(5)
      
      ))
        .exec(http("Civil_SDOE_FT_30_005_CaseDetails")
          .get(BaseURL + "/data/internal/cases/#{caseId}")
          .headers(Headers.commonHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
          .check(substring("Civil"))
          .check(status.in(200, 201, 304)))
      
      
    }
      
      
      /*======================================================================================
           * Create Civil Claim - Click on Tasks Tab
    ==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("Civil_SDOE_FT_40_TaskTabs") {
        exec(http("Civil_SDOE_FT_40_005_AssignToMe")
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
      .group("Civil_SDOE_FT_50_AssignToMe") {
        exec(http("Civil_SDOE_FT_50_005_AssignToMe")
          .post("/workallocation/task/#{JudgeId}/claim")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          .body(ElFileBody("bodies/sdofasttrack/AssignToMe.json"))
          .check(status.in(200, 201, 204,304))
         // .check(substring("assignee"))
        )
        
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      
      
      /*======================================================================================
     * Create Civil Claim - Start Event 'Directions - Fast Track'
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("Civil_SDOE_FT_60_DirectionsFastTrack") {
        exec(http("Civil_SDOE_FT_60_005_DirectionsFastTrack")
          .get("/cases/case-details/#{caseId}/trigger/CREATE_SDO/CREATE_SDOFastTrack?tid=#{JudgeId}")
          .headers(CivilDamagesHeader.headers_notify)
          .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
          //  .check(substring("assignee"))
        )
  
          .exec(Common.configurationui)
  
          .exec(Common.configJson)
  
          .exec(Common.TsAndCs)
  
          .exec(Common.configUI)
          
          .exec(Common.isAuthenticated)
          
          .exec(http("Civil_SDOE_FT_60_010_DirectionsFastTrack")
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
      .group("Civil_SDOE_FT_70_EnterJudgmentDamages") {
        exec(http("Civil_SDOE_FT_70_005_EnterJudgmentDamages")
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
      .group("Civil_SDOE_FT_80_WhatTrackAllocating") {
        exec(http("Civil_SDOE_FT_80_005_WhatTrackAllocating")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_SDOClaimsTrack")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sdofasttrack/WhatTrackAllocating.json"))
          .check(substring("documentLink"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      /*======================================================================================
* Create Civil Claim - What track are you allocating the claim to?
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("Civil_SDOE_FT_90_OrderType") {
        exec(http("Civil_SDOE_FT_90_005_OrderType")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_SDOOrderType")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sdofasttrack/OrderType.json"))
          .check(substring("documentLink"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  
      /*======================================================================================
* Create Civil Claim - Standard Direction Order Details
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("Civil_SDOE_FT_100_SDOdetails") {
        exec(http("Civil_SDOE_FT_100_005_SDOdetails")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_SDODisposalHearing")
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
      .group("Civil_SDOE_FT_110_SDOContinue") {
        exec(http("Civil_SDOE_FT_110_005_SDOContinue")
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
      .group("Civil_SDOE_FT_120_SDOSubmit") {
        exec(http("Civil_SDOE_FT_120_005_SDOSubmit")
          .post("/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .body(ElFileBody("bodies/sdofasttrack/SDOEnhancementsFastTrackSubmit.json"))
          .check(substring("CASE_PROGRESSION"))
        )
          .exec(http("Civil_SDOE_FT_120_010_SDOSubmit")
            .get("/ data/internal/cases/#{caseId}")
            .headers(CivilDamagesHeader.MoneyClaimPostHeader)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
            .check(status.in(200, 201, 204, 304))
          )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  
  /*======================================================================================
 * Standard Direction Order for Flight Delay
 ==========================================================================================*/
  
  
  val SDOFlightDelay =
    feed(sdoflightdelayfeeder)
      .group("Civil_SDOE_FD_30_BackToCaseDetailsPage") {
        
        exec(_.setAll(
          "Idempotencynumber" -> (Common.getIdempotency()),
          "LRrandomString" -> Common.randomString(5)
        
        ))
          .exec(http("Civil_SDOE_FD_30_005_CaseDetails")
            .get(BaseURL + "/data/internal/cases/#{caseId}")
            .headers(Headers.commonHeader)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
            .check(substring("Civil"))
            .check(status.in(200, 201, 304)))
        
        
      }
      
      
      /*======================================================================================
           * Create Civil Claim - Click on Tasks Tab
    ==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("Civil_SDOE_FD_40_TaskTabs") {
        exec(http("Civil_SDOE_FD_40_005_AssignToMe")
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
      .group("Civil_SDOE_FD_50_AssignToMe") {
        exec(http("Civil_SDOE_FD_50_005_AssignToMe")
          .post("/workallocation/task/#{JudgeId}/claim")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          .body(ElFileBody("bodies/sdofasttrack/AssignToMe.json"))
          .check(status.in(200, 201, 204, 304))
          // .check(substring("assignee"))
        )
        
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      
      
      /*======================================================================================
     * Create Civil Claim - Start Event 'Directions - Fast Track'
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("Civil_SDOE_FD_60_DirectionsFastTrack") {
        exec(http("Civil_SDOE_FD_60_005_DirectionsFastTrack")
          .get("/cases/case-details/#{caseId}/trigger/CREATE_SDO/CREATE_SDOSmallClaims?tid=#{JudgeId}")
          .headers(CivilDamagesHeader.headers_notify)
          .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
          //  .check(substring("assignee"))
        )
          
          .exec(http("Civil_SDOE_FD_60_010_DirectionsFastTrack")
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
      .group("Civil_SDOE_FD_70_EnterJudgmentDamages") {
        exec(http("Civil_SDOE_FD_70_005_EnterJudgmentDamages")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_SDOSDO")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sdoflightdelay/EnterJudgmentDamages.json"))
          .check(substring("drawDirectionsOrderRequired"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
* Create Civil Claim - What track are you allocating the claim to?
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("Civil_SDOE_FD_80_WhatTrackAllocating") {
        exec(http("Civil_SDOE_FD_80_005_WhatTrackAllocating")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_SDOClaimsTrack")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sdoflightdelay/WhatTrackAllocating.json"))
          .check(substring("documentLink"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      /*======================================================================================
* Create Civil Claim - Standard Direction Order Details
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("Civil_SDOE_FD_90_SDOdetails") {
        exec(http("Civil_SDOE_FD_90_005_SDOdetails")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_SDOSmallClaims")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sdoflightdelay/SDOenhancementssmallclaimsdetails.json"))
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
      .group("Civil_SDOE_FD_100_SDOContinue") {
        exec(http("Civil_SDOE_FD_100_005_SDOContinue")
          .post("/data/case-types/CIVIL/validate?pageId=pageId=CREATE_SDOOrderPreview")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sdoflightdelay/SDOEnhancementsContinueSmallClaims.json"))
          .check(substring("sdoOrderDocument"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
* Create Civil Claim - Standard Direction Order Submit
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("Civil_SDOE_FD_110_SDOSubmit") {
        exec(http("Civil_SDOE_FD_110_005_SDOSubmit")
          .post("/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .body(ElFileBody("bodies/sdoflightdelay/SDOEnhancementsSmallClaimsSubmit.json"))
          .check(substring("CASE_PROGRESSION"))
        )
  
          .exec(http("Civil_SDOE_FD_110_010_SDOSubmit")
            .get("/ data/internal/cases/#{caseId}")
            .headers(CivilDamagesHeader.MoneyClaimPostHeader)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
            .check(status.in(200, 201, 204, 304))
          )
          
         
        
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  val SDOSmallClaimsForDRH =
    feed(sdodrhfeeder)
      .group("Civil_SDOE_DRH_30_BackToCaseDetailsPage") {
        
        exec(_.setAll(
          "Idempotencynumber" -> (Common.getIdempotency()),
          "LRrandomString" -> Common.randomString(5)
       
        ))
          .exec(http("Civil_SDOE_DRH_30_005_CaseDetails")
            .get(BaseURL + "/data/internal/cases/#{caseId}")
            .headers(Headers.commonHeader)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
            .check(substring("Civil"))
            .check(status.in(200, 201, 304)))
        
        
      }
      
      
      /*======================================================================================
           * Create Civil Claim - Click on Tasks Tab
    ==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("Civil_SDOE_DRH_40_TaskTabs") {
        exec(http("Civil_SDOE_DRH_40_005_TaskTab")
          .post(BaseURL + "/workallocation/case/task/#{caseId}")
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
      .group("Civil_SDOE_DRH_50_AssignToMe") {
        exec(http("Civil_SDOE_DRH_50_005_AssignToMe")
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
      .group("Civil_SDOE_DRH_60_DirectionsDRH") {
        exec(http("Civil_SDOE_DRH_60_005_DirectionsDRH")
          .get("/cases/case-details/#{caseId}/trigger/CREATE_SDO/CREATE_SDOSmallClaims?tid=#{JudgeId}")
          .headers(CivilDamagesHeader.headers_notify)
          .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
          //  .check(substring("assignee"))
        )
          
          .exec(http("Civil_SDOE_DRH_60_010_DirectionsDRH")
            .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/CREATE_SDO?ignore-warning=false")
            .headers(CivilDamagesHeader.headers_notify)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
            .check(substring("CREATE_SDO"))
           
            .check(jsonPath("$.event_token").saveAs("event_token"))
          )
          .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
     * Create Civil Claim - Do you wish to enter judgment for a sum of damages to be decided ?No
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("Civil_SDOE_DRH_70_EnterJudgmentDamages") {
        exec(http("Civil_SDOE_DRH_70_005_EnterJudgmentDamages")
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
      .group("Civil_SDOE_DRH_80_WhatTrackAllocating") {
        exec(http("Civil_SDOE_DRH_80_005_WhatTrackAllocating")
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
      .group("Civil_SDOE_DRH_90_SDOdetails") {
        exec(http("Civil_SDOE_DRH_90_005_SDOdetails")
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
      .group("Civil_SDOE_DRH_100_SDOContinue") {
        exec(http("Civil_SDOE_DRH_100_005_SDOContinue")
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
      .group("Civil_SDOE_DRH_110_SDOSubmit") {
        exec(http("Civil_SDOE_DRH_110_005_SDOSubmit")
          .post("/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .body(ElFileBody("bodies/sdodrh/SDODRHSmallClaimsSubmit.json"))
          .check(substring("CASE_PROGRESSION"))
        )
          .exec(http("Civil_SDOE_DRH_110_010_SDOSubmit")
            .get("/ data/internal/cases/#{caseId}")
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
      group("Civil_SDOE_RRCByTri_30_BackToCaseDetailsPage") {
        exec(_.setAll(
          "Idempotencynumber" -> (Common.getIdempotency()),
        
        ))
          .exec(http("Civil_SDOE_RRCByTri_30_005_CaseDetails")
            .get(BaseURL + "/data/internal/cases/#{caseId}")
            .headers(Headers.commonHeader)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
            .check(substring("Civil"))
            .check(status.in(200, 201, 204,304)))
      }
  
      
      /*======================================================================================
           * Create Civil Claim - Click on Tasks Tab
    ==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("Civil_SDOE_RRCByTri_40_TaskTabs") {
        exec(http("Civil_SDOE_RRCByTri_40_005_TaskTabs")
          .post(BaseURL + "/workallocation/case/task/#{caseId}")
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
      .group("Civil_SDOE_RRCByTri_50_AssignToMe") {
        exec(http("Civil_SDOE_RRCByTri_50_005_AssignToMe")
          .post("/workallocation/task/#{TribunalJudgeId}/claim")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          .body(ElFileBody("bodies/sdorequestforreconsider/AssignToMe.json"))
          .check(status.in(200, 201, 204, 304))
          // .check(substring("assignee"))
        )
        .exec(http("Civil_SDOE_RRCByTri_50_010_AssignToMe")
          .post("/workallocation/case/task/#{caseId}")
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
      .group("Civil_SDOE_RRCByTri_60_DirectionsRRC") {
        exec(http("Civil_SDOE_RRCByTri_60_005_DirectionsRRC")
          .get("/cases/case-details/#{caseId}/trigger/CREATE_SDO/CREATE_SDOSmallClaims?tid=#{TribunalJudgeId}")
          .headers(CivilDamagesHeader.headers_notify)
          .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
          //  .check(substring("assignee"))
        )
          .exec(http("Civil_SDOE_RRCByTri_60_010_DirectionRRCCase")
            .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/CREATE_SDO?ignore-warning=false")
            .headers(CivilDamagesHeader.headers_notify)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
            .check(substring("CREATE_SDO"))
           
            .check(jsonPath("$.event_token").saveAs("event_token"))
          )
          .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
     * Create Civil Claim - Do you wish to enter judgment for a sum of damages to be decided ?No
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("Civil_SDOE_RRCByTri_70_EnterJudgmentDamages") {
        exec(http("Civil_SDOE_RRCByTri_70_005_EnterJudgmentDamages")
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
      .group("Civil_SDOE_RRC_80_WhatTrackAllocating") {
        exec(http("Civil_SDOE_RRCByTri_80_005_WhatTrackAllocating")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_SDOClaimsTrack")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sdorequestforreconsider/WhatTrackAllocating.json"))
          .check(substring("documentLink"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      /*======================================================================================
* Create Civil Claim - Standard Direction Order Details
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("Civil_SDOE_RRC_90_SDOdetails") {
        exec(http("Civil_SDOE_RRCByTri_90_005_SDOdetails")
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
      .group("Civil_SDOE_RRCByTri_100_SDOContinue") {
        exec(http("Civil_SDOE_RRC_100_005_SDOContinue")
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
      .group("Civil_SDOE_RRCTri_110_SDOSubmit") {
        exec(http("Civil_SDOE_RRCTri_110_005_SDOSubmit")
          .post("/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .body(ElFileBody("bodies/sdorequestforreconsider/SDOEnhancementsSmallClaimsSubmit.json"))
          .check(substring("CASE_PROGRESSION"))
        )
          .exec(http("Civil_SDOE_RRCByTri_110_010_SDOSubmit")
            .get("/data/internal/cases/#{caseId}")
            .headers(CivilDamagesHeader.MoneyClaimPostHeader)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
            .check(substring("case_id"))
          )
          
         
        
      }
      .pause(MinThinkTime, MaxThinkTime)
  
        .group("Civil_SDOE_RRCByJudge_120_ViewCaseByJudge") {
    
          exec(_.setAll(
            "Idempotencynumber" -> (Common.getIdempotency()),
    
          ))
            .exec(http("Civil_SDOE_RRCByTri_120_005_ViewCaseByJudge")
              .post(BaseURL + "/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
              .headers(CivilDamagesHeader.MoneyClaimNav)
              .body(ElFileBody("bodies/sdorequestforreconsider/viewcasebyjudge.json"))
              .header("accept", "application/json, text/plain, */*")
              .check(status.in(200, 201, 204, 304)))
      
            .exec(http("Civil_SDOE_RRCByJudge_120_005_CaseDetails")
              .get(BaseURL + "/api/wa-supported-jurisdiction/get")
              .headers(Headers.commonHeader)
              .header("accept", "application/json, text/plain, */*")
              .check(status.in(200, 201, 204, 304)))
    
        }
      
  /* ==========================================================================================
  Following is for request reconsideration from claimant
  =========================================================================================== */
  
  val SDORequestForReConsiderFromClaimant=
    group("Civil_SDOE_RRCByC_30_BackToCaseDetailsPage") {
        exec(_.setAll(
          "Idempotencynumber" -> (Common.getIdempotency()),
       
        ))
          .exec(http("Civil_SDOE_RRCByC_30_005_CaseDetails")
            .get(BaseURL + "/data/internal/cases/#{caseId}")
            .headers(Headers.commonHeader)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
            .check(substring("CIVIL"))
            .check(status.in(200, 201, 304)))
        
        
      }
  
      .group("Civil_SDOE_RRCByC_30_ViewCaseByJudge") {
    
        exec(_.setAll(
          "Idempotencynumber" -> (Common.getIdempotency()),
    
        ))
          .exec(http("Civil_SDOE_RRCByC_30_005_ViewCaseByJudge")
            .post(BaseURL + "/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
            .headers(CivilDamagesHeader.MoneyClaimNav)
            .body(ElFileBody("bodies/sdorequestforreconsider/viewcasebyjudge.json"))
            .header("accept", "application/json, text/plain, */*")
            .check(status.in(200, 201, 204, 304)))
  
          .exec(http("Civil_SDOE_RRCByC_30_005_CaseDetails")
            .get(BaseURL + "/api/wa-supported-jurisdiction/get")
            .headers(Headers.commonHeader)
            .header("accept", "application/json, text/plain, */*")
            .check(status.in(200, 201, 204, 304)))
    
      }
      
      
      /*======================================================================================
           * Create Civil Claim - Next Steps - Request For Reconsider from claimant
    ==========================================================================================*/
  
  
      // val returntocasedetailsafternotifydetails =
      .group("Civil_SDOE_RRCByC_40_RequestForReConsider") {
        exec(http("Civil_SDOE_RRCByC_040_005_RequestForReConsider")
          .get(BaseURL + "/workallocation/case/tasks/#{caseId}/event/REQUEST_FOR_RECONSIDERATION/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/json")
          .check(substring("task_required_for_event"))
        )
        
        .exec(http("Civil_SDOE_RRCByC_040_010_RequestForReConsider")
          .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/REQUEST_FOR_RECONSIDERATION?ignore-warning=false")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
          .check(substring("REQUEST_FOR_RECONSIDERATION"))
          .check(jsonPath("$.event_token").saveAs("event_token"))
        )
        .exec(http("Civil_SDOE_RRCByC_040_005_RequestForReConsider")
          .get(BaseURL + "/workallocation/case/tasks/#{caseId}/event/REQUEST_FOR_RECONSIDERATION/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/json")
          .check(substring("task_required_for_event"))
        )
  
          .exec(http("Civil_SDOE_RRCByC_040_005_RequestForReConsider")
            .get(BaseURL + "/data/internal/profile")
            .headers(CivilDamagesHeader.MoneyClaimNav)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-user-profile.v2+json;charset=UTF-8")
            .check(substring("Civil"))
          )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
  
    
      
      /*======================================================================================
           * Create Civil Claim - Mention reason for request for reconsideration from claimant
    ==========================================================================================*/
      //  val returntocasedetailsafternotifydetails =
      .group("Civil_SDOE_RRCByC_50_ReasonRFR") {
        exec(http("Civil_SDOE_RRCByC_50_005_ReasonRFR")
          .post("/data/case-types/CIVIL/validate?pageId=REQUEST_FOR_RECONSIDERATIONRequestForReconsideration")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sdorequestforreconsider/ReasonRFRContinue.json"))
          .check(status.in(200, 201, 204, 304))
           .check(substring("REQUEST_FOR_RECONSIDERATIONRequestForReconsideration"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      /*======================================================================================
* Create Civil Claim - Standard Direction Order Request For Reconsider - Submit by Claimant
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("Civil_SDOE_RRCByC_60_SDOSubmitForRFRByClaimant") {
        exec(http("Civil_SDOE_RRCByC_060_005_SDOSubmitForRFRByClaimant")
          .post("/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .body(ElFileBody("bodies/sdorequestforreconsider/SDORFRByClaimantSubmit.json"))
          .check(substring("CASE_PROGRESSION"))
        )
          .exec(http("Civil_SDOE_RRByC_060_010_RRCSubmit")
            .get("/ data/internal/cases/#{caseId}")
            .headers(CivilDamagesHeader.MoneyClaimPostHeader)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
           // .check(substring("Civil"))
          )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      .group("Civil_SDOE_RRCByC_70_ViewCaseByJudge") {
    
        exec(_.setAll(
          "Idempotencynumber" -> (Common.getIdempotency()),
    
        ))
          .exec(http("Civil_SDOE_RRCByC_70_005_ViewCaseByJudge")
            .post(BaseURL + "/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
            .headers(CivilDamagesHeader.MoneyClaimNav)
            .body(ElFileBody("bodies/sdorequestforreconsider/viewcasebyjudge.json"))
            .header("accept", "application/json, text/plain, */*")
            .check(status.in(200, 201, 204, 304)))
      
          .exec(http("Civil_SDOE_RRCByC_70_005_CaseDetails")
            .get(BaseURL + "/api/wa-supported-jurisdiction/get")
            .headers(Headers.commonHeader)
            .header("accept", "application/json, text/plain, */*")
            .check(status.in(200, 201, 204, 304)))
        
          .pause(20)
    
      }
  
  
  /* ==========================================================================================
   Following is for decision on request for reconsider - Judge
   =========================================================================================== */
  
  val SDODecisionOnRequestForReConsiderByJudge =
   
      group("Civil_SDOE_RRCByJudge_30_BackToCaseDetailsPage") {
        
        exec(_.setAll(
          "Idempotencynumber" -> (Common.getIdempotency()),
        
        ))
          .exec(http("Civil_SDOE_RRCByJudge_30_005_CaseDetails")
            .get(BaseURL + "/data/internal/cases/#{caseId}")
            .headers(Headers.commonHeader)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
            .check(substring("Civil"))
            .check(status.in(200, 201, 304)))
        
      }
  
  .group("Civil_SDOE_RRCByJudge_30_ViewCaseByJudge") {
    
    exec(_.setAll(
      "Idempotencynumber" -> (Common.getIdempotency()),
    
    ))
      .exec(http("Civil_SDOE_RRCByJudge_30_005_ViewCaseByJudge")
        .post(BaseURL + "/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
        .headers(CivilDamagesHeader.MoneyClaimNav)
        .body(ElFileBody("bodies/sdorequestforreconsider/viewcasebyjudge.json"))
        .header("accept", "application/json, text/plain, */*")
        .check(status.in(200, 201, 204, 304)))
  
      .exec(http("Civil_SDOE_RRCByJudge_30_010_CaseDetails")
        .get(BaseURL + "/api/wa-supported-jurisdiction/get")
        .headers(Headers.commonHeader)
        .header("accept", "application/json, text/plain, */*")
        .check(status.in(200, 201, 204, 304)))
    
  }
      
      
      /*======================================================================================
           * Create Civil Claim - Click on Tasks Tab
    ==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("Civil_SDOE_RRCByJudge_40_TaskTabs") {
        exec(http("Civil_SDOE_RRCByJudge_40_005_TaskTab")
          .post(BaseURL + "/workallocation/case/task/#{caseId}")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .body(ElFileBody("bodies/sdorequestforreconsider/TaskTab.json"))
          .check(jsonPath("$[0].id").saveAs("JudgeId"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      /*======================================================================================
           * Create Civil Claim - Start Event 'Assign To Me'
    ==========================================================================================*/
      //  val returntocasedetailsafternotifydetails =
      .group("Civil_SDOE_RRCByJudge_50_AssignToMe") {
        exec(http("Civil_SDOE_RRCByJudge_50_005_AssignToMe")
          .post("/workallocation/task/#{JudgeId}/claim")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          .body(ElFileBody("bodies/sdoflightdelay/AssignToMe.json"))
          .check(status.in(200, 201, 204, 304))
          // .check(substring("assignee"))
        )
        
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
     
      /*======================================================================================
     * Create Civil Claim - Start Event 'Directions - RFR' -
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("Civil_SDOE_RRCByJudge_60_DecisionOnRFRC") {
        exec(http("Civil_SDOE_RRCByJudge_60_005_DecisionOnRFRC")
          .get("/cases/case-details/#{caseId}/trigger/DECISION_ON_RECONSIDERATION_REQUEST/DECISION_ON_RECONSIDERATION_REQUEST?tid=#{JudgeId}")
          .headers(CivilDamagesHeader.headers_notify)
          .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
          .check(status.in(200, 201, 204, 304))
          // .check(substring("task_required_for_event"))
        )
          .exec(Common.configurationui)
  
          .exec(Common.configJson)
  
          .exec(Common.TsAndCs)
  
          .exec(Common.configUI)
  
          .exec(Common.isAuthenticated)
          
          .exec(http("Civil_SDOE_RRCByJudge_60_010_DirectionsFastTrack")
            .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/DECISION_ON_RECONSIDERATION_REQUEST?ignore-warning=false")
            .headers(CivilDamagesHeader.headers_notify)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
            .check(substring("DECISION_ON_RECONSIDERATION_REQUEST"))
            
            .check(jsonPath("$.event_token").saveAs("event_token"))
          )
  
        .exec(http("Civil_SDOE_RRCByJudge_060_015_DirectionsFastTrack")
          .get("/workallocation/case/tasks/#{caseId}/event/DECISION_ON_RECONSIDERATION_REQUEST/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(CivilDamagesHeader.headers_notify)
          .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
          .check(status.in(200, 201, 204, 304))
        )
          .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      /*======================================================================================
     * Create Civil Claim - Decision On Request For Reconsideration - No Create New SDO
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("Civil_SDOE_RRCByJudge_70_DecisionEnterJudgmentDamages") {
        exec(http("Civil_SDOE_RRCByJudge_70_005_DecisionEnterJudgmentDamages")
          .post("/data/case-types/CIVIL/validate?pageId=DECISION_ON_RECONSIDERATION_REQUESTJudgeResponseToReconsideration")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sdorequestforreconsider/DecisionOnRFRByJudge.json"))
          .check(substring("DECISION_ON_RECONSIDERATION_REQUESTJudgeResponseToReconsideration"))
        )
    
      }
      .pause(MinThinkTime, MaxThinkTime)
      
  
      /*======================================================================================
* Create Civil Claim - Decision On Request For ReConsideration - Submit
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("Civil_SDOE_RRCByJudge_110_DecisionOnRFRSubmit") {
        exec(http("Civil_SDOE_RRCByJudge_110_005_DecisionOnRFRSubmit")
          .post("/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .body(ElFileBody("bodies/sdorequestforreconsider/SDOEnhancementsDecisionOnRFRSubmitByJudge.json"))
          .check(substring("CIVIL"))
        )
  
        .exec(http("Civil_SDOE_RRCByJudge_110_010_DecisionOnRFRSubmit")
          .post("/workallocation/task/#{JudgeId}/complete")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/json")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .body(ElFileBody("bodies/sdorequestforreconsider/SDOEnhancementsDecisionOnRFRCompleteByJudge.json"))
          .check(status.in(200, 201, 204, 304))
        )
          
          .exec(http("Civil_SDOE_RRCByJudge_110_010_SDOCaseInternal")
            .get("/data/internal/cases/#{caseId}")
            .headers(CivilDamagesHeader.MoneyClaimPostHeader)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
            .check(substring("CIVIL"))
          )
    
    
      }
      .pause(MinThinkTime, MaxThinkTime)
  
        .group("Civil_SDOE_RRCByJudge_ViewCaseByJudge") {
    
          exec(_.setAll(
            "Idempotencynumber" -> (Common.getIdempotency()),
    
          ))
            .exec(http("Civil_SDOE_RRCByJudge_120_005_ViewCaseByJudge")
              .post(BaseURL + "/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
              .headers(CivilDamagesHeader.MoneyClaimNav)
              .body(ElFileBody("bodies/sdorequestforreconsider/viewcasebyjudge.json"))
              .header("accept", "application/json, text/plain, */*")
              .check(status.in(200, 201, 204, 304)))
      
            .exec(http("Civil_SDOE_RRCByJudge_120_010_CaseDetails")
              .get(BaseURL + "/api/wa-supported-jurisdiction/get")
              .headers(Headers.commonHeader)
              .header("accept", "application/json, text/plain, */*")
              .check(status.in(200, 201, 204, 304)))
    
        }
  
  
  val SDORequestForReConsiderByJudge =
   
      group("Civil_SDOE_RRCByTri_30_BackToCaseDetailsPage") {
        
        exec(_.setAll(
          "Idempotencynumber" -> (Common.getIdempotency()),
        
        ))
          .exec(http("Civil_SDOE_RRCByJudge_30_005_CaseDetails")
            .get(BaseURL + "/data/internal/cases/#{caseId}")
            .headers(Headers.commonHeader)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
            .check(substring("Civil"))
            .check(status.in(200, 201, 304)))
        
        
      }
  
        .group("Civil_SDOE_RRCByJudge_30_ViewCaseByJudge") {
    
          exec(_.setAll(
            "Idempotencynumber" -> (Common.getIdempotency()),
    
          ))
            .exec(http("Civil_SDOE_RRCByTri_30_005_ViewCaseByJudge")
              .post(BaseURL + "/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
              .headers(CivilDamagesHeader.MoneyClaimNav)
              .body(ElFileBody("bodies/sdorequestforreconsider/viewcasebyjudge.json"))
              .header("accept", "application/json, text/plain, */*")
              .check(status.in(200, 201, 204, 304)))
  
            .exec(http("Civil_SDOE_RRCByJudge_30_005_CaseDetails")
              .get(BaseURL + "/api/wa-supported-jurisdiction/get")
              .headers(Headers.commonHeader)
              .header("accept", "application/json, text/plain, */*")
              .check(status.in(200, 201, 204, 304)))
    
        }
      
      
      /*======================================================================================
           * Create Civil Claim - Click on Tasks Tab
    ==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("Civil_SDOE_RRCByJudge_40_TaskTabs") {
        exec(http("Civil_SDOE_RRCByJudge_40_005_TaskTabs")
          .post(BaseURL + "/workallocation/case/task/#{caseId}")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .body(ElFileBody("bodies/sdorequestforreconsider/TaskTab.json"))
          .check(jsonPath("$[0].id").saveAs("JudgeId"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      /*======================================================================================
           * Create Civil Claim - Start Event 'Assign To Me'
    ==========================================================================================*/
      //  val returntocasedetailsafternotifydetails =
      .group("Civil_SDOE_RRCByJudge_50_AssignToMe") {
        exec(http("Civil_SDOE_RRCByJudge_50_005_AssignToMe")
          .post("/workallocation/task/#{JudgeId}/claim")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          .body(ElFileBody("bodies/sdorequestforreconsider/AssignToMe.json"))
          .check(status.in(200, 201, 204, 304))
          // .check(substring("assignee"))
        )
          .exec(http("Civil_SDOE_RRCByJudge_50_010_AssignToMe")
            .post("/workallocation/case/task/#{caseId}")
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
      .group("Civil_SDOE_RRCByJudge_60_DirectionsRRC") {
        exec(http("Civil_SDOE_RRCByJudge_60_005_DirectionsRRC")
          .get("/cases/case-details/#{caseId}/trigger/CREATE_SDO/CREATE_SDOSmallClaims?tid=#{JudgeId}")
          .headers(CivilDamagesHeader.headers_notify)
          .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
          //  .check(substring("assignee"))
        )
  
          .exec(Common.configurationui)
  
          .exec(Common.configJson)
  
          .exec(Common.TsAndCs)
  
          .exec(Common.configUI)
  
          .exec(Common.isAuthenticated)
          
          .exec(http("Civil_SDOE_RRCByJudge_60_010_DirectionRRCCase")
            .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/CREATE_SDO?ignore-warning=false")
            .headers(CivilDamagesHeader.headers_notify)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
            .check(substring("CREATE_SDO"))
            .check(jsonPath("$.event_token").saveAs("event_token"))
          )
          .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      /*======================================================================================
* Create Civil Claim - Standard Direction Order Details
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("Civil_SDOE_RRCByJudge_70_SDOdetails") {
        exec(http("Civil_SDOE_RRCByJudge_70_005_SDOdetails")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_SDOSmallClaims")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sdorequestforreconsider/SDODetailsByJudge.json"))
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
      .group("Civil_SDOE_RRCByJudge_80_SDOContinue") {
        exec(http("Civil_SDOE_RRCByJudge_80_005_SDOContinue")
          .post("/data/case-types/CIVIL/validate?pageId=pageId=CREATE_SDOOrderPreview")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sdorequestforreconsider/SDOEnhancementsContinueSmallClaimsByJudgeFinal.json"))
          .check(substring("sdoOrderDocument"))
        )
        
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
* Create Civil Claim - Standard Direction Order Submit
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("Civil_SDOE_RRCJudge_090_SDOSubmit") {
        exec(http("Civil_SDOE_RRCJudge_090_005_SDOSubmit")
          .post("/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .body(ElFileBody("bodies/sdorequestforreconsider/SDOEnhancementsDecisionOnRFRSubmitByJudgeFinal.json"))
          .check(substring("CASE_PROGRESSION"))
        )
          .exec(http("Civil_SDOE_RRCByJudge_090_010_SDOSubmit")
            .get("/data/internal/cases/#{caseId}")
            .headers(CivilDamagesHeader.MoneyClaimPostHeader)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
            .check(substring("case_id"))
          )
        
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
  
  
  
  
  
  
     
  
}
