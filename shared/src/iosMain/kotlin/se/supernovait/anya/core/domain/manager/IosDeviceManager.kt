package se.supernovait.anya.core.domain.manager

import platform.UIKit.UIDevice
import se.supernovait.anya.core.domain.model.Platform
import kotlin.math.roundToInt

class IosDeviceManager : DeviceManager {
    override fun getPlatform(): Platform {
        return IosPlatform
    }

    override fun getBatteryLevel(): Int {
        UIDevice.currentDevice.batteryMonitoringEnabled = true
        val batteryLevel = UIDevice.currentDevice.batteryLevel

        return (batteryLevel * 100).roundToInt()
    }
}

private object IosPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}
