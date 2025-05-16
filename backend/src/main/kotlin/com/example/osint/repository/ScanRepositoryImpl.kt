package com.example.osint.repository

import com.example.osint.api.dto.ScanDto
import com.example.osint.core.domain.Artifact
import com.example.osint.repository.Tables.Artifacts
import com.example.osint.repository.Tables.Scans
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

class ScanRepositoryImpl : ScanRepository {
    override fun create(domain: String): Long = transaction {
        val now = Instant.now().toString()
        Scans.insertAndGetId { row ->
            row[Scans.domain] = domain
            row[Scans.start]  = now
        }.value
    }

    override fun complete(id: Long, artifacts: List<Artifact>) = transaction {
        val now = Instant.now().toString()
        Scans.update({ Scans.id eq id }) {
            it[Scans.end] = now
        }
        artifacts.forEach { art ->
            Artifacts.insert { row ->
                row[Artifacts.scanId] = id
                row[Artifacts.value]  = art.value
            }
        }
    }

    override fun findDtoById(id: Long): ScanDto = transaction {
        val scanRow = Scans.select { Scans.id eq id }.single()
        val values  = Artifacts
            .select { Artifacts.scanId eq id }
            .map { it[Artifacts.value] }
        ScanDto(
            id      = id,
            domain  = scanRow[Scans.domain],
            start   = scanRow[Scans.start],
            end     = scanRow[Scans.end] ?: "",
            results = values.distinct()
        )
    }

    override fun findAllDto(): List<ScanDto> = transaction {
    Scans
        .selectAll()
        .map { scanRow ->
            val id     = scanRow[Scans.id].value
            val domain = scanRow[Scans.domain]
            val start  = scanRow[Scans.start]
            val end    = scanRow[Scans.end] ?: ""
            val results = Artifacts
                .select { Artifacts.scanId eq id }
                .map { it[Artifacts.value] }
                .distinct()
        ScanDto(id, domain, start, end, results)
    }
}}