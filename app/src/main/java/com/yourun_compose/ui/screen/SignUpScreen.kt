package com.yourun_compose.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yourun_compose.ui.component.*
import com.yourun_compose.ui.state.auth.SignUpUiState
import com.yourun_compose.ui.theme.RoundButton
import com.yourun_compose.ui.theme.TextGray3
import com.yourun_compose.ui.viewmodel.auth.SignUpViewModel

@Composable
fun SignUpScreen(
    navController: NavController,
    onNavigateToTendency: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    BackHandler {
        if (uiState.step > 1) viewModel.onPrevClick()
        else navController.popBackStack()
    }

    LaunchedEffect(uiState.navigateToTendency) {
        if (uiState.navigateToTendency) {
            onNavigateToTendency()
            viewModel.onNavigateToTendencyHandled() // 상태 초기화 (재진입 방지)
        }
    }

    Scaffold(
        topBar = {
            YourunTopBar(
                title = "회원가입 (${uiState.step}/3)",
                onBackClick = {
                    if (uiState.step > 1) viewModel.onPrevClick()
                    else navController.popBackStack()
                }
            )
        },
        bottomBar = {
            Column(modifier = Modifier.padding(16.dp)) {
                if (uiState.errorMessage != null) {
                    Text(
                        text = uiState.errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                    )
                }

                YourunButton(
                    text = if (uiState.step == 3) "성향 테스트 시작하기" else "다음",
                    onClick = { viewModel.onNextClick() },
                    isEnabled = uiState.canMoveToNext
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier
            .padding(padding)
            .fillMaxSize()) {
            AnimatedContent(
                targetState = uiState.step,
                transitionSpec = {
                    if (targetState > initialState) {
                        // 다음 단계로 갈 때: 오른쪽에서 등장
                        slideInHorizontally { it } + fadeIn() togetherWith slideOutHorizontally { -it } + fadeOut()
                    } else {
                        // 이전 단계로 갈 때: 왼쪽에서 등장
                        slideInHorizontally { -it } + fadeIn() togetherWith slideOutHorizontally { it } + fadeOut()
                    }
                },
                label = "SignUpStepAnimation"
            ) { step ->
                when (step) {
                    1 -> SignUpStep1_Account(uiState, viewModel)
                    2 -> SignUpStep2_Terms(uiState, viewModel)
                    3 -> SignUpStep3_Profile(uiState, viewModel)
                    else -> Box(Modifier.fillMaxSize()) // 예외 처리
                }
            }

            YourunLoading(isLoading = uiState.isLoading)
        }
    }
}

// ========================================================================
// Step 1: 계정 정보 (이메일, 비밀번호)
// ========================================================================
@Composable
private fun SignUpStep1_Account(state: SignUpUiState, viewModel: SignUpViewModel) {

    var showPassword by remember { mutableStateOf(false) }
    var showPasswordCheck by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        Text("안녕하세요!", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
        Text("유런에 오신걸 환영해요", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))

        Spacer(modifier = Modifier.height(4.dp))

        Text("처음 오셨나요? 필요한 정보를 입력해주세요.", style = MaterialTheme.typography.bodySmall.copy(color = TextGray3))

        Spacer(modifier = Modifier.height(50.dp))

        YourunTextField(
            value = state.email,
            onValueChange = { viewModel.updateEmail(it) },
            label = "이메일(아이디)",
            errorMessage = if (state.isEmailValid && !state.isEmailChecked) "중복 확인이 필요합니다" else null,
            trailingIcon = {
                TextButton(
                    onClick = { viewModel.checkEmail() },
                    enabled = state.isEmailValid,
                    modifier = Modifier.padding(end = 8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = Color.White,
                        disabledContainerColor = RoundButton,
                        disabledContentColor = TextGray3
                    ),
                    shape = RoundedCornerShape(8.dp),
                    ) {
                    Text(
                        text = if (state.isEmailChecked) "완료" else "중복 확인",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        )

        YourunTextField(
            value = state.password,
            onValueChange = { viewModel.updatePassword(it) },
            label = "비밀번호 (영문, 숫자 포함 8 ~ 10자)",
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            errorMessage = if (state.password.isNotEmpty() && !state.isPasswordValid) "비밀번호 형식이 올바르지 않습니다" else null,
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "비밀번호 보기 전환"
                    )
                }
            }
        )

        YourunTextField(
            value = state.passwordcheck,
            onValueChange = { viewModel.updatePasswordCheck(it) },
            label = "비밀번호 확인",
            visualTransformation = if (showPasswordCheck) VisualTransformation.None else PasswordVisualTransformation(),
            errorMessage = if (state.passwordcheck.isNotEmpty() && !state.isPasswordMatch) "비밀번호가 일치하지 않습니다" else null,
            trailingIcon = {
                IconButton(onClick = { showPasswordCheck = !showPasswordCheck }) {
                    Icon(
                        imageVector = if (showPasswordCheck) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "비밀번호 보기 전환"
                    )
                }
            }
        )
    }
}

// ========================================================================
// Step 2: 약관 동의
// ========================================================================
@Composable
private fun SignUpStep2_Terms(state: SignUpUiState, viewModel: SignUpViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("서비스 이용을 위해\n약관에 동의해주세요.", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.toggleTermsAgreement() }
                .border(
                    width = 1.dp,
                    color = if (state.isTermsAgreed) MaterialTheme.colorScheme.primary else Color.LightGray,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = if (state.isTermsAgreed) MaterialTheme.colorScheme.primary else Color.LightGray
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "[필수] 이용약관 및 개인정보 처리방침 동의",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    color = RoundButton,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
        ) {
            Text(
                text = "개인 정보 수집/이용 동의",
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "유런은 러닝 기록을 공유하는 앱입니다.\n" +
                        "본인의 러닝 거리, 속도, 시간 등의 러닝 정보를\n" +
                        "타인과 공유할 수 있습니다.",
                style = MaterialTheme.typography.bodySmall.copy(color = TextGray3),
                lineHeight = TextUnit.Unspecified
            )
        }
    }
}

// ========================================================================
// Step 3: 프로필 설정 (닉네임, 태그)
// ========================================================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SignUpStep3_Profile(state: SignUpUiState, viewModel: SignUpViewModel) {

    val availableTags = listOf("#느긋하게", "#음악과", "#열정적", "#자기계발", "#에너자이저", "#왕초보")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("나를 표현할\n닉네임과 태그를 설정해주세요.", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(54.dp))

        Text("닉네임", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(10.dp))

        YourunTextField(
            value = state.nickname,
            onValueChange = { viewModel.updateNickname(it) },
            label = "",
            errorMessage = if (state.isNicknameValid && !state.isNicknameChecked) "중복 확인이 필요합니다" else null,
            trailingIcon = {
                TextButton(
                    onClick = { viewModel.checkNickname() },
                    enabled = state.isEmailValid,
                    modifier = Modifier.padding(end = 8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = Color.White,
                        disabledContainerColor = RoundButton,
                        disabledContentColor = TextGray3
                    ),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text(
                        text = if (state.isEmailChecked) "완료" else "중복 확인",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = TextGray3
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                "띄어쓰기 없이 한글 2~4자만 입력 가능해요",
                style = MaterialTheme.typography.labelLarge.copy(color = TextGray3)
            )
        }

        Spacer(modifier = Modifier.height(60.dp))

        Text("성향 태그", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(14.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 윗줄 3개
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                availableTags.take(3).forEach { tag ->
                    YourunTagBoxItem(
                        text = tag,
                        isSelected = state.selectedTags.contains(tag),
                        onClick = { viewModel.toggleTag(tag) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 아랫줄 3개
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                availableTags.takeLast(3).forEach { tag ->
                    YourunTagBoxItem(
                        text = tag,
                        isSelected = state.selectedTags.contains(tag),
                        onClick = { viewModel.toggleTag(tag) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        if (state.selectedTags.isNotEmpty() && state.selectedTags.size != 2) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "태그를 정확히 2개 선택해주세요.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = TextGray3
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                "내 러닝 스타일을 나타내는 태그 2개를 선택해주세요",
                style = MaterialTheme.typography.labelLarge.copy(color = TextGray3)
            )
        }
    }
}