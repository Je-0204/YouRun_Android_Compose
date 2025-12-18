package com.yourun_compose.ui.state.auth

data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val passwordcheck: String = "",
    val nickname: String = "",
    val tag1: String = "",
    val tag2: String = "",

    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isNicknameValid: Boolean = false,
    val isPasswordMatch: Boolean = false,

    val isEmailChecked: Boolean = false,
    val isNicknameChecked: Boolean = false,

    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSignUpSuccess: Boolean = false
) {
    // Button Enable
    val isInputComplete: Boolean
        get() = isEmailValid && isEmailChecked &&
                isPasswordValid && isPasswordMatch &&
                isNicknameValid && isNicknameChecked &&
                tag1.isNotBlank() && tag2.isNotBlank()
}