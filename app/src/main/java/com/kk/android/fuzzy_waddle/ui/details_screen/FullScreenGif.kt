package com.kk.android.fuzzy_waddle.ui.details_screen

import android.content.Intent
import android.util.Log
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.kk.android.fuzzy_waddle.R
import com.kk.android.fuzzy_waddle.util.Util

@Preview
@Composable
fun FullScreenGifPreview() {
    FullScreenGif("", 1.5f)
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FullScreenGif(gifImageUrl: String, gifImageAspectRatio: Float) {

    val gifImageUrlDecoded = Util.decodeString(gifImageUrl)

    Log.d("ddddd", "gifImageUrl: " + gifImageUrlDecoded)

    val context = LocalContext.current
    val share = {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, gifImageUrl)
        sendIntent.type = "text/plain"
        context.startActivity(Intent.createChooser(sendIntent, null))
    }

    Box() {
        GlideImage(
            model = gifImageUrlDecoded,
            contentDescription = "LoadImage",
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .aspectRatio(gifImageAspectRatio)
        ) {
            it
                .error(R.drawable.ic_broken_image)
                .placeholder(R.drawable.loading_img)
                .load(gifImageUrlDecoded)
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

