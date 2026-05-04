package io.itcorner.moodle.feature.grades

import io.itcorner.moodle.core.auth.TokenStore
import io.itcorner.moodle.core.network.MoodleApi
import io.itcorner.moodle.core.network.MoodleApiException
import io.itcorner.moodle.feature.courses.htmlUnescape
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

data class GradeItem(
    val id: Long,
    val name: String,
    val grade: String?,
    val percentage: String?,
)

@Singleton
class GradesRepository @Inject constructor(
    private val api: MoodleApi,
    private val tokenStore: TokenStore,
) {
    suspend fun loadGrades(courseId: Long): Result<List<GradeItem>> {
        val userId = tokenStore.userId() ?: return Result.failure(IllegalStateException("Not signed in"))
        return try {
            val response = api.getGradeItems(userId, courseId)
            val items = response.usergrades.firstOrNull()?.gradeitems.orEmpty().map {
                val name = (it.itemname?.takeIf { v -> v.isNotBlank() }
                    ?: if (it.itemtype.equals("course", ignoreCase = true)) "Course total" else "Grade item")
                    .htmlUnescape()
                GradeItem(
                    id = it.id,
                    name = name,
                    grade = it.gradeformatted.cleaned(),
                    percentage = it.percentageformatted.cleaned(),
                )
            }
            Result.success(items)
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: MoodleApiException) {
            Result.failure(e)
        }
    }

    private fun String?.cleaned(): String? {
        if (this.isNullOrBlank()) return null
        val trimmed = trim()
        // Moodle uses "-" or "- %" for ungraded items — show nothing instead.
        return when {
            trimmed == "-" -> null
            trimmed.replace(Regex("[\\s%-]"), "").isEmpty() -> null
            else -> trimmed
        }
    }
}
