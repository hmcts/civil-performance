package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils._
import utils.unspec_CreateClaim_Headers._
import utils.Headers
object unspec_CreateClaim {


  val BaseURL = Environment.baseURL
  val IdamURL = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val CreateUnSpecClaim =

    // ========================LANDING PAGE=====================,
    group("CivilDamage_UnSpecClaim_10_00_LandingPage") {
      exec(http("Jurisdictions")
        .get("/aggregated/caseworkers/:uid/jurisdictions?access=read")
        .headers(Headers.commonHeader)
        .check(substring("callback_get_case_url")))

      .exec(http("Organisation")
        .get("/api/organisation")
        .headers(Headers.commonHeader)
        .check(substring("organisationProfileIds")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // =====================CREATE CASE==================,
    .group("CivilDamage_UnSpecClaim_10_01_CreateCase") {
      exec(http("create")
        .get("/aggregated/caseworkers/:uid/jurisdictions?access=create")
        .headers(Headers.commonHeader)
        .check(substring("Create claim - Unspecified")))
    }
    .pause(MinThinkTime, MaxThinkTime)
    .group("CivilDamage_UnSpecClaim_10_02_CreateCase") {
      exec(http("IgnoreWarning")
        .get("/data/internal/case-types/CIVIL/event-triggers/CREATE_CLAIM?ignore-warning=false")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-case-trigger.v2+json;charset=UTF-8")
        .check(substring("Issue civil court proceedings"))
        .check(jsonPath("$.event_token").saveAs("event_token")))
    }
    .pause(MinThinkTime, MaxThinkTime)
    // ==================COURT PROCEEDINGS==================,
    .group("CivilDamage_UnSpecClaim_10_03_CreateCase") {
      exec(http("CLAIMEligibility")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMEligibility")
        .headers(Headers.validateHeader)
        .body(StringBody("""{"data": {"claimStarted": null}, "event": {"description": "", "id": "CREATE_CLAIM",
           |"summary": ""}, "event_data": {"claimStarted": null}, "event_token": "#{event_token}",
           |"ignore_warning": false}""".stripMargin))
        .check(substring("Birmingham Civil and Family Justice Centre")))
    }
    .pause(MinThinkTime, MaxThinkTime)
    // ==================FLE REF==================,
    .group("CivilDamage_UnSpecClaim_10_04_CreateCase") {
      exec(http("CLAIMReferences")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMReferences")
        .headers(Headers.validateHeader)
        .body(StringBody("""{"data": {}, "event": {"id": "CREATE_CLAIM", "summary": "", "description": ""},
            |"event_data": {"claimStarted": "Yes"}, "event_token": "#{event_token}",
            |"ignore_warning": false}""".stripMargin))
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/case-types/CIVIL/validate?" +
          "pageId=CREATE_CLAIMReferences")))
    }
    .pause(MinThinkTime, MaxThinkTime)
    // ==================COURT LOC==================,
    .group("CivilDamage_UnSpecClaim_10_05_CreateCase") {
      exec(http("CLAIMCourt")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMCourt")
        .headers(Headers.validateHeader)
        .body(ElFileBody("xunspec_CreateClam/courtLocation.dat"))
        .check(substring("applicantPreferredCourtLocationList")))
    }
    .pause(MinThinkTime, MaxThinkTime)
    // ==================CL ADDR==================,
    .group("CivilDamage_UnSpecClaim_10_06_CreateCase") {
      exec(http("postcode")
        .get("/api/addresses?postcode=HA11GP")
        .headers(Headers.commonHeader)
        .check(substring("https://api.os.uk/search/places/v1/postcode?postcode=HA11GP")))
    }
    .pause(MinThinkTime, MaxThinkTime)
    // ==================CL DETAILS==================,
    .group("CivilDamage_UnSpecClaim_10_07_CreateCase") {
      exec(http("CLAIMClaimant")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMClaimant")
        .headers(Headers.validateHeader)
        .body(ElFileBody("xunspec_CreateClam/claimantDetails.dat"))
        .check(substring("primaryAddress")))
    }
    .pause(MinThinkTime, MaxThinkTime)
    // ==================UNDER AGE==================,
    .group("CivilDamage_UnSpecClaim_10_08_CreateCase") {
      exec(http("CREATE_CLAIMClaimantLitigationFriend")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMClaimantLitigationFriend")
        .headers(Headers.validateHeader)
        .body(ElFileBody("xunspec_CreateClam/over18.dat"))
        .check(substring("applicant1LitigationFriendRequired")))
    }
    .pause(MinThinkTime, MaxThinkTime)
    // ==================CL REP EMAIL==================,
    .group("CivilDamage_UnSpecClaim_10_09_CreateCase") {
      exec(http("CREATE_CLAIMNotifications")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMNotifications")
        .headers(Headers.validateHeader)
        .body(ElFileBody("xunspec_CreateClam/claimantNotificationDetails.dat"))
        .check(substring("applicantSolicitor1UserDetails")))
    }
    .pause(MinThinkTime, MaxThinkTime)
    // ==================CL REP ADDR==================,
    .group("CivilDamage_UnSpecClaim_10_10_CreateCase") {
      exec(http("ClaimantSolicitorOrganisation")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMClaimantSolicitorOrganisation")
        .headers(Headers.validateHeader)
        .body(ElFileBody("xunspec_CreateClam/claimantSolicitorOrganisation.dat"))
        .check(substring("PrepopulateToUsersOrganisation")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==================CL REP CORRESPONDENCE ADDR==================,
    .group("CivilDamage_UnSpecClaim_10_11_CreateCase") {
      exec(http("ClaimantSolicitorAddres")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMClaimantSolicitorServiceAddress")
        .headers(Headers.validateHeader)
        .body(ElFileBody("xunspec_CreateClam/claimantServiceAddress.dat"))
        .check(substring("applicantSolicitor1ServiceAddressRequired")))
    }
    .pause(MinThinkTime, MaxThinkTime)
    // ==================ANOTHER CL==================,
    .group("CivilDamage_UnSpecClaim_10_12_CreateCase") {
      exec(http("AddAnotherClaimant")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMAddAnotherClaimant")
        .headers(Headers.validateHeader)
        .body(ElFileBody("xunspec_CreateClam/addAnotherClaimant.dat"))
        .check(substring("addApplicant2")))
    }
    .pause(MinThinkTime, MaxThinkTime)
    // ==================DF ADDR==================,
    .group("CivilDamage_UnSpecClaim_10_13_CreateCase") {
      exec(http("postcode")
        .get("/api/addresses?postcode=WD171BN")
        .headers(Headers.commonHeader)
        .check(substring("https://api.os.uk/search/places/v1/postcode?postcode=WD171BN")))
    }
    .pause(MinThinkTime, MaxThinkTime)
    // ==================DF DETAILS==================,
    .group("CivilDamage_UnSpecClaim_10_14_CreateCase") {
      exec(http("CLAIMDefendant")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMDefendant")
        .headers(Headers.validateHeader)
        .body(ElFileBody("xunspec_CreateClam/defendantDetails.dat"))
        .check(substring("DEFENDANT")))
    }
    .pause(MinThinkTime, MaxThinkTime)
    // ==================DF REP YES==================,
    .group("CivilDamage_UnSpecClaim_10_15_CreateCase") {
      exec(http("LegalRepresentation")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMLegalRepresentation")
        .headers(Headers.validateHeader)
        .body(ElFileBody("xunspec_CreateClam/defendantRepresented.dat"))
        .check(substring("respondent1Represented")))
    }
    .pause(MinThinkTime, MaxThinkTime)
    // ==================DF REP ADDR==================,
    .group("CivilDamage_UnSpecClaim_10_16_CreateCase") {
      exec(http("DefendantSolicitorOrganisation")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMDefendantSolicitorOrganisation")
        .headers(Headers.validateHeader)
        .body(ElFileBody("xunspec_CreateClam/defendantSolicitorOrganisation.dat"))
        .check(substring("respondent1OrganisationPolicy")))
    }
    .pause(MinThinkTime, MaxThinkTime)
    // ==================DF REP ADDR==================,
    .group("CivilDamage_UnSpecClaim_10_17_CreateCase") {
      exec(http("DefendantSolicitorServiceAddress")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMDefendantSolicitorServiceAddress")
        .headers(Headers.validateHeader)
        .body(ElFileBody("xunspec_CreateClam/defendantServiceAddress.dat"))
        .check(substring("respondentSolicitor1ServiceAddressRequired")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==================DF REP EMAIL==================,
    .group("CivilDamage_UnSpecClaim_10_18_CreateCase") {
      exec(http("CLAIMAddAnotherDefendant")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMDefendantSolicitorEmail")
        .headers(Headers.validateHeader)
        .body(ElFileBody("xunspec_CreateClam/defendantNotificationDetails.dat"))
        .check(substring("respondentSolicitor1EmailAddress")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==================ANOTHER DF==================,
    .group("CivilDamage_UnSpecClaim_10_19_CreateCase") {
      exec(http("AddAnotherDefendant")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMAddAnotherDefendant")
        .headers(Headers.validateHeader)
        .body(ElFileBody("xunspec_CreateClam/addAnotherDefendant.dat"))
        .check(substring("addRespondent2")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==================CLAIM TYPE==================,
    .group("CivilDamage_UnSpecClaim_10_20_CreateCase") {
      exec(http("CLAIMClaimType")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMClaimType")
        .headers(Headers.validateHeader)
        .body(ElFileBody("xunspec_CreateClam/claimType.dat"))
        .check(substring("claimTypeUnSpec")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==================CLAIM DESC==================,
    .group("CivilDamage_UnSpecClaim_10_21_CreateCase") {
      exec(http("CLAIMDetails")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMDetails")
        .headers(Headers.validateHeader)
        .body(ElFileBody("xunspec_CreateClam/claimDescription.dat"))
        .check(substring("detailsOfClaim")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==================UPLOAD PARTICULARS==================,
    .group("CivilDamage_UnSpecClaim_10_22_CreateCase") {
      exec(http("CLAIMUploadParticularsOfClaim")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMUploadParticularsOfClaim")
        .headers(Headers.validateHeader)
        .body(ElFileBody("xunspec_CreateClam/claimParticulars.dat"))
        .check(substring("uploadParticularsOfClaim")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==================CLAIM AMT==================,
    .group("CivilDamage_UnSpecClaim_10_23_CreateCase") {
      exec(http("CLAIMClaimValue")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMClaimValue")
        .headers(Headers.validateHeader)
        .body(ElFileBody("xunspec_CreateClam/claimAmount.dat"))
        .check(regex("calculatedAmountInPence\":\"(.*?)\"").saveAs("calculatedAmountInPence"))
        .check(regex("statementOfValueInPennies\":\"(.*?)\"").saveAs("statementOfValueInPennies")))
    }
    .pause(MinThinkTime, MaxThinkTime)
    // ==================CLAIM FEE==================,
    .group("CivilDamage_UnSpecClaim_10_24_CreateCase") {
      exec(http("CLAIMPbaNumber")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMPbaNumber")
        .headers(Headers.validateHeader)
        .body(ElFileBody("xunspec_CreateClam/claimFee.dat"))
        .check(substring("calculatedAmountInPence")))
    }
    .pause(MinThinkTime, MaxThinkTime)
    // ==================SOT==================,
    .group("CivilDamage_UnSpecClaim_10_25_CreateCase") {
      exec(http("StatementOfTruth")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMStatementOfTruth")
        .headers(Headers.validateHeader)
        .body(ElFileBody("xunspec_CreateClam/statementOfTruth.dat"))
        .check(substring("StatementOfTruth")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==================SUBMIT SOT==================,
    .group("CivilDamage_UnSpecClaim_10_26_CreateCase") {
      exec(http("Submit_Claim")
        .post("/data/case-types/CIVIL/cases?ignore-warning=false")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-case.v2+json;charset=UTF-8")
        .body(ElFileBody("xunspec_CreateClam/submitClaimFinal.dat"))
        .check(substring("CALLBACK_COMPLETED"))
        .check(jsonPath("$.id").saveAs("caseId")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    .group("CivilDamage_UnSpecClaim_10_27_CaseCreated") {
      exec(http("CaseCreated")
        .get("/data/internal/cases/#{caseId}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{caseId}")))
    }

    // ==================API ROLE ASSIGNMENT==================,
    .group("CivilDamage_UnSpecClaim_10_28_APIRoleAssignment") {
      exec(http("APIRoleAssignment")
        .post("/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
        .headers(Headers.commonHeader)
        .check(status.is(204)))
    }

    .group("CivilDamage_UnspecClaim_10_29_SupportedJurisdiction") {
      exec(http("SupportedJurisdiction")
        .get("/api/wa-supported-jurisdiction/get")
        .headers(Headers.commonHeader)
        .check(substring("CIVIL")))
    }
    .pause(10)

}