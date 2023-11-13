package com.kk.android.kt.gf.data

import coil.network.HttpException
import com.kk.android.kt.gf.Constants
import com.kk.android.kt.gf.model.GiphyResponse
import com.kk.android.kt.gf.util.ResourceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

interface GifImageRepository {
    fun getTrendingGifImages(skip: Int): Flow<ResourceState<GiphyResponse>>
    fun getGifImagesBySearchTerm(skip: Int, searchTerm: String): Flow<ResourceState<GiphyResponse>>
}

class NetworkGifImageRepository(private val gifImageApiService: GifImageApiService) : GifImageRepository {

    override fun getTrendingGifImages(skip: Int): Flow<ResourceState<GiphyResponse>> = flow {
        try {
            emit(ResourceState.Loading())
            emit(ResourceState.Success(gifImageApiService.getGiphyTrendingResponse(skip, Constants.PAGE_SIZE, Constants.GIPHY_API_KEY)))
        } catch (e: HttpException) {
            emit(ResourceState.Error(e.localizedMessage ?: "Unexpected Error(1)"))
        } catch (e: IOException) {
            emit(ResourceState.Error(e.localizedMessage ?: "Unexpected Error(2)"))
        }
    }

    override fun getGifImagesBySearchTerm(skip: Int, q: String): Flow<ResourceState<GiphyResponse>> = flow {
        try {
            emit(ResourceState.Loading())
            emit(ResourceState.Success(gifImageApiService.getGiphySearchTermResponse(skip, Constants.PAGE_SIZE, q, Constants.GIPHY_API_KEY)))
        } catch (e: HttpException) {
            emit(ResourceState.Error(e.localizedMessage ?: "Unexpected Error(3)"))
        } catch (e: IOException) {
            emit(ResourceState.Error(e.localizedMessage ?: "Unexpected Error(4)"))
        }
    }

}