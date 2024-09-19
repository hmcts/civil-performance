package uk.gov.hmcts.reform.civildamage.performance.scenarios.utils

object CivilDamagesHeader {
 val baseURL = Environment.baseURL
 val IdamUrl = Environment.idamURL
 
 
 //below are the headers
 
 val headers_viewAndRespond = Map(
  "accept" -> "application/json",
  "accept-encoding" -> "gzip, deflate, br",
  "accept-language" -> "en-US,en;q=0.9",
  "content-type" -> "application/json",
  "sec-ch-ua" -> """Google Chrome";v="89", "Chromium";v="89", ";Not A Brand";v="99""",
  "sec-ch-ua-mobile" -> "?0",
  "sec-fetch-dest" -> "empty",
  "sec-fetch-mode" -> "cors",
  "sec-fetch-site" -> "same-origin")
 
 val headers_viewAndRespondintent = Map(
  "accept-encoding" -> "gzip, deflate, br",
  "accept-language" -> "en-US,en;q=0.9",
  "content-type" -> "application/json",
  "sec-ch-ua" -> """Google Chrome";v="89", "Chromium";v="89", ";Not A Brand";v="99""",
  "sec-ch-ua-mobile" -> "?0",
  "sec-fetch-dest" -> "empty",
  "sec-fetch-mode" -> "cors",
  "sec-fetch-site" -> "same-origin")
 
 val MoneyClaimNavHeader = Map(
  "accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
  "accept-encoding" -> "gzip, deflate, br",
  "accept-language" -> "en-GB,en-US;q=0.9,en;q=0.8",
  "content-type" -> "text/html; charset=utf-8",
  "sec-fetch-dest" -> "document",
  "sec-fetch-mode" -> "navigate",
  "sec-fetch-site" -> "none"
 )


 val MoneyClaimPostHeader = Map(
  "accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
  "accept-encoding" -> "gzip, deflate, br",
  "accept-language" -> "en-US,en;q=0.9",
  "content-type" -> "application/json",
  "experimental" -> "true",
  "sec-ch-ua" -> """Google Chrome";v="89", "Chromium";v="89", ";Not A Brand";v="99""",
  "sec-ch-ua-mobile" -> "?0",
  "sec-fetch-dest" -> "empty",
  "sec-fetch-mode" -> "cors",
  "sec-fetch-site" -> "same-origin"
 )


 val CitizenSTUpload = Map(
  "accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
  "accept-encoding" -> "gzip, deflate, br",
  "accept-language" -> "en-GB,en-US;q=0.9,en;q=0.8",
  "content-type" -> "multipart/form-data; boundary=----WebKitFormBoundarylzXPkPZGjjxL8byV",
  "experimental" -> "true",
  "sec-ch-ua" -> """Google Chrome";v="117", "Not;A=Brand";v="8", "Chromium";v="117""",
  "sec-ch-ua-mobile" -> "?0",
  "sec-fetch-dest" -> "empty",
  "sec-fetch-mode" -> "cors",
  "sec-fetch-site" -> "same-origin"
 )
 
 val CUIR2Get = Map(
  "accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
  "accept-encoding" -> "gzip,deflate,br,zstd",
  "accept-language" -> "en-US,en;q=0.9",
  "sec-fetch-dest" -> "document",
  "sec-fetch-mode" -> "navigate",
  "sec-fetch-site" -> "same-origin",
  "content-type" -> "application/x-www-form-urlencoded",
  "Upgrade-Insecure-Requests" -> "1",
  "Sec-Fetch-User" -> "?1"
 )
 
 val CUIR2Post = Map(
  "accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
  "accept-encoding" -> "gzip,deflate,br,zstd",
  "accept-language" -> "en-US,en;q=0.9",
  "content-type" -> "application/x-www-form-urlencoded",
  "sec-fetch-dest" -> "document",
  "sec-fetch-mode" -> "navigate",
  "sec-fetch-site" -> "same-origin",
  "Sec-Fetch-User" -> "?1",
  "Upgrade-Insecure-Requests" -> "1"
 )

 val MoneyClaimDefPostHeader = Map(
  "accept-encoding" -> "gzip, deflate, br",
  "accept-language" -> "en-GB,en;q=0.9",
  "content-type" -> "application/json",
  "sec-fetch-dest" -> "empty",
  "sec-fetch-mode" -> "cors",
  "sec-fetch-site" -> "same-origin"
 )


 val MoneyClaimPostHeader2 = Map(
  "accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
  "accept-encoding" -> "gzip, deflate, br",
  "accept-language" -> "en-GB,en-US;q=0.9,en;q=0.8",
  "content-type" -> "application/json",
  "sec-fetch-dest" -> "empty",
  "sec-fetch-mode" -> "cors",
  "sec-fetch-site" -> "same-origin"
 )
 
 val DefCheckAndSendPost = Map(
  "accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
  "accept-encoding" -> "gzip, deflate, br",
  "accept-language" -> "en-GB,en-US;q=0.9,en;q=0.8",
  "content-type" -> "application/x-www-form-urlencoded",
  "sec-fetch-dest" -> "document",
  "sec-fetch-mode" -> "navigate",
  "sec-fetch-site" -> "same-origin"
 )


 val MoneyClaimSubmitHeader = Map(
  "accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-case.v2+json;charset=UTF-8",
  "accept-encoding" -> "gzip, deflate, br",
  "accept-language" -> "en-GB,en-US;q=0.9,en;q=0.8",
  "content-type" -> "application/json",
  "sec-fetch-dest" -> "empty",
  "sec-fetch-mode" -> "cors",
  "sec-fetch-site" -> "same-origin"
 )

 val MoneyClaimSignInHeader = Map(
  "accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
  "accept-encoding" -> "gzip, deflate, br",
  "accept-language" -> "en-GB,en-US;q=0.9,en;q=0.8",
  "content-type" -> "application/x-www-form-urlencoded",
  "sec-fetch-dest" -> "document",
  "sec-fetch-mode" -> "navigate",
  "sec-fetch-site" -> "same-origin"
 )


 val MoneyClaimNav = Map(
  "accept" -> "application/json",
  "accept-encoding" -> "gzip, deflate, br",
  "accept-language" -> "en-GB,en-US;q=0.9,en;q=0.8",
  "content-type" -> "application/json",
  "sec-fetch-dest" -> "empty",
  "sec-fetch-mode" -> "cors",
  "sec-fetch-site" -> "same-origin"
 )

 val CivilCitizenPost = Map(
  "accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
  "accept-encoding" -> "gzip, deflate, br, zstd",
  "accept-language" -> "en-US,en;q=0.9",
  "content-type" -> "application/x-www-form-urlencoded",
  "sec-fetch-dest" -> "document",
  "sec-fetch-mode" -> "navigate",
  "sec-fetch-site" -> "same-origin"
 )
 
 val CUIR2GetTest = Map(
  "accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
  "accept-encoding" -> "gzip,deflate,br,zstd",
  "accept-language" -> "en-US,en;q=0.9",
  "sec-fetch-dest" -> "document",
  "sec-fetch-mode" -> "navigate",
  "sec-fetch-site" -> "same-origin",
  "Upgrade-Insecure-Requests" -> "1",
  "Sec-Fetch-User" -> "?1",
  "sec-ch-ua-mobile" -> "?0",
  "sec-ch-ua-platform" -> "windows"
 )
 
 val CUIR2Get = Map(
  "accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
  "accept-encoding" -> "gzip,deflate,br,zstd",
  "accept-language" -> "en-US,en;q=0.9",
  "sec-fetch-dest" -> "document",
  "sec-fetch-mode" -> "navigate",
  "sec-fetch-site" -> "same-origin",
  "content-type" -> "application/x-www-form-urlencoded",
  "Upgrade-Insecure-Requests" -> "1",
  "Sec-Fetch-User" -> "?1"
 )
 
 val CUIR2Post = Map(
  "accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
  "accept-encoding" -> "gzip,deflate,br,zstd",
  "accept-language" -> "en-US,en;q=0.9",
  "content-type" -> "application/x-www-form-urlencoded",
  "sec-fetch-dest" -> "document",
  "sec-fetch-mode" -> "navigate",
  "sec-fetch-site" -> "same-origin",
 "Sec-Fetch-User" -> "?1",
  "Upgrade-Insecure-Requests" -> "1"
 )

 val CaseBundleCreation = Map(
  "accept" -> "*/*",
  "accept-encoding" -> "gzip, deflate, br",
  "accept-language" -> "en-GB,en-US;q=0.9,en;q=0.8",
  "content-type" -> "text/plain;charset=UTF-8",
  "sec-fetch-dest" -> "empty",
  "sec-fetch-mode" -> "cors",
  "sec-fetch-site" -> "same-origin"
 )


 val CivilNav = Map(
  "accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8",
  "accept-encoding" -> "gzip, deflate, br",
  "accept-language" -> "en-GB,en-US;q=0.9,en;q=0.8",
  "content-type" -> "application/json",
  "sec-fetch-dest" -> "empty",
  "sec-fetch-mode" -> "cors",
  "sec-fetch-site" -> "same-origin"
 )


 val headers_149 = Map(
  "accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-user-profile.v2+json;charset=UTF-8",
  "accept-encoding" -> "gzip, deflate, br",
  "accept-language" -> "en-US,en;q=0.9",
  "content-type" -> "application/json",
  "experimental" -> "true",
  "sec-ch-ua" -> """Google Chrome";v="89", "Chromium";v="89", ";Not A Brand";v="99""",
  "sec-ch-ua-mobile" -> "?0",
  "sec-fetch-dest" -> "empty",
  "sec-fetch-mode" -> "cors",
  "sec-fetch-site" -> "same-origin")
 
 val headers_658 = Map(
  "accept" -> "application/json, text/plain, */*",
  "accept-encoding" -> "gzip, deflate, br",
  "accept-language" -> "en-US,en;q=0.9",
  "sec-ch-ua" -> """Google Chrome";v="89", "Chromium";v="89", ";Not A Brand";v="99""",
  "sec-ch-ua-mobile" -> "?0",
  "sec-fetch-dest" -> "empty",
  "sec-fetch-mode" -> "cors",
  "sec-fetch-site" -> "same-origin",
  "x-dtpc" -> "1$290942374_968h89vNLRICPHQRUAGGMULUVVIPDFRFSJFRNKR-0e29",
  "x-dtreferer" -> ({baseURL+"/cases/case-create/CIVIL/UNSPECIFIED_CLAIMS/CREATE_CLAIM/CREATE_CLAIMStatementOfTruth"})
 )
 
 
 val headers_13 = Map (
  "accept" -> "application/json, text/plain, */*",
  "accept-encoding" -> "gzip, deflate, br",
  "accept-language" -> "en-US,en;q=0.9",
  "sec-ch-ua" -> """Google Chrome";v="89", "Chromium";v="89", ";Not A Brand";v="99""",
  "sec-ch-ua-mobile" -> "?0",
  "sec-fetch-dest" -> "empty",
  "sec-fetch-mode" -> "cors",
  "sec-fetch-site" -> "same-origin")
 
 val headers_104 = Map (
  "accept" -> "application/json",
  "accept-encoding" -> "gzip, deflate, br",
  "accept-language" -> "en-US,en;q=0.9",
  "content-type" -> "application/json",
  "sec-ch-ua" -> """Google Chrome";v="89", "Chromium";v="89", ";Not A Brand";v="99""",
  "sec-ch-ua-mobile" -> "?0",
  "sec-fetch-dest" -> "empty",
  "sec-fetch-mode" -> "cors",
  "sec-fetch-site" -> "same-origin")
 
 val headers_140 = Map (
  "accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-case-trigger.v2+json;charset=UTF-8",
  "accept-encoding" -> "gzip, deflate, br",
  "accept-language" -> "en-US,en;q=0.9",
  "content-type" -> "application/json",
  "experimental" -> "true",
  "sec-ch-ua" -> """Google Chrome";v="89", "Chromium";v="89", ";Not A Brand";v="99""",
  "sec-ch-ua-mobile" -> "?0",
  "sec-fetch-dest" -> "empty",
  "sec-fetch-mode" -> "cors",
  "sec-fetch-site" -> "same-origin")
 
 val headers_ViewResponseevent = Map(
  "accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-event-trigger.v2+json;charset=UTF-8",
  "accept-encoding" -> "gzip, deflate, br",
  "accept-language" -> "en-US,en;q=0.9",
  "content-type" -> "application/json",
  "experimental" -> "true",
  "sec-ch-ua" -> """Google Chrome";v="89", "Chromium";v="89", ";Not A Brand";v="99""",
  "sec-ch-ua-mobile" -> "?0",
  "sec-fetch-dest" -> "empty",
  "sec-fetch-mode" -> "cors",
  "sec-fetch-site" -> "same-origin")
 
 val headers_ViewRespondintent = Map(
  "accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.case-data-validate.v2+json;charset=UTF-8",
  "accept-encoding" -> "gzip, deflate, br",
  "accept-language" -> "en-US,en;q=0.9",
  "content-type" -> "application/json",
  "experimental" -> "true",
  "sec-ch-ua" -> """Google Chrome";v="89", "Chromium";v="89", ";Not A Brand";v="99""",
  "sec-ch-ua-mobile" -> "?0",
  "sec-fetch-dest" -> "empty",
  "sec-fetch-mode" -> "cors",
  "sec-fetch-site" -> "same-origin")
 
 val headers_intentsubmit = Map(
  "accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-event.v2+json;charset=UTF-8",
  "accept-encoding" -> "gzip, deflate, br",
  "accept-language" -> "en-US,en;q=0.9",
  "content-type" -> "application/json",
  "experimental" -> "true",
  "origin" -> baseURL,
  "sec-ch-ua" -> """Google Chrome";v="89", "Chromium";v="89", ";Not A Brand";v="99""",
  "sec-ch-ua-mobile" -> "?0",
  "sec-fetch-dest" -> "empty",
  "sec-fetch-mode" -> "cors",
  "sec-fetch-site" -> "same-origin",
 "x-xsrf-token" -> "${XSRFToken}")
 
  
 
 
 val CUI_Submit = Map(
  "accept" -> "application/vnd.uk.gov.hmcts.ccd-data-store-api.create-case.v2+json;charset=UTF-8",
  "accept-encoding" -> "gzip, deflate, br",
  "accept-language" -> "en-GB,en-US;q=0.9,en;q=0.8",
  "content-type" -> "application/json",
  "experimental" -> "true",
  "origin" -> baseURL,
  "sec-ch-ua" -> """Google Chrome";v="89", "Chromium";v="89", ";Not A Brand";v="99""",
  "sec-ch-ua-mobile" -> "?0",
  "sec-fetch-dest" -> "empty",
  "sec-fetch-mode" -> "cors",
  "sec-fetch-site" -> "same-origin",
  "Request-Context" -> "appId=cid-v1:7922b140-fa5f-482d-89b4-e66e9e6d675a",
  "Request-Id"   -> "|${requestId}.\"+Common.getRequestId()",
  "X-Xsrf-Token" -> "${XSRFToken}")
 

 
 
 
 
 

 
 

 
}
