package se.supernovait.anya.app.domain.mapper

import se.supernovait.anya.app.data.local.entity.Owner
import se.supernovait.anya.app.presentation.owner.state.OwnerState
import se.supernovait.anya.core.domain.util.isoString
import se.supernovait.anya.core.domain.util.toLocalDate

fun Owner.mapToState(): OwnerState {
    return OwnerState(
        id = id,
        firstname = firstname,
        lastname = lastname,
        username = username,
        dob = dob.toLocalDate(),
        imageUri = imageUri,
        address = address?.mapToState()
    )
}

fun OwnerState.mapToEntity(): Owner {
    return Owner(
        id = id,
        firstname = firstname,
        lastname = lastname,
        username = username,
        dob = dob.isoString(),
        imageUri = imageUri,
        address = address?.mapToEntity()
    )
}
