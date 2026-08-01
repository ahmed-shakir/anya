package se.supernovait.anya.core.data.network

import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.JsonConvertException
import se.supernovait.anya.core.domain.model.error.NetworkError
import se.supernovait.anya.core.domain.util.Result

suspend inline fun <reified T> responseToResult(response: HttpResponse): Result<T, NetworkError> {
    return when(response.status.value) {
        in 200..299 -> {
            try {
                Result.Success(response.body<T>())
            } catch(e: Exception) {
                val networkError = when(e) {
                    is NoTransformationFoundException,
                    is JsonConvertException -> NetworkError.SERIALIZATION
                    else -> NetworkError.UNKNOWN
                }
                Result.Failure(networkError)
            }
        }
        408 -> Result.Failure(NetworkError.REQUEST_TIMEOUT)
        429 -> Result.Failure(NetworkError.TOO_MANY_REQUESTS)
        in 500..599 -> Result.Failure(NetworkError.SERVER_ERROR)
        else -> Result.Failure(NetworkError.UNKNOWN)
    }
}
