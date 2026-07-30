package se.supernovait.anya.core.presentation.common.action

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.a11y_state_disabled
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.core.presentation.common.AnyaIcon
import se.supernovait.anya.core.presentation.util.action.AnyaActionDefaults
import se.supernovait.anya.core.presentation.util.action.AnyaActionStyle

@Composable
fun ShortcutButton(
    icon: DrawableResource,
    contentDescription: String,
    style: AnyaActionStyle = AnyaActionDefaults.defaultStyle,
    tint: Color = ButtonDefaults.buttonColors().contentColor,
    background: Color = ButtonDefaults.buttonColors().containerColor,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val backgroundColor = if(enabled) background else ButtonDefaults.buttonColors().disabledContainerColor
    val iconTint = if(enabled) tint else ButtonDefaults.buttonColors().disabledContentColor
    val a11yStatusDisabled = stringResource(Res.string.a11y_state_disabled)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(shape = RoundedCornerShape(15))
            .background(backgroundColor)
            .padding(vertical = MaterialTheme.spacing.small, horizontal = MaterialTheme.spacing.large)
            .clickable(enabled = enabled, onClick = onClick)
            .semantics {
                this.contentDescription = buildString {
                    append(contentDescription)
                    if (!enabled) append(" $a11yStatusDisabled")
                }
            }
    ) {
        AnyaIcon(icon = icon, size = style.iconSize, tint = iconTint)
    }
}
