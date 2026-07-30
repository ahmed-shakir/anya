package se.supernovait.anya.core.domain.util

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask
import platform.Foundation.writeToFile
import platform.posix.memcpy

object FileUtils {
    fun normalize(filename: String): String = filename.replace(" ", "_").trim()

    @OptIn(ExperimentalForeignApi::class)
    fun saveFileFromUri(filePath: String, data: NSData?) {
        data?.let {
            it.writeToFile(filePath, atomically = true)
            val bytes = ByteArray(it.length.toInt())
            memcpy(bytes.refTo(0), it.bytes, it.length)
        }
    }

    fun getFileUri(filename: String): String {
        val path = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, true).first().toString()
        return "$path/$filename"
    }
}