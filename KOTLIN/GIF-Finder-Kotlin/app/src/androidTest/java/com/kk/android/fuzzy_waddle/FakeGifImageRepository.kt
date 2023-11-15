package com.kk.android.fuzzy_waddle

import com.kk.android.fuzzy_waddle.data.GifImageRepository
import com.kk.android.fuzzy_waddle.model.GiphyResponse
import com.kk.android.fuzzy_waddle.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class FakeGifImageRepository : GifImageRepository {
    override fun getTrendingGifImages(skip: Int): Flow<Resource<GiphyResponse>> = flow {
        try {
            emit(Resource.Loading())
            emit(Resource.Success(GiphyResponse(emptyList())))
        } catch (e: IOException) {
            emit(Resource.Error(e.localizedMessage ?: "Unexpected Error(1)"))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unexpected Error(2)"))
        }
    }

    override fun getGifImagesBySearchTerm(
        skip: Int,
        searchTerm: String
    ): Flow<Resource<GiphyResponse>> = flow {
        try {
            emit(Resource.Loading())
            emit(Resource.Success(GiphyResponse(emptyList())))
        } catch (e: IOException) {
            emit(Resource.Error(e.localizedMessage ?: "Unexpected Error(1)"))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unexpected Error(2)"))
        }
    }
}