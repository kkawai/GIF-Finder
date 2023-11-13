package com.kk.android.fuzzy_waddle.ui.screens

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.kk.android.fuzzy_waddle.R
import com.kk.android.fuzzy_waddle.model.GiphyImage


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FullScreenGif(gifFinderViewModel: GifFinderViewModel, giphyImage: GiphyImage) {

    BackPressHandler(onBackPressed = {
        gifFinderViewModel.navigationState = NavigationState.HomeScreen
    })

    val context = LocalContext.current
    val share = {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, giphyImage.gifUrl())
        sendIntent.type = "text/plain"
        context.startActivity(Intent.createChooser(sendIntent, null))
    }

    Box() {
        GlideImage(
            model = giphyImage.gifUrl(),
            contentDescription = "LoadImage",
            modifier = Modifier
                .align(Alignment.Center)
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

        IconButton(
            onClick = { share() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_share),
                contentDescription = "share",
            )
        }
    }
}

