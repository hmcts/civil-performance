package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios.utils.Headers



object Logout{
  val Signout =
    // ========================SIGNOUT==================,
    exec(http("XUI_020_Signout")
      .get("/auth/logout")
      .headers(Headers.navigationHeader)
      .check(status.is(302)))

//      .exec(session => {
//        session.removeAll()
//      })



}