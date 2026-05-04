package io.itcorner.moodle.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class GradeReportResponse(
    val usergrades: List<UserGradeDto> = emptyList(),
)

@Serializable
data class UserGradeDto(
    val courseid: Long,
    val courseidnumber: String? = null,
    val userid: Long,
    val gradeitems: List<GradeItemDto> = emptyList(),
)

@Serializable
data class GradeItemDto(
    val id: Long,
    val itemname: String? = null,
    val itemtype: String? = null,
    val gradeformatted: String? = null,
    val percentageformatted: String? = null,
    val feedback: String? = null,
)
