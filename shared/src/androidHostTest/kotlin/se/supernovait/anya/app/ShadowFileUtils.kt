package se.supernovait.anya.app

import android.content.Context
import android.net.Uri
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements
import se.supernovait.anya.core.domain.util.FileUtils

@Implements(FileUtils::class)
class ShadowFileUtils {
    companion object {
        @Implementation
        @JvmStatic
        fun getImageUri(context: Context, filename: String): Uri {
            return Uri.parse("content://se.supernovait.anya.fileprovider/images/$filename")
        }
    }
}
