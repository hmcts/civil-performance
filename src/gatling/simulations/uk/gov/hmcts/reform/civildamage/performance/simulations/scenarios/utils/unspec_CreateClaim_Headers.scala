package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios.utils

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object unspec_CreateClaim_Headers {
//
//  val headers_0 = Map(
//    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
//    "Priority" -> "u=0, i",
//    "Sec-Fetch-Dest" -> "document",
//    "Sec-Fetch-Mode" -> "navigate",
//    "Sec-Fetch-Site" -> "none",
//    "Sec-Fetch-User" -> "?1",
//    "Upgrade-Insecure-Requests" -> "1"
//  )
//
//  val headers_1 = Map(
//    "Accept" -> "*/*",
//    "Priority" -> "u=4",
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )
//
//  val headers_2 = Map(
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )
//
//  val headers_7 = Map(
//    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
//    "Priority" -> "u=0, i",
//    "Sec-Fetch-Dest" -> "document",
//    "Sec-Fetch-Mode" -> "navigate",
//    "Sec-Fetch-Site" -> "same-origin",
//    "Upgrade-Insecure-Requests" -> "1"
//  )
//
//  val headers_8 = Map(
//    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
//    "Priority" -> "u=0, i",
//    "Sec-Fetch-Dest" -> "document",
//    "Sec-Fetch-Mode" -> "navigate",
//    "Sec-Fetch-Site" -> "same-site",
//    "Upgrade-Insecure-Requests" -> "1"
//  )
//
//  val headers_10 = Map(
//    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
//    "Priority" -> "u=0, i",
//    "Sec-Fetch-Dest" -> "document",
//    "Sec-Fetch-Mode" -> "navigate",
//    "Sec-Fetch-Site" -> "same-origin",
//    "Sec-Fetch-User" -> "?1",
//    "Upgrade-Insecure-Requests" -> "1"
//  )
//
//  val headers_11 = Map(
//    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
//    "Priority" -> "u=0, i",
//    "Sec-Fetch-Dest" -> "document",
//    "Sec-Fetch-Mode" -> "navigate",
//    "Sec-Fetch-Site" -> "same-site",
//    "Sec-Fetch-User" -> "?1",
//    "Upgrade-Insecure-Requests" -> "1"
//  )
//
//  val headers_13 = Map(
//    "Accept" -> "*/*",
//    "Priority" -> "u=4",
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )
//
//  val headers_14 = Map(
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )
//
//  val headers_15 = Map(
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )
//
//  val headers_16 = Map(
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )
//
//  val headers_17 = Map(
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )
//
//  val headers_18 = Map(
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )
//
//  val headers_19 = Map(
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )
//
//  val headers_20 = Map(
//    "Accept" -> "*/*",
//    "Access-Control-Request-Headers" -> "authorization,content-type",
//    "Access-Control-Request-Method" -> "GET",
//    "Priority" -> "u=4",
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-site"
//  )
//
//  val headers_21 = Map(
//    "Content-Type" -> "application/json",
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-site",
//  )
//
//  val headers_22 = Map(
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )
//
//  val headers_23 = Map(
//    "Accept" -> "application/json",
//    "Content-Type" -> "application/json",
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )
//
//  val headers_24 = Map(
//    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-workbasket-input-details.v2+json;charset=UTF-8",
//    "Content-Type" -> "application/json",
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//    "experimental" -> "true"
//  )
//
//  val headers_25 = Map(
//    "Accept" -> "application/json",
//    "Content-Type" -> "application/json",
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )
//
//  val headers_26 = Map(
//    "Accept" -> "image/avif,image/webp,image/png,image/svg+xml,image/*;q=0.8,*/*;q=0.5",
//    "Priority" -> "u=4, i",
//    "Sec-Fetch-Dest" -> "image",
//    "Sec-Fetch-Mode" -> "no-cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )

  val headers_27 = Map(
    "Accept" -> "application/json",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_28 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-case-trigger.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_29 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_30 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_31 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_32 = Map(
    "Accept" -> "application/json",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_33 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_34 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_35 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

//  val headers_36 = Map(
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )

  val headers_37 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_38 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_39 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_40 = Map(
    "Accept" -> "application/json",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_41 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_42 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_43 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_44 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_45 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_46 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_47 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_48 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_49 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_50 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_51 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_52 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_53 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-case.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_54 = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
    "Priority" -> "u=0, i",
    "Sec-Fetch-Dest" -> "document",
    "Sec-Fetch-Mode" -> "navigate",
    "Sec-Fetch-Site" -> "same-origin",
    "Sec-Fetch-User" -> "?1",
    "Upgrade-Insecure-Requests" -> "1"
  )

  val headers_55 = Map(
    "Accept" -> "*/*",
    "Priority" -> "u=4",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

//  val headers_56 = Map(
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )

  val headers_57 = Map(
    "If-Modified-Since" -> "Tue, 29 Oct 2024 16:28:58 GMT",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_58 = Map(
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

//  val headers_59 = Map(
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )
//
//  val headers_60 = Map(
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )

  val headers_61 = Map(
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_64 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json",
    "Content-Type" -> "application/json",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_65 = Map(
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_66 = Map(
    "Accept" -> "image/avif,image/webp,image/png,image/svg+xml,image/*;q=0.8,*/*;q=0.5",
    "If-Modified-Since" -> "Tue, 29 Oct 2024 16:28:49 GMT",
    "Priority" -> "u=5",
    "Sec-Fetch-Dest" -> "image",
    "Sec-Fetch-Mode" -> "no-cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_67 = Map(
    "Content-Type" -> "application/json",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_68 = Map(
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
  )

  val headers_69 = Map(
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
  )

  val headers_70 = Map(
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "X-Requested-With" -> "XMLHttpRequest"
  )

  val headers_71 = Map(
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_72 = Map(
    "Content-Type" -> "application/json",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "X-Requested-With" -> "XMLHttpRequest"
  )


}