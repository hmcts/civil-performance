package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils._

object XUIDefaultJudgment{

  val BaseURL = Environment.baseURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val DefaultJudgment =

    // ========================LANDING PAGE=====================,
    group("XUI_DefaultJudgment_400_LandingPage") {
      exec(http("Land_005_Jurisdictions")
        .get(BaseURL + "/aggregated/caseworkers/:uid/jurisdictions?access=read")
        .headers(Headers.commonHeader)
        .check(substring("callback_get_case_url")))

      .exec(http("Land_010_Organisation")
        .get(BaseURL + "/api/organisation")
        .headers(Headers.commonHeader)
        .check(substring("organisationProfileIds")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ========================SEARCH=====================,
    .group("XUI_DefaultJudgment_410_Search") {
      exec(http("Search_005_WorkBasket")
        .get(BaseURL + "/data/internal/case-types/CIVIL/work-basket-inputs")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-workbasket-input-details.v2+json;charset=UTF-8")
        .check(substring("workbasketInputs")))

      .exec(http("Search_010_CaseReference")
        .post(BaseURL + "/data/internal/searchCases?ctid=CIVIL&use_case=WORKBASKET&view=WORKBASKET&page=1&case_reference=#{caseId}")
        .headers(Headers.commonHeader)
        .body(StringBody("""{"size": 25}""".stripMargin))
        .check(substring("AWAITING_APPLICANT_INTENTION")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ================================OPEN CASE===============================,
    .group("XUI_DefaultJudgment_420_OpenCase") {
      exec(http("OpenCase_005_InternalCases")
        .get(BaseURL + "/data/internal/cases/#{caseId}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("Awaiting Defendant Response")))
      }

    .group("XUI_DefaultJudgment_430_RoleAssignment") {
      exec(http("OpenCase_010_RoleAssignment")
        .post(BaseURL + "/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
        .headers(Headers.commonHeader)
        .check(status.is(204)))
    }

    .group("XUI_DefaultJudgment_440_Jurisdiction") {
      exec(http("OpenCase_015_Jurisdiction")
        .get(BaseURL + "/api/wa-supported-jurisdiction/get")
        .headers(Headers.commonHeader)
        .check(substring("CIVIL")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==============================REQUEST DEFAULT JUDGMENT====================
    .group("XUI_DefaultJudgment_450_RequestDefaultJudgment") {
      exec(http("ReqDefJud_005_Jurisdiction")
        .get(BaseURL + "/workallocation/case/tasks/#{caseId}/event/DEFAULT_JUDGEMENT_SPEC/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(Headers.commonHeader)
        .check(substring("task_required_for_event")))

      .exec(http("ReqDefJud_010_Profile")
        .get(BaseURL + "/data/internal/profile")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-user-profile.v2+json;charset=UTF-8")
        .check(substring("#{claimantuser}")))

      .exec(http("ReqDefJud_015_IgnoreWarning")
        .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/DEFAULT_JUDGEMENT_SPEC?ignore-warning=false")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
        .check(substring("Request Default Judgment"))
        .check(regex("partyID\":\"(.*?)\"").saveAs("repPartyID"))
        .check(regex("partyName\":\"(.*?)\"").saveAs("partyName"))
        .check(jsonPath("$.event_token").saveAs("event_token")))
      .exitHereIf(session => !session.contains("repPartyID"))

      .exec(http("ReqDefJud_020_Jurisdiction")
        .get(BaseURL + "/workallocation/case/tasks/#{caseId}/event/DEFAULT_JUDGEMENT_SPEC/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(Headers.commonHeader)
        .check(substring("task_required_for_event")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ==============================SELECT DEFENDANT====================
    .group("XUI_DefaultJudgment_460_SelectDefendant") {
      exec(http("SelectDefendant")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFAULT_JUDGEMENT_SPECdefendantDetailsSpec")
        .headers(Headers.validateHeader)
        .body(ElFileBody("bodies/XUIDefaultJudgment/selectDefendant.json"))
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/case-types/CIVIL/validate?" +
          "pageId=DEFAULT_JUDGEMENT_SPECdefendantDetailsSpec")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ===========================STATEMENTS ABOUT DEFENDANT====================
    .group("XUI_DefaultJudgment_470_StatementsAboutDefendant") {
      exec(http("CertifyStatements")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFAULT_JUDGEMENT_SPECshowCertifyStatementSpec")
        .headers(Headers.validateHeader)
        .body(ElFileBody("bodies/XUIDefaultJudgment/certifyStatements.json"))
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/case-types/CIVIL/validate?" +
          "pageId=DEFAULT_JUDGEMENT_SPECshowCertifyStatementSpec")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ===========================HAS DEFENDANT PAID====================
    .group("XUI_DefaultJudgment_480_HasDefendantPaid") {
      exec(http("HasDefendantPaid")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFAULT_JUDGEMENT_SPECclaimPartialPayment")
        .headers(Headers.validateHeader)
        .body(ElFileBody("bodies/XUIDefaultJudgment/hasDefendantPaid.json"))
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/case-types/CIVIL/validate?" +
          "pageId=DEFAULT_JUDGEMENT_SPECclaimPartialPayment")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ===========================PAYMENT BREAKDOWN====================
    .group("XUI_DefaultJudgment_490_PaymentBreakdown") {
      exec(http("PaymentBreakdown")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFAULT_JUDGEMENT_SPECpaymentBreakdown")
        .headers(Headers.validateHeader)
        .body(ElFileBody("bodies/XUIDefaultJudgment/paymentBreakdown.json"))
        .check(substring("The judgment will order DEFENDANT to pay")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ===========================PAYMENT TYPE====================
    .group("XUI_DefaultJudgment_500_PaymentType") {
      exec(http("PaymentType")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DEFAULT_JUDGEMENT_SPECpaymentType")
        .headers(Headers.validateHeader)
        .body(ElFileBody("bodies/XUIDefaultJudgment/paymentType.json"))
        .check(substring("paymentTypeSelection")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    // ===========================CHECK YOUR ANSWERS====================
    .group("XUI_DefaultJudgment_510_CheckYourAnswers") {
      exec(http("CheckYourAnswers_005_Submit")
        .post(BaseURL + "/data/cases/#{caseId}/events")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
        .body(ElFileBody("bodies/XUIDefaultJudgment/checkYourAnswers.json"))
        .check(substring("The defendant will be served with the Default Judgment")))

      .exec(http("CheckYourAnswers_010_Cases")
        .get(BaseURL + "/data/internal/cases/#{caseId}")
        .headers(Headers.validateHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(substring("http://gateway-ccd.perftest.platform.hmcts.net/internal/cases/#{caseId}")))
    }
    .pause(MinThinkTime, MaxThinkTime)

}