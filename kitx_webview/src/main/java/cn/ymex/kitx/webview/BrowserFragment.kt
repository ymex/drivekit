package cn.ymex.kitx.webview

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * Created by ymex on 2019/6/10.
 * About:通用浏览器页面
 */


class BrowserFragment : Fragment() {


    var browserInitCallBack: BrowserInitCallBack? = null
    private lateinit var webViewX5: WebViewX5

    companion object {
        val LOAD_DATA = "key_load_data"//加载的数据或url
        val PROGRESS_BAR = "key_progress_bar"//是否显示加载进度 默认true

        fun instance(): BrowserFragment {
            return BrowserFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        permissionRequest.permissions(
//            0x11,
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_PHONE_STATE
//        )
        return inflater.inflate(R.layout.fragment_browser, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webViewX5 = view.findViewById(R.id.wvWeb)
        val progressBar = view.findViewById<SimpleProgressBar>(R.id.pbBar)
        browserInitCallBack?.run {
            this.browserInitFinished(getWebView())
        }

        loadData(progressBar)
    }

    private fun loadData(progressBar: ProgressCallBack) {

        arguments?.run {
            if (this.getBoolean(PROGRESS_BAR, true)) {
                getWebView().progressCallBack = progressBar
            }
            this.getString(LOAD_DATA)?.run {
                when {
                    this.startsWith("http://") or this.startsWith("https://") -> {
                        getWebView().loadUrl(this)
                    }
                    this.startsWith("file://") -> {
                        getWebView().loadUrl(this)
                    }
                    else -> {
                        getWebView().loadDataWithBaseURL(null, this, "text/html", "utf-8", null)
                    }
                }

            }
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BrowserInitCallBack) {
            browserInitCallBack = context
        }
    }

    private fun getWebView(): WebViewX5 {
        return webViewX5
    }

    override fun onDestroyView() {
        super.onDestroyView()
        getWebView().destroy()
    }


}