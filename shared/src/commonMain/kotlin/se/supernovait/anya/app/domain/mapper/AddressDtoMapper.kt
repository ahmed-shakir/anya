package se.supernovait.anya.app.domain.mapper

import se.supernovait.anya.app.data.local.entity.embedded.Address
import se.supernovait.anya.app.domain.model.dto.AddressDto

fun Address.mapToDto(): AddressDto {
    return AddressDto(
        street = street,
        postalCode = postalCode,
        city = city,
        county = county,
        country = country
    )
}

fun AddressDto.mapToEntity(): Address {
    return Address(
        street = street,
        postalCode = postalCode,
        city = city,
        county = county,
        country = country
    )
}
