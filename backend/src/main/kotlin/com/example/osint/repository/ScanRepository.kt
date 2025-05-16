package com.example.osint.repository

import com.example.osint.core.domain.Artifact
import com.example.osint.api.dto.ScanDto

interface ScanRepository {
    fun create(domain: String): Long
    fun complete(id: Long, artifacts: List<Artifact>)
    fun findDtoById(id: Long): ScanDto
    fun findAllDto(): List<ScanDto>
}