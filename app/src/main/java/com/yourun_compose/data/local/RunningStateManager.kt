package com.yourun_compose.data.local

import android.location.Location
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

data class RunningStateData(
    val isTracking: Boolean = false,
    val runTimeSeconds: Long = 0L,
    val distanceMeters: Int = 0,
    val currentPace: Double = 0.0,   // (minute/km)
    val pathPoints: List<Location> = emptyList()
)

@Singleton
class RunningStateManager @Inject constructor() {
    private val _runningState = MutableStateFlow(RunningStateData())
    val runningState = _runningState.asStateFlow()

    fun updateState(isTracking: Boolean, time: Long, distance: Int, pace: Double, location: Location?) {
        _runningState.update { current ->
            val newPath = if (location != null && isTracking) current.pathPoints + location else current.pathPoints
            current.copy(
                isTracking = isTracking,
                runTimeSeconds = time,
                distanceMeters = distance,
                currentPace = pace,
                pathPoints = newPath
            )
        }
    }

    fun reset() {
        _runningState.value = RunningStateData()
    }
}