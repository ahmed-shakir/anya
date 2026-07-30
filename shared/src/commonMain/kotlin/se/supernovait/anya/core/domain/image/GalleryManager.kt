package se.supernovait.anya.core.domain.image

import androidx.compose.runtime.Composable

expect class GalleryManager(onLaunch: () -> Unit) {
    fun launch()
}

@Composable
expect fun rememberGalleryManager(filename: String, onResult: (SharedImage?) -> Unit): GalleryManager
