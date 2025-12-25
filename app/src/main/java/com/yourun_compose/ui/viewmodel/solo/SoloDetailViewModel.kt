package com.yourun_compose.ui.viewmodel.solo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourun_compose.domain.usecase.challenge.GetSoloInfoUseCase
import com.yourun_compose.domain.usecase.challenge.JoinSoloChallengeUseCase
import com.yourun_compose.ui.state.solo.SoloDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SoloDetailViewModel @Inject constructor(
    private val getSoloInfoUseCase: GetSoloInfoUseCase,
    private val joinSoloChallengeUseCase: JoinSoloChallengeUseCase,
    savedStateHandle: SavedStateHandle // 네비게이션으로 넘어온 ID 받기용
) : ViewModel() {

    // NavHost에서 "soloDetail/{challengeId}" 형태로 넘겼다고 가정
    private val challengeId: Long = savedStateHandle.get<String>("challengeId")?.toLongOrNull() ?: -1L

    private val _uiState = MutableStateFlow(SoloDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        if (challengeId != -1L) {
            fetchDetail()
        } else {
            _uiState.update { it.copy(isLoading = false, errorMessage = "잘못된 접근입니다.") }
        }
    }

    private fun fetchDetail() {
        viewModelScope.launch {
            val result = getSoloInfoUseCase.getDetail(challengeId)
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
            val result = joinSoloChallengeUseCase(challengeId)

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