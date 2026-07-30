package se.supernovait.anya.core.domain.image

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get
import kotlinx.cinterop.reinterpret
import org.jetbrains.skia.Image
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation

actual class SharedImage(private val uri: String?, private val image: UIImage?) {

    @OptIn(ExperimentalForeignApi::class)
    actual fun toByteArray(): ByteArray? {
        return image?.let {
            val imageData = UIImageJPEGRepresentation(it, COMPRESSION_QUALITY)
            val bytes = imageData?.bytes?.reinterpret<ByteVar>()
            val length = imageData?.length?.toInt() ?: 0
            ByteArray(length) { index -> bytes!![index] }
        }
    }

    actual fun toImageBitmap(): ImageBitmap? {
        val byteArray = toByteArray()
        return byteArray?.let {
            Image.makeFromEncoded(it).toComposeImageBitmap()
        }
    }

    actual fun uri(): String? = uri

    companion object {
        const val COMPRESSION_QUALITY = 0.99
    }
}
