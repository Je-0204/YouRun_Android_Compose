package com.yourun_compose.ui.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourun_compose.domain.usecase.common.GetHomeDataUseCase
import com.yourun_compose.ui.state.home.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeDataUseCase: GetHomeDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchHomeData()
    }

    fun fetchHomeData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = getHomeDataUseCase()

            result.onSuccess { data ->
                _uiState.update {
                    it.copy(isLoading = false, homeData = data)
                }
            }.onFailure { e ->
                // 토큰 만료 에러(401) 처리 등은 Interceptor나 Repository 레벨에서 하거나
                // 여기서 감지해서 "로그인 화면으로 이동" 이벤트를 발생시킬 수도 있음.
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "데이터를 불러오지 못했습니다: ${e.message}")
                }
            }
        }
    }
}