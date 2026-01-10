package com.yourun_compose.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yourun_compose.ui.component.YourunButton
import com.yourun_compose.ui.component.YourunLoading
import com.yourun_compose.ui.component.YourunTextField
import com.yourun_compose.ui.component.YourunTopBar
import com.yourun_compose.ui.navigation.Screen
import com.yourun_compose.ui.theme.RoundButton
import com.yourun_compose.ui.viewmodel.auth.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // 로그인 성공 시 화면 이동 (Side Effect)
    LaunchedEffect(uiState.isLoginSuccess) {
        if (uiState.isLoginSuccess) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            YourunTopBar(title = "로그인")
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(64.dp))

                Text(
                    text = "YOU RUN",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    ),
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "만나지 않아도 함께 뛰어요!",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(modifier = Modifier.height(46.dp))

                // ID
                YourunTextField(
                    value = uiState.email,
                    onValueChange = { viewModel.updateEmail(it) },
                    label = "이메일",
                    errorMessage = if (uiState.emailError) "이메일을 확인해주세요" else null
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Password
                YourunTextField(
                    value = uiState.password,
                    onValueChange = { viewModel.updatePassword(it) },
                    label = "비밀번호",
                    visualTransformation = PasswordVisualTransformation(),
                    errorMessage = if (uiState.passwordError) "비밀번호를 확인해주세요" else null
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Login Button
                YourunButton(
                    text = "로그인",
                    onClick = { viewModel.login() },
                    isEnabled = uiState.email.isNotEmpty() && uiState.password.isNotEmpty()
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Sign Up Button
                YourunButton(
                    text = "회원가입",
                    onClick = { navController.navigate(Screen.SignUp.route) },
                    isEnabled = true,
                    containerColor = RoundButton
                )

                // 에러 메시지 토스트용 텍스트
                if (uiState.errorMessage != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = uiState.errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }

            YourunLoading(isLoading = uiState.isLoading)
        }
    }
}