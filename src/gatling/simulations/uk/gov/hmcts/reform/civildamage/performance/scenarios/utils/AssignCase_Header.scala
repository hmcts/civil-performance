package uk.gov.hmcts.reform.civildamage.performance.scenarios.utils

object AssignCase_Header {
  
  val manageOrgURL=Environment.manageOrgURL

  val headers_9 = Map(
    "Accept" -> "application/json, text/plain, */*",
    "Accept-Encoding" -> "gzip, deflate, br",
    "Accept-Language" -> "en-GB,en-US;q=0.9,en;q=0.8",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "authorization" -> "${authTokenResp}",
    "sec-ch-ua" -> """Chromium";v="86", "\"Not\\A;Brand";v="99", "Google Chrome";v="86""",
    "sec-ch-ua-mobile" -> "?0")

  val headers_37 = Map(
    "Accept" -> "application/json, text/plain, */*",
    "Accept-Encoding" -> "gzip, deflate, br",
    "Accept-Language" -> "en-GB,en-US;q=0.9,en;q=0.8",
    "Origin" -> manageOrgURL,
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "X-XSRF-TOKEN" -> "${XSRFToken}",
    "authorization" -> "${authTokenResp}",
    "sec-ch-ua" -> """Chromium";v="86", "\"Not\\A;Brand";v="99", "Google Chrome";v="86""",
    "sec-ch-ua-mobile" -> "?0")

  val headers_38 = Map(
    "Accept" -> "application/json, text/plain, */*",
    "Accept-Encoding" -> "gzip, deflate, br",
    "Accept-Language" -> "en-GB,en-US;q=0.9,en;q=0.8",
    "Origin" -> manageOrgURL,
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "X-XSRF-TOKEN" -> "${XSRFToken}",
    "authorization" -> "${authTokenResp}",
    "sec-ch-ua" -> """Chromium";v="86", "\"Not\\A;Brand";v="99", "Google Chrome";v="86""",
    "sec-ch-ua-mobile" -> "?0")

  val headers_54 = Map(
    "Accept" -> "application/json, text/plain, */*",
    "Accept-Encoding" -> "gzip, deflate, br",
    "Accept-Language" -> "en-GB,en-US;q=0.9,en;q=0.8",
    "Content-Type" -> "application/json",
    "Origin" -> manageOrgURL,
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "X-XSRF-TOKEN" -> "${XSRFToken}",
    "authorization" -> "${authTokenResp}",
    "sec-ch-ua" -> """Chromium";v="86", "\"Not\\A;Brand";v="99", "Google Chrome";v="86""",
    "sec-ch-ua-mobile" -> "?0")

}
