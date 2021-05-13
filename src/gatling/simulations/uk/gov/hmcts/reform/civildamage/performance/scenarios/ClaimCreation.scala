
package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, Environment}

object ClaimCreation {
  
  val BaseURL = Environment.baseURL
  val IdAMURL = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime
  
  
  val createclaim =
    group("CD_CreateClaim_030_CreateCase") {
      exec(http("CD_CreateClaim_030_CreateCase")
        .get("/aggregated/caseworkers/:uid/jurisdictions?access=create")
        .headers(CivilDamagesHeader.headers_104)
        .check(status.in(200, 304))
      ).exitHereIfFailed
    }
      .pause(MinThinkTime, MaxThinkTime)
  val startCreateClaim =
    group("CD_CreateClaim_040_StartCreateCase1") {
      exec(http("CD_CreateClaim_040_StartCreateCase1")
				.get("/data/internal/case-types/UNSPECIFIED_CLAIMS/event-triggers/CREATE_CLAIM?ignore-warning=false")
        .headers(CivilDamagesHeader.headers_140)
        .check(status.is(200))
        .check(jsonPath("$.event_token").optional.saveAs("event_token"))
      )
        .pause(MinThinkTime, MaxThinkTime)
    }
    
  val claimliability =
    group("CD_CreateClaim_050_CLAIMEligibility") {
      exec(http("CD_CreateClaim_050_Eligibility")
        .post("/data/case-types/UNSPECIFIED_CLAIMS/validate?pageId=CREATE_CLAIMEligibility")
        .headers(CivilDamagesHeader.headers_163)
        .body(StringBody("{\"data\":{},\"event\":{\"id\":\"CREATE_CLAIM\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{},\"event_token\":\"${event_token}\",\"ignore_warning\":false}"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  
  val claimreferences =
    group("CD_CreateClaim_060_CLAIMReferences") {
      exec(http("CD_CreateClaim_060_Reference")
        .post("/data/case-types/UNSPECIFIED_CLAIMS/validate?pageId=CREATE_CLAIMReferences")
        .headers(CivilDamagesHeader.headers_192)
        .body(StringBody("{\"data\":{\"solicitorReferences\":{\"applicantSolicitor1Reference\":\"claim ref\",\"respondentSolicitor1Reference\":\"def ref\"}},\"event\":{\"id\":\"CREATE_CLAIM\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"solicitorReferences\":{\"applicantSolicitor1Reference\":\"claim ref\",\"respondentSolicitor1Reference\":\"def ref\"}},\"event_token\":\"${event_token}\",\"ignore_warning\":false}"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  
  val claimcourt =
    group("CD_CreateClaim_070_CLAIMCourt") {
      exec(http("CD_CreateClaim_070_Court")
        .post("/data/case-types/UNSPECIFIED_CLAIMS/validate?pageId=CREATE_CLAIMCourt")
        .headers(CivilDamagesHeader.headers_213)
        .body(StringBody("{\"data\":{\"courtLocation\":{\"applicantPreferredCourt\":\"152\"}},\"event\":{\"id\":\"CREATE_CLAIM\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"solicitorReferences\":{\"applicantSolicitor1Reference\":\"claim ref\",\"respondentSolicitor1Reference\":\"def ref\"},\"courtLocation\":{\"applicantPreferredCourt\":\"152\"}},\"event_token\":\"${event_token}\",\"ignore_warning\":false}"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  val postcode =
    group("CD_CreateClaim_080_ClaimantPostcode") {
      exec(http("CD_CreateClaim_080_Postcode")
        .get("/api/addresses?postcode=TW33SD")
        .headers(CivilDamagesHeader.headers_104)
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  val createclaimclaimant =
    group("CD_CreateClaim_090_CLAIMClaimant") {
      exec(http("CD_CreateClaim_090_Claimant")
        .post("/data/case-types/UNSPECIFIED_CLAIMS/validate?pageId=CREATE_CLAIMClaimant")
        .headers(CivilDamagesHeader.headers_258)
        .body(StringBody("{\"data\":{\"applicant1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"companyName\":\"baccom\",\"soleTraderTitle\":null,\"partyName\":null,\"partyTypeDisplayValue\":null,\"primaryAddress\":{\"AddressLine1\":\"10 Hibernia Gardens\",\"AddressLine2\":\"\",\"AddressLine3\":\"\",\"PostTown\":\"Hounslow\",\"County\":\"\",\"Country\":\"United Kingdom\",\"PostCode\":\"TW3 3SD\"}}},\"event\":{\"id\":\"CREATE_CLAIM\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"solicitorReferences\":{\"applicantSolicitor1Reference\":\"claim ref\",\"respondentSolicitor1Reference\":\"def ref\"},\"courtLocation\":{\"applicantPreferredCourt\":\"152\"},\"applicant1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"companyName\":\"baccom\",\"soleTraderTitle\":null,\"partyName\":null,\"partyTypeDisplayValue\":null,\"primaryAddress\":{\"AddressLine1\":\"10 Hibernia Gardens\",\"AddressLine2\":\"\",\"AddressLine3\":\"\",\"PostTown\":\"Hounslow\",\"County\":\"\",\"Country\":\"United Kingdom\",\"PostCode\":\"TW3 3SD\"}}},\"event_token\":\"${event_token}\",\"ignore_warning\":false}"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  
  val createclaimlitigantfriend =
    group("CD_CreateClaim_100_CLAIMClaimantLitigationFriend") {
      exec(http("CD_CreateClaim_100_Litigation")
        .post("/data/case-types/UNSPECIFIED_CLAIMS/validate?pageId=CREATE_CLAIMClaimantLitigationFriend")
        .headers(CivilDamagesHeader.headers_273)
        .body(StringBody("{\"data\":{\"applicant1LitigationFriendRequired\":\"No\"},\"event\":{\"id\":\"CREATE_CLAIM\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"solicitorReferences\":{\"applicantSolicitor1Reference\":\"claim ref\",\"respondentSolicitor1Reference\":\"def ref\"},\"courtLocation\":{\"applicantPreferredCourt\":\"152\"},\"applicant1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"companyName\":\"baccom\",\"soleTraderTitle\":null,\"partyName\":null,\"partyTypeDisplayValue\":null,\"primaryAddress\":{\"AddressLine1\":\"10 Hibernia Gardens\",\"AddressLine2\":\"\",\"AddressLine3\":\"\",\"PostTown\":\"Hounslow\",\"County\":\"\",\"Country\":\"United Kingdom\",\"PostCode\":\"TW3 3SD\"}},\"applicant1LitigationFriendRequired\":\"No\"},\"event_token\":\"${event_token}\",\"ignore_warning\":false}"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  
  val createclaimantnotifications =
    group("CD_CreateClaim_110_CLAIMNotifications") {
      exec(http("CD_CreateClaim_110_Notifications")
        .post("/data/case-types/UNSPECIFIED_CLAIMS/validate?pageId=CREATE_CLAIMNotifications")
        .headers(CivilDamagesHeader.headers_295)
        .body(StringBody("{\"data\":{\"applicantSolicitor1CheckEmail\":{\"email\":\"civil.damages.claims+organisation.1.superuser@gmail.com\",\"correct\":\"Yes\"}},\"event\":{\"id\":\"CREATE_CLAIM\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"solicitorReferences\":{\"applicantSolicitor1Reference\":\"claim ref\",\"respondentSolicitor1Reference\":\"def ref\"},\"courtLocation\":{\"applicantPreferredCourt\":\"152\"},\"applicant1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"companyName\":\"baccom\",\"soleTraderTitle\":null,\"partyName\":\"baccom\",\"partyTypeDisplayValue\":\"Company\",\"primaryAddress\":{\"AddressLine1\":\"10 Hibernia Gardens\",\"AddressLine2\":\"\",\"AddressLine3\":\"\",\"PostTown\":\"Hounslow\",\"County\":\"\",\"Country\":\"United Kingdom\",\"PostCode\":\"TW3 3SD\"}},\"applicant1LitigationFriendRequired\":\"No\",\"applicantSolicitor1CheckEmail\":{\"email\":\"civil.damages.claims+organisation.1.superuser@gmail.com\",\"correct\":\"Yes\"}},\"event_token\":\"${event_token}\",\"ignore_warning\":false}"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  val caseshareorgs =
    group("CD_CreateClaim_120_CaseShare") {
      exec(http("CD_CreateClaim_120_CaseShare")
        .get("/api/caseshare/orgs")
        .headers(CivilDamagesHeader.headers_418)
        .check(status.in(200, 304))
      )
    }
  
  
  val claimantsolorganisation =
    group("CD_CreateClaim_130_CLAIMClaimantSolicitorOrganisation") {
      exec(http("CD_CreateClaim_130_Org")
        .post("/data/case-types/UNSPECIFIED_CLAIMS/validate?pageId=CREATE_CLAIMClaimantSolicitorOrganisation")
        .headers(CivilDamagesHeader.headers_347)
        .body(StringBody("{\"data\":{\"applicant1OrganisationPolicy\":{\"OrgPolicyCaseAssignedRole\":\"[APPLICANTSOLICITORONE]\",\"OrgPolicyReference\":\"applicant ord\",\"Organisation\":{\"OrganisationID\":\"7H4MGE5\",\"OrganisationName\":\"Civil Damages Claims - Organisation 1\"}}},\"event\":{\"id\":\"CREATE_CLAIM\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"solicitorReferences\":{\"applicantSolicitor1Reference\":\"claim ref\",\"respondentSolicitor1Reference\":\"def ref\"},\"courtLocation\":{\"applicantPreferredCourt\":\"152\"},\"applicant1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"companyName\":\"baccom\",\"soleTraderTitle\":null,\"partyName\":\"baccom\",\"partyTypeDisplayValue\":\"Company\",\"primaryAddress\":{\"AddressLine1\":\"10 Hibernia Gardens\",\"AddressLine2\":\"\",\"AddressLine3\":\"\",\"PostTown\":\"Hounslow\",\"County\":\"\",\"Country\":\"United Kingdom\",\"PostCode\":\"TW3 3SD\"}},\"applicant1LitigationFriendRequired\":\"No\",\"applicantSolicitor1CheckEmail\":{\"email\":\"civil.damages.claims+organisation.1.superuser@gmail.com\",\"correct\":\"Yes\"},\"applicant1OrganisationPolicy\":{\"OrgPolicyCaseAssignedRole\":\"[APPLICANTSOLICITORONE]\",\"OrgPolicyReference\":\"applicant ord\",\"Organisation\":{\"OrganisationID\":\"7H4MGE5\",\"OrganisationName\":\"Civil Damages Claims - Organisation 1\"}}},\"event_token\":\"${event_token}\",\"ignore_warning\":false}"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  val postcode1 =
    group("CD_CreateClaim_140_SolPostcode") {
      exec(http("CD_CreateClaim_140_Postcode")
        .get("/api/addresses?postcode=TW33SD")
        .headers(CivilDamagesHeader.headers_104)
        .check(status.in(200, 304))
      )
    }
  
  val claimdefendant =
    group("CD_CreateClaim_150_CLAIMDefendant") {
      exec(http("CD_CreateClaim_150_Def")
        .post("/data/case-types/UNSPECIFIED_CLAIMS/validate?pageId=CREATE_CLAIMDefendant")
        .headers(CivilDamagesHeader.headers_394)
        .body(StringBody("{\"data\":{\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"companyName\":\"defcom\",\"soleTraderTitle\":null,\"soleTraderTradingAs\":null,\"individualDateOfBirth\":null,\"soleTraderDateOfBirth\":null,\"partyName\":null,\"partyTypeDisplayValue\":null,\"primaryAddress\":{\"AddressLine1\":\"22 Hibernia Gardens\",\"AddressLine2\":\"\",\"AddressLine3\":\"\",\"PostTown\":\"Hounslow\",\"County\":\"\",\"Country\":\"United Kingdom\",\"PostCode\":\"TW3 3SD\"}}},\"event\":{\"id\":\"CREATE_CLAIM\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"solicitorReferences\":{\"applicantSolicitor1Reference\":\"claim ref\",\"respondentSolicitor1Reference\":\"def ref\"},\"courtLocation\":{\"applicantPreferredCourt\":\"152\"},\"applicant1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"companyName\":\"baccom\",\"soleTraderTitle\":null,\"partyName\":\"baccom\",\"partyTypeDisplayValue\":\"Company\",\"primaryAddress\":{\"AddressLine1\":\"10 Hibernia Gardens\",\"AddressLine2\":\"\",\"AddressLine3\":\"\",\"PostTown\":\"Hounslow\",\"County\":\"\",\"Country\":\"United Kingdom\",\"PostCode\":\"TW3 3SD\"}},\"applicant1LitigationFriendRequired\":\"No\",\"applicantSolicitor1CheckEmail\":{\"email\":\"civil.damages.claims+organisation.1.superuser@gmail.com\",\"correct\":\"Yes\"},\"applicant1OrganisationPolicy\":{\"OrgPolicyCaseAssignedRole\":\"[APPLICANTSOLICITORONE]\",\"OrgPolicyReference\":\"applicant ord\",\"Organisation\":{\"OrganisationID\":\"7H4MGE5\",\"OrganisationName\":\"Civil Damages Claims - Organisation 1\"}},\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"companyName\":\"defcom\",\"soleTraderTitle\":null,\"soleTraderTradingAs\":null,\"individualDateOfBirth\":null,\"soleTraderDateOfBirth\":null,\"partyName\":null,\"partyTypeDisplayValue\":null,\"primaryAddress\":{\"AddressLine1\":\"22 Hibernia Gardens\",\"AddressLine2\":\"\",\"AddressLine3\":\"\",\"PostTown\":\"Hounslow\",\"County\":\"\",\"Country\":\"United Kingdom\",\"PostCode\":\"TW3 3SD\"}}},\"event_token\":\"${event_token}\",\"ignore_warning\":false}"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  
  val claimLegalRep =
    group("CD_CreateClaim_160_CLAIMLegalRepresentation") {
      exec(http("CD_CreateClaim_160_Rep")
        .post("/data/case-types/UNSPECIFIED_CLAIMS/validate?pageId=CREATE_CLAIMLegalRepresentation")
        .headers(CivilDamagesHeader.headers_413)
        .body(StringBody("{\"data\":{\"respondent1Represented\":\"Yes\"},\"event\":{\"id\":\"CREATE_CLAIM\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"solicitorReferences\":{\"applicantSolicitor1Reference\":\"claim ref\",\"respondentSolicitor1Reference\":\"def ref\"},\"courtLocation\":{\"applicantPreferredCourt\":\"152\"},\"applicant1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"companyName\":\"baccom\",\"soleTraderTitle\":null,\"partyName\":\"baccom\",\"partyTypeDisplayValue\":\"Company\",\"primaryAddress\":{\"AddressLine1\":\"10 Hibernia Gardens\",\"AddressLine2\":\"\",\"AddressLine3\":\"\",\"PostTown\":\"Hounslow\",\"County\":\"\",\"Country\":\"United Kingdom\",\"PostCode\":\"TW3 3SD\"}},\"applicant1LitigationFriendRequired\":\"No\",\"applicantSolicitor1CheckEmail\":{\"email\":\"civil.damages.claims+organisation.1.superuser@gmail.com\",\"correct\":\"Yes\"},\"applicant1OrganisationPolicy\":{\"OrgPolicyCaseAssignedRole\":\"[APPLICANTSOLICITORONE]\",\"OrgPolicyReference\":\"applicant ord\",\"Organisation\":{\"OrganisationID\":\"7H4MGE5\",\"OrganisationName\":\"Civil Damages Claims - Organisation 1\"}},\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"companyName\":\"defcom\",\"soleTraderTitle\":null,\"soleTraderTradingAs\":null,\"individualDateOfBirth\":null,\"soleTraderDateOfBirth\":null,\"partyName\":null,\"partyTypeDisplayValue\":null,\"primaryAddress\":{\"AddressLine1\":\"22 Hibernia Gardens\",\"AddressLine2\":\"\",\"AddressLine3\":\"\",\"PostTown\":\"Hounslow\",\"County\":\"\",\"Country\":\"United Kingdom\",\"PostCode\":\"TW3 3SD\"}},\"respondent1Represented\":\"Yes\"},\"event_token\":\"${event_token}\",\"ignore_warning\":false}"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  val caseshareorgs1 =
    group("CD_CreateClaim_170_DefCaseShare") {
      exec(http("CD_CreateClaim_170_CaseShare")
        .get("/api/caseshare/orgs")
        .headers(CivilDamagesHeader.headers_418)
        .check(status.in(200, 304))
      )
    }
  
  val claimdefsolicitororg =
    group("CD_CreateClaim_180_CLAIMDefendantSolicitorOrganisation") {
      exec(http("CD_CreateClaim_180_Org")
        .post("/data/case-types/UNSPECIFIED_CLAIMS/validate?pageId=CREATE_CLAIMDefendantSolicitorOrganisation")
        .headers(CivilDamagesHeader.headers_469)
        .body(StringBody("{\"data\":{\"respondent1OrgRegistered\":\"Yes\",\"respondent1OrganisationPolicy\":{\"OrgPolicyCaseAssignedRole\":\"[RESPONDENTSOLICITORONE]\",\"OrgPolicyReference\":\"resporg\",\"Organisation\":{\"OrganisationID\":\"MEVCCQF\",\"OrganisationName\":\"Civil Damages Claims - Organisation 2\"}}},\"event\":{\"id\":\"CREATE_CLAIM\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"solicitorReferences\":{\"applicantSolicitor1Reference\":\"claim ref\",\"respondentSolicitor1Reference\":\"def ref\"},\"courtLocation\":{\"applicantPreferredCourt\":\"152\"},\"applicant1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"companyName\":\"baccom\",\"soleTraderTitle\":null,\"partyName\":\"baccom\",\"partyTypeDisplayValue\":\"Company\",\"primaryAddress\":{\"AddressLine1\":\"10 Hibernia Gardens\",\"AddressLine2\":\"\",\"AddressLine3\":\"\",\"PostTown\":\"Hounslow\",\"County\":\"\",\"Country\":\"United Kingdom\",\"PostCode\":\"TW3 3SD\"}},\"applicant1LitigationFriendRequired\":\"No\",\"applicantSolicitor1CheckEmail\":{\"email\":\"civil.damages.claims+organisation.1.superuser@gmail.com\",\"correct\":\"Yes\"},\"applicant1OrganisationPolicy\":{\"OrgPolicyCaseAssignedRole\":\"[APPLICANTSOLICITORONE]\",\"OrgPolicyReference\":\"applicant ord\",\"Organisation\":{\"OrganisationID\":\"7H4MGE5\",\"OrganisationName\":\"Civil Damages Claims - Organisation 1\"}},\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"companyName\":\"defcom\",\"soleTraderTitle\":null,\"soleTraderTradingAs\":null,\"individualDateOfBirth\":null,\"soleTraderDateOfBirth\":null,\"partyName\":null,\"partyTypeDisplayValue\":null,\"primaryAddress\":{\"AddressLine1\":\"22 Hibernia Gardens\",\"AddressLine2\":\"\",\"AddressLine3\":\"\",\"PostTown\":\"Hounslow\",\"County\":\"\",\"Country\":\"United Kingdom\",\"PostCode\":\"TW3 3SD\"}},\"respondent1Represented\":\"Yes\",\"respondent1OrgRegistered\":\"Yes\",\"respondent1OrganisationPolicy\":{\"OrgPolicyCaseAssignedRole\":\"[RESPONDENTSOLICITORONE]\",\"OrgPolicyReference\":\"resporg\",\"Organisation\":{\"OrganisationID\":\"MEVCCQF\",\"OrganisationName\":\"Civil Damages Claims - Organisation 2\"}}},\"event_token\":\"${event_token}\",\"ignore_warning\":false}"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  val claimdefsolicitororgemail =
    group("CD_CreateClaim_190_CLAIMDefendantSolicitorEmail") {
      exec(http("CD_CreateClaim_190_SolEmail")
        .post("/data/case-types/UNSPECIFIED_CLAIMS/validate?pageId=CREATE_CLAIMDefendantSolicitorEmail")
        .headers(CivilDamagesHeader.headers_491)
        .body(StringBody("{\"data\":{\"respondentSolicitor1EmailAddress\":\"civil.damages.claims+organisation.2.superuser@gmail.com\"},\"event\":{\"id\":\"CREATE_CLAIM\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"solicitorReferences\":{\"applicantSolicitor1Reference\":\"claim ref\",\"respondentSolicitor1Reference\":\"def ref\"},\"courtLocation\":{\"applicantPreferredCourt\":\"152\"},\"applicant1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"companyName\":\"baccom\",\"soleTraderTitle\":null,\"partyName\":\"baccom\",\"partyTypeDisplayValue\":\"Company\",\"primaryAddress\":{\"AddressLine1\":\"10 Hibernia Gardens\",\"AddressLine2\":\"\",\"AddressLine3\":\"\",\"PostTown\":\"Hounslow\",\"County\":\"\",\"Country\":\"United Kingdom\",\"PostCode\":\"TW3 3SD\"}},\"applicant1LitigationFriendRequired\":\"No\",\"applicantSolicitor1CheckEmail\":{\"email\":\"civil.damages.claims+organisation.1.superuser@gmail.com\",\"correct\":\"Yes\"},\"applicant1OrganisationPolicy\":{\"OrgPolicyCaseAssignedRole\":\"[APPLICANTSOLICITORONE]\",\"OrgPolicyReference\":\"applicant ord\",\"Organisation\":{\"OrganisationID\":\"7H4MGE5\",\"OrganisationName\":\"Civil Damages Claims - Organisation 1\"}},\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"companyName\":\"defcom\",\"soleTraderTitle\":null,\"soleTraderTradingAs\":null,\"individualDateOfBirth\":null,\"soleTraderDateOfBirth\":null,\"partyName\":null,\"partyTypeDisplayValue\":null,\"primaryAddress\":{\"AddressLine1\":\"22 Hibernia Gardens\",\"AddressLine2\":\"\",\"AddressLine3\":\"\",\"PostTown\":\"Hounslow\",\"County\":\"\",\"Country\":\"United Kingdom\",\"PostCode\":\"TW3 3SD\"}},\"respondent1Represented\":\"Yes\",\"respondent1OrgRegistered\":\"Yes\",\"respondent1OrganisationPolicy\":{\"OrgPolicyCaseAssignedRole\":\"[RESPONDENTSOLICITORONE]\",\"OrgPolicyReference\":\"resporg\",\"Organisation\":{\"OrganisationID\":\"MEVCCQF\",\"OrganisationName\":\"Civil Damages Claims - Organisation 2\"}},\"respondentSolicitor1EmailAddress\":\"civil.damages.claims+organisation.2.superuser@gmail.com\"},\"event_token\":\"${event_token}\",\"ignore_warning\":false}"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  
  val claimtype =
    group("CD_CreateClaim_200_CLAIMClaimType") {
      exec(http("CD_CreateClaim_200_ClaimType")
        .post("/data/case-types/UNSPECIFIED_CLAIMS/validate?pageId=CREATE_CLAIMClaimType")
        .headers(CivilDamagesHeader.headers_514)
        .body(StringBody("{\"data\":{\"claimType\":\"CLINICAL_NEGLIGENCE\"},\"event\":{\"id\":\"CREATE_CLAIM\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"solicitorReferences\":{\"applicantSolicitor1Reference\":\"claim ref\",\"respondentSolicitor1Reference\":\"def ref\"},\"courtLocation\":{\"applicantPreferredCourt\":\"152\"},\"applicant1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"companyName\":\"baccom\",\"soleTraderTitle\":null,\"partyName\":\"baccom\",\"partyTypeDisplayValue\":\"Company\",\"primaryAddress\":{\"AddressLine1\":\"10 Hibernia Gardens\",\"AddressLine2\":\"\",\"AddressLine3\":\"\",\"PostTown\":\"Hounslow\",\"County\":\"\",\"Country\":\"United Kingdom\",\"PostCode\":\"TW3 3SD\"}},\"applicant1LitigationFriendRequired\":\"No\",\"applicantSolicitor1CheckEmail\":{\"email\":\"civil.damages.claims+organisation.1.superuser@gmail.com\",\"correct\":\"Yes\"},\"applicant1OrganisationPolicy\":{\"OrgPolicyCaseAssignedRole\":\"[APPLICANTSOLICITORONE]\",\"OrgPolicyReference\":\"applicant ord\",\"Organisation\":{\"OrganisationID\":\"7H4MGE5\",\"OrganisationName\":\"Civil Damages Claims - Organisation 1\"}},\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"companyName\":\"defcom\",\"soleTraderTitle\":null,\"soleTraderTradingAs\":null,\"individualDateOfBirth\":null,\"soleTraderDateOfBirth\":null,\"partyName\":null,\"partyTypeDisplayValue\":null,\"primaryAddress\":{\"AddressLine1\":\"22 Hibernia Gardens\",\"AddressLine2\":\"\",\"AddressLine3\":\"\",\"PostTown\":\"Hounslow\",\"County\":\"\",\"Country\":\"United Kingdom\",\"PostCode\":\"TW3 3SD\"}},\"respondent1Represented\":\"Yes\",\"respondent1OrgRegistered\":\"Yes\",\"respondent1OrganisationPolicy\":{\"OrgPolicyCaseAssignedRole\":\"[RESPONDENTSOLICITORONE]\",\"OrgPolicyReference\":\"resporg\",\"Organisation\":{\"OrganisationID\":\"MEVCCQF\",\"OrganisationName\":\"Civil Damages Claims - Organisation 2\"}},\"respondentSolicitor1EmailAddress\":\"civil.damages.claims+organisation.2.superuser@gmail.com\",\"claimType\":\"CLINICAL_NEGLIGENCE\"},\"event_token\":\"${event_token}\",\"ignore_warning\":false}"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  
  val createclaimdetail =
    group("CD_CreateClaim_210_CLAIMDetails") {
      exec(http("CD_CreateClaim_210_Details")
        .post("/data/case-types/UNSPECIFIED_CLAIMS/validate?pageId=CREATE_CLAIMDetails")
        .headers(CivilDamagesHeader.headers_534)
        .body(StringBody("{\"data\":{\"detailsOfClaim\":\"asasjajashashahsjahsjahsjahsjahsjahsjahs\"},\"event\":{\"id\":\"CREATE_CLAIM\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"solicitorReferences\":{\"applicantSolicitor1Reference\":\"claim ref\",\"respondentSolicitor1Reference\":\"def ref\"},\"courtLocation\":{\"applicantPreferredCourt\":\"152\"},\"applicant1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"companyName\":\"baccom\",\"soleTraderTitle\":null,\"partyName\":\"baccom\",\"partyTypeDisplayValue\":\"Company\",\"primaryAddress\":{\"AddressLine1\":\"10 Hibernia Gardens\",\"AddressLine2\":\"\",\"AddressLine3\":\"\",\"PostTown\":\"Hounslow\",\"County\":\"\",\"Country\":\"United Kingdom\",\"PostCode\":\"TW3 3SD\"}},\"applicant1LitigationFriendRequired\":\"No\",\"applicantSolicitor1CheckEmail\":{\"email\":\"civil.damages.claims+organisation.1.superuser@gmail.com\",\"correct\":\"Yes\"},\"applicant1OrganisationPolicy\":{\"OrgPolicyCaseAssignedRole\":\"[APPLICANTSOLICITORONE]\",\"OrgPolicyReference\":\"applicant ord\",\"Organisation\":{\"OrganisationID\":\"7H4MGE5\",\"OrganisationName\":\"Civil Damages Claims - Organisation 1\"}},\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"companyName\":\"defcom\",\"soleTraderTitle\":null,\"soleTraderTradingAs\":null,\"individualDateOfBirth\":null,\"soleTraderDateOfBirth\":null,\"partyName\":null,\"partyTypeDisplayValue\":null,\"primaryAddress\":{\"AddressLine1\":\"22 Hibernia Gardens\",\"AddressLine2\":\"\",\"AddressLine3\":\"\",\"PostTown\":\"Hounslow\",\"County\":\"\",\"Country\":\"United Kingdom\",\"PostCode\":\"TW3 3SD\"}},\"respondent1Represented\":\"Yes\",\"respondent1OrgRegistered\":\"Yes\",\"respondent1OrganisationPolicy\":{\"OrgPolicyCaseAssignedRole\":\"[RESPONDENTSOLICITORONE]\",\"OrgPolicyReference\":\"resporg\",\"Organisation\":{\"OrganisationID\":\"MEVCCQF\",\"OrganisationName\":\"Civil Damages Claims - Organisation 2\"}},\"respondentSolicitor1EmailAddress\":\"civil.damages.claims+organisation.2.superuser@gmail.com\",\"claimType\":\"CLINICAL_NEGLIGENCE\",\"detailsOfClaim\":\"asasjajashashahsjahsjahsjahsjahsjahsjahs\"},\"event_token\":\"${event_token}\",\"ignore_warning\":false}"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  
  val postclaimdocs =
    group("CD_CreateClaim_220_Documents") {
      exec(http("CD_CreateClaim_220_Docs")
        .post("/documents")
        .headers(CivilDamagesHeader.headers_554)
        .bodyPart(RawFileBodyPart("files", "3MB.pdf")
          .fileName("3MB.pdf")
          .transferEncoding("binary"))
        .asMultipartForm
        .formParam("classification", "PUBLIC")
        .check(regex("""http://(.+)/""").saveAs("DMURL"))
        .check(regex("""internal/documents/(.+?)/binary""").saveAs("Document_ID"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  val createclaimupload =
    group("CD_CreateClaim_230_CLAIMUpload") {
      exec(http("CD_CreateClaim_230_Upload")
        .post("/data/case-types/UNSPECIFIED_CLAIMS/validate?pageId=CREATE_CLAIMUpload")
        .headers(CivilDamagesHeader.headers_572)
        .body(StringBody("{\"data\":{\"servedDocumentFiles\":{\"particularsOfClaimText\":null,\"particularsOfClaimDocument\":{\"document_url\":\"http://dm-store-perftest.service.core-compute-perftest.internal/documents/${Document_ID}\",\"document_binary_url\":\"http://dm-store-perftest.service.core-compute-perftest.internal/documents/${Document_ID}/binary\",\"document_filename\":\"3MB.pdf\"},\"medicalReport\":[],\"scheduleOfLoss\":[],\"certificateOfSuitability\":[],\"other\":[]}},\"event\":{\"id\":\"CREATE_CLAIM\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"solicitorReferences\":{\"applicantSolicitor1Reference\":\"claim ref\",\"respondentSolicitor1Reference\":\"def ref\"},\"courtLocation\":{\"applicantPreferredCourt\":\"152\"},\"applicant1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"companyName\":\"baccom\",\"soleTraderTitle\":null,\"partyName\":\"baccom\",\"partyTypeDisplayValue\":\"Company\",\"primaryAddress\":{\"AddressLine1\":\"10 Hibernia Gardens\",\"AddressLine2\":\"\",\"AddressLine3\":\"\",\"PostTown\":\"Hounslow\",\"County\":\"\",\"Country\":\"United Kingdom\",\"PostCode\":\"TW3 3SD\"}},\"applicant1LitigationFriendRequired\":\"No\",\"applicantSolicitor1CheckEmail\":{\"email\":\"civil.damages.claims+organisation.1.superuser@gmail.com\",\"correct\":\"Yes\"},\"applicant1OrganisationPolicy\":{\"OrgPolicyCaseAssignedRole\":\"[APPLICANTSOLICITORONE]\",\"OrgPolicyReference\":\"applicant ord\",\"Organisation\":{\"OrganisationID\":\"7H4MGE5\",\"OrganisationName\":\"Civil Damages Claims - Organisation 1\"}},\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"companyName\":\"defcom\",\"soleTraderTitle\":null,\"soleTraderTradingAs\":null,\"individualDateOfBirth\":null,\"soleTraderDateOfBirth\":null,\"partyName\":null,\"partyTypeDisplayValue\":null,\"primaryAddress\":{\"AddressLine1\":\"22 Hibernia Gardens\",\"AddressLine2\":\"\",\"AddressLine3\":\"\",\"PostTown\":\"Hounslow\",\"County\":\"\",\"Country\":\"United Kingdom\",\"PostCode\":\"TW3 3SD\"}},\"respondent1Represented\":\"Yes\",\"respondent1OrgRegistered\":\"Yes\",\"respondent1OrganisationPolicy\":{\"OrgPolicyCaseAssignedRole\":\"[RESPONDENTSOLICITORONE]\",\"OrgPolicyReference\":\"resporg\",\"Organisation\":{\"OrganisationID\":\"MEVCCQF\",\"OrganisationName\":\"Civil Damages Claims - Organisation 2\"}},\"respondentSolicitor1EmailAddress\":\"civil.damages.claims+organisation.2.superuser@gmail.com\",\"claimType\":\"CLINICAL_NEGLIGENCE\",\"detailsOfClaim\":\"asasjajashashahsjahsjahsjahsjahsjahsjahs\",\"servedDocumentFiles\":{\"particularsOfClaimText\":\"\",\"particularsOfClaimDocument\":{\"document_url\":\"http://dm-store-perftest.service.core-compute-perftest.internal/documents/${Document_ID}\",\"document_binary_url\":\"http://dm-store-perftest.service.core-compute-perftest.internal/documents/${Document_ID}/binary\",\"document_filename\":\"3MB.pdf\"},\"medicalReport\":[],\"scheduleOfLoss\":[],\"certificateOfSuitability\":[],\"other\":[]}},\"event_token\":\"${event_token}\",\"ignore_warning\":false}"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  val createclaimvalue =
    group("CD_CreateClaim_240_CLAIMClaimValue") {
      exec(http("CD_CreateClaim_240_ClaimValue")
        .post("/data/case-types/UNSPECIFIED_CLAIMS/validate?pageId=CREATE_CLAIMClaimValue")
        .headers(CivilDamagesHeader.headers_595)
        .body(StringBody("{\"data\":{\"claimValue\":{\"statementOfValueInPennies\":\"2500100\"}},\"event\":{\"id\":\"CREATE_CLAIM\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"solicitorReferences\":{\"applicantSolicitor1Reference\":\"claim ref\",\"respondentSolicitor1Reference\":\"def ref\"},\"courtLocation\":{\"applicantPreferredCourt\":\"152\"},\"applicant1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"companyName\":\"baccom\",\"soleTraderTitle\":null,\"partyName\":\"baccom\",\"partyTypeDisplayValue\":\"Company\",\"primaryAddress\":{\"AddressLine1\":\"10 Hibernia Gardens\",\"AddressLine2\":\"\",\"AddressLine3\":\"\",\"PostTown\":\"Hounslow\",\"County\":\"\",\"Country\":\"United Kingdom\",\"PostCode\":\"TW3 3SD\"}},\"applicant1LitigationFriendRequired\":\"No\",\"applicantSolicitor1CheckEmail\":{\"email\":\"civil.damages.claims+organisation.1.superuser@gmail.com\",\"correct\":\"Yes\"},\"applicant1OrganisationPolicy\":{\"OrgPolicyCaseAssignedRole\":\"[APPLICANTSOLICITORONE]\",\"OrgPolicyReference\":\"applicant ord\",\"Organisation\":{\"OrganisationID\":\"7H4MGE5\",\"OrganisationName\":\"Civil Damages Claims - Organisation 1\"}},\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"companyName\":\"defcom\",\"soleTraderTitle\":null,\"soleTraderTradingAs\":null,\"individualDateOfBirth\":null,\"soleTraderDateOfBirth\":null,\"partyName\":null,\"partyTypeDisplayValue\":null,\"primaryAddress\":{\"AddressLine1\":\"22 Hibernia Gardens\",\"AddressLine2\":\"\",\"AddressLine3\":\"\",\"PostTown\":\"Hounslow\",\"County\":\"\",\"Country\":\"United Kingdom\",\"PostCode\":\"TW3 3SD\"}},\"respondent1Represented\":\"Yes\",\"respondent1OrgRegistered\":\"Yes\",\"respondent1OrganisationPolicy\":{\"OrgPolicyCaseAssignedRole\":\"[RESPONDENTSOLICITORONE]\",\"OrgPolicyReference\":\"resporg\",\"Organisation\":{\"OrganisationID\":\"MEVCCQF\",\"OrganisationName\":\"Civil Damages Claims - Organisation 2\"}},\"respondentSolicitor1EmailAddress\":\"civil.damages.claims+organisation.2.superuser@gmail.com\",\"claimType\":\"CLINICAL_NEGLIGENCE\",\"detailsOfClaim\":\"asasjajashashahsjahsjahsjahsjahsjahsjahs\",\"servedDocumentFiles\":{\"particularsOfClaimText\":\"\",\"particularsOfClaimDocument\":{\"document_url\":\"http://dm-store-perftest.service.core-compute-perftest.internal/documents/${Document_ID}\",\"document_binary_url\":\"http://dm-store-perftest.service.core-compute-perftest.internal/documents/${Document_ID}/binary\",\"document_filename\":\"3MB.pdf\"},\"medicalReports\":[],\"scheduleOfLoss\":[],\"certificateOfSuitability\":[],\"other\":[]},\"claimValue\":{\"statementOfValueInPennies\":\"2500100\"}},\"event_token\":\"${event_token}\",\"ignore_warning\":false}"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  val createclaimpbanumber =
    group("CD_CreateClaim_250_CLAIMPbaNumber") {
      exec(http("CD_CreateClaim_250_PBANumber")
        .post("/data/case-types/UNSPECIFIED_CLAIMS/validate?pageId=CREATE_CLAIMPbaNumber")
        .headers(CivilDamagesHeader.headers_610)
        .body(StringBody("{\"data\":{\"claimFee\":{\"calculatedAmountInPence\":\"125005\",\"code\":\"FEE0209\",\"version\":\"3\"},\"applicantSolicitor1PbaAccounts\":{\"value\":{\"code\":\"66dc22d8-1554-454b-b153-a648ebe1ba30\",\"label\":\"PBA0077597\"},\"list_items\":[{\"code\":\"66dc22d8-1554-454b-b153-a648ebe1ba30\",\"label\":\"PBA0077597\"}]}},\"event\":{\"id\":\"CREATE_CLAIM\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"solicitorReferences\":{\"applicantSolicitor1Reference\":\"claim ref\",\"respondentSolicitor1Reference\":\"def ref\"},\"courtLocation\":{\"applicantPreferredCourt\":\"152\"},\"applicant1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"companyName\":\"baccom\",\"soleTraderTitle\":null,\"partyName\":\"baccom\",\"partyTypeDisplayValue\":\"Company\",\"primaryAddress\":{\"AddressLine1\":\"10 Hibernia Gardens\",\"AddressLine2\":\"\",\"AddressLine3\":\"\",\"PostTown\":\"Hounslow\",\"County\":\"\",\"Country\":\"United Kingdom\",\"PostCode\":\"TW3 3SD\"}},\"applicant1LitigationFriendRequired\":\"No\",\"applicantSolicitor1CheckEmail\":{\"email\":\"civil.damages.claims+organisation.1.superuser@gmail.com\",\"correct\":\"Yes\"},\"applicant1OrganisationPolicy\":{\"OrgPolicyCaseAssignedRole\":\"[APPLICANTSOLICITORONE]\",\"OrgPolicyReference\":\"applicant ord\",\"Organisation\":{\"OrganisationID\":\"7H4MGE5\",\"OrganisationName\":\"Civil Damages Claims - Organisation 1\"}},\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"companyName\":\"defcom\",\"soleTraderTitle\":null,\"soleTraderTradingAs\":null,\"individualDateOfBirth\":null,\"soleTraderDateOfBirth\":null,\"partyName\":\"defcom\",\"partyTypeDisplayValue\":\"Company\",\"primaryAddress\":{\"AddressLine1\":\"22 Hibernia Gardens\",\"AddressLine2\":\"\",\"AddressLine3\":\"\",\"PostTown\":\"Hounslow\",\"County\":\"\",\"Country\":\"United Kingdom\",\"PostCode\":\"TW3 3SD\"}},\"respondent1Represented\":\"Yes\",\"respondent1OrgRegistered\":\"Yes\",\"respondent1OrganisationPolicy\":{\"OrgPolicyCaseAssignedRole\":\"[RESPONDENTSOLICITORONE]\",\"OrgPolicyReference\":\"resporg\",\"Organisation\":{\"OrganisationID\":\"MEVCCQF\",\"OrganisationName\":\"Civil Damages Claims - Organisation 2\"}},\"respondentSolicitor1EmailAddress\":\"civil.damages.claims+organisation.2.superuser@gmail.com\",\"claimType\":\"CLINICAL_NEGLIGENCE\",\"detailsOfClaim\":\"asasjajashashahsjahsjahsjahsjahsjahsjahs\",\"servedDocumentFiles\":{\"particularsOfClaimText\":\"\",\"particularsOfClaimDocument\":{\"document_url\":\"http://dm-store-perftest.service.core-compute-perftest.internal/documents/${Document_ID}\",\"document_binary_url\":\"http://dm-store-perftest.service.core-compute-perftest.internal/documents/${Document_ID}/binary\",\"document_filename\":\"3MB.pdf\"},\"medicalReports\":[],\"scheduleOfLoss\":[],\"certificateOfSuitability\":[],\"other\":[]},\"claimValue\":{\"statementOfValueInPennies\":\"2500100\"},\"claimFee\":{\"calculatedAmountInPence\":\"125005\",\"code\":\"FEE0209\",\"version\":\"3\"},\"applicantSolicitor1PbaAccounts\":\"66dc22d8-1554-454b-b153-a648ebe1ba30\"},\"event_token\":\"${event_token}\",\"ignore_warning\":false}"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  val createclaimpaymentref =
    group("CD_CreateClaim_260_CLAIMPaymentReference") {
      exec(http("CD_CreateClaim_260_Ref")
        .post("/data/case-types/UNSPECIFIED_CLAIMS/validate?pageId=CREATE_CLAIMPaymentReference")
        .headers(CivilDamagesHeader.headers_627)
        .body(StringBody("{\"data\":{\"claimIssuedPaymentDetails\":{\"customerReference\":\"pba ref\",\"status\":null,\"reference\":null,\"errorMessage\":null,\"errorCode\":null}},\"event\":{\"id\":\"CREATE_CLAIM\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"solicitorReferences\":{\"applicantSolicitor1Reference\":\"claim ref\",\"respondentSolicitor1Reference\":\"def ref\"},\"courtLocation\":{\"applicantPreferredCourt\":\"152\"},\"applicant1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"companyName\":\"claimcom\",\"soleTraderTitle\":null,\"partyName\":\"claimcom\",\"partyTypeDisplayValue\":\"Company\",\"primaryAddress\":{\"AddressLine1\":\"16 Hibernia Gardens\",\"AddressLine2\":\"\",\"AddressLine3\":\"\",\"PostTown\":\"Hounslow\",\"County\":\"\",\"Country\":\"United Kingdom\",\"PostCode\":\"TW3 3SD\"}},\"applicant1LitigationFriendRequired\":\"No\",\"applicantSolicitor1CheckEmail\":{\"email\":\"civil.damages.claims+organisation.1.superuser@gmail.com\",\"correct\":\"Yes\"},\"applicant1OrganisationPolicy\":{\"OrgPolicyCaseAssignedRole\":\"[APPLICANTSOLICITORONE]\",\"OrgPolicyReference\":\"claim legal\",\"Organisation\":{\"OrganisationID\":\"7H4MGE5\",\"OrganisationName\":\"Civil Damages Claims - Organisation 1\"}},\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"companyName\":\"def com\",\"soleTraderTitle\":null,\"soleTraderTradingAs\":null,\"individualDateOfBirth\":null,\"soleTraderDateOfBirth\":null,\"partyName\":\"def com\",\"partyTypeDisplayValue\":\"Company\",\"primaryAddress\":{\"AddressLine1\":\"22 Hibernia Gardens\",\"AddressLine2\":\"\",\"AddressLine3\":\"\",\"PostTown\":\"Hounslow\",\"County\":\"\",\"Country\":\"United Kingdom\",\"PostCode\":\"TW3 3SD\"}},\"respondent1Represented\":\"Yes\",\"respondent1OrgRegistered\":\"Yes\",\"respondent1OrganisationPolicy\":{\"OrgPolicyCaseAssignedRole\":\"[RESPONDENTSOLICITORONE]\",\"OrgPolicyReference\":\"def refs\",\"Organisation\":{\"OrganisationID\":\"MEVCCQF\",\"OrganisationName\":\"Civil Damages Claims - Organisation 2\"}},\"respondentSolicitor1EmailAddress\":\"civil.damages.claims+organisation.2.superuser@gmail.com\",\"claimType\":\"CLINICAL_NEGLIGENCE\",\"detailsOfClaim\":\"asasasasasasasasass\",\"servedDocumentFiles\":{\"particularsOfClaimText\":null,\"particularsOfClaimDocument\":{\"document_url\":\"http://dm-store-perftest.service.core-compute-perftest.internal/documents/${Document_ID}\",\"document_binary_url\":\"http://dm-store-perftest.service.core-compute-perftest.internal/documents/${Document_ID}/binary\",\"document_filename\":\"3MB.pdf\"},\"medicalReport\":[],\"scheduleOfLoss\":[],\"certificateOfSuitability\":[],\"other\":[]},\"claimValue\":{\"statementOfValueInPennies\":\"2500100\"},\"claimFee\":{\"calculatedAmountInPence\":\"125005\",\"code\":\"FEE0209\",\"version\":\"3\"},\"applicantSolicitor1PbaAccounts\":{\"value\":{\"code\":\"ec7d1d27-8e08-4289-b8d1-755ff8b57cef\",\"label\":\"PBA0077597\"},\"list_items\":[{\"code\":\"ec7d1d27-8e08-4289-b8d1-755ff8b57cef\",\"label\":\"PBA0077597\"}]},\"claimIssuedPaymentDetails\":{\"customerReference\":\"pba ref\",\"status\":null,\"reference\":null,\"errorMessage\":null,\"errorCode\":null}},\"event_token\":\"${event_token}\",\"ignore_warning\":false}"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  val createclaimstatementoftruth =
    group("CD_CreateClaim_270_CLAIMStatementOfTruth") {
      exec(http("CD_CreateClaim_270_SOT")
        .post("/data/case-types/UNSPECIFIED_CLAIMS/validate?pageId=CREATE_CLAIMStatementOfTruth")
        .headers(CivilDamagesHeader.headers_650)
        .body(StringBody("{\"data\":{\"applicantSolicitor1ClaimStatementOfTruth\":{\"name\":\"vijay\",\"role\":\"senior solicitor\"}},\"event\":{\"id\":\"CREATE_CLAIM\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"solicitorReferences\":{\"applicantSolicitor1Reference\":\"claim ref\",\"respondentSolicitor1Reference\":\"def ref\"},\"courtLocation\":{\"applicantPreferredCourt\":\"152\"},\"applicant1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"companyName\":\"baccom\",\"soleTraderTitle\":null,\"partyName\":\"baccom\",\"partyTypeDisplayValue\":\"Company\",\"primaryAddress\":{\"AddressLine1\":\"10 Hibernia Gardens\",\"AddressLine2\":\"\",\"AddressLine3\":\"\",\"PostTown\":\"Hounslow\",\"County\":\"\",\"Country\":\"United Kingdom\",\"PostCode\":\"TW3 3SD\"}},\"applicant1LitigationFriendRequired\":\"No\",\"applicantSolicitor1CheckEmail\":{\"email\":\"civil.damages.claims+organisation.1.superuser@gmail.com\",\"correct\":\"Yes\"},\"applicant1OrganisationPolicy\":{\"OrgPolicyCaseAssignedRole\":\"[APPLICANTSOLICITORONE]\",\"OrgPolicyReference\":\"applicant ord\",\"Organisation\":{\"OrganisationID\":\"7H4MGE5\",\"OrganisationName\":\"Civil Damages Claims - Organisation 1\"}},\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"companyName\":\"defcom\",\"soleTraderTitle\":null,\"soleTraderTradingAs\":null,\"individualDateOfBirth\":null,\"soleTraderDateOfBirth\":null,\"partyName\":\"defcom\",\"partyTypeDisplayValue\":\"Company\",\"primaryAddress\":{\"AddressLine1\":\"22 Hibernia Gardens\",\"AddressLine2\":\"\",\"AddressLine3\":\"\",\"PostTown\":\"Hounslow\",\"County\":\"\",\"Country\":\"United Kingdom\",\"PostCode\":\"TW3 3SD\"}},\"respondent1Represented\":\"Yes\",\"respondent1OrgRegistered\":\"Yes\",\"respondent1OrganisationPolicy\":{\"OrgPolicyCaseAssignedRole\":\"[RESPONDENTSOLICITORONE]\",\"OrgPolicyReference\":\"resporg\",\"Organisation\":{\"OrganisationID\":\"MEVCCQF\",\"OrganisationName\":\"Civil Damages Claims - Organisation 2\"}},\"respondentSolicitor1EmailAddress\":\"civil.damages.claims+organisation.2.superuser@gmail.com\",\"claimType\":\"CLINICAL_NEGLIGENCE\",\"detailsOfClaim\":\"asasjajashashahsjahsjahsjahsjahsjahsjahs\",\"servedDocumentFiles\":{\"particularsOfClaimText\":\"\",\"particularsOfClaimDocument\":{\"document_url\":\"http://dm-store-perftest.service.core-compute-perftest.internal/documents/${Document_ID}\",\"document_binary_url\":\"http://dm-store-perftest.service.core-compute-perftest.internal/documents/${Document_ID}/binary\",\"document_filename\":\"3MB.pdf\"},\"medicalReports\":[],\"scheduleOfLoss\":[],\"certificateOfSuitability\":[],\"other\":[]},\"claimValue\":{\"statementOfValueInPennies\":\"2500100\"},\"claimFee\":{\"calculatedAmountInPence\":\"125005\",\"code\":\"FEE0209\",\"version\":\"3\"},\"applicantSolicitor1PbaAccounts\":{\"value\":{\"code\":\"66dc22d8-1554-454b-b153-a648ebe1ba30\",\"label\":\"PBA0077597\"},\"list_items\":[{\"code\":\"66dc22d8-1554-454b-b153-a648ebe1ba30\",\"label\":\"PBA0077597\"}]},\"paymentReference\":\"claim ref300\",\"applicantSolicitor1ClaimStatementOfTruth\":{\"name\":\"vijay\",\"role\":\"senior solicitor\"}},\"event_token\":\"${event_token}\",\"ignore_warning\":false}"))
        .check(status.in(200, 304))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  val submitclaimevent =
    group("CD_CreateClaim_280_SubmitClaim") {
      exec(http("CD_CreateClaim_280_005_SubmitClaim")
        .get("/data/internal/profile")
        .headers(CivilDamagesHeader.headers_149)
        .check(status.in(200, 304))
      )
        .exec(http("CD_CreateClaim_280_010_Orgs")
          .get("/api/caseshare/orgs")
          .headers(CivilDamagesHeader.headers_658)
          .check(status.in(200, 304))
        )
      .exec(http("CD_CreateClaim_280_015_Submit")
        .post("/data/case-types/UNSPECIFIED_CLAIMS/cases?ignore-warning=false")
        .headers(CivilDamagesHeader.headers_672)
        .body(StringBody("{\"data\":{\"solicitorReferences\":{\"applicantSolicitor1Reference\":\"claim ref\",\"respondentSolicitor1Reference\":\"def ref\"},\"courtLocation\":{\"applicantPreferredCourt\":\"152\"},\"applicant1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"companyName\":\"claimcom\",\"soleTraderTitle\":null,\"partyName\":\"claimcom\",\"partyTypeDisplayValue\":\"Company\",\"primaryAddress\":{\"AddressLine1\":\"16 Hibernia Gardens\",\"AddressLine2\":\"\",\"AddressLine3\":\"\",\"PostTown\":\"Hounslow\",\"County\":\"\",\"Country\":\"United Kingdom\",\"PostCode\":\"TW3 3SD\"}},\"applicant1LitigationFriendRequired\":\"No\",\"applicantSolicitor1CheckEmail\":{\"email\":\"civil.damages.claims+organisation.1.superuser@gmail.com\",\"correct\":\"Yes\"},\"applicantSolicitor1UserDetails\":{\"email\":null,\"id\":null},\"applicant1OrganisationPolicy\":{\"OrgPolicyCaseAssignedRole\":\"[APPLICANTSOLICITORONE]\",\"OrgPolicyReference\":\"claim legal\",\"Organisation\":{\"OrganisationID\":\"7H4MGE5\",\"OrganisationName\":\"Civil Damages Claims - Organisation 1\"}},\"respondent1\":{\"type\":\"COMPANY\",\"individualTitle\":null,\"companyName\":\"def com\",\"soleTraderTitle\":null,\"soleTraderTradingAs\":null,\"individualDateOfBirth\":null,\"soleTraderDateOfBirth\":null,\"partyName\":\"def com\",\"partyTypeDisplayValue\":\"Company\",\"primaryAddress\":{\"AddressLine1\":\"22 Hibernia Gardens\",\"AddressLine2\":\"\",\"AddressLine3\":\"\",\"PostTown\":\"Hounslow\",\"County\":\"\",\"Country\":\"United Kingdom\",\"PostCode\":\"TW3 3SD\"}},\"respondent1Represented\":\"Yes\",\"respondent1OrgRegistered\":\"Yes\",\"respondent1OrganisationPolicy\":{\"OrgPolicyCaseAssignedRole\":\"[RESPONDENTSOLICITORONE]\",\"OrgPolicyReference\":\"def refs\",\"Organisation\":{\"OrganisationID\":\"MEVCCQF\",\"OrganisationName\":\"Civil Damages Claims - Organisation 2\"}},\"respondentSolicitor1EmailAddress\":\"civil.damages.claims+organisation.2.superuser@gmail.com\",\"claimType\":\"CLINICAL_NEGLIGENCE\",\"detailsOfClaim\":\"asasasasasasasasass\",\"servedDocumentFiles\":{\"particularsOfClaimText\":null,\"particularsOfClaimDocument\":{\"document_url\":\"http://dm-store-perftest.service.core-compute-perftest.internal/documents/${Document_ID}\",\"document_binary_url\":\"http://dm-store-perftest.service.core-compute-perftest.internal/documents/${Document_ID}/binary\",\"document_filename\":\"3MB.pdf\"},\"medicalReport\":[],\"scheduleOfLoss\":[],\"certificateOfSuitability\":[],\"other\":[]},\"claimValue\":{\"statementOfValueInPennies\":\"2500100\"},\"claimFee\":{\"calculatedAmountInPence\":\"125005\",\"code\":\"FEE0209\",\"version\":\"3\"},\"applicantSolicitor1PbaAccounts\":{\"value\":{\"code\":\"ec7d1d27-8e08-4289-b8d1-755ff8b57cef\",\"label\":\"PBA0077597\"},\"list_items\":[{\"code\":\"ec7d1d27-8e08-4289-b8d1-755ff8b57cef\",\"label\":\"PBA0077597\"}]},\"claimIssuedPaymentDetails\":{\"customerReference\":\"pba ref\",\"status\":null,\"reference\":null,\"errorMessage\":null,\"errorCode\":null},\"applicantSolicitor1ClaimStatementOfTruth\":{\"name\":\"vijay\",\"role\":\"senior solicitor\"}},\"event\":{\"id\":\"CREATE_CLAIM\",\"summary\":\"\",\"description\":\"\"},\"event_token\":\"${event_token}\",\"ignore_warning\":false,\"draft_id\":null}"))
        .check(jsonPath("$.event_token").optional.saveAs("event_token_claimcreate"))
        .check(jsonPath("$.id").optional.saveAs("caseId"))
        .check(jsonPath("$.legacyCaseReference").optional.saveAs("claimNumber"))
        .check(status.in(200, 201))
      )
}
      .pause (MinThinkTime, MaxThinkTime)
  
  //this will get the cASE DETAILS
  val casedetailspage =
    group ("CD_CreateClaim_290_CasseDetailsPage") {
      exec(http("CD_CreateClaim_290_005_detail")
        .get("/cases/case-details/${caseId}")
        .headers(CivilDamagesHeader.headers_690)
        .check(status.in(200, 304))
      )
        .exec(http("CD_CreateClaim_290_010_config")
          .get("/api/configuration?configurationKey=termsAndConditionsEnabled")
          .headers(CivilDamagesHeader.headers_13)
          .check(status.in(200, 304))
        )
        .exec(http("CD_CreateClaim_290_015")
          .get("/external/configuration-ui/")
          .headers(CivilDamagesHeader.headers_701)
          .check(status.in(200, 304))
        )
        .exec(http("CD_CreateClaim_290_020")
          .get("/api/configuration?configurationKey=termsAndConditionsEnabled")
          .headers(CivilDamagesHeader.headers_13)
          .check(status.in(200, 304))
        )
        .exec(http("CD_CreateClaim_290_025")
          .get("/api/user/details")
          .headers(CivilDamagesHeader.headers_13)
          .check(status.in(200, 304))
        )
        .exec(http("CD_CreateClaim_290_030")
          .get("/auth/isAuthenticated")
          .headers(CivilDamagesHeader.headers_13)
          .check(status.in(200, 304))
        )
    }
      .pause (MinThinkTime, MaxThinkTime)
      
      val getcasedetailspage=
        group ("CD_CreateClaim_300_BackToCaseDetails") {
          exec(http("CD_CreateClaim_300_CaseDetails")
            .get("/data/internal/cases/${caseId}")
            .headers(CivilDamagesHeader.headers_717)
            .check(status.in(200, 304))
          )
        }
        .pause (MinThinkTime, MaxThinkTime)
  
  //claim notify
  val notifydefaboutclaim =
    group ("CD_CreateClaim_310_NotifyClaimEvent") {
      exec(http("CD_CreateClaim_310_005_Notify")
        .get("/data/internal/cases/${caseId}/event-triggers/NOTIFY_DEFENDANT_OF_CLAIM?ignore-warning=false")
        .headers(CivilDamagesHeader.headers_769)
        .check(status.in(200, 304))
        .check(jsonPath("$.event_token").optional.saveAs("event_token_notifyclaimtodef"))
      )
        .exec(http("CD_CreateClaim_310_010_profile")
          .get("/data/internal/profile")
          .headers(CivilDamagesHeader.headers_149)
          .check(status.in(200, 304))
        )
    }
      .pause (MinThinkTime, MaxThinkTime)
  val claimnotifyeventcontinue =
    group ("CD_CreateClaim_320_CLAIMAccessGrantedWarning") {
      exec(http("CD_CreateClaim_320_005_grant")
        .post("/data/case-types/UNSPECIFIED_CLAIMS/validate?pageId=NOTIFY_DEFENDANT_OF_CLAIMAccessGrantedWarning")
        .headers(CivilDamagesHeader.headers_783)
        .body(StringBody("{\"data\":{},\"event\":{\"id\":\"NOTIFY_DEFENDANT_OF_CLAIM\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{},\"event_token\":\"${event_token_notifyclaimtodef}\",\"ignore_warning\":false,\"case_reference\":\"${caseId}\"}"))
        .check(status.in(200, 304))
      )
        .exec(http("CD_CreateClaim_320_010_profile")
          .get("/data/internal/profile")
          .headers(CivilDamagesHeader.headers_149)
          .check(status.in(200, 304))
        )
    }
    .pause (MinThinkTime, MaxThinkTime)
  
  val claimnotifyeventsubmit =
    group ("CD_CreateClaim_330_ClaimNotifyEventSubmit") {
      exec(http("CD_CreateClaim_330_ClaimNotifyEventSubmit")
        .post("/data/cases/${caseId}/events")
        .headers(CivilDamagesHeader.headers_803)
        .body(StringBody("{\"data\":{},\"event\":{\"id\":\"NOTIFY_DEFENDANT_OF_CLAIM\",\"summary\":\"\",\"description\":\"\"},\"event_token\":\"${event_token_notifyclaimtodef}\",\"ignore_warning\":false}"))
        .check(status.in(200, 201))
      )
    }
  exec(session => session.set("caseId", "${caseId}"))
      .pause (MinThinkTime, MaxThinkTime)
  
  
  
  //end of claim creation and claim notify
  
  // with the above request claim notification is completed and now we can login as defendant solicitor and continue the casedetail
  
  //defendant activity begins after login into manage org and share the case
  
  val backtocasedetailsafterclaimnotify =
    group ("CD_CreateClaim_340_CasedetailsAfterClaimNotify") {
      exec(http("CD_CreateClaim_340_005_afterClaimNotify")
        .post("/workallocation/searchForCompletable")
        .headers(CivilDamagesHeader.headers_810)
        .body(StringBody("{\"searchRequest\":{\"ccdId\":\"${caseId}\",\"eventId\":\"NOTIFY_DEFENDANT_OF_CLAIM\",\"jurisdiction\":\"CIVIL\",\"caseTypeId\":\"UNSPECIFIED_CLAIMS\"}}"))
        .check(status.is(401))
      )
    
        .exec(http("CD_CreateClaim_340_010")
          .get("/data/internal/cases/${caseId}")
          .headers(CivilDamagesHeader.headers_717)
          .check(status.in(200, 201))
        )
    }
      .pause (MinThinkTime, MaxThinkTime)
  //defendant  journey starts
  
  val notifyclaimdetailsevent =
    group ("CD_CreateClaim_350_NotifyDetailsEvent") {
      exec(http("CD_CreateClaim_350_005_Detail")
        .get("/data/internal/cases/${caseId}/event-triggers/NOTIFY_DEFENDANT_OF_CLAIM_DETAILS?ignore-warning=false")
        .headers(CivilDamagesHeader.headers_769)
        .check(status.in(200, 304))
        .check(jsonPath("$.event_token").optional.saveAs("event_token_notifyclaimdetail"))
      )
    
        .exec(http("CD_CreateClaim_350_010_profile")
          .get("/data/internal/profile")
          .headers(CivilDamagesHeader.headers_149)
          .check(status.in(200, 304))
        )
    }
      .pause (MinThinkTime, MaxThinkTime)
  val notifyclaimdetailsupload =
    group ("CD_CreateClaim_360_CLAIM_DETAILSUpload") {
      exec(http("CD_CreateClaim_360_005_upload")
        .post("/data/case-types/UNSPECIFIED_CLAIMS/validate?pageId=NOTIFY_DEFENDANT_OF_CLAIM_DETAILSUpload")
        .headers(CivilDamagesHeader.headers_868)
        .body(StringBody("{\"data\":{\"servedDocumentFiles\":{\"particularsOfClaimText\":\"\",\"particularsOfClaimDocument\":{\"document_url\":\"http://dm-store-perftest.service.core-compute-perftest.internal/documents/${Document_ID}\",\"document_binary_url\":\"http://dm-store-perftest.service.core-compute-perftest.internal/documents/${Document_ID}/binary\",\"document_filename\":\"3MB.pdf\"},\"medicalReport\":[],\"scheduleOfLoss\":[],\"certificateOfSuitability\":[]}},\"event\":{\"id\":\"NOTIFY_DEFENDANT_OF_CLAIM_DETAILS\",\"summary\":\"\",\"description\":\"\"},\"event_data\":{\"servedDocumentFiles\":{\"particularsOfClaimText\":\"\",\"particularsOfClaimDocument\":{\"document_url\":\"http://dm-store-perftest.service.core-compute-perftest.internal/documents/${Document_ID}\",\"document_binary_url\":\"http://dm-store-perftest.service.core-compute-perftest.internal/documents/${Document_ID}/binary\",\"document_filename\":\"3MB.pdf\"},\"medicalReport\":[],\"scheduleOfLoss\":[],\"certificateOfSuitability\":[]}},\"event_token\":\"${event_token_notifyclaimdetail}\",\"ignore_warning\":false,\"case_reference\":\"${caseId}\"}"))
        .check(status.in(200, 304))
      )
        .exec(http("CD_CreateClaim_360_010_profile")
          .get("/data/internal/profile")
          .headers(CivilDamagesHeader.headers_149)
          .check(status.in(200, 304))
        )
    }
      .pause (MinThinkTime, MaxThinkTime)
  val notifyclaimdetailseventsubmit=
    group ("CD_CreateClaim_370_NotifyDetailsEventSubmit") {
      exec(http("CD_CreateClaim_370_EventSubmit")
        .post("/data/cases/${caseId}/events")
        .headers(CivilDamagesHeader.headers_886)
        .body(StringBody("{\"data\":{\"servedDocumentFiles\":{\"particularsOfClaimText\":\"\",\"particularsOfClaimDocument\":{\"document_url\":\"http://dm-store-perftest.service.core-compute-perftest.internal/documents/${Document_ID}\",\"document_binary_url\":\"http://dm-store-perftest.service.core-compute-perftest.internal/documents/${Document_ID}/binary\",\"document_filename\":\"3MB.pdf\"},\"medicalReports\":[],\"scheduleOfLoss\":[],\"certificateOfSuitability\":[],\"other\":[]}},\"event\":{\"id\":\"NOTIFY_DEFENDANT_OF_CLAIM_DETAILS\",\"summary\":\"\",\"description\":\"\"},\"event_token\":\"${event_token_notifyclaimdetail}\",\"ignore_warning\":false}"))
        .check(status.in(200,201))
      )
    }
    .pause (MinThinkTime, MaxThinkTime)
  
  
  val returntocasedetailsafternotifydetails =
    group ("CD_CreateClaim_380_ReturnToCaseDetailsAfterNotifyDetails") {
      exec(http("CD_CreateClaim_380_005_NotifyDetails")
        .post("/workallocation/searchForCompletable")
        .headers(CivilDamagesHeader.headers_894)
        .body(StringBody("{\"searchRequest\":{\"ccdId\":\"${caseId}\",\"eventId\":\"NOTIFY_DEFENDANT_OF_CLAIM_DETAILS\",\"jurisdiction\":\"CIVIL\",\"caseTypeId\":\"UNSPECIFIED_CLAIMS\"}}"))
        .check(status.is(401))
      )
    
        .exec(http("CD_CreateClaim_380_010_case")
          .get("/data/internal/cases/${caseId}")
          .headers(CivilDamagesHeader.headers_717)
          .check(status.in(200, 304))
        )
    }
      .pause (MinThinkTime, MaxThinkTime)
  
  
}
