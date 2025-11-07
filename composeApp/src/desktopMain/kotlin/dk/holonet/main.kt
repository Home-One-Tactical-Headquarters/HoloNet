package dk.holonet

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dk.holonet.di.diModules
import dk.holonet.ui.App
import dk.holonet.utils.createEmptyCursor
import org.koin.core.context.startKoin

fun main() = application {

    // Start Koin and load di modules
    startKoin {
        modules(diModules)
    }

    Window(
        onCloseRequest = {
            ::exitApplication.invoke()
        },
        title = "HoloNet",
        undecorated = true,
        state = rememberWindowState(placement = WindowPlacement.Fullscreen),
        onPreviewKeyEvent = {
            if (it.key == Key.Q) {
                ::exitApplication.invoke()
                true
            } else {
                false
            }
        }
    ) {
        App(modifier = Modifier.pointerHoverIcon(PointerIcon(createEmptyCursor())))
    }
}