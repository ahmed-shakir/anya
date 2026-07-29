package se.supernovait.anya.di

import androidx.room.RoomDatabase
import org.koin.dsl.module
import se.supernovait.anya.app.data.database.DatabaseManager
import se.supernovait.anya.app.data.local.CatDatabase

actual val platformModule = module {

    single<RoomDatabase.Builder<CatDatabase>> {
        DatabaseManager.createCatDatabase()
    }
}
