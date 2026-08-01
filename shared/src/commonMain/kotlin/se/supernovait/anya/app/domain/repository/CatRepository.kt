package se.supernovait.anya.app.domain.repository

import kotlinx.coroutines.flow.Flow
import se.supernovait.anya.app.data.local.entity.Cat
import se.supernovait.anya.app.data.local.entity.MedicalRecord
import se.supernovait.anya.app.data.local.entity.Owner
import se.supernovait.anya.app.data.local.entity.relation.CatAndOwner
import se.supernovait.anya.app.data.local.entity.relation.OwnerWithCats

interface CatRepository {
    fun getAllCats(ownerId: Long?): Flow<List<Cat>>
    fun getAllCatsOrderedByName(): Flow<List<Cat>>
    fun getAllCatsOrderedByBirthdate(): Flow<List<Cat>>
    suspend fun getCatById(id: Long): CatAndOwner?
    suspend fun upsertCat(cat: Cat)
    suspend fun deleteCat(cat: Cat)

    /* *** OWNER *** */

    fun getAllOwners(): Flow<List<Owner>>
    fun getAllOwnersOrderedByFirstname(): Flow<List<Owner>>
    fun getAllOwnersOrderedByLastname(): Flow<List<Owner>>
    fun getAllOwnersOrderedByBirthdate(): Flow<List<Owner>>
    suspend fun getOwnerById(id: Long): OwnerWithCats?
    suspend fun upsertOwner(owner: Owner)
    suspend fun deleteOwner(owner: Owner)

    fun getAllMedicalRecordsByCatId(catId: Long): Flow<List<MedicalRecord>>
    fun getAllMedicalRecordsByCatIdOrderedByTitle(catId: Long): Flow<List<MedicalRecord>>
    fun getAllMedicalRecordsByCatIdOrderedByDate(catId: Long): Flow<List<MedicalRecord>>
    suspend fun getMedicalRecordById(id: Long): MedicalRecord?
    suspend fun upsertMedicalRecord(medicalRecord: MedicalRecord)
    suspend fun deleteMedicalRecord(medicalRecord: MedicalRecord)
}
