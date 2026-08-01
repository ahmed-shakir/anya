package se.supernovait.anya.app.domain.mapper

import se.supernovait.anya.app.data.local.entity.MedicalRecord
import se.supernovait.anya.app.presentation.medical_record.state.MedicalRecordState
import se.supernovait.anya.core.domain.util.isoString
import se.supernovait.anya.core.domain.util.toLocalDate

fun MedicalRecord.mapToState(): MedicalRecordState {
    return MedicalRecordState(
        id = id,
        catId = catId,
        type = type,
        title = title,
        description = description,
        date = date.toLocalDate(),
        contagious = contagious
    )
}

fun MedicalRecordState.mapToEntity(): MedicalRecord {
    return MedicalRecord(
        id = id,
        catId = catId,
        type = type,
        title = title,
        description = description,
        date = date.isoString(),
        contagious = contagious
    )
}
