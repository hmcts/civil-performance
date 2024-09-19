package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, Environment}

object CUIR2Logout {
  val CitizenURL = Environment.citizenURL
  
  val CUILogout =
    
    group("CUI_999_Logout") {
      
      exec(http("Logout")
        .get(CitizenURL + "/logout")
        .headers(CivilDamagesHeader.CUIR2Get)
        .check(substring("Sign in or create an account")))
      
    }
}