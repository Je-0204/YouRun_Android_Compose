package com.yourun_compose.domain.usecase.challenge

import com.yourun_compose.data.model.challenge.CreateSoloChallengeRequest
import com.yourun_compose.data.repository.ChallengeRepository
import javax.inject.Inject

// Create
class CreateSoloChallengeUseCase @Inject constructor(private val repository: ChallengeRepository) {
    suspend operator fun invoke(endDate: String, distance: String) =
        repository.createSoloChallenge(CreateSoloChallengeRequest(endDate, distance))
}

// Get Info
class GetSoloInfoUseCase @Inject constructor(private val repository: ChallengeRepository) {
    suspend fun getPendingList() = repository.getPendingSoloChallenges()
    suspend fun getDetail(id: Long) = repository.getSoloChallengeDetail(id)
}

// Join
class JoinSoloChallengeUseCase @Inject constructor(private val repository: ChallengeRepository) {
    suspend operator fun invoke(id: Long) = repository.joinSoloChallenge(id)
}

// Get Progress
class GetSoloProgressUseCase @Inject constructor(private val repository: ChallengeRepository) {
    suspend fun getProgress() = repository.getSoloChallengeProgress()
    suspend fun getMatchingInfo() = repository.getSoloMatchingInfo()
}

// Get Result
class GetSoloResultUseCase @Inject constructor(private val repository: ChallengeRepository) {
    suspend operator fun invoke() = repository.getSoloChallengeResult()
}