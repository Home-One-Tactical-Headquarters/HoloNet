package dk.holonet.di

import dk.holonet.ui.AppViewModel
import dk.holonet.configuration.PluginService
import dk.holonet.core.services.ConfigurationService
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.dsl.onClose


val diModules = module {
    single { HttpClient(CIO) }
    single {
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
    } onClose {
        it?.cancel()
    }
    single { ConfigurationService() }
    single { PluginService(get(), get()) } onClose {
        it?.dispose()
    }
    viewModel { AppViewModel(get()) }
}