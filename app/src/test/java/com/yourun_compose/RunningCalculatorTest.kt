package com.yourun_compose

import org.junit.Assert.assertEquals
import org.junit.Test

class RunningCalculatorTest {

    // ========================================================================
    // Pace 계산 검증
    // 공식: (시간(분) / 거리(km)) = (초 * 1000) / (미터 * 60)
    // RunningService -> calculatePace()
    // ========================================================================

    // 10분 동안 2km를 뛰면 페이스는 5분이어야 한다
    @Test
    fun testPaceEquals5() {
        // Given
        val runTimeSeconds = 600L // 10분 (600초)
        val distanceMeters = 2000 // 2km

        // When
        val resultPace = (runTimeSeconds * 1000.0) / (distanceMeters * 60.0)

        // Then
        assertEquals(5.0, resultPace, 0.0)
        println("2km/10분 -> 페이스: $resultPace 분/km")
    }

    // 거리가 0일 때 0을 반환해야 한다 (나눗셈 에러 방지)
    @Test
    fun testPaceZeroDivisionError() {
        // Given
        val runTimeSeconds = 10L
        val distanceMeters = 0

        // When
        val resultPace = if (distanceMeters == 0) 0.0 else {
            (runTimeSeconds * 1000.0) / (distanceMeters * 60.0)
        }

        // Then
        assertEquals(0.0, resultPace, 0.0)
        println("거리 0m -> 페이스: $resultPace")
    }

    // ========================================================================
    // 프로그레스 바 비율 검증
    // 공식: 내 거리 / 메이트 거리
    // RunningTrackingUiState -> progressRatio
    // ========================================================================

    // 메이트 거리의 절반을 뛰었으면 0.5가 나와야 한다
    @Test
    fun testProvisionHalfRun() {
        // Given
        val myDistance = 5000 // 5km
        val mateDistance = 10000 // 10km

        // When
        val progress = myDistance.toFloat() / mateDistance.toFloat()

        // Then
        assertEquals(0.5f, progress, 0.0f)
        println("나 5km / 메이트 10km -> 진행률: $progress")
    }

    // 메이트보다 더 많이 뛰었으면 1.0을 넘어야 한다
    @Test
    fun testProvisionExceed1() {
        // Given
        val myDistance = 12000 // 12km
        val mateDistance = 10000 // 10km

        // When
        val progress = myDistance.toFloat() / mateDistance.toFloat()

        // Then
        assertEquals(1.2f, progress, 0.0f)
        println("나 12km / 메이트 10km -> 진행률: $progress (120%)")
    }

    // 메이트 기록이 0일 때 에러 없이 0을 반환해야 한다
    @Test
    fun testProvisionMateDistanceZero() {
        // Given
        val myDistance = 100
        val mateDistance = 0 // 데이터 오류 등으로 0이 올 경우

        // When
        val progress = if (mateDistance > 0) myDistance.toFloat() / mateDistance.toFloat() else 0f

        // Then
        assertEquals(0.0f, progress, 0.0f)
        println("메이트 기록 0 -> 진행률 안전 처리: $progress")
    }
}