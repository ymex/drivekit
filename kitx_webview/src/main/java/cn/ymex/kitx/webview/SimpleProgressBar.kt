package cn.ymex.kitx.webview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar

/**
 * About: 进度条
 */
class SimpleProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ProgressBar(context, attrs, defStyleAttr), ProgressCallBack {
    override fun showView() {
        visibility = View.VISIBLE
    }

    override fun hideView() {
        visibility = View.GONE
    }

    override fun setProgress(progress: Int) {
        super.setProgress(progress)
    }
}