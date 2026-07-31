package se.supernovait.anya.app.domain.mapper

import se.supernovait.anya.app.data.local.entity.Owner
import se.supernovait.anya.app.domain.model.dto.OwnerDto

fun Owner.mapToDto(): OwnerDto {
    return OwnerDto(
        id = id,
        firstname = firstname,
        lastname = lastname,
        username = username,
        dob = dob,
        address = address?.mapToDto()
    )
}

fun OwnerDto.mapToEntity(): Owner {
    return Owner(
        id = id,
        firstname = firstname,
        lastname = lastname,
        username = username,
        dob = dob,
        address = address?.mapToEntity()
    )
}
