package com.yourun_compose.ui.viewmodel.starting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourun_compose.data.local.SessionManager
import com.yourun_compose.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _destination = MutableStateFlow<String?>(null)
    val destination = _destination.asStateFlow()

    init {
        checkDestination()
    }

    private fun checkDestination() {
        viewModelScope.launch {
            val delayJob = launch { delay(2500) }

            val isFirst = sessionManager.isFirstRun.first()

            delayJob.join()

            if (isFirst) {
                _destination.value = Screen.Onboarding.route // 처음이면 온보딩
            } else {
                _destination.value = Screen.Login.route // 아니면 로그인
            }
        }
    }
}