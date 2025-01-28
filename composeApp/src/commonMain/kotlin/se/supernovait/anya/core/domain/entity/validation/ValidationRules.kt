package se.supernovait.anya.core.domain.entity.validation

data class ValidationRules(
    val minLength: ValidationRule<Int>? = null,
    val maxLength: ValidationRule<Int>? = null,
    val regex: ValidationRule<ValidationPattern>? = null
) {
    companion object {
        val defaultRules = ValidationRules(
            regex = ValidationRule(
                value = ValidationPattern.CUSTOM,
                customValue = ".*",
                message = "Format is invalid."
            )
        )
    }
}
