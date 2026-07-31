package se.supernovait.anya.app.presentation.owner.state

import se.supernovait.anya.app.domain.model.sort.OwnerSortOption

data class OwnerScreenState(
    val owners: List<OwnerState> = emptyList(),
    val selectedOwner: OwnerState? = null,
    val ownerToDelete: OwnerState? = null,
    val selectedSortOption: OwnerSortOption = OwnerSortOption.DEFAULT,
    val isLoading: Boolean = false,
    val showOwnerForm: Boolean = false,
    val showAddressForm: Boolean = false,
    val showSortMenu: Boolean = false
)
