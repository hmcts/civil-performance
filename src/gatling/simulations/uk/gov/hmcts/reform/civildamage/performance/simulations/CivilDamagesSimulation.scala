package uk.gov.hmcts.reform.civildamage.performance.simulations

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import uk.gov.hmcts.reform.civildamage.performance.scenarios._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.Environment
import io.gatling.core.controller.inject.open.OpenInjectionStep

import scala.swing.event.Key.Home
import io.gatling.core.pause.PauseType
import io.gatling.http

import scala.concurrent.duration.DurationInt


class CivilDamagesSimulation extends Simulation {
  
  val BaseURL = Environment.baseURL
  val loginFeeder = csv("login.csv").circular
	val defresponsecasesFeeder=csv("caseIds.csv").circular
	val sol7casesFeeder=csv("caseIdsSol7.csv").circular
	val sol8casesFeeder=csv("caseIdsSol8.csv").circular
	val defresponsecasesTrialFeeder=csv("caseIdsTrial.csv").circular
	val sol7casesTrialFeeder=csv("caseIdsSol7Trial.csv").circular
	val sol8casesTrialFeeder=csv("caseIdsSol8Trial.csv").circular
	
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
	
  // below scenario is for user data creation
  val UserCreationScenario = scenario("CMC User Creation")
    .exec(
      CreateUser.CreateCitizen("citizen")
        .pause(20)
    )
  
  //below scenario is to generate claims data for GA process
		
	val CivilUIClaimCreationScenario = scenario("Create Civil UI Claim")
		.feed(loginFeeder)
		.exitBlockOnFail {
			//CUI claim creation
			exec(Homepage.XUIHomePage)
				.exec(Login.XUILogin)
				.exec(CUIClaimCreation.run)
				// PBS payment
				.exec(CUIClaimCreation.PBSPayment)
				.pause(50)
			
				//Assign the claimant cse to defendant
				/*.exec(CivilAssignCase.cuiassign)
				.pause(20)*/
				.exec(Logout.XUILogout)
		}
	
	val CivilUIDefAndIntentScenario = scenario(" Civil UI Case Def and Intent")
		.feed(loginFeeder).feed(defresponsecasesFeeder)
		.exitBlockOnFail {
				//Defendant response
				exec(DefendantResponse.run)
					.pause(20)
			//claimant intention
				 .exec(Homepage.XUIHomePage)
				.exec(Login.XUILogin)
				.exec(ClaimantIntention.claimantintention)
					.pause(20)
				.exec(Logout.XUILogout)
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


	val CivilDamageScenario = scenario("Create Civil damage")
		.feed(loginFeeder)
		.repeat(1) {
			exitBlockOnFail {
				feed(defresponsecasesFeeder)
				.exec(EXUIMCLogin.manageCasesHomePage)
					.exec(EXUIMCLogin.manageCaseslogin)
					.exec(ClaimCreationLRvsLR.run)
				exec(CivilAssignCase.run)
				////	exec(CivilAssignCase.run)
				//		.exec(CivilAssignCase.run)
				//		.exec(EXUIMCLogin.manageCase_Logout)
			//		.exec(ClaimCreationLRvsLR.RespondToClaim)
				//.exec(EXUIMCLogin.manageCase_Logout)
				//		.exec(ClaimCreationLRvsLR.RespondToDefence)
				//.exec(CivilAssignCase.run)
				//	.exec(ClaimCreationLRvsLR.RespondToClaim)
			//		.exec(ClaimCreationLRvsLR.RespondToDefence)
				//		.exec(ClaimCreationLRvsLR.SDO)
				//		.pause(1)
				//		exec(CivilAssignCase.Auth)
			//		exec(CivilAssignCase.run)
				//		.pause(1)
				//			.exec(EXUIMCLogin.manageCase_Logout)
			}
		}
		.exec {
			session =>
				println(session)
				session
		}



	val CivilAssignScenario = scenario("Create Civil case assign")

		.exitBlockOnFail {

			exec(CivilAssignCase.cuiassign)

		}


	val CivilStrikeOut = scenario("Manually trigger strike out")
		.feed(loginFeeder)
		.exitBlockOnFail {
			feed(defresponsecasesFeeder)
		//	.exec(EXUIMCLogin.manageCasesHomePage)
		//		.exec(EXUIMCLogin.manageCasesloginToCentreAdminJourney)
		//		.exec(CaseProgression.HearingNotice)
		//		.exec(EXUIMCLogin.manageCase_Logout)

			.exec(CaseProgression.StrikeOut)

		}

	val CivilCaseProg = scenario("Create Civil damage")
		.feed(loginFeeder)
	.exitBlockOnFail {
		exec(EXUIMCLogin.manageCasesHomePage)
			.exec(EXUIMCLogin.manageCasesloginToCentreAdminJourney)
			.doSwitch("#{claimantuser}")(
				"civil.damages.claims+organisation.1.solicitor.1@gmail.com" -> feed(defresponsecasesFeeder),
				"hmcts.civil+organisation.1.solicitor.7@mailinator.com" -> feed(sol7casesFeeder),
				"hmcts.civil+organisation.1.solicitor.8@mailinator.com" -> feed(sol8casesFeeder)
			)
			.exec(CaseProgression.HearingNotice)
			.exec(EXUIMCLogin.manageCase_Logout)

				//		}
				//	.exitBlockOnFail {

				.exec(EXUIMCLogin.manageCasesHomePage)
				.exec(EXUIMCLogin.manageCasesloginToDefendantJourney)
				.exec(CaseProgression.EvidenceUploadDefendant)
				.exec(EXUIMCLogin.manageCase_Logout)
				//}

				//	.exitBlockOnFail {
				.exec(EXUIMCLogin.manageCasesHomePage)
				.exec(EXUIMCLogin.manageCaseslogin)
				.exec(CaseProgression.EvidenceUploadClaimant)
				.exec(CaseProgression.CaseFileView)

		//		.exec(CaseProgression.HearingFee)

		//		.exec(CaseProgression.BundleCreationIntegration)
							.doSwitch("#{claimantuser}")(
				"civil.damages.claims+organisation.1.solicitor.1@gmail.com" -> feed(defresponsecasesTrialFeeder),
				"hmcts.civil+organisation.1.solicitor.7@mailinator.com" -> feed(sol7casesTrialFeeder),
				"hmcts.civil+organisation.1.solicitor.8@mailinator.com" -> feed(sol8casesTrialFeeder)
							)
				.exec(CaseProgression.TrialReadiness)

				.exec(EXUIMCLogin.manageCase_Logout)
				//	}

				//	.exitBlockOnFail {
				.exec(EXUIMCLogin.manageCasesHomePage)
				.exec(EXUIMCLogin.manageCasesloginToJudgeJourney)
				.exec(CaseProgression.JudgeCaseNotes)
				.exec(CaseProgression.FinalGeneralOrders)
				.exec(EXUIMCLogin.manageCase_Logout)



		}

		.exec {
			session =>
				println(session)
				session
		}


	val CivilCaseDataPrep = scenario("Create Civil damage")
		.feed(loginFeeder)
		.exitBlockOnFail {
			exec(EXUIMCLogin.manageCasesHomePage)
				.exec(EXUIMCLogin.manageCaseslogin)
				.exec(ClaimCreationLRvsLR.run)

				.exec(CivilAssignCase.run)
				.exec(EXUIMCLogin.manageCase_Logout)

				.exec(EXUIMCLogin.manageCasesHomePage)
				.exec(EXUIMCLogin.manageCasesloginToDefendantJourney)
				.exec(ClaimCreationLRvsLR.RespondToClaim)
				.exec(EXUIMCLogin.manageCase_Logout)

			.exec(EXUIMCLogin.manageCasesHomePage)
				.exec(EXUIMCLogin.manageCaseslogin)
			//	.exec(ClaimCreationLRvsLR.run)


				.exec(ClaimCreationLRvsLR.RespondToDefence)
			.exec(EXUIMCLogin.manageCase_Logout)
				.exec(EXUIMCLogin.manageCasesHomePage)
				.exec(EXUIMCLogin.manageCasesloginToJudgeJourney)
				.exec(ClaimCreationLRvsLR.SDO)



		}

		.exec {
			session =>
				println(session)
				session
		}


	val STCitizen = scenario("Civil Citizen ST")
	//	.feed(loginFeeder)
		.exitBlockOnFail {
			exec(CivilCitizen.run)

		}

		.exec {
			session =>
				println(session)
				session
		}

		/*/*
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
*/

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
		//CivilCaseProg.inject(nothingFor(1),rampUsers(12) during (2700))
		CivilCaseDataPrep.inject(nothingFor(1),rampUsers(1) during (2))
		
).protocols(httpProtocol)
	
	/*setUp(
		CivilUIClaimCreationScenario.inject(simulationProfile(testType, claimsTargetPerHour, numberOfPipelineUsers)).pauses(pauseOption)
		//CivilUIDefAndIntentScenario.inject(simulationProfile(testType, defResponseAndIntentTargetPerHour, numberOfPipelineUsers)).pauses(pauseOption)
	).protocols(httpProtocol)*/
	
}