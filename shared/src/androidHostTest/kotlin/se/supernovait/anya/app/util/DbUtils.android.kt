package se.supernovait.anya.app.util

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import se.supernovait.anya.app.data.local.CatDatabase

actual fun createInMemoryDatabase(): CatDatabase {
    val context = ApplicationProvider.getApplicationContext<Context>()
    return Room.inMemoryDatabaseBuilder(
        context,
        CatDatabase::class.java
    ).build()
}
