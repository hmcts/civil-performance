package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object  CivilAssignCase {

	val AssignCase =
				exec(http("CIVIL_AssignCase_000_AssignCase")
					.post("http://civil-service-perftest.service.core-compute-perftest.internal/testing-support/assign-case/#{caseId}/RESPONDENTSOLICITORONE")
					.header("Authorization", "Bearer #{auth_token}")
					.header("Content-Type", "application/json")
					.header("Accept", "*/*")
					.check(status.in(200, 201))
				)
		
	}
