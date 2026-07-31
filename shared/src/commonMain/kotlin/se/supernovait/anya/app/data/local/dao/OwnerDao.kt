package se.supernovait.anya.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import se.supernovait.anya.app.data.local.entity.Owner
import se.supernovait.anya.app.data.local.entity.relation.OwnerWithCats

@Dao
interface OwnerDao {

    @Query("SELECT COUNT(*) FROM owners")
    suspend fun getOwnersCount(): Long

    @Query("SELECT * FROM owners")
    fun getAll(): Flow<List<Owner>>

    @Query("SELECT * FROM owners ORDER BY firstname ASC")
    fun getAllOrderedByFirstname(): Flow<List<Owner>>

    @Query("SELECT * FROM owners ORDER BY lastname ASC")
    fun getAllOrderedByLastname(): Flow<List<Owner>>

    @Query("SELECT * FROM owners ORDER BY dob ASC")
    fun getAllOrderedByBirthdate(): Flow<List<Owner>>

    @Transaction
    @Query("SELECT * FROM owners WHERE id = :id")
    suspend fun getById(id: Long): OwnerWithCats?

    @Query("SELECT * FROM owners WHERE id = :id")
    suspend fun getUserById(id: Long): Owner?

    @Query("SELECT * FROM owners WHERE username = :username")
    suspend fun getUserByUsername(username: String): Owner?

    @Upsert
    suspend fun upsert(owner: Owner): Long

    @Delete
    suspend fun delete(owner: Owner)
}
