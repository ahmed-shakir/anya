package se.supernovait.anya.core.domain.entity.validation

class ValidationResult(val error: String? = null) {
    val isValid: Boolean
        get() = error == null
}
