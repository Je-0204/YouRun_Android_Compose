package com.yourun_compose.ui.state.solo

import com.yourun_compose.data.model.challenge.SoloChallengeDetailData

data class SoloDetailUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val detailData: SoloChallengeDetailData? = null,
    val isJoinSuccess: Boolean = false,
    val joinedChallengeId: Long? = null
)