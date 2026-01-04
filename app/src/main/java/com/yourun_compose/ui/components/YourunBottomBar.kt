package com.yourun_compose.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.yourun_compose.R
import com.yourun_compose.ui.theme.Gray
import com.yourun_compose.ui.theme.RoundButtonBackground

// 탭 정의
enum class MainTab(
    val route: String,
    val icon: Any,
    val label: String,
) {
    HOME("home", Icons.Default.Home, "홈"),
    MATE("mate", Icons.Default.FavoriteBorder, "메이트"),
    RUNNING("running", R.drawable.img_running, "러닝"),
    CHALLENGE("challenge", Icons.Default.Star, "챌린지"),
    MY_PAGE("my_page", Icons.Default.Person, "마이런");

    companion object {
        fun getByRoute(route: String?): MainTab? = entries.find { it.route == route }
    }
}

@Composable
fun TabIcon(icon: Any,
            contentDescription: String,
            modifier: Modifier = Modifier
) {
    when (icon) {
        is ImageVector -> {
            Icon(imageVector = icon,
                contentDescription = contentDescription,
                modifier = modifier
            )
        }
        is Int -> {
            Icon(painter = painterResource(id = icon),
                contentDescription = contentDescription,
                modifier = modifier
            )
        }
    }
}

// FAB 스타일 바텀 바
@Composable
fun YourunBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        // ---------------------------------------------------------
        // Layer 1: 기본 네비게이션 바 (배경)
        // ---------------------------------------------------------
        NavigationBar(
            containerColor = Color.Black,
            tonalElevation = 8.dp
        ) {
            MainTab.entries.forEach { tab ->
                if (tab == MainTab.RUNNING) {
                    NavigationBarItem(
                        selected = false,
                        onClick = { },
                        icon = { Spacer(modifier = Modifier.size(24.dp)) },
                        label = { Spacer(modifier = Modifier.height(16.dp)) },
                        enabled = false
                    )
                } else {
                    NavigationBarItem(
                        selected = currentRoute == tab.route,
                        onClick = { onNavigate(tab.route) },
                        icon = {
                            TabIcon(
                                icon = tab.icon,
                                contentDescription = tab.label
                            )
                        },
                        label = { Text(text = tab.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = Gray,
                            unselectedTextColor = Gray,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }

        // ---------------------------------------------------------
        // Layer 2: 가운데 둥둥 떠있는 FAB (러닝 버튼)
        // ---------------------------------------------------------

        val isRunningSelected = currentRoute == MainTab.RUNNING.route

        FloatingActionButton(
            onClick = { onNavigate(MainTab.RUNNING.route) },
            shape = CircleShape,
            containerColor = if (isRunningSelected) MaterialTheme.colorScheme.primary else RoundButtonBackground,
            contentColor = if (isRunningSelected) RoundButtonBackground else Gray,
            modifier = Modifier
                .offset(y = (-30).dp)
                .size(64.dp)
        ) {
            TabIcon(
                icon = MainTab.RUNNING.icon,
                contentDescription = MainTab.RUNNING.label,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}