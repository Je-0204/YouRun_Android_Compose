package com.yourun_compose.data.repository

import com.yourun_compose.data.api.ApiService
import com.yourun_compose.data.model.user.UpdateUserRequest
import com.yourun_compose.data.model.user.UserInfo
import com.yourun_compose.data.model.user.UserMateInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val apiService: ApiService
) : BaseRepository() {

    // Get My Page Data
    suspend fun getMyPageData(): Result<UserInfo> {
        return safeApiCall { apiService.getMyPageData() }
    }

    // Update User Information
    suspend fun updateUserInfo(request: UpdateUserRequest): Result<UserInfo> {
        return safeApiCall { apiService.updateUserInfo(request) }
    }

    // Get Mates
    suspend fun getMates(): Result<List<UserMateInfo>> {
        return safeApiCall { apiService.getMates() }
    }

    // Add Mate
    suspend fun addMate(mateId: Long): Result<Boolean> {
        return safeApiCall { apiService.addMate(mateId) }
    }

    // Delete Mate
    suspend fun deleteMate(mateId: Long): Result<Boolean> {
        return safeApiCall { apiService.deleteMate(mateId) }
    }

    // Recommend Mates
    suspend fun getRecommendMates(): Result<List<UserMateInfo>> {
        return safeApiCall { apiService.getRecommendMates() }
    }
}