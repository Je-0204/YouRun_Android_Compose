package com.yourun_compose.ui.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourun_compose.domain.usecase.auth.KakaoLoginUseCase
import com.yourun_compose.domain.usecase.auth.LoginUseCase
import com.yourun_compose.ui.state.auth.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val kakaoLoginUseCase: KakaoLoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun updateEmail(input: String) {
        _uiState.update { it.copy(email = input, errorMessage = null) }
    }

    fun updatePassword(input: String) {
        _uiState.update { it.copy(password = input, errorMessage = null) }
    }

    fun login() {
        if (_uiState.value.email.isBlank() || _uiState.value.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Enter Email and Password.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = loginUseCase(_uiState.value.email, _uiState.value.password)

            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isLoginSuccess = true) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun kakaoLogin() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = kakaoLoginUseCase()

            result.onSuccess { tokenData ->
                // 토큰은 Repository에서 저장됨.
                // 여기서는 기존 유저인지 신규 유저인지 판단 필요 (서버 응답 필드 확인 필요)
                // *가정: TokenData에 isNewUser 같은 필드가 있거나,
                // 토큰만 오면 일단 성공으로 간주하고 메인에서 프로필 체크.
                _uiState.update { it.copy(isLoading = false, isKakaoLoginSuccess = true) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = "Kakao Login Failure: ${e.message}") }
            }
        }
    }
}