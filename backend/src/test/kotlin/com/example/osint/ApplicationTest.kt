package com.example.osint

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlin.test.*
import com.example.osint.db.DatabaseFactory
import org.koin.core.context.stopKoin

class ApplicationTest {
    @Test
    fun `GET api returns up message`() = testApplication {
        environment {
            config = MapApplicationConfig(
                "DB_PATH" to "jdbc:sqlite::memory:"
            )
        }
        application { module() }

        val response = client.get("/api")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("OSINT backend up!", response.bodyAsText())
    }

    @Test
    fun `POST api scans enqueues scan`() = testApplication {
        environment {
            config = MapApplicationConfig(
                "DB_PATH" to "jdbc:sqlite::memory:"
            )
        }
        application { module() }

        val response = client.post("/api/scans") {
            contentType(ContentType.Application.Json)
            setBody("""{"domain":"example.com"}""")
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(
            ContentType.Application.Json.withCharset(Charsets.UTF_8),
            response.contentType()
        )
        assertTrue(response.bodyAsText().contains("\"status\":\"finished\""))
    }
}