package dk.holonet

import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import dk.holonet.components.BorderPane
import dk.holonet.core.HoloNetModule
import dk.holonet.theme.HoloNetTheme

@Composable
fun App(centerModule: HoloNetModule) {
    HoloNetTheme {
        Surface {
            BorderPane(
                top = { Text("Top") },
                left = { Text("Left") },
                center = { centerModule.render() },
                right = { Text("Right") },
                bottom = { Text("Bottom") }
            )
        }
    }
}