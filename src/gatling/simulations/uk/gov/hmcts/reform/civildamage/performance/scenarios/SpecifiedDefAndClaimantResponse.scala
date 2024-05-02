
package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef.{http, _}
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, Common, Environment, Headers}

import java.io.{BufferedWriter, FileWriter}

object SpecifiedDefAndClaimantResponse {
  
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
               .get("/workallocation/case/tasks/#{caseId}/event/DEFENDANT_RESPONSE_SPEC/caseType/CIVIL/jurisdiction/CIVIL")
               .headers(CivilDamagesHeader.MoneyClaimNav)
               .check(substring("task_required_for_event"))
             )
        
               .exec(http("XUI_CreateClaim_420_010_RespondToClaim")
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
           .group("XUI_CreateClaim_430_RespondDefDetails") {
             exec(http("XUI_CreateClaim_430_005_RespondDefDetails")
               .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECRespondentCheckList")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
               .body(ElFileBody("bodies/specifiedclaimresponses/RespondDefCheckList.json"))
               .check(substring("DEFENDANT_RESPONSE_SPECRespondentCheckList"))
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
               .check(substring("DEFENDANT_RESPONSE_SPECResponseConfirmNameAddress"))
             )
           }
           .pause(MinThinkTime, MaxThinkTime)
  
  
           /*======================================================================================
        * Create Civil Claim - Respond to Claim Defendant Details
     ==========================================================================================*/
           // val returntocasedetailsafternotifydetails =
           .group("XUI_CreateClaim_430_RespondDefDetails") {
             exec(http("XUI_CreateClaim_430_005_RespondDefDetails")
               .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSE_SPECResponseConfirmDetails")
               .headers(CivilDamagesHeader.MoneyClaimPostHeader)
               .header("x-xsrf-token", "#{XSRFToken}")
               .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
               .body(ElFileBody("bodies/specifiedclaimresponses/RespondDefConfirmDetails.json"))
               .check(substring("DEFENDANT_RESPONSE_SPECResponseConfirmDetails"))
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
               .check(substring("DEFENDANT_RESPONSE_SPECRespondentResponseTypeSpec"))
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
               .check(substring("DEFENDANT_RESPONSE_SPECdefenceRoute"))
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
               .check(substring("DEFENDANT_RESPONSE_SPECUpload"))
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
               .check(substring("DEFENDANT_RESPONSE_SPECHowToAddTimeline"))
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
               .check(substring("DEFENDANT_RESPONSE_SPECHowToAddTimelineManual"))
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
               .check(substring("DEFENDANT_RESPONSE_SPECMediation"))
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
               .check(substring("DEFENDANT_RESPONSE_SPECSmallClaimExperts"))
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
               .check(substring("DEFENDANT_RESPONSE_SPECSmallClaimWitnesses"))
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
               .check(substring("DEFENDANT_RESPONSE_SPECSmaillClaimHearing"))
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
               .check(substring("DEFENDANT_RESPONSE_SPECRequestedCourtLocationLRspec"))
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
               .check(substring("DEFENDANT_RESPONSE_SPECHearingSupport"))
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
               .check(substring("DEFENDANT_RESPONSE_SPECVulnerabilityQuestions"))
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
               .check(substring("DEFENDANT_RESPONSE_SPECStatementOfTruth"))
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
     
      .group("XUI_CreateClaim_620_RespondToDefence") {
        exec(http("XUI_CreateClaim_620_005_RespondToDefence")
          .get("/workallocation/case/tasks/#{caseId}/event/CLAIMANT_RESPONSE_SPEC/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .check(substring("task_required_for_event"))
        )
          .exec(http("XUI_CreateClaim_620_010_RespondToDefence")
            .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/CLAIMANT_RESPONSE_SPEC?ignore-warning=false")
            .headers(CivilDamagesHeader.headers_notify)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
            .check(substring("CLAIMANT_RESPONSE_SPEC"))
            .check(jsonPath("$.case_fields[0].value.partyID").saveAs("repPartyID"))
            .check(jsonPath("$.case_fields[0].value.partyName").saveAs("partyName"))
            .check(jsonPath("$..formatted_value.documentLink.document_url").saveAs("document_url"))
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
      
      .group("XUI_CreateClaim_630_ViewAndRespond") {
        exec(http("XUI_CreateClaim_630_005_ViewAndRespond")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECRespondentResponse")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("x-xsrf-token", "#{XSRFToken}")
          .body(ElFileBody("bodies/specifiedclaimresponses/ViewAndRespond.json"))
          .check(substring("CLAIMANT_RESPONSE_SPECRespondentResponse"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      
     
      
      /*======================================================================================
  * Create Civil Claim - View and respond to defence Submit
  ==========================================================================================*/
      
      .group("XUI_CreateClaim_640_UploadResponseToDefSubmit") {
        exec(http("XUI_CreateClaim_640_005_UploadResponseToDefSubmit")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECApplicantDefenceResponseDocument")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .body(ElFileBody("bodies/specifiedclaimresponses/UploadResponseToDef.json"))
          .check(substring("SPECApplicantDefenceResponseDocument"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      /*======================================================================================
  * Create Civil Claim - Claim Expert
  ==========================================================================================*/
      
      .group("XUI_CreateClaim_650_FileDirectionsQuestionnaireDef") {
        exec(http("XUI_CreateClaim_650_005_FileDirectionsQuestionnaireDef")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECSmallClaimExperts")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .body(ElFileBody("bodies/specifiedclaimresponses/ExpertQuestionaire.json"))
          .check(substring("CLAIMANT_RESPONSE_SPECSmallClaimExperts"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
     
      
      
      
     
      
      
      /*======================================================================================
  * Create Civil Claim -Are there any witnesses who should attend the hearing?
  ==========================================================================================*/
      
      .group("XUI_CreateClaim_690_AnyWitnessesClaim") {
        exec(http("XUI_CreateClaim_690_005_AnyWitnessesClaim")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECSmallClaimWitnesses")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .body(ElFileBody("bodies/specifiedclaimresponses/AnyWitnessesClaim.json"))
          .check(substring("CLAIMANT_RESPONSE_SPECSmallClaimWitnesses"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
  * Create Civil Claim -Welsh language
  ==========================================================================================*/
      
      .group("XUI_CreateClaim_700_WelshLanguageClaim") {
        exec(http("XUI_CreateClaim_700_005_WelshLanguageClaim")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECLanguage")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .body(ElFileBody("bodies/specifiedclaimresponses/WelshLanguageClaim.json"))
          .check(substring("CLAIMANT_RESPONSE_SPECLanguage"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
  * Create Civil Claim -Hearing availability
  ==========================================================================================*/
      
      .group("XUI_CreateClaim_710_HearingAvailabilityClaim") {
        exec(http("XUI_CreateClaim_710_HearingAvailabilityClaim")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECHearing")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .body(ElFileBody("bodies/specifiedclaimresponses/HearingAvailabilityClaim.json"))
          .check(substring("CLAIMANT_RESPONSE_SPECHearing"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  
      /*======================================================================================
  * Create Civil Claim -court location
  ==========================================================================================*/
  
      .group("XUI_CreateClaim_710_HearingAvailabilityClaim") {
        exec(http("XUI_CreateClaim_710_HearingAvailabilityClaim")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECApplicantCourtLocationLRspec")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .body(ElFileBody("bodies/specifiedclaimresponses/CourtLocationCodeClaim.json"))
          .check(substring("CLAIMANT_RESPONSE_SPECApplicantCourtLocationLRspec"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      
      /*======================================================================================
  * Create Civil Claim -Does anyone require support for a court hearing?
  ==========================================================================================*/
      
      .group("XUI_CreateClaim_740_RequireSupportClaim") {
        exec(http("XUI_CreateClaim_740_005_RequireSupportClaim")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECHearingSupport")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .body(ElFileBody("bodies/specifiedclaimresponses/RequireSupportClaim.json"))
          .check(substring("CLAIMANT_RESPONSE_SPECHearingSupport"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
  * Create Civil Claim -Vulnerability Questions
  ==========================================================================================*/
      
      .group("XUI_CreateClaim_750_VulnerabilityQuestionsClaim") {
        exec(http("XUI_CreateClaim_750_005_VulnerabilityQuestionsClaim")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECVulnerabilityQuestions")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .body(ElFileBody("bodies/specifiedclaimresponses/VulnerabilityQuestionsClaim.json"))
          .check(substring("CLAIMANT_RESPONSE_SPECVulnerabilityQuestions"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
   
      
      
      
      /*======================================================================================
  * Create Civil Claim -Statement of Truth
  ==========================================================================================*/
      
      .group("XUI_CreateClaim_770_SoTClaim") {
        exec(http("XUI_CreateClaim_770_005_SoTClaim")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECStatementOfTruth")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .body(ElFileBody("bodies/specifiedclaimresponses/SoTClaim.json"))
          .check(substring("CLAIMANT_RESPONSE_SPECStatementOfTruth"))
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
            val fw = new BufferedWriter(new FileWriter("RequestForReconsiderCases.csv", true))
            try {
              fw.write(session("caseId").as[String] + "\r\n")
            } finally fw.close()
            session
          }
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
}
