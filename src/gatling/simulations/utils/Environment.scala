package utils

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object Environment {

  val httpConfig = scala.util.Properties.envOrElse("httpConfig", "http")
  val baseURL = "https://manage-case.perftest.platform.hmcts.net"
  val citizenURL = "https://civil-citizen-ui.perftest.platform.hmcts.net"
  val PaymentURL = scala.util.Properties.envOrElse("paymentURL", "https://card.payments.service.gov.uk")
  val idamURL = "https://idam-web-public.perftest.platform.hmcts.net"
  val idamAPIURL = "https://idam-api.perftest.platform.hmcts.net"
  val manageCaseURL = "https://manage-case.perftest.platform.hmcts.net"

  val HttpProtocol = http

  val minThinkTime = 5 //8
  val maxThinkTime = 8 //10

}