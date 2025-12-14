package com.yourun_compose.data.model.running

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class RunningResultRequest(
    val targetTime: Int,
    val startTime: String,
    val endTime: String,
    val totalDistance: Int
)

@Parcelize
data class RunningResultData(
    val id: Long,
    val userName: String,
    val startTime: String,
    val endTime: String,
    val totalDistance: Int,
    val totalTime: Int,
    val pace: Int,
    val isSoloChallengeInProgress: Boolean,
    val isCrewChallengeInProgress: Boolean
) : Parcelable

@Parcelize
data class RunningCreationData(
    val id: Long,
    val totalDistance: Int,
    val totalTime: Int,     // sec
    val pace: Int,          // minute/km
    val createdAt: String   // ISO 8601 String
) : Parcelable
