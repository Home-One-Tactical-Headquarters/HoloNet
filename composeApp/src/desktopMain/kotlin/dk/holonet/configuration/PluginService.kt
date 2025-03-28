package dk.holonet.configuration

import dk.holonet.core.HoloNetModule
import dk.holonet.core.HolonetConfiguration
import dk.holonet.core.getModulesToLoad
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.pf4j.DefaultPluginManager
import java.nio.file.Paths

class PluginService(
    private val configurationService: ConfigurationService
) {
    private lateinit var pluginManager: DefaultPluginManager

    private val _modules = MutableSharedFlow<List<HoloNetModule>>(replay = 5)
    val modules = _modules.asSharedFlow()

    suspend fun initialize(pluginDirsString: String) {
        val pluginDirs = listOf(Paths.get(pluginDirsString))
        pluginManager = DefaultPluginManager(pluginDirs)
        pluginManager.loadPlugins()
        pluginManager.startPlugins()

        configurationService.fetchConfiguration()
        configurationService.cachedConfig.collect { config ->
            loadModules(config)
        }
    }

    private suspend fun loadModules(config: HolonetConfiguration?) {
        if (config == null) {
            println("No configuration found, skipping module loading")
            return
        }
        val loadedModules = pluginManager.startedPlugins
            .filter { plugin -> config.getModulesToLoad().contains(plugin.pluginId) }
            .flatMap { plugin ->
                pluginManager.getExtensions(HoloNetModule::class.java, plugin.pluginId).map { module ->
                    val moduleConfig = config.modules[plugin.pluginId]
                    module.configure(moduleConfig)
                    module
                }
            }

        _modules.emit(loadedModules)
    }
}