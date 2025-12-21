package com.yourun_compose.ui.state.main

sealed interface AppStartDestination {
    object Splash : AppStartDestination // Loading
    object Login : AppStartDestination  // Login
    object Home : AppStartDestination   // Home
    object SoloProgress : AppStartDestination // Solo Challenge
    object CrewProgress : AppStartDestination // Crew Challenge
}

data class MainUiState(
    val startDestination: AppStartDestination = AppStartDestination.Splash,
    val isReady: Boolean = false
)