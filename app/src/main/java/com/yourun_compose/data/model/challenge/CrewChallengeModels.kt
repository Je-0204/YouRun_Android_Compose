package com.yourun_compose.data.model.challenge

import com.google.gson.annotations.SerializedName

data class CreateCrewChallengeRequest(
    val crewName: String,
    val slogan: String,
    val endDate: String
)

data class CreatedCrewChallengeData(
    val challengeId: Long,
    val crewName: String,
    val startDate: String,
    val endDate: String,
    val challengePeriod: Int,
    val tendency: String
)

data class CrewChallengeRes(
    val challengeId: Long,
    val crewName: String,
    val challengePeriod: Int,
    val remaining: Int,
    val reward: Int,
    val participantIdsInfo: List<MemberTendencyInfo>
)

data class CrewChallengeProgressData(
    val challengePeriod: Int,
    val myCrewName: String,
    val myCrewSlogan: String,
    val myCrewMembers: List<CrewProgressMember>,
    val myCrewDistance: Double,
    val matchedCrewName: String,
    val matchedCrewSlogan: String,
    val matchedCrewCreatorTendency: String,
    val matchedCrewDistance: Double,
    val now: String
)

data class CrewProgressMember(
    val userId: Int,
    val runningDistance: Double,
    val userTendency: String
)

data class CrewChallengeResultData(
    val challengePeriod: Int = 0,
    val myCrewName: String = "",
    val beforeDistance: Double = 0.0,
    val userDistance: Double = 0.0,
    val afterDistance: Double = 0.0,
    val matchedCrewName: String = "",
    val matchedCrewCreator: String = "",
    val matchedCrewDistance: Double = 0.0
)

data class CrewMatchingData(
    val period: Int,
    val crewName: String,
    val myCrewSlogan: String,

    @SerializedName("myParticipantIdsInfo")
    val myParticipants: List<MemberTendencyInfo>,

    val matchedCrewName: String,
    val matchedCrewSlogan: String,

    @SerializedName("matchedParticipantIdsInfo")
    val matchedParticipants: List<MemberTendencyInfo>
)

data class CrewJoinData(
    val challengeId: Long,
    val participantIds: List<Long>
)