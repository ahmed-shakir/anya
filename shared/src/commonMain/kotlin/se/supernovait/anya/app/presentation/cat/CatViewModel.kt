package se.supernovait.anya.app.presentation.cat

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
import kotlinx.serialization.json.Json
import se.supernovait.anya.app.data.local.entity.filterBySearchQuery
import se.supernovait.anya.app.domain.mapper.mapToDto
import se.supernovait.anya.app.domain.mapper.mapToEntity
import se.supernovait.anya.app.domain.mapper.mapToState
import se.supernovait.anya.app.domain.model.ShareType
import se.supernovait.anya.app.domain.model.sort.CatSortOption
import se.supernovait.anya.app.domain.repository.CatRepository
import se.supernovait.anya.app.presentation.address.AddressState
import se.supernovait.anya.app.presentation.address.isValid
import se.supernovait.anya.app.presentation.app.AppEvent
import se.supernovait.anya.app.presentation.app.auth.AuthenticationManager
import se.supernovait.anya.app.presentation.cat.state.CatScreenState
import se.supernovait.anya.app.presentation.cat.state.CatState
import se.supernovait.anya.app.presentation.cat.state.isValid
import se.supernovait.anya.app.presentation.navigation.Route
import se.supernovait.anya.core.domain.model.error.NetworkError
import se.supernovait.anya.core.domain.sharing.ShareHandler

@OptIn(ExperimentalCoroutinesApi::class)
class CatViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val authManager: AuthenticationManager,
    private val catRepository: CatRepository,
    private val shareHandler: ShareHandler,
    private val json: Json
) : ViewModel() {
    private val _sortType = MutableStateFlow(CatSortOption.DEFAULT)
    private val _searchQuery = MutableStateFlow("")
    private val _ownerId = savedStateHandle.toRoute<Route.Cat>().ownerId
    private val _cats = _sortType
        .flatMapLatest { sortType ->
            when(sortType) {
                CatSortOption.DEFAULT -> catRepository.getAllCats(_ownerId)
                CatSortOption.DATE_OF_BIRTH -> catRepository.getAllCatsOrderedByBirthdate()
                CatSortOption.NAME -> catRepository.getAllCatsOrderedByName()
                else -> catRepository.getAllCats(_ownerId)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _uiState = MutableStateFlow(CatScreenState())
    val uiState = combine(_uiState, _sortType, _searchQuery, _cats) { state, sortType, searchQuery, cats ->
        state.copy(
            cats = cats
                .filter { it.filterBySearchQuery(searchQuery) }
                .map { it.mapToState() },
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CatScreenState())

    private val _events = Channel<AppEvent>()
    val events = _events.receiveAsFlow()

    fun onEvent(event: CatScreenEvent) {
        when(event) {
            is CatScreenEvent.NavigateToCat,
            is CatScreenEvent.NavigateToOwner,
            is CatScreenEvent.NavigateToMedicalRecord -> { /* Handled in AnyaApp */ }
            is CatScreenEvent.LoadCat -> {
                val args = savedStateHandle.toRoute<Route.CatProfile>()
                getCatById(args.id)
            }
            is CatScreenEvent.SaveCat -> saveCat(event.cat, event.isCurrentUserOwner, event.useOwnerAddress)
            is CatScreenEvent.SaveAddress -> saveAddress(event.catId, event.address)
            is CatScreenEvent.DeleteCat -> deleteCat(event.cat)
            is CatScreenEvent.ConfirmDeleteCat -> toggleDeleteConfirmation(event.cat)
            is CatScreenEvent.DismissDeleteConfirmation -> toggleDeleteConfirmation(null)
            is CatScreenEvent.ShareCat -> shareCat(event.cat)
            is CatScreenEvent.FilterCats -> _searchQuery.value = event.searchQuery
            is CatScreenEvent.SortCats -> _sortType.value = event.sortType
            is CatScreenEvent.ShowSortMenu -> toggleSortMenu(true)
            is CatScreenEvent.HideSortMenu -> toggleSortMenu(false)
            is CatScreenEvent.ShowPedigree -> { toggleFileMenu(showMenu = true) }
            is CatScreenEvent.HidePedigree -> { toggleFileMenu(showMenu = false) }
            is CatScreenEvent.ShowCatForm -> toggleCatForm(event.cat, true)
            is CatScreenEvent.HideCatForm -> toggleCatForm(showForm = false)
            is CatScreenEvent.ShowAddressForm -> toggleAddressForm(event.cat, true)
            is CatScreenEvent.HideAddressForm -> toggleAddressForm(showForm = false)
        }
    }

    private fun getCatById(id: Long) {
        viewModelScope.launch {
            val catAndOwner = catRepository.getCatById(id)
            val cat = catAndOwner?.cat
            val owner = catAndOwner?.owner
            _uiState.update { currentState -> currentState.copy(selectedCat = cat?.mapToState(owner)) }
        }
    }

    private fun saveCat(cat: CatState, isCurrentUserOwner: Boolean, useOwnerAddress: Boolean) {
        toggleCatForm(showForm = false)

        if(cat.isValid()) {
            viewModelScope.launch {
                val currentUser = authManager.getCurrentUser()
                var catToSave = cat.mapToEntity()
                if(isCurrentUserOwner && cat.ownerId == null) {
                    catToSave = catToSave.copy(ownerId = currentUser?.id)
                }
                if(useOwnerAddress && cat.address == null) {
                    catToSave = catToSave.copy(address = currentUser?.address)
                }
                catRepository.upsertCat(catToSave)
            }
        } else {
            viewModelScope.launch {
                _events.send(AppEvent.Error(NetworkError.BAD_REQUEST))
            }
        }
    }

    private fun saveAddress(catId: Long, address: AddressState) {
        toggleAddressForm(showForm = false)

        if(address.isValid()) {
            viewModelScope.launch {
                val catAndOwner = catRepository.getCatById(catId)
                val cat = catAndOwner?.cat
                val catWithAddress = cat?.copy(address = address.mapToEntity())

                if(catWithAddress != null) {
                    catRepository.upsertCat(catWithAddress)
                } else {
                    _events.send(AppEvent.Error(NetworkError.SERVER_ERROR))
                }
            }
        } else {
            viewModelScope.launch {
                _events.send(AppEvent.Error(NetworkError.BAD_REQUEST))
            }
        }
    }

    private fun shareCat(cat: CatState) {
        viewModelScope.launch {
            val data = json.encodeToString(cat.mapToEntity().mapToDto())
            shareHandler.shareData(ShareType.CAT.id, data)
        }
    }

    private fun deleteCat(cat: CatState) {
        toggleDeleteConfirmation(null)
        viewModelScope.launch {
            catRepository.deleteCat(cat.mapToEntity())
            _events.send(AppEvent.NavigateBack)
        }
    }

    private fun toggleDeleteConfirmation(cat: CatState? = null) {
        _uiState.update { currentState -> currentState.copy(catToDelete = cat) }
    }

    private fun toggleCatForm(cat: CatState? = null, showForm: Boolean) {
        _uiState.update { currentState -> currentState.copy(selectedCat = cat, showCatForm = showForm) }
    }

    private fun toggleAddressForm(cat: CatState? = null, showForm: Boolean) {
        _uiState.update { currentState -> currentState.copy(selectedCat = cat, showAddressForm = showForm) }
    }

    private fun toggleSortMenu(showMenu: Boolean) {
        _uiState.update { currentState -> currentState.copy(showSortMenu = showMenu) }
    }

    private fun toggleFileMenu(showMenu: Boolean) {
        _uiState.update { currentState -> currentState.copy(showFileMenu = showMenu) }
    }
}
