package com.yourun_compose.data.local

import com.yourun_compose.data.model.auth.SignUpRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {
    private var isMatchingChecked: Boolean = false

    private var tempSignUpData: SignUpRequest? = null

    fun isAlreadyChecked(): Boolean = isMatchingChecked

    fun setChecked() {
        isMatchingChecked = true
    }

    fun saveTempSignUpData(data: SignUpRequest) {
        tempSignUpData = data
    }

    fun getTempSignUpData(): SignUpRequest? = tempSignUpData

    fun clearTempSignUpData() {
        tempSignUpData = null
    }
}