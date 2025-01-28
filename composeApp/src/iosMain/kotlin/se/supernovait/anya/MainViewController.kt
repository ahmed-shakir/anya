package se.supernovait.anya

import androidx.compose.ui.window.ComposeUIViewController
import se.supernovait.anya.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}
