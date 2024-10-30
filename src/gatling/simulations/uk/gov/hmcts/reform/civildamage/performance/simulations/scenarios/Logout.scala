package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios.utils.a_CreateClaim_Headers.headers_58


object Logout{
  val Signout =
    // ========================SIGNOUT==================,
    exec(http("XUI_020_Signout")
      .get("/auth/logout")
      .headers(headers_58)
      .check(status.is(302)))

//      .exec(session => {
//        session.removeAll()
//      })



}