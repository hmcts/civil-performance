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
	val defRequestChange=csv("defRequestChange.csv").circular
	
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

	val LipVsLR = scenario(" LiP vs LR")
		.feed(loginFeeder)
		.exec(CreateUser.CreateClaimantCitizen)
//		.exec(CreateUser.CreateXUICitizen)
//		.exitHere
		.exec(CUIR2HomePage.CUIR2HomePage)
		.exec(CUIR2Login.CUIR2Login)
		.exec(CUIR2ClaimCreation.run)
		.exec(CUIR2Logout.CUILogout)
		.exec(CivilAssignCase.cuiassign)
		.pause(120)
		.exec(_.set("loginFlag", "defendant"))
		.exec(XUILogin.Homepage)
		.exec(XUILogin.Loginpage)
		.exec(XUINoticeOfChange.noticeOfChange)
		.exec(XUIDefendantResponse.selectRespondToClaim)
		.exec(XUILogin.Logout)
		.exec(CUIR2HomePage.CUIR2HomePage)
		.exec(CUIR2Login.CUIR2Login)
		.exec(CUIR2ClaimantIntentionCaseProg.run)
		.exec(CUIR2Logout.CUILogout)
		.exitHere
		.exec(_.set("loginFlag", "judge"))
		.exec(XUILogin.Homepage)
		.exec(XUILogin.Loginpage)
		.exec(XUIJudgeSDO.standardDirectionOrder)
		.exec(XUILogin.Logout)





	/*
	#######################  CUI R2 Claim Creation Scenario Small Claim ############################################
	 */
	
	val CivilUIR2ClaimCreationScenario = scenario(" Civil UI R2 Claim Creation")
	
		.exitBlockOnFail {
			
			//Claim Creation
			exec(CreateUser.CreateDefCitizen)
			.repeat(1) {
				exec(CreateUser.CreateClaimantCitizen)
					.exec(CUIR2HomePage.CUIR2HomePage)
					.exec(CUIR2Login.CUIR2Login)
					.exec(CUIR2ClaimCreation.run)
					.exec(CUIR2Logout.CUILogout)
					//.exec(CivilAssignCase.cuiassign)
									}
			}
			
			
			/*
#######################  CUI R2 Claim Creation Scenario Small Claim ############################################
 */
			
			val CivilUIR2DefRequestChange = scenario(" Civil UI R2 Request Change")
				.feed(defRequestChange)
				.exitBlockOnFail {
					
					//Claim Creation
					exec(CUIR2HomePage.CUIR2HomePage)
						.exec(CUIR2Login.CUIR2DefTestLogin)
						.exec(CUIR2DefRequestChange.run)
						.exec(CUIR2Logout.CUILogout)
					
				}
			
			/*
#######################  CUI R2 Claim Creation ScenarioIntermediate Track ############################################
 */
			
			val CivilUIR2ClaimCreationIntermediateTrackScenario = scenario(" Civil UI R2 Claim Creation For Intermediate Track")
				
				.exitBlockOnFail {
					
					//Claim Creation
					exec(CreateUser.CreateDefCitizen)
						.repeat(1) {
							exec(CreateUser.CreateClaimantCitizen)
								.exec(CUIR2HomePage.CUIR2HomePage)
								.exec(CUIR2Login.CUIR2Login)
								.exec(CUIR2ClaimCreationIntermediateTrack.run)
								.exec(CUIR2Logout.CUILogout)
								.exec(CivilAssignCase.cuiassign)
						}
				}
			
			/*
#######################  CUI R2 Claim Creation ScenarioIntermediate Track ############################################
*/
			
			val CivilUIR2ClaimCreationMultiTrackScenario = scenario(" Civil UI R2 Claim Creation For Intermediate Track")
				
				.exitBlockOnFail {
					
					//Claim Creation
					exec(CreateUser.CreateDefCitizen)
						.repeat(1) {
							exec(CreateUser.CreateClaimantCitizen)
								.exec(CUIR2HomePage.CUIR2HomePage)
								.exec(CUIR2Login.CUIR2Login)
								.exec(CUIR2ClaimCreationMultiTrack.run)
								.exec(CUIR2Logout.CUILogout)
								.exec(CivilAssignCase.cuiassign)
						}
				}
			
			
			/*
#######################  CUI R2 Claim Creation Scenario Large Claim ############################################
 */
			
			val CivilUIR2ClaimCreationFTScenario = scenario(" Civil UI R2 Claim Creation Fast Track")
				
				.exitBlockOnFail {
					
					//Claim Creation
					exec(CreateUser.CreateDefCitizen)
						.repeat(1) {
							exec(CreateUser.CreateClaimantCitizen)
								.exec(CUIR2HomePage.CUIR2HomePage)
								.exec(CUIR2Login.CUIR2Login)
								.exec(CUIR2ClaimCreationCaseProgFastTrack.run)
								.exec(CUIR2Logout.CUILogout)
							//.exec(CivilAssignCase.cuiassign)
						}
					
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
#######################  CUI R2 Defendant Response ############################################
*/
			
			val CivilUIR2DefResponseCaseProgScenario = scenario(" Civil UI R2 Defendant Response")
				.feed(defresponsecasesFeeder)
				.exitBlockOnFail {
					exec(CUIR2HomePage.CUIR2HomePage)
						.exec(CUIR2Login.CUIR2DefLogin)
						.exec(CUIR2DefendantResponseCaseProg.run)
						.exec(CUIR2Logout.CUILogout)
				}
			
			/*
#######################  CUI R2 Defendant Response  For Fast Track  ############################################
*/
			
			val CivilUIR2DefResponseCaseProgFastTrackScenario = scenario(" Civil UI R2 Defendant Response Case prog Fast Track")
				.feed(defresponsecasesFeeder)
				.exitBlockOnFail {
					exec(CUIR2HomePage.CUIR2HomePage)
						.exec(CUIR2Login.CUIR2DefLogin)
						.exec(CUIR2DefendantResponseCaseProgFastTrack.run)
						.exec(CUIR2Logout.CUILogout)
				}
			
			
			/*
#######################  CUI R2 Defendant Response  For Intermediate Track  ############################################
*/
			
			val CivilUIR2DefResponseIntermediateTrackScenario = scenario(" Civil UI R2 Defendant Response Intermediate Track")
				.feed(defresponsecasesFeeder)
				.exitBlockOnFail {
					exec(CUIR2HomePage.CUIR2HomePage)
						.exec(CUIR2Login.CUIR2DefLogin)
						.exec(CUIR2DefendantResponseIntermediateTrack.run)
						.exec(CUIR2Logout.CUILogout)
				}
			
			
			/*
#######################  CUI R2 Defendant Response  For Multi Track  ############################################
*/
			
			val CivilUIR2DefResponseMultiTrackScenario = scenario(" Civil UI R2 Defendant Response Multi Track")
				.feed(defresponsecasesFeeder)
				.exitBlockOnFail {
					exec(CUIR2HomePage.CUIR2HomePage)
						.exec(CUIR2Login.CUIR2DefLogin)
						.exec(CUIR2DefendantResponseMultiTrack.run)
						.exec(CUIR2Logout.CUILogout)
				}
			
			/*
#######################  CUI R2 Claimant Intention ############################################
 */
			
			val CivilUIR2ClaimantIntentionScenario = scenario(" Civil UI R2 Claimant Intention For Small Claims")
				.feed(claimantIntentioncasesFeeder)
				.exitBlockOnFail {
					//view and response to defendant
					exec(CUIR2HomePage.CUIR2HomePage)
						.exec(CUIR2Login.CUIR2ClaimantIntentionLogin)
						.exec(CUIR2ClaimantIntention.run)
						.exec(CUIR2Logout.CUILogout)
				}
			
			/*
#######################  CUI R2 Claimant Intention For Case Prog ############################################
*/
			
			val CivilUIR2ClaimantIntentionFastTrackCaseProgScenario = scenario(" Civil UI R2 Claimant Intention for CaseProg Fast Track")
				.feed(claimantIntentioncasesFeeder)
				.exitBlockOnFail {
					//view and response to defendant
					exec(CUIR2HomePage.CUIR2HomePage)
						.exec(CUIR2Login.CUIR2ClaimantIntentionLogin)
						.exec(CUIR2ClaimantIntentionCaseProgFastTrack.run)
						.exec(CUIR2Logout.CUILogout)
				}
			
			
			/*
#######################  CUI R2 Claimant Intention For Case Prog ############################################
*/
			
			val CivilUIR2ClaimantIntentionCaseProgScenario = scenario(" Civil UI R2 Claimant Intention for CaseProg")
				.feed(claimantIntentioncasesFeeder)
				.exitBlockOnFail {
					//view and response to defendant
					exec(CUIR2HomePage.CUIR2HomePage)
						.exec(CUIR2Login.CUIR2ClaimantIntentionLogin)
						.exec(CUIR2ClaimantIntentionCaseProg.run)
						.exec(CUIR2Logout.CUILogout)
				}
			
			
			/*
#######################  CUI R2 Case Prog File Upload ############################################
*/
			
			val CivilUIR2CaseProgScenario = scenario(" Civil UI R2 Case Prog Scenario")
				.feed(claimantIntentioncasesFeeder)
				.exitBlockOnFail {
					//Below is for upload claimant evidence
					exec(CUIR2HomePage.CUIR2HomePage)
						.exec(CUIR2Login.CUIR2ClaimantIntentionLogin)
						.exec(CUIR2DocUploadCaseProg.CaseProgUploadDocsByClaimant)
						.pause(10)
						.exec(CUIR2DocUploadCaseProg.viewUploadedDocuments)
						.exec(CUIR2Logout.CUILogout)
						.pause(10)
					
					//below is the defendant upload documents
					/*	.exec(CUIR2HomePage.CUIR2HomePage)
				.exec(CUIR2Login.CUIR2DefLogin)
				.exec(CUIR2DocUploadCaseProg.CaseProgUploadDocsByDefendant)
				.pause(10)
				.exec(CUIR2DocUploadCaseProg.viewUploadedDocuments)
				.exec(CUIR2Logout.CUILogout)
				.pause(10)*/
					
					/* exec(CUIR2HomePage.CUIR2HomePage)
				.exec(CUIR2Login.CUIR2ClaimantIntentionLogin)
				.exec(CUIR2DocUploadCaseProg.viewOrderandNotices)
				.pause(10)
				.exec(CUIR2DocUploadCaseProg.viewUploadedDocuments)
				.pause(10)
				.exec(CUIR2DocUploadCaseProg.payHearingFee)
				.exec(CUIR2Logout.CUILogout)*/
				
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
			def simulationProfile (simulationType: String, numberOfPerformanceTestUsers: Double, numberOfPipelineUsers: Double): Seq[OpenInjectionStep] = {
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
			//	CivilUIR2ClaimCreationScenario.inject(nothingFor(1), rampUsers(5) during (100)),
				//	CivilUIR2ClaimCreationFTScenario.inject(nothingFor(1),rampUsers(15) during (300)),
				//	CivilUIR2ClaimCreationIntermediateTrackScenario.inject(nothingFor(1),rampUsers(1) during (1)),
				//	CivilUIR2ClaimCreationMultiTrackScenario.inject(nothingFor(1),rampUsers(1) during (1)),
				//	CivilUIR2DefResponseScenario.inject(nothingFor(30),rampUsers(100) during (3600)),
				//	CivilUIR2DefResponseCaseProgScenario.inject(nothingFor(3),rampUsers(15) during (200)),
				//	CivilUIR2DefResponseCaseProgFastTrackScenario.inject(nothingFor(4),rampUsers(15) during (300)),
				//	CivilUIR2ClaimantIntentionScenario.inject(nothingFor(50),rampUsers(25) during (3600))
				//	CivilUIR2ClaimantIntentionCaseProgScenario.inject(nothingFor(5),rampUsers(15) during (200))
				//	CivilUIR2ClaimantIntentionFastTrackCaseProgScenario.inject(nothingFor(5),rampUsers(15) during (300))
				//	CivilUIR2CaseProgScenario.inject(nothingFor(5),rampUsers(1) during (1))
				//	CivilCaseAssignScenario.inject(nothingFor(1),rampUsers(1) during (1))
				// Below is for data prep - Claim Creation
				// CivilUIR2ClaimCreationScenario.inject(nothingFor(1),rampUsers(150) during (1800))
				// Below is for creating the test data for claimant intention
				//CivilUIR2DefResponseScenario.inject(nothingFor(1),rampUsers(32) during (600)),
				//	CivilUIR2ClaimantIntentionScenario.inject(nothingFor(1),rampUsers(1) during (1)),
				//	CivilUIR2DefRequestChange.inject(nothingFor(1),rampUsers(1) during (1)),
				// Below set up is for background load for CUI R2 Journey
				
					//CivilUIR2ClaimCreationScenario.inject(nothingFor(1),rampUsers(5) during (100)),
//				CivilUIR2ClaimCreationScenario.inject(atOnceUsers(1)),


				LipVsLR.inject(rampUsers(65).during(2500))
//				tempLiPVsLR.inject(atOnceUsers(1))

		//CivilUIR2DefResponseScenario.inject(nothingFor(30),rampUsers(100) during (3600)),
	//	CivilUIR2ClaimantIntentionScenario.inject(nothingFor(50),rampUsers(25) during (3600))
			
			).protocols(httpProtocol)
			
		}
