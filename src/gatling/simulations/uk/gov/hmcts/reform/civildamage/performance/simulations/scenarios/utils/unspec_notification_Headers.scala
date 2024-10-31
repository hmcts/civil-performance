package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios.utils

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object unspec_notification_Headers{



  val headers_10 = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/png,image/svg+xml,*/*;q=0.8",
    "Origin" -> "https://idam-web-public.perftest.platform.hmcts.net",
    "Priority" -> "u=0, i",
    "Referer" -> "https://idam-web-public.perftest.platform.hmcts.net/login?client_id=xuiwebapp&redirect_uri=https://manage-case.perftest.platform.hmcts.net/oauth2/callback&state=Krj9iW1nOgXPV3pjIj2Ig5ZSX_moxitwSp0XC59p5qw&nonce=nrQJJYhg1VHsrFebZPoPq7OM_VQ9tU1BvA3_DVlTB9E&response_type=code&scope=profile%20openid%20roles%20manage-user%20create-user%20search-user&prompt=",
    "Sec-Fetch-Dest" -> "document",
    "Sec-Fetch-Mode" -> "navigate",
    "Sec-Fetch-Site" -> "same-origin",
    "Sec-Fetch-User" -> "?1",
    "Upgrade-Insecure-Requests" -> "1"
  )

  val headers_11 = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/png,image/svg+xml,*/*;q=0.8",
    "Priority" -> "u=0, i",
    "Referer" -> "https://idam-web-public.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "document",
    "Sec-Fetch-Mode" -> "navigate",
    "Sec-Fetch-Site" -> "same-site",
    "Sec-Fetch-User" -> "?1",
    "Upgrade-Insecure-Requests" -> "1"
  )


  val headers_20 = Map(
    "Accept" -> "*/*",
    "Access-Control-Request-Headers" -> "authorization,content-type",
    "Access-Control-Request-Method" -> "GET",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Priority" -> "u=4",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-site"
  )

  val headers_21 = Map(
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-site",
//    "authorization" -> "Bearer eyJ0eXAiOiJKV1QiLCJraWQiOiI4cDJpajg2S0pTeENKeGcveUovV2w3TjcxMXM9IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJjaXZpbC5kYW1hZ2VzLmNsYWltcytvcmdhbmlzYXRpb24uMS5zb2xpY2l0b3IuMUBnbWFpbC5jb20iLCJjdHMiOiJPQVVUSDJfU1RBVEVMRVNTX0dSQU5UIiwiYXV0aF9sZXZlbCI6MCwiYXVkaXRUcmFja2luZ0lkIjoiOGNhMTJhNDMtYjc1NS00Mzk0LTg4ZjgtZjcxZTE4MzMzZjVmLTUzNTQ5MCIsInN1Ym5hbWUiOiJjaXZpbC5kYW1hZ2VzLmNsYWltcytvcmdhbmlzYXRpb24uMS5zb2xpY2l0b3IuMUBnbWFpbC5jb20iLCJpc3MiOiJodHRwczovL2Zvcmdlcm9jay1hbS5zZXJ2aWNlLmNvcmUtY29tcHV0ZS1pZGFtLXBlcmZ0ZXN0LmludGVybmFsOjg0NDMvb3BlbmFtL29hdXRoMi9yZWFsbXMvcm9vdC9yZWFsbXMvaG1jdHMiLCJ0b2tlbk5hbWUiOiJhY2Nlc3NfdG9rZW4iLCJ0b2tlbl90eXBlIjoiQmVhcmVyIiwiYXV0aEdyYW50SWQiOiIzYzR1Rk1iSS11YVZzakF6NTBzM1pCTERjVG8iLCJub25jZSI6Im5yUUpKWWhnMVZIc3JGZWJaUG9QcTdPTV9WUTl0VTFCdkEzX0RWbFRCOUUiLCJhdWQiOiJ4dWl3ZWJhcHAiLCJuYmYiOjE3Mjk2OTQ3OTYsImdyYW50X3R5cGUiOiJhdXRob3JpemF0aW9uX2NvZGUiLCJzY29wZSI6WyJvcGVuaWQiLCJwcm9maWxlIiwicm9sZXMiLCJjcmVhdGUtdXNlciIsIm1hbmFnZS11c2VyIiwic2VhcmNoLXVzZXIiXSwiYXV0aF90aW1lIjoxNzI5Njk0Nzk1LCJyZWFsbSI6Ii9obWN0cyIsImV4cCI6MTcyOTcyMzU5NiwiaWF0IjoxNzI5Njk0Nzk2LCJleHBpcmVzX2luIjoyODgwMCwianRpIjoiNlp6ZHQtRzFmdUVmenhrVzV4MjRPNk1wWnc4In0.G1YHM5hcPCIc92dCoDPbuggaN7C__opYSrqHfHrFAH5UQ-OFC9N9J5Q677_7kCbx3XepXnm71QPam43RQjt6Wp_KzNW4LANplyYICU-RlvsPFJ4Qcajk_cQvSb7mME3ihSaqyUL3bt592n4HsX4RZJE9VB7nMxWukQz7T3BS0pyPAExsCnAo0foCikEFMTWmnDaJH6otNQ3qAtyuK3izkL4wfwfaUBWEqix-HRTthPA2iMHw4jWgHn9FrC3gfUdt0c-WnHO4pJi9JZwkWoGO9Op7NtKDN5qSCEq0wZgBA6wbvyJpXdZI9EfJF-aWFUWCaVyg1OyghgCg884E0XqaPg"
  )

  val headers_23 = Map(
    "Accept" -> "application/json",
    "Content-Type" -> "application/json",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_24 = Map(
    "Accept" -> "image/avif,image/webp,image/png,image/svg+xml,image/*;q=0.8,*/*;q=0.5",
    "Priority" -> "u=4, i",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "image",
    "Sec-Fetch-Mode" -> "no-cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_25 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-workbasket-input-details.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_26 = Map(
    "Accept" -> "application/json",
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
//    "X-XSRF-TOKEN" -> "5crpJ3Pc-c058q6QK6tN1-DzNfIq_IWHx2Xc",
  )

  val headers_27 = Map(
    "Accept" -> "application/json",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_28 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-case-trigger.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_29 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
//    "X-XSRF-TOKEN" -> "AdegF7bd-tkufxqiIqbnVDxeo6p-nf3Q-Ngw",
    "experimental" -> "true"
  )

  val headers_30 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
//    "X-XSRF-TOKEN" -> "F0lyf7tt-E3vrZ8MVjUe73_ROabsiB6roXAM",
    "experimental" -> "true"
  )

  val headers_31 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
//    "X-XSRF-TOKEN" -> "0f7gFtIB-_bzukDjLX1HPxfeBO7S9BQ0XTNM",
    "experimental" -> "true"
  )

  val headers_32 = Map(
    "Accept" -> "application/json",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_33 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
//    "X-XSRF-TOKEN" -> "qcwjRTxy-IoWoD9pzaOWslwIASsAocX33QFA",
    "experimental" -> "true"
  )

  val headers_34 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
//    "X-XSRF-TOKEN" -> "KqWXZunW-YSrAflhtXHb8mwft-wnr28y7x7I",
    "experimental" -> "true"
  )

  val headers_35 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",

    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
//    "X-XSRF-TOKEN" -> "laUuFDUr-SoaplLYAo6yjjypAP3y_zziQBHs",
    "experimental" -> "true",


  )


  val headers_37 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
//    "X-XSRF-TOKEN" -> "sGA4VFV9-IVHEywBDwZ3NyZN0gq9uTV4bfAY",
    "experimental" -> "true"
  )

  val headers_38 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
//    "X-XSRF-TOKEN" -> "Yc9dxjBH-TCAvFy4J_X5xrlk6SPkgBh-Sffg",
    "experimental" -> "true"
  )

  val headers_39 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
//    "X-XSRF-TOKEN" -> "xJZ7ikgk-BSYn1UGjr91-bFzjrwE1kteW1J0",
    "experimental" -> "true"
  )

  val headers_40 = Map(
    "Accept" -> "application/json",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_41 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
//    "X-XSRF-TOKEN" -> "smKKVdvM-OxnhylDQkwT51uGmiBYTfkrNIZA",
    "experimental" -> "true"
  )

  val headers_42 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
//    "X-XSRF-TOKEN" -> "XQcBpK4V-c0B6EO0fJSRG9vXls2513Qeh4Ck",
    "experimental" -> "true"
  )

  val headers_43 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
//    "X-XSRF-TOKEN" -> "lr4pKLDU-Spro_iQs7jMdgRtmYTYNUiby6cQ",
    "experimental" -> "true"
  )

  val headers_44 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
//    "X-XSRF-TOKEN" -> "Qvj3SEvS-qDv9bKv-osAMWgE2p4_DyxFdXGg",
    "experimental" -> "true"
  )

  val headers_45 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
//    "X-XSRF-TOKEN" -> "7lm71V8p-OxZ0Jjjf2QvQlHvw3F0UIlm1uHI",
    "experimental" -> "true"
  )

  val headers_46 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
//    "X-XSRF-TOKEN" -> "FFzRzYv1-hDbV1fVjyWo0OhTnJ1TBwNenZEU",
    "experimental" -> "true"
  )

  val headers_47 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
//    "X-XSRF-TOKEN" -> "Ak7syGZj-9752JJyVlYF3qJErZzxIWC_VHzI",
    "experimental" -> "true"
  )

  val headers_48 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
//    "X-XSRF-TOKEN" -> "5K04jMbU--7W9FHjaNfi4TdLUXxnoPguQlzw",
    "experimental" -> "true"
  )

  val headers_49 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",

    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
//    "X-XSRF-TOKEN" -> "NmZ4OR1T-TtYXNhLvn-djh2Jxb9kpiUYHRJw",
    "experimental" -> "true"
  )

  val headers_50 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
//    "X-XSRF-TOKEN" -> "9ZGZQnRx-w8uf1LQy4pS391f2fzYA18hU5XA",
    "experimental" -> "true"
  )

  val headers_51 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",

    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
//    "X-XSRF-TOKEN" -> "V4UvczgK-Zp1-R-CTn5y0BqabEJOTUy1RHQY",
    "experimental" -> "true"
  )

  val headers_52 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
//    "X-XSRF-TOKEN" -> "SaXi3RRm-XvP5ZXb2T2WjUm9xKvsKub8kNdk",
    "experimental" -> "true"


  )

  val headers_53 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-case.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
//    "X-XSRF-TOKEN" -> "uXXY8O1M-Z3Tlcaj-pT_Hzhq6YFG9d7ATjdU",
    "experimental" -> "true"


  )

  val headers_54 = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/png,image/svg+xml,*/*;q=0.8",
    "Priority" -> "u=0, i",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "document",
    "Sec-Fetch-Mode" -> "navigate",
    "Sec-Fetch-Site" -> "same-origin",
    "Sec-Fetch-User" -> "?1",
    "Upgrade-Insecure-Requests" -> "1"
  )

  val headers_55 = Map(
    "Accept" -> "*/*",
    "Priority" -> "u=4",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_63 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json",
    "Content-Type" -> "application/json",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_64 = Map(
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_65 = Map(
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
//    "X-XSRF-TOKEN" -> "lbYpdSLS-2kOAk4_0SBFzgACNOhnze536Xtk",
  )

  val headers_66 = Map(
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_67 = Map(
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_68 = Map(
//    "CSRF-Token" -> "qlKClsNu-0rqy8Mbv0aOhZw9CTWYej6ssTXI",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "X-Requested-With" -> "XMLHttpRequest"
  )

  val headers_70 = Map(
    "Accept" -> "*/*",
    "Priority" -> "u=4",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )


  val headers_78 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json",
    "Content-Type" -> "application/json",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_79 = Map(
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_80 = Map(
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_81 = Map(
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
//    "X-XSRF-TOKEN" -> "P3mDIODA-1p80wdvtSVJYjc8BLXJI-xFFZgI",
  )

  val headers_82 = Map(
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_83 = Map(
//    "CSRF-Token" -> "ROL8du0v-N67CFclKnTeU-LoF3eFLWO86L_Y",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "X-Requested-With" -> "XMLHttpRequest"
  )

  val headers_84 = Map(
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_85 = Map(
//    "CSRF-Token" -> "0gh6U220-qAz5rHDNVAWHuFNGY5M6Oefvifc",
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "X-Requested-With" -> "XMLHttpRequest",
//    "X-XSRF-TOKEN" -> "0gh6U220-qAz5rHDNVAWHuFNGY5M6Oefvifc",
  )

  val headers_87 = Map(
    "Accept" -> "*/*",
    "Priority" -> "u=4",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_95 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json",
    "Content-Type" -> "application/json",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_97 = Map(
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
//    "X-XSRF-TOKEN" -> "W0zV4Hys-EWZPshfQKCUFYbiS_rbrRpY3J_Q",
  )

  val headers_99 = Map(
    "Accept" -> "*/*",
    "Priority" -> "u=4",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_107 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json",
    "Content-Type" -> "application/json",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_109 = Map(
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
//    "X-XSRF-TOKEN" -> "gu1i1BRJ-GBrONkU0C7eQKmSP9qRG_zoksGs"
  )

  val headers_110 = Map(
    "Accept" -> "application/json",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )


  val headers_111 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_112 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-user-profile.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_113 = Map(
    "Accept" -> "application/json",
    "Content-Type" -> "application/json",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
  )

  val headers_114 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
//    "X-XSRF-TOKEN" -> "wshNdz40-IkfamroS-pH5hJwaNbRHOABVqME",
    "experimental" -> "true",
  )

  val headers_115 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
//    "X-XSRF-TOKEN" -> "mgY8YhGx-Cn4uRCI2CZ1lqbI-wGKfyVS_nSE",
    "experimental" -> "true",


  )

  val headers_116 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json",
    "Content-Type" -> "application/json",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true",
  )

  val headers_117 = Map(
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
  )

  val headers_118 = Map(
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
//    "X-XSRF-TOKEN" -> "VwX3mv8n-Yadsp_JmqRqafWlEZimNDI0pM3k"
  )

  val headers_120 = Map(
    "Accept" -> "*/*",
    "Priority" -> "u=4",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_130 = Map(
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
//    "X-XSRF-TOKEN" -> "GbkzeMC0-pRMv0KlGWvOlxe4Vx2PGyPCJpIk",

  )

  val headers_131 = Map(
    "Accept" -> "application/json",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_132 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_133 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-user-profile.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_134 = Map(
    "Accept" -> "application/json",
    "Content-Type" -> "application/json",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_135 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
//    "X-XSRF-TOKEN" -> "ENMX97ir-1i-Q-tfCgSTkR5G7zBL-5b-27Zg",
    "experimental" -> "true"
  )

  val headers_136 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
//    "X-XSRF-TOKEN" -> "sugmoXBc-XvzASvbF0NKKsnRIzmQt1YVNL_M",
    "experimental" -> "true"
  )

  val headers_137 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json",
    "Content-Type" -> "application/json",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )


}
