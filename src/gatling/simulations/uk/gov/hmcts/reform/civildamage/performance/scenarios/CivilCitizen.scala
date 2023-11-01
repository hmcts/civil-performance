
package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, Common, Environment, CsrfCheck}

import java.io.{BufferedWriter, FileWriter}

object CivilCitizen {

  val BaseURL = Environment.baseURL
  val CitizenURL = Environment.citizenURL
  val IdAMURL = Environment.idamURL
  val IdamUrl = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val caseFeeder=csv("caseIds.csv").circular

  /*======================================================================================
             * Click On pay from Service Page
  ==========================================================================================*/
  val run=


    exec(_.setAll(
      "Idempotencynumber" -> (Common.getIdempotency()),
      "EvidenceYear" -> Common.getYear(),
      "EvidenceDay" -> Common.getDay(),
      "EvidenceMonth" -> Common.getMonth(),
      "CitizenRandomString" -> Common.randomString(5))
    )
  //val Citizen =

    //val startCitizen =
    /*======================================================================================
                 * Civil Citizen - Home Page
      ==========================================================================================*/
    .group("Civil_Citizen_010_HomePage") {
      exec(http("Civil_Citizen_010_005_HomePage")
        .get(CitizenURL)
        .headers(CivilDamagesHeader.MoneyClaimNav)
        .check(substring("Submit a First-tier Tribunal form"))
      //  .check(jsonPath("$.event_token").optional.saveAs("event_token"))
      )
        .pause(MinThinkTime, MaxThinkTime)
    }

    /*======================================================================================
                 * Civil Citizen - Submit a First-tier Tribunal form
      ==========================================================================================*/
    .group("Civil_Citizen_020_SubmitAForm") {
      exec(http("Civil_Citizen_020_SubmitAForm")
        .get(CitizenURL + "/login")
        .headers(CivilDamagesHeader.MoneyClaimNav)
     //   .check(regex("/oauth2/callback&amp;state=(.*)&amp;nonce=").saveAs("state"))
     //   .check(regex("&nonce=(.*)&response_type").saveAs("nonce"))
        .check(CsrfCheck.save)
        .check(substring("Sign in or create an account"))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)


    /*======================================================================================
                 * Civil Citizen - Log In
      ==========================================================================================*/
    .group("Civil_Citizen_030_Login") {
      exec(http("Civil_Citizen_030_005_Login")
      .post(IdamUrl + "/login?client_id=sptribs-frontend&response_type=code&redirect_uri=" + CitizenURL + "/receiver")
    //    .post("https://idam-web-public.perftest.platform.hmcts.net/login?client_id=sptribs-frontend&response_type=code&redirect_uri=https://sptribs-frontend.perftest.platform.hmcts.net/receiver")
        .headers(CivilDamagesHeader.CivilCitizenPost)
        .formParam("username", "familyprivatelaw23@gmail.com")
      .formParam("password", "Password07")
      .formParam("save", "Sign in")
      .formParam("selfRegistrationEnabled", "true")
      .formParam("_csrf", "#{csrf}")
        .check(CsrfCheck.save)
      .check(substring("Who is the subject of this case?")))
    }
    .pause(MinThinkTime, MaxThinkTime)

      /*======================================================================================
                 * Civil Citizen - Who is the subject of this case?
      ==========================================================================================*/
      .group("Civil_Citizen_040_SubjectOfCase?") {
        exec(http("Civil_Citizen_040_005_SubjectOfCase")
          .post(CitizenURL + "/subject-details")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
          .formParam("_csrf", "#{csrf}")
          .formParam("subjectFullName", "#{CitizenRandomString}subjectFullName")
          .formParam("subjectDateOfBirth-day", "#{EvidenceDay}")
          .formParam("subjectDateOfBirth-month", "#{EvidenceMonth}")
          .formParam("subjectDateOfBirth-year", "#{EvidenceYear}")
          .formParam("saveAndContinue", "true")
          .check(substring("Enter contact information")))
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
               * Civil Citizen - Enter contact information
    ==========================================================================================*/
      .group("Civil_Citizen_050_EnterInformation") {
        exec(http("Civil_Citizen_050_005_EnterInformation")
          .post(CitizenURL + "/subject-contact-details")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("subjectEmailAddress", "#{CitizenRandomString}@gmail.com")
          .formParam("subjectContactNumber", "07455753710")
          .formParam("subjectAgreeContact", "")
          .formParam("subjectAgreeContact", "Yes")
          .formParam("saveAndContinue", "true")
          .check(substring("Is there a representative named on completed tribunal form?")))
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
             * Civil Citizen - Is there a representative named on completed tribunal form?
  ==========================================================================================*/
      .group("Civil_Citizen_060_RepresentativeNamed") {
        exec(http("Civil_Citizen_060_005_RepresentativeNamed")
          .post(CitizenURL + "/representation")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("representation", "Yes")
          .formParam("saveAndContinue", "true")
          .check(substring("Is the named representative legally qualified?")))
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
           * Civil Citizen - Is the named representative legally qualified?
==========================================================================================*/
      .group("Civil_Citizen_070_RepLegallyQualified?") {
        exec(http("Civil_Citizen_070_005_RepLegallyQualified")
          .post(CitizenURL + "/representation-qualified")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("representationQualified", "Yes")
          .formParam("saveAndContinue", "true")
          .check(substring("Representative's Details")))
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
         * Civil Citizen - Representative's Details
==========================================================================================*/
      .group("Civil_Citizen_080_RepDetails") {
        exec(http("Civil_Citizen_080_005_RepDetails")
          .post(CitizenURL + "/representative-details")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("representativeFullName", "#{CitizenRandomString}representativeFullName")
          .formParam("representativeOrganisationName", "#{CitizenRandomString}representativeOrganisationName")
          .formParam("representativeContactNumber", "07455753710")
          .formParam("representativeEmailAddress", "#{CitizenRandomString}@gmail.com")
          .formParam("saveAndContinue", "true")
          .check(CsrfCheck.save)
          .check(substring("Upload tribunal form")))
      }
      .pause(MinThinkTime, MaxThinkTime)

      /*======================================================================================
         * Civil Citizen - Upload tribunal form
==========================================================================================*/
      .group("Civil_Citizen_090_UploadTribunalForm") {
        exec(http("Civil_Citizen_090_005_UploadTribunalForm")
      .post(CitizenURL + "/upload-appeal-form?_csrf=#{csrf}")
      .headers(CivilDamagesHeader.CitizenSTUpload)
      .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
      .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundarylzXPkPZGjjxL8byV")
      .header("sec-fetch-dest", "document")
      .header("sec-fetch-mode", "navigate")
      .bodyPart(RawFileBodyPart("files[]", "1MB-c.pdf")
        .fileName("1MB-c.pdf")
        .transferEncoding("binary"))
      .asMultipartForm
        //  .formParam("_csrf", "#{csrf}")
  //    .check(jsonPath("$.documents[0].hashToken").saveAs("claimDisclosureHashToken"))
  //    .check(jsonPath("$.documents[0]._links.self.href").saveAs("claimDisclosureDocument_url"))
      .check(substring("1MB-c.pdf")))
}
.pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
       * Civil Citizen - Upload tribunal form
==========================================================================================*/
      .group("Civil_Citizen_100_TribunalForm") {
        exec(http("Civil_Citizen_100_005_TribunalForm")
          .post(CitizenURL + "/upload-appeal-form")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("documentUploadProceed", "true")
          .formParam("saveAndContinue", "true")
          .check(substring("Upload supporting documents")))
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
     * Civil Citizen - Upload supporting documents
==========================================================================================*/
      .group("Civil_Citizen_110_UploadSupportingDocuments") {
        exec(http("Civil_Citizen_110_005_UploadSupportingDocuments")
          .post(BaseURL + "/upload-supporting-documents?_csrf=#{csrf}")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
          .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundary112ZWDKSv3TEnbq3")
          .header("sec-fetch-dest", "document")
          .header("sec-fetch-mode", "navigate")
          .bodyPart(RawFileBodyPart("files", "1MB-c.pdf")
            .fileName("1MB-c.pdf")
            .transferEncoding("binary"))
          .asMultipartForm
          .formParam("_csrf", "#{csrf}")
          .check(substring("1MB-c.pdf")))
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
     * Civil Citizen - Upload supporting documents
==========================================================================================*/
      .group("Civil_Citizen_120_SupportingDocuments") {
        exec(http("Civil_Citizen_120_005_SupportingDocuments")
          .post(CitizenURL + "/upload-supporting-documents")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("documentUploadProceed", "true")
          .formParam("saveAndContinue", "true")
          .check(substring("Add information to a case")))
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
   * Civil Citizen - Add information to a case Upload
==========================================================================================*/
      .group("Civil_Citizen_130_AddInformationUpload") {
        exec(http("Civil_Citizen_130_005_AddInformationUpload")
          .post(BaseURL + "/upload-other-information?_csrf=#{csrf}")
          .headers(CivilDamagesHeader.MoneyClaimPostHeader)
          .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
          .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundary112ZWDKSv3TEnbq3")
          .header("sec-fetch-dest", "document")
          .header("sec-fetch-mode", "navigate")
          .bodyPart(RawFileBodyPart("files", "1MB-c.pdf")
            .fileName("1MB-c.pdf")
            .transferEncoding("binary"))
          .asMultipartForm
          .formParam("_csrf", "#{csrf}")
          .check(substring("1MB-c.pdf")))
      }
      .pause(MinThinkTime, MaxThinkTime)



      /*======================================================================================
   * Civil Citizen - Add information to a case
==========================================================================================*/
      .group("Civil_Citizen_140_AddInformation") {
        exec(http("Civil_Citizen_140_005_AddInformation")
          .post(CitizenURL + "/upload-other-information")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("documentRelevance", "#{CitizenRandomString}documentRelevance")
          .formParam("additionalInformation", "#{CitizenRandomString}additionalInformation")
          .formParam("documentUploadProceed", "true")
          .formParam("saveAndContinue", "true")
          .check(substring("Equality and diversity questions")))
      }
      .pause(MinThinkTime, MaxThinkTime)


      /*======================================================================================
 * Civil Citizen - Check your answers before submitting your tribunal form
==========================================================================================*/
      .group("Civil_Citizen_150_CheckYourAnswers") {
        exec(http("Civil_Citizen_150_005_CheckYourAnswers")
          .post(CitizenURL + "/check-your-answers")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("_csrf", "#{csrf}")
          .formParam("saveAndContinue", "true")
          .check(regex("""<strong>Case Number:<\/font><br>(\d{4} - \d{4} - \d{4} - \d{4})<\/strong>""").saveAs("referenceNumber")))
      }
      .pause(MinThinkTime, MaxThinkTime)

}
