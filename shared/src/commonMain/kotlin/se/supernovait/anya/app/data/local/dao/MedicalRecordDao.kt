package se.supernovait.anya.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import se.supernovait.anya.app.data.local.entity.MedicalRecord

@Dao
interface MedicalRecordDao {

    @Query("SELECT * FROM medical_records WHERE catId = :catId ORDER BY date DESC")
    fun getAllByCatId(catId: Long): Flow<List<MedicalRecord>>

    @Query("SELECT * FROM medical_records WHERE catId = :catId ORDER BY title ASC")
    fun getAllByCatIdOrderedByTitle(catId: Long): Flow<List<MedicalRecord>>

    @Query("SELECT * FROM medical_records WHERE catId = :catId ORDER BY date ASC")
    fun getAllByCatIdOrderedByDate(catId: Long): Flow<List<MedicalRecord>>

    @Query("SELECT * FROM medical_records WHERE id = :id")
    suspend fun getById(id: Long): MedicalRecord?

    @Upsert
    suspend fun upsert(medicalRecord: MedicalRecord)

    @Delete
    suspend fun delete(medicalRecord: MedicalRecord)
}
