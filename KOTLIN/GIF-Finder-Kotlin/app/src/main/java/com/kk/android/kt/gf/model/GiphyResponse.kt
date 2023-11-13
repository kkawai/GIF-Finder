package com.kk.android.kt.gf.model

import kotlinx.serialization.Serializable

@Serializable
data class GiphyResponse(val data: List<GiphyImage>)
