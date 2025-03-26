package dk.holonet.configuration

import dk.holonet.core.HoloNetModule
import dk.holonet.core.getModulesToLoad
import org.pf4j.DefaultPluginManager
import java.nio.file.Paths

class PluginService(
    private val configurationService: ConfigurationService
) {
    private lateinit var pluginManager: DefaultPluginManager

    fun initialize(pluginDirsString: String): PluginService {
        val pluginDirs = listOf(Paths.get(pluginDirsString))
        pluginManager = DefaultPluginManager(pluginDirs)
        pluginManager.loadPlugins()
        pluginManager.startPlugins()
        return this
    }

    suspend fun loadModules(): List<HoloNetModule> {
        val configuration = configurationService.fetchConfiguration()

        return pluginManager.startedPlugins
            .filter { plugin -> configuration.getModulesToLoad().contains(plugin.pluginId) }
            .flatMap { plugin ->
                pluginManager.getExtensions(HoloNetModule::class.java, plugin.pluginId).map { module ->
                    val moduleConfig = configuration.modules[plugin.pluginId]
                    module.configure(moduleConfig)
                    module
                }
            }
    }
}