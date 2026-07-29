package se.supernovait.anya.app.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import se.supernovait.anya.app.data.local.CatDatabase

object DatabaseManager {

    fun createCatDatabase(context: Context): RoomDatabase.Builder<CatDatabase> {
        val dbFile = context.getDatabasePath(CatDatabase.DATABASE_FILENAME)
        return Room.databaseBuilder<CatDatabase>(
            context = context.applicationContext,
            name = dbFile.absolutePath
        )
    }
}
