package com.example.osint.core.domain

sealed interface Artifact {
    val value: String
}

data class Subdomain(override val value: String) : Artifact

data class Email(override val value: String) : Artifact