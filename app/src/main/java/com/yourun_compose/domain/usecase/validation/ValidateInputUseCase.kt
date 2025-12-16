package com.yourun_compose.domain.usecase.validation

import android.util.Patterns
import javax.inject.Inject

class ValidateInputUseCase @Inject constructor() {

    // Email
    fun validateEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Password: 영문+숫자 포함, 8~10자
    fun validatePassword(password: String): Boolean {
        // (?=.*[A-Za-z]): 영문 포함
        // (?=.*[0-9]): 숫자 포함
        // [A-Za-z0-9]{8,10}: 영문, 숫자 조합으로 8~10자
        val regex = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z0-9]{8,10}$".toRegex()
        return regex.matches(password)
    }

    // Nickname: 띄어쓰기 없이 한글 2~4자
    fun validateNickname(nickname: String): Boolean {
        val regex = "^[가-힣]{2,4}$".toRegex()
        return regex.matches(nickname)
    }
}