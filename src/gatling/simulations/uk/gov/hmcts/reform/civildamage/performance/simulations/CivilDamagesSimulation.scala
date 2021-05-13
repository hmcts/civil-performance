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
    .inferHtmlResources()
    .silentResources
   
  
  
  // below scenario is for user data creation
  val UserCreationScenario = scenario("CMC User Creation")
    .exec(
      CreateUser.CreateCitizen("citizen")
        .pause(20)
    )
  
  //below scenario is to generate claims data
  
  val createClaim = scenario("Civil damage Claim Creation")
   .feed(loginFeeder).feed(sharecaseusersfeed)
    .repeat(1){
			/*
			Step 1: Following Transactions are for , accessing manage case, login as claimant solicitor and create a claim from claimant solicitor and logout
			 */
    exec(EXUIMCLogin.manageCasesHomePage)
      .exec(EXUIMCLogin.manageCaseslogin)
        .exec(ClaimCreation.createclaim)
       .exec(ClaimCreation.startCreateClaim)
			 .exec(ClaimCreation.claimliability)
			.exec(ClaimCreation.claimreferences)
			.exec(ClaimCreation.claimcourt)
			.exec(ClaimCreation.postcode)
			.exec(ClaimCreation.createclaimclaimant)
			.exec(ClaimCreation.createclaimlitigantfriend)
			.exec(ClaimCreation.createclaimantnotifications)
			.exec(ClaimCreation.caseshareorgs)
			.exec(ClaimCreation.claimantsolorganisation)
			.exec(ClaimCreation.postcode1)
			.exec(ClaimCreation.claimdefendant)
			.exec(ClaimCreation.claimLegalRep)
			.exec(ClaimCreation.caseshareorgs1)
			.exec(ClaimCreation.claimdefsolicitororg)
			.exec(ClaimCreation.claimdefsolicitororgemail)
			.exec(ClaimCreation.claimtype)
			.exec(ClaimCreation.createclaimdetail)
			.exec(ClaimCreation.postclaimdocs)
			.exec(ClaimCreation.createclaimupload)
			.exec(ClaimCreation.createclaimvalue)
			.exec(ClaimCreation.createclaimpbanumber)
			.exec(ClaimCreation.createclaimpaymentref)
			.exec(ClaimCreation.createclaimstatementoftruth)
			.exec(ClaimCreation.submitclaimevent)
			.exec(ClaimCreation.casedetailspage)
			.exec(ClaimCreation.getcasedetailspage)
		  	.pause(50)
			.exec(ClaimCreation.notifydefaboutclaim)
		.exec(ClaimCreation.claimnotifyeventcontinue)
		.exec(ClaimCreation.claimnotifyeventsubmit)
		.exec(ClaimCreation.backtocasedetailsafterclaimnotify)
		  	.pause(50)
		.exec(ClaimCreation.notifyclaimdetailsevent)
		.exec(ClaimCreation.notifyclaimdetailsupload)
		.exec(ClaimCreation.notifyclaimdetailseventsubmit)
		.exec(ClaimCreation.returntocasedetailsafternotifydetails)
			.exec(EXUIMCLogin.manageCase_Logout)
		  	.pause(50)
		
      /*
      Step 2: login to manage org as defendant solicitor to assign the case to other users from defendant solicitor firm
      
       */
		   .exec(EXUIMCLogin.manageOrgHomePage)
		 .exec(EXUIMCLogin.manageOrglogin)
		 .exec(EXUI_AssignCase.shareCase)
		 .exec(EXUIMCLogin.manageOrg_Logout)
		  	.pause(30)
		 /*
		 Step 3: login as defendant user  and complete the defendant journey and logout
		 
		  */
			.exec(EXUIMCLogin.manageCasesHomePage)
			.exec(EXUIMCLogin.manageCasesloginToDefendantJourney)
			.exec(DefendantResponse.acknowledgeclaimevent)
			.exec(DefendantResponse.claimConfirmnameandaddress)
			.exec(DefendantResponse.claimconfirmdetails)
			.exec(DefendantResponse.claimresponseintention)
				.exec(DefendantResponse.ackneventsubmit)
			.exec(DefendantResponse.backtocasedetailfromackn)
			.exec(DefendantResponse.defendantresponseevent)
			.exec(DefendantResponse.defresponsetype)
			.exec(DefendantResponse.defdocuments)
			.exec(DefendantResponse.defresponseupload)
			.exec(DefendantResponse.defresponseconfirmnameandaddr)
			.exec(DefendantResponse.defresponseconfirmdetails)
			.exec(DefendantResponse.responsefiledirectoryquestionaire)
			.exec(DefendantResponse.disclosureofelectronicsdoc)
			.exec(DefendantResponse.disclosureofnonelectronicsdoc)
			.exec(DefendantResponse.responsedisclosurereport)
			.exec(DefendantResponse.defresponseexpert)
			.exec(DefendantResponse.defresponsewitness)
			.exec(DefendantResponse.defresponselanguage)
				.exec(DefendantResponse.defresponsehearing)
				.exec(DefendantResponse.defresponsedraftdirections)
				.exec(DefendantResponse.defresponserequestedcourt)
				.exec(DefendantResponse.defresponsehearingsupport)
				.exec(DefendantResponse.responsefurtherinformation)
				.exec(DefendantResponse.defresponsestatementoftruth)
				.exec(DefendantResponse.defresponsesubmit)
				.exec(DefendantResponse.afterdefresponsebacktocasedetails)
			.exec(EXUIMCLogin.manageCase_Logout)
		  	.pause(50)
			
				
			/*
			Step 4: below is the journey for response to defendant by claimant
			
			 */
		  	.exec(EXUIMCLogin.manageCasesHomePage)
				.exec(EXUIMCLogin.manageCaseslogin)
			.exec(ClaimResponseToDefendant.claimantresponseevent)
			.exec(ClaimResponseToDefendant.claimresponsetype)
			.exec(ClaimResponseToDefendant.claimresponsedocument)
			.exec(ClaimResponseToDefendant.claimantresponsefiledirectoryquestionaire)
			.exec(ClaimResponseToDefendant.claimdisclosureofelectronicsdoc)
			.exec(ClaimResponseToDefendant.claiantdisclosureofnonelectronicsdoc)
			.exec(ClaimResponseToDefendant.claimantresponsedisclosurereport)
			.exec(ClaimResponseToDefendant.clamantdefresponseexpert)
			.exec(ClaimResponseToDefendant.claimantdefresponsewitness)
			.exec(ClaimResponseToDefendant.claimantdefresponselanguage)
			.exec(ClaimResponseToDefendant.claimantdefresponsehearing)
			.exec(ClaimResponseToDefendant.claimantresponsedocuments)
			.exec(ClaimResponseToDefendant.claimantdefresponsedraftdirections)
			.exec(ClaimResponseToDefendant.claimantdefresponsehearingsupport)
			.exec(ClaimResponseToDefendant.claimantresponsefurtherinformation)
			.exec(ClaimResponseToDefendant.claimantdefresponsestatementoftruth)
			.exec(ClaimResponseToDefendant.claimantdefresponsesubmit)
			.exec(ClaimResponseToDefendant. claimantafterdefresponsesearchforcompletable)
				.exec(EXUIMCLogin.manageCase_Logout)
		  	.pause(50)
    }
	
  setUp(
    createClaim.inject(nothingFor(1),rampUsers(100) during (1200))
  ).protocols(httpProtocol)
  
  
}