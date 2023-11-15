package com.kk.android.fuzzy_waddle.data

import com.kk.android.fuzzy_waddle.Constants
import com.kk.android.fuzzy_waddle.model.GiphyResponse
import com.kk.android.fuzzy_waddle.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

interface GifImageRepository {
    fun getTrendingGifImages(skip: Int): Flow<Resource<GiphyResponse>>
    fun getGifImagesBySearchTerm(skip: Int, searchTerm: String): Flow<Resource<GiphyResponse>>
}

class NetworkGifImageRepository(private val gifImageApiService: GifImageApiService) :
    GifImageRepository {

    override fun getTrendingGifImages(skip: Int): Flow<Resource<GiphyResponse>> = flow {
        try {
            emit(Resource.Loading())
            emit(
                Resource.Success(
                    gifImageApiService.getGiphyTrendingResponse(
                        skip,
                        Constants.PAGE_SIZE,
                        Constants.GIPHY_API_KEY
                    )
                )
            )
        } catch (e: IOException) {
            emit(Resource.Error(e.localizedMessage ?: "Unexpected Error(1)"))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unexpected Error(2)"))
        }
    }

    override fun getGifImagesBySearchTerm(
        skip: Int,
        q: String
    ): Flow<Resource<GiphyResponse>> = flow {
        try {
            emit(Resource.Loading())
            emit(
                Resource.Success(
                    gifImageApiService.getGiphySearchTermResponse(
                        skip,
                        Constants.PAGE_SIZE,
                        q,
                        Constants.GIPHY_API_KEY
                    )
                )
            )
        } catch (e: IOException) {
            emit(Resource.Error(e.localizedMessage ?: "Unexpected Error(3)"))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unexpected Error(4)"))
        }
    }

}