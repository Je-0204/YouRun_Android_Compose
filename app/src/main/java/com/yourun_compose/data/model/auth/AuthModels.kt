package com.yourun_compose.data.model.auth

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val email: String,
    val password: String
)

data class SignUpRequest(
    val email: String,
    val password: String,
    val passwordcheck: String,
    val nickname: String,
    val tendency: String, // 혹은 Tendency enum 사용 고려
    val tag1: String,
    val tag2: String
)

data class TokenData(
    @SerializedName("access_token", alternate = ["string"]) // Normal: access_token, Kakao: string
    val accessToken: String
)
