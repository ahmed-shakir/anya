package se.supernovait.anya.core.presentation.util

import anya.shared.generated.resources.Res
import anya.shared.generated.resources.error_bad_request
import anya.shared.generated.resources.error_conflict
import anya.shared.generated.resources.error_no_internet
import anya.shared.generated.resources.error_payload_too_large
import anya.shared.generated.resources.error_request_timeout
import anya.shared.generated.resources.error_serialization
import anya.shared.generated.resources.error_too_many_requests
import anya.shared.generated.resources.error_unauthenticated
import anya.shared.generated.resources.error_unauthorized
import anya.shared.generated.resources.error_unknown
import org.jetbrains.compose.resources.getString
import se.supernovait.anya.core.domain.model.error.NetworkError

suspend fun NetworkError.asString(): String {
    val resource = when(this) {
        NetworkError.BAD_REQUEST -> Res.string.error_bad_request
        NetworkError.CONFLICT -> Res.string.error_conflict
        NetworkError.NO_INTERNET -> Res.string.error_no_internet
        NetworkError.PAYLOAD_TOO_LARGE -> Res.string.error_payload_too_large
        NetworkError.REQUEST_TIMEOUT -> Res.string.error_request_timeout
        NetworkError.SERIALIZATION -> Res.string.error_serialization
        NetworkError.SERVER_ERROR -> Res.string.error_unknown
        NetworkError.TOO_MANY_REQUESTS -> Res.string.error_too_many_requests
        NetworkError.UNAUTHENTICATED -> Res.string.error_unauthenticated
        NetworkError.UNAUTHORIZED -> Res.string.error_unauthorized
        NetworkError.UNKNOWN -> Res.string.error_unknown
    }
    return getString(resource)
}
