package com.yourun_compose

import com.yourun_compose.data.model.user.UserInfo
import com.yourun_compose.domain.usecase.auth.CheckDuplicateUseCase
import com.yourun_compose.domain.usecase.user.GetMyProfileUseCase
import com.yourun_compose.domain.usecase.user.UpdateProfileUseCase
import com.yourun_compose.domain.usecase.validation.ValidateInputUseCase
import com.yourun_compose.ui.viewmodel.mypage.EditProfileViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EditProfileViewModelTest {

    private val getMyProfileUseCase = mockk<GetMyProfileUseCase>()
    private val updateProfileUseCase = mockk<UpdateProfileUseCase>(relaxed = true)
    private val validateUseCase = mockk<ValidateInputUseCase>(relaxed = true)
    private val checkDuplicateUseCase = mockk<CheckDuplicateUseCase>(relaxed = true)

    private lateinit var viewModel: EditProfileViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // 프로필 로딩 시 태그 리스트가 tag1과 tag2로 잘 분리되어야 한다
    @Test
    fun testDivideTags() = runTest {
        // Given
        val mockUser = UserInfo(
            id = 1, nickname = "기존닉",
            tags = listOf("#열정적", "#에너자이저"),
            tendency = "스프린터"
        )
        coEvery { getMyProfileUseCase() } returns Result.success(mockUser)

        // When: 뷰모델 생성 (init에서 loadCurrentProfile 실행됨)
        viewModel = EditProfileViewModel(getMyProfileUseCase, updateProfileUseCase, validateUseCase, checkDuplicateUseCase)

        // Then: UI State에 각각 쪼개져서 들어갔는지 확인
        assertEquals("#열정적", viewModel.uiState.value.tag1)
        assertEquals("#에너자이저", viewModel.uiState.value.tag2)

        println("태그 분리 테스트 통과: List -> String 변환 성공")
    }

    // 수정 요청 시 tag1과 tag2가 리스트로 잘 합쳐져서 전송되어야 한다
    @Test
    fun testCombineTag() = runTest {
        // Given
        val originalNick = "기존닉"
        val mockUser = UserInfo(
            id = 1,
            nickname = originalNick,
            tendency = "페이스메이커",
            tags = listOf("#태그1", "#태그2")
        )
        coEvery { getMyProfileUseCase() } returns Result.success(mockUser)

        // 중요: Update 성공 시 반환값 명시 (캐스팅 에러 방지)
        coEvery { updateProfileUseCase(any(), any()) } returns Result.success(mockUser)

        viewModel = EditProfileViewModel(getMyProfileUseCase, updateProfileUseCase, validateUseCase, checkDuplicateUseCase)
        advanceUntilIdle()

        // 사용자가 새로운 닉네임 입력
        val newNick = "새닉네임"
        every { validateUseCase.validateNickname(newNick) } returns true
        viewModel.updateNickname(newNick)

        // 닉네임 중복 체크 성공 시뮬레이션 (전송을 위해 필수)
        coEvery { checkDuplicateUseCase.checkNickname(newNick) } returns Result.success(false) // 중복 아님
        viewModel.checkNickname()
        advanceUntilIdle()

        // 사용자가 태그 입력
        viewModel.updateTag1("#느긋하게")
        viewModel.updateTag2("#음악과")

        // When
        viewModel.submitUpdate()
        advanceUntilIdle()

        // Then: UseCase에 ["#느긋하게", "#음악과"] 리스트로 넘어갔는지 확인
        val slot = slot<List<String>>()
        coVerify { updateProfileUseCase(newNick, capture(slot)) }

        assertEquals(listOf("#느긋하게", "#음악과"), slot.captured)
        println("태그 병합 테스트 통과: String -> List 변환 성공")
    }
}