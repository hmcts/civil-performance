package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios.utils

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object unspec_CreateClaim_Headerss {

  val headers_0 = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
    "Priority" -> "u=0, i",
    "Sec-Fetch-Dest" -> "document",
    "Sec-Fetch-Mode" -> "navigate",
    "Sec-Fetch-Site" -> "none",
    "Sec-Fetch-User" -> "?1",
    "Upgrade-Insecure-Requests" -> "1"
  )

  val headers_1 = Map(
    "Accept" -> "*/*",
    "Priority" -> "u=4",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_2 = Map(
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_7 = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
    "Priority" -> "u=0, i",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "document",
    "Sec-Fetch-Mode" -> "navigate",
    "Sec-Fetch-Site" -> "same-origin",
    "Upgrade-Insecure-Requests" -> "1"
  )

  val headers_8 = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
    "Priority" -> "u=0, i",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "document",
    "Sec-Fetch-Mode" -> "navigate",
    "Sec-Fetch-Site" -> "same-site",
    "Upgrade-Insecure-Requests" -> "1"
  )

  val headers_10 = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
    "Origin" -> "https://idam-web-public.perftest.platform.hmcts.net",
    "Priority" -> "u=0, i",
    "Referer" -> "https://idam-web-public.perftest.platform.hmcts.net/login?client_id=xuiwebapp&redirect_uri=https://manage-case.perftest.platform.hmcts.net/oauth2/callback&state=#{state}&nonce=#{nonce}&response_type=code&scope=profile%20openid%20roles%20manage-user%20create-user%20search-user&prompt=",
    "Sec-Fetch-Dest" -> "document",
    "Sec-Fetch-Mode" -> "navigate",
    "Sec-Fetch-Site" -> "same-origin",
    "Sec-Fetch-User" -> "?1",
    "Upgrade-Insecure-Requests" -> "1"
  )

  val headers_11 = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
    "Priority" -> "u=0, i",
    "Referer" -> "https://idam-web-public.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "document",
    "Sec-Fetch-Mode" -> "navigate",
    "Sec-Fetch-Site" -> "same-site",
    "Sec-Fetch-User" -> "?1",
    "Upgrade-Insecure-Requests" -> "1"
  )

  val headers_13 = Map(
    "Accept" -> "*/*",
    "Priority" -> "u=4",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_14 = Map(
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_15 = Map(
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_16 = Map(
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_17 = Map(
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_18 = Map(
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_19 = Map(
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
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
    //  		"authorization" -> "Bearer eyJ0eXAiOiJKV1QiLCJraWQiOiI4cDJpajg2S0pTeENKeGcveUovV2w3TjcxMXM9IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJjaXZpbC5kYW1hZ2VzLmNsYWltcytvcmdhbmlzYXRpb24uMS5zb2xpY2l0b3IuMUBnbWFpbC5jb20iLCJjdHMiOiJPQVVUSDJfU1RBVEVMRVNTX0dSQU5UIiwiYXV0aF9sZXZlbCI6MCwiYXVkaXRUcmFja2luZ0lkIjoiYjg3NTI3YjctMGEzNC00NDI0LWI5ZGMtOTMzY2I1ZjMxZDQ2LTg1Mjg1NzMiLCJzdWJuYW1lIjoiY2l2aWwuZGFtYWdlcy5jbGFpbXMrb3JnYW5pc2F0aW9uLjEuc29saWNpdG9yLjFAZ21haWwuY29tIiwiaXNzIjoiaHR0cHM6Ly9mb3JnZXJvY2stYW0uc2VydmljZS5jb3JlLWNvbXB1dGUtaWRhbS1wZXJmdGVzdC5pbnRlcm5hbDo4NDQzL29wZW5hbS9vYXV0aDIvcmVhbG1zL3Jvb3QvcmVhbG1zL2htY3RzIiwidG9rZW5OYW1lIjoiYWNjZXNzX3Rva2VuIiwidG9rZW5fdHlwZSI6IkJlYXJlciIsImF1dGhHcmFudElkIjoiSFpOdmNxaTBJb19UQXQ0RGoxQUk0bzUzWEhFIiwibm9uY2UiOiJkeWFlZnZ3dGJlYktEMjJFRlNvLXYxQU1SRFhfNU9CZml5d3NsZFp1MUgwIiwiYXVkIjoieHVpd2ViYXBwIiwibmJmIjoxNzMwMjIzMTIwLCJncmFudF90eXBlIjoiYXV0aG9yaXphdGlvbl9jb2RlIiwic2NvcGUiOlsib3BlbmlkIiwicHJvZmlsZSIsInJvbGVzIiwiY3JlYXRlLXVzZXIiLCJtYW5hZ2UtdXNlciIsInNlYXJjaC11c2VyIl0sImF1dGhfdGltZSI6MTczMDIyMzExOSwicmVhbG0iOiIvaG1jdHMiLCJleHAiOjE3MzAyNTE5MjAsImlhdCI6MTczMDIyMzEyMCwiZXhwaXJlc19pbiI6Mjg4MDAsImp0aSI6IkdnZ0N0SDNTZXZzV1JZOFpON3pQRXhSNUEtWSJ9.Bcv97P1XVk5uRIR1x5a927lxes_B4Ipuy0OFLflF9U4K5zJDn3VqFg0vA9xwFfi90FhjhUXmFbp5A_SEW00NTwzRdRfOzHr-2noUSn60sr4NftZXpg_eLxutUHnHbzyG89dQ3kgC0M8lr0JwHDKrrkiMflfbgHfZ9IdjekDUBGLAi3n2xZWQUCtACjLp4OuqALEdNGfAJI4l_AtXOKEpr5knnWJaN0-5ros9B9Ir5uOP8iHpcnbqN0fH-KIgyKHAKalN620T4GJAB9kcABb7tmga4xY3OMfvkHNLZrHkWbJez1haWg8WIJiuGtYSpAc-Aw72R9qGyL3l9u-ELWdfwg"
  )

  val headers_22 = Map(
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
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
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-workbasket-input-details.v2+json;charset=UTF-8",
    "Content-Type" -> "application/json",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_25 = Map(
    "Accept" -> "application/json",
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_26 = Map(
    "Accept" -> "image/avif,image/webp,image/png,image/svg+xml,image/*;q=0.8,*/*;q=0.5",
    "Priority" -> "u=4, i",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "image",
    "Sec-Fetch-Mode" -> "no-cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_27 = Map(
    "Accept" -> "application/json",
    "Content-Type" -> "application/json",
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "x-dtreferer" -> "https://manage-case.perftest.platform.hmcts.net/cases"
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
    "experimental" -> "true"
  )

  val headers_36 = Map(
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "x-dtreferer" -> "https://manage-case.perftest.platform.hmcts.net/cases/case-create/CIVIL/CIVIL/CREATE_CLAIM/CREATE_CLAIMNotifications"
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
    "experimental" -> "true"
  )

  val headers_54 = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
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

  val headers_56 = Map(
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_57 = Map(
    "If-Modified-Since" -> "Tue, 29 Oct 2024 16:28:58 GMT",
    "If-None-Match" -> """W/"7fe-192d91becf8:dtagent10299241001084140hsTy"""",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_58 = Map(
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_59 = Map(
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_60 = Map(
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_61 = Map(
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_64 = Map(
    "Accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json",
    "Content-Type" -> "application/json",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "experimental" -> "true"
  )

  val headers_65 = Map(
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_66 = Map(
    "Accept" -> "image/avif,image/webp,image/png,image/svg+xml,image/*;q=0.8,*/*;q=0.5",
    "If-Modified-Since" -> "Tue, 29 Oct 2024 16:28:49 GMT",
    "If-None-Match" -> """W/"269-192d91bc5e8"""",
    "Priority" -> "u=5",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "image",
    "Sec-Fetch-Mode" -> "no-cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_67 = Map(
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_68 = Map(
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "x-dtreferer" -> "https://manage-case.perftest.platform.hmcts.net/cases/case-details/#{caseId}"
  )

  val headers_69 = Map(
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "x-dtreferer" -> "https://manage-case.perftest.platform.hmcts.net/cases/case-details/#{caseId}"
  )

  val headers_70 = Map(
    "CSRF-Token" -> "BsvSO1AV-U5KEz_0qCR6Gh7JU0dsvuo8pqQ4",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "X-Requested-With" -> "XMLHttpRequest",
    "x-dtreferer" -> "https://manage-case.perftest.platform.hmcts.net/cases/case-details/#{caseId}"
  )

  val headers_71 = Map(
    "Priority" -> "u=0",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin"
  )

  val headers_72 = Map(
    "CSRF-Token" -> "xQ2xlp3i-kbRnQBMkIKYZT-rtMMrq8lVTSMY",
    "Content-Type" -> "application/json",
    "Origin" -> "https://manage-case.perftest.platform.hmcts.net",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "empty",
    "Sec-Fetch-Mode" -> "cors",
    "Sec-Fetch-Site" -> "same-origin",
    "X-Requested-With" -> "XMLHttpRequest"
  )

  val headers_75 = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
    "Priority" -> "u=0, i",
    "Referer" -> "https://manage-case.perftest.platform.hmcts.net/",
    "Sec-Fetch-Dest" -> "document",
    "Sec-Fetch-Mode" -> "navigate",
    "Sec-Fetch-Site" -> "same-site",
    "Sec-Fetch-User" -> "?1",
    "Upgrade-Insecure-Requests" -> "1"
  )

}