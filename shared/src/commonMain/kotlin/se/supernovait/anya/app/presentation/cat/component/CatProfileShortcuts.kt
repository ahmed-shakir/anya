package se.supernovait.anya.app.presentation.cat.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.a11y_action_delete_content_description
import anya.shared.generated.resources.a11y_action_share_content_description
import anya.shared.generated.resources.ic_delete
import anya.shared.generated.resources.ic_file_pdf
import anya.shared.generated.resources.ic_folder_plus
import anya.shared.generated.resources.ic_person
import anya.shared.generated.resources.ic_share
import anya.shared.generated.resources.screen_Cat_action_medical_record_content_description
import anya.shared.generated.resources.screen_Cat_action_owner_content_description
import anya.shared.generated.resources.screen_Cat_action_pedigree_content_description
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.app.presentation.cat.CatScreenEvent
import se.supernovait.anya.app.presentation.cat.state.CatState
import se.supernovait.anya.core.domain.model.action.ShortcutAction
import se.supernovait.anya.core.presentation.common.action.ShortcutButton
import se.supernovait.anya.core.presentation.common.preview.ComponentPreviewContainer
import se.supernovait.anya.core.presentation.common.preview.PreviewData
import se.supernovait.anya.core.presentation.util.action.AnyaActionDefaults

@Composable
fun CatProfileShortcuts(
    cat: CatState,
    onEvent: (CatScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val actions = listOf(
        ShortcutAction(
            icon = Res.drawable.ic_person,
            contentDescription = stringResource(Res.string.screen_Cat_action_owner_content_description),
            enabled = cat.ownerId != null,
            onClick = { onEvent(CatScreenEvent.NavigateToOwner(cat.ownerId ?: 0)) }
        ),
        ShortcutAction(
            icon = Res.drawable.ic_folder_plus,
            contentDescription = stringResource(Res.string.screen_Cat_action_medical_record_content_description),
            onClick = { onEvent(CatScreenEvent.NavigateToMedicalRecord(cat.id)) }
        ),
        ShortcutAction(
            icon = Res.drawable.ic_share,
            contentDescription = stringResource(Res.string.a11y_action_share_content_description, cat.name),
            onClick = { onEvent(CatScreenEvent.ShareCat(cat)) }
        ),
        ShortcutAction(
            icon = Res.drawable.ic_delete,
            contentDescription = stringResource(Res.string.a11y_action_delete_content_description, cat.name),
            onClick = { onEvent(CatScreenEvent.ConfirmDeleteCat(cat)) }
        ),
        ShortcutAction(
            icon = Res.drawable.ic_file_pdf,
            contentDescription = stringResource(Res.string.screen_Cat_action_pedigree_content_description),
            onClick = { onEvent(CatScreenEvent.ShowPedigree) }
        )
    )

    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium, Alignment.CenterHorizontally),
        contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium),
        modifier = modifier.fillMaxWidth()
    ) {
        items(actions) { action ->
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
        CatProfileShortcuts(cat = PreviewData.cat, onEvent = { })
    }
}

@PreviewLightDark
@Composable
private fun PreviewWithOwner() {
    ComponentPreviewContainer {
        CatProfileShortcuts(cat = PreviewData.cat.copy(ownerId = 1), onEvent = { })
    }
}
