
package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, Common, Environment}

import java.io.{BufferedWriter, FileWriter}

object UnSpecIntermediateTrackCaseProgression {

  val BaseURL = Environment.baseURL
  val IdAMURL = Environment.idamURL
  val IdamUrl = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val caseFeeder=csv("caseIds.csv").circular
  
  /*======================================================================================
         HearingNotice - Centre Admin
 ==========================================================================================*/
  val HearingNotice =
  // feed(cpfulltestFeeder)
    exec(_.setAll(
      "CaseProgRandomString" -> Common.randomString(5),
      "Plus2WeeksDay" -> Common.getPlus2WeeksDay(),
      "Plus2WeeksMonth" -> Common.getPlus2WeeksMonth(),
      "Plus2WeeksYear" -> Common.getPlus2WeeksYear(),
      "EvidenceYear" -> Common.getYear(),
      "EvidenceDay" -> Common.getDay(),
      "EvidenceMonth" -> Common.getMonth())
    )
      
      /*======================================================================================
                        * Civil Progression - Search Case
      ==========================================================================================*/
      .group("CivilIT_HearingNotice_030_SearchCase") {
        exec(http("CivilIT_HearingNotice_030_SearchCase")
          .get(BaseURL + "/data/internal/cases/#{caseId}")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
          .check(substring("Summary")))
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      /*======================================================================================
               *  Civil Progression - '	Directions - Hearing Notice
    ==========================================================================================*/
      .group("CivilIT_HearingNotice_040_ScheduleHearing") {
        exec(http("CivilIT_HearingNotice_040_005_ScheduleHearing")
          .get("/workallocation/case/tasks/#{caseId}/event/HEARING_SCHEDULED/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/json")
          .check(substring("task_required_for_event"))
        )
          
          .exec(http("CivilIT_HearingNotice_040_010_ScheduleHearing")
            .get("/data/internal/cases/#{caseId}/event-triggers/HEARING_SCHEDULED?ignore-warning=false")
            .headers(CivilDamagesHeader.MoneyClaimNav)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
            .check(substring("HEARING_SCHEDULED"))
            .check(jsonPath("$.event_token").saveAs("event_token"))
          )
          .exec(http("CivilIT_HearingNotice_040_015_ScheduleHearing")
            .get("/workallocation/case/tasks/#{caseId}/event/HEARING_SCHEDULED/caseType/CIVIL/jurisdiction/CIVIL")
            .headers(CivilDamagesHeader.MoneyClaimNav)
            .header("accept", "application/json")
            .check(substring("task_required_for_event"))
          )
          .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
             *  *  Civil Progression - '	What hearing notice do you want to create?
  ==========================================================================================*/
      .group("CivilIT_HearingNotice_050_WhatHearingNotice") {
        exec(http("CivilIT_HearingNotice_050_005_WhatHearingNotice")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=HEARING_SCHEDULEDHearingNoticeSelect")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/intermediateunspec/WhatHearingNotice.json"))
          .check(substring("HEARING_SCHEDULEDHearingNoticeSelect"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
           *  *  Civil Progression - '	Is this a listing or a relisting?
 ==========================================================================================*/
      .group("CivilIT_HearingNotice_060_ListOrRelisting") {
        exec(http("CivilIT_HearingNotice_060_005_ListOrRelisting")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=HEARING_SCHEDULEDListingOrRelisting")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/intermediateunspec/ListOrRelisting.json"))
          .check(substring("listingOrRelisting"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
         *  *  Civil Progression - '	Hearing Scheduled Details
 ==========================================================================================*/
      .group("CivilIT_HearingNotice_070_HearingDetails") {
        exec(http("CivilIT_HearingNotice_070_005_HearingDetails")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=HEARING_SCHEDULEDHearingDetails")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/intermediateunspec/HearingDetails.json"))
          .check(substring("validate?pageId=HEARING_SCHEDULEDHearingDetails"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      /*======================================================================================
       *  *  Civil Progression - '	Hearing Schedule Extra Details
 ==========================================================================================*/
      .group("CivilIT_HearingNotice_080_HearingExtraDetails") {
        exec(http("CivilIT_HearingNotice_080_005_HearingExtraDetails")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=HEARING_SCHEDULEDHearingInformation")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/intermediateunspec/HearingExtraDetails.json"))
          .check(substring("information"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      /*======================================================================================
         *  Civil Progression - '	Hearing Schedule Submit
 ==========================================================================================*/
      .group("CivilIT_HearingNotice_090_HearingScheduleSubmit") {
        exec(http("CivilIT_HearingNotice_090_005_HearingScheduleSubmit")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .header("Experimental", "true")
          .body(ElFileBody("bodies/intermediateunspec/HearingScheduleSubmit.json"))
          .check(substring("confirmation_header"))
        )
          .exec { session =>
            val fw = new BufferedWriter(new FileWriter("HearingScheduled.csv", true))
            try {
              fw.write(session("caseId").as[String] + "\r\n")
            } finally fw.close()
            session
          }
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  /*======================================================================================
         HearingNotice - Centre Admin
 ==========================================================================================*/
  val TransferOnlineByHearingAdmin =
  // feed(cpfulltestFeeder)
    exec(_.setAll(
      "CaseProgRandomString" -> Common.randomString(5),
      "Plus2WeeksDay" -> Common.getPlus2WeeksDay(),
      "Plus2WeeksMonth" -> Common.getPlus2WeeksMonth(),
      "Plus2WeeksYear" -> Common.getPlus2WeeksYear(),
      "EvidenceYear" -> Common.getYear(),
      "EvidenceDay" -> Common.getDay(),
      "EvidenceMonth" -> Common.getMonth())
    )
      
      /*======================================================================================
                        * Civil Progression - Search Case
      ==========================================================================================*/
      .group("CivilIT_TransferOnline_030_SearchCase") {
        exec(http("CivilIT_TransferOnline_030_SearchCase")
          .get(BaseURL + "/data/internal/cases/#{caseId}")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        //  .check(substring("Summary"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      /*======================================================================================
               *  Civil Progression - '	Transfer Online
    ==========================================================================================*/
      .group("CivilIT_TransferOnline_040_TransferOnline") {
        exec(http("CivilIT_TransferOnline_040_005_TransferOnline")
          .get("/workallocation/case/tasks/#{caseId}/event/TRANSFER_ONLINE_CASE/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/json")
          .check(substring("task_required_for_event"))
        )
          
          .exec(http("CivilIT_TransferOnline_040_010_TransferOnline")
            .get("/data/internal/cases/#{caseId}/event-triggers/TRANSFER_ONLINE_CASE?ignore-warning=false")
            .headers(CivilDamagesHeader.MoneyClaimNav)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
            .check(substring("TRANSFER_ONLINE_CASE"))
            .check(jsonPath("$.event_token").saveAs("event_token"))
          )
          .exec(http("CivilIT_TransferOnline_040_015_TransferOnline")
            .get("/workallocation/case/tasks/#{caseId}/event/TRANSFER_ONLINE_CASE/caseType/CIVIL/jurisdiction/CIVIL")
            .headers(CivilDamagesHeader.MoneyClaimNav)
            .header("accept", "application/json")
            .check(substring("task_required_for_event"))
          )
          .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
             *  *  Civil Progression - '	What hearing notice do you want to create?
  ==========================================================================================*/
      .group("CivilIT_TransferOnline_050_TransferOnlineCase") {
        exec(http("CivilIT_TransferOnline_050_005_TransferOnlineCase")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=TRANSFER_ONLINE_CASETransferOnlineCase")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/intermediateunspec/TransferOnlineCase.json"))
          .check(substring("TRANSFER_ONLINE_CASETransferOnlineCase"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      /*======================================================================================
         *  Civil Progression - '	Hearing Schedule Submit
 ==========================================================================================*/
      .group("CivilIT_TransferOnline_060_TransferOnlineSubmit") {
        exec(http("CivilIT_TransferOnline_060_005_TransferOnlineSubmit")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .header("Experimental", "true")
          .body(ElFileBody("bodies/intermediateunspec/TransferOnlineSubmit.json"))
          .check(substring("confirmation_header"))
        )
          .exec { session =>
            val fw = new BufferedWriter(new FileWriter("HearingScheduled.csv", true))
            try {
              fw.write(session("caseId").as[String] + "\r\n")
            } finally fw.close()
            session
          }
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  /*======================================================================================
         Hearing Fee - Claimant
==========================================================================================*/
  val HearingFee =

    exec(_.setAll(
      "CaseProgRandomString" -> Common.randomString(5),
      "Idempotencynumber" -> (Common.getIdempotency()))
    )

      /*======================================================================================
                        * Civil Progression - Search Case
      ==========================================================================================*/
      .group("CivilCaseProg_HearingFee_030_SearchCase") {
          exec(http("CivilCaseProg_HearingFee_030_SearchCase")
            .get(BaseURL + "/cases/case-details/#{caseId}")
            .headers(CivilDamagesHeader.MoneyClaimNav))
        // .check(substring("caseFile")))
      }
      .pause(MinThinkTime, MaxThinkTime)

      /*======================================================================================
                 *  Civil Progression - Service Request Tab
      ==========================================================================================*/
      .group("CivilCaseProg_HearingFee_160_ServiceRequest") {
        exec(http("CivilCaseProg_HearingFee_160_005_ServiceRequest")
          .get(BaseURL + "/payments/cases/#{caseId}/paymentgroups")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/json, text/plain, */*")
          .check(jsonPath("$.payment_groups[1].payment_group_reference").saveAs("payment_group_reference"))
          .check(substring("payment_groups"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
             *  Civil Progression - 'Pay Now' event
  ==========================================================================================*/
      .group("CivilCaseProg_HearingFee_170_PayNow") {
        exec(http("CivilCaseProg_HearingFee_170_005_PayNow")
          .post(BaseURL + "/payments/service-request/#{payment_group_reference}/card-payments")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/json, text/plain, */*")
          .body(ElFileBody("bodies/CaseProg/PayNowEvent.json"))
          .check(
            headerRegex("location", """\/card_details\/(.{26})""")
              .ofType[(String)]
              .saveAs("chargeId")
          )
        )
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
       *  Civil Progression - Card Details
==========================================================================================*/
      .group("CivilCaseProg_HearingFee_180_CardDetails") {
        exec(http("CivilCaseProg_HearingFee_180_005_CardDetails")
          .post(BaseURL + "/payments/service-request/#{payment_group_reference}/card-payments")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/json, text/plain, */*")
          .body(ElFileBody("bodies/CaseProg/PayNowEvent.json"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
               *  Civil Progression - 'Pay Now' event
    ==========================================================================================*/
      .group("CivilCaseProg_HearingFee_170_PayNow") {
        exec(http("CivilCaseProg_HearingFee_170_005_PayNow")
          .get(BaseURL + "/payments/pba-accounts")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .check(substring("organisationEntityResponse"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
             *  Civil Progression - Pay using PBA
  ==========================================================================================*/
      .group("CivilCaseProg_HearingFee_180_PayUsingPBA") {
        exec(http("CivilCaseProg_HearingFee_180_005_PayUsingPBA")
          .post(BaseURL + "/payments/service-request/#{payment_group_reference}/pba-payments")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/json, text/plain, */*")
          .body(ElFileBody("bodies/CaseProg/PayUsingPBA.json"))
          .check(substring("payment_reference"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
               *  Civil Progression - Return to Service Request Tab
    ==========================================================================================*/
      .group("CivilCaseProg_HearingFee_190_ServiceRequestReturn") {
        exec(http("CivilCaseProg_HearingFee_190_005_ServiceRequestReturn")
          .get("/payments/cases/#{caseId}/paymentgroups")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .check(jsonPath("$.payment_groups[1].service_request_status").is("Paid"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)




  /*======================================================================================
           Evidence Upload - Claimant
==========================================================================================*/
  val EvidenceUploadClaimant =

    exec(_.setAll(
      "CaseProgRandomString" -> Common.randomString(5),
  "EvidenceYear" -> Common.getYear(),
  "EvidenceDay" -> Common.getDay(),
  "EvidenceMonth" -> Common.getMonth())
    )

      /*======================================================================================
                        * Civil Progression - Search Case
      ==========================================================================================*/
      .group("CivilIT_UploadEvidence_050_SearchCase") {
          exec(http("CivilIT_UploadEvidence_030_SearchCase")
            .get(BaseURL + "/cases/case-details/#{caseId}")
            .headers(CivilDamagesHeader.MoneyClaimNav))
        // .check(substring("caseFile")))
      }
      .pause(MinThinkTime, MaxThinkTime)

      /*======================================================================================
                 *  Civil Progression - 'Upload Your Documents' event
      ==========================================================================================*/
      .group("CivilCaseProg_UploadEvidence_040_UploadYourDocs") {
        exec(http("CivilIT_UploadEvidence_040_005_UploadYourDocs")
          .get("/workallocation/case/tasks/#{caseId}/event/EVIDENCE_UPLOAD_APPLICANT/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .check(substring("task_required_for_event"))
        )
          .exec(http("CivilIT_UploadEvidence_040_010_UploadYourDocs")
            .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/EVIDENCE_UPLOAD_APPLICANT?ignore-warning=false")
            .headers(CivilDamagesHeader.headers_notify)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
            .check(substring("EVIDENCE_UPLOAD_APPLICANT"))
            //  .check(jsonPath("$.case_fields[4].formatted_value.partyID").saveAs("repPartyID"))
            //  .check(jsonPath("$.case_fields[4].formatted_value.partyName").saveAs("partyName"))
            .check(jsonPath("$.event_token").saveAs("event_token"))
          )
          .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))

      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
               *  Civil Progression - 'Upload Your Documents' Continue
    ==========================================================================================*/
      .group("CivilIT_UploadEvidence_050_UploadYourDocsContinue") {
        exec(http("CivilIT_UploadEvidence_050_005_UploadYourDocsContinue")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=EVIDENCE_UPLOAD_APPLICANTEvidenceUpload")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/intermediateunspec/UploadYourDocsContinue.json"))
          .check(substring("APPLICANTEvidenceUpload"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
             *  Civil Progression - Select the type of document
  ==========================================================================================*/
      .group("CivilIT_UploadEvidence_060_TypeOfDocument") {
        exec(http("CivilIT_UploadEvidence_060_005_TypeOfDocument")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=EVIDENCE_UPLOAD_APPLICANTDocumentSelectionFastTrack")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/intermediateunspec/TypeOfDocument.json"))
          .check(substring("EVIDENCE_UPLOAD_APPLICANTDocumentSelectionFastTrack"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
               *  Civil Progression - Disclosure List Upload Claim 1
    ==========================================================================================*/
      .group("CivilIT_UploadEvidence_070_DisclosureListClaim1") {
        exec(http("CivilIT_UploadEvidence_070_005_DisclosureListClaim1")
          .post(BaseURL + "/documentsv2")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundary5IivMwQhKZGomvfQ")
          .header("sec-fetch-dest", "empty")
          .header("sec-fetch-mode", "cors")
          .bodyPart(RawFileBodyPart("files", "3MB.pdf")
            .fileName("3MB.pdf")
            .transferEncoding("binary"))
          .asMultipartForm
          .formParam("classification", "PUBLIC")
          .formParam("caseTypeId", "CIVIL")
          .formParam("jurisdictionId", "CIVIL")
          .check(jsonPath("$.documents[0].hashToken").saveAs("claimMTDisclosureHashToken"))
          .check(jsonPath("$.documents[0]._links.self.href").saveAs("claimMTDisclosureDocument_url"))
          .check(substring("3MB.pdf")))
      }
      .pause(MinThinkTime, MaxThinkTime)

 
      /*======================================================================================
           *  Civil Progression - Select the type of document
==========================================================================================*/
      .group("CivilIT_UploadEvidence_080_EvidenceUploadClaim") {
        exec(http("CivilIT_UploadEvidence_080_005_EvidenceUploadClaim")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=EVIDENCE_UPLOAD_APPLICANTDocumentUpload")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .body(ElFileBody("bodies/intermediateunspec/EvidenceUploadClaim.json"))
          .check(substring("EVIDENCE_UPLOAD_APPLICANTDocumentUpload"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
         *  Civil Progression - Upload Documents Submit Claim
==========================================================================================*/
      .group("CivilIT_UploadEvidence_090_UploadDocumentsSubmitClaim") {
        exec(http("CivilCaseProg_UploadEvidence_090_005_UploadDocumentsSubmitClaim")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .header("Experimental", "true")
          .body(ElFileBody("bodies/intermediateunspec/UploadDocumentsSubmitClaim.json"))
          .check(substring("confirmation_header"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)


  /*======================================================================================
           Evidence Upload - Defendant
==========================================================================================*/
  val EvidenceUploadDefendant =

    exec(_.setAll(
      "CaseProgRandomString" -> Common.randomString(5),
      "EvidenceYear" -> Common.getYear(),
      "EvidenceDay" -> Common.getDay(),
     // "caseId" -> "1697800443682595",
      "EvidenceMonth" -> Common.getMonth())
    )

      /*======================================================================================
                        * Civil Progression - Search Case
      ==========================================================================================*/
      .group("CivilCaseProg_UploadEvidence_030_SearchCase") {
          exec(http("CivilCaseProg_UploadEvidence_030_SearchCase")
            .get(BaseURL + "/cases/case-details/#{caseId}")
            .headers(CivilDamagesHeader.MoneyClaimNav))
        // .check(substring("caseFile")))
      }
      .pause(MinThinkTime, MaxThinkTime)

      /*======================================================================================
                 *  Civil Progression - 'Upload Your Documents' event
      ==========================================================================================*/
      .group("CivilCaseProg_UploadEvidence_110_UploadYourDocsDefend") {
        exec(http("CivilCaseProg_UploadEvidence_110_005_UploadYourDocsDefend")
          .get("/workallocation/case/tasks/#{caseId}/event/EVIDENCE_UPLOAD_RESPONDENT/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .check(substring("task_required_for_event"))
        )

          .exec(http("CivilCaseProg_UploadEvidence_110_010_AddACaseNote")
            .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/EVIDENCE_UPLOAD_RESPONDENT?ignore-warning=false")
            .headers(CivilDamagesHeader.headers_notify)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
            .check(substring("EVIDENCE_UPLOAD_RESPONDENT"))
            //  .check(jsonPath("$.case_fields[4].formatted_value.partyID").saveAs("repPartyID"))
            //  .check(jsonPath("$.case_fields[4].formatted_value.partyName").saveAs("partyName"))
            .check(jsonPath("$.event_token").saveAs("event_token"))
          )
          .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))

      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
               *  Civil Progression - 'Upload Your Documents' Continue
    ==========================================================================================*/
      .group("CivilCaseProg_UploadEvidence_120_UploadYourDocsContinueDef") {
        exec(http("CivilCaseProg_UploadEvidence_120_005_UploadYourDocsContinueDef")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=EVIDENCE_UPLOAD_RESPONDENTEvidenceUpload")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/CaseProg/UploadYourDocsContinueDef.json"))
          .check(substring("caseProgAllocatedTrack"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
             *  Civil Progression - Select the type of document
  ==========================================================================================*/
      .group("CivilCaseProg_UploadEvidence_130_TypeOfDocumentDef") {
        exec(http("CivilCaseProg_UploadEvidence_130_005_TypeOfDocumentDef")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=EVIDENCE_UPLOAD_RESPONDENTDocumentSelectionFastTrack")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/CaseProg/TypeOfDocumentDef.json"))
          .check(substring("EVIDENCE_UPLOAD_RESPONDENTDocumentSelectionFastTrack"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
               *  Civil Progression - Disclosure List Upload Def
    ==========================================================================================*/
      .group("CivilCaseProg_UploadEvidence_140_DisclosureListClaimDef") {
        exec(http("CivilCaseProg_UploadEvidence_140_005_DisclosureListClaimDef")
          .post(BaseURL + "/documentsv2")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundary5IivMwQhKZGomvfQ")
          .header("sec-fetch-dest", "empty")
          .header("sec-fetch-mode", "cors")
          .bodyPart(RawFileBodyPart("files", "DisclosureList.docx")
            .fileName("DisclosureList.docx")
            .transferEncoding("binary"))
          .asMultipartForm
          .formParam("classification", "PUBLIC")
          .formParam("caseTypeId", "CIVIL")
          .formParam("jurisdictionId", "CIVIL")
          .check(jsonPath("$.documents[0].hashToken").saveAs("defDisclosureHashToken"))
          .check(jsonPath("$.documents[0]._links.self.href").saveAs("defDisclosureDocument_url"))
          .check(substring("DisclosureList.docx")))
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
         *  Civil Progression - Disclosure List Upload Def2
==========================================================================================*/
      .group("CivilCaseProg_UploadEvidence_141_DisclosureListClaimDef2") {
        exec(http("CivilCaseProg_UploadEvidence_141_005_DisclosureListClaimDef2")
          .post(BaseURL + "/documentsv2")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundary5IivMwQhKZGomvfQ")
          .header("sec-fetch-dest", "empty")
          .header("sec-fetch-mode", "cors")
          .bodyPart(RawFileBodyPart("files", "DisclosureList.docx")
            .fileName("DisclosureList.docx")
            .transferEncoding("binary"))
          .asMultipartForm
          .formParam("classification", "PUBLIC")
          .formParam("caseTypeId", "CIVIL")
          .formParam("jurisdictionId", "CIVIL")
          .check(jsonPath("$.documents[0].hashToken").saveAs("defDisclosure2HashToken"))
          .check(jsonPath("$.documents[0]._links.self.href").saveAs("defDisclosure2Document_url"))
          .check(substring("DisclosureList.docx")))
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
             *  Civil Progression - Witness Statement Upload Def
  ==========================================================================================*/
      .group("CivilCaseProg_UploadEvidence_142_WitnessStatementDef") {
        exec(http("CivilCaseProg_UploadEvidence_142_005_WitnessStatementDef")
          .post(BaseURL + "/documentsv2")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundary5IivMwQhKZGomvfQ")
          .header("sec-fetch-dest", "empty")
          .header("sec-fetch-mode", "cors")
          .bodyPart(RawFileBodyPart("files", "WitnessStatement.docx")
            .fileName("WitnessStatement.docx")
            .transferEncoding("binary"))
          .asMultipartForm
          .formParam("classification", "PUBLIC")
          .formParam("caseTypeId", "CIVIL")
          .formParam("jurisdictionId", "CIVIL")
          .check(jsonPath("$.documents[0].hashToken").saveAs("witnessStatementHashToken"))
          .check(jsonPath("$.documents[0]._links.self.href").saveAs("witnessStatementDocument_url"))
          .check(substring("WitnessStatement.docx")))
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
       *  Civil Progression - Witness Statement Upload Def 2
==========================================================================================*/
      .group("CivilCaseProg_UploadEvidence_143_WitnessStatementDef") {
        exec(http("CivilCaseProg_UploadEvidence_143_005_WitnessStatementDef")
          .post(BaseURL + "/documentsv2")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundary5IivMwQhKZGomvfQ")
          .header("sec-fetch-dest", "empty")
          .header("sec-fetch-mode", "cors")
          .bodyPart(RawFileBodyPart("files", "WitnessStatement.docx")
            .fileName("WitnessStatement.docx")
            .transferEncoding("binary"))
          .asMultipartForm
          .formParam("classification", "PUBLIC")
          .formParam("caseTypeId", "CIVIL")
          .formParam("jurisdictionId", "CIVIL")
          .check(jsonPath("$.documents[0].hashToken").saveAs("witnessStatement2HashToken"))
          .check(jsonPath("$.documents[0]._links.self.href").saveAs("witnessStatement2Document_url"))
          .check(substring("WitnessStatement.docx")))
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
           *  Civil Progression - Expert's Report Upload Claim
==========================================================================================*/
      .group("CivilCaseProg_UploadEvidence_144_ExpertReportDef") {
        exec(http("CivilCaseProg_UploadEvidence_144_005_ExpertReportDef")
          .post(BaseURL + "/documentsv2")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundary5IivMwQhKZGomvfQ")
          .header("sec-fetch-dest", "empty")
          .header("sec-fetch-mode", "cors")
          .bodyPart(RawFileBodyPart("files", "ExpertsReport.docx")
            .fileName("ExpertsReport.docx")
            .transferEncoding("binary"))
          .asMultipartForm
          .formParam("classification", "PUBLIC")
          .formParam("caseTypeId", "CIVIL")
          .formParam("jurisdictionId", "CIVIL")
          .check(jsonPath("$.documents[0].hashToken").saveAs("expertsReportHashToken"))
          .check(jsonPath("$.documents[0]._links.self.href").saveAs("expertsReportDocument_url"))
          .check(substring("ExpertsReport.docx")))
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
     *  Civil Progression - Expert's Report Upload Claim 2
==========================================================================================*/
      .group("CivilCaseProg_UploadEvidence_145_ExpertReportDef2") {
        exec(http("CivilCaseProg_UploadEvidence_145_005_ExpertReportDef2")
          .post(BaseURL + "/documentsv2")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundary5IivMwQhKZGomvfQ")
          .header("sec-fetch-dest", "empty")
          .header("sec-fetch-mode", "cors")
          .bodyPart(RawFileBodyPart("files", "ExpertsReport.docx")
            .fileName("ExpertsReport.docx")
            .transferEncoding("binary"))
          .asMultipartForm
          .formParam("classification", "PUBLIC")
          .formParam("caseTypeId", "CIVIL")
          .formParam("jurisdictionId", "CIVIL")
          .check(jsonPath("$.documents[0].hashToken").saveAs("expertsReport2HashToken"))
          .check(jsonPath("$.documents[0]._links.self.href").saveAs("expertsReport2Document_url"))
          .check(substring("ExpertsReport.docx")))
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
         *  Civil Progression - Case Summary Upload Claim
==========================================================================================*/
      .group("CivilCaseProg_UploadEvidence_146_CaseSummaryDef") {
        exec(http("CivilCaseProg_UploadEvidence_146_005_CaseSummaryDef")
          .post(BaseURL + "/documentsv2")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundary5IivMwQhKZGomvfQ")
          .header("sec-fetch-dest", "empty")
          .header("sec-fetch-mode", "cors")
          .bodyPart(RawFileBodyPart("files", "casesummary.docx")
            .fileName("casesummary.docx")
            .transferEncoding("binary"))
          .asMultipartForm
          .formParam("classification", "PUBLIC")
          .formParam("caseTypeId", "CIVIL")
          .formParam("jurisdictionId", "CIVIL")
          .check(jsonPath("$.documents[0].hashToken").saveAs("caseSummaryHashToken"))
          .check(jsonPath("$.documents[0]._links.self.href").saveAs("caseSummaryDocument_url"))
          .check(substring("casesummary.docx")))
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
   *  Civil Progression - Case Summary Upload Claim 2
==========================================================================================*/
      .group("CivilCaseProg_UploadEvidence_147_CaseSummaryDef2") {
        exec(http("CivilCaseProg_UploadEvidence_147_005_CaseSummaryDef2")
          .post(BaseURL + "/documentsv2")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundary5IivMwQhKZGomvfQ")
          .header("sec-fetch-dest", "empty")
          .header("sec-fetch-mode", "cors")
          .bodyPart(RawFileBodyPart("files", "casesummary.docx")
            .fileName("casesummary.docx")
            .transferEncoding("binary"))
          .asMultipartForm
          .formParam("classification", "PUBLIC")
          .formParam("caseTypeId", "CIVIL")
          .formParam("jurisdictionId", "CIVIL")
          .check(jsonPath("$.documents[0].hashToken").saveAs("caseSummary2HashToken"))
          .check(jsonPath("$.documents[0]._links.self.href").saveAs("caseSummary2Document_url"))
          .check(substring("casesummary.docx")))
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
           *  Civil Progression - Evidence Upload Claim Def
==========================================================================================*/
      .group("CivilCaseProg_UploadEvidence_148_EvidenceUploadClaimDef") {
        exec(http("CivilCaseProg_UploadEvidence_148_005_EvidenceUploadClaimDef")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=EVIDENCE_UPLOAD_RESPONDENTDocumentUpload")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .body(ElFileBody("bodies/CaseProg/EvidenceUploadClaimDef.json"))
          .check(substring("EVIDENCE_UPLOAD_RESPONDENTDocumentUpload"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
         *  Civil Progression - Upload Documents Submit Def
==========================================================================================*/
      .group("CivilCaseProg_UploadEvidence_150_UploadDocumentsSubmitDef") {
        exec(http("CivilCaseProg_UploadEvidence_080_005UploadDocumentsSubmitDef")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .header("Experimental", "true")
          .body(ElFileBody("bodies/CaseProg/UploadDocumentsSubmitDef.json"))
          .check(substring("confirmation_body"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)

  /*======================================================================================
             Make an order - Judge User
  ==========================================================================================*/
  val MakeAnOrder =
  //feed(cpfulltestFeeder)
    exec(_.setAll(
      "CaseProgRandomString" -> Common.randomString(5),
      "EvidenceYear" -> Common.getCurrentYear(),
      "EvidenceMonth" -> Common.getCurrentMonth(),
      "EvidenceDay" -> Common.getCurrentDay())
    )
    
      /*======================================================================================
                        * Civil Progression - Search Case
      ==========================================================================================*/
      .group("CivilIT_FinalOrders_030_SearchCase") {
        exec(http("CivilIT_FinalOrders_030_005_SearchCase")
          .get(BaseURL + "/data/internal/cases/#{caseId}")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
          .check(substring("Summary")))
      }
      .pause(MinThinkTime, MaxThinkTime)
    
    
    
      /*======================================================================================
                 *  Civil Progression - 'Make An Order' event
      ==========================================================================================*/
      .group("CivilIT_FinalOrders_040_MakeAnOrder") {
        exec(http("CivilIT_FinalOrders_040_005_MakeAnOrder")
          .get("/workallocation/case/tasks/{caseId}/event/GENERATE_DIRECTIONS_ORDER/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .check(substring("task_required_for_event"))
        )
          .exec(http("CivilIT_FinalOrders_040_010_MakeAnOrder")
            .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/GENERATE_DIRECTIONS_ORDER?ignore-warning=false")
            .headers(CivilDamagesHeader.headers_notify)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
            .check(substring("GENERATE_DIRECTIONS_ORDER"))
            //  .check(jsonPath("$.case_fields[4].formatted_value.partyID").saveAs("repPartyID"))
            //  .check(jsonPath("$.case_fields[4].formatted_value.partyName").saveAs("partyName"))
            .check(jsonPath("$.event_token").saveAs("event_token"))
          )
          .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))
      
      }
      .pause(MinThinkTime, MaxThinkTime)
    
    
      /*======================================================================================
     *  Civil Progression - Final order selection
  ==========================================================================================*/
      .group("CivilIT_FinalOrders_050_OrderSelection") {
        exec(http("CivilIT_FinalOrders_050_005_OrderTrackAllocation")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=GENERATE_DIRECTIONS_ORDERTrackAllocation")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/intermediateunspec/OrderSelection.json"))
           .check(substring("ORDERTrackAllocation"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      /*======================================================================================
     *  Civil Progression - Final order selection
  ==========================================================================================*/
      .group("CivilIT_FinalOrders_060_ComplexityBand") {
        exec(http("CivilIT_FinalOrders_060_005_ComplexityBand")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=GENERATE_DIRECTIONS_ORDERIntermediateTrackComplexityBand")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/intermediateunspec/OrderComplexityBand.json"))
          .check(substring("ORDERIntermediateTrackComplexityBand"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
      /*======================================================================================
     *  Civil Progression - Order Template
  ==========================================================================================*/
      .group("CivilIT_FinalOrders_070_OrderTemplate") {
        exec(http("CivilCP_FinalOrders_130_005_OrderTemplate")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=GENERATE_DIRECTIONS_ORDERSelectTemplate")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/intermediateunspec/OrderTemplate.json"))
          .check(jsonPath("$..finalOrderDownloadTemplateDocument.documentLink.document_url").saveAs("document_url"))
          .check(jsonPath("$..finalOrderDownloadTemplateDocument.documentLink.document_filename").saveAs("document_filename"))
          .check(jsonPath("$..finalOrderDownloadTemplateDocument.documentLink.document_hash").saveAs("hash_token"))
          .check(jsonPath("$..finalOrderDownloadTemplateDocument.documentSize").saveAs("MakeAnOrderdocumentSize"))
          .check(jsonPath("$..finalOrderDownloadTemplateDocument.createdDatetime").saveAs("MakeAnOrderCreateDateTime"))
          .check(substring("ORDERSelectTemplate"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      /*======================================================================================
     *  Civil Progression - Order Template
  ==========================================================================================*/
      .group("CivilIT_FinalOrders_080_OrderTemplateDownload") {
        exec(http("CivilIT_FinalOrders_080_005_OrderTemplateDownload")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=GENERATE_DIRECTIONS_ORDERDownloadTemplate")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/intermediateunspec/OrderTemplateDownload.json"))
           .check(substring("JUDGE_FINAL_ORDER"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
  
      /*======================================================================================
* Create Civil Claim - Upload draft directions
==========================================================================================*/
  
      .group("CivilIT_FinalOrders_090_UploadDraftDirections") {
        exec(http("CivilIT_FinalOrders_090_005_UploadDraftDirections")
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
          .check(jsonPath("$.documents[0].hashToken").saveAs("unspecmultitrackmakeanorderhashtoken"))
          .check(jsonPath("$.documents[0]._links.self.href").saveAs("unspecmultitrackmakeanorderdocurl"))
          .check(substring("casesummary.docx")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  
      /*======================================================================================
* Create Civil Claim - Upload draft directions Submit
==========================================================================================*/
  
      .group("CivilIT_FinalOrders_100_UploadDraftDirectionsSubmit") {
        exec(http("CivilIT_FinalOrders_100_005_UploadDraftDirectionsSubmit")
          .post("/data/case-types/CIVIL/validate?pageId=GENERATE_DIRECTIONS_ORDERUploadOrder")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("x-xsrf-token", "#{XSRFToken}")
          .body(ElFileBody("bodies/intermediateunspec/UploadDraftDirectionMakeAnOrder.json"))
          .check(substring("GENERATE_DIRECTIONS_ORDERUploadOrder"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
    
      /*======================================================================================
     *  Civil Progression - Final Orders Submit
  ==========================================================================================*/
      .group("CivilIT_FinalOrders_110_FinalOrdersSubmit") {
        exec(http("CivilIT_FinalOrders_110_005_FinalOrdersSubmit")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .body(ElFileBody("bodies/intermediateunspec/FinalOrdersSubmitMakeAnOrder.json"))
          .check(substring("confirmation_body"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
}
