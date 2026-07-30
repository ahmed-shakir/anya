package se.supernovait.anya.app.presentation.start

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import se.supernovait.anya.app.presentation.app.auth.AuthenticationState
import se.supernovait.anya.app.presentation.app.auth.LocalAuthState
import se.supernovait.anya.app.presentation.app.topbar.LocalTopBarState
import se.supernovait.anya.app.presentation.app.topbar.TopBarState
import se.supernovait.anya.app.util.AnyaBaseTest
import se.supernovait.anya.core.presentation.common.action.FabState
import se.supernovait.anya.core.presentation.common.action.LocalFabState
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class StartScreenTest : AnyaBaseTest() {

    @Test
    fun `clicking Cats tile triggers NavigateToCatScreen event`() = runComposeUiTest {
        var capturedEvent: StartScreenEvent? = null
        
        setContent {
            CompositionLocalProvider(
                LocalAuthState provides AuthenticationState.NotAuthenticated,
                LocalTopBarState provides TopBarState(),
                LocalFabState provides FabState()
            ) {
                StartScreen(onEvent = { capturedEvent = it })
            }
        }

        onNodeWithText("Cats", ignoreCase = true).performClick()
        
        assertEquals(StartScreenEvent.NavigateToCatScreen, capturedEvent)
    }

    @Test
    fun `clicking Owners tile triggers NavigateToOwnerScreen event`() = runComposeUiTest {
        var capturedEvent: StartScreenEvent? = null
        
        setContent {
            CompositionLocalProvider(
                LocalAuthState provides AuthenticationState.NotAuthenticated,
                LocalTopBarState provides TopBarState(),
                LocalFabState provides FabState()
            ) {
                StartScreen(onEvent = { capturedEvent = it })
            }
        }

        onNodeWithText("Owners", ignoreCase = true).performClick()
        
        assertEquals(StartScreenEvent.NavigateToOwnerScreen, capturedEvent)
    }

    @Test
    fun `clicking Censor tile triggers NavigateToCensoredTextScreen event`() = runComposeUiTest {
        var capturedEvent: StartScreenEvent? = null
        
        setContent {
            CompositionLocalProvider(
                LocalAuthState provides AuthenticationState.NotAuthenticated,
                LocalTopBarState provides TopBarState(),
                LocalFabState provides FabState()
            ) {
                StartScreen(onEvent = { capturedEvent = it })
            }
        }

        onNodeWithText("Censor", substring = true, ignoreCase = true).performClick()
        
        assertEquals(StartScreenEvent.NavigateToCensoredTextScreen, capturedEvent)
    }
}
