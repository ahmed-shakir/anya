package se.supernovait.anya.app.presentation.medical_record.screen

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
import se.supernovait.anya.app.domain.model.MedicalRecordType
import se.supernovait.anya.app.domain.model.sort.MedicalRecordSortOption
import se.supernovait.anya.app.presentation.app.auth.AuthenticationState
import se.supernovait.anya.app.presentation.app.auth.LocalAuthState
import se.supernovait.anya.app.presentation.app.topbar.LocalTopBarState
import se.supernovait.anya.app.presentation.app.topbar.TopBarState
import se.supernovait.anya.app.presentation.medical_record.MedicalRecordScreenEvent
import se.supernovait.anya.app.presentation.medical_record.state.MedicalRecordScreenState
import se.supernovait.anya.app.presentation.medical_record.state.MedicalRecordState
import se.supernovait.anya.app.util.AnyaBaseTest
import se.supernovait.anya.core.presentation.common.AnyaIcon
import se.supernovait.anya.core.presentation.common.action.AnyaIconButton
import se.supernovait.anya.core.presentation.common.action.FabState
import se.supernovait.anya.core.presentation.common.action.LocalFabState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalTestApi::class)
class MedicalRecordScreenTest : AnyaBaseTest() {

    @Test
    fun `comprehensive MedicalRecordScreen test`() = runComposeUiTest {
        var capturedEvent: MedicalRecordScreenEvent? = null
        val fabState = FabState()
        val topBarState = TopBarState()
        val userId = 123L
        val user = Owner(id = userId, firstname = "Test", lastname = "User", username = "testuser", dob = "1990-01-01")
        val record = MedicalRecordState(
            id = 1, 
            catId = 1, 
            title = "Vaccination", 
            type = MedicalRecordType.VACCINATION, 
            description = "Rabies", 
            date = LocalDate.parse("2023-01-01")
        )
        
        val uiState = mutableStateOf(MedicalRecordScreenState(records = listOf(record)))
        
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
                    MedicalRecordScreen(
                        uiState = uiState.value,
                        onEvent = { capturedEvent = it },
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }

        // 1. List display
        onNodeWithText("Vaccination").assertExists()

        // 2. FAB click -> ShowForm
        onNodeWithContentDescription("Add new medical record", ignoreCase = true).performClick()
        assertIs<MedicalRecordScreenEvent.ShowForm>(capturedEvent)
        capturedEvent = null

        // 3. Profile icon click -> NavigateToOwner
        onNodeWithContentDescription("Action to navigate to profile", ignoreCase = true).performClick()
        assertIs<MedicalRecordScreenEvent.NavigateToOwner>(capturedEvent)
        assertEquals(userId, (capturedEvent as MedicalRecordScreenEvent.NavigateToOwner).id)
        capturedEvent = null

        // 4. Search -> FilterRecords
        onNodeWithContentDescription("Search field", ignoreCase = true).performTextInput("Rabies")
        onNodeWithContentDescription("Search button", ignoreCase = true).performClick()
        assertIs<MedicalRecordScreenEvent.FilterRecords>(capturedEvent)
        assertEquals("Rabies", (capturedEvent as MedicalRecordScreenEvent.FilterRecords).searchQuery)
        capturedEvent = null

        // 5. Sort selection -> SortRecords
        onNodeWithContentDescription("Sort button", ignoreCase = true).performClick()
        onNodeWithText("Order by title", ignoreCase = true, substring = true).performClick()
        assertIs<MedicalRecordScreenEvent.SortRecords>(capturedEvent)
        assertEquals(MedicalRecordSortOption.TITLE, (capturedEvent as MedicalRecordScreenEvent.SortRecords).sortOption)
        capturedEvent = null

        // 6. Form visibility
        uiState.value = uiState.value.copy(showForm = true)
        onNodeWithText("Title", substring = true).assertExists()
        uiState.value = uiState.value.copy(showForm = false)

        // 7. Swipe and click delete icon -> ConfirmDeleteRecord
        onNode(hasContentDescription("Swipeable medical record list item Vaccination", substring = true)).performTouchInput {
            swipeRight()
        }
        onNodeWithContentDescription("Delete Vaccination button", ignoreCase = true, useUnmergedTree = true).performClick()
        assertIs<MedicalRecordScreenEvent.ConfirmDeleteRecord>(capturedEvent)
        assertEquals(record, (capturedEvent as MedicalRecordScreenEvent.ConfirmDeleteRecord).medicalRecord)
        capturedEvent = null

        // 8. Update state to show dialog
        uiState.value = uiState.value.copy(recordToDelete = record)
        
        // 9. Click Confirm in dialog -> DeleteRecord
        onNodeWithContentDescription("Confirm Button", ignoreCase = true).performClick()
        assertIs<MedicalRecordScreenEvent.DeleteRecord>(capturedEvent)
        assertEquals(record, (capturedEvent as MedicalRecordScreenEvent.DeleteRecord).medicalRecord)
        capturedEvent = null

        // 10. Click Dismiss in dialog -> DismissDeleteConfirmation
        onNodeWithContentDescription("Dismiss Button", ignoreCase = true).performClick()
        assertIs<MedicalRecordScreenEvent.DismissDeleteConfirmation>(capturedEvent)
    }
}
