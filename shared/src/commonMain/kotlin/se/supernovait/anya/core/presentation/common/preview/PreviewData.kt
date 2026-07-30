package se.supernovait.anya.core.presentation.common.preview

import se.supernovait.anya.app.presentation.owner.state.OwnerState
import se.supernovait.anya.core.domain.util.toLocalDate

object PreviewData {

    val owner = OwnerState(
        id = 1,
        firstname = "Jane",
        lastname = "Doe",
        dob = "1990-07-20".toLocalDate()
    )
}
