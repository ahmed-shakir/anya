package se.supernovait.anya.app.presentation.cat

import se.supernovait.anya.app.domain.model.sort.CatSortOption
import se.supernovait.anya.app.presentation.address.AddressState
import se.supernovait.anya.app.presentation.cat.state.CatState
import se.supernovait.anya.core.domain.model.AnyaEvent

sealed interface CatScreenEvent : AnyaEvent {
    data object LoadCat: CatScreenEvent
    data class NavigateToCat(val id: Long): CatScreenEvent
    data class NavigateToOwner(val id: Long): CatScreenEvent
    data class NavigateToMedicalRecord(val catId: Long): CatScreenEvent
    data class SaveCat(val cat: CatState, val isCurrentUserOwner: Boolean = false, val useOwnerAddress: Boolean = false): CatScreenEvent
    data class SaveAddress(val catId: Long, val address: AddressState): CatScreenEvent
    data class DeleteCat(val cat: CatState): CatScreenEvent
    data class ConfirmDeleteCat(val cat: CatState): CatScreenEvent
    data object DismissDeleteConfirmation: CatScreenEvent
    data class ShareCat(val cat: CatState): CatScreenEvent
    data class FilterCats(val searchQuery: String): CatScreenEvent
    data class SortCats(val sortOption: CatSortOption): CatScreenEvent
    data object ShowSortMenu: CatScreenEvent
    data object HideSortMenu: CatScreenEvent
    data object ShowPedigree: CatScreenEvent
    data object HidePedigree: CatScreenEvent
    data class ShowCatForm(val cat: CatState? = null): CatScreenEvent
    data object HideCatForm: CatScreenEvent
    data class ShowAddressForm(val cat: CatState? = null): CatScreenEvent
    data object HideAddressForm: CatScreenEvent
}
