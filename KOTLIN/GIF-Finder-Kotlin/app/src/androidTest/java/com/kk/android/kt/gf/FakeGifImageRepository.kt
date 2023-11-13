package com.kk.android.kt.gf

import coil.network.HttpException
import com.kk.android.kt.gf.data.GifImageRepository
import com.kk.android.kt.gf.model.GiphyResponse
import com.kk.android.kt.gf.util.ResourceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class FakeGifImageRepository : GifImageRepository {
    override fun getTrendingGifImages(skip: Int): Flow<ResourceState<GiphyResponse>> = flow {
        try {
            emit(ResourceState.Loading())
            emit(ResourceState.Success(GiphyResponse(emptyList())))
        } catch (e: HttpException) {
            emit(ResourceState.Error(e.localizedMessage ?: "Unexpected Error(1)"))
        } catch (e: IOException) {
            emit(ResourceState.Error(e.localizedMessage ?: "Unexpected Error(2)"))
        }
    }

    override fun getGifImagesBySearchTerm(
        skip: Int,
        searchTerm: String
    ): Flow<ResourceState<GiphyResponse>> = flow {
        try {
            emit(ResourceState.Loading())
            emit(ResourceState.Success(GiphyResponse(emptyList())))
        } catch (e: HttpException) {
            emit(ResourceState.Error(e.localizedMessage ?: "Unexpected Error(1)"))
        } catch (e: IOException) {
            emit(ResourceState.Error(e.localizedMessage ?: "Unexpected Error(2)"))
        }
    }
}