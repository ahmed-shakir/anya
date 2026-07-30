package se.supernovait.anya.core.presentation.common.input_field

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.a11y_date_picker_dialog
import anya.shared.generated.resources.default_dialog_cancel_action_label
import anya.shared.generated.resources.default_dialog_ok_action_label
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource
import se.supernovait.anya.core.domain.util.toLocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnyaDatePickerDialog(
    datePickerState: DatePickerState = rememberDatePickerState(),
    onDateSelected: (LocalDate?) -> Unit,
    onDismissRequest: () -> Unit
) {
    val a11yDatePickerDialogText = stringResource(Res.string.a11y_date_picker_dialog)

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis?.toLocalDate())
                onDismissRequest()
            }) {
                Text(stringResource(Res.string.default_dialog_ok_action_label))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(Res.string.default_dialog_cancel_action_label))
            }
        },
        modifier = Modifier.semantics {
            this.contentDescription = a11yDatePickerDialogText
            role = Role.ValuePicker
        }
    ) {
        DatePicker(state = datePickerState)
    }
}
