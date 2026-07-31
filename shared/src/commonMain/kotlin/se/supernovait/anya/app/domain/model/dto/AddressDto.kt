package se.supernovait.anya.app.domain.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class AddressDto(
    val street: String,
    val postalCode: String,
    val city: String,
    val county: String,
    val country: String
)
