package com.yourun_compose.ui.state.solo

import com.yourun_compose.data.model.challenge.SoloChallengeResultData

data class SoloResultUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val resultData: SoloChallengeResultData? = null
)