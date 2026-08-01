package se.supernovait.anya.app.data.local.fakes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import se.supernovait.anya.app.data.local.dao.MedicalRecordDao
import se.supernovait.anya.app.data.local.entity.MedicalRecord

class FakeMedicalRecordDao : MedicalRecordDao {
    private val records = MutableStateFlow<List<MedicalRecord>>(emptyList())

    override fun getAllByCatId(catId: Long): Flow<List<MedicalRecord>> = records.map { it.filter { r -> r.catId == catId } }
    override fun getAllByCatIdOrderedByTitle(catId: Long): Flow<List<MedicalRecord>> = records.map { it.filter { r -> r.catId == catId } }
    override fun getAllByCatIdOrderedByDate(catId: Long): Flow<List<MedicalRecord>> = records.map { it.filter { r -> r.catId == catId } }
    override suspend fun getById(id: Long): MedicalRecord? = records.value.find { it.id == id }
    override suspend fun upsert(medicalRecord: MedicalRecord) {
        val current = records.value.toMutableList()
        current.removeAll { it.id == medicalRecord.id }
        current.add(medicalRecord)
        records.value = current
    }
    override suspend fun delete(medicalRecord: MedicalRecord) {
        records.value = records.value.filter { it.id != medicalRecord.id }
    }
}
