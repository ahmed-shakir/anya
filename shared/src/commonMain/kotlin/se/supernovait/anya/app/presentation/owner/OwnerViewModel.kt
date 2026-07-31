package se.supernovait.anya.app.presentation.owner

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
import se.supernovait.anya.app.domain.model.sort.OwnerSortOption
import se.supernovait.anya.app.domain.repository.CatRepository
import se.supernovait.anya.app.presentation.address.AddressState
import se.supernovait.anya.app.presentation.address.isValid
import se.supernovait.anya.app.presentation.app.AppEvent
import se.supernovait.anya.app.presentation.navigation.Route
import se.supernovait.anya.app.presentation.owner.state.OwnerScreenState
import se.supernovait.anya.app.presentation.owner.state.OwnerState
import se.supernovait.anya.app.presentation.owner.state.isValid
import se.supernovait.anya.core.domain.model.error.NetworkError
import se.supernovait.anya.core.domain.sharing.ShareHandler

@OptIn(ExperimentalCoroutinesApi::class)
class OwnerViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val catRepository: CatRepository,
    private val shareHandler: ShareHandler,
    private val json: Json
) : ViewModel() {
    private val _sortOption = MutableStateFlow(OwnerSortOption.DEFAULT)
    private val _searchQuery = MutableStateFlow("")
    private val _owners = _sortOption
        .flatMapLatest { sortOption ->
            when(sortOption) {
                OwnerSortOption.DEFAULT -> catRepository.getAllOwners()
                OwnerSortOption.FIRSTNAME -> catRepository.getAllOwnersOrderedByFirstname()
                OwnerSortOption.LASTNAME -> catRepository.getAllOwnersOrderedByLastname()
                OwnerSortOption.DATE_OF_BIRTH -> catRepository.getAllOwnersOrderedByBirthdate()
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _uiState = MutableStateFlow(OwnerScreenState())
    val uiState = combine(_uiState, _sortOption, _searchQuery, _owners) { state, sortOption, searchQuery, owners ->
        state.copy(
            owners = owners
                .filter { it.filterBySearchQuery(searchQuery) }
                .map { it.mapToState() },
            selectedSortOption = sortOption
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), OwnerScreenState())

    private val _events = Channel<AppEvent>()
    val events = _events.receiveAsFlow()

    fun onEvent(event: OwnerScreenEvent) {
        when(event) {
            is OwnerScreenEvent.NavigateToOwner,
            is OwnerScreenEvent.NavigateToCats -> { /* Handled in AnyaApp */ }
            is OwnerScreenEvent.LoadOwner -> {
                val args = savedStateHandle.toRoute<Route.OwnerProfile>()
                getOwnerById(args.id)
            }
            is OwnerScreenEvent.SaveOwner -> saveOwner(event.owner)
            is OwnerScreenEvent.SaveAddress -> saveAddress(event.ownerId, event.address)
            is OwnerScreenEvent.DeleteOwner -> deleteOwner(event.owner)
            is OwnerScreenEvent.ConfirmDeleteOwner -> toggleDeleteConfirmation(event.owner)
            is OwnerScreenEvent.DismissDeleteConfirmation -> toggleDeleteConfirmation(null)
            is OwnerScreenEvent.ShareOwner -> shareOwner(event.owner)
            is OwnerScreenEvent.FilterOwners -> _searchQuery.value = event.searchQuery
            is OwnerScreenEvent.SortOwners -> _sortOption.value = event.sortOption
            is OwnerScreenEvent.ShowSortMenu -> toggleSortMenu(true)
            is OwnerScreenEvent.HideSortMenu -> toggleSortMenu(false)
            is OwnerScreenEvent.ShowOwnerForm -> toggleOwnerForm(event.owner, true)
            is OwnerScreenEvent.HideOwnerForm -> toggleOwnerForm(showForm = false)
            is OwnerScreenEvent.ShowAddressForm -> toggleAddressForm(event.owner, true)
            is OwnerScreenEvent.HideAddressForm -> toggleAddressForm(showForm = false)
            is OwnerScreenEvent.SignOut -> { /* Handled in AnyaApp */ }
        }
    }

    private fun getOwnerById(id: Long) {
        viewModelScope.launch {
            val ownerWithCats = catRepository.getOwnerById(id)
            val owner = ownerWithCats?.owner
            val cats = ownerWithCats?.cats ?: emptyList()
            _uiState.update { currentState -> currentState.copy(selectedOwner = owner?.mapToState(cats)) }
        }
    }

    private fun saveOwner(owner: OwnerState) {
        toggleOwnerForm(showForm = false)

        if(owner.isValid()) {
            viewModelScope.launch {
                catRepository.upsertOwner(owner.mapToEntity())
            }
        } else {
            viewModelScope.launch {
                _events.send(AppEvent.Error(NetworkError.BAD_REQUEST))
            }
        }
    }

    private fun saveAddress(ownerId: Long, address: AddressState) {
        toggleAddressForm(showForm = false)

        if(address.isValid()) {
            viewModelScope.launch {
                val owner = catRepository.getOwnerById(ownerId)?.owner
                val ownerWithAddress = owner?.copy(address = address.mapToEntity())

                if(ownerWithAddress != null) {
                    catRepository.upsertOwner(ownerWithAddress)
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

    private fun shareOwner(owner: OwnerState) {
        viewModelScope.launch {
            val data = json.encodeToString(owner.mapToEntity().mapToDto())
            shareHandler.shareData(ShareType.OWNER.id, data)
        }
    }

    private fun deleteOwner(owner: OwnerState) {
        toggleDeleteConfirmation(null)
        viewModelScope.launch {
            catRepository.deleteOwner(owner.mapToEntity())
            _events.send(AppEvent.NavigateBack)
        }
    }

    private fun toggleDeleteConfirmation(owner: OwnerState? = null) {
        _uiState.update { currentState -> currentState.copy(ownerToDelete = owner) }
    }

    private fun toggleOwnerForm(owner: OwnerState? = null, showForm: Boolean) {
        _uiState.update { currentState -> currentState.copy(selectedOwner = owner, showOwnerForm = showForm) }
    }

    private fun toggleAddressForm(owner: OwnerState? = null, showForm: Boolean) {
        _uiState.update { currentState -> currentState.copy(selectedOwner = owner, showAddressForm = showForm) }
    }

    private fun toggleSortMenu(showMenu: Boolean) {
        _uiState.update { currentState -> currentState.copy(showSortMenu = showMenu) }
    }
}
