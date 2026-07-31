package se.supernovait.anya.app.presentation.owner

import se.supernovait.anya.app.domain.model.sort.OwnerSortOption
import se.supernovait.anya.app.presentation.address.AddressState
import se.supernovait.anya.app.presentation.owner.state.OwnerState
import se.supernovait.anya.core.domain.model.AnyaEvent

sealed interface OwnerScreenEvent : AnyaEvent {
    data object LoadOwner: OwnerScreenEvent
    data class NavigateToOwner(val id: Long): OwnerScreenEvent
    data class NavigateToCats(val ownerId: Long): OwnerScreenEvent
    data class SaveOwner(val owner: OwnerState): OwnerScreenEvent
    data class SaveAddress(val ownerId: Long, val address: AddressState): OwnerScreenEvent
    data class DeleteOwner(val owner: OwnerState): OwnerScreenEvent
    data class ConfirmDeleteOwner(val owner: OwnerState): OwnerScreenEvent
    data object DismissDeleteConfirmation: OwnerScreenEvent
    data class ShareOwner(val owner: OwnerState): OwnerScreenEvent
    data class FilterOwners(val searchQuery: String): OwnerScreenEvent
    data class SortOwners(val sortOption: OwnerSortOption): OwnerScreenEvent
    data object ShowSortMenu: OwnerScreenEvent
    data object HideSortMenu: OwnerScreenEvent
    data class ShowOwnerForm(val owner: OwnerState? = null): OwnerScreenEvent
    data object HideOwnerForm: OwnerScreenEvent
    data class ShowAddressForm(val owner: OwnerState? = null): OwnerScreenEvent
    data object HideAddressForm: OwnerScreenEvent
    data object SignOut: OwnerScreenEvent
}
