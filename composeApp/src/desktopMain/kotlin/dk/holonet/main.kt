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
import dk.holonet.configuration.ConfigurationService
import dk.holonet.core.HoloNetModule
import dk.holonet.core.getModulesToLoad
import dk.holonet.di.configurationModule
import dk.holonet.di.getKoinInstance
import dk.holonet.di.ktorModule
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.inject
import org.pf4j.DefaultPluginManager
import java.nio.file.Paths

fun main() = application {

    // Start Koin and load di modules
    startKoin {
        modules(ktorModule, configurationModule)
    }

    val configService: ConfigurationService = getKoinInstance()
    val isLoading = remember { mutableStateOf(true) }
    val modules = remember { mutableStateOf<List<HoloNetModule>>(emptyList()) }

    val pluginsDir = BuildKonfig.pluginsDir
    val pluginManager = DefaultPluginManager(listOf(Paths.get(pluginsDir)))

    pluginManager.loadPlugins()
    pluginManager.startPlugins()

    LaunchedEffect(Unit) {
        try {
            val configuration = configService.fetchConfiguration()

            val loadedModules = pluginManager.startedPlugins
                .filter { plugin -> configuration.getModulesToLoad().contains(plugin.pluginId) }
                .flatMap { plugin ->
                    pluginManager.getExtensions(HoloNetModule::class.java, plugin.pluginId).map { module ->
                        val moduleConfig = configuration.modules[plugin.pluginId]
                        module.configure(moduleConfig)
                        module
                    }
                }
                .toMutableList()

            modules.value = loadedModules
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