package se.supernovait.anya.app.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.execSQL
import androidx.room.useWriterConnection
import se.supernovait.anya.app.data.local.dao.CatDao
import se.supernovait.anya.app.data.local.dao.MedicalRecordDao
import se.supernovait.anya.app.data.local.dao.OwnerDao
import se.supernovait.anya.app.data.local.entity.Cat
import se.supernovait.anya.app.data.local.entity.MedicalRecord
import se.supernovait.anya.app.data.local.entity.Owner

@Database(
    entities = [
        Cat::class,
        Owner::class,
        MedicalRecord::class,
    ], version = 1
)
@ConstructedBy(CatDatabaseConstructor::class)
abstract class CatDatabase : RoomDatabase() {
    abstract fun catDao(): CatDao
    abstract fun ownerDao(): OwnerDao
    abstract fun medicalRecordDao(): MedicalRecordDao

    companion object {
        const val DATABASE_FILENAME = "cat.db"
    }
}

/**
 * Suspending version of clearAllTables for KMP.
 */
suspend fun CatDatabase.clearAllTablesKmp() {
    useWriterConnection { connection ->
        connection.execSQL("DELETE FROM cats")
        connection.execSQL("DELETE FROM owners")
        connection.execSQL("DELETE FROM medical_records")

        // Reset autoincrement sequences
        connection.execSQL("DELETE FROM sqlite_sequence WHERE name='cats'")
        connection.execSQL("DELETE FROM sqlite_sequence WHERE name='owners'")
        connection.execSQL("DELETE FROM sqlite_sequence WHERE name='medical_records'")
    }
}

@Suppress("KotlinNoActualForExpect")
expect object CatDatabaseConstructor : RoomDatabaseConstructor<CatDatabase> {
    override fun initialize(): CatDatabase
}
