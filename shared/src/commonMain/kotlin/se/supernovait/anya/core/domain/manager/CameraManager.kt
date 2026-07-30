package se.supernovait.anya.core.domain.manager

import androidx.compose.runtime.Composable
import se.supernovait.anya.core.domain.image.SharedImage

expect class CameraManager(onLaunch: () -> Unit) {
    fun launch()
}

@Composable
expect fun rememberCameraManager(filename: String, onResult: (SharedImage?) -> Unit): CameraManager
