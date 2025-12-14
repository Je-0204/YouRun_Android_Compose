package com.yourun_compose.data.api

import android.util.Log
import com.yourun_compose.data.local.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestUrl = originalRequest.url.toString()

        // Login Request: Don't add Token
        if (requestUrl.contains("/api/v1/users/login")) {
            Log.d("Interceptor", "로그인 요청 - 헤더 미추가")
            return chain.proceed(originalRequest)
        }

        // Kakao Login Request: Redirect Manually
        if (requestUrl.contains("/api/v1/users/kakao-login")) {
            Log.d("Interceptor", "카카오 로그인 요청 - 리다이렉트 수동 처리")
            var response = chain.proceed(originalRequest)

            while (response.isRedirect) {
                val newUrl = response.header("Location")
                if (newUrl != null) {
                    Log.d("Interceptor", "리다이렉트 감지 -> $newUrl")
                    val newRequest = originalRequest.newBuilder()
                        .url(newUrl)
                        .build()
                    response.close()
                    response = chain.proceed(newRequest)
                } else {
                    break
                }
            }
            return response
        }

        // Normal Request: Add Bearer Token
        val token = tokenManager.getToken()
        return if (token.isNotEmpty()) {
            val newRequest = originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            Log.d("Interceptor", "Authorization 헤더 추가됨")
            chain.proceed(newRequest)
        } else {
            chain.proceed(originalRequest)
        }
    }
}