package se.supernovait.anya.app.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class CensoredTextDto(
    val result: String
)
