package io.itcorner.moodle.feature.coursedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.itcorner.moodle.core.ui.UiState
import io.itcorner.moodle.feature.courses.toUserMessage
import io.itcorner.moodle.navigation.NavArgs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseDetailViewModel @Inject constructor(
    private val repository: CourseDetailRepository,
    savedState: SavedStateHandle,
) : ViewModel() {

    private val courseId: Long = checkNotNull(savedState[NavArgs.COURSE_ID])
    val courseTitle: String = savedState[NavArgs.COURSE_TITLE] ?: "Course"

    private val _state = MutableStateFlow<UiState<List<Section>>>(UiState.Idle)
    val state: StateFlow<UiState<List<Section>>> = _state.asStateFlow()

    init { load() }

    fun load() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            _state.value = repository.loadSections(courseId).fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.toUserMessage()) },
            )
        }
    }
}
