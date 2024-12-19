package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios.utils
import io.gatling.core.Predef._
import io.gatling.http.Predef._

object Environment {

  val minThinkTime = 2//20
  val maxThinkTime = 4//20

  val idamURL = "https://idam-web-public.perftest.platform.hmcts.net"
  val baseURL = "https://manage-case.perftest.platform.hmcts.net"

  val httpProtocol = http
    .baseUrl(baseURL)
    //.disableFollowRedirect
    //.disableAutoReferer
    .inferHtmlResources()
    .silentResources
    .acceptHeader("application/json, text/plain, */*")
    .acceptEncodingHeader("gzip, deflate, br, zstd")
    .acceptLanguageHeader("en-US,en;q=0.9")
    //.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:131.0) Gecko/20100101 Firefox/131.0")

}