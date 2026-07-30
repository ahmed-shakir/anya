package se.supernovait.anya.core.presentation.common.modal

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnyaBottomSheet(
    contentDescription: String,
    onDismissRequest: (() -> Unit),
    content: @Composable ColumnScope.() -> Unit
) {
    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        onDismissRequest = { onDismissRequest() },
        tonalElevation = 0.dp,
        modifier = Modifier.semantics { this.contentDescription = contentDescription }
    ) {
        content()
    }
}
