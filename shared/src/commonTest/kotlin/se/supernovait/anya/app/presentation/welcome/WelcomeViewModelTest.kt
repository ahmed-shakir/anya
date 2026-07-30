package se.supernovait.anya.app.presentation.welcome

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import se.supernovait.anya.app.fakes.FakeAuthRepository
import se.supernovait.anya.app.presentation.app.AppEvent
import se.supernovait.anya.app.presentation.owner.state.OwnerState
import se.supernovait.anya.app.util.AnyaBaseTest
import se.supernovait.anya.core.domain.model.error.NetworkError
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class WelcomeViewModelTest : AnyaBaseTest() {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var authRepository: FakeAuthRepository
    private lateinit var viewModel: WelcomeViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        authRepository = FakeAuthRepository()
        viewModel = WelcomeViewModel(authRepository)
    }

    @AfterTest
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has forms hidden`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.showSignUpForm)
            assertFalse(state.showSignInForm)
        }
    }

    @Test
    fun `ShowSignUpForm event updates uiState`() = runTest {
        viewModel.uiState.test {
            viewModel.onEvent(WelcomeScreenEvent.ShowSignUpForm)
            testDispatcher.scheduler.advanceUntilIdle()
            assertTrue(expectMostRecentItem().showSignUpForm)
        }
    }

    @Test
    fun `SignIn event with valid user emits SignIn app event`() = runTest {
        authRepository.signUp(se.supernovait.anya.app.data.local.entity.Owner(id = 1, firstname = "John", lastname = "Doe", username = "johndoe", dob = "1990-01-01"))
        
        viewModel.events.test {
            viewModel.onEvent(WelcomeScreenEvent.SignIn("johndoe"))
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(AppEvent.SignIn, expectMostRecentItem())
        }
    }

    @Test
    fun `SignIn event with non-existent user emits UNAUTHORIZED error`() = runTest {
        viewModel.events.test {
            viewModel.onEvent(WelcomeScreenEvent.SignIn("nonexistent"))
            testDispatcher.scheduler.advanceUntilIdle()
            val event = expectMostRecentItem()
            assertTrue(event is AppEvent.Error)
            assertEquals(NetworkError.UNAUTHORIZED, (event as AppEvent.Error).error)
        }
    }

    @Test
    fun `SignUp with invalid profile emits BAD_REQUEST error`() = runTest {
        val invalidProfile = OwnerState(firstname = "", lastname = "", dob = null)
        
        viewModel.events.test {
            viewModel.onEvent(WelcomeScreenEvent.SignUp(invalidProfile))
            testDispatcher.scheduler.advanceUntilIdle()
            val event = expectMostRecentItem()
            assertTrue(event is AppEvent.Error)
            assertEquals(NetworkError.BAD_REQUEST, (event as AppEvent.Error).error)
        }
    }

    @Test
    fun `Unknown event emits UNKNOWN error`() = runTest {
        viewModel.events.test {
            viewModel.onEvent(WelcomeScreenEvent.NavigateToInfo) // Not handled in VM, goes to else branch
            testDispatcher.scheduler.advanceUntilIdle()
            val event = expectMostRecentItem()
            assertTrue(event is AppEvent.Error)
            assertEquals(NetworkError.UNKNOWN, (event as AppEvent.Error).error)
        }
    }
}
