package se.supernovait.anya.app.presentation.owner.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.a11y_action_delete_content_description
import anya.shared.generated.resources.a11y_action_share_content_description
import anya.shared.generated.resources.ic_cat
import anya.shared.generated.resources.ic_delete
import anya.shared.generated.resources.ic_share
import anya.shared.generated.resources.screen_Owner_action_cats_content_description
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.app.presentation.owner.OwnerScreenEvent
import se.supernovait.anya.app.presentation.owner.state.OwnerState
import se.supernovait.anya.core.domain.model.action.ShortcutAction
import se.supernovait.anya.core.presentation.common.action.ShortcutButton
import se.supernovait.anya.core.presentation.common.preview.ComponentPreviewContainer
import se.supernovait.anya.core.presentation.common.preview.PreviewData
import se.supernovait.anya.core.presentation.util.action.AnyaActionDefaults

@Composable
fun OwnerProfileShortcuts(
    owner: OwnerState,
    onEvent: (OwnerScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val actions = listOf(
        ShortcutAction(
            icon = Res.drawable.ic_cat,
            contentDescription = stringResource(Res.string.screen_Owner_action_cats_content_description),
            enabled = owner.cats.isNotEmpty() && !owner.isPreview,
            onClick = { onEvent(OwnerScreenEvent.NavigateToCats(owner.id)) }
        ),
        ShortcutAction(
            icon = Res.drawable.ic_share,
            contentDescription = stringResource(Res.string.a11y_action_share_content_description, owner.name),
            enabled = !owner.isPreview,
            onClick = { onEvent(OwnerScreenEvent.ShareOwner(owner)) }
        ),
        ShortcutAction(
            icon = Res.drawable.ic_delete,
            contentDescription = stringResource(Res.string.a11y_action_delete_content_description),
            enabled = !owner.isPreview,
            onClick = { onEvent(OwnerScreenEvent.ConfirmDeleteOwner(owner)) }
        )
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium, Alignment.CenterHorizontally),
        modifier = modifier.fillMaxWidth()
    ) {
        actions.forEach { action ->
            ShortcutButton(
                icon = action.icon,
                contentDescription = action.contentDescription,
                style = AnyaActionDefaults.smallStyle,
                enabled = action.enabled,
                onClick = action.onClick
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    ComponentPreviewContainer {
        OwnerProfileShortcuts(owner = PreviewData.owner, onEvent = { })
    }
}

@PreviewLightDark
@Composable
private fun PreviewWithCat() {
    ComponentPreviewContainer {
        OwnerProfileShortcuts(owner = PreviewData.owner.copy(cats = listOf(PreviewData.cat)), onEvent = { })
    }
}
