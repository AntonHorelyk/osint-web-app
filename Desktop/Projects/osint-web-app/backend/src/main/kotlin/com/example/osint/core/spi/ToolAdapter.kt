package com.example.osint.core.spi

import com.example.osint.core.domain.Artifact

interface ToolAdapter {
    suspend fun run(domain: String): List<Artifact>
}