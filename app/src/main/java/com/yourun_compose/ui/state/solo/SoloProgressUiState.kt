package com.yourun_compose.ui.state.solo

import com.yourun_compose.data.model.challenge.SoloMatchingData

data class SoloProgressUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val matchingInfo: SoloMatchingData? = null
)