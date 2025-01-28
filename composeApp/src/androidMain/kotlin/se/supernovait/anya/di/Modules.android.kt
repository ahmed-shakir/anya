package se.supernovait.anya.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import se.supernovait.anya.core.data.createDataStore
import se.supernovait.anya.core.data.network.HttpClientFactory
import se.supernovait.anya.core.domain.util.DeviceManager
import se.supernovait.anya.core.domain.util.applicationContext

actual val platformModule = module {
    singleOf(::DeviceManager)

    single<HttpClient> {
        HttpClientFactory.create(OkHttp.create())
    }

    single<DataStore<Preferences>> {
        createDataStore(context = applicationContext)
    }
}
