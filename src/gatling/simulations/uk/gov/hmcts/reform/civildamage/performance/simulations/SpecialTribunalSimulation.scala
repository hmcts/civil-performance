package uk.gov.hmcts.reform.civildamage.performance.simulations

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import uk.gov.hmcts.reform.civildamage.performance.scenarios._
import uk.gov.hmcts.reform.civildamage.performance.utils.Environment
import io.gatling.core.controller.inject.open.OpenInjectionStep

import scala.swing.event.Key.Home
import io.gatling.core.pause.PauseType
import io.gatling.http

import scala.concurrent.duration.DurationInt


class SpecialTribunalSimulation extends Simulation {
  
  val BaseURL = Environment.baseURL
  val loginFeeder = csv("login.csv").circular
	val stFeeder = csv("loginSt.csv").circular
	
  val httpProtocol = Environment.HttpProtocol
    .baseUrl(BaseURL)
   // .doNotTrackHeader("1")
   // .inferHtmlResources()
    .silentResources
		.header("Experimental", "true")

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

	val STCitizen = scenario("Civil Citizen ST")
	//	.feed(loginFeeder)
		.exitBlockOnFail {
			feed(stFeeder)

			.exec(CivilCitizen.run)

			.exec(EXUIMCLogin.manageCasesHomePage)
				.exec(EXUIMCLogin.manageCaseslogin)

				.exec(STRel3.ChangeStateSubmitted)
				.exec(STRel3.ContactParties)
				.exec(STRel3.DocumentUploadAmend)
				.exec(STRel3.ChangeStateCaseManagement)
				.exec(STRel3.CloseCase)
				.exec(STRel3.ChangeStateAwaiting)
				.exec(STRel3.IssueDecision)

		}

		.exec {
			session =>
				println(session)
				session
		}



	//defines the Gatling simulation model, based on the inputs
	def simulationProfile(simulationType: String, numberOfPerformanceTestUsers: Double, numberOfPipelineUsers: Double): Seq[OpenInjectionStep] = {
		simulationType match {
			case "perftest" =>
				if (debugMode == "off") {
					Seq(
						rampUsers(numberOfPerformanceTestUsers.toInt) during (testDurationMins minutes)
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
		/*CivilUIClaimCreationScenario.inject(nothingFor(5),rampUsers(90) during (3600)),
			CivilUIDefAndIntentScenario.inject(nothingFor(30),rampUsers(20) during (3600))*/
			//	CivilAssignScenario.inject(nothingFor(1),rampUsers(18) during (300))

	//	CivilCaseProg.inject(nothingFor(5),rampUsers(1) during (650))
		STCitizen.inject(nothingFor(1),rampUsers(30) during (3000))
).protocols(httpProtocol)
	
	/*setUp(
		CivilUIClaimCreationScenario.inject(simulationProfile(testType, claimsTargetPerHour, numberOfPipelineUsers)).pauses(pauseOption)
		//CivilUIDefAndIntentScenario.inject(simulationProfile(testType, defResponseAndIntentTargetPerHour, numberOfPipelineUsers)).pauses(pauseOption)
	).protocols(httpProtocol)*/
	
}