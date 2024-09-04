package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, Environment}

object CUIR2Login {
  
  
  val CitizenURL = Environment.citizenURL
  val IdamUrl = Environment.idamURL
  
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime
  
  /*====================================================================================
  *CUI R2 Login
  *=====================================================================================*/
  
  val CUIR2Login =
    
    group("CUIR2_Claimant_020_Login") {
      exec(flushHttpCache)
        .exec(http("CUIR2_Claimant_020_005_Login")
          .post(IdamUrl + "/login?client_id=civil_citizen_ui&response_type=code&redirect_uri=" + CitizenURL + "/oauth2/callback&profile openid roles manage-user create-user search-user")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("username", "#{claimantEmailAddress}")
          .formParam("password", "#{password}")
          .formParam("save", "Sign in")
          .formParam("selfRegistrationEnabled", "true")
          .formParam("_csrf", "#{csrf}")
          .check(substring("Your money claims account"))
        )
    }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  /*======================================================================================
                     * Civil UI Claim - Sign In
  ==============================================================================================*/
  val CUIR2DefLogin =
  group("CUIR2_Def_020_Login") {
    exec(flushHttpCache)
    .exec(http("CUIR2_Def_020_Login")
      .post(IdamUrl + "/login?client_id=civil_citizen_ui&response_type=code&redirect_uri=" + CitizenURL + "/oauth2/callback&scope=profile%20openid%20roles%20manage-user%20create-user%20search-user")
      .headers(CivilDamagesHeader.MoneyClaimSignInHeader)
      .formParam("username", "#{defEmail}")
      .formParam("password", "Password12!")
      .formParam("selfRegistrationEnabled", "true")
      .formParam("_csrf", "#{csrf}")
      .check(status.in(200, 304))
      .check(substring("Claims made against you"))
    )
  }
    .pause(MinThinkTime, MaxThinkTime)
  
  
  /*====================================================================================
  *CUI R2 Login
  *=====================================================================================*/
  
  val CUIR2ClaimantIntentionLogin =
    
    group("CUIR2_ClaimantIntention_020_Login") {
      exec(flushHttpCache)
        .exec(http("CUIR2_ClaimantIntention_020_005_Login")
          .post(IdamUrl + "/login?client_id=civil_citizen_ui&response_type=code&redirect_uri=" + CitizenURL + "/oauth2/callback&profile openid roles manage-user create-user search-user")
          .headers(CivilDamagesHeader.CivilCitizenPost)
          .formParam("username", "#{claimantEmail}")
          .formParam("password", "#{password}")
          .formParam("save", "Sign in")
          .formParam("selfRegistrationEnabled", "true")
          .formParam("_csrf", "#{csrf}")
          .check(substring("Your money claims account"))
        )
    }
      .pause(MinThinkTime, MaxThinkTime)
  
  
}