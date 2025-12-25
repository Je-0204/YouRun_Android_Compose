package com.yourun_compose.ui.state.solo

import java.time.LocalDate

data class SoloCreateUiState(
    val selectedDistance: Int = 1, // km
    val startDate: LocalDate? = null, // YYYY-MM-DD
    val endDate: LocalDate? = null,   // YYYY-MM-DD
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isCreateSuccess: Boolean = false
) {
    val isReadyToCreate: Boolean
        get() = startDate != null && endDate != null && selectedDistance > 0
}