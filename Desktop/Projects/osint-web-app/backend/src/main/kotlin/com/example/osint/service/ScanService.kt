package com.example.osint.service

import com.example.osint.core.spi.ToolAdapter
import com.example.osint.repository.ScanRepository
import com.example.osint.api.dto.ScanDto
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class ScanService(
    private val adapters: List<ToolAdapter>,
    private val repo: ScanRepository
) {
    suspend fun scan(domain: String): ScanDto = coroutineScope {
        val id = repo.create(domain)
        val jobs = adapters.map { async { it.run(domain) } }
        val artifacts = jobs.awaitAll().flatten()
        repo.complete(id, artifacts)
        repo.findDtoById(id)
    }

    fun getById(id: Long): ScanDto =
        repo.findDtoById(id)

    fun getAll(): List<ScanDto> =
        repo.findAllDto()
}