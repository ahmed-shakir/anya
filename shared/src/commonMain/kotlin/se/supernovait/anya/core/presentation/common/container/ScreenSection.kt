package se.supernovait.anya.core.presentation.common.container

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.a11y_action_edit_content_description
import anya.shared.generated.resources.ic_edit
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.core.presentation.common.action.AnyaIconButton
import se.supernovait.anya.core.presentation.common.preview.ComponentPreviewContainer
import se.supernovait.anya.core.presentation.common.text.AnyaBoldLabel
import se.supernovait.anya.core.presentation.common.text.AnyaHeading

@Composable
fun ScreenSection(
    modifier: Modifier = Modifier,
    title: String,
    onEdit: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = MaterialTheme.spacing.large)
            .then(modifier)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            AnyaHeading(text = title, style = MaterialTheme.typography.titleLarge)
            onEdit?.let {
                AnyaIconButton(
                    icon = Res.drawable.ic_edit,
                    contentDescription = stringResource(Res.string.a11y_action_edit_content_description, title),
                    onClick = onEdit
                )
            }
        }
        HorizontalDivider()
        Spacer(Modifier.height(MaterialTheme.spacing.small))
        content()
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    ComponentPreviewContainer {
        ScreenSection(title = "Screen section") {
            AnyaBoldLabel(text = "Screen section content")
        }
    }
}
