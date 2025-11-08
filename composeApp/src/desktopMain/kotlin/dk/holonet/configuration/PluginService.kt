package dk.holonet.configuration

import dk.holonet.core.HoloNetModule
import dk.holonet.core.HolonetConfiguration
import dk.holonet.core.getModulesToLoad
import dk.holonet.core.services.ConfigurationService
import dk.holonet.core.services.getPluginsFolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.pf4j.BasePluginLoader
import org.pf4j.ClassLoadingStrategy
import org.pf4j.CompoundPluginLoader
import org.pf4j.DefaultPluginClasspath
import org.pf4j.DefaultPluginManager
import org.pf4j.DevelopmentPluginLoader
import org.pf4j.JarPluginLoader
import org.pf4j.PluginClassLoader
import org.pf4j.PluginDescriptor
import org.pf4j.PluginLoader
import org.pf4j.util.FileUtils
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds
import java.nio.file.WatchKey

class PluginService(
    private val configurationService: ConfigurationService,
    private val coroutineScope: CoroutineScope
) {
    private lateinit var pluginManager: DefaultPluginManager
    private var watchKey: WatchKey? = null

    private val _modules = MutableSharedFlow<List<HoloNetModule>>(replay = 5)
    val modules = _modules.asSharedFlow()

    suspend fun initialize() {
        val pluginDirs = listOf(Paths.get(getPluginsFolder()))

        if (pluginDirs.none { Files.exists(it) }) {
            println("Creating plugins directory at ${pluginDirs.first()}")
            withContext(Dispatchers.IO) {
                Files.createDirectories(pluginDirs.first())
            }
        }

        pluginManager = HolonetPluginManager(pluginDirs)
        pluginManager.loadPlugins()
        pluginManager.startPlugins()

        // Start watching the plugins directory
        startWatchingPluginsFolder(pluginDirs.first())

        configurationService.fetchConfiguration()
        configurationService.cachedConfig.collect { config ->
            println("Configuration updated, reloading modules: $config")
            loadModules(config)
        }
    }

    private fun startWatchingPluginsFolder(pluginsPath: Path) {
        coroutineScope.launch(Dispatchers.IO) {
            val watchService = FileSystems.getDefault().newWatchService()
            watchKey = pluginsPath.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY
            )

            while (true) {
                val key = watchService.take() // Blocks until event occurs

                for (event in key.pollEvents()) {
                    val kind = event.kind()
                    val filename = event.context() as Path

                    println("Plugin directory change detected: $kind - $filename")

                    if (kind == StandardWatchEventKinds.ENTRY_CREATE ||
                        kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        loadPluginsNow()
                        break // Only reload once per batch of events
                    }
                }

                if (!key.reset()) {
                    break // Directory no longer accessible
                }
            }
        }
    }

    private fun loadPluginsNow() {
        pluginManager.loadPlugins()
        pluginManager.startPlugins()
    }

    fun dispose() {
        watchKey?.cancel()
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

private class HolonetPluginManager(pluginDirs: List<Path>) : DefaultPluginManager(pluginDirs) {
    override fun createPluginLoader(): PluginLoader {
        return CompoundPluginLoader()
            .add(CustomJarPluginLoader(this)) { this.isNotDevelopment }
    }
}

private class CustomJarPluginLoader(holonetPluginManager: HolonetPluginManager) :
    BasePluginLoader(holonetPluginManager, DefaultPluginClasspath()) {

    override fun isApplicable(pluginPath: Path): Boolean {
        return super.isApplicable(pluginPath) && FileUtils.isJarFile(pluginPath)
    }

    override fun createPluginClassLoader(pluginPath: Path?, pluginDescriptor: PluginDescriptor?): PluginClassLoader {
        val pluginClassLoader = PluginClassLoader(pluginManager, pluginDescriptor, javaClass.classLoader, ClassLoadingStrategy.ADP)
        pluginClassLoader.addFile(pluginPath?.toFile())
        return pluginClassLoader
    }
}