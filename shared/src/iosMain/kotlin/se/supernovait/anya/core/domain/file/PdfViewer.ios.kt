package se.supernovait.anya.core.domain.file

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIDocumentInteractionController

actual class PdfViewer {

    @OptIn(ExperimentalForeignApi::class)
    actual fun openPDF(uri: String) {
        val fileURL = NSURL.fileURLWithPath(uri)
        val viewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        val view = viewController?.view

        if (view != null) {
            UIDocumentInteractionController.interactionControllerWithURL(fileURL).apply {
                delegate = null // You can assign your own delegate if needed
                presentOpenInMenuFromRect(CGRectZero.readValue(), view, true)
            }
        } else {
            println("No view available to present the PDF.")
        }
    }
}
