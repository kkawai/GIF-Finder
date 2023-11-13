package com.kk.android.kt.gf.ui.screens

import com.kk.android.kt.gf.model.GiphyImage

data class ScreenState (
    val isLoading: Boolean = false,
    var gifImages: List<GiphyImage> = emptyList(),
    val error: String = "",
    var imageCount: Int = 0,
    val isEndReached: Boolean = false
)