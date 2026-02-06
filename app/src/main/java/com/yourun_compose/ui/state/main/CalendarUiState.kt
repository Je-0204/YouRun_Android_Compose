package com.yourun_compose.ui.state.main

import com.yourun_compose.domain.model.DailyRunRecord
import java.time.LocalDate

data class CalendarUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val runCount: Int = 12,
    val currentRecord: DailyRunRecord = DailyRunRecord()
)