package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.Headers._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils._

object XUILogin{

  val BaseURL = Environment.baseURL
  val IdamURL = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime


  val Homepage =
    exec(flushHttpCache)
    .exec(flushCookieJar)
    .group("XUI_010_Home") {
      exec(http("005_Home")
        .get(BaseURL + "/")
        .headers(navigationHeader)
        .header("sec-fetch-site", "none")
        .check(substring("HMCTS Manage cases")))
      .exec(Common.configurationuiXUI)
      .exec(Common.configJsonXUI)
      .exec(Common.TsAndCsXUI)
      .exec(Common.configUIXUI)
      .exec(Common.userDetailsXUI)
      .exec(Common.isAuthenticatedXUI)

      .exec(http("010_auth")
        .get(BaseURL + "/auth/login")
        .headers(navigationHeader)
        .check(substring("Sign in"))
        .check(currentLocationRegex("state=(.*?)&").saveAs("state"))
        .check(currentLocationRegex("nonce=(.*?)&").saveAs("nonce"))
        .check(regex("_csrf\" value=\"(.*)\"").saveAs("csrf")))

      .exec(getCookieValue(CookieKey("xui-webapp").withDomain(BaseURL.replace("https://", "")).saveAs("xuiWebAppCookie")))
    }
    .pause(MinThinkTime, MaxThinkTime)

  val Loginpage =
    group("XUI_020_Login") {
      doSwitch("#{loginFlag}") (
        "claimant" -> exec(http("005_clientId")
          .post(IdamURL + "/login?client_id=xuiwebapp&redirect_uri=" + BaseURL + "/oauth2/callback&state=#{state}&nonce=#{nonce}&" +
            "response_type=code&scope=profile%20openid%20roles%20manage-user%20create-user%20search-user&prompt=")
          .headers(Headers.navigationHeader)
          .formParam("username", "#{claimantuser}")
          .formParam("password", "#{password}")
          .formParam("selfRegistrationEnabled", "false")
          .formParam("azureLoginEnabled", "true")
          .formParam("mojLoginEnabled", "true")
          .formParam("_csrf", "#{csrf}")
          .check(substring("HMCTS Manage cases"))),

        "admin" -> exec(http("005_clientId")
          .post(IdamURL + "/login?client_id=xuiwebapp&redirect_uri=" + BaseURL + "/oauth2/callback&state=#{state}&nonce=#{nonce}&" +
            "response_type=code&scope=profile%20openid%20roles%20manage-user%20create-user%20search-user&prompt=")
          .headers(Headers.navigationHeader)
          .formParam("username", "#{centreadminuser}")
          .formParam("password", "#{password}")
          .formParam("selfRegistrationEnabled", "false")
          .formParam("azureLoginEnabled", "true")
          .formParam("mojLoginEnabled", "true")
          .formParam("_csrf", "#{csrf}")
          .check(substring("HMCTS Manage cases")))
      )

      .exec(getCookieValue(CookieKey("__auth__").withDomain(BaseURL.replace("https://", "")).saveAs("auth_token")))
      .exitHereIf(session => !session.contains("auth_token"))

      .exec(Common.configurationuiXUI)
      .exec(Common.configUIXUI)
      .exec(Common.TsAndCsXUI)
      .exec(Common.isAuthenticatedXUI)
      .exec(Common.userDetailsXUI)
      .exec(Common.monitoringToolsXUI)
      .exec(Common.isAuthenticatedXUI)

      .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).withSecure(true).saveAs("xsrf_token")))
      .exec(addCookie(Cookie("xui-webapp", "#{xuiWebAppCookie}").withMaxAge(28800).withSecure(true).withDomain(BaseURL.replace("https://", ""))))
      .exec(_.set("Idempotencynumber", Common.getIdempotency()))
    }
    .pause(MinThinkTime, MaxThinkTime)

  val Logout =
    exec(http("XUI_1000_Logout")
      .get(BaseURL + "/auth/logout")
      .headers(navigationHeader)
      .check(substring("Sign in")))
    .pause(MinThinkTime, MaxThinkTime)
}