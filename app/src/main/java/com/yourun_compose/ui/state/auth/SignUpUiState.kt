package com.yourun_compose.ui.state.auth

data class SignUpUiState(
    val step: Int = 1,

    val email: String = "",
    val isEmailValid: Boolean = false,
    val isEmailChecked: Boolean = false, // 중복 확인 완료 여부

    val password: String = "",
    val isPasswordValid: Boolean = false,

    val passwordcheck: String = "",
    val isPasswordMatch: Boolean = false,

    val isTermsAgreed: Boolean = false,

    val nickname: String = "",
    val isNicknameValid: Boolean = false,
    val isNicknameChecked: Boolean = false, // 중복 확인 완료 여부

    val selectedTags: List<String> = emptyList(),

    val tendencyQuestionIndex: Int = 0,
    val tendencyAnswers: MutableList<Int> = mutableListOf(),

    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSignUpSuccess: Boolean = false,
    val navigateToTendency: Boolean = false
) {
    // Next Button Enable
    val canMoveToNext: Boolean
        get() = when (step) {
            1 -> isEmailValid && isEmailChecked && isPasswordValid && isPasswordMatch
            2 -> isTermsAgreed
            3 -> isNicknameValid && isNicknameChecked && selectedTags.size == 2
            else -> false
        }
}