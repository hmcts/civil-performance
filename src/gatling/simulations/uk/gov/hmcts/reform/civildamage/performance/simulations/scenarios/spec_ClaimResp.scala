package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils._
import utils.spec_CL1_Headers._

import scala.concurrent.duration.DurationInt

object spec_ClaimResp{

  val BaseURL = Environment.baseURL
  val IdamURL = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

    // ==============================SELECT RESPOND TO DEFENDANT====================
    val ResToDef =
        // ================================OPEN CASE===============================,

        exec( http("Civil_RespToClaim_040_OpenCase")
          .get("/data/internal/cases/#{caseId}")
          .headers(headers_29))

          .pause(5)
          // =============================VIEW & RESPOND=============================,
        .group("Civil_CreateClaim_040_CreateCase") {
          exec( http("C_request_32")
            .get("/workallocation/case/tasks/#{caseId}/event/CLAIMANT_RESPONSE_SPEC/caseType/CIVIL/jurisdiction/CIVIL")
            .headers(headers_32))

          exec( http("C_request_34")
            .get("/data/internal/cases/#{caseId}/event-triggers/CLAIMANT_RESPONSE_SPEC?ignore-warning=false")
            .headers(headers_34)
            .check(jsonPath("$.case_fields[0].value.partyID").saveAs("repPartyID"))
            .check(jsonPath("$.case_fields[0].value.partyName").saveAs("partyName"))
            .check(jsonPath("$..formatted_value.documentLink.document_url").saveAs("document_url"))
            .check(jsonPath("$..formatted_value.documentLink.document_filename").saveAs("document_filename"))
            .check(jsonPath("$..formatted_value.documentSize").saveAs("document_size"))
            .check(jsonPath("$..formatted_value.createdDatetime").saveAs("createDateTime"))
            .check(jsonPath("$.event_token").saveAs("event_token")))
          }
          .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("xsrf_token")))


          .pause(10)

          // ========================DEF RESP FORM======================,
          .group("Civil_CreateClaim_050_CLAIMEligibility") {
          exec(http("Civil_CreateClaim_050_CLAIMEligibility")
              .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECRespondentResponse")
              .headers(Headers.validateHeader)
              .body(ElFileBody("c_RespToDef/0036_request.dat")))
          }
          .pause(10)

          // ==============================CONTINUE==========================,
          .group("Civil_CreateClaim_050_CLAIMEligibility") {
            exec(http("C_request_37")
              .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECApplicantDefenceResponseDocument")
              .headers(Headers.validateHeader)
              .body(ElFileBody("c_RespToDef/0037_request.dat")))
          }

          .pause(10)
          // =============================UESE OF EXPERT===============================,
          .exec( http("C_request_38")
            .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECSmallClaimExperts")
            .headers(Headers.validateHeader)
            .body(ElFileBody("c_RespToDef/0038_request.dat")))

          .pause(10)
          // ========================================WITNESS===================,
          .exec( http("C_request_39")
            .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECSmallClaimWitnesses")
            .headers(Headers.validateHeader)
            .body(ElFileBody("c_RespToDef/0039_request.dat")))
          .pause(10)

          // ===================================LANGUAGE===========================,
          .exec( http("C_request_40")
            .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECLanguage")
            .headers(Headers.validateHeader)
            .body(ElFileBody("c_RespToDef/0040_request.dat")))
          .pause(10)

          // ===========================HEARING==================,
          .exec( http("C_request_41")
            .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECHearing")
            .headers(Headers.validateHeader)
            .body(ElFileBody("c_RespToDef/0041_request.dat")))
          .pause(10)

          // ==================================REQUESTED COURT======================,
          .exec( http("C_request_42")
            .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECApplicantCourtLocationLRspec")
            .headers(Headers.validateHeader)
            .body(ElFileBody("c_RespToDef/0042_request.dat")))
          .pause(10)

          // ==============================SUPPORT WITH ACCESS NEEDS=======================,
          .exec( http("C_request_43")
            .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECHearingSupport")
            .headers(Headers.validateHeader)
            .body(ElFileBody("c_RespToDef/0043_request.dat")))
          .pause(10)

          // =====================================VULNERABILITY=======================,
          .exec( http("C_request_44")
            .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECVulnerabilityQuestions")
            .headers(Headers.validateHeader)
            .body(ElFileBody("c_RespToDef/0044_request.dat")))
          .pause(10)

          // =======================================================SOT===========================,
          .exec( http("C_request_45")
            .post("/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECStatementOfTruth")
            .headers(Headers.validateHeader)
            .body(ElFileBody("c_RespToDef/0045_request.dat")))
          .pause(10)

          // ==========================================SOT SUBMIT======================,
          .exec( http("C_request_46")
            .post("/data/cases/#{caseId}/events")
            .headers(headers_46)
            .body(ElFileBody("c_RespToDef/0046_request.dat")))

}