
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

  /*======================================================================================
             * SDO Fast Track
  ==========================================================================================*/
  val run=
    feed(sdoenhancementsfasttrackfeeder)
    .group("Civil_CreateClaim_330_BackToCaseDetailsPage") {
      exec(_.setAll(
        "Idempotencynumber" -> Common.getIdempotency(),
        "LRrandomString" -> Common.randomString(5),
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
          .body(ElFileBody("bodies/LRvsLR/TaskTab.json"))
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
         .body(ElFileBody("bodies/LRvsLR/AssignToMe.json"))
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
          .body(ElFileBody("bodies/LRvsLR/EnterJudgmentDamages.json"))
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
          .body(ElFileBody("bodies/LRvsLR/WhatTrackAllocating.json"))
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
    feed(sdoenhancementsfasttrackfeeder)
    .group("Civil_CreateClaim_330_BackToCaseDetailsPage") {
      
      
      exec(_.setAll(
        "Idempotencynumber" -> (Common.getIdempotency()),
        "LRrandomString" -> Common.randomString(5),
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
          .headers(Headers.commonHeader)
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
          .body(ElFileBody("bodies/LRvsLR/TaskTab.json"))
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
          .body(ElFileBody("bodies/LRvsLR/AssignToMe.json"))
          .check(status.in(200, 201, 204,304))
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
          .body(ElFileBody("bodies/LRvsLR/EnterJudgmentDamages.json"))
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
          .body(ElFileBody("bodies/LRvsLR/WhatTrackAllocating.json"))
          .check(substring("documentLink"))
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
          .body(ElFileBody("bodies/LRvsLR/SDOenhancementsfasttrackdetails.json"))
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
          .body(ElFileBody("bodies/LRvsLR/SDOEnhancementsContinueFastTrack.json"))
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
          .body(ElFileBody("bodies/LRvsLR/SDOEnhancementsFastTrackSubmit.json"))
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
  
  
  
  /*======================================================================================
 * Standard Direction Order for Flight Delay
 ==========================================================================================*/
  
  
  val SDOFlightDelay =
    feed(sdoenhancementsfasttrackfeeder)
      .group("Civil_CreateClaim_330_BackToCaseDetailsPage") {
        
        
        exec(_.setAll(
          "Idempotencynumber" -> (Common.getIdempotency()),
          "LRrandomString" -> Common.randomString(5),
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
            .headers(Headers.commonHeader)
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
          .body(ElFileBody("bodies/LRvsLR/TaskTab.json"))
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
          .body(ElFileBody("bodies/LRvsLR/AssignToMe.json"))
          .check(status.in(200, 201, 204, 304))
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
          .get("/cases/case-details/#{caseId}/trigger/CREATE_SDO/CREATE_SDOSmallClaims?tid=#{JudgeId}")
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
          .body(ElFileBody("bodies/sdoflightdelay/EnterJudgmentDamages.json"))
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
          .body(ElFileBody("bodies/sdoflightdelay/WhatTrackAllocating.json"))
          .check(substring("documentLink"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      /*======================================================================================
* Create Civil Claim - Standard Direction Order Details
==========================================================================================*/
      // val returntocasedetailsafternotifydetails =
      .group("XUI_CreateClaim_840_SDOdetails") {
        exec(http("XUI_CreateClaim_840_005_SDOdetails")
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
      .group("XUI_CreateClaim_850_SDOContinue") {
        exec(http("XUI_CreateClaim_850_005_SDOContinue")
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
      .group("XUI_CreateClaim_860_SDOSubmit") {
        exec(http("XUI_CreateClaim_860_005_SDOSubmit")
          .post("/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .body(ElFileBody("bodies/sdoflightdelay/SDOEnhancementsSmallClaimsSubmit.json"))
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
