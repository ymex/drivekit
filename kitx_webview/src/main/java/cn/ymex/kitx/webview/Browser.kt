package cn.ymex.kitx.webview

import android.content.Context
import android.util.Log
import com.tencent.smtt.sdk.QbSdk

object BrowserKitx {
    var debugModel = false
    fun initEnv(context: Context, debug: Boolean) {
        debugModel = debug
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        //x5内核初始化接口
        QbSdk.initX5Environment(context, object : QbSdk.PreInitCallback {

            override fun onViewInitFinished(arg0: Boolean) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("--x5", " onViewInitFinished is $arg0")
            }

            override fun onCoreInitFinished() {
                Log.d("--x5", " onCoreInitFinished")
            }
        })

    }
}