package se.supernovait.anya.core.domain.entity.validation

data class ValidationRule<T>(
    val value: T,
    val message: String,
    val customValue: String? = null
)
