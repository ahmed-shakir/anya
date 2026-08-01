package se.supernovait.anya.app.presentation.cat.screen

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import kotlinx.datetime.LocalDate
import se.supernovait.anya.app.presentation.app.auth.AuthenticationState
import se.supernovait.anya.app.presentation.app.auth.LocalAuthState
import se.supernovait.anya.app.presentation.app.topbar.LocalTopBarState
import se.supernovait.anya.app.presentation.app.topbar.TopBarState
import se.supernovait.anya.app.presentation.cat.CatScreenEvent
import se.supernovait.anya.app.presentation.cat.state.CatScreenState
import se.supernovait.anya.app.presentation.cat.state.CatState
import se.supernovait.anya.app.util.AnyaBaseTest
import se.supernovait.anya.core.presentation.common.action.FabState
import se.supernovait.anya.core.presentation.common.action.LocalFabState
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class CatProfileScreenTest : AnyaBaseTest() {

    @Test
    fun `when cat profile is displayed details are visible`() = runComposeUiTest {
        val cat = CatState(
            id = 1,
            name = "Whiskers",
            nickname = "Whisk",
            dob = LocalDate.parse("2020-01-01"),
            breed = "Siamese",
            eyeColor = "Blue",
            furColor = "White"
        )
        
        setContent {
            CompositionLocalProvider(
                LocalAuthState provides AuthenticationState.NotAuthenticated,
                LocalTopBarState provides TopBarState(),
                LocalFabState provides FabState()
            ) {
                CatProfileScreen(
                    uiState = CatScreenState(selectedCat = cat),
                    onEvent = {}
                )
            }
        }

        onNodeWithText("Whiskers").assertExists()
        onNodeWithText("Whisk").assertExists()
        onNodeWithText("Siamese").assertExists()
        onNodeWithText("Blue").assertExists()
    }

    @Test
    fun `clicking edit personal details triggers ShowCatForm event`() = runComposeUiTest {
        val cat = CatState(id = 1, name = "Whiskers", nickname = "Whiskers", dob = LocalDate.parse("2020-01-01"), breed = "Siamese")
        var capturedEvent: CatScreenEvent? = null
        
        setContent {
            CompositionLocalProvider(
                LocalAuthState provides AuthenticationState.NotAuthenticated,
                LocalTopBarState provides TopBarState(),
                LocalFabState provides FabState()
            ) {
                CatProfileScreen(
                    uiState = CatScreenState(selectedCat = cat),
                    onEvent = { capturedEvent = it }
                )
            }
        }

        onNodeWithContentDescription("Edit Personal details button", ignoreCase = true).performClick()
        
        assertEquals(CatScreenEvent.ShowCatForm(cat), capturedEvent)
    }

    @Test
    fun `clicking medical records triggers NavigateToMedicalRecord event`() = runComposeUiTest {
        val cat = CatState(id = 1, name = "Whiskers", nickname = "Whiskers", dob = LocalDate.parse("2020-01-01"), breed = "Siamese")
        var capturedEvent: CatScreenEvent? = null

        setContent {
            CompositionLocalProvider(
                LocalAuthState provides AuthenticationState.NotAuthenticated,
                LocalTopBarState provides TopBarState(),
                LocalFabState provides FabState()
            ) {
                CatProfileScreen(
                    uiState = CatScreenState(selectedCat = cat),
                    onEvent = { capturedEvent = it }
                )
            }
        }

        onNodeWithContentDescription("Medical record action", ignoreCase = true).performClick()

        assertEquals(CatScreenEvent.NavigateToMedicalRecord(1L), capturedEvent)
    }
}
