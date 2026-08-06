package se.supernovait.anya.app.domain.repository

import kotlinx.coroutines.flow.Flow
import se.supernovait.anya.app.data.local.entity.Owner

interface AuthRepository {
    /**
     * Observe the currently authenticated user's ID.
     */
    fun observeCurrentUserId(): Flow<Long?>

    /**
     * Observe user by ID.
     */
    fun observeUserById(id: Long): Flow<Owner?>

    /**
     * Get the currently authenticated user's ID synchronously (if possible/cached).
     */
    suspend fun getCurrentUserId(): Long?

    /**
     * Get user by ID.
     */
    suspend fun getUserById(id: Long): Owner?

    /**
     * Sign in an existing user by their username.
     * Returns the user if found, or an error if not found.
     */
    suspend fun signIn(username: String): Result<Owner>

    /**
     * Sign up a new user.
     * Persists the user and sets them as the currently authenticated identity.
     */
    suspend fun signUp(owner: Owner): Result<Owner>

    /**
     * Sign out the current user.
     * Clears the persistent identity.
     */
    suspend fun signOut()
}
