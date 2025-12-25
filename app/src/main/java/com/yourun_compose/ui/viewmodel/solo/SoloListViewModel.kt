package com.yourun_compose.ui.viewmodel.solo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourun_compose.domain.usecase.challenge.GetSoloInfoUseCase
import com.yourun_compose.ui.state.solo.SoloListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SoloListViewModel @Inject constructor(
    private val getSoloInfoUseCase: GetSoloInfoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SoloListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchPendingList()
    }

    fun fetchPendingList() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = getSoloInfoUseCase.getPendingList()

            result.onSuccess { listData ->
                _uiState.update { it.copy(isLoading = false, data = listData) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }
}