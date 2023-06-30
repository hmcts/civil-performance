
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
  
  val run=
    
    //Notify Details Event
    // val notifyclaimdetailsevent =
      group("CD_CreateClaim_350_NotifyDetailsEvent") {
        exec(http("CD_CreateClaim_310_005_Notify")
          .get("/workallocation/case/tasks/${caseId}/event/NOTIFY_DEFENDANT_OF_CLAIM/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(CivilDamagesHeader.headers_769)
          .check(status.in(200,201,304))
         
        )
      .exec(http("CD_CreateClaim_350_005_Detail")
        .get("/data/internal/cases/${caseId}/event-triggers/NOTIFY_DEFENDANT_OF_CLAIM_DETAILS?ignore-warning=false")
        .headers(CivilDamagesHeader.headers_769)
        //.header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
        .check(status.in(200, 304))
        .check(jsonPath("$.event_token").optional.saveAs("event_token_notifyclaimdetail"))
      )
  
        .exec(http("CD_CreateClaim_350_010_profile")
          .get("/data/internal/profile")
          .headers(CivilDamagesHeader.headers_149)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-user-profile.v2+json;charset=UTF-8")
          
          .check(status.in(200, 304))
        )
    }
      .pause(MinThinkTime, MaxThinkTime)
    // val notifyclaimdetailsupload =
      .group("CD_CreateClaim_360_CLAIM_DETAILSUpload") {
      exec(http("CD_CreateClaim_360_005_upload")
        .post("/data/case-types/CIVIL/validate?pageId=NOTIFY_DEFENDANT_OF_CLAIM_DETAILSUpload")
        .headers(CivilDamagesHeader.headers_868)
       // .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
        .body(ElFileBody("bodies/0210_request.json"))
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
       // .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
        .body(ElFileBody("bodies/0214_request.json"))
        .check(status.in(200, 201))
      )
        .exec(http("CD_CreateClaim_380_010_case")
          .get("/data/internal/cases/${caseId}")
          .headers(CivilDamagesHeader.headers_717)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
          
          .check(status.in(200, 304))
        )
    }
      .pause(MinThinkTime, MaxThinkTime)
  
  
    // val returntocasedetailsafternotifydetails =
      .group("CD_CreateClaim_380_ReturnToCaseDetailsAfterNotifyDetails") {
      exec(http("CD_CreateClaim_380_005_NotifyDetails")
        .post("/api/role-access/roles/manageLabellingRoleAssignment/${caseId}")
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
