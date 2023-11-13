package com.kk.android.fuzzy_waddle.ui.screens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.progressSemantics
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.kk.android.fuzzy_waddle.Constants
import com.kk.android.fuzzy_waddle.R
import com.kk.android.fuzzy_waddle.model.GiphyImage

@Composable
fun GifFinderAppUI() {
    val gifFinderViewModel: GifFinderViewModel =
        viewModel(factory = GifFinderViewModel.Factory)

    val screen = gifFinderViewModel.navigationState

    when (screen) {
        is NavigationState.HomeScreen ->
            HomeScreen(
                gifFinderViewModel = gifFinderViewModel,
                retryAction = { gifFinderViewModel.getGifImages() }
            )

        is NavigationState.DetailedGIFScreen ->
            FullScreenGif(
                gifFinderViewModel = gifFinderViewModel,
                giphyImage = screen.gifImage
            )
        is NavigationState.PrivacyPolicyScreen ->
            loadWebUrl(gifFinderViewModel = gifFinderViewModel)
    }
}

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
fun LoadingScreen(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier
            .progressSemantics()
            .size(Constants.CIRCLE_PROGRESS_SIZE.dp)
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GifImageCard(
    gifFinderViewModel: GifFinderViewModel,
    giphyImage: GiphyImage
) {

    GlideImage(
        model = giphyImage.gifUrl(),
        contentDescription = "LoadImage",
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                gifFinderViewModel.navigationState =
                    NavigationState.DetailedGIFScreen(giphyImage)
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
    gifFinderViewModel: GifFinderViewModel,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { TopAppBar(gifFinderViewModel, scrollBehavior = scrollBehavior) }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {

            val screenState = gifFinderViewModel.screenState.collectAsState()
            val lazyGridState = gifFinderViewModel.lazyStaggeredGridState

            if (!screenState.value.isLoading && screenState.value.error.isNotBlank() && screenState.value.gifImages.isEmpty()) {
                ErrorScreen(retryAction, modifier = modifier.fillMaxSize())
            } else if (screenState.value.isLoading && screenState.value.gifImages.isEmpty()) {
                LoadingScreen()
            } else {
                Column {

                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Adaptive(150.dp),
                        modifier = modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(Constants.VERTICAL_GRID_PADDING.dp),
                        state = lazyGridState,

                        content = {
                            items(screenState.value.gifImages) { image ->
                                GifImageCard(
                                    gifFinderViewModel = gifFinderViewModel,
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
                                        CircularProgressIndicator(
                                            modifier = modifier
                                                .progressSemantics()
                                                .size(Constants.CIRCLE_PROGRESS_SIZE.dp)
                                        )
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
                        gifFinderViewModel.getGifImages()
                    }
                }
            } //End Column
        } //End Surface
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(gifFinderViewModel: GifFinderViewModel, scrollBehavior: TopAppBarScrollBehavior, modifier: Modifier = Modifier) {

    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            ExpandableSearchView(searchDisplay = "",
                onSearchDisplayChanged = {
                    Log.i("ggggg", "search term: " + it)
                },
                onSearchDisplayClosed = {
                    Log.i("ggggg", "search display closed")
                },
                gifFinderViewModel = gifFinderViewModel)
        },
        modifier = modifier
    )
}