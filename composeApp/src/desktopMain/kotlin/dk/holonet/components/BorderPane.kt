package dk.holonet.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BorderPane(
    top: @Composable () -> Unit,
    left: @Composable () -> Unit,
    center: @Composable () -> Unit,
    right: @Composable () -> Unit,
    bottom: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(16.dp)
    ) {

        // Top Bar
        Box(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            top()
        }

        Row(modifier = Modifier.weight(1f)) {
            // Left Sidebar
            Box(
                modifier = Modifier.fillMaxHeight().wrapContentWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                left()
            }

            // Center Content
            Box(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                center()
            }

            // Right Sidebar
            Box(
                modifier = Modifier.fillMaxHeight().wrapContentWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                right()
            }
        }

        // Bottom Bar
        Box(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            bottom()
        }

    }

}