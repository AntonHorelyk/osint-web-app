package com.example.osint

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.jackson.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.plugin.Koin
import org.koin.ktor.ext.inject
import com.example.osint.di.appModule
import com.example.osint.service.ScanService
import com.example.osint.db.DatabaseFactory

fun Application.module() {
    install(Koin) { modules(appModule) }
    install(ContentNegotiation) { jackson() }
    install(CORS) {
        allowHost("*")
        allowMethod(HttpMethod.Post)
        allowHeader(HttpHeaders.ContentType)
    }
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respond(
                HttpStatusCode.InternalServerError,
                mapOf("error" to (cause.localizedMessage ?: "Unknown error"))
            )
        }
    }

    // default to in-memory for tests if DB_PATH not set
    val dbPath = System.getenv("DB_PATH") ?: ":memory:"
    DatabaseFactory.init("jdbc:sqlite:$dbPath")

    val scanService by inject<ScanService>()

    routing {
        route("/api") {
            get {
                call.respondText("OSINT backend up!")
            }
            get("/scans") {
                call.respond(scanService.getAll())
            }
            post("/scans") {
                val payload = call.receive<Map<String, String>>()
                val raw = payload["domain"] ?: throw IllegalArgumentException("Domain is required")
                val domain = runCatching { java.net.URI(raw).host }
                    .getOrNull()?.takeIf { it.isNotBlank() } ?: raw
                require(Regex("^(?:[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?\\.)+[A-Za-z]{2,}\$")
                    .matches(domain)) { "Invalid domain name: $domain" }
                call.respond(scanService.scan(domain))
            }
            get("/scans/{id}") {
                val id = call.parameters["id"]?.toLongOrNull() ?: throw IllegalArgumentException("Invalid id")
                call.respond(scanService.getById(id))
            }
        }
    }
}

fun main() {
    embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)
}