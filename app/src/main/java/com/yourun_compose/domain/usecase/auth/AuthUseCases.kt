package com.yourun_compose.domain.usecase.auth

import com.yourun_compose.data.local.SessionManager
import com.yourun_compose.data.model.auth.SignUpRequest
import com.yourun_compose.data.model.auth.UserInitRequest
import com.yourun_compose.data.repository.AuthRepository
import javax.inject.Inject

// Login
class LoginUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) =
        repository.login(com.yourun_compose.data.model.auth.LoginRequest(email, password))
}

// Kakao Login
class KakaoLoginUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.kakaoLogin()
}

// Sign Up
class SignUpUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(
        email: String, password: String, passwordCheck: String,
        nickname: String, tag1: String, tag2: String
    ): Result<Boolean> {
        val tendency = sessionManager.getTempTendency() ?: "페이스메이커"

        val request = SignUpRequest(
            email, password, passwordCheck, nickname, tendency, tag1, tag2
        )
        val result = repository.signUp(request)

        if (result.isSuccess) sessionManager.clearTempTendency()

        return result
    }
}

// Init Kakao User
class InitUserUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(nickname: String, tendency: String, tag1: String, tag2: String) =
        repository.initializeUser(UserInitRequest(nickname, tendency, tag1, tag2))
}

// Check Duplicate
class CheckDuplicateUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend fun checkEmail(email: String) = repository.checkEmailDuplicate(email)
    suspend fun checkNickname(nickname: String) = repository.checkNicknameDuplicate(nickname)
}

// Logout
class LogoutUseCase @Inject constructor(private val repository: AuthRepository) {
    operator fun invoke() = repository.logout()
}

// Save Temp Tendency
class SaveTempTendencyUseCase @Inject constructor(private val sessionManager: SessionManager) {
    operator fun invoke(tendency: String) = sessionManager.saveTempTendency(tendency)
}