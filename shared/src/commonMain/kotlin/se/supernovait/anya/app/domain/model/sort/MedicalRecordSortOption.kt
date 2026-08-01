package se.supernovait.anya.app.domain.model.sort

import anya.shared.generated.resources.Res
import anya.shared.generated.resources.sort_date_label
import anya.shared.generated.resources.sort_default_label
import anya.shared.generated.resources.sort_title_label
import org.jetbrains.compose.resources.StringResource
import se.supernovait.anya.core.presentation.common.menu.sort.SortOption

enum class MedicalRecordSortOption(override val label: StringResource) : SortOption {
    DEFAULT(Res.string.sort_default_label),
    DATE(Res.string.sort_date_label),
    TITLE(Res.string.sort_title_label)
}
