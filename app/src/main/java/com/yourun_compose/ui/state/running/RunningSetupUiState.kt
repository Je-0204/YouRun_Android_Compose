package com.yourun_compose.ui.state.running

import com.yourun_compose.data.model.user.UserMateInfo

data class RunningSetupUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val mateList: List<UserMateInfo> = emptyList(),

    val selectedMate: UserMateInfo? = null,
    val selectedDurationMinutes: Int? = null, // (15, 30, 45, 60)

    val availableDurations: List<Int> = listOf(15, 30, 45, 60)
) {
    val isReadyToStart: Boolean
        get() = selectedMate != null && selectedDurationMinutes != null
}