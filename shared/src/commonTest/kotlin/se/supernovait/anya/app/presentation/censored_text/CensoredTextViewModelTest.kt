package se.supernovait.anya.app.presentation.censored_text

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import se.supernovait.anya.app.fakes.FakeInsultCensorRepository
import se.supernovait.anya.app.util.AnyaBaseTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CensoredTextViewModelTest : AnyaBaseTest() {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: FakeInsultCensorRepository
    private lateinit var viewModel: CensoredTextViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeInsultCensorRepository()
        viewModel = CensoredTextViewModel(repository)
    }

    @AfterTest
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading then Success`() = runTest {
        viewModel.uiStateFlow.test {
            assertEquals(CensoredTextScreenState.Loading, awaitItem())
            val successState = awaitItem()
            assertTrue(successState is CensoredTextScreenState.Success)
            assertEquals(0, (successState as CensoredTextScreenState.Success).counter)
        }
    }

    @Test
    fun `censorText updates censoredText and increments counter`() = runTest {
        viewModel.uiStateFlow.test {
            awaitItem() // Loading
            awaitItem() // Success(0)
            
            viewModel.onEvent(CensoredTextEvent.UpdateUncensoredText("bad"))
            viewModel.onEvent(CensoredTextEvent.CensorText)
            
            testDispatcher.scheduler.advanceUntilIdle()
            val finalState = expectMostRecentItem()
            assertTrue(finalState is CensoredTextScreenState.Success)
            assertEquals("censored", (finalState as CensoredTextScreenState.Success).censoredText)
            assertEquals(1, finalState.counter)
        }
    }

    @Test
    fun `censorText failure updates state to Failure and provides retry lambda`() = runTest {
        repository.shouldReturnError = true
        
        viewModel.uiStateFlow.test {
            awaitItem() // Loading
            awaitItem() // Success(0)
            
            viewModel.onEvent(CensoredTextEvent.UpdateUncensoredText("bad"))
            viewModel.onEvent(CensoredTextEvent.CensorText)
            
            testDispatcher.scheduler.advanceUntilIdle()
            val failureState = expectMostRecentItem()
            assertTrue(failureState is CensoredTextScreenState.Failure)
            
            // Test retry
            repository.shouldReturnError = false
            (failureState as CensoredTextScreenState.Failure).onRetry()
            
            testDispatcher.scheduler.advanceUntilIdle()
            val finalState = expectMostRecentItem()
            assertTrue(finalState is CensoredTextScreenState.Success)
            assertEquals("censored", (finalState as CensoredTextScreenState.Success).censoredText)
        }
    }
}
