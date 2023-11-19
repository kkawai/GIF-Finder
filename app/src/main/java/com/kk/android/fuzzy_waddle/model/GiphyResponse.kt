package com.kk.android.fuzzy_waddle.model

import kotlinx.serialization.Serializable

@Serializable
data class GiphyResponse(val data: List<GiphyImage>)
