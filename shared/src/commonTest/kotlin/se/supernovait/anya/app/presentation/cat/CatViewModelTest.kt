package se.supernovait.anya.app.presentation.cat

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import se.supernovait.anya.app.data.local.entity.Cat
import se.supernovait.anya.app.fakes.FakeAuthRepository
import se.supernovait.anya.app.fakes.FakeCatRepository
import se.supernovait.anya.app.fakes.FakeShareHandler
import se.supernovait.anya.app.presentation.app.AppEvent
import se.supernovait.anya.app.presentation.app.auth.AuthenticationManager
import se.supernovait.anya.app.presentation.cat.state.CatState
import se.supernovait.anya.app.util.AnyaBaseTest
import se.supernovait.anya.core.domain.model.error.NetworkError
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CatViewModelTest : AnyaBaseTest() {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var catRepository: FakeCatRepository
    private lateinit var authManager: AuthenticationManager
    private lateinit var shareHandler: FakeShareHandler
    private val json = Json { ignoreUnknownKeys = true }

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        catRepository = FakeCatRepository()
        authManager = AuthenticationManager(FakeAuthRepository())
        shareHandler = FakeShareHandler()
    }

    @AfterTest
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state shows empty cats`() = runTest {
        val viewModel = createViewModel()
        
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.cats.isEmpty())
        }
    }

    @Test
    fun `when cats are loaded uiState is updated`() = runTest {
        val cat = Cat(id = 1, name = "Whiskers", nickname = "Whisk", dob = "2020-01-01", breed = "Siamese", eyeColor = "Blue", furColor = "White")
        catRepository.upsertCat(cat)
        
        val viewModel = createViewModel()

        viewModel.uiState.test {
            testDispatcher.scheduler.advanceUntilIdle()
            val state = expectMostRecentItem()
            assertEquals(1, state.cats.size)
            assertEquals("Whiskers", state.cats[0].name)
        }
    }

    @Test
    fun `search filters cats`() = runTest {
        catRepository.upsertCat(Cat(id = 1, name = "Whiskers", nickname = "Whisk", dob = "2020-01-01", breed = "Siamese", eyeColor = "Blue", furColor = "White"))
        catRepository.upsertCat(Cat(id = 2, name = "Felix", nickname = "Fix", dob = "2021-01-01", breed = "Persian", eyeColor = "Green", furColor = "Black"))
        
        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            viewModel.onEvent(CatScreenEvent.FilterCats("Whiskers"))
            testDispatcher.scheduler.advanceUntilIdle()
            val state = expectMostRecentItem()
            assertEquals(1, state.cats.size)
            assertEquals("Whiskers", state.cats[0].name)
        }
    }

    @Test
    fun `SaveCat with invalid data emits BAD_REQUEST error`() = runTest {
        val viewModel = createViewModel()
        val invalidCat = CatState(name = "", nickname = "", dob = null, breed = "")
        
        viewModel.events.test {
            viewModel.onEvent(CatScreenEvent.SaveCat(invalidCat, isCurrentUserOwner = false, useOwnerAddress = false))
            testDispatcher.scheduler.advanceUntilIdle()
            val event = expectMostRecentItem()
            assertTrue(event is AppEvent.Error)
            assertEquals(NetworkError.BAD_REQUEST, (event as AppEvent.Error).error)
        }
    }

    private fun createViewModel(ownerId: Long? = null): CatViewModel {
        val savedStateHandle = if (ownerId != null) {
            SavedStateHandle(mapOf("ownerId" to ownerId))
        } else {
            SavedStateHandle()
        }
        
        return CatViewModel(
            savedStateHandle = savedStateHandle,
            authManager = authManager,
            catRepository = catRepository,
            shareHandler = shareHandler,
            json = json
        )
    }
}
