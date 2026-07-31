package se.supernovait.anya.app.domain.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class OwnerDto(
    val id: Long,
    val firstname: String,
    val lastname: String,
    val username: String?,
    val dob: String,
    val address: AddressDto? = null
)
