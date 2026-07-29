package se.supernovait.anya.core.data.database

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

object DatabaseFactory {

    fun <T : RoomDatabase> create(builder: RoomDatabase.Builder<T>): T {
        return builder
            .fallbackToDestructiveMigration(dropAllTables = true)
            .setDriver(BundledSQLiteDriver())
            .build()
    }
}
