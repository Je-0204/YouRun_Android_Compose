package com.yourun_compose.ui.screen

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yourun_compose.R
import com.yourun_compose.ui.component.YourunButton
import com.yourun_compose.ui.component.YourunLoading
import com.yourun_compose.ui.component.YourunTopBar
import com.yourun_compose.ui.state.auth.TendencyTestUiState
import com.yourun_compose.ui.theme.Border
import com.yourun_compose.ui.theme.SelectedBtnBorder
import com.yourun_compose.ui.theme.TextPurple
import com.yourun_compose.ui.viewmodel.auth.TendencyTestViewModel

private data class ResultContent(
    val title: String,        // 예: 페이스 메이커
    val description: String,  // 설명글
    @param:DrawableRes val icon: Int,    // 성향 이미지
)

@Composable
fun TendencyTestScreen(
    navController: NavController,
    onSignUpSuccess: () -> Unit,
    viewModel: TendencyTestViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isFinished && uiState.tendencyResult != null) {
        TendencyResultScreen(
            resultType = uiState.tendencyResult!!,
            nickname = uiState.userNickname,
            onStartClick = onSignUpSuccess
        )
    } else {
        TendencyQuestionContent(
            uiState = uiState,
            onOptionSelect = { isTop -> viewModel.selectAnswer(isTop) },
            onBackClick = { navController.popBackStack() }
        )
    }

    YourunLoading(isLoading = uiState.isLoading)
}

@Composable
fun TendencyQuestionContent(
    uiState: TendencyTestUiState,
    onOptionSelect: (Boolean) -> Unit,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            YourunTopBar(title = "러닝 성향 테스트", onBackClick = onBackClick)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            CharacterProgressBar(progress = uiState.progress)

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "${uiState.currentQuestionIndex + 1} / 4",
                style = MaterialTheme.typography.bodySmall,
                color = TextPurple,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            AnimatedContent(
                targetState = uiState.questionText,
                transitionSpec = {
                    slideInHorizontally { it } + fadeIn() togetherWith slideOutHorizontally { -it } + fadeOut()
                },
                label = "QuestionAnimation"
            ) { question ->
                Text(
                    text = question,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                    textAlign = TextAlign.Center,
                    minLines = 2
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "둘 중 하나를 선택해주세요 :)",
                style = MaterialTheme.typography.bodySmall.copy(color = TextPurple)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 답변 A (위)
            SelectableAnswerButton(
                text = uiState.answerTopText,
                isSelected = uiState.selectedOption == true,
                onClick = { onOptionSelect(true) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 답변 B (아래)
            SelectableAnswerButton(
                text = uiState.answerBottomText,
                isSelected = uiState.selectedOption == false,
                onClick = { onOptionSelect(false) }
            )
        }
    }
}

// ------------------------------------------------------------------------
// 캐릭터 프로그레스 바
// ------------------------------------------------------------------------
@Composable
fun CharacterProgressBar(progress: Float) {

    val animatedProgress by animateFloatAsState(targetValue = progress, label = "ProgressAnim")

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
    ) {
        val trackWidth = maxWidth

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomStart
        ) {
            // 회색 트랙 (배경)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(Color.LightGray, RoundedCornerShape(4.dp))
            )

            // 진행된 트랙 (색상)
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .height(8.dp)
                    .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(4.dp))
            )

            val iconSize = 60.dp
            val iconOffset = (trackWidth * animatedProgress) - (iconSize / 2)

            Icon(
                painter = painterResource(R.drawable.img_pacemaker_running),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(iconSize)
                    .offset(x = if (iconOffset < 0.dp) 0.dp else iconOffset)
                    .padding(bottom = 8.dp)
            )
        }
    }
}

// 선택 가능한 답변 버튼
@Composable
fun SelectableAnswerButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.White
    val borderColor = if (isSelected) SelectedBtnBorder else Color.LightGray
    val textColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Black

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        border = BorderStroke(1.dp, borderColor),
        elevation = null // 그림자 제거
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

// ------------------------------------------------------------------------
// 결과 화면 (3가지 분기)
// ------------------------------------------------------------------------
@Composable
fun TendencyResultScreen(
    resultType: String,  // "스프린터", "페이스메이커", "트레일러너"
    nickname: String,    // 유저 닉네임
    onStartClick: () -> Unit, // 홈으로 이동
) {
    val content = when (resultType) {
        "스프린터" -> ResultContent(
            title = "스프린터",
            description = "전력 질주와 함께\n짜릿한 성취를 즐기는 스프린터!",
            icon = R.drawable.img_sprinter
        )
        "트레일러너" -> ResultContent(
            title = "트레일 러너",
            description = "길이 없으면 길을 만드는\n모험가 정신이 가득한 트레일 러너!",
            icon = R.drawable.img_trailrunner
        )
        else -> ResultContent( // 기본값: 페이스메이커
            title = "페이스 메이커",
            description = "어떤 상황에서도 속도를 조절하며\n목표에 도달하는 팀의 중심, 페이스 메이커!",
            icon = R.drawable.img_pacemaker
        )
    }

    Scaffold(
        topBar = {
            YourunTopBar(title = "러닝 성향 테스트 결과")
        },
        bottomBar = {
            Column(modifier = Modifier.padding(16.dp)) {
                YourunButton(
                    text = "회원가입 완료하기",
                    onClick = onStartClick
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "러닝 성향 테스트 결과",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)) {
                        append(nickname)
                    }
                    append("님은 ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)) {
                        append(content.title)
                    }
                    append("!")
                },
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            Box(
                modifier = Modifier
                    .size(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bgd_gradation_yellow),
                    contentDescription = "배경",
                    modifier = Modifier.fillMaxSize()
                )

                Image(
                    painter = painterResource(id = content.icon),
                    contentDescription = content.title,
                    modifier = Modifier.size(180.dp)
                )
            }

            Spacer(modifier = Modifier.height(60.dp))

            // 성향 설명
            Box(
                modifier = Modifier
                    .background(
                        color = Border,
                        shape = RoundedCornerShape(30.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    text = content.description,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}