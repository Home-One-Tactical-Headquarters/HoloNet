package dk.holonet.di

import dk.holonet.configuration.ConfigurationService
import dk.holonet.configuration.PluginService
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.koin.dsl.module


val diModules = module {
    single { HttpClient(CIO) }
    single { ConfigurationService() }
    single { PluginService(get()) }
}