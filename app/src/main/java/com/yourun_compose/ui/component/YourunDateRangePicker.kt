package com.yourun_compose.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yourun_compose.ui.theme.DateYellow
import com.yourun_compose.ui.theme.TextGray3
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YourunDateRangePicker(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    onDateSelected: (LocalDate, LocalDate) -> Unit // 선택 완료 시 콜백
) {
    if (showDialog) {
        val datePickerState = rememberDateRangePickerState()

        DatePickerDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(
                    onClick = {
                        val startMillis = datePickerState.selectedStartDateMillis
                        val endMillis = datePickerState.selectedEndDateMillis

                        if (startMillis != null && endMillis != null) {
                            val startDate = Instant.ofEpochMilli(startMillis)
                                .atZone(ZoneId.of("UTC")).toLocalDate()
                            val endDate = Instant.ofEpochMilli(endMillis)
                                .atZone(ZoneId.of("UTC")).toLocalDate()

                            onDateSelected(startDate, endDate)
                            onDismissRequest()
                        }
                    },
                    enabled = datePickerState.selectedStartDateMillis != null &&
                            datePickerState.selectedEndDateMillis != null
                ) {
                    Text("선택 완료")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text("취소")
                }
            }
        ) {
            DateRangePicker(
                state = datePickerState,
                modifier = Modifier.weight(1f),
                title = {
                    Text(
                        text = "챌린지 기간 선택",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = MaterialTheme.colorScheme.primary, // 선택 날짜 색
                    selectedDayContentColor = MaterialTheme.colorScheme.onPrimary, // 선택 날짜 글자색
                    dayInSelectionRangeContainerColor = DateYellow, // 범위 내 배경색
                    dayInSelectionRangeContentColor = TextGray3 // 범위 내 글자색
                )
            )
        }
    }
}