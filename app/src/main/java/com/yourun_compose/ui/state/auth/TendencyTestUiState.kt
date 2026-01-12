package com.yourun_compose.ui.state.auth

data class TendencyTestUiState(
    val currentQuestionIndex: Int = 0,
    val questionText: String = "",
    val answerTopText: String = "",
    val answerBottomText: String = "",

    val selectedOption: Boolean? = null, // null: 미선택, true: 위, false: 아래

    val progress: Float = 0.25f,

    val isLoading: Boolean = false,
    val isFinished: Boolean = false,

    val tendencyResult: String? = null,
    val userNickname: String = "",

    val errorMessage: String? = null,
    val isFatalError: Boolean = false
)