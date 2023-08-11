
package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, CsrfCheck, Environment, Common}

object ClaimantIntention {
  
  val BaseURL = Environment.baseURL
  val IdAMURL = Environment.idamURL
  val CivilUiURL = "https://civil-citizen-ui.perftest.platform.hmcts.net"
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime
  


  val Defendant =

  /*======================================================================================
   * Civil UI Claim - Navigate to case
==========================================================================================*/

  group("CD_ClaimIntention_740_FindClaim") {
    exec(http("CD_ClaimIntention_740_FindClaim")
      .get(BaseURL + "/cases/case-details/1691572505460279")
      .headers(CivilDamagesHeader.MoneyClaimNavHeader)
      .check(status.in(200, 304))
  //    .check(substring("Enter your claim number"))
    )
  }
      .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
     * Civil UI Claim - Select 'View and Respond to Case'
  ==========================================================================================*/

      .group("CD_DefResponse_750_ViewRespond") {
        exec(http("CD_DefResponse_750_005_ViewRespond")
          .get(BaseURL + "/workallocation/case/tasks/1691572505460279/event/CLAIMANT_RESPONSE_SPEC/caseType/CIVIL/jurisdiction/CIVIL")
          .headers(CivilDamagesHeader.MoneyClaimNavHeader)
          .check(status.in(200, 304))
          .check(substring("task_required_for_event"))
        )


          .exec(Common.profile)


        .exec(http("CD_DefResponse_750_010_ViewRespond")
          .get(BaseURL + "/data/internal/cases/1691572505460279/event-triggers/CLAIMANT_RESPONSE_SPEC?ignore-warning=false")
          .headers(CivilDamagesHeader.MoneyClaimNavHeader)
          .check(status.in(200, 304))
          .check(jsonPath("$.event_token").optional.saveAs("event_token"))
          .check(jsonPath("$.case_fields[90].formatted_value.partyID").saveAs("partyIDApp"))
          .check(jsonPath("$.case_fields[86].formatted_value").saveAs("respondent1PaymentDate"))
          .check(jsonPath("$.case_fields[77].formatted_value.documentLink.document_url").saveAs("document_url"))
          .check(jsonPath("$.case_fields[77].formatted_value.documentLink.document_filename").saveAs("document_filename"))
          .check(jsonPath("$.case_fields[77].formatted_value.documentSize").saveAs("documentSize"))
          .check(jsonPath("$.case_fields[77].formatted_value.createdDatetime").saveAs("createdDatetime"))


        )
          .exec(Common.userDetails)
      }
          .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
     * Civil UI Claim - Does the claimant want to settle the claim for the £10 the defendant admitted? - yes
  ==========================================================================================*/


          .group("CD_DefResponse_760_WantSettle") {
            exec(http("CD_DefResponse_760_005_WantSettle")
              .post(CivilUiURL + "/data/case-types/CIVIL/validate?pageId=CLAIMANT_RESPONSE_SPECRespondentResponse")
              .headers(CivilDamagesHeader.MoneyClaimPostHeader)
              .body(ElFileBody("bodies/cuiclaim/CivilClaimIntention-WantToSettle.json"))
              .check(status.in(200, 304))
              .check(substring("CLAIMANT_RESPONSE_SPECRespondentResponse"))
            )
          }
              .pause(MinThinkTime, MaxThinkTime)


              //Respond to Claim
              .group("CD_DefResponse_350_RespondClaim") {
                exec(http("CD_DefResponse_350_005_RespondClaim")
                  .post(CivilUiURL + "/assignclaim?id=#{caseId}")
                  .headers(CivilDamagesHeader.MoneyClaimPostHeader)
                  .formParam("id", "#{caseId}")
                  .check(status.in(200, 304))
                  .check(substring("Sign in or create an account"))
                )
              }
                  .pause(MinThinkTime, MaxThinkTime)





    /*======================================================================================
    * Civil UI Claim - Check answers and submit
 ==========================================================================================*/


    .group("CD_DefResponse_770_WantSettleSubmit") {
      exec(http("CD_DefResponse_770_005_WantSettleSubmit")
        .post(CivilUiURL + "data/cases/1691572505460279/events")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .body(ElFileBody("bodies/cuiclaim/CivilClaimIntention-WantToSettleSubmit.json"))
        .check(status.in(200, 304))
        .check(substring("CLAIMANT_RESPONSE_SPECRespondentResponse"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    //Respond to Claim
    .group("CD_DefResponse_350_RespondClaim") {
      exec(http("CD_DefResponse_350_005_RespondClaim")
        .post(CivilUiURL + "/assignclaim?id=#{caseId}")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("id", "#{caseId}")
        .check(status.in(200, 304))
        .check(substring("Sign in or create an account"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)

//sign in
    .group("CD_DefResponse_360_SignIn ") {
      exec(http("CD_DefResponse_360_005_SignIn")
        .post(CivilUiURL + "/login?client_id=civil_citizen_ui&response_type=code&redirect_uri=https://civil-citizen-ui.perftest.platform.hmcts.net/oauth2/callback&scope=profile%20openid%20roles%20manage-user%20create-user%20search-user")
        .headers(CivilDamagesHeader.MoneyClaimSignInHeader)
        .formParam("username", "sampankumar@hmcts.net")
        .formParam("password", "Password12!")
        .formParam("selfRegistrationEnabled", "true")
        .formParam("_csrf", "${csrf}")
        .check(status.in(200, 304))
        .check(substring("Your money claims account"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
                 * Civil UI Claim - Resume Claim
      ==========================================================================================*/

    .group("CD_DefResponse_370_ResumeClaim") {
      exec(http("CD_DefResponse_370_005_ResumeClaim")
        .get(CivilUiURL + "/dashboard/#{caseId}/defendant")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("About claim"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
               * Civil UI Claim - Respond to Claim
    ==========================================================================================*/

    .group("CD_DefResponse_380_RespondToClaim") {
      exec(http("CD_DefResponse_380_005_RespondToClaim")
        .get(CivilUiURL + "/case/#{caseId}/response/bilingual-language-preference")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Do you want to respond to this claim in Welsh?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
             * Civil UI Claim - Do you want to respond to this claim in Welsh?
  ==========================================================================================*/

    .group("CD_DefResponse_390_InWelsh") {
      exec(http("CD_DefResponse_390_005_InWelsh")
        .post(CivilUiURL + "/case/#{caseId}/response/bilingual-language-preference")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "ENGLISH")
        .check(status.in(200, 304))
        .check(substring("Respond to a money claim"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
           * Civil UI Claim - Confirm Your Details Redirect
==========================================================================================*/

    .group("CD_DefResponse_400_ConfirmDetails") {
      exec(http("CD_DefResponse_400_005_ConfirmDetails")
        .get(CivilUiURL + "/case/#{caseId}/response/your-details")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Confirm your details"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
         * Civil UI Claim - Confirm Your Details Confirm
==========================================================================================*/

    .group("CD_DefResponse_410_ConfirmDetailsConfirm") {
      exec(http("CD_DefResponse_410_005_ConfirmDetailsConfirm")
        .post(CivilUiURL + "/case/#{caseId}/response/your-details")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("individualTitle", "mr")
        .formParam("individualFirstName", "#{randomString}First")
        .formParam("individualLastName", "#{randomString}Last")
        .formParam("addressLine1", "#{randomString}Address1")
        .formParam("addressLine2", "")
        .formParam("addressLine3", "")
        .formParam("city", "#{randomString}Town")
        .formParam("postCode", "#{postcode}")
        .formParam("postToThisAddress", "no")
        .formParam("correspondenceAddressPostcode", "")
        .formParam("addressList", "")
        .formParam("addressLine1", "")
        .formParam("addressLine2", "")
        .formParam("addressLine3", "")
        .formParam("city", "")
        .formParam("postCode", "")
        .check(status.in(200, 304))
        .check(substring("Enter your date of birth"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
         * Civil UI Claim - Enter your date of birth
==========================================================================================*/

    .group("CD_DefResponse_420_DateOfBirth") {
      exec(http("CD_DefResponse_420_005_DateOfBirth")
        .post(CivilUiURL + "/case/1691572505460279/response/your-dob")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("day", "#{birthDay}")
        .formParam("month", "#{birthMonth}")
        .formParam("year", "#{birthYear}")
        .check(status.in(200, 304))
        .check(substring("Enter a phone number (optional)"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
       * Civil UI Claim - Enter a phone number
==========================================================================================*/

    .group("CD_DefResponse_430_PhoneNumber") {
      exec(http("CD_DefResponse_430_005_PhoneNumber")
        .post(CivilUiURL + "/case/1691572505460279/response/your-phone")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("telephoneNumber", "01234567890")
        .check(status.in(200, 304))
        .check(regex("""Confirm your details</a>
                       |                    </span>
                       |                    <strong class="govuk-tag app-task-list__tag ">
                       |                      COMPLETE""".stripMargin))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
         * Civil UI Claim - View your options before response deadline
==========================================================================================*/

    .group("CD_DefResponse_440_ViewOptions") {
      exec(http("CD_DefResponse_440_005_ViewOptions")
        .get(CivilUiURL + "/case/#{caseId}/response/understanding-your-options")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Requesting extra time"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
       * Civil UI Claim - Requesting extra time
==========================================================================================*/

    .group("CD_DefResponse_450_ExtraTime") {
      exec(http("CD_DefResponse_450_005_ExtraTime")
        .get(CivilUiURL + "/case/1691572505460279/response/response-deadline-options")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Response deadline"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
     * Civil UI Claim - Response deadline - no
==========================================================================================*/

    .group("CD_DefResponse_460_ResponseDeadline") {
      exec(http("CD_DefResponse_460_005_ResponseDeadline")
        .post(CivilUiURL + "/case/1691572505460279/response/response-deadline-options")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "no")
        .check(status.in(200, 304))
        .check(regex("""View your options before response deadline</a>
                       |                    </span>
                       |                    <strong class="govuk-tag app-task-list__tag ">
                       |                      COMPLETE""".stripMargin))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
         * Civil UI Claim - Choose A Response Redirect
==========================================================================================*/

    .group("CD_DefResponse_470_ChooseAResponse") {
      exec(http("CD_DefResponse_470_005_ChooseAResponse")
        .get(CivilUiURL + "/case/1691572505460279/response/response-type")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("How do you respond to the claim?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
     * Civil UI Claim - How do you respond to the claim? - I admit part of the claim
==========================================================================================*/

    .group("CD_DefResponse_480_ExtraTime") {
      exec(http("CD_DefResponse_480_005_ExtraTime")
        .post(CivilUiURL + "/case/1691572505460279/response/response-type")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("responseType", "PART_ADMISSION")
        .check(status.in(200, 304))
        .check(substring("Have you paid the claimant the amount you admit you owe?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
     * Civil UI Claim - Have you paid the claimant the amount you admit you owe? - no
==========================================================================================*/

    .group("CD_DefResponse_490_HaveYouPaid") {
      exec(http("CD_DefResponse_490_005_HaveYouPaid")
        .post(CivilUiURL + "/case/1691572505460279/response/partial-admission/already-paid")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "no")
        .check(status.in(200, 304))
        .check(regex("""Choose a response</a>
                       |                    </span>
                       |                    <strong class="govuk-tag app-task-list__tag ">
                       |                      COMPLETE""".stripMargin))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
         * Civil UI Claim - How much money do you admit you owe? - redirect
==========================================================================================*/

    .group("CD_DefResponse_500_ConfirmDetails") {
      exec(http("CD_DefResponse_500_005_ConfirmDetails")
        .get(CivilUiURL + "/case/1691572505460279/response/partial-admission/how-much-do-you-owe")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Confirm your details"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
       * Civil UI Claim - How much money do you admit you owe?
==========================================================================================*/

    .group("CD_DefResponse_510_MoneyOwed") {
      exec(http("CD_DefResponse_510_005_MoneyOwed")
        .post(CivilUiURL + "/case/1691572505460279/response/partial-admission/how-much-do-you-owe")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("amount", "#{birthDay}")
        .check(regex("""How much money do you admit you owe?</a>
                       |                    </span>
                       |                    <strong class="govuk-tag app-task-list__tag ">
                       |                      COMPLETE""".stripMargin))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
                //}


    /*======================================================================================
       * Civil UI Claim - Why do you disagree with the amount claimed? - redirect
==========================================================================================*/

    .group("CD_DefResponse_520_WhyDisagree") {
      exec(http("CD_DefResponse_520_005_WhyDisagree")
        .get(CivilUiURL + "case/1691572505460279/response/partial-admission/why-do-you-disagree")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("This includes the claim fee and any interest."))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
     * Civil UI Claim - Why do you disagree with the claim amount?
==========================================================================================*/

    .group("CD_DefResponse_530_WhyDisagreeExplain") {
      exec(http("CD_DefResponse_530_005_WhyDisagreeExplain")
        .post(CivilUiURL + "/case/1691572505460279/response/partial-admission/why-do-you-disagree")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("text", "#{randomString}Explanation")
        .check(substring("Add your timeline of events"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)




    /*======================================================================================
   * Civil UI Claim - Add your timeline of events
==========================================================================================*/

    .group("CD_DefResponse_540_TimelineOfEvents") {
      exec(http("CD_DefResponse_540_005_TimelineOfEvents")
        .post(CivilUiURL + "/case/1691572505460279/response/timeline")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("rows[0][day]", "#{birthDay}")
        .formParam("rows[0][month]", "#{birthMonth}")
        .formParam("rows[0][year]", "#{birthYear}")
        .formParam("rows[0][description]", "#{randomString}description0")
        .formParam("rows[1][day]", "#{birthDay}")
        .formParam("rows[1][month]", "#{birthMonth}")
        .formParam("rows[1][year]", "#{birthYear}")
        .formParam("rows[1][description]", "#{randomString}description1")
        .formParam("rows[2][day]", "#{birthDay}")
        .formParam("rows[2][month]", "#{birthMonth}")
        .formParam("rows[2][year]", "#{birthYear}")
        .formParam("rows[2][description]", "#{randomString}description2")
        .formParam("rows[3][day]", "#{birthDay}")
        .formParam("rows[3][month]", "#{birthMonth}")
        .formParam("rows[3][year]", "#{birthYear}")
        .formParam("rows[3][description]", "#{randomString}description3") //might need to change so all diff
        .formParam("comment]", "#{randomString}comment")
        .check(substring("List your evidence"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
* Civil UI Claim - List your evidence
==========================================================================================*/


.group("CD_DefResponse_550_ListEvidence") {
  exec(http("CD_DefResponse_550_005_ListEvidence")
  .post(CivilUiURL + "/case/1691572505460279/response/evidence")
  .headers(CivilDamagesHeader.MoneyClaimPostHeader)
  .formParam("_csrf", "#{csrf}")
  .formParam("evidenceItem[0][type]", "Expert witness")
  .formParam("evidenceItem[0][description]]", "#{randomString}timelineDescription Expert Witness")
  .formParam("evidenceItem[1][type]", "Photo evidence")
  .formParam("evidenceItem[1][description]]", "#{randomString}timelineDescription Photo Evidence")
  .formParam("evidenceItem[2][type]", "Contracts and agreements")
  .formParam("evidenceItem[2][description]]", "#{randomString}timelineDescription Contracts and Agreements")
  .formParam("evidenceItem[3][type]", "Statements of account")
  .formParam("evidenceItem[3][description]]", "#{randomString}timelineDescription Statements of Account")
  .formParam("comment", "#{randomString}comment")
    .check(regex("""Why do you disagree with the amount claimed?</a>
                   |                    </span>
                   |                    <strong class="govuk-tag app-task-list__tag ">
                   |                      COMPLETE""".stripMargin))
  )
}
  .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
       * Civil UI Claim - When will you pay? Redirect
==========================================================================================*/

    .group("CD_DefResponse_560_WhenPay") {
      exec(http("CD_DefResponse_560_005_WhenPay")
        .get(CivilUiURL + "/case/1691572505460279/response/partial-admission/payment-option")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("When do you want to pay the"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
   * Civil UI Claim - When do you want to pay the?
==========================================================================================*/

    .group("CD_DefResponse_570_PayDate") {
      exec(http("CD_DefResponse_570_005_PayDate")
        .post(CivilUiURL + "/case/1691572505460279/response/partial-admission/payment-option")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("paymentType", "IMMEDIATELY")
        .check(regex("""When will you pay the £10.00?</a>
                       |                    </span>
                       |                    <strong class="govuk-tag app-task-list__tag ">
                       |                      COMPLETE""".stripMargin))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
       * Civil UI Claim - Free telephone mediation Redirect
==========================================================================================*/

    .group("CD_DefResponse_580_FreeTelephone") {
      exec(http("CD_DefResponse_580_005_FreeTelephone")
        .get(CivilUiURL + "/case/1691572505460279/mediation/free-telephone-mediation")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Free telephone mediation"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
     * Civil UI Claim - Free telephone mediation
==========================================================================================*/

    .group("CD_DefResponse_590_FreeTelephoneMed") {
      exec(http("CD_DefResponse_590_005_FreeTelephoneMed")
        .get(CivilUiURL + "/case/1691572505460279/mediation/can-we-use")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Confirm your telephone number"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
 * Civil UI Claim - Confirm your telephone number - yes
==========================================================================================*/

    .group("CD_DefResponse_600_ConfirmNumber") {
      exec(http("CD_DefResponse_600_005_ConfirmNumber")
        .post(CivilUiURL + "/case/1691572505460279/mediation/can-we-use")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "yes")
        .formParam("mediationPhoneNumber", "")
        .check(regex("""Free telephone mediation</a>
                       |                    </span>
                       |                    <strong class="govuk-tag app-task-list__tag ">
                       |                      COMPLETE""".stripMargin))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
     * Civil UI Claim - Give us details in case there's a hearing Redirect
==========================================================================================*/

    .group("CD_DefResponse_610_GiveDetails") {
      exec(http("CD_DefResponse_610_005_GiveDetails")
        .get(CivilUiURL + "/case/1691572505460279/directions-questionnaire/determination-without-hearing")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Determination without Hearing Questions"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
     * Civil UI Claim - Determination without Hearing Questions
==========================================================================================*/

    .group("CD_DefResponse_620_Determination") {
      exec(http("CD_DefResponse_620_005_Determination")
        .post(CivilUiURL + "/case/1691572505460279/directions-questionnaire/determination-without-hearing")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "yes")
        .formParam("reasonForHearing", "")
        .check(substring("Using an expert"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
   * Civil UI Claim - Using an expert
==========================================================================================*/

    .group("CD_DefResponse_630_UsingExpert") {
      exec(http("CD_DefResponse_630_005_UsingExpert")
        .post(CivilUiURL + "/case/1691572505460279/directions-questionnaire/expert")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .check(substring("Do you want to give evidence yourself?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
 * Civil UI Claim - Do you want to give evidence yourself? - yes
==========================================================================================*/

    .group("CD_DefResponse_640_GiveEvidence") {
      exec(http("CD_DefResponse_640_005_GiveEvidence")
        .post(CivilUiURL + "/case/1691572505460279/directions-questionnaire/give-evidence-yourself")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "yes")
        .check(substring("Do you have other witnesses?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
* Civil UI Claim - Do you have other witnesses? - no
==========================================================================================*/

    .group("CD_DefResponse_650_OtherWitnesses") {
      exec(http("CD_DefResponse_650_005_OtherWitnesses")
        .post(CivilUiURL + "/case/1691572505460279/directions-questionnaire/defendant-witnesses")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("witnessItems[0][firstName]", "")
        .formParam("witnessItems[0][lastName]", "")
        .formParam("witnessItems[0][email]", "")
        .formParam("witnessItems[0][telephone]", "")
        .formParam("witnessItems[0][details]", "")
        .formParam("option", "yes")
        .check(substring("Are there any dates in the next 12 months when you, your experts or witnesses cannot go to a hearing?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
* Civil UI Claim - Are there any dates in the next 12 months when you, your experts or witnesses cannot go to a hearing? - no
==========================================================================================*/

    .group("CD_DefResponse_660_AnyDates") {
      exec(http("CD_DefResponse_660_005_AnyDates")
        .post(CivilUiURL + "/case/1691572505460279/directions-questionnaire/cant-attend-hearing-in-next-12-months")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "no")
        .check(substring("Do you want to ask for a telephone or video hearing?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
* Civil UI Claim - Do you want to ask for a telephone or video hearing? - yes
==========================================================================================*/

    .group("CD_DefResponse_670_AskForTelephone") {
      exec(http("CD_DefResponse_670_005_AskForTelephone")
        .post(CivilUiURL + "/case/1691572505460279/directions-questionnaire/phone-or-video-hearing")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "yes")
        .formParam("details", "#{randomString}details")
        .check(substring("Are you, your experts or witnesses vulnerable in a way that the court needs to consider?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
* Civil UI Claim - Are you, your experts or witnesses vulnerable in a way that the court needs to consider? - no
==========================================================================================*/

    .group("CD_DefResponse_680_Vulnerable") {
      exec(http("CD_DefResponse_680_005_Vulnerable")
        .post(CivilUiURL + "/case/1691572505460279/directions-questionnaire/vulnerability")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("vulnerabilityDetails", "")
        .formParam("option", "no")
        .check(substring("Do you, your experts or witnesses need support to attend a hearing?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)



    /*======================================================================================
* Civil UI Claim - Do you, your experts or witnesses need support to attend a hearing? - no
==========================================================================================*/

    .group("CD_DefResponse_690_NeedSupport") {
      exec(http("CD_DefResponse_690_005_NeedSupport")
        .post(CivilUiURL + "/case/1691572505460279/directions-questionnaire/support-required")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("model[items][0][fullName]", "")
        .formParam("model[items][0][signLanguageInterpreter][content]", "")
        .formParam("model[items][0][languageInterpreter][content]", "")
        .formParam("model[items][0][otherSupport][content]", "")
        .formParam("option", "no")
        .check(substring("Do you want to ask for the hearing to be held at a specific court?"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
* Civil UI Claim - Do you want to ask for the hearing to be held at a specific court? - no
==========================================================================================*/

    .group("CD_DefResponse_700_SpecifcCourt") {
      exec(http("CD_DefResponse_700_005_SpecifcCourt")
        .post(CivilUiURL + "/case/1691572505460279/directions-questionnaire/court-location")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("courtLocation", "")
        .formParam("reason", "")
        .formParam("option", "no")
        .check(substring("Welsh language"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
* Civil UI Claim - Welsh language
==========================================================================================*/

    .group("CD_DefResponse_710_WelshLanguage") {
      exec(http("CD_DefResponse_710_005_WelshLanguage")
        .post(CivilUiURL + "/case/1691572505460279/directions-questionnaire/welsh-language")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("speakLanguage", "en")
        .formParam("documentsLanguage", "en")
        .check(regex("""Give us details in case there&#39;s a hearing</a>
                       |                    </span>
                       |                    <strong class="govuk-tag app-task-list__tag ">
                       |                      COMPLETE""".stripMargin))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
   * Civil UI Claim - Check and submit your response Redirect
==========================================================================================*/

    .group("CD_DefResponse_720_CheckAndSubmit") {
      exec(http("CD_DefResponse_720_005_CheckAndSubmit")
        .get(CivilUiURL + "/case/1691572505460279/response/check-and-send")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Your answers will help us check we are treating people fairly and equally"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
* Civil UI Claim - Check your answers
==========================================================================================*/

    .group("CD_DefResponse_730_CheckYourAnswers") {
      exec(http("CD_DefResponse_730_005_CheckYourAnswers")
        .post(CivilUiURL + "/case/1691572505460279/response/check-and-send")
        .headers(CivilDamagesHeader.MoneyClaimPostHeader)
        .formParam("_csrf", "#{csrf}")
        .formParam("type", "basic")
        .formParam("isFullAmountRejected", "true")
        .formParam("signed", "true")
        .formParam("directionsQuestionnaireSigned", "true")
        .check(substring("You&#39;ve submitted your response"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
}