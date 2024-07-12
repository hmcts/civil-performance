package uk.gov.hmcts.reform.civildamage.performance.simulations

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import uk.gov.hmcts.reform.civildamage.performance.scenarios._
import utils.{Environment, IdamLogin, S2S}
import io.gatling.core.controller.inject.open.OpenInjectionStep

import scala.swing.event.Key.Home
import io.gatling.core.pause.PauseType
import io.gatling.http
import io.gatling.http.Predef.flushHttpCache

import scala.concurrent.duration.DurationInt


class CivilDamagesSimulation extends Simulation {
  
  val BaseURL = Environment.citizenURL
  val loginFeeder = csv("login.csv").circular
	val stFeeder = csv("loginSt.csv").circular
	val defresponsecasesFeeder=csv("defResponseDetails.csv").circular
	val claimantIntentioncasesFeeder=csv("claimantIntentionDetails.csv").circular
	val assigncasesFeeder=csv("caseIds.csv").circular
	//val viewAndResponseFeeder=csv("claimantcaseIds.csv").circular
	
	
	
	
  val httpProtocol = Environment.HttpProtocol
		.baseUrl(BaseURL)
		.doNotTrackHeader("1")
		.inferHtmlResources(DenyList("https://card.payments.service.gov.uk/.*"))
		.silentResources

	implicit val postHeaders: Map[String, String] = Map(
		"Origin" -> BaseURL
	)
	
	/* TEST TYPE DEFINITION */
	/* pipeline = nightly pipeline against the AAT environment (see the Jenkins_nightly file) */
	/* perftest (default) = performance test against the perftest environment */
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
	val claimsTargetPerHour: Double = 2//90
	val defResponseAndIntentTargetPerHour: Double = 1//20
	
	val rampUpDurationMins = 5
	val rampDownDurationMins = 5
	val  testDurationMins = 60
	
	val numberOfPipelineUsers = 5
	val pipelinePausesMillis: Long = 3000 //3 seconds

	
	//Determine the pause pattern to use:
	//Performance test = use the pauses defined in the scripts
	//Pipeline = override pauses in the script with a fixed value (pipelinePauseMillis)
	//Debug mode = disable all pauses
	val pauseOption: PauseType = debugMode match {
		case "off" if testType == "perftest" => constantPauses
		case "off" if testType == "pipeline" => customPauses(pipelinePausesMillis)
		case _ => disabledPauses
	}
	
	
	
	before {
		println(s"Test Type: ${testType}")
		println(s"Test Environment: ${env}")
		println(s"Debug Mode: ${debugMode}")
	}
	
  // below scenario is for user data creation
  val UserCreationScenario = scenario("CMC User Creation")
    .exec(
      CreateUser.CreateClaimantCitizen
        .pause(20)
    )
  
  //below scenario is to generate claims data for GA process
		
	
	
	
	

	
	
	
	/*
	#######################  CUI R2 Claim Creation ############################################
	 */
	
	val CivilUIR2ClaimCreationScenario = scenario(" Civil UI R2 Claim Creation")
	
		.exitBlockOnFail {
			
			//Claim Creation
			exec(CreateUser.CreateClaimantCitizen)
			.exec(CreateUser.CreateDefCitizen)
			.exec(CUIR2HomePage.CUIR2HomePage)
			.exec(CUIR2Login.CUIR2Login)
				.exec(CUIR2ClaimCreation.run)
				.exec(CUIR2Logout.CUILogout)
		}
	
	/*
#######################  CUI R2 Defendant Response ############################################
*/
	
	val CivilUIR2DefResponseScenario = scenario(" Civil UI R2 Defendant Response")
		.feed(defresponsecasesFeeder)
		.exitBlockOnFail {
			exec(CUIR2HomePage.CUIR2HomePage)
				.exec(CUIR2Login.CUIR2DefLogin)
			.exec(CUIR2DefendantResponse.run)
				.exec(CUIR2Logout.CUILogout)
					}
	
	/*
#######################  CUI R2 Claimant Intention ############################################
 */
	
	val CivilUIR2ClaimantIntentionScenario = scenario(" Civil UI R2 Claimant Intention")
		.feed(claimantIntentioncasesFeeder)
		.exitBlockOnFail {
			//view and response to defendant
				exec(CUIR2HomePage.CUIR2HomePage)
          .exec(CUIR2Login.CUIR2ClaimantIntentionLogin)
        .exec(CUIR2ClaimantIntention.run)
					.exec(CUIR2Logout.CUILogout)
		}
	
	/*======================================================================================
* Below scenario is for Assign cases to defendant user for CUI R2
======================================================================================*/
	val CivilCaseAssignScenario = scenario("Civil Case Assign")
		.feed(assigncasesFeeder)
		.exitBlockOnFail {
			exec(_.set("env", s"${env}"))
				.exec(CivilAssignCase.cuiassign)
		}
	
	

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
		CivilUIR2ClaimCreationScenario.inject(nothingFor(1),rampUsers(115) during (3600)),
		CivilUIR2DefResponseScenario.inject(nothingFor(30),rampUsers(100) during (3600)),
		CivilUIR2ClaimantIntentionScenario.inject(nothingFor(50),rampUsers(25) during (3600))
	//	CivilCaseAssignScenario.inject(nothingFor(1),rampUsers(125) during (600))
).protocols(httpProtocol)
	

	
}