package com.kk.android.fuzzy_waddle.ui.screens

import com.kk.android.fuzzy_waddle.model.GiphyImage

sealed interface CurrentScreen {
    object HomeScreen : CurrentScreen
    data class DetailedGIFScreen(val gifImage: GiphyImage) : CurrentScreen
    object PrivacyPolicyScreen : CurrentScreen
}