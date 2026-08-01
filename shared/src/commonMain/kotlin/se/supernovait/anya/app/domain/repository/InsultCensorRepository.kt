package se.supernovait.anya.app.domain.repository

import se.supernovait.anya.core.domain.model.error.NetworkError
import se.supernovait.anya.core.domain.util.Result

interface InsultCensorRepository {
    suspend fun censorWords(uncensored: String): Result<String, NetworkError>
    suspend fun getCounter(): Int
    suspend fun incrementCounter(): Int
}
