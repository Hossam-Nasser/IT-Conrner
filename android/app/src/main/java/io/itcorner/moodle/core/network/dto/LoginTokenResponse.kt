package io.itcorner.moodle.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginTokenResponse(
    val token: String? = null,
    val privatetoken: String? = null,
    val error: String? = null,
    val errorcode: String? = null,
)
