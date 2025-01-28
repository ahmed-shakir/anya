package se.supernovait.anya.core.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

fun createDataStore(context: Context): DataStore<Preferences> {
    return DataStoreFactory.create {
        context.filesDir.resolve(DataStoreFactory.DATA_STORE_FILE_NAME).absolutePath
    }
}
