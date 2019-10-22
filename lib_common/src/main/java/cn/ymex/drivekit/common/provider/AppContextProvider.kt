package cn.ymex.drivekit.common.provider

import android.app.Application
import cn.ymex.drivekit.common.app.IModelAppContext
import com.alibaba.android.arouter.facade.template.IProvider

/**
 * 模块application 暴露服务接口
 */
interface AppContextProvider : IProvider {
    fun getModelAppContext(application: Application): IModelAppContext
}