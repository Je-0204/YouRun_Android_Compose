package com.yourun_compose

import android.content.Context
import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import com.yourun_compose.data.local.RunningStateData
import com.yourun_compose.data.local.RunningStateManager
import com.yourun_compose.domain.usecase.running.GetMateRecordUseCase
import com.yourun_compose.domain.usecase.running.SaveRunningResultUseCase
import com.yourun_compose.ui.viewmodel.running.RunningTrackingViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RunningTrackingViewModelTest {

    private val context = mockk<Context>(relaxed = true)
    private val runningStateManager = mockk<RunningStateManager>(relaxed = true)
    private val getMateRecordUseCase = mockk<GetMateRecordUseCase>(relaxed = true)
    private val saveRunningResultUseCase = mockk<SaveRunningResultUseCase>(relaxed = true)

    private val savedStateHandle = SavedStateHandle(mapOf("time" to "30", "mateId" to "100"))
    private val fakeRunningStateFlow = MutableStateFlow(RunningStateData())

    private lateinit var viewModel: RunningTrackingViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        // Intent 생성자 모킹
        mockkConstructor(Intent::class)
        
        // setAction이 자기 자신(Intent)을 반환하도록 설정하여 apply 문법 지원
        every { anyConstructed<Intent>().setAction(any()) } answers {
            it.invocation.self as Intent
        }

        every { runningStateManager.runningState } returns fakeRunningStateFlow.asStateFlow()

        coEvery { getMateRecordUseCase(any()) } returns Result.success(5000)
        coEvery { saveRunningResultUseCase(any(), any()) } returns Result.success(mockk(relaxed = true))

        viewModel = RunningTrackingViewModel(
            context,
            runningStateManager,
            getMateRecordUseCase,
            saveRunningResultUseCase,
            savedStateHandle
        )
    }

    @After
    fun tearDown() {
        unmockkConstructor(Intent::class)
        Dispatchers.resetMain()
    }

    @Test
    fun testFinishRunning() = runTest {
        // Given
        fakeRunningStateFlow.value = RunningStateData(runTimeSeconds = 1799, isTracking = true)
        assertEquals(false, viewModel.uiState.value.isFinished)

        // When: 30분(1800초) 도달
        fakeRunningStateFlow.value = RunningStateData(runTimeSeconds = 1800, isTracking = true)

        // Then
        assertTrue("30분이 되면 종료 상태여야 함", viewModel.uiState.value.isFinished)
        coVerify(exactly = 1) { saveRunningResultUseCase(any(), 30) }

        // Intent 검증: 생성된 Intent에 ACTION_STOP이 설정되었는가?
        verify { anyConstructed<Intent>().setAction("ACTION_STOP") }
        verify { context.startService(any()) }
    }

    @Test
    fun testMateRecord() = runTest {
        advanceUntilIdle()
        assertEquals(5000, viewModel.uiState.value.mateRecordDistance)
    }
}
