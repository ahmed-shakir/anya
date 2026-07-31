package se.supernovait.anya.app.data.local.fakes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import se.supernovait.anya.app.data.local.dao.CatDao
import se.supernovait.anya.app.data.local.entity.Cat

class FakeCatDao : CatDao {
    private val cats = MutableStateFlow<List<Cat>>(emptyList())

    override suspend fun getCatsCount(): Long = cats.value.size.toLong()

    override fun getAll(): Flow<List<Cat>> = cats

    override fun getAllByOwnerId(ownerId: Long): Flow<List<Cat>> = cats.map { it.filter { c -> c.ownerId == ownerId } }

    override fun getAllOrderedByName(): Flow<List<Cat>> = cats.map { it.sortedBy { c -> c.name } }

    override fun getAllOrderedByBirthdate(): Flow<List<Cat>> = cats.map { it.sortedBy { c -> c.dob } }

    override suspend fun upsert(cat: Cat) {
        val current = cats.value.toMutableList()
        current.removeAll { it.id == cat.id }
        current.add(cat)
        cats.value = current
    }

    override suspend fun delete(cat: Cat) {
        cats.value = cats.value.filter { it.id != cat.id }
    }
}
