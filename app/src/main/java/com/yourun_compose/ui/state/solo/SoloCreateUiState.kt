package com.yourun_compose.ui.state.solo

data class SoloCreateUiState(
    val selectedDistance: Int = 1, // km
    val endDate: String = "",   // YYYY-MM-DD HH:mm
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isCreateSuccess: Boolean = false
)