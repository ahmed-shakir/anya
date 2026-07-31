package se.supernovait.anya.app.domain.model.sort

import anya.shared.generated.resources.Res
import anya.shared.generated.resources.sort_default_label
import anya.shared.generated.resources.sort_dob_label
import anya.shared.generated.resources.sort_name_label
import org.jetbrains.compose.resources.StringResource
import se.supernovait.anya.core.presentation.common.menu.sort.SortOption

enum class CatSortOption(override val label: StringResource) : SortOption {
    DEFAULT(Res.string.sort_default_label),
    DATE_OF_BIRTH(Res.string.sort_dob_label),
    NAME(Res.string.sort_name_label)
}
