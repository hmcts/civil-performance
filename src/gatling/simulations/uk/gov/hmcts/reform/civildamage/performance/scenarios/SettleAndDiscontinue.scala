
package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, Common, Environment, Headers}

import java.io.{BufferedWriter, FileWriter}

object SettleAndDiscontinue {

  val BaseURL = Environment.baseURL
  val IdAMURL = Environment.idamURL
  val IdamUrl = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val sdoenhancementsfasttrackfeeder=csv("sdoftcaseIds.csv").circular
  val sdoflightdelayfeeder=csv("sdoflightdelaycaseIds.csv").circular
  val sdodrhfeeder=csv("sdodrhcaseIds.csv").circular
  

  /*======================================================================================
            Following is for creating S&D for specific claim By Claimant
  ==========================================================================================*/
  
  val SettleSpecClaimByClaimant =
  // feed(cpfulltestFeeder)
    exec(_.setAll(
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
      .group("XUI_SettleByClaimant_030_SearchCase") {
        exec(http("XUI_SettleByClaimant_030_SearchCase")
          .get("/data/internal/cases/#{caseId}")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
          .check(substring("Summary")))
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      /*======================================================================================
               *  Settle And Discontinue
    ==========================================================================================*/
      .group("XUI_SettleByClaimant_040_ClickOnPaidInFull") {
        exec(http("XUI_SettleByClaimant_040_005_ClickOnPaidInFull")
          .get("/workallocation/case/tasks/#{caseId}/event/SETTLE_CLAIM_MARK_PAID_FULL/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/json")
          .check(substring("task_required_for_event"))
        )
          
          .exec(http("XUI_SettleByClaimant_040_010_ClickOnPaidInFull")
            .get("/data/internal/cases/#{caseId}/event-triggers/SETTLE_CLAIM_MARK_PAID_FULL?ignore-warning=false")
            .headers(CivilDamagesHeader.MoneyClaimNav)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
            .check(substring("SETTLE_CLAIM_MARK_PAID_FULL"))
            .check(jsonPath("$.event_token").saveAs("event_token"))
          )
          .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))
          
          .exec(http("XUI_SettleByClaimant_040_015_ClickOnPaidInFull")
            .get("/workallocation/case/tasks/#{caseId}/event/SETTLE_CLAIM_MARK_PAID_FULL/caseType/CIVIL/jurisdiction/CIVIL")
            .headers(CivilDamagesHeader.MoneyClaimNav)
            .header("accept", "application/json")
            .check(substring("task_required_for_event"))
          )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
             *  *  Settle And Discontinue - '	Option For Settlement
  ==========================================================================================*/
      .group("XUI_SettleByClaimant_050_OptionForSettlement") {
        exec(http("XUI_SettleByClaimant_050_005_OptionForSettlement")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=SETTLE_CLAIM_MARK_PAID_FULLOptionsForSettlement")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sd/OptionForSettlement.json"))
          .check(substring("SETTLE_CLAIM_MARK_PAID_FULL"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
           *  *  Settle And Discontinue - Mark claim as paid in full
==========================================================================================*/
      .group("XUI_SettleByClaimant_060_MarkClaimAsPaidFull") {
        exec(http("XUI_SettleByClaimant_060_005_MarkClaimAsPaidFull")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=SETTLE_CLAIM_MARK_PAID_FULLSingleClaimant")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sd/MarkClaimAsFull.json"))
          .check(substring("SETTLE_CLAIM_MARK_PAID_FULL"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      /*======================================================================================
         *  Settle And Discontinue -  Submit
==========================================================================================*/
      .group("XUI_SettleByClaimant_070_SubmitSDForSpec") {
        exec(http("XUI_SettleByClaimant_070_005_SubmitSDForSpec")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .header("Experimental", "true")
          .body(ElFileBody("bodies/sd/SDSubmit.json"))
          .check(substring("The claim has been marked as paid in full"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  /*======================================================================================
            Following is for creating Settlement By hearing Admin
  ==========================================================================================*/
  
  val SettleSpecClaimByHearingAdmin =
  // feed(cpfulltestFeeder)
    exec(_.setAll(
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
      .group("XUI_SettleByHearingAdmin_030_SearchCase") {
        exec(http("XUI_SettleByHearingAdmin_030_SearchCase")
          .get(BaseURL + "/data/internal/cases/#{caseId}")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
          .check(substring("Summary")))
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      /*======================================================================================
               *  Settle And Discontinue
    ==========================================================================================*/
      .group("XUI_SettleByHearingAdmin_040_ClickOnSettlement") {
        exec(http("XUI_SettleByHearingAdmin_040_005_ClickOnSettlement")
          .get("/workallocation/case/tasks/#{caseId}/event/SETTLE_CLAIM/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/json")
          .check(substring("task_required_for_event"))
        )
          
          .exec(http("XUI_SettleByHearingAdmin_040_010_ClickOnSettlement")
            .get("/data/internal/cases/#{caseId}/event-triggers/SETTLE_CLAIM?ignore-warning=false")
            .headers(CivilDamagesHeader.MoneyClaimNav)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
            .check(substring("SETTLE_CLAIM"))
            .check(jsonPath("$.event_token").saveAs("event_token"))
          )
          .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))
          
          .exec(http("XUI_SettleByHearingAdmin_040_015_ClickOnSettlement")
            .get("/workallocation/case/tasks/#{caseId}/event/SETTLE_CLAIM/caseType/CIVIL/jurisdiction/CIVIL")
            .headers(CivilDamagesHeader.MoneyClaimNav)
            .header("accept", "application/json")
            .check(substring("task_required_for_event"))
          )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
             *  *  Settle And Discontinue - '	ReasonForSettlement
  ==========================================================================================*/
      .group("XUI_SettleByHearingAdmin_050_ReasonForSettlement") {
        exec(http("XUI_SettleByHearingAdmin_050_005_ReasonForSettlement")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=SETTLE_CLAIMSettleClaim")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sd/ReasonForSettlement.json"))
          .check(substring("SETTLE_CLAIMSettleClaim"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
   
      
      /*======================================================================================
         *  Settle And Discontinue -  Submit
==========================================================================================*/
      .group("XUI_SettleByHearingAdmin_060_SubmitSDForSpec") {
        exec(http("XUI_SettleByHearingAdmin_060_005_SubmitSDForSpec")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .header("Experimental", "true")
          .body(ElFileBody("bodies/sd/SettleByHearingAdminSubmit.json"))
          .check(substring("Claim marked as settled"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  /*======================================================================================
            Following is for creating S&D for specific claim By Claimant
  ==========================================================================================*/
  
  val DiscontinueSpecClaimByClaimant =
  // feed(cpfulltestFeeder)
    exec(_.setAll(
      "CaseProgRandomString" -> Common.randomString(5),
      "Plus2WeeksDay" -> Common.getPlus2WeeksDay(),
      "Plus2WeeksMonth" -> Common.getPlus2WeeksMonth(),
      "Plus2WeeksYear" -> Common.getPlus2WeeksYear(),
      "EvidenceYear" -> Common.getYear(),
      "EvidenceDay" -> Common.getDay(),
      "EvidenceMonth" -> Common.getMonth())
    )
      
      /*======================================================================================
                        * DisContinue Full
      ==========================================================================================*/
      .group("XUI_DiscontinueByClaimant_030_SearchCase") {
        exec(http("XUI_DiscontinueByClaimant_030_SearchCase")
          .get(BaseURL + "/data/internal/cases/#{caseId}")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
          .check(substring("Summary")))
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      /*======================================================================================
               *  Settle And Discontinue
    ==========================================================================================*/
      .group("XUI_DiscontinueByClaimant_040_ClickOnDiscontinue") {
        exec(http("XUI_DiscontinueByClaimant_040_005_ClickOnDiscontinue")
          .get("/workallocation/case/tasks/#{caseId}/event/DISCONTINUE_CLAIM_CLAIMANT/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/json")
          .check(substring("task_required_for_event"))
        )
          
          .exec(http("XUI_DiscontinueByClaimant_040_010_ClickOnDiscontinue")
            .get("/data/internal/cases/#{caseId}/event-triggers/DISCONTINUE_CLAIM_CLAIMANT?ignore-warning=false")
            .headers(CivilDamagesHeader.MoneyClaimNav)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
            .check(substring("claimantWhoIsDiscontinuing"))
            .check(jsonPath("$.event_token").saveAs("event_token"))
          )
          .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))
          
          .exec(http("XUI_DiscontinueByClaimant_040_015_ClickOnDiscontinue")
            .get("/workallocation/case/tasks/#{caseId}/event/DISCONTINUE_CLAIM_CLAIMANT/caseType/CIVIL/jurisdiction/CIVIL")
            .headers(CivilDamagesHeader.MoneyClaimNav)
            .header("accept", "application/json")
            .check(substring("task_required_for_event"))
          )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
             *  *  Settle And Discontinue - '	Is court permission required yes
  ==========================================================================================*/
      .group("XUI_DiscontinueByClaimant_050_IsCourtPermissionRequired") {
        exec(http("XUI_DiscontinueByClaimant_050_005_IsCourtPermissionRequired")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DISCONTINUE_CLAIM_CLAIMANTCourtPermission")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sd/courtpermissionrequired.json"))
          .check(substring("DISCONTINUE_CLAIM_CLAIMANTCourtPermission"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
           *  *  Settle And Discontinue - Is court permission granted by judge
==========================================================================================*/
      .group("XUI_DiscontinueByClaimant_060_IsJudgePermissionGranted") {
        exec(http("XUI_DiscontinueByClaimant_060_005_IsJudgePermissionGranted")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DISCONTINUE_CLAIM_CLAIMANTPermissionGranted")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sd/isJudgePermissionGranted.json"))
          .check(substring("DISCONTINUE_CLAIM_CLAIMANTPermissionGranted"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
      /*======================================================================================
           *  *  Settle And Discontinue - type of discontinue
==========================================================================================*/
      .group("XUI_DiscontinueByClaimant_070_TypeOfDiscontinue") {
        exec(http("XUI_DiscontinueByClaimant_070_005_TypeOfDiscontinue")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DISCONTINUE_CLAIM_CLAIMANTDiscontinuanceType")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sd/ClaimantDiscontinueType.json"))
          .check(substring("DISCONTINUE_CLAIM_CLAIMANTDiscontinuanceType"))
        )
    
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      /*======================================================================================
         *  Settle And Discontinue -  Submit
==========================================================================================*/
      .group("XUI_DiscontinueByClaimant_080_DiscontinueSubmit") {
        exec(http("XUI_DiscontinueByClaimant_080_005_DiscontinueSubmit")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .header("Experimental", "true")
          .body(ElFileBody("bodies/sd/DiscontinueSubmit.json"))
          .check(substring("The claim has been marked as paid in full"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  /*======================================================================================
            Following is for creating S&D for specific claim By Claimant for Judicial referral
  ==========================================================================================*/
  
  val DiscontinueSpecClaimByClaimantForJDState =
  // feed(cpfulltestFeeder)
    exec(_.setAll(
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
      .group("XUI_DiscontinueByClaimant_030_SearchCase") {
        exec(http("XUI_DiscontinueByClaimant_030_SearchCase")
          .get(BaseURL + "/data/internal/cases/#{caseId}")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
          .check(substring("Summary")))
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      /*======================================================================================
               *  Settle And Discontinue
    ==========================================================================================*/
      .group("XUI_DiscontinueByClaimant_040_ClickOnDisContinue") {
        exec(http("XUI_DiscontinueByClaimant_040_005_ClickOnDisContinue")
          .get("/workallocation/case/tasks/1727083363722287/event/DISCONTINUE_CLAIM_CLAIMANT/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/json")
          .check(substring("task_required_for_event"))
        )
          
          .exec(http("XUI_DiscontinueByClaimant_040_010_ClickOnDisContinue")
            .get("/data/internal/cases/#{caseId}/event-triggers/DISCONTINUE_CLAIM_CLAIMANT?ignore-warning=false")
            .headers(CivilDamagesHeader.MoneyClaimNav)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
            .check(substring("claimantWhoIsDiscontinuing"))
            .check(jsonPath("$.event_token").saveAs("event_token"))
          )
          .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))
          
          .exec(http("XUI_DiscontinueByClaimant_040_015_ClickOnDisContinue")
            .get("/workallocation/case/tasks/#{caseId}/event/DISCONTINUE_CLAIM_CLAIMANT/caseType/CIVIL/jurisdiction/CIVIL")
            .headers(CivilDamagesHeader.MoneyClaimNav)
            .header("accept", "application/json")
            .check(substring("task_required_for_event"))
          )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
             *  *  Settle And Discontinue - '	Is court permission required yes
  ==========================================================================================*/
      .group("XUI_DiscontinueByClaimant_050_IsCourtPermissionRequired") {
        exec(http("XUI_DiscontinueByClaimant_050_005_IsCourtPermissionRequired")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DISCONTINUE_CLAIM_CLAIMANTCourtPermission")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sd/courtpermissionrequired.json"))
          .check(substring("DISCONTINUE_CLAIM_CLAIMANTCourtPermission"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
           *  *  Settle And Discontinue - Is court permission granted by judge
 ==========================================================================================*/
      .group("XUI_DiscontinueByClaimant_060_IsJudgePermissionGranted") {
        exec(http("XUI_DiscontinueByClaimant_060_005_IsJudgePermissionGranted")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DISCONTINUE_CLAIM_CLAIMANTPermissionGranted")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sd/isJudgePermissionGranted.json"))
          .check(substring("DISCONTINUE_CLAIM_CLAIMANTPermissionGranted"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      /*======================================================================================
           *  *  Settle And Discontinue - type of discontinue
 ==========================================================================================*/
      .group("XUI_DiscontinueByClaimant_070_TypeOfDiscontinue") {
        exec(http("XUI_DiscontinueByClaimant_070_005_TypeOfDiscontinue")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=DISCONTINUE_CLAIM_CLAIMANTDiscontinuanceType")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sd/ClaimantDiscontinueType.json"))
          .check(substring("DISCONTINUE_CLAIM_CLAIMANTDiscontinuanceType"))
        )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      /*======================================================================================
         *  Settle And Discontinue -  Submit
 ==========================================================================================*/
      .group("XUI_DiscontinueByClaimant_080_DiscontinueSubmit") {
        exec(http("XUI_DiscontinueByClaimant_080_005_DiscontinueSubmit")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .header("Experimental", "true")
          .body(ElFileBody("bodies/sd/DiscontinueSubmit.json"))
          .check(substring("Your request is being reviewed"))
        )
      }
        
      .pause(MinThinkTime, MaxThinkTime)
  
  /*======================================================================================
           Following for validate discontinue
  ==========================================================================================*/
  
  val ValidateDiscontinueByCTSCForJDState =
  // feed(cpfulltestFeeder)
    exec(_.setAll(
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
      .group("XUI_DiscontinueValidateByCTSC_030_SearchCase") {
        exec(http("XUI_DiscontinueValidateByCTSC_030_SearchCase")
          .get(BaseURL + "/data/internal/cases/#{caseId}")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
          .check(substring("Summary")))
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      /*======================================================================================
               *  Validate Discontinue
    ==========================================================================================*/
      .group("XUI_DiscontinueValidateByCTSC_040_ClickOnValidateDisContinue") {
        exec(http("XUI_DiscontinueValidateByCTSC_040_005_ClickOnValidateDisContinue")
          .get("/workallocation/case/tasks/#{caseId}/event/VALIDATE_DISCONTINUE_CLAIM_CLAIMANT/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/json")
          .check(substring("task_required_for_event"))
        )
          
          .exec(http("XUI_DiscontinueValidateByCTSC_040_010_ClickOnValidateDisContinue")
            .get("/data/internal/cases/#{caseId}/event-triggers/VALIDATE_DISCONTINUE_CLAIM_CLAIMANT?ignore-warning=false")
            .headers(CivilDamagesHeader.MoneyClaimNav)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
            .check(substring("VALIDATE_DISCONTINUE_CLAIM"))
            .check(jsonPath("$.event_token").saveAs("event_token"))
          )
          .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))
          
          .exec(http("XUI_DiscontinueValidateByCTSC_040_015_ClickOnValidateDisContinue")
            .get("/workallocation/case/tasks/#{caseId}/event/VALIDATE_DISCONTINUE_CLAIM_CLAIMANT/caseType/CIVIL/jurisdiction/CIVIL")
            .headers(CivilDamagesHeader.MoneyClaimNav)
            .header("accept", "application/json")
            .check(substring("task_required_for_event"))
          )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
             *  *  Settle And Discontinue - '	Validate Discontinuance  - yes
  ==========================================================================================*/
      .group("XUI_DiscontinueValidateByCTSC_060_ValidateDiscontinuance") {
        exec(http("XUI_DiscontinueValidateByCTSC_060_005_ValidateDiscontinuance")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=VALIDATE_DISCONTINUE_CLAIM_CLAIMANTValidateDiscontinuance")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sd/validatediscontinue.json"))
          .check(substring("VALIDATE_DISCONTINUE_CLAIM_CLAIMANTValidateDiscontinuance"))
        )
        .exec(http("XUI_DiscontinueValidateByCTSC_060_010_DiscontinuanceSubmit")
          .post(BaseURL + "/data/cases/#{caseId}/events")
          .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
          .header("X-Xsrf-Token", "#{XSRFToken}")
          .header("Experimental", "true")
          .body(ElFileBody("bodies/sd/validatediscontinuesubmit.json"))
          .check(substring("Information successfully validated"))
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  /*======================================================================================
           Following for validate discontinue
  ==========================================================================================*/
  
  val InValidateDiscontinueByCTSCForJDState =
  // feed(cpfulltestFeeder)
    exec(_.setAll(
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
      .group("XUI_DiscontinueInValidateByCTSC_030_SearchCase") {
        exec(http("XUI_DiscontinueInValidateByCTSC_030_SearchCase")
          .get(BaseURL + "/data/internal/cases/#{caseId}")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
          .check(substring("Summary")))
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      /*======================================================================================
               *  Validate Discontinue
    ==========================================================================================*/
      .group("XUI_DiscontinueInValidateByCTSC_040_ClickOnDisContinue") {
        exec(http("XUI_DiscontinueInValidateByCTSC_040_005_ClickOnDisContinue")
          .get("/workallocation/case/tasks/#{caseId}/event/VALIDATE_DISCONTINUE_CLAIM_CLAIMANT/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .header("accept", "application/json")
          .check(substring("task_required_for_event"))
        )
          
          .exec(http("XUI_DiscontinueInValidateByCTSC_040_010_ClickOnDisContinue")
            .get("/data/internal/cases/#{caseId}/event-triggers/VALIDATE_DISCONTINUE_CLAIM_CLAIMANT?ignore-warning=false")
            .headers(CivilDamagesHeader.MoneyClaimNav)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
            .check(substring("VALIDATE_DISCONTINUE_CLAIM"))
            .check(jsonPath("$.event_token").saveAs("event_token"))
          )
          .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))
          
          .exec(http("XUI_DiscontinueInValidateByCTSC_040_015_ClickOnDisContinue")
            .get("/workallocation/case/tasks/#{caseId}/event/VALIDATE_DISCONTINUE_CLAIM_CLAIMANT/caseType/CIVIL/jurisdiction/CIVIL")
            .headers(CivilDamagesHeader.MoneyClaimNav)
            .header("accept", "application/json")
            .check(substring("task_required_for_event"))
          )
        
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      
      
      /*======================================================================================
             *  *  Settle And Discontinue - '	Validate Discontinuance  - no
  ==========================================================================================*/
      .group("XUI_DiscontinueInValidateByCTSC_060_ValidateDiscontinue") {
        exec(http("XUI_DiscontinueInValidateByCTSC_060_005_InValidateDiscontinue")
          .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=VALIDATE_DISCONTINUE_CLAIM_CLAIMANTValidateDiscontinuance")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8")
          .body(ElFileBody("bodies/sd/invalidatediscontinue.json"))
          .check(substring("VALIDATE_DISCONTINUE_CLAIM_CLAIMANTValidateDiscontinuance"))
        )
          .exec(http("XUI_DiscontinueInValidateByCTSC_060_010_InValidateDiscontinueSubmit")
            .post(BaseURL + "/data/cases/#{caseId}/events")
            .headers(CivilDamagesHeader.MoneyClaimDefPostHeader)
            .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8")
            .header("X-Xsrf-Token", "#{XSRFToken}")
            .header("Experimental", "true")
            .body(ElFileBody("bodies/sd/invalidatediscontinuesubmit.json"))
            .check(substring("Unable to validate information"))
          )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  //claim should be at judicial referral and do the discontinue from claimant and login as ctsc admin validate the discontinue
  //claim should be at hearing readiness and do the discontinue from claimant and login as ctsc and validate
  //claim should be at waiting defendant and do the discontinue from claimant and login as ctsc admin and cancel task
  //all the above should be done
  
}
