package se.supernovait.anya.app.presentation.import

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.screen_Import_error_message
import anya.shared.generated.resources.screen_Import_success_message
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.getString
import se.supernovait.anya.app.domain.mapper.mapToEntity
import se.supernovait.anya.app.domain.model.ShareType
import se.supernovait.anya.app.domain.model.dto.CatDto
import se.supernovait.anya.app.domain.model.dto.OwnerDto
import se.supernovait.anya.app.domain.repository.CatRepository
import se.supernovait.anya.app.presentation.app.AppEvent
import se.supernovait.anya.app.presentation.navigation.Route
import se.supernovait.anya.core.domain.model.error.NetworkError

class ImportViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val catRepository: CatRepository,
    private val json: Json
) : ViewModel() {
    private val _uiState = MutableStateFlow(ImportScreenState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<AppEvent>()
    val events = _events.receiveAsFlow()

    init {
        val args = savedStateHandle.toRoute<Route.Import>()
        val type = ShareType.entries.find { it.id == args.type }
        _uiState.update { it.copy(type = type, data = args.data) }
        loadSummary(type, args.data)
    }

    fun onEvent(event: ImportScreenEvent) {
        when (event) {
            ImportScreenEvent.Import -> importData()
            ImportScreenEvent.Cancel -> { /* Handled in AnyaApp */ }
            ImportScreenEvent.ViewDetails -> { /* Handled in AnyaApp */ }
        }
    }

    private fun loadSummary(type: ShareType?, data: String) {
        try {
            when (type) {
                ShareType.CAT -> {
                    val dto = json.decodeFromString<CatDto>(data)
                    _uiState.update { it.copy(name = dto.name) }
                }
                ShareType.OWNER -> {
                    val dto = json.decodeFromString<OwnerDto>(data)
                    _uiState.update { it.copy(name = "${dto.firstname} ${dto.lastname}") }
                }
                null -> {}
            }
        } catch (e: Exception) {
            viewModelScope.launch {
                _events.send(AppEvent.Error(NetworkError.SERIALIZATION))
            }
        }
    }

    private fun importData() {
        val state = _uiState.value
        val type = state.type
        val data = state.data

        if (type == null || data.isBlank()) return

        _uiState.update { it.copy(isImporting = true) }
        viewModelScope.launch {
            try {
                when (type) {
                    ShareType.CAT -> {
                        val dto = json.decodeFromString<CatDto>(data)
                        // Reset ID to 0 to ensure it's saved as a new entry
                        catRepository.upsertCat(dto.mapToEntity().copy(id = 0, ownerId = null))
                    }
                    ShareType.OWNER -> {
                        val dto = json.decodeFromString<OwnerDto>(data)
                        // Reset ID to 0 to ensure it's saved as a new entry
                        catRepository.upsertOwner(dto.mapToEntity().copy(id = 0))
                    }
                }
                val successMessage = getString(Res.string.screen_Import_success_message, state.name)
                _events.send(AppEvent.Message(successMessage))
                _events.send(AppEvent.NavigateBack)
            } catch (e: Exception) {
                val errorMessage = getString(Res.string.screen_Import_error_message)
                _events.send(AppEvent.Message(errorMessage))
                _events.send(AppEvent.Error(NetworkError.SERVER_ERROR))
            } finally {
                _uiState.update { it.copy(isImporting = false) }
            }
        }
    }
}
