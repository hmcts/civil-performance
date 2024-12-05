
package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scenarios.CivilCitizen.MaxThinkTime
import utils.{CivilDamagesHeader, Common, CsrfCheck, Environment}

import java.io.{BufferedWriter, FileWriter}

object CUIR2DocUploadCaseProg {

  val BaseURL = Environment.baseURL
  val CitizenURL = Environment.citizenURL
  val IdAMURL = Environment.idamURL
  val IdamUrl = Environment.idamURL
  val paymentURL = Environment.PaymentURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val caseFeeder=csv("caseIds.csv").circular
  
  /*======================================================================================
             * Civil Citizen R2 Claim creation
  ==========================================================================================*/
  val CaseProgUploadDocsByClaimant =
  
    exec(_.setAll(
      "Idempotencynumber" -> (Common.getIdempotency()),
      "EvidenceYear" -> Common.getYear(),
      "EvidenceDay" -> Common.getDay(),
      "EvidenceMonth" -> Common.getMonth(),
      "CitizenRandomString" -> Common.randomString(5),
      "representativeFullName" -> (Common.randomString(5) + "representativeFullName"))
    )
  
  
  /*======================================================================================
                 * Civil UI Claim - Respond to Claim- Click On Claim
      ==========================================================================================*/
  .group("CUICP_Claimant_UploadDocs_030_ClickOnClaim") {
    exec(http("CUICP_Claimant_UploadDocs_030_005_ClickOnClaim")
      .get(CitizenURL + "/dashboard/#{caseId}/claimant")
      .headers(CivilDamagesHeader.MoneyClaimNavHeader)
      .check(status.in(200, 304))
      .check(substring("Your money claims account"))
    
    )
    //another request to be added
  }
    .pause(MinThinkTime, MaxThinkTime)
    
    /*======================================================================================
                   * Civil UI Claim - View And Respond to Claim- Click On Claim
        ==========================================================================================*/
    .group("CUICP_Claimant_UploadDocs_040_ClickOnUploadDocLink") {
      exec(http("CUICP_Claimant_UploadDocs_040_005_ClickOnUploadDocLink")
        .get(CitizenURL + "/case/#{caseId}/case-progression/upload-your-documents")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Upload your documents"))
      
      )
    }
    
    /*======================================================================================
                   * Civil UI Claim - Click On Start to display documents types
        ==========================================================================================*/
    .group("CUICP_Claimant_UploadDocs_050_ClickOnStartForDocTypes") {
      exec(http("CUICP_Claimant_UploadDocs_050_005_ClickOnStartForDocTypes")
        .get(CitizenURL + "/case/#{caseId}/case-progression/type-of-documents")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("What types of documents do you want to upload?"))
      
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
    
    
  
      /*======================================================================================
               * Civil Citizen - upload file
    ==========================================================================================*/
      .group("CUICP_Claimant_UploadDocs_060_TypeOfDocs") {
        exec(http("CUICP_Claimant_UploadDocs_060_005_TypeOfDocs")
          .post(CitizenURL + "/case/#{caseId}/case-progression/type-of-documents")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("witnessStatement", "witnessStatement")
          .check(CsrfCheck.save)
          .check(substring("Witness evidence")))
      }
      .pause(MinThinkTime, MaxThinkTime)
      .pause(10)
  
  
      /*======================================================================================
               * Civil Citizen - select doc types -upload witness statement file
    ==========================================================================================*/
      .group("CUICP_Claimant_UploadDocs_070_UploadFile") {
        exec(http("CUICP_Claimant_UploadDocs_070_005_UploadFile")
          .post(CitizenURL + "/upload-file")
          .headers(CivilDamagesHeader.CitizenSTUpload)
          .header("accept", "*/*")
          .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundary1cdrHU4TGOqco6W7")
          .header("sec-fetch-dest", "")
          .header("sec-fetch-mode", "cors")
          .header("csrf-token", "#{csrf}")
          .bodyPart(RawFileBodyPart("file", "1MB-c.pdf")
            .fileName("1MB-c.pdf")
            .transferEncoding("binary"))
          .asMultipartForm
          .check(
            // Extracting values from the JSON response and saving them to session variables
            jsonPath("$.documentLink.document_url").saveAs("documentUrl"),
            jsonPath("$.documentLink.document_binary_url").saveAs("documentBinaryUrl"),
            jsonPath("$.documentLink.document_filename").saveAs("documentFilename"),
            jsonPath("$.documentLink.document_hash").saveAs("documentHash"),
            jsonPath("$.documentName").saveAs("documentName"),
            jsonPath("$.documentSize").saveAs("documentSize"),
            jsonPath("$.createdDatetime").saveAs("createdDatetime"),
            jsonPath("$.createdBy").saveAs("createdBy")
          )
          .check(substring("#{documentName}")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  

  
  
      /*======================================================================================
               * Civil Citizen - do you want to proceed with the claim post
    ==========================================================================================*/
    .group("CUICP_Claimant_UploadDocs_080_UploadDocsContinue") {
      exec(http("CUICP_Claimant_UploadDocs_080_005_UploadDocsContinue")
        .post("/case/#{caseId}/case-progression/upload-documents")
        .headers(CivilDamagesHeader.CivilCitizenPost)
        .header("Content-Type", "application/x-www-form-urlencoded") // Ensure form-encoded data
  
        // Add other form parameters
        .formParam("_csrf", "#{csrf}")
        .formParam("witnessStatement[0][witnessName]", "asaas")
        .formParam("witnessStatement[0][dateInputFields][dateDay]", "01")
        .formParam("witnessStatement[0][dateInputFields][dateMonth]", "01")
        .formParam("witnessStatement[0][dateInputFields][dateYear]", "2024")
        .formParam(" witnessStatement[0][fileUpload]", "#{documentName}")
        
        // JSON as a single string form parameter
        .formParam("witnessStatement[0][caseDocument]",
          """{
              "documentLink": {
                "document_url": "#{documentUrl}",
                "document_binary_url": "#{documentBinaryUrl}",
                "document_filename": "#{documentFilename}",
                "document_hash": "#{documentHash}"
              },
              "documentName": "#{documentName}",
              "documentSize": #{documentSize},
              "createdDatetime": "#{createdDatetime}",
              "createdBy": "Civil"
            }""".stripMargin.replaceAll("\n", "").replaceAll("  ", "")) // Convert to single-line JSON string
  
        // Optionally, check the response
        .check(status.is(200)) // Adjust status code based on expected response
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
    
    
    
    /*======================================================================================
  * Civil UI Claim - Check your answers
  ==========================================================================================*/
    
    .group("CUICP_Claimant_UploadDocs_090_SubmitDocs") {
      exec(http("CUICP_Claimant_UploadDocs_090_005_SubmitDocs")
        .post(CitizenURL + "/case/#{caseId}/case-progression/check-and-send")
        .headers(CivilDamagesHeader.DefCheckAndSendPost)
        .formParam("_csrf", "#{csrf}")
        .formParam("type", "")
        .formParam("signed", "true")
        .check(substring("Documents uploaded"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
  
  /*======================================================================================
             * Documents uploaded by Defendant
  ==========================================================================================*/
  val CaseProgUploadDocsByDefendant =
    exec(_.setAll(
      "Idempotencynumber" -> (Common.getIdempotency()),
      "EvidenceYear" -> Common.getYear(),
      "EvidenceDay" -> Common.getDay(),
      "EvidenceMonth" -> Common.getMonth(),
      "CitizenRandomString" -> Common.randomString(5),
      "representativeFullName" -> (Common.randomString(5) + "representativeFullName"))
    )
  
  
  /*======================================================================================
                 * Civil UI Claim - Respond to Claim- Click On Claim
      ==========================================================================================*/
  .group("CUICP_Def_UploadDocs_030_ClickOnClaimToRespond") {
    exec(http("CUICP_Def_UploadDocs_030_005_ClickOnClaimToRespond")
      .get(CitizenURL + "/dashboard/#{caseId}/defendant")
      .headers(CivilDamagesHeader.MoneyClaimNavHeader)
      .check(status.in(200, 304))
      .check(substring("Upload documents"))
    
    )
    //another request to be added
  }
    .pause(MinThinkTime, MaxThinkTime)
    
    /*======================================================================================
                   * Civil UI Claim - View And Respond to Claim- Click On Claim
        ==========================================================================================*/
    .group("CUICP_Def_UploadDocs_040_ClickOnUploadDoc") {
      exec(http("CUICP_Def_UploadDocs_040_005_ClickOnUploadDoc")
        .get(CitizenURL + "/case/#{caseId}/case-progression/upload-your-documents")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Upload your documents"))
      
      )
    }
    
    /*======================================================================================
                   * Civil UI Claim - Click On Start to display documents types
        ==========================================================================================*/
    .group("CUICP_Def_UploadDocs_050_ClickOnStartForDocTypes") {
      exec(http("CUICP_Def_UploadDocs_050_005_ClickOnStartForDocTypes")
        .get(CitizenURL + "/case/#{caseId}/case-progression/type-of-documents")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("What types of documents do you want to upload?"))
      
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
    
    
    
    
    
    
    /*======================================================================================
             * Civil Citizen - select doc types -upload witness statement
  ==========================================================================================*/
    .group("CUICP_Def_UploadDocs_060_TypeOfDocs") {
      exec(http("CUICP_Def_UploadDocs_060_005_TypeOfDocs")
        .post(CitizenURL + "/case/#{caseId}/case-progression/type-of-documents")
        .headers(CivilDamagesHeader.CivilCitizenPost)
        .formParam("_csrf", "#{csrf}")
        .formParam("witnessStatement", "witnessStatement")
        // .check(CsrfCheck.save)
        .check(substring("Witness evidence")))
     . exec(http("CUICP_Def_UploadDocs_040_005_ClickOnStartForDocTypes")
        .get(CitizenURL + "/case/#{caseId}/case-progression/upload-documents")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(status.in(200, 304))
        .check(substring("Witness evidence")))
    }
    .pause(MinThinkTime, MaxThinkTime)
  
  
  
  
  
    /*======================================================================================
             * Civil Citizen - select doc types -upload witness statement file
  ==========================================================================================*/
    .group("CUICP_Def_UploadDocs_070_UploadFile") {
      exec(http("CUICP_Def_UploadDocs_070_005_UploadFile")
        .post(CitizenURL + "/upload-file")
        .headers(CivilDamagesHeader.CitizenSTUpload)
        .header("accept", "*/*")
        .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundary1cdrHU4TGOqco6W7")
        .header("sec-fetch-dest", "")
        .header("sec-fetch-mode", "cors")
        .header("csrf-token", "#{csrf}")
        .bodyPart(RawFileBodyPart("file", "1MB-c.pdf")
          .fileName("1MB-c.pdf")
          .transferEncoding("binary"))
        .asMultipartForm
        .check(
          // Extracting values from the JSON response and saving them to session variables
          jsonPath("$.documentLink.document_url").saveAs("documentUrl"),
          jsonPath("$.documentLink.document_binary_url").saveAs("documentBinaryUrl"),
          jsonPath("$.documentLink.document_filename").saveAs("documentFilename"),
          jsonPath("$.documentLink.document_hash").saveAs("documentHash"),
          jsonPath("$.documentName").saveAs("documentName"),
          jsonPath("$.documentSize").saveAs("documentSize"),
          jsonPath("$.createdDatetime").saveAs("createdDatetime"),
          jsonPath("$.createdBy").saveAs("createdBy")
        )
        .check(substring("#{documentName}")))
    }
    .pause(MinThinkTime, MaxThinkTime)
  
  
  
  
    /*======================================================================================
             * Civil Citizen - do you want to proceed with the claim post
  ==========================================================================================*/
    .group("CUICP_Def_UploadDocs_080_UploadDocsContinue") {
      exec(http("CUICP_Def_UploadDocs_080_005_UploadDocsContinue")
        .post("/case/#{caseId}/case-progression/upload-documents")
        .headers(CivilDamagesHeader.CivilCitizenPost)
        .header("Content-Type", "application/x-www-form-urlencoded") // Ensure form-encoded data
      
        // Add other form parameters
        .formParam("_csrf", "#{csrf}")
        .formParam("witnessStatement[0][witnessName]", "asaas")
        .formParam("witnessStatement[0][dateInputFields][dateDay]", "01")
        .formParam("witnessStatement[0][dateInputFields][dateMonth]", "01")
        .formParam("witnessStatement[0][dateInputFields][dateYear]", "2024")
        .formParam(" witnessStatement[0][fileUpload]", "#{documentName}")
      
        // JSON as a single string form parameter
        .formParam("witnessStatement[0][caseDocument]",
          """{
           "documentLink": {
             "document_url": "#{documentUrl}",
             "document_binary_url": "#{documentBinaryUrl}",
             "document_filename": "#{documentFilename}",
             "document_hash": "#{documentHash}"
           },
           "documentName": "#{documentName}",
           "documentSize": #{documentSize},
           "createdDatetime": "#{createdDatetime}",
           "createdBy": "Civil"
         }""".stripMargin.replaceAll("\n", "").replaceAll("  ", "")) // Convert to single-line JSON string
      
        // Optionally, check the response
        .check(status.is(200)) // Adjust status code based on expected response
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
  
  
  
    /*======================================================================================
   * Civil UI Claim - Check your answers
   ==========================================================================================*/
  
    .group("CUICP_Def_UploadDocs_090_SubmitDocs") {
      exec(http("CUICP_Def_UploadDocs_090_005_SubmitDocs")
        .post(CitizenURL + "/case/#{caseId}/case-progression/check-and-send")
        .headers(CivilDamagesHeader.DefCheckAndSendPost)
        .formParam("_csrf", "#{csrf}")
        .formParam("type", "")
        .formParam("signed", "true")
        .check(substring("Documents uploaded"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
  
  val payHearingFee =

  /*======================================================================================
           * Civil Citizen -  2.Prepare your claim - pay claim fee
==========================================================================================*/
    group("CUICP_Claimant_HearingPay_100_ClickOnHearingPay") {
      exec(http("CUICP_Claimant_HearingPay_100_005_ClickOnHearingPay")
        .get(CitizenURL + "/case/#{caseId}/case-progression/pay-hearing-fee")
        .headers(CivilDamagesHeader.CUIR2Get)
        .check(CsrfCheck.save)
        .check(substring("Pay hearing fee")))
    }
      .pause(MinThinkTime, MaxThinkTime)
      .pause(10)
  
  /*======================================================================================
           * Civil Citizen -  2.Start Paying Hearing Fee
==========================================================================================*/
  .group("CUICP_Claimant_HearingPay_110_StartHearingPayment") {
    exec(http("CUICP_Claimant_110_005_StartHearingPayment")
      .get(CitizenURL + "/case/#{caseId}/case-progression/pay-hearing-fee/apply-help-fee-selection")
      .headers(CivilDamagesHeader.CUIR2Get)
      .check(CsrfCheck.save)
      .check(substring("Pay hearing fee")))
  }
    .pause(MinThinkTime, MaxThinkTime)
    .pause(10)
      
    /*======================================================================================
             * Civil Citizen -  2.Prepare your claim - Continue to pay
  ==========================================================================================*/
    .group("CUICP_Claimant_HearingPay_120_ContinueToPayPost") {
      exec(http("CUICP_Claimant_HearingPay_120_005_ContinueToPay")
        .post(CitizenURL + "/case/#{caseId}/case-progression/pay-hearing-fee/apply-help-fee-selection")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "no")
        //.check(CsrfCheck.save)
        .check(substring("Enter card details"))
        .check(css("input[name='csrfToken']", "value").saveAs("_csrfTokenCardDetailPage"))
        .check(css("input[name='chargeId']", "value").saveAs("CardDetailPageChargeId"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
  
    /*======================================================================================
                  * Civil Citizen -  2.Prepare your claim - Enter Card Details
       ==========================================================================================*/
    .group("CUICP_Claimant_HearingPay_130_CardDetail_SubmitCardDetail") {
      exec(http("CUICP_Claimant_HearingPay_130_005_CardDetail_SubmitCardDetail")
        .post(paymentURL + "/card_details/#{CardDetailPageChargeId}")
        .formParam("chargeId", "#{CardDetailPageChargeId}")
        .formParam("csrfToken", "#{_csrfTokenCardDetailPage}")
        .formParam("cardNo", "4444333322221111")
        .formParam("expiryMonth", "07")
        .formParam("expiryYear", "26")
        .formParam("cardholderName", "Perf Tester")
        .formParam("cvc", "123")
        .formParam("addressLine1", "4")
        .formParam("addressLine2", "Hibernia Gardens")
        .formParam("addressCity", "Hounslow")
        .formParam("addressCountry", "GB")
        .formParam("addressPostcode", "TW3 3SD")
        .formParam("email", "vijaykanth6@gmail.com")
        .check(css("input[name='csrfToken']", "value").saveAs("_csrfTokenCardDetailConfirm"))
        .check(regex("Confirm your payment")))
    }
    .pause(MinThinkTime, MaxThinkTime)
  
    /*======================================================================================
                       * Civil Citizen -  2.Prepare your claim - Enter Card Details
     ==========================================================================================*/
  
    // confirm the card details and submit
    .group("CUICP_Claimant_HearingPay_140_CardDetail_ConfirmCardDetail") {
      exec(http("CUICP_Claimant_HearingPay_140_005_CardDetail_ConfirmCardDetail")
        .post(paymentURL + "/card_details/${CardDetailPageChargeId}/confirm")
        .formParam("csrfToken", "#{_csrfTokenCardDetailConfirm}")
        .formParam("chargeId", "#{CardDetailPageChargeId}")
        .check(regex("Your payment was"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
  
  
  /*======================================================================================
          * Civil Citizen -  2.Prepare your claim - ViewOrdersAndNotices
 ==========================================================================================*/
  
  val viewOrderandNotices=
 
    group("CUICP_Claimant_ViewHearings_150_ViewOrdersAndNotices") {
      exec(http("CUICP_Claimant_ViewHearings_150_005_ViewOrderNotices")
        .get(CitizenURL + "/case/#{caseId}/view-orders-and-notices")
        .headers(CivilDamagesHeader.CUIR2Get)
        .check(substring("View orders and notices")))
    }
      .pause(MinThinkTime, MaxThinkTime)
      .pause(10)
  
  /*======================================================================================
          * Civil Citizen -  2.Prepare your claim - ViewUploaded Documents
 ==========================================================================================*/
  
  val viewUploadedDocuments =
    
    group("CUICP_Claimant_ViewDocs_160_ViewUploadedDocuments") {
      exec(http("CUICP_Claimant_ViewHearings_160_005_ViewUploadedDocuments")
        .get(CitizenURL + "/case/#{caseId}/evidence-upload-documents")
        .headers(CivilDamagesHeader.CUIR2Get)
        .check(substring("View documents")))
    }
      .pause(MinThinkTime, MaxThinkTime)
      .pause(10)
  
  
}
