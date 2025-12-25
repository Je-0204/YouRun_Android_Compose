package com.yourun_compose.ui.viewmodel.crew

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourun_compose.domain.usecase.challenge.GetCrewInfoUseCase
import com.yourun_compose.ui.state.crew.CrewListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrewListViewModel @Inject constructor(
    private val getCrewInfoUseCase: GetCrewInfoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CrewListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchPendingList()
    }

    fun fetchPendingList() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = getCrewInfoUseCase.getPendingList()

            result.onSuccess { list ->
                _uiState.update { it.copy(isLoading = false, crewList = list) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }
}