package com.yourun_compose.ui.viewmodel.solo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourun_compose.domain.usecase.challenge.CreateSoloChallengeUseCase
import com.yourun_compose.ui.state.solo.SoloCreateUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SoloCreateViewModel @Inject constructor(
    private val createSoloChallengeUseCase: CreateSoloChallengeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SoloCreateUiState())
    val uiState = _uiState.asStateFlow()

    val availableDistances = listOf(1, 3, 5)

    fun selectDistance(distance: Int) {
        if (distance in availableDistances) {
            _uiState.update { it.copy(selectedDistance = distance) }
        }
    }

    fun updateEndDate(input: String) { _uiState.update { it.copy(endDate = input) } }

    fun createChallenge() {
        val state = _uiState.value
        if (state.endDate.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val distanceString = state.selectedDistance.toString()

            val result = createSoloChallengeUseCase(state.endDate, distanceString)

            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isCreateSuccess = true) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }
}