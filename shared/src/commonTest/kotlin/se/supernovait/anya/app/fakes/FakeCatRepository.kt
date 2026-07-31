package se.supernovait.anya.app.fakes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import se.supernovait.anya.app.data.local.entity.Owner
import se.supernovait.anya.app.domain.repository.CatRepository

class FakeCatRepository : CatRepository {
    private val owners = MutableStateFlow<List<Owner>>(emptyList())
    var shouldReturnError = false

    override fun getAllOwners(): Flow<List<Owner>> = owners
    override fun getAllOwnersOrderedByFirstname(): Flow<List<Owner>> = owners.map { it.sortedBy { o -> o.firstname } }
    override fun getAllOwnersOrderedByLastname(): Flow<List<Owner>> = owners.map { it.sortedBy { o -> o.lastname } }
    override fun getAllOwnersOrderedByBirthdate(): Flow<List<Owner>> = owners.map { it.sortedBy { o -> o.dob } }

    override suspend fun getOwnerById(id: Long): Owner? {
        if (shouldReturnError) throw Exception("Repository error")
        return owners.value.find { it.id == id }
    }

    override suspend fun upsertOwner(owner: Owner) {
        if (shouldReturnError) throw Exception("Repository error")
        val current = owners.value.toMutableList()
        current.removeAll { it.id == owner.id }
        current.add(owner)
        owners.value = current
    }

    override suspend fun deleteOwner(owner: Owner) {
        owners.value = owners.value.filter { it.id != owner.id }
    }
}
