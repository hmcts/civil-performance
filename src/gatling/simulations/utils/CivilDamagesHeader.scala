package utils

object CivilDamagesHeader {

 val baseURL = Environment.baseURL
 val IdamUrl = Environment.idamURL

 //below are the headers

 val MoneyClaimNavHeader = Map(
  "accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
  "accept-encoding" -> "gzip, deflate, br",
  "accept-language" -> "en-GB,en-US;q=0.9,en;q=0.8",
  "content-type" -> "text/html; charset=utf-8",
  "sec-fetch-dest" -> "document",
  "sec-fetch-mode" -> "navigate",
  "sec-fetch-site" -> "none"
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

}