
package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.SDOCivilProg.sdodrhfeeder
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, Common, Environment}

import java.io.{BufferedWriter, FileWriter}

object CUIR2CaseProgression {

  val BaseURL = Environment.baseURL
  val IdAMURL = Environment.idamURL
  val IdamUrl = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val caseFeeder=csv("caseIds.csv").circular
  val cpfulltestFeeder=csv("cpcaseIds.csv").circular
  

  /*======================================================================================
         HearingNotice - Centre Admin
==========================================================================================*/
  val HearingNotice =
    feed(cpfulltestFeeder)
    .exec(_.setAll(
      "CaseProgRandomString" -> Common.randomString(5),
      "Plus2WeeksDay" -> Common.getPlus2WeeksDay(),
      "Plus2WeeksMonth" -> Common.getPlus2WeeksMonth(),
      "Plus2WeeksYear" -> Common.getPlus2WeeksYear(),
      "EvidenceYear" -> Common.getYear(),
      "EvidenceDay" -> Common.getDay(),
      "EvidenceMonth" -> Common.getMonth())
    )

      /*======================================================================================
                        * Civil Progression - Search Case
      ==========================================================================================*/
      .group("CivilCP_HearingNotice_030_SearchCase") {
          exec(http("CivilCP_HearingNotice_030_SearchCase")
            .get(BaseURL + "/data/internal/cases/#{caseId}")
            .headers(CivilDamagesHeader.MoneyClaimNav)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
            .check(substring("Summary")))
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      /*======================================================================================
               *  Civil Progression - '	Directions - Hearing Notice
    ==========================================================================================*/
      .group("CivilCP_HearingNotice_040_ScheduleHearing") {
        exec(http("CivilCP_HearingNotice_040_005_ScheduleHearing")
          .get("/workallocation/case/tasks/#{caseId}/event/HEARING_SCHEDULED/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/json")
          .check(substring("task_required_for_event"))
        )
          
          .exec(http("CivilCP_HearingNotice_040_010_ScheduleHearing")
            .get("/data/internal/cases/#{caseId}/event-triggers/HEARING_SCHEDULED?ignore-warning=false")
            .headers(CivilDamagesHeader.MoneyClaimNav)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
            .check(substring("HEARING_SCHEDULED"))
            .check(jsonPath("$.event_token").saveAs("event_token"))
          )
          .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))
  
        .exec(http("CivilCP_HearingNotice_040_015_ScheduleHearing")
          .get("/workallocation/case/tasks/#{caseId}/event/HEARING_SCHEDULED/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/json")
           .check(substring("task_required_for_event"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
             *  *  Civil Progression - '	What hearing notice do you want to create?
  ==========================================================================================*/
      .group("CivilCP_HearingNotice_060_WhatHearingNotice") {
        exec(http("CivilCP_HearingNotice_060_005_WhatHearingNotice")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=HEARING_SCHEDULEDHearingNoticeSelect")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/CaseProg/WhatHearingNotice.json"))
          .check(substring("HEARING_SCHEDULEDHearingNoticeSelect"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
           *  *  Civil Progression - '	Is this a listing or a relisting?
==========================================================================================*/
      .group("CivilCP_HearingNotice_070_ListOrRelisting") {
        exec(http("CivilCP_HearingNotice_070_005_ListOrRelisting")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=HEARING_SCHEDULEDListingOrRelisting")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/CaseProg/ListOrRelisting.json"))
          .check(substring("listingOrRelisting"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
         *  *  Civil Progression - '	Hearing Scheduled Details
==========================================================================================*/
      .group("CivilCP_HearingNotice_080_HearingDetails") {
        exec(http("CivilCP_HearingNotice_080_005_HearingDetails")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=HEARING_SCHEDULEDHearingDetails")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/CaseProg/HearingDetails.json"))
          .check(substring("validate?pageId=HEARING_SCHEDULEDHearingDetails"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
       *  *  Civil Progression - '	Hearing Schedule Extra Details
==========================================================================================*/
      .group("CivilCP_HearingNotice_090_HearingExtraDetails") {
        exec(http("CivilCP_HearingNotice_090_005_HearingExtraDetails")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=HEARING_SCHEDULEDHearingInformation")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/CaseProg/HearingExtraDetails.json"))
          .check(substring("information"))
        )

      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
         *  Civil Progression - '	Hearing Schedule Submit
==========================================================================================*/
      .group("CivilCP_HearingNotice_100_HearingScheduleSubmit") {
        exec(http("CivilCP_HearingNotice_100_005_HearingScheduleSubmit")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .header("Experimental", "true")
          .body(ElFileBody("bodies/CaseProg/HearingScheduleSubmit.json"))
          .check(substring("confirmation_header"))
        )
          .exec { session =>
            val fw = new BufferedWriter(new FileWriter("HearingScheduled.csv", true))
            try {
              fw.write(session("caseId").as[String] + "\r\n")
            } finally fw.close()
            session
          }
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  /*======================================================================================
       Final / General Orders - Judge
  ==========================================================================================*/
  
  
  val FinalGeneralOrders =
    feed(cpfulltestFeeder)
    .exec(_.setAll(
      "CaseProgRandomString" -> Common.randomString(5),
      "EvidenceYear" -> Common.getCurrentYear(),
      "EvidenceMonth" -> Common.getCurrentMonth(),
      "EvidenceDay" -> Common.getCurrentDay())
    )
      
      /*======================================================================================
                        * Civil Progression - Search Case
      ==========================================================================================*/
      .group("CivilCP_FinalOrders_110_SearchCase") {
        exec(http("CivilCP_FinalOrders_110_005_SearchCase")
          .get(BaseURL + "/data/internal/cases/#{caseId}")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
          .check(substring("Summary")))
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
                 *  Civil Progression - 'Make An Order' event
      ==========================================================================================*/
      .group("CivilCP_FinalOrders_120_MakeAnOrder") {
        exec(http("CivilCP_FinalOrders_120_005_MakeAnOrder")
          .get("/workallocation/case/tasks/{caseId}/event/GENERATE_DIRECTIONS_ORDER/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .check(substring("task_required_for_event"))
        )
          
          .exec(http("CivilCP_FinalOrders_120_010_MakeAnOrder")
            .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/GENERATE_DIRECTIONS_ORDER?ignore-warning=false")
            .headers(CivilDamagesHeader.headers_notify)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
            .check(substring("GENERATE_DIRECTIONS_ORDER"))
            //  .check(jsonPath("$.case_fields[4].formatted_value.partyID").saveAs("repPartyID"))
            //  .check(jsonPath("$.case_fields[4].formatted_value.partyName").saveAs("partyName"))
            .check(jsonPath("$.event_token").saveAs("event_token"))
          )
          .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      /*======================================================================================
     *  Civil Progression - Final order selection
  ==========================================================================================*/
      .group("CivilCP_FinalOrders_130_OrderSelection") {
        exec(http("CivilCP_FinalOrders_130_005_OrderSelection")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=GENERATE_DIRECTIONS_ORDERFinalOrderSelect")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/CaseProg/OrderSelection.json"))
          // .check(substring("finalOrderDocument"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
           *  Civil Progression - Recitals and order
  ==========================================================================================*/
      .group("CivilCP_FinalOrders_140_RecitalsAndOrder") {
        exec(http("CivilCP_FinalOrders_140_005_RecitalsAndOrder")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=GENERATE_DIRECTIONS_ORDERFreeFormOrder")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/CaseProg/RecitalsAndOrder.json"))
          // .check(substring("finalOrderDocument"))
          .check(substring("GENERATE_DIRECTIONS_ORDERFreeFormOrder"))
          .check(jsonPath("$.data.finalOrderDocument.documentLink.document_url").saveAs("finalOrderDocument_url"))
          .check(jsonPath("$.data.finalOrderDocument.documentLink.document_hash").saveAs("finalOrderDocument_hash"))
          .check(jsonPath("$.data.finalOrderDocument.documentLink.document_filename").saveAs("finalOrderDocument_filename"))
          .check(jsonPath("$.data.finalOrderDocument.createdDatetime").saveAs("createdDatetime"))
          .check(jsonPath("$.data.finalOrderDocument.documentSize").saveAs("documentSize"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
         *  Civil Progression -Order PDF
  ==========================================================================================*/
      .group("CivilCP_FinalOrders_150_OrderPDF") {
        exec(http("CivilCP_FinalOrders_150_005_OrderPDF")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=GENERATE_DIRECTIONS_ORDERFinalOrderPreview")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/CaseProg/OrderPDF.json"))
          .check(substring("finalOrderDocument"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
     *  Civil Progression - Final Orders Submit
  ==========================================================================================*/
      .group("CivilCP_FinalOrders_160_FinalOrdersSubmit") {
        exec(http("CivilCP_FinalOrders_160_005_FinalOrdersSubmit")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .body(ElFileBody("bodies/CaseProg/FinalOrdersSubmit.json"))
          .check(substring("confirmation_body"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
}
