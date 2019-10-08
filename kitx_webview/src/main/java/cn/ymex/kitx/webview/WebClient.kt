package cn.ymex.kitx.webview

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient


open class WebClient : WebViewClient() {
    /**
     * 防止加载网页时调起系统浏览器
     */
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

        return when {
            url.startsWith("http:") or url.startsWith("https:") -> {
                view.loadUrl(url)
                true
            }
            else -> {
                try {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(url)
                    )
                    view.context.startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("kitx-webview",e.localizedMessage)
                }
                true
            }
        }

    }

    override fun shouldOverrideUrlLoading(view: WebView, url: WebResourceRequest?): Boolean {
        return super.shouldOverrideUrlLoading(view, url)
    }


    override fun onPageStarted(view: WebView, url: String, p2: Bitmap?) {
        super.onPageStarted(view, url, p2)
        if (view is WebViewX5) {
            view.progressCallBack?.run {
                showView()
            }
        }
    }


    override fun onPageFinished(view: WebView, url: String) {
        super.onPageFinished(view, url)
        if (view is WebViewX5) {
            view.progressCallBack?.run {
                hideView()
            }
        }
    }
}