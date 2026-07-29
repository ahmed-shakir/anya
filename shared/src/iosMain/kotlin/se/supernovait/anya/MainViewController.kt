package se.supernovait.anya

import androidx.compose.ui.window.ComposeUIViewController
import se.supernovait.anya.app.presentation.App
import se.supernovait.anya.di.initKoin

/**
 * iOS entry point for the Anya app (Compose Multiplatform).
 *
 * System shows LaunchScreen.storyboard natively when app launches
 */
fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}
