package se.supernovait.anya.app.presentation.medical_record

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import se.supernovait.anya.app.data.local.entity.MedicalRecord
import se.supernovait.anya.app.domain.model.MedicalRecordType
import se.supernovait.anya.app.fakes.FakeCatRepository
import se.supernovait.anya.app.presentation.app.AppEvent
import se.supernovait.anya.app.presentation.medical_record.state.MedicalRecordState
import se.supernovait.anya.app.util.AnyaBaseTest
import se.supernovait.anya.core.domain.model.error.NetworkError
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class MedicalRecordViewModelTest : AnyaBaseTest() {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var catRepository: FakeCatRepository

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        catRepository = FakeCatRepository()
    }

    @AfterTest
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state shows empty records`() = runTest {
        val viewModel = createViewModel(catId = 1L)
        
        viewModel.uiState.test {
            testDispatcher.scheduler.advanceUntilIdle()
            val state = expectMostRecentItem()
            assertTrue(state.records.isEmpty())
            assertEquals(1L, state.currentCatId)
        }
    }

    @Test
    fun `when records are loaded uiState is updated`() = runTest {
        val record = MedicalRecord(id = 1, catId = 1, type = MedicalRecordType.VACCINATION, title = "Vaccination", description = "Rabies", date = "2023-01-01")
        catRepository.upsertMedicalRecord(record)
        
        val viewModel = createViewModel(catId = 1L)

        viewModel.uiState.test {
            testDispatcher.scheduler.advanceUntilIdle()
            val state = expectMostRecentItem()
            assertEquals(1, state.records.size)
            assertEquals("Vaccination", state.records[0].title)
        }
    }

    @Test
    fun `SaveMedicalRecord with invalid data emits BAD_REQUEST`() = runTest {
        val viewModel = createViewModel(catId = 1L)
        val invalidRecord = MedicalRecordState(catId = 1, type = MedicalRecordType.OTHER, title = "", description = "", date = null)
        
        viewModel.events.test {
            viewModel.onEvent(MedicalRecordScreenEvent.SaveRecord(invalidRecord))
            testDispatcher.scheduler.advanceUntilIdle()
            val event = expectMostRecentItem()
            assertTrue(event is AppEvent.Error)
            assertEquals(NetworkError.BAD_REQUEST, (event as AppEvent.Error).error)
        }
    }

    @Test
    fun `getMedicalRecordById failure doesn't crash VM`() = runTest {
        catRepository.shouldReturnError = true
        val savedStateHandle = SavedStateHandle(mapOf("id" to 1L))
        val viewModel = MedicalRecordViewModel(savedStateHandle, catRepository)
        
        // This won't emit to events but it should handle the exception from repo
        viewModel.onEvent(MedicalRecordScreenEvent.LoadRecord)
        testDispatcher.scheduler.advanceUntilIdle()
        
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.selectedRecord == null)
        }
    }

    private fun createViewModel(catId: Long = 0L): MedicalRecordViewModel {
        val savedStateHandle = SavedStateHandle(mapOf("catId" to catId))
        return MedicalRecordViewModel(
            savedStateHandle = savedStateHandle,
            catRepository = catRepository
        )
    }
}
