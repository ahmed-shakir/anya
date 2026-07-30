package se.supernovait.anya.core.domain.file

import androidx.compose.runtime.Composable

expect class FileManager(onLaunch: (mimeTypes: List<String>) -> Unit) {
    fun launch(mimeTypes: List<String>)
}

@Composable
expect fun rememberFileManager(filename: String, onResult: (SharedFile?) -> Unit): FileManager

@Composable
expect fun loadFileFromDisk(uri: String?): SharedFile?
