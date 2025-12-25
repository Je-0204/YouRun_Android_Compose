package com.yourun_compose.ui.state.auth

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccess: Boolean = false,
    val isKakaoLoginSuccess: Boolean = false,
    val isKakaoNewUser: Boolean = false
)