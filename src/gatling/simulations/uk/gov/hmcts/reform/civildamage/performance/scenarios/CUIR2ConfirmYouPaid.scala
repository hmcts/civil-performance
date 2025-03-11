
package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef.{exec, _}
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, Common, CsrfCheck, Environment, Headers}

object CUIR2ConfirmYouPaid {

  val BaseURL = Environment.baseURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime


  val CoSC =

    /*======================================================================================
                   * Civil UI Claim - Click Claim
    ======================================================================================*/
    group("CUIR2_CoSC_030_SelectClaim") {
      exec(http("CUIR2_CoSC_030_SelectClaim")
        .get("/dashboard/#{caseId}/defendant")
        .headers(CivilDamagesHeader.CUIR2Get)
        .check(substring("Confirm you've paid a judgment")))
    }
      .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
               * Civil UI Claim - Click Confirm You've Paid
    ======================================================================================*/
    .group("CUIR2_CoSC_040_ClickConfirm") {
      exec(http("CUIR2_CoSC_040_ClickConfirm")
        .get("/case/#{caseId}/general-application/cosc/ask-proof-of-debt-payment-guidance")
        .headers(CivilDamagesHeader.CUIR2Get)
        .check(substring("If you paid within a month")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
               * Civil UI Claim - Continue
    ======================================================================================*/
    .group("CUIR2_CoSC_050_Continue") {
      exec(http("CUIR2_CoSC_050_Continue")
        .get("/case/#{caseId}/general-application/cosc/final-payment-date")
        .headers(CivilDamagesHeader.CUIR2Get)
        .check(CsrfCheck.save)
        .check(substring("When was the final payment?")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
               * Civil UI Claim - Enter Final Payment Date
    ======================================================================================*/
    .group("CUIR2_CoSC_060_EnterDate") {
      exec(http("CUIR2_CoSC_060_EnterDate")
        .post("/case/#{caseId}/general-application/cosc/final-payment-date")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("day", Common.getCurrentDay())
        .formParam("month", Common.getCurrentMonth())
        .formParam("year", Common.getCurrentYear())
        .check(CsrfCheck.save)
        .check(substring("Do you have evidence of the debt payment?")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
                     * Civil UI Claim - Evidence of the debt payment
    ======================================================================================*/
    .group("CUIR2_CoSC_070_Evidence") {
      exec(http("CUIR2_CoSC_070_Evidence")
        .post("/case/#{caseId}/general-application/cosc/debt-payment-evidence")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("debtPaymentOption", "UNABLE_TO_PROVIDE_EVIDENCE_OF_FULL_PAYMENT")
        .formParam("provideDetails", "Details")
        .check(CsrfCheck.save)
        .check(substring("Check your answers")))
    }
    .pause(MinThinkTime, MaxThinkTime)



//    /*======================================================================================
//               * Civil UI Claim - Evidence of the debt payment
//    ======================================================================================*/
//    .group("CUIR2_CoSC_070_Evidence") {
//      exec(http("CUIR2_CoSC_070_Evidence")
//        .post("/case/#{caseId}/general-application/cosc/debt-payment-evidence")
//        .headers(CivilDamagesHeader.CUIR2Post)
//        .formParam("_csrf", "#{csrf}")
//        .formParam("debtPaymentOption", "UPLOAD_EVIDENCE_DEBT_PAID_IN_FULL")
//        .formParam("provideDetails", "")
//        .check(CsrfCheck.save)
//        .check(substring("Upload documents to support your application")))
//    }
//    .pause(MinThinkTime, MaxThinkTime)
//
//    /*======================================================================================
//               * Civil UI Claim - Upload Evidence
//    ======================================================================================*/
//    .group("CUIR2_CoSC_080_UploadEvidence") {
//      exec(http("CUIR2_CoSC_080_UploadEvidence")
//        .post("/case/#{caseId}/general-application/upload-documents?index=undefined&_csrf=#{csrf}")
//        .headers(CivilDamagesHeader.CitizenSTUpload)
//        .bodyPart(RawFileBodyPart("files", "1MB-c.pdf").fileName("1MB-c.pdf")
//          .transferEncoding("binary")).asMultipartForm
//        .check(CsrfCheck.save)
//        .check(substring("1MB-c.pdf")))
//    }
//    .pause(MinThinkTime, MaxThinkTime)
//
//    /*======================================================================================
//               * Civil UI Claim - Continue
//    ======================================================================================*/
//    .group("CUIR2_CoSC_090_Continue") {
//      exec(http("CUIR2_CoSC_090_Continue")
//        .post("/case/#{caseId}/general-application/upload-documents?index=NaN&_csrf=#{csrf}")
//        .headers(CivilDamagesHeader.CitizenSTUpload)
//        .formParam("_csrf", "#{csrf}")
//        .formParam("selectedFile", "")
//        .check(CsrfCheck.save)
//        .check(substring("Check your answers")))
//    }
//    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
               * Civil UI Claim - Check Your Answers
    ======================================================================================*/
    .group("CUIR2_CoSC_080_CheckAnswers") {
      exec(http("CUIR2_CoSC_080_CheckAnswers")
        .post("/case/#{caseId}/general-application/cosc/check-your-answers?index=NaN")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("signed", "true")
        .formParam("name", "DEFENDANT")
        .check(substring("You need to pay the application fee"))
        .check(currentLocationRegex("id=(.*)").saveAs("applicationId")))
    }
    .pause(MinThinkTime, MaxThinkTime)


  val ApplicationFee =

    /*======================================================================================
               * Civil UI Claim - Application Fee
    ======================================================================================*/
    group("CUIR2_ConfirmPayment_090_ApplicationFee") {
      exec(http("CUIR2_ConfirmPayment_090_ApplicationFee")
        .get("/case/#{caseId}/general-application/apply-help-fee-selection?id=#{applicationId}&appFee=15")
        .headers(CivilDamagesHeader.CUIR2Get)
        .check(CsrfCheck.save)
        .check(substring("Do you want to apply for Help with Fees")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
               * Civil UI Claim - Apply for HWF
    ======================================================================================*/
    .group("CUIR2_ConfirmPayment_100_ApplyHWF") {
      exec(http("CUIR2_ConfirmPayment_100_ApplyHWF")
        .post("/case/#{caseId}/general-application/apply-help-fee-selection?id=#{applicationId}&appFee=15")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "no")
        .check(CsrfCheck.saveCard)
        .check(currentLocationRegex("card_details/(.*)").saveAs("cardDetails"))
        .check(substring("Enter card details")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
               * Civil UI Claim - Enter Card Details
    ======================================================================================*/
    .group("CUIR2_ConfirmPayment_110_EnterCardDetails") {
      exec(http("CUIR2_ConfirmPayment_110_CardConfirmation")
        .post("https://card.payments.service.gov.uk/check_card/#{cardDetails}")
        .headers(Headers.commonHeader)
        .header("accept", "*/*")
        .header("content-type", "application/x-www-form-urlencoded")
        .formParam("cardNo", "4444333322221111")
        .check(status.is(200)))

      .exec(http("CUIR2_ConfirmPayment_110_EnterCardDetails")
        .post("https://card.payments.service.gov.uk/card_details/#{cardDetails}")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("chargeId", "#{cardDetails}")
        .formParam("csrfToken", "#{csrf}")
        .formParam("cardNo", "4444333322221111")
        .formParam("expiryMonth", "10")
        .formParam("expiryYear", "26")
        .formParam("cardholderName", "DEFENDANT")
        .formParam("cvc", "123")
        .formParam("addressLine1", "10 Hibernia Gardens")
        .formParam("addressLine2", "")
        .formParam("addressCity", "Hounslow")
        .formParam("addressCountry", "GB")
        .formParam("addressPostcode", "TW3 3SD")
        .formParam("email", "civilmoneyclaimsdemo@gmail.com")
        .check(CsrfCheck.saveCard)
        .check(substring("Confirm your payment")))
    }
    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
               * Civil UI Claim - Confirm Payment
    ======================================================================================*/
    .group("CUIR2_ConfirmPayment_120_ConfirmPayment") {
      exec(http("CUIR2_ConfirmPayment_120_ConfirmPayment")
        .post("https://card.payments.service.gov.uk/card_details/#{cardDetails}/confirm")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("csrfToken", "#{csrf}")
        .formParam("chargeId", "#{cardDetails}")
        .check(substring("Your payment was successful")))
    }
    .pause(10)
}
