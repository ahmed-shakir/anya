package se.supernovait.anya.app.presentation.medical_record.state

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate
import se.supernovait.anya.app.domain.model.MedicalRecordType

@Immutable
data class MedicalRecordState(
    val id: Long = 0,
    val catId: Long,
    val type: MedicalRecordType,
    val title: String,
    val description: String,
    val date: LocalDate?,
    val contagious: Boolean = false
) {
    companion object {
        val empty = MedicalRecordState(catId = 0, type = MedicalRecordType.OTHER, title = "", description = "", date = null)
    }
}

fun MedicalRecordState.isValid(): Boolean {
    return this.catId > 0 &&
            this.title.isNotBlank() &&
            this.description.isNotBlank() &&
            this.date != null
}
