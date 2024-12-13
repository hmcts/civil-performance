package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.{CivilDamagesHeader, CsrfCheck, Environment}

object CUIR2DefendantRequestChange {

  val paymentURL = Environment.PaymentURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  /*======================================================================================
                 * Civil UI Claim - Raise a Request for a change
  ======================================================================================*/

  val run =

  /*======================================================================================
     * Civil UI Claim - Navigate to the claim homepage
  ======================================================================================*/

  group("CUIR2_DefRaiseChange_010_Homepage") {
    exec(http("CUIR2_DefRaiseChange_010_005_Homepage")
      .get("/dashboard/#{claimNumber}/defendant")
      .headers(CivilDamagesHeader.CUIR2Get)
      .check(substring("response to the claim")))
  }

  .pause(MinThinkTime, MaxThinkTime)

  /*======================================================================================
     * Civil UI Claim - Click on "Contact the court to request a change to my case"
  ======================================================================================*/

  .group("CUIR2_DefRaiseChange_020_RequestAChange") {
    exec(http("CUIR2_DefRaiseChange_020_005_RequestAChange")
      .get("/case/#{claimNumber}/general-application/application-type?linkFrom=start")
      .headers(CivilDamagesHeader.CUIR2Get)
      .check(CsrfCheck.save)
      .check(substring("When you request a change to your case")))
  }

  .pause(MinThinkTime, MaxThinkTime)

  /*======================================================================================
     * Civil UI Claim - Select ask to change a hearing date & click Continue
  ======================================================================================*/

  .group("CUIR2_DefRaiseChange_030_SelectChangeHearingDate") {
    exec(http("CUIR2_DefRaiseChange_030_005_SelectChangeHearingDate")
      .post("/case/#{claimNumber}/general-application/application-type?linkFrom=start")
      .headers(CivilDamagesHeader.CUIR2Post)
      .check(CsrfCheck.save)
      .formParam("_csrf", "#{csrf}")
      .formParam("option", "ADJOURN_HEARING")
      .check(substring("Agreement from the other parties")))
  }

  .pause(MinThinkTime, MaxThinkTime)

  /*======================================================================================
     * Civil UI Claim - Select Yes for Have other parties agreed and click Continue
  ======================================================================================*/

  .group("CUIR2_DefRaiseChange_O40_OtherPartiesHaveAgreed") {
    exec(http("CUIR2_DefRaiseChange_O40_005_OtherPartiesHaveAgreed")
      .post("/case/#{claimNumber}/general-application/agreement-from-other-party?index=0")
      .headers(CivilDamagesHeader.CUIR2Post)
      .formParam("_csrf", "#{csrf}")
      .formParam("option", "yes")
      .check(substring("Change a hearing date")))
  }

  .pause(MinThinkTime, MaxThinkTime)

  /*======================================================================================
   * Civil UI Claim - Click on Start now
  ======================================================================================*/

  .group("CUIR2_DefRaiseChange_050_StartNow") {
    exec(http("CUIR2_DefRaiseChange_050_005_StartNow")
      .get("/case/#{claimNumber}/general-application/claim-application-cost?index=0")
      .headers(CivilDamagesHeader.CUIR2Get)
      .check(CsrfCheck.save)
      .check(substring("When a judge makes a decision on your application")))
  }

  .pause(MinThinkTime, MaxThinkTime)

  /*======================================================================================
   * Civil UI Claim - Select No for Do you want to ask for your costs and click Continue
  ======================================================================================*/

  .group("CUIR2_DefRaiseChange_060_DoNotWantToReclaimCosts") {
    exec(http("CUIR2_DefRaiseChange_060_005_DoNotWantToReclaimCosts")
      .post("/case/#{claimNumber}/general-application/claim-application-cost?index=0")
      .headers(CivilDamagesHeader.CUIR2Post)
      .check(CsrfCheck.save)
      .formParam("_csrf", "#{csrf}")
      .formParam("option", "no")
      .check(substring("What order do you want the judge to make")))
  }

  .pause(MinThinkTime, MaxThinkTime)

  /*======================================================================================
     * Civil UI Claim - Leave the default text and click Continue
  ======================================================================================*/

  .group("CUIR2_DefRaiseChange_070_ConfirmHearingDateText") {
    exec(http("CUIR2_DefRaiseChange_070_005_ConfirmHearingDateText")
      .post("/case/#{claimNumber}/general-application/order-judge?index=0")
      .headers(CivilDamagesHeader.CUIR2Post)
      .check(CsrfCheck.save)
      .formParam("_csrf", "#{csrf}")
      .formParam("text", "The hearing arranged for [enter date] be moved to the first available date after [enter date], avoiding [enter dates to avoid].")
      .check(substring("Why are you requesting this order")))
  }

  .pause(MinThinkTime, MaxThinkTime)

  /*======================================================================================
   * Civil UI Claim - Add free text for the reason and click Continue
  ======================================================================================*/

  .group("CUIR2_DefRaiseChange_080_AddRequestReason") {
    exec(http("CUIR2_DefRaiseChange_080_005_AddRequestReason")
      .post("/case/#{claimNumber}/general-application/requesting-reason?index=0")
      .headers(CivilDamagesHeader.CUIR2Post)
      .check(CsrfCheck.save)
      .formParam("_csrf", "#{csrf}")
      .formParam("text", "perf testing")
      .check(substring("Do you want to add another application")))
  }

  .pause(MinThinkTime, MaxThinkTime)

  /*======================================================================================
     * Civil UI Claim - Select no for adding another application and click Continue
  ======================================================================================*/

  .group("CUIR2_DefRaiseChange_090_NotAddingAnotherApplication") {
    exec(http("CUIR2_DefRaiseChange_090_005_NotAddingAnotherApplication")
      .post("/case/#{claimNumber}/general-application/add-another-application?index=0")
      .headers(CivilDamagesHeader.CUIR2Post)
      .check(CsrfCheck.save)
      .formParam("_csrf", "#{csrf}")
      .formParam("option", "no")
      .check(substring("Do you want to upload documents to support your application")))
  }

  .pause(MinThinkTime, MaxThinkTime)

  /*======================================================================================
     * Civil UI Claim - Select No for uploading documents and click Continue
  ======================================================================================*/

  .group("CUIR2_DefRaiseChange_100_NotUploadingDocuments") {
    exec(http("CUIR2_DefRaiseChange_100_005_NotUploadingDocuments")
      .post("/case/#{claimNumber}/general-application/want-to-upload-documents?index=0")
      .headers(CivilDamagesHeader.CUIR2Post)
      .formParam("_csrf", "#{csrf}")
      .formParam("option", "no")
      .check(substring("Application hearing arrangements")))
  }

  .pause(MinThinkTime, MaxThinkTime)

  /*======================================================================================
     * Civil UI Claim - Click Continue
  ======================================================================================*/

  .group("CUIR2_DefRaiseChange_110_ContinueToHearingType") {
    exec(http("CUIR2_DefRaiseChange_110_005_ContinueToHearingType")
      .get("/case/#{claimNumber}/general-application/hearing-arrangement?index=0")
      .headers(CivilDamagesHeader.CUIR2Get)
      .check(CsrfCheck.save)
      .check(substring("What type of hearing would you prefer")))
  }

  .pause(MinThinkTime, MaxThinkTime)

  /*======================================================================================
     * Civil UI Claim - Select In person, add a reason and click Continue
  ======================================================================================*/
  .group("CUIR2_DefRaiseChange_120_ConfirmInPersonHearing") {
    exec(http("CUIR2_DefRaiseChange_120_005_ConfirmInPersonHearing")
      .post("/case/#{claimNumber}/general-application/hearing-arrangement?index=0")
      .headers(CivilDamagesHeader.CUIR2Post)
      .check(CsrfCheck.save)
      .formParam("_csrf", "#{csrf}")
      .formParam("option", "PERSON_AT_COURT")
      .formParam("reasonForPreferredHearingType", "perf test ")
      .formParam("courtLocation", "")
      .check(substring("Preferred telephone number")))
  }

  .pause(MinThinkTime, MaxThinkTime)

  /*======================================================================================
     * Civil UI Claim - Add telephone and email address details and click Continue
  ======================================================================================*/

  .group("CUIR2_DefRaiseChange_130_AddContactDetails") {
    exec(http("CUIR2_DefRaiseChange_130_005_AddContactDetails")
      .post("/case/#{claimNumber}/general-application/hearing-contact-details?index=0")
      .headers(CivilDamagesHeader.CUIR2Post)
      .check(CsrfCheck.save)
      .formParam("_csrf", "#{csrf}")
      .formParam("telephoneNumber", "07111111111")
      .formParam("emailAddress", "perftest@gmaill.com")
      .check(substring("Are there any dates when you cannot attend a hearing within the next 3 months")))
  }

  .pause(MinThinkTime, MaxThinkTime)

  /*======================================================================================
   * Civil UI Claim - Add a date within the next 3 months and click Continue
  ======================================================================================*/

  .group("CUIR2_DefRaiseChange_140_AddUnavailableDates") {
    exec(http("CUIR2_DefRaiseChange_140_005_AddUnavailableDates")
      .post("/case/#{claimNumber}/general-application/unavailable-dates?index=0")
      .headers(CivilDamagesHeader.CUIR2Post)
      .check(CsrfCheck.save)
      .formParam("_csrf", "#{csrf}")
      .formParam("items[0][type]", "SINGLE_DATE")
      .formParam("items[0][single][start][day]", "05")
      .formParam("items[0][single][start][month]", "02")
      .formParam("items[0][single][start][year]", "2025")
      .formParam("items[0][period][start][day]", "")
      .formParam("items[0][period][start][month]", "")
      .formParam("items[0][period][start][year]", "")
      .formParam("items[0][period][end][day]", "")
      .formParam("items[0][period][end][month]", "")
      .formParam("items[0][period][end][year]", "")
      .check(substring("If you need a sign language or language interpreter")))
  }

  .pause(MinThinkTime, MaxThinkTime)

  /*======================================================================================
     * Civil UI Claim - Select no adjustments/support and click Continue
  ======================================================================================*/

  .group("CUIR2_DefRaiseChange_150_NoReasonableAdjustments") {
    exec(http("CUIR2_DefRaiseChange_150_005_NoReasonableAdjustments")
      .post("/case/#{claimNumber}/general-application/hearing-support?index=0")
      .headers(CivilDamagesHeader.CUIR2Post)
      .formParam("_csrf", "#{csrf}")
      .formParam("signLanguageContent", "")
      .formParam("languageContent", "")
      .formParam("otherContent", "")
      .check(substring("Application fee to pay")))
  }

  .pause(MinThinkTime, MaxThinkTime)

  /*======================================================================================
     * Civil UI Claim - Click Continue for paying for your application
  ======================================================================================*/

  .group("CUIR2_DefRaiseChange_160_ContinueToPay") {
    exec(http("CUIR2_DefRaiseChange_160_005_ContinueToPay")
      .get("/case/#{claimNumber}/general-application/check-and-send?index=0")
      .headers(CivilDamagesHeader.CUIR2Get)
      .check(CsrfCheck.save)
      .check(substring("Check your answers")))
  }

  .pause(MinThinkTime, MaxThinkTime)

  /*======================================================================================
     * Civil UI Claim - Check all answers, tick the box, enter the name and click Submit
  ======================================================================================*/

  .group("CUIR2_DefRaiseChange_170_ConfirmAndSubmitAnswers") {
    exec(http("CUIR2_DefRaiseChange_170_005_ConfirmAndSubmitAnswers")
      .post("/case/#{claimNumber}/general-application/check-and-send?index=0")
      .headers(CivilDamagesHeader.CUIR2Post)
      .check(CsrfCheck.save)
      .formParam("_csrf", "#{csrf}")
      .formParam("signed", "true")
      .formParam("name", "perf test")
      .check(regex("""/case/#{claimNumber}/general-application/apply-help-fee-selection.id=(.+?)&amp;appFee=119" role="button" draggable=""").saveAs("feeSelectionId"))
      .check(substring("Until you pay the application fee")))
  }

  .pause(MinThinkTime, MaxThinkTime)

  /*======================================================================================
   * Civil UI Claim - Click on Pay application fee
  ======================================================================================*/

  .group("CUIR2_DefRaiseChange_180_PayApplicationFee") {
    exec(http("CUIR2_DefRaiseChange_180_005_PayApplicationFee")
      .get("/case/#{claimNumber}/general-application/apply-help-fee-selection?id=#{feeSelectionId}&appFee=119")
      .headers(CivilDamagesHeader.CUIR2Get)
      .check(CsrfCheck.save)
      .check(substring("Do you want to apply for help with fees?")))
  }

  //Longer think time defined here to ensure the correct charge ID is captured
  .pause(20)

  /*======================================================================================
     * Civil UI Claim - Select no for help with fees and click Continue
  ======================================================================================*/

  .group("CUIR2_DefRaiseChange_190_NoHelpWithFees") {
    exec(http("CUIR2_DefRaiseChange_190_005_NoHelpWithFees")
      .post("/case/#{claimNumber}/general-application/apply-help-fee-selection?id=#{feeSelectionId}&appFee=119")
      .disableFollowRedirect
      .headers(CivilDamagesHeader.CUIR2Post)
      .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
      .header("content-type", "application/x-www-form-urlencoded")
      .check(
        header("Location") // Extract the Location header
          .transform(location => location.split("/").last) // Optionally, extract the UUID directly
          .saveAs("CardDetailPageChargeId") // Save the extracted UUID
      )
      .formParam("_csrf", "#{csrf}")
      .formParam("option", "no")
      .check(status.in(200, 302)))
    //No text check here due to the redirect
  }

  //Longer think time defined here to ensure the correct charge ID is captured
  .pause(20)

  /*======================================================================================
   * Civil UI Claim - The below step is redirected to the card payment page,
   * but is manually required to capture the payment ID
  ======================================================================================*/

  .group("CUIR2_DefRaiseChange_200_GetPaymentID") {
    exec(http("CUIR2_DefRaiseChange_200_005_GetPaymentID")
      .get("https://card.payments.service.gov.uk/secure/#{CardDetailPageChargeId}")
      .headers(CivilDamagesHeader.CUIR2Post)
      .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
      .header("content-type", "application/x-www-form-urlencoded")
      .check(css("input[name='csrfToken']", "value").saveAs("_csrfTokenCardDetailPage"))
      .check(css("input[name='chargeId']", "value").saveAs("paymentId"))
      .check(status.is(200)))
    //No text check here due to the redirect
  }

  /*======================================================================================
     * Civil UI Claim - Enter required payment details and click Continue
  ======================================================================================*/

  .group("CUIR2_DefRaiseChange_210_EnterCardDetails") {
    exec(http("CUIR2_DefRaiseChange_210_005_EnterCardDetails")
      .post(paymentURL + "/card_details/#{paymentId}")
      .headers(CivilDamagesHeader.CUIR2Post)
      .formParam("chargeId", "#{paymentId}")
      .formParam("csrfToken", "#{_csrfTokenCardDetailPage}")
      .formParam("cardNo", "4444333322221111")
      .formParam("expiryMonth", "02")
      .formParam("expiryYear", "26")
      .formParam("cardholderName", "perf test")
      .formParam("cvc", "123")
      .formParam("addressLine1", "12 test street")
      .formParam("addressLine2", "")
      .formParam("addressCity", "london")
      .formParam("addressCountry", "GB")
      .formParam("addressPostcode", "kt25bu")
      .formParam("email", "perftest@gmail.com")
      .check(css("input[name='csrfToken']", "value").saveAs("_csrfTokenCardDetailConfirm"))
      .check(regex("Confirm your payment")))
  }

  .pause(MinThinkTime, MaxThinkTime)

  /*======================================================================================
     * Civil UI Claim - Review payment details and click on Confirm payment
  ======================================================================================*/

  .group("CUIR2_DefRaiseChange_220_ConfirmPayment") {
    exec(http("CUIR2_DefRaiseChange_220_005_ConfirmPayment")
      .post(paymentURL + "/card_details/#{paymentId}/confirm")
      .headers(CivilDamagesHeader.CUIR2Post)
      .formParam("csrfToken", "#{_csrfTokenCardDetailConfirm}")
      .formParam("chargeId", "#{paymentId}")
      .check(regex("Your payment was")))
  }

  .pause(MinThinkTime, MaxThinkTime)

  /*======================================================================================
     * Civil UI Claim - Click on Close and return to dashboard
  ======================================================================================*/

  .group("CUIR2_DefRaiseChange_230_Homepage") {
    exec(http("CUIR2_DefRaiseChange_230_005_Homepage")
      .get("/dashboard/#{claimNumber}/defendant")
      .headers(CivilDamagesHeader.CUIR2Get)
      .check(substring("Response to the claim")))
  }

}