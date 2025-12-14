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

data class DayResult(val day: Int, val distance: Int)

data class ChallengeMateProgressInfo(
    val challengeMateNickName: String,
    val challengeMateTendency: String,
    val successDay: Int,
    val isSuccess: Boolean,
    val distance: Int
)
