package com.kk.android.fuzzy_waddle.ui.home_screen

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.kk.android.fuzzy_waddle.Constants
import com.kk.android.fuzzy_waddle.R
import com.kk.android.fuzzy_waddle.model.GiphyImage
import com.kk.android.fuzzy_waddle.ui.common.ExpandableSearchView
import com.kk.android.fuzzy_waddle.ui.common.InfiniteListHandler
import com.kk.android.fuzzy_waddle.util.Util
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GifImageCard(
    onGifImageClicked: (gifImageUrl: String, gifImageAspectRatio: Float) -> Unit,
    giphyImage: GiphyImage
) {

    GlideImage(
        model = giphyImage.gifUrl(),
        contentDescription = "LoadImage",
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                giphyImage
                    .gifUrl()
                    ?.let { gifUrl ->
                        val url = Util.encodeString(gifUrl)
                        Log.d("ddddd", "encoded url: "+url)
                        onGifImageClicked(url, giphyImage.getGifAspectRatio())
                        Log.d("ddddd", "decoded url: "+Util.decodeString(url))
                    }
            }
            .aspectRatio(giphyImage.getGifAspectRatio())
    ) {
        it
            .error(R.drawable.ic_broken_image)
            .placeholder(R.drawable.loading_img)
            .load(giphyImage.gifUrl())
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSearchDisplayChanged: (text: String) -> Unit,
    onOverflowMenuClicked: () -> Unit,
    stateFlow: StateFlow<HomeScreenState>,
    getGifImagesWithSearchTerm: () -> Unit,
    lazyStaggeredGridState: LazyStaggeredGridState,
    getGifImages: ()-> Unit,
    onGifImageClicked: (gifImageUrl: String, gifImageAspectRatio: Float) -> Unit,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    searchTermHolder: SearchTermHolder,
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                onSearchDisplayChanged = onSearchDisplayChanged,
                savedSearchTerm = searchTermHolder.searchTerm,
                onOverflowMenuClicked = onOverflowMenuClicked,
                scrollBehavior = scrollBehavior,
                onSearchForGif = { getGifImagesWithSearchTerm()
                }
            )
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {

            val screenState = stateFlow.collectAsState()
            val lazyGridState = lazyStaggeredGridState

            Column {

                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Adaptive(150.dp),
                    modifier = modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(Constants.VERTICAL_GRID_PADDING.dp),
                    state = lazyGridState,

                    content = {
                        items(screenState.value.gifImages) { image ->
                            GifImageCard(
                                onGifImageClicked = onGifImageClicked,
                                giphyImage = image
                            )
                        }
                        item {
                            if (screenState.value.isLoading && screenState.value.gifImages.isNotEmpty()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    LoadingScreen()
                                }
                            }
                        }
                    }

                )
                InfiniteListHandler(lazyGridState = lazyGridState) {
                    Log.i(
                        "GifFinderApp ggggg",
                        "getting more starting at: " + screenState.value.imageCount
                    )
                    getGifImages()
                }
                if (screenState.value.isLoading && screenState.value.gifImages.isEmpty()) {
                    LoadingScreen()
                } else if (!screenState.value.isLoading && screenState.value.error.isNotBlank() && screenState.value.gifImages.isEmpty()) {
                    ErrorScreen(retryAction, modifier = modifier.fillMaxSize())
                }
            }
        } //End Column
    } //End Surface
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    onSearchDisplayChanged: (text: String) -> Unit,
    onSearchForGif: () -> Unit,
    savedSearchTerm: String,
    onOverflowMenuClicked: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {

    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            ExpandableSearchView(
                savedSearchTerm = savedSearchTerm,
                onSearchDisplayChanged = { text-> onSearchDisplayChanged(text)},
                onSearchDisplayClosed = {
                    Log.i("ggggg", "search display closed")
                },
                onOverflowMenuClicked = onOverflowMenuClicked,
                onSearchForGif = onSearchForGif

            )
        },
        modifier = modifier
    )
}