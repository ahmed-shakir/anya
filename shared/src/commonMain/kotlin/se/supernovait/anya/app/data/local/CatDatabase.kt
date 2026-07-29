package se.supernovait.anya.app.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import se.supernovait.anya.app.data.local.dao.OwnerDao
import se.supernovait.anya.app.data.local.entity.Owner

@Database(
    entities = [
        Owner::class
    ], version = 1
)
@ConstructedBy(CatDatabaseConstructor::class)
abstract class CatDatabase : RoomDatabase() {
    abstract fun ownerDao(): OwnerDao

    companion object {
        const val DATABASE_FILENAME = "cat.db"
    }
}

@Suppress("KotlinNoActualForExpect")
expect object CatDatabaseConstructor : RoomDatabaseConstructor<CatDatabase> {
    override fun initialize(): CatDatabase
}
