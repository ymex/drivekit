package cn.ymex.drivekit.main

import android.app.Application
import cn.ymex.drivekit.common.life.AppLifeCycle
import com.alibaba.android.arouter.launcher.ARouter

/**
 * Created by ymex on 2019/9/20.
 * About:
 */
class AppContext : Application() {
    override fun onCreate() {
        super.onCreate()
        MainAppLifeCycle(this).onCreate()
    }
}

class MainAppLifeCycle(val application: Application) : AppLifeCycle(application) {

    override fun onCreate() {
        println("---------create MainAppLifeCycle")
        ARouter.openLog()
        ARouter.openDebug()
        ARouter.init(application)

    }

    override fun onTerminate() {
        println("--------- MainAppLifeCycle onTerminate")
    }
}