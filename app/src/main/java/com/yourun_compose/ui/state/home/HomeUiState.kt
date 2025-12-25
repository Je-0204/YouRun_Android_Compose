package com.yourun_compose.ui.state.home

import com.yourun_compose.data.model.challenge.HomeChallengeData

data class HomeUiState(
    // Loading & Error
    val isLoading: Boolean = true,
    val errorMessage: String? = null,

    // Home Data (User Info & Challenge Status)
    val homeData: HomeChallengeData? = null
)