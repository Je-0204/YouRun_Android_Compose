package com.yourun_compose.ui.viewmodel.solo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourun_compose.domain.usecase.challenge.GetSoloProgressUseCase
import com.yourun_compose.ui.state.solo.SoloProgressUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SoloProgressViewModel @Inject constructor(
    private val getSoloProgressUseCase: GetSoloProgressUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SoloProgressUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadProgress()
    }

    fun loadProgress() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = getSoloProgressUseCase.getMatchingInfo()

            result.onSuccess { data ->
                _uiState.update { it.copy(isLoading = false, matchingInfo = data) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }
}