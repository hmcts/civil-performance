package utils

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object Environment {

  val httpConfig = scala.util.Properties.envOrElse("httpConfig", "http")
  val baseURL = "https://manage-case.#{env}.platform.hmcts.net"
  val citizenURL = "https://civil-citizen-ui.perftest.platform.hmcts.net"
  val baseDomain = scala.util.Properties.envOrElse("baseDomain", "manage-case.#{env}.platform.hmcts.net")
  val PaymentURL = scala.util.Properties.envOrElse("paymentURL", "https://card.payments.service.gov.uk")
  val idamURL = "https://idam-web-public.#{env}.platform.hmcts.net"
  val idamAPIURL = "https://idam-api.#{env}.platform.hmcts.net"
  val manageOrgURL = "https://manage-org.#{env}.platform.hmcts.net"
  val refDataApiURL = "https://rd-professional-api-#{env}.service.core-compute-#{env}.internal"
  val s2sUrl = "http://rpe-service-auth-provider-#{env}.service.core-compute-#{env}.internal"
  val manageCaseURL = "https://manage-case.#{env}.platform.hmcts.net"
  val idamCookieName="SESSION_ID"

  val HttpProtocol = http

  val minThinkTime = 1 //8
  val maxThinkTime = 2 //10

}