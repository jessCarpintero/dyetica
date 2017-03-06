package com.dyetica.app.utils;

import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Jess on 12/02/2017.
 */

public class MyWebViewClient  extends WebViewClient {

    private String currentUrl;

    public MyWebViewClient(String currentUrl) {
        this.currentUrl = currentUrl;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        if (url.contains(currentUrl)) {
            view.loadUrl(url);
            return false;
        }

        return true;
    }
}