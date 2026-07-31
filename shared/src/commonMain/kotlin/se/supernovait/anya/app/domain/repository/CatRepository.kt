package se.supernovait.anya.app.domain.repository

import kotlinx.coroutines.flow.Flow
import se.supernovait.anya.app.data.local.entity.Cat
import se.supernovait.anya.app.data.local.entity.Owner

interface CatRepository {
    fun getAllCats(ownerId: Long?): Flow<List<Cat>>
    fun getAllCatsOrderedByName(): Flow<List<Cat>>
    fun getAllCatsOrderedByBirthdate(): Flow<List<Cat>>
    suspend fun upsertCat(cat: Cat)
    suspend fun deleteCat(cat: Cat)

    /* *** OWNER *** */

    fun getAllOwners(): Flow<List<Owner>>
    fun getAllOwnersOrderedByFirstname(): Flow<List<Owner>>
    fun getAllOwnersOrderedByLastname(): Flow<List<Owner>>
    fun getAllOwnersOrderedByBirthdate(): Flow<List<Owner>>
    suspend fun getOwnerById(id: Long): Owner?
    suspend fun upsertOwner(owner: Owner)
    suspend fun deleteOwner(owner: Owner)
}
