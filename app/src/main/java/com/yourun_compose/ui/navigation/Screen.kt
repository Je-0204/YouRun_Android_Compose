package com.yourun_compose.ui.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")

    data object Onboarding : Screen("onboarding")

    // Authentication
    data object Login : Screen("login")
    data object SignUp : Screen("sign_up")

    data object TendencyTest : Screen("tendency_test")

    // Main Tab
    data object Home : Screen("home")
    data object Mate : Screen("mate")
    data object Challenge : Screen("challenge")
    data object MyPage : Screen("my_page")

    // MyPage
    data object EditProfile : Screen("edit_profile")

    // Calendar
    data object Calendar : Screen("calendar")

    // Running
    data object Running : Screen("running")

    data object RunningTracking : Screen("running_tracking/{time}/{mateId}/{mateName}") {
        fun createRoute(time: Int, mateId: Long, mateName: String): String {
            return "running_tracking/$time/$mateId/$mateName"
        }
    }

    data object RunningResult : Screen("running_result")

    // Create
    data object CreateCrew : Screen("create_crew")
    data object CreateSolo : Screen("create_solo")
}