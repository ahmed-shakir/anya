package se.supernovait.anya.app.presentation.info

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import se.supernovait.anya.app.presentation.app.auth.AuthenticationState
import se.supernovait.anya.app.presentation.app.auth.LocalAuthState
import se.supernovait.anya.app.presentation.app.topbar.LocalTopBarState
import se.supernovait.anya.app.presentation.app.topbar.TopBarState
import se.supernovait.anya.app.util.AnyaBaseTest
import se.supernovait.anya.core.presentation.common.action.FabState
import se.supernovait.anya.core.presentation.common.action.LocalFabState
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class InfoScreenTest : AnyaBaseTest() {

    @Test
    fun `when info screen is displayed version and platform are visible`() = runComposeUiTest {
        val uiState = InfoScreenState(
            networkStatus = "Online",
            batteryLevel = "80%"
        )
        
        setContent {
            CompositionLocalProvider(
                LocalAuthState provides AuthenticationState.NotAuthenticated,
                LocalTopBarState provides TopBarState(),
                LocalFabState provides FabState()
            ) {
                InfoScreen(uiState = uiState)
            }
        }

        onNodeWithText("About Anya", substring = true, ignoreCase = true).assertExists()
        onNodeWithText("0.9.0", substring = true).assertExists()
        onNodeWithText("Online", substring = true).assertExists()
        onNodeWithText("80%", substring = true).assertExists()
    }
}
