package com.yourun_compose.data.local

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {
    private var isMatchingChecked: Boolean = false

    fun isAlreadyChecked(): Boolean = isMatchingChecked

    fun setChecked() {
        isMatchingChecked = true
    }
}