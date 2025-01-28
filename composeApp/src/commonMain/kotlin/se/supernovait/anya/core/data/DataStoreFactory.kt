package se.supernovait.anya.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

object DataStoreFactory {
    const val DATA_STORE_FILE_NAME = "anya_prefs.preferences_pb"

    fun create(producePath: () -> String): DataStore<Preferences> {
        return PreferenceDataStoreFactory.createWithPath(
            produceFile = { producePath().toPath() }
        )
    }
}
