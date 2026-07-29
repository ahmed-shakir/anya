package se.supernovait.anya.app.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import se.supernovait.anya.app.data.local.dao.OwnerDao
import se.supernovait.anya.app.data.local.entity.Owner
import se.supernovait.anya.app.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val ownerDao: OwnerDao,
    private val prefs: DataStore<Preferences>
) : AuthRepository {

    override fun observeCurrentUserId(): Flow<Long?> {
        return prefs.data.map { preferences ->
            val id = preferences[longPreferencesKey(APP_USER_IDENTITY_KEY)]
            if (id == null || id == 0L) null else id
        }
    }

    override suspend fun getCurrentUserId(): Long? {
        return observeCurrentUserId().firstOrNull()
    }

    override suspend fun getUserById(id: Long): Owner? {
        return ownerDao.getUserById(id)
    }

    override suspend fun signUp(owner: Owner): Result<Owner> {
        return try {
            val id = ownerDao.upsert(owner)
            val savedUser = ownerDao.getUserById(id)
            if (savedUser != null) {
                saveUserToPrefs(id)
                Result.success(savedUser)
            } else {
                Result.failure(Exception("Failed to retrieve saved user"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signIn(username: String): Result<Owner> {
        val user = ownerDao.getUserByUsername(username)
        return if (user != null) {
            saveUserToPrefs(user.id)
            Result.success(user)
        } else {
            Result.failure(Exception("User not found"))
        }
    }

    override suspend fun signOut() {
        prefs.edit { preferences ->
            preferences.remove(longPreferencesKey(APP_USER_IDENTITY_KEY))
        }
    }

    private suspend fun saveUserToPrefs(id: Long) {
        prefs.edit { preferences ->
            preferences[longPreferencesKey(APP_USER_IDENTITY_KEY)] = id
        }
    }

    companion object {
        const val APP_USER_IDENTITY_KEY = "app_user_identity"
    }
}
