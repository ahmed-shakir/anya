package se.supernovait.anya.app.presentation.info

import se.supernovait.anya.core.domain.model.Platform

data class InfoScreenState(
    val platform: Platform = DefaultPlatform,
    val batteryLevel: String = "N/A",
    val networkStatus: String = "Unknown"
)

private object DefaultPlatform: Platform {
    override val name = "N/A"
}
