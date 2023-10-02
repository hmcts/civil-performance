package uk.gov.hmcts.reform.civildamage.performance.scenarios.utils

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object Environment {

  val httpConfig = scala.util.Properties.envOrElse("httpConfig", "http")
  val baseURL = "https://manage-case.perftest.platform.hmcts.net"
  val baseDomain = scala.util.Properties.envOrElse("baseDomain", "manage-case.perftest.platform.hmcts.net")
  val PaymentURL = scala.util.Properties.envOrElse("paymentURL", "https://www.payments.service.gov.uk")
  val idamURL = "https://idam-web-public.perftest.platform.hmcts.net"
  val idamAPIURL = "https://idam-api.perftest.platform.hmcts.net"
  val exuiDomain="manage-case.perftest.platform.hmcts.net"
  val manageOrgURL = "https://manage-org.perftest.platform.hmcts.net"
  val manageOrgDomain = "manage-org.perftest.platform.hmcts.net"
  val CivilUIURL=  "https://civil-citizen-ui.perftest.platform.hmcts.net"
  val idamCookieName="SESSION_ID"
  val HttpProtocol = http


/*
  val httpConfig = scala.util.Properties.envOrElse("httpConfig", "http")
  val baseURL = "https://manage-case.demo.platform.hmcts.net"
  val baseDomain = scala.util.Properties.envOrElse("baseDomain", "manage-case.demo.platform.hmcts.net")
  val PaymentURL = scala.util.Properties.envOrElse("paymentURL", "https://www.payments.service.gov.uk")
  val idamURL = "https://idam-web-public.demo.platform.hmcts.net"
  val idamAPIURL = "https://idam-api.demo.platform.hmcts.net"
  val exuiDomain="manage-case.demo.platform.hmcts.net"
  val manageOrgURL = "https://manage-org.demo.platform.hmcts.net"
  val manageOrgDomain = "manage-org.demo.platform.hmcts.net"
  val idamCookieName="SESSION_ID"
  val HttpProtocol = http

 */
  
  val minThinkTime = 3//20
  val maxThinkTime = 7//20
	
  
  val commonHeader = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
    "Accept-Encoding" -> "gzip, deflate, br",
    "Accept-Language" -> "en-GB,en-US;q=0.9,en;q=0.8",
    "Connection" -> "keep-alive",
    "Upgrade-Insecure-Requests" -> "1",
    "User-Agent" -> "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36")
  
  
  val headers_firstcontact = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
    "Upgrade-Insecure-Requests" -> "1")
  
  val headers_996 = Map(
    "Accept" -> "*/*",
    "Accept-Encoding" -> "gzip, deflate, br",
    "Accept-Language" -> "en-US,en;q=0.9",
    "Content-type" -> "application/json",
    "Origin" -> "https://www.payments.service.gov.uk",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36")
  
  val headers_withpin = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
    "Origin" -> idamURL,
    "TE" -> "Trailers",
    "Upgrade-Insecure-Requests" -> "1")
  
  val headers_claimsummary = Map(
    "accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
    "accept-encoding" -> "gzip, deflate, br",
    "accept-language" -> "en-US,en;q=0.9",
    "cache-control" -> "max-age=0",
    "origin" -> baseURL,
    "sec-ch-ua" -> """Google Chrome";v="89", "Chromium";v="89", ";Not A Brand";v="99""",
    "sec-ch-ua-mobile" -> "?0",
    "sec-fetch-dest" -> "document",
    "sec-fetch-mode" -> "navigate",
    "sec-fetch-site" -> "same-origin",
    "sec-fetch-user" -> "?1",
    "upgrade-insecure-requests" -> "1",
    "user-agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.82 Safari/537.36")
  
  val headers_25 = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
    "Origin" -> baseURL,
    "TE" -> "Trailers",
    "Upgrade-Insecure-Requests" -> "1")
  
  val headers_claimrefget = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
    "Origin" -> idamURL,
    "TE" -> "Trailers",
    "Upgrade-Insecure-Requests" -> "1")
  
  val headers_checkAndSend = Map(
    "accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
    "accept-encoding" -> "gzip, deflate, br",
    "accept-language" -> "en-US,en;q=0.9",
    "cache-control" -> "max-age=0",
    "origin" -> baseURL,
    "sec-ch-ua" -> """Google Chrome";v="89", "Chromium";v="89", ";Not A Brand";v="99""",
    "sec-ch-ua-mobile" -> "?0",
    "sec-fetch-dest" -> "document",
    "sec-fetch-mode" -> "navigate",
    "sec-fetch-site" -> "same-origin",
    "sec-fetch-user" -> "?1",
    "upgrade-insecure-requests" -> "1",
    "user-agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36")
  
 
  
  
}