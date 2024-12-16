package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.{CivilDamagesHeader, Common, Headers, Environment}

object CUIR2Login {

  val CitizenURL = Environment.citizenURL
  val IdamUrl = Environment.idamURL
  val manageCaseURL = Environment.manageCaseURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime
  
  /*====================================================================================
      *CUI R2 Login
  *===================================================================================*/
  
  val CUIR2Login =
    
    group("CUIR2_Claimant_020_Login") {
      exec(flushHttpCache)
        .exec(http("CUIR2_Claimant_020_005_Login")
          .post(IdamUrl + "/login?client_id=civil_citizen_ui&response_type=code&redirect_uri=" + CitizenURL + "/oauth2/callback&profile openid roles manage-user create-user search-user")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("username", "#{claimantEmailAddress}")
          .formParam("password", "Password12!")
          .formParam("save", "Sign in")
          .formParam("selfRegistrationEnabled", "true")
          .formParam("_csrf", "#{csrf}")
          .check(substring("Your money claims account")))
    }

    .pause(MinThinkTime, MaxThinkTime)

  /*======================================================================================
                     * Civil UI Claim - Sign In
  ======================================================================================*/

  val CUIR2DefLogin =

    group("CUIR2_Def_020_Login") {
      exec(http("CUIR2_Def_020_Login")
        .post(IdamUrl + "/login?client_id=civil_citizen_ui&response_type=code&redirect_uri=" + CitizenURL + "/oauth2/callback&profile openid roles manage-user create-user search-user")
        .headers(CivilDamagesHeader.MoneyClaimSignInHeader)
        .formParam("username", "#{defEmailAddress}")
        .formParam("password", "Password12!")
        .formParam("selfRegistrationEnabled", "true")
        .formParam("_csrf", "#{csrf}")
        .check(status.in(200, 304))
        .check(substring("Claims made against you")))
    }

    .pause(MinThinkTime, MaxThinkTime)

  /*====================================================================================
      *CUI R2 Login
  *===================================================================================*/

  val CUIR2ClaimantIntentionLogin =
    
    group("CUIR2_ClaimantIntention_020_Login") {
      exec(flushHttpCache)
        .exec(http("CUIR2_ClaimantIntention_020_005_Login")
          .post(IdamUrl + "/login?client_id=civil_citizen_ui&response_type=code&redirect_uri=" + CitizenURL + "/oauth2/callback&profile openid roles manage-user create-user search-user")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("username", "#{claimantEmail}")
          .formParam("password", "#{password}")
          .formParam("save", "Sign in")
          .formParam("selfRegistrationEnabled", "true")
          .formParam("_csrf", "#{csrf}")
          .check(substring("Your money claims account")))
    }

    .pause(MinThinkTime, MaxThinkTime)

  val XUIJudicialLogin =

    group("XUI_020_Login") {
      exec(http("XUI_020_005_Login")
        .post(IdamUrl + "/login?client_id=xuiwebapp&redirect_uri=" + manageCaseURL + "/oauth2/callback&state=#{state}&nonce=#{nonce}&response_type=code&scope=profile%20openid%20roles%20manage-user%20create-user%20search-user&prompt=")
        .formParam("username", "EMP261004@ejudiciary.net")
        .formParam("password", "Testing123")
        .formParam("azureLoginEnabled", "true")
        .formParam("mojLoginEnabled", "true")
        .formParam("selfRegistrationEnabled", "false")
        .formParam("_csrf", "#{csrf}")
        .headers(Headers.navigationHeader)
        .headers(Headers.postHeader)
        .check(regex("Manage cases")))

      //see xui-webapp cookie capture in the Homepage scenario for details of why this is being used
      .exec(addCookie(Cookie("xui-webapp", "#{xuiWebAppCookie}")
        .withMaxAge(28800)
        .withSecure(true)))

      .exec(Common.configurationui)
      .exec(Common.configJson)
      .exec(Common.TsAndCs)
      .exec(Common.configUI)
      .exec(Common.userDetails)
      .exec(Common.isAuthenticated)
      .exec(Common.monitoringTools)

      .exec(http("XUI_020_010_Jurisdictions")
        .get(manageCaseURL + "/aggregated/caseworkers/:uid/jurisdictions?access=read")
        .headers(Headers.commonHeader)
        .header("accept", "application/json")
        .check(substring("id"))
        .check(status.in(200,204,302,304,401)))

      .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(manageCaseURL.replace("https://", "")).saveAs("XSRFToken")))

      .exec(Common.orgDetails)

      .exec(http("XUI_020_015_WorkBasketInputs")
        .get(manageCaseURL + "/data/internal/case-types/GENERALAPPLICATION/work-basket-inputs")
        .headers(Headers.commonHeader)
        .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-workbasket-input-details.v2+json;charset=UTF-8")
        .check(regex("workbasketInputs|Not Found"))
        .check(status.in(200, 404)))

      .exec(http("XUI_020_020_SearchCases")
        .post(manageCaseURL + "/data/internal/searchCases?ctid=GENERALAPPLICATION&use_case=WORKBASKET&view=WORKBASKET&page=1")
        .headers(Headers.commonHeader)
        .header("accept", "application/json")
        .formParam("x-xsrf-token", "#{XSRFToken}")
        .body(StringBody("""{"size":25}"""))
        .check(substring("columns")))
    }

    .pause(MinThinkTime , MaxThinkTime)

}