package se.supernovait.anya.core.presentation.util

import anya.composeapp.generated.resources.Res
import anya.composeapp.generated.resources.error_conflict
import anya.composeapp.generated.resources.error_no_internet
import anya.composeapp.generated.resources.error_payload_too_large
import anya.composeapp.generated.resources.error_request_timeout
import anya.composeapp.generated.resources.error_serialization
import anya.composeapp.generated.resources.error_too_many_requests
import anya.composeapp.generated.resources.error_unauthorized
import anya.composeapp.generated.resources.error_unknown
import org.jetbrains.compose.resources.getString
import se.supernovait.anya.core.domain.entity.NetworkError

suspend fun NetworkError.asString(): String {
    val resource = when(this) {
        NetworkError.CONFLICT -> Res.string.error_conflict
        NetworkError.NO_INTERNET -> Res.string.error_no_internet
        NetworkError.PAYLOAD_TOO_LARGE -> Res.string.error_payload_too_large
        NetworkError.REQUEST_TIMEOUT -> Res.string.error_request_timeout
        NetworkError.SERIALIZATION -> Res.string.error_serialization
        NetworkError.SERVER_ERROR -> Res.string.error_unknown
        NetworkError.TOO_MANY_REQUESTS -> Res.string.error_too_many_requests
        NetworkError.UNAUTHORIZED -> Res.string.error_unauthorized
        NetworkError.UNKNOWN -> Res.string.error_unknown
    }
    return getString(resource)
}
