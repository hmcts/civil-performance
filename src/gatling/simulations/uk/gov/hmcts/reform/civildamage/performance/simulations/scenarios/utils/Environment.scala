package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios.utils

import scala.concurrent.duration._
import a_CreateClaimPay_Headers._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.gatling.core.scenario.Simulation

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.util.Random


object Environment {

  val minThinkTime = 8//20
  val maxThinkTime = 10//20


  val httpProtocol = http
    .baseUrl("https://manage-case.perftest.platform.hmcts.net")
    .disableFollowRedirect
    .disableAutoReferer
    .acceptHeader("application/json, text/plain, */*")
    .acceptEncodingHeader("gzip, deflate, br")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:131.0) Gecko/20100101 Firefox/131.0")


  val idamURL = "https://idam-web-public.perftest.platform.hmcts.net"
  val baseURL = "https://manage-case.perftest.platform.hmcts.net"



}