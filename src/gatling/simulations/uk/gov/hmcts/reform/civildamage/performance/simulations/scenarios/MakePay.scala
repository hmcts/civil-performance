package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios


import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils._

object MakePay  {


	val MakePayment =

		// ======================SERVICE TAB======================,
		group("Civil_UnSpecClaim_20_010_CreateCase_PaymentGroups") {
			exec(http("005_PaymentGroups")
				.get("/payments/cases/#{caseId}/paymentgroups")
				.headers(Headers.commonHeader)
				.check(regex("calculated_amount\":(.*?).00,").saveAs("calculated_amount"))
				.check(substring("payment_group_reference")))

			.exec(http("010_ServiceTab")
				.get("/pay-bulkscan/cases/#{caseId}")
				.headers(Headers.commonHeader)
				.check(substring("HMCTS Manage cases")))

			.exec(http("015_Case_Ids")
				.get("/payments/case-payment-orders?case_ids=#{caseId}")
				.headers(Headers.commonHeader)
				.header("csrf-token", "#{csrf}")
				.header("x-requested-with", "xmlhttprequest")
				.check(jsonPath("$.content[0].orderReference").saveAs("OrdRefNo")))
			.exitHereIf(session => !session.contains("OrdRefNo"))
		}
		.pause(2)

		// ======================PAY NOW======================,
		.group("Civil_UnSpecClaim_20_020_CreateCase_PayNow") {
			exec(http("005_PayNow")
				.get("/payments/pba-accounts")
				.headers(Headers.commonHeader)
				.check(substring("paymentAccount")))
		}
		.pause(2)

		// ======================CONFIRM PAY======================,
		.group("Civil_UnSpecClaim_20_030_CreateCase_ConfirmPayment") {
			exec(http("005_ConfirmPay")
				.post("/payments/service-request/#{OrdRefNo}/pba-payments")
				.headers(Headers.commonHeader)
				.header("x-requested-with", "xmlhttprequest")
				.body(StringBody("""{"account_number": "PBA0077597", "amount": #{calculated_amount}, "currency": "GBP",
						|"customer_reference": "NFT", "idempotency_key": "idam-key-#{Idempotencynumber}",
						|"organisation_name": "Civil Damages Claims - Organisation 1"}""".stripMargin))
				.check(substring("success")))
		}
		.pause(60)

}
