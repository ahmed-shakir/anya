package se.supernovait.anya.app.presentation.app.topbar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.app_icon
import anya.shared.generated.resources.app_name
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.getString

/**
 * A simple holder to set the TopBar for current screen including actions specific for that screen
 */
class TopBarState {
    var icon by mutableStateOf<DrawableResource?>(Res.drawable.app_icon)
    var title by mutableStateOf<String?>(null)
    var canNavigateBack by mutableStateOf(true)
    var actions by mutableStateOf<List<TopBarAction>>(emptyList())

    fun icon(icon: DrawableResource) {
        this.icon = icon
    }

    fun title(title: String) {
        this.title = title
    }

    fun actions(actions: List<TopBarAction>, canNavigateBack: Boolean = true) {
        this.actions = actions
        this.canNavigateBack = canNavigateBack
    }

    fun clearActions() {
        canNavigateBack = true
        actions = emptyList()
    }

    fun clear() {
        icon = null
        title = null
        canNavigateBack = true
        actions = emptyList()
    }

    suspend fun reset() {
        icon = Res.drawable.app_icon
        title = getString(Res.string.app_name)
        canNavigateBack = true
        actions = emptyList()
    }
}

val LocalTopBarState = staticCompositionLocalOf<TopBarState> {
    error("No TopBarState provided")
}
