package cn.ymex.drivekit.main

import android.app.Application
import cn.ymex.drivekit.common.BaseAppContext
import cn.ymex.drivekit.common.app.IModelAppContext

/**
 * Created by ymex on 2019/9/20.
 * About:
 */
class AppContext : BaseAppContext() {
    override fun onCreate() {
        super.onCreate()
        MainModelAppContext(this).onCreate()
    }
}

class MainModelAppContext(val application: Application) : IModelAppContext(application) {

    override fun onCreate() {
        println("---------create MainAppLifeCycle")

    }

    override fun onTerminate() {
        println("--------- MainAppLifeCycle onTerminate")
    }
}