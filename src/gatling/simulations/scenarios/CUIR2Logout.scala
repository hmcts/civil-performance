package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.{CivilDamagesHeader, Environment, Headers}

object CUIR2Logout {

  val CitizenURL = Environment.citizenURL
  val manageCaseURL = Environment.manageCaseURL

  val CUILogout =
    
    group("CUI_999_Logout") {
      exec(http("Logout")
        .get(CitizenURL + "/logout")
        .headers(CivilDamagesHeader.CUIR2Get)
        .check(substring("Sign in or create an account")))
    }

  val XUILogout =

    group("XUI_999_Logout") {
      exec(http("XUI_999_005_Logout")
        .get(manageCaseURL + "/auth/logout")
        .headers(Headers.navigationHeader))
    }
}