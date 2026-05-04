package io.itcorner.moodle.feature.courses

import io.itcorner.moodle.core.auth.TokenStore
import io.itcorner.moodle.core.network.MoodleApi
import io.itcorner.moodle.core.network.MoodleApiException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoursesRepository @Inject constructor(
    private val api: MoodleApi,
    private val tokenStore: TokenStore,
) {
    suspend fun loadCourses(): Result<List<Course>> {
        val userId = tokenStore.userId() ?: return Result.failure(IllegalStateException("Not signed in"))
        return try {
            val token = tokenStore.token()
            val list = api.getCourses(userId).map { it.toDomain(token) }
            Result.success(list)
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: MoodleApiException) {
            Result.failure(e)
        }
    }
}
