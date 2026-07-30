package se.supernovait.anya.app.domain.repository

import kotlinx.coroutines.flow.Flow
import se.supernovait.anya.app.data.local.entity.Owner

interface CatRepository {
    fun getAllOwners(): Flow<List<Owner>>
    fun getAllOwnersOrderedByFirstname(): Flow<List<Owner>>
    fun getAllOwnersOrderedByLastname(): Flow<List<Owner>>
    fun getAllOwnersOrderedByBirthdate(): Flow<List<Owner>>
    suspend fun getOwnerById(id: Long): Owner?
    suspend fun upsertOwner(owner: Owner)
    suspend fun deleteOwner(owner: Owner)
}
