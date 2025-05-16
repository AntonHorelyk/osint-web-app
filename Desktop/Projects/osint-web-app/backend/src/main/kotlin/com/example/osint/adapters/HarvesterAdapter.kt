package com.example.osint.adapters

import com.example.osint.core.domain.Artifact
import com.example.osint.core.domain.Email
import com.example.osint.core.spi.ToolAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object HarvesterParser {
    fun parseJson(json: String): List<String> {
        val regex = Regex("\"email\":\"([^\"]+)\"")
        return regex.findAll(json).map { it.groupValues[1] }.toList()
    }
}

class HarvesterAdapter : ToolAdapter {
    override suspend fun run(domain: String): List<Artifact> = withContext(Dispatchers.IO) {
        val tmpFile = "/tmp/${domain}-${System.nanoTime()}.json"
        ProcessBuilder(
            "theHarvester",
            "-d", domain,
            "-b", "all",
            "-f", tmpFile
        ).start().waitFor()
        val json = File(tmpFile).readText()
        File(tmpFile).delete()
        HarvesterParser.parseJson(json).map { Email(it) }
    }
}