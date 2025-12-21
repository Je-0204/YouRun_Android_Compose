package com.yourun_compose.ui.state.crew

import java.time.LocalDate

data class CrewCreateUiState(
    val name: String = "",
    val slogan: String = "",

    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,

    val isNameValid: Boolean = false,
    val isSloganValid: Boolean = false,

    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isCreateSuccess: Boolean = false
) {
    val isReadyToCreate: Boolean
        get() = isNameValid && isSloganValid && startDate != null && endDate != null
}