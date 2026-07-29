package se.supernovait.anya.app.fakes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import se.supernovait.anya.app.data.local.entity.Owner
import se.supernovait.anya.app.domain.repository.AuthRepository

class FakeAuthRepository : AuthRepository {
    private val _currentUserId = MutableStateFlow<Long?>(null)
    private val users = mutableMapOf<Long, Owner>()

    fun emitUser(user: Owner?) {
        if (user != null) {
            users[user.id] = user
        }
        _currentUserId.value = user?.id
    }

    override fun observeCurrentUserId(): Flow<Long?> = _currentUserId

    override suspend fun getCurrentUserId(): Long? = _currentUserId.value

    override suspend fun getUserById(id: Long): Owner? = users[id]

    override suspend fun signIn(username: String): Result<Owner> {
        val user = users.values.find { it.username == username }
        return if (user != null) {
            _currentUserId.value = user.id
            Result.success(user)
        } else {
            Result.failure(Exception("User not found"))
        }
    }

    override suspend fun signUp(owner: Owner): Result<Owner> {
        users[owner.id] = owner
        _currentUserId.value = owner.id
        return Result.success(owner)
    }

    override suspend fun signOut() {
        _currentUserId.value = null
    }
}
