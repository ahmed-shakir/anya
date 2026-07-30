package se.supernovait.anya.core.domain.file

import androidx.compose.ui.graphics.ImageBitmap

expect class SharedFile {
    fun toByteArray(): ByteArray?
    fun toImageBitmap(): ImageBitmap?
    fun toText(): String?
    fun fileName(): String?
    fun uri(): String
}
