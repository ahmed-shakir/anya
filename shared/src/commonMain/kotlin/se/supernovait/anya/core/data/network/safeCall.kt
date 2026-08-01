package se.supernovait.anya.core.data.network

import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import se.supernovait.anya.core.domain.model.error.NetworkError
import se.supernovait.anya.core.domain.util.Result

suspend inline fun <reified T> safeCall(execute: () -> HttpResponse): Result<T, NetworkError> {
    val response = try {
        execute()
    } catch(e: Exception) {
        currentCoroutineContext().ensureActive()

        val networkError = when(e) {
            is UnresolvedAddressException -> NetworkError.NO_INTERNET
            is SerializationException -> NetworkError.SERIALIZATION
            else -> NetworkError.UNKNOWN
        }
        return Result.Failure(networkError)
    }
    return responseToResult(response)
}
