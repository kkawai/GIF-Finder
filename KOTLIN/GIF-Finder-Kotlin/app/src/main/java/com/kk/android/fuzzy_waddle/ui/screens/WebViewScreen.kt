package com.kk.android.fuzzy_waddle.ui.screens

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kk.android.fuzzy_waddle.Constants

@Composable
fun loadWebUrl(gifFinderViewModel: GifFinderViewModel?, url: String = Constants.PRIVACY_POLICY_URL) {

    BackPressHandler(onBackPressed = {
        gifFinderViewModel?.navigationState = NavigationState.HomeScreen
    })

    val context = LocalContext.current
    AndroidView(factory = {
        WebView(context).apply {
            webViewClient = WebViewClient()
            loadUrl(url)
        }
    })
}

@Preview
@Composable
fun previewLoadWebUrl() {
    loadWebUrl(null, url = "http://google.com")
}