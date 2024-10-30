package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios.utils

object Headers {

  val commonHeader = Map(
    "content-type" -> "application/json",
    "sec-fetch-dest" -> "empty",
    "sec-fetch-mode" -> "cors",
    "sec-fetch-site" -> "same-origin")

  val navigationHeader = Map(
    "accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
    "sec-fetch-dest" -> "document",
    "sec-fetch-mode" -> "navigate",
    "sec-fetch-site" -> "same-origin",
    "sec-fetch-user" -> "?1",
    "upgrade-insecure-requests" -> "1")


  val postHeader = Map(
    "content-type" -> "application/x-www-form-urlencoded"
  )
  val MoneyClaimPostHeader = Map(
    "accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "content-type" -> "application/json",
    "experimental" -> "true",
    "sec-ch-ua-mobile" -> "?0",
    "sec-fetch-dest" -> "empty",
    "sec-fetch-mode" -> "cors",
    "sec-fetch-site" -> "same-origin"
  )
//
//  val headers_6 = Map(
//    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/png,image/svg+xml,*/*;q=0.8",
//    "Priority" -> "u=0, i",
//    "Sec-Fetch-Dest" -> "document",
//    "Sec-Fetch-Mode" -> "navigate",
//    "Sec-Fetch-Site" -> "same-origin",
//    "Upgrade-Insecure-Requests" -> "1"
//  )
}