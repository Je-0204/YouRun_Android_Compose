package com.yourun_compose.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.yourun_compose.ui.screen.LoginScreen
import com.yourun_compose.ui.screen.SignUpScreen
//import com.yourun_compose.ui.screen.SignUpScreen
import com.yourun_compose.ui.screen.SplashScreen

@Composable
fun YourunNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Splash
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }

        // Login
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }

        // Sign Up
        composable(Screen.SignUp.route) {
            SignUpScreen(
                navController = navController,
                onNavigateToTendency = {
                    navController.navigate(Screen.TendencyTest.route)
                }
            )
        }

        // Tendency Test
//        composable(Screen.TendencyTest.route) {
//            TendencyTestScreen(
//                navController = navController,
//                onSignUpSuccess = {
//                    // 가입 완료되면 홈으로 이동 (로그인/회원가입 스택 제거)
//                    navController.navigate(Screen.Home.route) {
//                        popUpTo(Screen.Login.route) { inclusive = true }
//                    }
//                }
//            )
//        }

        // Main Tab
        composable(Screen.Home.route) { PlaceholderScreen("홈 화면", navController) }
        composable(Screen.Mate.route) { PlaceholderScreen("메이트 화면", navController) }
        composable(Screen.Challenge.route) { PlaceholderScreen("챌린지 화면", navController) }
        composable(Screen.MyPage.route) { PlaceholderScreen("마이페이지", navController) }

        // Running
        composable(Screen.Running.route) {
            // 여기서 시작하기 누르면 -> createRoute() 써서 이동
            PlaceholderScreen("러닝 준비 (메이트/시간 선택)", navController)
        }

        // Running Tracking
        composable(
            route = Screen.RunningTracking.route,
            arguments = listOf(
                navArgument("time") { type = NavType.IntType },
                navArgument("mateId") { type = NavType.LongType },
                navArgument("mateName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val time = backStackEntry.arguments?.getInt("time") ?: 15
            val mateId = backStackEntry.arguments?.getLong("mateId") ?: -1L
            val mateName = backStackEntry.arguments?.getString("mateName") ?: ""

            // TODO: RunningTrackingScreen(...) 연결
            PlaceholderScreen("러닝 중: $time 분 / 메이트: $mateName", navController)
        }

        composable(Screen.RunningResult.route) {
            PlaceholderScreen("러닝 결과", navController)
        }
    }
}

// 임시 화면 (개발용)
@Composable
fun PlaceholderScreen(name: String, navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "여기는 $name 입니다")
    }
}