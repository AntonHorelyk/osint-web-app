package com.example.osint

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.jackson.*
import io.ktor.server.routing.*
import io.ktor.server.response.*

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) { jackson() }

        routing {
           get("/api")  { call.respondText("OSINT backend up!") }
    get("/api/") { call.respondText("OSINT backend up!") }
}
    }.start(wait = true)
}