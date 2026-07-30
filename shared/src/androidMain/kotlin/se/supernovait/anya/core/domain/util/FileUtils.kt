package se.supernovait.anya.core.domain.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import kotlinx.io.IOException
import kotlinx.io.files.FileNotFoundException
import java.io.File
import java.io.FileOutputStream
import java.util.Objects

object FileUtils {
    fun normalize(filename: String): String = filename.replace(" ", "_").trim()

    fun saveFileFromUri(context: Context, sourceUri: Uri, destinationUri: Uri) {
        try {
            val contentResolver = context.contentResolver
            val bytes = contentResolver.openInputStream(sourceUri)?.use { stream ->
                val bytesArray = stream.readBytes()
                stream.close()
                bytesArray
            }
            contentResolver.openFileDescriptor(destinationUri, "w")?.use { descriptor ->
                FileOutputStream(descriptor.fileDescriptor).use { stream ->
                    stream.write(bytes)
                    stream.close()
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getImageUri(context: Context, filename: String): Uri {
        val directory = File(context.filesDir, "images")
        directory.mkdirs()
        val file = File(directory, filename)
        return FileProvider.getUriForFile(Objects.requireNonNull(context), authority(context), file)
    }

    fun getDocumentUri(context: Context, filename: String): Uri {
        val directory = File(context.filesDir, "docs")
        directory.mkdirs()
        val file = File(directory, filename)
        return FileProvider.getUriForFile(Objects.requireNonNull(context), authority(context), file)
    }

    fun getFileUri(context: Context, filename: String): Uri {
        val file = File(context.filesDir, filename)
        return FileProvider.getUriForFile(Objects.requireNonNull(context), authority(context), file)
    }

    private fun authority(context: Context) = "${context.applicationContext.packageName}.provider"
}