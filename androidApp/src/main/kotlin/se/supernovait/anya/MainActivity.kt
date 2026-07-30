package se.supernovait.anya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import se.supernovait.anya.app.presentation.app.App
import se.supernovait.anya.app.presentation.app.initialization.AppInitializer
import se.supernovait.anya.core.presentation.common.preview.ScreenPreviewContainer

class MainActivity : ComponentActivity() {
    private val appInitializer: AppInitializer by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Start app initialization in the background
        lifecycleScope.launch {
            appInitializer.initialize()
        }

        // Keep splash screen visible while initializing
        // It will automatically dismiss once initialization completes (Success or Error state)
        splash.setKeepOnScreenCondition {
            appInitializer.isInitializing()
        }

        setContent {
            App()
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    ScreenPreviewContainer {
        App()
    }
}
