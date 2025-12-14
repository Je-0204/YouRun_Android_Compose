package com.yourun_compose.data.model.challenge

import com.yourun_compose.data.model.type.Tendency
import com.google.gson.annotations.SerializedName

data class HomeChallengeData(
    val tendency: String,
    val homeNickName: String,
    val soloReward: Long,
    val crewReward: Long,
    val soloChallenge: UserSoloChallengeInfo?,
    val crewChallenge: UserCrewChallengeInfo?
)

data class UserSoloChallengeInfo(
    val challengeId: Long? = null
)

data class UserCrewChallengeInfo(
    val challengeId: Long,
    val crewName: String,
    val challengeStatus: String,
    val challengePeriod: Int,
    val myParticipantIdsInfo: List<MemberTendencyInfo>,
    val crewDayCount: Int,
    val crewStartDate: String
)

data class MemberTendencyInfo(
    val userId: Long,
    @SerializedName("memberTendency")
    val memberTendencyRaw: String
) {
    val memberTendency: Tendency
        get() = Tendency.fromValue(memberTendencyRaw)
}

data class ChallengeMatchingResponse(
    val isSoloChallengeMatching: Boolean,
    val isCrewChallengeMatching: Boolean
)

data class ChallengeResultResponse(
    val challengePeriod: Int,
    val challengeDistance: Int,
    val dayCount: Int,
    val challengeMateInfo: ChallengeMateInfo,
    val userIsSuccess: Boolean,
    val userTendency: String
)

data class ChallengeMateInfo(
    val challengeMateNickName: String,
    val challengeMateTendency: String,
    val successDay: Int,
    val challengeMateIsSuccess: Boolean,
    val distance: Int
)

data class ResultContributionResponse(
    val challengePeriod: Int,
    val reward: Int,
    val crewName: String,
    val crewMemberDistance: List<CrewMember>,
    val mvpId: Int,
    val win: Boolean
)

data class CrewMember(
    val userId: Int,
    val runningDistance: Double,
    val userTendency: String,
    val rank: Int
)