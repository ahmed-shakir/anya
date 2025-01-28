package se.supernovait.anya.core.domain.util

import se.supernovait.anya.core.domain.entity.Platform

expect class DeviceManager {
    fun getPlatform(): Platform
    fun getBatteryLevel(): Int
}