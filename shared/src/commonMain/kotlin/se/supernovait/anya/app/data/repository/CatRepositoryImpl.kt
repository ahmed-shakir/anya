package se.supernovait.anya.app.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import se.supernovait.anya.app.data.local.dao.OwnerDao
import se.supernovait.anya.app.data.local.entity.Owner
import se.supernovait.anya.app.domain.repository.CatRepository
import kotlin.coroutines.CoroutineContext

class CatRepositoryImpl(
    private val ownerDao: OwnerDao
) : CatRepository {
    private val ioContext: CoroutineContext = Dispatchers.IO

    /* *** OWNER *** */

    override fun getAllOwners(): Flow<List<Owner>> {
        return ownerDao.getAll()
    }

    override fun getAllOwnersOrderedByFirstname(): Flow<List<Owner>> {
        return ownerDao.getAllOrderedByFirstname()
    }

    override fun getAllOwnersOrderedByLastname(): Flow<List<Owner>> {
        return ownerDao.getAllOrderedByLastname()
    }

    override fun getAllOwnersOrderedByBirthdate(): Flow<List<Owner>> {
        return ownerDao.getAllOrderedByBirthdate()
    }

    override suspend fun upsertOwner(owner: Owner) {
        withContext(ioContext) {
            ownerDao.upsert(owner)
        }
    }

    override suspend fun deleteOwner(owner: Owner) {
        withContext(ioContext) {
            ownerDao.delete(owner)
        }
    }
}
