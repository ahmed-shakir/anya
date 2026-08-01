package se.supernovait.anya.app.fakes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import se.supernovait.anya.app.data.local.entity.Cat
import se.supernovait.anya.app.data.local.entity.MedicalRecord
import se.supernovait.anya.app.data.local.entity.Owner
import se.supernovait.anya.app.data.local.entity.relation.CatAndOwner
import se.supernovait.anya.app.data.local.entity.relation.OwnerWithCats
import se.supernovait.anya.app.domain.repository.CatRepository

class FakeCatRepository : CatRepository {
    private val cats = MutableStateFlow<List<Cat>>(emptyList())
    private val owners = MutableStateFlow<List<Owner>>(emptyList())
    private val records = MutableStateFlow<List<MedicalRecord>>(emptyList())
    var shouldReturnError = false

    override fun getAllCats(ownerId: Long?): Flow<List<Cat>> = cats.map { 
        if (ownerId != null) it.filter { c -> c.ownerId == ownerId } else it 
    }
    override fun getAllCatsOrderedByName(): Flow<List<Cat>> = cats.map { it.sortedBy { c -> c.name } }
    override fun getAllCatsOrderedByBirthdate(): Flow<List<Cat>> = cats.map { it.sortedBy { c -> c.dob } }

    override suspend fun getCatById(id: Long): CatAndOwner? {
        if (shouldReturnError) throw Exception("Repository error")
        val cat = cats.value.find { it.id == id } ?: return null
        val owner = owners.value.find { it.id == cat.ownerId }
        return CatAndOwner(cat, owner)
    }

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

    override suspend fun getOwnerById(id: Long): OwnerWithCats? {
        if (shouldReturnError) throw Exception("Repository error")
        val owner = owners.value.find { it.id == id } ?: return null
        val ownerCats = cats.value.filter { it.ownerId == id }
        return OwnerWithCats(owner, ownerCats)
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

    override fun getAllMedicalRecordsByCatId(catId: Long): Flow<List<MedicalRecord>> = records.map { it.filter { r -> r.catId == catId } }
    override fun getAllMedicalRecordsByCatIdOrderedByTitle(catId: Long): Flow<List<MedicalRecord>> = records.map { it.filter { r -> r.catId == catId }.sortedBy { r -> r.title } }
    override fun getAllMedicalRecordsByCatIdOrderedByDate(catId: Long): Flow<List<MedicalRecord>> = records.map { it.filter { r -> r.catId == catId }.sortedBy { r -> r.date } }

    override suspend fun getMedicalRecordById(id: Long): MedicalRecord? = records.value.find { it.id == id }

    override suspend fun upsertMedicalRecord(medicalRecord: MedicalRecord) {
        val current = records.value.toMutableList()
        current.removeAll { it.id == medicalRecord.id }
        current.add(medicalRecord)
        records.value = current
    }

    override suspend fun deleteMedicalRecord(medicalRecord: MedicalRecord) {
        records.value = records.value.filter { it.id != medicalRecord.id }
    }
}
