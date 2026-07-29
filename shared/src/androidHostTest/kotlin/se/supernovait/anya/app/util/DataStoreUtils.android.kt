package se.supernovait.anya.app.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath
import java.io.File

actual fun createTestDataStore(): DataStore<Preferences> {
    val testFile = File.createTempFile("test_prefs", ".preferences_pb")
    testFile.deleteOnExit()
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = { testFile.absolutePath.toPath() }
    )
}
