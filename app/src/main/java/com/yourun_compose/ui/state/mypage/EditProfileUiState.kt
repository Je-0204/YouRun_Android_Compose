package com.yourun_compose.ui.state.mypage

data class EditProfileUiState(
    val nickname: String = "",
    val tag1: String = "",
    val tag2: String = "",
    val userTendency: String = "",

    val isNicknameValid: Boolean = true,
    val isNicknameChecked: Boolean = true,

    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isUpdateSuccess: Boolean = false
) {
    fun canSubmit(originalNickname: String): Boolean {
        val isNicknameChanged = nickname != originalNickname
        return if (isNicknameChanged) {
            isNicknameValid && isNicknameChecked && tag1.isNotBlank() && tag2.isNotBlank()
        } else {
            isNicknameValid && tag1.isNotBlank() && tag2.isNotBlank()
        }
    }
}