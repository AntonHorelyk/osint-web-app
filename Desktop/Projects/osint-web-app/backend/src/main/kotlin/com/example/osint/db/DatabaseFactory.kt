package com.example.osint.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import com.example.osint.repository.Tables

object DatabaseFactory {
  fun init(dbUrl: String) {
    val config = HikariConfig().apply {
      jdbcUrl = dbUrl
      driverClassName = "org.sqlite.JDBC"
      maximumPoolSize = 3
      isAutoCommit = false
      transactionIsolation = "TRANSACTION_SERIALIZABLE"
    }
    val ds = HikariDataSource(config)
    Database.connect(ds)
    transaction {
      SchemaUtils.createMissingTablesAndColumns(Tables.Scans, Tables.Artifacts)
    }
  }
}