package se.supernovait.anya.app.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class CensoredTextDto(
    val result: String
)
