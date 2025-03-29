package dk.holonet.configuration

import dk.holonet.core.HolonetConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File

class ConfigurationService {

    private val json = Json { ignoreUnknownKeys = true }

    private val _cachedConfig: MutableStateFlow<HolonetConfiguration?> = MutableStateFlow(null)
    val cachedConfig: StateFlow<HolonetConfiguration?> = _cachedConfig.asStateFlow()

    suspend fun fetchConfiguration(): HolonetConfiguration {
        return withContext(Dispatchers.IO) {

            // Load configuration if it exists in user.home
            val file = File(System.getProperty("user.home") + "/holonet/config.json")
            val configExists = file.exists() && file.isFile

            // If configuration exists, read it. Otherwise, use default configuration
            val jsonString: String = if (configExists) file.readText() else defaultConfigurationJson

            // Parse configuration
            val config = json.decodeFromString<HolonetConfiguration>(jsonString)

            // Cache configuration
            _cachedConfig.emit(config)

            // If configuration does not exist, write configuration
            if (!configExists) writeConfiguration(config)

            // Return configuration
            config
        }
    }

    private suspend fun writeConfiguration(configuration: HolonetConfiguration) {
        withContext(Dispatchers.IO) {
            val file = File(System.getProperty("user.home") + "/holonet/config.json")
            file.parentFile.mkdirs()
            file.writeText(json.encodeToString(configuration))
        }
    }

    suspend fun updateConfiguration(newConfig: HolonetConfiguration) {
        writeConfiguration(newConfig)
        fetchConfiguration()
    }
}

private val testJson: String = """
{
    "modules": {
        "clock": {
            "position": "top_left",
            "priority": 0
        },
        "calendar": {
            "position": "top_left",
            "priority": 1,
            "config": {
                "url": "https://calendar.google.com/calendar/ical/2vtvf94piqaq2fkc3eju74rjjc%40group.calendar.google.com/private-7feea036760e905ce7e11ead218b804a/basic.ics"
            }
        }
    }
}
""".trimIndent()

private val defaultConfigurationJson: String = """
{
    "modules": {
        "clock": {
            "position": "top_left",
            "priority": 0
        },
        "calendar": {
            "position": "top_left",
            "priority": 1,
            "config": {
                "url": "https://ics.calendarlabs.com/226/06a1cf11/UN_Holidays.ics"
            }
        }
    }
}
""".trimIndent()