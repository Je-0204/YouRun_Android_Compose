package com.yourun_compose.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yourun_compose.R
import com.yourun_compose.ui.component.YourunButton
import com.yourun_compose.ui.component.YourunLoading
import com.yourun_compose.ui.component.YourunTagBoxItem
import com.yourun_compose.ui.component.YourunTextField
import com.yourun_compose.ui.component.YourunTopBar
import com.yourun_compose.ui.theme.RoundButton
import com.yourun_compose.ui.theme.TextGray3
import com.yourun_compose.ui.viewmodel.mypage.EditProfileViewModel

@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(uiState.isUpdateSuccess) {
        if (uiState.isUpdateSuccess) {
            navController.popBackStack()
        }
    }

    val availableTags = listOf("#느긋하게", "#음악과", "#열정적", "#자기계발", "#에너자이저", "#왕초보")

    Scaffold(
        topBar = {
            YourunTopBar(
                title = "마이런",
                onBackClick = { navController.popBackStack() }
            )
        },
        containerColor = Color.White
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(46.dp))

                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                ) {
                    val imageRes = when (uiState.userTendency) {
                        "스프린터" -> R.drawable.img_profile_sprinter
                        "페이스 메이커" -> R.drawable.img_profile_pacemaker
                        "트레일 러너" -> R.drawable.img_profile_trailrunner
                        else -> R.drawable.img_profile_pacemaker
                    }

                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = "Profile Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = "닉네임",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(8.dp))

                YourunTextField(
                    value = uiState.nickname,
                    onValueChange = { viewModel.updateNickname(it) },
                    label = "",
                    // 에러 메시지: 중복확인 안됐거나, 뷰모델에서 에러가 넘어왔을 때
                    errorMessage = if (uiState.isNicknameValid && !uiState.isNicknameChecked)
                        "중복 확인이 필요합니다"
                    else
                        uiState.errorMessage,
                    trailingIcon = {
                        TextButton(
                            onClick = { viewModel.checkNickname() },
                            enabled = uiState.isNicknameValid && !uiState.isNicknameChecked,
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
                                text = if (uiState.isNicknameChecked) "완료" else "중복 확인",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp).align(Alignment.Start)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = TextGray3
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "띄어쓰기 없이 한글 2~4자만 입력 가능해요",
                        style = MaterialTheme.typography.labelLarge.copy(color = TextGray3)
                    )
                }

                Spacer(modifier = Modifier.height(50.dp))

                Text(
                    text = "성향 태그",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Start)
                )

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
                                isSelected = (tag == uiState.tag1 || tag == uiState.tag2),
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
                                isSelected = (tag == uiState.tag1 || tag == uiState.tag2),
                                onClick = { viewModel.toggleTag(tag) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp).align(Alignment.Start)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = TextGray3
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "내 러닝 스타일을 나타내는 태그 2개를 선택해주세요",
                        style = MaterialTheme.typography.labelLarge.copy(color = TextGray3)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.height(44.dp))

                YourunButton(
                    text = "변경하기",
                    onClick = { viewModel.submitUpdate() },
                    isEnabled = uiState.canSubmit,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }

            YourunLoading(isLoading = uiState.isLoading)
        }
    }
}