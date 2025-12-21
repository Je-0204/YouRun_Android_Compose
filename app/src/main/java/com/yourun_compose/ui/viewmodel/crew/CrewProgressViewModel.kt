package com.yourun_compose.ui.viewmodel.crew

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourun_compose.domain.usecase.challenge.GetCrewProgressUseCase
import com.yourun_compose.ui.state.crew.CrewProgressUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrewProgressViewModel @Inject constructor(
    private val getCrewProgressUseCase: GetCrewProgressUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CrewProgressUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadProgress()
    }

    fun loadProgress() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = getCrewProgressUseCase.getMatchingInfo()

            result.onSuccess { data ->
                _uiState.update { it.copy(isLoading = false, matchingData = data) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }
}