package simulations

import io.gatling.core.Predef._
import io.gatling.core.controller.inject.open.OpenInjectionStep
import io.gatling.core.pause.PauseType
import io.gatling.core.scenario.Simulation
import scenarios._
import utils.Environment
import scala.concurrent.duration._

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
		.inferHtmlResources()
//		.inferHtmlResources(DenyList("https://card.payments.service.gov.uk/.*"))
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

	/* PERFORMANCE TEST CONFIGURATION */
	val lipsTargetPerHour: Double = 110

	/* ADDITIONAL COMMAND LINE ARGUMENT OPTIONS */
	val debugMode = System.getProperty("debug", "off") //runs a single user e.g. ./gradle gatlingRun -Ddebug=on (default: off)
	val env = System.getProperty("env", environment) //manually override the environment aat|perftest e.g. ./gradle gatlingRun -Denv=aat
	/* ******************************** */
	
	/* PERFORMANCE TEST CONFIGURATION */
	val claimsTargetPerHour: Double = 2//90
	val defResponseAndIntentTargetPerHour: Double = 1//20

	val rampUpDurationMins = 5
	val rampDownDurationMins = 5
	val testDurationMins = 60
	
	val numberOfPipelineUsers = 5
	val pipelinePausesMillis: Long = 3000 //3 seconds

	//Determine the pause pattern to use:
	//Performance test = use the pauses defined in the scripts
	//Pipeline = override pauses in the script with a fixed value (pipelinePauseMillis)
	//Debug mode = disable all pauses
	val pauseOption: PauseType = debugMode match {
		case "off" if testType == "perftest" => constantPauses
		case "off" if testType == "pipeline" => customPauses(pipelinePausesMillis)
		case _ => constantPauses //disabledPauses
	}

	before {
		println(s"Test Type: ${testType}")
		println(s"Test Environment: ${env}")
		println(s"Debug Mode: ${debugMode}")
	}

/*======================================================================================
* Civil Lips vs Lips Scenario
======================================================================================*/
	val CivilLipsScenario = scenario("Civil Create Claim, Add Defendent and Lips")
//		.feed(assigncasesFeeder)
		.exitBlockOnFail {
			exec(_.set("env", s"${env}"))
			//Claim Creation
			.exec(CreateUser.CreateClaimantCitizen)
			.exec(CreateUser.CreateDefCitizen)
			.exec(CreateUser.CreateClaimantCitizen)
			.exec(CUIR2HomePage.CUIR2HomePage)
			.exec(CUIR2Login.CUIR2Login)
			.exec(CUIR2ClaimCreation.run)
			.exec(CUIR2Logout.CUILogout)
			.pause(30)
//			//assigning the case to defendant
			.exec(CivilAssignCase.cuiassign)
//			.exec(_.set("claimNumber", "1733929831126802"))
//			.exec(_.set("defEmailAddress", "cuiimtdefuservpviE@gmail.com"))
			.exec(CUIR2HomePage.CUIR2HomePage)
			.exec(CUIR2Login.CUIR2DefLogin)
			.exec(CUIR2DefendantResponse.run)
			.exec(CUIR2DefendantRequestChange.run)
			.exec(CUIR2Logout.CUILogout)

				//request change & make payment

				// login as claimant, review & respond to change

				// log in as judge to XUI and make an order

				//log in as claimant and upload doc - with notice application as well?




		}

	//defines the Gatling simulation model, based on the inputs
	def simulationProfile(simulationType: String, userPerHourRate: Double, numberOfPipelineUsers: Double): Seq[OpenInjectionStep] = {
		val userPerSecRate = userPerHourRate / 3600
		simulationType match {
			case "perftest" =>
				if (debugMode == "off") {
					Seq(
						rampUsersPerSec(0.00) to (userPerSecRate) during (rampUpDurationMins minutes),
						constantUsersPerSec(userPerSecRate) during (testDurationMins minutes),
						rampUsersPerSec(userPerSecRate) to (0.00) during (rampDownDurationMins minutes)
					)
				}
				else {
					Seq(atOnceUsers(1))
				}
			case "pipeline" =>
				Seq(rampUsers(numberOfPipelineUsers.toInt) during (2 minutes))
			case _ =>
				Seq(nothingFor(0))
		}
	}
	
	setUp(
		CivilLipsScenario.inject(simulationProfile(testType, lipsTargetPerHour, numberOfPipelineUsers)).pauses(pauseOption),
	).protocols(httpProtocol)
}