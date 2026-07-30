package se.supernovait.anya.core.presentation.common.modal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.a11y_button
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.core.presentation.common.text.AnyaBoldLabel

@Composable
fun AnyaDialog(
    contentDescription: String,
    onDismissRequest: (() -> Unit),
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
            .semantics { this.contentDescription = contentDescription }
    ) {
        Dialog(
            onDismissRequest = { onDismissRequest() },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            content()
        }
    }
}

@Composable
fun AnyaDialogButton(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit
) {
    val a11yButtonText = stringResource(Res.string.a11y_button)

    Button(
        onClick = onClick,
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = AlertDialogDefaults.containerColor,
            contentColor = AlertDialogDefaults.textContentColor
        ),
        modifier = modifier
            .padding(top = MaterialTheme.spacing.divider)
            .semantics { this.contentDescription = "$label $a11yButtonText" }
    ) {
        AnyaBoldLabel(text = label, modifier = Modifier.padding(vertical = MaterialTheme.spacing.small))
    }
}
