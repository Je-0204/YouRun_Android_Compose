package com.yourun_compose.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yourun_compose.R
import com.yourun_compose.ui.component.YourunButton
import com.yourun_compose.ui.component.YourunTopBar
import com.yourun_compose.ui.theme.RoundButton
import com.yourun_compose.ui.theme.TextPurple
import com.yourun_compose.ui.viewmodel.starting.OnboardingPage
import com.yourun_compose.ui.viewmodel.starting.OnboardingViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    navController: NavController,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {

    val pages = listOf(
        OnboardingPage(
            description = "반가워요!\n지금부터 유런에 대해\n알려드릴게요",
            subText = "만나지 않아도 함께 뛰어요, YOU+RUN!\n유런은 비대면 러닝 메이트와 함께 뛰는 러닝 앱이에요",
            imageRes = R.drawable.img_app_exp_characters
        ),
        OnboardingPage(
            description = "나는 어떤 캐릭터와\n어울릴까?",
            highlightText = "캐릭터",
            subText = "러닝 성향 테스트를 통해\n나와 어울리는 러닝 캐릭터를 알아볼 수 있어요!",
            imageRes = R.drawable.img_app_exp_characters2
        ),
        OnboardingPage(
            description = "러닝 메이트,\n널 이기고 말겠어!",
            highlightText = "러닝 메이트",
            subText = "비대면 러닝 메이트의 러닝 기록을\n비교하여 페이스 조절을 하며 뛸 수 있어요!",
            imageRes = R.drawable.img_app_exp_characters3
        ),
        OnboardingPage(
            description = "누가 누가\n더 많이, 더 꾸준히\n러닝 했을까?",
            highlightText = "더 많이, 더 꾸준히",
            subText = "개인 / 크루 챌린지 참여를 통해\n상대 크루와 러닝 대결을 즐기고 리워드를 얻어봐요!",
            imageRes = R.drawable.img_app_exp_characters
        ),
        OnboardingPage(
            description = "이 날,\n얼마나 러닝 했더라?",
            highlightText = "얼마나 러닝",
            subText = "매일 매일 러닝하고\n캘린더로 나의 기록을 한 눈에 파악할 수 있어요!",
            imageRes = R.drawable.img_app_exp_characters2
        ),
        OnboardingPage(
            description = "유런과 함께 러닝할\n준비 되었나요?",
            highlightText = "유런과 함께 러닝",
            subText = "지금 바로 유런에서\n다양한 사람들과 함께 러닝을 시작해 보아요!",
            imageRes = R.drawable.img_app_exp_characters3
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            YourunTopBar(
                title = "YOU+RUN?",
                onBackClick = { navController.popBackStack() }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(bottom = 48.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(pages.size) { iteration ->
                        val color = if (pagerState.currentPage == iteration)
                            MaterialTheme.colorScheme.secondary
                        else
                            RoundButton

                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(color)
                        )
                    }
                }

                val currentSubText = pages[pagerState.currentPage].subText
                val isLastPage = pagerState.currentPage == pages.lastIndex

                Text(
                    text = currentSubText,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(32.dp))

                YourunButton(
                    text = if (isLastPage) "유런 시작하기" else "다음",
                    onClick = {
                        if (isLastPage) {
                            viewModel.completeOnboarding(navController)
                        } else {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    ) { padding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 0.dp),
            pageSpacing = 0.dp
        ) { pageIndex ->
            val page = pages[pageIndex]

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                val styledText = buildAnnotatedString {
                    val fullText = page.description
                    val target = page.highlightText

                    if (target != null && fullText.contains(target)) {
                        val startIndex = fullText.indexOf(target)
                        val endIndex = startIndex + target.length

                        append(fullText.take(startIndex))

                        withStyle(
                            style = SpanStyle(
                                color = TextPurple,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(target)
                        }

                        append(fullText.substring(endIndex))
                    } else {
                        append(fullText)
                    }
                }

                Text(
                    text = styledText,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 24.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )

                Spacer(modifier = Modifier.height(26.dp))


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.bgd_gradation_yellow),
                        contentDescription = "배경",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Image(
                        painter = painterResource(id = page.imageRes),
                        contentDescription = "달리는 캐릭터들 이미지",
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentScale = ContentScale.FillWidth
                    )
                }
            }
        }
    }
}