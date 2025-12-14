package com.yourun_compose.data.remote

import com.yourun_compose.data.model.auth.*
import com.yourun_compose.data.model.challenge.*
import com.yourun_compose.data.model.common.BaseResponse
import com.yourun_compose.data.model.running.*
import com.yourun_compose.data.model.user.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ========================================================================
    // Auth
    // ========================================================================

    // Login
    @POST("users/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<BaseResponse<TokenData>>

    // Kakao Login
    @GET("users/kakao-login")
    suspend fun kakaoLogin(): Response<BaseResponse<TokenData>>

    // Kakao User Initial Info
    @POST("users/initialize")
    suspend fun initializeUser(
        @Body request: UserInitRequest
    ): Response<BaseResponse<Boolean>>

    // Sign up
    // 올바른 형식의 이메일 주소여야 합니다.
    // 비밀번호는 영문과 숫자를 포함하여 8~10자이어야 합니다.
    // 닉네임은 띄어쓰기 없이 한글 2~4자만 가능합니다.
    @POST("users")
    suspend fun signUp(
        @Body request: SignUpRequest
    ): Response<BaseResponse<Boolean>>

    // Check Email Duplicate
    @POST("users/duplicate")
    suspend fun checkEmailDuplicate(
        @Query("email") email: String
    ): Response<BaseResponse<Boolean>>

    // Check Nickname Duplicate
    @POST("users/check-nickname")
    suspend fun checkNicknameDuplicate(
        @Query("nickname") nickname: String
    ): Response<BaseResponse<Boolean>>


    // ========================================================================
    // User & Mate
    // ========================================================================

    // Get My Page Data
    @GET("mypage")
    suspend fun getMyPageData(): Response<BaseResponse<UserInfo>>

    // Update User Information
    @PATCH("mypage")
    suspend fun updateUserInfo(
        @Body request: UpdateUserRequest
    ): Response<BaseResponse<UserInfo>>

    // Get Mates
    @GET("users/mates")
    suspend fun getMates(): Response<BaseResponse<List<UserMateInfo>>>

    // Add Mate
    @POST("users/mates/{mateId}")
    suspend fun addMate(
        @Path("mateId") mateId: Long
    ): Response<BaseResponse<Boolean>>

    // Delete Mate
    @DELETE("users/mates/{mateId}")
    suspend fun deleteMate(
        @Path("mateId") mateId: Long
    ): Response<BaseResponse<Boolean>>

    // Recommend Mates
    @GET("users/mates/recommend")
    suspend fun getRecommendMates(): Response<BaseResponse<List<UserMateInfo>>>


    // ========================================================================
    // Home & Challenge Common
    // ========================================================================

    // Home Challenge Information
    @GET("users/home/challenges")
    suspend fun getHomeChallengeInfo(): Response<BaseResponse<HomeChallengeData>>

    // Check Matching Status
    @GET("users/challenges/check-matching")
    suspend fun checkMatchingStatus(): Response<BaseResponse<ChallengeMatchingResponse>>


    // ========================================================================
    // Running
    // ========================================================================

    // Send Running Result
    @POST("users/runnings")
    suspend fun sendRunningResult(
        @Body request: RunningResultRequest
    ): Response<BaseResponse<RunningCreationData>>

    // Get Running Detail
    @GET("users/runnings/{id}")
    suspend fun getRunningDetail(
        @Path("id") runningId: Long
    ): Response<BaseResponse<RunningResultData>>

    // Get Running Stats for months
    @GET("users/runnings/{year}/{month}")
    suspend fun getRunningStats(
        @Path("year") year: Int,
        @Path("month") month: Int
    ): Response<BaseResponse<List<RunningResultData>>>


    // ========================================================================
    // Solo Challenge
    // ========================================================================

    // Create Solo Challenge
    @POST("challenges/solo")
    suspend fun createSoloChallenge(
        @Body request: CreateSoloChallengeRequest
    ): Response<BaseResponse<CreatedSoloChallengeData>>

    // Get Pending Solo Challenges
    @GET("challenges/solo/pending")
    suspend fun getPendingSoloChallenges(): Response<BaseResponse<PendingSoloChallengesData>>

    // Get Solo Challenge Detail
    @GET("challenges/solo/pending/{challengeId}")
    suspend fun getSoloChallengeDetail(
        @Path("challengeId") challengeId: Long
    ): Response<BaseResponse<SoloChallengeDetailData>>

    // Join Solo Challenge
    @POST("challenges/solo/{challengeId}/join")
    suspend fun joinSoloChallenge(
        @Path("challengeId") challengeId: Long
    ): Response<BaseResponse<SoloJoinData>>

    // Get Solo Challenge Progress
    @GET("challenges/solo/progress")
    suspend fun getSoloChallengeProgress(): Response<BaseResponse<SoloChallengeProgressData>>

    // Get Solo Matching Data
    @GET("challenges/solo/matching")
    suspend fun getSoloMatchingInfo(): Response<BaseResponse<SoloMatchingData>>

    // Get Solo Challenge Result
    @GET("challenges/solo/running-result")
    suspend fun getSoloChallengeResult(): Response<BaseResponse<SoloChallengeResultData>>


    // ========================================================================
    // Crew Challenge
    // ========================================================================

    // Create Crew Challenge
    @POST("challenges/crew")
    suspend fun createCrewChallenge(
        @Body request: CreateCrewChallengeRequest
    ): Response<BaseResponse<CreatedCrewChallengeData>>

    // Get Pending Crew Challenges
    @GET("challenges/crew/pending")
    suspend fun getPendingCrewChallenges(): Response<BaseResponse<List<CrewChallengeRes>>>

    // Get Crew Challenge Detail
    @GET("challenges/crew/pending/{challengeId}")
    suspend fun getCrewChallengeDetail(
        @Path("challengeId") challengeId: Long
    ): Response<BaseResponse<CrewChallengeRes>>

    // Join Crew Challenge
    @POST("challenges/crew/{challengeId}/join")
    suspend fun joinCrewChallenge(
        @Path("challengeId") challengeId: Long
    ): Response<BaseResponse<CrewJoinData>>

    // Get Crew Challenge Progress
    @GET("challenges/crew/detail-progress")
    suspend fun getCrewChallengeProgress(): Response<BaseResponse<CrewChallengeProgressData>>

    // Get Crew Matching Data
    @GET("challenges/crew/matching")
    suspend fun getCrewMatchingData(): Response<BaseResponse<CrewMatchingData>>

    // Get Crew Challenge Result
    @GET("challenges/crew/running-result")
    suspend fun getCrewChallengeResult(): Response<BaseResponse<CrewChallengeResultData>>

    // Get Crew Contribution Result
    @GET("challenges/crew/ranking-result")
    suspend fun getCrewContributionResult(): Response<BaseResponse<List<ResultContributionResponse>>>

}