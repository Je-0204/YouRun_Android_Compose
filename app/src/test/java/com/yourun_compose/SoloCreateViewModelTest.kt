package com.yourun_compose

import com.yourun_compose.domain.usecase.challenge.CreateSoloChallengeUseCase
import com.yourun_compose.ui.viewmodel.solo.SoloCreateViewModel
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class SoloCreateViewModelTest {

    private val useCase = mockk<CreateSoloChallengeUseCase>()
    private lateinit var viewModel: SoloCreateViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = SoloCreateViewModel(useCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // 날짜 선택 순서가 잘못되어도(종료일이 더 빠름) 자동으로 정렬되어야 한다
    @Test
    fun testDateError() {
        // Given
        val laterDate = LocalDate.of(2025, 12, 25)
        val earlierDate = LocalDate.of(2025, 12, 24)

        // When: (start에 늦은 날짜, end에 빠른 날짜를 넣음)
        viewModel.updateDateRange(start = laterDate, end = earlierDate)

        // Then: 자동으로 start가 24일, end가 25일로 잡혀야 함
        assertEquals(earlierDate, viewModel.uiState.value.startDate)
        assertEquals(laterDate, viewModel.uiState.value.endDate)

        println("날짜 자동 정렬 테스트 통과")
    }
}