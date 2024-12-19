package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils._
import utils.Headers._

object Home{

  val BaseURL = Environment.baseURL
  val IdamURL = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime


  val Homepage =
    exec(flushHttpCache)
    .exec(flushCookieJar)//.exec(flushSessionCookies)
    .group("XUI_010_Home") {
      exec(http("005_Home")
        .get("/")
        .headers(navigationHeader)
        .header("sec-fetch-site", "none")
        .check(substring("HMCTS Manage cases")))
      .exec(Common.configurationui)
      .exec(Common.configJson)
      .exec(Common.TsAndCs)
      .exec(Common.configUI)
      .exec(Common.userDetails)
      .exec(Common.isAuthenticated)

      .exec(http("010_auth")
        .get("/auth/login")
        .headers(navigationHeader)
        .check(substring("Sign in"))
        .check(currentLocationRegex("state=(.*?)&").saveAs("state"))
        .check(currentLocationRegex("nonce=(.*?)&").saveAs("nonce"))
        .check(regex("_csrf\" value=\"(.*)\"").saveAs("csrf")))

      .exec(getCookieValue(CookieKey("xui-webapp").withDomain(BaseURL.replace("https://", "")).saveAs("xuiWebAppCookie")))
    }
    .pause(MinThinkTime, MaxThinkTime)
}