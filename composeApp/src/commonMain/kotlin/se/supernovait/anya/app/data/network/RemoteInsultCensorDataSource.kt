package se.supernovait.anya.app.data.network

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import se.supernovait.anya.app.data.network.dto.CensoredTextDto
import se.supernovait.anya.app.domain.InsultCensorDataSource
import se.supernovait.anya.core.data.network.safeCall
import se.supernovait.anya.core.domain.entity.NetworkError
import se.supernovait.anya.core.domain.util.Result
import se.supernovait.anya.core.domain.util.map

class RemoteInsultCensorDataSource(private val httpClient: HttpClient) : InsultCensorDataSource {
    companion object {
        const val INSULT_CENSOR_BASE_URL = "https://www.purgomalum.com/service/json"
    }

    override suspend fun censorWords(uncensored: String): Result<String, NetworkError> {
        return safeCall<CensoredTextDto> {
            httpClient.get(urlString = INSULT_CENSOR_BASE_URL) {
                parameter("text", uncensored)
            }
        }.map { response ->
            response.result
        }
    }
}
