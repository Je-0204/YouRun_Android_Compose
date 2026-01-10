package com.yourun_compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.unit.dp
import com.yourun_compose.ui.component.YourunBottomBar
import com.yourun_compose.ui.navigation.Screen
import com.yourun_compose.ui.navigation.YourunNavGraph

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.Mate.route,
        Screen.Challenge.route,
        Screen.MyPage.route,
        Screen.Running.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                YourunBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { targetRoute ->
                        navController.navigate(targetRoute) {
                            popUpTo(Screen.Home.route) {
                                saveState = true
                            }
                            launchSingleTop = true // 중복 쌓임 방지
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(if (showBottomBar) innerPadding else PaddingValues(0.dp))) {
            YourunNavGraph(navController = navController)
        }
    }
}