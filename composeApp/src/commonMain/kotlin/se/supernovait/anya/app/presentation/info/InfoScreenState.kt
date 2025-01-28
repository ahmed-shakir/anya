package se.supernovait.anya.app.presentation.info

import se.supernovait.anya.core.domain.entity.Platform

data class InfoScreenState(
    val platform: Platform = object: Platform {
        override val name = "N/A"
    },
    val batteryLevel: String = "N/A",
)
