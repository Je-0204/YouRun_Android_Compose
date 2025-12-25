package com.yourun_compose.ui.viewmodel.running

import android.content.Context
import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourun_compose.data.local.RunningStateManager
import com.yourun_compose.data.model.running.RunningCreationData
import com.yourun_compose.domain.usecase.running.GetMateRecordUseCase
import com.yourun_compose.domain.usecase.running.SaveRunningResultUseCase
import com.yourun_compose.service.RunningService
import com.yourun_compose.ui.state.running.RunningTrackingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunningTrackingViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val runningStateManager: RunningStateManager,
    private val getMateRecordUseCase: GetMateRecordUseCase,
    private val saveRunningResultUseCase: SaveRunningResultUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val targetMinutes = savedStateHandle.get<String>("time")?.toIntOrNull() ?: 30
    private val mateId = savedStateHandle.get<String>("mateId")?.toLongOrNull() ?: -1L
    private val mateName = savedStateHandle.get<String>("mateName") ?: "메이트"

    private val _uiState = MutableStateFlow(RunningTrackingUiState())
    val uiState = _uiState.asStateFlow()

    private var _resultResponse: RunningCreationData? = null
    val resultResponse: RunningCreationData? get() = _resultResponse

    init {
        initializeRun()
    }

    private fun initializeRun() {
        _uiState.update { it.copy(targetTimeMinutes = targetMinutes, mateName = mateName) }

        if (mateId != -1L) {
            viewModelScope.launch {
                getMateRecordUseCase(mateId)
                    .onSuccess { distance ->
                        _uiState.update { it.copy(mateRecordDistance = if (distance > 0) distance else 1) }
                    }
                    .onFailure {
                        _uiState.update { it.copy(errorMessage = "메이트 기록 로딩 실패") }
                    }
            }
        }

        viewModelScope.launch {
            runningStateManager.runningState.collect { stateData ->
                checkTimeLimit(stateData.runTimeSeconds)

                _uiState.update {
                    it.copy(
                        isTracking = stateData.isTracking,
                        myRunTimeSeconds = stateData.runTimeSeconds,
                        myDistanceMeters = stateData.distanceMeters,
                        myPace = stateData.currentPace,
                        pathPoints = stateData.pathPoints
                    )
                }
            }
        }
    }

    // 자동 종료 체크
    private fun checkTimeLimit(currentSeconds: Long) {
        val targetSeconds = targetMinutes * 60

        if (currentSeconds >= targetSeconds && !_uiState.value.isFinished) {
            finishRunning()
        }
    }

    // 러닝 시작
    fun startRunning() {
        val intent = Intent(context, RunningService::class.java).apply { action = "ACTION_START" }
        context.startService(intent)
    }

    // 일시 정지
    fun pauseRunning() {
        val intent = Intent(context, RunningService::class.java).apply { action = "ACTION_PAUSE" }
        context.startService(intent)
    }

    // 러닝 종료 (시간 도달 시 자동 호출)
    private fun finishRunning() {
        // 1. 서비스 멈춤
        val intent = Intent(context, RunningService::class.java).apply { action = "ACTION_STOP" }
        context.startService(intent)

        // 2. 결과 서버 전송
        viewModelScope.launch {
            val finalState = runningStateManager.runningState.value

            // UseCase가 Start/End Time 계산 및 Request 생성 담당
            val result = saveRunningResultUseCase(finalState, targetMinutes)

            result.onSuccess { response ->
                _resultResponse = response // 결과 데이터 저장 (화면 이동 시 사용)
                _uiState.update { it.copy(isFinished = true) } // UI에 종료 알림
                runningStateManager.reset()
            }.onFailure { e ->
                _uiState.update { it.copy(errorMessage = "결과 전송 실패: ${e.message}") }
            }
        }
    }
}