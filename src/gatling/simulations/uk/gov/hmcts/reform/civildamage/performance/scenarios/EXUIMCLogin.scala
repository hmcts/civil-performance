package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{Common, Environment, Headers, LoginHeader}

object EXUIMCLogin {
  
  val manageOrgURL=Environment.manageOrgURL
  val exuiDomain=Environment.exuiDomain
  val manageOrgDomain= Environment.manageOrgDomain
  val baseURL = Environment.baseURL
  val baseDomain=Environment.baseDomain
  val IdAMURL = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime
  val CommonHeader = Environment.commonHeader
  
  
  /*====================================================================================
  *Business process : Access Home Page by hitting the URL and relavant sub requests
  *below requests are Homepage and relavant sub requests for Manage cases
  *=====================================================================================*/
  
  val manageCasesHomePage =
    exec(flushHttpCache)
      .exec(flushCookieJar)
      .group("CivilDamages_010_Homepage") {
    
    exec(http("CivilDamages_010_005_Homepage")
         .get("/")
         .headers(LoginHeader.headers_0)
         .header("x-dynatrace-test", "TSN=01Homepage;LSN=Civil;LTN=Pipeline")
         .check(status.in(200, 304)))//.exitHereIfFailed
    .exec(http("CivilDamages_010_010_HomepageConfigUI")
          .get("/external/configuration-ui")
          .headers(LoginHeader.headers_1)
          .header("x-dynatrace-test", "TSN=Homepage_ConfigUI;LSN=Civil;LTN=Pipeline"))
    .exec(http("CivilDamages_010_015_HomepageConfigJson")
          .get("/assets/config/config.json")
          .headers(LoginHeader.headers_1)
      .header("x-dynatrace-test", "TSN=Homepage_ConfigJson;LSN=Civil;LTN=Pipeline"))
    .exec(http("CivilDamages_010_020_HomepageTCEnabled")
          .get("/api/configuration?configurationKey=termsAndConditionsEnabled").headers(LoginHeader.headers_1))
    .exec(http("CivilDamages_010_025_HomepageIsAuthenticated")
          .get("/auth/isAuthenticated")
          .headers(LoginHeader.headers_1)
          .header("x-dynatrace-test", "TSN=Homepage_IsAuthenticated;LSN=Civil;LTN=Pipeline"))
    .exec(http("CivilDamages_010_030_AuthLogin")
          .get("/auth/login")
          .header("x-dynatrace-test", "TSN=02AuthLogin;LSN=Civil;LTN=Pipeline")
          .headers(LoginHeader.headers_4)
          .check(css("input[name='_csrf']", "value").saveAs("csrfToken"))
     // .check(regex("manage-user%20create-user&state=(.*)&client").saveAs("state")))
    //below nonce will be applicable when perftest is in pace
          .check(regex("/oauth2/callback&state=(.*)&nonce=").saveAs("state"))
          .check(regex("&nonce=(.*)&response_type").saveAs("nonce")))
    
  } .pause(MinThinkTime, MaxThinkTime)
  
  
  /*====================================================================================
  Business process : Access Home Page by hitting the URL and relavant sub requests
  following is for manage org home page which is used for RJ and Manage Org
  =====================================================================================*/
  
  val manageOrgHomePage =
  
    exec(flushHttpCache).exec(flushSessionCookies).exec(flushCookieJar)
      .exec(http("CivilDamages_MO_010_Homepage")
         .get(manageOrgURL + "/")
         .headers(LoginHeader.headers_0)
         .check(status.in(200,304)))//.exitHereIfFailed
    
    .exec(http("CivilDamages_010_010_HomepageConfigUI")
          .get(manageOrgURL + "/external/configuration-ui")
          .headers(LoginHeader.headers_1))
    
    .exec(http("CivilDamages_010_015_HomepageConfigJson")
          .get(manageOrgURL + "/assets/config/config.json")
          .headers(LoginHeader.headers_1))
    
    .exec(http("CivilDamages_010_020_HomepageTCEnabled")
          .get(manageOrgURL + "/api/configuration?configurationKey=termsAndConditionsEnabled")
          .headers(LoginHeader.headers_1))
    
    .exec(http("CivilDamages_010_025_HomepageIsAuthenticated")
          .get(manageOrgURL + "/auth/isAuthenticated")
          .headers(LoginHeader.headers_1))
    
    .exec(http("CivilDamages_010_030_AuthLogin")
          .get(manageOrgURL + "/auth/login")
          .headers(LoginHeader.headers_4)
          .check(css("input[name='_csrf']", "value").saveAs("csrfToken"))
      .check(regex("manage-user%20create-user%20manage-roles&state=(.*)&client").saveAs("state"))
          /*.check(regex("manage-user%20create-user%20manage-roles&state=(.*)&client").saveAs("state"))
          .check(regex("&nonce=(.*)&response_type").saveAs("nonce"))*/
    )
    
    
    .pause( MinThinkTime, MaxThinkTime )
  
  /*====================================================================================
  *Business process : Access Login Page by hitting the Manage Org Login URL and relavant
  * sub requests ,following is for manage org login page which is used for complete the
  * FR RJ and Manage Org Functionalities
  =====================================================================================*/
  
  val manageOrglogin =
    
    group("CivilDamages_020_SignIn") {
      exec(flushHttpCache).exec(http("CivilDamages_020_005_SignIn")
                                .post(IdAMURL + "/login?response_type=code&redirect_uri=https%3A%2F%2F" + manageOrgDomain + "%2Foauth2%2Fcallback&scope=profile%20openid%20roles%20manage-user%20create-user%20manage-roles&state=#{state}&client_id=xuimowebapp")
        .formParam("username", "#{manageorgsuperuser}")
        .formParam("password", "Password12!")
        .formParam("save", "Sign in")
        .formParam("selfRegistrationEnabled", "false")
        .formParam("_csrf", "#{csrfToken}")
        .headers(LoginHeader.headers_login_submit)
        .check(status.in(200, 304, 302)))//.exitHereIfFailed
      
      .exec(getCookieValue(CookieKey("__auth__").withDomain(manageOrgDomain).saveAs("authTokenResp")))
      .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(manageOrgDomain).saveAs("XSRFToken")))
      
      .exec(http("CivilDamages_020_010_Homepage")
            .get(manageOrgURL + "/external/config/ui")
            .headers(LoginHeader.headers_0)
            .check(status.in(200, 304)))
      
      .exec(http("CivilDamages_020_015_Homepage")
            .get("/api/user/details")
            .headers(LoginHeader.headers_manageorglogin)
            .check(status.in(200, 304)))
      
      .exec(http("CivilDamages_020_020_SignInTCEnabled")
            .get("/auth/isAuthenticated")
            .headers(LoginHeader.headers_manageorglogin)
            .check(status.in(200, 304)))
      
      .exec(http("CivilDamages_020_025_SignInTCEnabled")
            .get("/external/configuration?configurationKey=feature.termsAndConditionsEnabled")
            .headers(LoginHeader.headers_manageorglogin)
            .check(status.in(200, 304)))
      
      .exec(http("CivilDamages_020_030_APIOrg")
            .get("/api/organisation")
            .headers(LoginHeader.headers_manageorglogin)
            .check(status.in(200, 304)))
    }
    
    .pause(MinThinkTime , MaxThinkTime)
  
  /*====================================================================================
  *Business process : Access Login Page by hitting the Manage Case Login URL and relavant
  * sub requests ,following is for manage org login page which is used for complete the
  * service related journeys like divorce,fpla,iac,probate etc...
  =====================================================================================*/
  
  //The below request may need amending depending on the authentication type set by XUI team
  //The top row is for OAUTH2 and the bottom row is for OPIDC
  
  val manageCasesloginold =
    group("CivilDamages_020_005_SignIn") {
      exec(flushHttpCache).exec(http("CivilDamages_020_005_SignIn")
        /*.post(IdAMURL + "/login?response_type=code&redirect_uri=" + baseURL + "%2Foauth2%2Fcallback&scope=profile%20openid%20roles%20manage-user%20create-user&state=${state}&client_id=xuiwebapp")*/
        .post(IdAMURL + "/login?client_id=xuiwebapp&redirect_uri=" + baseURL + "/oauth2/callback&state=#{state}&nonce=#{nonce}&response_type=code&scope=profile%20openid%20roles%20manage-user%20create-user&prompt=")
                                .formParam("username", "#{claimantuser}")
                                .formParam("password", "#{password}")
        //below are additional values
                               /* .formParam("azureLoginEnabled", "true")
                                .formParam("mojLoginEnabled", "true")*/
                                .formParam("save", "Sign in")
                                .formParam("selfRegistrationEnabled", "false")
                                .formParam("_csrf", "#{csrfToken}")
                                .headers(LoginHeader.headers_login_submit)
                                .header("x-dynatrace", "FW4;TSN=CivilOnDynatrace;PSL=CitizenLoginOnDynatrace")
                                .check(status.in(200, 304, 302))).exitHereIfFailed
                                //.check(regex("Manage Cases"))).exitHereIfFailed
      
      //following is the other way of getting cookies
      // .check(headerRegex("Set-Cookie","__auth-token=(.*)").saveAs("authToken"))
      
      .exec(http("CivilDamages_020_010_configUI")
            .get("/external/config/ui")
            .headers(LoginHeader.headers_0)
            .check(status.in(200, 304)))
      
      .exec(http("CivilDamages_020_015_Config")
            .get("/assets/config/config.json")
            .headers(LoginHeader.headers_0)
            .check(status.in(200, 304)))
      
      .exec(http("CivilDamages_020_020_SignInTCEnabled")
            .get("/api/configuration?configurationKey=termsAndConditionsEnabled")
            .headers(LoginHeader.headers_38)
            .check(status.in(200, 304)))
      
      .exec(http("CivilDamages_020_025_SignInGetUserId")
            .get("/api/user/details")
            .headers(LoginHeader.headers_0)
            .check(status.in(200, 304)))
      
    /*  .repeat(1, "count") {
        exec(http("CivilDamages_020_030_AcceptT&CAccessJurisdictions#{count}")
             .get("/aggregated/caseworkers/:uid/jurisdictions?access=read")
             .headers(LoginHeader.headers_access_read)
             .check(status.in(200, 304, 302)))

      }
             */
     /* .exec(http("CivilDamages_020_035_GetWorkBasketInputs")
            .get("/data/internal/case-types/FinancialRemedyMVP2/work-basket-inputs")
            .headers(LoginHeader.headers_17)
            .check(status.in(200, 304, 302)))*/
      
      .exec(http("CivilDamages_020_040_HomepageIsAuthenticated")
            .get("/auth/isAuthenticated")
            .headers(LoginHeader.headers_0))
        
      .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(baseDomain).saveAs("XSRFToken")))
      
    }
    .pause(MinThinkTime, MaxThinkTime)
  
  /*====================================================================================
  *Business process : Access Login Page by hitting the Manage Case Login URL and relavant
  * sub requests ,following is for manage org login page which is used for complete the
  * service related journeys like divorce,fpla,iac,probate etc...
  =====================================================================================*/
  
  //The below request may need amending depending on the authentication type set by XUI team
  //The top row is for OAUTH2 and the bottom row is for OPIDC
  
  val manageCaseslogin =
    group("XUI_020_Login") {
      exec(http("XUI_020_005_Login")
        .post(IdAMURL + "/login?client_id=xuiwebapp&redirect_uri=" + baseURL + "/oauth2/callback&state=#{state}&nonce=#{nonce}&response_type=code&scope=profile%20openid%20roles%20manage-user%20create-user%20search-user&prompt=")
        .formParam("username", "#{claimantuser}")
        .formParam("password", "#{password}")
        .formParam("azureLoginEnabled", "true")
        .formParam("mojLoginEnabled", "true")
        .formParam("selfRegistrationEnabled", "false")
        .formParam("_csrf", "#{csrf}")
        .headers(Headers.navigationHeader)
        .headers(Headers.postHeader)
        .check(regex("Manage cases")))
      
        //see xui-webapp cookie capture in the Homepage scenario for details of why this is being used
        .exec(addCookie(Cookie("xui-webapp", "#{xuiWebAppCookie}")
          .withMaxAge(28800)
          .withSecure(true)))
      
        .exec(Common.configurationui)
      
        .exec(Common.configJson)
      
        .exec(Common.TsAndCs)
      
        .exec(Common.configUI)
      
        .exec(Common.userDetails)
      
        .exec(Common.isAuthenticated)
      
        .exec(Common.monitoringTools)
      
        //if there is no in-flight case, set the case to 0 for the activity calls
        .doIf("#{caseId.isUndefined()}") {
          exec(_.set("caseId", "0"))
        }
      
        // .exec(Common.caseActivityGet)
      
        .exec(http("XUI_020_010_Jurisdictions")
          .get("/aggregated/caseworkers/:uid/jurisdictions?access=read")
          .headers(Headers.commonHeader)
          .header("accept", "application/json")
          .check(substring("id")))
      
        .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(baseURL.replace("https://", "")).saveAs("XSRFToken")))
      
        .exec(Common.orgDetails)
      
        .exec(http("XUI_020_015_WorkBasketInputs")
          .get("/data/internal/case-types/#{caseType}/work-basket-inputs")
          .headers(Headers.commonHeader)
          .header("accept", "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-workbasket-input-details.v2+json;charset=UTF-8")
          .check(regex("workbasketInputs|Not Found"))
          .check(status.in(200, 404)))
      
        .exec(http("XUI_020_020_SearchCases")
          .post("/data/internal/searchCases?ctid=#{caseType}&use_case=WORKBASKET&view=WORKBASKET&page=1")
          .headers(Headers.commonHeader)
          .header("accept", "application/json")
          .formParam("x-xsrf-token", "#{XSRFToken}")
          .body(StringBody("""{"size":25}"""))
          .check(substring("columns")))
    
    }
      .pause(MinThinkTime, MaxThinkTime)
  
  val manageCasesloginToDefendantJourney =
    group("CivilDamages_020_005_SignInDef") {
      exec(flushHttpCache).exec(http("CivilDamages_020_005_SignInDef")
        /*.post(IdAMURL + "/login?response_type=code&redirect_uri=" + baseURL + "%2Foauth2%2Fcallback&scope=profile%20openid%20roles%20manage-user%20create-user&state=${state}&client_id=xuiwebapp")*/
        .post(IdAMURL + "/login?client_id=xuiwebapp&redirect_uri=" + baseURL + "/oauth2/callback&state=#{state}&nonce=#{nonce}&response_type=code&scope=profile%20openid%20roles%20manage-user%20create-user&prompt=")
        .formParam("username", "#{defendantuser}")
        .formParam("password", "#{password}")
        .formParam("save", "Sign in")
        .formParam("selfRegistrationEnabled", "false")
        .formParam("_csrf", "#{csrfToken}")
        .headers(LoginHeader.headers_login_submit)
        .check(status.in(200, 304, 302))).exitHereIfFailed
        //.check(regex("Manage Cases"))).exitHereIfFailed

        //following is the other way of getting cookies
        // .check(headerRegex("Set-Cookie","__auth-token=(.*)").saveAs("authToken"))

        .exec(http("CivilDamages_020_010_configUI")
          .get("/external/config/ui")
          .headers(LoginHeader.headers_0)
          .check(status.in(200, 304)))

        .exec(http("CivilDamages_020_015_Config")
          .get("/assets/config/config.json")
          .headers(LoginHeader.headers_0)
          .check(status.in(200, 304)))

        .exec(http("CivilDamages_020_020_SignInTCEnabled")
          .get("/api/configuration?configurationKey=termsAndConditionsEnabled")
          .headers(LoginHeader.headers_38)
          .check(status.in(200, 304)))

        .exec(http("CivilDamages_020_025_SignInGetUserId")
          .get("/api/user/details")
          .headers(LoginHeader.headers_0)
          .check(status.in(200, 304)))

        .repeat(1, "count") {
          exec(http("CivilDamages_020_030_AcceptT&CAccessJurisdictions#{count}")
            .get("/aggregated/caseworkers/:uid/jurisdictions?access=read")
            .headers(LoginHeader.headers_access_read)
            .check(status.in(200, 304, 302)))
        }
        /* .exec(http("CivilDamages_020_035_GetWorkBasketInputs")
               .get("/data/internal/case-types/FinancialRemedyMVP2/work-basket-inputs")
               .headers(LoginHeader.headers_17)
               .check(status.in(200, 304, 302)))*/

        .exec(http("CivilDamages_020_040_HomepageIsAuthenticated")
          .get("/auth/isAuthenticated")
          .headers(LoginHeader.headers_0))

        .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(baseDomain).saveAs("XSRFToken")))
      
    }
      .pause(MinThinkTime, MaxThinkTime)



  val manageCasesloginToCentreAdminJourney =
    group("CivilDamages_020_005_SignInCentreAdmin") {
      exec(flushHttpCache).exec(http("CivilDamages_020_005_SignIn")
        /*.post(IdAMURL + "/login?response_type=code&redirect_uri=" + baseURL + "%2Foauth2%2Fcallback&scope=profile%20openid%20roles%20manage-user%20create-user&state=${state}&client_id=xuiwebapp")*/
        .post(IdAMURL + "/login?client_id=xuiwebapp&redirect_uri=" + baseURL + "/oauth2/callback&state=#{state}&nonce=#{nonce}&response_type=code&scope=profile%20openid%20roles%20manage-user%20create-user&prompt=")
        .formParam("username", "#{centreadminuser}")
        .formParam("password", "#{password}")
        .formParam("save", "Sign in")
        .formParam("selfRegistrationEnabled", "false")
        .formParam("_csrf", "#{csrfToken}")
        .headers(LoginHeader.headers_login_submit)
        .check(status.in(200, 304, 302))).exitHereIfFailed
        //.check(regex("Manage Cases"))).exitHereIfFailed

        //following is the other way of getting cookies
        // .check(headerRegex("Set-Cookie","__auth-token=(.*)").saveAs("authToken"))

        .exec(http("CivilDamages_020_010_configUI")
          .get("/external/config/ui")
          .headers(LoginHeader.headers_0)
          .check(status.in(200, 304)))

        .exec(http("CivilDamages_020_015_Config")
          .get("/assets/config/config.json")
          .headers(LoginHeader.headers_0)
          .check(status.in(200, 304)))

        .exec(http("CivilDamages_020_020_SignInTCEnabled")
          .get("/api/configuration?configurationKey=termsAndConditionsEnabled")
          .headers(LoginHeader.headers_38)
          .check(status.in(200, 304)))

        .exec(http("CivilDamages_020_025_SignInGetUserId")
          .get("/api/user/details")
          .headers(LoginHeader.headers_0)
          .check(status.in(200, 304)))

        .repeat(1, "count") {
          exec(http("CivilDamages_020_030_AcceptT&CAccessJurisdictions#{count}")
            .get("/aggregated/caseworkers/:uid/jurisdictions?access=read")
            .headers(LoginHeader.headers_access_read)
            .check(status.in(200, 304, 302)))
        }
        /* .exec(http("CivilDamages_020_035_GetWorkBasketInputs")
               .get("/data/internal/case-types/FinancialRemedyMVP2/work-basket-inputs")
               .headers(LoginHeader.headers_17)
               .check(status.in(200, 304, 302)))*/

        .exec(http("CivilDamages_020_040_HomepageIsAuthenticated")
          .get("/auth/isAuthenticated")
          .headers(LoginHeader.headers_0))

        .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(baseDomain).saveAs("XSRFToken")))

    }
      .pause(MinThinkTime, MaxThinkTime)


  
  val manageCasesloginToJudgeJourney =
    group("CivilDamages_020_005_SignInJudge") {
      exec(flushHttpCache).exec(http("CivilDamages_020_005_SignIn")
        /*.post(IdAMURL + "/login?response_type=code&redirect_uri=" + baseURL + "%2Foauth2%2Fcallback&scope=profile%20openid%20roles%20manage-user%20create-user&state=${state}&client_id=xuiwebapp")*/
        .post(IdAMURL + "/login?client_id=xuiwebapp&redirect_uri=" + baseURL + "/oauth2/callback&state=#{state}&nonce=#{nonce}&response_type=code&scope=profile%20openid%20roles%20manage-user%20create-user&prompt=")
        .formParam("username", "#{judgeuser}")
        .formParam("password", "#{judgepassword}")
        .formParam("save", "Sign in")
        .formParam("selfRegistrationEnabled", "false")
        .formParam("_csrf", "#{csrfToken}")
        .headers(LoginHeader.headers_login_submit)
        .check(status.in(200, 304, 302))).exitHereIfFailed
        //.check(regex("Manage Cases"))).exitHereIfFailed

        //following is the other way of getting cookies
        // .check(headerRegex("Set-Cookie","__auth-token=(.*)").saveAs("authToken"))

        .exec(http("CivilDamages_020_010_configUI")
          .get("/external/config/ui")
          .headers(LoginHeader.headers_0)
          .check(status.in(200, 304)))

        .exec(http("CivilDamages_020_015_Config")
          .get("/assets/config/config.json")
          .headers(LoginHeader.headers_0)
          .check(status.in(200, 304)))

        .exec(http("CivilDamages_020_020_SignInTCEnabled")
          .get("/api/configuration?configurationKey=termsAndConditionsEnabled")
          .headers(LoginHeader.headers_38)
          .check(status.in(200, 304)))

        .exec(http("CivilDamages_020_025_SignInGetUserId")
          .get("/api/user/details")
          .headers(LoginHeader.headers_0)
          .check(status.in(200, 304)))

        .repeat(1, "count") {
          exec(http("CivilDamages_020_030_AcceptT&CAccessJurisdictions#{count}")
            .get("/aggregated/caseworkers/:uid/jurisdictions?access=read")
            .headers(LoginHeader.headers_access_read)
            .check(status.in(200, 304, 302)))
        }
        /* .exec(http("CivilDamages_020_035_GetWorkBasketInputs")
               .get("/data/internal/case-types/FinancialRemedyMVP2/work-basket-inputs")
               .headers(LoginHeader.headers_17)
               .check(status.in(200, 304, 302)))*/

        .exec(http("CivilDamages_020_040_HomepageIsAuthenticated")
          .get("/auth/isAuthenticated")
          .headers(LoginHeader.headers_0))

        .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(baseDomain).saveAs("XSRFToken")))

    }
      .pause(MinThinkTime, MaxThinkTime)

  
  val manageCase_Logout =group("ManageCase_Def_SignOut") {
    exec(http("Claimant_SignOut")
      .get("/api/logout")
      .headers(LoginHeader.headers_signout)
      .check(status.in(200, 304, 302)))
  }
    .pause(20)
  
  val manageOrg_Logout =
    group("ManageOrg_SignOut") {
      exec(http("ManageOrg_SignOut")
        .get(manageOrgURL + "/api/logout")
        .headers(LoginHeader.headers_signout)
        .check(status.in(200, 304, 302))
      )
    }
      .pause(20)
  
  
}