package io.itcorner.moodle.feature.coursedetail

import io.itcorner.moodle.core.auth.TokenStore
import io.itcorner.moodle.core.network.MoodleApi
import io.itcorner.moodle.core.network.MoodleApiException
import io.itcorner.moodle.feature.courses.htmlUnescape
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

data class Section(
    val id: Long,
    val name: String,
    val visible: Boolean,
    val modules: List<Module>,
)

data class Module(
    val id: Long,
    val name: String,
    val url: String?,
    val iconUrl: String?,
    val modName: String?,
)

@Singleton
class CourseDetailRepository @Inject constructor(
    private val api: MoodleApi,
    private val tokenStore: TokenStore,
) {
    suspend fun loadSections(courseId: Long): Result<List<Section>> = try {
        val token = tokenStore.token()
        val sections = api.getCourseContents(courseId).map { dto ->
            Section(
                id = dto.id,
                name = dto.name.htmlUnescape().ifBlank { "Untitled section" },
                visible = (dto.visible ?: 1) == 1,
                modules = dto.modules.map { m ->
                    Module(
                        id = m.id,
                        name = m.name.htmlUnescape(),
                        url = m.url,
                        iconUrl = m.modicon?.let { withToken(it, token) },
                        modName = m.modname,
                    )
                },
            )
        }
        Result.success(sections)
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: MoodleApiException) {
        Result.failure(e)
    }

    private fun withToken(url: String, token: String?): String {
        if (token.isNullOrBlank()) return url
        val sep = if (url.contains("?")) "&" else "?"
        return "$url${sep}token=$token"
    }
}
