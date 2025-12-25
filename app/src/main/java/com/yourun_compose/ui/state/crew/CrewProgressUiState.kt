package com.yourun_compose.ui.state.crew

import com.yourun_compose.data.model.challenge.CrewMatchingData

data class CrewProgressUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val matchingData: CrewMatchingData? = null
)