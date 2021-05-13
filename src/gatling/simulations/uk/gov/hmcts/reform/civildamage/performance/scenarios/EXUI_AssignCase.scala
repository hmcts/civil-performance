package uk.gov.hmcts.reform.civildamage.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.scenarios.utils.{AssignCase_Header, Environment}

object EXUI_AssignCase {

	val minThinkTime = Environment.minThinkTime
	val maxThinkTime = Environment.maxThinkTime

	val manageOrgURL=Environment.manageOrgURL
	
	/*======================================================================================
*Business process : As part of the create FR respondent application share a case
* Below group contains all the share the unassigned case
======================================================================================*/
	val shareCase =
		group("CD_ShareACase_030_UnassignedCases") {
			exec(http("CivilDamages_030_UnassignedCases")
				.post(manageOrgURL + "/api/unassignedCaseTypes")
				.headers(AssignCase_Header.headers_37)
				.check(status in(200, 304)))
		}
		
		.group("CD_ShareACase_040_FRConsented") {
			exec(http("CD_ShareACase_040_FRConsented")
				.post(manageOrgURL + "/api/unassignedcases?caseTypeId=UNSPECIFIED_CLAIMS")
				.headers(AssignCase_Header.headers_38)
				.check(status in(200, 304)))
		}
		.pause(minThinkTime, maxThinkTime)
		
	
/*======================================================================================
*Business process : As part of the create FR Respondent application share a case
* Below group contains all the share the unassigned case by caseId
======================================================================================*/
		
		
		.group("CD_ShareACase_050_ShareCaseByCaseId") {
			exec(http("CD_ShareACase_050_ShareCaseByCaseId")
				.get(manageOrgURL + "/api/caseshare/cases?case_ids=${caseId}")
				.headers(AssignCase_Header.headers_9)
				.check(status in(200, 304)))
		}
		.pause(minThinkTime, maxThinkTime)
	
/*======================================================================================
*Business process : As part of the create FR Respondent application share a case
* Below group contains all the share the unassigned case- share case users
======================================================================================*/
		.group("CD_ShareACase_060_ShareCaseUsers") {
			exec(http("CD_ShareACase_060_ShareCaseUsers")
				.get(manageOrgURL + "/api/caseshare/users")
				.headers(AssignCase_Header.headers_9)
				/*.check(jsonPath("$..email").find(1).saveAs("email"))
				.check(jsonPath("$..firstName").find(1).saveAs("firstName"))
				.check(jsonPath("$..idamId").find(1).saveAs("idamId"))
				.check(jsonPath("$..lastName").find(1).saveAs("lastName"))*/
				.check(status in(200, 304)))
		}
		.pause(minThinkTime, maxThinkTime)
	
/*======================================================================================
*Business process : As part of the create FR Respondent application share a case
* Below group contains all the share the unassigned case-final assignment of share a case
======================================================================================*/
		
		.group("CD_ShareACase_070_ShareCaseAssignments") {
			exec(http("CD_ShareACase_ShareCaseAssignments")
				.post(manageOrgURL + "/api/caseshare/case-assignments")
				.headers(AssignCase_Header.headers_54)
				.body(StringBody(
					"""{
																	 |    "sharedCases":
																	 |    [
																	 |        {"caseId":"${caseId}",
																	 |        "caseTitle":"${caseId}",
																	 |        "caseTypeId":"UNSPECIFIED_CLAIMS",
																	 |        "pendingShares":
																	 |        [
																	 |            {"email":"${email}",
																	 |            "firstName":"${firstName}",
																	 |            "idamId":"${idamId}",
																	 |            "lastName":"${lastName}"}
																	 |        ],
																	 |        "pendingUnshares":[]}
																	 |    ]
																	 |}""".stripMargin))
				.check(status in(201, 304)))
		}
		.pause(minThinkTime, maxThinkTime)
		
}
