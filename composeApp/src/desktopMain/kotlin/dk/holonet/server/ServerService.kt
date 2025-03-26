package dk.holonet.server

import dk.holonet.configuration.ConfigurationService
import dk.holonet.core.HolonetConfiguration
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json

class ServerService(
    private val configurationService: ConfigurationService
) {

    private lateinit var server: EmbeddedServer<*, *>

    suspend fun start() {
        coroutineScope {
            server = embeddedServer(Netty, port = 8080, module = { module(configurationService) })
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

    routing {
        get("/") {
            val config: HolonetConfiguration = configurationService.cachedConfig ?: HolonetConfiguration()
            call.respond(config)
        }
    }
}