package com.kk.android.fuzzy_waddle.ui.screens

import android.text.TextUtils
import android.util.Log
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kk.android.fuzzy_waddle.GifFinderApplication
import com.kk.android.fuzzy_waddle.data.GifImageRepository
import com.kk.android.fuzzy_waddle.model.GiphyResponse
import com.kk.android.fuzzy_waddle.util.Resource
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.plus
import java.util.stream.Collectors

class GifFinderViewModel(private val gifImageRepository: GifImageRepository) : ViewModel() {

    private val _screenState = MutableStateFlow(ScreenState())
    val screenState = _screenState.asStateFlow()

    var currentScreen: CurrentScreen by mutableStateOf(CurrentScreen.HomeScreen)
    private set

    //remembers scroll position across screen rotations by being in the ViewModel
    val lazyStaggeredGridState: LazyStaggeredGridState = LazyStaggeredGridState(0,0)

    //remember searchTerm in the expandable SearchBar
    var searchTerm: String by mutableStateOf("")
        private set

    init {
        getGifImages()
    }

    fun showScreen(newScreen: CurrentScreen) {
        currentScreen = newScreen
    }

    fun getGifImagesWithSearchTerm(newSearchTerm: String) {

        if (!searchTerm.equals(newSearchTerm)) {
            searchTerm = newSearchTerm
            i = 1
            _screenState.value.imageCount = 0
            _screenState.value.gifImages = emptyList()
        }
        getGifImages()
    }

    private fun handleResult(result: Resource<GiphyResponse>) {
        when (result) {
            is Resource.Success -> result.data?.let { data -> onRequestSuccess(data) }
            is Resource.Error -> onRequestError(result.message)
            is Resource.Loading -> onRequestLoading()
        }
    }

    fun getGifImages() {

        if (searchTerm.isNotBlank()) {
            gifImageRepository.getGifImagesBySearchTerm(skip = _screenState.value.imageCount, searchTerm = searchTerm)
                .distinctUntilChanged()
                .onEach{ result ->
                    handleResult(result)
                }
                .launchIn(viewModelScope + SupervisorJob())
        } else {
            gifImageRepository.getTrendingGifImages(skip = _screenState.value.imageCount)
                .distinctUntilChanged()
                .onEach { result ->
                    handleResult(result)
                }
                .launchIn(viewModelScope + SupervisorJob())
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GifFinderApplication)
                val gifImageRepository = application.appContainer.gifImageRepository
                GifFinderViewModel(gifImageRepository = gifImageRepository)
            }
        }
    }

    private var i : Int = 1

    private fun onRequestSuccess(giphyResponse: GiphyResponse) {

        val newGifImages = giphyResponse.data.stream()
            .filter { gif -> gif.setTempKey(i++) > 0 && !TextUtils.isEmpty(gif.gifUrl()) && !TextUtils.isEmpty(gif.gifMmsUrl()) && !TextUtils.isEmpty(gif.stillUrl())}
            .collect(Collectors.toList())

        Log.i("GifFinderViewModel ggggg","newGifImages: " + newGifImages.size)
        _screenState.update {
            it.copy(
                isLoading = false,
                gifImages = it.gifImages + newGifImages,
                error = "",
                imageCount = it.gifImages.size + newGifImages.size,
                isEndReached = newGifImages.size == 0
            )
        }
    }

    private fun onRequestError(error: String?) {
        _screenState.update {
            it.copy(
                isLoading = false,
                error = error?: "Unexpected error",
            )
        }
    }

    private fun onRequestLoading() {
        _screenState.update {
            it.copy(
                isLoading = true
            )
        }
    }

}