package dk.holonet

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dk.holonet.components.BorderPane
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
                        topBar = modules.filter { it.position == Position.TOP_BAR }.map { { it.render() } },
                        bottomBar = modules.filter { it.position == Position.BOTTOM_BAR }.map { { it.render() } },
                        topLeft = modules.filter { it.position == Position.TOP_LEFT }.sortedBy { it.priority }
                            .map { { it.render() } },
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
                is UiState.Error -> {}
            }

        }
    }
}
