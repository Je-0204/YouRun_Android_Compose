package com.yourun_compose.ui.viewmodel.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourun_compose.domain.usecase.user.ManageMateUseCase
import com.yourun_compose.ui.state.mypage.MateRecommendUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MateRecommendViewModel @Inject constructor(
    private val manageMateUseCase: ManageMateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MateRecommendUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchRecommendations()
    }

    fun fetchRecommendations() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = manageMateUseCase.getRecommendMates()

            result.onSuccess { list ->
                _uiState.update { it.copy(isLoading = false, recommendList = list) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun addMate(mateId: Long) {
        viewModelScope.launch {
            val result = manageMateUseCase.addMate(mateId)

            result.onSuccess {
                _uiState.update { it.copy(actionMessage = "메이트가 추가되었습니다!") }
                // 추가된 후 목록을 다시 불러올지, 아니면 목록에서만 지울지는 UX 결정 사항
                // 여기서는 목록 갱신
                fetchRecommendations()
            }.onFailure { e ->
                _uiState.update { it.copy(actionMessage = "추가 실패: ${e.message}") }
            }
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(actionMessage = null) }
    }
}