package se.supernovait.anya.core.presentation.common.preview

import se.supernovait.anya.app.presentation.address.AddressState
import se.supernovait.anya.app.presentation.owner.state.OwnerState
import se.supernovait.anya.core.domain.util.toLocalDate

object PreviewData {
    val address = AddressState(
        street = "Imaginary Street 3",
        city = "Dubai",
        county = "Dubai",
        country = "UAE"
    )

    val owner = OwnerState(
        id = 1,
        firstname = "Jane",
        lastname = "Doe",
        dob = "1990-07-20".toLocalDate(),
        address = address
    )
}
