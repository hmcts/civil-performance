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
      .exec(http("005_clientId")
        .post(IdamURL + "/login?client_id=xuiwebapp&redirect_uri=" + BaseURL + "/oauth2/callback&state=#{state}&nonce=#{nonce}&" +
          "response_type=code&scope=profile%20openid%20roles%20manage-user%20create-user%20search-user&prompt=")
        .headers(Headers.navigationHeader)
        .formParam("username", "#{LoginId}")
        .formParam("password", "#{Passwordx}")
        .formParam("selfRegistrationEnabled", "false")
        .formParam("azureLoginEnabled", "true")
        .formParam("mojLoginEnabled", "true")
        .formParam("_csrf", "#{csrf}")
        .check(substring("HMCTS Manage cases")))
        //.check(status.is(302)).check(headerRegex("Location", "callback\\?code=([^&]+)").saveAs("codex")))


      .exec(getCookieValue(CookieKey("__auth__").withDomain(BaseURL.replace("https://", "")).saveAs("auth_token")))

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