package com.yourun_compose.domain.usecase.common

import com.yourun_compose.data.local.SessionManager
import com.yourun_compose.data.repository.ChallengeRepository
import javax.inject.Inject

// Get Home Data
class GetHomeDataUseCase @Inject constructor(private val repository: ChallengeRepository) {
    suspend operator fun invoke() = repository.getHomeChallengeInfo()
}

// Check Matching
class CheckMatchingUseCase @Inject constructor(
    private val repository: ChallengeRepository,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(): String? {
        if (sessionManager.isAlreadyChecked()) return null

        val result = repository.checkMatchingStatus()
        sessionManager.setChecked()

        return if (result.isSuccess) {
            val data = result.getOrNull()
            when {
                data?.isSoloChallengeMatching == true -> "SOLO"
                data?.isCrewChallengeMatching == true -> "CREW"
                else -> null
            }
        } else null
    }
}