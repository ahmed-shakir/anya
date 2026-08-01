package se.supernovait.anya.app.domain.model

enum class MedicalRecordType {
    DISEASE,
    INJURY,
    OTHER,
    VACCINATION;

    companion object {
        fun ordinalOf(ordinal: Int): MedicalRecordType {
            return entries.first { it.ordinal == ordinal }
        }
    }
}