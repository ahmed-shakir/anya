package se.supernovait.anya.app.data.local.fakes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import se.supernovait.anya.app.data.local.dao.OwnerDao
import se.supernovait.anya.app.data.local.entity.Owner

class FakeOwnerDao : OwnerDao {
    private val owners = MutableStateFlow<List<Owner>>(emptyList())

    override suspend fun getOwnersCount(): Long = owners.value.size.toLong()
    override fun getAll(): Flow<List<Owner>> = owners
    override fun getAllOrderedByFirstname(): Flow<List<Owner>> = owners
    override fun getAllOrderedByLastname(): Flow<List<Owner>> = owners
    override fun getAllOrderedByBirthdate(): Flow<List<Owner>> = owners
    override suspend fun getUserById(id: Long): Owner? = owners.value.find { it.id == id }
    override suspend fun getUserByUsername(username: String): Owner? = owners.value.find { it.username == username }
    override suspend fun upsert(owner: Owner): Long {
        val current = owners.value.toMutableList()
        current.removeAll { it.id == owner.id }
        current.add(owner)
        owners.value = current
        return owner.id
    }
    override suspend fun delete(owner: Owner) {
        owners.value = owners.value.filter { it.id != owner.id }
    }
}
