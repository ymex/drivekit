package cn.ymex.kitx.webview

import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView

/**
 * Created by ymex on 2019/6/9.
 * About:
 */

open class ChromeClient : WebChromeClient() {

    override fun onProgressChanged(view : WebView, progress: Int) {
        super.onProgressChanged(view, progress)
        if (view is WebViewX5) {
            view.progressCallBack?.run {
                setProgress(progress)
            }
        }
    }


}