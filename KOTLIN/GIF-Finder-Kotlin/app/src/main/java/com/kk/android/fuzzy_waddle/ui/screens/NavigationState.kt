package com.kk.android.fuzzy_waddle.ui.screens

import com.kk.android.fuzzy_waddle.model.GiphyImage

sealed interface NavigationState {
    object HomeScreen : NavigationState
    data class DetailedGIFScreen(val gifImage: GiphyImage) : NavigationState
    object PrivacyPolicyScreen : NavigationState
}