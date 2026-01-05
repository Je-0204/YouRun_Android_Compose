package com.yourun_compose

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class YourunApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // 앱이 켜질 때 알림 채널 생성 (러닝 서비스용)
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "running_channel"
            val channelName = "러닝 기록 알림"
            val channelDescription = "러닝 중 거리와 시간을 표시합니다."

            // IMPORTANCE_LOW: 알림음이 울리지 않음 (1초마다 갱신되므로 조용해야 함)
            val importance = NotificationManager.IMPORTANCE_LOW

            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}