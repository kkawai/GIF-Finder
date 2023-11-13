package com.kk.android.kt.gf.ui.screens

import com.kk.android.kt.gf.model.GiphyImage

sealed interface NavigationState {
    object HomeScreen : NavigationState
    data class DetailedGIFScreen(val gifImage: GiphyImage) : NavigationState
    object PrivacyPolicyScreen : NavigationState
}