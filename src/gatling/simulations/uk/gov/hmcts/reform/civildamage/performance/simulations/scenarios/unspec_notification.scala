package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios.utils.Headers.commonHeader
import utils._
import utils.ua_unspec_CreateClaim_Headers._

import scala.concurrent.duration._

object unspec_notification {

    val BaseURL = Environment.baseURL
    val IdamURL = Environment.idamURL
    val MinThinkTime = Environment.minThinkTime
    val MaxThinkTime = Environment.maxThinkTime

//
//  val CreateUnSpecClaim =
//    // =======================CREATECASE=======================,
//    group("CivilDamage_UnSpecClaim_10_CreateCase") {
//      exec(http("005_CreateCase")
//        .get("/aggregated/caseworkers/:uid/jurisdictions?access=create")
//        .headers(headers_27))
//    }
//      .pause(5)
//      // =======================START.CASE=======================,
//      .group("CivilDamage_UnSpecClaim_10_CreateCase") {
//        exec(http("005_StartCase")
//          .get("/data/internal/case-types/CIVIL/event-triggers/CREATE_CLAIM?ignore-warning=false")
//          .headers(headers_28)
//          .check(jsonPath("$.event_token").optional.saveAs("event_token"))
//          .check(status.is(200)))
//      }
//      .pause(10)
//      // ======================ISSUE_CIVIL_PROCEEDINGS=======================,
//      .group("CivilDamage_UnSpecClaim_10_CreateCase") {
//        exec(http("005_Eligibility")
//          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMEligibility")
//          .headers(headers_29)
//          .body(ElFileBody("ua_unspec_CreateClaim_Bodies/0029_request.dat")))
//      }
//      .pause(8)
//      // =======================FILE REF=======================,
//      .group("CivilDamage_UnSpecClaim_10_CreateCase") {
//        exec(http("005_References")
//          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMReferences")
//          .headers(headers_30)
//          .body(ElFileBody("ua_unspec_CreateClaim_Bodies/0030_request.dat")))
//      }
//      .pause(10)
//      // =======================COURT LOCATION=======================,
//      .group("CivilDamage_UnSpecClaim_10_CreateCase") {
//        exec(http("005_CLAIMCourt")
//          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMCourt")
//          .headers(headers_31)
//          .body(ElFileBody("ua_unspec_CreateClaim_Bodies/0031_request.dat")))
//      }
//      .pause(10)
//      // =======================CL_ADDR=======================,
//      .group("CivilDamage_UnSpecClaim_10_CreateCase") {
//        exec(http("005_ClaimantPostcode")
//          .get("/api/addresses?postcode=HA11GP")
//          .headers(headers_32))
//      }
//      .pause(10)
//      // =======================CL_DETAILS=======================,
//      .group("CivilDamage_UnSpecClaim_10_CreateCase") {
//        exec(http("005_CLAIMClaimant")
//          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMClaimant")
//          .headers(headers_33)
//          .body(ElFileBody("ua_unspec_CreateClaim_Bodies/0033_request.dat")))
//          .pause(17)
//      }
//      // =======================CL_MINOR=======================,
//      .group("CivilDamage_UnSpecClaim_10_CreateCase") {
//      exec(http("005_ClaimantFriend")
//        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMClaimantLitigationFriend")
//        .headers(headers_34)
//        .body(ElFileBody("ua_unspec_CreateClaim_Bodies/0034_request.dat")))
//      }
//  .pause(10)
//  // =======================NOTIFICATION EMAIL=======================,
//  .group("CivilDamage_UnSpecClaim_10_CreateCase") {
//    exec(http("005_CLAIMNotify")
//      .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMNotifications")
//      .headers(headers_35)
//      .body(ElFileBody("ua_unspec_CreateClaim_Bodies/0035_request.dat")))
//  }
//  .pause(15)
//  // =======================CL_REP_ORG=======================,
//  .group("CivilDamage_UnSpecClaim_10_CreateCase") {
//    exec(http("Civil_CreateClaim_130_ClaimantSolOrg")
//      .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMClaimantSolicitorOrganisation")
//      .headers(headers_37)
//      .body(ElFileBody("ua_unspec_CreateClaim_Bodies/0037_request.dat")))
//  }
//  .pause(10)
//    // ======================CL REP ADDR=======================,
//    .group("CivilDamage_UnSpecClaim_10_CreateCase") {
//      exec(http("005_ClaimantSolServiceAddr")
//        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMClaimantSolicitorServiceAddress")
//        .headers(headers_38)
//        .body(ElFileBody("ua_unspec_CreateClaim_Bodies/0038_request.dat")))
//    }
//  .pause(10)
//
//  // =======================ANOTHER CL=======================,
//  .group("CivilDamage_UnSpecClaim_10_CreateCase") {
//    exec(http("005_AddAnotherClaimant")
//      .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMAddAnotherClaimant")
//      .headers(headers_39)
//      .body(ElFileBody("ua_unspec_CreateClaim_Bodies/0039_request.dat")))
//  }
//  .pause(11)
//  // =======================DF ADDR=======================,
//  .group("CivilDamage_UnSpecClaim_10_CreateCase") {
//    exec(http("005_SolPostcode")
//      .get("/api/addresses?postcode=WD171BN")
//      .headers(headers_40))
//  }
//  .pause(27)
//    // =======================DF DETAILS=======================,
//    .group("CivilDamage_UnSpecClaim_10_CreateCase") {
//      exec(http("005_DefDetails")
//        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMDefendant")
//        .headers(headers_41)
//        .body(ElFileBody("ua_unspec_CreateClaim_Bodies/0041_request.dat")))
//    }
//  .pause(17)
//
//  // =======================DF REP=======================,
//  .group("CivilDamage_UnSpecClaim_10_CreateCase") {
//    exec(http("005_LegalRep")
//      .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMLegalRepresentation")
//      .headers(headers_42)
//      .body(ElFileBody("ua_unspec_CreateClaim_Bodies/0042_request.dat")))
//  }
//  .pause(25)
//  // =======================DF REP ORG=======================,
//  .group("CivilDamage_UnSpecClaim_10_CreateCase") {
//    exec(http("005_DefSolOrg")
//      .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMDefendantSolicitorOrganisation")
//      .headers(headers_43)
//      .body(ElFileBody("ua_unspec_CreateClaim_Bodies/0043_request.dat")))
//  }
//  .pause(10)
//  // =======================DF ADDR=======================,
//  .group("CivilDamage_UnSpecClaim_10_CreateCase") {
//    exec(http("005_ClaimantSolicitorAddr")
//      .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMDefendantSolicitorServiceAddress")
//      .headers(headers_44)
//      .body(ElFileBody("ua_unspec_CreateClaim_Bodies/0044_request.dat")))
//  }
//  .pause(9)
//  // =======================DF REP EMAIL=======================,
//  .group("CivilDamage_UnSpecClaim_10_CreateCase") {
//    exec(http("005_DefSolEmail")
//      .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMDefendantSolicitorEmail")
//      .headers(headers_45)
//      .body(ElFileBody("ua_unspec_CreateClaim_Bodies/0045_request.dat")))
//  }
//  .pause(9)
//  // =======================ANOTHER DF=======================,
//  .group("CivilDamage_UnSpecClaim_10_CreateCase") {
//    exec(http("005_AddAnotherDef")
//      .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMAddAnotherDefendant")
//      .headers(headers_46)
//      .body(ElFileBody("ua_unspec_CreateClaim_Bodies/0046_request.dat")))
//  }
//  .pause(11)
//  // =======================CLAIM TYPE=======================,
//  .group("CivilDamage_UnSpecClaim_10_CreateCase") {
//    exec(http("005_ClaimType")
//      .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMClaimType")
//      .headers(headers_47)
//      .body(ElFileBody("ua_unspec_CreateClaim_Bodies/0047_request.dat")))
//  }
//  .pause(12)
//  // =======================DESC CLAIM=======================,
//  .group("CivilDamage_UnSpecClaim_10_CreateCase") {
//    exec(http("005_CLAIMDetails")
//      .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMDetails")
//      .headers(headers_48)
//      .body(ElFileBody("ua_unspec_CreateClaim_Bodies/0048_request.dat")))
//  }
//  .pause(14)
//  // =======================UPLOAD CLAIM DETAILS=======================,
//  .group("CivilDamage_UnSpecClaim_10_CreateCase") {
//    exec(http("005_UploadParticularsOfClaim")
//      .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMuploadParticularsOfClaim")
//      .headers(headers_49)
//      .body(ElFileBody("ua_unspec_CreateClaim_Bodies/0049_request.dat")))
//  }
//  .pause(11)
//  // =======================CLAIM VAL=======================,
//  .group("CivilDamage_UnSpecClaim_10_CreateCase") {
//  exec(http("005_CLAIMClaimValue")
//      .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMClaimValue")
//      .headers(headers_50)
//      .body(ElFileBody("ua_unspec_CreateClaim_Bodies/0050_request.dat"))
//    .check(regex("calculatedAmountInPence\":\"(.*?)\"").saveAs("calculatedAmountInPence"))
//    .check(regex("statementOfValueInPennies\":\"(.*?)\"").saveAs("statementOfValueInPennies"))
//    .check(bodyString.saveAs("responseBody"))
//  )
//  }
//
//  .pause(17)
//  // =======================CLAIM FEE=======================,
//  .group("CivilDamage_UnSpecClaim_10_CreateCase") {
//  exec(http("005_CLAIMPbaNo")
//      .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMPbaNumber")
//      .headers(headers_51)
//      .body(ElFileBody("ua_unspec_CreateClaim_Bodies/0051_request.dat")))
//  }
//  .pause(19)
//  // =======================SOT=======================,
//  .group("CivilDamage_UnSpecClaim_10_CreateCase") {
//  exec(http("005_CLAIMStatementOfTruth")
//      .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIMStatementOfTruth")
//      .headers(headers_52)
//      .body(ElFileBody("ua_unspec_CreateClaim_Bodies/0052_request.dat")))
//  }
//
//  .pause(19)
//  // =======================SOT SUBMIT=======================,
//  .group("CivilDamage_UnSpecClaim_10_CreateCase") {
//  exec(http("005_SubmitClaim")
//      .post("/data/case-types/CIVIL/cases?ignore-warning=false")
//      .headers(headers_53)
//      .body(ElFileBody("ua_unspec_CreateClaim_Bodies/0053_request.dat"))
//      .check(substring("callback_response_status_code\":200"))
//      .check(jsonPath("$.event_token").optional.saveAs("event_token"))
//      .check(jsonPath("$.id").optional.saveAs("caseId")))
//  }
//  .pause(50)




  val NotifyClaim =


      // =======================NOTIFY CLAIM=======================,
    group("Civil_UnSpecClaim_10_NotifyDEF") {
      exec(http("005_jurisdiction")
        .get("/workallocation/case/tasks/#{caseId}/event/NOTIFY_DEFENDANT_OF_CLAIM/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(headers_110))
        .pause(5)
        .exec(http("010_warning")
          .get("/data/internal/cases/#{caseId}/event-triggers/NOTIFY_DEFENDANT_OF_CLAIM?ignore-warning=false")
          .headers(headers_111)
          .check(status.in(200, 304))
          .check(jsonPath("$.event_token").optional.saveAs("event_token")))

        .exec(http("015_Profile")
          .get("/data/internal/profile")
          .headers(headers_112))

        .exec(http("020_jurisdiction")
          .get("/workallocation/case/tasks/#{caseId}/event/NOTIFY_DEFENDANT_OF_CLAIM/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(headers_113))
    }
        .pause(10)

      // =======================DF REP NOTIFY=======================,
  .group("Civil_UnSpecClaim_10_NotifyDEF") {
        exec(http("005_CLAIMAccessGrantedWarning")
          .post("/data/case-types/CIVIL/validate?pageId=NOTIFY_DEFENDANT_OF_CLAIMAccessGrantedWarning")
          .headers(headers_114)
          .body(ElFileBody("ua_unspec_CreateClaim_Bodies/0114_request.dat")))
      }
          .pause(10)

      // =======================SUBMIT NOTIFY=======================,
      .group("Civil_UnSpecClaim_10_NotifyDEF") {
        exec(http("330_SubmitNotification")
          .post("/data/cases/#{caseId}/events")
          .headers(headers_115)
          .body(ElFileBody("ua_unspec_CreateClaim_Bodies/0115_request.dat")))

//          .exec(http("request_116")
//            .get("/data/internal/cases/#{caseId}")
//            .headers(headers_116))
      }
          .pause(10)

      // =======================CLOSE & RETURN TO CASE DETAILS=======================,
      .group("Civil_UnSpecClaim_10_NotifyDEF") {
        exec(http("005_jurisdiction")
          .get("/api/wa-supported-jurisdiction/get")
          .headers(headers_117))

          .exec(http("010_LabellingRoleAssignment")
            .post("/api/role-access/roles/manageLabellingRoleAssignment/#{caseId}")
            .headers(headers_118)
            .body(ElFileBody("ua_unspec_CreateClaim_Bodies/0118_request.bin")))
      }
      .pause(10)


  val NotifyClaimDetails =
group("Civil_UnSpecClaim_10_NotifyDetails") {
  // =======================NOTIFY CLAIM DETAILS=======================,
  exec(http("005_jurisdiction")
    .get("/workallocation/case/tasks/#{caseId}/event/NOTIFY_DEFENDANT_OF_CLAIM_DETAILS/caseType/CIVIL/jurisdiction/CIVIL")
    .headers(headers_131))

    .exec(http("010_IgnoreWarning")
      .get("/data/internal/cases/#{caseId}/event-triggers/NOTIFY_DEFENDANT_OF_CLAIM_DETAILS?ignore-warning=false")
      .headers(headers_132)
      .check(status.in(200, 304))
      .check(jsonPath("$.event_token").optional.saveAs("event_token")))

    .exec(http("015_profile")
      .get("/data/internal/profile")
      .headers(headers_133))

    .exec(http("020_jurisdiction")
      .get("/workallocation/case/tasks/#{caseId}/event/NOTIFY_DEFENDANT_OF_CLAIM_DETAILS/caseType/CIVIL/jurisdiction/CIVIL")
      .headers(headers_134))
}
      .pause(19)

      // =======================UPLOAD DOC PARTICULARS=======================,
      .group("Civil_UnSpecClaim_10_NotifyDetails") {
        exec(http("005_DetailsUpload")
          .post("/data/case-types/CIVIL/validate?pageId=NOTIFY_DEFENDANT_OF_CLAIM_DETAILSUpload")
          .headers(headers_135)
          .body(ElFileBody("ua_unspec_CreateClaim_Bodies/0135_request.dat")))
      }
      .pause(10)

      // =======================SUBMIT CLAIM DETAILS=======================,
      .group("Civil_UnSpecClaim_10_NotifyDetails") {
        exec(http("005_SubmitNotifyDetails")
          .post("/data/cases/#{caseId}/events")
          .headers(headers_136)
          .body(ElFileBody("ua_unspec_CreateClaim_Bodies/0136_request.dat")))
      }
}
