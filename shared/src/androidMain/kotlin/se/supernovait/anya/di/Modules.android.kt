package se.supernovait.anya.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.RoomDatabase
import org.koin.dsl.module
import se.supernovait.anya.app.data.database.DatabaseManager
import se.supernovait.anya.app.data.local.CatDatabase
import se.supernovait.anya.core.data.preferences.createDataStore
import se.supernovait.anya.core.domain.util.applicationContext

actual val platformModule = module {

    single<RoomDatabase.Builder<CatDatabase>> {
        DatabaseManager.createCatDatabase(context = applicationContext)
    }

    single<DataStore<Preferences>> {
        createDataStore(context = applicationContext)
    }
}
