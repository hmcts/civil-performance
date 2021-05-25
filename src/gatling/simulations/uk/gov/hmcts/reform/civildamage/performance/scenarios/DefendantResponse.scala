
package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, Environment}

object DefendantResponse {
  
  val BaseURL = Environment.baseURL
  val IdAMURL = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime
  
  
  
  
  
  //def run(implicit postHeaders: Map[String, String]): ChainBuilder = {
    //acknowledge claim event by defendent
  
    val run =
      group("CD_DefResponse_40_AcknClaimEvent") {
        exec(http("CD_DefResponse_40_005_Ackn")
          .get("/data/internal/cases/${caseId}/event-triggers/ACKNOWLEDGE_CLAIM?ignore-warning=false")
          .headers(CivilDamagesHeader.headers_769)
          .check(status.in(200, 304))
          .check(jsonPath("$.event_token").optional.saveAs("event_token_ackknclaim"))
        )
          .exec(http("CD_DefResponse_40_010_Profile")
            .get("/data/internal/profile")
            .headers(CivilDamagesHeader.headers_149)
            .check(status.in(200, 304))
          )
      }
        .pause(MinThinkTime, MaxThinkTime)
          .pause(50)
  
    //val claimConfirmnameandaddress =
      .group("CD_DefResponse_50_ConfirmNameAddrs") {
        exec(http("CD_DefResponse_50_ConfirmName")
          .post("/data/case-types/CIVIL/validate?pageId=ACKNOWLEDGE_CLAIMConfirmNameAddress")
          .headers(CivilDamagesHeader.headers_955)
          .body(StringBody("{\"data\":{},\"event\":{\"id\":\"ACKNOWLEDGE_CLAIM\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{},\"event_token\":\"${event_token_ackknclaim}\",\"ignore_warning\":false,\"case_reference\":\"${caseId}\"}"))
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
  
    //val claimconfirmdetails =
      .group("CD_DefResponse_60_CLAIMConfirmDetails") {
        exec(http("CD_DefResponse_60_ConfirmDetails")
          .post("/data/case-types/CIVIL/validate?pageId=ACKNOWLEDGE_CLAIMConfirmDetails")
          .headers(CivilDamagesHeader.headers_969)
          .body(StringBody("{\"data\":{\"solicitorReferences\":{\"respondentSolicitor1Reference\":\"def ref\",\"applicantSolicitor1Reference\":\"claim ref\"},\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"individualFirstName\":null,\"individualLastName\":null,\"companyName\":\"defcom\",\"organisationName\":null,\"soleTraderTitle\":null,\"soleTraderFirstName\":null,\"soleTraderLastName\":null,\"soleTraderTradingAs\":null,\"partyName\":\"defcom\",\"partyTypeDisplayValue\":\"Company\"}},\"event\":{\"id\":\"ACKNOWLEDGE_CLAIM\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"solicitorReferences\":{\"respondentSolicitor1Reference\":\"def ref\",\"applicantSolicitor1Reference\":\"claim ref\"},\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"individualFirstName\":null,\"individualLastName\":null,\"companyName\":\"defcom\",\"organisationName\":null,\"soleTraderTitle\":null,\"soleTraderFirstName\":null,\"soleTraderLastName\":null,\"soleTraderTradingAs\":null,\"partyName\":\"defcom\",\"partyTypeDisplayValue\":\"Company\"}},\"event_token\":\"${event_token_ackknclaim}\",\"ignore_warning\":false,\"case_reference\":\"${caseId}\"}"))
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
  
    //val claimresponseintention =
      .group("CD_DefResponse_70_CLAIMResponseIntention") {
        exec(http("CD_DefResponse_70_005_Intention")
          .post("/data/case-types/CIVIL/validate?pageId=ACKNOWLEDGE_CLAIMResponseIntention")
          .headers(CivilDamagesHeader.headers_988)
          .body(StringBody("{\"data\":{\"respondent1ClaimResponseIntentionType\":\"FULL_DEFENCE\"},\"event\":{\"id\":\"ACKNOWLEDGE_CLAIM\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"solicitorReferences\":{\"respondentSolicitor1Reference\":\"def ref\",\"applicantSolicitor1Reference\":\"claim ref\"},\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"individualFirstName\":null,\"individualLastName\":null,\"companyName\":\"defcom\",\"organisationName\":null,\"soleTraderTitle\":null,\"soleTraderFirstName\":null,\"soleTraderLastName\":null,\"soleTraderTradingAs\":null,\"partyName\":\"defcom\",\"partyTypeDisplayValue\":\"Company\"},\"respondent1ClaimResponseIntentionType\":\"FULL_DEFENCE\"},\"event_token\":\"${event_token_ackknclaim}\",\"ignore_warning\":false,\"case_reference\":\"${caseId}\"}"))
          .check(status.in(200, 304))
        )
          .exec(http("CD_DefResponse_70_010Profile")
          .get("/data/internal/profile")
          .headers(CivilDamagesHeader.headers_149)
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
  
    //val ackneventsubmit =
      .group("CD_DefResponse_80_AcknEventSubmit") {
        exec(http("CD_DefResponse_80_EventSubmit")
          .post("/data/cases/${caseId}/events")
          .headers(CivilDamagesHeader.headers_1008)
          .body(StringBody("{\"data\":{\"solicitorReferences\":{\"respondentSolicitor1Reference\":\"def ref\",\"applicantSolicitor1Reference\":\"claim ref\"},\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"individualFirstName\":null,\"individualLastName\":null,\"companyName\":\"defcom\",\"organisationName\":null,\"soleTraderTitle\":null,\"soleTraderFirstName\":null,\"soleTraderLastName\":null,\"soleTraderTradingAs\":null,\"partyName\":\"defcom\",\"partyTypeDisplayValue\":\"Company\",\"primaryAddress\":{\"AddressLine1\":\"22 Hibernia Gardens\",\"AddressLine2\":null,\"AddressLine3\":null,\"PostTown\":\"Hounslow\",\"County\":null,\"PostCode\":\"TW3 3SD\",\"Country\":\"United Kingdom\"}},\"respondent1ClaimResponseIntentionType\":\"FULL_DEFENCE\"},\"event\":{\"id\":\"ACKNOWLEDGE_CLAIM\",\"summary\":\"\",\"description\":\"\"},\"event_token\":\"${event_token_ackknclaim}\",\"ignore_warning\":false}"))
          .check(status.in(200, 201))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
  
    //val backtocasedetailfromackn =
      .group("CD_DefResponse_90_ReturnToCaseFromAcknEvent") {
        exec(http("CD_DefResponse_90_005_returncase")
          .post("/workallocation/searchForCompletable")
          .headers(CivilDamagesHeader.headers_1015)
          .body(StringBody("{\"searchRequest\":{\"ccdId\":\"${caseId}\",\"eventId\":\"ACKNOWLEDGE_CLAIM\",\"jurisdiction\":\"CIVIL\",\"caseTypeId\":\"UNSPECIFIED_CLAIMS\"}}"))
          .check(status.is(401))
        )
        
          .exec(http("CD_DefResponse_90_010_internal")
            .get("/data/internal/cases/${caseId}")
            .headers(CivilDamagesHeader.headers_717)
            .check(status.in(200, 304))
          )
      }
        .pause(MinThinkTime, MaxThinkTime)
  
    //val defendantresponseevent =
      .group("CD_DefResponse_100_DefResponseEvent") {
        exec(http("CD_DefResponse_100_005_DefResponse")
          .get("/data/internal/cases/${caseId}/event-triggers/DEFENDANT_RESPONSE?ignore-warning=false")
          .headers(CivilDamagesHeader.headers_769)
          .check(status.in(200, 304))
          .check(jsonPath("$.event_token").optional.saveAs("event_token_defresponse"))
        )
          .exec(http("CD_DefResponse_100_010_Profile")
            .get("/data/internal/profile")
            .headers(CivilDamagesHeader.headers_149)
            .check(status.in(200, 304))
          )
      }
        .pause(MinThinkTime, MaxThinkTime)
  
    //val defresponsetype =
      .group("CD_DefResponse_110_DefResponseType") {
        exec(http("CD_DefResponse_110_ResponseType")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSERespondentResponseType")
          .headers(CivilDamagesHeader.headers_1066)
          .body(StringBody("{\"data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\"},\"event\":{\"id\":\"DEFENDANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\"},\"event_token\":\"${event_token_defresponse}\",\"ignore_warning\":false,\"case_reference\":\"${caseId}\"}"))
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
  
    //val defdocuments =
      .group("CD_DefResponse_120_documents") {
        exec(http("CD_DefResponse_120_docs")
          .post("/documents")
          .headers(CivilDamagesHeader.headers_1084)
          .bodyPart(RawFileBodyPart("files", "1MB-c.pdf")
            .fileName("1MB-c.pdf")
            .transferEncoding("binary")
          )
          .asMultipartForm
          .formParam("classification", "PUBLIC")
          .check(regex("""http://(.+)/""").saveAs("DMURL"))
          .check(regex("""internal/documents/(.+?)/binary""").saveAs("Document_ID"))
          .check(status.in(200, 304))
        )
      }
      //.exec(session => session.set("Document_ID", "${Document_ID}"))
      .pause(MinThinkTime, MaxThinkTime)
  
    //val defresponseupload =
      .group("CD_DefResponse_130_upload") {
        exec(http("CD_DefResponse_130_upload")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEUpload")
          .headers(CivilDamagesHeader.headers_1092)
          .body(StringBody("{\"data\":{\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}/binary\",\"document_filename\":\"1MB-c.pdf\"}}},\"event\":{\"id\":\"DEFENDANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}/binary\",\"document_filename\":\"1MB-c.pdf\"}}},\"event_token\":\"${event_token_defresponse}\",\"ignore_warning\":false,\"case_reference\":\"${caseId}\"}"))
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
  
    //val defresponseconfirmnameandaddr =
      .group("CD_DefResponse_140_ConfirmNameAddr") {
        exec(http("CD_DefResponse_140_NameAddr")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEConfirmNameAddress")
          .headers(CivilDamagesHeader.headers_1110)
          .body(StringBody("{\"data\":{},\"event\":{\"id\":\"DEFENDANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}/binary\",\"document_filename\":\"1MB-c.pdf\"}}},\"event_token\":\"${event_token_defresponse}\",\"ignore_warning\":false,\"case_reference\":\"${caseId}\"}"))
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
  
    //val defresponseconfirmdetails =
      .group("CD_DefResponse_150_RespConfirmDetails") {
        exec(http("CD_DefResponse_150_Confirm")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEConfirmDetails")
          .headers(CivilDamagesHeader.headers_1132)
          .body(StringBody("{\"data\":{\"solicitorReferences\":{\"respondentSolicitor1Reference\":\"def ref\",\"applicantSolicitor1Reference\":\"claim ref\"},\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"individualFirstName\":null,\"individualLastName\":null,\"companyName\":\"defcom\",\"organisationName\":null,\"soleTraderTitle\":null,\"soleTraderFirstName\":null,\"soleTraderLastName\":null,\"soleTraderTradingAs\":null,\"partyName\":\"defcom\",\"partyTypeDisplayValue\":\"Company\"}},\"event\":{\"id\":\"DEFENDANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}/binary\",\"document_filename\":\"1MB-c.pdf\"}},\"solicitorReferences\":{\"respondentSolicitor1Reference\":\"def ref\",\"applicantSolicitor1Reference\":\"claim ref\"},\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"individualFirstName\":null,\"individualLastName\":null,\"companyName\":\"defcom\",\"organisationName\":null,\"soleTraderTitle\":null,\"soleTraderFirstName\":null,\"soleTraderLastName\":null,\"soleTraderTradingAs\":null,\"partyName\":\"defcom\",\"partyTypeDisplayValue\":\"Company\"}},\"event_token\":\"${event_token_defresponse}\",\"ignore_warning\":false,\"case_reference\":\"${caseId}\"}"))
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
  
    //val responsefiledirectoryquestionaire =
      .group("CD_DefResponse_160_DirectionsQuestionaire") {
        exec(http("CD_DefResponse_160_DirectionsQuestion")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEFileDirectionsQuestionnaire")
          .headers(CivilDamagesHeader.headers_1174)
          .body(StringBody("{\"data\":{\"respondent1DQFileDirectionsQuestionnaire\":{\"oneMonthStayRequested\":\"Yes\",\"reactionProtocolCompliedWith\":\"Yes\",\"explainedToClient\":[\"CONFIRM\"]}},\"event\":{\"id\":\"DEFENDANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}/binary\",\"document_filename\":\"1MB-c.pdf\"}},\"solicitorReferences\":{\"respondentSolicitor1Reference\":\"def ref\",\"applicantSolicitor1Reference\":\"claim ref\"},\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"individualFirstName\":null,\"individualLastName\":null,\"companyName\":\"defcom\",\"organisationName\":null,\"soleTraderTitle\":null,\"soleTraderFirstName\":null,\"soleTraderLastName\":null,\"soleTraderTradingAs\":null,\"partyName\":\"defcom\",\"partyTypeDisplayValue\":\"Company\"},\"respondent1DQFileDirectionsQuestionnaire\":{\"oneMonthStayRequested\":\"Yes\",\"reactionProtocolCompliedWith\":\"Yes\",\"explainedToClient\":[\"CONFIRM\"]}},\"event_token\":\"${event_token_defresponse}\",\"ignore_warning\":false,\"case_reference\":\"${caseId}\"}"))
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
  
    //val disclosureofelectronicsdoc =
      .group("CD_DefResponse_170_ClosureOfElectronics") {
        exec(http("CD_DefResponse_170_Electronics")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEDisclosureOfElectronicDocuments")
          .headers(CivilDamagesHeader.headers_1199)
          .body(StringBody("{\"data\":{\"respondent1DQDisclosureOfElectronicDocuments\":{\"reachedAgreement\":\"Yes\"}},\"event\":{\"id\":\"DEFENDANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}/binary\",\"document_filename\":\"1MB-c.pdf\"}},\"solicitorReferences\":{\"respondentSolicitor1Reference\":\"def ref\",\"applicantSolicitor1Reference\":\"claim ref\"},\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"individualFirstName\":null,\"individualLastName\":null,\"companyName\":\"defcom\",\"organisationName\":null,\"soleTraderTitle\":null,\"soleTraderFirstName\":null,\"soleTraderLastName\":null,\"soleTraderTradingAs\":null,\"partyName\":\"defcom\",\"partyTypeDisplayValue\":\"Company\"},\"respondent1DQFileDirectionsQuestionnaire\":{\"oneMonthStayRequested\":\"Yes\",\"reactionProtocolCompliedWith\":\"Yes\",\"explainedToClient\":[\"CONFIRM\"]},\"respondent1DQDisclosureOfElectronicDocuments\":{\"reachedAgreement\":\"Yes\"}},\"event_token\":\"${event_token_defresponse}\",\"ignore_warning\":false,\"case_reference\":\"${caseId}\"}"))
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
  
    //val disclosureofnonelectronicsdoc =
      .group("CD_DefResponse_180_ClosureOfNonElectronics") {
        exec(http("CD_DefResponse_180_NonElectronics")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEDisclosureOfNonElectronicDocuments")
          .headers(CivilDamagesHeader.headers_1217)
          .body(StringBody("{\"data\":{\"respondent1DQDisclosureOfNonElectronicDocuments\":{\"directionsForDisclosureProposed\":\"No\",\"standardDirectionsRequired\":null,\"bespokeDirections\":null}},\"event\":{\"id\":\"DEFENDANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}/binary\",\"document_filename\":\"1MB-c.pdf\"}},\"solicitorReferences\":{\"respondentSolicitor1Reference\":\"def ref\",\"applicantSolicitor1Reference\":\"claim ref\"},\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"individualFirstName\":null,\"individualLastName\":null,\"companyName\":\"defcom\",\"organisationName\":null,\"soleTraderTitle\":null,\"soleTraderFirstName\":null,\"soleTraderLastName\":null,\"soleTraderTradingAs\":null,\"partyName\":\"defcom\",\"partyTypeDisplayValue\":\"Company\"},\"respondent1DQFileDirectionsQuestionnaire\":{\"oneMonthStayRequested\":\"Yes\",\"reactionProtocolCompliedWith\":\"Yes\",\"explainedToClient\":[\"CONFIRM\"]},\"respondent1DQDisclosureOfElectronicDocuments\":{\"reachedAgreement\":\"Yes\"},\"respondent1DQDisclosureOfNonElectronicDocuments\":{\"directionsForDisclosureProposed\":\"No\",\"standardDirectionsRequired\":null,\"bespokeDirections\":null}},\"event_token\":\"${event_token_defresponse}\",\"ignore_warning\":false,\"case_reference\":\"${caseId}\"}"))
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
  
    //val responsedisclosurereport =
      .group("CD_DefResponse_190_RespDisclosureReport") {
        exec(http("CD_DefResponse_190_Report")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEDisclosureReport")
          .headers(CivilDamagesHeader.headers_1239)
          .body(StringBody("{\"data\":{\"respondent1DQDisclosureReport\":{\"disclosureFormFiledAndServed\":\"No\",\"disclosureProposalAgreed\":\"No\",\"draftOrderNumber\":null}},\"event\":{\"id\":\"DEFENDANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}/binary\",\"document_filename\":\"1MB-c.pdf\"}},\"solicitorReferences\":{\"respondentSolicitor1Reference\":\"def ref\",\"applicantSolicitor1Reference\":\"claim ref\"},\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"individualFirstName\":null,\"individualLastName\":null,\"companyName\":\"defcom\",\"organisationName\":null,\"soleTraderTitle\":null,\"soleTraderFirstName\":null,\"soleTraderLastName\":null,\"soleTraderTradingAs\":null,\"partyName\":\"defcom\",\"partyTypeDisplayValue\":\"Company\"},\"respondent1DQFileDirectionsQuestionnaire\":{\"oneMonthStayRequested\":\"Yes\",\"reactionProtocolCompliedWith\":\"Yes\",\"explainedToClient\":[\"CONFIRM\"]},\"respondent1DQDisclosureOfElectronicDocuments\":{\"reachedAgreement\":\"Yes\"},\"respondent1DQDisclosureOfNonElectronicDocuments\":{\"directionsForDisclosureProposed\":\"No\",\"standardDirectionsRequired\":null,\"bespokeDirections\":null},\"respondent1DQDisclosureReport\":{\"disclosureFormFiledAndServed\":\"No\",\"disclosureProposalAgreed\":\"No\",\"draftOrderNumber\":null}},\"event_token\":\"${event_token_defresponse}\",\"ignore_warning\":false,\"case_reference\":\"${caseId}\"}"))
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
  
    //val defresponseexpert =
      .group("CD_DefResponse_200_RESPONSEExperts") {
        exec(http("CD_DefResponse_200_Experts")
          .post("/data/case-types/CIVIL///validate?pageId=DEFENDANT_RESPONSEExperts")
          .headers(CivilDamagesHeader.headers_1334)
          .body(StringBody("{\"data\":{\"respondent1DQExperts\":{\"expertRequired\":\"Yes\",\"expertReportsSent\":\"YES\",\"jointExpertSuitable\":\"No\",\"details\":[{\"value\":{\"name\":\"asasasa\",\"fieldOfExpertise\":\"fgffgfgfg\",\"whyRequired\":\"ggfgfgfgfgfgfgfgfgfg\",\"estimatedCost\":\"2000000\"},\"id\":null}]}},\"event\":{\"id\":\"DEFENDANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}/binary\",\"document_filename\":\"1MB-c.pdf\"}},\"solicitorReferences\":{\"respondentSolicitor1Reference\":\"def ref\",\"applicantSolicitor1Reference\":\"claim ref\"},\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"individualFirstName\":null,\"individualLastName\":null,\"companyName\":\"defcom\",\"organisationName\":null,\"soleTraderTitle\":null,\"soleTraderFirstName\":null,\"soleTraderLastName\":null,\"soleTraderTradingAs\":null,\"partyName\":\"defcom\",\"partyTypeDisplayValue\":\"Company\"},\"respondent1DQFileDirectionsQuestionnaire\":{\"oneMonthStayRequested\":\"Yes\",\"reactionProtocolCompliedWith\":\"Yes\",\"explainedToClient\":[\"CONFIRM\"]},\"respondent1DQDisclosureOfElectronicDocuments\":{\"reachedAgreement\":\"Yes\"},\"respondent1DQDisclosureOfNonElectronicDocuments\":{\"directionsForDisclosureProposed\":\"No\",\"standardDirectionsRequired\":null,\"bespokeDirections\":null},\"respondent1DQDisclosureReport\":{\"disclosureFormFiledAndServed\":\"No\",\"disclosureProposalAgreed\":\"No\",\"draftOrderNumber\":null},\"respondent1DQExperts\":{\"expertRequired\":\"Yes\",\"expertReportsSent\":\"YES\",\"jointExpertSuitable\":\"No\",\"details\":[{\"value\":{\"name\":\"asasasa\",\"fieldOfExpertise\":\"fgffgfgfg\",\"whyRequired\":\"ggfgfgfgfgfgfgfgfgfg\",\"estimatedCost\":\"2000000\"},\"id\":null}]}},\"event_token\":\"${event_token_defresponse}\",\"ignore_warning\":false,\"case_reference\":\"${caseId}\"}"))
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
  
    //val defresponsewitness =
      .group("CD_DefResponse_210_RESPONSEWitnesses") {
        exec(http("CD_DefResponse_210_Witness")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEWitnesses")
          .headers(CivilDamagesHeader.headers_1352)
          .body(StringBody("{\"data\":{\"respondent1DQWitnesses\":{\"witnessesToAppear\":\"No\"}},\"event\":{\"id\":\"DEFENDANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}/binary\",\"document_filename\":\"1MB-c.pdf\"}},\"solicitorReferences\":{\"respondentSolicitor1Reference\":\"def ref\",\"applicantSolicitor1Reference\":\"claim ref\"},\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"individualFirstName\":null,\"individualLastName\":null,\"companyName\":\"defcom\",\"organisationName\":null,\"soleTraderTitle\":null,\"soleTraderFirstName\":null,\"soleTraderLastName\":null,\"soleTraderTradingAs\":null,\"partyName\":\"defcom\",\"partyTypeDisplayValue\":\"Company\"},\"respondent1DQFileDirectionsQuestionnaire\":{\"oneMonthStayRequested\":\"Yes\",\"reactionProtocolCompliedWith\":\"Yes\",\"explainedToClient\":[\"CONFIRM\"]},\"respondent1DQDisclosureOfElectronicDocuments\":{\"reachedAgreement\":\"Yes\"},\"respondent1DQDisclosureOfNonElectronicDocuments\":{\"directionsForDisclosureProposed\":\"No\",\"standardDirectionsRequired\":null,\"bespokeDirections\":null},\"respondent1DQDisclosureReport\":{\"disclosureFormFiledAndServed\":\"No\",\"disclosureProposalAgreed\":\"No\",\"draftOrderNumber\":null},\"respondent1DQExperts\":{\"expertRequired\":\"Yes\",\"expertReportsSent\":\"YES\",\"jointExpertSuitable\":\"No\",\"details\":[{\"value\":{\"name\":\"asasasa\",\"fieldOfExpertise\":\"fgffgfgfg\",\"whyRequired\":\"ggfgfgfgfgfgfgfgfgfg\",\"estimatedCost\":\"2000000\"},\"id\":null}]},\"respondent1DQWitnesses\":{\"witnessesToAppear\":\"No\"}},\"event_token\":\"${event_token_defresponse}\",\"ignore_warning\":false,\"case_reference\":\"${caseId}\"}"))
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
  
    //val defresponselanguage =
      .group("CD_DefResponse_220_RESPONSELanguage") {
        exec(http("CD_DefResponse_220_Language")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSELanguage")
          .headers(CivilDamagesHeader.headers_1379)
          .body(StringBody("{\"data\":{\"respondent1DQLanguage\":{\"evidence\":\"ENGLISH\",\"court\":\"ENGLISH\",\"documents\":\"ENGLISH\"}},\"event\":{\"id\":\"DEFENDANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}/binary\",\"document_filename\":\"1MB-c.pdf\"}},\"solicitorReferences\":{\"respondentSolicitor1Reference\":\"def ref\",\"applicantSolicitor1Reference\":\"claim ref\"},\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"individualFirstName\":null,\"individualLastName\":null,\"companyName\":\"defcom\",\"organisationName\":null,\"soleTraderTitle\":null,\"soleTraderFirstName\":null,\"soleTraderLastName\":null,\"soleTraderTradingAs\":null,\"partyName\":\"defcom\",\"partyTypeDisplayValue\":\"Company\"},\"respondent1DQFileDirectionsQuestionnaire\":{\"oneMonthStayRequested\":\"Yes\",\"reactionProtocolCompliedWith\":\"Yes\",\"explainedToClient\":[\"CONFIRM\"]},\"respondent1DQDisclosureOfElectronicDocuments\":{\"reachedAgreement\":\"Yes\"},\"respondent1DQDisclosureOfNonElectronicDocuments\":{\"directionsForDisclosureProposed\":\"No\",\"standardDirectionsRequired\":null,\"bespokeDirections\":null},\"respondent1DQDisclosureReport\":{\"disclosureFormFiledAndServed\":\"No\",\"disclosureProposalAgreed\":\"No\",\"draftOrderNumber\":null},\"respondent1DQExperts\":{\"expertRequired\":\"Yes\",\"expertReportsSent\":\"YES\",\"jointExpertSuitable\":\"No\",\"details\":[{\"value\":{\"name\":\"asasasa\",\"fieldOfExpertise\":\"fgffgfgfg\",\"whyRequired\":\"ggfgfgfgfgfgfgfgfgfg\",\"estimatedCost\":\"2000000\"},\"id\":null}]},\"respondent1DQWitnesses\":{\"witnessesToAppear\":\"No\"},\"respondent1DQLanguage\":{\"evidence\":\"ENGLISH\",\"court\":\"ENGLISH\",\"documents\":\"ENGLISH\"}},\"event_token\":\"${event_token_defresponse}\",\"ignore_warning\":false,\"case_reference\":\"${caseId}\"}"))
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
  
    //val defresponsehearing =
      .group("CD_DefResponse_230_RESPONSEHearing") {
        exec(http("CD_DefResponse_230_Hearing")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEHearing")
          .headers(CivilDamagesHeader.headers_1404)
          .body(StringBody("{\"data\":{\"respondent1DQHearing\":{\"hearingLength\":\"ONE_DAY\",\"unavailableDatesRequired\":\"No\"}},\"event\":{\"id\":\"DEFENDANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}/binary\",\"document_filename\":\"1MB-c.pdf\"}},\"solicitorReferences\":{\"respondentSolicitor1Reference\":\"def ref\",\"applicantSolicitor1Reference\":\"claim ref\"},\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"individualFirstName\":null,\"individualLastName\":null,\"companyName\":\"defcom\",\"organisationName\":null,\"soleTraderTitle\":null,\"soleTraderFirstName\":null,\"soleTraderLastName\":null,\"soleTraderTradingAs\":null,\"partyName\":\"defcom\",\"partyTypeDisplayValue\":\"Company\"},\"respondent1DQFileDirectionsQuestionnaire\":{\"oneMonthStayRequested\":\"Yes\",\"reactionProtocolCompliedWith\":\"Yes\",\"explainedToClient\":[\"CONFIRM\"]},\"respondent1DQDisclosureOfElectronicDocuments\":{\"reachedAgreement\":\"Yes\"},\"respondent1DQDisclosureOfNonElectronicDocuments\":{\"directionsForDisclosureProposed\":\"No\",\"standardDirectionsRequired\":null,\"bespokeDirections\":null},\"respondent1DQDisclosureReport\":{\"disclosureFormFiledAndServed\":\"No\",\"disclosureProposalAgreed\":\"No\",\"draftOrderNumber\":null},\"respondent1DQExperts\":{\"expertRequired\":\"Yes\",\"expertReportsSent\":\"YES\",\"jointExpertSuitable\":\"No\",\"details\":[{\"value\":{\"name\":\"asasasa\",\"fieldOfExpertise\":\"fgffgfgfg\",\"whyRequired\":\"ggfgfgfgfgfgfgfgfgfg\",\"estimatedCost\":\"2000000\"},\"id\":null}]},\"respondent1DQWitnesses\":{\"witnessesToAppear\":\"No\"},\"respondent1DQLanguage\":{\"evidence\":\"ENGLISH\",\"court\":\"ENGLISH\",\"documents\":\"ENGLISH\"},\"respondent1DQHearing\":{\"hearingLength\":\"ONE_DAY\",\"unavailableDatesRequired\":\"No\"}},\"event_token\":\"${event_token_defresponse}\",\"ignore_warning\":false,\"case_reference\":\"${caseId}\"}"))
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
  
    //val defresponsedraftdirections =
      .group("CD_DefResponse_240_RESPONSEDraftDirections") {
        exec(http("CD_DefResponse_240_Directions")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEDraftDirections")
          .headers(CivilDamagesHeader.headers_1426)
          .body(StringBody("{\"data\":{},\"event\":{\"id\":\"DEFENDANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}/binary\",\"document_filename\":\"1MB-c.pdf\"}},\"solicitorReferences\":{\"respondentSolicitor1Reference\":\"def ref\",\"applicantSolicitor1Reference\":\"claim ref\"},\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"individualFirstName\":null,\"individualLastName\":null,\"companyName\":\"defcom\",\"organisationName\":null,\"soleTraderTitle\":null,\"soleTraderFirstName\":null,\"soleTraderLastName\":null,\"soleTraderTradingAs\":null,\"partyName\":\"defcom\",\"partyTypeDisplayValue\":\"Company\"},\"respondent1DQFileDirectionsQuestionnaire\":{\"oneMonthStayRequested\":\"Yes\",\"reactionProtocolCompliedWith\":\"Yes\",\"explainedToClient\":[\"CONFIRM\"]},\"respondent1DQDisclosureOfElectronicDocuments\":{\"reachedAgreement\":\"Yes\"},\"respondent1DQDisclosureOfNonElectronicDocuments\":{\"directionsForDisclosureProposed\":\"No\",\"standardDirectionsRequired\":null,\"bespokeDirections\":null},\"respondent1DQDisclosureReport\":{\"disclosureFormFiledAndServed\":\"No\",\"disclosureProposalAgreed\":\"No\",\"draftOrderNumber\":null},\"respondent1DQExperts\":{\"expertRequired\":\"Yes\",\"expertReportsSent\":\"YES\",\"jointExpertSuitable\":\"No\",\"details\":[{\"value\":{\"name\":\"asasasa\",\"fieldOfExpertise\":\"fgffgfgfg\",\"whyRequired\":\"ggfgfgfgfgfgfgfgfgfg\",\"estimatedCost\":\"2000000\"},\"id\":null}]},\"respondent1DQWitnesses\":{\"witnessesToAppear\":\"No\"},\"respondent1DQLanguage\":{\"evidence\":\"ENGLISH\",\"court\":\"ENGLISH\",\"documents\":\"ENGLISH\"},\"respondent1DQHearing\":{\"hearingLength\":\"ONE_DAY\",\"unavailableDatesRequired\":\"No\"}},\"event_token\":\"${event_token_defresponse}\",\"ignore_warning\":false,\"case_reference\":\"${caseId}\"}"))
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
  
    //val defresponserequestedcourt =
      .group("CD_DefResponse_250_RESPONSERequestedCourt") {
        exec(http("CD_DefResponse_250_Court")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSERequestedCourt")
          .headers(CivilDamagesHeader.headers_1454)
          .body(StringBody("{\"data\":{\"respondent1DQRequestedCourt\":{\"requestHearingAtSpecificCourt\":\"No\"}},\"event\":{\"id\":\"DEFENDANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}/binary\",\"document_filename\":\"1MB-c.pdf\"}},\"solicitorReferences\":{\"respondentSolicitor1Reference\":\"def ref\",\"applicantSolicitor1Reference\":\"claim ref\"},\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"individualFirstName\":null,\"individualLastName\":null,\"companyName\":\"defcom\",\"organisationName\":null,\"soleTraderTitle\":null,\"soleTraderFirstName\":null,\"soleTraderLastName\":null,\"soleTraderTradingAs\":null,\"partyName\":\"defcom\",\"partyTypeDisplayValue\":\"Company\"},\"respondent1DQFileDirectionsQuestionnaire\":{\"oneMonthStayRequested\":\"Yes\",\"reactionProtocolCompliedWith\":\"Yes\",\"explainedToClient\":[\"CONFIRM\"]},\"respondent1DQDisclosureOfElectronicDocuments\":{\"reachedAgreement\":\"Yes\"},\"respondent1DQDisclosureOfNonElectronicDocuments\":{\"directionsForDisclosureProposed\":\"No\",\"standardDirectionsRequired\":null,\"bespokeDirections\":null},\"respondent1DQDisclosureReport\":{\"disclosureFormFiledAndServed\":\"No\",\"disclosureProposalAgreed\":\"No\",\"draftOrderNumber\":null},\"respondent1DQExperts\":{\"expertRequired\":\"Yes\",\"expertReportsSent\":\"YES\",\"jointExpertSuitable\":\"No\",\"details\":[{\"value\":{\"name\":\"asasasa\",\"fieldOfExpertise\":\"fgffgfgfg\",\"whyRequired\":\"ggfgfgfgfgfgfgfgfgfg\",\"estimatedCost\":\"2000000\"},\"id\":null}]},\"respondent1DQWitnesses\":{\"witnessesToAppear\":\"No\"},\"respondent1DQLanguage\":{\"evidence\":\"ENGLISH\",\"court\":\"ENGLISH\",\"documents\":\"ENGLISH\"},\"respondent1DQHearing\":{\"hearingLength\":\"ONE_DAY\",\"unavailableDatesRequired\":\"No\"},\"respondent1DQRequestedCourt\":{\"requestHearingAtSpecificCourt\":\"No\"}},\"event_token\":\"${event_token_defresponse}\",\"ignore_warning\":false,\"case_reference\":\"${caseId}\"}"))
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
  
    //val defresponsehearingsupport =
      .group("CD_DefResponse_260_RESPONSEHearingSupport") {
        exec(http("CD_DefResponse_260_Support")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEHearingSupport")
          .headers(CivilDamagesHeader.headers_1476)
          .body(StringBody("{\"data\":{},\"event\":{\"id\":\"DEFENDANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}/binary\",\"document_filename\":\"1MB-c.pdf\"}},\"solicitorReferences\":{\"respondentSolicitor1Reference\":\"def ref\",\"applicantSolicitor1Reference\":\"claim ref\"},\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"individualFirstName\":null,\"individualLastName\":null,\"companyName\":\"defcom\",\"organisationName\":null,\"soleTraderTitle\":null,\"soleTraderFirstName\":null,\"soleTraderLastName\":null,\"soleTraderTradingAs\":null,\"partyName\":\"defcom\",\"partyTypeDisplayValue\":\"Company\"},\"respondent1DQFileDirectionsQuestionnaire\":{\"oneMonthStayRequested\":\"Yes\",\"reactionProtocolCompliedWith\":\"Yes\",\"explainedToClient\":[\"CONFIRM\"]},\"respondent1DQDisclosureOfElectronicDocuments\":{\"reachedAgreement\":\"Yes\"},\"respondent1DQDisclosureOfNonElectronicDocuments\":{\"directionsForDisclosureProposed\":\"No\",\"standardDirectionsRequired\":null,\"bespokeDirections\":null},\"respondent1DQDisclosureReport\":{\"disclosureFormFiledAndServed\":\"No\",\"disclosureProposalAgreed\":\"No\",\"draftOrderNumber\":null},\"respondent1DQExperts\":{\"expertRequired\":\"Yes\",\"expertReportsSent\":\"YES\",\"jointExpertSuitable\":\"No\",\"details\":[{\"value\":{\"name\":\"asasasa\",\"fieldOfExpertise\":\"fgffgfgfg\",\"whyRequired\":\"ggfgfgfgfgfgfgfgfgfg\",\"estimatedCost\":\"2000000\"},\"id\":null}]},\"respondent1DQWitnesses\":{\"witnessesToAppear\":\"No\"},\"respondent1DQLanguage\":{\"evidence\":\"ENGLISH\",\"court\":\"ENGLISH\",\"documents\":\"ENGLISH\"},\"respondent1DQHearing\":{\"hearingLength\":\"ONE_DAY\",\"unavailableDatesRequired\":\"No\"},\"respondent1DQRequestedCourt\":{\"requestHearingAtSpecificCourt\":\"No\"},\"respondent1DQHearingSupport\":{\"signLanguageRequired\":null,\"languageToBeInterpreted\":null,\"otherSupport\":null,\"requirements\":[]}},\"event_token\":\"${event_token_defresponse}\",\"ignore_warning\":false,\"case_reference\":\"${caseId}\"}"))
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
  
    //val responsefurtherinformation =
      .group("CD_DefResponse_270_RESPONSEFurtherInformation") {
        exec(http("CD_DefResponse_270_Info")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEFurtherInformation")
          .headers(CivilDamagesHeader.headers_1502)
          .body(StringBody("{\"data\":{\"respondent1DQFurtherInformation\":{\"futureApplications\":\"No\",\"otherInformationForJudge\":null}},\"event\":{\"id\":\"DEFENDANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}/binary\",\"document_filename\":\"1MB-c.pdf\"}},\"solicitorReferences\":{\"respondentSolicitor1Reference\":\"def ref\",\"applicantSolicitor1Reference\":\"claim ref\"},\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"individualFirstName\":null,\"individualLastName\":null,\"companyName\":\"defcom\",\"organisationName\":null,\"soleTraderTitle\":null,\"soleTraderFirstName\":null,\"soleTraderLastName\":null,\"soleTraderTradingAs\":null,\"partyName\":\"defcom\",\"partyTypeDisplayValue\":\"Company\"},\"respondent1DQFileDirectionsQuestionnaire\":{\"oneMonthStayRequested\":\"Yes\",\"reactionProtocolCompliedWith\":\"Yes\",\"explainedToClient\":[\"CONFIRM\"]},\"respondent1DQDisclosureOfElectronicDocuments\":{\"reachedAgreement\":\"Yes\"},\"respondent1DQDisclosureOfNonElectronicDocuments\":{\"directionsForDisclosureProposed\":\"No\",\"standardDirectionsRequired\":null,\"bespokeDirections\":null},\"respondent1DQDisclosureReport\":{\"disclosureFormFiledAndServed\":\"No\",\"disclosureProposalAgreed\":\"No\",\"draftOrderNumber\":null},\"respondent1DQExperts\":{\"expertRequired\":\"Yes\",\"expertReportsSent\":\"YES\",\"jointExpertSuitable\":\"No\",\"details\":[{\"value\":{\"name\":\"asasasa\",\"fieldOfExpertise\":\"fgffgfgfg\",\"whyRequired\":\"ggfgfgfgfgfgfgfgfgfg\",\"estimatedCost\":\"2000000\"},\"id\":null}]},\"respondent1DQWitnesses\":{\"witnessesToAppear\":\"No\"},\"respondent1DQLanguage\":{\"evidence\":\"ENGLISH\",\"court\":\"ENGLISH\",\"documents\":\"ENGLISH\"},\"respondent1DQHearing\":{\"hearingLength\":\"ONE_DAY\",\"unavailableDatesRequired\":\"No\"},\"respondent1DQRequestedCourt\":{\"requestHearingAtSpecificCourt\":\"No\"},\"respondent1DQHearingSupport\":{\"signLanguageRequired\":null,\"languageToBeInterpreted\":null,\"otherSupport\":null,\"requirements\":[]},\"respondent1DQFurtherInformation\":{\"futureApplications\":\"No\",\"otherInformationForJudge\":null}},\"event_token\":\"${event_token_defresponse}\",\"ignore_warning\":false,\"case_reference\":\"${caseId}\"}"))
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
  
    //val defresponsestatementoftruth =
      .group("CD_DefResponse_280_RESPONSEStatementOfTruth") {
        exec(http("CD_DefResponse_280_005_ST")
          .post("/data/case-types/CIVIL/validate?pageId=DEFENDANT_RESPONSEStatementOfTruth")
          .headers(CivilDamagesHeader.headers_1530)
          .body(StringBody("{\"data\":{\"respondent1DQStatementOfTruth\":{\"name\":\"respname\",\"role\":\"senior solicitor\"}},\"event\":{\"id\":\"DEFENDANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}/binary\",\"document_filename\":\"1MB-c.pdf\"}},\"solicitorReferences\":{\"respondentSolicitor1Reference\":\"def ref\",\"applicantSolicitor1Reference\":\"claim ref\"},\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"individualFirstName\":null,\"individualLastName\":null,\"companyName\":\"defcom\",\"organisationName\":null,\"soleTraderTitle\":null,\"soleTraderFirstName\":null,\"soleTraderLastName\":null,\"soleTraderTradingAs\":null,\"partyName\":\"defcom\",\"partyTypeDisplayValue\":\"Company\"},\"respondent1DQFileDirectionsQuestionnaire\":{\"oneMonthStayRequested\":\"Yes\",\"reactionProtocolCompliedWith\":\"Yes\",\"explainedToClient\":[\"CONFIRM\"]},\"respondent1DQDisclosureOfElectronicDocuments\":{\"reachedAgreement\":\"Yes\"},\"respondent1DQDisclosureOfNonElectronicDocuments\":{\"directionsForDisclosureProposed\":\"No\",\"standardDirectionsRequired\":null,\"bespokeDirections\":null},\"respondent1DQDisclosureReport\":{\"disclosureFormFiledAndServed\":\"No\",\"disclosureProposalAgreed\":\"No\",\"draftOrderNumber\":null},\"respondent1DQExperts\":{\"expertRequired\":\"Yes\",\"expertReportsSent\":\"YES\",\"jointExpertSuitable\":\"No\",\"details\":[{\"value\":{\"name\":\"asasasa\",\"fieldOfExpertise\":\"fgffgfgfg\",\"whyRequired\":\"ggfgfgfgfgfgfgfgfgfg\",\"estimatedCost\":\"2000000\"},\"id\":null}]},\"respondent1DQWitnesses\":{\"witnessesToAppear\":\"No\"},\"respondent1DQLanguage\":{\"evidence\":\"ENGLISH\",\"court\":\"ENGLISH\",\"documents\":\"ENGLISH\"},\"respondent1DQHearing\":{\"hearingLength\":\"ONE_DAY\",\"unavailableDatesRequired\":\"No\"},\"respondent1DQRequestedCourt\":{\"requestHearingAtSpecificCourt\":\"No\"},\"respondent1DQHearingSupport\":{\"signLanguageRequired\":null,\"languageToBeInterpreted\":null,\"otherSupport\":null,\"requirements\":[]},\"respondent1DQFurtherInformation\":{\"futureApplications\":\"No\",\"otherInformationForJudge\":null},\"respondent1DQStatementOfTruth\":{\"name\":\"respname\",\"role\":\"senior solicitor\"}},\"event_token\":\"${event_token_defresponse}\",\"ignore_warning\":false,\"case_reference\":\"${caseId}\"}"))
          .check(status.in(200, 304))
        )
        exec(http("CD_DefResponse_280_010_profile")
          .get("/data/internal/profile")
          .headers(CivilDamagesHeader.headers_149)
          .check(status.in(200, 304))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
  
    //val defresponsesubmit =
      .group("CD_DefResponse_290_DefRespSubmit") {
        exec(http("CD_DefResponse_290_Submit")
          .post("/data/cases/${caseId}/events")
          .headers(CivilDamagesHeader.headers_1549)
          .body(StringBody("{\"data\":{\"respondent1ClaimResponseType\":\"FULL_DEFENCE\",\"respondent1ClaimResponseDocument\":{\"file\":{\"document_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}\",\"document_binary_url\":\"http://dm-store-aat.service.core-compute-aat.internal/documents/${Document_ID}/binary\",\"document_filename\":\"1MB-c.pdf\"}},\"solicitorReferences\":{\"respondentSolicitor1Reference\":\"def ref\",\"applicantSolicitor1Reference\":\"claim ref\"},\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"individualFirstName\":null,\"individualLastName\":null,\"companyName\":\"defcom\",\"organisationName\":null,\"soleTraderTitle\":null,\"soleTraderFirstName\":null,\"soleTraderLastName\":null,\"soleTraderTradingAs\":null,\"partyName\":\"defcom\",\"partyTypeDisplayValue\":\"Company\",\"primaryAddress\":{\"AddressLine1\":\"22 Hibernia Gardens\",\"AddressLine2\":null,\"AddressLine3\":null,\"PostTown\":\"Hounslow\",\"County\":null,\"PostCode\":\"TW3 3SD\",\"Country\":\"United Kingdom\"}},\"respondent1DQFileDirectionsQuestionnaire\":{\"oneMonthStayRequested\":\"Yes\",\"reactionProtocolCompliedWith\":\"Yes\",\"explainedToClient\":[\"CONFIRM\"]},\"respondent1DQDisclosureOfElectronicDocuments\":{\"reachedAgreement\":\"Yes\"},\"respondent1DQDisclosureOfNonElectronicDocuments\":{\"directionsForDisclosureProposed\":\"No\",\"standardDirectionsRequired\":null,\"bespokeDirections\":null},\"respondent1DQDisclosureReport\":{\"disclosureFormFiledAndServed\":\"No\",\"disclosureProposalAgreed\":\"No\",\"draftOrderNumber\":null},\"respondent1DQExperts\":{\"expertRequired\":\"Yes\",\"expertReportsSent\":\"YES\",\"jointExpertSuitable\":\"No\",\"details\":[{\"value\":{\"name\":\"asasasa\",\"fieldOfExpertise\":\"fgffgfgfg\",\"whyRequired\":\"ggfgfgfgfgfgfgfgfgfg\",\"estimatedCost\":\"2000000\"},\"id\":null}]},\"respondent1DQWitnesses\":{\"witnessesToAppear\":\"No\"},\"respondent1DQLanguage\":{\"evidence\":\"ENGLISH\",\"court\":\"ENGLISH\",\"documents\":\"ENGLISH\"},\"respondent1DQHearing\":{\"hearingLength\":\"ONE_DAY\",\"unavailableDatesRequired\":\"No\"},\"respondent1DQRequestedCourt\":{\"requestHearingAtSpecificCourt\":\"No\"},\"respondent1DQHearingSupport\":{\"signLanguageRequired\":null,\"languageToBeInterpreted\":null,\"otherSupport\":null,\"requirements\":[]},\"respondent1DQFurtherInformation\":{\"futureApplications\":\"No\",\"otherInformationForJudge\":null},\"respondent1DQStatementOfTruth\":{\"name\":\"respname\",\"role\":\"senior solicitor\"}},\"event\":{\"id\":\"DEFENDANT_RESPONSE\",\"summary\":\"\",\"description\":\"\"},\"event_token\":\"${event_token_defresponse}\",\"ignore_warning\":false}"))
          .check(status.in(200, 201))
        )
      }
        .pause(MinThinkTime, MaxThinkTime)
          .pause(50)
  
    //val afterdefresponsebacktocasedetails =
      .group("CD_DefResponse_300_BacktoCaseDetailsAfterDefResponse") {
        exec(http("CD_DefResponse_300_005_DefResponse")
          .post("/workallocation/searchForCompletable")
          .headers(CivilDamagesHeader.headers_1556)
          .body(StringBody("{\"searchRequest\":{\"ccdId\":\"${caseId}\",\"eventId\":\"DEFENDANT_RESPONSE\",\"jurisdiction\":\"CIVIL\",\"caseTypeId\":\"CIVIL\"}}"))
          .check(status.is(401))
        )
        
          .exec(http("CD_DefResponse_300_010_Internal")
            .get("/data/internal/cases/${caseId}")
            .headers(CivilDamagesHeader.headers_717)
            .check(status.in(200, 304))
          )
      }
        .pause(MinThinkTime, MaxThinkTime)
    
  
  //}
}
