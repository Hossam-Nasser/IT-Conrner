package io.itcorner.moodle.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.itcorner.moodle.core.auth.AuthRepository
import io.itcorner.moodle.feature.courses.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUi(
    val username: String = "student1",
    val password: String = "Demo@12345",
    val userId: String = AuthRepository.DEV_USER_ID.toString(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUi())
    val state: StateFlow<LoginUi> = _state.asStateFlow()

    fun setUsername(value: String) = _state.update { it.copy(username = value, error = null) }
    fun setPassword(value: String) = _state.update { it.copy(password = value, error = null) }
    fun setUserId(value: String) = _state.update { it.copy(userId = value.filter { c -> c.isDigit() }, error = null) }

    fun submit() {
        val s = _state.value
        val userId = s.userId.toLongOrNull()
        if (s.username.isBlank() || s.password.isBlank() || userId == null) {
            _state.update { it.copy(error = "Please fill in all fields (numeric user id).") }
            return
        }
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            authRepository.login(s.username, s.password, userId).fold(
                onSuccess = { _state.update { it.copy(isLoading = false, success = true) } },
                onFailure = { e -> _state.update { it.copy(isLoading = false, error = e.toUserMessage()) } },
            )
        }
    }

    fun useDevToken() {
        authRepository.useDevToken()
        _state.update { it.copy(success = true) }
    }
}
