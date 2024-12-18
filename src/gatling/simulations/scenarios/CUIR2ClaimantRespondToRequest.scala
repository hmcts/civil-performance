package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.{CivilDamagesHeader, CsrfCheck, Environment}
import java.io.{BufferedWriter, FileWriter}

object CUIR2ClaimantRespondToRequest {

  val paymentURL = Environment.PaymentURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  /*======================================================================================
            * Civil UI Claim - Respond to the Defendant's Request for a change
  ======================================================================================*/

  val run =

    /*======================================================================================
     * Civil UI Claim - Navigate to the claim homepage
    ======================================================================================*/

    exec(http("CUIR2_ClaimantChangeRespond_010_HomePage")
      .get("/dashboard/#{claimNumber}/claimantNewDesign")
      .headers(CivilDamagesHeader.CUIR2Get)
      .check(regex("""a href=/case/#{claimNumber}/response/general-application/(.+?)/respondent-information rel=\"noopener noreferrer\" class=\"govuk-link\">Review and respond to the request</a><""").saveAs("newClaimNumber")))

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
      * Civil UI Claim - Navigate to the recently raised Request
    ======================================================================================*/
  
    .exec(http("CUIR2_ClaimantChangeRespond_020_ViewRequestChange")
      .get("/case/#{claimNumber}/response/general-application/#{newClaimNumber}/respondent-information")
      .headers(CivilDamagesHeader.CUIR2Get)
      .check(substring("The other parties have requested a change to the case")))

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
      * Civil UI Claim - Click on the Start now button
    ======================================================================================*/

    .exec(http("CUIR2_ClaimantChangeRespond_030_ClickStartNow")
      .get("/case/#{claimNumber}/response/general-application/#{newClaimNumber}/view-application")
      .headers(CivilDamagesHeader.CUIR2Get)
      .check(substring("This is the application that the other parties have made")))

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
      * Civil UI Claim - Click on Respond to application button
    ======================================================================================*/

    .exec(http("CUIR2_ClaimantChangeRespond_040_RespondToApplication")
      .get("/case/#{claimNumber}/response/general-application/#{newClaimNumber}/agree-to-order")
      .headers(CivilDamagesHeader.CUIR2Get)
      .check(CsrfCheck.save)
      .check(substring("The other parties have said that")))

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
      * Civil UI Claim - Select agree to order and Continue
    ======================================================================================*/

    .exec(http("CUIR2_ClaimantChangeRespond_050_AgreeAndContinue")
      .post("/case/#{claimNumber}/response/general-application/#{newClaimNumber}/agree-to-order")
      .headers(CivilDamagesHeader.CUIR2Post)
      .check(CsrfCheck.save)
      .formParam("_csrf", "#{csrf}")
      .formParam("option", "yes")
      .check(substring("Do you want to upload documents to support your response")))

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
      * Civil UI Claim - Select yes to upload documents and continue
    ======================================================================================*/

    .exec(http("CUIR2_ClaimantChangeRespond_060_AgreeToUploadDocuments")
      .post("/case/#{claimNumber}/response/general-application/#{newClaimNumber}/want-to-upload-document")
      .headers(CivilDamagesHeader.CUIR2Post)
      .check(CsrfCheck.save)
      .formParam("_csrf", "#{csrf}")
      .formParam("option", "yes"))

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
      * Civil UI Claim - Add new document
    ======================================================================================*/

    .exec(http("CUIR2_ClaimantChangeRespond_070_AddNewDocument")
      .post("/case/#{claimNumber}/response/general-application/#{newClaimNumber}/upload-documents?_csrf=#{csrf}")
      .headers(CivilDamagesHeader.CUIR2Post)
      .check(CsrfCheck.save))

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
      * Civil UI Claim - Select the document and click Upload
    ======================================================================================*/

    .exec(http("CUIR2_ClaimantChangeRespond_080_DocumentFileUpload")
      .post("/case/#{claimNumber}/response/general-application/#{newClaimNumber}/upload-documents?_csrf=#{csrf}")
      .check(CsrfCheck.save)
      .headers(CivilDamagesHeader.CitizenSTUpload)
      .bodyPart(RawFileBodyPart("selectedFile", "3MB.pdf")
      .contentType("application/pdf")
      .fileName("3MB.pdf")
      .transferEncoding("binary")))

  .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
      * Civil UI Claim - Select preferred hearing type and click Continue
    ======================================================================================*/

    .exec(http("CUIR2_ClaimantChangeRespond_090_SelectHearingArrangement")
      .post("/case/#{claimNumber}/response/general-application/#{newClaimNumber}/hearing-arrangement")
      .headers(CivilDamagesHeader.CUIR2Post)
      .check(CsrfCheck.save)
      .formParam("_csrf", "#{csrf}")
      .formParam("option", "PERSON_AT_COURT")
      .formParam("reasonForPreferredHearingType", "Perf testing")
      .formParam("courtLocation", ""))

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
      * Civil UI Claim - Enter contact details and click Continue
    ======================================================================================*/

    .exec(http("CUIR2_ClaimantChangeRespond_100_EnterContactDetails")
      .post("/case/#{claimNumber}/response/general-application/#{newClaimNumber}/hearing-contact-details")
      .headers(CivilDamagesHeader.CUIR2Post)
      .check(CsrfCheck.save)
      .formParam("_csrf", "#{csrf}")
      .formParam("telephoneNumber", "07000000000")
      .formParam("emailAddress", "#{defEmailAddress}"))

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
      * Civil UI Claim - Enter any unavailable dates and click Continue
    ======================================================================================*/

    .exec(http("CUIR2_ClaimantChangeRespond_110_EnterUnavailableDates")
      .post("/case/#{claimNumber}/response/general-application/#{newClaimNumber}/unavailable-dates")
      .headers(CivilDamagesHeader.CUIR2Post)
      .check(CsrfCheck.save)
      .formParam("_csrf", "#{csrf}")
      .formParam("items[0][single][start][day]", "")
      .formParam("items[0][single][start][month]", "")
      .formParam("items[0][single][start][year]", "")
      .formParam("items[0][period][start][day]", "")
      .formParam("items[0][period][start][month]", "")
      .formParam("items[0][period][start][year]", "")
      .formParam("items[0][period][end][day]", "")
      .formParam("items[0][period][end][month]", "")
      .formParam("items[0][period][end][year]", ""))

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
      * Civil UI Claim - Select no reasonable adjustments and click Continue
    ======================================================================================*/

    .exec(http("CUIR2_ClaimantChangeRespond_120_ReasonableAdjustments")
      .post("/case/#{claimNumber}/response/general-application/#{newClaimNumber}/hearing-support")
      .headers(CivilDamagesHeader.CUIR2Post)
      .check(CsrfCheck.save)
      .formParam("_csrf", "#{csrf}")
      .formParam("signLanguageContent", "")
      .formParam("languageContent", "")
      .formParam("otherContent", ""))

    .pause(MinThinkTime, MaxThinkTime)

    /*======================================================================================
      * Civil UI Claim - Enter the name and click Continue
    ======================================================================================*/

    .exec(http("CUIR2_ClaimantChangeRespond_130_EnterNameAndSubmit")
      .post("/case/#{claimNumber}/response/general-application/#{newClaimNumber}/check-and-send")
      .headers(CivilDamagesHeader.CUIR2Post)
      .formParam("_csrf", "#{csrf}")
      .formParam("signed", "true")
      .formParam("name", "Perf test"))

    .pause(MinThinkTime, MaxThinkTime)

    .exec { session =>
       val fw = new BufferedWriter(new FileWriter("LIPSJudicialData.csv", true))
       try {
         fw.write(session("newClaimNumber").as[String] + "\r\n")
       } finally fw.close()
       session
     }

    /*======================================================================================
      * Civil UI Claim - Return to the citizen homepage/dashboard
    ======================================================================================*/

    .exec(http("CUIR2_ClaimantChangeRespond_140_ReturnHome")
      .get("/dashboard/#{claimNumber}/claimantNewDesign")
      .headers(CivilDamagesHeader.CUIR2Get))

    .pause(MinThinkTime, MaxThinkTime)
}
