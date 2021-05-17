package uk.gov.hmcts.reform.civildamage.performance.simulations

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import uk.gov.hmcts.reform.civildamage.performance.scenarios._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.Environment


class CivilDamagesSimulation extends Simulation {
  
  val BaseURL = Environment.baseURL
  val loginFeeder = csv("login.csv").circular
  val defendantloginFeeder = csv("defendantlogin.csv").circular
  val claimcreatedefuserFeeder = csv("claimcreatedeflogin.csv").circular
	val sharecaseusersfeed=csv("sharecaseusers.csv").random
  val httpProtocol = Environment.HttpProtocol
    .baseUrl(BaseURL)
    //.doNotTrackHeader("1")
    //.inferHtmlResources()
    //.silentResources
	implicit val postHeaders: Map[String, String] = Map(
		"Origin" -> BaseURL
	)
  
  
  // below scenario is for user data creation
  val UserCreationScenario = scenario("CMC User Creation")
    .exec(
      CreateUser.CreateCitizen("citizen")
        .pause(20)
    )
  
  //below scenario is to generate claims data
	
	
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
		
	
	
	
	
	
	
	
	










setUp(
	CivilDamageScenario.inject(nothingFor(1),rampUsers(3) during (100))
).protocols(httpProtocol)


}