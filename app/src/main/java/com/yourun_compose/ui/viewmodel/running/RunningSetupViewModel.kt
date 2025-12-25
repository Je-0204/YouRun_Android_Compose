package com.yourun_compose.ui.viewmodel.running

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourun_compose.data.model.user.UserMateInfo
import com.yourun_compose.domain.usecase.user.ManageMateUseCase
import com.yourun_compose.ui.state.running.RunningSetupUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunningSetupViewModel @Inject constructor(
    private val manageMateUseCase: ManageMateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RunningSetupUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchMates()
    }

    private fun fetchMates() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = manageMateUseCase.getMates()
            result.onSuccess { list ->
                _uiState.update { it.copy(isLoading = false, mateList = list) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun selectMate(mate: UserMateInfo) {
        _uiState.update { it.copy(selectedMate = mate) }
    }

    // (15, 30, 45, 60)
    fun selectDuration(minutes: Int) {
        _uiState.update { it.copy(selectedDurationMinutes = minutes) }
    }
}