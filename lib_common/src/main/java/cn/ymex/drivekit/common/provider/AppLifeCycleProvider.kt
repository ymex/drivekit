package cn.ymex.drivekit.common.provider

import android.app.Application
import cn.ymex.drivekit.common.life.AppLifeCycle
import com.alibaba.android.arouter.facade.template.IProvider

/**
 * 模块application 暴露服务接口
 */
interface AppLifeCycleProvider : IProvider {
    fun getLifeCycle(application: Application): AppLifeCycle
}