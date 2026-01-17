package com.yourun_compose.ui.viewmodel.starting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.annotation.DrawableRes
import com.yourun_compose.data.local.SessionManager
import com.yourun_compose.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

data class OnboardingPage(
    val description: String,
    val highlightText: String? = null,
    val subText: String,
    @param:DrawableRes val imageRes: Int
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    fun completeOnboarding(navController: NavController) {
        viewModelScope.launch {
            sessionManager.completeOnboarding()
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Onboarding.route) { inclusive = true }
            }
        }
    }
}