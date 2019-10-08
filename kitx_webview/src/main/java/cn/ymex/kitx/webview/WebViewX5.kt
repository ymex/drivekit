package cn.ymex.kitx.webview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import com.tencent.smtt.export.external.interfaces.IX5WebSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebStorage
import com.tencent.smtt.sdk.WebView


class WebViewX5 @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {

    var progressCallBack: ProgressCallBack? = null


    init {
        val webStorage = WebStorage.getInstance()
        webStorage.getOrigins {
            it.forEach { entry ->
                println("--x5  key : ${entry.key}  val:${entry.value}")

            }
        }
        this.webChromeClient = ChromeClient()
        this.setWebViewClient(WebClient())
        initWebViewSettings()
        this.view.isClickable = true
    }


    fun setWebViewClient(client: WebClient) {
        this.webViewClient = client
    }


    fun setChromeClient(client: ChromeClient) {
        this.webChromeClient = client
    }


    private fun initWebViewSettings() {
        val webSetting = this.settings
        webSetting.javaScriptEnabled = true
        webSetting.javaScriptCanOpenWindowsAutomatically = true
        webSetting.allowFileAccess = true
        webSetting.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        webSetting.setSupportZoom(true)
        webSetting.builtInZoomControls = true
        webSetting.useWideViewPort = true
        webSetting.setSupportMultipleWindows(true)
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true)
        // webSetting.setDatabaseEnabled(true);
        webSetting.domStorageEnabled = true
        webSetting.setGeolocationEnabled(true)
        webSetting.setAppCacheMaxSize(java.lang.Long.MAX_VALUE)
        webSetting.pluginState = WebSettings.PluginState.ON_DEMAND
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH)
        webSetting.cacheMode = WebSettings.LOAD_NO_CACHE
        webSetting.userAgentString = webSetting.userAgentString + "kitx_webview"//UA

        settingsExtension?.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY)//extension
    }


    override fun destroy() {
        // 清理缓存
        loadUrl("about:blank")
        loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
        clearHistory()
        super.destroy()
    }

    override fun drawChild(canvas: Canvas, child: View, drawingTime: Long): Boolean {
        if (!BrowserKitx.debugModel) {
            return super.drawChild(canvas, child, drawingTime)
        }
        val ret = super.drawChild(canvas, child, drawingTime)
        canvas.save()
        val paint = Paint()
        paint.color = 0x7fff0000
        paint.textSize = 24f
        paint.isAntiAlias = true
        if (x5WebViewExtension != null) {
            canvas.drawText(
                this.context.packageName + "-pid:"
                        + android.os.Process.myPid(), 10f, 50f, paint
            )
            canvas.drawText(
                "X5  Core:" + QbSdk.getTbsVersion(this.context), 10f,
                100f, paint
            )
        } else {
            canvas.drawText(
                (this.context.packageName + "-pid:"
                        + android.os.Process.myPid()), 10f, 50f, paint
            )
            canvas.drawText("Sys Core", 10f, 100f, paint)
        }
        canvas.drawText(Build.MANUFACTURER, 10f, 150f, paint)
        canvas.drawText(Build.MODEL, 10f, 200f, paint)
        canvas.restore()
        return ret
    }
}