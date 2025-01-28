package se.supernovait.anya.core.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.StringResource
import se.supernovait.anya.core.presentation.common.loading.AnyaEmbeddedLoadingIndicator
import se.supernovait.anya.core.presentation.common.text.AnyaBoldLabel

@Composable
fun AnyaButton(
    label: String,
    modifier: Modifier = Modifier,
    size: AnyaActionSize = AnyaActionSize.DEFAULT,
    enabled: Boolean = true,
    loading: Boolean = false,
    onClick: () -> Unit
) {
    Button(onClick = onClick, enabled = enabled) {
        if (loading) {
            AnyaEmbeddedLoadingIndicator()
        } else {
            AnyaActionText(
                text = label,
                size = size,
                modifier = modifier
            )
        }
    }
}

@Composable
fun AnyaOutlinedButton(
    label: String,
    modifier: Modifier = Modifier,
    size: AnyaActionSize = AnyaActionSize.DEFAULT,
    enabled: Boolean = true,
    loading: Boolean = false,
    onClick: () -> Unit
) {
    OutlinedButton(onClick = onClick, enabled = enabled) {
        if (loading) {
            AnyaEmbeddedLoadingIndicator()
        } else {
            AnyaActionText(
                text = label,
                size = size,
                modifier = modifier
            )
        }
    }
}

@Composable
fun AnyaTextButton(
    label: String,
    modifier: Modifier = Modifier,
    size: AnyaActionSize = AnyaActionSize.DEFAULT,
    enabled: Boolean = true,
    loading: Boolean = false,
    onClick: () -> Unit
) {
    TextButton(onClick = onClick, enabled = enabled) {
        if (loading) {
            AnyaEmbeddedLoadingIndicator()
        } else {
            AnyaActionText(
                text = label,
                size = size,
                modifier = modifier
            )
        }
    }
}

@Composable
fun AnyaTextAction(
    label: String,
    modifier: Modifier = Modifier,
    size: AnyaActionSize = AnyaActionSize.DEFAULT,
    enabled: Boolean = true,
    loading: Boolean = false,
    onClick: () -> Unit
) {
    val labelColor = ButtonDefaults.textButtonColors().contentColor
    val disabledLabelColor = ButtonDefaults.textButtonColors().disabledContentColor

    if (loading) {
        AnyaEmbeddedLoadingIndicator()
    } else {
        AnyaActionText(
            text = label,
            size = size,
            color = if (enabled) labelColor else disabledLabelColor,
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
                .clickable(enabled = enabled, onClick = { onClick() })
                .then(modifier)
        )
    }
}

@Composable
fun AnyaIconButton(
    icon: ImageVector,
    descriptionId: StringResource,
    modifier: Modifier = Modifier,
    size: AnyaActionSize = AnyaActionSize.DEFAULT,
    enabled: Boolean = true,
    loading: Boolean = false,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick, enabled = enabled) {
        if (loading) {
            AnyaEmbeddedLoadingIndicator()
        } else {
            val iconSize = when(size) {
                AnyaActionSize.DEFAULT -> 24.dp
                AnyaActionSize.LARGE -> 32.dp
                AnyaActionSize.SMALL -> 16.dp
            }

            AnyaIcon(icon = icon, descriptionId = descriptionId, size = iconSize, modifier = modifier)
        }
    }
}

@Composable
private fun AnyaActionText(
    text: String,
    modifier: Modifier = Modifier,
    size: AnyaActionSize = AnyaActionSize.DEFAULT,
    color: Color = MaterialTheme.colorScheme.onPrimary
) {
    val style = when(size) {
        AnyaActionSize.DEFAULT -> MaterialTheme.typography.headlineMedium
        AnyaActionSize.LARGE -> MaterialTheme.typography.headlineSmall
        AnyaActionSize.SMALL -> MaterialTheme.typography.bodyMedium
    }

    AnyaBoldLabel(
        text = text,
        style = style,
        color = color,
        modifier = modifier
    )
}

enum class AnyaActionSize {
    DEFAULT,
    LARGE,
    SMALL
}
