package se.supernovait.anya.app.presentation.medical_record

import se.supernovait.anya.app.domain.model.sort.MedicalRecordSortOption
import se.supernovait.anya.app.presentation.medical_record.state.MedicalRecordState
import se.supernovait.anya.core.domain.model.AnyaEvent

sealed interface MedicalRecordScreenEvent : AnyaEvent {
    data object LoadRecord: MedicalRecordScreenEvent
    data class NavigateToRecord(val id: Long): MedicalRecordScreenEvent
    data class NavigateToOwner(val id: Long): MedicalRecordScreenEvent
    data class SaveRecord(val medicalRecord: MedicalRecordState): MedicalRecordScreenEvent
    data class DeleteRecord(val medicalRecord: MedicalRecordState): MedicalRecordScreenEvent
    data class ConfirmDeleteRecord(val medicalRecord: MedicalRecordState): MedicalRecordScreenEvent
    data object DismissDeleteConfirmation: MedicalRecordScreenEvent
    data class FilterRecords(val searchQuery: String): MedicalRecordScreenEvent
    data class SortRecords(val sortOption: MedicalRecordSortOption): MedicalRecordScreenEvent
    data object ShowSortMenu: MedicalRecordScreenEvent
    data object HideSortMenu: MedicalRecordScreenEvent
    data class ShowForm(val medicalRecord: MedicalRecordState? = null): MedicalRecordScreenEvent
    data object HideForm: MedicalRecordScreenEvent
}
