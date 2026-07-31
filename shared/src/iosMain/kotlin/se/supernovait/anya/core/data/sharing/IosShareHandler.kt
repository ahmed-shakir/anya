package se.supernovait.anya.core.data.sharing

import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import se.supernovait.anya.core.domain.sharing.ShareHandler
import se.supernovait.anya.core.domain.sharing.ShareUrlBuilder

class IosShareHandler : ShareHandler {
    override fun shareData(type: String, data: String) {
        val shareText = ShareUrlBuilder.build(type, data)
        val activityViewController = UIActivityViewController(listOf(shareText), null)
        
        val window = UIApplication.sharedApplication.keyWindow
        val rootViewController = window?.rootViewController
        
        rootViewController?.presentViewController(activityViewController, animated = true, completion = null)
    }
}
