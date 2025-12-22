package com.yourun_compose.ui.state.mypage

import com.yourun_compose.data.model.user.UserInfo
import com.yourun_compose.data.model.user.UserMateInfo

data class MyPageUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val userInfo: UserInfo? = null,
    val mateList: List<UserMateInfo> = emptyList(),
    val isLogoutSuccess: Boolean = false
) {
    // UI에서 보여줄 때 데이터가 로딩 전이라도 빈 값으로 처리하기 위한 헬퍼 프로퍼티들
    val displayNickname: String get() = userInfo?.nickname ?: ""
    val displayTag1: String get() = userInfo?.tags?.getOrNull(0) ?: ""
    val displayTag2: String get() = userInfo?.tags?.getOrNull(1) ?: ""
    val displayCrewReward: String get() = userInfo?.crewReward?.toString() ?: "0"
    val displaySoloReward: String get() = userInfo?.personalReward?.toString() ?: "0"
    val displayMvpCount: String get() = userInfo?.mvp?.toString() ?: "0"
}