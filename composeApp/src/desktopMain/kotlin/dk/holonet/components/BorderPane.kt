package dk.holonet.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BorderPane(
    topBar: List<@Composable () -> Unit>,
    bottomBar: List<@Composable () -> Unit>,
    topLeft: List<@Composable () -> Unit>,
    topCenter: List<@Composable () -> Unit>,
    topRight: List<@Composable () -> Unit>,
    upperThird: List<@Composable () -> Unit>,
    middleCenter: List<@Composable () -> Unit>,
    lowerThird: List<@Composable () -> Unit>,
    bottomLeft: List<@Composable () -> Unit>,
    bottomCenter: List<@Composable () -> Unit>,
    bottomRight: List<@Composable () -> Unit>
) {
    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Top bar (light gray), pinned to the top
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)          // Adjust to preferred height
                .align(Alignment.TopCenter)
        ) {
            Column {
                repeat(topBar.size) {
                    topBar[it]()
                }
            }
        }

        // Bottom bar (light gray), pinned to the bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)          // Adjust to preferred height
                .align(Alignment.BottomCenter)
        ) {
            Column {
                repeat(bottomBar.size) {
                    bottomBar[it]()
                }
            }
        }

        // Main content column, taking the remaining space between top/bottom bars
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp, bottom = 48.dp) // Same height used for top/bottom bars
        ) {
            // ----------------------
            // Top row with 3 blocks
            // ----------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()      // top_left
                ) {
                    Column {
                        repeat(topLeft.size) {
                            topLeft[it]()
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()    // top_center
                ) {
                    Column {
                        repeat(topCenter.size) {
                            topCenter[it]()
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()    // top_right
                ) {
                    Column {
                        repeat(topRight.size) {
                            topRight[it]()
                        }
                    }
                }
            }

            // ---------------------------------------
            // Middle stack: upper_third, middle_center,
            // lower_third
            // ---------------------------------------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // You can adjust the weight to change relative height
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()   // upper_third
                ) {
                    Column {
                        repeat(upperThird.size) {
                            upperThird[it]()
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()     // middle_center
                ) {
                    Column {
                        repeat(middleCenter.size) {
                            middleCenter[it]()
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()  // lower_third
                ) {
                    Column {
                        repeat(lowerThird.size) {
                            lowerThird[it]()
                        }
                    }
                }
            }

            // -------------------------
            // Bottom row with 3 blocks
            // -------------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()      // bottom_left
                ) {
                    Column {
                        repeat(bottomLeft.size) {
                            bottomLeft[it]()
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()     // bottom_center
                ) {
                    Column {
                        repeat(bottomCenter.size) {
                            bottomCenter[it]()
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()    // bottom_right
                ) {
                    Column {
                        repeat(bottomRight.size) {
                            bottomRight[it]()
                        }
                    }
                }
            }
        }
    }
}