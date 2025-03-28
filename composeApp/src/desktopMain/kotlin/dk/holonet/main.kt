package dk.holonet

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dk.holonet.di.diModules
import org.koin.core.context.startKoin

fun main() = application {

    // Start Koin and load di modules
    startKoin {
        modules(diModules)
    }

    Window(
        onCloseRequest = {
//            appViewModel.stopServer() // TODO: Add stop server method
            ::exitApplication.invoke()
        },
        title = "HoloNet",
        undecorated = true,
        state = rememberWindowState(size = DpSize(900.dp, 1520.dp))
    ) {
        App()
    }
}