package io.itcorner.moodle.feature.courses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.itcorner.moodle.core.network.MoodleApiException
import io.itcorner.moodle.core.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CoursesViewModel @Inject constructor(
    private val repository: CoursesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<List<Course>>>(UiState.Idle)
    val state: StateFlow<UiState<List<Course>>> = _state.asStateFlow()

    init { load() }

    fun load() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            _state.value = repository.loadCourses().fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.toUserMessage()) },
            )
        }
    }
}

internal fun Throwable.toUserMessage(): String = when (this) {
    is IOException -> "No internet connection. Check your network and try again."
    is MoodleApiException -> message ?: "Moodle returned an error."
    else -> "Something went wrong. Please try again."
}
