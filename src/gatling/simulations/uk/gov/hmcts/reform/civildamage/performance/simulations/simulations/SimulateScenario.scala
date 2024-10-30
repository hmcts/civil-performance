package uk.gov.hmcts.reform.civildamage.performance.simulations
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.scenario.Simulation
//import simulations.scenarios._
import scenarios.utils.Environment._
import scenarios.utils._
import scenarios._
import io.gatling.core.controller.inject.open.OpenInjectionStep
//import uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios.unspec_CreateClaim1.CreateUnSpecClaim

class SimulateScenario extends Simulation {
  val BaseURL = Environment.baseURL
  val IdamURL = Environment.idamURL
  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime
  val CaseIdFeeder = csv("caseIds.csv").circular
  val loginFeeder = csv("login.csv").circular
//  val ClaimAmt = 5000
//  val ClaimAmt = 11000
//  val ClaimAmt = 35000

  implicit val postHeaders: Map[String, String] = Map(
    "Origin" -> BaseURL
  )

  val CreateUnSpecClaimSCN = scenario("UnSpec_CreateClaim")
    .feed(CaseIdFeeder).feed(loginFeeder)
    //==========01.Create Claim==================
        .exec(_.set("LoginId", "civil.damages.claims+organisation.1.solicitor.1@gmail.com"))
        .exec(_.set("Passwordx", "Password12!"))
        .exec(Home.Homepage)
        .exec(Login.Loginpage)
        .exec(unspec_CreateClaim.CreateUnSpecClaim)
        .exec(Logout.Signout)
        .exec(FileWriterx.WriteToFile)
        .pause(50)
//============02.Make Pay & Notify================
    .exec(_.set("LoginId", "civil.damages.claims+organisation.1.solicitor.1@gmail.com"))
    .exec(_.set("Passwordx", "Password12!"))
    .exec(Home.Homepage)
    .exec(Login.Loginpage)
    .pause(30)
    .exec(MakePay.MakePay)
    .pause(30)
     .exec(Logout.Signout)
    .pause(10)
//============02.Notify================
          .exec(_.set("LoginId", "civil.damages.claims+organisation.1.solicitor.1@gmail.com"))
          .exec(_.set("Passwordx", "Password12!"))
          .exec(Home.Homepage)
          .exec(Login.Loginpage)
          .exec(unspec_notification.NotifyClaim)
          .exec(Logout.Signout)
          .pause(10)
//============02.Notify================
    .exec(_.set("LoginId", "civil.damages.claims+organisation.1.solicitor.1@gmail.com"))
    .exec(_.set("Passwordx", "Password12!"))
    .exec(Home.Homepage)
    .exec(Login.Loginpage)
    .exec(unspec_notification.NotifyClaimDetails)
    .exec(Logout.Signout)
    .pause(10)
//============03.Respond to Claimant===============
          .exec(_.set("LoginId", "civil.damages.claims+organisation.2.solicitor.1@gmail.com"))
          .exec(_.set("Passwordx", "Password12!"))
          .exec(Home.Homepage)
          .exec(Login.Loginpage)
          .exec(CivilAssignCase.AssignCase)
          .pause(50)
          .exec(unspec_DF_resp.DF_Resp)
          .exec(Logout.Signout)
          .pause(10)
//===========04.Claimant Response to DF================
      .exec(_.set("LoginId", "civil.damages.claims+organisation.1.solicitor.1@gmail.com"))
      .exec(_.set("Passwordx", "Password12!"))
      .exec(Home.Homepage)
      .exec(Login.Loginpage)
      .exec(unspec_CL_Resp.RespToDF)
      .exec(Logout.Signout)
      .pause(20)
//
////==============================JUDGE RESP==================================================
//          .exec(_.set("LoginId", "EMP261004@ejudiciary.net"))
//          .exec(_.set("Passwordx", "Testing123"))
//          .exec(Home.Homepage)
//          .exec(Login.Loginpage)
//          .exec(unspec_Jud_Hear.sdoJudge)
//          .exec(Logout.Signout)
//          .pause(10)
//    //==============================HEARING RESP==================================================
//    .exec(_.set("LoginId", "hearings_admin_region2_user@justice.gov.uk"))
//    .exec(_.set("Passwordx", "Password12!"))
//    .exec(Home.Homepage)
//    .exec(Login.Loginpage)
//    .exec(unspec_Jud_Hear.HearingAdmin)
//    .exec(Logout.Signout)
//    .pause(10)
////==============================RESP RESP==================================================
//    .exec(_.set("LoginId", "civil.damages.claims+organisation.2.solicitor.1@gmail.com"))
//    .exec(_.set("Passwordx", "Password12!"))
//    .exec(Home.Homepage)
//    .exec(Login.Loginpage)
//    .exec(unspec_DF_CL_FinalOrder.DF_upload)
//    .exec(Logout.Signout)
//    .pause(10)
//    //==============================CLAIMANT RESP==================================================
//
//    .exec(_.set("LoginId", "civil.damages.claims+organisation.1.solicitor.1@gmail.com"))
//    .exec(_.set("Passwordx", "Password12!"))
//    .exec(Home.Homepage)
//    .exec(Login.Loginpage)
//    .exec(unspec_DF_CL_FinalOrder.CL_upload)
//    .exec(Logout.Signout)
//    .pause(10)
//    //==============================JUDGE RESP==================================================
//
//    .exec(_.set("LoginId", "EMP261004@ejudiciary.net"))
//    .exec(_.set("Passwordx", "Testing123"))
//    .exec(Home.Homepage)
//    .exec(Login.Loginpage)
//    .exec(unspec_DF_CL_FinalOrder.FinalOrder)
//    .exec(Logout.Signout)
//    .pause(10)
//val SCN = scenario("SpecCourtClaimCommon")
//  .exec(test.tasty)


  //==============================SPEC=====================================
  val CreateSpecClaimSCN = scenario("Spec_Create_Claim")
    .feed(CaseIdFeeder)
//    //    //==========01.Create Claim==================
//          .exec(_.set("LoginId", "civil.damages.claims+organisation.1.solicitor.1@gmail.com"))
//          .exec(_.set("Passwordx", "Password12!"))
//          .exec(Home.Homepage)
//          .exec(Login.Loginpage)
//          .exec(spec_CreateClaim.CreateSpecClaim)
////          .pause(25)
////          .exec(a_CreateClaim.ClaimFeePayment)
//          .exec(Logout.Signout)
//          .exec(FileWriterx.WriteToFile)
//        .pause(25)
//
//        //==========01.Make Payment==================
//        .exec(_.set("LoginId", "civil.damages.claims+organisation.1.solicitor.1@gmail.com"))
//        .exec(_.set("Passwordx", "Password12!"))
//        .exec(Home.Homepage)
//        .exec(Login.Loginpage)
//        .exec(spec_CreateClaim.ClaimFeePayment)
//        .exec(Logout.Signout)
//        .pause(25)
//
//        //==========02.Assign Case to Def & Def Response==================
//    .exec(_.set("LoginId", "civil.damages.claims+organisation.2.solicitor.1@gmail.com"))
//    .exec(_.set("Passwordx", "Password12!"))
//    .exec(Home.Homepage)
//    .exec(Login.Loginpage)
//    .pause(25)
//    .exec(CivilAssignCase.AssignCase)
//    .pause(25)
//    .exec(spec_DefResp.selectRespon2Claim)
//    .exec(Logout.Signout)
//    .pause(25)
//
//        //==========02.Def Response==================
//    .exec(_.set("LoginId", "civil.damages.claims+organisation.2.solicitor.1@gmail.com"))
//    .exec(_.set("Passwordx", "Password12!"))
//    .exec(Home.Homepage)
//    .exec(Login.Loginpage)
//    .exec(spec_DefResp.selectRespon2Claim)
//    .exec(Logout.Signout)
//    .pause(25)

    //    //==========03.Claimant Response==================
    .exec(_.set("LoginId", "civil.damages.claims+organisation.1.solicitor.1@gmail.com"))
    .exec(_.set("Passwordx", "Password12!"))
    .exec(Home.Homepage)
    .exec(Login.Loginpage)
    .exec(spec_ClaimResp.ResToDef)
    .exec(Logout.Signout)
    .pause(25)

    //    //==========04.Judge SDO==================
    .exec(_.set("LoginId", "EMP261004@ejudiciary.net"))
    .exec(_.set("Passwordx", "Testing123"))
    .exec(Home.Homepage)
    .exec(Login.Loginpage)
    .exec(spec_SDO_Judge.sdoJudge)
    .exec(Logout.Signout)
    .pause(25)

    //    //==========05.Hearing Schedule==================
    .exec(_.set("LoginId", "hearings_admin_region2_user@justice.gov.uk"))
    .exec(_.set("Passwordx", "Password12!"))
    .exec(Home.Homepage)
    .exec(Login.Loginpage)
    .exec(spec_HearingAdmin.ClickTaskTab)
    .exec(spec_HearingAdmin.ScheduleHearing)
    .exec(Logout.Signout)

  //    //    //==========06.Defendant File Upload==================
  //    .exec(_.set("LoginId", "civil.damages.claims+organisation.2.solicitor.1@gmail.com"))
  //    .exec(_.set("Passwordx", "Password12!"))
  //    .exec(Home.Homepage)
  //    .exec(Login.Loginpage)
  //    .exec(f_DefResp.GoToCase)
  //    .exec(f_DefResp.SelectUpload)
  //    .exec(Logout.Signout)

  //    //    //==========07.Claimant File Upload==================
  //    .exec(_.set("LoginId", "civil.damages.claims+organisation.1.solicitor.1@gmail.com"))
  //    .exec(_.set("Passwordx", "Password12!"))
  //    .exec(Home.Homepage)
  //    .exec(Login.Loginpage)
  //    .exec(g_ClaimResp.GoToCase)
  //    .exec(g_ClaimResp.SelectUpload)
  //    .exec(Logout.Signout)

  //    //    //==========08.Judge Final Order==================
  //    .exec(_.set("LoginId", "EMP261004@ejudiciary.net"))
  //    .exec(_.set("Passwordx", "Testing123"))
  //    .exec(Home.Homepage)
  //    .exec(Login.Loginpage)
  //    .exec(h_CourtOrder.GoToCase)
  //    .exec(h_CourtOrder.MakeAnOrder)
  //    .exec(Logout.Signout)
  setUp(
        CreateUnSpecClaimSCN.inject(atOnceUsers(1))
//     CreateSpecClaimSCN.inject(atOnceUsers(1))
  ).protocols(httpProtocol)
}
