package se.supernovait.anya.app.fakes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import se.supernovait.anya.app.data.local.entity.Cat
import se.supernovait.anya.app.data.local.entity.Owner
import se.supernovait.anya.app.domain.repository.CatRepository

class FakeCatRepository : CatRepository {
    private val cats = MutableStateFlow<List<Cat>>(emptyList())
    private val owners = MutableStateFlow<List<Owner>>(emptyList())
    var shouldReturnError = false

    override fun getAllCats(ownerId: Long?): Flow<List<Cat>> = cats.map { 
        if (ownerId != null) it.filter { c -> c.ownerId == ownerId } else it 
    }
    override fun getAllCatsOrderedByName(): Flow<List<Cat>> = cats.map { it.sortedBy { c -> c.name } }
    override fun getAllCatsOrderedByBirthdate(): Flow<List<Cat>> = cats.map { it.sortedBy { c -> c.dob } }

    override suspend fun upsertCat(cat: Cat) {
        if (shouldReturnError) throw Exception("Repository error")
        val current = cats.value.toMutableList()
        current.removeAll { it.id == cat.id }
        current.add(cat)
        cats.value = current
    }

    override suspend fun deleteCat(cat: Cat) {
        if (shouldReturnError) throw Exception("Repository error")
        cats.value = cats.value.filter { it.id != cat.id }
    }

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
