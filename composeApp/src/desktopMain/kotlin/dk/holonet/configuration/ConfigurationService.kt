package dk.holonet.configuration

import dk.holonet.core.HolonetConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.*

class ConfigurationService {

    private val json = Json { ignoreUnknownKeys = true }
    private var cachedConfig: HolonetConfiguration? = null

    suspend fun fetchConfiguration(): HolonetConfiguration {
        return withContext(Dispatchers.IO) {
            val jsonString = testJson
            val config = json.decodeFromString<HolonetConfiguration>(jsonString)
            cachedConfig = config
            config
        }
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