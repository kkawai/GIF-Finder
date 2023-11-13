package com.kk.android.fuzzy_waddle.data

import com.kk.android.fuzzy_waddle.Constants
import com.kk.android.fuzzy_waddle.model.GiphyResponse
import com.kk.android.fuzzy_waddle.util.ResourceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

interface GifImageRepository {
    fun getTrendingGifImages(skip: Int): Flow<ResourceState<GiphyResponse>>
    fun getGifImagesBySearchTerm(skip: Int, searchTerm: String): Flow<ResourceState<GiphyResponse>>
}

class NetworkGifImageRepository(private val gifImageApiService: GifImageApiService) :
    GifImageRepository {

    override fun getTrendingGifImages(skip: Int): Flow<ResourceState<GiphyResponse>> = flow {
        try {
            emit(ResourceState.Loading())
            emit(
                ResourceState.Success(
                    gifImageApiService.getGiphyTrendingResponse(
                        skip,
                        Constants.PAGE_SIZE,
                        Constants.GIPHY_API_KEY
                    )
                )
            )
        } catch (e: IOException) {
            emit(ResourceState.Error(e.localizedMessage ?: "Unexpected Error(1)"))
        } catch (e: Exception) {
            emit(ResourceState.Error(e.localizedMessage ?: "Unexpected Error(2)"))
        }
    }

    override fun getGifImagesBySearchTerm(
        skip: Int,
        q: String
    ): Flow<ResourceState<GiphyResponse>> = flow {
        try {
            emit(ResourceState.Loading())
            emit(
                ResourceState.Success(
                    gifImageApiService.getGiphySearchTermResponse(
                        skip,
                        Constants.PAGE_SIZE,
                        q,
                        Constants.GIPHY_API_KEY
                    )
                )
            )
        } catch (e: IOException) {
            emit(ResourceState.Error(e.localizedMessage ?: "Unexpected Error(3)"))
        } catch (e: Exception) {
            emit(ResourceState.Error(e.localizedMessage ?: "Unexpected Error(4)"))
        }
    }

}