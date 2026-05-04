package io.itcorner.moodle.core.network

import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject

/**
 * Moodle returns HTTP 200 even on application errors, signalling failure
 * via a JSON envelope { exception, errorcode, message }. Detect that shape
 * and surface it as a typed exception so repositories can route it cleanly.
 */
class MoodleErrorInterceptor @Inject constructor(
    private val json: Json,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val body = response.body ?: return response
        val raw = body.string()
        val mediaType = body.contentType()

        if (raw.contains("\"exception\"") && raw.contains("\"errorcode\"")) {
            runCatching {
                json.decodeFromString(MoodleErrorEnvelope.serializer(), raw)
            }.getOrNull()?.let { envelope ->
                throw MoodleApiException(
                    errorCode = envelope.errorcode,
                    message = envelope.message ?: envelope.exception ?: "Moodle error",
                )
            }
        }
        return response.newBuilder().body(raw.toResponseBody(mediaType)).build()
    }
}
