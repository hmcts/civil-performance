package uk.gov.hmcts.reform.civildamage.performance.simulations
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.scenario.Simulation
import scenarios.utils.Environment._
import scenarios.utils._
import scenarios._
import io.gatling.core.controller.inject.open.OpenInjectionStep


class SimulateScenario extends Simulation {
  val BaseURL = Environment.baseURL
  val IdamURL = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime
  val CaseIdFeeder = csv("caseIds.csv").circular
  val loginFeeder = csv("users.csv").circular
//  val ClaimAmt = 5000
//  val ClaimAmt = 11000
//  val ClaimAmt = 35000

  implicit val postHeaders: Map[String, String] = Map(
    "Origin" -> BaseURL
  )

  //==================Unspecified=======================

  val CreateUnSpecClaimSCN = scenario("UnSpec_CreateClaim")
//============01. Create Claim=========================
    .feed(loginFeeder)
    .exec(_.set("loginFlag", "claimant"))
    .exec(Home.Homepage)
    .exec(Login.Loginpage)
    .exec(unspec_CreateClaim.CreateUnSpecClaim)
    .exec(MakePay.MakePayment)
    .exec(unspec_notification.NotifyClaim)
    .exec(Logout.Signout)
    .pause(MaxThinkTime)

//============02. Respond to Claimant==================
    .exec(_.set("loginFlag", "defendant"))
    .exec(Home.Homepage)
    .exec(Login.Loginpage)
    .exec(unspec_DF1_resp.DF_Resp)
    .exec(Logout.Signout)
    .pause(MaxThinkTime)

//===========03. Claimant Response to DF================
    .exec(_.set("loginFlag", "claimant"))
    .exec(Home.Homepage)
    .exec(Login.Loginpage)
    .exec(unspec_CL1_Resp.RespToDF)
    .exec(Logout.Signout)
    .pause(MaxThinkTime)

//============04. Judge SDO==============================
    .exec(_.set("loginFlag", "judge"))
    .exec(Home.Homepage)
    .exec(Login.Loginpage)
    .exec(unspec_Jud_Hear.sdoJudge)
    .exec(Logout.Signout)
    .pause(MaxThinkTime)

//============05. Hearing Admin===========================
    .exec(_.set("loginFlag", "admin"))
    .exec(Home.Homepage)
    .exec(Login.Loginpage)
    .exec(unspec_Jud_Hear.HearingAdmin)
    .exec(Logout.Signout)
    .pause(MaxThinkTime)

//=============06. Respondent Doc Upload===================
    .exec(_.set("loginFlag", "defendant"))
    .exec(Home.Homepage)
    .exec(Login.Loginpage)
    .exec(unspec_DF2_CL2_FinalOrder_Not_In_Scope.DF_upload)
    .exec(Logout.Signout)
    .pause(MaxThinkTime)

//==============07. Claimant Doc Upload====================
    .exec(_.set("loginFlag", "claimant"))
    .exec(Home.Homepage)
    .exec(Login.Loginpage)
    .exec(unspec_DF2_CL2_FinalOrder_Not_In_Scope.CL_upload)
    .exec(Logout.Signout)
    .pause(MaxThinkTime)

//===============08. Judge Final Order=====================
    .exec(_.set("loginFlag", "judge"))
    .exec(Home.Homepage)
    .exec(Login.Loginpage)
    .exec(unspec_DF2_CL2_FinalOrder_Not_In_Scope.FinalOrder)
    .exec(Logout.Signout)



  //==================Specified=============================

  val CreateSpecClaimSCN = scenario("Spec_Create_Claim")
//==========01. Create Claim==================
    .feed(loginFeeder)
    .exec(_.set("loginFlag", "claimant"))
    .exec(Home.Homepage)
    .exec(Login.Loginpage)
    .exec(spec_CreateClaim.CreateSpecClaim)
    .exec(spec_CreateClaim.ClaimFeePayment)
    .exec(Logout.Signout)
    .pause(MaxThinkTime)

//==========02. Def Response==================
    .exec(_.set("loginFlag", "defendant"))
    .exec(Home.Homepage)
    .exec(Login.Loginpage)
    .exec(spec_DF1_Resp.selectRespondToClaim)
    .exec(Logout.Signout)
    .pause(MaxThinkTime)

//==========03. Claimant Response==================
    .exec(_.set("loginFlag", "claimant"))
    .exec(Home.Homepage)
    .exec(Login.Loginpage)
    .exec(spec_CL1_Resp.RespToDef)
    .exec(Logout.Signout)
    .pause(MaxThinkTime)

//==========04. Judge SDO==================
    .exec(_.set("loginFlag", "judge"))
    .exec(Home.Homepage)
    .exec(Login.Loginpage)
    .exec(spec_SDO_Judge.sdoJudge)
    .exec(Logout.Signout)
    .pause(MaxThinkTime)

//==========05.Hearing Schedule==================
    .exec(_.set("loginFlag", "admin"))
    .exec(Home.Homepage)
    .exec(Login.Loginpage)
    .exec(spec_HearingAdmin.HearingAdmin)
    .exec(Logout.Signout)
    .pause(MaxThinkTime)

//==========06. Respondent Doc Upload==================
    .exec(_.set("loginFlag", "defendant"))
    .exec(Home.Homepage)
    .exec(Login.Loginpage)
    .exec(spec_DF2_CL2_FinalOrder_Not_In_Scope.DF_upload)
    .exec(Logout.Signout)
    .pause(MaxThinkTime)

//==========07. Claimant Doc Upload==================
    .exec(_.set("loginFlag", "claimant"))
    .exec(Home.Homepage)
    .exec(Login.Loginpage)
    .exec(spec_DF2_CL2_FinalOrder_Not_In_Scope.CL_upload)
    .exec(Logout.Signout)
    .pause(MaxThinkTime)

//==========08. Judge Final Order=====================
    .exec(_.set("loginFlag", "judge"))
    .exec(Home.Homepage)
    .exec(Login.Loginpage)
    .exec(spec_DF2_CL2_FinalOrder_Not_In_Scope.FinalOrder)
    .exec(Logout.Signout)

  setUp(
    CreateUnSpecClaimSCN.inject(nothingFor(120),rampUsers(40).during(2500)),
    CreateSpecClaimSCN.inject(rampUsers(40).during(2500))
  ).protocols(httpProtocol)
}
