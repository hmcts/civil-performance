package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios


import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios.utils.Common

object MakePay  {

   val headers_32 = Map(
  		"Priority" -> "u=0",
  		"Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
  		"Sec-Fetch-Dest" -> "empty",
  		"Sec-Fetch-Mode" -> "cors",
  		"Sec-Fetch-Site" -> "same-origin",
  )
     val headers_33 = Map(
  		"Priority" -> "u=0",
  		"Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
  		"Sec-Fetch-Dest" -> "empty",
  		"Sec-Fetch-Mode" -> "cors",
  		"Sec-Fetch-Site" -> "same-origin",
  )
	val headers_35 = Map(
		"Priority" -> "u=0",
		"Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
		"Sec-Fetch-Dest" -> "empty",
		"Sec-Fetch-Mode" -> "cors",
		"Sec-Fetch-Site" -> "same-origin"
	)
   val headers_34 = Map(
  		"Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
  		"Sec-Fetch-Dest" -> "empty",
  		"Sec-Fetch-Mode" -> "cors",
  		"Sec-Fetch-Site" -> "same-origin",
  		"X-Requested-With" -> "XMLHttpRequest",
  )

   val headers_36 = Map(
  		"Content-Type" -> "application/json",
  		"Origin" -> "https://manage-case.perftest.platform.hmcts.net",
  		"Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
  		"Sec-Fetch-Dest" -> "empty",
  		"Sec-Fetch-Mode" -> "cors",
  		"Sec-Fetch-Site" -> "same-origin",
  		"X-Requested-With" -> "XMLHttpRequest"
  )

  
   val MakePay =
   exec(_.setAll(
		"Idempotencynumber" -> (Common.getIdempotency()),
		"LRrandomString" -> Common.randomString(5))
	)
      // ======================SERVICE TAB======================,
	.group("Civil_CreateClaim_010_MakePayment") {
				exec(http("005_ServiceTab")
					.get("/pay-bulkscan/cases/#{caseId}")
					.headers(headers_32)
					.check(bodyString.saveAs("responseBody")))

					.exec(http("010_PaymentGroups")
						.get("/payments/cases/#{caseId}/paymentgroups")
						.headers(headers_33)
						.check(bodyString.saveAs("responseBody"))
						.check(regex("calculated_amount\":(.*?).00,").saveAs("calculated_amount"))
					)
					.exec { session =>
						// Retrieve a variable from the session and print it
						println("Session2======================================" + session)
						println("Captured response body: " + session("responseBody").as[String])
						session // must return the session object
					}

					.exec(http("015_case_ids")
						.get("/payments/case-payment-orders?case_ids=#{caseId}")
						.headers(headers_34)
						.check(jsonPath("$.content[0].orderReference").optional.saveAs("OrdRefNo"))
						.check(bodyString.saveAs("responseBody"))
					)
			}
      .pause(20)
      // ======================PAY NOW======================,
	.group("Civil_CreateClaim_010_MakePay") {
				exec(http("005_PayNow")
					.get("/payments/pba-accounts")
					.headers(headers_35))
			}
      .pause(20)
      // ======================CONFIRM PAY======================,
	.group("Civil_CreateClaim_010_MakePay") {
				exec(http("005_ConfrmPay")
					.post("/payments/service-request/#{OrdRefNo}/pba-payments")
					.headers(headers_36)
//					.body(ElFileBody("Del/0036_request.json")))
					.body(StringBody(
								"""{
									| "account_number": "PBA0077597",
									| "amount": #{calculated_amount},
									| "currency": "GBP",
									| "customer_reference": "NFT",
									| "idempotency_key": "idam-key-0-#{Idempotencynumber}",
									| "organisation_name": "Civil Damages Claims - Organisation 1"
									|}""".stripMargin
							)).asJson)
			}
      .pause(10)

}
