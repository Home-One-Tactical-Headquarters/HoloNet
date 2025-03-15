package dk.holonet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dk.holonet.components.BorderPane
import dk.holonet.theme.HoloNetTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import holonet.composeapp.generated.resources.Res
import holonet.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    HoloNetTheme {
        Surface {
            BorderPane(
                top = { Text("Top") },
                left = { Text("Left") },
                center = { Text("Center") },
                right = { Text("Right") },
                bottom = { Text("Bottom") }
            )
        }
    }
}