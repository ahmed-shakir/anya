package se.supernovait.anya.app.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import se.supernovait.anya.core.data.network.safeCall
import se.supernovait.anya.core.domain.model.error.NetworkError
import se.supernovait.anya.core.domain.util.Result
import se.supernovait.anya.core.domain.util.map

class InsultCensorApi(private val httpClient: HttpClient) {
    companion object {
        const val INSULT_CENSOR_BASE_URL = "https://www.purgomalum.com/service/json"
    }

    suspend fun censorWords(uncensored: String): Result<String, NetworkError> {
        return safeCall<CensoredTextDto> {
            httpClient.get(urlString = INSULT_CENSOR_BASE_URL) {
                parameter("text", uncensored)
            }
        }.map { response ->
            response.result
        }
    }
}
