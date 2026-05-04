package io.itcorner.moodle.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class CourseDto(
    val id: Long,
    val fullname: String,
    val shortname: String? = null,
    val progress: Double? = null,
    val courseimage: String? = null,
    val overviewfiles: List<OverviewFileDto> = emptyList(),
)

@Serializable
data class OverviewFileDto(
    val fileurl: String? = null,
    val mimetype: String? = null,
)
