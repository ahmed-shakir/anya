package se.supernovait.anya.app.domain.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class CatDto(
    val id: Long,
    val ownerId: Long?,
    val name: String,
    val nickname: String,
    val dob: String,
    val breed: String,
    val eyeColor: String,
    val furColor: String,
    val sterilized: Boolean,
    val address: AddressDto? = null
)
