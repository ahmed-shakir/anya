package se.supernovait.anya.app.presentation.app.topbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.app_logo_description
import anya.shared.generated.resources.ic_arrow_back
import anya.shared.generated.resources.ic_more_vert
import anya.shared.generated.resources.topbar_action_back_content_description
import anya.shared.generated.resources.topbar_action_menu_content_description
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.core.presentation.common.AnyaIcon
import se.supernovait.anya.core.presentation.common.action.AnyaIconButton
import se.supernovait.anya.core.presentation.common.preview.ComponentPreviewContainer
import se.supernovait.anya.core.presentation.common.text.AnyaHeading
import se.supernovait.anya.core.presentation.common.text.AnyaLabel

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(state: TopBarState = TopBarState(), navigateUp: () -> Unit = { }, modifier: Modifier = Modifier) {
    TopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                state.icon?.let {
                    AnyaIcon(
                        icon = it,
                        contentDescription = Res.string.app_logo_description,
                        modifier = Modifier.size(MaterialTheme.spacing.iconLarge)
                    )
                }
                state.title?.let {
                    AnyaHeading(text = it, style = MaterialTheme.typography.headlineSmall)
                }
            }
        },
        navigationIcon = {
            if (state.canNavigateBack) {
                AnyaIconButton(
                    icon = Res.drawable.ic_arrow_back,
                    contentDescription = stringResource(Res.string.topbar_action_back_content_description),
                    onClick = navigateUp
                )
            }
        },
        actions = {
            when (state.actions.size) {
                0 -> {} // No actions
                1 -> {
                    // Single action - display directly
                    val action = state.actions.first()
                    AnyaIconButton(
                        icon = action.icon,
                        contentDescription = action.contentDescription,
                        onClick = action.onClick
                    )
                }
                else -> {
                    // Multiple actions - use dropdown menu
                    var menuExpanded by remember { mutableStateOf(false) }

                    AnyaIconButton(
                        icon = Res.drawable.ic_more_vert,
                        contentDescription = stringResource(Res.string.topbar_action_menu_content_description),
                        onClick = { menuExpanded = true }
                    )

                    DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                        state.actions.forEach { action ->
                            DropdownMenuItem(
                                text = { AnyaLabel(action.label) },
                                leadingIcon = {
                                    AnyaIcon(
                                        icon = action.icon,
                                        size = MaterialTheme.spacing.iconSmall
                                    )
                                },
                                onClick = {
                                    action.onClick()
                                    menuExpanded = false
                                },
                                modifier = Modifier.semantics {
                                    this.contentDescription = action.contentDescription
                                }
                            )
                        }
                    }
                }
            }
        },
        modifier = modifier
    )
}

@PreviewLightDark
@Composable
private fun Preview() {
    ComponentPreviewContainer {
        AppTopBar(state = TopBarState())
    }
}
