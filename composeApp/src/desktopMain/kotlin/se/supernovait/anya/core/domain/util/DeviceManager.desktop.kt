package se.supernovait.anya.core.domain.util

import oshi.SystemInfo
import se.supernovait.anya.core.domain.entity.Platform
import kotlin.math.roundToInt

actual class DeviceManager() {
    actual fun getPlatform(): Platform {
        return object: Platform {
            override val name: String = "Java ${System.getProperty("java.version")}"
        }
    }

    actual fun getBatteryLevel(): Int {
        val systemInfo = SystemInfo()
        val batteryLevel = systemInfo.hardware.powerSources.firstOrNull()

        return batteryLevel?.remainingCapacityPercent?.times(100)?.roundToInt() ?: -1
    }
}
