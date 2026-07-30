package se.supernovait.anya.core.presentation.common.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.coroutines.delay
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.core.presentation.common.preview.ScreenPreviewContainer
import se.supernovait.anya.core.presentation.common.text.AnyaLabel
import se.supernovait.anya.core.presentation.util.Text
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun LoadingScreen(modifier: Modifier = Modifier, loadingText: Text? = null) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        focusManager.clearFocus()
        delay(50.milliseconds) // Add a little delay so clearFocus have time to trigger.
        focusRequester.requestFocus()
    }

    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EmbeddedLoadingIndicator(
                size = MaterialTheme.spacing.x2Large,
                modifier = Modifier.focusRequester(focusRequester)
            )
            loadingText?.let {
                AnyaLabel(
                    text = it.text,
                    modifier = Modifier.padding(MaterialTheme.spacing.medium)
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    ScreenPreviewContainer {
        LoadingScreen()
    }
}
