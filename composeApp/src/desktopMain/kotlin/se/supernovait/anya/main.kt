package se.supernovait.anya

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import se.supernovait.anya.di.initKoin

fun main() {
    initKoin()
    application {
        Window(onCloseRequest = ::exitApplication, title = "Anya") {
            App()
        }
    }
}
