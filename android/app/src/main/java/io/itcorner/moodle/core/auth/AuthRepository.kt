package io.itcorner.moodle.core.auth

import io.itcorner.moodle.core.network.MoodleApi
import io.itcorner.moodle.core.network.MoodleApiException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: MoodleApi,
    private val tokenStore: TokenStore,
) {
    fun isLoggedIn(): Boolean = tokenStore.token() != null

    fun useDevToken() {
        tokenStore.save(token = DEV_TOKEN, userId = DEV_USER_ID)
    }

    suspend fun login(username: String, password: String, userId: Long): Result<Unit> {
        return try {
            val response = api.login(username, password)
            val token = response.token
            if (token.isNullOrBlank()) {
                Result.failure(
                    MoodleApiException(
                        errorCode = response.errorcode,
                        message = response.error ?: "Invalid credentials",
                    )
                )
            } else {
                tokenStore.save(token, userId)
                Result.success(Unit)
            }
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: MoodleApiException) {
            Result.failure(e)
        }
    }

    fun logout() = tokenStore.clear()

    companion object {
        const val DEV_TOKEN = "c269d73b8ec3265227714bf37f4dd2e4"
        const val DEV_USER_ID = 1003L
    }
}
