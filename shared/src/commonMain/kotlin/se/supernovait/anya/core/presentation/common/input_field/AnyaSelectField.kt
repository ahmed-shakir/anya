package se.supernovait.anya.core.presentation.common.input_field

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.a11y_select_field
import anya.shared.generated.resources.a11y_select_field_menu
import anya.shared.generated.resources.a11y_state_collapsed
import anya.shared.generated.resources.a11y_state_disabled
import anya.shared.generated.resources.a11y_state_expanded
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.core.domain.model.SelectFieldOption
import se.supernovait.anya.core.presentation.common.preview.ComponentPreviewContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnyaSelectField(
    modifier: Modifier = Modifier,
    label: String = "",
    placeholder: String = "",
    initialValue: String = "",
    contentDescription: String? = null,
    options: List<SelectFieldOption>,
    onValueChange: (option: SelectFieldOption) -> Unit,
    fullWidth: Boolean = true,
    enabled: Boolean = true
) {
    val initialSelectedOption = options.first { option -> option.value.equals(initialValue, true) }.value
    var selectedOption by rememberSaveable {
        mutableStateOf(initialSelectedOption)
    }
    val (allowExpanded, setExpanded) = remember { mutableStateOf(false) }
    val expanded = allowExpanded && options.isNotEmpty()

    val a11ySelectFieldText = stringResource(Res.string.a11y_select_field, initialValue)
    val a11ySelectFieldMenuText = stringResource(Res.string.a11y_select_field_menu)
    val a11yStateDisabled = stringResource(Res.string.a11y_state_disabled)
    val a11yStateExpanded = stringResource(Res.string.a11y_state_expanded)
    val a11yStateCollapsed = stringResource(Res.string.a11y_state_collapsed)
    val fullWidthModifier = Modifier.fillMaxWidth().takeIf { fullWidth } ?: Modifier
    val fieldModifier = fullWidthModifier
        .padding(horizontal = MaterialTheme.spacing.extraSmall)
        .semantics {
            this.contentDescription = buildString {
                if (!contentDescription.isNullOrBlank()) {
                    append(contentDescription)
                } else if (label.isNotBlank()) {
                    append("$label $a11ySelectFieldText")
                } else if (placeholder.isNotBlank()) {
                    append(placeholder)
                } else {
                    append(a11ySelectFieldText)
                }
                if (!enabled) append(" $a11yStateDisabled")
            }
            role = Role.DropdownList
            stateDescription = if (expanded) a11yStateExpanded else a11yStateCollapsed
        }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = setExpanded
    ) {
        Column {
            OutlinedTextField(
                value = selectedOption,
                onValueChange = { },
                singleLine = true,
                readOnly = true,
                label = { Text(text = label) },
                placeholder = { Text(text = placeholder) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = fieldModifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
            )
        }

        ExposedDropdownMenu(
            expanded = enabled && expanded,
            onDismissRequest = { setExpanded(false) },
            modifier = fullWidthModifier
                .padding(horizontal = MaterialTheme.spacing.extraSmall)
                .heightIn(max = 280.dp).then(modifier)
                .semantics { this.contentDescription = a11ySelectFieldMenuText }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option.value, style = MaterialTheme.typography.bodyLarge) },
                    onClick = {
                        selectedOption = option.value
                        onValueChange(option)
                        setExpanded(false)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun DropdownExamplePreview() {
    ComponentPreviewContainer {
        AnyaSelectField(
            options = listOf(
                SelectFieldOption(1, "Option 1"),
                SelectFieldOption(2, "Option 2"),
                SelectFieldOption(3, "Option 3"),
                SelectFieldOption(4, "Option 4"),
                SelectFieldOption(5, "Option 5")
            ),
            onValueChange = { }
        )
    }
}
