package se.supernovait.anya.app.presentation.app.auth

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import se.supernovait.anya.app.data.local.entity.Owner
import se.supernovait.anya.app.fakes.FakeAuthRepository
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AuthenticationManagerTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var authRepository: FakeAuthRepository
    private lateinit var authManager: AuthenticationManager

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        authRepository = FakeAuthRepository()
        authManager = AuthenticationManager(authRepository)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is NotAuthenticated`() = runTest {
        assertEquals(AuthenticationState.NotAuthenticated, authManager.getCurrentState())
        assertFalse(authManager.isAuthenticated())
    }

    @Test
    fun `when user is logged in state becomes Authenticated`() = runTest {
        val user = Owner(id = 1, firstname = "John", lastname = "Doe", username = "johndoe", dob = "1990-01-01")
        
        authRepository.emitUser(user)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(authManager.isAuthenticated())
        assertEquals(user, authManager.getCurrentUser())
        assertTrue(authManager.authState.value is AuthenticationState.Authenticated)
    }

    @Test
    fun `when user logs out state becomes NotAuthenticated`() = runTest {
        val user = Owner(id = 1, firstname = "John", lastname = "Doe", username = "johndoe", dob = "1990-01-01")
        authRepository.emitUser(user)
        testDispatcher.scheduler.advanceUntilIdle()

        authManager.logout()
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(authManager.isAuthenticated())
        assertEquals(AuthenticationState.NotAuthenticated, authManager.getCurrentState())
    }

    @Test
    fun `authState flow emits correct states`() = runTest {
        val user = Owner(id = 1, firstname = "John", lastname = "Doe", username = "johndoe", dob = "1990-01-01")
        
        authManager.authState.test {
            assertEquals(AuthenticationState.NotAuthenticated, awaitItem())
            
            authRepository.emitUser(user)
            val authenticatedState = awaitItem()
            assertTrue(authenticatedState is AuthenticationState.Authenticated)
            assertEquals(user, (authenticatedState as AuthenticationState.Authenticated).user)

            authRepository.signOut()
            assertEquals(AuthenticationState.NotAuthenticated, awaitItem())
        }
    }
}
