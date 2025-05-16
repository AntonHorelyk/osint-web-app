package com.example.osint.adapters

import com.example.osint.core.domain.Artifact
import com.example.osint.core.domain.Subdomain
import com.example.osint.core.spi.ToolAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AmassAdapter : ToolAdapter {
    override suspend fun run(domain: String): List<Artifact> = withContext(Dispatchers.IO) {
        val proc = ProcessBuilder("amass", "enum", "-d", domain, "-o", "-")
            .redirectErrorStream(true)
            .start()
        val lines = proc.inputStream.bufferedReader().readLines()
        proc.waitFor()
        lines.filter { it.isNotBlank() }.map { Subdomain(it.trim()) }
    }
}