package com.yourun_compose.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.yourun_compose.R
import com.yourun_compose.data.local.RunningStateManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class RunningService : Service() {

    @Inject
    lateinit var runningStateManager: RunningStateManager

    // 위치 관련 변수
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    // 코루틴 (타이머용)
    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private var timerJob: Job? = null

    // 러닝 데이터
    private var isTracking = false
    private var runTimeSeconds = 0L
    private var distanceMeters = 0
    private var lastLocation: Location? = null

    // 알림 상수
    private val CHANNEL_ID = "running_channel"
    private val NOTIFICATION_ID = 1

    // 명령어 상수
    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_STOP = "ACTION_STOP"
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // 위치 업데이트 콜백 정의
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                if (isTracking) {
                    result.locations.lastOrNull()?.let { location ->
                        updateLocation(location)
                    }
                }
            }
        }
    }

    // ViewModel에서 startService()를 호출하면 여기가 실행됨
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startRunning()
            ACTION_PAUSE -> pauseRunning()
            ACTION_STOP -> stopRunning()
        }
        return START_STICKY // 강제 종료 시 시스템이 서비스 재시작 시도
    }

    @SuppressLint("MissingPermission")
    private fun startRunning() {
        if (isTracking) return // 이미 실행 중이면 중복 시작 방지

        isTracking = true
        startForegroundService() // 알림 띄우기 (죽지 않게)

        // 위치 요청 설정
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 3000L) // 3초 주기
            .setMinUpdateDistanceMeters(2f) // 최소 2m 이동해야 갱신
            .build()

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

        startTimer()
    }

    private fun pauseRunning() {
        isTracking = false
        timerJob?.cancel() // 타이머 멈춤
        fusedLocationProviderClient.removeLocationUpdates(locationCallback) // GPS 멈춤

        // 상태 업데이트 (UI에 일시정지 상태 알림)
        updateStateManager(null)
        updateNotification("일시 정지됨")
    }

    private fun stopRunning() {
        isTracking = false
        try {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        } catch (e: Exception) { e.printStackTrace() }

        timerJob?.cancel()
        stopForeground(STOP_FOREGROUND_REMOVE) // 알림 제거
        stopSelf() // 서비스 자신 파괴
    }

    // 타이머 로직 (1초마다 실행)
    private fun startTimer() {
        timerJob?.cancel()
        timerJob = serviceScope.launch {
            while (isTracking) {
                delay(1000L)
                runTimeSeconds++

                // 위치가 안 바뀌어도 시간은 가니까 상태 업데이트
                updateStateManager(null)
                updateNotification(formatTime(runTimeSeconds))
            }
        }
    }

    private fun updateLocation(newLocation: Location) {
        // 거리 계산: 이전 위치가 있을 때만
        if (lastLocation != null) {
            val distanceGap = lastLocation!!.distanceTo(newLocation) // 리턴값: 미터(m)

            // 튐 방지: 너무 조금 움직였거나(0.5m 미만), 순간이동급(3초에 50m 이상)은 무시
            if (distanceGap > 0.5 && distanceGap < 50) {
                distanceMeters += distanceGap.toInt()
            }
        }
        lastLocation = newLocation

        // 상태 및 알림 업데이트
        updateStateManager(newLocation)
    }

    // RunningStateManager로 데이터 전송
    private fun updateStateManager(location: Location?) {
        runningStateManager.updateState(
            isTracking = isTracking,
            time = runTimeSeconds,
            distance = distanceMeters,
            pace = calculatePace(),
            location = location
        )
    }

    // 페이스 계산 (분/km)
    private fun calculatePace(): Double {
        if (distanceMeters == 0) return 0.0

        // 시간(분) / 거리(km)
        // = (초 / 60) / (미터 / 1000)
        // = (초 * 1000) / (미터 * 60)
        return (runTimeSeconds * 1000.0) / (distanceMeters * 60.0)
    }

    // 알림 생성 및 표시
    private fun startForegroundService() {
        val manager = getSystemService(NotificationManager::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Running Tracker",
                NotificationManager.IMPORTANCE_LOW // 알림 소리 없음
            )
            manager.createNotificationChannel(channel)
        }

        startForeground(NOTIFICATION_ID, createNotification("러닝 시작!"))
    }

    private fun updateNotification(contentText: String) {
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(NOTIFICATION_ID, createNotification("시간: $contentText"))
    }

    private fun createNotification(content: String): Notification {
        // TODO: 클릭 시 RunningActivity로 이동하는 PendingIntent 추가 가능
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("러닝 중 🏃")
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .build()
    }

    // 00:00:00 포맷터
    private fun formatTime(seconds: Long): String {
        val h = seconds / 3600
        val m = (seconds % 3600) / 60
        val s = seconds % 60
        return if (h > 0) String.format(Locale.getDefault(),"%d:%02d:%02d", h, m, s)
        else String.format(Locale.getDefault(),"%02d:%02d", m, s)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}