package com.yourun_compose.ui.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourun_compose.data.local.SessionManager
import com.yourun_compose.data.model.auth.SignUpRequest
import com.yourun_compose.domain.usecase.auth.CheckDuplicateUseCase
import com.yourun_compose.domain.usecase.validation.ValidateInputUseCase
import com.yourun_compose.ui.state.auth.SignUpUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val validateUseCase: ValidateInputUseCase,
    private val checkDuplicateUseCase: CheckDuplicateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()

    fun updateEmail(input: String) {
        val isValid = validateUseCase.validateEmail(input)
        _uiState.update {
            it.copy(
                email = input,
                isEmailValid = isValid,
                isEmailChecked = false,
                errorMessage = null
            )
        }
    }

    fun updatePassword(input: String) {
        val isValid = validateUseCase.validatePassword(input)
        _uiState.update {
            it.copy(
                password = input,
                isPasswordValid = isValid,
                isPasswordMatch = input == it.passwordcheck,
                errorMessage = null
            )
        }
    }

    fun updatePasswordCheck(input: String) {
        _uiState.update {
            it.copy(
                passwordcheck = input,
                isPasswordMatch = it.password == input
            )
        }
    }

    fun updateNickname(input: String) {
        val isValid = validateUseCase.validateNickname(input)
        _uiState.update {
            it.copy(
                nickname = input,
                isNicknameValid = isValid,
                isNicknameChecked = false,
                errorMessage = null
            )
        }
    }

    fun updateTag1(input: String) { _uiState.update { it.copy(tag1 = input) } }
    fun updateTag2(input: String) { _uiState.update { it.copy(tag2 = input) } }

    // Check Email Duplicated
    fun checkEmail() {
        if (!_uiState.value.isEmailValid) return
        viewModelScope.launch {
            val result = checkDuplicateUseCase.checkEmail(_uiState.value.email)
            result.onSuccess { isDup ->
                _uiState.update { it.copy(isEmailChecked = !isDup, errorMessage = if(isDup) "이미 사용 중인 이메일입니다." else "사용 가능한 이메일입니다.") }
            }.onFailure {
                _uiState.update { it.copy(errorMessage = "중복 확인 오류") }
            }
        }
    }

    // Check Nickname Duplicated
    fun checkNickname() {
        if (!_uiState.value.isNicknameValid) return
        viewModelScope.launch {
            val result = checkDuplicateUseCase.checkNickname(_uiState.value.nickname)
            result.onSuccess { isDup ->
                _uiState.update { it.copy(isNicknameChecked = !isDup, errorMessage = if(isDup) "이미 사용 중인 닉네임입니다." else "사용 가능한 닉네임입니다.") }
            }.onFailure {
                _uiState.update { it.copy(errorMessage = "중복 확인 오류") }
            }
        }
    }

    fun onNextClick() {
        val state = _uiState.value
        if (!state.isInputComplete) {
            _uiState.update { it.copy(errorMessage = "모든 정보를 입력하고 중복 확인을 완료해주세요.") }
            return
        }

        val tempData = SignUpRequest(
            email = state.email,
            password = state.password,
            passwordcheck = state.passwordcheck,
            nickname = state.nickname,
            tendency = "",
            tag1 = state.tag1,
            tag2 = state.tag2
        )

        sessionManager.saveTempSignUpData(tempData)

        _uiState.update { it.copy(isSignUpSuccess = true) }
    }
}