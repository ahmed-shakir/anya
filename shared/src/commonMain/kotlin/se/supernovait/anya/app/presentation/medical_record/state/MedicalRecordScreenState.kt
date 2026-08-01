package se.supernovait.anya.app.presentation.medical_record.state

import se.supernovait.anya.app.domain.model.sort.MedicalRecordSortOption

data class MedicalRecordScreenState(
    val records: List<MedicalRecordState> = emptyList(),
    val selectedRecord: MedicalRecordState? = null,
    val recordToDelete: MedicalRecordState? = null,
    val currentCatId: Long? = null,
    val sortType: MedicalRecordSortOption = MedicalRecordSortOption.DEFAULT,
    val showForm: Boolean = false,
    val showSortMenu: Boolean = false
)
