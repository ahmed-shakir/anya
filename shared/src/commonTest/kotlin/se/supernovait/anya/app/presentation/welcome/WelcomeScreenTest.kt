package se.supernovait.anya.app.presentation.welcome

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
class WelcomeScreenTest : AnyaBaseTest() {

    @Test
    fun `when sign up button is clicked it triggers ShowSignUpForm event`() = runComposeUiTest {
        var capturedEvent: WelcomeScreenEvent? = null
        
        setContent {
            CompositionLocalProvider(
                LocalAuthState provides AuthenticationState.NotAuthenticated,
                LocalTopBarState provides TopBarState(),
                LocalFabState provides FabState()
            ) {
                WelcomeScreen(
                    uiState = WelcomeScreenState(),
                    onEvent = { capturedEvent = it }
                )
            }
        }

        onNodeWithText("Sign up", substring = true, ignoreCase = true).performClick()
        
        assertEquals(WelcomeScreenEvent.ShowSignUpForm, capturedEvent)
    }

    @Test
    fun `when sign in button is clicked it triggers ShowSignInForm event`() = runComposeUiTest {
        var capturedEvent: WelcomeScreenEvent? = null
        
        setContent {
            CompositionLocalProvider(
                LocalAuthState provides AuthenticationState.NotAuthenticated,
                LocalTopBarState provides TopBarState(),
                LocalFabState provides FabState()
            ) {
                WelcomeScreen(
                    uiState = WelcomeScreenState(),
                    onEvent = { capturedEvent = it }
                )
            }
        }

        onNodeWithText("Sign in", substring = true, ignoreCase = true).performClick()
        
        assertEquals(WelcomeScreenEvent.ShowSignInForm, capturedEvent)
    }
}
