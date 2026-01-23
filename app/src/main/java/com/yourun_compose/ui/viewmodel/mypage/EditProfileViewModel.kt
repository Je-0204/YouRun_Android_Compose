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

            getMyProfileUseCase()
                .onSuccess { data ->
                    originalNickname = data.nickname
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            nickname = data.nickname,
                            tag1 = data.tags.getOrNull(0) ?: "",
                            tag2 = data.tags.getOrNull(1) ?: "",
                            userTendency = data.tendency,
                            isNicknameChecked = true
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "정보를 불러오지 못했습니다.") }
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
                isNicknameChecked = (input == originalNickname),
                errorMessage = if (!isValid && input.isNotEmpty()) "한글 2~4자(공백 없음)만 가능" else null
            )
        }
    }

    fun toggleTag(tag: String) {
        _uiState.update { state ->
            if (state.tag1 == tag) {
                state.copy(tag1 = "")
            } else if (state.tag2 == tag) {
                state.copy(tag2 = "")
            } else {
                if (state.tag1.isEmpty()) {
                    state.copy(tag1 = tag)
                } else if (state.tag2.isEmpty()) {
                    state.copy(tag2 = tag)
                } else {
                    state
                }
            }
        }
    }

    fun checkNickname() {
        if (!_uiState.value.isNicknameValid) return

        if (_uiState.value.nickname == originalNickname) {
            _uiState.update { it.copy(isNicknameChecked = true) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            checkDuplicateUseCase.checkNickname(_uiState.value.nickname)
                .onSuccess { isDup ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isNicknameChecked = !isDup,
                            errorMessage = if (isDup) "이미 사용 중인 닉네임입니다." else null
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "중복 확인 실패") }
                }
        }
    }

    fun submitUpdate() {
        val state = _uiState.value
        if (!state.canSubmit) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val tagsList = listOf(state.tag1, state.tag2).filter { it.isNotEmpty() }

            updateProfileUseCase(state.nickname, tagsList)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, isUpdateSuccess = true) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "수정 실패") }
                }
        }
    }
}