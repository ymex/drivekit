package cn.ymex.kitx.kits

import android.app.Application
import android.content.Context

/**
 * 扩展Application  增加对app 进入前后台的监听
 */
open class ApplicationContext : Application(), ApplicationState.Callback {


    override fun onCreate() {
        super.onCreate()
        ActivityStack.instance().registerActivityLifecycleCallbacks(this)
        ApplicationState.instance(this, this).registApplicationState()
        Kits.create(this)
    }

    override fun onAppEnterForeground(context: Context?) {

    }

    override fun onAppEnterBackground(context: Context?) {

    }
}
