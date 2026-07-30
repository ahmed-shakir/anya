package se.supernovait.anya.core.presentation.common.menu

import androidx.compose.foundation.clickable
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.ic_keyboard_arrow_right
import anya.shared.generated.resources.ic_person
import anya.shared.generated.resources.user_profile_action_content_description
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.core.presentation.common.AnyaIcon
import se.supernovait.anya.core.presentation.common.preview.ComponentPreviewContainer
import se.supernovait.anya.core.presentation.util.action.AnyaActionDefaults
import se.supernovait.anya.core.presentation.util.action.AnyaActionStyle

@Composable
fun MainMenuItem(
    label: String,
    icon: DrawableResource,
    contentDescription: String? = null,
    style: AnyaActionStyle = AnyaActionDefaults.defaultStyle,
    tint: Color = ButtonDefaults.buttonColors().containerColor,
    background: Color = ButtonDefaults.buttonColors().contentColor,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(text = label, style = MaterialTheme.typography.bodyLarge, fontSize = style.fontSize) },
        leadingContent = { AnyaIcon(icon = icon, size = style.iconSize, tint = tint) },
        trailingContent = { AnyaIcon(icon = Res.drawable.ic_keyboard_arrow_right) },
        colors = ListItemDefaults.colors(background),
        modifier = Modifier
            .clickable(onClick = onClick)
            .semantics {
                this.contentDescription = contentDescription ?: label
                role = Role.Button
            }
    )
}

@PreviewLightDark
@Composable
private fun Preview() {
    ComponentPreviewContainer {
        MainMenuItem(
            label = "Item 1",
            icon = Res.drawable.ic_person,
            contentDescription = stringResource(Res.string.user_profile_action_content_description),
            onClick = { }
        )
    }
}
