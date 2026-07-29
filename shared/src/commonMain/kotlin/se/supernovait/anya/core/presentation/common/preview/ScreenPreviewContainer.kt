package se.supernovait.anya.core.presentation.common.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import se.supernovait.anya.app.presentation.app.theme.AnyaTheme

@Composable
fun ScreenPreviewContainer(content: @Composable () -> Unit) {
    AnyaTheme {
        Scaffold {
            Column(Modifier.padding(it).fillMaxWidth()) {
                content()
            }
        }
    }
}
