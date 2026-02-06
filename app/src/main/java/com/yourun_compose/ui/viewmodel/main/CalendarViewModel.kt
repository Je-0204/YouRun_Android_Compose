package com.yourun_compose.ui.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourun_compose.domain.model.DailyRunRecord
import com.yourun_compose.ui.state.main.CalendarUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchRecord(LocalDate.now())
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
        fetchRecord(date)
    }

    private fun fetchRecord(date: LocalDate) {
        viewModelScope.launch {
            if (date.dayOfMonth % 2 == 0) {
                _uiState.update {
                    it.copy(
                        currentRecord = DailyRunRecord(
                            runTimeMinutes = 45,
                            runDistanceKm = 5.2,
                            isPersonalBest = (date.dayOfMonth == 10),
                            hasRecord = true
                        )
                    )
                }
            } else {
                _uiState.update {
                    it.copy(currentRecord = DailyRunRecord(hasRecord = false))
                }
            }
        }
    }
}