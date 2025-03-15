package dk.holonet.modules

import androidx.compose.runtime.Composable

interface Module {
    val id: String
    val config: ModuleConfig

    @Composable
    fun render()
}