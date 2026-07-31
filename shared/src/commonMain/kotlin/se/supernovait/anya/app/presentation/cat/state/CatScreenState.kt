package se.supernovait.anya.app.presentation.cat.state

import se.supernovait.anya.app.domain.model.sort.CatSortOption

data class CatScreenState(
    val cats: List<CatState> = emptyList(),
    val selectedCat: CatState? = null,
    val catToDelete: CatState? = null,
    val selectedSortOption: CatSortOption = CatSortOption.DEFAULT,
    val showCatForm: Boolean = false,
    val showAddressForm: Boolean = false,
    val showSortMenu: Boolean = false,
    val showFileMenu: Boolean = false
)
