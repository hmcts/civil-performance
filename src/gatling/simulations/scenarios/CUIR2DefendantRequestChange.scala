package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.{CivilDamagesHeader, CsrfCheck, Environment}

object CUIR2DefendantRequestChange {

  val BaseURL = Environment.baseURL
  val IdAMURL = Environment.idamURL
  val paymentURL = Environment.PaymentURL
  val CivilUiURL = "https://civil-citizen-ui.#{env}.platform.hmcts.net"
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val run=

  /*======================================================================================
                 * Civil UI Claim - Respond to Claim- Click On Claim
  ======================================================================================*/

  exec(http("request_home")
    .get("/dashboard/#{claimNumber}/defendant")
    .headers(CivilDamagesHeader.CUIR2Get)
//    .check(CsrfCheck.save)
  )

  .pause(MinThinkTime, MaxThinkTime)

  .exec(http("request_00")
    .get("/case/#{claimNumber}/general-application/application-type?linkFrom=start")
    .headers(CivilDamagesHeader.CUIR2Get)
    .check(CsrfCheck.save))

  .pause(MinThinkTime, MaxThinkTime)

  .exec(http("request_0")
    .post("/case/#{claimNumber}/general-application/application-type?linkFrom=start")
    .headers(CivilDamagesHeader.CUIR2Post)
    .check(CsrfCheck.save)
    .formParam("_csrf", "#{csrf}")
    .formParam("option", "ADJOURN_HEARING"))

  .pause(MinThinkTime, MaxThinkTime)

  .exec(http("request_2")
    .post("/case/#{claimNumber}/general-application/agreement-from-other-party?index=0")
    .headers(CivilDamagesHeader.CUIR2Post)
    .formParam("_csrf", "#{csrf}")
    .formParam("option", "yes"))

  .pause(MinThinkTime, MaxThinkTime)

  .exec(http("request_4")
    .get("/case/#{claimNumber}/general-application/claim-application-cost?index=0")
    .headers(CivilDamagesHeader.CUIR2Get)
    .check(CsrfCheck.save))

  .pause(MinThinkTime, MaxThinkTime)

  .exec(http("request_6")
    .post("/case/#{claimNumber}/general-application/claim-application-cost?index=0")
    .headers(CivilDamagesHeader.CUIR2Post)
    .check(CsrfCheck.save)
    .formParam("_csrf", "#{csrf}")
    .formParam("option", "no"))

  .pause(MinThinkTime, MaxThinkTime)

  .exec(http("request_8")
    .post("/case/#{claimNumber}/general-application/order-judge?index=0")
    .headers(CivilDamagesHeader.CUIR2Post)
    .check(CsrfCheck.save)
    .formParam("_csrf", "#{csrf}")
    .formParam("text", "The hearing arranged for [enter date] be moved to the first available date after [enter date], avoiding [enter dates to avoid]."))

  .pause(MinThinkTime, MaxThinkTime)

  .exec(http("request_10")
    .post("/case/#{claimNumber}/general-application/requesting-reason?index=0")
    .headers(CivilDamagesHeader.CUIR2Post)
    .check(CsrfCheck.save)
    .formParam("_csrf", "#{csrf}")
    .formParam("text", "perf testing"))

  .pause(MinThinkTime, MaxThinkTime)

  .exec(http("request_12")
    .post("/case/#{claimNumber}/general-application/add-another-application?index=0")
    .headers(CivilDamagesHeader.CUIR2Post)
    .check(CsrfCheck.save)
    .formParam("_csrf", "#{csrf}")
    .formParam("option", "no"))

  .pause(MinThinkTime, MaxThinkTime)

  .exec(http("request_14")
    .post("/case/#{claimNumber}/general-application/want-to-upload-documents?index=0")
    .headers(CivilDamagesHeader.CUIR2Post)
    .formParam("_csrf", "#{csrf}")
    .formParam("option", "no"))

  .pause(MinThinkTime, MaxThinkTime)

  .exec(http("request_16")
    .get("/case/#{claimNumber}/general-application/hearing-arrangement?index=0")
    .headers(CivilDamagesHeader.CUIR2Get)
    .check(CsrfCheck.save))

  .pause(MinThinkTime, MaxThinkTime)

  .exec(http("request_18")
    .post("/case/#{claimNumber}/general-application/hearing-arrangement?index=0")
    .headers(CivilDamagesHeader.CUIR2Post)
    .check(CsrfCheck.save)
    .formParam("_csrf", "#{csrf}")
    .formParam("option", "PERSON_AT_COURT")
    .formParam("reasonForPreferredHearingType", "perf test ")
    .formParam("courtLocation", ""))

  .pause(MinThinkTime, MaxThinkTime)

  .exec(http("request_20")
    .post("/case/#{claimNumber}/general-application/hearing-contact-details?index=0")
    .headers(CivilDamagesHeader.CUIR2Post)
    .check(CsrfCheck.save)
    .formParam("_csrf", "#{csrf}")
    .formParam("telephoneNumber", "07111111111")
    .formParam("emailAddress", "perftest@gmaill.com"))

  .pause(MinThinkTime, MaxThinkTime)

  .exec(http("request_22")
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
    .formParam("items[0][period][end][year]", ""))

  .pause(MinThinkTime, MaxThinkTime)

  .exec(http("request_24")
    .post("/case/#{claimNumber}/general-application/hearing-support?index=0")
    .headers(CivilDamagesHeader.CUIR2Post)
    .formParam("_csrf", "#{csrf}")
    .formParam("signLanguageContent", "")
    .formParam("languageContent", "")
    .formParam("otherContent", ""))

  .pause(MinThinkTime, MaxThinkTime)

  .exec(http("request_26")
    .get("/case/#{claimNumber}/general-application/check-and-send?index=0")
    .headers(CivilDamagesHeader.CUIR2Get)
    .check(CsrfCheck.save))

  .pause(MinThinkTime, MaxThinkTime)

  .exec(http("request_28")
    .post("/case/#{claimNumber}/general-application/check-and-send?index=0")
    .headers(CivilDamagesHeader.CUIR2Post)
    .check(CsrfCheck.save)
    .formParam("_csrf", "#{csrf}")
    .formParam("signed", "true")
    .formParam("name", "perf test")
    .check(regex("""/case/#{claimNumber}/general-application/apply-help-fee-selection.id=(.{8}-.{4}-.{4}-.{4}-.{12})&amp;appFee=119" role="button" draggable=""").saveAs("feeSelectionId"))
//    .check(bodyString.saveAs("BODY"))
  )

//  .exec(session => {
//    val response = session("BODY").as[String]
//    println(s"Response body: \n$response")
//    session
//  })

  .pause(MinThinkTime, MaxThinkTime)

  .exec(http("request_30")
    .get("/case/#{claimNumber}/general-application/apply-help-fee-selection?id=#{feeSelectionId}&appFee=119")
    .headers(CivilDamagesHeader.CUIR2Get)
//    .check(CsrfCheck.save)
  )

  .pause(MinThinkTime, MaxThinkTime)

  .exec(http("request_31")
    .post("/case/#{claimNumber}/general-application/apply-help-fee-selection?id=#{feeSelectionId}&appFee=119") //84f9db4d-5bfe-4393-baae-f252fbf2e407
//    .disableFollowRedirect
    .headers(CivilDamagesHeader.CUIR2Post)
    .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
    .header("content-type", "application/x-www-form-urlencoded")
    .check(CsrfCheck.save)
    .check(headerRegex("location", """https:\/\/card.payments.service.gov.uk\/secure\/(.{8}-.{4}-.{4}-.{4}-.{12})""").ofType[(String)].saveAs("CardDetailPageChargeId")) //.ofType[(String)]
//    .check(regex("""a href="/case/#{claimNumber}/general-application/(.+?)/view-application.index=""").saveAs("newClaimNumber"))
    .formParam("_csrf", "#{csrf}")
    .formParam("option", "no")
    .check(status.in(200, 302))
  )

  .pause(MinThinkTime, MaxThinkTime)

  .exec(http("request_33")
    .get("https://card.payments.service.gov.uk/secure/#{CardDetailPageChargeId}")
    .disableFollowRedirect
    .headers(CivilDamagesHeader.CUIR2Post)
    .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
    .header("content-type", "application/x-www-form-urlencoded")
    .check(
      headerRegex("location", """\/card_details\/(.{26})""")
        .ofType[(String)]
        .saveAs("paymentId")
    )
    .check(status.is(303)))

  .pause(MinThinkTime, MaxThinkTime)

  .exec(http("request_36")
    .post(paymentURL + "/card_details/#{paymentId}")
    .headers(CivilDamagesHeader.CUIR2Post)
    .check(CsrfCheck.save)
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
    .check(css("input[name='csrfToken']", "value").saveAs("_csrfTokenCardDetailConfirm")))

  .pause(MinThinkTime, MaxThinkTime)

  .exec(http("request_38")
    .post(paymentURL + "/card_details/#{paymentId}/confirm")
    .headers(CivilDamagesHeader.CUIR2Post)
    .formParam("csrfToken", "#{_csrfTokenCardDetailConfirm}")
    .formParam("chargeId", "#{paymentId}"))

  .pause(MinThinkTime, MaxThinkTime)

//  .exec(http("request_40")
//    .get("/case/#{claimNumber}/generalApplication/cancel")
//    .headers(headers_4))

}
