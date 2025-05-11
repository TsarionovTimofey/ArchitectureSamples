package com.common.web

import android.annotation.SuppressLint
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import timber.log.Timber

@SuppressLint("SetJavaScriptEnabled")
fun WebView.setupJSAndChrome() {
    clearCache(true)
    CookieManager.getInstance().removeAllCookies(null)
    CookieManager.getInstance().flush()
    clearFormData()
    clearHistory()
    clearSslPreferences()
    settings.javaScriptCanOpenWindowsAutomatically = true
    settings.domStorageEnabled = true
    settings.useWideViewPort = true
    settings.javaScriptEnabled = true
    webChromeClient = object : WebChromeClient() {
        override fun onConsoleMessage(
            message: String?,
            lineNumber: Int,
            sourceID: String?
        ) {
            Timber.tag("\uD83D\uDCB3").d("WebView Error: $message")
        }
    }
}
