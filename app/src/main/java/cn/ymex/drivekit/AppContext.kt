package cn.ymex.drivekit

import android.app.Application
import cn.ymex.drivekit.common.kits.aRouter
import cn.ymex.drivekit.common.life.AppLifeCycleManager
import cn.ymex.drivekit.common.provider.AppLifeCycleProvider
import cn.ymex.kitx.kits.ApplicationContext
import com.alibaba.android.arouter.launcher.ARouter

/**
 * Created by ymex on 2019/9/20.
 * About:
 */
class AppContext : ApplicationContext() {
    override fun onCreate() {
        super.onCreate()
        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        // openLog ： 打印日志 。openDebug：开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        ARouter.openLog()
        ARouter.openDebug()
        ARouter.init(this)


        //注册module main 模块
        val mainLifeCycle = aRouter("/provider/life/main").navigation()
        if (mainLifeCycle != null) {
            AppLifeCycleManager.register((mainLifeCycle as AppLifeCycleProvider).getLifeCycle(this))
        }
        AppLifeCycleManager.execute()
    }
}