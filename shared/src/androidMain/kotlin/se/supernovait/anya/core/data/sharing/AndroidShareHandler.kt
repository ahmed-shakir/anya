package se.supernovait.anya.core.data.sharing

import android.content.Intent
import se.supernovait.anya.core.domain.sharing.ShareHandler
import se.supernovait.anya.core.domain.sharing.ShareUrlBuilder
import se.supernovait.anya.core.domain.util.applicationContext

class AndroidShareHandler : ShareHandler {
    override fun shareData(type: String, data: String) {
        val shareText = ShareUrlBuilder.buildHttps(type, data)
        val intent = Intent(Intent.ACTION_SEND).apply {
            this.type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val chooser = Intent.createChooser(intent, "Share $type").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        applicationContext.startActivity(chooser)
    }
}
