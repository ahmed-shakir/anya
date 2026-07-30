package se.supernovait.anya

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.ComposeUIViewController
import se.supernovait.anya.app.presentation.app.App
import se.supernovait.anya.app.presentation.app.initialization.AppInitializationState
import se.supernovait.anya.di.KoinHelper
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
    // Get AppInitializer from Koin via KoinHelper
    val appInitializer = remember {
        KoinHelper().getAppInitializer()
    }

    val initState by appInitializer.appInitState.collectAsState()
    var initializationStarted by remember { mutableStateOf(false) }

    // Start initialization once when the view controller is first composed
    LaunchedEffect(Unit) {
        if (!initializationStarted) {
            initializationStarted = true
            appInitializer.initialize()
        }
    }

    when (initState) {
        is AppInitializationState.Initializing -> {
            // Do NOT render anything here
            // Let the native LaunchScreen.storyboard stay visible
            // Once initialization completes, this state changes and content is rendered
        }
        is AppInitializationState.Success,
        is AppInitializationState.Error -> {
            // Initialization complete (success or error)
            // Show the app - App() will decide between AnyaApp or ErrorScreen
            // iOS will automatically dismiss the LaunchScreen when content appears
            App()
        }
    }
}
