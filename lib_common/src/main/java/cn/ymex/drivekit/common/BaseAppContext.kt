package cn.ymex.drivekit.common

import cn.ymex.kitx.kits.ApplicationContext
import com.alibaba.android.arouter.launcher.ARouter

/**
 * 基础application
 */
open class BaseAppContext : ApplicationContext() {
    override fun onCreate() {
        super.onCreate()
        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        // openLog ： 打印日志 。openDebug：开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        ARouter.openLog()
        ARouter.openDebug()
        ARouter.init(this)
    }
}