package com.yourun_compose.ui.state.mypage

import com.yourun_compose.R
import com.yourun_compose.data.model.user.UserInfo
import com.yourun_compose.data.model.user.UserMateInfo

data class MyPageUiState(
    val isLoading: Boolean = false,
    val userInfo: UserInfo? = null,
    val mateList: List<UserMateInfo> = emptyList(),
    val errorMessage: String? = null,
    val isLogoutSuccess: Boolean = false
) {
    // UI에서 보여줄 때 데이터가 로딩 전이라도 빈 값으로 처리하기 위한 헬퍼 프로퍼티들
    val displayNickname: String get() = userInfo?.nickname ?: "제제"
    val displayTags: List<String> get() = userInfo?.tags.takeIf { !it.isNullOrEmpty() } ?: listOf("#열정적", "#에너자이저")
    val displayTendency: String get() = userInfo?.tendency ?: "페이스메이커"
    val displayCrewReward: Long get() = userInfo?.crewReward ?: 0
    val displaySoloReward: Long get() = userInfo?.personalReward ?: 0
    val displayMvpCount: Long get() = userInfo?.mvp ?: 0
    val profileImageRes: Int
        get() = when (displayTendency) {
            "페이스 메이커" -> R.drawable.img_profile_pacemaker
            "트레일 러너" -> R.drawable.img_profile_trailrunner
            "스프린터" -> R.drawable.img_profile_sprinter
            else -> R.drawable.img_profile_pacemaker
        }
}