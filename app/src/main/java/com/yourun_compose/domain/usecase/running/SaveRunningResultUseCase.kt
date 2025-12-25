package com.yourun_compose.domain.usecase.running

import com.yourun_compose.data.local.RunningStateData
import com.yourun_compose.data.model.running.RunningCreationData
import com.yourun_compose.data.model.running.RunningResultRequest
import com.yourun_compose.data.repository.RunningRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class SaveRunningResultUseCase @Inject constructor(
    private val repository: RunningRepository
) {
    suspend operator fun invoke(data: RunningStateData, targetTimeMinutes: Int): Result<RunningCreationData> {

        val now = LocalDateTime.now()
        val endTimeStr = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) // "2023-12-25T14:30:00"

        // 시작 시간 = 현재 - 뛴 시간(초)
        val startTime = now.minusSeconds(data.runTimeSeconds)
        val startTimeStr = startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        val request = RunningResultRequest(
            targetTime = targetTimeMinutes * 60,
            startTime = startTimeStr,
            endTime = endTimeStr,
            totalDistance = data.distanceMeters
        )

        return repository.sendRunningResult(request)
    }
}