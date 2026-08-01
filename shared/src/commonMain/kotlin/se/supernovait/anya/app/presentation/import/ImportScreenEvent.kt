package se.supernovait.anya.app.presentation.import

sealed interface ImportScreenEvent {
    data object Import: ImportScreenEvent
    data object Cancel: ImportScreenEvent
    data object ViewDetails: ImportScreenEvent
}
