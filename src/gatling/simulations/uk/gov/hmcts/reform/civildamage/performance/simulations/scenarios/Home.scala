package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils._
import utils.Headers._

import scala.concurrent.duration.DurationInt

object Home{

  val BaseURL = Environment.baseURL
  val IdamURL = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime


  val Homepage =
    exec(flushHttpCache).exec(flushCookieJar)//.exec(flushSessionCookies)
      .group("XUI_010_Home") {
      exec(http("005_Home")
          .get("/")
          .headers(navigationHeader))
          .pause(1)
          .exec(Common.configurationui)
          .exec(Common.configJson)
          .exec(Common.TsAndCs)
          .exec(Common.configUI)
          .exec(Common.userDetails)
          .exec(Common.isAuthenticated)
          //        .exec(Common.authLogin)

          .exec(http("010_auth")
            .get("/auth/login")
//            .headers(headers_6)
            .headers(navigationHeader)
            .check(status.is(302))
            .check(headerRegex("Location", "state=(.*?)&").saveAs("state"))
            .check(headerRegex("Location", "nonce=([^&]+)").saveAs("nonce"))
          )
          .pause(256.milliseconds)

          .exec(http("015_authorize")
            .get(IdamURL + "/o/authorize?client_id=xuiwebapp&scope=profile%20openid%20roles%20manage-user%20create-user%20search-user&response_type=code&redirect_uri=https%3A%2F%2Fmanage-case.perftest.platform.hmcts.net%2Foauth2%2Fcallback&state=#{state}&prompt=login&nonce=#{nonce}")
//            .headers(headers_7)
            .headers(navigationHeader)
            .check(status.is(302)))

          .exec(http("020_client")
            .get(IdamURL + "/login?client_id=xuiwebapp&redirect_uri=https://manage-case.perftest.platform.hmcts.net/oauth2/callback&state=#{state}&nonce=#{nonce}&response_type=code&scope=profile%20openid%20roles%20manage-user%20create-user%20search-user&prompt=")
//            .headers(headers_7)
            .headers(navigationHeader)
            .check(regex("_csrf\" value=\"(.*)\"").saveAs("csrf")))

          .exec(getCookieValue(CookieKey("xui-webapp").withDomain(BaseURL.replace("https://", "")).saveAs("xuiWebAppCookie")))
        //      .pause(MinThinkTime, MaxThinkTime)
      }
}