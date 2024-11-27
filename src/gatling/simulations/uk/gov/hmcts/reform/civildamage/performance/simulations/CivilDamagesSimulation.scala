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
  
  val BaseURL = Environment.baseURL
  val loginFeeder = csv("login.csv").circular
	val cpLoginFeeder = csv("cuir2cplogin.csv").circular
	val stFeeder = csv("loginSt.csv").circular
	val defresponsecasesFeeder=csv("caseIds.csv").circular
	val sol7casesFeeder=csv("caseIdsSol7.csv").circular
	val sol8casesFeeder=csv("caseIdsSol8.csv").circular
	val defresponsecasesTrialFeeder=csv("caseIdsTrial.csv").circular
	val casesfordefresponseFeeder=csv("CivilCreatedCaseIds.csv").circular
	val sol7casesTrialFeeder=csv("caseIdsSol7Trial.csv").circular
	val sol8casesTrialFeeder=csv("caseIdsSol8Trial.csv").circular
	val pbacasesfeeder=csv("pbacases.csv").circular
	val assigncasesFeeder=csv("assigncasesfeeder.csv").circular
	val sdoRFRFeeder=csv("sdorfrcases.csv").circular
	val viewandresponsefeeder = csv("viewandresponsecases.csv").circular
	val cpfulltestsmallclaimsFeeder=csv("cuir2cpsmallclaims.csv").circular
	val cpfulltestfasttrackFeeder=csv("cuir2cpfasttrack.csv").circular
	val updateSubmitDateForIntermediateMulti=csv("updatesubmitdateimi.csv").circular
	
	
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
				.exec(CivilAssignCase.cuiassign)
				.exec(Logout.XUILogout)
				/*
			defendant and clamant intent journey for specidied cases starts here
				 */
			/*	.exec(EXUIMCLogin.manageCasesHomePage)
        .exec(EXUIMCLogin.manageCasesloginToDefendantJourney)
        .exec(SpecifiedDefAndClaimantResponse.RespondToClaim)
        .exec(EXUIMCLogin.manageCase_Logout)
        .pause(20)
        .exec(Homepage.XUIHomePage)
        .exec(Login.XUILogin)
        .exec(SpecifiedDefAndClaimantResponse.RespondToDefence)
        .exec(EXUIMCLogin.manageCase_Logout)*/
			
		}
	
	
	
	
	//below scenario is to generate claims data for Request For Reconsider
	
	val RequestForReConsiderScenario = scenario("Create Civil UI Claim")
		.feed(loginFeeder)
		.exitBlockOnFail {
			//CUI claim creation
			exec(Homepage.XUIHomePage)
				.exec(Login.XUILogin)
				.exec(CUIClaimCreationForSmallClaimDRH.run)
				// PBS payment
				.exec(CUIClaimCreation.PBSPayment)
				.pause(50)
				.exec(CivilAssignCase.run)
				.exec(Logout.XUILogout)
				/*
      defendant and clamant intent journey for specidied cases starts here
         */
				.exec(EXUIMCLogin.manageCasesHomePage)
				.exec(EXUIMCLogin.manageCasesloginToDefendantJourney)
				.exec(SpecifiedDefAndClaimantResponseDRH.RespondToClaim)
				.exec(EXUIMCLogin.manageCase_Logout)
				.pause(20)
				.exec(Homepage.XUIHomePage)
				.exec(Login.XUILogin)
				.exec(SpecifiedDefAndClaimantResponseDRH.RespondToDefence)
				.exec(EXUIMCLogin.manageCase_Logout)
			
		}
	
	
	//below scenario is to generate claims data for Request For Reconsider
	
	val ClaimCreationDRHScenario = scenario("Create Civil UI Claim")
		.feed(loginFeeder)
		.exitBlockOnFail {
			//CUI claim creation
			exec(Homepage.XUIHomePage)
				.exec(Login.XUILogin)
				.exec(CUIClaimCreationForSmallClaimDRH.run)
				// PBS payment
				.exec(CUIClaimCreation.PBSPayment)
				.pause(50)
				.exec(CivilAssignCase.run)
				.exec(Logout.XUILogout)
				/*
      defendant and clamant intent journey for specidied cases starts here
         */
				.exec(EXUIMCLogin.manageCasesHomePage)
				.exec(EXUIMCLogin.manageCasesloginToDefendantJourney)
				.exec(SpecifiedDefAndClaimantResponseDRH.RespondToClaim)
				.exec(EXUIMCLogin.manageCase_Logout)
				.pause(20)
				.exec(Homepage.XUIHomePage)
				.exec(Login.XUILogin)
				.exec(SpecifiedDefAndClaimantResponseDRH.RespondToDefence)
				.exec(EXUIMCLogin.manageCase_Logout)
			
		}
	
	//just to test pba payment
	
	val PBAServiceScenario = scenario("PBA Service")
		.feed(loginFeeder).feed(pbacasesfeeder)
		.exitBlockOnFail {
			//CUI claim creation
			exec(Homepage.XUIHomePage)
				.exec(Login.XUILogin)
			
				// PBS payment
				.exec(CUIClaimCreation.PBSPayment)
				.pause(50)
			/*	.exec(CivilAssignCase.run)
				.exec(Logout.XUILogout)
				/*
      defendant and clamant intent journey for specidied cases starts here
         */
				.exec(EXUIMCLogin.manageCasesHomePage)
				.exec(EXUIMCLogin.manageCasesloginToDefendantJourney)
				.exec(SpecifiedDefAndClaimantResponse.RespondToClaim)
				.exec(EXUIMCLogin.manageCase_Logout)
				.pause(20)
				.exec(Homepage.XUIHomePage)
				.exec(Login.XUILogin)
				.exec(SpecifiedDefAndClaimantResponse.RespondToDefence)
				.exec(EXUIMCLogin.manageCase_Logout)*/
			
		}
	
	//below is for flight delay claim
	
	val FlightDelayClaimCreationScenario = scenario("Create Flight Delay Claim")
		.feed(loginFeeder)
		.exitBlockOnFail {
			//CUI claim creation
			exec(Homepage.XUIHomePage)
				.exec(Login.XUILogin)
				.exec(FlightDelaySpecifiedClaimCreation.run)
				.pause(50)
				/*
				below are the alternative payment
				 */
				/*	.exec(S2S.s2s("ccd_data"))
				.exec(IdamLogin.GetIdamToken)
					.exec(S2S.s2s("xui_webapp"))
					.exec(S2S.s2s("civil_service"))
					.exec(IdamLogin.GetIdamTokenPayments)
				.exec(ClaimCreationLRvsLR.civilAddPayment)*/
				
				
				// PBS payment
				.exec(FlightDelaySpecifiedClaimCreation.PBSPayment)
				.pause(50)
				.exec(CivilAssignCase.run)
				.exec(Logout.XUILogout)
				/*
				following are for defendant response
				 */
				.exec(EXUIMCLogin.manageCasesHomePage)
				.exec(EXUIMCLogin.manageCasesloginToDefendantJourney)
				.exec(SpecifiedDefAndClaimantResponse.RespondToClaim)
				.exec(EXUIMCLogin.manageCase_Logout)
			//	.pause(20)
			feed(viewandresponsefeeder)
				.exec(Homepage.XUIHomePage)
				.exec(Login.XUILogin)
				.exec(SpecifiedDefAndClaimantResponse.RespondToDefence)
				.exec(EXUIMCLogin.manageCase_Logout)
		}
	
	val SpecifiedClaimAndIntentScenario = scenario("Def And Intent")
		.feed(loginFeeder).feed(defresponsecasesFeeder)
		.exitBlockOnFail {
			//CUI claim creation
		
				/*
        following are for defendant response
         */
				exec(EXUIMCLogin.manageCasesHomePage)
				.exec(EXUIMCLogin.manageCasesloginToDefendantJourney)
				.exec(SpecifiedDefAndClaimantResponse.RespondToClaim)
				.exec(EXUIMCLogin.manageCase_Logout)
				.pause(20)
			//feed(viewandresponsefeeder)
				.exec(Homepage.XUIHomePage)
				.exec(Login.XUILogin)
				.exec(SpecifiedDefAndClaimantResponse.RespondToDefence)
				.exec(EXUIMCLogin.manageCase_Logout)
		}
	
	val SpecifiedMultiTrackClaimAndIntentScenario = scenario("Def And Intent For Multi Track")
		.feed(loginFeeder).feed(defresponsecasesFeeder)
		.exitBlockOnFail {
			//CUI claim creation
			
			/*
      following are for defendant response
       */
			exec(EXUIMCLogin.manageCasesHomePage)
				.exec(EXUIMCLogin.manageCasesloginToDefendantJourney)
				.exec(SpecifiedMultiTrackDefAndClaimantResponse.RespondToClaim)
				.exec(EXUIMCLogin.manageCase_Logout)
				.pause(20)
				//feed(viewandresponsefeeder)
				exec(Homepage.XUIHomePage)
				.exec(Login.XUILogin)
				.exec(SpecifiedMultiTrackDefAndClaimantResponse.RespondToDefence)
				.exec(EXUIMCLogin.manageCase_Logout)
				
					//below is the defendant upload documents
					.exec(CUIR2HomePage.CUIR2HomePage)
					.exec(CUIR2Login.CUIR2DefLogin)
					.exec(CUIR2DocUploadCaseProg.CaseProgUploadDocsByDefendantForFastTrack)
					.pause(10)
					.exec(CUIR2DocUploadCaseProg.viewUploadedDocumentsForFastTrack)
					.exec(CUIR2Logout.CUILogout)
					.pause(10)
					
					//Following is for creating the hearing notice for small claims
					.exec(Homepage.XUIHomePage)
					.exec(Login.XUICenterAdminLogin)
					.exec(CUIR2CaseProgression.HearingNoticeFastTrack)
					.exec(EXUIMCLogin.manageCase_Logout)
					.pause(10)
					
			
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
	#######################  CUI R2 Claim Creation ############################################
	 */
	
	val CivilUIR2ClaimCreationScenario = scenario(" Civil UI R2 Claim Creation")
		
		.exitBlockOnFail {
			//Claim Creation
			
			exec(CUIR2HomePage.CUIR2HomePage)
			.exec(CUIR2Login.CUIR2Login)
			
				//claimant intention
			/*	.exec(Homepage.XUIHomePage)
				.exec(Login.XUILogin)
				.exec(ClaimantIntention.claimantintention)
				.pause(20)
				.exec(Logout.XUILogout)*/
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
	
	/*======================================================================================
  Below scenario is for creating the test data for GA Application
  ======================================================================================*/
	val CivilDamageScenario = scenario("Create Civil Cases For GA to Test")
		.feed(loginFeeder)
		.repeat(1) {
			exitBlockOnFail {
				feed(defresponsecasesFeeder)
				.exec(EXUIMCLogin.manageCasesHomePage)
					.exec(EXUIMCLogin.manageCaseslogin)
					.exec(ClaimCreationLRvsLR.run)
				.exec(CivilAssignCase.run)
			
			}
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
		//	.exec(EXUIMCLogin.manageCasesloginToCentreAdminJourney)
	//		.doSwitch("#{claimantuser}")(
	//			"civil.damages.claims+organisation.1.solicitor.1@gmail.com" -> feed(defresponsecasesFeeder),
	//			"hmcts.civil+organisation.1.solicitor.7@mailinator.com" -> feed(sol7casesFeeder),
	//			"hmcts.civil+organisation.1.solicitor.8@mailinator.com" -> feed(sol8casesFeeder)
	//		)
		//	.exec(CaseProgression.HearingNotice)
		//	.exec(EXUIMCLogin.manageCase_Logout)

				//		}
				//	.exitBlockOnFail {

			//	.exec(EXUIMCLogin.manageCasesHomePage)
			//	.exec(EXUIMCLogin.manageCasesloginToDefendantJourney)
		//		.exec(CaseProgression.EvidenceUploadDefendant)
		//		.exec(EXUIMCLogin.manageCase_Logout)
				//}

				//	.exitBlockOnFail {
				.exec(EXUIMCLogin.manageCasesHomePage)
				.exec(EXUIMCLogin.manageCaseslogin)
		//		.exec(CaseProgression.EvidenceUploadClaimant)

		//		.exec(CaseProgression.CaseFileView)

		//		.exec(CaseProgression.HearingFee)

		//		.exec(CaseProgression.BundleCreationIntegration)
							.doSwitch("#{claimantuser}")(
				"civil.damages.claims+organisation.1.solicitor.1@gmail.com" -> feed(defresponsecasesTrialFeeder),
				"hmcts.civil+organisation.1.solicitor.7@mailinator.com" -> feed(sol7casesTrialFeeder),
				"hmcts.civil+organisation.1.solicitor.8@mailinator.com" -> feed(sol8casesTrialFeeder)
							)
		//		.exec(CaseProgression.TrialReadiness)

				.exec(EXUIMCLogin.manageCase_Logout)
				//	}


				//	.exitBlockOnFail {
				.exec(EXUIMCLogin.manageCasesHomePage)
				.exec(EXUIMCLogin.manageCasesloginToJudgeJourney)
				.exec(CaseProgression.JudgeCaseNotes)
		//		.exec(CaseProgression.FinalGeneralOrders)
				.exec(EXUIMCLogin.manageCase_Logout)

		}

		.exec {
			session =>
				println(session)
				session
		}
	
	/*======================================================================================
  * Below scenario is for cases that are data prep for hearing management.
  ======================================================================================*/
	val CivilCaseDataPrep = scenario("Create Civil damage")
		.feed(loginFeeder).feed(casesfordefresponseFeeder)
		.exitBlockOnFail {
			exec(_.set("env", s"${env}"))
			.exec(Homepage.XUIHomePage)
				.exec(Login.XUILogin)
				.exec(ClaimCreationLRvsLR.run)
			//	.exec(S2S.s2s("ccd_data"))
				//.exec(IdamLogin.GetIdamToken)
		//	.exec(S2S.s2s("xui_webapp"))
			//	.exec(S2S.s2s("civil_service"))
			//	.exec(IdamLogin.GetIdamTokenPayments)
				/*.exec(ClaimCreationLRvsLR.addPBAPayment)
				.pause(50)
				.exec(ClaimCreationLRvsLR.notifyClaim)
				.exec(CivilAssignCase.run)
				.exec(EXUIMCLogin.manageCase_Logout)*/
			//	feed(assigncasesFeeder)
			/*	.exec(EXUIMCLogin.manageCasesHomePage)
				.exec(EXUIMCLogin.manageCasesloginToDefendantJourney)
				.exec(ClaimCreationLRvsLR.RespondToClaim)
				.exec(EXUIMCLogin.manageCase_Logout)
				.pause(20)*/
				/*.exec(Homepage.XUIHomePage)
				.exec(Login.XUILogin)
				.exec(ClaimCreationLRvsLR.RespondToDefence)
			.exec(EXUIMCLogin.manageCase_Logout)*/
			/*	.exec(EXUIMCLogin.manageCasesHomePage)
				.exec(EXUIMCLogin.manageCasesloginToJudgeJourney)
				.exec(ClaimCreationLRvsLR.SDO)*/
		}
	
	
	/*======================================================================================
* Below scenario is for cases that are data prep for hearing management.
======================================================================================*/
	val UnSpecIntermediateTrack = scenario("Create Intermediate Track")
		.feed(loginFeeder).feed(casesfordefresponseFeeder)
		.exitBlockOnFail {
			exec(_.set("env", s"${env}"))
				//un spec intermediate claim creation steps
				.exec(Homepage.XUIHomePage)
				.exec(Login.XUILogin)
				.exec(UnspecIntermediateTrack.run)
				
				// adding apymnt through pba
			.exec(UnspecIntermediateTrack.addPBAPayment)
      .pause(50)
      .exec(UnspecIntermediateTrack.notifyClaim)
				
				// assign case
				
      .exec(CivilAssignCase.cuiassign)
				.pause(10)
      .exec(EXUIMCLogin.manageCase_Logout)
				// intermediate track unspec defendant journey
				
			.exec(EXUIMCLogin.manageCasesHomePage)
        .exec(EXUIMCLogin.manageCasesloginToDefendantJourney)
        .exec(UnspecIntermediateTrack.RespondToClaim)
        .exec(EXUIMCLogin.manageCase_Logout)
        .pause(20)
				
				// intermediate track unspec claimant intent
			.exec(Homepage.XUIHomePage)
      .exec(Login.XUILogin)
      .exec(UnspecIntermediateTrack.RespondToDefence)
    .exec(EXUIMCLogin.manageCase_Logout)
				.pause(120)
				
				//Following is for creating the transfer  online
				.exec(Homepage.XUIHomePage)
				.exec(Login.XUICenterAdminLogin)
				.pause(10)
				.exec(UnSpecIntermediateTrackCaseProgression.TransferOnlineByHearingAdmin)
				.exec(EXUIMCLogin.manageCase_Logout)
				.pause(20)
				
				//Following is for make an order
				.exec(Homepage.XUIHomePage)
				.exec(Login.XUIJudgeLogin)
				.exec(UnSpecIntermediateTrackCaseProgression.MakeAnOrder)
				.exec(EXUIMCLogin.manageCase_Logout)
				.pause(20)
				
				//Following is for upload documents
				.exec(Homepage.XUIHomePage)
				.exec(Login.XUILogin)
				.exec(UnSpecIntermediateTrackCaseProgression.EvidenceUploadClaimant)
				.exec(EXUIMCLogin.manageCase_Logout)
				.pause(20)
				
				// Follwing is for Hearing Notice
				.exec(Homepage.XUIHomePage)
				.exec(Login.XUICenterAdminLogin)
				.exec(UnSpecIntermediateTrackCaseProgression.HearingNotice)
				.exec(EXUIMCLogin.manageCase_Logout)
				.pause(20)
			
			
		}
	
	
	//below scenario is to generate claims data for GA process
	
	val SpecMultiTrack = scenario("Spec Multi Track End to End Journey")
		.feed(loginFeeder)//.feed(defresponsecasesFeeder)
		.exitBlockOnFail {
			//CUI claim creation
			exec(Homepage.XUIHomePage)
				.exec(Login.XUILogin)
				.exec(CUIClaimCreationForMultiTrack.run)
				// PBS payment
				.exec(CUIClaimCreationForMultiTrack.PBSPayment)
				.pause(50)
				.exec(CivilAssignCase.cuiassign)
				.pause(10)
				.exec(Logout.XUILogout)
			// below is the defendant jourey multi claim
			.exec(EXUIMCLogin.manageCasesHomePage)
				.exec(EXUIMCLogin.manageCasesloginToDefendantJourney)
				.exec(SpecifiedMultiTrackDefAndClaimantResponse.RespondToClaim)
				.exec(EXUIMCLogin.manageCase_Logout)
				.pause(20)
			// This is the multi claim response to defendant
			.exec(Homepage.XUIHomePage)
				.exec(Login.XUILogin)
				.exec(SpecifiedMultiTrackDefAndClaimantResponse.RespondToDefence)
				.exec(EXUIMCLogin.manageCase_Logout)
				.pause(100)
				
				//Following is for creating the transfer  online
			/*	.exec(Homepage.XUIHomePage)
				.exec(Login.XUICenterAdminLogin)
				.pause(10)
				.exec(SpecMultiTrackCaseProgression.TransferOnlineByHearingAdmin)
				.exec(EXUIMCLogin.manageCase_Logout)
				.pause(10)*/
				
				//Following is for make an order
				.exec(Homepage.XUIHomePage)
				.exec(Login.XUIJudgeLogin)
				.exec(SpecMultiTrackCaseProgression.SpecMultiTrackMakeAnOrder)
				.exec(EXUIMCLogin.manageCase_Logout)
				.pause(20)
				
				//Following is for upload documents
				.exec(Homepage.XUIHomePage)
				.exec(Login.XUILogin)
				.exec(SpecMultiTrackCaseProgression.EvidenceUploadClaimant)
				.exec(EXUIMCLogin.manageCase_Logout)
				.pause(20)
				
				// Follwing is for Hearing Notice
				.exec(Homepage.XUIHomePage)
				.exec(Login.XUICenterAdminLogin)
				.exec(SpecMultiTrackCaseProgression.HearingNotice)
				.exec(EXUIMCLogin.manageCase_Logout)
				.pause(10)
		}
	
	
	/*======================================================================================
* Below scenario is for cases that are data prep for hearing management.
======================================================================================*/
	val UpdateSubmitDateFromUnSpecClaimCreation = scenario("Update Submit Date")
		.feed(loginFeeder) .feed(updateSubmitDateForIntermediateMulti)
		.exitBlockOnFail {
			
			exec(S2S.s2s("civil_service"))
			.exec(UnspecIntermediateTrack.UnSpecClaimUpdateWithSubmittedDate)
			
		}
	
	
	/*======================================================================================
* Below scenario is for cases that are data prep for hearing management.
======================================================================================*/
	val CivilCaseAssignScenario = scenario("Civil Case Assign")
		.feed(assigncasesFeeder)
		.exitBlockOnFail {
			exec(_.set("env", s"${env}"))
				.exec(CivilAssignCase.run)
		}
	
	/*======================================================================================
* Below scenario is for SDO Enhancements Fast Track
======================================================================================*/
	val SDOEnhancementsFastTrack = scenario("SDO Enhancements Fast Track")
		.feed(loginFeeder) //.feed(casesfordefresponseFeeder)
		.exitBlockOnFail {
			exec(_.set("env", s"${env}"))
				.exec(Homepage.XUIHomePage)
				.exec(Login.XUIJudgeLogin)
				.exec(SDO.SDOEnhancementFastTrack)
				.exec(EXUIMCLogin.manageCase_Logout)
			
		}
	
	/*======================================================================================
* Below scenario is for SDO Enhancements Fast Track  - Flight Delay
======================================================================================*/
	val SDOEnhancementsFlightDelay = scenario("SDO Enhancements Flight Delay")
		.feed(loginFeeder) //.feed(casesfordefresponseFeeder)
		.exitBlockOnFail {
			exec(_.set("env", s"${env}"))
				.exec(Homepage.XUIHomePage)
				.exec(Login.XUIJudgeLogin)
				.exec(SDO.SDOFlightDelay)
				.exec(EXUIMCLogin.manageCase_Logout)
			
		}
	
	
	/*======================================================================================
* Below scenario is for SDO Enhancements Small Claims - DRH
======================================================================================*/
	val SDOEnhancementsDRH = scenario("SDO Enhancements DRH")
		.feed(loginFeeder) //.feed(casesfordefresponseFeeder)
		.exitBlockOnFail {
			exec(_.set("env", s"${env}"))
				.exec(Homepage.XUIHomePage)
				.exec(Login.XUIJudgeRegion4Login)
				.exec(SDO.SDOSmallClaimsForDRH)
				.exec(EXUIMCLogin.manageCase_Logout)
			
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
	val CUIR2SmallClaimsCaseProgression = scenario("SDO For CUIR2 CaseProgression Small Claims")
		.feed(cpLoginFeeder) .feed(cpfulltestsmallclaimsFeeder)
		.exitBlockOnFail {
			exec(_.set("env", s"${env}"))
				//Below is for upload claimant evidence
					exec(CUIR2HomePage.CUIR2HomePage)
            .exec(CUIR2Login.CUIR2ClaimantIntentionLogin)
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
					//	.exec(CUIR2DocUploadCaseProg.viewBundleForSmallClaims)
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
		.feed(cpLoginFeeder).feed(cpfulltestfasttrackFeeder)
		.exitBlockOnFail {
			exec(_.set("env", s"${env}"))
			//Below is for upload claimant evidence
			.exec(CUIR2HomePage.CUIR2HomePage)
				.exec(CUIR2Login.CUIR2ClaimantIntentionLogin)
				.exec(CUIR2DocUploadCaseProg.CaseProgUploadDocsByClaimantForFastTrack)
				.pause(10)
				.exec(CUIR2DocUploadCaseProg.viewUploadedDocumentsForFastTrack)
				.exec(CUIR2Logout.CUILogout)
				.pause(10)
				
				//below is the defendant upload documents
				.exec(CUIR2HomePage.CUIR2HomePage)
				.exec(CUIR2Login.CUIR2DefLogin)
				.exec(CUIR2DocUploadCaseProg.CaseProgUploadDocsByDefendantForFastTrack)
				.pause(10)
				.exec(CUIR2DocUploadCaseProg.viewUploadedDocumentsForFastTrack)
				.exec(CUIR2Logout.CUILogout)
				.pause(10)
				
				//Following is for creating the hearing notice for small claims
				.exec(Homepage.XUIHomePage)
				.exec(Login.XUICenterAdminLogin)
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
	
	/*======================================================================================
* Below scenario is for SDO Request For Re Consider -
======================================================================================*/
	val SDORequestForReConsider = scenario("SDO Request For Reconsider")
		.feed(loginFeeder) .feed(sdoRFRFeeder)
		.exitBlockOnFail {
			exec(_.set("env", s"${env}"))
				//below login as tribunal user for region 4
				.exec(Homepage.XUIHomePage)
				.exec(Login.XUITribunalLogin)//this user is for sdo region 4 tribunal user which is ia requirement for request for reconsider
				.exec(SDO.SDORequestForReConsiderByTribunal)
				.exec(EXUIMCLogin.manageCase_Logout)
			
				//again login as claimant and request for re consider
				.exec(Homepage.XUIHomePage)
				.exec(Login.XUILogin)
				.exec(SDO.SDORequestForReConsiderFromClaimant)
				.exec(EXUIMCLogin.manageCase_Logout)
				.pause(20)
				
			// again login as judge and complete
				.exec(Homepage.XUIHomePage)
				.exec(Login.XUIJudgeRegion4Login)
				.exec(SDO.SDODecisionOnRequestForReConsiderByJudge)
				.pause(20)
				.exec(SDO.SDORequestForReConsiderByJudge)
				.exec(EXUIMCLogin.manageCase_Logout)
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
		//Below are all SDO Enhancements scenarios
		/*SDOEnhancementsFastTrack.inject(nothingFor(10),rampUsers(15) during (3600)),
		SDOEnhancementsFlightDelay.inject(nothingFor(50),rampUsers(15) during (3600)),
		SDOEnhancementsDRH.inject(nothingFor(100),rampUsers(15) during (3600)),
		SDORequestForReConsider.inject(nothingFor(150),rampUsers(12) during (3600))*/
		// Below is for creating small claims on CUIR2
		
	//	SDOSmallClaimsCUIR2.inject(nothingFor(1),rampUsers(1) during (1)),
		//SDOFastTrackCUIR2.inject(nothingFor(1),rampUsers(3) during (50)),
	//	CUIR2SmallClaimsCaseProgression.inject(nothingFor(1),rampUsers(14) during (3600)),
//		CUIR2FastTrackCaseProgression.inject(nothingFor(50),rampUsers(14) during (3600)),
		//	CivilUIR2ClaimCreationScenario.inject(nothingFor(1),rampUsers(1) during (1))
		
		//Following is the spec claim end to end journey
	//	CivilUIClaimCreationScenario.inject(nothingFor(1),rampUsers(180) during (3600))
	//	SpecifiedClaimAndIntentScenario.inject(nothingFor(1),rampUsers(89) during (1800))
		
	//		PBAServiceScenario.inject(nothingFor(1),rampUsers(1) during (1))
	//	CivilCaseAssignScenario.inject(nothingFor(1),rampUsers(81) during (300))
	//		RequestForReConsiderScenario.inject(nothingFor(1),rampUsers(25) during (1800))
		//		ClaimCreationDRHScenario.inject(nothingFor(1),rampUsers(20) during (1200))
	//	FlightDelayClaimCreationScenario.inject(nothingFor(1),rampUsers(25) during (1800))
		//CivilUIClaimCreationScenario.inject(nothingFor(5),rampUsers(90) during (3600)),
		//	CivilUIDefAndIntentScenario.inject(nothingFor(30),rampUsers(2) during (50))
	//	CivilCaseProg.inject(nothingFor(5),rampUsers(1) during (650))
		//CivilCaseProg.inject(nothingFor(1),rampUsers(12) during (2700))
	//CivilCaseDataPrep.inject(nothingFor(1),rampUsers(1) during (6))
	//	UpdateSubmitDateFromUnSpecClaimCreation.inject(nothingFor(1),rampUsers(1) during (6))
			UnSpecIntermediateTrack.inject(nothingFor(1),rampUsers(30) during (3600)),
			SpecMultiTrack.inject(nothingFor(100),rampUsers(30) during (3600)),
	//	SpecifiedMultiTrackClaimAndIntentScenario.inject(nothingFor(30),rampUsers(3) during (50))
	//	STCitizen.inject(nothingFor(1),rampUsers(1) during (2700))
	//	CivilDamageScenario.inject(nothingFor(1),rampUsers(1) during (2))
).protocols(httpProtocol)
	
	/*setUp(
		CivilUIClaimCreationScenario.inject(simulationProfile(testType, claimsTargetPerHour, numberOfPipelineUsers)).pauses(pauseOption)
		//CivilUIDefAndIntentScenario.inject(simulationProfile(testType, defResponseAndIntentTargetPerHour, numberOfPipelineUsers)).pauses(pauseOption)
	).protocols(httpProtocol)*/
	
}