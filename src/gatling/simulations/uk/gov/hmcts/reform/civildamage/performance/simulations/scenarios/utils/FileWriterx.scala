package uk.gov.hmcts.reform.civildamage.performance.simulations.scenarios.utils

import java.io.{BufferedWriter, FileWriter}
import io.gatling.core.Predef._


object FileWriterx{

  val WriteToFile =
    exec { session =>
        val fw = new BufferedWriter(new FileWriter("caseIds.csv", true))
        try {
          fw.write(session("caseId").as[String] + "\r\n")
        } finally fw.close()
        session
      }
}