
package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.utils.{SpecilTribHeader, Common, Environment}

import java.io.{BufferedWriter, FileWriter}

object STRel3 {

  val BaseURL = Environment.baseURL
  val IdAMURL = Environment.idamURL
  val IdamUrl = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  /*======================================================================================
         HearingNotice - Centre Admin
==========================================================================================*/
  val ContactParties =

    exec(_.setAll(
      "StRandomString" -> Common.randomString(5))
    )

      /*======================================================================================
                        * Special Tribunals - Search Case
      ==========================================================================================*/
      .group("ST_ContactParties_190_SearchCase") {
          exec(http("ST_HearingNotice_190_SearchCase")
            .get(BaseURL + "/cases/case-details/#{caseId}")
            .headers(SpecilTribHeader.MoneyClaimNav))
          //  .check(jsonPath("$[0].id").saveAs("JudgeId")))
        // .check(substring("caseFile")))
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
            * Special Tribunals - 'Document Management Upload'
==========================================================================================*/
      .group("ST_ContactParties_200_DocumentUploadEvent") {
        exec(http("ST_ContactParties_200_005_DocumentUploadEvent")
          .get(BaseURL + "/workallocation/case/tasks/#{caseId}/event/caseworker-document-management/caseType/CriminalInjuriesCompensation/jurisdiction/ST_CIC")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/json")
          //     .check(jsonPath("$[0].id").saveAs("AdminId"))
        )


        exec(http("ST_ContactParties_200_010_DocumentUploadEvent")
          .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/respondent-document-management?ignore-warning=false")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
          .check(jsonPath("$.event_token").saveAs("event_token"))
          //     .check(jsonPath("$[0].id").saveAs("AdminId"))
        )
        // .check(substring("caseFile")))
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
 *  *  Special Tribunals - Documents Upload
==========================================================================================*/
      .group("ST_ContactParties_210_DocumentsUpload") {
        exec(http("ST_ContactParties_210_005_DocumentsUpload")
          .post(BaseURL + "/documentsv2")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundaryuJElnDg2cGglAAuk")
          .header("sec-fetch-dest", "empty")
          .header("sec-fetch-mode", "cors")
          .bodyPart(RawFileBodyPart("files", "3MB.pdf")
            .fileName("3MB.pdf")
            .transferEncoding("binary"))
          .asMultipartForm
          .formParam("classification", "PUBLIC")
          .formParam("caseTypeId", "#{caseId}")
          .formParam("jurisdictionId", "ST_CIC")
          .check(jsonPath("$.documents[0].hashToken").saveAs("hashToken"))
          .check(jsonPath("$.documents[0]._links.self.href").saveAs("disclosureDocument_url"))
          .check(substring("3MB.pdf")))
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
 *  *  Special Tribunals - Document Upload Submit
==========================================================================================*/
      .group("ST_ContactParties_220_DocumentsUploadSubmit") {
        exec(http("ST_ContactParties_220_005_DocumentsUploadSubmit")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=respondent-document-managementuploadCaseDocuments")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/DocumentsUploadSubmit.json"))
          .check(jsonPath("$.data.newCaseworkerCICDocument[0].id").saveAs("allCaseworkerCICDocument"))
      //    .check(substring("Check your answers"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
*  *  Special Tribunals - Document Management Upload Submit
==========================================================================================*/
      .group("ST_ContactParties_230_DocumentManagementUploadSubmit") {
        exec(http("ST_ContactParties_230_005_DocumentManagementUploadSubmit")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/DocumentManagementUploadSubmit.json"))
          .check(substring("CALLBACK_COMPLETED"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
                  * Special Tribunals - 'Case: Contact Parties'
==========================================================================================*/
      .group("ST_ContactParties_240_ContactParties") {
          exec(http("ST_ContactParties_240_005_ContactParties")
            .get(BaseURL + "/workallocation/case/tasks/#{caseId}/event/contact-parties/caseType/CriminalInjuriesCompensation/jurisdiction/ST_CIC")
            .headers(SpecilTribHeader.MoneyClaimPostHeader)
            .header("accept", "application/json, text/plain, */*")
       //     .check(jsonPath("$[0].id").saveAs("AdminId"))
          )


        exec(http("ST_ContactParties_240_010_ContactParties")
          .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/contact-parties?ignore-warning=false")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          .check(jsonPath("$.event_token").saveAs("event_token"))
          //     .check(jsonPath("$[0].id").saveAs("AdminId"))
        )
        // .check(substring("caseFile")))
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
       *  *  Special Tribunals - Documents to include
==========================================================================================*/
      .group("ST_ContactParties_250_DocumentsToInclude") {
        exec(http("ST_ContactParties_250_005_DocumentsToInclude")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=respondent-contact-partiescontactPartiesSelectDocument")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/DocumentsToInclude.json"))
          .check(substring("contactPartiesDocumentsDocumentList"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
       *  *  Special Tribunals - Which parties do you want to contact?
==========================================================================================*/
      .group("ST_ContactParties_260_WhichParties") {
        exec(http("ST_ContactParties_260_005_WhichParties")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=contact-partiespartiesToContact")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/WhichParties.json"))
          .check(substring("cicCaseNotifyPartyMessage"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
     *  *  Special Tribunals - Contact parties Submit
==========================================================================================*/
      .group("ST_ContactParties_270_ContactPartiesSubmit") {
        exec(http("ST_ContactParties_270_005_ContactPartiesSubmit")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/ContactPartiesSubmit.json"))
          .check(substring("CALLBACK_COMPLETED"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



  val DocumentUploadAmend  =

    exec(_.setAll(
      "StRandomString" -> Common.randomString(5))
    )

      /*======================================================================================
                        * Special Tribunals - Search Case
      ==========================================================================================*/
      .group("ST_DocumentUploadAmend_280_SearchCase") {
        exec(http("ST_HearingNotice_280_SearchCase")
          .get(BaseURL + "/cases/case-details/#{caseId}")
          .headers(SpecilTribHeader.MoneyClaimNav))
        //  .check(jsonPath("$[0].id").saveAs("JudgeId")))
        // .check(substring("caseFile")))
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
            * Special Tribunals - 'Document Upload Amend'
==========================================================================================*/
      .group("ST_DocumentUploadAmend_290_DocumentAmendEvent") {
        exec(http("ST_ContactParties_290_005_DocumentAmendEvent")
          .get(BaseURL + "/workallocation/case/tasks/#{caseId}/event/caseworker-amend-document/caseType/CriminalInjuriesCompensation/jurisdiction/ST_CIC")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          //     .check(jsonPath("$[0].id").saveAs("AdminId"))
        )


        exec(http("ST_ContactParties_290_010_DocumentAmendEvent")
          .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/caseworker-amend-document?ignore-warning=false")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          .check(jsonPath("$.event_token").saveAs("event_token"))
          .check(jsonPath("$.case_fields[4].formatted_value.list_items[0].label").saveAs("documentLabel"))
          .check(jsonPath("$.case_fields[4].formatted_value.list_items[0].code").saveAs("documentCode"))
          //     .check(jsonPath("$[0].id").saveAs("AdminId"))
        )
        // .check(substring("caseFile")))
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
     *  *  Special Tribunals - Which parties do you want to contact?
==========================================================================================*/
      .group("ST_DocumentUploadAmend_300_SelectDocuments") {
        exec(http("ST_DocumentUploadAmend_300_005_SelectDocuments")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=caseworker-amend-documentselectCaseDocuments")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/SelectDocuments.json"))
          .check(substring("caseworker-amend-document"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
   *  *  Special Tribunals - Amend the document details below
==========================================================================================*/
      .group("ST_DocumentUploadAmend_310_AmendDocumentDetails") {
        exec(http("ST_DocumentUploadAmend_310_005_AmendDocumentDetails")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=caseworker-amend-documentamendCaseDocuments")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/AmendDocumentDetails.json"))
          .check(substring("cicCaseSelectedDocument"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
 *  *  Special Tribunals - Amend the document details below
==========================================================================================*/
      .group("ST_DocumentUploadAmend_320_AmendDocumentSubmit") {
        exec(http("ST_DocumentUploadAmend_320_005_AmendDocumentSubmit")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/AmendDocumentSubmit.json"))
          .check(substring("CALLBACK_COMPLETED"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)

  /*======================================================================================
*  *  Special Tribunals - 'Close Case' event
==========================================================================================*/

  val CloseCase  =

    exec(_.setAll(
      "StRandomString" -> Common.randomString(5),
      "EvidenceYear" -> Common.getYear(),
      "EvidenceDay" -> Common.getDay(),
      "EvidenceMonth" -> Common.getMonth())
    )

      /*======================================================================================
                        * Special Tribunals - Search Case
      ==========================================================================================*/
      .group("ST_CloseCase_360_SearchCase") {
        exec(http("ST_HearingNotice_360_005_SearchCase")
          .get(BaseURL + "/cases/case-details/#{caseId}")
          .headers(SpecilTribHeader.MoneyClaimNav))
        //  .check(jsonPath("$[0].id").saveAs("JudgeId")))
        // .check(substring("caseFile")))
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
            * Special Tribunals - 'Close Case' Event
==========================================================================================*/
      .group("ST_CloseCase_370_CloseCaseEvent") {
        exec(http("ST_CloseCase_370_005_CloseCaseEvent")
          .get(BaseURL + "/workallocation/case/tasks/#{caseId}/event/caseworker-close-the-case/caseType/CriminalInjuriesCompensation/jurisdiction/ST_CIC")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          //     .check(jsonPath("$[0].id").saveAs("AdminId"))
        )


        exec(http("ST_CloseCase_370_010_CloseCaseEvent")
          .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/caseworker-close-the-case?ignore-warning=false")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          .check(jsonPath("$.event_token").saveAs("event_token"))
     //     .check(jsonPath("$.case_fields[3].formatted_value.list_items[0].label").saveAs("documentLabel"))
      //    .check(jsonPath("$.case_fields[3].formatted_value.list_items[0].code").saveAs("documentCode"))
          //     .check(jsonPath("$[0].id").saveAs("AdminId"))
        )
        // .check(substring("caseFile")))
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
 *  *  Special Tribunals - Are you sure you want to close this case?
==========================================================================================*/
      .group("ST_CloseCase_380_AreYouSure") {
        exec(http("ST_CloseCase_380_005_AmendDocumentDetails")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=caseworker-close-the-casecloseCaseWarning")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/AreYouSure.json"))
          .check(substring("caseworker-close-the-case"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
* *  Special Tribunals - Select Reason
==========================================================================================*/
      .group("ST_CloseCase_390_CloseReason") {
        exec(http("ST_CloseCase_390_005_CloseReason")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=caseworker-close-the-casecloseCasePageSelectReason")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/CloseReason.json"))
          .check(substring("closeCloseCaseReason"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* *  Special Tribunals - Who withdrew from the case?
==========================================================================================*/
      .group("ST_CloseCase_400_WhoWithdrewCase") {
        exec(http("ST_CloseCase_400_005_WhoWithdrewCase")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=caseworker-close-the-casecloseCaseWithdrawalDetails")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/WhoWithdrewCase.json"))
          .check(substring("closeWithdrawalFullName"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* *  Special Tribunals - Upload Document
==========================================================================================*/
      .group("ST_CloseCase_410_SupportingDocs") {
        exec(http("ST_CloseCase_410_005_SupportingDocs")
          .post(BaseURL + "/documentsv2")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundaryuJElnDg2cGglAAuk")
          .header("sec-fetch-dest", "empty")
          .header("sec-fetch-mode", "cors")
          .bodyPart(RawFileBodyPart("files", "3MB.pdf")
            .fileName("3MB.pdf")
            .transferEncoding("binary"))
          .asMultipartForm
          .formParam("classification", "PUBLIC")
          .formParam("caseTypeId", "CriminalInjuriesCompensation")
          .formParam("jurisdictionId", "ST_CIC")
        //  .check(jsonPath("$.documents.closeDocuments[0].id").saveAs("closeDocumentsId"))
          .check(jsonPath("$.documents[0].hashToken").saveAs("closeHashToken"))
          .check(jsonPath("$.documents[0]._links.self.href").saveAs("closeDisclosureDocument_url"))
          .check(substring("3MB.pdf")))
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* *  Special Tribunals - Submit Documents
==========================================================================================*/
      .group("ST_CloseCase_420_SupportingDocsSubmit") {
        exec(http("ST_CloseCase_420_005_SupportingDocsSubmit")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=caseworker-close-the-casecloseCaseUploadDocuments")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/SupportingDocsSubmit.json"))
          .check(jsonPath("$.data.closeDocuments[0].id").saveAs("closeDocumentsId"))
          .check(jsonPath("$.data.dssCaseDataRepresentativeFullName").saveAs("dssCaseDataRepresentativeFullName"))
          .check(jsonPath("$.data.dssCaseDataSubjectFullName").saveAs("dssCaseDataSubjectFullName"))
          .check(substring("closeDocuments"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* *  Special Tribunals - Select recipients
==========================================================================================*/
      .group("ST_CloseCase_430_SelectRecipients") {
        exec(http("ST_CloseCase_430_005_SelectRecipients")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=caseworker-close-the-casecloseCaseSelectRecipients")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/SelectRecipients.json"))
          .check(substring("closeCloseCaseReason"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* *  Special Tribunals - Close Case Submit
==========================================================================================*/
      .group("ST_CloseCase_440_CloseCaseSubmit") {
        exec(http("ST_CloseCase_440_005_CloseCaseSubmit")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/CloseCaseSubmit.json"))
          .check(substring("CALLBACK_COMPLETED"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



  /*======================================================================================
*  *  Special Tribunals - 'Issue a Decision' event
==========================================================================================*/

  val IssueDecision   =

    exec(_.setAll(
      "StRandomString" -> Common.randomString(5),
      "EvidenceYear" -> Common.getYear(),
      "EvidenceDay" -> Common.getDay(),
      "EvidenceMonth" -> Common.getMonth())
    )

      /*======================================================================================
                        * Special Tribunals - Search Case
      ==========================================================================================*/
      .group("ST_IssueDecision_480_SearchCase") {
        exec(http("ST_IssueDecision_480_005_SearchCase")
          .get(BaseURL + "/cases/case-details/#{caseId}")
          .headers(SpecilTribHeader.MoneyClaimNav))
        //  .check(jsonPath("$[0].id").saveAs("JudgeId")))
        // .check(substring("caseFile")))
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
            * Special Tribunals - 'Close Case' Event
==========================================================================================*/
      .group("ST_IssueDecision_490_IssueDecisionEvent") {
        exec(http("ST_IssueDecision_490_005_IssueDecisionEvent")
          .get(BaseURL + "/workallocation/case/tasks/#{caseId}/event/caseworker-issue-decision/caseType/CriminalInjuriesCompensation/jurisdiction/ST_CIC")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          //     .check(jsonPath("$[0].id").saveAs("AdminId"))
        )


        exec(http("ST_IssueDecision_490_010_IssueDecisionEvent")
          .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/caseworker-issue-decision?ignore-warning=false")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          .check(jsonPath("$.event_token").saveAs("event_token"))
          //     .check(jsonPath("$.case_fields[3].formatted_value.list_items[0].label").saveAs("documentLabel"))
          //    .check(jsonPath("$.case_fields[3].formatted_value.list_items[0].code").saveAs("documentCode"))
          //     .check(jsonPath("$[0].id").saveAs("AdminId"))
        )
        // .check(substring("caseFile")))
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* *  Special Tribunals - How would you like to create the decision notice?
==========================================================================================*/
      .group("ST_IssueDecision_500_CreateDecisionNotice") {
        exec(http("ST_IssueDecision_500_005_CreateDecisionNotice")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=caseworker-issue-decisionSelectIssueNoticeOption")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/CreateDecisionNotice.json"))
          .check(substring("caseIssueDecisionDecisionNotice"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* *  Special Tribunals - Select a template
==========================================================================================*/
      .group("ST_IssueDecision_510_SelectTemplate") {
        exec(http("ST_IssueDecision_510_005_SelectTemplate")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=caseworker-issue-decisionissueDecisionSelectTemplate")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/SelectTemplate.json"))
          .check(substring("caseIssueDecisionIssueDecisionTemplate"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* *  Special Tribunals - Edit Decision
==========================================================================================*/
      .group("ST_IssueDecision_520_EditDecision") {
        exec(http("ST_IssueDecision_520_005_EditDecision")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=caseworker-issue-decisionissueDecisionMainContent")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/EditDecision.json"))
          .check(substring("decisionMainContent"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
* *  Special Tribunals - Decision Notice Signature
==========================================================================================*/
      .group("ST_IssueDecision_530_DecisionNoticeSignature") {
        exec(http("ST_IssueDecision_530_005_DecisionNoticeSignature")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=caseworker-issue-decisionissueDecisionAddDocumentFooter")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/DecisionNoticeSignature.json"))
          .check(jsonPath("$.data.caseIssueDecisionIssueDecisionDraft.document_url").saveAs("caseIssueUrl"))
          .check(jsonPath("$.data.caseIssueDecisionIssueDecisionDraft.document_filename").saveAs("caseIssueFileName"))
       //   .check(jsonPath("$.data.applicantFlags.partyName").saveAs("partyName"))
       //   .check(jsonPath("$.data.subjectName").saveAs("subjectName"))
          .check(substring("decisionSignature"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* *  Special Tribunals - Decision Notice Signature
==========================================================================================*/
      .group("ST_IssueDecision_540_DecisionNoticePreview") {
        exec(http("ST_IssueDecision_540_005_DecisionNoticePreview")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=caseworker-issue-decisionissueDecisionPreviewTemplate")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/DecisionNoticePreview.json"))
          .check(substring("caseIssueDecisionIssueDecisionDraft"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* *  Special Tribunals - Decision information recipient
==========================================================================================*/
      .group("ST_IssueDecision_550_DecisionInformationRecipient") {
        exec(http("ST_IssueDecision_550_005_DecisionInformationRecipient")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=caseworker-issue-decisionissueDecisionSelectRecipients")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/DecisionInformationRecipient.json"))
          .check(substring("cicCaseNotifyPartySubject"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* *  Special Tribunals - Issue Decision Submit
==========================================================================================*/
      .group("ST_IssueDecision_560_IssueDecisionSubmit") {
        exec(http("ST_IssueDecision_560_005_IssueDecisionSubmit")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/IssueDecisionSubmit.json"))
          .check(substring("CaseManagement"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)




  /*======================================================================================
*  *  Special Tribunals - 'Test Change State' event
==========================================================================================*/

  val ChangeStateSubmitted   =

    exec(_.setAll(
      "StRandomString" -> Common.randomString(5),
      "EvidenceYear" -> Common.getYear(),
      "EvidenceDay" -> Common.getDay(),
      "EvidenceMonth" -> Common.getMonth())
    )


      /*======================================================================================
            * Special Tribunals - 'Change State' Event
==========================================================================================*/
      .group("ST_ChangeState_160_ChangeState") {
        exec(http("ST_ChangeState_160_005_ChangeState")
          .get(BaseURL + "/workallocation/case/tasks/#{caseId}/event/change-state/caseType/CriminalInjuriesCompensation/jurisdiction/ST_CIC")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          //     .check(jsonPath("$[0].id").saveAs("AdminId"))
        )


        exec(http("ST_ChangeState_160_005_ChangeState")
          .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/change-state?ignore-warning=false")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
          .check(jsonPath("$.event_token").saveAs("event_token"))
          //     .check(jsonPath("$.case_fields[3].formatted_value.list_items[0].label").saveAs("documentLabel"))
          //    .check(jsonPath("$.case_fields[3].formatted_value.list_items[0].code").saveAs("documentCode"))
          //     .check(jsonPath("$[0].id").saveAs("AdminId"))
        )
        // .check(substring("caseFile")))
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* *  Special Tribunals - Select Submitted
==========================================================================================*/
      .group("ST_ChangeState_170_ChangeStateSubmitted") {
        exec(http("ST_ChangeState_170_005_SelectSubmitted")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=change-statetestChangeState")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/SelectSubmitted.json"))
          .check(substring("Submitted"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* *  Special Tribunals - Select Submitted Submit
==========================================================================================*/
      .group("ST_ChangeState_180_ChangeStateSubmittedSubmit") {
        exec(http("ST_ChangeState_180_005_ChangeStateSubmittedSubmit")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/ChangeStateSubmittedSubmit.json"))
          .check(substring("Submitted"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



  /*======================================================================================
*  *  Special Tribunals - 'Test Change State' event
==========================================================================================*/

  val ChangeStateAwaiting   =

    exec(_.setAll(
      "StRandomString" -> Common.randomString(5),
      "EvidenceYear" -> Common.getYear(),
      "EvidenceDay" -> Common.getDay(),
      "EvidenceMonth" -> Common.getMonth())
    )


      /*======================================================================================
            * Special Tribunals - 'Change State' Event
==========================================================================================*/
      .group("ST_ChangeState_450_ChangeState") {
        exec(http("ST_ChangeState_450_005_ChangeState")
          .get(BaseURL + "/workallocation/case/tasks/#{caseId}/event/change-state/caseType/CriminalInjuriesCompensation/jurisdiction/ST_CIC")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          //     .check(jsonPath("$[0].id").saveAs("AdminId"))
        )


        exec(http("ST_ChangeState_450_005_ChangeState")
          .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/change-state?ignore-warning=false")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
          .check(jsonPath("$.event_token").saveAs("event_token"))
          //     .check(jsonPath("$.case_fields[3].formatted_value.list_items[0].label").saveAs("documentLabel"))
          //    .check(jsonPath("$.case_fields[3].formatted_value.list_items[0].code").saveAs("documentCode"))
          //     .check(jsonPath("$[0].id").saveAs("AdminId"))
        )
        // .check(substring("caseFile")))
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* *  Special Tribunals - Select Awaiting Outcome
==========================================================================================*/
      .group("ST_ChangeState_460_ChangeStateAwaiting") {
        exec(http("ST_ChangeState_460_ChangeStateAwaiting")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=change-statetestChangeState")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/ChangeStateAwaiting.json"))
          .check(substring("AwaitingOutcome"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* *  Special Tribunals - Select Submitted Submit
==========================================================================================*/
      .group("ST_ChangeState_470_ChangeStateAwaitingSubmit") {
        exec(http("ST_ChangeState_470_ChangeStateAwaitingSubmit")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/ChangeStateAwaitingSubmit.json"))
          .check(substring("AwaitingOutcome"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



  val ChangeStateCaseManagement  =

    exec(_.setAll(
      "StRandomString" -> Common.randomString(5),
      "EvidenceYear" -> Common.getYear(),
      "EvidenceDay" -> Common.getDay(),
      "EvidenceMonth" -> Common.getMonth())
    )


      /*======================================================================================
            * Special Tribunals - 'Change State' Event
==========================================================================================*/
      .group("ST_ChangeState_330_ChangeState") {
        exec(http("ST_ChangeState_330_005_ChangeState")
          .get(BaseURL + "/workallocation/case/tasks/#{caseId}/event/change-state/caseType/CriminalInjuriesCompensation/jurisdiction/ST_CIC")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
          //     .check(jsonPath("$[0].id").saveAs("AdminId"))
        )


        exec(http("ST_ChangeState_330_005_ChangeState")
          .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/change-state?ignore-warning=false")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          .check(jsonPath("$.event_token").saveAs("event_token"))
          //     .check(jsonPath("$.case_fields[3].formatted_value.list_items[0].label").saveAs("documentLabel"))
          //    .check(jsonPath("$.case_fields[3].formatted_value.list_items[0].code").saveAs("documentCode"))
          //     .check(jsonPath("$[0].id").saveAs("AdminId"))
        )
        // .check(substring("caseFile")))
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* *  Special Tribunals - Select CaseManagement Outcome
==========================================================================================*/
      .group("ST_ChangeState_340_ChangeStateCaseManagement") {
        exec(http("ST_ChangeState_340_005_ChangeStateCaseManagement")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=change-statetestChangeState")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/ChangeStateCaseManagement.json"))
          .check(substring("CaseManagement"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* *  Special Tribunals -  Case Management Submit
==========================================================================================*/
      .group("ST_ChangeState_350_ChangeStateCaseManagementSubmit") {
        exec(http("ST_ChangeState_350_005_ChangeStateCaseManagementSubmit")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(SpecilTribHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/ChangeStateCaseManagementSubmit.json"))
          .check(substring("CaseManagement"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)

}
