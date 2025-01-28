package se.supernovait.anya.core.domain.entity

enum class NetworkError : Error {
    CONFLICT,
    NO_INTERNET,
    PAYLOAD_TOO_LARGE,
    REQUEST_TIMEOUT,
    SERIALIZATION,
    SERVER_ERROR,
    TOO_MANY_REQUESTS,
    UNAUTHORIZED,
    UNKNOWN;
}
