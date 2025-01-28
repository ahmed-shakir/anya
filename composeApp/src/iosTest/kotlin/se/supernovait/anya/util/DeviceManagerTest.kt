package se.supernovait.anya.util

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotZero
import se.supernovait.anya.core.domain.util.DeviceManager
import kotlin.test.Test

class DeviceManagerTest {

    @Test
    fun testGetBatteryLevel() {
        assertThat(DeviceManager().getBatteryLevel()).isNotZero()
    }

    @Test
    fun testGetPlatform() {
        assertThat(DeviceManager().getPlatform().name).isEqualTo("iOS 18.2")
    }
}
