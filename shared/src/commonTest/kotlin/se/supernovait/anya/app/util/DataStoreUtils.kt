package se.supernovait.anya.app.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

expect fun createTestDataStore(): DataStore<Preferences>
