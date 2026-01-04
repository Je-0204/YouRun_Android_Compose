package com.yourun_compose.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack // 뒤로 가기 아이콘
import androidx.compose.material.icons.filled.DateRange // 캘린더 아이콘
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YourunTopBar(
    title: String,
    onBackClick: (() -> Unit)? = null,     // 뒤로가기 버튼 (null이면 숨김)
    onCalendarClick: (() -> Unit)? = null, // 캘린더 버튼 (null이면 숨김)
    containerColor: Color = MaterialTheme.colorScheme.primary
) {
    CenterAlignedTopAppBar(
        // 제목
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        // 왼쪽 아이콘 (뒤로가기)
        navigationIcon = {
            if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back Button"
                    )
                }
            }
        },
        // 오른쪽 아이콘 (캘린더)
        actions = {
            if (onCalendarClick != null) {
                IconButton(onClick = onCalendarClick) {
                    Icon(
                        imageVector = Icons.Default.DateRange, // 달력 모양 아이콘
                        contentDescription = "Calendar Button"
                    )
                }
            }
        },
        // 색상 설정
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}