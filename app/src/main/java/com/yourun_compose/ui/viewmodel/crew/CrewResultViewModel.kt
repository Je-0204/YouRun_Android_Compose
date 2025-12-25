package com.yourun_compose.ui.viewmodel.crew

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourun_compose.domain.usecase.challenge.GetCrewResultUseCase
import com.yourun_compose.ui.state.crew.CrewResultUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrewResultViewModel @Inject constructor(
    private val getCrewResultUseCase: GetCrewResultUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CrewResultUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchResultData()
    }

    private fun fetchResultData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val resultDeferred = async { getCrewResultUseCase.getResult() }
            val contributionDeferred = async { getCrewResultUseCase.getContribution() }

            val resultRes = resultDeferred.await()
            val contributionRes = contributionDeferred.await()

            if (resultRes.isSuccess && contributionRes.isSuccess) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        resultData = resultRes.getOrNull(),
                        contributionList = contributionRes.getOrNull() ?: emptyList()
                    )
                }
            } else {
                val errorMsg = resultRes.exceptionOrNull()?.message
                    ?: contributionRes.exceptionOrNull()?.message
                    ?: "결과 정보를 불러오지 못했습니다."

                _uiState.update { it.copy(isLoading = false, errorMessage = errorMsg) }
            }
        }
    }
}