
package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, Environment}

object ClaimResponseToDefendant {
  
  val BaseURL = Environment.baseURL
  val IdAMURL = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime
  
  
  //def run(implicit postHeaders: Map[String, String]): ChainBuilder = {
    //claim response event to trigger
    //claim response event to trigger
    val run =
    group("CD_ClaimantResponse_30_RespondentResponseEvent") {
      exec(http("CD_ClaimantResponse_30_005_Event")
        .get("/data/internal/cases/#{caseId}/event-triggers/CLAIMANT_RESPONSE?ignore-warning=false")
        .headers(CivilDamagesHeader.headers_769)
        .check(status.in(200, 304))
        .check(jsonPath("$.event_token").optional.saveAs("event_token_claimantresponse"))
      )
        .exec(http("CD_ClaimantResponse_30_010_Profile")
          .get("/data/internal/profile")
          .headers(CivilDamagesHeader.headers_149)
          .check(status.in(200, 304))
        )
    }
      .pause(MinThinkTime, MaxThinkTime)
    
    //claimant response to defendant
    
    //val claimresponsetype =
      .group("CD_ClaimantResponse_40_RespondentResponse") {
        exec(http("CD_ClaimantResponse_40_Response")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSERespondentResponse")
          .headers(CivilDamagesHeader.headers_1604)
          .body(StringBody("{\"data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{Document_ID}\",\"document_filename\":\"1MB-c.pdf\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{Document_ID}/binary\"}},\"applicant1ProceedWithClaim\":\"Yes\",\"allocatedTrack\":\"MULTI_CLAIM\",\"claimType\":\"CLINICAL_NEGLIGENCE\"},\"event\":{\"id\":\"CLAIMANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{Document_ID}\",\"document_filename\":\"1MB-c.pdf\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{Document_ID}/binary\"}},\"applicant1ProceedWithClaim\":\"Yes\",\"allocatedTrack\":\"MULTI_CLAIM\",\"claimType\":\"CLINICAL_NEGLIGENCE\"},\"event_token\":\"#{event_token_claimantresponse}\",\"ignore_warning\":false,\"case_reference\":\"#{caseId}\"}"))
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
    
    
    //val claimresponsedocument =
      .group("CD_ClaimantResponse_50_ResponseDocument") {
        exec(http("CD_ClaimantResponse_50_Document")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEApplicantDefenceResponseDocument")
          .headers(CivilDamagesHeader.headers_1625)
          .body(StringBody("{\"data\":{\"applicant1DefenceResponseDocument\":{}},\"event\":{\"id\":\"CLAIMANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{Document_ID}\",\"document_filename\":\"1MB-c.pdf\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{Document_ID}/binary\"}},\"applicant1ProceedWithClaim\":\"Yes\",\"allocatedTrack\":\"MULTI_CLAIM\",\"claimType\":\"CLINICAL_NEGLIGENCE\",\"applicant1DefenceResponseDocument\":{}},\"event_token\":\"#{event_token_claimantresponse}\",\"ignore_warning\":false,\"case_reference\":\"#{caseId}\"}"))
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
    
    //val claimantresponsefiledirectoryquestionaire =
      .group("CD_ClaimantResponse_60_DirectionsQuestionnaire") {
        exec(http("CD_ClaimantResponse_60_Questionaire")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEFileDirectionsQuestionnaire")
          .headers(CivilDamagesHeader.headers_1658)
          .body(StringBody("{\"data\":{\"applicant1DQFileDirectionsQuestionnaire\":{\"oneMonthStayRequested\":\"No\",\"reactionProtocolCompliedWith\":\"Yes\",\"explainedToClient\":[\"CONFIRM\"]}},\"event\":{\"id\":\"CLAIMANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{Document_ID}\",\"document_filename\":\"1MB-c.pdf\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#®®®®®®®®®{Document_ID}/binary\"}},\"applicant1ProceedWithClaim\":\"Yes\",\"allocatedTrack\":\"MULTI_CLAIM\",\"claimType\":\"CLINICAL_NEGLIGENCE\",\"applicant1DefenceResponseDocument\":{},\"applicant1DQFileDirectionsQuestionnaire\":{\"oneMonthStayRequested\":\"No\",\"reactionProtocolCompliedWith\":\"Yes\",\"explainedToClient\":[\"CONFIRM\"]}},\"event_token\":\"#{event_token_claimantresponse}\",\"ignore_warning\":false,\"case_reference\":\"#{caseId}\"}"))
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
    
    //val claimdisclosureofelectronicsdoc =
      .group("CD_ClaimantResponse_70_ElectronicDocuments") {
        exec(http("CD_ClaimantResponse_70_document")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEDisclosureOfElectronicDocuments")
          .headers(CivilDamagesHeader.headers_1679)
          .body(StringBody("{\"data\":{\"applicant1DQDisclosureOfElectronicDocuments\":{\"reachedAgreement\":\"Yes\"}},\"event\":{\"id\":\"CLAIMANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{Document_ID}\",\"document_filename\":\"1MB-c.pdf\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{Document_ID}/binary\"}},\"applicant1ProceedWithClaim\":\"Yes\",\"allocatedTrack\":\"MULTI_CLAIM\",\"claimType\":\"CLINICAL_NEGLIGENCE\",\"applicant1DefenceResponseDocument\":{},\"applicant1DQFileDirectionsQuestionnaire\":{\"oneMonthStayRequested\":\"No\",\"reactionProtocolCompliedWith\":\"Yes\",\"explainedToClient\":[\"CONFIRM\"]},\"applicant1DQDisclosureOfElectronicDocuments\":{\"reachedAgreement\":\"Yes\"}},\"event_token\":\"#{event_token_claimantresponse}\",\"ignore_warning\":false,\"case_reference\":\"#{caseId}\"}"))
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
    //val claiantdisclosureofnonelectronicsdoc =
      .group("CD_ClaimantResponse_80_NonElectronicDocuments") {
        exec(http("CD_ClaimantResponse_80_documents")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEDisclosureOfNonElectronicDocuments")
          .headers(CivilDamagesHeader.headers_1698)
          .body(StringBody("{\"data\":{\"applicant1DQDisclosureOfNonElectronicDocuments\":{\"directionsForDisclosureProposed\":\"No\",\"standardDirectionsRequired\":null,\"bespokeDirections\":null}},\"event\":{\"id\":\"CLAIMANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{Document_ID}\",\"document_filename\":\"1MB-c.pdf\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{Document_ID}/binary\"}},\"applicant1ProceedWithClaim\":\"Yes\",\"allocatedTrack\":\"MULTI_CLAIM\",\"claimType\":\"CLINICAL_NEGLIGENCE\",\"applicant1DefenceResponseDocument\":{},\"applicant1DQFileDirectionsQuestionnaire\":{\"oneMonthStayRequested\":\"No\",\"reactionProtocolCompliedWith\":\"Yes\",\"explainedToClient\":[\"CONFIRM\"]},\"applicant1DQDisclosureOfElectronicDocuments\":{\"reachedAgreement\":\"Yes\"},\"applicant1DQDisclosureOfNonElectronicDocuments\":{\"directionsForDisclosureProposed\":\"No\",\"standardDirectionsRequired\":null,\"bespokeDirections\":null}},\"event_token\":\"#{event_token_claimantresponse}\",\"ignore_warning\":false,\"case_reference\":\"#{caseId}\"}"))
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
    
    //val claimantresponsedisclosurereport =
      .group("CD_ClaimantResponse_90_DisclosureReport") {
        exec(http("CD_ClaimantResponse_90_disclosure")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEDisclosureReport")
          .headers(CivilDamagesHeader.headers_1727)
          .body(StringBody("{\"data\":{\"applicant1DQDisclosureReport\":{\"disclosureFormFiledAndServed\":\"Yes\",\"disclosureProposalAgreed\":\"No\",\"draftOrderNumber\":null}},\"event\":{\"id\":\"CLAIMANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{Document_ID}\",\"document_filename\":\"1MB-c.pdf\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{Document_ID}/binary\"}},\"applicant1ProceedWithClaim\":\"Yes\",\"allocatedTrack\":\"MULTI_CLAIM\",\"claimType\":\"CLINICAL_NEGLIGENCE\",\"applicant1DefenceResponseDocument\":{},\"applicant1DQFileDirectionsQuestionnaire\":{\"oneMonthStayRequested\":\"No\",\"reactionProtocolCompliedWith\":\"Yes\",\"explainedToClient\":[\"CONFIRM\"]},\"applicant1DQDisclosureOfElectronicDocuments\":{\"reachedAgreement\":\"Yes\"},\"applicant1DQDisclosureOfNonElectronicDocuments\":{\"directionsForDisclosureProposed\":\"No\",\"standardDirectionsRequired\":null,\"bespokeDirections\":null},\"applicant1DQDisclosureReport\":{\"disclosureFormFiledAndServed\":\"Yes\",\"disclosureProposalAgreed\":\"No\",\"draftOrderNumber\":null}},\"event_token\":\"#{event_token_claimantresponse}\",\"ignore_warning\":false,\"case_reference\":\"#{caseId}\"}"))
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
    
    //val clamantdefresponseexpert =
      .group("CD_ClaimantResponse_100_RESPONSEExperts") {
        exec(http("CD_ClaimantResponse_100_Experts")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEExperts")
          .headers(CivilDamagesHeader.headers_1808)
          .body(StringBody("{\"data\":{\"applicant1DQExperts\":{\"expertRequired\":\"Yes\",\"expertReportsSent\":\"YES\",\"jointExpertSuitable\":\"No\",\"details\":[{\"value\":{\"name\":\"expertclaim\",\"fieldOfExpertise\":\"expertclaim\",\"whyRequired\":\"sdssdsds\",\"estimatedCost\":\"2500100\"},\"id\":null}]}},\"event\":{\"id\":\"CLAIMANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{Document_ID}\",\"document_filename\":\"1MB-c.pdf\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{Document_ID}/binary\"}},\"applicant1ProceedWithClaim\":\"Yes\",\"allocatedTrack\":\"MULTI_CLAIM\",\"claimType\":\"CLINICAL_NEGLIGENCE\",\"applicant1DefenceResponseDocument\":{},\"applicant1DQFileDirectionsQuestionnaire\":{\"oneMonthStayRequested\":\"No\",\"reactionProtocolCompliedWith\":\"Yes\",\"explainedToClient\":[\"CONFIRM\"]},\"applicant1DQDisclosureOfElectronicDocuments\":{\"reachedAgreement\":\"Yes\"},\"applicant1DQDisclosureOfNonElectronicDocuments\":{\"directionsForDisclosureProposed\":\"No\",\"standardDirectionsRequired\":null,\"bespokeDirections\":null},\"applicant1DQDisclosureReport\":{\"disclosureFormFiledAndServed\":\"Yes\",\"disclosureProposalAgreed\":\"No\",\"draftOrderNumber\":null},\"applicant1DQExperts\":{\"expertRequired\":\"Yes\",\"expertReportsSent\":\"YES\",\"jointExpertSuitable\":\"No\",\"details\":[{\"value\":{\"name\":\"expertclaim\",\"fieldOfExpertise\":\"expertclaim\",\"whyRequired\":\"sdssdsds\",\"estimatedCost\":\"2500100\"},\"id\":null}]}},\"event_token\":\"#{event_token_claimantresponse}\",\"ignore_warning\":false,\"case_reference\":\"#{caseId}\"}"))
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
    
    //val claimantdefresponsewitness =
      .group("CD_ClaimantResponse_110_RESPONSEWitnesses") {
        exec(http("CD_ClaimantResponse_110_Witness")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEWitnesses")
          .headers(CivilDamagesHeader.headers_1825)
          .body(StringBody("{\"data\":{\"applicant1DQWitnesses\":{\"witnessesToAppear\":\"No\"}},\"event\":{\"id\":\"CLAIMANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{Document_ID}\",\"document_filename\":\"1MB-c.pdf\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{Document_ID}/binary\"}},\"applicant1ProceedWithClaim\":\"Yes\",\"allocatedTrack\":\"MULTI_CLAIM\",\"claimType\":\"CLINICAL_NEGLIGENCE\",\"applicant1DefenceResponseDocument\":{},\"applicant1DQFileDirectionsQuestionnaire\":{\"oneMonthStayRequested\":\"No\",\"reactionProtocolCompliedWith\":\"Yes\",\"explainedToClient\":[\"CONFIRM\"]},\"applicant1DQDisclosureOfElectronicDocuments\":{\"reachedAgreement\":\"Yes\"},\"applicant1DQDisclosureOfNonElectronicDocuments\":{\"directionsForDisclosureProposed\":\"No\",\"standardDirectionsRequired\":null,\"bespokeDirections\":null},\"applicant1DQDisclosureReport\":{\"disclosureFormFiledAndServed\":\"Yes\",\"disclosureProposalAgreed\":\"No\",\"draftOrderNumber\":null},\"applicant1DQExperts\":{\"expertRequired\":\"Yes\",\"expertReportsSent\":\"YES\",\"jointExpertSuitable\":\"No\",\"details\":[{\"value\":{\"name\":\"expertclaim\",\"fieldOfExpertise\":\"expertclaim\",\"whyRequired\":\"sdssdsds\",\"estimatedCost\":\"2500100\"},\"id\":null}]},\"applicant1DQWitnesses\":{\"witnessesToAppear\":\"No\"}},\"event_token\":\"#{event_token_claimantresponse}\",\"ignore_warning\":false,\"case_reference\":\"#{caseId}\"}"))
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
    
    //val claimantdefresponselanguage =
      .group("CD_ClaimantResponse_120_RESPONSELanguage") {
        exec(http("CD_ClaimantResponse_120_Language")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSELanguage")
          .headers(CivilDamagesHeader.headers_1848)
          .body(StringBody("{\"data\":{\"applicant1DQLanguage\":{\"evidence\":\"ENGLISH\",\"court\":\"ENGLISH\",\"documents\":\"ENGLISH\"}},\"event\":{\"id\":\"CLAIMANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{Document_ID}\",\"document_filename\":\"1MB-c.pdf\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{Document_ID}/binary\"}},\"applicant1ProceedWithClaim\":\"Yes\",\"allocatedTrack\":\"MULTI_CLAIM\",\"claimType\":\"CLINICAL_NEGLIGENCE\",\"applicant1DefenceResponseDocument\":{},\"applicant1DQFileDirectionsQuestionnaire\":{\"oneMonthStayRequested\":\"No\",\"reactionProtocolCompliedWith\":\"Yes\",\"explainedToClient\":[\"CONFIRM\"]},\"applicant1DQDisclosureOfElectronicDocuments\":{\"reachedAgreement\":\"Yes\"},\"applicant1DQDisclosureOfNonElectronicDocuments\":{\"directionsForDisclosureProposed\":\"No\",\"standardDirectionsRequired\":null,\"bespokeDirections\":null},\"applicant1DQDisclosureReport\":{\"disclosureFormFiledAndServed\":\"Yes\",\"disclosureProposalAgreed\":\"No\",\"draftOrderNumber\":null},\"applicant1DQExperts\":{\"expertRequired\":\"Yes\",\"expertReportsSent\":\"YES\",\"jointExpertSuitable\":\"No\",\"details\":[{\"value\":{\"name\":\"expertclaim\",\"fieldOfExpertise\":\"expertclaim\",\"whyRequired\":\"sdssdsds\",\"estimatedCost\":\"2500100\"},\"id\":null}]},\"applicant1DQWitnesses\":{\"witnessesToAppear\":\"No\"},\"applicant1DQLanguage\":{\"evidence\":\"ENGLISH\",\"court\":\"ENGLISH\",\"documents\":\"ENGLISH\"}},\"event_token\":\"#{event_token_claimantresponse}\",\"ignore_warning\":false,\"case_reference\":\"#{caseId}\"}"))
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
    
    //val claimantdefresponsehearing =
      .group("CD_ClaimantResponse_130_RESPONSEHearing") {
        exec(http("CD_ClaimantResponse_130_Hearing")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEHearing")
          .headers(CivilDamagesHeader.headers_1869)
          .body(StringBody("{\"data\":{\"applicant1DQHearing\":{\"hearingLength\":\"ONE_DAY\",\"unavailableDatesRequired\":\"No\"}},\"event\":{\"id\":\"CLAIMANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{Document_ID}\",\"document_filename\":\"1MB-c.pdf\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{Document_ID}/binary\"}},\"applicant1ProceedWithClaim\":\"Yes\",\"allocatedTrack\":\"MULTI_CLAIM\",\"claimType\":\"CLINICAL_NEGLIGENCE\",\"applicant1DefenceResponseDocument\":{},\"applicant1DQFileDirectionsQuestionnaire\":{\"oneMonthStayRequested\":\"No\",\"reactionProtocolCompliedWith\":\"Yes\",\"explainedToClient\":[\"CONFIRM\"]},\"applicant1DQDisclosureOfElectronicDocuments\":{\"reachedAgreement\":\"Yes\"},\"applicant1DQDisclosureOfNonElectronicDocuments\":{\"directionsForDisclosureProposed\":\"No\",\"standardDirectionsRequired\":null,\"bespokeDirections\":null},\"applicant1DQDisclosureReport\":{\"disclosureFormFiledAndServed\":\"Yes\",\"disclosureProposalAgreed\":\"No\",\"draftOrderNumber\":null},\"applicant1DQExperts\":{\"expertRequired\":\"Yes\",\"expertReportsSent\":\"YES\",\"jointExpertSuitable\":\"No\",\"details\":[{\"val\":{\"name\":\"expertclaim\",\"fieldOfExpertise\":\"expertclaim\",\"whyRequired\":\"sdssdsds\",\"estimatedCost\":\"2500100\"},\"id\":null}]},\"applicant1DQWitnesses\":{\"witnessesToAppear\":\"No\"},\"applicant1DQLanguage\":{\"evidence\":\"ENGLISH\",\"court\":\"ENGLISH\",\"documents\":\"ENGLISH\"},\"applicant1DQHearing\":{\"hearingLength\":\"ONE_DAY\",\"unavailableDatesRequired\":\"No\"}},\"event_token\":\"#{event_token_claimantresponse}\",\"ignore_warning\":false,\"case_reference\":\"#{caseId}\"}"))
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
    
    //val claimantresponsedocuments =
      .group("CD_ClaimantResponse_140_documents") {
        exec(http("CD_ClaimantResponse_140_documents")
          .post("/documents")
          .headers(CivilDamagesHeader.headers_1084)
          .bodyPart(RawFileBodyPart("files", "2MB.pdf")
            .fileName("2MB.pdf")
            .transferEncoding("binary")
          )
          .asMultipartForm
          .formParam("classification", "PUBLIC")
          .check(regex("""internal/documents/(.+?)/binary""").saveAs("DocumentIDForClaimResponse"))
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
    
    //val claimantdefresponsedraftdirections =
      .group("CD_ClaimantResponse_150_RESPONSEDraftDirections") {
        exec(http("CD_ClaimantResponse_150_Directions")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEDraftDirections")
          .headers(CivilDamagesHeader.headers_1885)
          .body(StringBody("{\"data\":{\"applicant1DQDraftDirections\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{DocumentIDForClaimResponse}\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{DocumentIDForClaimResponse}/binary\",\"document_filename\":\"2MB.pdf\"}},\"event\":{\"id\":\"CLAIMANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{Document_ID}\",\"document_filename\":\"1MB-c.pdf\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{Document_ID}/binary\"}},\"applicant1ProceedWithClaim\":\"Yes\",\"allocatedTrack\":\"MULTI_CLAIM\",\"claimType\":\"CLINICAL_NEGLIGENCE\",\"applicant1DefenceResponseDocument\":{},\"applicant1DQFileDirectionsQuestionnaire\":{\"oneMonthStayRequested\":\"No\",\"reactionProtocolCompliedWith\":\"Yes\",\"explainedToClient\":[\"CONFIRM\"]},\"applicant1DQDisclosureOfElectronicDocuments\":{\"reachedAgreement\":\"Yes\"},\"applicant1DQDisclosureOfNonElectronicDocuments\":{\"directionsForDisclosureProposed\":\"Yes\",\"standardDirectionsRequired\":\"Yes\",\"bespokeDirections\":null},\"applicant1DQDisclosureReport\":{\"disclosureFormFiledAndServed\":\"Yes\",\"disclosureProposalAgreed\":\"No\",\"draftOrderNumber\":null},\"applicant1DQExperts\":{\"expertRequired\":\"No\",\"details\":[]},\"applicant1DQWitnesses\":{\"witnessesToAppear\":\"No\",\"details\":[]},\"applicant1DQLanguage\":{\"evidence\":\"ENGLISH\",\"court\":\"ENGLISH\",\"documents\":\"ENGLISH\"},\"applicant1DQHearing\":{\"hearingLength\":\"ONE_DAY\",\"unavailableDatesRequired\":\"No\",\"unavailableDates\":[]},\"applicant1DQDraftDirections\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{DocumentIDForClaimResponse}\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{DocumentIDForClaimResponse}/binary\",\"document_filename\":\"2MB.pdf\"}},\"event_token\":\"#{event_token_claimantresponse}\",\"ignore_warning\":false,\"case_reference\":\"#{caseId}\"}"))
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
    
    /*//val defresponserequestedcourt=
    exec(http("request_1199")
      .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSERequestedCourt")
      .headers(CivilDamagesHeader.headers_1454)
      .body(StringBody("RecordedSimulationcd_1454_request.json"))
      .check(status.in(200, 304)))*/
    
    //val claimantdefresponsehearingsupport =
      .group("CD_ClaimantResponse_160_RESPONSEHearingSupport") {
        exec(http("CD_ClaimantResponse_160_Support")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEHearingSupport")
          .headers(CivilDamagesHeader.headers_1899)
          .body(StringBody("{\"data\":{},\"event\":{\"id\":\"CLAIMANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{Document_ID}\",\"document_filename\":\"1MB-c.pdf\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{Document_ID}/binary\"}},\"applicant1ProceedWithClaim\":\"Yes\",\"allocatedTrack\":\"MULTI_CLAIM\",\"claimType\":\"CLINICAL_NEGLIGENCE\",\"applicant1DefenceResponseDocument\":{},\"applicant1DQFileDirectionsQuestionnaire\":{\"oneMonthStayRequested\":\"No\",\"reactionProtocolCompliedWith\":\"Yes\",\"explainedToClient\":[\"CONFIRM\"]},\"applicant1DQDisclosureOfElectronicDocuments\":{\"reachedAgreement\":\"Yes\"},\"applicant1DQDisclosureOfNonElectronicDocuments\":{\"directionsForDisclosureProposed\":\"Yes\",\"standardDirectionsRequired\":\"Yes\",\"bespokeDirections\":null},\"applicant1DQDisclosureReport\":{\"disclosureFormFiledAndServed\":\"Yes\",\"disclosureProposalAgreed\":\"No\",\"draftOrderNumber\":null},\"applicant1DQExperts\":{\"expertRequired\":\"No\",\"details\":[]},\"applicant1DQWitnesses\":{\"witnessesToAppear\":\"No\",\"details\":[]},\"applicant1DQLanguage\":{\"evidence\":\"ENGLISH\",\"court\":\"ENGLISH\",\"documents\":\"ENGLISH\"},\"applicant1DQHearing\":{\"hearingLength\":\"ONE_DAY\",\"unavailableDatesRequired\":\"No\",\"unavailableDates\":[]},\"applicant1DQDraftDirections\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{DocumentIDForClaimResponse}\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{DocumentIDForClaimResponse}/binary\",\"document_filename\":\"2MB.pdf\"},\"applicant1DQHearingSupport\":{\"signLanguageRequired\":null,\"languageToBeInterpreted\":null,\"otherSupport\":null,\"requirements\":[]}},\"event_token\":\"#{event_token_claimantresponse}\",\"ignore_warning\":false,\"case_reference\":\"#{caseId}\"}"))
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
    
    //val claimantresponsefurtherinformation =
      .group("CD_ClaimantResponse_170_FurtherInformation") {
        exec(http("CD_ClaimantResponse_170_Info")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEFurtherInformation")
          .headers(CivilDamagesHeader.headers_1923)
          .body(StringBody("{\"data\":{\"applicant1DQFurtherInformation\":{\"futureApplications\":\"No\",\"otherInformationForJudge\":\"daasasasasaaaaaaaaaaaaaaaa\"}},\"event\":{\"id\":\"CLAIMANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{Document_ID}\",\"document_filename\":\"1MB-c.pdf\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{Document_ID}/binary\"}},\"applicant1ProceedWithClaim\":\"Yes\",\"allocatedTrack\":\"MULTI_CLAIM\",\"claimType\":\"CLINICAL_NEGLIGENCE\",\"applicant1DefenceResponseDocument\":{},\"applicant1DQFileDirectionsQuestionnaire\":{\"oneMonthStayRequested\":\"No\",\"reactionProtocolCompliedWith\":\"Yes\",\"explainedToClient\":[\"CONFIRM\"]},\"applicant1DQDisclosureOfElectronicDocuments\":{\"reachedAgreement\":\"Yes\"},\"applicant1DQDisclosureOfNonElectronicDocuments\":{\"directionsForDisclosureProposed\":\"Yes\",\"standardDirectionsRequired\":\"Yes\",\"bespokeDirections\":null},\"applicant1DQDisclosureReport\":{\"disclosureFormFiledAndServed\":\"Yes\",\"disclosureProposalAgreed\":\"No\",\"draftOrderNumber\":null},\"applicant1DQExperts\":{\"expertRequired\":\"No\",\"details\":[]},\"applicant1DQWitnesses\":{\"witnessesToAppear\":\"No\",\"details\":[]},\"applicant1DQLanguage\":{\"evidence\":\"ENGLISH\",\"court\":\"ENGLISH\",\"documents\":\"ENGLISH\"},\"applicant1DQHearing\":{\"hearingLength\":\"ONE_DAY\",\"unavailableDatesRequired\":\"No\",\"unavailableDates\":[]},\"applicant1DQDraftDirections\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{DocumentIDForClaimResponse}\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{DocumentIDForClaimResponse}/binary\",\"document_filename\":\"2MB.pdf\"},\"applicant1DQHearingSupport\":{\"signLanguageRequired\":null,\"languageToBeInterpreted\":null,\"otherSupport\":null,\"requirements\":[]},\"applicant1DQFurtherInformation\":{\"futureApplications\":\"No\",\"otherInformationForJudge\":\"daasasasasaaaaaaaaaaaaaaaa\"}},\"event_token\":\"#{event_token_claimantresponse}\",\"ignore_warning\":false,\"case_reference\":\"#{caseId}\"}"))
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
    
    //val claimantdefresponsestatementoftruth =
      .group("CD_ClaimantResponse_180_StatementOfTruth") {
        exec(http("CD_ClaimantResponse_180_005_ST")
          .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSEStatementOfTruth")
          .headers(CivilDamagesHeader.headers_1946)
          .body(StringBody("{\"data\":{\"applicant1DQStatementOfTruth\":{\"name\":\"sajin\",\"role\":\"senior solicitor\"}},\"event\":{\"id\":\"CLAIMANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{Document_ID}\",\"document_filename\":\"1MB-c.pdf\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{Document_ID}/binary\"}},\"applicant1ProceedWithClaim\":\"Yes\",\"allocatedTrack\":\"MULTI_CLAIM\",\"claimType\":\"CLINICAL_NEGLIGENCE\",\"applicant1DefenceResponseDocument\":{},\"applicant1DQFileDirectionsQuestionnaire\":{\"oneMonthStayRequested\":\"No\",\"reactionProtocolCompliedWith\":\"Yes\",\"explainedToClient\":[\"CONFIRM\"]},\"applicant1DQDisclosureOfElectronicDocuments\":{\"reachedAgreement\":\"Yes\"},\"applicant1DQDisclosureOfNonElectronicDocuments\":{\"directionsForDisclosureProposed\":\"Yes\",\"standardDirectionsRequired\":\"Yes\",\"bespokeDirections\":null},\"applicant1DQDisclosureReport\":{\"disclosureFormFiledAndServed\":\"Yes\",\"disclosureProposalAgreed\":\"No\",\"draftOrderNumber\":null},\"applicant1DQExperts\":{\"expertRequired\":\"No\",\"details\":[]},\"applicant1DQWitnesses\":{\"witnessesToAppear\":\"No\",\"details\":[]},\"applicant1DQLanguage\":{\"evidence\":\"ENGLISH\",\"court\":\"ENGLISH\",\"documents\":\"ENGLISH\"},\"applicant1DQHearing\":{\"hearingLength\":\"ONE_DAY\",\"unavailableDatesRequired\":\"No\",\"unavailableDates\":[]},\"applicant1DQDraftDirections\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{DocumentIDForClaimResponse}\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{DocumentIDForClaimResponse}/binary\",\"document_filename\":\"2MB.pdf\"},\"applicant1DQHearingSupport\":{\"signLanguageRequired\":null,\"languageToBeInterpreted\":null,\"otherSupport\":null,\"requirements\":[]},\"applicant1DQFurtherInformation\":{\"futureApplications\":\"No\",\"otherInformationForJudge\":\"daasasasasaaaaaaaaaaaaaaaa\"},\"applicant1DQStatementOfTruth\":{\"name\":\"sajin\",\"role\":\"senior solicitor\"}},\"event_token\":\"#{event_token_claimantresponse}\",\"ignore_warning\":false,\"case_reference\":\"#{caseId}\"}"))
          .check(status.in(200, 304))
        )
          .exec(http("CD_ClaimantResponse_180_010_Profile")
            .get("/data/internal/profile")
            .headers(CivilDamagesHeader.headers_149)
            .check(status.in(200, 304))
          )
      }
        .pause(MinThinkTime, MaxThinkTime)
    
    //val claimantdefresponsesubmit =
      .group("CD_ClaimantResponse_190_SubmitResponse") {
        exec(http("CD_ClaimantResponse_190_submit")
          .post("/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.headers_1963)
          .body(StringBody("{\"data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{Document_ID}\",\"document_filename\":\"1MB-c.pdf\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{Document_ID}/binary\"}},\"applicant1ProceedWithClaim\":\"Yes\",\"allocatedTrack\":\"MULTI_CLAIM\",\"claimType\":\"CLINICAL_NEGLIGENCE\",\"applicant1DefenceResponseDocument\":{},\"applicant1DQFileDirectionsQuestionnaire\":{\"oneMonthStayRequested\":\"No\",\"reactionProtocolCompliedWith\":\"Yes\",\"explainedToClient\":[\"CONFIRM\"]},\"applicant1DQDisclosureOfElectronicDocuments\":{\"reachedAgreement\":\"Yes\"},\"applicant1DQDisclosureOfNonElectronicDocuments\":{\"directionsForDisclosureProposed\":\"Yes\",\"standardDirectionsRequired\":\"Yes\",\"bespokeDirections\":null},\"applicant1DQDisclosureReport\":{\"disclosureFormFiledAndServed\":\"Yes\",\"disclosureProposalAgreed\":\"No\",\"draftOrderNumber\":null},\"applicant1DQExperts\":{\"expertRequired\":\"No\",\"details\":[]},\"applicant1DQWitnesses\":{\"witnessesToAppear\":\"No\",\"details\":[]},\"applicant1DQLanguage\":{\"evidence\":\"ENGLISH\",\"court\":\"ENGLISH\",\"documents\":\"ENGLISH\"},\"applicant1DQHearing\":{\"hearingLength\":\"ONE_DAY\",\"unavailableDatesRequired\":\"No\",\"unavailableDates\":[]},\"applicant1DQDraftDirections\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{DocumentIDForClaimResponse}\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/#{DocumentIDForClaimResponse}/binary\",\"document_filename\":\"2MB.pdf\"},\"applicant1DQHearingSupport\":{\"signLanguageRequired\":null,\"languageToBeInterpreted\":null,\"otherSupport\":null,\"requirements\":[]},\"applicant1DQFurtherInformation\":{\"futureApplications\":\"No\",\"otherInformationForJudge\":\"daasasasasaaaaaaaaaaaaaaaa\"},\"applicant1DQStatementOfTruth\":{\"name\":\"sajin\",\"role\":\"senior solicitor\"}},\"event\":{\"id\":\"CLAIMANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_token\":\"#{event_token_claimantresponse}\",\"ignore_warning\":false}"))
          .check(status.in(200, 201))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
		  	.pause(50)
    
    //val claimantafterdefresponsesearchforcompletable =
      .group("CD_ClaimantResponse_200_backtoCaseFromClaimResponse") {
        exec(http("CD_ClaimantResponse_200_005_casedetails")
          .post("/workallocation/searchForCompletable")
          .headers(CivilDamagesHeader.headers_1970)
          .body(StringBody("{\"searchRequest\":{\"ccdId\":\"#{caseId}\",\"eventId\":\"CLAIMANT_RESPONSE\",\"jurisdiction\":\"CIVIL\",\"caseTypeId\":\"CIVIL\"}}"))
          .check(status.is(401))
        )
          
          .exec(http("CD_ClaimantResponse_200_010_case")
            .get("/data/internal/cases/#{caseId}")
            .headers(CivilDamagesHeader.headers_717)
            .check(status.in(200, 304))
          )
      }
        .pause(MinThinkTime, MaxThinkTime)
     
//  }
}