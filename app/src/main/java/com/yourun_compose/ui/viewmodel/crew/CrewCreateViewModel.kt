package com.yourun_compose.ui.viewmodel.crew

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourun_compose.domain.usecase.challenge.CreateCrewChallengeUseCase
import com.yourun_compose.ui.state.crew.CrewCreateUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class CrewCreateViewModel @Inject constructor(
    private val createCrewChallengeUseCase: CreateCrewChallengeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CrewCreateUiState())
    val uiState = _uiState.asStateFlow()

    private val nameRegex = Regex("^[가-힣]{2,5}$") // 한글 2~5자, 공백 없음
    private val sloganRegex = Regex("^[가-힣\\s]{3,12}$") // 한글+공백 3~12자

    fun updateName(input: String) {
        val isValid = nameRegex.matches(input)
        _uiState.update {
            it.copy(
                name = input,
                isNameValid = isValid,
                errorMessage = if (!isValid && input.isNotEmpty()) "한글 2~5자(공백 제외)만 가능합니다." else null
            )
        }
    }

    fun updateSlogan(input: String) {
        val isValid = sloganRegex.matches(input)
        _uiState.update {
            it.copy(
                slogan = input,
                isSloganValid = isValid,
                errorMessage = if (!isValid && input.isNotEmpty()) "한글 3~12자(공백 포함)만 가능합니다." else null
            )
        }
    }

    // 캘린더에서 날짜 범위 선택 시 호출
    fun updateDateRange(start: LocalDate, end: LocalDate) {
        val (finalStart, finalEnd) = if (start.isAfter(end)) end to start else start to end
        _uiState.update {
            it.copy(startDate = finalStart, endDate = finalEnd, errorMessage = null)
        }
    }

    fun createChallenge() {
        val state = _uiState.value
        if (!state.isReadyToCreate) {
            _uiState.update { it.copy(errorMessage = "입력 조건을 확인해주세요.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val endDateString = state.endDate!!.format(formatter)

            val result = createCrewChallengeUseCase(state.name, state.slogan, endDateString)

            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isCreateSuccess = true) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }
}