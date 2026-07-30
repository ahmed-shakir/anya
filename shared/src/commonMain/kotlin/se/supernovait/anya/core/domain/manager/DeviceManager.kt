package se.supernovait.anya.core.domain.manager

import se.supernovait.anya.core.domain.model.Platform

interface DeviceManager {
    fun getPlatform(): Platform
    fun getBatteryLevel(): Int
}
