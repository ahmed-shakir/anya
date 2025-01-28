package se.supernovait.anya.util.presentation.ui.welcome

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import se.supernovait.anya.app.presentation.info.InfoScreen
import se.supernovait.anya.app.presentation.info.InfoScreenState
import kotlin.test.Test

class InfoScreenUITest {
    private val infoScreenState = InfoScreenState()

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testInfoScreen() = runComposeUiTest {
        setContent {
            InfoScreen(uiState = infoScreenState)
        }

        onNodeWithText("About Anya app").assertExists()
        onNodeWithText("Get to know Anya").assertExists()
        onNodeWithText("Made by Supernova Technology AB").assertExists()
        onNodeWithText("Version: 0.9.0").assertExists()
        onNodeWithText("Platform: ${infoScreenState.platform.name}").assertExists()
        onNodeWithText("Current battery level: ${infoScreenState.batteryLevel}").assertExists()
    }
}
