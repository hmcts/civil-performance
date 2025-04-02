
package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef.{exec, _}
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.CsrfCheck
//import io.gatling.javaapi.http.HttpDsl.RawFileBodyPart
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, Common, CsrfCheck, Environment, Headers}

object CUIR2DocUploadCaseProg {

  val BaseURL = Environment.baseURL
  val CitizenURL = Environment.citizenURL
  val IdAMURL = Environment.idamURL
  val IdamUrl = Environment.idamURL
  val paymentURL = Environment.PaymentURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val caseFeeder=csv("claimNumbers.csv").circular
  
  /*======================================================================================
             * Civil Citizen R2 CP Docs Upload For Small claims
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
  .group("CUICPSC_Claimant_UploadDocs_030_ClickOnClaim") {
    exec(http("CUICPSC_Claimant_UploadDocs_030_005_ClickOnClaim")
      .get(CitizenURL + "/dashboard/#{claimNumber}/claimant")
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
    .group("CUICPSC_Claimant_UploadDocs_040_ClickOnUploadDocLink") {
      exec(http("CUICPSC_Claimant_UploadDocs_040_005_ClickOnUploadDocLink")
        .get(CitizenURL + "/case/#{claimNumber}/case-progression/upload-your-documents")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Upload your documents"))
      
      )
    }
    
    /*======================================================================================
                   * Civil UI Claim - Click On Start to display documents types
        ==========================================================================================*/
    .group("CUICPSC_Claimant_UploadDocs_050_ClickOnStartForDocTypes") {
      exec(http("CUICPSC_Claimant_UploadDocs_050_005_ClickOnStartForDocTypes")
        .get(CitizenURL + "/case/#{claimNumber}/case-progression/type-of-documents")
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
      .group("CUICPSC_Claimant_UploadDocs_060_TypeOfDocs") {
        exec(http("CUICPSC_Claimant_UploadDocs_060_005_TypeOfDocs")
          .post(CitizenURL + "/case/#{claimNumber}/case-progression/type-of-documents")
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
      .group("CUICPSC_Claimant_UploadDocs_070_UploadFile") {
        exec(http("CUICPFT_Claimant_UploadDocs_070_005_UploadFile")
          .post(CitizenURL + "/case/#{claimNumber}/case-progression/upload-documents?_csrf=#{csrf}")
          .headers(Headers.uploadHeader)
          .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
          .formParam("_csrf", "#{csrf}") // CSRF token
          .formParam("witnessStatement[0][witnessName]", "aasasasas")
          .formParam("witnessStatement[0][dateInputFields][dateDay]", "01")
          .formParam("witnessStatement[0][dateInputFields][dateMonth]", "08")
          .formParam("witnessStatement[0][dateInputFields][dateYear]", "2024")
          .formParam("action", "witnessStatement[0][uploadButton]")
          .bodyPart(RawFileBodyPart("witnessStatement[0][fileUpload]", "3MB.pdf")
            .fileName("3MB.pdf")
            .transferEncoding("binary"))
          .asMultipartForm
        //  .formParam("classification", "PUBLIC")
          
          .check(
            regex("""document_url&quot;:&quot;(http[^&]+)""").saveAs("documentUrl"),
            regex("""document_binary_url&quot;:&quot;(http[^&]+)""").saveAs("documentBinaryUrl"),
            regex("""document_filename&quot;:&quot;([^&]+)""").saveAs("documentFilename"),
            regex("""document_hash&quot;:&quot;([a-fA-F0-9]+)""").saveAs("documentHash"),
            regex("""documentSize&quot;:([0-9]+)""").saveAs("documentSize"),
            regex("""documentName&quot;:&quot;([^&]+)""").saveAs("documentName"),
            regex("""createdDatetime&quot;:&quot;([^&]+)""").saveAs("createdDatetime"),
            regex("""createdBy&quot;:&quot;([^&]+)""").saveAs("createdBy")
          )
          
          .check(CsrfCheck.save)
          .check(substring("#{documentName}")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  
      /*======================================================================================
               * Civil Citizen - do you want to proceed with the claim post
    ==========================================================================================*/
    .group("CUICPSC_Claimant_UploadDocs_080_UploadDocsContinue") {
      exec(http("CUICPSC_Claimant_UploadDocs_080_005_UploadDocsContinue")
        .post(CitizenURL+"/case/#{claimNumber}/case-progression/upload-documents?_csrf=#{csrf}")
        .headers(Headers.uploadHeader)
       // .header("Content-Type", "application/x-www-form-urlencoded") // Ensure form-encoded data
  
        // Add other form parameters
      //  .formParam("_csrf", "#{csrf}")
        .formParam("witnessStatement[0][witnessName]", "aasasasas")
        .formParam("witnessStatement[0][dateInputFields][dateDay]", "01")
        .formParam("witnessStatement[0][dateInputFields][dateMonth]", "08")
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
              "createdBy": "createdBy"
            }""".stripMargin.replaceAll("\n", "").replaceAll("  ", "")) // Convert to single-line JSON string
        // Optionally, check the response
        .check(CsrfCheck.save)
        .check(status.is(200)) // Adjust status code based on expected response
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
    
    
    
    
    /*======================================================================================
  * Civil UI Claim - Check your answers
  ==========================================================================================*/
    
    .group("CUICPSC_Claimant_UploadDocs_090_SubmitDocs") {
      exec(http("CUICPSC_Claimant_UploadDocs_090_005_SubmitDocs")
        .post(CitizenURL + "/case/#{claimNumber}/case-progression/check-and-send")
        .headers(CivilDamagesHeader.DefCheckAndSendPost)
        .formParam("_csrf", "#{csrf}")
        .formParam("type", "")
        .formParam("signed", "true")
        .check(substring("Documents uploaded"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
  
  
  /*======================================================================================
             * Civil Citizen R2 CP Docs Upload For Fast Track
  ==========================================================================================*/
  val CaseProgUploadDocsByClaimantForFastTrack =
    
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
      .group("CUICPFT_Claimant_UploadDocs_030_ClickOnClaim") {
        exec(http("CUICPFT_Claimant_UploadDocs_030_005_ClickOnClaim")
          .get(CitizenURL + "/dashboard/#{claimNumber}/claimant")
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
      .group("CUICPFT_Claimant_UploadDocs_040_ClickOnUploadDocLink") {
        exec(http("CUICPFT_Claimant_UploadDocs_040_005_ClickOnUploadDocLink")
          .get(CitizenURL + "/case/#{claimNumber}/case-progression/upload-your-documents")
          .headers(CivilDamagesHeader.MoneyClaimNavHeader)
          .check(CsrfCheck.save)
          .check(status.in(200, 304))
          .check(substring("Upload your documents"))
        
        )
      }
      
      /*======================================================================================
                     * Civil UI Claim - Click On Start to display documents types
          ==========================================================================================*/
      .group("CUICPFT_Claimant_UploadDocs_050_ClickOnStartForDocTypes") {
        exec(http("CUICPFT_Claimant_UploadDocs_050_005_ClickOnStartForDocTypes")
          .get(CitizenURL + "/case/#{claimNumber}/case-progression/type-of-documents")
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
      .group("CUICPFT_Claimant_UploadDocs_060_TypeOfDocs") {
        exec(http("CUICPFT_Claimant_UploadDocs_060_005_TypeOfDocs")
          .post(CitizenURL + "/case/#{claimNumber}/case-progression/type-of-documents")
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
      .group("CUICPFT_Claimant_UploadDocs_070_UploadFile") {
        exec(http("CUICPFT_Claimant_UploadDocs_070_005_UploadFile")
          .post(CitizenURL + "/case/#{claimNumber}/case-progression/upload-documents?_csrf=#{csrf}")
          .headers(Headers.uploadHeader)
          .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
          .formParam("_csrf", "#{csrf}") // CSRF token
          .formParam("witnessStatement[0][witnessName]", "aasasasas")
          .formParam("witnessStatement[0][dateInputFields][dateDay]", "01")
          .formParam("witnessStatement[0][dateInputFields][dateMonth]", "08")
          .formParam("witnessStatement[0][dateInputFields][dateYear]", "2024")
          .formParam("action", "witnessStatement[0][uploadButton]")
          .bodyPart(RawFileBodyPart("witnessStatement[0][fileUpload]", "3MB.pdf")
            .fileName("3MB.pdf")
            .transferEncoding("binary"))
          .asMultipartForm
          //  .formParam("classification", "PUBLIC")
      
          .check(
            regex("""document_url&quot;:&quot;(http[^&]+)""").saveAs("documentUrl"),
            regex("""document_binary_url&quot;:&quot;(http[^&]+)""").saveAs("documentBinaryUrl"),
            regex("""document_filename&quot;:&quot;([^&]+)""").saveAs("documentFilename"),
            regex("""document_hash&quot;:&quot;([a-fA-F0-9]+)""").saveAs("documentHash"),
            regex("""documentSize&quot;:([0-9]+)""").saveAs("documentSize"),
            regex("""documentName&quot;:&quot;([^&]+)""").saveAs("documentName"),
            regex("""createdDatetime&quot;:&quot;([^&]+)""").saveAs("createdDatetime"),
            regex("""createdBy&quot;:&quot;([^&]+)""").saveAs("createdBy")
          )
      
          .check(CsrfCheck.save)
          .check(substring("#{documentName}")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  
      /*======================================================================================
               * Civil Citizen - do you want to proceed with the claim post
    ==========================================================================================*/
      .group("CUICPFT_Claimant_UploadDocs_080_UploadDocsContinue") {
        exec(http("CUICPSC_Claimant_UploadDocs_080_005_UploadDocsContinue")
          .post(CitizenURL + "/case/#{claimNumber}/case-progression/upload-documents?_csrf=#{csrf}")
          .headers(Headers.uploadHeader)
          // .header("Content-Type", "application/x-www-form-urlencoded") // Ensure form-encoded data
      
          // Add other form parameters
          //  .formParam("_csrf", "#{csrf}")
          .formParam("witnessStatement[0][witnessName]", "aasasasas")
          .formParam("witnessStatement[0][dateInputFields][dateDay]", "01")
          .formParam("witnessStatement[0][dateInputFields][dateMonth]", "08")
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
             "createdBy": "createdBy"
           }""".stripMargin.replaceAll("\n", "").replaceAll("  ", "")) // Convert to single-line JSON string
          // Optionally, check the response
          .check(CsrfCheck.save)
          .check(status.is(200)) // Adjust status code based on expected response
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  
  
  
      /*======================================================================================
    * Civil UI Claim - Check your answers
    ==========================================================================================*/
      
      .group("CUICPFT_Claimant_UploadDocs_090_SubmitDocs") {
        exec(http("CUICPFT_Claimant_UploadDocs_090_005_SubmitDocs")
          .post(CitizenURL + "/case/#{claimNumber}/case-progression/check-and-send")
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
  .group("CUICPSC_Def_UploadDocs_030_ClickOnClaimToRespond") {
    exec(http("CUICPSC_Def_UploadDocs_030_005_ClickOnClaimToRespond")
      .get(CitizenURL + "/dashboard/#{claimNumber}/defendant")
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
    .group("CUICPSC_Def_UploadDocs_040_ClickOnUploadDoc") {
      exec(http("CUICPSC_Def_UploadDocs_040_005_ClickOnUploadDoc")
        .get(CitizenURL + "/case/#{claimNumber}/case-progression/upload-your-documents")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Upload your documents"))
      
      )
    }
    
    /*======================================================================================
                   * Civil UI Claim - Click On Start to display documents types
        ==========================================================================================*/
    .group("CUICPSC_Def_UploadDocs_050_ClickOnStartForDocTypes") {
      exec(http("CUICPSC_Def_UploadDocs_050_005_ClickOnStartForDocTypes")
        .get(CitizenURL + "/case/#{claimNumber}/case-progression/type-of-documents")
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
    .group("CUICPSC_Def_UploadDocs_060_TypeOfDocs") {
      exec(http("CUICPSC_Def_UploadDocs_060_005_TypeOfDocs")
        .post(CitizenURL + "/case/#{claimNumber}/case-progression/type-of-documents")
        .headers(CivilDamagesHeader.CivilCitizenPost)
        .formParam("_csrf", "#{csrf}")
        .formParam("witnessStatement", "witnessStatement")
        // .check(CsrfCheck.save)
        .check(substring("Witness evidence")))
        
     . exec(http("CUICPSC_Def_UploadDocs_040_005_ClickOnStartForDocTypes")
        .get(CitizenURL + "/case/#{claimNumber}/case-progression/upload-documents")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(status.in(200, 304))
        .check(substring("Witness evidence")))
    }
    .pause(MinThinkTime, MaxThinkTime)
  
  
  
  
  
      /*======================================================================================
               * Civil Citizen - select doc types -upload witness statement file
    ==========================================================================================*/
      .group("CUICPSC_Def_UploadDocs_070_UploadFile") {
        exec(http("CUICPSC_Def_UploadDocs_070_005_UploadFile")
          .post(CitizenURL + "/case/#{claimNumber}/case-progression/upload-documents?_csrf=#{csrf}")
          .headers(Headers.uploadHeader)
          .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
          .formParam("_csrf", "#{csrf}") // CSRF token
          .formParam("witnessStatement[0][witnessName]", "aasasasas")
          .formParam("witnessStatement[0][dateInputFields][dateDay]", "01")
          .formParam("witnessStatement[0][dateInputFields][dateMonth]", "08")
          .formParam("witnessStatement[0][dateInputFields][dateYear]", "2024")
          .formParam("action", "witnessStatement[0][uploadButton]")
          .bodyPart(RawFileBodyPart("witnessStatement[0][fileUpload]", "3MB.pdf")
            .fileName("3MB.pdf")
            .transferEncoding("binary"))
          .asMultipartForm
          //  .formParam("classification", "PUBLIC")
      
          .check(
            regex("""document_url&quot;:&quot;(http[^&]+)""").saveAs("documentUrl"),
            regex("""document_binary_url&quot;:&quot;(http[^&]+)""").saveAs("documentBinaryUrl"),
            regex("""document_filename&quot;:&quot;([^&]+)""").saveAs("documentFilename"),
            regex("""document_hash&quot;:&quot;([a-fA-F0-9]+)""").saveAs("documentHash"),
            regex("""documentSize&quot;:([0-9]+)""").saveAs("documentSize"),
            regex("""documentName&quot;:&quot;([^&]+)""").saveAs("documentName"),
            regex("""createdDatetime&quot;:&quot;([^&]+)""").saveAs("createdDatetime"),
            regex("""createdBy&quot;:&quot;([^&]+)""").saveAs("createdBy")
          )
      
          .check(CsrfCheck.save)
          .check(substring("#{documentName}")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  
      /*======================================================================================
               * Civil Citizen - do you want to proceed with the claim post
    ==========================================================================================*/
      .group("CUICPSC_Def_UploadDocs_080_UploadDocsContinue") {
        exec(http("CUICPSC_Def_UploadDocs_080_005_UploadDocsContinue")
          .post(CitizenURL + "/case/#{claimNumber}/case-progression/upload-documents?_csrf=#{csrf}")
          .headers(Headers.uploadHeader)
          // .header("Content-Type", "application/x-www-form-urlencoded") // Ensure form-encoded data
      
          // Add other form parameters
          //  .formParam("_csrf", "#{csrf}")
          .formParam("witnessStatement[0][witnessName]", "aasasasas")
          .formParam("witnessStatement[0][dateInputFields][dateDay]", "01")
          .formParam("witnessStatement[0][dateInputFields][dateMonth]", "08")
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
           "createdBy": "createdBy"
         }""".stripMargin.replaceAll("\n", "").replaceAll("  ", "")) // Convert to single-line JSON string
          // Optionally, check the response
          .check(CsrfCheck.save)
          .check(status.is(200)) // Adjust status code based on expected response
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
    /*======================================================================================
   * Civil UI Claim - Check your answers
   ==========================================================================================*/
  
    .group("CUICPSC_Def_UploadDocs_090_SubmitDocs") {
      exec(http("CUICPSC_Def_UploadDocs_090_005_SubmitDocs")
        .post(CitizenURL + "/case/#{claimNumber}/case-progression/check-and-send")
        .headers(CivilDamagesHeader.DefCheckAndSendPost)
        .formParam("_csrf", "#{csrf}")
        .formParam("type", "")
        .formParam("signed", "true")
        .check(substring("Documents uploaded"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
  
  
  /*======================================================================================
             * Documents uploaded by Defendant For Fast Track Claims
  ==========================================================================================*/
  val CaseProgUploadDocsByDefendantForFastTrack =
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
      .group("CUICPFT_Def_UploadDocs_030_ClickOnClaimToRespond") {
        exec(http("CUICPFT_Def_UploadDocs_030_005_ClickOnClaimToRespond")
          .get(CitizenURL + "/dashboard/#{claimNumber}/defendant")
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
      .group("CUICPFT_Def_UploadDocs_040_ClickOnUploadDoc") {
        exec(http("CUICPFT_Def_UploadDocs_040_005_ClickOnUploadDoc")
          .get(CitizenURL + "/case/#{claimNumber}/case-progression/upload-your-documents")
          .headers(CivilDamagesHeader.MoneyClaimNavHeader)
          .check(CsrfCheck.save)
          .check(status.in(200, 304))
          .check(substring("Upload your documents"))
        
        )
      }
      
      /*======================================================================================
                     * Civil UI Claim - Click On Start to display documents types
          ==========================================================================================*/
      .group("CUICPFT_Def_UploadDocs_050_ClickOnStartForDocTypes") {
        exec(http("CUICPFT_Def_UploadDocs_050_005_ClickOnStartForDocTypes")
          .get(CitizenURL + "/case/#{claimNumber}/case-progression/type-of-documents")
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
      .group("CUICPFT_Def_UploadDocs_060_TypeOfDocs") {
        exec(http("CUICPFT_Def_UploadDocs_060_005_TypeOfDocs")
          .post(CitizenURL + "/case/#{claimNumber}/case-progression/type-of-documents")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("witnessStatement", "witnessStatement")
          // .check(CsrfCheck.save)
          .check(substring("Witness evidence")))
          .exec(http("CUICPFT_Def_UploadDocs_040_005_ClickOnStartForDocTypes")
            .get(CitizenURL + "/case/#{claimNumber}/case-progression/upload-documents")
            .headers(CivilDamagesHeader.MoneyClaimNavHeader)
            .check(status.in(200, 304))
            .check(substring("Witness evidence")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  
  
      /*======================================================================================
               * Civil Citizen - select doc types -upload witness statement file
    ==========================================================================================*/
      .group("CUICPFT_Def_UploadDocs_070_UploadFile") {
        exec(http("CUICPFT_Def_UploadDocs_070_005_UploadFile")
          .post(CitizenURL + "/case/#{claimNumber}/case-progression/upload-documents?_csrf=#{csrf}")
          .headers(Headers.uploadHeader)
          .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
          .formParam("_csrf", "#{csrf}") // CSRF token
          .formParam("witnessStatement[0][witnessName]", "aasasasas")
          .formParam("witnessStatement[0][dateInputFields][dateDay]", "01")
          .formParam("witnessStatement[0][dateInputFields][dateMonth]", "08")
          .formParam("witnessStatement[0][dateInputFields][dateYear]", "2024")
          .formParam("action", "witnessStatement[0][uploadButton]")
          .bodyPart(RawFileBodyPart("witnessStatement[0][fileUpload]", "3MB.pdf")
            .fileName("3MB.pdf")
            .transferEncoding("binary"))
          .asMultipartForm
          //  .formParam("classification", "PUBLIC")
      
          .check(
            regex("""document_url&quot;:&quot;(http[^&]+)""").saveAs("documentUrl"),
            regex("""document_binary_url&quot;:&quot;(http[^&]+)""").saveAs("documentBinaryUrl"),
            regex("""document_filename&quot;:&quot;([^&]+)""").saveAs("documentFilename"),
            regex("""document_hash&quot;:&quot;([a-fA-F0-9]+)""").saveAs("documentHash"),
            regex("""documentSize&quot;:([0-9]+)""").saveAs("documentSize"),
            regex("""documentName&quot;:&quot;([^&]+)""").saveAs("documentName"),
            regex("""createdDatetime&quot;:&quot;([^&]+)""").saveAs("createdDatetime"),
            regex("""createdBy&quot;:&quot;([^&]+)""").saveAs("createdBy")
          )
      
          .check(CsrfCheck.save)
          .check(substring("#{documentName}")))
      }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  
      /*======================================================================================
               * Civil Citizen - do you want to proceed with the claim post
    ==========================================================================================*/
      .group("CUICPFT_Def_UploadDocs_080_UploadDocsContinue") {
        exec(http("CUICPFT_Def_UploadDocs_080_005_UploadDocsContinue")
          .post(CitizenURL + "/case/#{claimNumber}/case-progression/upload-documents?_csrf=#{csrf}")
          .headers(Headers.uploadHeader)
          // .header("Content-Type", "application/x-www-form-urlencoded") // Ensure form-encoded data
      
          // Add other form parameters
          //  .formParam("_csrf", "#{csrf}")
          .formParam("witnessStatement[0][witnessName]", "aasasasas")
          .formParam("witnessStatement[0][dateInputFields][dateDay]", "01")
          .formParam("witnessStatement[0][dateInputFields][dateMonth]", "08")
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
            "createdBy": "createdBy"
          }""".stripMargin.replaceAll("\n", "").replaceAll("  ", "")) // Convert to single-line JSON string
          // Optionally, check the response
          .check(CsrfCheck.save)
          .check(status.is(200)) // Adjust status code based on expected response
        )
      }
      .pause(MinThinkTime, MaxThinkTime)
      
      /*======================================================================================
     * Civil UI Claim - Check your answers
     ==========================================================================================*/
      
      .group("CUICPFT_Def_UploadDocs_090_SubmitDocs") {
        exec(http("CUICPFT_Def_UploadDocs_090_005_SubmitDocs")
          .post(CitizenURL + "/case/#{claimNumber}/case-progression/check-and-send")
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
    group("CUICPFT_Claimant_HearingPay_100_ClickOnHearingPay") {
      exec(http("CUICPFT_Claimant_HearingPay_100_005_ClickOnHearingPay")
        .get(CitizenURL + "/case/#{claimNumber}/case-progression/pay-hearing-fee")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(CsrfCheck.save)
        .check(substring("Pay hearing fee")))
    }
      .pause(MinThinkTime, MaxThinkTime)
      .pause(10)
  
  /*======================================================================================
           * Civil Citizen -  2.Start Paying Hearing Fee
==========================================================================================*/
  .group("CUICPFT_Claimant_HearingPay_110_StartHearingPayment") {
    exec(http("CUICPFT_Claimant_110_005_StartHearingPayment")
      .get(CitizenURL + "/case/#{claimNumber}/case-progression/pay-hearing-fee/apply-help-fee-selection")
      .headers(CivilDamagesHeader.MoneyClaimNavHeader)
      .check(CsrfCheck.save)
      .check(substring("Pay hearing fee")))
  }
    .pause(MinThinkTime, MaxThinkTime)
    .pause(10)
      
    /*======================================================================================
             * Civil Citizen -  2.Prepare your claim - Continue to pay
  ==========================================================================================*/
    .group("CUICPFT_Claimant_HearingPay_120_ContinueToPayPost") {
      exec(http("CUICPFT_Claimant_HearingPay_120_005_ContinueToPay")
        .post(CitizenURL + "/case/#{claimNumber}/case-progression/pay-hearing-fee/apply-help-fee-selection")
        .headers(CivilDamagesHeader.CivilCitizenPost)
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
    .group("CUICPFT_Claimant_HearingPay_130_CardDetail_SubmitCardDetail") {
      exec(http("CUICPFT_Claimant_HearingPay_130_005_CardDetail_SubmitCardDetail")
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
    .group("CUICPFT_Claimant_HearingPay_140_CardDetail_SubmitConfirmCardDetail") {
      exec(http("CUICPFT_Claimant_HearingPay_140_005_SubmitCardDetail_ConfirmCardDetail")
        .post(paymentURL + "/card_details/${CardDetailPageChargeId}/confirm")
        .formParam("csrfToken", "#{_csrfTokenCardDetailConfirm}")
        .formParam("chargeId", "#{CardDetailPageChargeId}")
        .check(regex("Your payment was"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
  
  
  /*======================================================================================
          * Civil Citizen -  2.Prepare your claim - ViewOrdersAndNotices For Small Claims
 ==========================================================================================*/
  
  val viewOrderandNotices=
 
    group("CUICPSC_Claimant_ViewHearings_150_ViewOrdersAndNotices") {
      exec(http("CUICPSC_Claimant_ViewHearings_150_005_ViewOrderNotices")
        .get(CitizenURL + "/case/#{claimNumber}/view-orders-and-notices")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(substring("View orders and notices")))
    }
      .pause(MinThinkTime, MaxThinkTime)
      .pause(10)
  
  
  
  /*======================================================================================
          * Civil Citizen -  2.Prepare your claim - ViewOrdersAndNotices For Small Claims
 ==========================================================================================*/
  
  val viewOrderandNoticesForFastTrack =
    
    group("CUICPFT_Claimant_ViewHearings_150_ViewOrdersAndNotices") {
      exec(http("CUICPFT_Claimant_ViewHearings_150_005_ViewOrderNotices")
        .get(CitizenURL + "/case/#{claimNumber}/view-orders-and-notices")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(substring("View orders and notices")))
    }
      .pause(MinThinkTime, MaxThinkTime)
      .pause(10)
  
  
  /*======================================================================================
          * Civil Citizen -  2.Prepare your claim - ViewUploaded Documents - Small Claims
 ==========================================================================================*/
  
  val viewUploadedDocuments =
    
    group("CUICPSC_Claimant_ViewDocs_160_ViewUploadedDocuments") {
      exec(http("CUICPSC_Claimant_ViewHearings_160_005_ViewUploadedDocuments")
        .get(CitizenURL + "/case/#{claimNumber}/evidence-upload-documents")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(substring("View documents")))
    }
      .pause(MinThinkTime, MaxThinkTime)
      .pause(10)
  
  /*======================================================================================
          * Civil Citizen -  2.Prepare your claim - ViewUploaded Documents - Fast Claims
 ==========================================================================================*/
  
  val viewUploadedDocumentsForFastTrack =
    
    group("CUICPFT_Claimant_ViewDocs_160_ViewUploadedDocuments") {
      exec(http("CUICPFT_Claimant_ViewHearings_160_005_ViewUploadedDocuments")
        .get(CitizenURL + "/case/#{claimNumber}/evidence-upload-documents")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(substring("View documents")))
    }
      .pause(MinThinkTime, MaxThinkTime)
      .pause(10)
  
  
  
  /*======================================================================================
          * Civil Citizen -  View Bundles
 ==========================================================================================*/
  
  val viewBundle =
    
    group("CUICPFT_Claimant_ViewDocs_160_ViewBundles") {
      exec(http("CUICPFT_Claimant_ViewHearings_160_005_viewBundles")
        .get(CitizenURL + "/case/#{claimNumber}/bundle-overview")
        .headers(CivilDamagesHeader.MoneyClaimNavHeader)
        .check(substring("View the bundle")))
    }
      .pause(MinThinkTime, MaxThinkTime)
      .pause(10)
  
  
  /*======================================================================================
          * Civil Citizen -  Trial Complete
 ==========================================================================================*/
  
  val TrialArrangements=
  
  /*======================================================================================
                 * Civil UI Claim - Click Trial Arrangements
      ==========================================================================================*/
  group("CUICPFT_Claimant_UploadDocs_040_ClickOnTrialArrangements") {
    exec(http("CUICPFT_Claimant_UploadDocs_040_005_ClickOnTrialArrangements")
      .get(CitizenURL + "/case/#{claimNumber}/case-progression/finalise-trial-arrangements")
      .headers(CivilDamagesHeader.MoneyClaimNavHeader)
      .check(CsrfCheck.save)
      .check(status.in(200, 304))
      .check(substring("Finalise your trial arrangements"))
    
    )
  }
    .pause(MinThinkTime, MaxThinkTime)
  
  /*======================================================================================
                 * Civil UI Claim - Click Trial Arrangements
      ==========================================================================================*/
  .group("CUICPFT_Claimant_UploadDocs_040_IsCaseReadyGet") {
    exec(http("CUICPFT_Claimant_UploadDocs_040_005_IsCaseReady")
      .get(CitizenURL + "/case/#{claimNumber}/case-progression/finalise-trial-arrangements/is-case-ready")
      .headers(CivilDamagesHeader.MoneyClaimNavHeader)
      .check(CsrfCheck.save)
      .check(status.in(200, 304))
      .check(substring("Finalise your trial arrangements"))
    
    )
  }
    .pause(MinThinkTime, MaxThinkTime)
  
   
  
    /*======================================================================================
                   * Civil UI Claim - Click Trial Arrangements
        ==========================================================================================*/
    .group("CUICPFT_Claimant_UploadDocs_040_IsCaseReadyPost") {
      exec(http("CUICPFT_Claimant_UploadDocs_040_005_IsCaseReadyPost")
        .post(CitizenURL + "/case/#{claimNumber}/case-progression/finalise-trial-arrangements/is-case-ready")
        .headers(CivilDamagesHeader.CivilCitizenPost)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "yes")
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Finalise your trial arrangements"))
    
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
  
    /*======================================================================================
                   * Civil UI Claim - Click Trial Arrangements
        ==========================================================================================*/
    .group("CUICPFT_Claimant_UploadDocs_040_HasAnythingChanged") {
      exec(http("CUICPFT_Claimant_UploadDocs_040_005_HasAnythingChanged")
        .post(CitizenURL + "/case/#{claimNumber}/case-progression/finalise-trial-arrangements/has-anything-changed")
        .headers(CivilDamagesHeader.CivilCitizenPost)
        .formParam("_csrf", "#{csrf}")
        .formParam("option", "no")
        .formParam("textArea", "asasasasas")
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Finalise your trial arrangements"))
    
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
  
  
    /*======================================================================================
                   * Civil UI Claim - Checkyour answers
        ==========================================================================================*/
    .group("CUICPFT_Claimant_UploadDocs_040_HearingDuration") {
      exec(http("CUICPFT_Claimant_UploadDocs_040_005_HearingDuration")
        .post(CitizenURL + "/case/#{claimNumber}/case-progression/finalise-trial-arrangements/hearing-duration-other-info")
        .headers(CivilDamagesHeader.CivilCitizenPost)
        .formParam("_csrf", "#{csrf}")
        .formParam("otherInformation", "sdsdsdsdsd")
        .check(CsrfCheck.save)
        .check(status.in(200, 304))
        .check(substring("Check your answers"))
    
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
  
    /*======================================================================================
                   * Civil UI Claim - Click Trial Arrangements
        ==========================================================================================*/
    .group("CUICPFT_Claimant_UploadDocs_040_TrialArrangementsSubmit") {
      exec(http("CUICPFT_Claimant_UploadDocs_040_005_TrialArrangementsSubmit")
        .post(CitizenURL + "/case/#{claimNumber}/case-progression/finalise-trial-arrangements/check-trial-arrangements")
        .headers(CivilDamagesHeader.CivilCitizenPost)
        .formParam("_csrf", "#{csrf}")
        .check(status.in(200, 304))
        .check(substring("You have said this case is ready for trial"))
    
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
  
  
 
  
  
}
