package dk.holonet

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dk.holonet.config.BuildKonfig
import dk.holonet.configuration.PluginService
import dk.holonet.core.HoloNetModule
import dk.holonet.di.diModules
import dk.holonet.di.getKoinInstance
import org.koin.core.context.startKoin

fun main() = application {

    // Start Koin and load di modules
    startKoin {
        modules(diModules)
    }

    val isLoading = remember { mutableStateOf(true) }
    val modules = remember { mutableStateOf<List<HoloNetModule>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            val pluginService: PluginService = getKoinInstance()
            modules.value = pluginService.initialize(BuildKonfig.pluginsDir).loadModules()
        } finally {
            isLoading.value = false
        }
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "HoloNet",
        undecorated = true,
        state = rememberWindowState(size = DpSize(900.dp, 1520.dp))
    ) {
        if (isLoading.value) {
            // Loading Screen
        } else {
            App(modules.value)
        }
    }
}