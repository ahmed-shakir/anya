package se.supernovait.anya.app.presentation.import

import se.supernovait.anya.app.domain.model.ShareType

data class ImportScreenState(
    val type: ShareType? = null,
    val name: String = "",
    val data: String = "",
    val isImporting: Boolean = false
)
