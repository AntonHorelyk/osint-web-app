package com.example.osint.api.dto

data class ScanDto(
    val id: Long,
    val domain: String,
    val start: String,
    val end: String,
    val results: List<String>
)