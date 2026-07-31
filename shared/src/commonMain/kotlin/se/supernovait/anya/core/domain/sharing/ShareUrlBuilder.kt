package se.supernovait.anya.core.domain.sharing

import io.ktor.http.decodeURLQueryComponent
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
object ShareUrlBuilder {
    private const val SCHEME = "anya"
    private const val HOST = "share"
    private const val SCHEME_HTTPS = "https"
    private const val HOST_HTTPS = "anya.supernovait.se"
    private const val PATH_HTTPS = "/share"
    private const val PARAM_TYPE = "type"
    private const val PARAM_DATA = "data"

    fun build(type: String, data: String): String {
        val encodedData = Base64.UrlSafe.encode(data.encodeToByteArray())
        return "$SCHEME://$HOST?$PARAM_TYPE=$type&$PARAM_DATA=$encodedData"
    }

    fun buildHttps(type: String, data: String): String {
        val encodedData = Base64.UrlSafe.encode(data.encodeToByteArray())
        return "$SCHEME_HTTPS://$HOST_HTTPS$PATH_HTTPS?$PARAM_TYPE=$type&$PARAM_DATA=$encodedData"
    }

    fun parse(url: String): Pair<String?, String?> {
        val isValidCustom = url.startsWith("$SCHEME://$HOST")
        val isValidHttps = url.startsWith("$SCHEME_HTTPS://$HOST_HTTPS$PATH_HTTPS")
        
        if (!isValidCustom && !isValidHttps) return null to null
        
        return try {
            val queryParams = url.substringAfter("?").split("&").associate {
                val parts = it.split("=", limit = 2)
                if (parts.size == 2) {
                    parts[0].decodeURLQueryComponent() to parts[1].decodeURLQueryComponent()
                } else "" to ""
            }
            val type = queryParams[PARAM_TYPE]
            val data = queryParams[PARAM_DATA]?.let { 
                Base64.UrlSafe.decode(it).decodeToString()
            }
            type to data
        } catch (e: Exception) {
            null to null
        }
    }
}
