package se.supernovait.anya.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.RoomDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import se.supernovait.anya.app.data.database.DatabaseManager
import se.supernovait.anya.app.data.local.CatDatabase
import se.supernovait.anya.core.data.network.IosNetworkHandler
import se.supernovait.anya.core.data.preferences.createDataStore
import se.supernovait.anya.core.domain.manager.DeviceManager
import se.supernovait.anya.core.domain.manager.IosDeviceManager
import se.supernovait.anya.core.domain.network.NetworkHandler

actual val platformModule = module {
    singleOf(::IosDeviceManager).bind<DeviceManager>()

    single<NetworkHandler> {
        IosNetworkHandler()
    }

    single<RoomDatabase.Builder<CatDatabase>> {
        DatabaseManager.createCatDatabase()
    }

    single<DataStore<Preferences>> {
        createDataStore()
    }
}
