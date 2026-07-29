package se.supernovait.anya.app.presentation.app.auth

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import se.supernovait.anya.app.data.local.entity.Owner
import se.supernovait.anya.app.domain.repository.AuthRepository

/**
 * Manages the app-wide authentication state.
 * Exposes AppAuthState as a StateFlow that all screens can observe reactively.
 *
 * Singleton - there's only one instance throughout the app's lifetime.
 * Inject this into ViewModels or Composables to observe/update auth state.
 */
class AuthenticationManager(private val authRepository: AuthRepository) {
    private val managerScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val _authState = MutableStateFlow<AuthenticationState>(AuthenticationState.NotAuthenticated)
    val authState: StateFlow<AuthenticationState> = _authState.asStateFlow()

    init {
        observeAuthChanges()
    }

    private fun observeAuthChanges() {
        authRepository.observeCurrentUserId()
            .onEach { userId ->
                if (userId != null) {
                    val user = authRepository.getUserById(userId)
                    if (user != null) {
                        _authState.value = AuthenticationState.Authenticated(user)
                    } else {
                        _authState.value = AuthenticationState.NotAuthenticated
                    }
                } else {
                    _authState.value = AuthenticationState.NotAuthenticated
                }
            }
            .launchIn(managerScope)
    }

    /**
     * Log out the current user and return to unauthenticated state.
     */
    fun logout() {
        managerScope.launch {
            authRepository.signOut()
        }
    }

    /**
     * Get the current authentication state without collecting as flow.
     * Useful when you need the state synchronously.
     */
    fun getCurrentState(): AuthenticationState = _authState.value

    /**
     * Convenience function to check if user is currently authenticated.
     */
    fun isAuthenticated(): Boolean = _authState.value is AuthenticationState.Authenticated

    /**
     * Get the currently authenticated user, or null if not authenticated.
     */
    fun getCurrentUser(): Owner? = (_authState.value as? AuthenticationState.Authenticated)?.user
}
