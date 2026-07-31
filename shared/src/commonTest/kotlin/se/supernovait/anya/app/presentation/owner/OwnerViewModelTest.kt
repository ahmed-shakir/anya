package se.supernovait.anya.app.presentation.owner

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import se.supernovait.anya.app.data.local.entity.Owner
import se.supernovait.anya.app.fakes.FakeCatRepository
import se.supernovait.anya.app.presentation.address.AddressState
import se.supernovait.anya.app.presentation.app.AppEvent
import se.supernovait.anya.app.presentation.owner.state.OwnerState
import se.supernovait.anya.app.util.AnyaBaseTest
import se.supernovait.anya.core.domain.model.error.NetworkError
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class OwnerViewModelTest : AnyaBaseTest() {
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
    fun `initial state shows empty owners`() = runTest {
        val viewModel = createViewModel()
        
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.owners.isEmpty())
        }
    }

    @Test
    fun `when owners are loaded uiState is updated`() = runTest {
        val owner = Owner(id = 1, firstname = "John", lastname = "Doe", username = "johndoe", dob = "1990-01-01")
        catRepository.upsertOwner(owner)
        
        val viewModel = createViewModel()

        viewModel.uiState.test {
            testDispatcher.scheduler.advanceUntilIdle()
            val state = expectMostRecentItem()
            assertEquals(1, state.owners.size)
            assertEquals("John", state.owners[0].firstname)
        }
    }

    @Test
    fun `SaveOwner saves owner to repository`() = runTest {
        val viewModel = createViewModel()
        val ownerState = OwnerState(id = 0, firstname = "Jane", lastname = "Doe", username = "janedoe", dob = LocalDate.parse("1995-01-01"))
        
        catRepository.getAllOwners().test {
            viewModel.onEvent(OwnerScreenEvent.SaveOwner(ownerState))
            testDispatcher.scheduler.advanceUntilIdle()
            val owners = expectMostRecentItem()
            assertEquals(1, owners.size)
            assertEquals("Jane", owners[0].firstname)
        }
    }

    @Test
    fun `SaveOwner with invalid data emits BAD_REQUEST`() = runTest {
        val viewModel = createViewModel()
        val invalidOwner = OwnerState(firstname = "", lastname = "", dob = null)

        viewModel.events.test {
            viewModel.onEvent(OwnerScreenEvent.SaveOwner(invalidOwner))
            testDispatcher.scheduler.advanceUntilIdle()
            val event = expectMostRecentItem()
            assertTrue(event is AppEvent.Error)
            assertEquals(NetworkError.BAD_REQUEST, (event as AppEvent.Error).error)
        }
    }

    @Test
    fun `SaveAddress for missing owner emits SERVER_ERROR`() = runTest {
        val viewModel = createViewModel()
        val address = AddressState(street = "Main St", city = "City", county = "County", country = "Country", postalCode = "12345")

        viewModel.events.test {
            viewModel.onEvent(OwnerScreenEvent.SaveAddress(ownerId = 999L, address = address))
            testDispatcher.scheduler.advanceUntilIdle()
            val event = expectMostRecentItem()
            assertTrue(event is AppEvent.Error)
            assertEquals(NetworkError.SERVER_ERROR, (event as AppEvent.Error).error)
        }
    }

    private fun createViewModel(): OwnerViewModel {
        return OwnerViewModel(
            savedStateHandle = SavedStateHandle(),
            catRepository = catRepository
        )
    }
}
