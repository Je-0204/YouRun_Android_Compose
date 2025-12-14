package com.yourun_compose.data.repository

import com.yourun_compose.data.api.ApiService
import com.yourun_compose.data.local.TokenManager
import com.yourun_compose.data.model.auth.LoginRequest
import com.yourun_compose.data.model.auth.SignUpRequest
import com.yourun_compose.data.model.auth.TokenData
import com.yourun_compose.data.model.auth.UserInitRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) : BaseRepository() {

    // ========================================================================
    // Login
    // ========================================================================
    suspend fun login(request: LoginRequest): Result<TokenData> {
        val result = safeApiCall { apiService.login(request) }

        result.onSuccess { tokenData ->
            tokenManager.saveToken(tokenData.accessToken)
        }

        return result
    }

    // ========================================================================
    // Kakao Login
    // ========================================================================
    suspend fun kakaoLogin(): Result<TokenData> {
        val result = safeApiCall { apiService.kakaoLogin() }

        result.onSuccess { tokenData ->
            tokenManager.saveToken(tokenData.accessToken)
        }

        return result
    }

    // ========================================================================
    // Sign Up & Initial Setting
    // ========================================================================

    // Sign Up
    suspend fun signUp(request: SignUpRequest): Result<Boolean> {
        return safeApiCall { apiService.signUp(request) }
    }

    // Check Email Duplicate
    suspend fun checkEmailDuplicate(email: String): Result<Boolean> {
        return safeApiCall { apiService.checkEmailDuplicate(email) }
    }

    // Check Nickname Duplicate
    suspend fun checkNicknameDuplicate(nickname: String): Result<Boolean> {
        return safeApiCall { apiService.checkNicknameDuplicate(nickname) }
    }

    // Kakao User Initialize
    suspend fun initializeUser(request: UserInitRequest): Result<Boolean> {
        return safeApiCall { apiService.initializeUser(request) }
    }

    // Logout
    fun logout() {
        tokenManager.clearToken()
    }
}