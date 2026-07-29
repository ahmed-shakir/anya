package se.supernovait.anya.app.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import se.supernovait.anya.app.data.local.CatDatabase

object DatabaseManager {

    fun createCatDatabase(): RoomDatabase.Builder<CatDatabase> {
        val dbFile = documentDirectory() + "/${CatDatabase.DATABASE_FILENAME}"
        return Room.databaseBuilder<CatDatabase>(name = dbFile)
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val directory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    )
    return requireNotNull(directory?.path)
}
