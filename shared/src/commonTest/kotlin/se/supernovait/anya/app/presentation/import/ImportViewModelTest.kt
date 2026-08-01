package se.supernovait.anya.app.presentation.import

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import se.supernovait.anya.app.domain.model.ShareType
import se.supernovait.anya.app.domain.model.dto.CatDto
import se.supernovait.anya.app.fakes.FakeCatRepository
import se.supernovait.anya.app.presentation.app.AppEvent
import se.supernovait.anya.app.util.AnyaBaseTest
import se.supernovait.anya.core.domain.model.error.NetworkError
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ImportViewModelTest : AnyaBaseTest() {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var catRepository: FakeCatRepository
    private val json = Json { ignoreUnknownKeys = true }

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
    fun `when viewmodel is initialized with cat data summary name is correct`() = runTest {
        val catDto = CatDto(
            id = 1, ownerId = null, name = "Whiskers", nickname = "Whisk",
            dob = "2020-01-01", breed = "Siamese", eyeColor = "Blue",
            furColor = "White", sterilized = false
        )
        val data = json.encodeToString(catDto)
        val viewModel = createViewModel(ShareType.CAT.id, data)
        
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("Whiskers", state.name)
            assertEquals(ShareType.CAT, state.type)
        }
    }

    @Test
    fun `Import failure emits error message and SERVER_ERROR`() = runTest {
        catRepository.shouldReturnError = true
        val catDto = CatDto(
            id = 1, ownerId = null, name = "Whiskers", nickname = "Whisk",
            dob = "2020-01-01", breed = "Siamese", eyeColor = "Blue",
            furColor = "White", sterilized = false
        )
        val data = json.encodeToString(catDto)
        val viewModel = createViewModel(ShareType.CAT.id, data)
        
        viewModel.events.test {
            viewModel.onEvent(ImportScreenEvent.Import)
            testDispatcher.scheduler.advanceUntilIdle()
            
            val msgEvent = awaitItem()
            assertTrue(msgEvent is AppEvent.Message)
            
            val errEvent = awaitItem()
            assertTrue(errEvent is AppEvent.Error)
            assertEquals(NetworkError.SERVER_ERROR, (errEvent as AppEvent.Error).error)
        }
    }

    private fun createViewModel(type: String, data: String): ImportViewModel {
        val savedStateHandle = SavedStateHandle(mapOf("type" to type, "data" to data))
        return ImportViewModel(
            savedStateHandle = savedStateHandle,
            catRepository = catRepository,
            json = json
        )
    }
}
