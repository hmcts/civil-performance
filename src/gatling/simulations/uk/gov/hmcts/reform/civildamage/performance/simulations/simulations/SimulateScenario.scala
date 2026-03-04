package uk.gov.hmcts.reform.civildamage.performance.simulations
import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import scenarios.utils.Environment._
import scenarios.utils._
import scenarios._


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

  setUp(
    CreateUnSpecClaimSCN.inject(rampUsers(300).during(300))
  ).protocols(httpProtocol)
}
