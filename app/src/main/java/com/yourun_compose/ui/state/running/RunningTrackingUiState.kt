package com.yourun_compose.ui.state.running

import android.location.Location
import java.util.Locale

data class RunningTrackingUiState(
    val targetTimeMinutes: Int = 30,
    val mateName: String = "",
    val mateRecordDistance: Int = 1,

    val myRunTimeSeconds: Long = 0L,
    val myDistanceMeters: Int = 0,
    val myPace: Double = 0.0,
    val pathPoints: List<Location> = emptyList(),
    val isTracking: Boolean = false,

    val isFinished: Boolean = false,
    val errorMessage: String? = null
) {
    val progressRatio: Float
        get() {
            if (mateRecordDistance <= 0) return 0f
            return myDistanceMeters.toFloat() / mateRecordDistance.toFloat()
        }

    val remainingTimeSeconds: Long
        get() = ((targetTimeMinutes * 60) - myRunTimeSeconds).coerceAtLeast(0)

    // 화면 표시용 포맷 (00:00) - 남은 시간 보여주기
    val formattedRemainingTime: String
        get() {
            val min = remainingTimeSeconds / 60
            val sec = remainingTimeSeconds % 60
            return String.format(Locale.getDefault(),"%02d:%02d", min, sec)
        }

    // 화면용: 이동 거리 (km, 소수점 첫째자리)
    val formattedDistanceKm: String
        get() = String.format(Locale.getDefault(),"%.1f", myDistanceMeters / 1000f)

    // 페이스 포맷 (0'00'')
    val formattedPace: String
        get() {
            if (myPace == 0.0) return "-'--''"
            val pMin = myPace.toInt()
            val pSec = ((myPace - pMin) * 60).toInt()
            return String.format(Locale.getDefault(),"%d'%02d''", pMin, pSec)
        }
}