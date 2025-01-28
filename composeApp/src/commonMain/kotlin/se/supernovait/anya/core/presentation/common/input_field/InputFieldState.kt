package se.supernovait.anya.core.presentation.common.input_field

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

class InputFieldState(value: String, error: String?, isValid: Boolean) {
    var value by mutableStateOf(value)
        private set
    var error by mutableStateOf(error)
        private set
    var isValid by mutableStateOf(isValid)
        private set

    fun value(value: String) {
        this.value = value
    }

    fun error(error: String?) {
        this.error = error
    }

    fun setIsValid(isValid: Boolean) {
        this.isValid = isValid
    }
}

@Composable
fun rememberInputFieldState(value: String, error: String? = null, isValid: Boolean = true): InputFieldState {
    return rememberSaveable(saver = listSaver(
        save = {
            listOf(it.value, it.error, it.isValid.toString())
        },
        restore = {
            InputFieldState(it[0].orEmpty(), it[1], it[2].toBoolean())
        }
    )) {
        InputFieldState(value, error, isValid)
    }
}
