package se.supernovait.anya.app.presentation.import.screen

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import se.supernovait.anya.app.domain.model.ShareType
import se.supernovait.anya.app.presentation.app.auth.AuthenticationState
import se.supernovait.anya.app.presentation.app.auth.LocalAuthState
import se.supernovait.anya.app.presentation.app.topbar.LocalTopBarState
import se.supernovait.anya.app.presentation.app.topbar.TopBarState
import se.supernovait.anya.app.presentation.import.ImportScreen
import se.supernovait.anya.app.presentation.import.ImportScreenEvent
import se.supernovait.anya.app.presentation.import.ImportScreenState
import se.supernovait.anya.app.util.AnyaBaseTest
import se.supernovait.anya.core.presentation.common.action.FabState
import se.supernovait.anya.core.presentation.common.action.LocalFabState
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class ImportScreenTest : AnyaBaseTest() {

    @Test
    fun `when cat import is displayed name is visible`() = runComposeUiTest {
        val uiState = ImportScreenState(
            type = ShareType.CAT,
            name = "Whiskers",
            data = "{}"
        )
        
        setContent {
            CompositionLocalProvider(
                LocalAuthState provides AuthenticationState.NotAuthenticated,
                LocalTopBarState provides TopBarState(),
                LocalFabState provides FabState()
            ) {
                ImportScreen(
                    uiState = uiState,
                    onEvent = {}
                )
            }
        }

        onNodeWithText("Whiskers", substring = true).assertExists()
    }

    @Test
    fun `clicking import triggers Import event`() = runComposeUiTest {
        var capturedEvent: ImportScreenEvent? = null
        
        setContent {
            CompositionLocalProvider(
                LocalAuthState provides AuthenticationState.NotAuthenticated,
                LocalTopBarState provides TopBarState(),
                LocalFabState provides FabState()
            ) {
                ImportScreen(
                    uiState = ImportScreenState(name = "Whiskers", data = "{}", type = ShareType.CAT),
                    onEvent = { capturedEvent = it }
                )
            }
        }

        onNodeWithText("Import", ignoreCase = true).performClick()
        
        assertEquals(ImportScreenEvent.Import, capturedEvent)
    }

    @Test
    fun `clicking cancel triggers Cancel event`() = runComposeUiTest {
        var capturedEvent: ImportScreenEvent? = null
        
        setContent {
            CompositionLocalProvider(
                LocalAuthState provides AuthenticationState.NotAuthenticated,
                LocalTopBarState provides TopBarState(),
                LocalFabState provides FabState()
            ) {
                ImportScreen(
                    uiState = ImportScreenState(name = "Whiskers", data = "{}", type = ShareType.CAT),
                    onEvent = { capturedEvent = it }
                )
            }
        }

        onNodeWithText("Cancel", ignoreCase = true).performClick()
        
        assertEquals(ImportScreenEvent.Cancel, capturedEvent)
    }
}
