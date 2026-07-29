package se.supernovait.anya.app.util

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import se.supernovait.anya.app.data.local.CatDatabase

actual fun createInMemoryDatabase(): CatDatabase {
    return Room.inMemoryDatabaseBuilder<CatDatabase>()
        .setDriver(BundledSQLiteDriver())
        .build()
}
