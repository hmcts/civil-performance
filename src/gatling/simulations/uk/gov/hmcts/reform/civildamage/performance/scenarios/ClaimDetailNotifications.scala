
package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, Environment}

object ClaimDetailNotifications {
  
  val BaseURL = Environment.baseURL
  val IdAMURL = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime
  
  def run(implicit postHeaders: Map[String, String]): ChainBuilder = {
    
    //Notify Details Event
    // val notifyclaimdetailsevent =
      group("CD_CreateClaim_350_NotifyDetailsEvent") {
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
      .pause(MinThinkTime, MaxThinkTime)
    // val notifyclaimdetailsupload =
      .group("CD_CreateClaim_360_CLAIM_DETAILSUpload") {
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
      .pause(MinThinkTime, MaxThinkTime)
    // val notifyclaimdetailseventsubmit=
      .group("CD_CreateClaim_370_NotifyDetailsEventSubmit") {
      exec(http("CD_CreateClaim_370_EventSubmit")
        .post("/data/cases/${caseId}/events")
        .headers(CivilDamagesHeader.headers_886)
        .body(StringBody("{\"data\":{\"servedDocumentFiles\":{\"particularsOfClaimText\":\"\",\"particularsOfClaimDocument\":{\"document_url\":\"http://dm-store-perftest.service.core-compute-perftest.internal/documents/${Document_ID}\",\"document_binary_url\":\"http://dm-store-perftest.service.core-compute-perftest.internal/documents/${Document_ID}/binary\",\"document_filename\":\"3MB.pdf\"},\"medicalReports\":[],\"scheduleOfLoss\":[],\"certificateOfSuitability\":[],\"other\":[]}},\"event\":{\"id\":\"NOTIFY_DEFENDANT_OF_CLAIM_DETAILS\",\"summary\":\"\",\"description\":\"\"},\"event_token\":\"${event_token_notifyclaimdetail}\",\"ignore_warning\":false}"))
        .check(status.in(200, 201))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)
  
  
    // val returntocasedetailsafternotifydetails =
      .group("CD_CreateClaim_380_ReturnToCaseDetailsAfterNotifyDetails") {
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
      .pause(MinThinkTime, MaxThinkTime)
   
  }
}
