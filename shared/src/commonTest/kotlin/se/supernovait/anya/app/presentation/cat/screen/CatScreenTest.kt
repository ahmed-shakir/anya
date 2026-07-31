package se.supernovait.anya.app.presentation.cat.screen

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
import se.supernovait.anya.app.domain.model.sort.CatSortOption
import se.supernovait.anya.app.presentation.app.auth.AuthenticationState
import se.supernovait.anya.app.presentation.app.auth.LocalAuthState
import se.supernovait.anya.app.presentation.app.topbar.LocalTopBarState
import se.supernovait.anya.app.presentation.app.topbar.TopBarState
import se.supernovait.anya.app.presentation.cat.CatScreenEvent
import se.supernovait.anya.app.presentation.cat.state.CatScreenState
import se.supernovait.anya.app.presentation.cat.state.CatState
import se.supernovait.anya.app.util.AnyaBaseTest
import se.supernovait.anya.core.presentation.common.AnyaIcon
import se.supernovait.anya.core.presentation.common.action.AnyaIconButton
import se.supernovait.anya.core.presentation.common.action.FabState
import se.supernovait.anya.core.presentation.common.action.LocalFabState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalTestApi::class)
class CatScreenTest : AnyaBaseTest() {

    @Test
    fun `comprehensive CatScreen test`() = runComposeUiTest {
        var capturedEvent: CatScreenEvent? = null
        val fabState = FabState()
        val topBarState = TopBarState()
        val userId = 123L
        val user = Owner(id = userId, firstname = "Test", lastname = "User", username = "testuser", dob = "1990-01-01")
        val cat = CatState(id = 1, name = "Whiskers", nickname = "Whiskers", dob = LocalDate.parse("2020-01-01"), breed = "Siamese")
        
        val uiState = mutableStateOf(CatScreenState(cats = listOf(cat)))
        
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
                    CatScreen(
                        uiState = uiState.value,
                        onEvent = { capturedEvent = it },
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }

        // 1. List display
        onNodeWithText("Whiskers").assertExists()

        // 2. FAB click -> ShowCatForm
        onNodeWithContentDescription("Add new cat", ignoreCase = true).performClick()
        assertIs<CatScreenEvent.ShowCatForm>(capturedEvent)
        capturedEvent = null

        // 3. Profile icon click -> NavigateToOwner
        onNodeWithContentDescription("Action to navigate to profile", ignoreCase = true).performClick()
        assertIs<CatScreenEvent.NavigateToOwner>(capturedEvent)
        assertEquals(userId, (capturedEvent as CatScreenEvent.NavigateToOwner).id)
        capturedEvent = null

        // 4. Search -> FilterCats
        onNodeWithContentDescription("Search field", ignoreCase = true).performTextInput("Whisk")
        onNodeWithContentDescription("Search button", ignoreCase = true).performClick()
        assertIs<CatScreenEvent.FilterCats>(capturedEvent)
        assertEquals("Whisk", (capturedEvent as CatScreenEvent.FilterCats).searchQuery)
        capturedEvent = null

        // 5. Sort selection -> SortCats
        onNodeWithContentDescription("Sort button", ignoreCase = true).performClick()
        onNodeWithText("Order by name", ignoreCase = true, substring = true).performClick()
        assertIs<CatScreenEvent.SortCats>(capturedEvent)
        assertEquals(CatSortOption.NAME, (capturedEvent as CatScreenEvent.SortCats).sortOption)
        capturedEvent = null

        // 6. CatForm visibility
        uiState.value = uiState.value.copy(showCatForm = true)
        onNodeWithText("Name", substring = true).assertExists()
        uiState.value = uiState.value.copy(showCatForm = false)

        // 7. Swipe and click delete icon -> ConfirmDeleteCat
        onNode(hasContentDescription("Swipeable cat list item Whiskers", substring = true)).performTouchInput {
            swipeRight()
        }
        onNodeWithContentDescription("Delete Whiskers Button", ignoreCase = true, useUnmergedTree = true).performClick()
        assertIs<CatScreenEvent.ConfirmDeleteCat>(capturedEvent)
        assertEquals(cat, (capturedEvent as CatScreenEvent.ConfirmDeleteCat).cat)
        capturedEvent = null

        // 8. Update state to show dialog
        uiState.value = uiState.value.copy(catToDelete = cat)
        
        // 9. Click Confirm in dialog -> DeleteCat
        onNodeWithContentDescription("Confirm Button", ignoreCase = true).performClick()
        assertIs<CatScreenEvent.DeleteCat>(capturedEvent)
        assertEquals(cat, (capturedEvent as CatScreenEvent.DeleteCat).cat)
        capturedEvent = null

        // 10. Click Dismiss in dialog -> DismissDeleteConfirmation
        onNodeWithContentDescription("Dismiss Button", ignoreCase = true).performClick()
        assertIs<CatScreenEvent.DismissDeleteConfirmation>(capturedEvent)
    }
}
