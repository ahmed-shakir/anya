package se.supernovait.anya.app.domain.mapper

import se.supernovait.anya.app.data.local.entity.embedded.Address
import se.supernovait.anya.app.presentation.owner.state.AddressState

fun Address.mapToState(): AddressState {
    return AddressState(
        street = street,
        postalCode = postalCode,
        city = city,
        county = county,
        country = country
    )
}

fun AddressState.mapToEntity(): Address {
    return Address(
        street = street,
        postalCode = postalCode,
        city = city,
        county = county,
        country = country
    )
}
