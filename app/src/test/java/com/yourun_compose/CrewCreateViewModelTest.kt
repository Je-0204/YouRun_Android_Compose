package com.yourun_compose

import com.yourun_compose.domain.usecase.challenge.CreateCrewChallengeUseCase
import com.yourun_compose.ui.viewmodel.crew.CrewCreateViewModel
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class CrewCreateViewModelTest {

    private val useCase = mockk<CreateCrewChallengeUseCase>()

    private lateinit var viewModel: CrewCreateViewModel

    @Before
    fun setup() {
        // ViewModel 테스트를 위해 Main 쓰레드를 테스트용으로 교체
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = CrewCreateViewModel(useCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ========================================================================
    // 크루명 유효성 검사
    // 규칙: 한글 2~5자, 공백(띄어쓰기) 불가
    // ========================================================================

    // 정상적인 한글 2~5자는 통과해야 한다
    @Test
    fun testCrewNameKorean() {
        // Given & When
        viewModel.updateName("러닝") // 2자

        // Then
        assertTrue("러닝은 유효해야 함", viewModel.uiState.value.isNameValid)

        // Given & When
        viewModel.updateName("러닝크루임") // 5자

        // Then
        assertTrue("러닝크루임은 유효해야 함", viewModel.uiState.value.isNameValid)
    }

    // 공백이 있거나 글자수가 맞지 않으면 실패해야 한다
    @Test
    fun testCrewNameError() {
        // 공백 포함
        viewModel.updateName("런 크루")
        assertFalse("공백 포함은 실패해야 함", viewModel.uiState.value.isNameValid)

        // 너무 짧음 (1자)
        viewModel.updateName("런")
        assertFalse("1자는 실패해야 함", viewModel.uiState.value.isNameValid)

        // 너무 김 (6자)
        viewModel.updateName("투머치러닝크루")
        assertFalse("6자는 실패해야 함", viewModel.uiState.value.isNameValid)

        // 영어/숫자 포함
        viewModel.updateName("Run")
        assertFalse("영어는 실패해야 함", viewModel.uiState.value.isNameValid)
    }

    // ========================================================================
    // Slogan 유효성 검사
    // 규칙: 한글+공백 3~12자
    // ========================================================================

    // 띄어쓰기가 포함된 한글 3~12자는 통과해야 한다
    @Test
    fun testSloganKorean() {
        // Given & When
        viewModel.updateName("러닝팀")

        // 공백 없는 경우
        viewModel.updateSlogan("화이팅")
        assertTrue("3자는 유효해야 함", viewModel.uiState.value.isSloganValid)

        // 공백 있는 경우
        viewModel.updateSlogan("같이 달려요")
        assertTrue("공백 포함도 유효해야 함", viewModel.uiState.value.isSloganValid)

        // 12자
        viewModel.updateSlogan("슬로건 열두자 테스트임")
        assertTrue("12자는 유효해야 함", viewModel.uiState.value.isSloganValid)
    }

    // 글자수 미달이거나 초과하면 실패해야 한다
    @Test
    fun testSloganError() {
        // 너무 짧음
        viewModel.updateSlogan("야")
        assertFalse("1자는 실패해야 함", viewModel.uiState.value.isSloganValid)

        // 너무 김 (13자)
        viewModel.updateSlogan("이구호는너무길어서시스템이거부해야합니다")
        assertFalse("13자는 실패해야 함", viewModel.uiState.value.isSloganValid)
    }

    // ========================================================================
    // 생성 준비 완료 (isReadyToCreate) 검사
    // 조건: 이름 유효 + 구호 유효 + 날짜 선택됨
    // ========================================================================

    // 모든 조건이 충족되어야만 생성 버튼이 활성화된다
    @Test
    fun testActivateButton() {
        // 아무것도 입력 안 함 -> False
        assertFalse(viewModel.uiState.value.isReadyToCreate)

        // 이름만 입력 -> False
        viewModel.updateName("러닝팀")
        assertFalse(viewModel.uiState.value.isReadyToCreate)

        // 구호도 입력 -> False (아직 날짜가 없음)
        viewModel.updateSlogan("아자아자")
        assertFalse(viewModel.uiState.value.isReadyToCreate)

        // 날짜까지 입력 -> True
        viewModel.updateDateRange(LocalDate.now(), LocalDate.now().plusDays(5))
        assertTrue("모든 조건 충족 시 True여야 함", viewModel.uiState.value.isReadyToCreate)

        println("크루 생성 유효성 검사 완료")
    }
}