package cn.ymex.drivekit

import android.content.Context
import cn.ymex.drivekit.common.BaseAppContext
import cn.ymex.drivekit.common.kits.aRouter
import cn.ymex.drivekit.common.app.ModelAppContextManager
import cn.ymex.drivekit.common.provider.AppContextProvider

/**
 * Created by ymex on 2019/9/20.
 * About:
 */
class AppContext : BaseAppContext() {
    override fun onCreate() {
        super.onCreate()

        //注册module main 模块
        val mainAppContext = aRouter("/provider/life/main").navigation()
        if (mainAppContext != null) {
            ModelAppContextManager.register((mainAppContext as AppContextProvider).getModelAppContext(this))
        }

        ModelAppContextManager.execute()
    }


    override fun onAppEnterBackground(context: Context?) {
        super.onAppEnterBackground(context)
        println("--------------------app:onAppEnterBackground")

    }

    override fun onAppEnterForeground(context: Context?) {
        super.onAppEnterForeground(context)
        println("--------------------app:onAppEnterForeground")
    }
}