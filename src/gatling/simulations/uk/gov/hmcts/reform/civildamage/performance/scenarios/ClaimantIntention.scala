
package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, CsrfCheck, Environment, Common}

object ClaimantIntention {
  
  val BaseURL = Environment.baseURL
  val IdAMURL = Environment.idamURL
  val CivilUiURL = Environment.CivilUIURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime
  


  val claimantintention =

  /*======================================================================================
   * Civil UI Claimant Intention - Click On Case
==========================================================================================*/
   /* group("CD_ClaimIntention_740_Click On Claim") {
      exec(http("CD_ClaimIntention_740_ClickOnClaim")
        .get(BaseURL + "/data/internal/cases/#{caseId}")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
        .check(status.in(200, 304))
        //    .check(substring("Enter your claim number"))
      )
    }
      .pause(MinThinkTime, MaxThinkTime)*/
  
  /*======================================================================================
     * Civil UI Claim - Navigate to case
  ==========================================================================================*/

 /* .group("CD_ClaimIntention_740_FindClaim") {
    exec(http("CD_ClaimIntention_740_FindClaim")
      .get(BaseURL + "/cases/case-details/#{caseId}")
      .headers(CivilDamagesHeader.MoneyClaimNavHeader)
      .check(status.in(200, 304))
  //    .check(substring("Enter your claim number"))
    )
    
  }
      .pause(MinThinkTime, MaxThinkTime)*/


    /*======================================================================================
     * Civil UI Claim - Select 'View and Respond to Case' from WA
  ==========================================================================================*/

      group("CUI_ClaimIntent_030_ViewRespond") {
        exec(http("CUI_ClaimIntent_030_005_ViewRespond")
          .get(BaseURL + "/workallocation/case/tasks/#{caseId}/event/CLAIMANT_RESPONSE_SPEC/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(CivilDamagesHeader.headers_viewAndRespond)
          .check(status.in(200, 304))
          .check(substring("task_required_for_event"))
        )
  
          .exec(http("Civil_CreateClaim_030_010_profile")
            .get("/data/internal/profile")
            .headers(CivilDamagesHeader.headers_149)
            .check(status.in(200, 304))
          )


        .exec(http("CUI_ClaimIntent_030_015_ViewRespond")
          .get(BaseURL + "/data/internal/cases/#{caseId}/event-triggers/CLAIMANT_RESPONSE_SPEC?ignore-warning=false")
          .headers(CivilDamagesHeader.headers_ViewResponseevent)
         //  .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8")
          .check(status.in(200, 304))
          .check(jsonPath("$.event_token").optional.saveAs("event_token_intention"))
          .check(jsonPath("$.case_fields[91].formatted_value.partyID").saveAs("partyIDApp"))
          .check(jsonPath("$.case_fields[87].formatted_value").saveAs("respondent1PaymentDate"))
          .check(jsonPath("$.case_fields[77].formatted_value.documentLink.document_url").saveAs("document_url"))
          .check(jsonPath("$.case_fields[77].formatted_value.documentLink.document_filename").saveAs("document_filename"))
          .check(jsonPath("$.case_fields[77].formatted_value.documentSize").saveAs("documentSize"))
          .check(jsonPath("$.case_fields[77].formatted_value.createdDatetime").saveAs("createdDatetime"))
        )
          .exec(Common.userDetails)
      }
          .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
     * Civil UI Claim - Does the claimant want to settle the claim for the £500 the defendant admitted? - yes
  ==========================================================================================*/


          .group("CUI_ClaimIntent_040_WantSettle") {
            exec(http("CUI_ClaimIntent_040_005_WantSettle")
              .post(BaseURL + "/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECRespondentResponse")
              .headers(CivilDamagesHeader.headers_163)
              .body(ElFileBody("bodies/Claimantintention/CivilClaimIntention-WantToSettle.json"))
              .check(status.in(200, 304))
             // .check(substring("CLAIMANT_RESPONSE_SPECRespondentResponse"))
            )
          }
              .pause(MinThinkTime, MaxThinkTime)
        

            /*  //Respond to Claim
              .group("CD_DefResponse_350_RespondClaim") {
                exec(http("CD_DefResponse_350_005_RespondClaim")
                  .post(BaseURL + "/assignclaim?id=#{caseId}")
                  .headers(CivilDamagesHeader.MoneyClaimPostHeader)
                  .formParam("id", "#{caseId}")
                  .check(status.in(200, 304))
                  .check(substring("Sign in or create an account"))
                )
              }
                  .pause(MinThinkTime, MaxThinkTime)*/





    /*======================================================================================
    * Civil UI Claim - Check answers and submit
 ==========================================================================================*/


    .group("CUI_ClaimIntent_050_WantSettleSubmit") {
      exec(http("CUI_ClaimIntent_050_005_WantSettleSubmit")
        .post(BaseURL + "/data/cases/#{caseId}/events")
        .headers(CivilDamagesHeader.headers_intentsubmit)
        .body(ElFileBody("bodies/ClaimantIntention/CivilClaimIntention-WantToSettle.json"))
        .check(status.in(200,201,304))
      //  .check(substring("CLAIMANT_RESPONSE_SPECRespondentResponse"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


   
}