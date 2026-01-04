package com.yourun_compose.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yourun_compose.ui.theme.Gray600
import com.yourun_compose.ui.theme.GrayBorder
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

@Composable
fun YourunExpandableCalendar(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    // 캘린더 상태 (true: 월간 뷰, false: 주간 뷰)
    var isExpanded by remember { mutableStateOf(true) }

    var currentMonth by remember { mutableStateOf(YearMonth.from(selectedDate)) }

    // 날짜 포맷
    val headerFormatter = DateTimeFormatter.ofPattern("yyyy년 M월")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
            .animateContentSize()
    ) {
        // 헤더 (년/월 표시 + 이전/다음 버튼)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 이전 달 버튼
            if (isExpanded) {
                IconButton(onClick = {
                    currentMonth = currentMonth.minusMonths(1)
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                        contentDescription = "이전 달"
                    )
                }
            }

            // 년/월 텍스트
            Text(
                text = currentMonth.format(headerFormatter),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            )

            // 다음 달 버튼
            if (isExpanded) {
                IconButton(onClick = {
                    currentMonth = currentMonth.plusMonths(1)
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                        contentDescription = "다음 달"
                    )
                }
            }
        }

        // 요일 헤더 (일 월 화 수 목 금 토)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            val daysOfWeek = listOf("일", "월", "화", "수", "목", "금", "토")
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    color = Gray600,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 날짜 그리드
        // 표시할 날짜 리스트 계산
        val daysToShow = remember(isExpanded, currentMonth, selectedDate) {
            if (isExpanded) {
                // 월간: 달력 전체 날짜 구하기
                getDaysInMonth(currentMonth)
            } else {
                // 주간: 선택된 날짜가 포함된 주(일~토) 구하기
                getDaysInWeek(selectedDate)
            }
        }

        // 날짜 그리기
        val rows = daysToShow.chunked(7)
        rows.forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                week.forEach { date ->
                    // 날짜 아이템 컴포넌트
                    DayItem(
                        date = date,
                        isSelected = date == selectedDate,
                        isCurrentMonth = YearMonth.from(date) == currentMonth,
                        onClick = {
                            onDateSelected(date)
                            // 날짜 선택 시, 보고 있는 달도 업데이트
                            currentMonth = YearMonth.from(date)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 접기/펼치기 버튼 (하단 중앙)
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = { isExpanded = !isExpanded }) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "달력 접기/펼치기",
                    tint = GrayBorder
                )
            }
        }
    }
}

// ------------------------------------------------------------------------
// 하위 컴포넌트: 날짜 아이템 (동그라미)
// ------------------------------------------------------------------------
@Composable
fun RowScope.DayItem(
    date: LocalDate,
    isSelected: Boolean,
    isCurrentMonth: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            color = when {
                isSelected -> Color.Black // 선택됨
                !isCurrentMonth -> Color.LightGray // 이번 달 아님
                else -> Gray600// 평범한 날
            },
            style = MaterialTheme.typography.bodyMedium.copy(
                // 이번 달이 아니면 글자 굵기도 가볍게 조절
                fontWeight = if (isCurrentMonth) FontWeight.Normal else FontWeight.Light
            )
        )
    }
}

// ------------------------------------------------------------------------
// 날짜 계산 헬퍼 함수
// ------------------------------------------------------------------------

// 특정 달의 달력 날짜 리스트 (이전 달/다음 달 여분 포함)
private fun getDaysInMonth(yearMonth: YearMonth): List<LocalDate> {
    val firstDayOfMonth = yearMonth.atDay(1)
    val lastDayOfMonth = yearMonth.atEndOfMonth()

    // 달력 시작일: 1일이 속한 주의 일요일
    val startDay = firstDayOfMonth.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
    // 달력 종료일: 마지막 날이 속한 주의 토요일
    val endDay = lastDayOfMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))

    val days = mutableListOf<LocalDate>()
    var current = startDay
    while (!current.isAfter(endDay)) {
        days.add(current)
        current = current.plusDays(1)
    }
    return days
}

// 특정 날짜가 속한 주(일~토) 리스트
private fun getDaysInWeek(date: LocalDate): List<LocalDate> {
    val startDay = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
    val endDay = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))

    val days = mutableListOf<LocalDate>()
    var current = startDay
    while (!current.isAfter(endDay)) {
        days.add(current)
        current = current.plusDays(1)
    }
    return days
}