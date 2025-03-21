package dk.holonet

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dk.holonet.config.BuildKonfig
import dk.holonet.configuration.ConfigurationService
import dk.holonet.core.HoloNetModule
import dk.holonet.core.getModulesToLoad
import dk.holonet.di.ktorModule
import org.koin.core.context.startKoin
import org.pf4j.DefaultPluginManager
import java.nio.file.Paths

fun main() = application {

    // Start Koin and load di modules
    startKoin {
        modules(ktorModule)
    }

    val configService = ConfigurationService()
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
    ) {
        if (isLoading.value) {
            // Loading Screen
        } else {
            App(modules.value)
        }
    }
}