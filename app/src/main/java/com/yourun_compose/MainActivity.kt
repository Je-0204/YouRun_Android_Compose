package com.yourun_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yourun_compose.ui.screen.LoginScreen
import com.yourun_compose.ui.screen.SplashScreen
import com.yourun_compose.ui.theme.YouRun_composeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YouRun_composeTheme {
                val navController = rememberNavController()

                // 네비게이션 호스트 설정
                NavHost(navController = navController, startDestination = "splash") {

                    // 1. 스플래시 화면 정의
                    composable("splash") {
                        SplashScreen(navController = navController)
                    }

                    // 2. 로그인 화면 정의
                    composable("login") {
                        LoginScreen()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    YouRun_composeTheme {
        LoginScreen()
    }
}