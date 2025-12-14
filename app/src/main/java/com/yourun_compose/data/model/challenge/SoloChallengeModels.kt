package com.yourun_compose.data.model.challenge

import com.yourun_compose.data.model.type.Tendency
import com.google.gson.annotations.SerializedName

data class CreateSoloChallengeRequest(
    val endDate: String,
    val challengeDistance: String
)

data class CreatedSoloChallengeData(
    val challengeId: Long,
    val startDate: String,
    val endDate: String,
    val challengePeriod: Int,
    val tendency: String
)

data class SoloChallengeRes(
    val challengeId: Long,
    val challengeDistance: Int,
    val challengePeriod: Int,
    @SerializedName("challengeCreatorNickName") val challengeCreatorNickname: String,
    @SerializedName("challengeCreatorHashTags") val challengeCreatorHashtags: List<String>,
    val reward: Int,
    val challengeCreatorTendency: Tendency
)

data class PendingSoloChallengesData(
    val userId: Long,
    val userTendency: String,
    val userCrewReward: Long,
    val userSoloReward: Long,

    @SerializedName("soloChallengeResList")
    val challenges: List<SoloChallengeRes>
)

data class SoloChallengeProgressData(
    val challengePeriod: Int,
    val challengeDistance: Int,
    val dayCount: Int,
    val now: String,
    val dayResultInfo: List<DayResult>,
    val challengeMateInfo: ChallengeMateProgressInfo,
    val isSuccess: Boolean,
    val tendency: String
)

data class SoloChallengeDetailData(
    val startDate: String,
    val endDate: String,
    val challengeDistance: Int,
    val challengePeriod: Int,
    val challengeCreatorNickName: String,

    @SerializedName("challengeCreatorHashTags")
    val challengeCreatorHashtags: List<String>,

    val tendency: Tendency,
    val reward: Int,
    val countDay: Int
)

data class SoloChallengeResultData(
    val challengePeriod: Int,
    val challengeDistance: Int,
    val dayCount: Int,
    val challengeMateInfo: SoloChallengeMateData,
    val userIsSuccess: Boolean,
    val userTendency: Tendency
)

data class SoloChallengeMateData(
    val challengeMateNickName: String,
    val challengeMateTendency: Tendency,
    val successDay: Int,
    val challengeMateIsSuccess: Boolean,
    val distance: Int
)

data class DayResult(val day: Int, val distance: Int)

data class ChallengeMateProgressInfo(
    val challengeMateNickName: String,
    val challengeMateTendency: String,
    val successDay: Int,
    val isSuccess: Boolean,
    val distance: Int
)

data class SoloMatchingData(
    val period: Int,
    val challengeDistance: Int,

    val userTendency: Tendency,
    val userNickName: String,
    val userCountDay: Int,
    val userHashTags: List<String>,

    val challengeMateTendency: Tendency,
    val challengeMateNickName: String,
    val challengeMateCountDay: Int,
    val challengeMateHashTags: List<String>
)

data class SoloJoinData(
    val challengeId: Long,
    val userId: Long
)
