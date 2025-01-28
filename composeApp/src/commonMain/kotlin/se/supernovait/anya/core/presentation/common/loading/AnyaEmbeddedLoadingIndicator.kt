package se.supernovait.anya.core.presentation.common.loading

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import se.supernovait.anya.ui.theme.spacing

@Composable
fun AnyaEmbeddedLoadingIndicator(modifier: Modifier = Modifier, color: Color = Color.White) {
    CircularProgressIndicator(
        modifier = Modifier.size(MaterialTheme.spacing.medium).then(modifier),
        strokeWidth = 1.dp,
        color = color,
        trackColor = MaterialTheme.colorScheme.surfaceVariant
    )
}
