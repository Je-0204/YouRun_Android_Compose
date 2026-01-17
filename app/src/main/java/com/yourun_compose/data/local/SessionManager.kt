package com.yourun_compose.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.yourun_compose.data.model.auth.SignUpRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

@Singleton
class SessionManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    companion object {
        private val IS_FIRST_RUN = booleanPreferencesKey("is_first_run")
    }

    // 처음 실행 여부 확인
    val isFirstRun: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_FIRST_RUN] ?: true
        }

    private var isMatchingChecked: Boolean = false

    private var tempSignUpData: SignUpRequest? = null

    // 온보딩 완료 처리
    suspend fun completeOnboarding() {
        context.dataStore.edit { preferences ->
            preferences[IS_FIRST_RUN] = false
        }
    }

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