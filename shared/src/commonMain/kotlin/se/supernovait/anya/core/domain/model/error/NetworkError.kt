package se.supernovait.anya.core.domain.model.error

enum class NetworkError : Error {
    BAD_REQUEST,
    CONFLICT,
    NO_INTERNET,
    PAYLOAD_TOO_LARGE,
    REQUEST_TIMEOUT,
    SERIALIZATION,
    SERVER_ERROR,
    TOO_MANY_REQUESTS,
    UNAUTHENTICATED,
    UNAUTHORIZED,
    UNKNOWN
}
