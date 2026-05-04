package io.itcorner.moodle.feature.courses

import androidx.core.text.HtmlCompat
import io.itcorner.moodle.core.network.dto.CourseDto

data class Course(
    val id: Long,
    val name: String,
    val shortName: String?,
    val progressPercent: Int?,
    val imageUrl: String?,
)

fun CourseDto.toDomain(authToken: String?): Course {
    val raw = courseimage?.takeIf { it.isNotBlank() }
        ?: overviewfiles.firstOrNull { it.mimetype?.startsWith("image/") == true }?.fileurl
        ?: overviewfiles.firstOrNull()?.fileurl
    val tokenizedUrl = raw?.let { withToken(it, authToken) }
    return Course(
        id = id,
        name = fullname.htmlUnescape(),
        shortName = shortname?.takeIf { it.isNotBlank() },
        progressPercent = progress?.toInt(),
        imageUrl = tokenizedUrl,
    )
}

private fun withToken(url: String, token: String?): String {
    if (token.isNullOrBlank()) return url
    val sep = if (url.contains("?")) "&" else "?"
    return "$url${sep}token=$token"
}

internal fun String.htmlUnescape(): String =
    HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().trim()
