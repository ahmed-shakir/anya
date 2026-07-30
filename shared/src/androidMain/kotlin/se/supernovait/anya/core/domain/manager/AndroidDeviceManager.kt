package se.supernovait.anya.core.domain.manager

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import se.supernovait.anya.core.domain.model.Platform
import kotlin.math.roundToInt

class AndroidDeviceManager(private val context: Context) : DeviceManager {
    override fun getPlatform(): Platform {
        return AndroidPlatform
    }

    override fun getBatteryLevel(): Int {
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = context.registerReceiver(null, intentFilter)
        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1

        return (level / scale.toFloat() * 100).roundToInt()
    }
}

private object AndroidPlatform: Platform {
    override val name = "Android ${Build.VERSION.SDK_INT}"
}
