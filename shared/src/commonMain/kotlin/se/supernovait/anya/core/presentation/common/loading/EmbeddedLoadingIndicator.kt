package se.supernovait.anya.core.presentation.common.loading

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.core.presentation.common.preview.ComponentPreviewContainer

@Composable
fun EmbeddedLoadingIndicator(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    size: Dp = MaterialTheme.spacing.medium
) {
    CircularProgressIndicator(
        color = color,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
        strokeWidth = 1.dp,
        modifier = Modifier
            .size(size)
            .focusable(true)
            .then(modifier)
    )
}

@PreviewLightDark
@Composable
private fun Preview() {
    ComponentPreviewContainer {
        Column(verticalArrangement = Arrangement.Center) {
            EmbeddedLoadingIndicator()
        }
    }
}
