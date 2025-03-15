package dk.holonet.server

import dk.holonet.configuration.ConfigurationService
import dk.holonet.core.HolonetConfiguration
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.singlePageApplication
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json

class ServerService(
    private val configurationService: ConfigurationService
) {

    private lateinit var server: EmbeddedServer<*, *>

    suspend fun start() {
        coroutineScope {
            server = embeddedServer(Netty, port = 8081, module = { module(configurationService) })
            server.start(wait = true)
        }
    }

    fun stop() {
        server.stop(1000, 1000)
    }
}

fun Application.module(
    configurationService: ConfigurationService
) {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }

    install(CORS) {
        anyHost() // TODO: Replace with specific host
        allowCredentials = true
        allowHeaders { true }
        allowHeader(HttpHeaders.ContentType)
    }

    routing {
        singlePageApplication {
            useResources = true
            filesPath = "HomeOne"
            defaultPage = "index.html"
        }

        /*get("/") {
            val config: HolonetConfiguration = configurationService.cachedConfig.value ?: HolonetConfiguration()
            call.respond(config)
        }*/

        post("/update") {
            val newConfig: HolonetConfiguration = call.receive()
            configurationService.updateConfiguration(newConfig)
            call.respond(HttpStatusCode.OK)
        }

    }
}