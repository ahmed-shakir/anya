package se.supernovait.anya.core.domain.util

import platform.UIKit.UIDevice
import se.supernovait.anya.core.domain.entity.Platform
import kotlin.math.roundToInt

actual class DeviceManager() {
    actual fun getPlatform(): Platform {
        return object: Platform {
            override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
        }
    }

    actual fun getBatteryLevel(): Int {
        UIDevice.currentDevice.batteryMonitoringEnabled = true
        val batteryLevel = UIDevice.currentDevice.batteryLevel

        return (batteryLevel * 100).roundToInt()
    }
}
