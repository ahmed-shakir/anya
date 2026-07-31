package se.supernovait.anya.core.presentation.common.input_field

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.a11y_checkbox
import anya.shared.generated.resources.a11y_state_disabled
import anya.shared.generated.resources.a11y_switch
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.core.presentation.common.preview.ComponentPreviewContainer
import se.supernovait.anya.core.presentation.common.text.AnyaBoldLabel

@Composable
fun AnyaCheckbox(
    modifier: Modifier = Modifier,
    label: String? = null,
    contentDescription: String? = null,
    initialValue: Boolean = false,
    enabled: Boolean = true,
    onCheckedChange: (checked: Boolean) -> Unit
) {
    var checked by remember { mutableStateOf(initialValue) }
    val disabledColor = CheckboxDefaults.colors().disabledBorderColor
    val labelColor = if(enabled) Color.Unspecified else disabledColor
    val a11yCheckboxText = stringResource(Res.string.a11y_checkbox)
    val a11yStatusDisabled = stringResource(Res.string.a11y_state_disabled)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.semantics {
            this.contentDescription = buildString {
                if (!contentDescription.isNullOrBlank()) {
                    append(contentDescription)
                } else if (!label.isNullOrBlank()) {
                    append("$label $a11yCheckboxText")
                } else {
                    append(a11yCheckboxText)
                }
                if (!enabled) append(" $a11yStatusDisabled")
            }
            role = Role.Checkbox
        }
    ) {
        Checkbox(
            enabled = enabled,
            checked = checked,
            onCheckedChange = {
                checked = it
                onCheckedChange(it)
            }
        )
        label?.let {
            AnyaBoldLabel(
                text = it,
                color = labelColor,
                modifier = Modifier
                    .padding(start = MaterialTheme.spacing.small)
                    .clickable(enabled = enabled, onClick = { checked = !checked })
            )
        }
    }
}

@Composable
fun AnyaSwitch(
    modifier: Modifier = Modifier,
    label: String? = null,
    contentDescription: String? = null,
    initialValue: Boolean = false,
    enabled: Boolean = true,
    onCheckedChange: (checked: Boolean) -> Unit
) {
    var checked by remember { mutableStateOf(initialValue) }
    val a11ySwitchText = stringResource(Res.string.a11y_switch)
    val a11yStatusDisabled = stringResource(Res.string.a11y_state_disabled)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(horizontal = MaterialTheme.spacing.small)
            .then(modifier)
            .semantics {
                this.contentDescription = buildString {
                    if (!contentDescription.isNullOrBlank()) {
                        append(contentDescription)
                    } else if (!label.isNullOrBlank()) {
                        append("$label $a11ySwitchText")
                    } else {
                        append(a11ySwitchText)
                    }
                    if (!enabled) append(" $a11yStatusDisabled")
                }
                role = Role.Switch
            }
    ) {
        label?.let {
            AnyaBoldLabel(
                text = it,
                modifier = Modifier
                    .padding(end = MaterialTheme.spacing.medium)
                    .clickable(enabled = enabled, onClick = { checked = !checked })
            )
        }
        Switch(
            enabled = enabled,
            checked = checked,
            onCheckedChange = {
                checked = it
                onCheckedChange(it)
            }
        )
    }
}

@PreviewLightDark
@Composable
private fun AnyaCheckboxPreview() {
    ComponentPreviewContainer {
        AnyaCheckbox(contentDescription = "", onCheckedChange = { })
    }
}

@PreviewLightDark
@Composable
private fun AnyaSwitchPreview() {
    ComponentPreviewContainer {
        AnyaSwitch(contentDescription = "", onCheckedChange = { })
    }
}
