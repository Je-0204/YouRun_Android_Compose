package com.yourun_compose.ui.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourun_compose.data.local.TokenManager
import com.yourun_compose.domain.usecase.common.CheckMatchingUseCase
import com.yourun_compose.ui.state.main.AppStartDestination
import com.yourun_compose.ui.state.main.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val checkMatchingUseCase: CheckMatchingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    init {
        checkAppStartStatus()
    }

    private fun checkAppStartStatus() {
        viewModelScope.launch {
            val minSplashTime = 1000L
            val startTime = System.currentTimeMillis()

            val token = tokenManager.getToken()

            var destination: AppStartDestination = AppStartDestination.Login

            if (token.isNotBlank()) {
                val matchingResult = checkMatchingUseCase()

                destination = when (matchingResult) {
                    "SOLO" -> AppStartDestination.SoloProgress
                    "CREW" -> AppStartDestination.CrewProgress
                    else -> AppStartDestination.Home // not matching
                }
            }

            val elapsedTime = System.currentTimeMillis() - startTime
            if (elapsedTime < minSplashTime) {
                delay(minSplashTime - elapsedTime)
            }

            _uiState.update {
                it.copy(startDestination = destination, isReady = true)
            }
        }
    }
}