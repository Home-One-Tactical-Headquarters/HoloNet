package dk.holonet

import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import dk.holonet.components.BorderPane
import dk.holonet.modules.examples.ClockModule
import dk.holonet.theme.HoloNetTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun App() {
    HoloNetTheme {
        Surface {
            BorderPane(
                top = { Text("Top") },
                left = { Text("Left") },
                center = { ClockModule().render() },
                right = { Text("Right") },
                bottom = { Text("Bottom") }
            )
        }
    }
}