package com.yourun_compose.data.repository

import com.yourun_compose.data.api.ApiService
import com.yourun_compose.data.model.challenge.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChallengeRepository @Inject constructor(
    private val apiService: ApiService
) : BaseRepository() {

    // ========================================================================
    // Home & Common
    // ========================================================================

    // Home Challenge Information
    suspend fun getHomeChallengeInfo(): Result<HomeChallengeData> {
        return safeApiCall { apiService.getHomeChallengeInfo() }
    }

    // Check Matching Status
    suspend fun checkMatchingStatus(): Result<ChallengeMatchingResponse> {
        return safeApiCall { apiService.checkMatchingStatus() }
    }


    // ========================================================================
    // Solo Challenge
    // ========================================================================

    // Create Solo Challenge
    suspend fun createSoloChallenge(request: CreateSoloChallengeRequest): Result<CreatedSoloChallengeData> {
        return safeApiCall { apiService.createSoloChallenge(request) }
    }

    // Get Pending Solo Challenges
    suspend fun getPendingSoloChallenges(): Result<PendingSoloChallengesData> {
        return safeApiCall { apiService.getPendingSoloChallenges() }
    }

    // Get Solo Challenge Detail
    suspend fun getSoloChallengeDetail(challengeId: Long): Result<SoloChallengeDetailData> {
        return safeApiCall { apiService.getSoloChallengeDetail(challengeId) }
    }

    // Join Solo Challenge
    suspend fun joinSoloChallenge(challengeId: Long): Result<SoloJoinData> {
        return safeApiCall { apiService.joinSoloChallenge(challengeId) }
    }

    // Get Solo Challenge Progress
    suspend fun getSoloChallengeProgress(): Result<SoloChallengeProgressData> {
        return safeApiCall { apiService.getSoloChallengeProgress() }
    }

    // Get Solo Matching Data
    suspend fun getSoloMatchingInfo(): Result<SoloMatchingData> {
        return safeApiCall { apiService.getSoloMatchingInfo() }
    }

    // Get Solo Challenge Result
    suspend fun getSoloChallengeResult(): Result<SoloChallengeResultData> {
        return safeApiCall { apiService.getSoloChallengeResult() }
    }


    // ========================================================================
    // Crew Challenge
    // ========================================================================

    // Create Crew Challenge
    suspend fun createCrewChallenge(request: CreateCrewChallengeRequest): Result<CreatedCrewChallengeData> {
        return safeApiCall { apiService.createCrewChallenge(request) }
    }

    // Get Pending Crew Challenges
    suspend fun getPendingCrewChallenges(): Result<List<CrewChallengeRes>> {
        return safeApiCall { apiService.getPendingCrewChallenges() }
    }

    // Get Crew Challenge Detail
    suspend fun getCrewChallengeDetail(challengeId: Long): Result<CrewChallengeRes> {
        return safeApiCall { apiService.getCrewChallengeDetail(challengeId) }
    }

    // Join Crew Challenge
    suspend fun joinCrewChallenge(challengeId: Long): Result<CrewJoinData> {
        return safeApiCall { apiService.joinCrewChallenge(challengeId) }
    }

    // Get Crew Challenge Progress
    suspend fun getCrewChallengeProgress(): Result<CrewChallengeProgressData> {
        return safeApiCall { apiService.getCrewChallengeProgress() }
    }

    // Get Crew Matching Data
    suspend fun getCrewMatchingData(): Result<CrewMatchingData> {
        return safeApiCall { apiService.getCrewMatchingData() }
    }

    // Get Crew Challenge Result
    suspend fun getCrewChallengeResult(): Result<CrewChallengeResultData> {
        return safeApiCall { apiService.getCrewChallengeResult() }
    }

    // Get Crew Contribution Result
    suspend fun getCrewContributionResult(): Result<List<ResultContributionResponse>> {
        return safeApiCall { apiService.getCrewContributionResult() }
    }
}