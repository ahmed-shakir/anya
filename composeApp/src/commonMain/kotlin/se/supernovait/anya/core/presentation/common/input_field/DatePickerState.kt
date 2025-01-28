package se.supernovait.anya.core.presentation.common.input_field

import kotlinx.datetime.LocalDate

data class DatePickerState(
    var date: LocalDate? = null,
    var isPickerOpen: Boolean = false
)
