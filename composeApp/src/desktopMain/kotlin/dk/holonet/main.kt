package dk.holonet

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dk.holonet.config.BuildKonfig
import dk.holonet.core.HoloNetModule
import org.pf4j.DefaultPluginManager
import java.nio.file.Paths

fun main() = application {

    val pluginsDir = BuildKonfig.pluginsDir
    println("Plugins dir: $pluginsDir")
    val pluginManager = DefaultPluginManager(listOf(Paths.get(pluginsDir)))

    pluginManager.loadPlugins()
    pluginManager.startPlugins()

    val modules: List<HoloNetModule> = pluginManager.getExtensions(HoloNetModule::class.java)
    var module: HoloNetModule? = null

    println("Loaded modules: ${modules.size}")
    modules.forEach {
        module = it
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "HoloNet",
        undecorated = true,
    ) {
        App(module!!)
    }
}