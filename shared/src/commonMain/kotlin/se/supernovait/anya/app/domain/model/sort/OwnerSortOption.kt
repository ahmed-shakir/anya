package se.supernovait.anya.app.domain.model.sort

import anya.shared.generated.resources.Res
import anya.shared.generated.resources.sort_default_label
import anya.shared.generated.resources.sort_dob_label
import anya.shared.generated.resources.sort_firstname_label
import anya.shared.generated.resources.sort_lastname_label
import org.jetbrains.compose.resources.StringResource
import se.supernovait.anya.core.presentation.common.menu.sort.SortOption

enum class OwnerSortOption(override val label: StringResource) : SortOption {
    DEFAULT(Res.string.sort_default_label),
    DATE_OF_BIRTH(Res.string.sort_dob_label),
    FIRSTNAME(Res.string.sort_firstname_label),
    LASTNAME(Res.string.sort_lastname_label),
}
