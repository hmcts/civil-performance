package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{CivilDamagesHeader, CsrfCheck, Environment}

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
      exec(http("CUIR2_Claimant_020_005_Login")
        .get(IdamUrl + "/enter-email")
        .headers(CivilDamagesHeader.CUILoginGet)
        .check(CsrfCheck.save)
        .check(substring("Enter your email address")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*===============================================================================================
    * Enter Email
    ===============================================================================================*/

    .group("CUIR2_Claimant_023_Login_EnterEmail") {
      exec(http("CUIR2_Claimant_023_005_Login_EnterEmail")
        .post(IdamUrl + "/enter-email")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("email", "#{claimantEmailAddress}")
        .formParam("_csrf", "#{csrf}")
        .check(CsrfCheck.save)
        .check(substring("Enter your password")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*===============================================================================================
    * Enter Password (login)
    ===============================================================================================*/

    .group("CUIR2_Claimant_026_Login_EnterPassword") {
      exec(http("CUIR2_Claimant_026_005_Login_EnterPassword")
        .post(IdamUrl + "/enter-password")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("action", "_submit")
        .formParam("password", "#{password}")
        .formParam("_csrf", "#{csrf}")
        .check(substring("Your money claims account")))
    }
    .pause(MinThinkTime, MaxThinkTime)
  
  
  /*======================================================================================
                     * Civil UI Claim - Sign In
  ==============================================================================================*/
  val CUIR2DefLogin =

    group("CUIR2_Defendant_020_Login") {
      exec(http("CUIR2_Defendant_020_005_Login")
        .get(IdamUrl + "/enter-email")
        .headers(CivilDamagesHeader.CUILoginGet)
        .check(CsrfCheck.save)
        .check(substring("Enter your email address")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*===============================================================================================
    * Enter Email
    ===============================================================================================*/

    .group("CUIR2_Defendant_023_Login_EnterEmail") {
      exec(http("CUIR2_Defendant_023_005_Login_EnterEmail")
        .post(IdamUrl + "/enter-email")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("email", "#{defEmailAddress}")
        .formParam("_csrf", "#{csrf}")
        .check(CsrfCheck.save)
        .check(substring("Enter your password")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*===============================================================================================
    * Enter Password (login)
    ===============================================================================================*/

    .group("CUIR2_Defendant_026_Login_EnterPassword") {
      exec(http("CUIR2_Defendant_026_005_Login_EnterPassword")
        .post(IdamUrl + "/enter-password")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("action", "_submit")
        .formParam("password", "#{password}")
        .formParam("_csrf", "#{csrf}")
        .check(substring("Your money claims account")))
    }
    .pause(MinThinkTime, MaxThinkTime)
  
  
  /*====================================================================================
  *CUI R2 Login
  *=====================================================================================*/
  
  val CUIR2ClaimantIntentionLogin =

    group("CUIR2_Claimantintention_020_Login") {
      exec(http("CUIR2_Claimantintention_020_005_Login")
        .get(IdamUrl + "/enter-email")
        .headers(CivilDamagesHeader.CUILoginGet)
        .check(CsrfCheck.save)
        .check(substring("Enter your email address")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*===============================================================================================
    * Enter Email
    ===============================================================================================*/

    .group("CUIR2_Claimantintention_023_Login_EnterEmail") {
      exec(http("CUIR2_Claimantintention_023_005_Login_EnterEmail")
        .post(IdamUrl + "/enter-email")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("email", "#{claimantEmailAddress}")
        .formParam("_csrf", "#{csrf}")
        .check(CsrfCheck.save)
        .check(substring("Enter your password")))
    }

    .pause(MinThinkTime, MaxThinkTime)

    /*===============================================================================================
    * Enter Password (login)
    ===============================================================================================*/

    .group("CUIR2_Claimantintention_026_Login_EnterPassword") {
      exec(http("CUIR2_Claimantintention_026_005_Login_EnterPassword")
        .post(IdamUrl + "/enter-password")
        .headers(CivilDamagesHeader.CUIR2Post)
        .formParam("action", "_submit")
        .formParam("password", "#{password}")
        .formParam("_csrf", "#{csrf}")
        .check(substring("Your money claims account")))
    }
    .pause(MinThinkTime, MaxThinkTime)
  
  
}