package se.supernovait.anya.app.presentation.app.auth

import androidx.compose.runtime.compositionLocalOf
import se.supernovait.anya.app.data.local.entity.Owner

/**
 * Represents the authentication state of the app.
 * Holds the currently authenticated user throughout the app's lifetime.
 * Exposed as a singleton StateFlow for reactive access by all screens.
 */
sealed interface AuthenticationState {
    data object NotAuthenticated : AuthenticationState
    data class Authenticated(val user: Owner) : AuthenticationState
    data class Error(val message: String, val throwable: Throwable? = null) : AuthenticationState
}

val AuthenticationState.user: Owner?
    get() = (this as? AuthenticationState.Authenticated)?.user

val AuthenticationState.userId: Long
    get() = user?.id ?: 0L

val LocalAuthState = compositionLocalOf<AuthenticationState> { AuthenticationState.NotAuthenticated }
