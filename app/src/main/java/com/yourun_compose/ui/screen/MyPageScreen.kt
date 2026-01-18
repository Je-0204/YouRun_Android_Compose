package com.yourun_compose.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yourun_compose.R
import com.yourun_compose.ui.component.YourunButton
import com.yourun_compose.ui.component.YourunLoading
import com.yourun_compose.ui.component.YourunTopBar
import com.yourun_compose.ui.navigation.Screen
import com.yourun_compose.ui.theme.CircleBackground
import com.yourun_compose.ui.theme.GrayFont
import com.yourun_compose.ui.theme.RoundButton
import com.yourun_compose.ui.viewmodel.mypage.MyPageViewModel

@Composable
fun MyPageScreen(
    navController: NavController,
    viewModel: MyPageViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    val topBarHeight = 56.dp

    Scaffold(
        containerColor = Color.White
    ) { padding ->
        Box(
            modifier = Modifier
            .fillMaxSize()
            .padding(bottom = padding.calculateBottomPadding())
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(bottom = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
                        Spacer(modifier = Modifier.height(topBarHeight))

                        Box(
                            modifier = Modifier
                                .size(140.dp)
                                .clip(CircleShape)
                        ) {
                            Image(
                                painter = painterResource(id = uiState.profileImageRes),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = uiState.displayNickname,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            uiState.displayTags.forEach { tag ->
                                TagChip(text = tag)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(50.dp))

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            // TODO: 정보 수정 화면으로 이동
                            // navController.navigate(Screen.EditProfile.route)
                        }
                        .background(RoundButton)
                        .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "내 정보 수정",
                        style = MaterialTheme.typography.titleSmall.copy(color = GrayFont)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CircleBackground),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem(
                            label = "크루 리워드",
                            count = uiState.displayCrewReward,
                            iconRes = R.drawable.img_crew_reward
                        )

                        // 구분선
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(40.dp)
                                .background(Color.White.copy(alpha = 0.5f))
                        )

                        StatItem(
                            label = "개인 리워드",
                            count = uiState.displaySoloReward,
                            iconRes = R.drawable.img_solo_reward
                        )

                        // 구분선
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(40.dp)
                                .background(Color.White.copy(alpha = 0.5f))
                        )

                        StatItem(
                            label = "MVP",
                            count = uiState.displayMvpCount,
                            iconRes = R.drawable.img_mvp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(50.dp))

                Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                    YourunButton(
                        text = "성향 테스트 다시하기",
                        onClick = {
                            navController.navigate(Screen.TendencyTest.route)
                        },
                        containerColor = RoundButton,
                        contentColor = GrayFont
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            YourunTopBar(
                title = "마이런",
                containerColor = Color.Transparent
            )

            YourunLoading(isLoading = uiState.isLoading)
        }
    }
}

@Composable
private fun TagChip(text: String) {
    Box(
        modifier = Modifier
            .background(
                color = Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = Color.Black
        )
    }
}

@Composable
private fun StatItem(label: String, count: Long, iconRes: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = Color.White
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                modifier = Modifier.size(20.dp),
                tint = Color.Unspecified
            )

            Text(
                text = "$count",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}