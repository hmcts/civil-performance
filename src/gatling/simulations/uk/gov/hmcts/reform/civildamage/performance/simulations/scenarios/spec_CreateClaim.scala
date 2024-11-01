package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.spec_CreateClaim_Headers._
import utils._


object spec_CreateClaim {

  val BaseURL = Environment.baseURL
  val IdamURL = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val CreateSpecClaim =
    //    // ============================START========================,
    group("Civil_CreateSpecClaim_10_01_CreateCase") {
      exec(http("StartCase")
        .get("/data/internal/case-types/CIVIL/event-triggers/CREATE_CLAIM_SPEC?ignore-warning=false")
        .headers(headers_28)
        .check(bodyString.saveAs("responseBody"))
        .check(jsonPath("$.event_token").optional.saveAs("event_token"))
        .check(status.is(200)))
    }
      .pause(15)

      // ==================CONTINUE1=================,
      .group("Civil_CreateSpecClaim_10_02_CreateCase") {
        exec(http("CaseChecklist")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECCheckList")
          .headers(Headers.validateHeader)
          .body(ElFileBody("a_CreateClaim_bodies/0030_request.dat")))
      }
      .pause(15)
      .group("Civil_CreateSpecClaim_10_03_CreateCase") {
        exec(http("Eligibility")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECEligibility")
          .headers(Headers.validateHeader)
          .body(ElFileBody("a_CreateClaim_bodies/0030_request.dat")))
      }
      .pause(15)

      .group("Civil_CreateSpecClaim_10_04_CreateCase") {
        exec(http("References")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECReferences")
          .headers(Headers.validateHeader)
          .body(ElFileBody("a_CreateClaim_bodies/0030_request.dat")))
      }
      .pause(15)

      // ======================CLAIM ADDR=========,
      .group("Civil_CreateSpecClaim_10_05_CreateCase") {
        exec(http("PostCodeSearch")
          .get("/api/addresses?postcode=HA11GP")
          .headers(headers_32)
          .check(substring("This record is within England")))
          .pause(15)
      }
      // ============================CLAIM DETAIL===========,
      .group("Civil_CreateSpecClaim_10_06_CreateCase") {
        exec(http("Claimant")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimant")
          .headers(Headers.validateHeader)
          .body(ElFileBody("a_CreateClaim_bodies/0033_request.dat"))
          .check(substring("COMPANY")))
      }
      .pause(15)

      // ==================ANOTHER CLAIMANT===============,
      .group("Civil_CreateSpecClaim_10_07_CreateCase") {
        exec(http("AddAnotherClaimant")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECAddAnotherClaimant")
          .headers(Headers.validateHeader)
          .body(ElFileBody("a_CreateClaim_bodies/0034_request.dat"))
          .check(substring("addApplicant2")))
      }
      .pause(15)

      // ==================CLAMANT EMAIL============,
      .group("Civil_CreateSpecClaim_10_08_CreateCase") {
        exec(http("Notifications")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECNotifications")
          .headers(Headers.validateHeader)
          .body(ElFileBody("a_CreateClaim_bodies/0035_request.dat"))
          .check(substring("type\":\"COMPANY")))
      }

      // ==========================LEGAL REP===============,
      .group("Civil_CreateSpecClaim_10_09_CreateCase") {
        exec(http("ClaimantSolOrg")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimantSolicitorOrganisation")
          .headers(Headers.validateHeader)
          .body(ElFileBody("a_CreateClaim_bodies/0037_request.dat"))
          .check(substring("United Kingdom")))
      }
      .pause(15)

      // ================LEGAL REP POSTAL==============,
      .group("Civil_CreateSpecClaim_10_10_CreateCase") {
        exec(http("CorrespondenceAddress")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECspecCorrespondenceAddress")
          .headers(Headers.validateHeader)
          .body(ElFileBody("a_CreateClaim_bodies/0038_request.dat")))
      }
      .pause(15)

      // =======================DEF ADDR===========,
      .group("Civil_CreateSpecClaim_10_11_CreateCase") {
        exec(http("DefPostCode")
          .get("/api/addresses?postcode=WD171BN")
          .headers(headers_39)
          .check(substring("X_COORDINATE")))
      }
      .pause(15)

    // ========================DEF DETAILS=============,
    .group("Civil_CreateSpecClaim_10_11_CreateCase") {
      exec(http("DefendantDetails")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECDefendant")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/0040_request.dat")))
    }
      .pause(15)
      // ==================DEF LEGAL REP=============,
      .group("Civil_CreateSpecClaim_10_12_CreateCase") {
        exec(http("DefLegalRep")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECLegalRepresentation")
          .headers(Headers.validateHeader)
          .body(ElFileBody("a_CreateClaim_bodies/0041_request.dat")))
      }
      .pause(4)

      .group("Civil_CreateSpecClaim_10_13_CreateCase") {
        exec(http("DefSolOrg")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECDefendantSolicitorOrganisation")
          .headers(Headers.validateHeader)
          .body(ElFileBody("a_CreateClaim_bodies/0042_request.dat")))
      }
      .pause(4)

      .group("Civil_CreateSpecClaim_10_14_CreateCase") {
      exec(http("CDefSolEmail")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECDefendantSolicitorEmail")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/0043_request.dat"))
      )
      }
    .pause(15)
    .group("Civil_CreateSpecClaim_10_15_CreateCase") {
      exec(http("DefAddr")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECspecRespondentCorrespondenceAddress")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/0044_request.dat"))
        .check(substring("civilmoneyclaimsdemo@gmail.com")))
    }
    .pause(15)


    // ============CREATE CLAIM SPECIFIED===============,
    .group("Civil_CreateSpecClaim_10_16_CreateCase") {
      exec(http("AddAnotherDefendant")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECAddAnotherDefendant")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/0045_request.dat")))
    }
      .pause(15)


      // =====================AIRLINE CLAIM===========,
      .group("Civil_CreateSpecClaim_10_17_CreateCase") {
        exec(http("FlightDelayClaim")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECFlightDelayClaim")
          .headers(Headers.validateHeader)
          .body(ElFileBody("a_CreateClaim_bodies/0046_request.dat"))
          .check(substring("United Kingdom")))
      }
      .pause(15)


      // ==================DESC CLAIM===============,
      .group("Civil_CreateSpecClaim_10_18CreateCase") {
        exec(http("CaseDetails")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECDetails")
          .headers(Headers.validateHeader)
          .body(ElFileBody("a_CreateClaim_bodies/0047_request.dat")))
      }
      .pause(15)


      // ===============CLAIM TIMELINE=================,
      .group("Civil_CreateSpecClaim_10_19_CreateCase") {
        exec(http("UploadClaimDoc")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECUploadClaimDocument")
          .headers(Headers.validateHeader)
          .body(ElFileBody("a_CreateClaim_bodies/0048_request.dat"))
          .check(substring("MANUAL")))
      }
      .pause(15)

      .group("Civil_CreateSpecClaim_10_20_CreateCase") {
        exec(http("ClaimTimeline")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimTimeline")
          .headers(Headers.validateHeader)
          .body(ElFileBody("a_CreateClaim_bodies/0049_request.dat")))
      }
      .pause(15)

      // LIST EVIDENCE=================================,
      .group("Civil_CreateSpecClaim_10_21_CreateCase") {
        exec(http("SPECEvidenceList")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECEvidenceList")
          .headers(Headers.validateHeader)
          .body(ElFileBody("a_CreateClaim_bodies/0050_request.dat"))
          .check(substring("expertWitnessEvidence")))
      }
      .pause(15)


   // =========================CLAIM AMOUNT===================,
    .group("Civil_CreateSpecClaim_10_22_CreateCase") {
      exec(http("CREATE_CLAIM_SPECClaimAmount")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimAmount")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/0051_request.dat"))
//        .check(regex("calculatedAmountInPence\":\"(.*?)\"").saveAs("calculatedAmountInPence"))
//        .check(regex("statementOfValueInPennies\":\"(.*?)\"").saveAs("statementOfValueInPennies"))
        .check(bodyString.saveAs("responseBody")))
    }
      .pause(5)

      .group("Civil_CreateSpecClaim_10_23_CreateCase") {
      exec(http("CREATE_CLAIM_SPECClaimAmountDetails")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimAmountDetails")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/0052_request.dat"))
        .check(substring("totalClaimAmount\":\"9000")))
        }
      .pause(15)

      .group("Civil_CreateSpecClaim_10_24_CreateCase") {
        exec(http("CREATE_CLAIM_SPECClaimInterest")
          .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECClaimInterest")
          .headers(Headers.validateHeader)
          .body(ElFileBody("a_CreateClaim_bodies/0053_request.json"))
          .check(substring("isFlightDelayClaim\":\"No")))
      }
      .pause(15)

      .group("Civil_CreateSpecClaim_10_25_CreateCase") {
      exec(http("CREATE_CLAIM_SPECInterestSummary")
        .post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECInterestSummary")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/0054_request.json"))
        .check(substring("PBA0077597")))
        }
      .pause(15)

      .group("Civil_CreateSpecClaim_10_26_CreateCase") {
      exec(http("CREATE_CLAIM_SPECPbaNumber").post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECPbaNumber")
        .headers(Headers.validateHeader)
        .body(ElFileBody("a_CreateClaim_bodies/0055_request.json"))
        .check(substring("calculatedAmountInPence")))
      }
      .pause(16)

      // =======================SOT===============,
      .group("Civil_CreateSpecClaim_10_27_CreateCase") {
      exec(http("StatementOfTruth").post("/data/case-types/CIVIL/validate?pageId=CREATE_CLAIM_SPECStatementOfTruth")
          .headers(headers_57)
          .body(ElFileBody("a_CreateClaim_bodies/0056_request.json"))
          .check(substring("addApplicant2\":\"No"))
          .check(bodyString.saveAs("responseBody")))
      }
      .pause(8)

      // =============================SUBMIT================,
      .group("Civil_CreateSpecClaim_10_28_CreateCase") {
      exec(http("SOT_Submit").post("/data/case-types/CIVIL/cases?ignore-warning=false")
          .headers(headers_57)
          .body(ElFileBody("a_CreateClaim_bodies/0057_request.json"))
          .check(substring("callback_response_status_code\":200"))
          .check(jsonPath("$.event_token").optional.saveAs("event_token"))
          .check(jsonPath("$.id").optional.saveAs("caseId")))
      }
      .pause(40)

  val ClaimFeePayment =
    // ================================service request tab==================================,
    group("Civil_CreateSpecClaim_10_29_CreateCase") {
      exec(http("CaseNo")
        .get("/pay-bulkscan/cases/#{caseId}")
        .headers(headers_331))

        .exec(http("paymentgroups")
          .get("/payments/cases/#{caseId}/paymentgroups")
          .headers(headers_331))

        .exec(http("PayOrder")
          .get("/payments/case-payment-orders?case_ids=#{caseId}")
          .headers(headers_333)
          .check(jsonPath("$.content[0].orderReference").optional.saveAs("OrdRefNo")))
    }
      .pause(10)


    // ==========================================Click Pay Now=======================,
    .group("Civil_CreateSpecClaim_10_29_CreateCase") {
      exec(http("PayNow")
        .get("/payments/pba-accounts")
        .headers(Headers.validateHeader))
    }
      .pause(10)

    // ====================CONFIRM====PAY========================,
    .group("Civil_CreateSpecClaim_10_30_CreateCase") {
        exec(http("ConfirmPayment")
          .post("/payments/service-request/#{OrdRefNo}/pba-payments")
          .headers(headers_335)
          .body(ElFileBody("a_CreateClaim_bodies/0035_request.json")))
      }
.pause(40)


}