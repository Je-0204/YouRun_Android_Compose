package com.yourun_compose.ui.viewmodel.solo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourun_compose.domain.usecase.challenge.GetSoloResultUseCase
import com.yourun_compose.ui.state.solo.SoloResultUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SoloResultViewModel @Inject constructor(
    private val getSoloResultUseCase: GetSoloResultUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SoloResultUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchResult()
    }

    private fun fetchResult() {
        viewModelScope.launch {
            val result = getSoloResultUseCase()

            result.onSuccess { data ->
                _uiState.update { it.copy(isLoading = false, resultData = data) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }
}