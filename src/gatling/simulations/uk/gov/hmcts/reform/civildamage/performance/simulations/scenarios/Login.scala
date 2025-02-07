package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils._

object Login {
  val BaseURL = Environment.baseURL
  val IdamURL = Environment.idamURL

  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val Loginpage =
    group("XUI_020_Login") {
      exec(flushHttpCache)
      .doSwitch("#{loginFlag}") (
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

        "defendant" -> exec(http("005_clientId")
          .post(IdamURL + "/login?client_id=xuiwebapp&redirect_uri=" + BaseURL + "/oauth2/callback&state=#{state}&nonce=#{nonce}&" +
            "response_type=code&scope=profile%20openid%20roles%20manage-user%20create-user%20search-user&prompt=")
          .headers(Headers.navigationHeader)
          .formParam("username", "#{defendantuser}")
          .formParam("password", "#{password}")
          .formParam("selfRegistrationEnabled", "false")
          .formParam("azureLoginEnabled", "true")
          .formParam("mojLoginEnabled", "true")
          .formParam("_csrf", "#{csrf}")
          .check(substring("HMCTS Manage cases"))),

        "judge" -> exec(http("005_clientId")
          .post(IdamURL + "/login?client_id=xuiwebapp&redirect_uri=" + BaseURL + "/oauth2/callback&state=#{state}&nonce=#{nonce}&" +
            "response_type=code&scope=profile%20openid%20roles%20manage-user%20create-user%20search-user&prompt=")
          .headers(Headers.navigationHeader)
          .formParam("username", "#{judgeuser}")
          .formParam("password", "#{judgepassword}")
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

      .exec(Common.configurationui)
      .exec(Common.configUI)
      .exec(Common.TsAndCs)
      .exec(Common.isAuthenticated)
      .exec(Common.userDetails)
      .exec(Common.monitoringTools)
      .exec(Common.isAuthenticated)

      .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("xsrf_token")))
      .exec(addCookie(Cookie("xui-webapp", "#{xuiWebAppCookie}").withMaxAge(28800).withSecure(true)))
      .exec(_.setAll(
        "Idempotencynumber" -> Common.getIdempotency(),
        "LRrandomString" -> Common.randomString(5))
      )
    }
    .pause(MinThinkTime, MaxThinkTime)
}