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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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

    // 커스텀 캘린더에서 "첫 번째 클릭(start)"과 "두 번째 클릭(end)"이 완료되었을 때 이 함수를 호출해주세요.
    fun updateDateRange(start: LocalDate, end: LocalDate) {
        val (finalStart, finalEnd) = if (start.isAfter(end)) {
            end to start
        } else {
            start to end
        }

        _uiState.update {
            it.copy(startDate = finalStart, endDate = finalEnd, errorMessage = null)
        }
    }

    fun createChallenge() {
        val state = _uiState.value
        if (state.startDate == null || state.endDate == null) {
            _uiState.update { it.copy(errorMessage = "기간을 설정해주세요.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val distanceString = state.selectedDistance.toString()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val endDateString = state.endDate.format(formatter)

            val result = createSoloChallengeUseCase(endDateString, distanceString)

            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isCreateSuccess = true) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }
}