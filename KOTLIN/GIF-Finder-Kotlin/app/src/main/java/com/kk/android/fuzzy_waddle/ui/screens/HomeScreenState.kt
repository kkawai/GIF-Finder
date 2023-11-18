package com.kk.android.fuzzy_waddle.ui.screens

import com.kk.android.fuzzy_waddle.model.GiphyImage

data class HomeScreenState (
    val isLoading: Boolean = false,
    var gifImages: List<GiphyImage> = emptyList(),
    val error: String = "",
    var imageCount: Int = 0,
    val isEndReached: Boolean = false
)