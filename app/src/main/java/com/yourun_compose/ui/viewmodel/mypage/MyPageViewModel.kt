package com.yourun_compose.ui.viewmodel.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourun_compose.domain.usecase.auth.LogoutUseCase
import com.yourun_compose.domain.usecase.user.GetMyProfileUseCase
import com.yourun_compose.domain.usecase.user.ManageMateUseCase
import com.yourun_compose.ui.state.mypage.MyPageUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val manageMateUseCase: ManageMateUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyPageUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchMyPageData()
    }

    fun fetchMyPageData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val profileDeferred = async { getMyProfileUseCase() }
            val matesDeferred = async { manageMateUseCase.getMates() }

            val profileResult = profileDeferred.await()
            val matesResult = matesDeferred.await()

            if (profileResult.isSuccess) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        userInfo = profileResult.getOrNull(),
                        mateList = matesResult.getOrNull() ?: emptyList()
                    )
                }
            } else {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = profileResult.exceptionOrNull()?.message)
                }
            }
        }
    }

    fun deleteMate(mateId: Long) {
        viewModelScope.launch {
            val result = manageMateUseCase.deleteMate(mateId)
            result.onSuccess {
                // 성공하면 목록 새로고침 (혹은 로컬 리스트에서 제거)
                fetchMyPageData()
            }.onFailure { e ->
                _uiState.update { it.copy(errorMessage = "삭제 실패: ${e.message}") }
            }
        }
    }

    fun logout() {
        logoutUseCase()
        _uiState.update { it.copy(isLogoutSuccess = true) }
    }
}