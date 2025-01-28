package se.supernovait.anya.core.domain.entity.validation

enum class ValidationPattern(val pattern: String = "") {
    CUSTOM,
    EMAIL("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{2,})$"),
    ISO_DATE("^(19|20)\\d{2}-(0[1-9]|1[1,2])-(0[1-9]|[12][0-9]|3[01])$"),
    PASSWORD("^([a-zA-Z0-9\\+\\*\\-\\?\\!]+)$"),
    PERSON_ID("^(\\d{8}-\\d{4})$"),
    PHONE("^(\\+[0-9]{1,3}[\\- ]{0,1}[0-9]{1,3})?([0-9]{1,3}[\\-]{0,1})?([0-9]{5,7})$")
}
