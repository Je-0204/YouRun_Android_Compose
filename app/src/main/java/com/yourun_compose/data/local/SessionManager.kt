package com.yourun_compose.data.local

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {
    private var isMatchingChecked: Boolean = false

    private var tempTendencyResult: String? = null

    fun isAlreadyChecked(): Boolean = isMatchingChecked

    fun setChecked() {
        isMatchingChecked = true
    }

    fun saveTempTendency(tendency: String) {
        tempTendencyResult = tendency
    }

    fun getTempTendency(): String? = tempTendencyResult

    fun clearTempTendency() {
        tempTendencyResult = null
    }
}