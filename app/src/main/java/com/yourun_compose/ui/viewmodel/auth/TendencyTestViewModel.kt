package com.yourun_compose.ui.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourun_compose.data.local.SessionManager
import com.yourun_compose.domain.usecase.auth.SignUpUseCase
import com.yourun_compose.ui.state.auth.TendencyTestUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private data class QuestionData(
    val question: String,
    val answerTop: String,
    val answerBottom: String
)

@HiltViewModel
class TendencyTestViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(TendencyTestUiState())
    val uiState = _uiState.asStateFlow()

    private val questions = listOf(
        QuestionData("오늘은 꼭 운동을 해야지!\n 어느 시간에 나갈 예정인가요?",
            "하루의 시작을 상쾌하게! 아침 7시",
            "운동은 역시 하루의 끝에! 저녁 9시"),
        QuestionData("운동하기 싫은데...\n 그치만 이럴 때 의지가 불타올라!",
            "라이벌에게 질 수 없다! 경쟁할 때",
            "다 함께 으쌰으쌰! 협동할 때"),
        QuestionData("러닝 목표를 세울 때\n 더 중요하다고 여기는 것은?",
            "짧고 굵게! 목표 시간 채우기",
            "가능한 길게! 목표 거리 채우기"),
        QuestionData("토끼와 거북이의 경주!\n 당신이라면 어떤 동물일까요?",
            "총알처럼 재빠르게! 토끼",
            "한 발 한 발 꾸준하게! 거북이")
    )

    private var currentTotalScore = 0

    init { loadQuestion(0) }

    private fun loadQuestion(index: Int) {
        if (index < questions.size) {
            val q = questions[index]
            _uiState.update {
                it.copy(
                    currentQuestionIndex = index,
                    questionText = q.question,
                    answerTopText = q.answerTop,
                    answerBottomText = q.answerBottom,
                    selectedOption = null, // 선택 초기화
                    progress = (index + 1) / 4f
                )
            }
        }
    }

    fun selectAnswer(isTop: Boolean) {
        if (_uiState.value.isLoading || _uiState.value.selectedOption != null) return

        viewModelScope.launch {
            _uiState.update { it.copy(selectedOption = isTop) }

            delay(300)

            currentTotalScore += if (isTop) 1 else 2
            val nextIndex = _uiState.value.currentQuestionIndex + 1

            if (nextIndex < questions.size) {
                loadQuestion(nextIndex)
            } else {
                finishTestAndSignUp()
            }
        }
    }

    private fun finishTestAndSignUp() {
        val tendencyResult = when (currentTotalScore) {
            in 4..5 -> "스프린터"
            in 6..7 -> "페이스메이커"
            8 -> "트레일러너"
            else -> "페이스메이커"
        }

        val savedData = sessionManager.getTempSignUpData()
        if (savedData == null) {
            _uiState.update {
                it.copy(
                    errorMessage = "가입 정보가 유실되었습니다. 처음부터 다시 시도해주세요.",
                    isFatalError = true
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = signUpUseCase(
                savedData.email, savedData.password, savedData.passwordcheck,
                savedData.nickname, tendencyResult, savedData.tag1, savedData.tag2
            )

            result.onSuccess {
                    sessionManager.clearTempSignUpData()
                    _uiState.update { it.copy(
                        isLoading = false,
                        isFinished = true,
                        tendencyResult = tendencyResult,
                        userNickname =  savedData.nickname
                    ) }
                }
                .onFailure { e ->
                    val errorMsg = e.message ?: "알 수 없는 오류가 발생했습니다."

                    if (errorMsg.contains("이미 존재") || errorMsg.contains("중복")) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "이미 가입된 정보입니다. 정보를 수정해주세요.",
                                isFatalError = true
                            )
                        }
                    } else {
                        // 일시적 오류
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "회원가입 실패: $errorMsg"
                            )
                        }
                    }
                }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}