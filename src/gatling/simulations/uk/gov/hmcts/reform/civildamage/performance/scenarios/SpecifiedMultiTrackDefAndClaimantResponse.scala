
package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef.{exec, _}
import io.gatling.http.Predef.{http, _}
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, Common, Environment}

import java.io.{BufferedWriter, FileWriter}

object SpecifiedMultiTrackDefAndClaimantResponse {
  
  val BaseURL = Environment.baseURL
  val IdAMURL = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime
  val postcodeFeeder = csv("postcodes.csv").random
 
  
  /*======================================================================================
                 * Create Specified Claim - Start Event 'Respond to Claim'
      ==========================================================================================*/
  
       val RespondToClaim =
  
         group("CivilMT_DefResponse_030_BackToCaseDetailsPage") {
           exec(http("CivilMT_DefResponse_030_005_CaseDetails")
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
           .group("CivilMT_DefResponse_040_RespondToClaim") {
             exec(http("CivilMT_DefResponse_40_005_RespondToClaim")
               .get("/workallocation/case/tasks/#{caseId}/event/DEFENDANT_RESPONSE_SPEC/caseType/CIVIL/jurisdiction/CIVIL")
               .headers(CivilDamagesHeader.MoneyClaimNav)
               .check(substring("task_required_for_event"))
             )
        
               .exec(http("CivilMT_DefResponse_040_010_RespondToClaim")
                 .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/DEFENDANT_RESPONSE_SPEC?ignore-warning=false")
                 .headers(CivilDamagesHeader.headers_notify)
                 .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
                 .check(substring("DEFENDANT_RESPONSE"))
                /* .check(jsonPath("$.case_fields[75].value.partyID").saveAs("repPartyID"))
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
           .group("CivilMT_DefResponse_050_RespondDefDetails") {
             exec(http("CivilMT_DefResponse_430_005_RespondDefDetails")
               .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECRespondentCheckList")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
               .body(ElFileBody("bodies/multitrackspec/RespondDefCheckList.json"))
               .check(substring("DEFENDANT_RESPONSE_SPECRespondentCheckList"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
    
    
    
           /*======================================================================================
        * Create Civil Claim - Respond to Claim Defendant Details
     ==========================================================================================*/
           // val returntocasedetailsafternotifydetails =
           .group("CivilMT_DefResponse_060_RespondDefDetails") {
             exec(http("CivilMT_DefResponse_060_005_RespondDefDetails")
               .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECResponseConfirmNameAddress")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
               .body(ElFileBody("bodies/multitrackspec/RespondDefDetails.json"))
               .check(substring("DEFENDANT_RESPONSE_SPECResponseConfirmNameAddress"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
  
  
           /*======================================================================================
        * Create Civil Claim - Respond to Claim Defendant Details
     ==========================================================================================*/
           // val returntocasedetailsafternotifydetails =
           .group("CivilMT_DefResponse_070_RespondDefDetails") {
             exec(http("CivilMT_DefResponse_070_005_RespondDefDetails")
               .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECResponseConfirmDetails")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
               .body(ElFileBody("bodies/multitrackspec/RespondDefConfirmDetails.json"))
               .check(substring("DEFENDANT_RESPONSE_SPECResponseConfirmDetails"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
    
    
    
           /*======================================================================================
     * Create Civil Claim - Respond to Claim Defendant Choice - Reject
     ==========================================================================================*/
    
           .group("CivilMT_DefResponse_080_ResponseTypeSpec") {
             exec(http("CivilMT_DefResponse_080_005_RespondDefChoice")
               .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECRespondentResponseTypeSpec")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .body(ElFileBody("bodies/multitrackspec/RespondDefChoice.json"))
               .check(substring("DEFENDANT_RESPONSE_SPECRespondentResponseTypeSpec"))
               //change when you get around to it
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
  
  
           /*======================================================================================
     * Create Civil Claim - Respond to Claim Defendant Choice - Defence route
     ==========================================================================================*/
  
           .group("CivilMT_DefResponse_090_SpecDefRoute") {
             exec(http("CivilMT_DefResponse_440_005_SpecDefRoute")
               .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECdefenceRoute")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .body(ElFileBody("bodies/multitrackspec/RespondDefRoute.json"))
               .check(substring("DEFENDANT_RESPONSE_SPECdefenceRoute"))
               //change when you get around to it
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
  
  
           /*======================================================================================
     * Create Civil Claim - Your File Reference
     ==========================================================================================*/
    
           .group("CivilMT_DefResponse_100_SpecUpload") {
             exec(http("CivilMT_DefResponse_100_005_SpecUpload")
               .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECUpload")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .body(ElFileBody("bodies/multitrackspec/RespSpecUpload.json"))
               .check(substring("DEFENDANT_RESPONSE_SPECUpload"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
  
  
  
           /*======================================================================================
     * Create Civil Claim - Adding Timeline
     ==========================================================================================*/
  
           .group("CivilMT_DefResponse_110_AddTimeLine") {
             exec(http("CivilMT_DefResponse_110_005_AddTimeLine")
               .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECHowToAddTimeline")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .body(ElFileBody("bodies/multitrackspec/RespSpecTimeline.json"))
               .check(substring("DEFENDANT_RESPONSE_SPECHowToAddTimeline"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
  
  
           /*======================================================================================
     * Create Civil Claim - Adding manual Timeline
     ==========================================================================================*/
  
           .group("CivilMT_DefResponse_120_TimelineManual") {
             exec(http("CivilMT_DefResponse_120_005_TimelineManual")
               .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECHowToAddTimelineManual")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .body(ElFileBody("bodies/multitrackspec/RespSpecTimelineManual.json"))
               .check(substring("DEFENDANT_RESPONSE_SPECHowToAddTimelineManual"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
  
  
           /*======================================================================================
     * Create Civil Claim -File directions questionnaire
     ==========================================================================================*/
  
           .group("CivilMT_DefResponse_130_FileDirectionsQuestionnaire") {
             exec(http("CivilMT_DefResponse_130_005_FileDirectionsQuestionnaire")
               .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECFileDirectionsQuestionnaire")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .body(ElFileBody("bodies/multitrackspec/RespFileDirectionsQuestionnaire.json"))
               .check(substring("respondent1DQFileDirectionsQuestionnaire"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
  
           /*======================================================================================
     * Create Civil Claim - Disclosure of electronic documents
     ==========================================================================================*/
  
           .group("CivilMT_DefResponse_140_DisclosureElectDocuments") {
             exec(http("CivilMT_DefResponse_140_005_DisclosureElectDocuments")
               .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEDisclosureOfElectronicDocuments")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .body(ElFileBody("bodies/multitrackspec/DisclosureElectDocuments.json"))
               .check(substring("RESPONSEDisclosureOfElectronicDocuments"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
  
  
  
  
           /*======================================================================================
 * Create Civil Claim - Disclosure of non-electronic documents
 ==========================================================================================*/
  
           .group("CivilMT_DefResponse_150_DisclosureNonElectDocuments") {
             exec(http("CivilMT_DefResponse_150_005_DisclosureNonElectDocuments")
               .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEDisclosureOfNonElectronicDocuments")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .body(ElFileBody("bodies/multitrackspec/DisclosureNonElectDocuments.json"))
               .check(substring("RESPONSEDisclosureOfNonElectronicDocuments"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
  
  
           /*======================================================================================
 * Create Civil Claim - Disclosure of non-electronic documents
 ==========================================================================================*/
  
           .group("CivilMT_DefResponse_160_SpecDisclosureReports") {
             exec(http("CivilMT_DefResponse_160_005_SpecDisclosureReports")
               .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECDisclosureReport")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .body(ElFileBody("bodies/multitrackspec/DisclosureReports.json"))
               .check(substring("SPECDisclosureReport"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
  
          
           
           
         
           /*======================================================================================
     * Create Civil Claim - Do you want to use an expert?
     ==========================================================================================*/
    
           .group("CivilMT_DefResponse_170_UseAnExpert") {
             exec(http("CivilMT_DefResponse_170_005_UseAnExpert")
               .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECExperts")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .body(ElFileBody("bodies/multitrackspec/UseAnExpert.json"))
               .check(substring("DEFENDANT_RESPONSE_SPECExperts"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
    
    
    
           /*======================================================================================
     * Create Civil Claim - Are there any witnesses who should attend the hearing?
     ==========================================================================================*/
    
           .group("CivilMT_DefResponse_180_AnyWitnesses") {
             exec(http("CivilMT_DefResponse_180_005_AnyWitnesses")
               .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECWitnesses")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .body(ElFileBody("bodies/multitrackspec/WitnessesToAppear.json"))
               .check(substring("DEFENDANT_RESPONSE_SPECWitnesses"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
    
    
    
           /*======================================================================================
     * Create Civil Claim - Welsh language
     ==========================================================================================*/
    
           .group("CivilMT_DefResponse_190_WelshLanguage") {
             exec(http("CivilMT_DefResponse_190_005_WelshLanguage")
               .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECLanguage")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .body(ElFileBody("bodies/multitrackspec/WelshLanguage.json"))
               .check(substring("DEFENDANT_RESPONSE_SPECLanguage"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
    
    
           /*======================================================================================
     * Create Civil Claim - Hearing Availability
     ==========================================================================================*/
    
           .group("CivilMT_DefResponse_200_HearingAvailability") {
             exec(http("CivilMT_DefResponse_200_005_HearingAvailability")
               .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECHearingLRspec")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .body(ElFileBody("bodies/multitrackspec/HearingAvailability.json"))
               .check(substring("DEFENDANT_RESPONSE"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
  
  
           /*======================================================================================
     * Create Civil Claim - Upload draft directions
     ==========================================================================================*/
  
           .group("CivilMT_DefResponse_210_UploadDraftDirections") {
             exec(http("CivilMT_DefResponse_210_005_UploadDraftDirections")
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
               .check(jsonPath("$.documents[0].hashToken").saveAs("specdefmultitrackhashtoken"))
               .check(jsonPath("$.documents[0]._links.self.href").saveAs("specdefmultitrackdocurl"))
               .check(substring("casesummary.docx")))
           }
           .pause(MinThinkTime, MaxThinkTime)
  
  
  
           /*======================================================================================
     * Create Civil Claim - Upload draft directions Submit
     ==========================================================================================*/
  
           .group("CivilMT_DefResponse_220_ResponseDraftDirections") {
             exec(http("CivilMT_DefResponse_220_005_ResponseDraftDirections")
               .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEDraftDirections")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .body(ElFileBody("bodies/multitrackspec/UploadDraftDirectionsDef.json"))
               .check(substring("RESPONSEDraftDirections"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
  
  
  
           /*======================================================================================
     * Create Civil Claim - Court location code
     ==========================================================================================*/
    
           .group("CivilMT_DefResponse_230_CourtLocationCode") {
             exec(http("CivilMT_DefResponse_230_005_CourtLocationCode")
               .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECRequestedCourtLocationLRspec")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .body(ElFileBody("bodies/multitrackspec/ResCourtLocationCode.json"))
               .check(substring("DEFENDANT_RESPONSE_SPECRequestedCourtLocationLRspec"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
    
           /*======================================================================================
     * Create Civil Claim - Support with access needs
     ==========================================================================================*/
    
           .group("CivilMT_DefResponse_240_SupportAccessNeeds") {
             exec(http("CivilMT_DefResponse_240_005_SupportAccessNeeds")
               .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECHearingSupport")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .body(ElFileBody("bodies/multitrackspec/SupportAccessNeeds.json"))
               .check(substring("DEFENDANT_RESPONSE_SPECHearingSupport"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
    
    
    
           /*======================================================================================
     * Create Civil Claim - Vulnerability Questions
     ==========================================================================================*/
    
           .group("CivilMT_DefResponse_250_VulnerabilityQuestions") {
             exec(http("CivilMT_DefResponse_250_005_VulnerabilityQuestions")
               .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECVulnerabilityQuestions")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .body(ElFileBody("bodies/multitrackspec/VulnerabilityQuestions.json"))
               .check(substring("DEFENDANT_RESPONSE_SPECVulnerabilityQuestions"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
    
    
           /*======================================================================================
     * Create Civil Claim - Respond to claim SoT
     ==========================================================================================*/
    
           .group("CivilMT_DefResponse_260_RespondToClaimSoT") {
             exec(http("CivilMT_DefResponse_260_005_RespondToClaimSoT")
               .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECStatementOfTruth")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .body(ElFileBody("bodies/multitrackspec/RespondToClaimSoT.json"))
               .check(substring("DEFENDANT_RESPONSE_SPECStatementOfTruth"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
    
    
    
           /*======================================================================================
     * Create Civil Claim - Respond to claim Submit
     ==========================================================================================*/
    
           .group("CivilMT_DefResponse_270_RespondToClaimSubmit") {
             exec(http("CivilMT_DefResponse_270_005_RespondToClaimSubmit")
               .post("/data/cases/#{caseId}/events")
               .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
               .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
               .header("X-Xsrf-Token", "#{XSRFToken}")
               .body(ElFileBody("bodies/multitrackspec/RespondToClaimSubmit.json"))
               .check(substring("AWAITING_APPLICANT_INTENTION"))
             )
        
              /* .exec { session =>
                 val fw = new BufferedWriter(new FileWriter("ResponseToClaimCompleted.csv", true))
                 try {
                   fw.write(session("caseId").as[String] + "\r\n")
                 } finally fw.close()
                 session
               }*/
           }
           .pause(MinThinkTime, MaxThinkTime)
  
  
  /*======================================================================================
      * Create Specified Civil Claim - Respond to Defence
      ==========================================================================================*/
  
  val RespondToDefence =
    
    group("CivilMT_ClaimantIntent_030_BackToCaseDetailsPage") {
      
      exec(_.setAll(
        "Idempotencynumber" -> (Common.getIdempotency()),
        "LRrandomString" -> Common.randomString(5)
      ))
        
        .exec(http("Civil_CreateClaim_030_005_CaseDetails")
          .get("/data/internal/cases/#{caseId}")
          .headers(CivilDamagesHeader.headers_717)
          .check(substring("Civil"))
          .check(status.in(200, 201, 304)))
      
      
    }
      /*======================================================================================
           * Create Civil Claim - Start Event 'View and Respond to Defence'
    ==========================================================================================*/
     
      .group("CivilMT_ClaimantIntent_040_RespondToDefence") {
        exec(http("CivilMT_ClaimantIntent_040_005_RespondToDefence")
          .get("/workallocation/case/tasks/#{caseId}/event/CLAIMANT_RESPONSE_SPEC/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .check(substring("task_required_for_event"))
        )
          .exec(http("CivilMT_ClaimantIntent_040_010_RespondToDefence")
            .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/CLAIMANT_RESPONSE_SPEC?ignore-warning=false")
            .headers(CivilDamagesHeader.headers_notify)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
            .check(substring("CLAIMANT_RESPONSE_SPEC"))
           // .check(jsonPath("$.case_fields[0].value.partyID").saveAs("repPartyID"))
           // .check(jsonPath("$.case_fields[0].value.partyName").saveAs("partyName"))
            .check(jsonPath("$..formatted_value.documentLink.document_url").saveAs("document_url"))
            .check(regex("partyID\":\"(.*?)\"").saveAs("repPartyID"))
            .check(regex("partyName\":\"(.*?)\"").saveAs("partyName"))
            .check(jsonPath("$..formatted_value.documentLink.document_filename").saveAs("document_filename"))
            .check(jsonPath("$..formatted_value.documentSize").saveAs("document_size"))
            .check(jsonPath("$..formatted_value.createdDatetime").saveAs("createDateTime"))
            .check(jsonPath("$.event_token").saveAs("event_token"))
          )
          .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
  * Create Civil Claim - View and respond to defence
  ==========================================================================================*/
      
      .group("CivilMT_ClaimantIntent_050_ViewAndRespond") {
        exec(http("CivilMT_ClaimantIntent_050_005_ViewAndRespond")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECRespondentResponse")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("x-xsrf-token", "#{XSRFToken}")
          .body(ElFileBody("bodies/multitrackspec/ViewAndRespond.json"))
          .check(substring("CLAIMANT_RESPONSE_SPECRespondentResponse"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      
     
      
      /*======================================================================================
  * Create Civil Claim - View and respond to defence Submit
  ==========================================================================================*/
      
      .group("CivilMT_ClaimantIntent_060_DefenceResposeDocument") {
        exec(http("CivilMT_ClaimantIntent_060_005_DefenceResponseDocument")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECApplicantDefenceResponseDocument")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .body(ElFileBody("bodies/multitrackspec/UploadResponseToDef.json"))
          .check(substring("SPECApplicantDefenceResponseDocument"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      /*======================================================================================
* Create Civil Claim -File directions questionnaire
==========================================================================================*/
  
      .group("CivilMT_ClaimantIntent_070_FileDirectionsQuestionnaire") {
        exec(http("CivilMT_ClaimantIntent_070_005_FileDirectionsQuestionnaire")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECFileDirectionsQuestionnaire")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("x-xsrf-token", "#{XSRFToken}")
          .body(ElFileBody("bodies/multitrackspec/ClaimantFileDirectionsQuestionnaire.json"))
          .check(substring("CLAIMANT_RESPONSE_SPECFileDirectionsQuestionnaire"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
* Create Civil Claim - Disclosure of electronic documents
==========================================================================================*/
  
      .group("CivilMT_ClaimantIntent_080_DisclosureElectDocuments") {
        exec(http("CivilMT_ClaimantIntent_080_005_DisclosureElectDocuments")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECDisclosureOfElectronicDocuments")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("x-xsrf-token", "#{XSRFToken}")
          .body(ElFileBody("bodies/multitrackspec/ClaimantDisclosureElectDocuments.json"))
          .check(substring("CLAIMANT_RESPONSE_SPECDisclosureOfElectronicDocuments"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  
  
      /*======================================================================================
* Create Civil Claim - Disclosure of non-electronic documents
==========================================================================================*/
  
      .group("CivilMT_ClaimantIntent_090_DisclosureNonElectDocuments") {
        exec(http("CivilMT_ClaimantIntent_090_005_DisclosureNonElectDocuments")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECDisclosureOfNonElectronicDocuments")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("x-xsrf-token", "#{XSRFToken}")
          .body(ElFileBody("bodies/multitrackspec/ClaimantDisclosureNonElectDocuments.json"))
          .check(substring("RESPONSE_SPECDisclosureOfNonElectronicDocuments"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  
  
      /*======================================================================================
* Create Civil Claim - Disclosure of non-electronic documents
==========================================================================================*/
  
      .group("CivilMT_ClaimantIntent_100_SpecDisclosureReports") {
        exec(http("CivilMT_ClaimantIntent_100_005_SpecDisclosureReports")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECDisclosureReport")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("x-xsrf-token", "#{XSRFToken}")
          .body(ElFileBody("bodies/multitrackspec/ClaimantDisclosureReports.json"))
          .check(substring("SPECDisclosureReport"))
          )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      /*======================================================================================
  * Create Civil Claim - Claim Expert
  ==========================================================================================*/
      
      .group("CivilMT_ClaimantIntent_110_Experts") {
        exec(http("CivilMT_ClaimantIntent_110_005_FileDirectionsQuestionnaireDef")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECExperts")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .body(ElFileBody("bodies/multitrackspec/ClaimantExpertQuestionaire.json"))
          .check(substring("CLAIMANT_RESPONSE_SPECExperts"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
     
      
      /*======================================================================================
  * Create Civil Claim -Are there any witnesses who should attend the hearing?
  ==========================================================================================*/
      
      .group("CivilMT_ClaimantIntent_120_AnyWitnessesClaim") {
        exec(http("CivilMT_ClaimantIntent_120_005_AnyWitnessesClaim")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECWitnesses")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .body(ElFileBody("bodies/multitrackspec/ClaimAnyWitnessesClaim.json"))
          .check(substring("CLAIMANT_RESPONSE_SPECWitnesses"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
  * Create Civil Claim -Welsh language
  ==========================================================================================*/
      
      .group("CivilMT_ClaimantIntent_130_WelshLanguageClaim") {
        exec(http("CivilMT_ClaimantIntent_130_005_WelshLanguageClaim")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECLanguage")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .body(ElFileBody("bodies/multitrackspec/WelshLanguageClaim.json"))
          .check(substring("CLAIMANT_RESPONSE_SPECLanguage"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
  * Create Civil Claim -Hearing availability
  ==========================================================================================*/
      
      .group("CivilMT_ClaimantIntent_140_HearingAvailability") {
        exec(http("CivilMT_ClaimantIntent_140_HearingAvailability")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECHearing")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .body(ElFileBody("bodies/multitrackspec/HearingAvailabilityClaim.json"))
          .check(substring("CLAIMANT_RESPONSE_SPECHearing"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      /*======================================================================================
* Create Civil Claim - Upload draft directions
==========================================================================================*/
  
      .group("CivilMT_ClaimantIntent_150_UploadDraftDirections") {
        exec(http("CivilMT_ClaimantIntent_150_005_UploadDraftDirections")
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
          .check(jsonPath("$.documents[0].hashToken").saveAs("specclaimantmultitrackhashtoken"))
          .check(jsonPath("$.documents[0]._links.self.href").saveAs("specclaimantmultitrackdocurl"))
          .check(substring("casesummary.docx")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  
      /*======================================================================================
* Create Civil Claim - Upload draft directions Submit
==========================================================================================*/
  
      .group("CivilMT_ClaimantIntent_160_UploadDraftDirectionsSubmit") {
        exec(http("CivilMT_ClaimantIntent_160_005_UploadDraftDirectionsSubmit")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECDraftDirections")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("x-xsrf-token", "#{XSRFToken}")
          .body(ElFileBody("bodies/multitrackspec/UploadDraftDirectionsClaimant.json"))
          .check(substring("CLAIMANT_RESPONSE_SPECDraftDirections"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
  * Create Civil Claim -court location
  ==========================================================================================*/
  
      .group("CivilMT_ClaimantIntent_170_HearingAvailabilityClaim") {
        exec(http("CivilMT_ClaimantIntent_170_HearingAvailabilityClaim")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECApplicantCourtLocationLRspec")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("x-xsrf-token", "#{XSRFToken}")
          .body(ElFileBody("bodies/multitrackspec/CourtLocationCodeClaim.json"))
          .check(substring("CLAIMANT_RESPONSE_SPECApplicantCourtLocationLRspec"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      
  
      
      /*======================================================================================
  * Create Civil Claim -Does anyone require support for a court hearing?
  ==========================================================================================*/
      
      .group("CivilMT_DefResponse_180_RequireSupportClaim") {
        exec(http("CivilMT_DefResponse_180_005_RequireSupportClaim")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECHearingSupport")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .body(ElFileBody("bodies/multitrackspec/RequireSupportClaim.json"))
          .check(substring("CLAIMANT_RESPONSE_SPECHearingSupport"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
  * Create Civil Claim -Vulnerability Questions
  ==========================================================================================*/
      
      .group("CivilMT_ClaimantIntent_190_VulnerabilityQuestionsClaim") {
        exec(http("CivilMT_ClaimantIntent_190_005_VulnerabilityQuestionsClaim")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECVulnerabilityQuestions")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("x-xsrf-token", "#{XSRFToken}")
          .body(ElFileBody("bodies/multitrackspec/VulnerabilityQuestionsClaim.json"))
          .check(substring("CLAIMANT_RESPONSE_SPECVulnerabilityQuestions"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      /*======================================================================================
  * Create Civil Claim -Statement of Truth
  ==========================================================================================*/
      
      .group("CivilMT_ClaimantIntent_200_SoTClaim") {
        exec(http("CivilMT_ClaimantIntent_200_005_SoTClaim")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECStatementOfTruth")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("x-xsrf-token", "#{XSRFToken}")
          .body(ElFileBody("bodies/multitrackspec/SoTClaim.json"))
          .check(substring("CLAIMANT_RESPONSE_SPECStatementOfTruth"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
  * Create Civil Claim -View and respond to defence submit
  ==========================================================================================*/
      
      .group("CivilMT_ClaimantIntent_210_ViewRespondToDefenceSubmit") {
        exec(http("CivilMT_ClaimantIntent_210_005_ViewRespondToDefenceSubmit")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("x-xsrf-token", "#{XSRFToken}")
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/multitrackspec/ViewRespondToDefenceSubmit.json"))
          .check(substring("JUDICIAL_REFERRAL"))
        )
          
          .exec { session =>
            val fw = new BufferedWriter(new FileWriter("SpecJDStateLatest.csv", true))
            try {
              fw.write(session("caseId").as[String] + "\r\n")
            } finally fw.close()
            session
          }
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
}
