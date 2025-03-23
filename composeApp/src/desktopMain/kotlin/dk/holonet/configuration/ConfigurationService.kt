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
            "position": "center",
            "config": {
                "key1": "Hello,",
                "key2": "World!"
            }
        },
        "calendar": {
            "position": "left",
            "config": {
                "type": "meat-and-filler",
                "sentences": "5"
            }
        }
    }
}
""".trimIndent()