package se.supernovait.anya.app.presentation.owner.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.test.swipeRight
import kotlinx.datetime.LocalDate
import se.supernovait.anya.app.data.local.entity.Owner
import se.supernovait.anya.app.domain.model.sort.OwnerSortOption
import se.supernovait.anya.app.presentation.app.auth.AuthenticationState
import se.supernovait.anya.app.presentation.app.auth.LocalAuthState
import se.supernovait.anya.app.presentation.app.topbar.LocalTopBarState
import se.supernovait.anya.app.presentation.app.topbar.TopBarState
import se.supernovait.anya.app.presentation.owner.OwnerScreenEvent
import se.supernovait.anya.app.presentation.owner.state.OwnerScreenState
import se.supernovait.anya.app.presentation.owner.state.OwnerState
import se.supernovait.anya.app.util.AnyaBaseTest
import se.supernovait.anya.core.presentation.common.AnyaIcon
import se.supernovait.anya.core.presentation.common.action.AnyaIconButton
import se.supernovait.anya.core.presentation.common.action.FabState
import se.supernovait.anya.core.presentation.common.action.LocalFabState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalTestApi::class)
class OwnerScreenTest : AnyaBaseTest() {

    @Test
    fun `comprehensive OwnerScreen test`() = runComposeUiTest {
        var capturedEvent: OwnerScreenEvent? = null
        val fabState = FabState()
        val topBarState = TopBarState()
        val userId = 123L
        val user = Owner(id = userId, firstname = "Test", lastname = "User", username = "testuser", dob = "1990-01-01")
        val owner = OwnerState(id = 1, firstname = "John", lastname = "Doe", dob = LocalDate.parse("1990-01-01"))
        
        val uiState = mutableStateOf(OwnerScreenState(owners = listOf(owner)))
        
        setContent {
            CompositionLocalProvider(
                LocalAuthState provides AuthenticationState.Authenticated(user = user),
                LocalTopBarState provides topBarState,
                LocalFabState provides fabState
            ) {
                Scaffold(
                    topBar = {
                        Row {
                            topBarState.actions.forEach { action ->
                                AnyaIconButton(
                                    icon = action.icon,
                                    contentDescription = action.contentDescription,
                                    onClick = action.onClick
                                )
                            }
                        }
                    },
                    floatingActionButton = {
                        val icon = fabState.icon
                        val onClick = fabState.onClick
                        if (icon != null && onClick != null) {
                            FloatingActionButton(onClick = onClick) {
                                AnyaIcon(icon = icon, contentDescription = fabState.contentDescription)
                            }
                        }
                    }
                ) { padding ->
                    OwnerScreen(
                        uiState = uiState.value,
                        onEvent = { capturedEvent = it },
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }

        // 1. List display
        onNodeWithText("John Doe").assertExists()

        // 2. FAB click -> ShowOwnerForm
        onNodeWithContentDescription("Add new owner", ignoreCase = true).performClick()
        assertIs<OwnerScreenEvent.ShowOwnerForm>(capturedEvent)
        capturedEvent = null

        // 3. Profile icon click -> NavigateToOwner
        onNodeWithContentDescription("Action to navigate to profile", ignoreCase = true).performClick()
        assertIs<OwnerScreenEvent.NavigateToOwner>(capturedEvent)
        assertEquals(userId, (capturedEvent as OwnerScreenEvent.NavigateToOwner).id)
        capturedEvent = null

        // 4. Search -> FilterOwners
        onNodeWithContentDescription("Search field", ignoreCase = true).performTextInput("John")
        onNodeWithContentDescription("Search button", ignoreCase = true).performClick()
        assertIs<OwnerScreenEvent.FilterOwners>(capturedEvent)
        assertEquals("John", (capturedEvent as OwnerScreenEvent.FilterOwners).searchQuery)
        capturedEvent = null

        // 5. Sort selection -> SortOwners
        onNodeWithContentDescription("Sort button", ignoreCase = true).performClick()
        onNodeWithText("Order by firstname", ignoreCase = true, substring = true).performClick()
        assertIs<OwnerScreenEvent.SortOwners>(capturedEvent)
        assertEquals(OwnerSortOption.FIRSTNAME, (capturedEvent as OwnerScreenEvent.SortOwners).sortType)
        capturedEvent = null

        // 6. OwnerForm visibility
        uiState.value = uiState.value.copy(showOwnerForm = true)
        onNodeWithText("Firstname", substring = true).assertExists()
        uiState.value = uiState.value.copy(showOwnerForm = false)

        // 7. Swipe and click delete icon -> ConfirmDeleteOwner
        onNode(hasContentDescription("Swipeable owner list item John Doe", substring = true)).performTouchInput {
            swipeRight()
        }
        onNodeWithContentDescription("Delete John Doe Button", ignoreCase = true, useUnmergedTree = true).performClick()
        assertIs<OwnerScreenEvent.ConfirmDeleteOwner>(capturedEvent)
        assertEquals(owner, (capturedEvent as OwnerScreenEvent.ConfirmDeleteOwner).owner)
        capturedEvent = null

        // 8. Update state to show dialog
        uiState.value = uiState.value.copy(ownerToDelete = owner)
        
        // 9. Click Confirm in dialog -> DeleteOwner
        onNodeWithContentDescription("Confirm Button", ignoreCase = true).performClick()
        assertIs<OwnerScreenEvent.DeleteOwner>(capturedEvent)
        assertEquals(owner, (capturedEvent as OwnerScreenEvent.DeleteOwner).owner)
        capturedEvent = null

        // 10. Click Dismiss in dialog -> DismissDeleteConfirmation
        onNodeWithContentDescription("Dismiss Button", ignoreCase = true).performClick()
        assertIs<OwnerScreenEvent.DismissDeleteConfirmation>(capturedEvent)
    }
}
