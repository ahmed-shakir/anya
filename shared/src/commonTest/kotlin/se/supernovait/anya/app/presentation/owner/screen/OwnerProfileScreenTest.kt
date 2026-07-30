package se.supernovait.anya.app.presentation.owner.screen

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.runComposeUiTest
import kotlinx.datetime.LocalDate
import se.supernovait.anya.app.presentation.app.auth.AuthenticationState
import se.supernovait.anya.app.presentation.app.auth.LocalAuthState
import se.supernovait.anya.app.presentation.app.topbar.LocalTopBarState
import se.supernovait.anya.app.presentation.app.topbar.TopBarState
import se.supernovait.anya.app.presentation.owner.OwnerScreenEvent
import se.supernovait.anya.app.presentation.owner.state.OwnerScreenState
import se.supernovait.anya.app.presentation.owner.state.OwnerState
import se.supernovait.anya.app.util.AnyaBaseTest
import se.supernovait.anya.core.presentation.common.action.FabState
import se.supernovait.anya.core.presentation.common.action.LocalFabState
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class OwnerProfileScreenTest : AnyaBaseTest() {

    @Test
    fun `when owner profile is displayed details are visible`() = runComposeUiTest {
        val owner = OwnerState(
            id = 1,
            firstname = "John",
            lastname = "Doe",
            username = "johndoe",
            dob = LocalDate.parse("1990-01-01")
        )
        
        setContent {
            CompositionLocalProvider(
                LocalAuthState provides AuthenticationState.NotAuthenticated,
                LocalTopBarState provides TopBarState(),
                LocalFabState provides FabState()
            ) {
                OwnerProfileScreen(
                    uiState = OwnerScreenState(selectedOwner = owner),
                    onEvent = {}
                )
            }
        }

        onNodeWithText("John Doe").assertExists()
        onNodeWithText("johndoe").assertExists()
    }

    @Test
    fun `clicking edit personal details triggers ShowOwnerForm event`() = runComposeUiTest {
        val owner = OwnerState(id = 1, firstname = "John", lastname = "Doe", dob = LocalDate.parse("1990-01-01"))
        var capturedEvent: OwnerScreenEvent? = null
        
        setContent {
            CompositionLocalProvider(
                LocalAuthState provides AuthenticationState.NotAuthenticated,
                LocalTopBarState provides TopBarState(),
                LocalFabState provides FabState()
            ) {
                OwnerProfileScreen(
                    uiState = OwnerScreenState(selectedOwner = owner),
                    onEvent = { capturedEvent = it }
                )
            }
        }

        onNodeWithContentDescription("Edit Personal details button", ignoreCase = true).performClick()
        
        assertEquals(OwnerScreenEvent.ShowOwnerForm(owner), capturedEvent)
    }

    @Test
    fun `clicking Sign Out triggers SignOut event`() = runComposeUiTest {
        val owner = OwnerState(id = 1, firstname = "John", lastname = "Doe", dob = LocalDate.parse("1990-01-01"))
        var capturedEvent: OwnerScreenEvent? = null
        
        setContent {
            CompositionLocalProvider(
                LocalAuthState provides AuthenticationState.NotAuthenticated,
                LocalTopBarState provides TopBarState(),
                LocalFabState provides FabState()
            ) {
                OwnerProfileScreen(
                    uiState = OwnerScreenState(selectedOwner = owner),
                    onEvent = { capturedEvent = it }
                )
            }
        }

        onNodeWithText("Sign out", substring = true, ignoreCase = true)
            .performScrollTo()
            .performClick()
        
        assertEquals(OwnerScreenEvent.SignOut, capturedEvent)
    }
}
