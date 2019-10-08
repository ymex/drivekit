package cn.ymex.kitx.webview


//progressbar view
interface ProgressCallBack {
    fun showView()
    fun hideView()
    fun setProgress(progress:Int)
}

//browser fragment init callback
interface BrowserInitCallBack{
    fun browserInitFinished(webView: WebViewX5)
}