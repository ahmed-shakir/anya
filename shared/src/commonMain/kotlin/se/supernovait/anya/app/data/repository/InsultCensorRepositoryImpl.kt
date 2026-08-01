package se.supernovait.anya.app.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import se.supernovait.anya.app.data.remote.InsultCensorApi
import se.supernovait.anya.app.domain.repository.InsultCensorRepository
import se.supernovait.anya.core.domain.model.error.NetworkError
import se.supernovait.anya.core.domain.util.Result
import kotlin.coroutines.CoroutineContext

class InsultCensorRepositoryImpl(
    private val prefs: DataStore<Preferences>,
    private val insultCensorApi: InsultCensorApi
) : InsultCensorRepository {
    private val ioContext: CoroutineContext = Dispatchers.IO

    override suspend fun censorWords(uncensored: String): Result<String, NetworkError> {
        return withContext(ioContext) {
            insultCensorApi.censorWords(uncensored)
        }
    }

    override suspend fun getCounter(): Int {
        return prefs
            .data
            .map {
                val counterKey = intPreferencesKey(CENSORED_TEXT_COUNTER_KEY)
                it[counterKey] ?: 0
            }
            .first()
    }

    override suspend fun incrementCounter(): Int {
        prefs.edit { dataStore ->
            val counterKey = intPreferencesKey(CENSORED_TEXT_COUNTER_KEY)
            val counter = getCounter() + 1
            dataStore[counterKey] = counter
        }
        return getCounter()
    }

    companion object {
        const val CENSORED_TEXT_COUNTER_KEY = "censored_text_counter"
    }
}
