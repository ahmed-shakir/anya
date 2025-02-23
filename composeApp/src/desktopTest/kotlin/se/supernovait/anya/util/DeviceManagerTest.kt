package se.supernovait.anya.util

import assertk.assertThat
import assertk.assertions.isEqualTo
import se.supernovait.anya.core.domain.util.DeviceManager
import kotlin.test.Test

class DeviceManagerTest {

    @Test
    fun testGetBatteryLevel() {
        assertThat(DeviceManager().getBatteryLevel()).isEqualTo(100)
    }

    @Test
    fun testGetPlatform() {
        assertThat(DeviceManager().getPlatform().name).isEqualTo("Java 21.0.1")
    }
}
