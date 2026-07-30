package se.supernovait.anya.core.domain.file

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get
import kotlinx.cinterop.reinterpret
import org.jetbrains.skia.Image
import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Foundation.dataWithContentsOfURL

actual class SharedFile(private val uri: String) {

    @OptIn(ExperimentalForeignApi::class)
    actual fun toByteArray(): ByteArray? {
        return loadData(uri)?.let {
            val bytes = it.bytes?.reinterpret<ByteVar>()
            val length = it.length.toInt()
            ByteArray(length) { index -> bytes!![index] }
        }
    }

    actual fun toImageBitmap(): ImageBitmap? {
        val byteArray = toByteArray()
        return byteArray?.let {
            Image.makeFromEncoded(it).toComposeImageBitmap()
        }
    }

    @OptIn(BetaInteropApi::class)
    actual fun toText(): String? {
        return loadData(uri)?.let {
            NSString.create(it, NSUTF8StringEncoding)?.toString()
        }
    }

    actual fun fileName(): String? {
        return NSURL(string = uri).lastPathComponent
    }

    actual fun uri(): String = uri

    private fun loadData(uri: String): NSData? {
        try {
            return NSData.dataWithContentsOfURL(NSURL(string = uri))
        } catch(e: Exception) {
            println("Error loading data from URL")
            return null
        }
    }
}
