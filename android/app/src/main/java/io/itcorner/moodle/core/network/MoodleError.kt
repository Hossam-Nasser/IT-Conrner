package io.itcorner.moodle.core.network

import kotlinx.serialization.Serializable

@Serializable
data class MoodleErrorEnvelope(
    val exception: String? = null,
    val errorcode: String? = null,
    val message: String? = null,
)

class MoodleApiException(
    val errorCode: String?,
    message: String,
) : RuntimeException(message)
