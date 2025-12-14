package com.yourun_compose.data.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        val cleanedToken = token.trim()
        val success = prefs.edit().putString("access_token", cleanedToken).commit()

        if (success) {
            Log.d("TOKEN_MANAGER", "새로운 토큰 저장 완료: $cleanedToken")
        } else {
            Log.e("TOKEN_MANAGER", "토큰 저장 실패")
        }
    }

    fun getToken(): String {
        val token = prefs.getString("access_token", "") ?: ""
        // 로그가 너무 많으면 release 빌드에선 제외하는 게 좋음
        Log.d("TOKEN_MANAGER", "불러온 JWT: $token")
        return token
    }

    fun clearToken() {
        prefs.edit().remove("access_token").apply()
        Log.d("TOKEN_MANAGER", "토큰 삭제됨")
    }
}
