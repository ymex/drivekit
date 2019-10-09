package cn.ymex.kitx.kits

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner

/**
 * 扩展Application  增加对app 进入前后台的监听
 */
open class ApplicationContext : Application() {
    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            fun onStart() {
                onAppEnterForeground()
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun onResume() {
                onAppEnterBackground()
            }
        })
        Kits.create(this)
    }

    /**
     * app enter Foreground
     */
    protected fun onAppEnterForeground() {

    }

    /**
     * app enter background
     */
    protected fun onAppEnterBackground() {

    }


}
