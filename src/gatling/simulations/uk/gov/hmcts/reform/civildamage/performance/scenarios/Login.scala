package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{Common, Environment, Headers}

object Login {
  
  val BaseURL = Environment.baseURL
  val IdamUrl = Environment.idamURL
  
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime
  
  /*====================================================================================
  *Manage Case Login
  *=====================================================================================*/
  
  val XUILogin =
    
    group("XUI_020_Login") {
      exec(flushHttpCache)
        .exec(http("XUI_020_005_Login")
          .post(IdamUrl + "/login?client_id=xuiwebapp&redirect_uri=" + BaseURL + "/oauth2/callback&state=#{state}&nonce=#{nonce}&response_type=code&scope=profile%20openid%20roles%20manage-user%20create-user%20search-user&prompt=")
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
        
        //.exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))
        .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).withSecure(true).saveAs("XSRFToken")))
  
        .exec(Common.orgDetails)
      
    
      
    }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  val manageCasesloginToDefendantJourney =
    group("CivilDamages_020_005_SignInDef") {
      exec(flushHttpCache).exec(http("CivilDamages_020_005_SignInDef")
        /*.post(IdAMURL + "/login?response_type=code&redirect_uri=" + baseURL + "%2Foauth2%2Fcallback&scope=profile%20openid%20roles%20manage-user%20create-user&state=${state}&client_id=xuiwebapp")*/
        .post(IdamUrl + "/login?client_id=xuiwebapp&redirect_uri=" + BaseURL + "/oauth2/callback&state=#{state}&nonce=#{nonce}&response_type=code&scope=profile%20openid%20roles%20manage-user%20create-user&prompt=")
        .formParam("username", "#{defendantuser}")
        .formParam("password", "#{password}")
        .formParam("save", "Sign in")
        .formParam("selfRegistrationEnabled", "false")
        .formParam("_csrf", "#{csrf}")
        .headers(Headers.navigationHeader)
        .headers(Headers.postHeader)
        .check(status.in(200, 304, 302))).exitHereIfFailed
       
  
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
  
        //.exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).saveAs("XSRFToken")))
        .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).withSecure(true).saveAs("XSRFToken")))
  
        .exec(Common.orgDetails)
      
    }
      .pause(MinThinkTime, MaxThinkTime)
  
  /*====================================================================================
  *Manage Case Judge Login
  *=====================================================================================*/
  
  val XUIJudgeLogin =
    
    group("XUI_020_Login") {
      exec(flushHttpCache)
        .exec(http("XUI_020_005_Login")
          .post(IdamUrl + "/login?client_id=xuiwebapp&redirect_uri=" + BaseURL + "/oauth2/callback&state=#{state}&nonce=#{nonce}&response_type=code&scope=profile%20openid%20roles%20manage-user%20create-user%20search-user&prompt=")
          .formParam("username", "#{judgeuser}")
          .formParam("password", "#{judgepassword}")
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
  
        .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).withSecure(true).saveAs("XSRFToken")))
        .exec(Common.orgDetails)
      
      
      
    }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  /*====================================================================================
  *Manage Case Judge Login
  *=====================================================================================*/
  
  val XUIJudgeRegion4Login =
    
    group("XUI_020_Login") {
      exec(flushHttpCache)
        .exec(http("XUI_020_005_Login")
          .post(IdamUrl + "/login?client_id=xuiwebapp&redirect_uri=" + BaseURL + "/oauth2/callback&state=#{state}&nonce=#{nonce}&response_type=code&scope=profile%20openid%20roles%20manage-user%20create-user%20search-user&prompt=")
          .formParam("username", "#{judgeregion4user}")
          .formParam("password", "#{judgeregion4password}")
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
  
        .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).withSecure(true).saveAs("XSRFToken")))
        .exec(Common.orgDetails)
      
      
      
    }
      .pause(MinThinkTime, MaxThinkTime)
  
  // Tribunal login is a condition for region 4 for SDO
  val XUITribunalLogin =
    
    group("XUI_020_Login") {
      exec(flushHttpCache)
        .exec(http("XUI_020_005_Login")
          .post(IdamUrl + "/login?client_id=xuiwebapp&redirect_uri=" + BaseURL + "/oauth2/callback&state=#{state}&nonce=#{nonce}&response_type=code&scope=profile%20openid%20roles%20manage-user%20create-user%20search-user&prompt=")
          .formParam("username", "#{tribunaluser}")
          .formParam("password", "#{tribunalpassword}")
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
  
        .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).withSecure(true).saveAs("XSRFToken")))
        
        .exec(Common.orgDetails)
      
     
      
    }
      .pause(MinThinkTime, MaxThinkTime)
  
  
  // Tribunal login is a condition for region 4 for SDO
  val XUICenterAdminLogin =
    
    group("XUI_020_Login") {
      exec(flushHttpCache)
        .exec(http("XUI_020_005_Login")
          .post(IdamUrl + "/login?client_id=xuiwebapp&redirect_uri=" + BaseURL + "/oauth2/callback&state=#{state}&nonce=#{nonce}&response_type=code&scope=profile%20openid%20roles%20manage-user%20create-user%20search-user&prompt=")
          .formParam("username", "#{centreadminuser}")
          .formParam("password", "#{tribunalpassword}")
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
  
        .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).withSecure(true).saveAs("XSRFToken")))
        
        .exec(Common.orgDetails)
      
     
      
    }
      .pause(MinThinkTime, MaxThinkTime)
      
  
  //below is for ctsc login
  
  // Tribunal login is a condition for region 4 for SDO
  val XUICTSCLogin =
    
    group("XUI_020_Login") {
      exec(flushHttpCache)
        .exec(http("XUI_020_005_Login")
          .post(IdamUrl + "/login?client_id=xuiwebapp&redirect_uri=" + BaseURL + "/oauth2/callback&state=#{state}&nonce=#{nonce}&response_type=code&scope=profile%20openid%20roles%20manage-user%20create-user%20search-user&prompt=")
          .formParam("username", "#{ctscadminuser}")
          .formParam("password", "#{ctscadminpassword}")
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
  
        .exec(getCookieValue(CookieKey("XSRF-TOKEN").withDomain(BaseURL.replace("https://", "")).withSecure(true).saveAs("XSRFToken")))
    
        .exec(Common.orgDetails)
    }
    
    }