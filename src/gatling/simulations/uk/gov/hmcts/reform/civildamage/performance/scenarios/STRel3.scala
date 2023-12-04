
package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, Common, Environment}

import java.io.{BufferedWriter, FileWriter}

object STRel3 {

  val BaseURL = Environment.baseURL
  val IdAMURL = Environment.idamURL
  val IdamUrl = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val caseFeeder=csv("caseIds.csv").circular

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
      .group("ST_ContactParties_030_SearchCase") {
          exec(http("ST_HearingNotice_030_SearchCase")
            .get(BaseURL + "/cases/case-details/#{caseId}")
            .headers(CivilDamagesHeader.MoneyClaimNav))
          //  .check(jsonPath("$[0].id").saveAs("JudgeId")))
        // .check(substring("caseFile")))
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
            * Special Tribunals - 'Document Management Upload'
==========================================================================================*/
      .group("ST_ContactParties_040_DocumentUploadEvent") {
        exec(http("ST_ContactParties_040_005_DocumentUploadEvent")
          .get(BaseURL + "/workallocation/case/tasks/#{caseId}/event/caseworker-document-management/caseType/CriminalInjuriesCompensation/jurisdiction/ST_CIC")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/json")
          //     .check(jsonPath("$[0].id").saveAs("AdminId"))
        )


        exec(http("ST_ContactParties_040_010_DocumentUploadEvent")
          .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/caseworker-document-management?ignore-warning=false")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
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
      .group("ST_ContactParties_050_DocumentsUpload") {
        exec(http("ST_ContactParties_050_005_DocumentsUpload")
          .post(BaseURL + "/documentsv2")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
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
      .group("ST_ContactParties_060_DocumentsUploadSubmit") {
        exec(http("ST_ContactParties_060_005_DocumentsUploadSubmit")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=caseworker-document-managementuploadCaseDocuments")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
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
      .group("ST_ContactParties_070_DocumentManagementUploadSubmit") {
        exec(http("ST_ContactParties_070_005_DocumentManagementUploadSubmit")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/DocumentManagementUploadSubmit.json"))
          .check(substring("CALLBACK_COMPLETED"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
                  * Special Tribunals - 'Case: Contact Parties'
==========================================================================================*/
      .group("ST_ContactParties_080_ContactParties") {
          exec(http("ST_ContactParties_080_005_ContactParties")
            .get(BaseURL + "/workallocation/case/tasks/#{caseId}/event/contact-parties/caseType/CriminalInjuriesCompensation/jurisdiction/ST_CIC")
            .headers(CivilDamagesHeader.MoneyClaimPostHeader)
            .header("accept", "application/json, text/plain, */*")
       //     .check(jsonPath("$[0].id").saveAs("AdminId"))
          )


        exec(http("ST_ContactParties_080_010_ContactParties")
          .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/contact-parties?ignore-warning=false")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
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
      .group("ST_ContactParties_090_DocumentsToInclude") {
        exec(http("ST_ContactParties_090_005_DocumentsToInclude")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=respondent-contact-partiescontactPartiesSelectDocument")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/DocumentsToInclude.json"))
          .check(substring("contactPartiesDocumentsDocumentList"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
       *  *  Special Tribunals - Which parties do you want to contact?
==========================================================================================*/
      .group("ST_ContactParties_100_WhichParties") {
        exec(http("ST_ContactParties_100_005_WhichParties")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=contact-partiespartiesToContact")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/WhichParties.json"))
          .check(substring("cicCaseNotifyPartyMessage"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
     *  *  Special Tribunals - Contact parties Submit
==========================================================================================*/
      .group("ST_ContactParties_110_ContactPartiesSubmit") {
        exec(http("ST_ContactParties_110_005_ContactPartiesSubmit")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
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
      .group("ST_DocumentUploadAmend_120_SearchCase") {
        exec(http("ST_HearingNotice_120_SearchCase")
          .get(BaseURL + "/cases/case-details/#{caseId}")
          .headers(CivilDamagesHeader.MoneyClaimNav))
        //  .check(jsonPath("$[0].id").saveAs("JudgeId")))
        // .check(substring("caseFile")))
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
            * Special Tribunals - 'Document Upload Amend'
==========================================================================================*/
      .group("ST_DocumentUploadAmend_130_DocumentAmendEvent") {
        exec(http("ST_ContactParties_130_005_DocumentAmendEvent")
          .get(BaseURL + "/workallocation/case/tasks/#{caseId}/event/caseworker-amend-document/caseType/CriminalInjuriesCompensation/jurisdiction/ST_CIC")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          //     .check(jsonPath("$[0].id").saveAs("AdminId"))
        )


        exec(http("ST_ContactParties_130_010_DocumentAmendEvent")
          .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/caseworker-amend-document?ignore-warning=false")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
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
      .group("ST_DocumentUploadAmend_140_SelectDocuments") {
        exec(http("ST_DocumentUploadAmend_140_005_SelectDocuments")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=caseworker-amend-documentselectCaseDocuments")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/SelectDocuments.json"))
          .check(substring("caseworker-amend-document"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
   *  *  Special Tribunals - Amend the document details below
==========================================================================================*/
      .group("ST_DocumentUploadAmend_150_AmendDocumentDetails") {
        exec(http("ST_DocumentUploadAmend_150_005_AmendDocumentDetails")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=caseworker-amend-documentamendCaseDocuments")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/AmendDocumentDetails.json"))
          .check(substring("cicCaseSelectedDocument"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
 *  *  Special Tribunals - Amend the document details below
==========================================================================================*/
      .group("ST_DocumentUploadAmend_160_AmendDocumentSubmit") {
        exec(http("ST_DocumentUploadAmend_160_005_AmendDocumentSubmit")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
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
      .group("ST_CloseCase_170_SearchCase") {
        exec(http("ST_HearingNotice_170_005_SearchCase")
          .get(BaseURL + "/cases/case-details/#{caseId}")
          .headers(CivilDamagesHeader.MoneyClaimNav))
        //  .check(jsonPath("$[0].id").saveAs("JudgeId")))
        // .check(substring("caseFile")))
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
            * Special Tribunals - 'Close Case' Event
==========================================================================================*/
      .group("ST_CloseCase_180_CloseCaseEvent") {
        exec(http("ST_CloseCase_180_005_CloseCaseEvent")
          .get(BaseURL + "/workallocation/case/tasks/#{caseId}/event/caseworker-close-the-case/caseType/CriminalInjuriesCompensation/jurisdiction/ST_CIC")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          //     .check(jsonPath("$[0].id").saveAs("AdminId"))
        )


        exec(http("ST_CloseCase_180_010_CloseCaseEvent")
          .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/caseworker-close-the-case?ignore-warning=false")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
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
      .group("ST_CloseCase_190_AreYouSure") {
        exec(http("ST_CloseCase_190_005_AmendDocumentDetails")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=caseworker-close-the-casecloseCaseWarning")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/AreYouSure.json"))
          .check(substring("caseworker-close-the-case"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
* *  Special Tribunals - Select Reason
==========================================================================================*/
      .group("ST_CloseCase_200_CloseReason") {
        exec(http("ST_CloseCase_200_005_CloseReason")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=caseworker-close-the-casecloseCasePageSelectReason")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/CloseReason.json"))
          .check(substring("closeCloseCaseReason"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* *  Special Tribunals - Who withdrew from the case?
==========================================================================================*/
      .group("ST_CloseCase_210_WhoWithdrewCase") {
        exec(http("ST_CloseCase_210_005_WhoWithdrewCase")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=caseworker-close-the-casecloseCaseWithdrawalDetails")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/WhoWithdrewCase.json"))
          .check(substring("closeWithdrawalFullName"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* *  Special Tribunals - Upload Document
==========================================================================================*/
      .group("ST_CloseCase_220_SupportingDocs") {
        exec(http("ST_CloseCase_220_005_SupportingDocs")
          .post(BaseURL + "/documentsv2")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
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
      .group("ST_CloseCase_230_SupportingDocsSubmit") {
        exec(http("ST_CloseCase_230_005_SupportingDocsSubmit")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=caseworker-close-the-casecloseCaseUploadDocuments")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
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
      .group("ST_CloseCase_240_SelectRecipients") {
        exec(http("ST_CloseCase_240_005_SelectRecipients")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=caseworker-close-the-casecloseCaseSelectRecipients")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/SelectRecipients.json"))
          .check(substring("closeCloseCaseReason"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* *  Special Tribunals - Close Case Submit
==========================================================================================*/
      .group("ST_CloseCase_250_CloseCaseSubmit") {
        exec(http("ST_CloseCase_250_005_CloseCaseSubmit")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
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
      .group("ST_IssueDecision_260_SearchCase") {
        exec(http("ST_IssueDecision_260_005_SearchCase")
          .get(BaseURL + "/cases/case-details/#{caseId}")
          .headers(CivilDamagesHeader.MoneyClaimNav))
        //  .check(jsonPath("$[0].id").saveAs("JudgeId")))
        // .check(substring("caseFile")))
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
            * Special Tribunals - 'Close Case' Event
==========================================================================================*/
      .group("ST_IssueDecision_290_IssueDecisionEvent") {
        exec(http("ST_IssueDecision_290_005_IssueDecisionEvent")
          .get(BaseURL + "/workallocation/case/tasks/#{caseId}/event/caseworker-issue-decision/caseType/CriminalInjuriesCompensation/jurisdiction/ST_CIC")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          //     .check(jsonPath("$[0].id").saveAs("AdminId"))
        )


        exec(http("ST_IssueDecision_290_010_IssueDecisionEvent")
          .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/caseworker-issue-decision?ignore-warning=false")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
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
      .group("ST_IssueDecision_300_CreateDecisionNotice") {
        exec(http("ST_IssueDecision_300_005_CreateDecisionNotice")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=caseworker-issue-decisionSelectIssueNoticeOption")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/CreateDecisionNotice.json"))
          .check(substring("caseIssueDecisionDecisionNotice"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* *  Special Tribunals - Select a template
==========================================================================================*/
      .group("ST_IssueDecision_310_SelectTemplate") {
        exec(http("ST_IssueDecision_310_005_SelectTemplate")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=caseworker-issue-decisionissueDecisionSelectTemplate")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/SelectTemplate.json"))
          .check(substring("caseIssueDecisionIssueDecisionTemplate"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* *  Special Tribunals - Edit Decision
==========================================================================================*/
      .group("ST_IssueDecision_320_EditDecision") {
        exec(http("ST_IssueDecision_320_005_EditDecision")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=caseworker-issue-decisionissueDecisionMainContent")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/EditDecision.json"))
          .check(substring("decisionMainContent"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
* *  Special Tribunals - Decision Notice Signature
==========================================================================================*/
      .group("ST_IssueDecision_330_DecisionNoticeSignature") {
        exec(http("ST_IssueDecision_330_005_DecisionNoticeSignature")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=caseworker-issue-decisionissueDecisionAddDocumentFooter")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
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
      .group("ST_IssueDecision_340_DecisionNoticePreview") {
        exec(http("ST_IssueDecision_340_005_DecisionNoticePreview")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=caseworker-issue-decisionissueDecisionPreviewTemplate")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/DecisionNoticePreview.json"))
          .check(substring("caseIssueDecisionIssueDecisionDraft"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* *  Special Tribunals - Decision information recipient
==========================================================================================*/
      .group("ST_IssueDecision_350_DecisionInformationRecipient") {
        exec(http("ST_IssueDecision_350_005_DecisionInformationRecipient")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=caseworker-issue-decisionissueDecisionSelectRecipients")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/DecisionInformationRecipient.json"))
          .check(substring("cicCaseNotifyPartySubject"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* *  Special Tribunals - Issue Decision Submit
==========================================================================================*/
      .group("ST_IssueDecision_360_IssueDecisionSubmit") {
        exec(http("ST_IssueDecision_360_005_IssueDecisionSubmit")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
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
      .group("ST_ChangeState_290_ChangeState") {
        exec(http("ST_ChangeState_290_005_ChangeState")
          .get(BaseURL + "/workallocation/case/tasks/#{caseId}/event/change-state/caseType/CriminalInjuriesCompensation/jurisdiction/ST_CIC")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          //     .check(jsonPath("$[0].id").saveAs("AdminId"))
        )


        exec(http("ST_ChangeState_290_005_ChangeState")
          .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/change-state?ignore-warning=false")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
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
      .group("ST_ChangeState_290_ChangeStateSubmitted") {
        exec(http("ST_ChangeState_290_005_SelectSubmitted")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=change-statetestChangeState")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/SelectSubmitted.json"))
          .check(substring("Submitted"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* *  Special Tribunals - Select Submitted Submit
==========================================================================================*/
      .group("ST_ChangeState_290_ChangeStateSubmittedSubmit") {
        exec(http("ST_ChangeState_290_005_ChangeStateSubmittedSubmit")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
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
      .group("ST_ChangeState_290_ChangeState") {
        exec(http("ST_ChangeState_290_005_ChangeState")
          .get(BaseURL + "/workallocation/case/tasks/#{caseId}/event/change-state/caseType/CriminalInjuriesCompensation/jurisdiction/ST_CIC")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/json, text/plain, */*")
          //     .check(jsonPath("$[0].id").saveAs("AdminId"))
        )


        exec(http("ST_ChangeState_290_005_ChangeState")
          .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/change-state?ignore-warning=false")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
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
      .group("ST_ChangeState_290_ChangeStateAwaiting") {
        exec(http("ST_ChangeState_290_ChangeStateAwaiting")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=change-statetestChangeState")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/ChangeStateAwaiting.json"))
          .check(substring("AwaitingOutcome"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* *  Special Tribunals - Select Submitted Submit
==========================================================================================*/
      .group("ST_ChangeState_290_ChangeStateAwaitingSubmit") {
        exec(http("ST_ChangeState_290_ChangeStateAwaitingSubmit")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
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
      .group("ST_ChangeState_290_ChangeState") {
        exec(http("ST_ChangeState_290_005_ChangeState")
          .get(BaseURL + "/workallocation/case/tasks/#{caseId}/event/change-state/caseType/CriminalInjuriesCompensation/jurisdiction/ST_CIC")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
          //     .check(jsonPath("$[0].id").saveAs("AdminId"))
        )


        exec(http("ST_ChangeState_290_005_ChangeState")
          .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/change-state?ignore-warning=false")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
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
      .group("ST_ChangeState_290_ChangeStateCaseManagement") {
        exec(http("ST_ChangeState_290_005_ChangeStateCaseManagement")
          .post(BaseURL + "/data/case-types/CriminalInjuriesCompensation/validate?pageId=change-statetestChangeState")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/ChangeStateCaseManagement.json"))
          .check(substring("CaseManagement"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
* *  Special Tribunals -  Case Management Submit
==========================================================================================*/
      .group("ST_ChangeState_290_ChangeStateCaseManagementSubmit") {
        exec(http("ST_ChangeState_290_005_ChangeStateCaseManagementSubmit")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/ST/ChangeStateCaseManagementSubmit.json"))
          .check(substring("CaseManagement"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)

}
