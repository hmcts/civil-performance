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

  val httpProtocol = Environment.HttpProtocol
		.baseUrl(BaseURL)
		.doNotTrackHeader("1")
		.inferHtmlResources()
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
	val CivilLipsScenario = scenario("Civil Create Claim for LIPS")
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
			// Assign the case to defendant
			.exec(CivilAssignCase.cuiassign)

//			.exec(_.set("claimNumber", "1734013013817931"))
//			.exec(_.set("defEmailAddress", "cuiimtdefusermXYVu@gmail.com"))

			// Login as Defendant & Reply
			.exec(CUIR2HomePage.CUIR2HomePage)
			.exec(CUIR2Login.CUIR2DefLogin)
			.exec(CUIR2DefendantResponse.run)

			// Defendant to Request a Hearing Change
			.exec(CUIR2DefendantRequestChange.run)
			.exec(CUIR2Logout.CUILogout)
			.pause(10)

//			.exec(_.set("claimantEmailAddress", "cuiimtclaimantuserBrbxN@gmail.com"))

			// Login as Claimant & Reply to Request
			.exec(CUIR2HomePage.CUIR2HomePage)
			.exec(CUIR2Login.CUIR2Login)
			.exec(CUIR2ClaimantRespondToRequest.run)
			.pause(10)

//			.exec(_.set("newClaimNumber", "1734021576998733"))

			//Login as Judge & Make an Order
			.exec(CUIR2HomePage.XUIHomePage)
			.exec(CUIR2Login.XUIJudicialLogin)
			.exec(CUIR2JudicialMakeDecision.run)
			.exec(CUIR2Logout.XUILogout)

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