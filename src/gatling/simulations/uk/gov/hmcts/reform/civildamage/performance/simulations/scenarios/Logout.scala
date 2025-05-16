package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios.utils.Headers



object Logout{
  val Signout =
    // ========================SIGN OUT==================,
    group("XUI_030_SignOut") {
      exec(http("005_SignOut")
        .get("/auth/logout")
        .headers(Headers.navigationHeader)
        .check(substring("Sign in")))
    }
}