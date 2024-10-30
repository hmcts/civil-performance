package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils._
import utils.unspec_CreateClaim_Headers._

object unspec_CreateClaim {


  val BaseURL = Environment.baseURL
  val IdamURL = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val CreateUnSpecClaim =

    // =====================create case==================,
    group("CivilDamage_UnSpecClaim_10_01_CreateCase") {
      exec(http("create")
        .get("/aggregated/caseworkers/:uid/jurisdictions?access=create")
        .headers(headers_27))
    }
      .pause(7)
      .group("CivilDamage_UnSpecClaim_10_02_CreateCase") {
        exec(http("IgnoreWarning")
          .get("/data/internal/case-types/CIVIL/event-triggers/CREATE_CLAIM?ignore-warning=false")
          .headers(headers_28)
          .check(jsonPath("$.event_token").optional.saveAs("event_token")))
      }
      .pause(7)
      // ==================COURT PROCEEDINGS==================,
      .group("CivilDamage_UnSpecClaim_10_03_CreateCase") {
        exec(http("CLAIMEligibility")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMEligibility")
          .headers(headers_29)
          .body(ElFileBody("xunspec_CreateClam/0029_request.dat")))
      }
      .pause(7)
      // ==================FLE REF==================,
      .group("CivilDamage_UnSpecClaim_10_04_CreateCase") {
        exec(http("CLAIMReferences")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMReferences")
          .headers(headers_30)
          .body(ElFileBody("xunspec_CreateClam/0030_request.dat")))
      }
      .pause(7)
      // ==================COURT LOC==================,
      .group("CivilDamage_UnSpecClaim_10_05_CreateCase") {
        exec(http("CLAIMCourt")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMCourt")
          .headers(headers_31)
          .body(ElFileBody("xunspec_CreateClam/0031_request.dat")))
      }
      .pause(7)
      // ==================CL ADDR==================,
      .group("CivilDamage_UnSpecClaim_10_06_CreateCase") {
        exec(http("postcode")
          .get("/api/addresses?postcode=HA11GP")
          .headers(headers_32))
      }
      .pause(7)
      // ==================CL DETAILS==================,
      .group("CivilDamage_UnSpecClaim_10_07_CreateCase") {
        exec(http("CLAIMClaimant")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMClaimant")
          .headers(headers_33)
          .body(ElFileBody("xunspec_CreateClam/0033_request.dat")))
      }
      .pause(7)
      // ==================UNDER AGE==================,
      .group("CivilDamage_UnSpecClaim_10_08_CreateCase") {
        exec(http("CREATE_CLAIMClaimantLitigationFriend")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMClaimantLitigationFriend")
          .headers(headers_34)
          .body(ElFileBody("xunspec_CreateClam/0034_request.dat")))
      }
      .pause(7)
      // ==================CL REP EMAIL==================,
      .group("CivilDamage_UnSpecClaim_10_09_CreateCase") {
        exec(http("CREATE_CLAIMNotifications")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMNotifications")
          .headers(headers_35)
          .body(ElFileBody("xunspec_CreateClam/0035_request.dat")))
      }
      .pause(7)
      // ==================CL REP ADDR==================,
      .group("CivilDamage_UnSpecClaim_10_10_CreateCase") {
        exec(http("ClaimantSolicitorOrganisation")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMClaimantSolicitorOrganisation")
          .headers(headers_37)
          .body(ElFileBody("xunspec_CreateClam/0037_request.dat")))
      }
      .pause(7)

      // ==================CL REP CORRESPONDENCE ADDR==================,
      .group("CivilDamage_UnSpecClaim_10_11_CreateCase") {
        exec(http("ClaimantSolicitorAddres")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMClaimantSolicitorServiceAddress")
          .headers(headers_38)
          .body(ElFileBody("xunspec_CreateClam/0038_request.dat")))
      }
      .pause(7)
      // ==================ANOTHER CL==================,
      .group("CivilDamage_UnSpecClaim_10_12_CreateCase") {
        exec(http("AddAnotherClaimant")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMAddAnotherClaimant")
          .headers(headers_39)
          .body(ElFileBody("xunspec_CreateClam/0039_request.dat")))
      }
      .pause(7)
      // ==================DF ADDR==================,
      .group("CivilDamage_UnSpecClaim_10_13_CreateCase") {
        exec(http("postcode")
          .get("/api/addresses?postcode=WD171BN")
          .headers(headers_40))
      }
      .pause(7)
      // ==================DF DETAILS==================,
      .group("CivilDamage_UnSpecClaim_10_14_CreateCase") {
        exec(http("CLAIMDefendant")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMDefendant")
          .headers(headers_41)
          .body(ElFileBody("xunspec_CreateClam/0041_request.dat")))
      }
      .pause(7)
      // ==================DF REP YES==================,
      .group("CivilDamage_UnSpecClaim_10_15_CreateCase") {
        exec(http("LegalRepresentation")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMLegalRepresentation")
          .headers(headers_42)
          .body(ElFileBody("xunspec_CreateClam/0042_request.dat")))
      }
      .pause(7)
      // ==================DF REP ADDR==================,
      .group("CivilDamage_UnSpecClaim_10_16_CreateCase") {
        exec(http("DefendantSolicitorOrganisation")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMDefendantSolicitorOrganisation")
          .headers(headers_43)
          .body(ElFileBody("xunspec_CreateClam/0043_request.dat")))
      }
      .pause(7)
      // ==================DF REP ADDR==================,
      .group("CivilDamage_UnSpecClaim_10_17_CreateCase") {
        exec(http("DefendantSolicitorServiceAddress")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMDefendantSolicitorServiceAddress")
          .headers(headers_44)
          .body(ElFileBody("xunspec_CreateClam/0044_request.dat")))
      }
      .pause(7)

      // ==================DF REP EMAIL==================,
      .group("CivilDamage_UnSpecClaim_10_18_CreateCase") {
        exec(http("CLAIMAddAnotherDefendant")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMDefendantSolicitorEmail")
          .headers(headers_45)
          .body(ElFileBody("xunspec_CreateClam/0045_request.dat")))
      }
      .pause(7)

      // ==================ANTHER DF==================,
      .group("CivilDamage_UnSpecClaim_10_19_CreateCase") {
        exec(http("AddAnotherDefendant")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMAddAnotherDefendant")
          .headers(headers_46)
          .body(ElFileBody("xunspec_CreateClam/0046_request.dat")))
      }
      .pause(7)

      // ==================CLAIM TYPE==================,
      .group("CivilDamage_UnSpecClaim_10_20_CreateCase") {
        exec(http("CLAIMClaimType")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMClaimType")
          .headers(headers_47)
          .body(ElFileBody("xunspec_CreateClam/0047_request.dat")))
      }
      .pause(7)

      // ==================CLAIM DESC==================,
      .group("CivilDamage_UnSpecClaim_10_21_CreateCase") {
        exec(http("CLAIMDetails")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMDetails")
          .headers(headers_48)
          .body(ElFileBody("xunspec_CreateClam/0048_request.dat")))
      }
      .pause(7)

      // ==================UPLOAD PARTICULARS==================,
      .group("CivilDamage_UnSpecClaim_10_22_CreateCase") {
        exec(http("CLAIMuploadParticularsOfClaim")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMuploadParticularsOfClaim")
          .headers(headers_49)
          .body(ElFileBody("xunspec_CreateClam/0049_request.dat")))
      }
      .pause(7)

      // ==================CLAIM AMT==================,
      .group("CivilDamage_UnSpecClaim_10_23_CreateCase") {
        exec(http("CLAIMClaimValue")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMClaimValue")
          .headers(headers_50)
          .body(ElFileBody("xunspec_CreateClam/0050_request.dat"))
          .check(regex("calculatedAmountInPence\":\"(.*?)\"").saveAs("calculatedAmountInPence"))
          .check(regex("statementOfValueInPennies\":\"(.*?)\"").saveAs("statementOfValueInPennies"))
          .check(bodyString.saveAs("responseBody")))
      }
      .pause(7)
      // ==================CLAIM FEE==================,
      .group("CivilDamage_UnSpecClaim_10_24_CreateCase") {
        exec(http("CLAIMPbaNumber")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMPbaNumber")
          .headers(headers_51)
          .body(ElFileBody("xunspec_CreateClam/0051_request.dat")))
      }
      .pause(7)
      // ==================SOT==================,
      .group("CivilDamage_UnSpecClaim_10_25_CreateCase") {
        exec(http("StatementOfTruth")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMStatementOfTruth")
          .headers(headers_52)
          .body(ElFileBody("xunspec_CreateClam/0052_request.dat")))
      }
      .pause(7)

      // ==================SUBMIT SOT==================,
      .group("CivilDamage_UnSpecClaim_10_26_CreateCase") {
        exec(http("Submit_Claim")
        .post("/data/case-types/CIVIL/cases?ignore-warning=false")
        .headers(headers_53)
        .body(ElFileBody("xunspec_CreateClam/0053_request.dat"))
        .check(jsonPath("$.event_token").optional.saveAs("event_token"))
        .check(jsonPath("$.id").optional.saveAs("caseId")))
}
    .pause(27)


}