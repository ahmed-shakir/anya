package se.supernovait.anya.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import se.supernovait.anya.app.data.local.entity.Cat

@Dao
interface CatDao {

    @Query("SELECT COUNT(*) FROM cats")
    suspend fun getCatsCount(): Long

    @Query("SELECT * FROM cats")
    fun getAll(): Flow<List<Cat>>

    @Query("SELECT * FROM cats WHERE ownerId = :ownerId")
    fun getAllByOwnerId(ownerId: Long): Flow<List<Cat>>

    @Query("SELECT * FROM cats ORDER BY name ASC")
    fun getAllOrderedByName(): Flow<List<Cat>>

    @Query("SELECT * FROM cats ORDER BY dob ASC")
    fun getAllOrderedByBirthdate(): Flow<List<Cat>>

    @Upsert
    suspend fun upsert(cat: Cat)

    @Delete
    suspend fun delete(cat: Cat)
}
