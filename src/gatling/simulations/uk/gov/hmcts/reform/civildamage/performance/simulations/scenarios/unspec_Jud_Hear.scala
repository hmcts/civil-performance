package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.unspec_Jud_Hear_Headers._
import utils._

import scala.concurrent.duration.DurationInt

object unspec_Jud_Hear {
	val BaseURL = Environment.baseURL
	val IdamURL = Environment.idamURL
	val MinThinkTime = Environment.minThinkTime
	val MaxThinkTime = Environment.maxThinkTime

  val sdoJudge =

      exec(_.setAll(
            "HearingYear" -> Common.getYearFuture(),
            "HearingDay" -> Common.getDay(),
            "HearingMonth" -> Common.getMonth(),
            "Plus4WeeksDay" -> Common.getPlus4WeeksDay(),
            "Plus4WeeksMonth" -> Common.getPlus4WeeksMonth(),
            "Plus4WeeksYear" -> Common.getPlus4WeeksYear(),
            "Plus6WeeksDay" -> Common.getPlus6WeeksDay(),
            "Plus6WeeksMonth" -> Common.getPlus6WeeksMonth(),
            "Plus6WeeksYear" -> Common.getPlus6WeeksYear(),
            "Plus8WeeksDay" -> Common.getPlus8WeeksDay(),
            "Plus8WeeksMonth" -> Common.getPlus8WeeksMonth(),
            "Plus8WeeksYear" -> Common.getPlus8WeeksYear(),
            "Plus10WeeksDay" -> Common.getPlus10WeeksDay(),
            "Plus10WeeksMonth" -> Common.getPlus10WeeksMonth(),
            "Plus10WeeksYear" -> Common.getPlus10WeeksYear(),
            "Plus12WeeksDay" -> Common.getPlus12WeeksDay(),
            "Plus12WeeksMonth" -> Common.getPlus12WeeksMonth(),
            "Plus12WeeksYear" -> Common.getPlus12WeeksYear(),
            "LRrandomString" -> Common.randomString(5)
      ))


      // =======================OPEN CASE=======================,
     .group("Civil_40_UnSpecClaim_SDO") {
      exec(http("005_OpenCase")
          .get(BaseURL + "/data/internal/cases/#{caseId}")
          .headers(headers_50))
      }
      .pause(144.milliseconds)

      // =======================TASK TAB=======================,

      .group("Civil_40_UnSpecClaim_SDO") {
        exec(http("005_TaskTab")
          .post(BaseURL + "/workallocation/case/task/#{caseId}")
          .headers(headers_64).header("X-Xsrf-Token", "#{xsrf_token}")
          .body(ElFileBody("ud_ue_jud_hear_bodies/0064_request.json"))
          .check(jsonPath("$[0].id").saveAs("JudgeId")))
      }
          // =======================ASIGN TO ME=======================,

      .group("Civil_40_UnSpecClaim_SDO") {
        exec(http("005_AssignToMe")
          .post(BaseURL + "/workallocation/task/#{JudgeId}/claim")
          .headers(headers_71)
          .body(ElFileBody("ud_ue_jud_hear_bodies/0071_request.bin")))
      }
          .pause(3)

          // =======================FAST TRACK=======================,

      .group("Civil_40_UnSpecClaim_SDO") {
        exec(http("005_FastTrack")
          .get(BaseURL + "/cases/case-details/#{caseId}/trigger/CREATE_SDO/CREATE_SDOFastTrack?tid=#{JudgeId}")
          .headers(headers_78))
          .pause(820.milliseconds)

          .exec(Common.configurationui)
          .exec(Common.configUI)
          .exec(Common.TsAndCs)
          .exec(Common.userDetails)
          .exec(Common.isAuthenticated)

          .exec(http("request_83")
            .get(BaseURL + "/api/monitoring-tools")
            .headers(headers_80))

          .exec(http("request_85")
            .get(BaseURL + "/workallocation/case/tasks/#{caseId}/event/CREATE_SDO/caseType/CIVIL/jurisdiction/CIVIL")
            .headers(headers_85))

          .exec(http("request_88")
            .get(BaseURL + "/data/internal/cases/#{caseId}")
            .headers(headers_88))

          .pause(161.milliseconds)
          .exec(http("request_89")
            .get(BaseURL + "/data/internal/profile")
            .headers(headers_89))


          .exec(http("request_90")
            .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/CREATE_SDO?ignore-warning=false")
            .headers(headers_90)
            .check(jsonPath("$.event_token").saveAs("event_token")))
          .pause(193.milliseconds)
      }
          .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("xsrf_token")))

          // ===========================SDO AMOUNT ===========================,
      .group("Civil_40_UnSpecClaim_SDO") {
        exec(http("005_SDO_Amount")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_SDOSDO")
          .headers(Headers.validateHeader).header("X-Xsrf-Token", "#{xsrf_token}")
          .body(ElFileBody("ud_ue_jud_hear_bodies/0100_request.dat")))
      }
          .pause(449.milliseconds)

          // =======================ALLOCATE FAST TRACK=======================,

      .group("Civil_40_UnSpecClaim_SDO") {
        exec(http("005_ClaimsTrack")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_SDOClaimsTrack")
          .headers(Headers.validateHeader).header("X-Xsrf-Token", "#{xsrf_token}")
          .body(ElFileBody("ud_ue_jud_hear_bodies/0113_request.dat")))
      }
          // =======================ORDER DETAILS=======================,

      .group("Civil_40_UnSpecClaim_SDO") {
        exec(http("005_FastTrack")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_SDOFastTrack")
          .headers(Headers.validateHeader).header("X-Xsrf-Token", "#{xsrf_token}")
          .body(ElFileBody("ud_ue_jud_hear_bodies/0128_request.dat"))
          .check(jsonPath("$.data.sdoOrderDocument.createdDatetime").saveAs("createdDatetime"))
          .check(jsonPath("$.data.sdoOrderDocument.documentLink.document_url").saveAs("sdoDocument_url"))
          .check(jsonPath("$.data.sdoOrderDocument.documentLink.document_hash").saveAs("sdoDocument_hash"))
          .check(jsonPath("$.data.sdoOrderDocument.documentName").saveAs("sdoDocumentName"))
          .check(jsonPath("$.data.sdoOrderDocument.documentSize").saveAs("documentSize")))
      }
              .pause(265.milliseconds)

              // =======================VIEW DIRECTIONAL ORDER=======================,
      .group("Civil_40_UnSpecClaim_SDO") {
        exec(http("005_OrderPreview")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CREATE_SDOOrderPreview")
          .headers(Headers.validateHeader)
          .body(ElFileBody("ud_ue_jud_hear_bodies/0136_request.dat")))
      }
              .pause(2)

              // =======================SUBMIT SDO=======================,
      .group("Civil_40_UnSpecClaim_SDO") {
        exec(http("005_SubmitSDO")
          .get(BaseURL + "/workallocation/task/#{JudgeId}")
          .headers(headers_141))
      }
      .pause(2)

      .group("Civil_40_UnSpecClaim_SDO") {
        exec( http("005_SubmitSDO")
        .post(BaseURL + "/data/cases/#{caseId}/events")
        .headers(headers_144)
        .body(ElFileBody("ud_ue_jud_hear_bodies/0144_request.dat")))
      }
        .pause(921.milliseconds)


      val HearingAdmin =
        exec(_.setAll(
          "CaseProgRandomString" -> Common.randomString(5),
          "Plus2WeeksDay" -> Common.getPlus2WeeksDay(),
          "Plus2WeeksMonth" -> Common.getPlus2WeeksMonth(),
          "Plus2WeeksYear" -> Common.getPlus2WeeksYear(),
          "EvidenceYear" -> Common.getYear(),
          "EvidenceDay" -> Common.getDay(),
          "EvidenceMonth" -> Common.getMonth())
        )

      // =======================OPEN CASE=======================,
      .group("Civil_50_UnSpecClaim_HearingAdmin") {
        exec(http("005_OpenCase")
          .get(BaseURL + "/data/internal/cases/#{caseId}")
          .headers(headers_189))
          .pause(167.milliseconds)

      }

          .pause(5)
      // =======================HEARING NOTICE DROPDOWN=======================,

    .group("Civil_50_UnSpecClaim_HearingAdmin") {
      exec(http("005_jurisdiction")
        .get(BaseURL + "/workallocation/case/tasks/#{caseId}/event/HEARING_SCHEDULED/caseType/CIVIL/jurisdiction/CIVIL")
        .headers(headers_232))

        .exec(http("010_RESPONSEExperts")
          .get(BaseURL + "/data/internal/profile")
          .headers(headers_233))
        .pause(2)

        .exec(http("015_IgnoreWarning")
          .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/HEARING_SCHEDULED?ignore-warning=false")
          .headers(headers_236)
          .check(jsonPath("$.event_token").saveAs("event_token"))
        )

        .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("xsrf_token")))

    .pause(5)
      // =======================SMALL CLAIM=======================,

      .group("Civil_50_UnSpecClaim_HearingAdmin") {
        exec(http("005_HearingNoticeSelect")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=HEARING_SCHEDULEDHearingNoticeSelect")
          .headers(Headers.validateHeader)
          .body(ElFileBody("ud_ue_jud_hear_bodies/0246_request.dat")))
      }
      .pause(5)
      // =======================LISTING=======================,

      .group("Civil_50_UnSpecClaim_HearingAdmin") {
        exec(http("005_ListingOrRelisting")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=HEARING_SCHEDULEDListingOrRelisting")
          .headers(Headers.validateHeader)
          .body(ElFileBody("ud_ue_jud_hear_bodies/0251_request.dat")))
      }
      .pause(3)

          //
    // =======================NOTICE LETTER INFO=======================,
    .group("Civil_50_UnSpecClaim_HearingAdmin") {
      exec(http("005_HearingInformation")
        .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=HEARING_SCHEDULEDHearingInformation")
        .headers(Headers.validateHeader)
        .body(ElFileBody("ud_ue_jud_hear_bodies/0280_request.dat")))
    }
        .pause(768.milliseconds)

      // =======================SUBMIT=======================,

    .group("Civil_50_UnSpecClaim_HearingAdmin") {
      exec(http("005_Submit")
        .post(BaseURL + "/data/cases/#{caseId}/events")
        .headers(headers_287)
        .body(ElFileBody("ud_ue_jud_hear_bodies/0287_request.dat")))
    }

}
}