package se.supernovait.anya.app.domain.mapper

import se.supernovait.anya.app.data.local.entity.Cat
import se.supernovait.anya.app.domain.model.dto.CatDto

fun Cat.mapToDto(): CatDto {
    return CatDto(
        id = id,
        ownerId = ownerId,
        name = name,
        nickname = nickname,
        dob = dob,
        breed = breed,
        eyeColor = eyeColor,
        furColor = furColor,
        sterilized = sterilized,
        address = address?.mapToDto()
    )
}

fun CatDto.mapToEntity(): Cat {
    return Cat(
        id = id,
        ownerId = ownerId,
        name = name,
        nickname = nickname,
        dob = dob,
        breed = breed,
        eyeColor = eyeColor,
        furColor = furColor,
        sterilized = sterilized,
        address = address?.mapToEntity()
    )
}
