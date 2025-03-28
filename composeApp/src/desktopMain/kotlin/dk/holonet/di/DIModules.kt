package dk.holonet.di

import dk.holonet.ui.AppViewModel
import dk.holonet.configuration.ConfigurationService
import dk.holonet.configuration.PluginService
import dk.holonet.server.ServerService
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val diModules = module {
    single { HttpClient(CIO) }
    single { ConfigurationService() }
    single { PluginService(get()) }
    single { ServerService(get())}
    viewModel { AppViewModel(get(), get()) }
}