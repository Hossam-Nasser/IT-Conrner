package io.itcorner.moodle.navigation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.itcorner.moodle.core.auth.AuthRepository
import javax.inject.Inject

@HiltViewModel
class StartGateViewModel @Inject constructor(
    auth: AuthRepository,
) : ViewModel() {
    val initiallyLoggedIn: Boolean = auth.isLoggedIn()
}
