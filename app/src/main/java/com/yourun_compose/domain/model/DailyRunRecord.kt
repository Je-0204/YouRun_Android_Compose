package com.yourun_compose.domain.model

import java.util.Locale

data class DailyRunRecord(
    val runTimeMinutes: Int = 0,
    val runDistanceKm: Double = 0.0,
    val isPersonalBest: Boolean = false,
    val hasRecord: Boolean = false
) {
    val calculatedPace: String
        get() {
            if (runDistanceKm <= 0.0 || runTimeMinutes <= 0) return "0'00''"

            // 페이스 = 시간(분) / 거리(km)
            val paceVal = runTimeMinutes.toDouble() / runDistanceKm
            val minutes = paceVal.toInt()
            val seconds = ((paceVal - minutes) * 60).toInt()

            return String.format(Locale.US, "%d'%02d''", minutes, seconds)
        }
}