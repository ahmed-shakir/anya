package se.supernovait.anya.util

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import assertk.assertThat
import assertk.assertions.isEqualTo
import se.supernovait.anya.core.domain.util.DeviceManager
import kotlin.test.Test

class DeviceManagerTest {

    @Test
    fun testGetBatteryLevel() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        assertThat(DeviceManager(context).getBatteryLevel()).isEqualTo(100)
    }

    @Test
    fun testGetPlatform() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        assertThat(DeviceManager(context).getPlatform().name).isEqualTo("Android 35")
    }
}
