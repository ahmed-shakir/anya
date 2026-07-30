package se.supernovait.anya.app.fakes

import se.supernovait.anya.core.domain.model.Platform
import se.supernovait.anya.core.domain.manager.DeviceManager

class FakeDeviceManager : DeviceManager {
    override fun getPlatform(): Platform = object : Platform {
        override val name: String = "TestPlatform"
    }
    override fun getBatteryLevel(): Int = 100
}
