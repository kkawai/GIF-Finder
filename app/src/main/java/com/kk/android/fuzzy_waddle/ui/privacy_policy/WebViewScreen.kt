package com.kk.android.fuzzy_waddle.ui.privacy_policy

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.kk.android.fuzzy_waddle.Constants

@Composable
fun WebViewScreen(url: String = Constants.PRIVACY_POLICY_URL) {

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
    WebViewScreen("http://google.com")
}