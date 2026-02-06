package com.yourun_compose.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.yourun_compose.ui.component.YourunTopBar
import com.yourun_compose.ui.viewmodel.main.CalendarViewModel
import com.yourun_compose.domain.model.DailyRunRecord
import com.yourun_compose.ui.component.YourunExpandableCalendar
import com.yourun_compose.ui.theme.SelectedBtnBorder
import com.yourun_compose.R
import com.yourun_compose.ui.theme.Border
import com.yourun_compose.ui.theme.DateOrange
import com.yourun_compose.ui.theme.Gray600
import com.yourun_compose.ui.theme.GrayBorder
import com.yourun_compose.ui.theme.Purple400
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CalendarScreen(
    navController: NavController,
    viewModel: CalendarViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    val dateFormatter = remember {
        DateTimeFormatter.ofPattern("yyyy년 M월 d일", Locale.KOREAN)
    }

    Scaffold(
        topBar = {
            YourunTopBar(title = "나의 러닝 기록")
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            YourunExpandableCalendar(
                selectedDate = uiState.selectedDate,
                onDateSelected = { viewModel.onDateSelected(it) }
            )

            HorizontalDivider(
                thickness = 8.dp,
                color = GrayBorder
            )

            Spacer(modifier = Modifier.height(40.dp))

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = uiState.selectedDate.format(dateFormatter),
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = SelectedBtnBorder
                        )
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Box(
                        modifier = Modifier
                            .background(
                                color = DateOrange,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "유런과 ${uiState.runCount}일째!",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "얼마나 뛰었을까요?",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(40.dp))

                if (uiState.currentRecord.hasRecord) {
                    RunRecordCard(record = uiState.currentRecord)

                    Spacer(modifier = Modifier.height(24.dp))

                    // 최고 기록일 때만 표시
                    if (uiState.currentRecord.isPersonalBest) {
                        Text(
                            text = "이번 달 최고 기록 달성!",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                } else {
                    EmptyRecordBox()
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun RunRecordCard(record: DailyRunRecord) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_foot_print),
                        contentDescription = "거리",
                        modifier = Modifier.size(44.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "거리",
                        style = MaterialTheme.typography.titleSmall
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = String.format(Locale.US, "%.1f km", record.runDistanceKm),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(Border, RoundedCornerShape(16.dp))
                        .padding(20.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_clock),
                        contentDescription = "시간",
                        modifier = Modifier
                            .size(44.dp)
                            .align(Alignment.TopStart)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "시간",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.align(Alignment.TopEnd)
                    )

                    Spacer(modifier = Modifier.height(90.dp))

                    Text(
                        text = "${record.runTimeMinutes}분",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.align(Alignment.BottomEnd)
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(Purple400, RoundedCornerShape(16.dp))
                        .padding(20.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_pace),
                        contentDescription = "페이스",
                        modifier = Modifier
                            .size(44.dp)
                            .align(Alignment.TopStart)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "페이스",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.align(Alignment.TopEnd)
                    )

                    Spacer(modifier = Modifier.height(90.dp))

                    Text(
                        text = record.calculatedPace,
                        style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
                        modifier = Modifier.align(Alignment.BottomEnd)
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyRecordBox() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(20.dp))
            .border(1.dp, Color(0xFFE9ECEF), RoundedCornerShape(20.dp))
            .padding(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "러닝 기록이 없는 날이에요.",
            color = Gray600,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}