package se.supernovait.anya.app.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Scaffold allows you to implement a UI with the basic Material Design layout structure.
 * Scaffold provides slots for the most common top-level Material components, such as TopAppBar, BottomAppBar, FloatingActionButton, and Drawer.
 */
@Composable
fun AnyaApp() {

    Scaffold(topBar = { TopBar(canNavigateBack = false) }) {
        innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text("Welcome to Anya")
        }
    }
}
