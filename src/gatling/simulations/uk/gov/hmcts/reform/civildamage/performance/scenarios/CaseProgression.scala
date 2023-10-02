
package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, Common, Environment,Headers,CsrfCheck}

import java.io.{BufferedWriter, FileWriter}

object CaseProgression {

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

    exec(_.setAll(
      "CaseProgRandomString" -> Common.randomString(5),
      "Plus2WeeksDay" -> Common.getPlus6WeeksDay(),
      "Plus2WeeksMonth" -> Common.getPlus6WeeksMonth(),
      "Plus2WeeksYear" -> Common.getPlus6WeeksYear(),
      "EvidenceYear" -> Common.getYear(),
      "EvidenceDay" -> Common.getDay(),
      "EvidenceMonth" -> Common.getMonth())
    )

      /*======================================================================================
                        * Civil Progression - Search Case
      ==========================================================================================*/
      .group("CivilCaseProg_HearingNotice_030_SearchCase") {
          exec(http("CivilCaseProg_HearingNotice_030_SearchCase")
            .get(BaseURL + "/cases/case-details/#{caseId}")
            .headers(CivilDamagesHeader.MoneyClaimNav))
          //  .check(jsonPath("$[0].id").saveAs("JudgeId")))
        // .check(substring("caseFile")))
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
                  * Civil Progression - Task List
==========================================================================================*/
      .group("CivilCaseProg_HearingNotice_035_TaskList") {
          exec(http("CivilCaseProg_HearingNotice_035_005_TaskList")
            .post(BaseURL + "/workallocation/case/task/#{caseId}")
            .headers(CivilDamagesHeader.MoneyClaimPostHeader)
            .header("accept", "application/json, text/plain, */*")
            .body(ElFileBody("bodies/CaseProg/HearingTaskList.json"))
            .check(jsonPath("$[0].id").saveAs("AdminId")))
        // .check(substring("caseFile")))
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
                      * Civil Progression - Assign to Centre Admin
    ==========================================================================================*/
      .group("CivilCaseProg_HearingNotice_040_AssignCentreAdmin") {
          exec(http("CivilCaseProg_HearingNotice_040_005_AssignCentreAdmin")
            .get(BaseURL + "/workallocation/task/#{AdminId}/claim")
            .headers(CivilDamagesHeader.MoneyClaimPostHeader)
            .header("accept", "application/json, text/plain, */*")
            .body(ElFileBody("bodies/CaseProg/AssignToMe.json")))
        // .check(substring("caseFile")))
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
               *  Civil Progression - '	Directions - Schedule A Hearing' event
    ==========================================================================================*/
      .group("CivilCaseProg_HearingNotice_050_ScheduleHearing") {
        exec(http("CivilCaseProg_HearingNotice_050_005_ScheduleHearing")
          .get("/cases/case-details/#{caseId}/trigger/HEARING_SCHEDULED/HEARING_SCHEDULEDHearingNoticeSelect?tid=#{AdminId}")
          .headers(CivilDamagesHeader.MoneyClaimNav)
         // .check(substring("task_required_for_event"))
        )


          .exec(Common.configurationui)

          .exec(Common.configUI)

          .exec(Common.configJson)

          .exec(Common.TsAndCs)

          .exec(Common.isAuthenticated)

          .exec(http("CivilCaseProg_HearingNotice_050_010_ScheduleHearing")
            .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/HEARING_SCHEDULED?ignore-warning=false")
            .headers(CivilDamagesHeader.headers_notify)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
            .check(substring("HEARING_SCHEDULED"))
            //  .check(jsonPath("$.case_fields[4].formatted_value.partyID").saveAs("repPartyID"))
            //  .check(jsonPath("$.case_fields[4].formatted_value.partyName").saveAs("partyName"))
            .check(jsonPath("$.event_token").saveAs("event_token"))
          )
          .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))

      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
             *  *  Civil Progression - '	What hearing notice do you want to create?
  ==========================================================================================*/
      .group("CivilCaseProg_HearingNotice_060_WhatHearingNotice") {
        exec(http("CivilCaseProg_HearingNotice_060_005_WhatHearingNotice")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=HEARING_SCHEDULEDHearingNoticeSelect")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/CaseProg/WhatHearingNotice.json"))
          .check(substring("HEARING_SCHEDULEDHearingNoticeSelect"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
           *  *  Civil Progression - '	Is this a listing or a relisting?
==========================================================================================*/
      .group("CivilCaseProg_HearingNotice_070_ListOrRelisting") {
        exec(http("CivilCaseProg_HearingNotice_070_005_ListOrRelisting")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=HEARING_SCHEDULEDListingOrRelisting")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/CaseProg/ListOrRelisting.json"))
          .check(substring("listingOrRelisting"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
         *  *  Civil Progression - '	Hearing Scheduled Details
==========================================================================================*/
      .group("CivilCaseProg_HearingNotice_080_HearingDetails") {
        exec(http("CivilCaseProg_HearingNotice_080_005_HearingDetails")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=HEARING_SCHEDULEDHearingDetails")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/CaseProg/HearingDetails.json"))
          .check(substring("validate?pageId=HEARING_SCHEDULEDHearingDetails"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
       *  *  Civil Progression - '	Hearing Schedule Extra Details
==========================================================================================*/
      .group("CivilCaseProg_HearingNotice_090_HearingExtraDetails") {
        exec(http("CivilCaseProg_HearingNotice_090_005_HearingExtraDetails")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=HEARING_SCHEDULEDHearingInformation")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/CaseProg/HearingExtraDetails.json"))
          .check(substring("information"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
         *  Civil Progression - '	Hearing Schedule Submit
==========================================================================================*/
      .group("CivilCaseProg_HearingNotice_100_HearingScheduleSubmit") {
        exec(http("CivilCaseProg_HearingNotice_100_005_HearingScheduleSubmit")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .header("Experimental", "true")
          .body(ElFileBody("bodies/CaseProg/HearingScheduleSubmit.json"))
          .check(substring("confirmation_header"))
        )
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
      .group("CivilCaseProg_UploadEvidence_200_SearchCase") {
          exec(http("CivilCaseProg_UploadEvidence_200_SearchCase")
            .get(BaseURL + "/cases/case-details/#{caseId}")
            .headers(CivilDamagesHeader.MoneyClaimNav))
        // .check(substring("caseFile")))
      }
      .pause(MinThinkTime, MaxThinkTime)

      /*======================================================================================
                 *  Civil Progression - 'Upload Your Documents' event
      ==========================================================================================*/
      .group("CivilCaseProg_UploadEvidence_210_UploadYourDocs") {
        exec(http("CivilCaseProg_UploadEvidence_210_005_UploadYourDocs")
          .get("/workallocation/case/tasks/#{caseId}/event/EVIDENCE_UPLOAD_APPLICANT/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .check(substring("task_required_for_event"))
        )

          .exec(http("CivilCaseProg_UploadEvidence_210_010_UploadYourDocs")
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
      .group("CivilCaseProg_UploadEvidence_220_UploadYourDocsContinue") {
        exec(http("CivilCaseProg_UploadEvidence_220_005_UploadYourDocsContinue")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=EVIDENCE_UPLOAD_APPLICANTEvidenceUpload")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/CaseProg/UploadYourDocsContinue.json"))
          .check(substring("caseProgAllocatedTrack"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
             *  Civil Progression - Select the type of document
  ==========================================================================================*/
      .group("CivilCaseProg_UploadEvidence_230_TypeOfDocument") {
        exec(http("CivilCaseProg_UploadEvidence_230_005_TypeOfDocument")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=EVIDENCE_UPLOAD_APPLICANTDocumentSelectionFastTrack")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/CaseProg/TypeOfDocument.json"))
          .check(substring("EVIDENCE_UPLOAD_APPLICANTDocumentSelectionFastTrack"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
               *  Civil Progression - Disclosure List Upload Claim 1
    ==========================================================================================*/
      .group("CivilCaseProg_UploadEvidence_240_DisclosureListClaim1") {
        exec(http("CivilCaseProg_UploadEvidence_240_005_DisclosureListClaim1")
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
          .check(jsonPath("$.documents[0].hashToken").saveAs("claimDisclosureHashToken"))
          .check(jsonPath("$.documents[0]._links.self.href").saveAs("claimDisclosureDocument_url"))
          .check(substring("DisclosureList.docx")))
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
         *  Civil Progression - Disclosure List Upload Claim 2
==========================================================================================*/
      .group("CivilCaseProg_UploadEvidence_241_DisclosureListClaim2") {
        exec(http("CivilCaseProg_UploadEvidence_241_005_DisclosureListClaim2")
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
          .check(jsonPath("$.documents[0].hashToken").saveAs("claimDisclosure2HashToken"))
          .check(jsonPath("$.documents[0]._links.self.href").saveAs("claimDisclosure2Document_url"))
          .check(substring("DisclosureList.docx")))
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
             *  Civil Progression - Witness Statement Upload Claim 1
  ==========================================================================================*/
      .group("CivilCaseProg_UploadEvidence_242_WitnessStatementClaim1") {
        exec(http("CivilCaseProg_UploadEvidence_242_005_WitnessStatementClaim")
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
       *  Civil Progression - Witness Statement Upload Claim 2
==========================================================================================*/
      .group("CivilCaseProg_UploadEvidence_243_WitnessStatementClaim2") {
        exec(http("CivilCaseProg_UploadEvidence_243_005_WitnessStatementClaim2")
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
      .group("CivilCaseProg_UploadEvidence_244_ExpertReportClaim1") {
        exec(http("CivilCaseProg_UploadEvidence_244_005_ExpertReportClaim1")
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
      .group("CivilCaseProg_UploadEvidence_245_ExpertReportClaim2") {
        exec(http("CivilCaseProg_UploadEvidence_245_005_ExpertReportClaim2")
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
         *  Civil Progression - Case Summary Upload Claim 1
==========================================================================================*/
      .group("CivilCaseProg_UploadEvidence_246_CaseSummaryClaim1") {
        exec(http("CivilCaseProg_UploadEvidence_246_005_CaseSummaryClaim1")
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
      .group("CivilCaseProg_UploadEvidence_247_CaseSummaryClaim2") {
        exec(http("CivilCaseProg_UploadEvidence_247_005_CaseSummaryClaim2")
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
           *  Civil Progression - Select the type of document
==========================================================================================*/
      .group("CivilCaseProg_UploadEvidence_250_EvidenceUploadClaim") {
        exec(http("CivilCaseProg_UploadEvidence_250_005_EvidenceUploadClaim")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=EVIDENCE_UPLOAD_APPLICANTDocumentUpload")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .body(ElFileBody("bodies/CaseProg/EvidenceUploadClaim.json"))
          .check(substring("EVIDENCE_UPLOAD_APPLICANTDocumentUpload"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
         *  Civil Progression - Upload Documents Submit Claim
==========================================================================================*/
      .group("CivilCaseProg_UploadEvidence_260_UploadDocumentsSubmitClaim") {
        exec(http("CivilCaseProg_UploadEvidence_080_005UploadDocumentsSubmitClaim")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .header("Experimental", "true")
          .body(ElFileBody("bodies/CaseProg/UploadDocumentsSubmitClaim.json"))
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
             Case Notes - Judge User
  ==========================================================================================*/


  val JudgeCaseNotes =


    exec(_.setAll(
      "CaseProgRandomString" -> Common.randomString(5))
    )

      /*======================================================================================
                        * Civil Progression - Search Case
      ==========================================================================================*/
      .group("CivilCaseProg_CaseNotes_030_SearchCase") {
        exec(http("CivilCaseProg_CaseNotes_030_SearchCase")
          .get(BaseURL + "/cases/case-details/#{caseId}")
          .headers(CivilDamagesHeader.MoneyClaimNav))
         // .check(substring("caseFile")))
      }
      .pause(MinThinkTime, MaxThinkTime)

      /*======================================================================================
                   *  Civil Progression - 'Add a Case Note' event
        ==========================================================================================*/
      .group("CivilCaseProg_CaseNotes_370_AddACaseNote") {
        exec(http("CivilCaseProg_CaseNotes_040_005_AddACaseNote")
          .get("/workallocation/case/tasks/#{caseId}/event/EVIDENCE_UPLOAD_JUDGE/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .check(substring("task_required_for_event"))
        )

          .exec(http("CivilCaseProg_CaseNotes_380_010_AddACaseNote")
            .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/EVIDENCE_UPLOAD_JUDGE?ignore-warning=false")
            .headers(CivilDamagesHeader.headers_notify)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
            .check(substring("EVIDENCE_UPLOAD_JUDGE"))
            //  .check(jsonPath("$.case_fields[4].formatted_value.partyID").saveAs("repPartyID"))
            //  .check(jsonPath("$.case_fields[4].formatted_value.partyName").saveAs("partyName"))
            .check(jsonPath("$.event_token").saveAs("event_token"))
          )
          .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))

      }
      .pause(MinThinkTime, MaxThinkTime)




      /*======================================================================================
                   *  Civil Progression - Document with a Note
        ==========================================================================================*/
      .group("CivilCaseProg_CaseNotes_390_DocWithANote") {
        exec(http("CivilCaseProg_CaseNotes_390_005_DocWithANote")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=EVIDENCE_UPLOAD_JUDGECaseNoteSelection")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/CaseProg/DocWithANote.json"))
          .check(substring("caseNoteType"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
                 *  Civil Progression - Upload Document
      ==========================================================================================*/
      .group("CivilCaseProg_CaseNotes_400_UploadCaseNoteDoc") {
        exec(http("CivilCaseProg_CaseNotes_400_005_UploadCaseNoteDoc")
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
          .formParam("caseTypeId", "#{caseId}")
          .formParam("jurisdictionId", "CIVIL")
          .check(jsonPath("$.documents[0].hashToken").saveAs("caseNoteHashToken"))
          .check(jsonPath("$.documents[0]._links.self.href").saveAs("caseNotedefenceDocument_url"))
          .check(substring("casesummary.docx")))
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
                 *  Civil Progression - Upload Case Note
      ==========================================================================================*/
      .group("CivilCaseProg_CaseNotes_415_DocWithANoteUpload") {
        exec(http("CivilCaseProg_CaseNotes_415_005_DocWithANoteUpload")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=EVIDENCE_UPLOAD_JUDGEUploadDocumentAndNote")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .body(ElFileBody("bodies/CaseProg/DocWithANoteUpload.json"))
          .check(substring("documentAndNote"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
               *  Civil Progression - Upload Case Note Submit
    ==========================================================================================*/
      .group("CivilCaseProg_CaseNotes_420_DocWithNoteSubmit") {
        exec(http("CivilCaseProg_CaseNotes_420_005_DocWithNoteSubmit")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .header("Experimental", "true")
          .body(ElFileBody("bodies/CaseProg/DocWithNoteSubmit.json"))
          .check(substring("confirmation_header"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
                      * Civil Progression - Return to Case Details
    ==========================================================================================*/
      .group("CivilCaseProg_CaseNotes_080_ReturnToCase") {
          exec(Common.userDetails)
      }
      .pause(MinThinkTime, MaxThinkTime)

  /*======================================================================================
             Case File View - Claimant
  ==========================================================================================*/


  val CaseFileView =


    exec(_.setAll(
      "CaseProgRandomString" -> Common.randomString(5))
    )

      /*======================================================================================
                        * Civil Progression - Search Case
      ==========================================================================================*/
      .group("CivilCaseProg_CaseFileView_030_SearchCase") {
          exec(http("CivilCaseProg_CaseNotes_030_SearchCase")
            .get(BaseURL + "/cases/case-details/#{caseId}")
            .headers(CivilDamagesHeader.MoneyClaimNav))
        // .check(substring("caseFile")))
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
                 *  Civil Progression - 'Case File' tab
      ==========================================================================================*/
      .group("CivilCaseProg_CaseFileView_270_CaseFileTab") {
        exec(http("CivilCaseProg_CaseFileView_270_005_CaseFileTab")
          .get(BaseURL + "/categoriesAndDocuments/#{caseId}")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .check(substring("applicant1DefenceResponseDocument"))
          .check(regex("""documents/(\w{8}-\w{4}-\w{4}-\w{4}-\w{12})/binary","attribute_path":"applicant1DefenceResponseDocument.file""").saveAs("applicant1DefenceResponseDocument"))
          .check(regex("""documents/(\w{8}-\w{4}-\w{4}-\w{4}-\w{12})","document_filename":"fast_track_sdo_""").saveAs("disclosureListDocument"))
          .check(regex("""documents/(\w{8}-\w{4}-\w{4}-\w{4}-\w{12})/binary","attribute_path":"documentExpertReport""").saveAs("documentExpertReport"))
          .check(regex("""documents/(\w{8}-\w{4}-\w{4}-\w{4}-\w{12})","document_filename":"casesummary.docx""").saveAs("caseSummaryDocument"))
          .check(regex("""documents/(\w{8}-\w{4}-\w{4}-\w{4}-\w{12})","document_filename":"Witness Statement of""").saveAs("witnessStatementDocument"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
               *  Civil Progression - applicant1DefenceResponseDocument File
    ==========================================================================================*/
      .group("CivilCaseProg_CaseFileView_280_DefResponseDocument") {
        exec(http("CivilCaseProg_CaseFileView_280_005_DefResponseDocument")
          .get(BaseURL + "/em-anno/annotation-sets/filter?documentId=#{applicant1DefenceResponseDocument}")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/json, text/plain, */*")
          .check(status.in(204))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
             *  Civil Progression - disclosureListDocument File
  ==========================================================================================*/
      .group("CivilCaseProg_CaseFileView_290_disclosureListDocument") {
        exec(http("CivilCaseProg_CaseFileView_290_005_disclosureListDocument")
          .get(BaseURL + "/em-anno/annotation-sets/filter?documentId=#{disclosureListDocument}")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/json, text/plain, */*")
          .check(status.in(204))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
           *  Civil Progression - documentExpertReport File
==========================================================================================*/
      .group("CivilCaseProg_CaseFileView_290_documentExpertReport") {
        exec(http("CivilCaseProg_CaseFileView_290_005_documentExpertReport")
          .get(BaseURL + "/em-anno/annotation-sets/filter?documentId=#{documentExpertReport}")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/json, text/plain, */*")
          .check(status.in(204))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
         *  Civil Progression -caseSummaryDocument File
==========================================================================================*/
      .group("CivilCaseProg_CaseFileView_300_caseSummaryDocument") {
        exec(http("CivilCaseProg_CaseFileView_300_005_caseSummaryDocument")
          .get(BaseURL + "/em-anno/annotation-sets/filter?documentId=#{caseSummaryDocument}")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/json, text/plain, */*")
          .check(status.in(204))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
       *  Civil Progression -witnessStatementDocument File
==========================================================================================*/
      .group("CivilCaseProg_CaseFileView_300_witnessStatementDocument") {
        exec(http("CivilCaseProg_CaseFileView_300_005_witnessStatementDocument")
          .get(BaseURL + "/em-anno/annotation-sets/filter?documentId=#{witnessStatementDocument}")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/json, text/plain, */*")
          .check(status.in(204))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



  /*======================================================================================
           Trial Readiness - Claimant
==========================================================================================*/


  val TrialReadiness =


    exec(_.setAll(
      "CaseProgRandomString" -> Common.randomString(5))
    )

      /*======================================================================================
                        * Civil Progression - Search Case
      ==========================================================================================*/
      .group("CivilCaseProg_TrialReadiness_030_SearchCase") {
          exec(http("CivilCaseProg_TrialReadiness_030_005_SearchCase")
            .get(BaseURL + "/cases/case-details/#{caseId}")
            .headers(CivilDamagesHeader.MoneyClaimNav))
        // .check(substring("caseFile")))
      }
      .pause(MinThinkTime, MaxThinkTime)

      /*======================================================================================
                   *  Civil Progression - 'Confirm Trial Arrangements'
        ==========================================================================================*/
      .group("CivilCaseProg_TrialReadiness_310_ConfirmTrialArrange") {
        exec(http("CivilCaseProg_TrialReadiness_310_005_ConfirmTrialArrange")
          .get("/workallocation/case/tasks/{caseId}/event/TRIAL_READINESS/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .check(substring("task_required_for_event"))
        )

          .exec(http("CivilCaseProg_TrialReadiness_320_010_ConfirmTrialArrange")
            .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/TRIAL_READINESS?ignore-warning=false")
            .headers(CivilDamagesHeader.headers_notify)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
            .check(substring("TRIAL_READINESS"))
            //  .check(jsonPath("$.case_fields[4].formatted_value.partyID").saveAs("repPartyID"))
            //  .check(jsonPath("$.case_fields[4].formatted_value.partyName").saveAs("partyName"))
            .check(jsonPath("$.event_token").saveAs("event_token"))
          )
          .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
             *  Civil Progression - Confirm this case is ready for trial or hearing
  ==========================================================================================*/
      .group("CivilCaseProg_TrialReadiness_330_ConfirmTrialOrHearing") {
        exec(http("CivilCaseProg_TrialReadiness_330_005_ConfirmTrialOrHearing")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=TRIAL_READINESSConfirmReadyClaimant")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/CaseProg/ConfirmTrialOrHearing.json"))
          .check(substring("trialReadyApplicant"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
       *  Civil Progression - Trial Readiness Submit
==========================================================================================*/
      .group("CivilCaseProg_TrialReadiness_340_TrialReadinessSubmit") {
        exec(http("CivilCaseProg_TrialReadiness_340_005_TrialReadinessSubmit")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .body(ElFileBody("bodies/CaseProg/TrialReadinessSubmit.json"))
          .check(substring("confirmation_body"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)


  /*======================================================================================
        Bundle Creation Integration - Claimant
==========================================================================================*/


  val BundleCreationIntegration =


    exec(_.setAll(
      "CaseProgRandomString" -> Common.randomString(5),
      "env" -> "perftest")
    )

      /*======================================================================================
                        * Civil Progression - Search Case
      ==========================================================================================*/
      .group("CivilCaseProg_BundleCreation_030_SearchCase") {
          exec(http("CivilCaseProg_BundleCreation_030_005_SearchCase")
            .get(BaseURL + "/cases/case-details/#{caseId}")
            .headers(CivilDamagesHeader.MoneyClaimNav))
        // .check(substring("caseFile")))
      }
      .pause(MinThinkTime, MaxThinkTime)

      /*======================================================================================
                   *  Civil Progression - Run Bundle Creation API
        ==========================================================================================*/
      .group("CivilCaseProg_BundleCreation_350_BundleAPI") {
        exec(http("CivilCaseProg_BundleCreation_040_005_BundleAPI")
          .get("http://civil-service-#{env}.service.core-compute-#{env}.internal/testing-support/#{caseId}/trigger-trial-bundle")
          .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJraWQiOiI4cDJpajg2S0pTeENKeGcveUovV2w3TjcxMXM9IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJjaXZpbC5kYW1hZ2VzLmNsYWltcytvcmdhbmlzYXRpb24uMi5zb2xpY2l0b3IuMUBnbWFpbC5jb20iLCJjdHMiOiJPQVVUSDJfU1RBVEVMRVNTX0dSQU5UIiwiYXV0aF9sZXZlbCI6MCwiYXVkaXRUcmFja2luZ0lkIjoiODRkODg2YTktYTA4Ni00ZDgwLWI1YmYtNDIxMGYzZmZmZDkyLTE2NDEzNDUzIiwic3VibmFtZSI6ImNpdmlsLmRhbWFnZXMuY2xhaW1zK29yZ2FuaXNhdGlvbi4yLnNvbGljaXRvci4xQGdtYWlsLmNvbSIsImlzcyI6Imh0dHBzOi8vZm9yZ2Vyb2NrLWFtLnNlcnZpY2UuY29yZS1jb21wdXRlLWlkYW0tcGVyZnRlc3QuaW50ZXJuYWw6ODQ0My9vcGVuYW0vb2F1dGgyL3JlYWxtcy9yb290L3JlYWxtcy9obWN0cyIsInRva2VuTmFtZSI6ImFjY2Vzc190b2tlbiIsInRva2VuX3R5cGUiOiJCZWFyZXIiLCJhdXRoR3JhbnRJZCI6IktWbmFmLVdLYTQ0OE9qaXIxcG5jamR1clJjRSIsImF1ZCI6ImhtY3RzIiwibmJmIjoxNjk2MjU3MDkzLCJncmFudF90eXBlIjoicGFzc3dvcmQiLCJzY29wZSI6WyJvcGVuaWQiLCJwcm9maWxlIiwicm9sZXMiXSwiYXV0aF90aW1lIjoxNjk2MjU3MDkzLCJyZWFsbSI6Ii9obWN0cyIsImV4cCI6MTY5NjI4NTg5MywiaWF0IjoxNjk2MjU3MDkzLCJleHBpcmVzX2luIjoyODgwMCwianRpIjoiRWhkeEgzV09BTC1pRU4wNmNNVHdJLXNKakhFIn0.MNUz65zXFegseEO3IUHKxkeVd6uKe1Dgp7BLC9yPTNmU4AcLVzus3o5r5InCaZXTc2fA92iMG6AvTw_KPavaXt0kfqx32Dq4RT8co1HrJpfPgNipU4u0jJD-2H4x1JkIigqgT8nIIRY1Dv5y-iCH_aygmb9pzJrVCKozu7kgXQ6wNn09L3Vgr79T1Zv8KYJ33FI5rKOP58aYmFVT8z-g7RfH2dZboiLMMwuL0g2_jSlV_5f5jnbt5LtBy0boN9vbonvkTRSzE_n6Sswffa40RxajubOuHJQwsMsxPphUIM7CyhzAQPTjWEawxmsnAsc0w2erTRcOZ5Z0oeVX6dIKkQ")
          .header("Content-Type", "application/json")
          .header("Accept", "*/*")
          .check(status.in(200, 201))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)


          /*======================================================================================
                 *  Civil Progression - Open Bundle
      ==========================================================================================*/
          .group("CivilCaseProg_BundleCreation_360_OpenBundle") {
            exec(http("CivilCaseProg_BundleCreation_050_005_OpenBundle")
              .get(BaseURL + "/data/internal/cases/#{caseId}")
              .headers(CivilDamagesHeader.MoneyClaimNav)
              .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
              .check(substring("CREATE_BUNDLE"))
            )

          }
          .pause(MinThinkTime, MaxThinkTime)


  /*======================================================================================
       Final / General Orders - Judge
==========================================================================================*/


  val  FinalGeneralOrders =

    exec(_.setAll(
      "CaseProgRandomString" -> Common.randomString(5),
      "EvidenceYear" -> Common.getCurrentYear(),
      "EvidenceMonth" -> Common.getCurrentMonth(),
      "EvidenceDay" -> Common.getCurrentDay())
    )

      /*======================================================================================
                        * Civil Progression - Search Case
      ==========================================================================================*/
      .group("CivilCaseProg_FinalOrders_030_SearchCase") {
          exec(http("CivilCaseProg_FinalOrders_030_005_SearchCase")
            .get(BaseURL + "/cases/case-details/#{caseId}")
            .headers(CivilDamagesHeader.MoneyClaimNav))
        // .check(substring("caseFile")))
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
                 *  Civil Progression - 'Make An Order' event
      ==========================================================================================*/
      .group("CivilCaseProg_FinalOrders_430_MakeAnOrder") {
        exec(http("CivilCaseProg_FinalOrders_430_005_MakeAnOrder")
          .get("/workallocation/case/tasks/{caseId}/event/GENERATE_DIRECTIONS_ORDER/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .check(substring("task_required_for_event"))
        )

          .exec(http("CivilCaseProg_FinalOrders_440_010_MakeAnOrder")
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
           *  Civil Progression - Recitals and order
==========================================================================================*/
      .group("CivilCaseProg_FinalOrders_450_RecitalsAndOrder") {
        exec(http("CivilCaseProg_FinalOrders_450_005_RecitalsAndOrder")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=GENERATE_DIRECTIONS_ORDERFreeFormOrder")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/CaseProg/RecitalsAndOrder.json"))
          .check(substring("GENERATE_DIRECTIONS_ORDERFreeFormOrder"))
          .check(jsonPath("$.data.finalOrderDocument.document_url").saveAs("finalOrderDocument_url"))
          .check(jsonPath("$.data.finalOrderDocument.document_hash").saveAs("finalOrderDocument_hash"))
          .check(jsonPath("$.data.finalOrderDocument.document_filename").saveAs("finalOrderDocument_filename"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
         *  Civil Progression -Order PDF
==========================================================================================*/
      .group("CivilCaseProg_FinalOrders_460_OrderPDF") {
        exec(http("CivilCaseProg_FinalOrders_460_005_OrderPDF")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=GENERATE_DIRECTIONS_ORDERFinalOrderPreview")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/CaseProg/OrderPDF.json"))
          .check(substring("finalOrderDocument"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
     *  Civil Progression - Final Orders Submit
==========================================================================================*/
      .group("CivilCaseProg_FinalOrders_470_FinalOrdersSubmit") {
        exec(http("CivilCaseProg_FinalOrders_470_005_FinalOrdersSubmit")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .body(ElFileBody("bodies/CaseProg/FinalOrdersSubmit.json"))
          .check(substring("confirmation_body"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)


}
