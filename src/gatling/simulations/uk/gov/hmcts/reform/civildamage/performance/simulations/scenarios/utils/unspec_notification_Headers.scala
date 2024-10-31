package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios.utils

object unspec_notification_Headers{



//  val headers_10 = Map(
//    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/png,image/svg+xml,*/*;q=0.8",
//
//    "Priority" -> "u=0, i",
//
//    "Sec-Fetch-Dest" -> "document",
//    "Sec-Fetch-Mode" -> "navigate",
//    "Sec-Fetch-Site" -> "same-origin",
//    "Sec-Fetch-User" -> "?1",
//    "Upgrade-Insecure-Requests" -> "1"
//  )
//
//  val headers_11 = Map(
//    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/png,image/svg+xml,*/*;q=0.8",
//    "Priority" -> "u=0, i",
//
//    "Sec-Fetch-Dest" -> "document",
//    "Sec-Fetch-Mode" -> "navigate",
//    "Sec-Fetch-Site" -> "same-site",
//    "Sec-Fetch-User" -> "?1",
//    "Upgrade-Insecure-Requests" -> "1"
//  )
//
//
//  val headers_20 = Map(
//    "Accept" -> "*/*",
//    "Access-Control-Request-Headers" -> "authorization,content-type",
//    "Access-Control-Request-Method" -> "GET",
//
//    "Priority" -> "u=4",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-site"
//  )
//
//  val headers_21 = Map(
//    "Content-Type" -> "application/json",
//
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-site",
//)
//
//  val headers_23 = Map(
//    "Accept" -> "application/json",
//    "Content-Type" -> "application/json",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )
//
//  val headers_24 = Map(
//    "Accept" -> "image/avif,image/webp,image/png,image/svg+xml,image/*;q=0.8,*/*;q=0.5",
//    "Priority" -> "u=4, i",
//
//    "Sec-Fetch-Dest" -> "image",
//    "Sec-Fetch-Mode" -> "no-cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )
//
//  val headers_25 = Map(
//    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-workbasket-input-details.v2+json;charset=UTF-8",
//    "Content-Type" -> "application/json",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//    "experimental" -> "true"
//  )
//
//  val headers_26 = Map(
//    "Accept" -> "application/json",
//    "Content-Type" -> "application/json",
//
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//
//  )
//
//  val headers_27 = Map(
//    "Accept" -> "application/json",
//    "Content-Type" -> "application/json",
//    "Priority" -> "u=0",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )
//
//  val headers_28 = Map(
//    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-case-trigger.v2+json;charset=UTF-8",
//    "Content-Type" -> "application/json",
//    "Priority" -> "u=0",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//    "experimental" -> "true"
//  )
//
//  val headers_29 = Map(
//    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
//    "Content-Type" -> "application/json",
//
//    "Priority" -> "u=0",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//
//    "experimental" -> "true"
//  )
//
//  val headers_30 = Map(
//    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
//    "Content-Type" -> "application/json",
//
//    "Priority" -> "u=0",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//
//    "experimental" -> "true"
//  )
//
//  val headers_31 = Map(
//    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
//    "Content-Type" -> "application/json",
//
//    "Priority" -> "u=0",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//
//    "experimental" -> "true"
//  )
//
//  val headers_32 = Map(
//    "Accept" -> "application/json",
//    "Content-Type" -> "application/json",
//    "Priority" -> "u=0",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )
//
//  val headers_33 = Map(
//    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
//    "Content-Type" -> "application/json",
//
//    "Priority" -> "u=0",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//
//    "experimental" -> "true"
//  )
//
//  val headers_34 = Map(
//    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
//    "Content-Type" -> "application/json",
//
//    "Priority" -> "u=0",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//
//    "experimental" -> "true"
//  )
//
//  val headers_35 = Map(
//    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
//    "Content-Type" -> "application/json",
//
//    "Priority" -> "u=0",
//
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//
//    "experimental" -> "true",
//
//
//  )
//
//
//  val headers_37 = Map(
//    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
//    "Content-Type" -> "application/json",
//
//    "Priority" -> "u=0",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//
//    "experimental" -> "true"
//  )
//
//  val headers_38 = Map(
//    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
//    "Content-Type" -> "application/json",
//
//    "Priority" -> "u=0",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//
//    "experimental" -> "true"
//  )
//
//  val headers_39 = Map(
//    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
//    "Content-Type" -> "application/json",
//
//    "Priority" -> "u=0",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//
//    "experimental" -> "true"
//  )
//
//  val headers_40 = Map(
//    "Accept" -> "application/json",
//    "Content-Type" -> "application/json",
//    "Priority" -> "u=0",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )
//
//  val headers_41 = Map(
//    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
//    "Content-Type" -> "application/json",
//
//    "Priority" -> "u=0",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//
//    "experimental" -> "true"
//  )
//
//  val headers_42 = Map(
//    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
//    "Content-Type" -> "application/json",
//
//    "Priority" -> "u=0",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//
//    "experimental" -> "true"
//  )
//
//  val headers_43 = Map(
//    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
//    "Content-Type" -> "application/json",
//
//    "Priority" -> "u=0",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//
//    "experimental" -> "true"
//  )
//
//  val headers_44 = Map(
//    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
//    "Content-Type" -> "application/json",
//
//    "Priority" -> "u=0",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//
//    "experimental" -> "true"
//  )
//
//  val headers_45 = Map(
//    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
//    "Content-Type" -> "application/json",
//
//    "Priority" -> "u=0",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//
//    "experimental" -> "true"
//  )
//
//  val headers_46 = Map(
//    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
//    "Content-Type" -> "application/json",
//
//    "Priority" -> "u=0",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//
//    "experimental" -> "true"
//  )
//
//  val headers_47 = Map(
//    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
//    "Content-Type" -> "application/json",
//
//    "Priority" -> "u=0",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//
//    "experimental" -> "true"
//  )
//
//  val headers_48 = Map(
//    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
//    "Content-Type" -> "application/json",
//
//    "Priority" -> "u=0",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//
//    "experimental" -> "true"
//  )
//
//  val headers_49 = Map(
//    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
//    "Content-Type" -> "application/json",
//
//    "Priority" -> "u=0",
//
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//
//    "experimental" -> "true"
//  )
//
//  val headers_50 = Map(
//    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
//    "Content-Type" -> "application/json",
//
//    "Priority" -> "u=0",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//
//    "experimental" -> "true"
//  )
//
//  val headers_51 = Map(
//    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
//    "Content-Type" -> "application/json",
//
//    "Priority" -> "u=0",
//
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//
//    "experimental" -> "true"
//  )
//
//  val headers_52 = Map(
//    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
//    "Content-Type" -> "application/json",
//
//    "Priority" -> "u=0",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//
//    "experimental" -> "true"
//
//
//  )
//
//  val headers_53 = Map(
//    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-case.v2+json;charset=UTF-8",
//    "Content-Type" -> "application/json",
//
//    "Priority" -> "u=0",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//
//    "experimental" -> "true"
//
//
//  )
//
//  val headers_54 = Map(
//    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/png,image/svg+xml,*/*;q=0.8",
//    "Priority" -> "u=0, i",
//
//    "Sec-Fetch-Dest" -> "document",
//    "Sec-Fetch-Mode" -> "navigate",
//    "Sec-Fetch-Site" -> "same-origin",
//    "Sec-Fetch-User" -> "?1",
//    "Upgrade-Insecure-Requests" -> "1"
//  )
//
//  val headers_55 = Map(
//    "Accept" -> "*/*",
//    "Priority" -> "u=4",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )
//
//  val headers_63 = Map(
//    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json",
//    "Content-Type" -> "application/json",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//    "experimental" -> "true"
//  )
//
//  val headers_64 = Map(
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )
//
//  val headers_65 = Map(
//    "Content-Type" -> "application/json",
//
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//
//  )
//
//  val headers_66 = Map(
//    "Priority" -> "u=0",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )
//
//  val headers_67 = Map(
//    "Priority" -> "u=0",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )
//
//  val headers_68 = Map(
////    "CSRF-Token" -> "qlKClsNu-0rqy8Mbv0aOhZw9CTWYej6ssTXI",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//    "X-Requested-With" -> "XMLHttpRequest"
//  )
//
//  val headers_70 = Map(
//    "Accept" -> "*/*",
//    "Priority" -> "u=4",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )
//
//
//  val headers_78 = Map(
//    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json",
//    "Content-Type" -> "application/json",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//    "experimental" -> "true"
//  )
//
//  val headers_79 = Map(
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )
//
//  val headers_80 = Map(
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )
//
//  val headers_81 = Map(
//    "Content-Type" -> "application/json",
//
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//
//  )
//
//  val headers_82 = Map(
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )
//
//  val headers_83 = Map(
////    "CSRF-Token" -> "ROL8du0v-N67CFclKnTeU-LoF3eFLWO86L_Y",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//    "X-Requested-With" -> "XMLHttpRequest"
//  )
//
//  val headers_84 = Map(
//    "Priority" -> "u=0",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )
//
//  val headers_85 = Map(
////    "CSRF-Token" -> "0gh6U220-qAz5rHDNVAWHuFNGY5M6Oefvifc",
//    "Content-Type" -> "application/json",
//
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//    "X-Requested-With" -> "XMLHttpRequest",
//
//  )
//
//  val headers_87 = Map(
//    "Accept" -> "*/*",
//    "Priority" -> "u=4",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )
//
//  val headers_95 = Map(
//    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json",
//    "Content-Type" -> "application/json",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//    "experimental" -> "true"
//  )
//
//  val headers_97 = Map(
//    "Content-Type" -> "application/json",
//
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//
//  )
//
//  val headers_99 = Map(
//    "Accept" -> "*/*",
//    "Priority" -> "u=4",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin"
//  )
//
//  val headers_107 = Map(
//    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json",
//    "Content-Type" -> "application/json",
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//    "experimental" -> "true"
//  )

  val headers_109 = Map(
    "Content-Type" -> "application/json",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_110 = Map(
    "Accept" -> "application/json",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )


  val headers_111 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_112 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-user-profile.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_113 = Map(
    "Accept" -> "application/json",
    "Content-Type" -> "application/json",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
  )

  val headers_114 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true",
  )

  val headers_115 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true",
  )

//  val headers_116 = Map(
//    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json",
//    "Content-Type" -> "application/json",
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//    "experimental" -> "true",
//  )

  val headers_117 = Map(
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
  )

  val headers_118 = Map(
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_120 = Map(
    "Accept" -> "*/*",
    "Priority" -> "u=4",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

//  val headers_130 = Map(
//    "Content-Type" -> "application/json",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//
//
//  )

  val headers_131 = Map(
    "Accept" -> "application/json",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_132 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_133 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-user-profile.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_134 = Map(
    "Accept" -> "application/json",
    "Content-Type" -> "application/json",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

//  val headers_135 = Map(
//    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
//    "Content-Type" -> "application/json",
//    "Priority" -> "u=0",
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//    "experimental" -> "true"
//  )

  val headers_136 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )
//
//  val headers_137 = Map(
//    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json",
//    "Content-Type" -> "application/json",
//
//    "Sec-Fetch-Dest" -> "empty",
//    "Sec-Fetch-Mode" -> "cors",
//    "Sec-Fetch-Site" -> "same-origin",
//    "experimental" -> "true"
//  )


}
