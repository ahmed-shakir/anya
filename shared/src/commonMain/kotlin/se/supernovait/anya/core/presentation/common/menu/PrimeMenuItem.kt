package se.supernovait.anya.core.presentation.common.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.ic_person
import anya.shared.generated.resources.user_profile_action_content_description
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.core.presentation.common.AnyaIcon
import se.supernovait.anya.core.presentation.common.preview.ComponentPreviewContainer
import se.supernovait.anya.core.presentation.common.text.AnyaBoldLabel
import se.supernovait.anya.core.presentation.util.action.AnyaActionDefaults
import se.supernovait.anya.core.presentation.util.action.AnyaActionStyle

@Composable
fun PrimeMenuItem(
    label: String,
    icon: DrawableResource,
    contentDescription: String? = null,
    style: AnyaActionStyle = AnyaActionDefaults.defaultStyle,
    tint: Color = ButtonDefaults.buttonColors().containerColor,
    background: Color = ButtonDefaults.buttonColors().contentColor,
    onClick: () -> Unit
) {
    val iconStyle = AnyaActionDefaults.largeStyle

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .clip(shape = RoundedCornerShape(8))
            .background(background)
            .clickable(onClick = onClick)
            .padding(vertical = MaterialTheme.spacing.large, horizontal = MaterialTheme.spacing.medium)
            .semantics {
                this.contentDescription = contentDescription ?: label
                role = Role.Button
            }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)) {
            AnyaIcon(icon = icon, size = iconStyle.iconSize, tint = tint)
            AnyaBoldLabel(text = label, fontSize = style.fontSize)
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    ComponentPreviewContainer {
        PrimeMenuItem(
            label = "Item 1",
            icon = Res.drawable.ic_person,
            contentDescription = stringResource(Res.string.user_profile_action_content_description),
            onClick = { }
        )
    }
}
