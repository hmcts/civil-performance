package uk.gov.hmcts.reform.civildamage.performance.simulations

import io.gatling.commons.stats.assertion.Assertion
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
  
  val BaseURL = Environment.baseURL
	val citizenURL= Environment.citizenURL
  val loginFeeder = csv("login.csv").circular
	val cpLoginFeeder = csv("cuir2cplogin.csv").circular
	val assigncasesFeeder=csv("assigncasesfeeder.csv").circular
	val cpfulltestsmallclaimsFeeder=csv("cuir2cpsmallclaims.csv").circular

	
	
	
  val httpProtocol = Environment.HttpProtocol
    .baseUrl(BaseURL)
   // .doNotTrackHeader("1")
    .inferHtmlResources()
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
		case "pipeline" => "perftest"
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

	val smallClaimsAmount = "9000"
	val fastClaimsAmount = "20000"

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
				.exec(CivilAssignCase.cuiassign)
				.exec(Logout.XUILogout)
			
		}
	
	
	/*======================================================================================
* Below scenario is for cases that are data prep for hearing management.
======================================================================================*/
	val CivilCaseAssignScenario = scenario("Civil Case Assign")
		.feed(assigncasesFeeder)
		.exitBlockOnFail {
			exec(_.set("env", s"${env}"))
				.exec(CivilAssignCase.cuiassign)
		}
	
	

	

	
	
	/*======================================================================================
* Below scenario is for SDO  Small Claims - CUI R2 Civil
======================================================================================*/
	val SDOSmallClaimsCUIR2= scenario("SDO For CUIR2 Claims Small Track")
	.exec(flushHttpCache).feed(loginFeeder) //.feed(casesfordefresponseFeeder)
		.exitBlockOnFail {
			exec(_.set("env", s"${env}"))
				.exec(Homepage.XUIHomePage)
				.exec(Login.XUIJudgeLogin)
				.exec(SDOCivilProg.SDOSmallClaimsForCUIR2)
				.exec(EXUIMCLogin.manageCase_Logout)
			
		}
	
		
	/*======================================================================================
* Below scenario is for SDO  Small Claims - CUI R2 Civil
======================================================================================*/
	val SDOFastTrackCUIR2 = scenario("SDO For CUIR2 Claims fast Track")
		.feed(loginFeeder) //.feed(casesfordefresponseFeeder)
		.exitBlockOnFail {
			exec(_.set("env", s"${env}"))
				.exec(Homepage.XUIHomePage)
				.exec(Login.XUIJudgeLogin)
				.exec(SDOCivilProg.SDOFastTrackForCUIR2)
				.exec(EXUIMCLogin.manageCase_Logout)
			
		}

	/*======================================================================================
* Below scenario is for   Small Claims - CUI R2 Civil Case progression -Full Scenario
======================================================================================*/
	val CUIR2SmallClaimsCaseProgression = scenario(" CUIR2 CaseProgression Small Claims")
		.feed(cpLoginFeeder) .feed(cpfulltestsmallclaimsFeeder)
		.exitBlockOnFail {
		exec(_.set("CUIR2SmallClaimsAmount", s"${smallClaimsAmount}"))
			//.exec(_.set("CourtLocation", "9000")) - WIP
			.exec(_.set("env", s"${env}"))
      .exec(_.set("testType", s"${testType}"))
				.exec(CreateUser.CreateDefCitizen)
				.repeat(1) {
					exec(CreateUser.CreateClaimantCitizen)
						.exec(CUIR2HomePage.CUIR2HomePage)
						.exec(CUIR2Login.CUIR2Login)
						.exec(CUIR2ClaimCreation.run)
						.exec(CUIR2Logout.CUILogout)
						.pause(20)
						.exec(CivilAssignCase.cuiassign)
						.pause(10)
				}
				// below is for defendant response for case prog small track
				.exec(CUIR2HomePage.CUIR2HomePage)
				.exec(CUIR2Login.CUIR2DefLogin)
				.exec(CUIR2DefendantResponseCaseProg.run)
				.exec(CUIR2Logout.CUILogout)

				//below is the for claimant intention for case prog for small claims

				.exec(CUIR2HomePage.CUIR2HomePage)
				.exec(CUIR2Login.CUIR2Login)
				.exec(CUIR2ClaimantIntentionCaseProg.run)
				.exec(CUIR2Logout.CUILogout)
        .doIfEquals("#{testType}", "pipeline") (exitHere)

				// below is 80% cases are now stopped here

				// 🎯 **80% Users Exit Here**
				.randomSwitch(
					80.0 -> exec { session =>
						println("✅ Stopping Execution for 80% Users")
						session.markAsFailed
					}
					//YR: Added below code to delete user after use
					//.exec(CUIClaimCreationWithAPI.deleteClaimantUser)
					//.exec(CUIClaimCreationWithAPI.deleteDefendantUser)
				)
				// below is for SDO for small claims

				.exec(Homepage.XUIHomePage)
				.exec(Login.XUIJudgeLogin)
				.exec(SDOCivilProg.MediaionUnsuccessfulBeforeSDO)
				//.exec(SDOCivilProg.SDOSmallClaimsForCUIR2)
				.exec(EXUIMCLogin.manageCase_Logout)

				//YR - 13/05/2025 - New code to continue applicaton with LA user after judge completes the mediation
				.exec(Homepage.XUIHomePage)
				.exec(Login.XUILALogin)
				.exec(SDOCivilProg.SDOSmallClaimsForCUIR2)
				.exec(EXUIMCLogin.manageCase_Logout)

				//Below is for upload claimant evidence
				.exec(CUIR2HomePage.CUIR2HomePage)
				.exec(CUIR2Login.CUIR2Login)
				.exec(CUIR2DocUploadCaseProg.CaseProgUploadDocsByClaimant)
				.pause(10)
				.exec(CUIR2DocUploadCaseProg.viewUploadedDocuments)
				.exec(CUIR2Logout.CUILogout)
				.pause(10)

				//below is the defendant upload documents
				.exec(CUIR2HomePage.CUIR2HomePage)
				.exec(CUIR2Login.CUIR2DefLogin)
				.exec(CUIR2DocUploadCaseProg.CaseProgUploadDocsByDefendant)
				.pause(10)
				.exec(CUIR2DocUploadCaseProg.viewUploadedDocuments)
				.exec(CUIR2Logout.CUILogout)
				.pause(10)
				//Following is for creating the hearing notice for small claims
				.exec(Homepage.XUIHomePage)
				.exec(Login.XUICenterAdminLogin)
				.exec(CUIR2CaseProgression.HearingNotice)
				.exec(EXUIMCLogin.manageCase_Logout)
				.pause(10)
				//Following is for paying Hearing Fee
				.exec(CUIR2HomePage.CUIR2HomePage)
				.exec(CUIR2Login.CUIR2ClaimantIntentionLogin)
				.exec(CUIR2DocUploadCaseProg.viewOrderandNotices)
				.pause(10)
				.exec(CUIR2DocUploadCaseProg.viewUploadedDocuments)
				.pause(10)
				.exec(CUIR2DocUploadCaseProg.payHearingFee)
				.pause(20)
				.exec(CivilAssignCase.cuibundle)
				.pause(30)
				// view the bundle
				.exec(CUIR2DocUploadCaseProg.viewBundle)
				.exec(CUIR2Logout.CUILogout)
				.pause(20)
				//Following is For creating the Final Order For Smaill Claims
				.exec(Homepage.XUIHomePage)
				.exec(Login.XUIJudgeLogin)
				.exec(CUIR2CaseProgression.FinalGeneralOrders)
				.exec(EXUIMCLogin.manageCase_Logout)
				//YR: Added below code to delete user after use
				//.exec(CUIClaimCreationWithAPI.deleteClaimantUser)
				//.exec(CUIClaimCreationWithAPI.deleteDefendantUser)
		}


	/*======================================================================================
* Below scenario is for   Small Claims - CUI R2 Civil Case progression -Full Scenario
======================================================================================*/
	val CUIR2SmallClaimsCaseProgressionWithSDOViaJudge = scenario(" CUIR2 CaseProgression Small Claims")
		.feed(cpLoginFeeder) .feed(cpfulltestsmallclaimsFeeder)
		.exitBlockOnFail {
			exec(_.set("env", s"${env}"))
			.exec(CreateUser.CreateDefCitizen)
				.repeat(1) {
					exec(CreateUser.CreateClaimantCitizen)
						.exec(CUIR2HomePage.CUIR2HomePage)
						.exec(CUIR2Login.CUIR2Login)
						.exec(CUIR2ClaimCreation.run)
						.exec(CUIR2Logout.CUILogout)
						.pause(20)
						.exec(CivilAssignCase.cuiassign)
						.pause(10)
				}
			// below is for defendant response for case prog small track
			.exec(CUIR2HomePage.CUIR2HomePage)
				.exec(CUIR2Login.CUIR2DefLogin)
				.exec(CUIR2DefendantResponseCaseProg.run)
				.exec(CUIR2Logout.CUILogout)
				
				//below is the for claimant intention for case prog for small claims
			
			.exec(CUIR2HomePage.CUIR2HomePage)
				.exec(CUIR2Login.CUIR2Login)
				.exec(CUIR2ClaimantIntentionCaseProg.run)
				.exec(CUIR2Logout.CUILogout)
				
				// below is 80% cases are now stopped here
				
				// 🎯 **80% Users Exit Here**
				.randomSwitch(
					80.0 -> exec { session =>
						println("✅ Stopping Execution for 80% Users")
						session.markAsFailed
					}
				)
			// below is for SDO for small claims
				
				.exec(Homepage.XUIHomePage)
				.exec(Login.XUIJudgeLogin)
				.exec(SDOCivilProg.MediaionUnsuccessfulBeforeSDO)
				.exec(SDOCivilProg.SDOSmallClaimsForCUIR2)
				.exec(EXUIMCLogin.manageCase_Logout)

				//Below is for upload claimant evidence
					.exec(CUIR2HomePage.CUIR2HomePage)
            .exec(CUIR2Login.CUIR2Login)
           .exec(CUIR2DocUploadCaseProg.CaseProgUploadDocsByClaimant)
            .pause(10)
           .exec(CUIR2DocUploadCaseProg.viewUploadedDocuments)
            .exec(CUIR2Logout.CUILogout)
            .pause(10)
            
            //below is the defendant upload documents
          .exec(CUIR2HomePage.CUIR2HomePage)
            .exec(CUIR2Login.CUIR2DefLogin)
           .exec(CUIR2DocUploadCaseProg.CaseProgUploadDocsByDefendant)
            .pause(10)
           .exec(CUIR2DocUploadCaseProg.viewUploadedDocuments)
            .exec(CUIR2Logout.CUILogout)
            .pause(10)
				//Following is for creating the hearing notice for small claims
			.exec(Homepage.XUIHomePage)
				.exec(Login.XUICenterAdminLogin)
				.exec(CUIR2CaseProgression.HearingNotice)
				.exec(EXUIMCLogin.manageCase_Logout)
				.pause(10)
			//Following is for paying Hearing Fee
			.exec(CUIR2HomePage.CUIR2HomePage)
				.exec(CUIR2Login.CUIR2ClaimantIntentionLogin)
				.exec(CUIR2DocUploadCaseProg.viewOrderandNotices)
				.pause(10)
				.exec(CUIR2DocUploadCaseProg.viewUploadedDocuments)
				.pause(10)
				.exec(CUIR2DocUploadCaseProg.payHearingFee)
						.pause(20)
						.exec(CivilAssignCase.cuibundle)
						.pause(30)
						// view the bundle
						.exec(CUIR2DocUploadCaseProg.viewBundle)
				.exec(CUIR2Logout.CUILogout)
				.pause(20)
				//Following is For creating the Final Order For Smaill Claims
				.exec(Homepage.XUIHomePage)
				.exec(Login.XUIJudgeLogin)
				.exec(CUIR2CaseProgression.FinalGeneralOrders)
				.exec(EXUIMCLogin.manageCase_Logout)
		}
		
	/*======================================================================================
* Below scenario is for   Fast Track - CUI R2 Civil Case progression -Full Scenario
======================================================================================*/
	val CUIR2FastTrackCaseProgression = scenario("SDO For CUIR2 CaseProgression Fast Track")
		.feed(cpLoginFeeder)//.feed(cpfulltestfasttrackFeeder)
		.exitBlockOnFail {
			exec(_.set("CUIR2FastClaimsAmount", s"${fastClaimsAmount}"))
			.exec(_.set("env", s"${env}"))
      .exec(_.set("testType", s"${testType}"))
				.exec(CreateUser.CreateDefCitizen)
				.repeat(1) {
					exec(CreateUser.CreateClaimantCitizen)
						.exec(CUIR2HomePage.CUIR2HomePage)
						.exec(CUIR2Login.CUIR2Login)
						.exec(CUIR2ClaimCreationFastTrack.run)
						.exec(CUIR2Logout.CUILogout)
						.pause(30)
						.exec(CivilAssignCase.cuiassign)
						.pause(10)
				}
				
				// below is for defendant response for case prog fast track claims
				.exec(CUIR2HomePage.CUIR2HomePage)
				.exec(CUIR2Login.CUIR2DefLogin)
				.exec(CUIR2DefendantResponseCaseProgFastTrack.run)
				.exec(CUIR2Logout.CUILogout)
				.pause(10)
				//below is the for claimant intention for case prog for fast track claims
				
				.exec(CUIR2HomePage.CUIR2HomePage)
				.exec(CUIR2Login.CUIR2ClaimantIntentionLogin)
				.exec(CUIR2ClaimantIntentionCaseProgFastTrack.run)
				.exec(CUIR2Logout.CUILogout)
        .doIfEquals("#{testType}", "pipeline") (exitHere)
				.pause(120)
				// below is the SDO for fast track
				
				// 🎯 **80% Users Exit Here**
				.randomSwitch(
					80.0 -> exec { session =>
						println("✅ Stopping Execution for 80% Users")
						session.markAsFailed
					}
				)
				
				.exec(Homepage.XUIHomePage)
				.exec(Login.XUIJudgeLogin)
			//	.exec(SDOCivilProg.MediaionUnsuccessfulBeforeSDO)
				.exec(SDOCivilProg.SDOFastTrackForCUIR2)
				.exec(EXUIMCLogin.manageCase_Logout)
				.pause(30)
			//Below is for upload claimant evidence
			.exec(CUIR2HomePage.CUIR2HomePage)
				.exec(CUIR2Login.CUIR2ClaimantIntentionLogin)
				.exec(CUIR2DocUploadCaseProg.CaseProgUploadDocsByClaimantForFastTrack)
				.pause(10)
				.exec(CUIR2DocUploadCaseProg.viewUploadedDocuments)
				.exec(CUIR2Logout.CUILogout)
				.pause(10)
				
				//below is the defendant upload documents
				.exec(CUIR2HomePage.CUIR2HomePage)
				.exec(CUIR2Login.CUIR2DefLogin)
				.exec(CUIR2DocUploadCaseProg.CaseProgUploadDocsByDefendantForFastTrack)
				.pause(10)
				.exec(CUIR2DocUploadCaseProg.viewUploadedDocuments)
				.exec(CUIR2Logout.CUILogout)
				.pause(10)
				
				//Following is for creating the hearing notice for small claims
				.exec(Homepage.XUIHomePage)
				.exec(Login.XUICenterAdminLogin)
				//YR - 19/05/2025 - Hearing data changed for it to be 4 weeks into future
				.exec(CUIR2CaseProgression.HearingNoticeFastTrack)
				.exec(EXUIMCLogin.manageCase_Logout)
				.pause(10)
				//Following is for paying Hearing Fee
				.exec(CUIR2HomePage.CUIR2HomePage)
				.exec(CUIR2Login.CUIR2ClaimantIntentionLogin)
				.exec(CUIR2DocUploadCaseProg.viewOrderandNoticesForFastTrack)
				.pause(10)
				.exec(CUIR2DocUploadCaseProg.viewUploadedDocumentsForFastTrack)
				.pause(10)
				.exec(CUIR2DocUploadCaseProg.payHearingFee)
				.pause(20)
				//Trial Complete
				.exec(CUIR2DocUploadCaseProg.TrialArrangements)
				.pause(30)
				//Following is for creating the bundle
				.exec(CivilAssignCase.cuibundle)
				.pause(20)
			//	.exec(CUIR2DocUploadCaseProg.viewBundle)
				
				.exec(CUIR2Logout.CUILogout)
				.pause(10)
				
				//Following is For creating the Final Order For fast claims
				.exec(Homepage.XUIHomePage)
				.exec(Login.XUIJudgeLogin)
				.exec(CUIR2CaseProgression.FinalGeneralOrders)
				.exec(EXUIMCLogin.manageCase_Logout)
		}
	

	/*
#######################  CUI R2 Claim Creation Scenario With API ############################################
 */
	
	val CivilUIR2ClaimCreationWithAPIScenario = scenario(" Civil UI R2 Claim Creation with API")
		
		.exitBlockOnFail {
			
			
			//Claim Creation
			/*	exec(CreateUser.CreateDefCitizen)
          .repeat(1) {*/
			exec(CreateUser.CreateClaimantCitizen)
				.exec(CivilAssignCase.AuthForClaimCreationAPI)
				.exec(S2S.s2s())
				
					.exec(CUIClaimCreationWithAPI.getUserId)
				.repeat(1) {
					exec(CUIClaimCreationWithAPI.CreateClaimCUIR2WithAPI)
						.pause(2)
				}
				//YR: Added below code to delete user after use
				//.exec(CUIClaimCreationWithAPI.deleteClaimantUser)
				}


  //defines the test assertions, based on the test type
  def assertions(simulationType: String): Seq[Assertion] = {
    simulationType match {
      case "perftest" =>
        if (debugMode == "off") {
          Seq(global.successfulRequests.percent.gte(95),
            details("CivilCP_FinalOrders_160_FinalOrdersSubmit").successfulRequests.percent.gte(80),
            details("CUICPFT_Claimant_UploadDocs_040_TrialArrangementsSubmit").successfulRequests.percent.gte(80),
            details("CUICPFT_Claimant_ViewDocs_160_ViewBundles").successfulRequests.percent.gte(80)
          )
        }
        else {
          Seq(global.successfulRequests.percent.is(100))
        }
      case "pipeline" =>
        if (debugMode == "off") {
          Seq(global.successfulRequests.percent.gte(95),
            details("CUICPSC_ClaimantIntention_100_CheckAndSubmit").successfulRequests.percent.gte(80),
          )
        }
        else {
          Seq(global.successfulRequests.percent.is(100))
        }
      case _ =>
        Seq()
    }
  }

testType match {
  case "perftest" =>
    setUp(
      CUIR2SmallClaimsCaseProgression.inject(nothingFor(1),rampUsers(150) during (1100)),
      CUIR2FastTrackCaseProgression.inject(nothingFor(50),rampUsers(150) during (1100)),
    ).protocols(httpProtocol)
      .assertions(assertions(testType))
  case "pipeline" =>
    setUp(
      CUIR2SmallClaimsCaseProgression.inject(rampUsers(5) during(15)).pauses(constantPauses)
    ).protocols(httpProtocol)
      .assertions(assertions(testType))
  case _ =>
    exitHere
}

//setUp(
//
//	//*** Regression - Single User - 1 Iteration ***
//	//********************************************************************************
//	//CUIR2SmallClaimsCaseProgression.inject(nothingFor(1),rampUsers(1) during (1)),
//	//CUIR2FastTrackCaseProgression.inject(nothingFor(1),rampUsers(1) during (1))
//	//********************************************************************************
//
//
//	//*** Regression - Full Load ***
//	//********************************************************************************
//	CUIR2SmallClaimsCaseProgression.inject(nothingFor(1),rampUsers(150) during (1100)),
//	CUIR2FastTrackCaseProgression.inject(nothingFor(50),rampUsers(150) during (1100)),
//	//********************************************************************************
//
//
//	//*** Data Inserts - Single User - 1 Iteration ***
//	//********************************************************************************
////	CivilUIR2ClaimCreationWithAPIScenario.inject(nothingFor(1),rampUsers(1) during (1))
//	// ********************************************************************************
//
//
//	//*** Data Inserts - Full Load ***
//	//********************************************************************************
//	//CivilUIR2ClaimCreationWithAPIScenario.inject(nothingFor(1),rampUsers(3600) during (64800))
//	//********************************************************************************
//
//
//).protocols(httpProtocol)
//  .assertions(assertions(testType))

}