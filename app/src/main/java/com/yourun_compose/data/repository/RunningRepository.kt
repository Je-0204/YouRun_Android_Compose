package com.yourun_compose.data.repository

import com.yourun_compose.data.api.ApiService
import com.yourun_compose.data.model.running.RunningCreationData
import com.yourun_compose.data.model.running.RunningResultData
import com.yourun_compose.data.model.running.RunningResultRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RunningRepository @Inject constructor(
    private val apiService: ApiService
) : BaseRepository() {

    // Get Mate Running Record
    suspend fun getMateRunningRecord(mateId: Long): Result<Int> {
        return safeApiCall { apiService.getRunningDetail(mateId) }
            .map { response -> response.totalDistance }
    }

    // Send Running Result
    suspend fun sendRunningResult(request: RunningResultRequest): Result<RunningCreationData> {
        return safeApiCall { apiService.sendRunningResult(request) }
    }

    // Get Running Detail
    suspend fun getRunningDetail(runningId: Long): Result<RunningResultData> {
        return safeApiCall { apiService.getRunningDetail(runningId) }
    }

    // Get Running Stats for months
    suspend fun getRunningStats(year: Int, month: Int): Result<List<RunningResultData>> {
        return safeApiCall { apiService.getRunningStats(year, month) }
    }
}