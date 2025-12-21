package com.yourun_compose.ui.viewmodel.crew

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourun_compose.domain.usecase.challenge.GetCrewInfoUseCase
import com.yourun_compose.domain.usecase.challenge.JoinCrewChallengeUseCase
import com.yourun_compose.ui.state.crew.CrewDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrewDetailViewModel @Inject constructor(
    private val getCrewInfoUseCase: GetCrewInfoUseCase,
    private val joinCrewChallengeUseCase: JoinCrewChallengeUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val challengeId: Long = savedStateHandle.get<String>("challengeId")?.toLongOrNull() ?: -1L

    private val _uiState = MutableStateFlow(CrewDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        if (challengeId != -1L) fetchDetail()
        else _uiState.update { it.copy(isLoading = false, errorMessage = "잘못된 접근입니다.") }
    }

    private fun fetchDetail() {
        viewModelScope.launch {
            val result = getCrewInfoUseCase.getDetail(challengeId)
            result.onSuccess { data ->
                _uiState.update { it.copy(isLoading = false, detailData = data) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun joinChallenge() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = joinCrewChallengeUseCase(challengeId)

            result.onSuccess { joinData ->
                _uiState.update {
                    it.copy(isLoading = false, isJoinSuccess = true, joinedChallengeId = joinData.challengeId)
                }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }
}