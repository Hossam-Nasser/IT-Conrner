package io.itcorner.moodle.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class SectionDto(
    val id: Long,
    val name: String,
    val summary: String? = null,
    val visible: Int? = null,
    val modules: List<ModuleDto> = emptyList(),
)

@Serializable
data class ModuleDto(
    val id: Long,
    val name: String,
    val url: String? = null,
    val modname: String? = null,
    val modplural: String? = null,
    val modicon: String? = null,
    val visible: Int? = null,
)
