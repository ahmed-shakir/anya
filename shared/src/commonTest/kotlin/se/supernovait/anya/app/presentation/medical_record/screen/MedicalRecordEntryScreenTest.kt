package se.supernovait.anya.app.presentation.medical_record.screen

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import kotlinx.datetime.LocalDate
import se.supernovait.anya.app.domain.model.MedicalRecordType
import se.supernovait.anya.app.presentation.app.auth.AuthenticationState
import se.supernovait.anya.app.presentation.app.auth.LocalAuthState
import se.supernovait.anya.app.presentation.app.topbar.LocalTopBarState
import se.supernovait.anya.app.presentation.app.topbar.TopBarState
import se.supernovait.anya.app.presentation.medical_record.MedicalRecordScreenEvent
import se.supernovait.anya.app.presentation.medical_record.state.MedicalRecordScreenState
import se.supernovait.anya.app.presentation.medical_record.state.MedicalRecordState
import se.supernovait.anya.app.util.AnyaBaseTest
import se.supernovait.anya.core.presentation.common.action.FabState
import se.supernovait.anya.core.presentation.common.action.LocalFabState
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class MedicalRecordEntryScreenTest : AnyaBaseTest() {

    @Test
    fun `when medical record is displayed details are visible`() = runComposeUiTest {
        val record = MedicalRecordState(
            id = 1,
            catId = 1,
            title = "Vaccination",
            type = MedicalRecordType.VACCINATION,
            description = "Rabies shot",
            date = LocalDate.parse("2023-01-01")
        )
        
        setContent {
            CompositionLocalProvider(
                LocalAuthState provides AuthenticationState.NotAuthenticated,
                LocalTopBarState provides TopBarState(),
                LocalFabState provides FabState()
            ) {
                MedicalRecordEntryScreen(
                    uiState = MedicalRecordScreenState(selectedRecord = record),
                    onEvent = {}
                )
            }
        }

        onNodeWithText("Vaccination").assertExists()
        onNodeWithText("Rabies shot").assertExists()
    }

    @Test
    fun `clicking edit triggers ShowForm event`() = runComposeUiTest {
        val record = MedicalRecordState(id = 1, catId = 1, title = "Vaccination", type = MedicalRecordType.VACCINATION, description = "Rabies shot", date = LocalDate.parse("2023-01-01"))
        var capturedEvent: MedicalRecordScreenEvent? = null
        
        setContent {
            CompositionLocalProvider(
                LocalAuthState provides AuthenticationState.NotAuthenticated,
                LocalTopBarState provides TopBarState(),
                LocalFabState provides FabState()
            ) {
                MedicalRecordEntryScreen(
                    uiState = MedicalRecordScreenState(selectedRecord = record),
                    onEvent = { capturedEvent = it }
                )
            }
        }

        onNodeWithContentDescription("Edit", substring = true, ignoreCase = true).performClick()
        
        assertEquals(MedicalRecordScreenEvent.ShowForm(record), capturedEvent)
    }

    @Test
    fun `clicking delete triggers ConfirmDeleteRecord event`() = runComposeUiTest {
        val record = MedicalRecordState(id = 1, catId = 1, title = "Vaccination", type = MedicalRecordType.VACCINATION, description = "Rabies shot", date = LocalDate.parse("2023-01-01"))
        var capturedEvent: MedicalRecordScreenEvent? = null
        
        setContent {
            CompositionLocalProvider(
                LocalAuthState provides AuthenticationState.NotAuthenticated,
                LocalTopBarState provides TopBarState(),
                LocalFabState provides FabState()
            ) {
                MedicalRecordEntryScreen(
                    uiState = MedicalRecordScreenState(selectedRecord = record),
                    onEvent = { capturedEvent = it }
                )
            }
        }

        onNodeWithContentDescription("Delete", substring = true, ignoreCase = true).performClick()
        
        assertEquals(MedicalRecordScreenEvent.ConfirmDeleteRecord(record), capturedEvent)
    }
}
