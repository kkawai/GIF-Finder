package com.kk.android.fuzzy_waddle;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PrivacyPolicyActivity extends AppCompatActivity {

    private static final String PRIVACY_POLICY_URL = "https://raw.githack.com/kkawai/GIF-Finder/main/external/privacy_policy.html";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView webView = new WebView(this);
        setContentView(webView);
        webView.loadUrl(PRIVACY_POLICY_URL);
    }
}
