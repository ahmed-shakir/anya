package se.supernovait.anya.app.presentation.censored_text.screen

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import se.supernovait.anya.app.fakes.FakeInsultCensorRepository
import se.supernovait.anya.app.presentation.app.auth.AuthenticationState
import se.supernovait.anya.app.presentation.app.auth.LocalAuthState
import se.supernovait.anya.app.presentation.app.topbar.LocalTopBarState
import se.supernovait.anya.app.presentation.app.topbar.TopBarState
import se.supernovait.anya.app.presentation.censored_text.CensoredTextEvent
import se.supernovait.anya.app.presentation.censored_text.CensoredTextViewModel
import se.supernovait.anya.app.util.AnyaBaseTest
import se.supernovait.anya.core.presentation.common.action.FabState
import se.supernovait.anya.core.presentation.common.action.LocalFabState
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class CensoredTextScreenTest : AnyaBaseTest() {

    @Test
    fun `when success state is loaded counter is displayed`() = runComposeUiTest {
        val repository = FakeInsultCensorRepository()
        val viewModel = CensoredTextViewModel(repository)
        
        setContent {
            CompositionLocalProvider(
                LocalAuthState provides AuthenticationState.NotAuthenticated,
                LocalTopBarState provides TopBarState(),
                LocalFabState provides FabState()
            ) {
                CensoredTextScreen(
                    viewModel = viewModel,
                    onEvent = {}
                )
            }
        }

        // Wait for Loading to finish
        advanceTime()
        
        onNodeWithText("times", substring = true, ignoreCase = true).assertExists()
    }

    @Test
    fun `entering text and clicking Censor triggers CensorText event`() = runComposeUiTest {
        val repository = FakeInsultCensorRepository()
        val viewModel = CensoredTextViewModel(repository)
        var capturedEvent: CensoredTextEvent? = null
        
        setContent {
            CompositionLocalProvider(
                LocalAuthState provides AuthenticationState.NotAuthenticated,
                LocalTopBarState provides TopBarState(),
                LocalFabState provides FabState()
            ) {
                CensoredTextScreen(
                    viewModel = viewModel,
                    onEvent = { capturedEvent = it }
                )
            }
        }

        advanceTime()

        onNodeWithText("Uncensored", substring = true, ignoreCase = true).performTextInput("bad")
        onNode(hasText("Censor", ignoreCase = true) and hasClickAction()).performClick()
        
        assertEquals(CensoredTextEvent.CensorText, capturedEvent)
    }
}
