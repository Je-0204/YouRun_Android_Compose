package com.yourun_compose.ui.state.mypage

import com.yourun_compose.data.model.user.UserMateInfo

data class MateRecommendUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,

    val recommendList: List<UserMateInfo> = emptyList(),

    // 친구 추가 요청 결과 메시지 (Toast용)
    val actionMessage: String? = null
)