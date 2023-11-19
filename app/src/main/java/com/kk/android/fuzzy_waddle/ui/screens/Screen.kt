package com.kk.android.fuzzy_waddle.ui.screens

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home_screen")
    object DetailedGIFScreen : Screen("detailed_gif_screen")
    object PrivacyPolicyScreen : Screen("privacy_policy_screen")
}