package com.common.web

import android.graphics.Bitmap
import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import timber.log.Timber

class JSWebClientImpl(
    private val pageFinished: ((url: String?) -> Unit) = {},
    private val pageStarted: ((url: String?) -> Unit) = {},
    private val openIntent: ((url: Uri) -> Unit) = {},
    private val openMailto: ((url: Uri) -> Unit) = {},
    private val openTel: ((url: Uri) -> Unit) = {}
) : WebViewClient(), JSWebClient {

    private var isPageStarted: Boolean = false
    var script: String? = null

    override fun onPageFinished(view: WebView?, url: String?) {
        pageFinished(url)
        super.onPageFinished(view, url)
        if (!isPageStarted) {
            Timber.tag("☕️").d("WebView evaluate js $script")
            script?.let {
                view?.evaluateJavascript(it, null)
            }
            isPageStarted = true
        }
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        pageStarted(url)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url = request?.url
        return when {
            url != null && url.toString().startsWith("mailto:") -> {
                openMailto(url)
                true
            }
            url != null && url.toString().startsWith("tel:") -> {
                openTel(url)
                true
            }
            url != null && url.toString().startsWith("intent://") -> {
                openIntent(url)
                true
            }
            else -> super.shouldOverrideUrlLoading(view, request)
        }
    }
}
