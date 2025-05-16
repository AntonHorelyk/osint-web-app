package com.example.osint.repository

import org.jetbrains.exposed.dao.id.LongIdTable

object Tables {
  object Scans : LongIdTable("scans") {
    val domain    = varchar("domain", 255)
    val start     = varchar("start_ts", 64)
    val end       = varchar("end_ts", 64).nullable()
  }

  object Artifacts : LongIdTable("artifacts") {
    val scanId = reference("scan_id", Scans)
    val value  = varchar("value", 1024)
  }
}
