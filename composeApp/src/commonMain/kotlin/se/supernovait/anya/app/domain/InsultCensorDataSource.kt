package se.supernovait.anya.app.domain

import se.supernovait.anya.core.domain.entity.NetworkError
import se.supernovait.anya.core.domain.util.Result

interface InsultCensorDataSource {
    suspend fun censorWords(uncensored: String): Result<String, NetworkError>
}
