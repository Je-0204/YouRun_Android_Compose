package com.yourun_compose.ui.state.crew

import com.yourun_compose.data.model.challenge.CrewChallengeResultData
import com.yourun_compose.data.model.challenge.ResultContributionResponse

data class CrewResultUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,

    // Result
    val resultData: CrewChallengeResultData? = null,

    // Contribution Ranking List
    val contributionList: List<ResultContributionResponse> = emptyList()
)