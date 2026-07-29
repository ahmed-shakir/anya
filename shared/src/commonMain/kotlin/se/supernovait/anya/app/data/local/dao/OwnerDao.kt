package se.supernovait.anya.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import se.supernovait.anya.app.data.local.entity.Owner

@Dao
interface OwnerDao {

    @Query("SELECT * FROM owners")
    fun getAll(): Flow<List<Owner>>

    @Query("SELECT * FROM owners WHERE id = :id")
    suspend fun getUserById(id: Long): Owner?

    @Query("SELECT * FROM owners WHERE username = :username")
    suspend fun getUserByUsername(username: String): Owner?

    @Upsert
    suspend fun upsert(owner: Owner): Long

    @Delete
    suspend fun delete(owner: Owner)
}
