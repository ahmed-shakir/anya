package se.supernovait.anya.core.domain.model.action

import org.jetbrains.compose.resources.DrawableResource

data class ShortcutAction(
    val icon: DrawableResource,
    val contentDescription: String,
    val enabled: Boolean = true,
    val onClick: () -> Unit
)
