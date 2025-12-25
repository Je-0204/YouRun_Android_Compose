package com.yourun_compose.ui.state.auth

data class TendencyTestUiState(
    val currentQuestionIndex: Int = 0,
    val questionText: String = "",
    val answerTopText: String = "",
    val answerBottomText: String = "",

    val isLoading: Boolean = false,
    val isFinished: Boolean = false,
    val errorMessage: String? = null,
    val isFatalError: Boolean = false
)