package com.kk.android.kt.gf

import com.kk.android.kt.gf.data.GifImageApiService
import com.kk.android.kt.gf.model.GiphyResponse

class FakeGifImageApiService : GifImageApiService {
    override suspend fun getGiphyTrendingResponse(
        offset: Int,
        limit: Int,
        apiKey: String
    ): GiphyResponse {
        return GiphyResponse(emptyList())
    }

    override suspend fun getGiphySearchTermResponse(
        offset: Int,
        limit: Int,
        q: String,
        apiKey: String
    ): GiphyResponse {
        return GiphyResponse(emptyList())
    }
}