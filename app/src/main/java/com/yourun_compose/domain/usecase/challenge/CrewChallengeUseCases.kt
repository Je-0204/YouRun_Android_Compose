package com.yourun_compose.domain.usecase.challenge

import com.yourun_compose.data.model.challenge.CreateCrewChallengeRequest
import com.yourun_compose.data.repository.ChallengeRepository
import javax.inject.Inject

// Create
class CreateCrewChallengeUseCase @Inject constructor(private val repository: ChallengeRepository) {
    suspend operator fun invoke(name: String, slogan: String, endDate: String) =
        repository.createCrewChallenge(CreateCrewChallengeRequest(name, slogan, endDate))
}

// Get Info
class GetCrewInfoUseCase @Inject constructor(private val repository: ChallengeRepository) {
    suspend fun getPendingList() = repository.getPendingCrewChallenges()
    suspend fun getDetail(id: Long) = repository.getCrewChallengeDetail(id)
}

// Join
class JoinCrewChallengeUseCase @Inject constructor(private val repository: ChallengeRepository) {
    suspend operator fun invoke(id: Long) = repository.joinCrewChallenge(id)
}

// Get Progress
class GetCrewProgressUseCase @Inject constructor(private val repository: ChallengeRepository) {
    suspend fun getProgress() = repository.getCrewChallengeProgress()
    suspend fun getMatchingInfo() = repository.getCrewMatchingData()
}

// Get Result
class GetCrewResultUseCase @Inject constructor(private val repository: ChallengeRepository) {
    suspend fun getResult() = repository.getCrewChallengeResult()
    suspend fun getContribution() = repository.getCrewContributionResult()
}