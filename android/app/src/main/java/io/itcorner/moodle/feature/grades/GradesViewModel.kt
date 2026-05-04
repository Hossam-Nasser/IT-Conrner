package io.itcorner.moodle.feature.grades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.itcorner.moodle.core.ui.UiState
import io.itcorner.moodle.feature.courses.Course
import io.itcorner.moodle.feature.courses.CoursesRepository
import io.itcorner.moodle.feature.courses.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GradesUi(
    val courses: UiState<List<Course>> = UiState.Idle,
    val selectedCourseId: Long? = null,
    val items: UiState<List<GradeItem>> = UiState.Idle,
)

@HiltViewModel
class GradesViewModel @Inject constructor(
    private val coursesRepository: CoursesRepository,
    private val gradesRepository: GradesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(GradesUi())
    val state: StateFlow<GradesUi> = _state.asStateFlow()

    init { loadCourses() }

    fun loadCourses() {
        _state.update { it.copy(courses = UiState.Loading) }
        viewModelScope.launch {
            coursesRepository.loadCourses().fold(
                onSuccess = { courses ->
                    val firstId = courses.firstOrNull()?.id
                    _state.update { it.copy(courses = UiState.Success(courses), selectedCourseId = firstId) }
                    if (firstId != null) loadItems(firstId)
                },
                onFailure = { error ->
                    _state.update { it.copy(courses = UiState.Error(error.toUserMessage())) }
                },
            )
        }
    }

    fun selectCourse(courseId: Long) {
        if (_state.value.selectedCourseId == courseId) return
        _state.update { it.copy(selectedCourseId = courseId) }
        loadItems(courseId)
    }

    private fun loadItems(courseId: Long) {
        _state.update { it.copy(items = UiState.Loading) }
        viewModelScope.launch {
            val next = gradesRepository.loadGrades(courseId).fold(
                onSuccess = { list -> UiState.Success(list) },
                onFailure = { e -> UiState.Error(e.toUserMessage()) },
            )
            _state.update { it.copy(items = next) }
        }
    }
}
