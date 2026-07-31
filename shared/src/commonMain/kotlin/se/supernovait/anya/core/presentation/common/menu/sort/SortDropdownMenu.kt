package se.supernovait.anya.core.presentation.common.menu.sort

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.a11y_action_sort_content_description
import anya.shared.generated.resources.ic_check
import anya.shared.generated.resources.ic_sort
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.domain.model.sort.OwnerSortOption
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.core.presentation.common.AnyaIcon
import se.supernovait.anya.core.presentation.common.action.AnyaIconButton
import se.supernovait.anya.core.presentation.common.preview.ComponentPreviewContainer

@Composable
fun <T> SortDropdownMenu(
    allOptions: List<T>,
    selectedSortOption: T,
    onSortOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = MaterialTheme.spacing.iconMedium
) where T : Enum<T>, T : SortOption {
    var expanded by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.TopEnd,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        AnyaIconButton(
            icon = Res.drawable.ic_sort,
            contentDescription = stringResource(Res.string.a11y_action_sort_content_description),
            iconSize = iconSize,
            onClick = { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            shape = MaterialTheme.shapes.large,
            onDismissRequest = { expanded = false }
        ) {
            allOptions.forEachIndexed { index, sortOption ->
                DropdownMenuItem(
                    text = { Text(stringResource(sortOption.label)) },
                    trailingIcon = {
                        if (sortOption == selectedSortOption) {
                            AnyaIcon(Res.drawable.ic_check)
                        }
                    },
                    onClick = {
                        onSortOptionSelected(sortOption)
                        expanded = false
                    }
                )
                if (index < allOptions.lastIndex) {
                    HorizontalDivider()
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    ComponentPreviewContainer {
        SortDropdownMenu(
            allOptions = OwnerSortOption.entries,
            selectedSortOption = OwnerSortOption.DEFAULT,
            onSortOptionSelected = { }
        )
    }
}
