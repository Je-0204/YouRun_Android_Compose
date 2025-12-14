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