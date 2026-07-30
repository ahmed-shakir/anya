package se.supernovait.anya.core.presentation.common.action

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

/**
 * A simple holder for whatever the current screen wants the FAB to do
 */
class FabState {
    var icon by mutableStateOf<DrawableResource?>(null)
    var contentDescription by mutableStateOf<StringResource?>(null)
    var onClick by mutableStateOf<(() -> Unit)?>(null)

    fun set(icon: DrawableResource, contentDescription: StringResource, onClick: () -> Unit) {
        this.icon = icon
        this.contentDescription = contentDescription
        this.onClick = onClick
    }

    fun clear() {
        icon = null
        contentDescription = null
        onClick = null
    }
}

val LocalFabState = staticCompositionLocalOf<FabState> {
    error("No FabState provided")
}
