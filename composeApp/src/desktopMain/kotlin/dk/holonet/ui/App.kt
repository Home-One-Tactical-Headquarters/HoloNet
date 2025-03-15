package dk.holonet.ui

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dk.holonet.components.BorderPane
import dk.holonet.core.HoloNetModule
import dk.holonet.core.Position
import dk.holonet.theme.HoloNetTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(
    viewModel: AppViewModel = koinViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    HoloNetTheme {
        Surface {
            when (val state = uiState) {
                is UiState.Loading -> {
                    // Show loading screen
                }
                is UiState.Loaded -> {
                    val modules = state.modules
                    BorderPane(
                        topBar = modules.getModulesByPosition(Position.TOP_BAR),
                        bottomBar = modules.getModulesByPosition(Position.BOTTOM_BAR),
                        topLeft = modules.getModulesByPosition(Position.TOP_LEFT),
                        topCenter = modules.getModulesByPosition(Position.TOP_CENTER),
                        topRight = modules.getModulesByPosition(Position.TOP_RIGHT),
                        upperThird = modules.getModulesByPosition(Position.UPPER_THIRD),
                        middleCenter = modules.getModulesByPosition(Position.MIDDLE_THIRD),
                        lowerThird = modules.getModulesByPosition(Position.LOWER_THIRD),
                        bottomLeft = modules.getModulesByPosition(Position.BOTTOM_LEFT),
                        bottomCenter = modules.getModulesByPosition(Position.BOTTOM_CENTER),
                        bottomRight = modules.getModulesByPosition(Position.BOTTOM_RIGHT)
                    )
                }
                is UiState.Error -> {}
            }

        }
    }
}

private fun List<HoloNetModule>.getModulesByPosition(position: Position): List<@Composable () -> Unit> {
    return this.filter { it.position == position }.sortedBy { it.priority }.map { { it.render() } }
}
