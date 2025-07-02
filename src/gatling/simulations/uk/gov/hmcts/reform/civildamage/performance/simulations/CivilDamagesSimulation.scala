package uk.gov.hmcts.reform.civildamage.performance.simulations

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import uk.gov.hmcts.reform.civildamage.performance.scenarios._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.Environment
import io.gatling.core.controller.inject.open.OpenInjectionStep

import scala.swing.event.Key.Home
import io.gatling.core.pause.PauseType

import scala.concurrent.duration.DurationInt


class CivilDamagesSimulation extends Simulation {
  
  val BaseURL = Environment.baseURL
  val loginFeeder = csv("login.csv").circular
	val sharecaseusersfeed=csv("sharecaseusers.csv").random
  val httpProtocol = Environment.HttpProtocol
    .baseUrl(BaseURL)
    //.doNotTrackHeader("1")
    //.inferHtmlResources()
    //.silentResources
	implicit val postHeaders: Map[String, String] = Map(
		"Origin" -> BaseURL
	)

	val testType = scala.util.Properties.envOrElse("TEST_TYPE", "perftest")

	//set the environment based on the test type
	val environment = testType match {
		case "perftest" => "perftest"
		case "pipeline" => "aat"
		case _ => "**INVALID**"
	}

	/* ADDITIONAL COMMAND LINE ARGUMENT OPTIONS */
	val debugMode = System.getProperty("debug", "off") //runs a single user e.g. ./gradle gatlingRun -Ddebug=on (default: off)
	val env = System.getProperty("env", environment) //manually override the environment aat|perftest e.g. ./gradle gatlingRun -Denv=aat
	/* ******************************** */

	/* PERFORMANCE TEST CONFIGURATION */
	val testDurationMins = 60
	val numberOfPerformanceTestUsers: Double = 30
	val numberOfPipelineUsers: Double = 10

	val pipelinePausesMillis: Long = 3000 //3 seconds


	val pauseOption: PauseType = debugMode match {
		case "off" => constantPauses
		case _ => disabledPauses
	}
  
  
  // below scenario is for user data creation
  val UserCreationScenario = scenario("CMC User Creation")
    .exec(
      CreateUser.CreateCitizen("citizen")
        .pause(20)
    )
  
  //below scenario is to generate claims data for GA process
	
	
	val CivilClaimsScenario = scenario("Create Civil damage")
		.feed(loginFeeder).feed(sharecaseusersfeed)
		.exitBlockOnFail {
			exec(Homepage.XUIHomePage)
				.exec(Login.XUILogin)
				.exec(ClaimCreation.run)
				.pause(50)
				//.exec(ClaimDetailNotifications.run)
				//	.pause(50)
				.exec(Logout.XUILogout)
		}
	
	
	val CivilUIScenario = scenario("Create Civil UI Case")
		.feed(loginFeeder).feed(sharecaseusersfeed)
		.exitBlockOnFail {
			
			exec(Homepage.XUIHomePage)
				.exec(Login.XUILogin)
				.exec(CUIClaimCreation.run)
				.exec(CUIClaimCreation.PBSPayment)
				.pause(50)
				//.exec(ClaimDetailNotifications.run)
				//	.pause(50)
				.exec(Logout.XUILogout)
		}
		.exec {
			session =>
				println(session)
				session
		}
	
	
	
	/*
    Step 2: login to manage org as defendant solicitor to assign the case to other users from defendant solicitor firm
   
     */
	/*	.exec(EXUIMCLogin.manageOrgHomePage)
		.exec(EXUIMCLogin.manageOrglogin)
		.exec(EXUI_AssignCase.run)
		.exec(EXUIMCLogin.manageOrg_Logout)
		.pause(50)
	/*
Step 3: login as defendant user  and complete the defendant journey and logout

 */
		
		.exec(EXUIMCLogin.manageCasesHomePage)
		.exec(EXUIMCLogin.manageCasesloginToDefendantJourney)
		.exec(DefendantResponse.run)
		.pause(50)
		.exec(EXUIMCLogin.manageCase_Logout)
		
		/*
	 Step 4: below is the journey for response to defendant by claimant
		*/
		.exec(EXUIMCLogin.manageCasesHomePage)
		.exec(EXUIMCLogin.manageCaseslogin)
		.exec(ClaimResponseToDefendant.run)
		.pause(50)
		.exec(EXUIMCLogin.manageCase_Logout)
		*/
	
	
	val CivilAssignScenario = scenario("Create Civil case assign")
		
		.exitBlockOnFail {
			
			exec(CivilAssignCase.run)
			
		}

	val CivilDamageScenario = scenario("Create Civil damage")
		.feed(loginFeeder).feed(sharecaseusersfeed)
		.exec(EXUIMCLogin.manageCasesHomePage)
		.exec(EXUIMCLogin.manageCaseslogin)
		.exec(ClaimCreation.run)
		.pause(50)
		.exec(ClaimDetailNotifications.run)
		.pause(50)
		.exec(EXUIMCLogin.manageCase_Logout)

		/*
      Step 2: login to manage org as defendant solicitor to assign the case to other users from defendant solicitor firm

       */
		.exec(EXUIMCLogin.manageOrgHomePage)
		.exec(EXUIMCLogin.manageOrglogin)
		.exec(EXUI_AssignCase.run)
		.exec(EXUIMCLogin.manageOrg_Logout)
		.pause(50)
		/*
  Step 3: login as defendant user  and complete the defendant journey and logout

   */

		.exec(EXUIMCLogin.manageCasesHomePage)
		.exec(EXUIMCLogin.manageCasesloginToDefendantJourney)
		.exec(DefendantResponse.run)
		.pause(50)
		.exec(EXUIMCLogin.manageCase_Logout)

		/*
	 Step 4: below is the journey for response to defendant by claimant
		*/
		.exec(EXUIMCLogin.manageCasesHomePage)
		.exec(EXUIMCLogin.manageCaseslogin)
		.exec(ClaimResponseToDefendant.run)
		.pause(50)
		.exec(EXUIMCLogin.manageCase_Logout)


	//defines the Gatling simulation model, based on the inputs
	def simulationProfile(simulationType: String, numberOfPerformanceTestUsers: Double, numberOfPipelineUsers: Double): Seq[OpenInjectionStep] = {
		simulationType match {
			case "perftest" =>
				if (debugMode == "off") {
					Seq(
						rampUsers(numberOfPerformanceTestUsers.toInt) during (testDurationMins)
					)
				}
				else {
					Seq(atOnceUsers(1))
				}
			case "pipeline" =>
				Seq(rampUsers(numberOfPipelineUsers.toInt) during (2))
			case _ =>
				Seq(nothingFor(0))
		}
	}
	
	
	setUp(
		//CivilClaimsScenario.inject(nothingFor(1),rampUsers(300) during (3600))
		//	CivilUIScenario.inject(nothingFor(1),rampUsers(1) during (1))
				CivilDamageScenario.inject(nothingFor(1.minutes),rampUsers(1) during (12.minutes))
).protocols(httpProtocol).assertions(
		global.successfulRequests.percent.gte(100)) //YR: Remove this code


}