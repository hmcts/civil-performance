package uk.gov.hmcts.reform.civildamage.performance.scenarios.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.util.Random
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.check.CheckBuilder
import io.gatling.core.check.jsonpath.JsonPathCheckType
import com.fasterxml.jackson.databind.JsonNode


object Common {

  val rnd = new Random()
  val now = LocalDate.now()
  val patternDay = DateTimeFormatter.ofPattern("dd")
  val patternMonth = DateTimeFormatter.ofPattern("MM")
  val patternYear = DateTimeFormatter.ofPattern("yyyy")

  def randomString(length: Int) = {
    rnd.alphanumeric.filter(_.isLetter).take(length).mkString
  }
  
  def randomRepeatInteger() : Integer=
  {
    val list=List(300, 280, 260, 250, 240, 200,220,350)
    //val list=List(2,3)
    val start = 200
    val end   = 300
    val rnd = new scala.util.Random
    list(rnd.nextInt(list.length))
  }
  
  import scala.util.Random
  object MyClass {
    def main(args: Array[String]) {
      val list = List(12, 65, 89, 41, 99, 102)
      val random = new Random
      println("Random value of the list " + list(random.nextInt(list.length)))
    }
  }
  
  def getRequestId (): String = {
    val chars = ('a' to 'z') ++ ('A' to 'Z') ++ ('0' to '9')
    Random.shuffle(chars).take(5).mkString
  }

  def getDay(): String = {
    (1 + rnd.nextInt(28)).toString.format(patternDay).reverse.padTo(2, '0').reverse //pads single-digit dates with a leading zero
  }

  def getMonth(): String = {
    (1 + rnd.nextInt(12)).toString.format(patternMonth).reverse.padTo(2, '0').reverse //pads single-digit dates with a leading zero
  }
  /*
  def getDay(): String = {
    (10 + rnd.nextInt(28)).toString.format(patternDay).reverse.padTo(2, '0').reverse //pads single-digit dates with a leading zero
  }

  def getMonth(): String = {
    (11 + rnd.nextInt(12)).toString.format(patternMonth).reverse.padTo(2, '0').reverse //pads single-digit dates with a leading zero
  }
  */
  //Dod <= 21 years
  def getYear(): String = {
    now.minusYears(30 + rnd.nextInt(50)).format(patternYear)
  }


  def getYearFuture(): String = {
    now.plusYears(1).format(patternYear)
  }


  def getCurrentYear(): String = {
    now.format(patternYear)
  }

  def getCurrentMonth(): String = {
    now.format(patternMonth)
  }

  def getCurrentDay(): String = {
    now.format(patternDay)
  }

  def getNextMonth(): String = {
    now.plusMonths(1).format(patternYear)
  }


  def getNextNextMonth(): String = {
    now.plusMonths(2).format(patternYear)
  }

  def getPlus2WeeksDay(): String = {
    now.plusWeeks(2).format(patternDay)
  }

  def getPlus2WeeksMonth(): String = {
    now.plusWeeks(2).format(patternMonth)
  }

  def getPlus2WeeksYear(): String = {
    now.plusWeeks(2).format(patternYear)
  }

  def getPlus4WeeksDay(): String = {
    now.plusWeeks(4).format(patternDay)
  }

  def getPlus4WeeksMonth(): String = {
    now.plusWeeks(4).format(patternMonth)
  }

  def getPlus4WeeksYear(): String = {
    now.plusWeeks(4).format(patternYear)
  }


  def getPlus6WeeksDay(): String = {
    now.plusWeeks(6).format(patternDay)
  }

  def getPlus6WeeksMonth(): String = {
    now.plusWeeks(6).format(patternMonth)
  }

  def getPlus6WeeksYear(): String = {
    now.plusWeeks(6).format(patternYear)
  }


  def getPlus8WeeksDay(): String = {
    now.plusWeeks(8).format(patternDay)
  }

  def getPlus8WeeksMonth(): String = {
    now.plusWeeks(8).format(patternMonth)
  }

  def getPlus8WeeksYear(): String = {
    now.plusWeeks(8).format(patternYear)
  }


  def getPlus10WeeksDay(): String = {
    now.plusWeeks(10).format(patternDay)
  }

  def getPlus10WeeksMonth(): String = {
    now.plusWeeks(10).format(patternMonth)
  }

  def getPlus10WeeksYear(): String = {
    now.plusWeeks(10).format(patternYear)
  }

  def getPlus12WeeksDay(): String = {
    now.plusWeeks(12).format(patternDay)
  }

  def getPlus12WeeksMonth(): String = {
    now.plusWeeks(12).format(patternMonth)
  }

  def getPlus12WeeksYear(): String = {
    now.plusWeeks(12).format(patternYear)
  }
  
  def getIdempotency (): String = {
    Random.nextDouble().toString.replace(".", "-")
  }
  
  
  
  
  //Date of Separation >= 6 years < 30 years
  def getSeparationYear (): String = {
    now.minusYears(6 + rnd.nextInt(23)).format(patternYear)
  }
  
  //Reference Date = 5 years and 6 months before the current date in the format 8 May 2016

  //Date of Birth >= 35 years
  def getDobYear(): String = {
    now.minusYears(25 + rnd.nextInt(70)).format(patternYear)
  }

  
  //Date of Birth <= 18 years
  def getDobYearChild (): String = {
    now.minusYears(2 + rnd.nextInt(15)).format(patternYear)
  }
  
  //Date of Death <= 21 years
  def getDodYear (): String = {
    now.minusYears(1 + rnd.nextInt(20)).format(patternYear)
  }
  
  //Saves partyId
  def savePartyId: CheckBuilder[JsonPathCheckType, JsonNode] = jsonPath("$.case_fields[*].value[*].value.party.partyId").saveAs("partyId")
  
  //Saves user ID
  def saveId: CheckBuilder[JsonPathCheckType, JsonNode] = jsonPath("$.case_fields[*].value[0].id").saveAs("id")
  
  /*======================================================================================
  * Common XUI Calls
  ======================================================================================*/
  
  val postcodeFeeder = csv("postcodes.csv").random
  
  val postcodeLookup =
    feed(postcodeFeeder)
      .exec(http("XUI_Common_000_PostcodeLookup")
        .get("/api/addresses?postcode=${postcode}")
        .headers(Headers.commonHeader)
        .header("accept", "application/json")
        .check(jsonPath("#.header.totalresults").ofType[Int].gt(0))
        .check(regex(""""(?:BUILDING|ORGANISATION)_.+" : "(.+?)",(?s).*?"(?:DEPENDENT_LOCALITY|THOROUGHFARE_NAME)" : "(.+?)",.*?"POST_TOWN" : "(.+?)",.*?"POSTCODE" : "(.+?)"""")
          .ofType[(String, String, String, String)].findRandom.saveAs("addressLines")))
  
  def healthcheck (path: String) =
    exec(http("XUI_Common_000_Healthcheck")
      .get(s"/api/healthCheck?path=${path}")
      .headers(Headers.commonHeader)
      .header("accept", "application/json, text/plain, */*")
      .check(substring("""{"healthState":true}""")))
  
  val activity =
    exec(http("XUI_Common_000_ActivityOptions")
      .options("/activity/cases/${caseId}/activity")
      .headers(Headers.commonHeader)
      .header("accept", "application/json, text/plain, */*")
      .header("sec-fetch-site", "same-site")
      .check(status.in(200, 304, 403)))
  
  val caseActivityGet =
    exec(http("XUI_Common_000_ActivityOptions")
      .options("/activity/cases/${caseId}/activity")
      .headers(Headers.commonHeader)
      .header("accept", "application/json, text/plain, */*")
      .header("sec-fetch-site", "same-site")
      .check(status.in(200, 304, 403)))
      
      .exec(http("XUI_Common_000_ActivityGet")
        .get("/activity/cases/${caseId}/activity")
        .headers(Headers.commonHeader)
        .header("accept", "application/json, text/plain, */*")
        .header("sec-fetch-site", "same-site")
        .check(status.in(200, 304, 403)))
  
  val caseActivityPost =
    exec(http("XUI_Common_000_ActivityOptions")
      .options("/activity/cases/${caseId}/activity")
      .headers(Headers.commonHeader)
      .header("accept", "application/json, text/plain, */*")
      .header("sec-fetch-site", "same-site")
      .check(status.in(200, 304, 403)))
      
      .exec(http("XUI_Common_000_ActivityPost")
        .post("/activity/cases/${caseId}/activity")
        .headers(Headers.commonHeader)
        .header("accept", "application/json, text/plain, */*")
        .header("sec-fetch-site", "same-site")
        .body(StringBody("{\n  \"activity\": \"view\"\n}"))
        .check(status.in(200, 201, 304, 403)))
  
  val configurationui =
    exec(http("XUI_Common_000_ConfigurationUI")
      .get("/external/configuration-ui/")
      .headers(Headers.commonHeader)
      .header("accept", "*/*")
      .check(substring("ccdGatewayUrl")))
  
  val configJson =
    exec(http("XUI_Common_000_ConfigJson")
      .get("/assets/config/config.json")
      .header("accept", "application/json, text/plain, */*")
      .check(substring("caseEditorConfig")))
  
  val TsAndCs =
    exec(http("XUI_Common_000_TsAndCs")
      .get("/api/configuration?configurationKey=termsAndConditionsEnabled")
      .headers(Headers.commonHeader)
      .header("accept", "application/json, text/plain, */*")
      .check(substring("false")))
  
  val userDetails =
    exec(http("XUI_Common_000_UserDetails")
      .get("/api/user/details")
      .headers(Headers.commonHeader)
      .header("accept", "application/json, text/plain, */*"))
  
  val configUI =
    exec(http("XUI_Common_000_ConfigUI")
      .get("/external/config/ui")
      .headers(Headers.commonHeader)
      .header("accept", "application/json, text/plain, */*")
      .check(substring("ccdGatewayUrl")))
  
  val isAuthenticated =
    exec(http("XUI_Common_000_IsAuthenticated")
      .get("/auth/isAuthenticated")
      .headers(Headers.commonHeader)
      .header("accept", "application/json, text/plain, */*")
      .check(regex("true|false")))
  
  val profile =
    exec(http("XUI_Common_000_Profile")
      .get("/data/internal/profile")
      .headers(Headers.commonHeader)
      .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-user-profile.v2+json;charset=UTF-8")
      .check(jsonPath("$.user.idam.id").notNull))
  
  val monitoringTools =
    exec(http("XUI_Common_000_MonitoringTools")
      .get("/api/monitoring-tools")
      .headers(Headers.commonHeader)
      .header("accept", "application/json, text/plain, */*")
      .check(jsonPath("$.key").notNull))
  
  val caseShareOrgs =
    exec(http("XUI_Common_000_CaseShareOrgs")
      .get("/api/caseshare/orgs")
      .headers(Headers.commonHeader)
      .header("accept", "application/json, text/plain, */*")
      .check(jsonPath("$.name").notNull))
  
  val orgDetails =
    exec(http("XUI_Common_000_OrgDetails")
      .get("/api/organisation")
      .headers(Headers.commonHeader)
      .header("accept", "application/json, text/plain, */*")
      .check(regex("name|Organisation route error"))
      .check(status.in(200, 304, 403)))


  val configurationuiXUI =
    exec(http("XUI_Common_000_ConfigurationUI")
      .get(Environment.baseURL + "/external/configuration-ui/")
      .headers(Headers.commonHeader)
      .header("accept", "*/*")
      .check(substring("ccdGatewayUrl")))

  val configJsonXUI =
    exec(http("XUI_Common_000_ConfigJson")
      .get(Environment.baseURL + "/assets/config/config.json")
      .header("accept", "application/json, text/plain, */*")
      .check(substring("caseEditorConfig")))

  val TsAndCsXUI =
    exec(http("XUI_Common_000_TsAndCs")
      .get(Environment.baseURL + "/api/configuration?configurationKey=termsAndConditionsEnabled")
      .headers(Headers.commonHeader)
      .header("accept", "application/json, text/plain, */*")
      .check(substring("false")))

  val userDetailsXUI =
    exec(http("XUI_Common_000_UserDetails")
      .get(Environment.baseURL + "/api/user/details")
      .headers(Headers.commonHeader)
      .header("accept", "application/json, text/plain, */*")
      .check(status.in(200, 401)))

  val configUIXUI =
    exec(http("XUI_Common_000_ConfigUI")
      .get(Environment.baseURL + "/external/config/ui")
      .headers(Headers.commonHeader)
      .header("accept", "application/json, text/plain, */*")
      .check(substring("ccdGatewayUrl")))

  val isAuthenticatedXUI =
    exec(http("XUI_Common_000_IsAuthenticated")
      .get(Environment.baseURL + "/auth/isAuthenticated")
      .headers(Headers.commonHeader)
      .header("accept", "application/json, text/plain, */*")
      .check(regex("true|false")))

  val monitoringToolsXUI =
    exec(http("XUI_Common_000_MonitoringTools")
      .get(Environment.baseURL + "/api/monitoring-tools")
      .headers(Headers.commonHeader)
      .header("accept", "application/json, text/plain, */*")
      .check(jsonPath("$.key").notNull))

}