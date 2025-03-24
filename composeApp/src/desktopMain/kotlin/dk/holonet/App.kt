package dk.holonet

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import dk.holonet.components.BorderPane
import dk.holonet.core.HoloNetModule
import dk.holonet.core.Position
import dk.holonet.theme.HoloNetTheme

@Composable
fun App(modules: List<HoloNetModule>) {
    HoloNetTheme {
        Surface {
            BorderPane(
                topBar = modules.filter { it.position == Position.TOP_BAR }.map { { it.render() } },
                bottomBar = modules.filter { it.position == Position.BOTTOM_BAR }.map { { it.render() } },
                topLeft = modules.filter { it.position == Position.TOP_LEFT }.sortedBy { it.priority }.map { { it.render() } },
                topCenter = modules.filter { it.position == Position.TOP_CENTER }.map { { it.render() } },
                topRight = modules.filter { it.position == Position.TOP_RIGHT }.map { { it.render() } },
                upperThird = modules.filter { it.position == Position.UPPER_THIRD }.map { { it.render() } },
                middleCenter = modules.filter { it.position == Position.MIDDLE_THIRD }.map { { it.render() } },
                lowerThird = modules.filter { it.position == Position.LOWER_THIRD }.map { { it.render() } },
                bottomLeft = modules.filter { it.position == Position.BOTTOM_LEFT }.map { { it.render() } },
                bottomCenter = modules.filter { it.position == Position.BOTTOM_CENTER }.map { { it.render() } },
                bottomRight = modules.filter { it.position == Position.BOTTOM_RIGHT }.map { { it.render() } }
            )
        }
    }
}
