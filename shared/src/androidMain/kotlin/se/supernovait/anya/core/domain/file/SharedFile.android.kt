package se.supernovait.anya.core.domain.file

import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

actual class SharedFile(private val uri: Uri, private val contentResolver: ContentResolver) {

    actual fun toByteArray(): ByteArray? {
        return contentResolver.openInputStream(uri)?.readBytes()
    }

    actual fun toImageBitmap(): ImageBitmap? {
        val byteArray = toByteArray()
        return byteArray?.let {
            BitmapFactory.decodeByteArray(it, 0, it.size).asImageBitmap()
        }
    }

    actual fun toText(): String? {
        return contentResolver.openInputStream(uri)?.bufferedReader().use { it?.readText() }
    }

    actual fun fileName(): String? {
        var fileName: String? = null
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
        return fileName
    }

    actual fun uri(): String = uri.toString()
}
