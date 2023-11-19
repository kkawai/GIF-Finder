package com.kk.android.fuzzy_waddle

import com.kk.android.fuzzy_waddle.data.GifImageApiService
import com.kk.android.fuzzy_waddle.model.GiphyResponse

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