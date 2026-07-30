package se.supernovait.anya.app.presentation.app.topbar

import org.jetbrains.compose.resources.DrawableResource

data class TopBarAction(
    val icon: DrawableResource,
    var label: String,
    var contentDescription: String,
    val onClick: () -> Unit
)
