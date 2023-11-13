package com.kk.android.kt.gf.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.kk.android.kt.gf.model.GiphyResponse
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface AppContainer {
    val gifImageRepository: GifImageRepository
}

interface GifImageApiService {
    @GET("/v1/gifs/trending")
    suspend fun getGiphyTrendingResponse(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("api_key") apiKey: String
    ): GiphyResponse

    @GET("/v1/gifs/search")
    suspend fun getGiphySearchTermResponse(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("q") q: String,
        @Query("api_key") apiKey: String
    ): GiphyResponse

}

class DefaultAppContainer : AppContainer {

    private val baseUrl =
        "https://api.giphy.com"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json {
            ignoreUnknownKeys = true
        }.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: GifImageApiService by lazy {
        retrofit.create(GifImageApiService::class.java)
    }

    override val gifImageRepository: GifImageRepository by lazy {
        NetworkGifImageRepository(retrofitService)
    }

}

