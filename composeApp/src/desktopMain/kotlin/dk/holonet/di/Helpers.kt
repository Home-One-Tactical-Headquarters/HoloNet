package dk.holonet.di

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

inline fun <reified T> getKoinInstance(): T {
    return object : KoinComponent {
        val value: T by inject()
    }.value
}

fun getHolonetFolder(): String = System.getProperty("user.home") + "/Holonet/"
fun getConfig(): String = getHolonetFolder() + "config.json"
fun getPluginsFolder(): String = getHolonetFolder() + "plugins/"