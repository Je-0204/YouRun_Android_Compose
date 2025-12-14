package com.yourun_compose.domain.usecase

import com.yourun_compose.data.local.SessionManager
import com.yourun_compose.data.repository.ChallengeRepository
import javax.inject.Inject

class CheckMatchingUseCase @Inject constructor(
    private val repository: ChallengeRepository,
    private val sessionManager: SessionManager
) {
    // 반환값: 이동해야 할 화면 타입 ("SOLO", "CREW", or null)
    suspend operator fun invoke(): String? {
        if (sessionManager.isAlreadyChecked()) {
            return null
        }

        val result = repository.checkMatchingStatus()

        sessionManager.setChecked()

        return if (result.isSuccess) {
            val data = result.getOrNull()
            when {
                data?.isSoloChallengeMatching == true -> "SOLO"
                data?.isCrewChallengeMatching == true -> "CREW"
                else -> null
            }
        } else {
            null
        }
    }
}