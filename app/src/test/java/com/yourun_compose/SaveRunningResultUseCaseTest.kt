package com.yourun_compose

import com.yourun_compose.data.local.RunningStateData
import com.yourun_compose.data.model.running.RunningResultRequest
import com.yourun_compose.data.repository.RunningRepository
import com.yourun_compose.domain.usecase.running.SaveRunningResultUseCase
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SaveRunningResultUseCaseTest {

    private val repository = mockk<RunningRepository>()

    private val useCase = SaveRunningResultUseCase(repository)

    // 러닝 결과를 서버로 보낼 때 데이터 변환이 올바르게 되어야 한다
    @Test
    fun testSaveRunningResult() = runTest {
        // Given
        val targetMinutes = 30 // 30분
        val runData = RunningStateData(
            runTimeSeconds = 600, // 10분 (600초)
            distanceMeters = 2500 // 2.5km
        )

        // Mocking: repository.sendRunningResult()가 호출될 때
        // 매개변수로 들어온 request를 'slot'이라는 통에 캡쳐(Capture)하겠다고 설정
        val slot = slot<RunningResultRequest>()
        coEvery { repository.sendRunningResult(capture(slot)) } returns Result.success(mockk(relaxed = true))

        // When
        useCase(runData, targetMinutes)

        // Then
        // 캡쳐된 요청 데이터를 꺼내서 확인
        val request = slot.captured

        // 검증 1: 목표 시간이 '분 -> 초'로 잘 변환되었는가? (30분 * 60 = 1800초)
        assertEquals(1800, request.targetTime)

        // 검증 2: 거리가 그대로 잘 들어갔는가?
        assertEquals(2500, request.totalDistance)

        // 검증 3: 시작/종료 시간 문자열이 생성되었는가?
        // UseCase 내부에서 LocalDateTime.now()를 쓰므로 정확한 값을 예측하긴 힘들지만,
        // 빈 값이 아니고, ISO 포맷의 특징인 "T"가 포함되어 있는지 확인
        assertTrue("시작 시간이 비어있음", request.startTime.isNotBlank())
        assertTrue("종료 시간이 비어있음", request.endTime.isNotBlank())
        assertTrue("ISO 포맷 아님(T 누락)", request.startTime.contains("T"))

        // 눈으로 확인하기 위해 출력
        println("=== [서버 전송용 데이터 변환 결과] ===")
        println("목표 시간: ${request.targetTime} (초)")
        println("이동 거리: ${request.totalDistance} (미터)")
        println("시작 시간: ${request.startTime}")
        println("종료 시간: ${request.endTime}")
    }
}