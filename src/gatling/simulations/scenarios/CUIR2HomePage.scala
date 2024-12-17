package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.{CivilDamagesHeader, Common, Headers, CsrfCheck, Environment}

object CUIR2HomePage {
  
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime
  val BaseURL = Environment.baseURL
  val CitizenURL = Environment.citizenURL
  val manageCaseURL = Environment.manageCaseURL

  /*====================================================================================
  *CUI R2  Homepage
  *=====================================================================================*/
  
  val CUIR2HomePage =
    exec(flushHttpCache)
      .exec(flushCookieJar)
      .group("CUIR2_010_Homepage") {
        exec(http("CUIR2_010_005_Homepage")
          .get(CitizenURL + "/")
          .headers(CivilDamagesHeader.MoneyClaimNav)
          .check(CsrfCheck.save)
          .check(substring("Sign in or create an account")))
      }
      .pause(MinThinkTime, MaxThinkTime)

  /*====================================================================================
  *XUI  Homepage
  *=====================================================================================*/

  val XUIHomePage =

    exec(flushHttpCache)
    .exec(flushCookieJar)

    .group("XUI_010_Homepage") {
      exec(http("XUI_010_005_Homepage")
        .get(manageCaseURL + "/")
        .headers(Headers.navigationHeader)
        .header("sec-fetch-site", "none"))

        .exec(Common.configurationui)
        .exec(Common.configJson)
        .exec(Common.TsAndCs)
        .exec(Common.configUI)

        // .exec(Common.userDetails)
        .exec(http("XUI_Common_000_UserDetails")
          .get(manageCaseURL + "/api/user/details?refreshRoleAssignments=undefined")
          .headers(Headers.commonHeader)
          .header("accept", "application/json, text/plain, */*")
          .check(status.in(200, 304, 401)))

        .exec(Common.isAuthenticated)

        .exec(http("XUI_010_010_AuthLogin")
          .get(manageCaseURL + "/auth/login")
          .headers(Headers.navigationHeader)
          .check(CsrfCheck.save)
          .check(regex("/oauth2/callback&amp;state=(.*)&amp;nonce=").saveAs("state"))
          .check(regex("nonce=(.*)&amp;response_type").saveAs("nonce")))
    }

    .pause(MinThinkTime, MaxThinkTime)


}