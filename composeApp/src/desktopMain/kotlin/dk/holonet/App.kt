package dk.holonet

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import dk.holonet.components.BorderPane
import dk.holonet.core.Position
import dk.holonet.core.HoloNetModule
import dk.holonet.theme.HoloNetTheme

@Composable
fun App(modules: List<HoloNetModule>) {
    HoloNetTheme {
        Surface {
            BorderPane(
                top = { modules.firstOrNull { it.position == Position.TOP }?.render() },
                left = { modules.firstOrNull { it.position == Position.LEFT }?.render() },
                center = { modules.firstOrNull { it.position == Position.CENTER }?.render() },
                right = { modules.firstOrNull { it.position == Position.RIGHT }?.render() },
                bottom = { modules.firstOrNull { it.position == Position.BOTTOM }?.render() }
            )
        }
    }
}