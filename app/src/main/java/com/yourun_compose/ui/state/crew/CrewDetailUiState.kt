package com.yourun_compose.ui.state.crew

import com.yourun_compose.data.model.challenge.CrewChallengeRes

data class CrewDetailUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val detailData: CrewChallengeRes? = null,

    val isJoinSuccess: Boolean = false,
    val joinedChallengeId: Long? = null
)