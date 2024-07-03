package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.CUIR2ClaimCreation.IdamUrl
import uk.gov.hmcts.reform.civildamage.performance.scenarios.CUIR2HomePage.{MaxThinkTime, MinThinkTime}
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, Common, Environment, Headers}

object CUIR2Login {
  
  
  val CitizenURL = Environment.citizenURL
  val IdamUrl = Environment.idamURL
  
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime
  
  /*====================================================================================
  *CUI R2 Login
  *=====================================================================================*/
  
  val CUIR2Login =
    
    group("CUIR2_020_Login") {
      exec(flushHttpCache)
        .exec(http("CUIR2_020_005_Login")
          .post(IdamUrl + "/login?client_id=civil_citizen_ui&response_type=code&redirect_uri=" + CitizenURL + "/oauth2/callback&profile openid roles manage-user create-user search-user")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("username", "cuiclaimantuser@gmail.com")
          .formParam("password", "Password12!")
          .formParam("save", "Sign in")
          .formParam("selfRegistrationEnabled", "true")
          .formParam("_csrf", "#{csrf}")
          .check(substring("Your money claims account"))
        )
    }
      .pause(MinThinkTime, MaxThinkTime)
  
  
}