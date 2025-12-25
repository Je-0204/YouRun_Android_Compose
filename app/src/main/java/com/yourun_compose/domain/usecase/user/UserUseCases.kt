package com.yourun_compose.domain.usecase.user

import com.yourun_compose.data.model.user.UpdateUserRequest
import com.yourun_compose.data.repository.UserRepository
import javax.inject.Inject

// Get My Profile
class GetMyProfileUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke() = repository.getMyPageData()
}

// Update Profile
class UpdateProfileUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(nickname: String, tags: List<String>) =
        repository.updateUserInfo(UpdateUserRequest(nickname, tags))
}

// Manage Mate
class ManageMateUseCase @Inject constructor(private val repository: UserRepository) {
    suspend fun getMates() = repository.getMates()
    suspend fun addMate(mateId: Long) = repository.addMate(mateId)
    suspend fun deleteMate(mateId: Long) = repository.deleteMate(mateId)
    suspend fun getRecommendMates() = repository.getRecommendMates()
}