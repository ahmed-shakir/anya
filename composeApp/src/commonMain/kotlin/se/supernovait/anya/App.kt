package se.supernovait.anya

import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import se.supernovait.anya.app.presentation.AnyaApp
import se.supernovait.anya.ui.theme.AnyaTheme

@Composable
@Preview
fun App() {
    AnyaTheme {
        KoinContext {
            AnyaApp()
        }
    }
}

// TODO: add animations
// TODO: add menu (screen)
// TODO: add bottom app bar (menu)
