package se.supernovait.anya.app.domain.mapper

import se.supernovait.anya.app.data.local.entity.Cat
import se.supernovait.anya.app.data.local.entity.Owner
import se.supernovait.anya.app.presentation.cat.state.CatState
import se.supernovait.anya.core.domain.util.isoString
import se.supernovait.anya.core.domain.util.toLocalDate

fun Cat.mapToState(owner: Owner? = null): CatState {
    return CatState(
        id = id,
        ownerId = ownerId,
        name = name,
        nickname = nickname,
        dob = dob.toLocalDate(),
        breed = breed,
        eyeColor = eyeColor,
        furColor = furColor,
        sterilized = sterilized,
        imageUri = imageUri,
        pedigreeUri = pedigreeUri,
        address = address?.mapToState(),
        owner = owner?.mapToState()
    )
}

fun CatState.mapToEntity(): Cat {
    return Cat(
        id = id,
        ownerId = ownerId,
        name = name,
        nickname = nickname,
        dob = dob.isoString(),
        breed = breed,
        eyeColor = eyeColor,
        furColor = furColor,
        sterilized = sterilized,
        imageUri = imageUri,
        pedigreeUri = pedigreeUri,
        address = address?.mapToEntity()
    )
}
