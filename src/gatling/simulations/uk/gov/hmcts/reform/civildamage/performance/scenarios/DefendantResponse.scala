
package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, Environment, CsrfCheck}

object DefendantResponse {
  
  val BaseURL = Environment.baseURL
  val IdAMURL = Environment.idamURL
  val CivilUiURL = "https://civil-citizen-ui.perftest.platform.hmcts.net"
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



  val Defendant =

//Navigate to Money Claims page - Enter claim number

  group("CD_DefResponse_320_ReferencePage") {
    exec(http("CD_DefResponse-320_005_ReferencePage")
      .get(CivilUiURL + "/first-contact/claim-reference")
      .headers(CivilDamagesHeader.MoneyClaimNavHeader)
      .check(CsrfCheck.save)
      .check(status.in(200, 304))
      .check(substring("Enter your claim number"))
    )
  }
      .pause(MinThinkTime, MaxThinkTime)


//Enter claim Reference
      .group("CD_DefResponse_330_ClaimReference ") {
        exec(http("CD_DefResponse_330_005_ClaimReference")
          .post(CivilUiURL + "/first-contact/claim-reference")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .formParam("_csrf", "${csrf}")
          .formParam("reference", "${reference}")
          .check(status.in(200, 304))
          .check(substring("Enter security code"))
        )
      }
          .pause(MinThinkTime, MaxThinkTime)


          //Enter security code
          .group("CD_DefResponse_340_SecurityCode") {
            exec(http("CD_DefResponse_340_005_SignIn")
              .post(CivilUiURL + "/first-contact/pin")
              .headers(CivilDamagesHeader.MoneyClaimPostHeader)
              .formParam("_csrf", "${csrf}")
              .formParam("pin", "W4WX3TEBVL26")
              .check(status.in(200, 304))
              .check(substring("Claim details"))
            )
          }
              .pause(MinThinkTime, MaxThinkTime)


              //Respond to Claim
              .group("CD_DefResponse_350_RespondClaim") {
                exec(http("CD_DefResponse_350_005_RespondClaim")
                  .post(CivilUiURL + "/assignclaim?id=#{caseId}")
                  .headers(CivilDamagesHeader.MoneyClaimPostHeader)
                  .formParam("id", "#{caseId}")
                  .check(status.in(200, 304))
                  .check(substring("Sign in or create an account"))
                )
              }
                  .pause(MinThinkTime, MaxThinkTime)

//sign in
    .group("CD_DefResponse_360_SignIn ") {
      exec(http("CD_DefResponse_360_005_SignIn")
        .post(CivilUiURL + "/login?client_id=civil_citizen_ui&response_type=code&redirect_uri=https://civil-citizen-ui.perftest.platform.hmcts.net/oauth2/callback&scope=profile%20openid%20roles%20manage-user%20create-user%20search-user")
        .headers(CivilDamagesHeader.MoneyClaimSignInHeader)
        .formParam("username", "sampankumar@hmcts.net")
        .formParam("password", "Password12!")
        .formParam("selfRegistrationEnabled", "true")
        .formParam("_csrf", "${csrf}")
        .check(status.in(200, 304))
        .check(substring("Your money claims account"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
                 * Civil UI Claim - Resume Claim
      ==========================================================================================*/

    .group("CD_DefResponse_370_ResumeClaim") {
      exec(http("CD_DefResponse_370_005_ResumeClaim")
        .get(CivilUiURL + "/dashboard/#{caseId}/defendant")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("About claim"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
               * Civil UI Claim - Respond to Claim
    ==========================================================================================*/

    .group("CD_DefResponse_380_RespondToClaim") {
      exec(http("CD_DefResponse_380_005_RespondToClaim")
        .get(CivilUiURL + "/case/#{caseId}/response/bilingual-language-preference")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Do you want to respond to this claim in Welsh?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
             * Civil UI Claim - Do you want to respond to this claim in Welsh?
  ==========================================================================================*/

    .group("CD_DefResponse_390_InWelsh") {
      exec(http("CD_DefResponse_390_005_InWelsh")
        .post(CivilUiURL + "/case/#{caseId}/response/bilingual-language-preference")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "ENGLISH")
        .check(status.in(200, 304))
        .check(substring("Respond to a money claim"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
           * Civil UI Claim - Confirm Your Details Redirect
==========================================================================================*/

    .group("CD_DefResponse_400_ConfirmDetails") {
      exec(http("CD_DefResponse_400_005_ConfirmDetails")
        .get(CivilUiURL + "/case/#{caseId}/response/your-details")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Confirm your details"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
         * Civil UI Claim - Confirm Your Details Confirm
==========================================================================================*/

    .group("CD_DefResponse_410_ConfirmDetailsConfirm") {
      exec(http("CD_DefResponse_410_005_ConfirmDetailsConfirm")
        .post(CivilUiURL + "/case/#{caseId}/response/your-details")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("individualTitle", "mr")
        .formParam("individualFirstName", "#{randomString}First")
        .formParam("individualLastName", "#{randomString}Last")
        .formParam("addressLine1", "#{randomString}Address1")
        .formParam("addressLine2", "")
        .formParam("addressLine3", "")
        .formParam("city", "#{randomString}Town")
        .formParam("postCode", "#{postcode}")
        .formParam("postToThisAddress", "no")
        .formParam("correspondenceAddressPostcode", "")
        .formParam("addressList", "")
        .formParam("addressLine1", "")
        .formParam("addressLine2", "")
        .formParam("addressLine3", "")
        .formParam("city", "")
        .formParam("postCode", "")
        .check(status.in(200, 304))
        .check(substring("Enter your date of birth"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
         * Civil UI Claim - Enter your date of birth
==========================================================================================*/

    .group("CD_DefResponse_420_DateOfBirth") {
      exec(http("CD_DefResponse_420_005_DateOfBirth")
        .post(CivilUiURL + "/case/1691572505460279/response/your-dob")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("day", "#{birthDay}")
        .formParam("month", "#{birthMonth}")
        .formParam("year", "#{birthYear}")
        .check(status.in(200, 304))
        .check(substring("Enter a phone number (optional)"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
       * Civil UI Claim - Enter a phone number
==========================================================================================*/

    .group("CD_DefResponse_430_PhoneNumber") {
      exec(http("CD_DefResponse_430_005_PhoneNumber")
        .post(CivilUiURL + "/case/1691572505460279/response/your-phone")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("telephoneNumber", "01234567890")
        .check(status.in(200, 304))
        .check(regex("""Confirm your details</a>
                       |                    </span>
                       |                    <strong class="govuk-tag app-task-list__tag ">
                       |                      COMPLETE""".stripMargin))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
         * Civil UI Claim - View your options before response deadline
==========================================================================================*/

    .group("CD_DefResponse_440_ViewOptions") {
      exec(http("CD_DefResponse_440_005_ViewOptions")
        .get(CivilUiURL + "/case/#{caseId}/response/understanding-your-options")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Requesting extra time"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
       * Civil UI Claim - Requesting extra time
==========================================================================================*/

    .group("CD_DefResponse_450_ExtraTime") {
      exec(http("CD_DefResponse_450_005_ExtraTime")
        .get(CivilUiURL + "/case/1691572505460279/response/response-deadline-options")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Response deadline"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
     * Civil UI Claim - Response deadline - no
==========================================================================================*/

    .group("CD_DefResponse_460_ResponseDeadline") {
      exec(http("CD_DefResponse_460_005_ResponseDeadline")
        .post(CivilUiURL + "/case/1691572505460279/response/response-deadline-options")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "no")
        .check(status.in(200, 304))
        .check(regex("""View your options before response deadline</a>
                       |                    </span>
                       |                    <strong class="govuk-tag app-task-list__tag ">
                       |                      COMPLETE""".stripMargin))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
         * Civil UI Claim - Choose A Response Redirect
==========================================================================================*/

    .group("CD_DefResponse_470_ChooseAResponse") {
      exec(http("CD_DefResponse_470_005_ChooseAResponse")
        .get(CivilUiURL + "/case/1691572505460279/response/response-type")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("How do you respond to the claim?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
     * Civil UI Claim - How do you respond to the claim? - I admit part of the claim
==========================================================================================*/

    .group("CD_DefResponse_480_ExtraTime") {
      exec(http("CD_DefResponse_480_005_ExtraTime")
        .post(CivilUiURL + "/case/1691572505460279/response/response-type")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("responseType", "PART_ADMISSION")
        .check(status.in(200, 304))
        .check(substring("Have you paid the claimant the amount you admit you owe?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
     * Civil UI Claim - Have you paid the claimant the amount you admit you owe? - no
==========================================================================================*/

    .group("CD_DefResponse_490_HaveYouPaid") {
      exec(http("CD_DefResponse_490_005_HaveYouPaid")
        .post(CivilUiURL + "/case/1691572505460279/response/partial-admission/already-paid")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "no")
        .check(status.in(200, 304))
        .check(regex("""Choose a response</a>
                       |                    </span>
                       |                    <strong class="govuk-tag app-task-list__tag ">
                       |                      COMPLETE""".stripMargin))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
         * Civil UI Claim - How much money do you admit you owe? - redirect
==========================================================================================*/

    .group("CD_DefResponse_500_ConfirmDetails") {
      exec(http("CD_DefResponse_500_005_ConfirmDetails")
        .get(CivilUiURL + "/case/1691572505460279/response/partial-admission/how-much-do-you-owe")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Confirm your details"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
       * Civil UI Claim - How much money do you admit you owe?
==========================================================================================*/

    .group("CD_DefResponse_510_MoneyOwed") {
      exec(http("CD_DefResponse_510_005_MoneyOwed")
        .post(CivilUiURL + "/case/1691572505460279/response/partial-admission/how-much-do-you-owe")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("amount", "#{birthDay}")
        .check(regex("""How much money do you admit you owe?</a>
                       |                    </span>
                       |                    <strong class="govuk-tag app-task-list__tag ">
                       |                      COMPLETE""".stripMargin))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
                //}


    /*======================================================================================
       * Civil UI Claim - Why do you disagree with the amount claimed? - redirect
==========================================================================================*/

    .group("CD_DefResponse_520_WhyDisagree") {
      exec(http("CD_DefResponse_520_005_WhyDisagree")
        .get(CivilUiURL + "case/1691572505460279/response/partial-admission/why-do-you-disagree")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("This includes the claim fee and any interest."))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
     * Civil UI Claim - Why do you disagree with the claim amount?
==========================================================================================*/

    .group("CD_DefResponse_530_WhyDisagreeExplain") {
      exec(http("CD_DefResponse_530_005_WhyDisagreeExplain")
        .post(CivilUiURL + "/case/1691572505460279/response/partial-admission/why-do-you-disagree")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("text", "#{randomString}Explanation")
        .check(substring("Add your timeline of events"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)




    /*======================================================================================
   * Civil UI Claim - Add your timeline of events
==========================================================================================*/

    .group("CD_DefResponse_540_TimelineOfEvents") {
      exec(http("CD_DefResponse_540_005_TimelineOfEvents")
        .post(CivilUiURL + "/case/1691572505460279/response/timeline")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("rows[0][day]", "#{birthDay}")
        .formParam("rows[0][month]", "#{birthMonth}")
        .formParam("rows[0][year]", "#{birthYear}")
        .formParam("rows[0][description]", "#{randomString}description0")
        .formParam("rows[1][day]", "#{birthDay}")
        .formParam("rows[1][month]", "#{birthMonth}")
        .formParam("rows[1][year]", "#{birthYear}")
        .formParam("rows[1][description]", "#{randomString}description1")
        .formParam("rows[2][day]", "#{birthDay}")
        .formParam("rows[2][month]", "#{birthMonth}")
        .formParam("rows[2][year]", "#{birthYear}")
        .formParam("rows[2][description]", "#{randomString}description2")
        .formParam("rows[3][day]", "#{birthDay}")
        .formParam("rows[3][month]", "#{birthMonth}")
        .formParam("rows[3][year]", "#{birthYear}")
        .formParam("rows[3][description]", "#{randomString}description3") //might need to change so all diff
        .formParam("comment]", "#{randomString}comment")
        .check(substring("List your evidence"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
* Civil UI Claim - List your evidence
==========================================================================================*/


.group("CD_DefResponse_550_ListEvidence") {
  exec(http("CD_DefResponse_550_005_ListEvidence")
  .post(CivilUiURL + "/case/1691572505460279/response/evidence")
  .headers(CivilDamagesHeader.MoneyClaimPostHeader)
  .formParam("_csrf", "#{csrf}")
  .formParam("evidenceItem[0][type]", "Expert witness")
  .formParam("evidenceItem[0][description]]", "#{randomString}timelineDescription Expert Witness")
  .formParam("evidenceItem[1][type]", "Photo evidence")
  .formParam("evidenceItem[1][description]]", "#{randomString}timelineDescription Photo Evidence")
  .formParam("evidenceItem[2][type]", "Contracts and agreements")
  .formParam("evidenceItem[2][description]]", "#{randomString}timelineDescription Contracts and Agreements")
  .formParam("evidenceItem[3][type]", "Statements of account")
  .formParam("evidenceItem[3][description]]", "#{randomString}timelineDescription Statements of Account")
  .formParam("comment", "#{randomString}comment")
    .check(regex("""Why do you disagree with the amount claimed?</a>
                   |                    </span>
                   |                    <strong class="govuk-tag app-task-list__tag ">
                   |                      COMPLETE""".stripMargin))
  )
}
  .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
       * Civil UI Claim - When will you pay? Redirect
==========================================================================================*/

    .group("CD_DefResponse_560_WhenPay") {
      exec(http("CD_DefResponse_560_005_WhenPay")
        .get(CivilUiURL + "/case/1691572505460279/response/partial-admission/payment-option")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("When do you want to pay the"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
   * Civil UI Claim - When do you want to pay the?
==========================================================================================*/

    .group("CD_DefResponse_570_PayDate") {
      exec(http("CD_DefResponse_570_005_PayDate")
        .post(CivilUiURL + "/case/1691572505460279/response/partial-admission/payment-option")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("paymentType", "IMMEDIATELY")
        .check(regex("""When will you pay the £10.00?</a>
                       |                    </span>
                       |                    <strong class="govuk-tag app-task-list__tag ">
                       |                      COMPLETE""".stripMargin))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
       * Civil UI Claim - Free telephone mediation Redirect
==========================================================================================*/

    .group("CD_DefResponse_580_FreeTelephone") {
      exec(http("CD_DefResponse_580_005_FreeTelephone")
        .get(CivilUiURL + "/case/1691572505460279/mediation/free-telephone-mediation")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Free telephone mediation"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
     * Civil UI Claim - Free telephone mediation
==========================================================================================*/

    .group("CD_DefResponse_590_FreeTelephoneMed") {
      exec(http("CD_DefResponse_590_005_FreeTelephoneMed")
        .get(CivilUiURL + "/case/1691572505460279/mediation/can-we-use")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Confirm your telephone number"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
 * Civil UI Claim - Confirm your telephone number - yes
==========================================================================================*/

    .group("CD_DefResponse_600_ConfirmNumber") {
      exec(http("CD_DefResponse_600_005_ConfirmNumber")
        .post(CivilUiURL + "/case/1691572505460279/mediation/can-we-use")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "yes")
        .formParam("mediationPhoneNumber", "")
        .check(regex("""Free telephone mediation</a>
                       |                    </span>
                       |                    <strong class="govuk-tag app-task-list__tag ">
                       |                      COMPLETE""".stripMargin))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
     * Civil UI Claim - Give us details in case there's a hearing Redirect
==========================================================================================*/

    .group("CD_DefResponse_610_GiveDetails") {
      exec(http("CD_DefResponse_610_005_GiveDetails")
        .get(CivilUiURL + "/case/1691572505460279/directions-questionnaire/determination-without-hearing")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Determination without Hearing Questions"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
     * Civil UI Claim - Determination without Hearing Questions
==========================================================================================*/

    .group("CD_DefResponse_620_Determination") {
      exec(http("CD_DefResponse_620_005_Determination")
        .post(CivilUiURL + "/case/1691572505460279/directions-questionnaire/determination-without-hearing")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "yes")
        .formParam("reasonForHearing", "")
        .check(substring("Using an expert"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
   * Civil UI Claim - Using an expert
==========================================================================================*/

    .group("CD_DefResponse_630_UsingExpert") {
      exec(http("CD_DefResponse_630_005_UsingExpert")
        .post(CivilUiURL + "/case/1691572505460279/directions-questionnaire/expert")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .check(substring("Do you want to give evidence yourself?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
 * Civil UI Claim - Do you want to give evidence yourself? - yes
==========================================================================================*/

    .group("CD_DefResponse_640_GiveEvidence") {
      exec(http("CD_DefResponse_640_005_GiveEvidence")
        .post(CivilUiURL + "/case/1691572505460279/directions-questionnaire/give-evidence-yourself")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "yes")
        .check(substring("Do you have other witnesses?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
* Civil UI Claim - Do you have other witnesses? - no
==========================================================================================*/

    .group("CD_DefResponse_650_OtherWitnesses") {
      exec(http("CD_DefResponse_650_005_OtherWitnesses")
        .post(CivilUiURL + "/case/1691572505460279/directions-questionnaire/defendant-witnesses")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("witnessItems[0][firstName]", "")
        .formParam("witnessItems[0][lastName]", "")
        .formParam("witnessItems[0][email]", "")
        .formParam("witnessItems[0][telephone]", "")
        .formParam("witnessItems[0][details]", "")
        .formParam("option", "yes")
        .check(substring("Are there any dates in the next 12 months when you, your experts or witnesses cannot go to a hearing?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
* Civil UI Claim - Are there any dates in the next 12 months when you, your experts or witnesses cannot go to a hearing? - no
==========================================================================================*/

    .group("CD_DefResponse_660_AnyDates") {
      exec(http("CD_DefResponse_660_005_AnyDates")
        .post(CivilUiURL + "/case/1691572505460279/directions-questionnaire/cant-attend-hearing-in-next-12-months")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "no")
        .check(substring("Do you want to ask for a telephone or video hearing?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
* Civil UI Claim - Do you want to ask for a telephone or video hearing? - yes
==========================================================================================*/

    .group("CD_DefResponse_670_AskForTelephone") {
      exec(http("CD_DefResponse_670_005_AskForTelephone")
        .post(CivilUiURL + "/case/1691572505460279/directions-questionnaire/phone-or-video-hearing")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "yes")
        .formParam("details", "#{randomString}details")
        .check(substring("Are you, your experts or witnesses vulnerable in a way that the court needs to consider?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
* Civil UI Claim - Are you, your experts or witnesses vulnerable in a way that the court needs to consider? - no
==========================================================================================*/

    .group("CD_DefResponse_680_Vulnerable") {
      exec(http("CD_DefResponse_680_005_Vulnerable")
        .post(CivilUiURL + "/case/1691572505460279/directions-questionnaire/vulnerability")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("vulnerabilityDetails", "")
        .formParam("option", "no")
        .check(substring("Do you, your experts or witnesses need support to attend a hearing?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
* Civil UI Claim - Do you, your experts or witnesses need support to attend a hearing? - no
==========================================================================================*/

    .group("CD_DefResponse_690_NeedSupport") {
      exec(http("CD_DefResponse_690_005_NeedSupport")
        .post(CivilUiURL + "/case/1691572505460279/directions-questionnaire/support-required")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("model[items][0][fullName]", "")
        .formParam("model[items][0][signLanguageInterpreter][content]", "")
        .formParam("model[items][0][languageInterpreter][content]", "")
        .formParam("model[items][0][otherSupport][content]", "")
        .formParam("option", "no")
        .check(substring("Do you want to ask for the hearing to be held at a specific court?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
* Civil UI Claim - Do you want to ask for the hearing to be held at a specific court? - no
==========================================================================================*/

    .group("CD_DefResponse_700_SpecifcCourt") {
      exec(http("CD_DefResponse_700_005_SpecifcCourt")
        .post(CivilUiURL + "/case/1691572505460279/directions-questionnaire/court-location")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("courtLocation", "")
        .formParam("reason", "")
        .formParam("option", "no")
        .check(substring("Welsh language"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
* Civil UI Claim - Welsh language
==========================================================================================*/

    .group("CD_DefResponse_710_WelshLanguage") {
      exec(http("CD_DefResponse_710_005_WelshLanguage")
        .post(CivilUiURL + "/case/1691572505460279/directions-questionnaire/welsh-language")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("speakLanguage", "en")
        .formParam("documentsLanguage", "en")
        .check(regex("""Give us details in case there&#39;s a hearing</a>
                       |                    </span>
                       |                    <strong class="govuk-tag app-task-list__tag ">
                       |                      COMPLETE""".stripMargin))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
   * Civil UI Claim - Check and submit your response Redirect
==========================================================================================*/

    .group("CD_DefResponse_720_CheckAndSubmit") {
      exec(http("CD_DefResponse_720_005_CheckAndSubmit")
        .get(CivilUiURL + "/case/1691572505460279/response/check-and-send")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Your answers will help us check we are treating people fairly and equally"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
* Civil UI Claim - Check your answers
==========================================================================================*/

    .group("CD_DefResponse_730_CheckYourAnswers") {
      exec(http("CD_DefResponse_730_005_CheckYourAnswers")
        .post(CivilUiURL + "/case/1691572505460279/response/check-and-send")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("type", "basic")
        .formParam("isFullAmountRejected", "true")
        .formParam("signed", "true")
        .formParam("directionsQuestionnaireSigned", "true")
        .check(substring("You&#39;ve submitted your response"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
}