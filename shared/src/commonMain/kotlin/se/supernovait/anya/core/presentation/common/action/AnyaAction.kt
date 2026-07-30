package se.supernovait.anya.core.presentation.common.action

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.a11y_state_disabled
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.core.presentation.common.AnyaIcon
import se.supernovait.anya.core.presentation.common.loading.EmbeddedLoadingIndicator
import se.supernovait.anya.core.presentation.util.action.AnyaActionDefaults

@Composable
fun AnyaButton(
    label: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    shape: Shape = ButtonDefaults.shape,
    enabled: Boolean = true,
    loading: Boolean = false,
    onClick: () -> Unit
) {
    val a11yStatusDisabled = stringResource(Res.string.a11y_state_disabled)
    val buttonModifier = Modifier
        .padding(horizontal = MaterialTheme.spacing.extraSmall)
        .then(modifier)
        .semantics {
            this.contentDescription = buildString {
                if (contentDescription.isNullOrBlank()) append(label) else append(contentDescription)
                if (!enabled) append(" $a11yStatusDisabled")
            }
            role = Role.Button
        }

    Button(onClick = onClick, enabled = enabled, shape = shape, modifier = buttonModifier) {
        if (loading) {
            EmbeddedLoadingIndicator()
        } else {
            Text(text = label, style = textStyle)
        }
    }
}

@Composable
fun AnyaOutlinedButton(
    label: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    enabled: Boolean = true,
    loading: Boolean = false,
    onClick: () -> Unit
) {
    val a11yStatusDisabled = stringResource(Res.string.a11y_state_disabled)
    val buttonModifier = Modifier
        .padding(horizontal = MaterialTheme.spacing.extraSmall)
        .then(modifier)
        .semantics {
            this.contentDescription = buildString {
                if (contentDescription.isNullOrBlank()) append(label) else append(contentDescription)
                if (!enabled) append(" $a11yStatusDisabled")
            }
            role = Role.Button
        }

    OutlinedButton(onClick = onClick, enabled = enabled, modifier = buttonModifier) {
        if (loading) {
            EmbeddedLoadingIndicator()
        } else {
            Text(text = label, style = textStyle)
        }
    }
}

@Composable
fun AnyaTextButton(
    label: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    enabled: Boolean = true,
    loading: Boolean = false,
    onClick: () -> Unit
) {
    val a11yStatusDisabled = stringResource(Res.string.a11y_state_disabled)
    val buttonModifier = modifier.semantics {
        this.contentDescription = buildString {
            if (contentDescription.isNullOrBlank()) append(label) else append(contentDescription)
            if (!enabled) append(" $a11yStatusDisabled")
        }
        role = Role.Button
    }

    TextButton(onClick = onClick, enabled = enabled, modifier = buttonModifier) {
        if (loading) {
            EmbeddedLoadingIndicator()
        } else {
            Text(text = label, style = textStyle)
        }
    }
}

@Composable
fun AnyaTextAction(
    label: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    icon: DrawableResource? = null,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    color: Color = ButtonDefaults.textButtonColors().contentColor,
    enabled: Boolean = true,
    loading: Boolean = false,
    onClick: () -> Unit
) {
    val disabledLabelColor = ButtonDefaults.textButtonColors().disabledContentColor
    val a11yStatusDisabled = stringResource(Res.string.a11y_state_disabled)
    val actionModifier = Modifier
        .clickable(enabled = enabled, onClick = { onClick() })
        .then(modifier)
        .semantics {
            this.contentDescription = buildString {
                if (contentDescription.isNullOrBlank()) append(label) else append(contentDescription)
                if (!enabled) append(" $a11yStatusDisabled")
            }
            role = Role.Button
        }

    if (loading) {
        EmbeddedLoadingIndicator()
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small, Alignment.CenterHorizontally),
            modifier = actionModifier
        ) {
            icon?.let {
                AnyaIcon(
                    icon = it,
                    size = textStyle.fontSize.value.dp,
                    tint = if (enabled) color else disabledLabelColor
                )
            }
            Text(
                text = label,
                style = textStyle,
                color = if (enabled) color else disabledLabelColor
            )
        }
    }
}

@Composable
fun AnyaIconButton(
    icon: DrawableResource,
    contentDescription: String,
    modifier: Modifier = Modifier,
    iconSize: Dp = AnyaActionDefaults.defaultStyle.iconSize,
    tint: Color = LocalContentColor.current,
    background: Color = Color.Unspecified,
    enabled: Boolean = true,
    loading: Boolean = false,
    onClick: () -> Unit
) {
    val a11yStatusDisabled = stringResource(Res.string.a11y_state_disabled)
    val buttonModifier = Modifier
        .background(background)
        .then(modifier)
        .semantics {
            this.contentDescription = buildString {
                append(contentDescription)
                if (!enabled) append(" $a11yStatusDisabled")
            }
            role = Role.Button
        }

    IconButton(onClick = onClick, enabled = enabled, modifier = buttonModifier) {
        if (loading) {
            EmbeddedLoadingIndicator()
        } else {
            AnyaIcon(icon = icon, size = iconSize, tint = tint)
        }
    }
}
