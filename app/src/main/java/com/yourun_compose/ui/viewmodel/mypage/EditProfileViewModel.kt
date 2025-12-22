package com.yourun_compose.ui.viewmodel.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourun_compose.domain.usecase.auth.CheckDuplicateUseCase
import com.yourun_compose.domain.usecase.user.GetMyProfileUseCase
import com.yourun_compose.domain.usecase.user.UpdateProfileUseCase
import com.yourun_compose.domain.usecase.validation.ValidateInputUseCase
import com.yourun_compose.ui.state.mypage.EditProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val validateUseCase: ValidateInputUseCase,
    private val checkDuplicateUseCase: CheckDuplicateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState = _uiState.asStateFlow()

    private var originalNickname: String = ""

    init {
        loadCurrentProfile()
    }

    private fun loadCurrentProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = getMyProfileUseCase()
            result.onSuccess { data ->
                originalNickname = data.nickname
                val firstTag = data.tags.getOrNull(0) ?: ""
                val secondTag = data.tags.getOrNull(1) ?: ""
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        nickname = data.nickname,
                        tag1 = firstTag,
                        tag2 = secondTag,
                        userTendency = data.tendency
                    )
                }
            }.onFailure {
                _uiState.update { it.copy(isLoading = false, errorMessage = "정보 로딩 실패") }
            }
        }
    }

    fun updateNickname(input: String) {
        val isValid = validateUseCase.validateNickname(input)
        _uiState.update {
            it.copy(
                nickname = input,
                isNicknameValid = isValid,
                // 닉네임이 원본과 다르면 중복체크 필요(false), 같으면 필요없음(true)
                isNicknameChecked = input == originalNickname,
                errorMessage = if (!isValid) "한글 2~4자(공백 없음)만 가능" else null
            )
        }
    }

    fun updateTag1(input: String) { _uiState.update { it.copy(tag1 = input) } }
    fun updateTag2(input: String) { _uiState.update { it.copy(tag2 = input) } }

    fun checkNickname() {
        if (!_uiState.value.isNicknameValid) return
        viewModelScope.launch {
            val result = checkDuplicateUseCase.checkNickname(_uiState.value.nickname)
            result.onSuccess { isDup ->
                _uiState.update {
                    it.copy(
                        isNicknameChecked = !isDup,
                        errorMessage = if (isDup) "중복된 닉네임입니다." else "사용 가능합니다."
                    )
                }
            }
        }
    }

    fun submitUpdate() {
        if (!_uiState.value.canSubmit(originalNickname)) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val tagsList = listOf(_uiState.value.tag1, _uiState.value.tag2)

            val result = updateProfileUseCase(
                _uiState.value.nickname,
                tagsList
            )
            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isUpdateSuccess = true) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }
}