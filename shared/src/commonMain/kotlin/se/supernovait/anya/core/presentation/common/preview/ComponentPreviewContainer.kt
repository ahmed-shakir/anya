package se.supernovait.anya.core.presentation.common.preview

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import se.supernovait.anya.app.presentation.app.theme.AnyaTheme

@Composable
fun ComponentPreviewContainer(content: @Composable () -> Unit) {
    AnyaTheme {
        Surface {
            content()
        }
    }
}
