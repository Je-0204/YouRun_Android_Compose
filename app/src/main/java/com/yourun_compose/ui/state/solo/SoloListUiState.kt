package com.yourun_compose.ui.state.solo

import com.yourun_compose.data.model.challenge.PendingSoloChallengesData

data class SoloListUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val data: PendingSoloChallengesData? = null
)