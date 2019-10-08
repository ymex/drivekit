package cn.ymex.drivekit.main.provider

import android.app.Application
import android.content.Context
import cn.ymex.drivekit.common.life.AppLifeCycle
import cn.ymex.drivekit.common.provider.AppLifeCycleProvider
import cn.ymex.drivekit.main.MainAppLifeCycle
import com.alibaba.android.arouter.facade.annotation.Route

@Route(path = "/provider/life/main", name = "module_main")
class ImpAppLifeCycleService : AppLifeCycleProvider {
    override fun getLifeCycle(application: Application): AppLifeCycle {
        return MainAppLifeCycle(application)
    }

    override fun init(context: Context) {

    }
}