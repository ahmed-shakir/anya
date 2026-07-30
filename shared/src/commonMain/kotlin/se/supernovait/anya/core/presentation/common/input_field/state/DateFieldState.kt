package se.supernovait.anya.core.presentation.common.input_field.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.datetime.LocalDate

class DateFieldState(date: LocalDate?, isPickerOpen: Boolean) {
    var date by mutableStateOf(date)
        private set
    var isPickerOpen by mutableStateOf(isPickerOpen)
        private set

    fun date(date: LocalDate?) {
        this.date = date
    }

    fun setIsPickerOpen(isPickerOpen: Boolean) {
        this.isPickerOpen = isPickerOpen
    }
}

@Composable
fun rememberDateFieldState(date: LocalDate? = null, isPickerOpen: Boolean = false): DateFieldState {
    return rememberSaveable(saver = listSaver(
        save = {
            listOf(it.date?.toString(), it.isPickerOpen.toString())
        },
        restore = { state ->
            DateFieldState(state[0]?.let { LocalDate.parse(it) }, state[1].toBoolean())
        }
    )) {
        DateFieldState(date, isPickerOpen)
    }
}
