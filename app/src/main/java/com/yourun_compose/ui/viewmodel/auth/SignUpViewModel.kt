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

    fun checkEmail() {
        if (!_uiState.value.isEmailValid) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            checkDuplicateUseCase.checkEmail(_uiState.value.email)
                .onSuccess { isDup ->
                    _uiState.update { it.copy(isLoading = false, isEmailChecked = !isDup, errorMessage = if(isDup) "이미 사용 중입니다." else "사용 가능합니다.") }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "중복 확인 실패") }
                }
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

    fun toggleTermsAgreement() {
        _uiState.update { it.copy(isTermsAgreed = !it.isTermsAgreed) }
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

    fun checkNickname() {
        if (!_uiState.value.isNicknameValid) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            checkDuplicateUseCase.checkNickname(_uiState.value.nickname)
                .onSuccess { isDup ->
                    _uiState.update { it.copy(isLoading = false, isNicknameChecked = !isDup, errorMessage = if(isDup) "이미 사용 중입니다." else "사용 가능합니다.") }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "중복 확인 실패") }
                }
        }
    }

    fun toggleTag(tag: String) {
        _uiState.update { state ->
            val currentTags = state.selectedTags.toMutableList()
            if (currentTags.contains(tag)) currentTags.remove(tag)
            else if (currentTags.size < 2) currentTags.add(tag)
            state.copy(selectedTags = currentTags)
        }
    }

    fun onNextClick() {
        val state = _uiState.value

        if (!state.canMoveToNext) {
            _uiState.update { it.copy(errorMessage = "입력 정보를 확인해주세요.") }
            return
        }

        if (state.step < 3) {
            _uiState.update { it.copy(step = it.step + 1) }
        } else {
            saveTempDataAndNavigate()
        }
    }

    private fun saveTempDataAndNavigate() {
        val state = _uiState.value

        val tempData = SignUpRequest(
            email = state.email,
            password = state.password,
            passwordcheck = state.passwordcheck,
            nickname = state.nickname,
            tendency = "",
            tag1 = state.selectedTags.getOrElse(0) { "" },
            tag2 = state.selectedTags.getOrElse(1) { "" },
        )

        sessionManager.saveTempSignUpData(tempData)

        _uiState.update { it.copy(navigateToTendency = true) }
    }

    fun onPrevClick() {
        if (_uiState.value.step > 1) {
            _uiState.update { it.copy(step = it.step - 1) }
        }
    }

    fun onNavigateToTendencyHandled() {
        _uiState.update { it.copy(navigateToTendency = false) }
    }
}