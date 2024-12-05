package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.{CivilDamagesHeader, Common, CsrfCheck, Environment, Headers}

object CUIR2HomePage {
  
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime
  val BaseURL = Environment.baseURL
  val CitizenURL = Environment.citizenURL
  
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
  
}