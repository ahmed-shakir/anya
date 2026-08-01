package se.supernovait.anya.app.presentation.medical_record

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.supernovait.anya.app.data.local.entity.filterBySearchQuery
import se.supernovait.anya.app.domain.mapper.mapToEntity
import se.supernovait.anya.app.domain.mapper.mapToState
import se.supernovait.anya.app.domain.model.sort.MedicalRecordSortOption
import se.supernovait.anya.app.domain.repository.CatRepository
import se.supernovait.anya.app.presentation.app.AppEvent
import se.supernovait.anya.app.presentation.medical_record.state.MedicalRecordScreenState
import se.supernovait.anya.app.presentation.medical_record.state.MedicalRecordState
import se.supernovait.anya.app.presentation.medical_record.state.isValid
import se.supernovait.anya.app.presentation.navigation.Route
import se.supernovait.anya.core.domain.model.error.NetworkError

@OptIn(ExperimentalCoroutinesApi::class)
class MedicalRecordViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val catRepository: CatRepository
) : ViewModel() {
    private val _sortOption = MutableStateFlow(MedicalRecordSortOption.DEFAULT)
    private val _searchQuery = MutableStateFlow("")
    private val _catId = savedStateHandle.toRoute<Route.MedicalRecord>().catId
    private val _records = _sortOption
        .flatMapLatest { sortOption ->
            when(sortOption) {
                MedicalRecordSortOption.DEFAULT -> catRepository.getAllMedicalRecordsByCatId(_catId)
                MedicalRecordSortOption.TITLE -> catRepository.getAllMedicalRecordsByCatIdOrderedByTitle(_catId)
                MedicalRecordSortOption.DATE -> catRepository.getAllMedicalRecordsByCatIdOrderedByDate(_catId)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _uiState = MutableStateFlow(MedicalRecordScreenState())
    val uiState = combine(_uiState, _sortOption, _searchQuery, _records) { state, sortOption, searchQuery, records ->
        state.copy(
            records = records
                .filter { it.filterBySearchQuery(searchQuery) }
                .map { it.mapToState() },
            selectedSortOption = sortOption,
            currentCatId = _catId
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MedicalRecordScreenState())

    private val _events = Channel<AppEvent>()
    val events = _events.receiveAsFlow()

    fun onEvent(event: MedicalRecordScreenEvent) {
        when(event) {
            is MedicalRecordScreenEvent.NavigateToRecord,
            is MedicalRecordScreenEvent.NavigateToOwner -> { /* Handled in AnyaApp */ }
            is MedicalRecordScreenEvent.LoadRecord -> {
                val args = savedStateHandle.toRoute<Route.MedicalRecordEntry>()
                getMedicalRecordById(args.id)
            }
            is MedicalRecordScreenEvent.SaveRecord -> saveMedicalRecord(event.medicalRecord)
            is MedicalRecordScreenEvent.DeleteRecord -> deleteMedicalRecord(event.medicalRecord)
            is MedicalRecordScreenEvent.ConfirmDeleteRecord -> toggleDeleteConfirmation(event.medicalRecord)
            is MedicalRecordScreenEvent.DismissDeleteConfirmation -> toggleDeleteConfirmation(null)
            is MedicalRecordScreenEvent.FilterRecords -> _searchQuery.value = event.searchQuery
            is MedicalRecordScreenEvent.SortRecords -> _sortOption.value = event.sortOption
            is MedicalRecordScreenEvent.ShowSortMenu -> toggleSortMenu(true)
            is MedicalRecordScreenEvent.HideSortMenu -> toggleSortMenu(false)
            is MedicalRecordScreenEvent.ShowForm -> toggleMedicalRecordForm(event.medicalRecord, true)
            is MedicalRecordScreenEvent.HideForm -> toggleMedicalRecordForm(showForm = false)
        }
    }

    private fun getMedicalRecordById(id: Long) {
        viewModelScope.launch {
            val record = catRepository.getMedicalRecordById(id)
            _uiState.update { currentState -> currentState.copy(selectedRecord = record?.mapToState()) }
        }
    }

    private fun saveMedicalRecord(record: MedicalRecordState) {
        toggleMedicalRecordForm(showForm = false)
        val recordToSave = record.copy(catId = _catId.takeIf { it > 0 } ?: record.catId)

        if(recordToSave.isValid()) {
            viewModelScope.launch {
                catRepository.upsertMedicalRecord(recordToSave.mapToEntity())
            }
        } else {
            viewModelScope.launch {
                _events.send(AppEvent.Error(NetworkError.BAD_REQUEST))
            }
        }
    }

    private fun deleteMedicalRecord(record: MedicalRecordState) {
        toggleDeleteConfirmation(null)
        viewModelScope.launch {
            catRepository.deleteMedicalRecord(record.mapToEntity())
            _events.send(AppEvent.NavigateBack)
        }
    }

    private fun toggleDeleteConfirmation(record: MedicalRecordState? = null) {
        _uiState.update { currentState -> currentState.copy(recordToDelete = record) }
    }

    private fun toggleMedicalRecordForm(record: MedicalRecordState? = null, showForm: Boolean) {
        _uiState.update { currentState -> currentState.copy(selectedRecord = record, showForm = showForm) }
    }

    private fun toggleSortMenu(showMenu: Boolean) {
        _uiState.update { currentState -> currentState.copy(showSortMenu = showMenu) }
    }
}
