package com.yourun_compose.ui.state.crew

import com.yourun_compose.data.model.challenge.CrewChallengeRes

data class CrewListUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val crewList: List<CrewChallengeRes> = emptyList()
)