package se.supernovait.anya.core.domain.file

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri
import se.supernovait.anya.core.domain.model.file.MimeType

actual class PdfViewer(private val context: Context) {

    actual fun openPDF(uri: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri.toUri(), MimeType.PDF)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "No app found to open the PDF.", Toast.LENGTH_LONG).show()
        }
    }
}
