package se.supernovait.anya.core.presentation.common.error

import se.supernovait.anya.core.domain.model.RecoveryOption

data class ErrorScreenState(
    val title: String,
    val description: String,
    val primaryActionLabel: String,
    val secondaryActionLabel: String? = null,
    val secondaryAction: RecoveryOption? = null
)
